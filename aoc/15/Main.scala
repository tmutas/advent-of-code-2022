
import scala.io.Source._
import scala.collection.mutable
import scala.math

import breeze.linalg._
import breeze.numerics._
import breeze.plot._

def parseSingleLine(line : String) : DenseMatrix[Int] = 
    val lineregex  = """Sensor at x=([-\d]*), y=([-\d]*): closest beacon is at x=([-\d]*), y=([-\d]*)""".r
    line match 
        case lineregex(xs, ys, xb, yb) => return DenseMatrix((xs.toInt, ys.toInt), (xb.toInt, yb.toInt))
        case _ => return DenseMatrix((0, 0), (0, 0))

def parseLines(lines : Iterator[String]) : List[DenseMatrix[Int]] = 
    lines.map(parseSingleLine).toList

def coverSignalArea(coveredGrid : DenseVector[Double], signalGrid : DenseVector[Double], pos : DenseMatrix[Int], offx : Int, offy : Int, observedRow : Int) =  
    val signalPosition = pos(0, ::)
    val beaconPosition = pos(1, ::)

    val diff = signalPosition - beaconPosition
    val dist = sum(abs(diff))

    val sx = signalPosition(0) - offx
    val sy = signalPosition(1) - offy

    val bx = beaconPosition(0) - offx
    val by = beaconPosition(1) - offy

    //signalGrid(sx, sy) = 0.5
    if observedRow == by then signalGrid(bx) = 1.0

    for 
        i <- 0 to dist 
    do 
        val xLower = math.max(sx - i, 0)
        val xUpper = math.min(sx + i, coveredGrid.length - 1)
        val yFrom = sy - dist + i
        val yTo = sy + dist - i
        
        if (observedRow <= yTo) && (observedRow >= yFrom)
        then
            coveredGrid(xLower) = 1.0
            coveredGrid(xUpper) = 1.0

def isCovered(x : Int, y : Int, allpos : List[DenseMatrix[Int]] ) : Boolean =
    val position = DenseVector[Int](x, y).t

    var i = 0
    var covered = false
    while (i < allpos.length && !covered)
    do
        val signalPosition = allpos(i)(0, ::)
        val beaconPosition = allpos(i)(1, ::)
        val signalDiff = signalPosition - beaconPosition
        val posDiff = signalPosition - position

        val dist = sum(abs(signalDiff))
        val pointDist = sum(abs(posDiff))
        if pointDist <= dist then covered = true
        i += 1

    return covered

def findEdgePoints(allpos : List[DenseMatrix[Int]], maxIndex : Int) : mutable.ArrayBuffer[(Int, Int)] =
    var i = 0
    var points = mutable.ArrayBuffer[(Int, Int)]((0,0))
    for m <- allpos
    do
        val signalPosition = m(0, ::)
        val beaconPosition = m(1, ::)
        println(s"Add Points for Signal at $signalPosition")

        val signalDiff = signalPosition - beaconPosition
        val dist = sum(abs(signalDiff))
        
        val sx = signalPosition(0)
        val sy = signalPosition(1)
        for 
            i <- 1 to (dist - 1) 
        do 
            val xLower = math.max(sx - i, 1)
            val xUpper = math.min(sx + i, maxIndex - 1)
            val yFrom = math.max(sy - dist + i, 1)
            val yTo = math.min(sy + dist - i, maxIndex - 1)
            points += ((xLower - 1, yFrom))
            points += ((xLower - 1, yTo))
            points += ((xUpper + 1, yFrom))
            points += ((xUpper + 1, yTo))

        for 
            i <- Vector(0, dist) 
        do 
            val xLower = math.max(sx - i, 1)
            val xUpper = math.min(sx + i, maxIndex - 1)
            val yFrom = math.max(sy - dist + i, 1)
            val yTo = math.min(sy + dist - i, maxIndex - 1)
            points += ((xLower - 1, yFrom))
            points += ((xLower, yFrom - 1))
            points += ((xLower, yFrom + 1))
            points += ((xLower - 1, yTo))
            points += ((xLower, yTo + 1))
            points += ((xLower, yTo - 1))
            points += ((xUpper + 1, yFrom))
            points += ((xUpper, yFrom - 1))
            points += ((xUpper, yFrom + 1))
            points += ((xUpper + 1, yTo))
            points += ((xUpper, yTo + 1))
            points += ((xUpper, yTo - 1))

    return points


@main def Run(args: String*) =
    val inputlines : Iterator[String] = 
        if args.nonEmpty then fromFile(args(0)).getLines() else Array[String]("bla", "bla2").iterator

    val positions = parseLines(inputlines)
    
    // Get the grid size and offset 
    val xmax_raw = positions.map(m => max(m(::, 0))).max
    val xmin_raw = positions.map(m => min(m(::, 0))).min
    val ymax = positions.map(m => max(m(::, 1))).max
    val ymin = positions.map(m => min(m(::, 1))).min

    val xmax = math.max(xmax_raw, ymax) + 1000000
    val xmin = math.min(xmin_raw, ymin) - 1000000

    println(s"Creating matrices ($xmin - $xmax, $ymin - $ymax")
    var coveredGrid = DenseVector.zeros[Double](xmax - xmin + 1)
    var signalGrid = coveredGrid.copy
    println("Processing signal positions")
    positions.foreach(m => coverSignalArea(coveredGrid, signalGrid, m, xmin, ymin, args(1).toInt - ymin))

    
    val sumCovered = sum(coveredGrid)
    val sumSignals = sum(signalGrid)
    val score = sumCovered - sumSignals

    println(s"Part 1: ${score} from $sumCovered covered spots and $sumSignals signals")

    // Part 2
    
    val part2max = args(2).toInt
    val edgePoints = findEdgePoints(positions, part2max)
    
    println(s"Search ${edgePoints.size}")
    def findBeacon() = 
        var count = 0
        var xFinal = -1
        var yFinal = -1
        for 
            search <- edgePoints
            if xFinal < 0
        do 
            if count % 100000 == 0 then println(s"$count / ${edgePoints.size} (${count.toDouble / edgePoints.size * 100}%)")
            val (i, j) = search
            if !isCovered(i, j, positions) 
            then
                xFinal = i
                yFinal = j
                println(s"Part 2: At ($xFinal, $yFinal) with score ${xFinal.toLong*4000000 + yFinal}")
            count += 1

    findBeacon()
    /*
    val f1 = Figure()
    f1.subplot(0) += image(coveredGrid(::, coveredGrid.cols - 1 to 0 by -1).t)
    f1.saveas("coveredarea.png")

    signalGrid = signalGrid + coveredGrid*0.2
    val f2 = Figure()
    f2.subplot(0) += image(signalGrid(::, signalGrid.cols - 1 to 0 by -1).t)
    f2.saveas("signalpositions.png")
    */
