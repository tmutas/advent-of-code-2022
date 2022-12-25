import scala.io.Source._
import scala.collection.mutable
import scala.math

def parseSingleLine(line : String) : (String, Int, Vector[String]) = 
    val lineregex  = """Valve (\w*) has flow rate=(\d*); tunnels? leads? to valves? (.*)""".r
    line match 
        case lineregex(node, flow, edges) => return (node, flow.toInt, edges.split(", ").toVector)
        case _ => return ("", 0, Vector(""))

def parseLines(lines : Iterator[String]) : (Map[String, Int], Map[String, Vector[String]]) = 
    val nodes_raw = lines.map(parseSingleLine).toList

    val nodes = (for (node, flow, _) <- nodes_raw yield node -> flow).toMap

    val edges = (for (node, _, edges_list) <- nodes_raw yield node -> edges_list).toMap

    return (nodes, edges)

@main def Run(args: String*) =
    val inputlines : Iterator[String] = 
        if args.nonEmpty then fromFile(args(0)).getLines() else Array[String]("bla").iterator
    
    val (nodes, edges) = parseLines(inputlines)


