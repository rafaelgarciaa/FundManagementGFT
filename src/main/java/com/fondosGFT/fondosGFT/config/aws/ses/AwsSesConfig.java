package com.fondosGFT.fondosGFT.config.aws.ses;

import io.awspring.cloud.ses.SimpleEmailServiceMailSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
public class AwsSesConfig {

    // Spring Cloud AWS generalmente autoconfigura SesClient si las propiedades están bien.
    // Sin embargo, si lo necesitas explicitamente:
    // @Bean
    // public SesClient sesClient() {
    //    return SesClient.builder()
    //            .region(Region.of("sa-east-1")) // Asegúrate de importar software.amazon.awssdk.regions.Region
    //            .credentialsProvider(DefaultCredentialsProvider.create()) // O usa StaticCredentialsProvider.create(AwsBasicCredentials.create("YOUR_ACCESS_KEY", "YOUR_SECRET_KEY"))
    //            .build();
    // }

    @Bean
    public SimpleEmailServiceMailSender mailSender(SesClient sesClient) {
        return new SimpleEmailServiceMailSender(sesClient);
    }
}