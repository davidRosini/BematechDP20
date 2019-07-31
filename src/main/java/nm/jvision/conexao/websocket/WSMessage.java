package nm.jvision.conexao.websocket;

/**
 * @author David
 */
public class WSMessage {

    public static final String SYSTEM_USER = "system";
    private final MessageType type;
    private final Data data;

    public WSMessage(MessageType messageType, MessageOrigin messageOrigin, String from, String to, String message) {
        this.type = messageType;
        this.data = new Data(messageOrigin, from, to, message);
    }

    public MessageType getType() {
        return type;
    }

    public Data getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof WSMessage)) {
            return false;
        }

        WSMessage wsMessage = (WSMessage) o;

        if (data != null ? !data.equals(wsMessage.data) : wsMessage.data != null) {
            return false;
        }

        if (type != wsMessage.type) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    public enum MessageOrigin {
        SYSTEM,
        USER
    }

    public enum MessageType {
        LOGIN,
        LOGOUT,
        MESSAGE,
        MODULE_DEPLOY,
        MODULE_UNDEPLOY,
        ERROR,
        NOTIFICATION
    }

    public static class Data {

        private final MessageOrigin messageOrigin;
        private final String from;
        private final String to;
        private final String message;

        public Data(MessageOrigin messageOrigin, String from, String to, String message) {
            this.messageOrigin = messageOrigin;
            this.from = from;
            this.to = to;
            this.message = message;
        }

        public MessageOrigin getMessageOrigin() {
            return messageOrigin;
        }

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Data)) {
                return false;
            }

            Data data = (Data) o;

            if (from != null ? !from.equals(data.from) : data.from != null) {
                return false;
            }
            if (message != null ? !message.equals(data.message) : data.message != null) {
                return false;
            }
            if (messageOrigin != data.messageOrigin) {
                return false;
            }
            if (to != null ? !to.equals(data.to) : data.to != null) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = messageOrigin != null ? messageOrigin.hashCode() : 0;
            result = 31 * result + (from != null ? from.hashCode() : 0);
            result = 31 * result + (to != null ? to.hashCode() : 0);
            result = 31 * result + (message != null ? message.hashCode() : 0);
            return result;
        }

    }
}