
package com.obiscr.chatgpt.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Tag;
import com.obiscr.OpenAIProxy;
import com.obiscr.chatgpt.analytics.AnalyticsManager;
import com.obiscr.chatgpt.util.EmailValidator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.Proxy;
import java.util.*;

import static com.obiscr.chatgpt.MyToolWindowFactory.*;

/**
 * @author Wuzi
 * Supports storing the application settings in a persistent way.
 * The {@link State} and {@link Storage} annotations define the name of the data and the file name where
 * these persistent application settings are stored.
 */
@State(
        name = "com.obiscr.chatgpt.settings.OpenAISettingsState",
        storages = @Storage("ChatGPTSettingsPlugin.xml")
)
public class OpenAISettingsState implements PersistentStateComponent<OpenAISettingsState> {
  public String getAbiEmail() {
    return abiEmail;
  }

  public void setAbiEmail(String abiEmail) {
    this.abiEmail = abiEmail;
    if (EmailValidator.isValidEmail(abiEmail)) {
      AnalyticsManager.getInstance().identify(abiEmail);
    }
  }

  private String abiEmail = "";
  public String customizeUrl = "";
  public String readTimeout = "50000";
  public String connectionTimeout = "50000";
  public Boolean enableProxy = false;
  public Boolean enableAvatar = true;
  public SettingConfiguration.SettingProxyType proxyType =
          SettingConfiguration.SettingProxyType.DIRECT;

  public String proxyHostname = "";
  public String proxyPort = "10000";

  public String accessToken = "";
  public String expireTime = "";
  public String imageUrl = "https://cdn.auth0.com/avatars/me.png";
  public String apiKey = "";
  public Boolean enableLineWarp = true;

  @Deprecated
  public List<String> customActionsPrefix = new ArrayList<>();

  public String chatGptModel = "text-davinci-002-render-sha";
  public String gpt35Model = "gpt-3.5-turbo";
  public Boolean enableContext = false;
  public String assistantApiKey = "";
  public Boolean enableTokenConsumption = false;
  public Boolean enableGPT35StreamResponse = false;
  public String gpt35TurboUrl = "https://api.openai.com/v1/chat/completions";

  public Boolean enableProxyAuth = false;
  public String proxyUsername = "";
  public String proxyPassword = "";

  public Boolean enableCustomizeGpt35TurboUrl = false;
  public Boolean enableCustomizeChatGPTUrl = false;
  public Boolean enabledCustomDatasources = false;
  public String indexNames = "";
  public String gpt35RoleText = "You are a helpful language assistant";
  public String prompt1Name = "Code Review";
  public String prompt1Value = "Review the code with the rules:\n" +
          "Apply the principles of DRY, KISS, YAGNI and Clean Code during the review process.\n" +
          "For each fix, create a section in markdown entry with the following information: previous_code (code before the fix), fixed_code (code after the fix), and comments (explanation of the fix).\n" +
          "Make sure to add what was the source of the issue (source_of_correction) like: KISS, DRY, YAGNI or Clean code.";
  public String prompt2Name = "Create Unit Tests";
  public String prompt2Value = "Create test cases for the function following these rules:\n" +
          "The tests must be written using the same language as the provided code. \n" +
          "Put the tests cases in the same markdown section. \n" +
          "Name the object of the class_name as sut.\n" +
          "If it wouldn't be possible to infer a type, class or interface ask the developer to provide more details of it. \n" +
          "Make sure to cover the throw scenarios as well.";
  public String prompt3Name = "Find a Bug";
  public String prompt3Value = "Find the bug in the code below:";

  @Tag("customPrompts")
  public Map<String, String> customPrompts = new HashMap<>();

  public static OpenAISettingsState getInstance() {
    return ApplicationManager.getApplication().getService(OpenAISettingsState.class);
  }

  @Nullable
  @Override
  public OpenAISettingsState getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull OpenAISettingsState state) {
    XmlSerializerUtil.copyBean(state, this);
  }

  public void reload() {
    loadState(this);
  }

  public Proxy getProxy() {
    Proxy proxy = null;
    if (enableProxy) {
      Proxy.Type type = proxyType ==
              SettingConfiguration.SettingProxyType.HTTP ? Proxy.Type.HTTP :
              proxyType == SettingConfiguration.SettingProxyType.SOCKS ? Proxy.Type.SOCKS :
                      Proxy.Type.DIRECT;
      proxy = new OpenAIProxy(proxyHostname, Integer.parseInt(proxyPort),
              type).build();
    }
    return proxy;
  }
}
