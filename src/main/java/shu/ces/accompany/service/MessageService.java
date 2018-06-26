package shu.ces.accompany.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import shu.ces.accompany.model.Robot;
import shu.ces.accompany.model.RobotMessage;
import shu.ces.accompany.model.User;
import shu.ces.accompany.model.UserMessage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int addUserMessage(UserMessage userMessage){

        return jdbcTemplate.update("insert into user_message(user_id,send_time,content,audio_path) values (?,?,?,?)",
                userMessage.getUser_id(),userMessage.getSend_time(),userMessage.getContent(),userMessage.getAudio_path());
    }

    public int addRobotMessage(RobotMessage robotMessage){

        return jdbcTemplate.update("insert into robot_message(user_id,robot_id,send_time,content,audio_path) values (?,?,?,?,?)",
                robotMessage.getUser_id(),robotMessage.getRobot_id(),robotMessage.getSend_time(),robotMessage.getContent(),robotMessage.getAudio_path());
    }


    // query chat history

    public List<UserMessage> queryUserHistoty(User user){
        Object [] params=new Object[]{user.getUser_id()};
        List<UserMessage> list=jdbcTemplate.query("select * from user_message where user_id=?",params,new MessageService.UserMessageMapper());
        System.out.println(list.size());

        return list;
    }

    protected class UserMessageMapper implements RowMapper {
        public UserMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserMessage userMessage=new UserMessage();
            userMessage.setUser_id(rs.getInt("user_id"));
            userMessage.setSend_time(rs.getTimestamp("send_time"));
            userMessage.setContent(rs.getString("content"));
            userMessage.setAudio_path(rs.getString("audio_path"));

            return userMessage;
        }
    }


    public List<RobotMessage> queryRobotHistory(User user, Robot robot) {

        Object[] params = new Object[]{user.getUser_id(), robot.getRobot_id()};
        List<RobotMessage> list = jdbcTemplate.query("select * from robot_message where user_id=? and robot_id=?", params, new BeanPropertyRowMapper<>(RobotMessage.class));
        return list;
    }

}
