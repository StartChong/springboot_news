package cn.icloudit.itfree.service.impl;

import cn.icloudit.itfree.entity.Carousel;
import cn.icloudit.itfree.entity.CarouselExample;
import cn.icloudit.itfree.mapper.CarouselMapper;
import cn.icloudit.itfree.service.ICarouselService;
import cn.icloudit.itfree.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarouselServiceImpl implements ICarouselService {

    @Autowired
    private CarouselMapper carouselMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<Carousel> queryAll() {
        List<Carousel> carousels = (List<Carousel>)redisTemplate.opsForValue().get("carouselList");
        if (carousels != null){
            return carousels;
        }else {
            CarouselExample example = new CarouselExample();
            CarouselExample.Criteria c = example.createCriteria();
            c.andStatusEqualTo(0);
            example.setOrderByClause("sort ASC,id ASC");
            carousels = carouselMapper.selectByExample(example);
            redisTemplate.opsForValue().set("carouselList",carousels);
            return carousels;
        }
    }

    @Override
    public int save(Carousel entity) {
        return carouselMapper.insert(entity);
    }

    @Override
    public int delete(Object id) {
        return carouselMapper.deleteByPrimaryKey((Integer) id);
    }

    @Override
    public Carousel queryById(Object id) {
        return carouselMapper.selectByPrimaryKey((Integer) id);
    }

    @Override
    public int update(Carousel entity) {
        return carouselMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public List<Carousel> queryByTj(Carousel entity) {
        return null;
    }

    @Override
    public List<Carousel> queryByPager(Carousel entity) {
        CarouselExample example = new CarouselExample();
        example.setOrderByClause("sort ASC,id ASC");
        if (entity != null){
            CarouselExample.Criteria c = example.createCriteria();
            if (StringUtils.isNotEmpty(entity.getTitle())){
                c.andTitleLike("%"+ entity.getTitle() +"%");
            }
        }
        return carouselMapper.selectByExample(example);
    }
}
