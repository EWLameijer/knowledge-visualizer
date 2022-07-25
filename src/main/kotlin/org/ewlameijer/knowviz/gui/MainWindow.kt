package org.ewlameijer.knowviz.gui

import org.ewlameijer.knowviz.data.KnowledgeBase
import java.awt.Color
import java.awt.FlowLayout
import javax.swing.JButton
import javax.swing.JFrame

class MainWindow(knowledgeBase: KnowledgeBase) : JFrame() {
    init {
        setSize(1000, 700)
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true
        this.contentPane.background = Color.WHITE
        layout = FlowLayout()
        knowledgeBase.concepts().toList().forEach {
            println(it.text)
            val button = JButton(it.text)
            add(button)
        }
    }
}
