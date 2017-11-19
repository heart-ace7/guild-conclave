package io.hub.guild.repository.article;

import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

import java.io.IOException;
import java.io.InputStream;

public interface ArticleImageRepository {

    PutObjectResult putObject(final String bucketName, final String objectKey, final InputStream is) throws IOException;

    S3Object getObject(final String bucketName, final String objectKey);

}
