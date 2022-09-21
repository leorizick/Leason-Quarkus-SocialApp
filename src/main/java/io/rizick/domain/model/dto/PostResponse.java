package io.rizick.domain.model.dto;

import io.rizick.domain.model.Post;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostResponse {

    private String text;
    private LocalDateTime date;

    public static PostResponse fromEntity (Post post){
        PostResponse postResponse = PostResponse.builder()
                .date(post.getDate())
                .text(post.getText())
                .build();
        return postResponse;
    }
}
