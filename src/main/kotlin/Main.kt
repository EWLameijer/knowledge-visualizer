import org.ewlameijer.knowviz.files.FileParser

fun main() {
    println("Knowledge Visualizer")
    val kb = FileParser("knowledge.txt").parse()
    println(kb)
    println("Add a relationship (originConcept, relationship, targetconcept)")
    println("Example: method [ENTER] has a(n)[ENTER] return value[ENTER]")

    val originConcept = getLine()
    val relationship = getLine()
    val targetConcept = getLine()

    kb.addRelationship(originConcept, relationship, targetConcept)
    println("New version:\n\n")
    println(kb)
}

fun getLine() : String {
    var input: String?
    do {
        input = readLine()
    } while (input.isNullOrBlank())
    return input
}



