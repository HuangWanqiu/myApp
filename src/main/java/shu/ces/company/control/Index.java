package shu.ces.company.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller

public class Index {

   @RequestMapping("/")
    public String index() {
        initVoiceSdk();
        return "index";
    }

    public void  initVoiceSdk(){

        SpeechUtility.createUtility( SpeechConstant.APPID +"=5b2f3c48");
    }


    @GetMapping("/main")
    public String main() {
        return "main";
    }




}
