package me.jiangcai.wx.classics;

import me.jiangcai.wx.PublicAccountSupplier;
import me.jiangcai.wx.TokenType;
import me.jiangcai.wx.model.PublicAccount;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * 只有一个公众号的
 *
 * @author CJ
 */
public class SinglePublicAccountSupplier implements PublicAccountSupplier {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS"
            , Locale.CHINA);
    private final PublicAccount publicAccount;

    public SinglePublicAccountSupplier(PublicAccount publicAccount) {
        this.publicAccount = publicAccount;
    }

    @Override
    public List<? extends PublicAccount> getAccounts() {
        return Collections.singletonList(publicAccount);
    }

    @Override
    public PublicAccount findByIdentifier(String identifier) {
        return publicAccount;
    }

    @Override
    public void updateToken(PublicAccount account, TokenType type, String token, LocalDateTime timeToExpire)
            throws BackingStoreException {
        Preferences preferences = Preferences.userNodeForPackage(PublicAccount.class);
        Preferences infoPreferences = tokenPreferences(account, type, preferences);
        if (token != null)
            infoPreferences.put("token", token);
        else
            infoPreferences.remove("token");

        if (timeToExpire != null)
            infoPreferences.put("expire", dateTimeFormatter.format(timeToExpire));
        else
            infoPreferences.remove("expire");

        preferences.flush();
    }

    private Preferences tokenPreferences(PublicAccount account, TokenType type, Preferences preferences) {
        Preferences accountPreferences = preferences.node(account.getAppID());
        return accountPreferences.node(type.name());
    }

    @Override
    public void getTokens(PublicAccount account) {
        Preferences preferences = Preferences.userNodeForPackage(PublicAccount.class);

        Preferences access = tokenPreferences(account, TokenType.access, preferences);
        String accessTokenText = access.get("token", null);
        String accessExpireText = access.get("expire", null);
        if (accessTokenText != null) {
            account.setAccessToken(accessTokenText);
        }
        if (accessExpireText != null) {
            account.setTimeToExpire(LocalDateTime.from(dateTimeFormatter.parse(accessExpireText)));
        }

        Preferences javascript = tokenPreferences(account, TokenType.javascript, preferences);

        String javascriptTokenText = javascript.get("token", null);
        String javascriptExpireText = javascript.get("expire", null);
        if (javascriptTokenText != null) {
            account.setJavascriptTicket(javascriptTokenText);
        }
        if (javascriptExpireText != null) {
            account.setJavascriptTimeToExpire(LocalDateTime.from(dateTimeFormatter.parse(javascriptExpireText)));
        }
    }

    @Override
    public PublicAccount findByHost(String host) {
        return publicAccount;
    }
}
