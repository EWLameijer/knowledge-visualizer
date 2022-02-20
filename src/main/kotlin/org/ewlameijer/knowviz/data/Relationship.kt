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
    ),
    HasA(
        "has a(n)",
        RelationshipArity.OneToMany
    )
}

sealed class Relationship(val origin: Concept, val relationshipType: RelationshipType) {
    companion object {
        fun fromString(s: String?) = RelationshipType.values().firstOrNull { it.text == s }

        fun ofWithIsValid(source: Concept?, s: String?, destinations: Set<Concept>): Boolean =
            if (source == null) false
            else {
                when (fromString(s)?.arity) {
                    null -> false
                    RelationshipArity.OneToOne -> destinations.size == 1
                    RelationshipArity.OneToMany -> destinations.isNotEmpty()
                }
            }

        fun of(source: Concept?, s: String?, destinations: Set<Concept>): Relationship {
            require(ofWithIsValid(source, s, destinations))
            val relationshipType = fromString(s)!!
            return if (relationshipType.arity == RelationshipArity.OneToOne) OneToOneRelationship(
                source!!,
                relationshipType,
                destinations.toList().first()
            ) else OneToManyRelationship(
                source!!,
                relationshipType,
                destinations.toMutableSet() // make defensive copy against clearing original set
            )
        }
    }

    abstract fun hasDestination(concept: Concept) : Boolean

    abstract fun canAddDestination(concept: Concept) : Boolean

    abstract fun addDestination(concept: Concept)
}

class OneToOneRelationship(origin: Concept, relationshipType: RelationshipType, val destination: Concept) :
    Relationship(origin, relationshipType) {

    override fun hasDestination(concept: Concept): Boolean = destination == concept
    override fun canAddDestination(concept: Concept) = false
    override fun addDestination(concept: Concept) =
        throw IllegalStateException("OneToOneRelationship: Can't add a destination to an existing OneToOneRelationship.")

    override fun toString() = "$origin $relationshipType $destination"
}

class OneToManyRelationship(origin: Concept, relationshipType: RelationshipType, val destinations: MutableSet<Concept>) :
    Relationship(origin, relationshipType) {

    override fun hasDestination(concept: Concept): Boolean = concept in destinations
    override fun canAddDestination(concept: Concept) = concept !in destinations
    override fun addDestination(concept: Concept) {
        require(canAddDestination(concept)) { "OneToManyRelationship.addDestination: Cannot add duplicate destination concept."}
        destinations.add(concept)
    }

    override fun toString() = destinations.joinToString("\n") { "$origin $relationshipType $it" }
}