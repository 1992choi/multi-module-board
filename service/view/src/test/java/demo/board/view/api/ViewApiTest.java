package demo.board.view.api;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ViewApiTest {
    RestClient restClient = RestClient.create("http://localhost:9003");

    @Test
    void viewTest() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch latch = new CountDownLatch(10000);

        for (int i = 0; i < 10000; i++) {
            executorService.submit(() -> {
                restClient.post()
                        .uri("/v1/article-views/articles/{articleId}/users/{userId}", 6L, 1L)
                        .retrieve();
                latch.countDown();
            });
        }

        latch.await();

        Long count = restClient.get()
                .uri("/v1/article-views/articles/{articleId}/count", 6L)
                .retrieve()
                .body(Long.class);

        System.out.println("count = " + count); // 어뷰징 방지 정책이 추가된 경우 count = 1. (1번 밖에 처리가 되지 않기 때문)
    }
}
