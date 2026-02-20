const socket = new WebSocket("ws://localhost:8080/08_TecWeb/actions");

function send(data) {
    var json = JSON.stringify(data);

    socket.send(json);
}


socket.onmessage =  function (event){
	console.log(event);
	 var message = JSON.parse(event.data);
	 if(message.valid)
		 {
		 	var log = document.getElementById("risultato");
			log.value = "";
		    console.log(event.data);
		   
		    log.value = message.risultato;
		 }else{
			 alert("hai superato il limite massimo di richieste per sessione");
		 }
	
}
