package bl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.BlockingQueue;

/**
 * Created by chenh on 2016/5/24.
 */
public class AutoController {
    private static BlockingQueue<ServantSheet> processing;

    public void addServantSheet(ServantSheet servantSheet) {
        processing.add(servantSheet);
    }

    private void finishServantSheet(ServantSheet servantSheet){
        processing.remove(servantSheet);
    }

    private void procced(ServantSheet servantSheet){

    }
}
