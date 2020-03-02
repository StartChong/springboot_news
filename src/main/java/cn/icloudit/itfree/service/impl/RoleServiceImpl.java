package cn.icloudit.itfree.service.impl;

import cn.icloudit.itfree.entity.Role;
import cn.icloudit.itfree.entity.RoleExample;
import cn.icloudit.itfree.mapper.RoleMapper;
import cn.icloudit.itfree.service.IRoleService;
import cn.icloudit.itfree.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> queryAll() {
        return roleMapper.selectByExample(null);
    }

    @Override
    public int save(Role entity) {
        return roleMapper.insert(entity);
    }

    @Override
    public int delete(Object id) {
        return roleMapper.deleteByPrimaryKey((Integer) id);
    }

    @Override
    public Role queryById(Object id) {
        return roleMapper.selectByPrimaryKey((Integer) id);
    }

    @Override
    public int update(Role entity) {
        return roleMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public List<Role> queryByTj(Role entity) {
        return null;
    }

    @Override
    public List<Role> queryByPager(Role entity) {
        RoleExample example = new RoleExample();
        if (entity != null){
            RoleExample.Criteria c = example.createCriteria();
            if (StringUtils.isNotEmpty(entity.getName())){
                c.andNameLike("%"+ entity.getName() +"%");
            }
        }
        return roleMapper.selectByExample(example);
    }
}
