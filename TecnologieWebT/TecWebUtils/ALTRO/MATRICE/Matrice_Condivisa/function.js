var righe = 5;
var colonne = 5;
var matrix = [];

function initialize(){
	for(var i = 0 ; i< righe ; i++){
		matrix [i] = [];
		for( var j = 0 ; j < colonne ; j++){
			matrix [i][j] = '';
		}
	}
}

initialize();

document.addEventListener('DOMContentLoaded', function() {
    setInterval(askModify, 1000);
});

function callbackM(xhr){
	if(xhr.readyState === 4 && xhr.status === 200){
		console.log(xhr.responseText);
		console.log(JSON.parse(xhr.responseText));
		var matNew = JSON.parse(xhr.responseText);
		modifyMatrix(matNew);
	}
}

function askModify(){
	var xhr = new XMLHttpRequest();
	
	if(xhr){
		doGetReq(xhr);
	}
	else{
		document.removeEventListener('onLoad', setInterval(askModify(),1000));
		alert("Il tuo browser è troppo vecchio per poter effettuare chiamate Ajax");
	}
}

function doGetReq(xhr){
	xhr.onreadystatechange = function () { callbackM(xhr); };
	
	try{
		xhr.open("get","s1",true);
	}
	catch(e){
		alert(e);
	}
	xhr.setRequestHeader("connection","close");
	
	xhr.send(null);
	
}
function modifyMatrix(matNew){
	matrix = matNew.matrice;
	for(var i = 0 ; i< righe ; i++){
		for( var j = 0 ; j < colonne ; j++){
			document.getElementById(i+' '+j).value = matrix[i][j];
		}
	}
}

function validate(element){
	var nomeCella = element.name;
	console.log(nomeCella);
	var elem = element.value;
	var indici = (nomeCella.trim()).split(" ");
	var i = parseInt(indici[0].trim());
	var j = parseInt(indici[1].trim());
	if(isNaN(i) || isNaN(j)){
		alert("Errore lato client!");
		return;
	}
	else{
		if(elem !== ''){
			if(elem.length != 1){
				element.value = elem.slice(0,1);
			}
			else{
				//modificare in base alla traccia
				console.log("Invio modifica alla servlet elem="+elem);
				matrix[i][j] = elem;
				reqToServlet(elem,i,j);
				return;
			}
		}
	}
	
}

function callbackP(xhr){
	if(xhr.readyState === 4 && xhr.status === 200){
		console.log(xhr.responseText);
		console.log(JSON.parse(xhr.responseText));
		var result = JSON.parse(xhr.responseText);
		if(result === false){
			alert("Cambiamento non riuscito");
		}	
	}
}

function doPostReq(xhr,elem,i,j){
	xhr.onreadystatechange = function () { callbackP(xhr); };
	
	try{
		xhr.open("post","s1",true);
	}
	catch(e){
		alert(e);
	}
	xhr.setRequestHeader("connection","close");
	var argument = "i="+i+"&j="+j+"&valore="+elem;
	xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	console.log(argument);
	xhr.send(argument);
}

function reqToServlet(elem,i,j){
	var xhr = new XMLHttpRequest();
	if(xhr){
		doPostReq(xhr,elem,i,j);
	}
	else{
		alert("Il tuo browser non regge tecnologia Ajax!");
	}
}