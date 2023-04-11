package com.tianhe.iniviewer.data.model

import com.tianhe.iniviewer.ui.main.tree.node.KVTreeNode
import com.tianhe.iniviewer.ui.main.tree.node.MultiSectionTreeNode
import com.tianhe.iniviewer.ui.main.tree.node.SectionTreeNode
import java.util.*
import javax.swing.tree.TreeNode

/**
 * Created by tianhe on 2023/4/9
 */
class SectionTree(private val rootName: String) {
    var rootNode: MultiSection? = null

    init {
        rootNode = SectionDict.get(rootName)
        if (rootNode == null) {
            rootNode = SectionDict.findIgnoreCase(rootName)
        }
    }


    fun buildTreeModel(): TreeNode? {
        rootNode?.let {
            return traversalTree(it)
        }
        return null
    }


    private fun traversalTree(root: MultiSection): TreeNode {
        val rootNode = MultiSectionTreeNode(root)
        val queue: Queue<TreeNode> = LinkedList()
        queue.offer(rootNode)
        while (queue.isNotEmpty()) {
            val model = queue.poll()
            if (model is MultiSectionTreeNode) {
                handleMultiSectionTreeNode(model, queue)
            } else if (model is SectionTreeNode) {
                handleSectionTreeNode(model, queue)
            } else if (model is KVTreeNode) {
                handleKVTreeNode(model, queue)
            }
        }
        return rootNode
    }

    private fun handleMultiSectionTreeNode(node: MultiSectionTreeNode, queue: Queue<TreeNode>) {
        if (node.isConflictNode()) {
            //冲突的情况，把冲突的section作为子树
            node.getSections().forEach {
                val nextNode = SectionTreeNode(it)
                node.add(nextNode)
                queue.offer(nextNode)
            }
        } else {
            //不冲突，只有一个section
            node.section.let { section ->
                section.properties.forEach {
                    val nextNode = KVTreeNode(it.key, it.value, section)
                    node.add(nextNode)
                    queue.offer(nextNode)
                }
            }
        }
    }

    private fun handleSectionTreeNode(node: SectionTreeNode, queue: Queue<TreeNode>) {
        node.section.let { section ->
            section.properties.forEach {
                val nextNode = KVTreeNode(it.key, it.value, section)
                node.add(nextNode)
                queue.offer(nextNode)
            }
        }
    }

    private fun handleKVTreeNode(node: KVTreeNode, queue: Queue<TreeNode>) {
        val value = node.value
        if (value.isMultiReference) {
            //引用多值
            value.values?.forEach { name ->
                SectionDict.get(name)?.let {
                    val nextNode = MultiSectionTreeNode(it)
                    node.add(nextNode)
                    queue.offer(nextNode)
                }
            }
        } else if (value.isReference) {
            SectionDict.get(value.value)?.let {
                val nextNode = MultiSectionTreeNode(it)
                node.add(nextNode)
                queue.offer(nextNode)
            }
        }

    }


}