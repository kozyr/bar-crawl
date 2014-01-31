import configs.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import services.PlaceService;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;

@ContextConfiguration(classes={AppConfig.class, TestDataConfig.class})
public class PlaceServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private PlaceService placeService;

}