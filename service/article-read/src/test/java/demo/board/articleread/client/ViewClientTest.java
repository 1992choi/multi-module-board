package demo.board.articleread.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class ViewClientTest {
    @Autowired
    ViewClient viewClient;

    @Test
    void readCacheableTest() throws InterruptedException {
        viewClient.count(1L); // 로그 출력
        viewClient.count(1L); // 로그 미출력 (캐시로 조회했기 때문)
        viewClient.count(1L); // 로그 미출력 (캐시로 조회했기 때문)

        TimeUnit.SECONDS.sleep(3);
        viewClient.count(1L); // 로그 출력
    }

    /*
        @Cacheable 사용과 최적화된 캐시인 @OptimizedCacheable 사용 차이

        - @Cacheable
          - 동시에 트래픽이 몰려온다면, 각 요청별로 cache miss되어 원본 데이터를 계속 조회하고, 결과 값을 계속 재캐시한다.
          - 아래 테스트 케이스를 실행 후 '[ViewClient.count] articleId=1'를 조회하면 21번 찍힘을 확인. (요청별로 계속 원본 데이터를 조회하기 때문)

        - @OptimizedCacheable
          - 위 방법의 한계점을 극복하기 위하여 만든 비즈니스 로직을 녹여내어 직접 만든 어노테이션으로써 트래픽이 몰려도 최초 요청 때만 원본 데이터를 조회하고 결과 값을 재캐시한다.
          - 아래 테스트 케이스를 실행 후 '[ViewClient.count] articleId=1'를 조회하면 5번 찍힘을 확인. (첫 요청만 원본 데이터를 조회하기 때문)
     */
    @Test
    void readCacheableMultiThreadTest() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        viewClient.count(1L); // init cache

        for (int i = 0; i < 5; i++) {
            CountDownLatch latch = new CountDownLatch(5);
            for (int j = 0; j < 5; j++) {
                executorService.submit(() -> {
                    viewClient.count(1L);
                    latch.countDown();
                });
            }
            latch.await();
            TimeUnit.SECONDS.sleep(2);
            System.out.println("=== cache expired ===");
        }
    }

}