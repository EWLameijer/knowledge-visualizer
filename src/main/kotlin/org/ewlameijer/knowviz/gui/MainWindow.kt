package org.ewlameijer.knowviz.gui

import org.ewlameijer.knowviz.data.KnowledgeBase
import java.awt.Color
import java.awt.Font
import java.awt.FontMetrics
import javax.swing.JButton
import javax.swing.JFrame
import kotlin.random.Random

const val windowWidth = 1000
const val windowHeight = 700
const val buttonHeight = 30
const val horizontalTextMargin = 20

class MainWindow(knowledgeBase: KnowledgeBase) : JFrame() {
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
        }
        repaint()
    }
}

class MovableButtonComponent(text: String) : JButton(text) {
    init {
        val textWidth = getTextWidth(this.font, text)

        val thisButtonWidth = textWidth + 2 * horizontalTextMargin
        this.setBounds(randomX(thisButtonWidth), randomY(), thisButtonWidth, buttonHeight)
    }
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
