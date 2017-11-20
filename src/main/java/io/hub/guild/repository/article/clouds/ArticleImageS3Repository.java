package io.hub.guild.repository.article.clouds;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import io.hub.guild.repository.article.ArticleImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;

@Repository
public class ArticleImageS3Repository implements ArticleImageRepository {
    @Autowired
    private AmazonS3 s3client;

    @Override
    public PutObjectResult putObject(final String bucketName,
                                     final String objectKey,
                                     final InputStream is) throws IOException {
        final ObjectMetadata meta = new ObjectMetadata();
        // FIXME set content-md5
        // meta.setContentMD5(new String(Base64.encodeBase64(DigestUtils.md5(is))));

        return s3client.putObject(bucketName, objectKey, is, meta);
    }

    @Override
    public S3Object getObject(final String bucketName,
                              final String objectKey) {
        return s3client.getObject(bucketName, objectKey);
    }

}
