package cn.icloudit.itfree.service.impl;

import cn.icloudit.itfree.entity.NewsExample;
import cn.icloudit.itfree.entity.Tags;
import cn.icloudit.itfree.entity.TempletJson;
import cn.icloudit.itfree.mapper.NewsMapper;
import cn.icloudit.itfree.entity.News;
import cn.icloudit.itfree.service.INewsService;
import cn.icloudit.itfree.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServiceImpl implements INewsService {

    @Autowired
    private NewsMapper newsMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<News> queryAll() {
        return newsMapper.selectByExampleWithBLOBs(null);
    }

    @Override
    public int save(News entity) {
        return newsMapper.insert(entity);
    }

    @Override
    public int delete(Object id) {
        return newsMapper.deleteByPrimaryKey((Integer) id);
    }

    @Override
    public News queryById(Object id) {
        return newsMapper.selectByPrimaryKey((Integer) id);
    }

    @Override
    public int update(News entity) {
        return newsMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public List<News> queryByTj(News entity) {
        return null;
    }

    @Override
    public List<News> queryByPager(News entity) {
        NewsExample example = new NewsExample();
        if (entity != null){
            NewsExample.Criteria c = example.createCriteria();
            if (StringUtils.isNotEmpty(entity.getTitle())){
                c.andTitleLike("%"+ entity.getTitle() +"%");
            }
            if (entity.getCategoryId() != null && entity.getCategoryId() != 0){
                c.andCategoryIdEqualTo(entity.getCategoryId());
            }
            if (StringUtils.isNotEmpty(entity.getTags())){
                c.andTagsEqualTo(entity.getTags());
            }
        }
        example.setOrderByClause("createTime DESC,id ASC");
        return newsMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public List<Tags> queryHotTags(Integer count) {
        return newsMapper.selectHotTags(count);
    }
}
