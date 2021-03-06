package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteDao {
    /**
     * 通过cid查询总条数
     * @param cid
     * @return
     */
    public int findTotalCount(int cid,String rname);

    /**
     * 查询当前页的数据集合
     * @param cid
     * @param start
     * @param pageSize
     * @return
     */
    public List<Route> findByPage(int cid, int start, int pageSize,String rname);

    /**
     * 依靠rid查询项目信息
     * @param rid
     * @return
     */
    public Route findOne(int rid);
}
