import { useEffect, useState } from "react";

import "../styles/ReadingHistoryPage.css";

import { ReadingHistory } from "../model/ReadingHistory";

import { getReadingHistory } from "../services/readingHistoryService";

export function ReadingHistoryPage() {

    const [lecturas, setLecturas] =
        useState<ReadingHistory[]>([]);

    const [loading, setLoading] =
        useState(true);

    useEffect(() => {

        async function loadData() {

            try {

                const data =
                    await getReadingHistory(1);

                setLecturas(data);

            } catch (error) {

                console.error(error);

            } finally {

                setLoading(false);

            }
        }

        loadData();

    }, []);

    if (loading) {

        return (
            <div className="history-page">
                <h2>Cargando historial...</h2>
            </div>
        );
    }

    return (

        <div className="history-page">

            <h1 className="history-title">
                Historial de Lecturas
            </h1>

            <p className="history-subtitle">
                Consulta las últimas lecturas registradas por los sensores.
            </p>

            {lecturas.length === 0 ? (

                <div className="empty-state">

                    No existen lecturas registradas.

                </div>

            ) : (

                <div className="history-table-container">

                    <table className="history-table">

                        <thead>

                            <tr>

                                <th>Fecha/Hora</th>

                                <th>Sensor</th>

                                <th>Valor</th>

                                <th>Estado</th>

                            </tr>

                        </thead>

                        <tbody>

                            {lecturas.map(
                                (lectura, index) => (

                                    <tr key={index}>

                                        <td>
                                            {lectura.fechaHora}
                                        </td>

                                        <td>
                                            {lectura.sensor}
                                        </td>

                                        <td>

                                            {lectura.valor}
                                            {" "}
                                            {lectura.unidad}

                                        </td>

                                        <td>

                                            {lectura.estado === "Normal" ? (

                                                <span className="status-normal">
                                                    ✅ Normal
                                                </span>

                                            ) : (

                                                <span className="status-warning">
                                                    ⚠️ Alta
                                                </span>

                                            )}

                                        </td>

                                    </tr>

                                )
                            )}

                        </tbody>

                    </table>

                </div>

            )}

        </div>
    );
}