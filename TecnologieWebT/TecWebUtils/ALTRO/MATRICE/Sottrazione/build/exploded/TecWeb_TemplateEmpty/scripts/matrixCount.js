/*function myFunction()
{
	
	var text = document.getElementById("myText");
	var fileName = document.getElementById("fileName");
	
	
	
	if(text.value.length > 0 && fileName.value.length > 0)
		{
		
		// assegnazione oggetto XMLHttpRequest
			var	xhr = myGetXmlHttpRequest();
			var xhr2 = myGetXmlHttpRequest();
			if ( xhr ) 
				countingAJAX(xhr, xhr2, fileName.value, text.value); 
			else 
				countingIframe(); 
		}else
			{
				alert("the inputs text cannot be empty!");		
			}
	
		

}*/



function printResult( stringa ) {
   
	// variabili di funzione
	
	
		var result = stringa.split("@");
		var resElement;
		for(var i =0; i<result.length; i++)
			{
			resElement = document.getElementById("r"+i);
			resElement.value = result[i];
			}
		// Ottengo la lista delle sezioni del docuemento
		
		
		
		
		for(var i=0; i<8; i++)
			{
				var elementA = document.getElementById("a"+i);
				var elementB = document.getElementById("b"+i);
				elementA.value = '';
				elementB.value = '';
			}
		
	
	
     

}// parsificaJSON()





/*
 * Funzione di callback
 */
function callback( theXhr ) {

	
	if ( theXhr.readyState === 2 ) {
	    	//theElement.innerHTML = "Richiesta inviata...";
	}// if 2
	else if ( theXhr.readyState === 3 ) {
    	//	theElement.innerHTML = "Ricezione della risposta...";
	}// if 3
	else if ( theXhr.readyState === 4 ) {
		// verifica della risposta da parte del server
		if ( theXhr.status === 200 ) {
			// operazione avvenuta con successo
			printResult(theXhr.responseText);
			
			//location.reload();
		}// if 200

		 else {
	        	// errore di caricamento
			 alert("Impossibile effettuare l'operazione richiesta.");
//	        	theElement.innerHTML = "Impossibile effettuare l'operazione richiesta.<br />";
//	        	theElement.innerHTML += "Errore riscontrato: " + theXhr.statusText;
	        }// else (if ! 200)
	}// if 4

} // callback();








function countingIframe() {
	// qui faccio scaricare al browser direttamente il contenuto del feed come src dell'iframe.
	Alert("Impossibile effettuare l'operazione, il tuo browser è troppo vecchio")
	
	// non riesco tuttavia a intervenire per parsificarlo! è il browser che renderizza il src del iframe!
}// caricaFeedIframe()



function countingMatrixAJAX( theXhr, mode, aVal, bVal) {
    
	
	// impostazione controllo e stato della richiesta
	theXhr.onreadystatechange = function() { callback(theXhr); };
	// impostazione richiesta asincrona in GET
	// del file specificato
	try {
		theXhr.open("post", "MatrixCalculator", true);
	}
	catch(e) {
		// Exceptions are raised when trying to access cross-domain URIs 
		alert(e);
	}

	// rimozione dell'header "connection" come "keep alive"
	theXhr.setRequestHeader("connection", "close");
	var argument = "mode="+mode+"&aVal="+aVal+"&bVal="+bVal;
	// invio richiesta
	//
	theXhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	theXhr.send(argument);

} // caricaFeedAJAX()

function myFunction(caller)
{
	
	var mode = caller.getAtribute("name"); //dovrebbe essere id
	
	var aValues = "";
	var bValues = "";
	for(var i=0; i<8;i++)
		{
			aValues = aValues + MyGetElementById("a"+i).value+"@";
			bValues = bValues + MyGetElementById("b"+i).value+"@";
		}
	var	xhr = myGetXmlHttpRequest();

			if ( xhr ) 
				countingMatrixAJAX(xhr, mode, aValues, bValues); 
			else 
				countingIframe(); 
		
	
		

}

function checkMatrix()
{
	var nota= false;
	var notb= false;
	for(var i =0; i<8; i++)
		{
			var currentValue = MyGetElementById("a"+i).value;
			if(currentValue == '' || isNaN(currentValue))
				{
					nota=true;
				}
		}
	for(var i =0; i<8; i++)
	{
		var currentValue = MyGetElementById("b"+i).value;
		if(currentValue == '' || isNaN(currentValue))
			{
				notb=true;
			}
	}
	
	//se tutti i campi sono riempiti con numeri
	if(!notb && !nota)
		{
		var currentValue = MyGetElementById("secret");
		currentValue.style.display = 'block';
		}
}