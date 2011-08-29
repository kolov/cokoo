package net.kolov.hs.modules.hs.client;

import java.io.Serializable;

/**
 * User: assen
 * Date: 5/12/11
 */
public class ClientStatus implements Serializable {

    private String text;
    private ClientUser user;

    public ClientUser getUser() {
        return user;
    }

    public void setUser(ClientUser user) {
        this.user = user;
    }

    public ClientStatus() {
    }

    public ClientStatus(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
