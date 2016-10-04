package me.jiangcai.wx.classics;

import me.jiangcai.wx.PublicAccountSupplier;
import me.jiangcai.wx.model.PublicAccount;

import java.util.Collections;
import java.util.List;

/**
 * 只有一个公众号的
 * @author CJ
 */
public class SinglePublicAccountSupplier implements PublicAccountSupplier {

    private final PublicAccount publicAccount;

    public SinglePublicAccountSupplier(PublicAccount publicAccount) {
        this.publicAccount = publicAccount;
    }

    @Override
    public List<PublicAccount> getAccounts() {
        return Collections.singletonList(publicAccount);
    }

    @Override
    public PublicAccount findByIdentifier(String identifier) {
        return publicAccount;
    }

    @Override
    public PublicAccount findByHost(String host) {
        return publicAccount;
    }
}
