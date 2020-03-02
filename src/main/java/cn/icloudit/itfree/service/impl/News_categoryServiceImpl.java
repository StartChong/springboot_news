package cn.icloudit.itfree.service.impl;

import cn.icloudit.itfree.entity.News_categoryExample;
import cn.icloudit.itfree.mapper.News_categoryMapper;
import cn.icloudit.itfree.entity.News_category;
import cn.icloudit.itfree.service.INews_categoryService;
import cn.icloudit.itfree.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class News_categoryServiceImpl implements INews_categoryService {

    @Autowired
    private News_categoryMapper news_categoryMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<News_category> queryAll() {
        List<News_category> news_categories = (List<News_category>)redisTemplate.opsForValue().get("News_categoryList");
        if (news_categories == null){
            News_categoryExample example = new News_categoryExample();
            example.setOrderByClause("sort ASC,id ASC");
            news_categories = news_categoryMapper.selectByExample(example);
            redisTemplate.opsForValue().set("News_categoryList",news_categories);
        }
        return news_categories;
    }

    @Override
    public int save(News_category entity) {
        return news_categoryMapper.insert(entity);
    }

    @Override
    public int delete(Object id) {
        return news_categoryMapper.deleteByPrimaryKey((Integer)id);
    }

    @Override
    public News_category queryById(Object id) {
        return news_categoryMapper.selectByPrimaryKey((Integer)id);
    }

    @Override
    public int update(News_category entity) {
        return news_categoryMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public List<News_category> queryByTj(News_category entity) {
        return null;
    }

    @Override
    public List<News_category> queryByPager(News_category entity) {
        News_categoryExample example = new News_categoryExample();
        if (entity != null){
            if (StringUtils.isNotEmpty(entity.getName())){
                News_categoryExample.Criteria c = example.createCriteria();
                c.andNameLike("%"+ entity.getName() +"%");
            }
        }
        example.setOrderByClause("sort ASC,id ASC");
        return news_categoryMapper.selectByExample(example);
    }
}
