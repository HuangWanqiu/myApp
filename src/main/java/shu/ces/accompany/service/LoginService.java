package shu.ces.accompany.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import shu.ces.accompany.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Service
public class LoginService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public User queryPassword(User user){

        Object [] params=new Object[]{user.getEmail()};
        List<User> list=jdbcTemplate.query("select * from user where email=?",params,new UserMapper());
        user=list.get(0);
        System.out.println(list.size());
        System.out.println(user.getUser_name());
        System.out.println(user.getUser_password());

        return user;
    }

    protected class UserMapper implements RowMapper {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user=new User();
            user.setUser_password(rs.getString("user_password"));
            user.setMotto(rs.getString("motto"));
            user.setUser_id(rs.getInt("user_id"));
            user.setEmail(rs.getString("email"));
            user.setPhone_number(rs.getString("phone_number"));
            user.setHead_portrait_path(rs.getString("head_portrait_path"));
            user.setUser_name(rs.getString("user_name"));
            return user;
        }
    }

}
