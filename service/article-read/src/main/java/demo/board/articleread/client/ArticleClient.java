package demo.board.articleread.client;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleClient {
    private RestClient restClient;
    @Value("${endpoints.demo-board-article-service.url}")
    private String articleServiceUrl;

    @PostConstruct
    public void initRestClient() {
        restClient = RestClient.create(articleServiceUrl);
    }

    public Optional<ArticleResponse> read(Long articleId) {
        try {
            ArticleResponse articleResponse = restClient.get()
                    .uri("/v1/articles/{articleId}", articleId)
                    .retrieve()
                    .body(ArticleResponse.class);
            return Optional.ofNullable(articleResponse);
        } catch (Exception e) {
            log.error("[ArticleClient.read] articleId={}", articleId, e);
            return Optional.empty();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArticlePageResponse {
        private List<ArticleResponse> articles;
        private Long articleCount;

        public static ArticlePageResponse EMPTY = new ArticlePageResponse(List.of(), 0L);
    }

    @Getter
    public static class ArticleResponse {
        private Long articleId;
        private String title;
        private String content;
        private Long boardId;
        private Long writerId;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
