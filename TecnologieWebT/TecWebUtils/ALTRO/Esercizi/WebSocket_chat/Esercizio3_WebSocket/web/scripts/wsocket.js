const socket = new WebSocket("ws://localhost:8081/Esercizio3_WebSocket/actions");

function send( data) {
    var json = JSON.stringify(data);
	console.log(json);
    socket.send(json);
}


socket.onmessage =  function (event){
	
	console.log("[socket.onmessage]", event.data);
	
	var output = JSON.parse(event.data);
	var id = output.user;
	var mess = output.message;
	var valido = output.valid;
	var chiusa = output.close;
	if(chiusa === true ){
		alert("Chat chiusa, non puoi più inviare messaggi!");
	}
	else if(valido === false){
		alert("Messaggio non valido!");
	}
	else{
		var message = id+ " : "+mess+"\n";
		document.getElementById('chat').textContent+=message;
	}
	
}


function validate(id)
{
	console.log("Id="+id);
	var element = document.getElementById(id);
	var value = element.value;
	console.log(value);
	value = value.trim();
	if(value === ''){
		alert("Non puoi inviare un messaggio vuoto!!!");
	}
	else{
		element.value = "";
		messaggio = value;
		send(messaggio);	
	}
}