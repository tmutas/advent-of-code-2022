package com.tmutas.aoc2022.day09

import scala.io.Source._
import scala.collection.mutable
import scala.math

case class Point(
    var x : Int = 0,
    var y : Int = 0
):

    def moveFromInstruction(direction : String) = direction match
        case "R" => x += 1
        case "L" => x -= 1
        case "U" => y += 1
        case "D" => y -= 1

    def diff(other : Point) : Point = 
        // Pointing in the direction of other
        Point(other.x - x, other.y - y)

    def moveFromDiff(diff : Point) =
        y += diff.y.sign
        x += diff.x.sign

    def maxAbs : Int = math.max(x.abs, y.abs)

    override def toString(): String = s"($x, $y)"

class Rope(val head : Point = Point(), val tail : Point = Point()):
    val tailPositions = mutable.Set[Point]()

    def performSingleStep(direction : String) : Point = 
        head.moveFromInstruction(direction)
        val diff = tail.diff(head)
        if diff.maxAbs >= 2 then tail.moveFromDiff(diff)
        
        return tail.copy()

    def performSteps(direction : String, steps : Int) =
        // Returns tail position after every step
        for i <- 1 to steps do tailPositions += performSingleStep(direction)

@main def Run(args: String*) =
    val lines : Iterator[String] = 
        if args.nonEmpty then fromFile(args(0)).getLines() else Array[String]().iterator
    
    var score = 0
    var score2 = 0

    var rope = Rope()
    
    val instructions = for line <- lines yield line.split(" ")

    for inst <- instructions do rope.performSteps(inst(0), inst(1).toInt) 
    
    println(rope.tailPositions.size)