package data;

/**
 * Created by chenh on 2016/5/22.
 */
public class SimDataFactory {

    private static SimDataService simDataService;
    public static SimDataService getSimDataService(){
        if(simDataService==null)
            simDataService=new SimData();
            //simDataService=new SimDataStup();
        return simDataService;
    }
}
