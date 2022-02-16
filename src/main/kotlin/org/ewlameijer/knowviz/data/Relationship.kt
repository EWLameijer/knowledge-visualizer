package org.ewlameijer.knowviz.data

enum class RelationshipArity { OneToOne, OneToMany }
enum class RelationshipType(val text: String, val arity: RelationshipArity) {
    IsA(
        "is a(n)",
        RelationshipArity.OneToOne
    ),
    CanHave(
        "can have",
        RelationshipArity.OneToMany
    )
}

sealed class Relationship(val origin: Concept, val relationshipType: RelationshipType) {
    companion object {
        fun ofWithIsValid(source: Concept?, s: String?, destinations: Set<Concept>): Boolean =
            if (source == null) false
            else {
                val selectedRelationship = RelationshipType.values().firstOrNull { it.text == s }
                when (selectedRelationship?.arity) {
                    null -> false
                    RelationshipArity.OneToOne -> destinations.size == 1
                    RelationshipArity.OneToMany -> destinations.isNotEmpty()
                }
            }

        fun of(source: Concept?, s: String?, destinations: Set<Concept>): Relationship {
            require(ofWithIsValid(source, s, destinations))
            val relationshipType = RelationshipType.values().first{ it.text == s }
            return if (relationshipType.arity == RelationshipArity.OneToOne) OneToOneRelationship(
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

class OneToOneRelationship(origin: Concept, relationshipType: RelationshipType, val destination: Concept) :
    Relationship(origin, relationshipType) {
    override fun toString() = "$origin $relationshipType $destination"
}

class OneToManyRelationship(origin: Concept, relationshipType: RelationshipType, val destinations: Set<Concept>) :
    Relationship(origin, relationshipType) {
    override fun toString() = destinations.joinToString("\n") { "$origin $relationshipType $it" }
}