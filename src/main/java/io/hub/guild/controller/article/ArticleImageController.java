package io.hub.guild.controller.article;

import com.google.common.collect.ImmutableMap;
import io.hub.guild.model.internal.article.ArticleImageUploadDto;
import io.hub.guild.service.article.ArticleImageService;
import io.hub.guild.service.article.ArticleImageService.ArticleImageFunction;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@Secured("ROLE_USER")
public class ArticleImageController {

    @Autowired
    private ArticleImageService articleImageService;

    @GetMapping(value = "guilds/{guildId}/article-images/{fileName}")
    public void fetchImage(@PathVariable final Long guildId, @PathVariable final String fileName, final HttpServletResponse response) throws IOException {
        final ArticleImageFunction function = in -> {
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);

            IOUtils.copy(in, response.getOutputStream());
        };

        articleImageService.fetchImage(guildId, fileName, function);
    }

    @PostMapping(value = "guilds/{guildId}/article-images", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<String, String> uploadImage(@PathVariable final Long guildId, @RequestParam("imageFile") final MultipartFile imageFile) throws IOException {
        final ArticleImageUploadDto dto = articleImageService.upload(imageFile.getInputStream(), guildId);

        final UriComponents uriComponents = MvcUriComponentsBuilder
                .fromMethodName(ArticleImageController.class, "fetchImage", guildId, dto.getFileName(), null)
                .build();
        return ImmutableMap.of("imagePath", uriComponents.getPath());
    }
}
