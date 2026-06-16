import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080";

function toBooleanActuatorState(value: unknown): boolean {
  return value === true || String(value).trim().toUpperCase() === "ON";
}

export function connectWebSocket(
  invernaderoId: string | number,
  onActuatorMessage: (data: Record<string, boolean>) => void
) {
  const client = new Client({
    webSocketFactory: () => new SockJS(`${API_BASE_URL}/ws-invernadero`),
    reconnectDelay: 5000,
  });

  client.onConnect = () => {
    console.log("WS conectado. Invernadero:", invernaderoId);
    console.log("Suscrito a:", `/topic/actuadores/${invernaderoId}`);

    client.subscribe(`/topic/actuadores/${invernaderoId}`, (message) => {
      const payload = JSON.parse(message.body);

      console.log("RAW WS ACTUADORES:", payload);

      const actuadoresTraducidos: Record<string, boolean> = {};

      Object.keys(payload).forEach((key) => {
        actuadoresTraducidos[key] = toBooleanActuatorState(payload[key]);
      });

      onActuatorMessage(actuadoresTraducidos);
    });
  };

  client.onStompError = (frame) => {
    console.error("STOMP ERROR:", frame.headers.message, frame.body);
  };

  client.onWebSocketError = (event) => {
    console.error("WS ERROR:", event);
  };

  client.activate();

  return () => {
    void client.deactivate();
  };
}