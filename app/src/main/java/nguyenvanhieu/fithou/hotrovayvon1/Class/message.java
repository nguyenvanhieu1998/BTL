package nguyenvanhieu.fithou.hotrovayvon1.Class;

public class message {
    public String sender;
    public String receiver;
    public String sendMessage;
    public String type;
    public int isSeen;

    public message() {
    }

    public message(String sender, String receiver, String sendMessage, String type, int isSeen) {
        this.sender = sender;
        this.receiver = receiver;
        this.sendMessage = sendMessage;
        this.type = type;
        this.isSeen = isSeen;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(String sendMessage) {
        this.sendMessage = sendMessage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(int isSeen) {
        this.isSeen = isSeen;
    }
}
