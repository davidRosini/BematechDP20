package nm.jvision.conexao.websocket;

import javax.websocket.ClientEndpointConfig;
import java.util.*;

/**
 * @author David
 */
public class AuthorizationConfigurator extends ClientEndpointConfig.Configurator {

    @Override
    public void beforeRequest(final Map<String, List<String>> headers) {
        final Calendar calendar = Calendar.getInstance();
        final int varying = calendar.get(Calendar.DAY_OF_MONTH)
                + calendar.get(Calendar.MONTH)
                + calendar.get(Calendar.YEAR)
                + calendar.get(Calendar.HOUR_OF_DAY)
                + calendar.get(Calendar.MINUTE);
        String value = String.format("%010d", ~new Random(varying).nextInt());
        headers.put("cookie", Collections.singletonList("jvision.login=" + value));
        headers.put("authentication_cookie", Collections.singletonList("true"));
    }
}
