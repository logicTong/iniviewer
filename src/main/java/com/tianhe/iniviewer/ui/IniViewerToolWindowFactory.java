package com.tianhe.iniviewer.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.tianhe.iniviewer.data.PathList;
import com.tianhe.iniviewer.logic.IniViewer;
import com.tianhe.iniviewer.ui.main.MainPanel;
import org.jetbrains.annotations.NotNull;

public class IniViewerToolWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        IniViewer.INSTANCE.init(project, toolWindow);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(new MainPanel(project), null, false);
        toolWindow.getContentManager().addContent(content);
    }




}
