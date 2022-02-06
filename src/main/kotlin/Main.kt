import org.ewlameijer.knowviz.data.CanHave
import org.ewlameijer.knowviz.data.Concept

fun main() {
    println("Knowledge Visualizer")
    val node = Concept("class (concept)")
    println(node)
    val fieldNode = Concept("field")
    val relationship = CanHave(node, mutableSetOf(fieldNode))
    println(relationship)
}