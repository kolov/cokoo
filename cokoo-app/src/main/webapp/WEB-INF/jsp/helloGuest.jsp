<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<html>
<head>
    <title>Hello Guest</title>
    <link rel="stylesheet" href="style.css">
    <script type="text/javascript">

        var _gaq = _gaq || [];
        _gaq.push(['_setAccount', 'UA-1853286-10']);
        _gaq.push(['_trackPageview']);

        (function() {
            var ga = document.createElement('script');
            ga.type = 'text/javascript';
            ga.async = true;
            ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
            var s = document.getElementsByTagName('script')[0];
            s.parentNode.insertBefore(ga, s);
        })();

    </script>
</head>
<body>
<div id="container">

    <div id="header">


    </div>
    <div id="content">
        <div class="box title heading"><h1>&nbsp;Welcome, Guest</h1></div>
        <h2>Guide your contacts' tweets into different tracks</h2>
        Linear view of the tweets of <i>all</i> people you've chosen to follow is fine, unless...
        you want to ignore some and be sure you miss nothing from others:
        <ul>
            <li>
                Divide your contacts in categories. Drag & Drop them around.
            </li>

            <li>
                See you star contacts' tweets on top.
            </li>
            <li>
                For a while, ignore some contacts' tweets.
            </li>
            <li>
                Get GTalk instant message when a star contact tweets.
            </li>


        </ul>
        <c:choose>
            <c:when test="${googleId !=null}">
                <p>
                    Hi [${googleId}]
                    <a href="${googleLogoutUrl}"> Log out</a>
                </p>
            </c:when>
            <c:otherwise>
                <p>
                    To be able to resend some tweets toGTalk, we need your authorisation. It is safe, see what Google
                    says
                    about
                    it <a
                        href="http://code.google.com/apis/accounts/docs/OpenID.html">here</a>.
                </p>
                <c:if test="${googleLoginUrl !=null}">
                    <p>
                        If you've already registered here with your Google account, please
                        <button onclick="javascript:window.location='${googleLoginUrl}';">Log in with
                            google
                        </button>
                        again. (It is always a good idea to do that first)
                    </p>
                </c:if>
            </c:otherwise>
        </c:choose>

        <p>You can take a look without Google OpenID login.
            To access the tweets you follow, we need your Twitter authorization. </p>

        <p>Once again, it safe according to Twitter, check it out <a
                href="http://dev.twitter.com/pages/oauth_faq">here</a>.


            <button onclick="javascript:window.location='register.do';">Go to Twitter</button>
        </p>
    </div>
</div>
</body>

</html>