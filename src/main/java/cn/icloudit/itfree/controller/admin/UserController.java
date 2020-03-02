package cn.icloudit.itfree.controller.admin;


import cn.icloudit.itfree.entity.Log;
import cn.icloudit.itfree.entity.User;
import cn.icloudit.itfree.service.ILogService;
import cn.icloudit.itfree.service.IUserService;
import cn.icloudit.itfree.utils.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private ILogService logService;

    @Value("${upload.dir}")
    private String UPLOAD_DIR;

    @RequestMapping("/login")
    @ResponseBody
    public Object login(User user, HttpSession session, String code){
        User loginUser = null;
        if (user == null){
            return AjaxResult.error("请输入用户名和密码！");
        }
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())){
            return AjaxResult.error("请输入用户名或密码！");
        }
        if (StringUtils.isEmpty(code)){
            return AjaxResult.error("请输入验证码！");
        }
        if (session.getAttribute(RandomValidateCode.RANDOMCODEKEY).toString().toUpperCase().equals(code.toUpperCase())){
            loginUser = userService.login(user);
        } else {
            addLog("用户名为{" + user.getUsername() + "}的验证码输入错误！");
            return AjaxResult.error("验证码输入错误！");
        }
        if (loginUser == null){
            addLog("用户名为{" + user.getUsername() + "},密码为{" + user.getPassword() + "}登录失败！");
            return AjaxResult.error("用户名或密码错误！");
        }
        session.setAttribute("LOGIN_USER",loginUser);
        addLog("用户名为{" + loginUser.getUsername() + "},角色为{" + loginUser.getRole().getName() + "}的用户登录成功！");
        return AjaxResult.success("登录成功！");
    }

    private void addLog(String msg){
        Log log = new Log();
        log.setCreateTime(new Date());
        log.setContent(msg);
        logService.save(log);
    }

    @RequestMapping("/checkCode")
    public void checkCode(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //设置相应类型,告诉浏览器输出的内容为图片
        response.setContentType("image/jpeg");

        //设置响应头信息，告诉浏览器不要缓存此内容
        response.setHeader("pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);

        RandomValidateCode randomValidateCode = new RandomValidateCode();
        try {
            randomValidateCode.getRandcode(request, response);//输出图片方法
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/loginOut")
    public String loginOut(HttpSession session){
        session.removeAttribute("LOGIN_USER");
        return "redirect:/page/admin/login";
    }

    @RequestMapping("/queryAll")
    @ResponseBody
    public List queryall(){
        List<User> list = userService.queryAll();
        return list;
    }

    @RequestMapping("/queryByPager")
    @ResponseBody
    public Object queryByPager(@RequestParam(value = "limit",defaultValue = "2") int limit, @RequestParam(value = "page",defaultValue = "1") int page, User user){
        PageHelper.startPage(page,limit);
        List<User> list = userService.queryByPager(user);
        PageInfo<User> pageInfo = new PageInfo<>(list);
        Map<String, Object> map = new HashMap<>();
        return Layui.loadJsonPage(pageInfo);
    }

    @RequestMapping("/upload_photo")
    @ResponseBody
    public Object upload_photo(MultipartFile file, HttpServletRequest request){
        return UploadImgUtils.uploadImg(file,request, UPLOAD_DIR,"User_photo");
    }


    @RequestMapping("/add")
    @ResponseBody
    public Object add(User user){
        try {
            userService.save(user);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error("添加失败！");
        }
        return AjaxResult.success("添加成功！");
    }

    @RequestMapping("/toUpdate")
    public Object toUpdate(Integer id, Model model){
        User user = userService.queryById(id);
        model.addAttribute("user",user);
        return "admin/user/user-update";
    }

    @RequestMapping("/update")
    @ResponseBody
    public Object update(User User){
        try {
            userService.update(User);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error("修改失败！");
        }
        return AjaxResult.success("修改成功！");
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Object delete(Integer[] ids){
        String msgs = "";
        int okCount = 0;
        for (Integer id:ids) {
            try {
                userService.delete(id);
                okCount++;
            }catch (Exception e){
                User user = userService.queryById(id);
                msgs += "'"+ user.getUsername() +"'存在删除风险,删除失败！\n";
            }
        }
        if (okCount == 0){
            return AjaxResult.error("删除失败！");
        } else if (okCount < ids.length){
            return AjaxResult.success("部分信息删除成功！\n" + msgs);
        } else {
            return AjaxResult.success("删除成功！");
        }
    }
}
