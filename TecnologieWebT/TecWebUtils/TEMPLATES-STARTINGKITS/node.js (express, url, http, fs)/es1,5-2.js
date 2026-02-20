const url = require('url');
const express = require('express');
const server = express();

const fs = require('fs');
const readline = require("readline");

const hostname = '127.0.0.1';
const port = 3000;

server.get('*', (req, res) => {
  const parsedUrl = new URL(req.url, `http://${req.headers.host}`);

  let totRighe = 0;
  let totParole = 0;
  let record = 0;
  let results = [];

  if (parsedUrl.pathname.length > 1) {
    var rl = readline.createInterface({
      input: fs.createReadStream(parsedUrl.pathname.replace('/', '')),
      output: process.stdout,
      terminal: false
    });

    rl.on('line', (line) => {
      let numParole = line.trim().split(/\s+/).length;
      totParole += numParole;

      results.push({ riga: line, numParole: numParole });

      if (numParole > record.numParole)
        record = totRighe;

      totRighe++;
    });

    rl.on('close', () => {
      res.setHeader('Content-Type', 'text/plain; charset=utf-8');
      results.forEach((result, i) => {
        res.write('\nn.' + (i) + ' (' + result.numParole + 'w)\t' + result.riga)
      });
      res.write('\n\n=========================================================================');
      res.write('\nTot parole: ' + totParole);
      res.write('\nTot righe: ' + totRighe);
      res.write('\n---------------------------------------------------------------------------');
      res.end('\nRiga record:' + '\nn.' + + (record + 1) + ' (' + results[record].numParole + 'w)\t' + results[record].riga);
    })

    rl.on('error', (err) => {
      console.error('Errore durante la lettura del file:', err);
      res.setHeader('Content-Type', 'text/plain; charset=utf-8');
      res.end("Errore nella lettura del file");
    })
  }
  else {
    res.setHeader('Content-Type', 'text/plain; charset=utf-8');
    res.end("Inserire un file nell'url (separandolo con /)");
  }
});

server.listen(port, hostname, () => {
  console.log(`Server running at http://${hostname}:${port}/`);
});