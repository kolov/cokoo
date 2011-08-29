package net.kolov.hs.app;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import net.kolov.hs.modules.hs.model.AppUser;
import net.kolov.hs.modules.hs.server.UserRepository;
import net.kolov.hs.modules.hs.server.Users;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.UUID;

/**
 * AppUser: assen
 * Date: 4/30/11
 */
@Controller
public class Hello {

    @Value("${oauth.key}")
    private String authKey;

    @Value("${oauth.secret}")
    private String authSecret;

    @Value("${callback.url}")
    private String callbackUrl;

    private static final String NAME_ID_COOKIE = "pipipipid";

    private Users userRepository = new UserRepository();


    @RequestMapping("/twau.do")
    public ModelAndView auth(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException, TwitterException {
        String oauth_token = request.getParameter("oauth_token");
        String oauth_verifier = request.getParameter("oauth_verifier");
        try {
            SessionData sessionData = SessionData.getSessionData(session);
        } catch (Exception e) {

            // The auth request din't come from here - show data
            ModelAndView mav = new ModelAndView();
            mav.addObject("oauth_token", oauth_token);
            mav.addObject("oauth_verifier", oauth_verifier);
            mav.setViewName("showToken");
            return mav;
        }


        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(authKey, authSecret);
        AccessToken accessToken = twitter.getOAuthAccessToken(oauth_token, oauth_verifier);

        twitter = new TwitterFactory().getOAuthAuthorizedInstance(authKey, authSecret, accessToken);
        String twitterName = twitter.getScreenName();

        String userToken = accessToken.getToken();
        String userKey = accessToken.getTokenSecret();
        SessionData sessionData = SessionData.getSessionData(session);

        Users users = new UserRepository();
        AppUser knownUser = users.findRegisteredTwitterUser(twitter.getId());
        AppUser user = null;
        if (knownUser != null) {
            Cookie[] cookies = request.getCookies();

            if (cookies != null) {
                for (int i = 0; i < cookies.length; i++) {
                    Cookie cookie = cookies[i];
                    if (cookie.getName().equals(NAME_ID_COOKIE)) {
                        cookie.setValue(knownUser.getUuid());
                        break;
                    }
                }
            } else {
                // unexpected, the cookie must be there
                throw new RuntimeException("Unexpected");
            }
            user = knownUser;
            sessionData.setUser(knownUser);
            sessionData.save(session);
            addCookie(response, knownUser);
        } else {
            user = sessionData.getUser();
            user.setToken(userToken);
            user.setTokenKey(userKey);
            user.setTwitterName(twitterName);
            user.setTwitterId(twitter.getId());
            user = userRepository.save(user);
        }


        sessionData.save(session);

        return navigateToMainUserPage(user);
    }

    private ModelAndView navigateToMainUserPage(AppUser user) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("user", user);
        mav.setViewName("redirectToHello");
        return mav;
    }

    @RequestMapping("/register.do")
    public void register(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {


        try {
            SessionData sessionData = SessionData.getSessionData(session);
        } catch (Exception e) {
            response.sendRedirect("/hello.do");
        }

        twitter4j.Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(authKey, authSecret);
        RequestToken requestToken = twitter.getOAuthRequestToken();

        String url = requestToken.getAuthorizationURL();


        response.sendRedirect(url);
    }


    @RequestMapping("/signout.do")
    public void signout(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // response will contain empty cookie
        removeCookie(response);

        if (request.getUserPrincipal() != null) {
            UserService userService = UserServiceFactory.getUserService();
            final String logoutURL = userService.createLogoutURL("/hello.do");
            response.sendRedirect(logoutURL);
        } else {
            response.sendRedirect("/hello.do");
        }

    }

    @RequestMapping("/hello.do")
    public ModelAndView helloWorld(HttpServletRequest request, HttpServletResponse response) {
        {


            AppUser user = getLoggedUserFromRequest(request);

            if (user == null) {
                user = getUserFromCookie(request);
            }
            if (user == null) {
                user = createLoggedUserFromRequest(request);
            }

            // no id, no cookie = make new
            if (user == null) {
                user = new AppUser();
            }
            if (user.getUuid() == null) {
                user.setUuid(UUID.randomUUID().toString());
                addCookie(response, user);
            }


            HttpSession session = request.getSession(true);
            new SessionData(authKey, authSecret, user).save(session);


            saveRequest(request);


            return navigate(request, user);
        }

    }

    private void saveRequest(HttpServletRequest request) {
        AppUser user = getLoggedUserFromRequest(request);
        net.kolov.hs.app.Request r = new net.kolov.hs.app.Request();
        if (user != null) {
            r.setGoogleId(user.getGoogleId());
            r.setTwitterId(user.getTwitterName());
        }

        r.setCookie(getIdCookie(request));
        r.setTime(new Date());
        new UserRepository().save(r);
    }

    private ModelAndView navigate(HttpServletRequest request, AppUser user) {

        if ("test@example.com".equals(user.getGoogleId())) {
            user.setToken("33521578-vuVUWRQgXkv0Uuhkq1AjCBDquHY0lIx7GgTJKk20o");
            user.setTokenKey("GpOIyPk2V2qFlKNGMLSuELuwU5TH68nPoNMFrMRqZg");
        }

        if (user.getToken() != null && user.getTokenKey() != null) {
            // known twitter id
            return navigateToMainUserPage(user);
        } else {
            // unknown twitter id
            ModelAndView mav = new ModelAndView();
            mav.addObject("user", user);

            UserService userService = UserServiceFactory.getUserService();

            String googleId = user.getGoogleId();
            if (googleId == null) {
                mav.addObject("googleLoginUrl", userService.createLoginURL(request.getRequestURI(), "/greg.do"));

            } else {
                mav.addObject("googleId", googleId);
                mav.addObject("googleLogoutUrl", userService.createLogoutURL("/hello.do"));
            }
            mav.setViewName("helloGuest");
            return mav;
        }
    }

    @RequestMapping("/helloUser.do")
    public ModelAndView helloUser(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        saveRequest(request);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("helloUser");
        return mav;

    }

    @RequestMapping("/greg.do")
    public ModelAndView googleRegistered(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        {
            Principal p = request.getUserPrincipal();
            if (p == null) {
                response.sendRedirect("/hello.do");
            }
            // we have principal
            SessionData sessionData = null;
            try {
                sessionData = SessionData.getSessionData(session);
            } catch (Exception e) {
                response.sendRedirect("/hello.do");
            }
            // we have session user
            AppUser user = sessionData.getUser();
            user.setGoogleId(p.getName());


            user = userRepository.save(user);
            sessionData.save(session);

            return navigate(request, user);
        }
    }

    private AppUser getLoggedUserFromRequest(HttpServletRequest request) {
        Principal p = request.getUserPrincipal();
        if (p != null) {
            return userRepository.findByGoogleId(p.getName());
        } else {
            return null;
        }
    }

    private AppUser createLoggedUserFromRequest(HttpServletRequest request) {
        Principal p = request.getUserPrincipal();
        if (p != null) {
            AppUser user = new AppUser();
            user.setGoogleId(p.getName());
            return user;

        } else {
            return null;
        }
    }

    private AppUser getUserFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        AppUser user = null;
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookie.getName().equals(NAME_ID_COOKIE)) {
                    user = userRepository.findByUuid(cookie.getValue());
                    break;
                }
            }
        }
        if (user != null) {
            // user known from cookie
            Principal p = request.getUserPrincipal();
            if (p != null && !p.getName().equals(user.getGoogleId())) {
                user.setGoogleId(p.getName());
                user = userRepository.save(user);
            }
        }
        return user;
    }

    private void addCookie(HttpServletResponse response, AppUser user) {
        Cookie c = new Cookie(NAME_ID_COOKIE, user.getUuid());
        c.setMaxAge(60 * 60 * 24 * 60);
        response.addCookie(c);
    }

    private void removeCookie(HttpServletResponse response) {
        Cookie c = new Cookie(NAME_ID_COOKIE, "");
        c.setMaxAge(0);
        response.addCookie(c);
    }

    private String getIdCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookie.getName().equals(NAME_ID_COOKIE)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
