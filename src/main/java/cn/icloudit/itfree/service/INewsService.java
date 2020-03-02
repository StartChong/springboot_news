package cn.icloudit.itfree.service;

import cn.icloudit.itfree.entity.News;
import cn.icloudit.itfree.entity.Tags;

import java.util.List;

public interface INewsService extends IBaseService<News> {
    List<Tags> queryHotTags(Integer count);
}
