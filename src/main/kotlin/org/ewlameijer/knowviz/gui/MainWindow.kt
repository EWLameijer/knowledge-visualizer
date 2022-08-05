package org.ewlameijer.knowviz.gui

import org.ewlameijer.knowviz.data.KnowledgeBase
import java.awt.Color
import javax.swing.JFrame
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

const val windowWidth = 1000
const val windowHeight = 700
const val buttonHeight = 30
const val horizontalTextMargin = 20

class MainWindow(knowledgeBase: KnowledgeBase) : JFrame() {
    private val concepts = mutableListOf<MovableButtonComponent>()
    private val borderForceStrength = 100_000.0
    private val betweenConceptForceStrength = 200_000.0

    init {
        setSize(windowWidth, windowHeight)
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true
        this.contentPane.background = Color.WHITE
        layout = null
        addButtons(knowledgeBase.concepts(), ::ConceptComponent)
        addButtons(knowledgeBase.relationships(), ::RelationshipComponent)
        optimizeLayout()
    }

    private fun <T> addButtons(items: Set<T>, constructor: (T) -> MovableButtonComponent) {
        items.forEach {
            val button = constructor(it)
            add(button)
            concepts += button
        }
    }

    private fun optimizeLayout() {
        repeat(10) {
            singleRoundLayoutOptimization()
            repaint()
        }
    }

    private fun singleRoundLayoutOptimization() {
        concepts.forEachIndexed { index, component ->
            var stepSize = 10.0
            val otherConcepts = concepts.filterIndexed { otherIndex, _ -> otherIndex != index }
            while (stepSize >= 1.0) {
                val center = component.center()
                val currentForces = forcesAt(center, otherConcepts)

                // Okay. Now try a step in the right direction. Say of size 10. If it is better, execute it. If it is worse, halve the step it.
                val possibleNewPosition = center + CoordinateDifference.from(currentForces.direction * stepSize)
                val newForces = forcesAt(possibleNewPosition, otherConcepts)
                if (newForces.size < currentForces.size) component.moveTo(possibleNewPosition) else stepSize /= 2
            }

            // TODO: make extra concepts like "has a" for extra boxes
            // TODO: take dynamic window size
            // TODO: now draw arrows/lines between linked concepts. Next step: make them arrows.

        }
    }

    private fun forcesAt(position: Coordinate, otherComponents: List<MovableButtonComponent>): RepellingForce {
        val borderCoordinates =
            listOf(position.x to 0, position.x to windowHeight, 0 to position.y, windowWidth to position.y)
        val borderForces = borderCoordinates.map { (x, y) ->
            RepellingForce.from(Coordinate(x, y), position, borderForceStrength)
        }
        val conceptForces =
            otherComponents.map { RepellingForce.from(it.center(), position, betweenConceptForceStrength) }
        return (borderForces + conceptForces).sum()
    }
}



