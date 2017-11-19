package io.hub.guild.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "clouds.aws.s3")
@Data
public class AwsS3Properties {

    private String endpointUrl;

    private String region;

    private String accessKey;

    private String secretKey;

    private String bucketName;

}
