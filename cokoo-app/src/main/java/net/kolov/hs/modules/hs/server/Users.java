package net.kolov.hs.modules.hs.server;

import net.kolov.hs.modules.hs.client.ClientUser;
import net.kolov.hs.modules.hs.model.AppUser;
import net.kolov.hs.modules.hs.model.TwitterUser;

import java.util.List;

public interface Users {

    void create(AppUser user);

    AppUser findById(String id);

    AppUser findByUuid(String uuid);

    AppUser save(AppUser user);

    List<ClientUser> getFriends(AppUser user, Integer fromLevel, Integer toLevel);

    void storeFriends(AppUser user, List<TwitterUser> twitterUsers);

    void moveFriend(int twitterId, Integer level, AppUser user);

    AppUser findRegisteredTwitterUser(int id);

    AppUser findByGoogleId(String name);

    TwitterUser getTwitterUserByTwitterId(int id);

    List<AppUser> findAll();

    AppUser findByTwitterId(String substring);
}