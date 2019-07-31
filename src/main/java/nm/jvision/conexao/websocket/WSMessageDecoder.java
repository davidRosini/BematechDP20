package nm.jvision.conexao.websocket;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.StringReader;

/**
 * @author David
 */
public class WSMessageDecoder implements Decoder.Text<WSMessage> {

    @Override
    public WSMessage decode(String textMessage) throws DecodeException {

        JsonObject obj = Json.createReader(new StringReader(textMessage)).readObject();
        JsonObject data = obj.getJsonObject("data");

        WSMessage.MessageType type = WSMessage.MessageType.valueOf(obj.getString("type"));
        WSMessage.MessageOrigin origin = WSMessage.MessageOrigin.valueOf(data.getString("messageOrigin"));

        return new WSMessage(type, origin, data.getString("from"), data.getString("to"), data.getString("message"));
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }
}