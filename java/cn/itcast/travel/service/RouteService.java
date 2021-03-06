package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

public interface RouteService {
   /**
    * 根据类别进行分页查询
    * @param cid
    * @param currentPage
    * @param pageSize
    * @return
    */
   public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize,String rname);

   /**
    * 根据id查询路线详细信息
    * @param rid
    * @return
    */
   public Route findOne(String rid);
}
