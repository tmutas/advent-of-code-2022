import scala.io.Source._

def getMarker(input : String, uniques : Int) : Int =
    var score = -1
    for 
        i <- 0 to input.length() - uniques
        if input.substring(i, i + uniques).distinct.length() == uniques
        if score < 0
    do
        score = i + uniques
    return score

@main def Run(args: String*) =
    val lines = fromFile(args(0)).getLines()

    val input = lines.next()

    val score = getMarker(input, 4)
    val score2 = getMarker(input, 14)

    println(s"Part 1: $score")
    println(s"Part 2: $score2")


        
        