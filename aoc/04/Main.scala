import scala.io.Source._

@main def Run(args: String*) =
    val lines : Iterator[String] = fromFile(args(0)).getLines()
    
    var score = 0
    var score2 = 0
    for 
        line <- lines
    do
        val ranges = for v <- line.split(",") yield v.split("-").map(x => x.toInt)
        val diffs = for v <- ranges yield v(1) - v(0)
        // Part 1
        if 
            diffs(0) >= diffs(1)
        then
            if ranges(0)(0) <= ranges(1)(0) && ranges(0)(1) >= ranges(1)(1) then score += 1
        else 
            if ranges(0)(0) >= ranges(1)(0) && ranges(0)(1) <= ranges(1)(1) then score += 1
        // Part 2
        if 
            (ranges(0)(0) <= ranges(1)(0) && ranges(0)(1) >= ranges(1)(0)) ||
            (ranges(1)(0) <= ranges(0)(0) && ranges(1)(1) >= ranges(0)(0))
        then score2 += 1

    println(s"Part 1: $score")
    println(s"Part 2: $score2")


        
        