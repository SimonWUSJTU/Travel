package cn.itcast.travel.service;

import cn.itcast.travel.domain.User;

public interface UserService {
    /**
     * 注册服务函数
     * @param user
     * @return
     */
    public boolean regist(User user);

    /**
     * 激活服务函数
     * @param code
     * @return
     */
    public boolean active(String code);

    /**
     * 登录服务
     * @param user
     * @return
     */
    public User login(User user);
}
