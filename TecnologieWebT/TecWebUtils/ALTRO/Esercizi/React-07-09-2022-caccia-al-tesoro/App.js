import React, { useState } from "react";
import Configurazione from "./Configurazione";
import Mappa from "./Mappa";
import Punteggio from "./Punteggio";
import './App.css';

function App() {

  const [length, setLength] = useState(5);
  const [width, setWidth] = useState(5);
  const [tentativi, setTentativi] = useState(0);
  const [punteggio, setPunteggio] = useState(0);
  const [mappa, setMappa] = useState([]);
  const [TRow, setTRow] = useState(0);
  const [TCell, setTCell] = useState(0);


  const handleConfigSubmit = (newLength, newWidth) => {
    const map = inizializeMap(newLength, newWidth);
    setMappa(map);


    setLength(newLength);
    setWidth(newWidth);
    setTentativi(0);
    setPunteggio(0);
  }

  const inizializeMap = (length, width) => {
    const map = Array.from({ length: length }, () => Array(width).fill('giallo'));
    const randomRow = Math.floor(Math.random() * length);
    const randomCell = Math.floor(Math.random() * width);

    // Assegna il tesoro nella posizione casuale
    setTRow(randomRow);
    setTCell(randomCell);
    map[randomRow][randomCell] = 'T';
    return map;
  }

  const handleCellClick = (rowIndex, cellIndex) => {

    if (rowIndex === TRow && cellIndex === TCell) {
      mappa[rowIndex][cellIndex] = 'blu';
      alert("HAI VINTO!");
      tentativi <= 10 ? setPunteggio(5) : setPunteggio(2);
    } else {
      mappa[rowIndex][cellIndex] = 'grigio';
      setTentativi(tentativi + 1);
    }

    setMappa([...mappa]);

  }



  return (
    <div className="App">
      <Configurazione onConfigSubmit={handleConfigSubmit} />
      <Mappa mappa={mappa} onCellClick={handleCellClick} />
      <Punteggio tentativi={tentativi} punteggio={punteggio} />
    </div>
  );
};

export default App;
