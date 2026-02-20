import React, { useState } from 'react';
import Configurazione from "./Configurazione";
import Visualizzazione from "./Visualizzazione";
import Estrazione from "./Estrazione";
import "./index.css";

import "./App.css";

const App = () => {
  const [schedina, setSchedina] = useState(Array(5).fill(""));
  const [estratti, setEstratti] = useState([]);
  const [risultato, setRisultato] = useState("");
  const [color, setColor] = useState("");
  const [commons, setCommons] = useState([]);

  const generaSchedinaVuota = () => {
    setSchedina(Array(5).fill(""));
  };

  const compilaSchedina = (index, value) => {
    const nuovaSchedina = [...schedina];
    nuovaSchedina[index] = value;
    setSchedina(nuovaSchedina);
  };

  const finalizzaGiocata = () => {
    // Controllo che ogni casella contenga un numero ammissibile
    if (schedina.every(numero => /^\d+$/.test(numero) && numero >= 1 && numero <= 10)) {
      // Impostare la schedina valida per la visualizzazione
      const numeriCasuali = estraiNumeri();
      setEstratti(numeriCasuali);
      setRisultato("Schedina valida");
      const updateCommons = confrontaArray(schedina, numeriCasuali);
      setCommons(updateCommons);
      // console.log(updateCommons);
      var newColor = "";
      switch (updateCommons.length) {

        case 2: newColor = "giallo"; break;
        case 3: newColor = "verde"; break;
        case 4: newColor = "blu"; break;
        case 5: newColor = "rosso"; break;
        default: newColor = ""; break;
      }

      setColor(newColor);
      //   console.log(newColor);


    } else {
      setRisultato("Schedina non valida");
    }
  };

  function confrontaArray(array1, array2) {
    var elementiComuni = [];
    //  console.log(array1);
    // console.log(array2);

    for (let i = 0; i < array2.length; i++) {
      if (array1.includes(array2[i].toString())) {
        elementiComuni.push(array2[i]);
      }
    }

    // console.log(elementiComuni);

    return elementiComuni;
  }

  const estraiNumeri = () => {
    // Genera 5 numeri casuali ammissibili
    var numeriEstratti = [];
    while (numeriEstratti.length < 5) {
      const numeroCasuale = Math.floor(Math.random() * 10) + 1;
      if (!numeriEstratti.includes(numeroCasuale)) {
        numeriEstratti.push(numeroCasuale);
      }
    }
    return numeriEstratti;
  };




  return (
    <div className="App">
      <Configurazione
        schedina={schedina}
        generaSchedinaVuota={generaSchedinaVuota}
        compilaSchedina={compilaSchedina}
        finalizzaGiocata={finalizzaGiocata}
      />
      <Visualizzazione schedina={schedina} risultato={risultato} color={color} commons={commons} />
      <Estrazione numeriEstratti={estratti} estraiNumeri={finalizzaGiocata} />
    </div>
  );
}


export default App;