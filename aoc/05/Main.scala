import scala.io.Source._

import scala.collection.mutable
import scala.collection.mutable.ArrayDeque

import scala.util.matching.Regex

def parseStacks(lines : Iterator[String], numberOfStacks : Int) : IndexedSeq[ArrayDeque[String]] =
    var stacks = for i <- 0 to numberOfStacks yield new ArrayDeque[String]
    for
        line <- lines
        if line.contains("[")
    do
        for 
            (stack, pos) <- (0 to numberOfStacks).map(j => (j, 4*j + 1))
            if line.substring(pos, pos + 1) != " "
        do
            stacks(stack).append(line.substring(pos, pos + 1))
    stacks

def performMove(fromStack : ArrayDeque[String], toStack : ArrayDeque[String], num : Int) =
    for 
        _ <- 1 to num 
    do 
        toStack.prepend(fromStack.removeHead())

@main def Run(args: String*) =
    val (lines1, lines2) = fromFile(args(0)).getLines().duplicate

    var stacks = parseStacks(lines1, args(1).toInt - 1)
    
    val move_regex : Regex = """move (\d*) from (\d*) to (\d*)""".r 

    for 
        line <- lines2
        if line.startsWith("move")
    do
        line match
            case move_regex(num, from, to) => performMove(stacks(from.toInt - 1), stacks(to.toInt - 1), num.toInt)
            case _ => println("Faulty line")

    for stack <- stacks do print(stack.head)
    println()
