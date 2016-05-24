package data;

import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by 王栋 on 2016/5/13 0013.
 */
public class SimData implements SimDataService{

    private ArrayList<String> stockIDCache;
    private ArrayList<StockInstant> stockInstantCache;
    private static boolean isReadFromDB;
    private static boolean isAlreadyStart;
    private static Properties properties;

    public void register(StockInstant stockInstant){
        if(stockIDCache ==null){
            stockIDCache =new ArrayList<>();
            stockInstantCache =new ArrayList<>();
        }
        String id=stockInstant.StocksID;
        stockIDCache.add(id);
        stockInstantCache.add(stockInstant);

    }

    public void ditory(String id){
        int index= stockIDCache.indexOf(id);
        stockIDCache.remove(index);
        stockInstantCache.remove(index);
    }

    static{
        isReadFromDB = false;
        properties = new Properties();
        try {

            FileInputStream inputStream = new FileInputStream(new File(JdbcUtil.class.getClassLoader().getResource("StocksData/initSim.properties").getPath()));
            FileOutputStream outputStream = new FileOutputStream(new File(JdbcUtil.class.getClassLoader().getResource("StocksData/initSim.properties").getPath()));
            properties.load(inputStream);

            if(properties.getProperty("isAlreadyStart")==null){
                properties.setProperty("isAlreadyStart","false");
            }else{
                isAlreadyStart=Boolean.parseBoolean(properties.getProperty("isAlreadyStart"));
                properties.store(outputStream,"");
            }
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            isAlreadyStart = false;
            e.printStackTrace();
        }

    }

    @Override
    public boolean isAlreadyStart() {
        return isAlreadyStart;
    }

    @Override
    public void setStartFlag(boolean flag) {
        isAlreadyStart = flag;
        try {
            FileOutputStream outputStream = new FileOutputStream(new File(JdbcUtil.class.getClassLoader().getResource("StocksData/initSim.properties").getPath()));
            properties.setProperty("isAlreadyStart",Boolean.toString(flag));
            properties.store(outputStream,"");
            outputStream.close();

        } catch (FileNotFoundException e) {
            File file = new File(JdbcUtil.class.getClassLoader().getResource("StocksData/initSim.properties").getPath());
            if(!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public User getUser() {
        User user = User.getInstance();

        if(!isReadFromDB) {
            try {
                Connection connection = JdbcUtil.getConnection();
                Statement statement = connection.createStatement();
                String sql = "select * from ";
                ResultSet cash = statement.executeQuery(sql + TableInfo.cash);
                ResultSet mystocks = statement.executeQuery(sql + TableInfo.mystocks);
                ResultSet transactionLogs = statement.executeQuery(sql + TableInfo.transactionLog);


                if (cash.next()) {
                    user.setCash(cash.getDouble(1));//将从数据库中拿到的钱数放进去
                }

                while (mystocks.next()) {
//                String stockIDCache,String stockName,double perCost,int nums
                    MyStocks myStock = new MyStocks(mystocks.getString(TableInfo.sid),/* mystocks.getString(TableInfo.sname),*/
                            mystocks.getDouble(TableInfo.perCost), mystocks.getInt(TableInfo.nums));
                    user.myStocks.add(myStock);
                }


                while (transactionLogs.next()) {
                   // String time, String stockIDCache, String stockName, double price, boolean type, int nums
                    TransactionLog log = new TransactionLog(DateUtil.LogDate2String(new Date(transactionLogs.getDate(TableInfo.time).getTime())),
                            transactionLogs.getString(TableInfo.sid),transactionLogs.getDouble(TableInfo.price),
                            transactionLogs.getBoolean(TableInfo.type),transactionLogs.getInt(TableInfo.nums));
                    user.transactionLogs.add(log);
                }

                //关闭资源
                JdbcUtil.close(connection, statement, cash);
                connection = null;
                statement = null;
                JdbcUtil.close(connection, statement, mystocks);
                JdbcUtil.close(connection, statement, transactionLogs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return  user;
    }

    @Override
    public void refreshUser(User user) {
        //HashMap<String,MyStocks> myStocks = new HashMap<String,MyStocks>();
        //HashMap<String,TransactionLog> logs = new HashMap<String,TransactionLog>();
        try {
            if(!TableOperation.isExist(TableInfo.user)){
             createTableUser();
                if(!TableOperation.isExist(TableInfo.mystocks)){
                    createTableMystocks();
                    if(!TableOperation.isExist(TableInfo.transactionLog)){
                        createTableLog();
                    }
                }
            }

            //正式开始refresh
            Connection connection = JdbcUtil.getConnection();

            Statement statement = connection.createStatement();
            //String sql = "select * from ";
            //ResultSet mystocks = statement.executeQuery(sql+TableInfo.mystocks);
            //ResultSet transcationLogs = statement.executeQuery(sql+TableInfo.transactionLog);
            connection.setAutoCommit(false);
            String sql = "DELETE from ";
            statement.executeUpdate(sql+TableInfo.cash);
            statement.executeUpdate(sql+TableInfo.mystocks);
            statement.executeUpdate(sql+TableInfo.transactionLog);
            //关个资源
            JdbcUtil.close(null,statement);
            sql = "INSERT INTO "+TableInfo.cash + " VALUES(?);";
            PreparedStatement preparedStatement= connection.prepareStatement(sql);
            preparedStatement.setDouble(1,user.getCash());
            preparedStatement.executeUpdate();
            JdbcUtil.close(null,preparedStatement);
            //preparedStatement = null;
            sql = "INSERT INTO "+TableInfo.mystocks+" VALUES(?,?,?,?);";
            preparedStatement = connection.prepareStatement(sql);
            for(MyStocks po:user.myStocks){
//                String stockIDCache,String stockName,double perCost,int num
                preparedStatement.setString(1,po.getStockID());
                //preparedStatement.setString(2,po.getStockName());
                preparedStatement.setDouble(2,po.getPerCost());
                preparedStatement.setInt(3,po.getHoldingNum());
                preparedStatement.executeUpdate();
            }

            JdbcUtil.close(null,preparedStatement);
            //preparedStatement = null;
            sql = "INSERT INTO "+TableInfo.transactionLog+" VALUES(?,?,?,?,?);";
            preparedStatement = connection.prepareStatement(sql);
            for(TransactionLog log : user.transactionLogs){
//                String time, String stockIDCache, String stockName, double price, boolean type, int nums
                preparedStatement.setDate(1,new Date(DateUtil.String2LogDate(log.getTime()).getTime()));
                preparedStatement.setString(2,log.getStockID());
                //preparedStatement.setString(3,logAndCancel.getStockName());
                preparedStatement.setDouble(3,log.getPrice());
                preparedStatement.setBoolean(4,log.isType());
                preparedStatement.setInt(5,log.getNums());
                preparedStatement.executeUpdate();
            }

            connection.commit();
            JdbcUtil.close(connection,statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }




    }

//    private ArrayList<StockPO> getMyStocks(){
//        return null;
//    }

    private void createTable(String sql) throws SQLException{
        Connection connection = JdbcUtil.getConnection();
        Statement statement = connection.createStatement();
       // String sql = "create table "+TableInfo.user+"("+TableInfo.cash+" double "+");";
        statement.executeUpdate(sql);

        JdbcUtil.close(connection,statement);
    }
    //如果没有user表格创建
    private void createTableUser() throws SQLException {
       // Connection connection = JdbcUtil.getConnection();
       // Statement statement = connection.createStatement();
        String sql = "create table "+TableInfo.user+"("+TableInfo.cash+" double "+");";
        //statement.executeUpdate(sql);

        //JdbcUtil.close(connection,statement);
        createTable(sql);
    }
    //如果没有mystocks创建一个
    private void createTableMystocks() throws SQLException {
 //       Connection connection = JdbcUtil.getConnection();
 //       Statement statement = connection.createStatement();
//        String stockIDCache,String stockName,double perCost,int num
        String sql = "create table "+TableInfo.mystocks+"("+TableInfo.sid+" varchar(8) PRIMARY  KEY ,"/*+TableInfo.sname+" varchar(10),"*/+TableInfo.perCost+" double,"+TableInfo.nums+" int"+");";
//        statement.executeUpdate(sql);
//        JdbcUtil.close(connection,statement);
        createTable(sql);
    }
    //如果没有createTableLog创建一个
    private void createTableLog() throws SQLException {
//        String time, String stockIDCache, String stockName, double price, boolean type, int nums
//        Connection connection = JdbcUtil.getConnection();
//        Statement statement = connection.createStatement();
        String sql = "create TABLE "+TableInfo.transactionLog+"("+TableInfo.time+" date PRIMARY  KEY ,"+TableInfo.sid+" VARCHAR(8),"+/*TableInfo.sname+" VARCHAR(10),"+*/TableInfo.price+" double,"+TableInfo.type+" boolean,"+TableInfo.nums+" int"+");";
//        statement.executeUpdate(sql);
//
//        JdbcUtil.close(connection,statement);
        createTable(sql);
    }

    @Override
    public String getName(String stockID) {
        for(StockPO po: GetStock.stocksMap){
            if(stockID.equals(po.getName())){
                return  po.getCompany();
            }
        }
        return "unknown";
    }

    @Override
    public ArrayList<String> getAllStockID() {
        ArrayList<String> result = new ArrayList<String>();
        if(GetStock.stocksMap.size()==0){
            try {
                new GetStock().getAllStocks(StockType.stocks);
            } catch (NetworkException e) {
                e.printStackTrace();
            }
        }
        for(StockPO po : GetStock.stocksMap){
            result.add(po.getName());
        }
        return  result;
    }

    @Override
    public ArrayList<String> getAllRecommendStockID() {
        ArrayList<String> result = new ArrayList<String>();
        if(FavoriteData.favorite.size()==0){
            new FavoriteData().initFavorite();
        }

        for(StockPO po : FavoriteData.favorite){
            result.add(po.getName());
        }
        return result;
    }

    @Override
    public StockInstant getStockInstant(String stockID) {
        if(stockIDCache ==null){
            stockIDCache =new ArrayList<>();
            stockInstantCache =new ArrayList<>();
        }
        if(stockIDCache.contains(stockID)){
            return stockInstantCache.get(stockIDCache.indexOf(stockID));
        }else{
            StockInstant stockInstant=getStockInstantBySina(stockID);
            if(stockInstant==null){
                //TODO try another GET way  Maybe not have ohlala
            }
            return  stockInstant;
        }
    }

    @Override
    public ImageIcon getDayK(String stockID) {
        URL url = null;
        try {
            url = new URL("http://image.sinajs.cn/newchart/daily/n/"+stockID+".gif");
            ImageIcon imageIcon = new ImageIcon(url);
            return  imageIcon;
        } catch (MalformedURLException e) {
            ImageIcon imageIcon = new ImageIcon("NetworkException","网络连接失败or网络地址失效导致无法显示股票"+stockID+"的日k线图");
//            e.printStackTrace();
 //           imageIcon.getDescription();//如果显示不了的话 可以调用这个方法 来进行提示错误的信息
            return imageIcon;
        }

    }

    @Override
    public ImageIcon getWeekK(String stockID) {

        URL url = null;
        try {
            url = new URL("http://image.sinajs.cn/newchart/weekly/n/"+stockID+".gif");
            ImageIcon imageIcon = new ImageIcon(url);
            return  imageIcon;
        } catch (MalformedURLException e) {
            ImageIcon imageIcon = new ImageIcon("NetworkException","网络连接失败or网络地址失效导致无法显示股票"+stockID+"的周k线图");
//            e.printStackTrace();
            //           imageIcon.getDescription();//如果显示不了的话 可以调用这个方法 来进行提示错误的信息
            return imageIcon;
        }
    }

    @Override
    public ImageIcon getMonthK(String stockID) {

        URL url = null;
        try {
            url = new URL("http://image.sinajs.cn/newchart/monthly/n/"+stockID+".gif");
            ImageIcon imageIcon = new ImageIcon(url);
            return  imageIcon;
        } catch (MalformedURLException e) {
            ImageIcon imageIcon = new ImageIcon("NetworkException","网络连接失败or网络地址失效导致无法显示股票"+stockID+"的月k线图");
//            e.printStackTrace();
            //           imageIcon.getDescription();//如果显示不了的话 可以调用这个方法 来进行提示错误的信息
            return imageIcon;
        }
    }

    @Override
    public ImageIcon getTimeLine(String stockID) {

        URL url = null;
        try {
            url = new URL("http://image.sinajs.cn/newchart/min/n/"+stockID+".gif");
            ImageIcon imageIcon = new ImageIcon(url);
            return  imageIcon;
        } catch (MalformedURLException e) {
            ImageIcon imageIcon = new ImageIcon("NetworkException","网络连接失败or网络地址失效导致无法显示股票"+stockID+"的分时线图");
//            e.printStackTrace();
            //           imageIcon.getDescription();//如果显示不了的话 可以调用这个方法 来进行提示错误的信息
            return imageIcon;
        }
    }

    @Override
    public boolean isStockID(String stockID) {
        for(StockPO po : GetStock.stocksMap){
            if(po.getName().equals(stockID))
                return  true;
        }
        return false;

    }

    private StockInstant getStockInstantBySina(String stockID){
        String split1 = "=";
        String split2 = ",";
        StockInstant stockInstant = null;
        URL ur = null;
        try {
            ur = new URL("http://hq.sinajs.cn/list=" + stockID);
            HttpURLConnection uc = (HttpURLConnection) ur.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(ur.openStream(), "GBK"));
            String msg = null;
            if((msg = reader.readLine())!=null){
                //查看每次从sina API获取的内容--------
                System.out.println(msg);
                //----------------------------------
                if(msg.split(split1).length==2){
                    String[] infos = msg.split(split1)[1].replace("\"","").split(split2);
                    if(infos.length>=32){
                        stockInstant = initStockInstant(stockID,infos);
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  stockInstant;
    }

    private  StockInstant initStockInstant(String stockID,String[] infos){
        return  new StockInstant(stockID,infos[0],Double.parseDouble(infos[1]),Double.parseDouble(infos[2]),
                Double.parseDouble(infos[3]),Double.parseDouble(infos[4]),Double.parseDouble(infos[5]),
                Double.parseDouble(infos[6]),Double.parseDouble(infos[7]),Long.parseLong(infos[8]),
                Double.parseDouble(infos[9]),Long.parseLong(infos[10]),Double.parseDouble(infos[11]),
                Long.parseLong(infos[12]),Double.parseDouble(infos[13]),Long.parseLong(infos[14]),Double.parseDouble(infos[15]),
                Long.parseLong(infos[16]),Double.parseDouble(infos[17]),Long.parseLong(infos[18]),Double.parseDouble(infos[19]),
                Long.parseLong(infos[20]),Double.parseDouble(infos[21]),Long.parseLong(infos[22]),Double.parseDouble(infos[23]),
                Long.parseLong(infos[24]),Double.parseDouble(infos[25]),Long.parseLong(infos[26]),Double.parseDouble(infos[27]),
                Long.parseLong(infos[28]),Double.parseDouble(infos[29]), DateUtil.String2LogDate(infos[30]+" "+infos[31]));

    }

    //    String[] header = {"股票名字","今日开盘价    ","昨日收盘价","当前价格",
//            "今日最高价","今日最低价","竟买价","竞卖价","成交的股票数","成交金额(元)",
//            "买一","买一","买二","买二","买三","买三","买四","买四","买五",
//            "买五","卖一","卖一","卖二","卖二","卖三","卖三","卖四",
//            "卖四","卖五","卖五","日期","时间"} ;

    public static void main(String[] args){
        System.out.print(new SimData().getStockInstant("sh600000").toString());
        GetStock getStock = new GetStock();
        try {
            getStock.getAllStocks(StockType.stocks);
        } catch (NetworkException e) {
            e.printStackTrace();
        }
//        for(StockPO po:GetStock.stocksMap){
//            System.out.println(new SimData().getStockInstant(po.getName()).toString());
//        }
        while(true){
            System.out.println(new SimData().getStockInstant("sh600000").toString());
        }
    }
}
