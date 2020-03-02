package cn.icloudit.itfree.controller.home;


import cn.icloudit.itfree.entity.Carousel;
import cn.icloudit.itfree.entity.Comment;
import cn.icloudit.itfree.entity.News;
import cn.icloudit.itfree.service.ICarouselService;
import cn.icloudit.itfree.service.ICommentService;
import cn.icloudit.itfree.service.INewsService;
import cn.icloudit.itfree.service.INews_categoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/home/index")
public class IndexController {

    @Autowired
    private ICarouselService carouselService;
    @Autowired
    private INewsService newsService;
    @Autowired
    private INews_categoryService news_categoryService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ICommentService commentService;

    @RequestMapping("/")
    public Object index(@RequestParam(value = "limit",defaultValue = "10") int limit, @RequestParam(value = "page",defaultValue = "1") int page,News news,
                        Model model){
        if (news.getId() != null){
            News news1 = (News)redisTemplate.opsForValue().get("news_"+ news.getId());
            if (news1 == null){
                news1 = newsService.queryById(news.getId());
                redisTemplate.opsForValue().set("news_" + news.getId(),news1);
            }
            // 阅读数增加
            news1.setViewNumber(news1.getViewNumber() + 1);
            newsService.update(news1);
            redisTemplate.opsForValue().set("news_" + news.getId(),news1);
            redisTemplate.delete("newsList");
            model.addAttribute("nDetail",news1);
            // 评论
            List<Comment> commentList = (List<Comment>)redisTemplate.opsForHash().get("commentList","commentList_" +news1.getId() +"_" + page);
            if (commentList == null || commentList.size() == 0){
                Comment comment = new Comment();
                comment.setNewsId(news1.getId());
                PageHelper.startPage(page,limit);
                commentList = commentService.queryByPager(comment);
                redisTemplate.opsForHash().put("commentList","commentList_" +news1.getId() +"_" + page,commentList);
            }
            // 评论分页
            PageInfo<Comment> pageInfo = new PageInfo<>(commentList);
            model.addAttribute("pager",pageInfo);

        }else {
            List<News> newsList = (List<News>)redisTemplate.opsForHash().get("newsList","newsList"+ page);
            if (newsList == null || newsList.size() == 0){
                PageHelper.startPage(page,limit);
                newsList = newsService.queryByPager(news);
                redisTemplate.opsForHash().put("newsList","newsList"+ page,newsList);
            }
            // 新闻分页
            PageInfo<News> pageInfo = new PageInfo<>(newsList);
            model.addAttribute("pager",pageInfo);
        }

        // 新闻对象参数
        model.addAttribute("n",news);
        // 最前两则新闻
        List<News> list2 = (List<News>)redisTemplate.opsForHash().get("newsList","newsList"+ page);
        List<News> listTwo = new ArrayList<>();
        if (list2 != null && list2.size() >= 1){
            if (list2.size() == 1){
                listTwo.add(list2.get(0));
                listTwo.add(list2.get(0));
            } else {
                listTwo.add(list2.get(0));
                listTwo.add(list2.get(1));
            }
        }
        model.addAttribute("newsShowTwo",listTwo);

        // 热门标签
        model.addAttribute("hotTags",newsService.queryHotTags(6));

        // 幻灯片
        model.addAttribute("carouselList",carouselService.queryAll());
        // 新闻类别
        model.addAttribute("newsCategoryList",news_categoryService.queryAll());
        return "home/index";
    }

}
