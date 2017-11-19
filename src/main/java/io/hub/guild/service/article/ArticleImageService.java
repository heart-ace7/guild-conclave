package io.hub.guild.service.article;

import com.amazonaws.services.s3.model.S3Object;
import io.hub.guild.configuration.properties.AwsS3Properties;
import io.hub.guild.model.internal.article.ArticleImageUploadDto;
import io.hub.guild.repository.article.ArticleImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ArticleImageService {
    @Autowired
    private ArticleImageRepository articleImageRepository;

    @Autowired
    private AwsS3Properties properties;

    private static final String UPLOAD_EXTENSION = "jpg";

    /**
     * 記事の画像イメージをアップロードします.
     *
     * @param is      画像ストリーム
     * @param guildId ギルドID
     * @return dto
     */
    public ArticleImageUploadDto upload(final InputStream is, final Long guildId) throws IOException {
        final String imageDirectoryPath = createImageDirectoryPath(guildId);
        final String fileName = UUID.randomUUID().toString();

        try (final InputStream stream = convertImageFormat(is, UPLOAD_EXTENSION)) {
            articleImageRepository.putObject(properties.getBucketName(), (imageDirectoryPath + fileName + "." + UPLOAD_EXTENSION), stream);
        }

        final ArticleImageUploadDto dto = new ArticleImageUploadDto();
        dto.setDirectoryPath(imageDirectoryPath);
        dto.setFileName(fileName);
        dto.setExtension(UPLOAD_EXTENSION);

        return dto;
    }

    /**
     * 記事の画像イメージを取得します.
     *
     * @param guildId  ギルドID
     * @param fileName 検索ファイル名
     * @param function 書き込みfunction
     * @throws IOException
     */
    public void fetchImage(final Long guildId, final String fileName, final ArticleImageFunction function) throws IOException {
        final String imageDirectoryPath = createImageDirectoryPath(guildId);

        final S3Object object = articleImageRepository.getObject(properties.getBucketName(), (imageDirectoryPath + fileName + "." + UPLOAD_EXTENSION));
        function.writeImage(object.getObjectContent());
    }

    /**
     * 記事の画像イメージを指定の形式に変換します.
     *
     * @param is         対象のストリーム
     * @param formatName ファイル形式
     * @return 変換後のストリーム
     */
    private InputStream convertImageFormat(final InputStream is, final String formatName) {
        Assert.notNull(is, "InputStream must be non-null.");
        Assert.notNull(formatName, "Format name must be non-null.");

        try (final ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            final BufferedImage tmp = ImageIO.read(is);

            if (tmp == null) {
                throw new RuntimeException("Article image is null.");
            }
            if (hasImageFormat(tmp, formatName)) {
                return is;
            }
            // pngを変換すると色がおかしくなる場合があるので, Graphicに描画してから変換する
            final BufferedImage image = new BufferedImage(tmp.getWidth(), tmp.getHeight(), BufferedImage.TYPE_INT_RGB);
            final Graphics graphics = image.getGraphics();
            graphics.drawImage(tmp, 0, 0, null);
            graphics.dispose();

            ImageIO.write(image, formatName, out);

            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Fail to read article image.");
        }
    }

    /**
     * ストリームが対象の形式を持つかを返します.
     *
     * @param is         対象のストリーム
     * @param formatName ファイル形式
     * @return true: formatあり / false: formatなし
     * @throws IOException
     */
    private boolean hasImageFormat(final BufferedImage is, final String formatName) throws IOException {
        Assert.notNull(is, "InputStream must be non-null.");
        Assert.notNull(formatName, "Format name must be non-null.");

        final Iterator<ImageReader> readers = ImageIO.getImageReaders(is);

        while (readers.hasNext()) {
            final ImageReader reader = readers.next();

            if (reader.getFormatName().equals(formatName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 保存画像のディレクトリpathを作成します.
     *
     * @param guildId ギルドID
     * @return path
     */
    private static String createImageDirectoryPath(final Long guildId) {
        return "image/guild/" + guildId + "/article/";
    }

    @FunctionalInterface
    public interface ArticleImageFunction {
        void writeImage(final InputStream in) throws IOException;
    }
}