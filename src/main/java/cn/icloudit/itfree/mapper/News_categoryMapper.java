package cn.icloudit.itfree.mapper;

import cn.icloudit.itfree.entity.News_category;
import cn.icloudit.itfree.entity.News_categoryExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface News_categoryMapper {
    int countByExample(News_categoryExample example);

    int deleteByExample(News_categoryExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(News_category record);

    int insertSelective(News_category record);

    List<News_category> selectByExample(News_categoryExample example);

    News_category selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") News_category record, @Param("example") News_categoryExample example);

    int updateByExample(@Param("record") News_category record, @Param("example") News_categoryExample example);

    int updateByPrimaryKeySelective(News_category record);

    int updateByPrimaryKey(News_category record);
}