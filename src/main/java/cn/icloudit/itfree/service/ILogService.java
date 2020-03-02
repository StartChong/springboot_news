package cn.icloudit.itfree.service;

import cn.icloudit.itfree.entity.Log;

import java.util.Date;
import java.util.List;

public interface ILogService extends IBaseService<Log> {
    List<Log> queryByPager(Log log, Date start, Date end);
}
