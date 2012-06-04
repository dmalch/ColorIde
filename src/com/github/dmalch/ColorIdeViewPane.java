package com.github.dmalch;

import com.intellij.ide.projectView.impl.ProjectViewPane;
import com.intellij.ide.projectView.impl.ProjectViewTree;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

public class ColorIdeViewPane extends ProjectViewPane {
    public ColorIdeViewPane(Project project) {
        super(project);
    }

    @Override
    protected ProjectViewTree createTree(final DefaultTreeModel treeModel) {
        final ProjectViewTree tree = super.createTree(treeModel);

        return (ProjectViewTree) colorizeTree(tree);
    }

    public static JTree colorizeTree(final JTree tree) {
        final EditorColorsScheme globalScheme = EditorColorsManager.getInstance().getGlobalScheme();

        final Color color = globalScheme.getColor(EditorColors.CARET_COLOR);
        tree.setBackground(globalScheme.getDefaultBackground());
        tree.setForeground(color);
        return tree;
    }
}
