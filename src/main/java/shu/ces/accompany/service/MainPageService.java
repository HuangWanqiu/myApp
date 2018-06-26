package shu.ces.accompany.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import shu.ces.accompany.model.User;

@Service
public class MainPageService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int updateMainPage(User user){

        return jdbcTemplate.update("update user set user_name=?,email=?,phone_number=?,motto=?,user_password=?," +
                "head_portrait_path=? where user_id=?",new Object[]{user.getUser_name(),user.getEmail(),user.getPhone_number(),
                user.getMotto(),user.getUser_password(),user.getHead_portrait_path(),user.getUser_id()});
    }

}
