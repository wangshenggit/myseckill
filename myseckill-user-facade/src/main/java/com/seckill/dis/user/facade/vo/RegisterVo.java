package com.seckill.dis.user.facade.vo;

import com.sun.istack.internal.NotNull;

import java.io.Serializable;

/**
 * 注册参数
 *
 * @author noodle
 */
public class RegisterVo implements Serializable {

    private Long phone;
    private String nickname;

    private String head;
    private String password;

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "RegisterVo{" +
                "phone=" + phone +
                ", nickname='" + nickname + '\'' +
                ", head='" + head + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
