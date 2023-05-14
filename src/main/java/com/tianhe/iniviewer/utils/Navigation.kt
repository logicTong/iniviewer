package com.tianhe.iniviewer.utils

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.editor.VisualPosition
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiDocumentManager
import com.tianhe.iniviewer.data.model.Section
import java.io.File

/**
 * Created by tianhe on 2023/4/27
 */
class Navigation(val project: Project) {

    val TAG = "Navigation"

    fun navigationToEditor(section: Section, lineNum: Int) {
        FileEditorManagerEx.getInstanceEx(project).selectedTextEditor?.let { editor ->
            val editingFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.document)?.virtualFile?.path
            Log.d(TAG, "navigationToFile: editingFile = $editingFile")
            if (section.intFilePath == editingFile) {
                scrollToLineNum(editor, lineNum, true)
                return
            }
        }
        openEditor(section.intFilePath, lineNum)
    }

    private fun openEditor(file: String, lineNum: Int) {
        VfsUtil.findFileByIoFile(File(file), false)?.let { vf ->
            FileEditorManagerEx.getInstanceEx(project).openTextEditor(OpenFileDescriptor(project, vf, 0), true)
                ?.let { editor ->
                    scrollToLineNum(editor, lineNum, true)
                }
        }
    }


    private fun scrollToLineNum(editor: Editor, lineNum: Int, select: Boolean) {
        val position = VisualPosition(lineNum, 0)
        editor.caretModel.moveToVisualPosition(position)
        editor.scrollingModel.scrollToCaret(ScrollType.CENTER)
    }

//    private fun selectLine(editor: Editor, lineNum: Int) {
//        if (editor is TextEditor) {
//            val document = editor.document
//            val startOffset = document.getLineStartOffset(lineNum)
//            val endOffset = document.getLineEndOffset(lineNum)
//            editor.selectionModel.setSelection(startOffset, endOffset)
//        }
//    }

}