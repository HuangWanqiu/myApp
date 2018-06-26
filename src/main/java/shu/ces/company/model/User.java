package shu.ces.company.model;


public class User {
    private int user_id;
    private String user_name;
    private String email;
    private String phone_number;
    private String user_password ;
    private String motto;
    private String head_portrait_path;


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public String getHead_portrait_path() {
        return head_portrait_path;
    }

    public void setHead_portrait_path(String head_portrait_path) {
        this.head_portrait_path = head_portrait_path;
    }

}
