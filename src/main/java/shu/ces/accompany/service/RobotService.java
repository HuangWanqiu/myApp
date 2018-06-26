package shu.ces.accompany.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import shu.ces.accompany.model.Robot;

@Service
public class RobotService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int addRobot(Robot robot){
        return jdbcTemplate.update("insert into robot(robot_name,robot_setting) values(?,?)",robot.getRobot_name(),robot.getRobot_setting());
    }


    public int updateRobot(Robot robot){

        return jdbcTemplate.update("update robot set robot_name=?,robot_setting=? where robot_id=?",
                new Object[]{robot.getRobot_name(),robot.getRobot_setting(),robot.getRobot_id()});
    }


    public int queryCount(){
        String sql="select count(*) from robot";
        BeanPropertyRowMapper<Robot> robotBeanPropertyRowMapper =new BeanPropertyRowMapper<>(Robot.class);
        int count=jdbcTemplate.queryForObject(sql,Integer.class);
        return count;
    }
}
