package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {
    private CategoryDao dao=new CategoryDaoImpl();
    @Override
    public List<Category> findAll() {
        //获取jedis的客户端
        Jedis jedis=JedisUtil.getJedis();
        //查询缓存是否存在数据
        Set<Tuple> categorys=jedis.zrangeWithScores("category",0,-1);
        //判断查询的是否为空
        List<Category> cs=null;

        if(categorys==null||categorys.size()==0){
            //缓存内无数据，从数据库进行查询
            cs=dao.findAll();
            //讲数据存到redis中去，key,category
            for(int i=0;i<cs.size();i++){
                jedis.zadd("category",cs.get(i).getCid(),cs.get(i).getCname());
            }

        }else{
            cs=new ArrayList<Category>();
            //将set中的数据存到list中去
            for(Tuple tuple:categorys){
                Category category=new Category();
                category.setCname(tuple.getElement());
                category.setCid((int)tuple.getScore());
                cs.add(category);
            }
        }
        return cs;
    }
}
