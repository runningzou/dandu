package com.runningzou.dandu.model.entity;

import java.util.List;

/**
 * Created by runningzou on 17-10-28.
 */

public class Result<T> {

    private String status;
    private String msg;
    private int code;
    private T datas;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getDatas() {
        return datas;
    }

    public void setDatas(T datas) {
        this.datas = datas;
    }
}
