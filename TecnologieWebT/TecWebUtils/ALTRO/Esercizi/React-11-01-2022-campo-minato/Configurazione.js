import React, { useState } from 'react';


const Configurazione = ({ onConfigSubmit }) => {


    const [dim, setDim] = useState(2);
    const [passi, setPassi] = useState(3);

    const handleChangeDim = (e) => {
        const w = parseInt(e.target.value, 10);
        setDim(w >= 2 ? w : 2);
    }

    const handleChangePassi = (e) => {
        const l = parseInt(e.target.value, 10);
        setPassi(l >= 3 ? l : 3);
    }

    const start = (e) => {
        onConfigSubmit(dim, passi);
    }

    return (

        <div className='config'>

            <label>Dimensione: </label>
            <input type='number' value={dim} min="2" onChange={handleChangeDim} ></input>

            <label>Passi: </label>
            <input type='number' value={passi} min="3" onChange={handleChangePassi} ></input>

            <button onClick={start}>Iniziamo:</button>

        </div>
    );


};


export default Configurazione;