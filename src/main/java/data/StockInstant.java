package data;


import util.DateUtil;
import util.IsIncrease;

import java.util.Date;

/**
 * Created by chenh on 2016/5/13.
 */
public class StockInstant implements Runnable{
//    String[] header = {"股票名字","今日开盘价    ","昨日收盘价","当前价格",
//            "今日最高价","今日最低价","竟买价","竞卖价","成交的股票数","成交金额(元)",
//            "买一","买一","买二","买二","买三","买三","买四","买四","买五",
//            "买五","卖一","卖一","卖二","卖二","卖三","卖三","卖四",
//            "卖四","卖五","卖五","日期","时间"} ;
    public String StocksID;//股票ID
    public String StocksName;//股票名字

    public double open;//今日开盘价
    public double lastClose;//昨日收盘价
    public double current;//当前价格
    public double high;//今日最高价
    public double low;//今日最低价
    public double buy;//竞买价格
    public double sell;//竞卖价格
    public long volume;//成交量
    public double dealMoney;//成交金额

    public long buy1Amount;//买一股数
    public double buy1;//买一
    public long buy2Amount;//买二股数
    public double buy2;//买二
    public long buy3Amount;//买三股数
    public double buy3;//买三
    public long buy4Amount;//买四股数
    public double buy4;//买四
    public long buy5Amount;//买五股数
    public double buy5;//买五


    public long sell1Amount;//卖一股数
    public double sell1;//卖一
    public long sell2Amount;//卖二股数
    public double sell2;//卖二
    public long sell3Amount;//卖三股数
    public double sell3;//卖三
    public long sell4Amount;//卖四股数
    public double sell4;//卖四
    public long sell5Amount;//卖五股数
    public double sell5;//卖五


    public IsIncrease isIncrease;//是否涨了 是一个枚举类型 定义在util包中
    public double rate;//涨幅或者跌幅 是一个百分数 即(当前价格-开盘价)/开盘价*100


    public Date time;//时间
    public String timeString;//时间字符串


    public StockInstant(String stocksID, String stocksName, double open, double lastClose, double current, double high, double low, double buy, double sell, long volume, double dealMoney, long buy1Amount, double buy1, long buy2Amount, double buy2, long buy3Amount, double buy3, long buy4Amount, double buy4, long buy5Amount, double buy5, long sell1Amount, double sell1, long sell2Amount, double sell2, long sell3Amount, double sell3, long sell4Amount, double sell4, long sell5Amount, double sell5, Date time) {
        StocksID = stocksID;
        StocksName = stocksName;
        this.open = open;
        this.lastClose = lastClose;
        this.current = current;
        this.high = high;
        this.low = low;
        this.buy = buy;
        this.sell = sell;
        this.volume = volume;
        this.dealMoney = dealMoney;
        this.buy1Amount = buy1Amount;
        this.buy1 = buy1;
        this.buy2Amount = buy2Amount;
        this.buy2 = buy2;
        this.buy3Amount = buy3Amount;
        this.buy3 = buy3;
        this.buy4Amount = buy4Amount;
        this.buy4 = buy4;
        this.buy5Amount = buy5Amount;
        this.buy5 = buy5;
        this.sell1Amount = sell1Amount;
        this.sell1 = sell1;
        this.sell2Amount = sell2Amount;
        this.sell2 = sell2;
        this.sell3Amount = sell3Amount;
        this.sell3 = sell3;
        this.sell4Amount = sell4Amount;
        this.sell4 = sell4;
        this.sell5Amount = sell5Amount;
        this.sell5 = sell5;
        this.time = time;
        this.timeString = DateUtil.LogDate2String(time);

        this.rate = (current-lastClose)/lastClose * 100;
        this.isIncrease = getIsIncrease(current,open);
        SimDataFactory.getSimDataService().register(this);
        Thread t=new Thread(this);
        t.start();
    }

    private  IsIncrease getIsIncrease(double current,double open){
        if(current>open){
            return IsIncrease.INCREASE; //涨了
        }else if(current<open){
            return IsIncrease.DECREASE;//跌了
        }
        return  IsIncrease.EQUAL;//没波动
    }
    @Override
    public String toString() {
        return "StockInstant{" +
                "StocksID='" + StocksID + '\'' +
                ", StocksName='" + StocksName + '\'' +
                ", open=" + open +
                ", lastClose=" + lastClose +
                ", current=" + current +
                ", high=" + high +
                ", low=" + low +
                ", buy=" + buy +
                ", sell=" + sell +
                ", volume=" + volume +
                ", dealMoney=" + dealMoney +
                ", buy1Amount=" + buy1Amount +
                ", buy1=" + buy1 +
                ", buy2Amount=" + buy2Amount +
                ", buy2=" + buy2 +
                ", buy3Amount=" + buy3Amount +
                ", buy3=" + buy3 +
                ", buy4Amount=" + buy4Amount +
                ", buy4=" + buy4 +
                ", buy5Amount=" + buy5Amount +
                ", buy5=" + buy5 +
                ", sell1Amount=" + sell1Amount +
                ", sell1=" + sell1 +
                ", sell2Amount=" + sell2Amount +
                ", sell2=" + sell2 +
                ", sell3Amount=" + sell3Amount +
                ", sell3=" + sell3 +
                ", sell4Amount=" + sell4Amount +
                ", sell4=" + sell4 +
                ", sell5Amount=" + sell5Amount +
                ", sell5=" + sell5 +
                ", time=" + time +
                ", timeString='" + timeString + '\'' +
                '}';
    }


    @Override
    public void run() {
        try {
            Thread.sleep(10000);
            SimDataFactory.getSimDataService().ditory(this.StocksID);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
