package data;

import bl.ServantSheet;
import bl.User;

/**
 * 本类为网络服务需要实现的能供客户端调用的方法
 * Created by chenh on 2016/5/24.
 */
public interface ConnectService {
    /**
     * 返回User信息
     * @param username
     * @return
     */
    public User getUser(String username);

    /**
     * 增加单据
     * @return 服务器是否收到消息
     */
    public boolean addSheet(ServantSheet);
}
