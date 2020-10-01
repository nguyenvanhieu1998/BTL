package nguyenvanhieu.fithou.hotrovayvon1.Class;

import java.io.Serializable;

public class baiDang implements Serializable {
    private String id;
    private String name;
    private String imageMemberWrite;
    private String hinhThuc;
    private String content;
    private String money;
    private String laiSuat;
    private String thoiHan;
    private String dateWrite;
    private String uid;
    private int check;
    private int notified;
    public baiDang() {
    }

    public baiDang(String id, String name, String imageMemberWrite, String hinhThuc, String content, String money, String laiSuat, String thoiHan, String dateWrite, String uid, int check,int notified) {
        this.id = id;
        this.name = name;
        this.imageMemberWrite = imageMemberWrite;
        this.hinhThuc = hinhThuc;
        this.content = content;
        this.money = money;
        this.laiSuat = laiSuat;
        this.thoiHan = thoiHan;
        this.dateWrite = dateWrite;
        this.uid = uid;
        this.check = check;
        this.notified = notified;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageMemberWrite() {
        return imageMemberWrite;
    }

    public void setImageMemberWrite(String imageMemberWrite) {
        this.imageMemberWrite = imageMemberWrite;
    }

    public String getHinhThuc() {
        return hinhThuc;
    }

    public void setHinhThuc(String hinhThuc) {
        this.hinhThuc = hinhThuc;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateWrite() {
        return dateWrite;
    }

    public void setDateWrite(String dateWrite) {
        this.dateWrite = dateWrite;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getLaiSuat() {
        return laiSuat;
    }

    public void setLaiSuat(String laiSuat) {
        this.laiSuat = laiSuat;
    }

    public String getThoiHan() {
        return thoiHan;
    }

    public void setThoiHan(String thoiHan) {
        this.thoiHan = thoiHan;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public int getNotified() {
        return notified;
    }

    public void setNotified(int notified) {
        this.notified = notified;
    }
}
