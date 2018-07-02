package shu.ces.company.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import shu.ces.company.com.baidu.speech.restapi.ttsdemo.TtsMain;
import shu.ces.company.model.Robot;
import shu.ces.company.model.RobotMessage;
import shu.ces.company.model.User;
import shu.ces.company.model.UserMessage;
import shu.ces.company.service.MessageService;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Controller
public class Message {

    private  String currentPath;

    public String getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(String currentPath) {
        this.currentPath = currentPath;
    }

    public static void parse(String source, String target) throws Exception {
        float sampleRate = 16000;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        AudioFormat af = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
        File sourceFile = new File(source);
        FileOutputStream out = new FileOutputStream(new File(target));
        AudioInputStream audioInputStream = new AudioInputStream(new FileInputStream(sourceFile), af, sourceFile.length());
        AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, out);
        audioInputStream.close();
        out.flush();
        out.close();
    }

    public void getVoiceOfXiaoxin(String message,String filePath){


        SpeechSynthesizer mTts= SpeechSynthesizer.createSynthesizer( );

        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoxin");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速，范围0~100
        mTts.setParameter(SpeechConstant.PITCH, "50");//设置语调，范围0~100
        mTts.setParameter(SpeechConstant.VOLUME, "50");//设置音量，范围0~100


        SynthesizeToUriListener synthesizeToUriListener = new SynthesizeToUriListener() {
            @Override
            public void onEvent(int i, int i1, int i2, int i3, Object o, Object o1) {

            }

            //progress为合成进度0~100
            public void onBufferProgress(int progress) {
                if(progress==100)
                    try {
                        parse(filePath+".pcm", filePath+".wav");
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }

            }
            //会话合成完成回调接口
            //uri为合成保存地址，error为错误信息，为null时表示合成会话成功
            public void onSynthesizeCompleted(String uri, SpeechError error) {
                System.out.println(uri);

                if(error!=null) {
                    System.out.println(error.toString());
                    System.out.println("failed");
                }
            }
        };

        mTts.synthesizeToUri(message, filePath+".pcm",synthesizeToUriListener);


    }


    @Autowired
    MessageService messageService;


    @GetMapping(value="/message")
    public String message(){

        return "message";
    }


    public String getRobotAnswer(String message) {
        Process proc;
        String result="";
        try {
            proc = Runtime.getRuntime().exec("C:\\Anaconda3\\python.exe  D:\\github\\src\\main\\java\\shu\\ces\\company\\python\\robot.py  "+message);
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(), "GBK"));
            String line = null;
            while ((line = in.readLine()) != null) {

                System.out.println(line);
                if(line!=null)
                result=line;
            }

            in.close();
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
           e.printStackTrace();

        }

        return result;


    }

    @PostMapping(value = "/text_to_audio")
    @ResponseBody
    public String wordToAudio(@RequestParam("word") String word){
        String audioPath=getCurrentPath();

        return audioPath;
    }




    @PostMapping(value="/handle_message")
    @ResponseBody

    public String handleMessage(@RequestParam("message") String receivedMessage, HttpSession httpSession){

        // get currentUser and currentRobot from session
        String sendMessage="";
        System.out.println("receivedMessage: "+receivedMessage);
        List<String> list=new ArrayList<>();

        if(receivedMessage.indexOf("藏头诗")>0){
            String keys[]={"上海大学","黄婉秋美","小可可爱","移动终端","不忘初心"};
            try {
                FileInputStream inputStream = new FileInputStream("D:\\github\\src\\main\\java\\shu\\ces\\company\\poem\\tibetan_poem.txt");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String str=null;
                while ((str = bufferedReader.readLine()) != null) {
                    System.out.println(str);
                    list.add(str);
                }

                for(int i=0;i<keys.length;i++){

                    if(receivedMessage.indexOf(keys[i])>0){
                        sendMessage=list.get(i);

                        break;
                    }
                }

            }catch (Exception e){

            }


        }else if(receivedMessage.indexOf("诗")>0){
            try {
                FileInputStream inputStream = new FileInputStream("D:\\github\\src\\main\\java\\shu\\ces\\company\\poem\\poem.txt");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String str = null;
                while ((str = bufferedReader.readLine()) != null) {
                    System.out.println(str);
                    list.add(str);
                }

                int num=(int)Math.random()*list.size();
                sendMessage=list.get(num);
                System.out.println(list.size());
                System.out.println("sendMessage: "+sendMessage);

                //close
                inputStream.close();
                bufferedReader.close();
            }catch (IOException e){
                System.out.println(e.getMessage());
            }


        }else{
            sendMessage=getRobotAnswer(receivedMessage);
        }
        System.out.println("sendMessage: "+sendMessage);

        User u=(User) httpSession.getAttribute("currentUser");
        Timestamp timestamp=new Timestamp(System.currentTimeMillis());


        try{
             //get current path
            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            File upload = new File(path.getAbsolutePath(),"static/upload/audio/"+u.getPhone_number());

            if(!upload.exists()) upload.mkdirs();
            System.out.println("upload url:"+upload.getPath());
            long time=timestamp.getTime();

            /*
            String fileName=upload.getPath()+"/"+String.valueOf(time)+".mp3";
            int index=fileName.indexOf("upload");
            String audioPath=fileName.substring(index);
            setCurrentPath(audioPath);
            System.out.println(audioPath);
            (new TtsMain()).run(sendMessage,fileName);
            */
            String fileName=upload.getPath()+"/"+String.valueOf(time);
            getVoiceOfXiaoxin(sendMessage,fileName);
            int index=fileName.indexOf("upload");
            String audioPath=fileName.substring(index);
            setCurrentPath(audioPath+".wav");
            System.out.println(audioPath+".wav");

        }catch (Exception e)
        {
            System.out.println("出错了");
            System.out.println(e.getStackTrace());
        }



        // save user message


        UserMessage userMessage=new UserMessage();
        userMessage.setUser_id(u.getUser_id());
        userMessage.setSend_time(timestamp);
        userMessage.setContent(receivedMessage);
        userMessage.setAudio_path("null");
        messageService.addUserMessage(userMessage);

        // save robot message



        Robot r=(Robot) httpSession.getAttribute("currentRobot");
        RobotMessage robotMessage=new RobotMessage();
        robotMessage.setUser_id(u.getUser_id());
        robotMessage.setRobot_id(r.getRobot_id());
        robotMessage.setSend_time(timestamp);
        robotMessage.setAudio_path(getCurrentPath());
        robotMessage.setContent(sendMessage);
        messageService.addRobotMessage(robotMessage);


        return sendMessage;
    }


    @GetMapping(value="/query_user_history")
    @ResponseBody
    public List<UserMessage> getUserHistory(HttpSession httpSession){

        User u=(User) httpSession.getAttribute("currentUser");
        return messageService.queryUserHistoty(u);

    }

    @GetMapping(value="/query_robot_history")
    @ResponseBody

    public List<RobotMessage> getRobotHistory(HttpSession httpSession){

        Robot r=(Robot) httpSession.getAttribute("currentRobot");
        User u=(User) httpSession.getAttribute("currentUser");
        return messageService.queryRobotHistory(u,r);

    }


}
