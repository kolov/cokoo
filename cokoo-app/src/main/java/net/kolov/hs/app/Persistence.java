package net.kolov.hs.app;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

/**
 * AppUser: assen
 * Date: 5/1/11
 */
public class Persistence {
    static PersistenceManagerFactory factory = null;

    public static synchronized PersistenceManagerFactory getManager() {
        if (factory == null) {
            factory = JDOHelper.getPersistenceManagerFactory("transactions-optional");
        }
        return factory;
    }
}
