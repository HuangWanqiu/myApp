package shu.ces.company.model;

import java.sql.Timestamp;

public class RobotMessage {
    private int user_id;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getRobot_id() {
        return robot_id;
    }

    public void setRobot_id(int robot_id) {
        this.robot_id = robot_id;
    }

    public Timestamp getSend_time() {
        return send_time;
    }

    public void setSend_time(Timestamp send_time) {
        this.send_time = send_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAudio_path() {
        return audio_path;
    }

    public void setAudio_path(String audio_path) {
        this.audio_path = audio_path;
    }

    private int robot_id;
    private Timestamp send_time;
    private String content;
    private String audio_path;

}
