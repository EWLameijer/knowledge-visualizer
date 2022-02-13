package org.ewlameijer.knowviz.files

import org.ewlameijer.knowviz.data.Concept
import org.ewlameijer.knowviz.data.KnowledgeBase
import org.ewlameijer.knowviz.data.Relationship
import java.io.File

class FileParser(filename: String) {
    private val lines = File(filename).readLines()
    private val concepts = mutableSetOf<Concept>()
    private val relationships = mutableSetOf<Relationship>()
    private var currentConcept: Concept? = null
    private var currentRelationshipText: String? = null
    private var currentTargetConcepts = mutableSetOf<Concept>()

    fun parse(): KnowledgeBase {
        for (line in lines) {
            if (line.isBlank()) continue
            val currentTerm = line.trim()
            when (line.depth) {
                0 -> updateRootConcept(currentTerm)
                1 -> updateRelationship(currentTerm)
                else -> updateTargetConcepts(currentTerm)
            }
        }
        saveConcept()
        return KnowledgeBase(concepts, relationships)
    }

    private fun updateTargetConcepts(term: String) {
        currentTargetConcepts += Concept(term)
    }

    private fun updateRelationship(term: String) {
        saveConcept()
        currentRelationshipText = term
    }

    private fun updateRootConcept(term: String) {
        saveConcept()
        currentConcept = Concept(term)
    }

    private fun saveConcept() {
        if (currentConcept == null || currentRelationshipText == null) return
        if (Relationship.ofWithIsValid(currentConcept, currentRelationshipText, currentTargetConcepts)) {
            relationships += Relationship.of(currentConcept, currentRelationshipText, currentTargetConcepts)
            concepts += currentConcept!!
            concepts += currentTargetConcepts
            currentTargetConcepts = mutableSetOf()
            currentRelationshipText = null
        } else throw IllegalArgumentException("File badly formatted!")
    }

    private val String.depth: Int
        get() = this.takeWhile { it == ' ' }.length / 2
}