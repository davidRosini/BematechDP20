package nm.jvision.conexao.websocket;

import javax.json.Json;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * @author David
 */
public class WSMessageEncoder implements Encoder.Text<WSMessage> {

    @Override
    public String encode(WSMessage wsMessage) throws EncodeException {

        return Json.createObjectBuilder()
                .add("type", wsMessage.getType().name())
                .add("data", Json.createObjectBuilder()
                        .add("messageOrigin", wsMessage.getData().getMessageOrigin().name())
                        .add("from", wsMessage.getData().getFrom())
                        .add("to", wsMessage.getData().getTo())
                        .add("message", wsMessage.getData().getMessage()).build()).build().toString();
    }

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }
}