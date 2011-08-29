package net.kolov.hs.modules.hs.server;

import net.kolov.hs.app.Persistence;
import net.kolov.hs.app.Request;
import net.kolov.hs.modules.hs.assembler.ClientUserAssembler;
import net.kolov.hs.modules.hs.client.ClientUser;
import net.kolov.hs.modules.hs.model.AppUser;
import net.kolov.hs.modules.hs.model.Levels;
import net.kolov.hs.modules.hs.model.Relation;
import net.kolov.hs.modules.hs.model.TwitterUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import java.util.*;

public class UserRepository implements Users {

    private Logger LOG = LoggerFactory.getLogger(UserRepository.class);
    private PersistenceManagerFactory pmfInstance = Persistence.getManager();
    private ClientUserAssembler clientUserAssembler = new ClientUserAssembler();


    public void create(AppUser user) {
        PersistenceManager pm = pmfInstance.getPersistenceManager();
        try {
            user.setFirstTime(new Date());
            pm.makePersistent(user);
        } finally {
            pm.close();
        }
    }

    @Override
    public AppUser findById(String id) {
        PersistenceManager pm = pmfInstance.getPersistenceManager();
        Long l = null;
        try {
            l = new Long(id);
        } catch (NumberFormatException e) {
            return null;
        }

        try {
            return pm.getObjectById(AppUser.class, l);

        } finally {
            pm.close();
        }
    }

    @Override
    public AppUser findByUuid(String uuid) {
        PersistenceManager pm = pmfInstance.getPersistenceManager();
        try {
            Query q = pm.newQuery(AppUser.class, "uuid == '" + uuid + "'");
            Collection result = (Collection) q.execute();
            if (result.isEmpty()) {
                return null;
            } else {
                return pm.detachCopy((AppUser) result.iterator().next());
            }

        } finally {
            pm.close();
        }
    }


    public AppUser save(AppUser user) {
        PersistenceManager pm = pmfInstance.getPersistenceManager();
        try {
            pm.makePersistent(user);
            return pm.detachCopy(user);
        } finally {
            pm.close();
        }
    }

    @Override
    public List<ClientUser> getFriends(AppUser user, Integer fromLevel, Integer toLevel) {
        PersistenceManager pm = pmfInstance.getPersistenceManager();
        try {
            String sql = "follower == " + user.getTwitterId() + "";

            if (fromLevel != null) {
                sql += " && level >= " + fromLevel;
            }
            if (toLevel != null) {
                sql += " && level < " + toLevel;
            }
            Query q = pm.newQuery(Relation.class, sql);
            Collection<Relation> result = (Collection) q.execute();
            if (result.isEmpty()) {
                return Collections.emptyList();
            } else {
                List<ClientUser> r = new java.util.ArrayList<ClientUser>();
                for (Relation rel : result) {
                    q = pm.newQuery(TwitterUser.class, "twitterId == " + rel.getFollowed() + "");
                    Collection twitterUsers = (Collection) q.execute();
                    if (!twitterUsers.isEmpty()) {
                        TwitterUser tu = (TwitterUser) twitterUsers.iterator().next();
                        r.add(clientUserAssembler.from(tu, rel));
                    }
                }
                return r;
            }
        } finally {
            pm.close();
        }
    }

    @Override
    public void storeFriends(AppUser user, List<TwitterUser> twitterUsers) {
        PersistenceManager pm = pmfInstance.getPersistenceManager();
        try {


            int follower = user.getTwitterId();
            for (TwitterUser twitterUser : twitterUsers) {
                int followed = twitterUser.getTwitterId();
                Query q = pm.newQuery(Relation.class, "follower == " + follower + " && followed == " + followed);
                Collection result = (Collection) q.execute();
                if (result.isEmpty()) {
                    // no relation, create new
                    Relation r = new Relation();

                    r.setFollower(follower);
                    r.setFollowed(followed);
                    r.setLevel(Levels.INITIAL);
                    pm.makePersistent(r);
                } else {
                    Relation r = (Relation) result.iterator().next();
                    if (r.getLevel() != Levels.INITIAL) {
                        r.setLevel(Levels.INITIAL);
                        pm.makePersistent(r);
                    }
                }

                q = pm.newQuery(TwitterUser.class, "twitterId == " + followed);
                result = (Collection) q.execute();
                if (result.isEmpty()) {
                    pm.makePersistent(twitterUser);
                }
            }
        } finally {
            pm.close();
        }
    }

    @Override
    public void moveFriend(int twitterId, Integer toLevel, AppUser user) {
        PersistenceManager pm = pmfInstance.getPersistenceManager();
        try {
            final String sql = "follower == " + user.getTwitterId() + " && followed == " + twitterId;
            Query q = pm.newQuery(Relation.class, sql);
            Collection result = (Collection) q.execute();
            if (result.isEmpty()) {
                throw new RuntimeException("Moving failed: Reletion (" + sql + ") delivered no results");
            }

            Relation rel = (Relation) result.iterator().next();
            LOG.info("Moved friend " + twitterId + " from level " + rel.getLevel() + " to " + toLevel);
            rel.setLevel(toLevel);
            pm.makePersistent(rel);
        } finally {
            pm.close();
        }
    }

    @Override
    public AppUser findRegisteredTwitterUser(int id) {
        PersistenceManager pm = pmfInstance.getPersistenceManager();
        try {
            Query q = pm.newQuery(AppUser.class, "twitterId == " + id);
            Collection result = (Collection) q.execute();
            if (result.isEmpty()) {
                return null;
            } else {
                AppUser user = (AppUser) result.iterator().next();
                return pm.detachCopy(user);
            }

        } finally {
            pm.close();
        }

    }

    @Override
    public AppUser findByGoogleId(String name) {
        PersistenceManager pm = pmfInstance.getPersistenceManager();
        try {
            Query q = pm.newQuery(AppUser.class, "googleId == '" + name + "'");
            Collection result = (Collection) q.execute();
            if (result.isEmpty()) {
                return null;
            } else {
                AppUser user = (AppUser) result.iterator().next();
                return pm.detachCopy(user);
            }

        } finally {
            pm.close();
        }
    }

    @Override
    public TwitterUser getTwitterUserByTwitterId(int id) {
        PersistenceManager pm = pmfInstance.getPersistenceManager();
        try {
            Query q = pm.newQuery(TwitterUser.class, "twitterId == " + id + "");
            Collection result = (Collection) q.execute();
            if (result.isEmpty()) {
                return null;
            } else {
                TwitterUser user = (TwitterUser) result.iterator().next();
                return pm.detachCopy(user);
            }

        } finally {
            pm.close();
        }
    }

    @Override
    public List<AppUser> findAll() {
        List<AppUser> result = new ArrayList<AppUser>();
        PersistenceManager pm = pmfInstance.getPersistenceManager();
        try {
            Query q = pm.newQuery(AppUser.class);
            Collection users = (Collection) q.execute();
            for (Object user : users) {
                result.add((AppUser) pm.detachCopy(user));
            }
            return result;
        } finally {
            pm.close();
        }
    }

    @Override
    public AppUser findByTwitterId(String twitterName) {
        PersistenceManager pm = pmfInstance.getPersistenceManager();
        try {
            Query q = pm.newQuery(AppUser.class, "twitterName == " + twitterName + "");
            Collection result = (Collection) q.execute();
            if (result.isEmpty()) {
                return null;
            } else {
                AppUser user = (AppUser) result.iterator().next();
                return pm.detachCopy(user);
            }

        } finally {
            pm.close();
        }
    }

    public void save(Request r) {
        PersistenceManager pm = pmfInstance.getPersistenceManager();
        try {
            pm.makePersistent(r);
        } finally {
            pm.close();
        }
    }
}
