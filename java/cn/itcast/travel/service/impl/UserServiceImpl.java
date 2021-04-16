package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {

    UserDao userDao=new UserDaoImpl();
    /**
     * 注册服务函数
     * @param user
     * @return
     */
    @Override
    public boolean regist(User user) {
       User u=userDao.findByUsername(user.getUsername());
        if(u !=null){
            //数据库有同名数据
            //保存失败
            return false;
        }else{

            // 数据库没有重复的数据
             //保存数据

            //激活过程
            //1,设置激活码。唯一字符
            user.setCode(UuidUtil.getUuid());
            //2,设置激活状态
            user.setStatus("N");
            userDao.save(user);
            //3,激活邮件发送，邮件正文跳转servlet
            String centent="<a herf='http://localhost/travel/user/active?code="+user.getCode()+"'>点击激活【黑马旅游网】</a>";
            MailUtils.sendMail(user.getEmail(),centent,"激活邮件");
            return true;
        }
    }
    /**
     * 激活服务函数
     * @param code
     * @return
     */
    @Override
    public boolean active(String code) {
        //1，根据激活码查询用户
        User user=userDao.findByCode(code);
        //2，调用dao修改激活状态的方法
        if(user!=null){
            userDao.upDateStatus(user);
            return true;
        }else {
            return false;
        }
    }
    /**
     * 登录服务
     * @param user
     * @return
     */
    @Override
    public User login(User user) {
        return userDao.findByUsernameAndPassword(user.getUsername(),user.getPassword());
    }
}
