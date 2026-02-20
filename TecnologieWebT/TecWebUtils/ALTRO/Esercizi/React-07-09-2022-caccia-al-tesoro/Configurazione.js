import React, { useState } from 'react';


const Configurazione = ({ onConfigSubmit }) => {


    const [length, setLength] = useState(5);
    const [width, setWidth] = useState(5);

    const handleChangeWidth = (e) => {
        const w = parseInt(e.target.value, 10);
        setWidth(w >= 5 ? w : 5);
    }

    const handleChangeLength = (e) => {
        const l = parseInt(e.target.value, 10);
        setLength(l >= 5 ? l : 5);
    }

    const start = (e) => {
        onConfigSubmit(length, width);
    }

    return (

        <div className='config'>

            <label>Width: </label>
            <input type='number' value={width} min="5" onChange={handleChangeWidth} ></input>

            <label>Length: </label>
            <input type='number' value={length} min="5" onChange={handleChangeLength} ></input>

            <button onClick={start}>Iniziamo:</button>

        </div>
    );


};


export default Configurazione;