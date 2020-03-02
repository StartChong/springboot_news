package cn.icloudit.itfree.service;

import cn.icloudit.itfree.entity.User;

public interface IUserService extends IBaseService<User> {

    User login(User user);
}
