package io.hub.guild.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "clouds.aws.s3")
@Data
public class AwsS3Properties {
    @NotNull
    private String endpointUrl;

    private String region;

    private String accessKey;

    private String secretKey;

    private String bucketName;

    private Integer connectionTimeout = 5 * 1_000;

    private Integer requestTimeout = 0;

    private Integer socketTimeout = 10 * 1_000;
}
