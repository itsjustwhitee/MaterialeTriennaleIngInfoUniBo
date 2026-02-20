import React, { useState } from 'react';
import Visualizzazione from "./Visualizzazione";
import Estrazione from "./Estrazione";
import "./index.css";

const Configurazione = ({ schedina, generaSchedinaVuota, compilaSchedina, finalizzaGiocata }) => {



    return (
        <div className="Configurazione">
            <button onClick={generaSchedinaVuota}>Genera Schedina Vuota</button>
            {schedina.map((numero, index) => (
                <input
                    key={index}
                    type="text"
                    value={numero}
                    onChange={(e) => compilaSchedina(index, e.target.value)}
                />
            ))}
            <button onClick={finalizzaGiocata}>Finalizza Giocata</button>
        </div>
    );


};


export default Configurazione;