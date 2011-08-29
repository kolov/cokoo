package net.kolov.hs.modules.hs.model;

import javax.jdo.annotations.*;
import java.io.Serializable;
import java.util.Date;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private String uuid;

    @Persistent
    private String twitterName;
    @Persistent
    private Date firstTime;
    @Persistent
    private Date lastTime;
    @Persistent
    private Boolean notifyStarTweets = true;

    public Boolean getNotifyStarTweets() {
        return notifyStarTweets;
    }

    public void setNotifyStarTweets(Boolean notifyStarTweets) {
        this.notifyStarTweets = notifyStarTweets;
    }


    public long getLastHomelineId() {
        return lastHomelineId;
    }

    public void setLastHomelineId(long lastHomelineId) {
        this.lastHomelineId = lastHomelineId;
    }

    @Persistent
    private long lastHomelineId = -1;

    public boolean isXmppContact() {
        return xmppContact;
    }

    public void setXmppContact(boolean xmppContact) {
        this.xmppContact = xmppContact;
    }

    @Persistent
    private boolean xmppContact;


    @Persistent(nullValue = NullValue.NONE)
    private int twitterId;

    public Date getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Date firstTime) {
        this.firstTime = firstTime;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public int getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(int twitterId) {
        this.twitterId = twitterId;
    }

    public String getTwitterName() {
        return twitterName;
    }

    public void setTwitterName(String twitterName) {
        this.twitterName = twitterName;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Persistent
    private String googleId;
    @Persistent
    private String token;
    @Persistent
    private String tokenKey;

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }

    public boolean isRegistered() {
        return token != null && tokenKey != null;
    }
}
