package shu.ces.accompany.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import shu.ces.accompany.model.User;
import shu.ces.accompany.service.MainPageService;
import shu.ces.accompany.service.MessageService;

import javax.servlet.http.HttpSession;

@Controller
public class MainPage {
    @Autowired
    MainPageService mainPageService;

    @GetMapping(value="/query_user_main_page")
    @ResponseBody
    public User queryUserMainPage(HttpSession httpSession){
        User u=(User) httpSession.getAttribute("currentUser");
        return u;

    }

    @PostMapping(value="/update_user_main_page")
    @ResponseBody

    public int updateUserMainPage(User user){

        return mainPageService.updateMainPage(user);
    }
}
