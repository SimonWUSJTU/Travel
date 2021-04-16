package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;

public interface FavoriteDao {
    public Favorite findByRidAndUid(int parseInt, int uid);

    public int findCountByRid(int rid);

    public void add(int rid,int uid);
}
