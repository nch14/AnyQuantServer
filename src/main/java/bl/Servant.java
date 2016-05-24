package bl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.BlockingQueue;

/**
 * Created by chenh on 2016/5/21.
 */
public class Servant{








    private static Servant servant;
    private ArrayList<DaySheet> daySheets;


    public static Servant getInstance(){
        if (servant==null)
            servant=new Servant();
        return servant;
    }


   private Servant(){
       daySheets=new ArrayList<>();
   }

    public void addServantSheet(ServantSheet servantSheet) {
        String date=(new SimpleDateFormat("yyyyMMdd")).format(Calendar.getInstance().getTime());
        if(daySheets.get(daySheets.size()-1).data.equals(date)){
            daySheets.get(daySheets.size()-1).add(servantSheet);
        }else{
            DaySheet daySheet=new DaySheet();
            daySheets.add(daySheet);
            daySheet.add(servantSheet);
        }
    }
   public void cancelServantSheet(String sheetID){
       for (int i=daySheets.size();i>0;i--){
           DaySheet daySheet=daySheets.get(i);
           for(int j=0;j<daySheet.servantSheets.size();j++){
               ServantSheet servantSheet=daySheet.servantSheets.get(j);
               if (servantSheet.getSheetID().equals(sheetID)){
                   servantSheet.setState(2);
                   return;
               }
           }
       }
    }

    /**
     * 获得尚未处理完毕的订单
     * @return
     */
    public ArrayList<ServantSheet> getInProgressSheet(){
        ArrayList<ServantSheet> inProgress=new ArrayList<>();
        for (int i=0;i<daySheets.size();i++){
            DaySheet daySheet=daySheets.get(i);
            for(int j=0;j<daySheet.servantSheets.size();j++){
                ServantSheet servantSheet=daySheet.servantSheets.get(j);
                if (servantSheet.state==0)
                    inProgress.add(servantSheet);
            }
        }
        return inProgress;
    }
    /**
     * 获得委托成功的订单
     * @return
     */
    public ArrayList<ServantSheet> getSuccessSheet(){
        ArrayList<ServantSheet> inProgress=new ArrayList<>();
        for (int i=0;i<daySheets.size();i++){
            DaySheet daySheet=daySheets.get(i);
            for(int j=0;j<daySheet.servantSheets.size();j++){
                ServantSheet servantSheet=daySheet.servantSheets.get(j);
                if (servantSheet.state==1)
                    inProgress.add(servantSheet);
            }
        }
        return inProgress;
    }

    /**
     * 获得委托失败的订单
     * @return
     */
    public ArrayList<ServantSheet> getFailedSheet(){
        ArrayList<ServantSheet> inProgress=new ArrayList<>();
        for (int i=0;i<daySheets.size();i++){
            DaySheet daySheet=daySheets.get(i);
            for(int j=0;j<daySheet.servantSheets.size();j++){
                ServantSheet servantSheet=daySheet.servantSheets.get(j);
                if (servantSheet.state==2)
                    inProgress.add(servantSheet);
            }
        }
        return inProgress;
    }

    /**
     * 获得全部的委托
     * @return
     */
    public ArrayList<ServantSheet> geAllSheet(){
        ArrayList<ServantSheet> inProgress=new ArrayList<>();
        for (int i=0;i<daySheets.size();i++){
            DaySheet daySheet=daySheets.get(i);
            for(int j=0;j<daySheet.servantSheets.size();j++){
                ServantSheet servantSheet=daySheet.servantSheets.get(j);
                inProgress.add(servantSheet);
            }
        }
        return inProgress;
    }

    /**
     * 获得今日委托成功的订单
     * @return
     */
    public ArrayList<ServantSheet> getTodaySuccessSheet(String date){
        ArrayList<ServantSheet> inProgress=new ArrayList<>();
        if (daySheets.size()==0){
            return inProgress;
        }else {
            DaySheet daySheet = daySheets.get(daySheets.size() - 1);
            if (daySheet.data.equals(date)){
                for (int j = 0; j < daySheet.servantSheets.size(); j++) {
                    ServantSheet servantSheet = daySheet.servantSheets.get(j);
                    if (servantSheet.state == 1)
                        inProgress.add(servantSheet);
                }
            }
            return inProgress;
        }
    }

    /**
     * 获得今日全部的委托
     * @return
     */
    public ArrayList<ServantSheet> getTodayAllSheet(String date){
        ArrayList<ServantSheet> inProgress=new ArrayList<>();
        if (daySheets.size()==0){
            return inProgress;
        }else {
            DaySheet daySheet = daySheets.get(daySheets.size() - 1);
            if (daySheet.data.equals(date)){
                for (int j = 0; j < daySheet.servantSheets.size(); j++) {
                    ServantSheet servantSheet = daySheet.servantSheets.get(j);
                    inProgress.add(servantSheet);
                }
            }
            return inProgress;
        }
    }



    class DaySheet{
        /**
         * 年月日
         */
        String data;
        ArrayList<ServantSheet> servantSheets;

        public DaySheet(){
            data=(new SimpleDateFormat("yyyyMMdd")).format(Calendar.getInstance().getTime());
            servantSheets=new ArrayList<>();
        }
        public void add(ServantSheet servantSheet){
            servantSheets.add(servantSheet);
        }

    }
}
