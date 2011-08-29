package net.kolov.hs.modules.hs.server;

import com.google.appengine.api.xmpp.*;
import net.kolov.hs.modules.hs.model.AppUser;
import org.springframework.stereotype.Component;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * User: assen
 * Date: 6/6/11
 */
@Component
public class Messenger {
    public void sendMessage(AppUser user, String s) {

        JID jid = new JID(user.getGoogleId());

        Message msg = new MessageBuilder()
                .withRecipientJids(jid)
                .withBody(s)
                .build();

        boolean messageSent = false;
        XMPPService xmpp = XMPPServiceFactory.getXMPPService();
        if (xmpp.getPresence(jid).isAvailable()) {
            SendResponse status = xmpp.sendMessage(msg);
            messageSent = (status.getStatusMap().get(jid) == SendResponse.Status.SUCCESS);
        }

        if (false && !messageSent) {
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);

            javax.mail.Message mailMsg = new MimeMessage(session);
            try {
                mailMsg.setFrom(new InternetAddress("assen.kolov@gmail.com", "Assen N. Kolov"));
                mailMsg.addRecipient(javax.mail.Message.RecipientType.TO,
                        new InternetAddress(user.getGoogleId(), user.getGoogleId()));
                mailMsg.setSubject("Friend move");
                mailMsg.setText(s);
                Transport.send(mailMsg);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
