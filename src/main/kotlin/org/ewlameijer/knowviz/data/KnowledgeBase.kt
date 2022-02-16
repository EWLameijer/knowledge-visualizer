package org.ewlameijer.knowviz.data

class KnowledgeBase(private val concepts: Set<Concept>, private val relationships: Set<Relationship>) {

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
