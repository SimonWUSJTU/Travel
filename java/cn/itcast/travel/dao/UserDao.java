package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Category;
import cn.itcast.travel.domain.User;

import java.util.List;

public interface UserDao {
    /**
     * 通过用户名查找用户
     * @param username
     * @return
     */
    public  User  findByUsername(String username);

    /**
     * 保存用户信息
     * @param user
     */
    public void save(User user);
    /**
     * 根据code查询用户
     * @param code
     * @return
     */
    public User findByCode(String code);
    /**
     * 更新用户的激活状态
     * @param user
     */
    public void upDateStatus(User user);

    /**
     * 利用用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    public User findByUsernameAndPassword(String username, String password);


}
