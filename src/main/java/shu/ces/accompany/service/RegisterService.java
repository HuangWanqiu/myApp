package shu.ces.accompany.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import shu.ces.accompany.model.User;

@Service
public class RegisterService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int add(User user){
        user.setHead_portrait_path("tmp");
        System.out.println(user.getUser_name());
        System.out.println(user.getEmail());
        System.out.println(user.getPhone_number());
        System.out.println(user.getUser_password());
        System.out.println(user.getMotto());
        System.out.println(user.getHead_portrait_path());
        return jdbcTemplate.update("insert into user(user_name,user_password,email,phone_number,head_portrait_path) values(?,?,?,?,?)",
                user.getUser_name(),user.getUser_password(),user.getPhone_number(),user.getMotto(),user.getHead_portrait_path());
    }
}
