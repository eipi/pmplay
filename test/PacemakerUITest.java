import controllers.PacemakerApi;
import controllers.PacemakerImpl;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;


/**
 * Created by dbdon_000 on 20/10/2014.
 */
public class PacemakerUITest extends BaseTestPacemaker {

    private static final PacemakerApi API = new PacemakerImpl("UI_TEST");
    private static final PacemakerUI UI =  new PacemakerUI(API);

    private static final String ONE_STR = "1";
    private static final Long ONE_NUM = 1l;

    @Test
    public void testUI() throws Exception {
        assertTrue(API.getUsers().isEmpty());
        UI.createUser(ONE_STR, ONE_STR, ONE_STR, ONE_STR);
        assertFalse(API.getUsers().isEmpty());
        UI.deleteUser(ONE_NUM);
        assertTrue(API.getUsers().isEmpty());

    }


}
