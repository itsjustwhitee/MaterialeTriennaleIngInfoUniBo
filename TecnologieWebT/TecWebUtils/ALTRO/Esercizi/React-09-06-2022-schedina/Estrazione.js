import React from 'react';
import Configurazione from "./Configurazione";
import Visualizzazioni from "./Visualizzazione";
import "./index.css";

const Estrazione = ({ numeriEstratti, estraiNumeri }) => {
    return (
        <div className="Estrazione">
            <h2>Estrazione Numeri</h2>
            <button onClick={estraiNumeri}>Estrai Numeri</button>
            {numeriEstratti.map((numero, index) => (
                <div key={index}>{numero}</div>
            ))}
        </div>
    );
};

export default Estrazione;
