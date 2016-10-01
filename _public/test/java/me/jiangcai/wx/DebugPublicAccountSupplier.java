package me.jiangcai.wx;

import me.jiangcai.wx.model.PublicAccount;

import java.util.Collections;
import java.util.List;

/**
 * @author CJ
 */
public class DebugPublicAccountSupplier implements PublicAccountSupplier {
    @Override
    public List<PublicAccount> getAccounts() {
        return Collections.singletonList(new DebugPublicAccount());
    }

    @Override
    public PublicAccount findByIdentifier(String identifier) {
        return new DebugPublicAccount();
    }

    @Override
    public PublicAccount findByHost(String host) {
        return new DebugPublicAccount();
    }
}
