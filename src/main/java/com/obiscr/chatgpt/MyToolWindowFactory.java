package com.obiscr.chatgpt;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.*;
import com.obiscr.chatgpt.analytics.AnalyticsManager;
import com.obiscr.chatgpt.message.ChatGPTBundle;
import com.obiscr.chatgpt.settings.OpenAISettingsState;
import com.obiscr.chatgpt.ui.action.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Wuzi
 */
public class MyToolWindowFactory implements ToolWindowFactory {

    public static final Key ACTIVE_CONTENT = Key.create("ActiveContent");
    public static final String GPT35_TRUBO_CONTENT_NAME = "BeesAI";

    /**
     * Create the tool window content.
     *
     * @param project    current project
     * @param toolWindow current tool window
     */
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        AnalyticsManager.getInstance().setup();

        ContentFactory contentFactory = ContentFactory.getInstance();

        GPT35TurboToolWindow gpt35TurboToolWindow = new GPT35TurboToolWindow(project);
        Content gpt35Turbo = contentFactory.createContent(gpt35TurboToolWindow.getContent(), GPT35_TRUBO_CONTENT_NAME, false);
        gpt35Turbo.setCloseable(false);

        toolWindow.getContentManager().addContent(gpt35Turbo);

        // Set the default component. It require the 1st container
        project.putUserData(ACTIVE_CONTENT, gpt35TurboToolWindow.getPanel());

        List<AnAction> actionList = new ArrayList<>();
        actionList.add(new HelpAction());
        actionList.add(new SettingAction(ChatGPTBundle.message("action.settings")));
        actionList.add(new GitHubAction());
        actionList.add(new PluginAction());
        toolWindow.setTitleActions(actionList);
    }
}
