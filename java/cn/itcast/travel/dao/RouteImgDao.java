package cn.itcast.travel.dao;

import cn.itcast.travel.domain.RouteImg;

import java.util.List;

public interface RouteImgDao {
    /**
     * 依据rid查询图片列表
     * @param rid
     * @return
     */
    public List<RouteImg> findByRid(int rid);
}
