package cn.icloudit.itfree.service.impl;

import cn.icloudit.itfree.entity.User;
import cn.icloudit.itfree.entity.UserExample;
import cn.icloudit.itfree.mapper.UserMapper;
import cn.icloudit.itfree.service.IUserService;
import cn.icloudit.itfree.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public List<User> queryAll() {
        return userMapper.selectByExample(null);
    }

    @Override
    public int save(User entity) {
        return userMapper.insert(entity);
    }

    @Override
    public int delete(Object id) {
        return userMapper.deleteByPrimaryKey((Integer) id);
    }

    @Override
    public User queryById(Object id) {
        return userMapper.selectByPrimaryKey((Integer) id);
    }

    @Override
    public int update(User entity) {
        return userMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public List<User> queryByTj(User entity) {
        UserExample example = new UserExample();
        if (entity != null){
            UserExample.Criteria c = example.createCriteria();
            if (StringUtils.isNotEmpty(entity.getUsername())){
                c.andUsernameEqualTo(entity.getUsername());
            }
        }
        return userMapper.selectByExample(example);
    }

    @Override
    public List<User> queryByPager(User entity) {
        UserExample example = new UserExample();
        if (entity != null){
            UserExample.Criteria c = example.createCriteria();
            if (StringUtils.isNotEmpty(entity.getUsername())){
                c.andUsernameLike("%"+ entity.getUsername() +"%");
            }
        }
        return userMapper.selectByExample(example);
    }

    @Override
    public User login(User user) {
        UserExample example = new UserExample();
        if (user != null){
            UserExample.Criteria c = example.createCriteria();
            if (StringUtils.isNotEmpty(user.getUsername()) && StringUtils.isNotEmpty(user.getPassword())){
                c.andUsernameEqualTo(user.getUsername());
                c.andPasswordEqualTo(user.getPassword());
                List<User> users = userMapper.selectByExample(example);
                return users.size() > 0?users.get(0):null;
            }
        }
        return null;
    }
}
