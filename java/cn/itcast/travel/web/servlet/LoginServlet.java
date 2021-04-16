package cn.itcast.travel.web.servlet;


import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * 用户激活
 */
@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		this.doPost(request,response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//验证码逻辑
		//1,获取用户名和密码数据
		Map<String,String[]> map=request.getParameterMap();
		User user= new User();
		//1-1将数据包装成对象
		try {
			BeanUtils.populate(user,map);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		//2,调用service查询用户
		UserService service=new UserServiceImpl();
		User u=service.login(user);

		ResultInfo info=new ResultInfo();
		//3，对返回的结果进行判断是否存在
		if(u==null){
			//用户名或者是密码错误
			info.setFlag(false);
			info.setErrorMsg("用户名或密码错误");
		}

		if(u!=null&&!"Y".equals(u.getStatus())){
			//用户尚未激活
			info.setFlag(false);
			info.setErrorMsg("您的账号尚未激活，请激活");
		}
		if(u != null && "Y".equals(u.getStatus())){
			request.getSession().setAttribute("user",u);//登录成功标记

			//登录成功
			info.setFlag(true);
		}
		//4，对数据进行响应
		ObjectMapper mapper=new ObjectMapper();
		response.setContentType("application/json;charset=utf-8");
		mapper.writeValue(response.getOutputStream(),info);
		}
	}



