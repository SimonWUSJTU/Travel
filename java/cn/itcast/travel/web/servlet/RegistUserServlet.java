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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * 用户注册
 */
@WebServlet("/registUserServlet")
public class RegistUserServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		this.doPost(request,response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//验证码的验证
		//从请求的json中获取check的value
		String check=request.getParameter("check");
		//1，从session中获取验证码
		HttpSession session=request.getSession();
		String checkcode_server= (String) session.getAttribute("CHECKCODE_SERVER");
		//2，与业面提交的数据中的check的value进行比较
		if(checkcode_server==null||!checkcode_server.equalsIgnoreCase(check)){
			 //验证码验证失败
			ResultInfo info=new ResultInfo();
			info.setFlag(false);
			info.setErrorMsg("验证码错误！");
			//将info对象序列化成为json;因为本部分采用的是异步响应的措施
			ObjectMapper mapper=new ObjectMapper();
			String json=mapper.writeValueAsString(info);

			//将json数据写回客户端
			//设置content-type
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write(json);
			return;
		}

		//1,获取数据
		Map<String,String[]> map=request.getParameterMap();
		//2,封装对象
		User user=new User();
		try {
			BeanUtils.populate(user,map);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		//3，调用service完成注册
		UserService service=new UserServiceImpl();
		boolean flag=service.regist(user);
		System.out.println("我是傻逼");
		//4，响应数据
		ResultInfo info=new ResultInfo();
		if(flag){
			//注册成功响应
			info.setFlag(true);
		}else{
			//注册失败响应
			info.setFlag(false);
			info.setErrorMsg("注册失败！");
		}

		//将info对象序列化成为json;因为本部分采用的是异步响应的措施
		ObjectMapper mapper=new ObjectMapper();
		String json=mapper.writeValueAsString(info);

		//将json数据写回客户端
		//设置content-type
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}
}



