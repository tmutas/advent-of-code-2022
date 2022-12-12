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
    
    // Stores the shortest distance to the point
    // Set to value longer than any distance
    val defaultDist = xm*ym
    val distMap = mutable.ArrayBuffer.fill(xm, ym)(defaultDist)

    val ex = tempheightMap.indexWhere(_.contains(endVal))
    val ey = tempheightMap(ex).indexWhere(_ == endVal)

    val sx = tempheightMap.indexWhere(_.contains(startVal))
    val sy = tempheightMap(sx).indexWhere(_ == startVal)

    // Update start point with highest height and zero distance
    val temp2heightMap = tempheightMap.updated(sx, tempheightMap(sx).updated(sy, Char.char2int('z')))
    val heightMap = temp2heightMap.updated(ex, temp2heightMap(ex).updated(ey, Char.char2int('z')))

    distMap(sx)(sy) = 0

    println(s"($sx, $sy) ($ex, $ey)")

    def explore(i : Int, j : Int, preDist : Int) : Int = 
        val curDist = preDist + 1
        val maxClimbHeight = heightMap(i)(j) + 1
        if 
            !(i == ex && j == ey) 
        then
            for 
                (in, jn) <- Vector((i-1, j), (i+1, j), (i, j-1), (i, j+1))
                if in >= 0 && in < xm && jn >= 0 && jn < ym
                if distMap(in)(jn) > curDist
                if distMap(in)(jn) == defaultDist
                if heightMap(in)(jn) <= maxClimbHeight
            do
                distMap(in)(jn) = curDist
                explore(in, jn, curDist)
            return 0
        else
            return 0

    val res = explore(sx, sy, 0)

    heightMap.foreach(println)
    distMap.foreach(println)

    println(s"Part 1: ${distMap(ex)(ey)} at ($ex, $ey)")