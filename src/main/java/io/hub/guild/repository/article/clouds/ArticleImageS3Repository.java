package io.hub.guild.repository.article.clouds;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import io.hub.guild.configuration.properties.AwsS3Properties;
import io.hub.guild.repository.article.ArticleImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;

@Repository
public class ArticleImageS3Repository implements ArticleImageRepository {
    @Autowired
    private AwsS3Properties properties;

    @Override
    public PutObjectResult putObject(final String bucketName, final String objectKey, final InputStream is) throws IOException {
        final AmazonS3 s3 = createS3Client();

        final ObjectMetadata meta = new ObjectMetadata();
        // FIXME set content-md5
        // meta.setContentMD5(new String(Base64.encodeBase64(DigestUtils.md5(is))));

        return s3.putObject(bucketName, objectKey, is, meta);
    }

    @Override
    public S3Object getObject(final String bucketName, final String objectKey) {
        final AmazonS3 s3 = createS3Client();

        return s3.getObject(bucketName, objectKey);
    }

    private AmazonS3 createS3Client() {
        final AWSCredentials credentials = new BasicAWSCredentials(properties.getAccessKey(), properties.getSecretKey());
        final EndpointConfiguration endpointConfig = new EndpointConfiguration(properties.getEndpointUrl(), properties.getRegion());

        final ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTPS);
        clientConfig.setConnectionTimeout(5_000);

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withClientConfiguration(clientConfig)
                .withEndpointConfiguration(endpointConfig)
                .build();
    }

}
