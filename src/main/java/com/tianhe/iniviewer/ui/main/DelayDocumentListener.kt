package com.tianhe.iniviewer.ui.main

import com.tianhe.iniviewer.utils.Log
import javax.swing.JTextField
import javax.swing.Timer
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

/**
 * Created by tianhe on 2023/4/9
 */
abstract class DelayDocumentListener(private val textField: JTextField, delay: Int) : DocumentListener {

    val TAG = "DelayDocumentListener"

    private val delayTimer = Timer(delay) {
        onTextChange(textField.text)
    }

    init {
        delayTimer.isRepeats = false
    }

    override fun insertUpdate(e: DocumentEvent?) {
        Log.d(TAG, "treeRootText: insertUpdate text=${textField.text}")
        delayNotify()
    }

    override fun removeUpdate(e: DocumentEvent?) {
        Log.d(TAG, "treeRootText: removeUpdate text=${textField.text}")
        delayNotify()
    }

    override fun changedUpdate(e: DocumentEvent?) {
        Log.d(TAG, "treeRootText: changedUpdate text=${textField.text}")

    }

    private fun delayNotify() {
        if (delayTimer.isRunning) {
            delayTimer.stop()
        }
        delayTimer.start()
    }

    abstract fun onTextChange(text: String)
}