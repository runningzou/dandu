package com.runningzou.dandu.model.entity;

/**
 * Created by runningzou on 17-10-28.
 */

public class GetListParameter {

    private String c;
    private String a;
    private int page;
    private int model;
    private String pageId;
    private String createTime;
    private String client;
    private String version;
    private String time;
    private String deviceId ;
    private int show_sdv;

    public GetListParameter() {
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getShow_sdv() {
        return show_sdv;
    }

    public void setShow_sdv(int show_sdv) {
        this.show_sdv = show_sdv;
    }
}
