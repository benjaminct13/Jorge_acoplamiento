package simulador_invernadero.mqtt;

public class MqttConfig {
    // Configuración global estática
    public static final String BROKER = "tcp://localhost:1883";
    public static final int QOS = 1;

    // Generamos el tópico de envío dinámicamente
    public static String getTopicSensores(String invernaderoId) {
        return "invernadero/" + invernaderoId + "/sensores";
    }

    // Generamos el tópico de escucha dinámicamente
    public static String getTopicComandos(String invernaderoId) {
        return "invernadero/" + invernaderoId + "/comandos";
    }

    // Evitamos que Mosquitto los desconecte dándole un ID único a cada emulador
    public static String getClientId(String invernaderoId) {
        return "emulador-inv-" + invernaderoId;
    }
}