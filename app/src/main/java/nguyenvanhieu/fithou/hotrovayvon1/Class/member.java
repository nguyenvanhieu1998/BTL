package nguyenvanhieu.fithou.hotrovayvon1.Class;

public class member {
    private String name;
    private String accountType;
    private String email;
    private String phone;
    private String address;
    private String uid;
    private String photoURL;
    private String status;
    private String permission;
    public member() {
    }

    public member(String name, String accountType, String email, String phone, String address, String uid, String photoURL, String status, String permission) {
        this.name = name;
        this.accountType = accountType;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.uid = uid;
        this.photoURL = photoURL;
        this.status = status;
        this.permission = permission;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
