const http = require('http');

const hostname = '127.0.0.1';
const port = 3000;

const server = http.createServer((req, res) => {
  var fs = require("fs");
  let output; 
  fs.readFile("MyFile2.txt", (error, buff) => {
    if(!error)
      {
        console.log("File content", buff.toString());
        let parole = buff.toString().trim().split(/\s+/);
        output = { 
            parole: parole.length, 
            testo: buff
          };
          res.statusCode = 200;
      }
      else
      {
        output = {
          parole: 0,
          testo: "Errore nella lettura del file"
        }
        res.statusCode = 500;
      }
      res.setHeader('Content-Type', 'text/plain; charset=utf-8');
      res.write('Conenuto: \n' + output.testo);
      res.end('\nTotale: ' + output.parole);
  });
});

server.listen(port, hostname, () => {
  console.log(`Server running at http://${hostname}:${port}/`);
});