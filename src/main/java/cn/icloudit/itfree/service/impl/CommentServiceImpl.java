package cn.icloudit.itfree.service.impl;

import cn.icloudit.itfree.entity.Comment;
import cn.icloudit.itfree.entity.CommentExample;
import cn.icloudit.itfree.mapper.CommentMapper;
import cn.icloudit.itfree.service.ICommentService;
import cn.icloudit.itfree.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements ICommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comment> queryAll() {
        return commentMapper.selectByExample(null);
    }

    @Override
    public int save(Comment entity) {
        return commentMapper.insert(entity);
    }

    @Override
    public int delete(Object id) {
        return commentMapper.deleteByPrimaryKey((Integer) id);
    }

    @Override
    public Comment queryById(Object id) {
        return commentMapper.selectByPrimaryKey((Integer) id);
    }

    @Override
    public int update(Comment entity) {
        return commentMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public List<Comment> queryByTj(Comment entity) {
        return null;
    }

    @Override
    public List<Comment> queryByPager(Comment entity) {
        CommentExample example = new CommentExample();
        if (entity != null){
            CommentExample.Criteria c = example.createCriteria();
            if (StringUtils.isNotEmpty(entity.getNewsId())){
                if (entity.getNewsId() != 0){
                    c.andNewsIdEqualTo(entity.getNewsId());
                }
            }
            if (StringUtils.isNotEmpty(entity.getContent())){
                c.andContentLike("%"+ entity.getContent() +"%");
            }
            if (StringUtils.isNotEmpty(entity.getNickname())){
                c.andNicknameLike("%"+ entity.getNickname() +"%");
            }
        }
        example.setOrderByClause("createTime desc,id desc");
        return commentMapper.selectByExample(example);
    }
}
