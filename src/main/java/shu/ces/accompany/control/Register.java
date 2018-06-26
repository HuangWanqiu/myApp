package shu.ces.accompany.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shu.ces.accompany.model.User;
import shu.ces.accompany.service.RegisterService;
import java.io.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class Register {

    @GetMapping(value = "/register")
    public String register(){
        return "register";
    }

    @Autowired
    private RegisterService registerService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {

                File path = new File(ResourceUtils.getURL("classpath:").getPath());
                if(!path.exists()) path = new File("");

                System.out.println("path:"+path.getAbsolutePath());

                File upload = new File(path.getAbsolutePath(),"static/upload/images");
                if(!upload.exists()) upload.mkdirs();
                System.out.println("upload url:"+upload.getPath());


                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(new File(file.getOriginalFilename())));
                out.write(file.getBytes());
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "upload success," + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "upload failed," + e.getMessage();
            }
            return "上传成功";
        } else {
            return "上传失败，因为文件是空的.";
        }
    }

    @PostMapping(value="/register_add")
    @ResponseBody
    public String handle_register(User user){

        System.out.println(user.getHead_portrait_path());

        user.setHead_portrait_path("tmp");
        //int result=registerService.add(user);
        //System.out.println(result);
        return "OK";
    }




}
