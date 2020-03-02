package cn.icloudit.itfree.controller.admin;


import cn.icloudit.itfree.entity.Role;
import cn.icloudit.itfree.service.IRoleService;
import cn.icloudit.itfree.utils.AjaxResult;
import cn.icloudit.itfree.utils.Layui;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @RequestMapping("/queryAll")
    @ResponseBody
    public List queryall(){
        List<Role> list = roleService.queryAll();
        return list;
    }

    @RequestMapping("/queryByPager")
    @ResponseBody
    public Object queryByPager(@RequestParam(value = "limit",defaultValue = "2") int limit, @RequestParam(value = "page",defaultValue = "1") int page, Role role){
        PageHelper.startPage(page,limit);
        List<Role> list = roleService.queryByPager(role);
        PageInfo<Role> pageInfo = new PageInfo<>(list);
        return Layui.loadJsonPage(pageInfo);
    }


    @RequestMapping("/add")
    @ResponseBody
    public Object add(Role role){
        try {
            roleService.save(role);
        }catch (Exception e){
            return AjaxResult.error("添加失败！");
        }
        return AjaxResult.success("添加成功！");
    }

    @RequestMapping("/toUpdate")
    public Object toUpdate(Integer id, Model model){
        Role role = roleService.queryById(id);
        model.addAttribute("role",role);
        return "admin/role/role-update";
    }

    @RequestMapping("/update")
    @ResponseBody
    public Object update(Role role){
        try {
            roleService.update(role);
        }catch (Exception e){
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
                roleService.delete(id);
                okCount++;
            }catch (Exception e){
                Role role = roleService.queryById(id);
                msgs += "'"+ role.getName() +"'存在删除风险,删除失败！\n";
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
