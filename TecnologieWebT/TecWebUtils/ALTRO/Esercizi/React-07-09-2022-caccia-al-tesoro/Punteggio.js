import React from 'react';


const Punteggio = ({ tentativi, punteggio }) => {
    return (
        <div>
            <p>Tentativi: {tentativi}</p>
            {punteggio !== null && <p>Punteggio: {punteggio} punti </p>}
        </div>
    );
};

export default Punteggio;