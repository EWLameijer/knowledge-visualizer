import org.ewlameijer.knowviz.files.FileParser
import org.ewlameijer.knowviz.gui.MainWindow
import java.io.File

const val fileName = "knowledge.txt"

fun main() {
    println("Knowledge Visualizer")
    val kb = FileParser(fileName).parse()
    println(kb)
    MainWindow(kb)
    println("Add a relationship (originConcept, relationship, targetconcept)")
    println("Example: method [ENTER] has a(n)[ENTER] return value[ENTER]")

    val originConcept = getLine()
    val relationship = getLine()
    val targetConcept = getLine()

    kb.addRelationship(originConcept, relationship, targetConcept)
    println("New version:\n\n")
    println(kb)
    File(fileName).writeText(kb.toString())
}

fun getLine() : String {
    var input: String?
    do {
        input = readLine()
    } while (input.isNullOrBlank())
    return input
}



