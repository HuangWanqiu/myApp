package shu.ces.company.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import shu.ces.company.model.User;
import shu.ces.company.service.LoginService;

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
    @ResponseBody

    public String loginVerification( User user, HttpSession httpSession){

        System.out.println("用户邮箱："+user.getEmail());
        System.out.println("用户密码："+user.getUser_password());
        String input_password=user.getUser_password();

        user=loginService.queryPassword(user);


        httpSession.setAttribute("currentUser",user);

        if(!user.getUser_password().equals(input_password))
            return "password is wrong";

        User u=(User) httpSession.getAttribute("currentUser");
        System.out.println(u.getUser_name());
        return "OK";
    }

}
