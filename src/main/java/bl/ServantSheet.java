package bl;
import data.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by chenh on 2016/5/21.
 */
public class ServantSheet{
    /**
     * 创建者用户名
     */
    String username;
    /**
     * 0:待处理    1：已完成   2：已撤销
     */
    int state;
    /**
     * 表单ID
     */
    String sheetID;
    /**
     * 股票ID
     */
    String stockID;
    /**
     *创建时间
     */
    Date initDate;
    /**
     * 完成时间
     */
    Date finishDate;
    /**
     * 委托数量
     */
    private int num;
    /**
     * 完成数量
     */
    private int SuccessNum;
    /**
     * 委托价
     */
    private double prize;
    /**
     * 均价
     */
    private double perPrice;
    /**
     * true:买入
     * false:卖出
     */
    boolean buyOrSell;

    public ServantSheet(String stockID,double prize,int num,boolean buyOrSell){
        this.stockID=stockID;
        this.prize=prize;
        this.num=num;
        this.buyOrSell=buyOrSell;
        initDate=Calendar.getInstance().getTime();
        sheetID=(new SimpleDateFormat("yyyyMMddHHmmss")).format(Calendar.getInstance().getTime())+(int)(Math.random()*10);
        Servant.getInstance().addServantSheet(this);
    }

    public String getSheetID(){
        return sheetID;
    }
    public void setState(int state) {
        this.state = state;
    }

    public Date getInitDate(){
        return initDate;
    }
    public Date getFinishDate(){
        return finishDate;
    }
    public String getStockID(){
        return stockID;
    }

    public int getSuccessNum() {
        return SuccessNum;
    }

    public void setSuccessNum(int successNum) {
        SuccessNum = successNum;
    }

    public double getPerPrice() {
        return perPrice;
    }

    public void setPerPrice(double perPrice) {
        this.perPrice = perPrice;
    }

    public int getNum(){
        return num;
    }

    public double getPrice(){
        return prize;
    }
    public int getState(){
        return state;
    }

    public boolean getType(){
        return buyOrSell;
    }
}
