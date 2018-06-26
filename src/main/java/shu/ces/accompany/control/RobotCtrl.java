package shu.ces.accompany.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import shu.ces.accompany.model.Robot;
import shu.ces.accompany.service.RobotService;

import javax.servlet.http.HttpSession;

@Controller
public class RobotCtrl {


    @Autowired
    RobotService robotService;

    @GetMapping(value="/robot")
    public String robot(){
        return "robot";
    }


    @PostMapping(value="/add_robot")
    @ResponseBody
    public String addRobot(Robot robot, HttpSession httpSession){

        robotService.addRobot(robot);
        int robot_id=robotService.queryCount();
        robot.setRobot_id(robot_id);

        httpSession.setAttribute("currentRobot",robot);
        System.out.println(robot_id);

        return "Ok";
    }

    @PostMapping(value="/update_robot")
    @ResponseBody
    public String updateRobot(Robot robot,HttpSession httpSession){
        Robot r=(Robot) httpSession.getAttribute("currentRobot");
        int robot_id=r.getRobot_id();
        robot.setRobot_id(robot_id);
        robotService.updateRobot(robot);
        return "OK";
    }

}
