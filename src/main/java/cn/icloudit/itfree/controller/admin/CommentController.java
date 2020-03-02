package cn.icloudit.itfree.controller.admin;


import cn.icloudit.itfree.entity.Comment;
import cn.icloudit.itfree.entity.News;
import cn.icloudit.itfree.service.ICommentService;
import cn.icloudit.itfree.service.INewsService;
import cn.icloudit.itfree.utils.AjaxResult;
import cn.icloudit.itfree.utils.Layui;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("comment")
public class CommentController {

    @Autowired
    private ICommentService commentService;
    @Autowired
    private INewsService newsService;
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/queryAll")
    @ResponseBody
    public List queryall(){
        List<Comment> list = commentService.queryAll();
        return list;
    }

    @RequestMapping("/queryByPager")
    @ResponseBody
    public Object queryByPager(@RequestParam(value = "limit",defaultValue = "2") int limit, @RequestParam(value = "page",defaultValue = "1") int page, Comment comment){
        PageHelper.startPage(page,limit);
        List<Comment> list = commentService.queryByPager(comment);
        PageInfo<Comment> pageInfo = new PageInfo<>(list);
        return Layui.loadJsonPage(pageInfo);
    }


    @RequestMapping("/add")
    @ResponseBody
    public Object add(Comment comment){
        if (comment.getNewsId() == null){
            return AjaxResult.error("请选择要评论的新闻！");
        }
        if (comment.getNickname() == ""){
            return AjaxResult.error("昵称不能为空！");
        }
        if (comment.getContent() == ""){
            return AjaxResult.error("评论不能为空！");
        }
        if (comment.getContent().length() > 500){
            return AjaxResult.error("评论字数不能超过500字");
        }
        try {
            comment.setCreateTime(new Date());
            News news = newsService.queryById(comment.getNewsId());
            news.setCommentNumber(news.getCommentNumber() + 1);
            newsService.update(news);
            redisTemplate.opsForValue().set("news_" + news.getId(),news);
            redisTemplate.delete("newsList");
            redisTemplate.delete("commentList");
            commentService.save(comment);
        }catch (Exception e){
            return AjaxResult.error("添加失败！");
        }
        return AjaxResult.success("添加成功！");
    }

    @RequestMapping("/toUpdate")
    public Object toUpdate(Integer id, Model model){
        Comment comment = commentService.queryById(id);
        model.addAttribute("comment",comment);
        return "admin/comment/comment-update";
    }

    @RequestMapping("/update")
    @ResponseBody
    public Object update(Comment comment){
        try {
            commentService.update(comment);
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
                commentService.delete(id);
                okCount++;
            }catch (Exception e){
                msgs += "评论编号为：'"+ id +"'存在删除风险,删除失败！\n";
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
