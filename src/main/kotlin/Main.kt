import org.ewlameijer.knowviz.files.FileParser

fun main() {
    println("Knowledge Visualizer")
    val kb = FileParser("knowledge.txt").parse()
    println(kb)
}



