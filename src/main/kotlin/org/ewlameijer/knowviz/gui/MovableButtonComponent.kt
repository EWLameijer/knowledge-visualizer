package org.ewlameijer.knowviz.gui

import java.awt.Font
import java.awt.FontMetrics
import java.awt.Rectangle
import javax.swing.JButton
import kotlin.random.Random

class MovableButtonComponent(text: String) : JButton(text) {
    private val textWidth = getTextWidth(this.font, text)

    private val thisButtonWidth = textWidth + 2 * horizontalTextMargin

    init {
        this.setBounds(randomX(thisButtonWidth), randomY(), thisButtonWidth, buttonHeight)
    }

    private fun Rectangle.center() = Coordinate(x + width / 2, y + height / 2)

    fun center(): Coordinate = bounds.center()

    private fun randomX(textWidth: Int): Int = Random.nextInt(windowWidth - textWidth - 10)

    private fun randomY(): Int = Random.nextInt(windowHeight - buttonHeight)

    //https://stackoverflow.com/questions/69042679/is-there-a-function-to-find-the-amount-of-space-a-text-inside-a-button-will-take
    private fun getTextWidth(font: Font?, text: String?): Int {
        val metrics: FontMetrics = object : FontMetrics(font) {
            private val serialVersionUID = 345265L
        }
        val bounds = metrics.getStringBounds(text, null)
        return bounds.width.toInt()
    }

    fun moveTo(newPosition: Coordinate) {
        this.setBounds(newPosition.x - thisButtonWidth / 2, newPosition.y - buttonHeight / 2, thisButtonWidth, buttonHeight)
    }
}