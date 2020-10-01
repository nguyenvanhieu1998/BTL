package nguyenvanhieu.fithou.hotrovayvon1.Class;

public class chat {
    private int id;
    private int imageChat;
    private String Name;
    private String accountType;

    public chat() {
    }

    public chat(int id, int imageChat, String name, String accountType) {
        this.id = id;
        this.imageChat = imageChat;
        Name = name;
        this.accountType = accountType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImageChat() {
        return imageChat;
    }

    public void setImageChat(int imageChat) {
        this.imageChat = imageChat;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
