package ch.hevs.cloudio.example.iobox.tools;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.security.KeyStore;

public class LedSwitcher {
    public static void main(String... args) throws Exception {
        if (args.length == 1) {
            String payload = "on".equals(args[0].toLowerCase()) ? "{true" : "{false";
            new LedSwitcher().changeLed(payload);
        }
    }

    private void changeLed(String value) throws Exception {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setSocketFactory(createSocketFactory());

        MqttClient client = new MqttClient("ssl://213.221.143.116:8883", "control");
        client.connect(options);
        client.publish("@set/Development1/nodes/io/objects/outputs/attributes/led0", value.getBytes(), 0, false);
        client.disconnect();
    }

    private SSLSocketFactory createSocketFactory() throws Exception {
        KeyStore endpointKeyCertStore = KeyStore.getInstance("PKCS12");
        endpointKeyCertStore.load(ResourceLoader.getResource("classpath:cloud.io/Development1.p12", this),
            "".toCharArray());

        KeyManagerFactory endpointKeyCertManagerFactory =
            KeyManagerFactory.getInstance("SunX509");
        endpointKeyCertManagerFactory.init(endpointKeyCertStore, "".toCharArray());

        // Authority certificate in JKS format.
        KeyStore authorityKeyStore = KeyStore.getInstance("JKS");
        authorityKeyStore.load(ResourceLoader.getResource("classpath:cloud.io/authority.jks", this),
            "cloudio".toCharArray());

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        trustManagerFactory.init(authorityKeyStore);

        // Create SSL Context.
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(endpointKeyCertManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

        return sslContext.getSocketFactory();
    }
}
