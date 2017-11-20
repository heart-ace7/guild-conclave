package io.hub.guild.configuration;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.hub.guild.configuration.properties.AwsS3Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({AwsS3Properties.class})
public class AwsS3Configuration {

    @Autowired
    private AwsS3Properties properties;

    @Bean
    public AmazonS3 amazonS3Client() {
        final EndpointConfiguration endpointConfig = new EndpointConfiguration(
            properties.getEndpointUrl(), properties.getRegion());

        final ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTPS);
        clientConfig.setConnectionTimeout(properties.getConnectionTimeout());
        clientConfig.setRequestTimeout(properties.getRequestTimeout());
        clientConfig.setSocketTimeout(properties.getSocketTimeout());

        final AWSCredentials credentials = new BasicAWSCredentials(properties.getAccessKey(),
                                                                   properties.getSecretKey());
        return AmazonS3ClientBuilder
            .standard()
            .withEndpointConfiguration(endpointConfig)
            .withClientConfiguration(clientConfig)
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .build();

    }
}
