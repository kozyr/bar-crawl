import org.junit.Test;
import play.libs.WS;
import play.mvc.Result;

import java.util.concurrent.TimeUnit;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.*;

public class ApplicationTest {

    private static final double TEST_LAT = 41.944632;
    private static final double TEST_LON = -87.654455;

    @Test
    public void callIndex() {
        running(fakeApplication(), new Runnable() {
            public void run() {
                Result result = callAction(controllers.routes.ref.Application.index());
                assertThat(status(result)).isEqualTo(OK);
                assertThat(contentType(result)).isEqualTo("text/html");
                assertThat(charset(result)).isEqualTo("utf-8");
                assertThat(contentAsString(result)).contains("Place Crawl App");
            }
        });
    }

    @Test
    public void callPlaceCrawl() {
        running(fakeApplication(), new Runnable() {
            public void run() {
                Result result = callAction(controllers.routes.ref.Application.placeCrawl(TEST_LAT, TEST_LON));
                assertThat(status(result)).isEqualTo(OK);
                assertThat(contentType(result)).isEqualTo("application/json");
                String content = contentAsString(result);
                assertThat(content).startsWith("{");
                assertThat(content).contains("osm_id");
            }
        });
    }


    @Test
    public void realCrawlRequest() {
        running(testServer(3333), new Runnable() {
            public void run() {
                assertThat(WS.url("http://localhost:3333/place-crawl").
                        setQueryParameter("lat", String.valueOf(TEST_LAT)).
                        setQueryParameter("lon", String.valueOf(TEST_LON)).
                        get().get(10, TimeUnit.SECONDS).getStatus()).isEqualTo(OK);
            }
        });
    }
}
