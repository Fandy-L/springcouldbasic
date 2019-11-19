package com.to8to.tbt.msc.entity;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @author juntao.guo
 */
public class MailAuthenticator extends Authenticator {
    String userName = null;

    String password = null;

    public MailAuthenticator(String username, String password) {
        this.userName = username;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
    }
}
