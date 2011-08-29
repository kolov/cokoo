package net.kolov.hs.modules.hs.assembler;

/**
 * User: assen
 * Date: 6/8/11
 */
public class PrimitiveUtils {
    public static boolean getValue(Boolean notifyStarTweets) {
        return notifyStarTweets == null ? false : notifyStarTweets.booleanValue();
    }
}
