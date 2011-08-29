package net.kolov.hs.modules.hs.client;

import javax.jdo.annotations.Persistent;
import java.io.Serializable;

/**
 * Client info in browser
 * User: assen
 * Date: 5/14/11
 */
public class ClientUser implements Serializable {
    private String twitterName;
    private String googleName;
    private String picurl;
    private int twitterId;
    private boolean xmppContact;
    private String googleLoginUrl;
    private int level;


    public boolean isNotifyStarTweets() {
        return notifyStarTweets;
    }

    public void setNotifyStarTweets(boolean notifyStarTweets) {
        this.notifyStarTweets = notifyStarTweets;
    }

    @Persistent
    private boolean notifyStarTweets = true;

    public String getGoogleLoginUrl() {
        return googleLoginUrl;
    }

    public void setGoogleLoginUrl(String googleLoginUrl) {
        this.googleLoginUrl = googleLoginUrl;
    }

    public boolean isXmppContact() {
        return xmppContact;
    }

    public void setXmppContact(boolean xmppContact) {
        this.xmppContact = xmppContact;
    }

    public int getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(int twitterId) {
        this.twitterId = twitterId;
    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getGoogleName() {
        return googleName;
    }

    public void setGoogleName(String googleName) {
        this.googleName = googleName;
    }

    public String getTwitterName() {
        return twitterName;
    }

    public void setTwitterName(String twitterName) {
        this.twitterName = twitterName;
    }
}
