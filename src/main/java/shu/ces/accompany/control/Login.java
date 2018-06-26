package shu.ces.accompany.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import shu.ces.accompany.model.User;
import shu.ces.accompany.service.LoginService;

import javax.servlet.http.HttpSession;


@Controller
public class Login {

    @Autowired
    LoginService loginService;

    @GetMapping(value = "/login")
    public String login(){

        return "login";
    }

    @PostMapping(value="/login_verification")
    public String login_verification(User user, HttpSession httpSession){

        System.out.println(user.getEmail());

        System.out.println(user.getUser_password());
        String input_password=user.getUser_password();

        user=loginService.queryPassword(user);


        httpSession.setAttribute("currentUser",user);

        if(!user.getUser_password().equals(input_password))
            System.out.println("password is wrong");

        User u=(User) httpSession.getAttribute("currentUser");
        System.out.println(u.getUser_name());



        return "redirect:message";
    }

}
