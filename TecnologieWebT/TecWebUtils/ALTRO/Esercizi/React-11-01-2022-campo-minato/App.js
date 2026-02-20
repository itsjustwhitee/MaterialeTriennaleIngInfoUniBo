import React, { useState } from "react";
import Configurazione from "./Configurazione";
import Mappa from "./Mappa";
import Punteggio from "./Punteggio";
import './App.css';

function App() {

  const [dim, setDim] = useState(2);
  const [passi, setPassi] = useState(3);
  const [punteggio, setPunteggio] = useState(0);
  const [mappa, setMappa] = useState([]);
  const [TRow1, setTRow1] = useState(0);
  const [TCell1, setTCell1] = useState(0);
  const [TRow2, setTRow2] = useState(0);
  const [TCell2, setTCell2] = useState(0);


  const handleConfigSubmit = (newDim, newPassi) => {
    const map = inizializeMap(newDim);
    setMappa(map);
    setDim(newDim);
    setPassi(newPassi);
    setPunteggio(0);
  }

  const inizializeMap = (newDim) => {
    const map = Array.from({ length: newDim }, () => Array(newDim).fill('giallo'));
    const randomRow1 = Math.floor(Math.random() * newDim);
    const randomCell1 = Math.floor(Math.random() * newDim);

    const randomRow2 = Math.floor(Math.random() * newDim);
    const randomCell2 = Math.floor(Math.random() * newDim);

    // Assegna il tesoro nella posizione casuale
    setTRow1(randomRow1);
    setTCell1(randomCell1);
    setTRow2(randomRow2);
    setTCell2(randomCell2);
    map[randomRow1][randomCell1] = 'MINA';
    map[randomRow2][randomCell2] = 'MINA';
    return map;
  }

  const handleCellClick = (rowIndex, cellIndex) => {

    var finished = false;
    if ((rowIndex === TRow1 && cellIndex === TCell1) || (rowIndex === TRow2 && cellIndex === TCell2)) {
      mappa[rowIndex][cellIndex] = 'rosso';
      alert("Partita terminata: percorso non completato!");
      finished = true;
    } else {
      mappa[rowIndex][cellIndex] = 'blu';
      setPunteggio(punteggio + 5);
      if (passi === 1) {
        alert("Partita terminata: percorso completato!");
        finished = true;
      } else setPassi(passi - 1);
    }

    //   setMappa([...mappa]);

    if (finished === true) {
      handleConfigSubmit(dim, passi);
      finished = false;
    }
  }



  return (
    <div className="App">
      <Configurazione onConfigSubmit={handleConfigSubmit} />
      <Mappa mappa={mappa} onCellClick={handleCellClick} />
      <Punteggio passi={passi} punteggio={punteggio} />
    </div>
  );
};

export default App;
