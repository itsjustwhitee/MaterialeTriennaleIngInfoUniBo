import React, { useState } from 'react';
import Configurazione from "./Configurazione";
import Estrazione from "./Estrazione";
import "./index.css";

const Visualizzazione = ({ schedina, risultato, color, commons }) => {
    console.log("commons " + commons);
    console.log("schedina " + schedina);
    console.log("colore " + color);

    return (
        <div className="Visualizzazione">
            <h2>Visualizzazione Schedina</h2>
            {schedina.map((numero, index) => (

                <div key={index} id={commons.includes(numero) ? color : ""} className={risultato === "Schedina non valida" ? '' : 'Schedina Valida'}>
                    {numero}
                </div>
            ))}
            <p>{risultato}</p>
        </div>
    );
};

export default Visualizzazione;