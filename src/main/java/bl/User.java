package bl;

import ui.core.VirtualKeyPanel;
import ui.simDataService.SimDataFactory;
import ui.simDataService.SimDataService;
import ui.simDataService.StockInstant;

import java.util.ArrayList;

/**
 * Created by chenh on 2016/5/12.
 */
public class User {
    public String userName;
    public String password;

    private  double cash;
    public ArrayList<MyStocks> myStocks;


    /**
     * 服务器端不应该创建User对象
     */
    public User(){
        cash=100000;
        myStocks =new ArrayList<MyStocks>();
        simDataService= SimDataFactory.getSimDataService();

    }

    public boolean bargen(String stockID, int nums, double price, boolean type){
        if(type){
            if (cash<nums*price) {
                return false;
            }else
                cash -= nums * price;
        }else{
            for(int i = 0; i< myStocks.size(); i++){
                if(myStocks.get(i).stockID.equals(stockID)){
                    MyStocks stock=  myStocks.get(i);
                   if(stock.holdingNum <nums) {

                       return false ;
                   }
                    break;
                }
            }
            cash+=nums*price;
        }
        TransactionLog transactionLog=new TransactionLog(stockID,price,type,nums);
        this.addTransactionLog(transactionLog);
        this.refreshMyStocks(stockID,nums,type);
        return true;
    }


    private void refreshMyStocks(String stockID,int nums,boolean type){

        for(int i = 0; i< myStocks.size(); i++){
            if(myStocks.get(i).stockID.equals(stockID)){
                MyStocks stock=  myStocks.get(i);
                if(type){
                    stock.holdingNum +=nums;
                }else{
                    stock.holdingNum -=nums;
                }
                break;
            }
        }

    }
    private void addTransactionLog(TransactionLog transactionLog){
        this.transactionLogs.add(transactionLog);
    }

    public double getCash(){
        return cash;
    }

    /**
     * 获得总资产，即所有股票的总市值和现金的和
     * @return
     */
    public double getTotalMoney(){
        double totalMoney=0;
        for(int i = 0; i< myStocks.size(); i++){
            String stockID= myStocks.get(i).getStockID();
            StockInstant stockInstant=simDataService.getStockInstant(stockID);
            totalMoney+= myStocks.get(i).getHoldingNum()*stockInstant.current;
        }
        totalMoney+=cash;
        return totalMoney;
    }
    public int getTransactionDays(){
        return 0;
    }

    /**
     * 返回的是去除了百分号的部分
     * 如果显示，请加上百分号
     * 如果用来使用，请除以100
     * @return
     */
    public double getSuccessRate(){
        return 0;
    }
    /**
     * 返回的是去除了百分号的部分
     * 如果显示，请加上百分号
     * 如果用来使用，请除以100
     * @return
     */
    public double getTotalBenifitRate(){
        return 0;
    }
    /**
     * 返回的是去除了百分号的部分
     * 如果显示，请加上百分号
     * 如果用来使用，请除以100
     * @return
     */
    public double getMonthBenifitRate(){
        return 0;
    }
    /**
     * 返回的是去除了百分号的部分
     * 如果显示，请加上百分号
     * 如果用来使用，请除以100
     * @return
     */
    public double getWeekBenifitRate(){
        return 0;
    }

    /**
     *获得已买入股票数的种类数
     * @return 当前持有多少只股票
     */
    public int getHoldings() {
        return myStocks.size();
    }

    /**
     * 获得当前拥有的该只股票可交易数
     * @param id
     * @return
     */
    public int getCanSellNum(String id){
        int num=0;
        for(int i = 0; i< myStocks.size(); i++){
            if(myStocks.get(i).getStockID().equals(id)){
                num= myStocks.get(i).getCanSell();
                break;
            }
        }
        return num;
    }

    //为了方便 添加的一个方法
    public void setCash(double cash){ this.cash = cash; }

    public ArrayList<MyStocks> getMyStocks(){
        return myStocks;
    }

    /**
     * 得到浮动盈亏
     * 此处用（当前价-成本价）*数量的计算方式、可能有问题！！！
     * @return
     */
    public double getFloteProfit(){
        int floteProfit=0;
        for(int i = 0; i< myStocks.size(); i++){
            String id=myStocks.get(i).getStockID();
            floteProfit+=myStocks.get(i).getHoldingNum()*(simDataService.getStockInstant(id).current-myStocks.get(i).getPerCost());
        }
        return floteProfit;
    }
}
