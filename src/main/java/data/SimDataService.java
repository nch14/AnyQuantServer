package data;

import bl.User;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by chenh on 2016/5/12.
 */
public interface SimDataService {

    /**
     * 模拟炒股模块已经是否已经被激活（根据本地标记）
     * （如果未曾激活，将进入模拟炒股模块初始化设置，如果已经激活，跳过）
     * @return
     */
    public boolean isAlreadyStart();

    /**
     * 设置炒股模块是否激活的标记
     * @param flag 设置为true 激活/false 反激活
     */
    public void setStartFlag(boolean flag);

    /**
     * 获得玩家的VO
     * @return
     */
    public User getUser(String username);

    /**
     * 更新玩家信息
     * @param user
     */
    public void refreshUser(User user);

    /**
     * 根据股票的ID获得股票的名字
     * @param stockID 股票ID
     * @return 股票名字
     */
    public String getName(String stockID);
    /**
     * 获得所有A股的ID
     * @return
     */
    public ArrayList<String> getAllStockID();

    /**
     * 获得所有我们推荐的（50只）股票的ID
     * @return
     */
    public ArrayList<String> getAllRecommendStockID();

    /**
     * 获得股票的实时数据
     * @param stockID 股票的ID
     * @return 一个StockInstant数据类型
     */
    public StockInstant getStockInstant(String stockID);

    /**
     * 日K线查询
     * @param stockID
     * @return
     */
    public ImageIcon getDayK(String stockID);
    /**
     * 周K线查询
     * @param stockID
     * @return
     */
    public ImageIcon getWeekK(String stockID);
    /**
     * 月K线查询
     * @param stockID
     * @return
     */
    public ImageIcon getMonthK(String stockID);

    /**
     * 分时线的查询
     * @param stockID
     * @return
     */
    public ImageIcon getTimeLine(String stockID);


    /**
     * 是否是一个股票名
     * @param stockID
     * @return
     */
    public boolean isStockID(String stockID);

    /**
     * 登记一条股票实时信息
     * @param stockInstant
     */
    public void register(StockInstant stockInstant);

    /**
     * 从内存中除去过期的股票实时消息
     * @param stockID
     */
    public void ditory(String stockID);


}

