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
import java.lang.reflect.Method;
import java.util.Map;


@WebServlet("/user/*")  //  /user /add /find
public class UserServlet extends BaseServlet {
	/**
	 * 注册功能
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void regist(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
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

	/**
	 * 登录功能
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void login(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException {
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

	/**
	 * 查找功能
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void findOne(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException {
		//获取session中获取登录的用户信息
		Object user=request.getSession().getAttribute("user");
		//将user写回到客户端（也就是异步交互用的json，然后再$.post的回调函数当中）
		ObjectMapper mapper=new ObjectMapper();
		response.setContentType("application/json;charset=utf-8");
		mapper.writeValue(response.getOutputStream(),user);
	}

	/**
	 * 激活功能
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void active(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException {
		//1,获取激活码
		String code = request.getParameter("code");
		if (code != null) {
			//2，存在激活码，那么激活
			UserService service = new UserServiceImpl();
			boolean flag = service.active(code);
			//3判断返回标志
			String msg = null;
			if (flag) {
				//激活成功
				msg = "激活成功，请<a herf='login.html'>登录</a>";
			} else {
				//激活失败
				msg = "激活失败，请联系管理员";
			}
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(msg);
		}
	}

	/**
	 * 退出功能
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1.销毁session
		request.getSession().invalidate();

		//2.跳转登录页面
		response.sendRedirect(request.getContextPath()+"/login.html");
	}
}



