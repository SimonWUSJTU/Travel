package cn.itcast.travel.web.servlet;


import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 旅游路线查阅
 */
@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {

	private RouteService routeService=new RouteServiceImpl();
	private FavoriteService favoriteService=new FavoriteServiceImpl();


	public void pageQuery(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
		//1,接受参数
		String currentPageStr=request.getParameter("currentPage");
		String pageSizeStr=request.getParameter("pageSize");
		String cidStr=request.getParameter("cid");

		//接受rname 线路名称
		String rname=request.getParameter("rname");
		rname = new String(rname.getBytes("iso-8859-1"),"utf-8");

		//2，处理参数
		int cid=0;//类别id
		if(cidStr!= null &&cidStr.length()>0&&!"null".equals(cidStr)){
			cid=Integer.parseInt(cidStr);
		}
		int currentPage=0;//当前页码，如果不传递，则默认为第一页
		if(currentPageStr!=null&&currentPageStr.length()>0){
			currentPage=Integer.parseInt(currentPageStr);
		}else{
			currentPage=1;
		}
		int pageSize=0;//每页的条数
		if(pageSizeStr!=null&&pageSizeStr.length()>0){
			 pageSize=Integer.parseInt(pageSizeStr);
		}else{
			pageSize=5;
		}
		//3，调用service查询pageBean对象
		PageBean<Route> pb=routeService.pageQuery(cid,currentPage,pageSize,rname);
		//4,将pageBean对象序列化为Json，返回
		writeValue(pb,response);
	}
	/**
	 * 根据id查询一个旅游线路的详细信息
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//1.接收id
		String rid = request.getParameter("rid");
		//2.调用service查询route对象
		Route route = routeService.findOne(rid);
		//3.转为json写回客户端
		writeValue(route,response);
	}

	/**
	 * 判断该用户是否收藏改路线
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1，获取线路id
		String rid=request.getParameter("rid");
		//2，获取用户user//从seccsion中读取
		User user=(User) request.getSession().getAttribute("user");
		int uid;
		if(user==null){
			//没有查询到
			uid=0;

		}else{
			//用户已经登录
			uid=user.getUid();
		}
		//3,调用service查询数据库
		boolean flag=favoriteService.isFavorite(rid,uid);
		//4，返回处理数据
		writeValue(flag,response);
	}


	/**
	 * 添加收藏
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1. 获取线路rid
		String rid = request.getParameter("rid");
		//2. 获取当前登录的用户
		User user = (User) request.getSession().getAttribute("user");
		int uid;//用户id
		if(user == null){
			//用户尚未登录
			return ;
		}else{
			//用户已经登录
			uid = user.getUid();
		}


		//3. 调用service添加
		favoriteService.add(rid,uid);

	}
	}




