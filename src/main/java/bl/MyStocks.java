package bl;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by chenh on 2016/5/13.
 */
public class MyStocks {
    /**
     *  String[] boxHead = new String[]{"股票代码", "股票名称", "市值", "盈亏金额", "盈亏百分比", "持仓","可用","成本","现价"};
     */
    String stockID;

    int holdingNum;

    double perCost;

    String today;

    int cannotSell;

    SimStockService simGameBLService;

    public MyStocks(String stockID, double perCost, int num){
        this.stockID=stockID;
        this.perCost=perCost;
        this.holdingNum =num;

        simGameBLService=SimStockBL.getInstance();

    }

    public void buyStocks(double perCost,int num){
        today=(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(Calendar.getInstance().getTime());
        this.perCost=(this.perCost*holdingNum+perCost*num)/(holdingNum+num);
        this.holdingNum+=num;
        cannotSell+=num;

    }

    public void sellStocks(int num){
        this.holdingNum-=num;
    }

    /**
     * 获得当前可交易股票数
     * @return
     */
    public int getCanSell(){
        if((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(Calendar.getInstance().getTime()).equals(today))
            return holdingNum-cannotSell;
        else{
            holdingNum+=cannotSell;
            cannotSell=0;
            return holdingNum;
        }
    }

    /**
     * 获得当前股票总数
     * @return
     */
    public String getStockID() {
        return stockID;
    }

//    public String getStockName() {
//        return stockName;
//    }

    public int getHoldingNum() {
        return holdingNum;
    }

    public double getPerCost() {
        return perCost;
    }
    public double getTotalProfit(){
        return holdingNum*perCost-simGameBLService.getStockInstant(this.stockID).current;
    }
    public double getTotalProfitRate(){
        return (holdingNum*perCost-simGameBLService.getStockInstant(this.stockID).current)/holdingNum*perCost;
    }
}
