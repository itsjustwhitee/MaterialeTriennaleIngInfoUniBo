var socket = new WebSocket("ws://localhost:8080/ES3/websocket");
console.log("ws://localhost:8080/ES3/websocket");
var counter=0;
var lista=[];

function send(data) {
    socket.send(data);
}

socket.onmessage =  function (event){
    //Se ricevo JSON
    //var message = JSON.parse(event.data);
    var message = (event.data);

    if(message.match('avviato')){
        console.log("Gioco Avviato");
        createPrinter(event.data);
    }
    else if(message.match('end')){
        console.log("Gioco finito");
        createPrinter(event.data);
    }else{
        console.log("Evento ricevuto: "+event.data);
        createPrinter(event.data);
    }

}

function createPrinter(msg){
    var txt = document.createElement("testo");
    txt.innerHTML = msg;
    var br = document.createElement("br");
    document.body.appendChild(br);
    document.body.appendChild(txt);
}
function myFunction(){
    var value=document.getElementById("numbers").value;
    console.log("Frase: "+value);
    lista=value.split(' ');
    send(value);
}

function handleMatch(e){
    var scelta=document.getElementById(e).innerHTML;

    console.log("Frase: "+scelta);
    send(scelta);
}


