package com.tmutas.aoc2022.day11

import scala.io.Source._
import scala.collection.mutable
import scala.math

class Monkey(
    val items : mutable.ArrayDeque[Long],
    val opType : String,
    val opValue : Long,
    val testDivisibleBy : Long,
    val monkeyIndexIfTrue : Int,
    val monkeyIndexifFalse : Int,
    val divideLevel : Boolean = true,
):

    var itemsInspected : Long = 0

    def doTurn() : IndexedSeq[(Int, Long)] = items.removeAll().map(inspectItem).toIndexedSeq

    def inspectItem(i : Long) : (Int, Long) =
        itemsInspected += 1
        var value = performOperation(i)
        if divideLevel then value /= 3
        val toMonkey = if value % testDivisibleBy == 0 then monkeyIndexIfTrue else monkeyIndexifFalse
        return (toMonkey, value)

    def performOperation(i : Long) = opType match
            case "*" => i * opValue
            case "+" => i + opValue
            case "**" => i * i

    override def toString() = s"""Items: $items, done $itemsInspected inspections, performs ($opType, $opValue)"""
        + s""" divisible by $testDivisibleBy, to $monkeyIndexIfTrue or $monkeyIndexifFalse"""
        
class Monkeys(val monkeys : IndexedSeq[Monkey]):
    val gcd = monkeys.map(_.testDivisibleBy).toSet.product

    def doRound() = monkeys.foreach(doMonkeyTurn)

    def doMonkeyTurn(mon : Monkey) = 
        val throws = mon.doTurn()
        for t <- throws do t match
            case (toMonkey, value) => monkeys(toMonkey).items += value % gcd


def parseMonkey(it : Seq[String], divideLevel : Boolean = true) : Monkey = 
    val itemsPattern = """  Starting items: (.*)""".r
    val opPattern = """  Operation: new = old (.) (\d*)""".r
    val opExpPattern = """  Operation: new = old (.) old""".r
    val divByPattern = """  Test: divisible by (\d*)""".r
    val toMonkeyPattern = """    If (.*): throw to monkey (\d*)""".r

    var items = mutable.ArrayDeque[Long]()
    var opTuple = ("", (-1).toLong)
    var testDivisibleBy : Long= -1
    var monkeyIndexIfFalse = -1
    var monkeyIndexIfTrue = -1

    for 
        line <- it
    do
        line match
            case itemsPattern(ar) => items ++= ar.split(", ").map(_.toLong)
            case opPattern(opType, opValue) => opTuple = (opType, opValue.toLong)
            case opExpPattern(opType) => opTuple = ("**", -1)
            case divByPattern(div) => testDivisibleBy = div.toLong
            case toMonkeyPattern(boolValue, index) => 
                if 
                   boolValue.toBoolean 
                then 
                    monkeyIndexIfTrue = index.toInt 
                else 
                    monkeyIndexIfFalse = index.toInt
            case _ => ""

    return Monkey(items, opTuple(0), opTuple(1), testDivisibleBy, monkeyIndexIfTrue, monkeyIndexIfFalse, divideLevel)


@main def Run(args: String*) =
    val inputlines : Iterator[String] = 
        if args.nonEmpty then fromFile(args(0)).getLines() else Array[String]().iterator
    
    val (lines1, lines2) = inputlines.duplicate

    // Part 1
    val monkeys = Monkeys(lines1.grouped(7).map(parseMonkey(_, true)).toIndexedSeq)

    println("Initial State:")
    monkeys.monkeys.foreach(println)

    for i <- 1 to 20 do monkeys.doRound()

    println("End state:")
    monkeys.monkeys.foreach(println)

    val top = monkeys.monkeys.map(_.itemsInspected).sorted.takeRight(2)
    
    println(s"PART 1 SCORE: ${top} with product ${top.product}")

    // Part 2
    val monkeys2 = Monkeys(lines2.grouped(7).map(parseMonkey(_, false)).toIndexedSeq)
    println()
    println("=========================")
    println("PART 2")
    println("Initial State:")
    monkeys2.monkeys.foreach(println)

    for i <- 1 to 10000 do monkeys2.doRound()

    println("End state:")
    monkeys2.monkeys.foreach(println)

    val top2 = monkeys2.monkeys.map(_.itemsInspected).sorted.takeRight(2)
    
    println(s"PART 2 SCORE: ${top2} with product ${top2.map(_.toLong).product}")
