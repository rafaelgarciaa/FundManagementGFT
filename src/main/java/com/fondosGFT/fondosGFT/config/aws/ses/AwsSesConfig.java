package com.fondosGFT.fondosGFT.config.aws.ses;

import io.awspring.cloud.ses.SimpleEmailServiceMailSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.ses.SesClient;

/**
 * Configuration class for AWS Simple Email Service (SES).
 * This class defines beans necessary for integrating Spring's MailSender
 * with AWS SES, allowing the application to send emails via AWS.
 */
@Configuration
public class AwsSesConfig {

    /**
     * Configures and provides a {@link SimpleEmailServiceMailSender} bean.
     * This bean integrates Spring's MailSender interface with AWS SES,
     * enabling the application to send emails using Amazon's email service.
     * It automatically uses the provided {@link SesClient} to interact with SES.
     *
     * <p>This method leverages Spring Boot's autoconfiguration capabilities for AWS
     * and requires the {@code spring-cloud-aws-starter-ses} dependency.</p>
     *
     * @param sesClient An instance of {@link SesClient} automatically injected by Spring.
     * This client is responsible for low-level interactions with the AWS SES API.
     * @return A fully configured {@link SimpleEmailServiceMailSender} bean, ready to be
     * used for sending emails within the application.
     */
    @Bean
    public SimpleEmailServiceMailSender mailSender(SesClient sesClient) {
        return new SimpleEmailServiceMailSender(sesClient);
    }
}