package com.tmutas.aoc2022.day11

import scala.io.Source._
import scala.collection.mutable
import scala.math

class Monkey(
    val items : mutable.ArrayDeque[Int],
    val opType : String,
    val opValue : Int,
    val testDivisibleBy : Int,
    val monkeyIndexIfTrue : Int,
    val monkeyIndexifFalse : Int
):

    def doTurn() : IndexedSeq[(Int, Int)] = items.removeAll().map(inspectItem).toIndexedSeq

    def inspectItem(i : Int) : (Int, Int) =
        var value = performOperation(i)
        value /= 3
        val toMonkey = if value % testDivisibleBy == 0 then monkeyIndexIfTrue else monkeyIndexifFalse
        return (toMonkey, value)

    def performOperation(i : Int) = opType match
            case "*" => i * opValue
            case "+" => i + opValue
            case "**" => i * i

    override def toString() = s"""Items: $items, performs ($opType, $opValue)"""
        + s""" divisible by $testDivisibleBy, to $monkeyIndexIfTrue or $monkeyIndexifFalse"""
        
class Monkeys(val monkeys : IndexedSeq[Monkey]):
    def doRound() = monkeys.foreach(doMonkeyTurn)

    def doMonkeyTurn(mon : Monkey) = 
        val throws = mon.doTurn()
        for t <- throws do t match
            case (toMonkey, value) => monkeys(toMonkey).items.append(value)
        

def parseMonkey(it : Seq[String]) : Monkey = 
    val itemsPattern = """  Starting items: (.*)""".r
    val opPattern = """  Operation: new = old (.) (\d*)""".r
    val opExpPattern = """  Operation: new = old (.) old""".r
    val divByPattern = """  Test: divisible by (\d*)""".r
    val toMonkeyPattern = """    If (.*): throw to monkey (\d*)""".r

    var items = mutable.ArrayDeque[Int]()
    var opTuple = ("", - 1)
    var testDivisibleBy = -1
    var monkeyIndexIfFalse = -1
    var monkeyIndexIfTrue = -1

    for 
        line <- it
    do
        line match
            case itemsPattern(ar) => items ++= ar.split(", ").map(_.toInt)
            case opPattern(opType, opValue) => opTuple = (opType, opValue.toInt)
            case opExpPattern(opType) => opTuple = ("**", -1)
            case divByPattern(div) => testDivisibleBy = div.toInt
            case toMonkeyPattern(boolValue, index) => 
                if 
                   boolValue.toBoolean 
                then 
                    monkeyIndexIfTrue = index.toInt 
                else 
                    monkeyIndexIfFalse = index.toInt
            case _ => ""

    return Monkey(items, opTuple(0), opTuple(1), testDivisibleBy, monkeyIndexIfTrue, monkeyIndexIfFalse)


@main def Run(args: String*) =
    val lines : Iterator[String] = 
        if args.nonEmpty then fromFile(args(0)).getLines() else Array[String]().iterator
    
    val monkeys = Monkeys(lines.grouped(7).map(parseMonkey).toIndexedSeq)

    monkeys.monkeys.foreach(println)

