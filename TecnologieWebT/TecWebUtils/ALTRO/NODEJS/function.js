//-----------PARSE PATHNAME IN URL---------------------------
const whichPage = url.parse(req.url).pathname;
console.log("whichPage", whichPage);
const filename = whichPage.substr(1, whichPage.length);
//-----------------------------------------------------------


//-------------------FILE------------------------------------
//Read file
const fs = require('fs');
fs.readFile("myFile.txt", function(error, dataBuffer){
	// convenzione Node per callback: primo argomento è oggetto
	// js di errore

	let lines = dataBuffer.toString().split("\n");
  }); 

//Handle error 
const fs = file_stream.createReadStream(file_name)
fs.on('error', function (err) {
            alert("File inesistente o errore nell\'apertura del file");
            html+=' </body>'+
            '</html>';
        res.write(html);
        res.end();
});

//-----------------------------------------------------------



//-------------------READLINE--------------------------------
const readline = require('readline');
const rl = readline.createInterface({
    input: fs.createReadStream(filename),
    output: process.stdout,
    terminal: false
  });


  rl.on('line', function (line) {
    //do something with line
    let words = line.trim().split(" ");
  })

  rl.on('close', function(){
	//do something when EOF is reached
	html+=' </body>'+
	       '</html>';

	res.write(html);
	res.end();
  })


//-----------------------------------------------------------
