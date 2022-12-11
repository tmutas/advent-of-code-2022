import scala.io.Source._
import scala.collection.mutable

@main def Run(args: String*) =
    val lines : Iterator[String] = fromFile(args(0)).getLines()
    
    var score = 0
    var score2 = 0

    val dirStack = mutable.ArrayBuffer[String]()
    
    val fileSizeMap = mutable.HashMap[String, Int]().withDefaultValue(0)
    val subDirMap = mutable.HashMap[String, mutable.ArrayBuffer[String]]()

    val cdRegEx = """\$ cd (.*)""".r
    val lsRegEx = """\$ ls """.r
    val dirRegEx = """dir (.*)""".r
    val fileRegEx = """(\d*) (.*)""".r

    var curDir = ""
    for 
        line <- lines
    do
        curDir = dirStack.mkString("/")
        line match
            case "$ cd .." => dirStack.trimEnd(1)
            case "$ ls" => subDirMap(curDir) = mutable.ArrayBuffer[String]()
            case cdRegEx(d) => dirStack.append(d)
            case dirRegEx(d) => subDirMap(curDir).addOne(s"$curDir/$d")
            case fileRegEx(f, _) => fileSizeMap(curDir) += f.toInt
            case _ => ""

    def calcSize(d : String) : Int = 
        return fileSizeMap(d) + subDirMap(d).map(calcSize).sum

    val dirSums = subDirMap.map((d, _) => calcSize(d))

    println(s"Part 1: ${dirSums.filter(_ <= 100000).sum}")
    
    val diff = calcSize("/") - 40000000
    println(s"Part 2: ${dirSums.filter(_ > diff).min}")