/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simulador_invernadero.service;

import java.util.List;
import simulador_invernadero.actuadores.ActuadorManager;
import simulador_invernadero.communication.CommandHandler;
import simulador_invernadero.dto.ComandoDTO;
import simulador_invernadero.dto.LecturaDTO;
import simulador_invernadero.model.Clima;
import simulador_invernadero.mqtt.MqttPublisher;
import simulador_invernadero.qeue.CommandQueue;

/**
 *
 * @author Ariadna
 */
public class SimulacionService {
    private final Clima clima;

    private volatile boolean activa = false;

    private final SensorService sensorService;

    private final MqttPublisher publisher;

    private final CommandQueue queue;

    private final CommandHandler handler;

    private final ActuadorManager manager;

    public SimulacionService(
            Clima clima,
            SensorService sensorService,
            MqttPublisher publisher,
            CommandQueue queue,
            CommandHandler handler,
            ActuadorManager manager
    ) {

        this.clima = clima;
        this.sensorService = sensorService;
        this.publisher = publisher;
        this.queue = queue;
        this.handler = handler;
        this.manager = manager;
    }

    public void iniciar() {

    while (true) {

        try {

            procesarComandos();

            if (!activa) {
                Thread.sleep(100);
                continue;
            }

            clima.evolucionar();

            clima.aplicarActuadores(manager);

            List<LecturaDTO> lecturas =
                    sensorService.leerSensores();

            for (LecturaDTO lectura : lecturas) {
                publisher.publish(lectura);
            }

            mostrarEstado();

            Thread.sleep(2000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

    private void procesarComandos() {

    while (queue.tieneComandos()) {

        ComandoDTO comando = queue.obtener();

        if ("SIMULACION".equalsIgnoreCase(comando.getActuador())) {

            if ("INICIAR".equalsIgnoreCase(comando.getAccion())) {

                iniciarSimulacion();

                System.out.println("Simulación iniciada");
            }

            else if ("DETENER".equalsIgnoreCase(comando.getAccion())) {

                detenerSimulacion();

                System.out.println("Simulación detenida");
            }

            continue;
        }

        handler.procesar(comando);
    }
}

    private void mostrarEstado() {

        System.out.println(
                "\n========== ESTADO =========="
        );

        System.out.println(
                "Temperatura: "
                        + clima.getTemperatura()
        );

        System.out.println(
                "Humedad: "
                        + clima.getHumedad()
        );

        System.out.println(
                "Luminosidad: "
                        + clima.getLuz()
        );

        System.out.println(
                "CO2: "
                        + clima.getCo2()
        );
    }

    public void iniciarSimulacion() {
    activa = true;
}

public void detenerSimulacion() {
    activa = false;
}

public boolean isActiva() {
    return activa;
}

}