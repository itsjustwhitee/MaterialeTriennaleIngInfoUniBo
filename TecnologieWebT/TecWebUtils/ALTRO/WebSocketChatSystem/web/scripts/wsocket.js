const socket = new WebSocket("ws://localhost:8080/14-04-2022-WebSocket/actions");

function send( data) {
    var json = JSON.stringify(data);
    socket.send(json);
}


socket.onmessage =  function (event){
	var message = JSON.parse(event.data);
	if(message.valid){
		var elemento = document.getElementById("textarea");
		elemento.innerHTML += (message.messaggio + "</br>");
	}else{
		if(message.chiusa){
			alert("LA CHAT è CHIUSA");
		}else{
			alert("E' stato inviato un messaggio proibito");
		}
	}
}

function myFunction()
{
	var message = document.getElementById("message").value;

	send(message);
	
}
