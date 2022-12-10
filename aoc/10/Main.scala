
package com.tmutas.aoc2022.day09

import scala.io.Source._
import scala.collection.mutable
import scala.math


@main def Run(args: String*) =
    val lines : Iterator[String] = 
        if args.nonEmpty then fromFile(args(0)).getLines() else Array[String]().iterator
    

    val signals = mutable.ArrayBuffer(1)
    for 
        line <- lines 
    do 
        signals += signals.last
        if 
            line.length() > 4
        then
            signals += (signals.last + line.split(" ")(1).toInt)

    println(signals)

    val score_cycles = for i <- 0 to 5 yield 20 + i*40

    val scores = (for i <- score_cycles yield signals(i - 1)*i)

    println(s"Part 1: ${scores.sum}")

    // Part 2

    val output_lines = for i <- 0 until 6 yield StringBuilder()

    for 
        i <- 0 until 6
        j <- 0 until 40
    do
        val curpos = signals(j+i*40)
        val positions = Set(curpos - 1, curpos, curpos + 1)
        output_lines(i) += (if positions.contains(j) then '#' else '.')
    
    for line <- output_lines do println(line)