package com.tianhe.iniviewer.utils

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.fileChooser.FileChooserFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import java.io.File

/**
 * Created by tianhe on 2023/4/27
 */
class FileChooser(val project: Project) {


    fun chooseDirectory(lastPath: String?): String? {
        val chooser = FileChooserFactory.getInstance()
            .createFileChooser(FileChooserDescriptorFactory.createSingleFolderDescriptor(), project, null)
        val lastDir = if (lastPath != null && File(lastPath).exists()) {
            val file = File(lastPath)
            if (file.isFile) {
                VfsUtil.findFileByIoFile(File(lastPath).parentFile, false)
            } else {
                VfsUtil.findFileByIoFile(file, false)
            }
        } else {
            getProjectRootDir()
        }
        chooser.choose(project, lastDir).let {
            if (it.isNotEmpty()) {
                return it[0].path
            }
        }
        return null
    }


    private fun getProjectRootDir(): VirtualFile? {
        if (project.basePath == null) {
            return null
        }
        return VfsUtil.findFileByIoFile(File(project.basePath!!), false)
    }
}