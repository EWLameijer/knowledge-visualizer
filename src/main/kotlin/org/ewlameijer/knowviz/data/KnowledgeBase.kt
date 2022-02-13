package org.ewlameijer.knowviz.data

class KnowledgeBase(private val concepts: Set<Concept>, private val relationships: Set<Relationship>) {
    override fun toString() =
        "Concepts:\n  ${concepts.joinToString("\n  ")}\nRelationships:\n  ${relationships.joinToString("\n  ")}"
}
