package net.kolov.hs.modules.hs.model;

import javax.jdo.annotations.*;
import java.io.Serializable;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Relation implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;


    @Persistent
    private int follower;
    @Persistent
    private int followed;
    @Persistent
    private int level = 0;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getFollowed() {
        return followed;
    }

    public void setFollowed(int followed) {
        this.followed = followed;
    }

    public int getFollower() {
        return follower;
    }

    public void setFollower(int follower) {
        this.follower = follower;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
