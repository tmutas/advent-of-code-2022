
import scala.io.Source._
import scala.collection.mutable

import breeze.linalg._
import breeze.plot._

def parseSingleLine(line : String) : Array[Vector[Int]]= 
    line.split(" -> ").map(_.split(",")).map(x => Vector[Int](x(0).toInt, x(1).toInt))

def parseLines(lines : Iterator[String]) : Iterator[Array[Vector[Int]]]= lines.map(parseSingleLine)

def applyPath(rocks : DenseMatrix[Double], fromPoint : Vector[Int], toPoint : Vector[Int]) =  
    val diff = toPoint - fromPoint
    if 
        diff(0) == 0 
    then 
        rocks(fromPoint(0), fromPoint(1) to toPoint(1) by diff(1).sign) := 1.0
    else
        rocks(fromPoint(0) to toPoint(0) by diff(0).sign, fromPoint(1)) := 1.0

def fallSand(rocks : DenseMatrix[Double], max : Int, x : Int = 500, y : Int = 0) : (Int, Int) =
    if y == max then return (x, y)
    
    if rocks(x, y + 1) == 0.0
    then 
        //rocks(x, y + 1) = 0.5
        return fallSand(rocks, max, x, y + 1)
    else if rocks(x - 1, y + 1) == 0.0
    then
        //rocks(x - 1, y + 1) = 0.5
        return fallSand(rocks, max, x - 1, y + 1)
    else if rocks(x + 1, y + 1) == 0.0
    then
        //rocks(x + 1, y + 1) = 0.5
        return fallSand(rocks, max, x + 1, y + 1)
    else
        return (x, y)

@main def Run(args: String*) =
    val inputlines : Iterator[String] = 
        if args.nonEmpty then fromFile(args(0)).getLines() else Array[String]().iterator
    
    var rocks = DenseMatrix.zeros[Double](1000,200)

    val rockPaths = parseLines(inputlines)

    var rockDepths = mutable.ArrayBuffer[Int]()

    for 
        line <- rockPaths 
    do 
        for i <- 0 until line.length - 1 
        do 
            applyPath(rocks, line(i), line(i + 1))
            rockDepths += line(i)(1)
            rockDepths += line(i + 1)(1)

    var rocks2 = rocks.copy

    // Part 1
    var end = false
    var score1 = 0
    while !end
    do
        var (x, y) = fallSand(rocks, rocks.cols - 1)
        if y == rocks.cols - 1
        then end = true 
        else 
            score1 = score1 + 1
            rocks(x, y) = 0.5
    println(s"Part 1: $score1")
    val f1 = Figure()
    f1.subplot(0) += image(rocks(400 until 600, 199 until 0 by -1).t)
    f1.saveas("rocks.png")

    // Part 2

    var score2 = 0
    while rocks2(500, 0) == 0
    do
        var (x, y) = fallSand(rocks2, rockDepths.max + 1)
        score2 = score2 + 1
        rocks2(x, y) = 0.5

    println(s"Part 2: $score2")
    val f2 = Figure()
    f2.subplot(0) += image(rocks2(400 until 600, rockDepths.max until 0 by -1).t)
    f2.saveas("rocks2.png")
