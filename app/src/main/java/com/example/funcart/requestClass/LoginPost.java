package com.example.funcart.requestClass;

/**
 * Created by mario on 29/05/17.
 */

public class LoginPost {

    private String emailOrPhonenumber;
    private String password;

    public LoginPost(){

    }

    public LoginPost(String emailOrPhonenumber, String password) {
        this.emailOrPhonenumber = emailOrPhonenumber;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmailOrPhonenumber() {
        return emailOrPhonenumber;
    }
    public void setEmailOrPhonenumber(String emailOrPhonenumber) {
        this.emailOrPhonenumber = emailOrPhonenumber;
    }
}
