import { ReadingHistory } from "../model/ReadingHistory";

export async function getReadingHistory(
    greenhouseId: number
): Promise<ReadingHistory[]> {

    return Promise.resolve([
        {
            fechaHora: "15/06/2026 16:30",
            sensor: "Temperatura",
            valor: 30,
            unidad: "°C",
            estado: "Alta"
        },
        {
            fechaHora: "15/06/2026 16:30",
            sensor: "Humedad",
            valor: 45,
            unidad: "%",
            estado: "Normal"
        }
    ]);
}