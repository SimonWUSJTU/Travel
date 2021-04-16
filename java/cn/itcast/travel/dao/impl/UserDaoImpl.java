package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class UserDaoImpl implements UserDao {
    //获取链接数据库的对象
    private JdbcTemplate template=new JdbcTemplate(JDBCUtils.getDataSource());
    /**
     * 通过用户名查找用户
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username){
        User user=null;
        try {
            //1,定义sql
            String sql = "select * from tab_user where username = ?";
            //2，执行sql
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);
        }catch (Exception e){

        }
        return user;
    }
    /**
     * 保存用户信息
     * @param user
     */
    @Override
    public void save(User user){
        //1,定义sql
        String sql="insert into tab_user(username,password,name,birthday,sex,telephone,email,status,code) values(?,?,?,?,?,?,?,?,?)";
        //2,执行sql
        template.update(sql,user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getBirthday(),
                user.getSex(),
                user.getTelephone(),
                user.getEmail(),
                user.getStatus(),
                user.getCode()
        );
    }

    /**
     * 根据code查询用户
     * @param code
     * @return
     */
    @Override
    public User findByCode(String code) {
        User user=new User();
        try {
            String sql = "select * from tab_user where code = ?";
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), code);
        }catch (Exception e){
            e.printStackTrace();
        }

        return user;
    }

    /**
     * 更新用户的激活状态
     * @param user
     */
    @Override
    public void upDateStatus(User user) {
        String sql="update tab_user set status ='Y' where uid = ?";
        template.update(sql,user.getUid());
    }

    /**
     * 利用用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    @Override
    public User findByUsernameAndPassword(String username, String password) {
        User user=null;
        try {
            //1,定义sql
            String sql = "select * from tab_user where username = ? and password = ?";
            //2，执行sql
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username,password);
        }catch (Exception e){

        }
        return user;
    }

}
