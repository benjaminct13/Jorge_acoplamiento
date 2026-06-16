/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simulador_invernadero.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import simulador_invernadero.communication.CommandListener;

/**
 *
 * @author erick
 */
public class MqttSubscriber {

    private final MqttClient client;

    public MqttSubscriber(String invernaderoId, CommandListener listener) throws Exception {

        String clientId = "sub-" + MqttConfig.getClientId(invernaderoId);
        String topicComandos = MqttConfig.getTopicComandos(invernaderoId);

        client = new MqttClient(MqttConfig.BROKER, clientId);

        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);

        client.connect(options);

        // Suscribirse al tópico dinámico
        client.subscribe(topicComandos, (topic, message) -> {
            String payload = new String(message.getPayload());
            listener.onMessage(payload);
        });

        System.out.println("[MQTT] Escuchando comandos en: " + topicComandos);
    }
}
