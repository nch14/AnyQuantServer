package data;
import bl.User;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by chenh on 2016/5/13.
 */
public class SimDataStup implements SimDataService {
    @Override
    public boolean isAlreadyStart() {
        return true;
    }

    @Override
    public void setStartFlag(boolean flag) {

    }

    @Override
    public User getUser(String username) {
        return User.creatUser();
    }

    @Override
    public void refreshUser(User user) {

    }

    @Override
    public String getName(String stockID) {
        return null;
    }

    @Override
    public ArrayList<String> getAllStockID() {
        return null;
    }

    @Override
    public ArrayList<String> getAllRecommendStockID() {
        return null;
    }

    @Override
    public StockInstant getStockInstant(String stockID) {
        return new StockInstant("sh600000","白云机场",12.1,11.2,12.3,12.4,11.9,12.1,12.2,
         111101200, 845456465.63,12300,12.1,12300,12.1,12300,12.1,12300,12.1,12300,12.1,
                12300,12.2,12300,12.2,12300,12.2,12300,12.2,12300,12.2,new Date());
    }

    @Override
    public ImageIcon getDayK(String stockID) {
        return null;
    }

    @Override
    public ImageIcon getWeekK(String stockID) {
        return null;
    }

    @Override
    public ImageIcon getMonthK(String stockID) {
        return null;
    }

    @Override
    public ImageIcon getTimeLine(String stockID) {
        return null;
    }

    @Override
    public boolean isStockID(String stockID) {
        return true;
    }

    @Override
    public void register(StockInstant stockInstant) {

    }

    @Override
    public void ditory(String stockID) {

    }
//    String stocksID, String stocksName, double open, double lastClose, double current, double high, double low, double buy, double sell,
// long volume, double dealMoney, long buy1Amount, double buy1, long buy2Amount, double buy2, long buy3Amount, double buy3, long buy4Amount, double buy4, long buy5Amount, double buy5, long sell1Amount, double sell1, long sell2Amount, double sell2, long sell3Amount, double sell3, long sell4Amount, double sell4, long sell5Amount, double sell5, Date time
}
