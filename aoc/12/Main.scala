import scala.io.Source._
import scala.collection.mutable
import scala.math

@main def Run(args: String*) =
    val inputlines : Iterator[String] = 
        if args.nonEmpty then fromFile(args(0)).getLines() else Array[String]().iterator
    
    val startVal = Char.char2int('S')
    val endVal = Char.char2int('E')
    
    val tempheightMap = inputlines.toVector.map(_.toVector.map(_.intValue))
    
    val xm = tempheightMap.length
    val ym = tempheightMap(0).length

    val ex = tempheightMap.indexWhere(_.contains(endVal))
    val ey = tempheightMap(ex).indexWhere(_ == endVal)

    val sx = tempheightMap.indexWhere(_.contains(startVal))
    val sy = tempheightMap(sx).indexWhere(_ == startVal)

    // Update start point with highest height and zero distance
    val temp2heightMap = tempheightMap.updated(sx, tempheightMap(sx).updated(sy, Char.char2int('z')))
    val heightMap = temp2heightMap.updated(ex, temp2heightMap(ex).updated(ey, Char.char2int('z')))

    def findPath(sx : Int, sy : Int) :  scala.collection.mutable.ArrayBuffer[scala.collection.mutable.ArrayBuffer[Int]] =
        // Stores the shortest distance to the point
        // Set to value longer than any distance
        val defaultDist = xm*ym
        val distMap = mutable.ArrayBuffer.fill(xm, ym)(defaultDist)
        distMap(sx)(sy) = 0

        var exploreStack = mutable.Stack[(Int, Int)]((sx, sy))
            
        def explore(point : (Int, Int)) = 
            val (i, j) = point
            val curDist = distMap(i)(j) + 1
            val maxClimbHeight = heightMap(i)(j) + 1
            if 
                !(i == ex && j == ey) 
            then
                for 
                    (in, jn) <- Vector((i-1, j), (i+1, j), (i, j-1), (i, j+1))
                    if in >= 0 && in < xm && jn >= 0 && jn < ym
                    if distMap(in)(jn) > curDist
                    if heightMap(in)(jn) <= maxClimbHeight
                do
                    distMap(in)(jn) = curDist
                    exploreStack.push((in, jn))

        while 
            !exploreStack.isEmpty
        do 
            explore(exploreStack.pop())

        return distMap

    val part1distMap = findPath(sx, sy)
    println(s"Part 1: From ($sx, $sy) distance ${part1distMap(ex)(ey)} at ($ex, $ey)")
    println("=======================")
    // ==================
    // PART 2

    val alldists = 
    for 
        i <- 0 until xm
        j <- 0 until ym
        if heightMap(i)(j) == Char.char2int('a')
    yield
        findPath(i, j)(ex)(ey)
    
    println(s"Part 2: All Distances from a: ${alldists} at ($ex, $ey)")
    println(s"Part 2: Minimal distance is ${alldists.min}")

