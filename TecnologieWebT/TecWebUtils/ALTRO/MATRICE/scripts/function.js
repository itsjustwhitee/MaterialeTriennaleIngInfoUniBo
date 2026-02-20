/**
 * 
 */
 //inizializza la matrice con ''
function init (){
	matrix = [];
	for(let i = 0;i<leng;i++){
		matrix[i] = [];
		for(let j = 0;j<leng;j++){
			matrix[i][j]='';
			document.getElementById(i+' '+j).value='';
		}
	}
	return;
}

var matrix = [];
var leng=6;

//controlla che all'inserimento di un carattere siano solamente ammessi numeri interi
function validate(element){
	console.log(element.name+" "+element.value);
	var ok = element.name;
	var regexNumerico = /^[0-9]+$/;
	var paramaters = ok.split(' ');
	var i = parseInt(paramaters[0].trim());
	var j = parseInt(paramaters[1].trim());
	var valore= element.value.trim();
	if(valore === ''){
		matrix[i][j] = '';
	}
	else{
		if(! regexNumerico.test(valore)){
			alert("Devi inserire un numero intero!!!!");
			var newValore = valore.slice(0,valore.length - 1);
			if(isNaN(parseInt(newValore))){
				matrix[i][j]='';
				element.value='';
			}
			else{
				var parsedValue = parseInt(newValore);
				matrix[i][j]=parsedValue;
				element.value=parsedValue;
			}
		}
		else{
			var parsedValue = parseInt(valore);
			matrix[i][j] = parsedValue;
		}
	}
}

//controlla al click del bottone che tutta la matrice sia inserita, modifica secondo le specifiche del problema
function send(){
	var problema = false;
	for(let i = 0;i<leng && problema === false;i++){
		for(let j=0;j<leng;j++){
			if(matrix[i][j] === ''){
				problema = true;
				alert('inserisci tutti i numeri prima di inviare!');
				break;
			}
		}
	}
	if(problema === false){
		alert("Corretto!");
	}
	
}

