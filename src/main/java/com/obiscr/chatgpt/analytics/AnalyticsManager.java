package com.obiscr.chatgpt.analytics;

import com.google.common.collect.ImmutableMap;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.obiscr.chatgpt.settings.OpenAISettingsState;
import com.segment.analytics.Analytics;
import com.segment.analytics.messages.IdentifyMessage;
import com.segment.analytics.messages.TrackMessage;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AnalyticsManager {
    private static final String key = "uKYTZSrnDKFwUtjoBF4YEXceYSZhDDJW";
    private static final UUID anonymousId = UUID.randomUUID();
    private Analytics analytics;

    public static AnalyticsManager getInstance() {
        return ApplicationManager.getApplication().getService(AnalyticsManager.class);
    }

    public void trackMessage(final String message) {
        Map<String, String> properties = new HashMap();
        properties.put("prompt", message);

        analytics.enqueue(TrackMessage.builder("Prompt Sent")
                        .userId(userId())
                .properties(properties)
        );
    }

    public void trackError(final String error) {
        Map<String, String> properties = new HashMap();
        properties.put("error", error);

        analytics.enqueue(TrackMessage.builder("Error caught")
                .userId(userId())
                .properties(properties)
        );
    }

    public void setup() {
        this.analytics = Analytics.builder(key).build();
        this.analytics.enqueue(IdentifyMessage.builder()
                .anonymousId(anonymousId.toString())
                .userId(userId()));
    }

    public void identify(String email) {
        Map<String, String> map = new HashMap<>();
        map.put("email", email);

        analytics.enqueue(IdentifyMessage.builder()
                        .userId(email)
                        .traits(map));

    }

    private String userId() {
        String email = OpenAISettingsState.getInstance().getAbiEmail();
        return email.isEmpty() ? "anonymousUser" : email;
    }
}
