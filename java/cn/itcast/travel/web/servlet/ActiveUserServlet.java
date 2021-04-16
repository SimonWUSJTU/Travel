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
 * 用户激活
 */
@WebServlet("/activeUserServelt")
public class ActiveUserServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		this.doPost(request,response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
}



