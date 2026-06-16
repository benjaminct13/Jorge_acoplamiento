/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simulador_invernadero.mqtt;

/**
 *
 * @author erick
 */
import org.eclipse.paho.client.mqttv3.*;
import simulador_invernadero.dto.LecturaDTO;

public class MqttPublisher {

    private final MqttClient client;
    private final String topicSensores;

    public MqttPublisher(String invernaderoId) throws MqttException {
        // Generar IDs y Tópicos dinámicos
        String clientId = MqttConfig.getClientId(invernaderoId);
        this.topicSensores = MqttConfig.getTopicSensores(invernaderoId);

        client = new MqttClient(MqttConfig.BROKER, clientId);

        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);

        client.connect(options);
        System.out.println("[MQTT] Conectado al broker como: " + clientId);
    }

    public void publish(LecturaDTO lectura) {
        try {
            String payload = lectura.toString();
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(MqttConfig.QOS);

            // Publicar en el tópico dinámico
            client.publish(this.topicSensores, message);
            System.out.println("[MQTT] Publicado en " + this.topicSensores + " -> " + payload);
        } catch (Exception e) {
            System.out.println("[MQTT] Error publicando");
            e.printStackTrace();
        }
    }
    // ... resto de tu código (disconnect) ...
}
