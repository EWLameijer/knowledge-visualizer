package org.ewlameijer.knowviz.gui

import org.ewlameijer.knowviz.data.KnowledgeBase
import java.awt.Color
import java.awt.Font
import java.awt.FontMetrics
import java.awt.Rectangle
import javax.swing.JButton
import javax.swing.JFrame
import kotlin.math.sqrt
import kotlin.random.Random

const val windowWidth = 1000
const val windowHeight = 700
const val buttonHeight = 30
const val horizontalTextMargin = 20

class MainWindow(knowledgeBase: KnowledgeBase) : JFrame() {
    val concepts = mutableListOf<MovableButtonComponent>()
    val borderForceStrength = 100.0

    init {
        setSize(windowWidth, windowHeight)
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true
        this.contentPane.background = Color.WHITE
        layout = null
        knowledgeBase.concepts().toList().forEach {
            println(it.text)
            val button = MovableButtonComponent(it.text)
            add(button)
            concepts += button
        }
        optimizeLayout()
        repaint()
    }

    fun optimizeLayout() {
        concepts.forEach {
            val center = it.getCenter()
            // a force: needs a distance between two points, and a direction, and a relative strength (assume quadratic?)
            // try step of 10
            val forceFromTop = RepellingForce.from(Coordinate(center.x, 0), center, borderForceStrength)

        }

    }
}

data class Coordinate(val x: Int, val y: Int) {
    fun directionTo(other: Coordinate) = Direction(other.x - x, other.y - y)
}

data class Direction(val dx: Int, val dy: Int) {
    companion object {
        val none = Direction(0, 0)
    }

    val size = sqrt((dx * dx + dy * dy).toDouble())

    fun normalize() =
        if (this == none) NormalizedDirection(0.0, 0.0)
        else NormalizedDirection(dx / size, dy / size)
}

data class DirectionVector(val dx: Double, val dy: Double)

class NormalizedDirection(private val dx: Double, private val dy: Double) {
    operator fun times(factor: Double) = DirectionVector(dx * factor, dy * factor)
}

class RepellingForce(val dx: Double, val dy: Double) {
    companion object {
        fun from(repellerPosition: Coordinate, repelledPosition: Coordinate, strength: Double): RepellingForce {
            val vectorToOther = repellerPosition.directionTo(repelledPosition)
            val distance = vectorToOther.size
            val forceSize = strength / (distance * distance)
            val forceDirection = vectorToOther.normalize()
            val totalForce = forceDirection * forceSize
            return RepellingForce(totalForce.dx, totalForce.dy)
        }
    }
}

class MovableButtonComponent(text: String) : JButton(text) {
    init {
        val textWidth = getTextWidth(this.font, text)

        val thisButtonWidth = textWidth + 2 * horizontalTextMargin
        this.setBounds(randomX(thisButtonWidth), randomY(), thisButtonWidth, buttonHeight)
    }

    private fun Rectangle.center() = Coordinate(x + width / 2, y + height / 2)

    fun getCenter(): Coordinate = bounds.center()
}

val randomizer = Random.Default

fun randomX(textWidth: Int): Int = randomizer.nextInt(windowWidth - textWidth - 10)

fun randomY(): Int = randomizer.nextInt(windowHeight - buttonHeight)

//https://stackoverflow.com/questions/69042679/is-there-a-function-to-find-the-amount-of-space-a-text-inside-a-button-will-take
fun getTextWidth(font: Font?, text: String?): Int {
    val metrics: FontMetrics = object : FontMetrics(font) {
        private val serialVersionUID = 345265L
    }
    val bounds = metrics.getStringBounds(text, null)
    return bounds.width.toInt()
}
