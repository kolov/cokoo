package net.kolov.twiscriminator.sc

import net.kolov.hs.modules.hs.client.ClientUser
import net.kolov.hs.modules.hs.model.{Relation, TwitterUser, Levels}
import twitter4j.User

/**
 * User: assen
 * Date: 5/18/11 
 */

class ScalaAssembler {
  def from(tu: TwitterUser, rel: Relation): ClientUser = {
    from(tu, rel.getLevel)
  }

  def from(tu: TwitterUser, level: Int): ClientUser = {
    var cu: ClientUser = new ClientUser
    cu.setTwitterName(tu.getScreenName)
    cu.setPicurl(tu.getPicUrl)
    cu.setLevel(level)
    cu.setTwitterId(tu.getTwitterId)
    return cu
  }

  def from(tu: TwitterUser): ClientUser = {
    from(tu, Levels.INITIAL)
  }

  def from(tweetUser: User): ClientUser = {
    var cu: ClientUser = new ClientUser
    cu.setTwitterName(tweetUser.getScreenName)
    cu.setTwitterId(tweetUser.getId)
    cu.setPicurl(tweetUser.getProfileImageURL.toString)
    return cu
  }

}