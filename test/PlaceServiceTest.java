import configs.AppConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import services.BarService;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;

@ContextConfiguration(classes={AppConfig.class, TestDataConfig.class})
public class PlaceServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private BarService barService;

}