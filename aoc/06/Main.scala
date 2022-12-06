import scala.io.Source._

@main def Run(args: String*) =
    val lines = fromFile(args(0)).getLines()
    
    var score = -1
    var score2 = -1

    val input = lines.next()
    for 
        i <- 0 to input.length() - 4
        if input.substring(i, i + 4).distinct.length() == 4 
        if score < 0
    do
        score = i + 4

    for 
        i <- 0 to input.length() - 14
        if input.substring(i, i + 14).distinct.length() == 14
        if score2 < 0
    do
        score2 = i + 14

    println(s"Part 1: $score")
    println(s"Part 2: $score2")


        
        