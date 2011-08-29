package net.kolov.hs.modules.hs.model;

/**
 * User: assen
 * Date: 6/9/11
 */

import javax.jdo.annotations.*;
import java.util.Date;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Question {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)

    private Long key;

    @Persistent(defaultFetchGroup = "true")
    private String question;

    @Persistent
    private String asker;

    @Persistent
    private Date asked;

    @Persistent(defaultFetchGroup = "true")
    private String answer;

    @Persistent
    private String answerer;

    @Persistent
    private Date answered;

    public Long getKey() {
        return key;
    }
}

