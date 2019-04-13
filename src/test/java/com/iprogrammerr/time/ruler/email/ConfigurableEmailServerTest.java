package com.iprogrammerr.time.ruler.email;

import com.icegreen.greenmail.junit.GreenMailRule;
import com.iprogrammerr.time.ruler.Configuration;
import com.iprogrammerr.time.ruler.matcher.MimeMessageMatcher;
import com.iprogrammerr.time.ruler.mock.RandomEmails;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.mail.internet.MimeMessage;

public class ConfigurableEmailServerTest {

    private final Configuration configuration = Configuration.fromClassPath();
    @Rule
    public final GreenMailRule rule = new MailRuleFactory().smtp(configuration.smtpPort());

    @Before
    public void setup() {
        rule.setUser(configuration.adminEmail(), configuration.adminPassword());
    }

    @Test
    public void sendsEmail() {
        ConfigurableEmailServer emailServer = new ConfigurableEmailServer(
            configuration.adminEmail(), configuration.adminPassword(), configuration.smtpHost(),
            configuration.smtpPort()
        );
        Email email = new RandomEmails().email();
        emailServer.send(email);
        MimeMessage received = rule.getReceivedMessages()[0];
        MatcherAssert.assertThat(
            "Should receive sent email", received,
            new MimeMessageMatcher(configuration.adminEmail(), email.recipient, email.subject, email.text)
        );
    }
}
