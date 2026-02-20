
function parsificaJson(response){ //esempio classe Risultato attributi ServerCount,ServerTime,BeanCount,BeanTime
	var risultato = JSON.parse(response);
	var output = "<p> Risultato Servlet <br/> Counter = "+risultato.ServerCount+" Tempo impiegato = "+risultato.ServerTime+"ms<br/> Risultato Bean<br/> Counter = "+risultato.BeanCount+" Tempo impiegato = "+risultato.BeanTime+"ms</p>";
	console.log(output);
	return output;
}

function callback(xhr){
	if(xhr.readyState === 2){
		//Richiesta inviata...
	}
	else if(xhr.readyState === 3){
		//Ricezione risposta...
	}
	else if(xhr.readyState === 4){
		if(xhr.status === 200){
			var output=parsificaJson(xhr.responseText);	// fai quello che devi fare	
		}
		else{
			//document.getElementById("result").innerHTML="<p>ops qualcosa è andato storto, codice di ritorno="+xhr.status+"</p>"
		}
	}
}

function ajaxJson(xhr){
	
	xhr.onreadystatechange = function () { callback(xhr); };
	
	try{
		xhr.open("post","getList",true); // per get xhr.open("get","nomeservlet?attributo1=valore&attributo2=valore...
	}
	catch(e){
		alert(e);
	}
	xhr.setRequestHeader("connection","close");
	var argument = "file1="+nameFile[0]+"&file2="+nameFile[1]+"&file3="+nameFile[2]; // esempio di chiamata post
	xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded"); // fondamentale per indicare al server il tipo //encoded 			//dei dati passati
	console.log(argument);
	xhr.send(argument);
}


function iframe(){
	alert("Impossibile effettuare l'operazione, il tuo browser è troppo vecchio");
}

function doAjax(){
	var xhr = new XMLHttpRequest();
	if(xhr)
		ajaxJson(xhr);
	else
		iframe();	
}
