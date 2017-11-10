package io.hub.guild.model.entity.guild;

import lombok.Data;

@Data
public class Guild {
    private Long id;

    private Long gameId;

    private String title;

    private Long priority;
}
