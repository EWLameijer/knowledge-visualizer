package org.ewlameijer.knowviz.data

sealed class Relationship(val origin: Concept)

abstract class OneToManyRelationship(origin: Concept, val destinations: MutableSet<Concept>) : Relationship(origin) {
    abstract val relationshipType: String
    override fun toString() = destinations.joinToString("\n") { "$origin $relationshipType $it" }

}

class CanHave(origin: Concept, destinations: MutableSet<Concept>) : OneToManyRelationship(origin, destinations) {
    override val relationshipType = "can have"
}