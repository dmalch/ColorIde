package com.github.dmalch;

import com.intellij.ide.projectView.impl.AbstractProjectViewPane;
import com.intellij.ide.projectView.impl.ProjectViewPane;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.extensions.ExtensionPoint;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.extensions.ExtensionsArea;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ColorIdeProjectComponent implements ProjectComponent {
    private Project project;

    public ColorIdeProjectComponent(Project project) {
        this.project = project;
    }

    public void initComponent() {
    }

    public void disposeComponent() {
    }

    @NotNull
    public String getComponentName() {
        return "ColorIdeProjectComponent";
    }

    public void projectOpened() {
        final ExtensionsArea area = Extensions.getArea(project);
        final ExtensionPoint<AbstractProjectViewPane> extensionPoint = area.getExtensionPoint(AbstractProjectViewPane.EP_NAME);
        final AbstractProjectViewPane[] extensions = extensionPoint.getExtensions();

        for (AbstractProjectViewPane extension : extensions) {
            if ((extension instanceof ProjectViewPane) && !(extension instanceof ColorIdeViewPane)) {
                extensionPoint.unregisterExtension(extension);
            }
        }
    }

    public void projectClosed() {
    }
}
