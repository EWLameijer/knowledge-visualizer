package org.ewlameijer.knowviz.data

class KnowledgeBase(private val concepts: MutableSet<Concept>, private val relationships: MutableSet<Relationship>) {

    fun addRelationship(originConceptAsString: String, relationshipAsString: String, targetConceptAsString: String) {
        val originConcept = getOrCreateConcept(originConceptAsString)
        val targetConcept = getOrCreateConcept(targetConceptAsString)

        if (Relationship.ofWithIsValid(originConcept, relationshipAsString, setOf(targetConcept))) {
            if (canUpdateExistingRelationship(relationshipAsString, originConcept, targetConcept)) return
            if (originConcept !in concepts) concepts.add(originConcept)
            if (targetConcept !in concepts) concepts.add(targetConcept)
            relationships.add(Relationship.of(originConcept,relationshipAsString, setOf(targetConcept)))
        } else println("This relationship is invalid!")
    }

    // imperfect logic
    // originalConceptExists  T   F
    // targetConceptExists    T   F
    // o+t linked by samR     T   F
    //
    private fun canUpdateExistingRelationship(
        relationshipAsString: String,
        originConcept: Concept,
        targetConcept: Concept
    ): Boolean {
        val relationshipType = Relationship.fromString(relationshipAsString)
        if (originConcept in concepts) {
            val potentialMatch =
                relationships.firstOrNull { it.origin == originConcept && it.relationshipType == relationshipType }
            if (potentialMatch != null) {
                addTargetConceptIfPossible(potentialMatch, targetConcept)
                return true
            } // there is no potential match
        }
        return false
    }

    private fun addTargetConceptIfPossible(
        potentialMatch: Relationship,
        targetConcept: Concept
    ) {
        if (potentialMatch.hasDestination(targetConcept)) {
            println("Duplicate relationship")
        } else {
            if (potentialMatch.canAddDestination(targetConcept)) {
                potentialMatch.addDestination(targetConcept)
                if (targetConcept !in concepts) concepts.add(targetConcept)
            } else println("Cannot change relationship")
        }
    }

    private fun getOrCreateConcept(originConceptAsString: String) =
        if (concepts.any { it.text == originConceptAsString }) concepts.first { it.text == originConceptAsString }
        else Concept(originConceptAsString)

    override fun toString() = buildString {
        val originToRelationships = relationships.groupBy { it.origin }
        originToRelationships.forEach { (concept, relationships) ->
            appendLine(concept)
            relationships.forEach { relationship ->
                appendLine("  ${relationship.relationshipType.text}")
                if (relationship is OneToOneRelationship)
                    appendLine("    ${relationship.destination}")
                else (relationship as OneToManyRelationship).destinations.forEach { destination ->
                    appendLine("    $destination")
                }
            }
            appendLine()
        }
    }
}
