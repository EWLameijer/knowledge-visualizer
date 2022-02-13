package org.ewlameijer.knowviz.data

enum class RelationshipArity { OneToOne, OneToMany }
enum class RelationshipType { IsA, CanHave }
data class RelationshipData(val arity: RelationshipArity, val type: RelationshipType)

val relationships = mapOf(
    "is a(n)" to RelationshipData(RelationshipArity.OneToOne, RelationshipType.IsA),
    "can have" to RelationshipData(RelationshipArity.OneToMany, RelationshipType.CanHave)
)

sealed class Relationship(val origin: Concept, val relationshipType: RelationshipType) {
    companion object {
        fun ofWithIsValid(source: Concept?, s: String?, destinations: Set<Concept>): Boolean =
            if (source == null) false else
                when (relationships[s]?.arity) {
                    null -> false
                    RelationshipArity.OneToOne -> destinations.size == 1
                    RelationshipArity.OneToMany -> destinations.isNotEmpty()
                }

        fun of(source: Concept?, s: String?, destinations: Set<Concept>): Relationship {
            require(ofWithIsValid(source, s, destinations))
            val (relationshipArity, relationshipType) = relationships[s]!!
            return if (relationshipArity == RelationshipArity.OneToOne) OneToOneRelationship(
                source!!,
                relationshipType,
                destinations.toList().first()
            ) else OneToManyRelationship(
                source!!,
                relationshipType,
                destinations.toSet() // make defensive copy against clearing original set
            )
        }
    }
}

class OneToOneRelationship(origin: Concept, relationshipType: RelationshipType, private val destination: Concept) :
    Relationship(origin, relationshipType) {
    override fun toString() = "$origin $relationshipType $destination"
}

class OneToManyRelationship(origin: Concept, relationshipType: RelationshipType, private val destinations: Set<Concept>) :
    Relationship(origin, relationshipType) {
    override fun toString() = destinations.joinToString("\n") { "$origin $relationshipType $it" }
}