package cn.icloudit.itfree.controller.admin;


import cn.icloudit.itfree.entity.News_category;
import cn.icloudit.itfree.service.INews_categoryService;
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
@RequestMapping("newsCategory")
public class News_categoryController {

    @Autowired
    private INews_categoryService news_categoryService;

    @RequestMapping("/queryAll")
    @ResponseBody
    public List queryall(){
        List<News_category> list = news_categoryService.queryAll();
        return list;
    }

    @RequestMapping("/queryByPager")
    @ResponseBody
    public Object queryByPager(@RequestParam(value = "limit",defaultValue = "2") int limit, @RequestParam(value = "page",defaultValue = "1") int page, News_category news_category){
        PageHelper.startPage(page,limit);
        List<News_category> list = news_categoryService.queryByPager(news_category);
        PageInfo<News_category> pageInfo = new PageInfo<>(list);
        return Layui.loadJsonPage(pageInfo);
    }


    @RequestMapping("/add")
    @ResponseBody
    public Object add(News_category news_category){
        try {
            news_categoryService.save(news_category);
        }catch (Exception e){
            return AjaxResult.error("添加失败！");
        }
        return AjaxResult.success("添加成功！");
    }

    @RequestMapping("/toUpdate")
    public Object toUpdate(Integer id, Model model){
        News_category news_category = news_categoryService.queryById(id);
        model.addAttribute("newsCategory",news_category);
        return "admin/newsCategory/newsCategory-update";
    }

    @RequestMapping("/update")
    @ResponseBody
    public Object update(News_category news_category){
        try {
            news_categoryService.update(news_category);
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
                news_categoryService.delete(id);
                okCount++;
            }catch (Exception e){
                News_category news = news_categoryService.queryById(id);
                msgs += "'"+ news.getName() +"'存在删除风险,删除失败！\n";
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
