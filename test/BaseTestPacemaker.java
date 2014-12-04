import persistence.DataBaserXStreamImpl;

/**
 * Created by naysayer on 19/10/2014.
 */
public class BaseTestPacemaker {

    public BaseTestPacemaker() {
        DataBaserXStreamImpl.DELETE_ON_EXIT = Boolean.TRUE;
    }

}
