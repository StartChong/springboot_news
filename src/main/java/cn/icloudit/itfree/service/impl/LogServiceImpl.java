package cn.icloudit.itfree.service.impl;

import cn.icloudit.itfree.entity.Log;
import cn.icloudit.itfree.entity.LogExample;
import cn.icloudit.itfree.mapper.LogMapper;
import cn.icloudit.itfree.service.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LogServiceImpl implements ILogService {

    @Autowired
    private LogMapper logMapper;

    @Override
    public List<Log> queryAll() {
        return logMapper.selectByExample(null);
    }

    @Override
    public int save(Log entity) {
        return logMapper.insert(entity);
    }

    @Override
    public int delete(Object id) {
        return logMapper.deleteByPrimaryKey((Integer) id);
    }

    @Override
    public Log queryById(Object id) {
        return logMapper.selectByPrimaryKey((Integer) id);
    }

    @Override
    public int update(Log entity) {
        return logMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public List<Log> queryByTj(Log entity) {
        return null;
    }

    @Override
    public List<Log> queryByPager(Log entity) {
        return logMapper.selectByExample(null);
    }

    @Override
    public List<Log> queryByPager(Log log, Date start, Date end) {
        LogExample example = new LogExample();
        if (start != null || end != null || log != null){
            LogExample.Criteria c = example.createCriteria();
            if (start != null && end == null){
                c.andCreateTimeGreaterThanOrEqualTo(start);
            } else if (start == null && end != null){
                c.andCreateTimeLessThanOrEqualTo(end);
            } else if (start != null && end != null){
                c.andCreateTimeBetween(start,end);
            }
        }
        return logMapper.selectByExample(example);
    }
}
