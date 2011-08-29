package net.kolov.hs.modules.hs.model;

import javax.jdo.annotations.*;
import java.io.Serializable;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class TwitterUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;


    @Persistent
    private int twitterId;

    public int getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(int twitterId) {
        this.twitterId = twitterId;
    }

    @Persistent
    private String screenName;
    @Persistent
    private String picUrl;

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
