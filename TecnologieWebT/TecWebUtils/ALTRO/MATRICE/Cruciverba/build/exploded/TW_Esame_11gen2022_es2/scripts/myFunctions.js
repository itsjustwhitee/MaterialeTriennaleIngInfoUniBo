function checkPassword(f){ //Verifica che i due campi password siano identici
	let pass = f.Pwd.value;
	let check = f.ConfirmPwd.value;
	if(pass !== check){
		document.getElementById('errorP').innerText = "Il campo di conferma della password deve essere uguale a quello della password";
		document.getElementById('check').value = "";
		document.getElementById('signUpBtn').disabled = true;
		document.getElementById('signUpBtn').style.opacity = (0.4);
	}else{
		document.getElementById('errorP').innerText = "";
		document.getElementById('signUpBtn').disabled = false;
		document.getElementById('signUpBtn').style.opacity = (1);
	}
}
   		
function hasSpaces(field) {
	return /\s/.test(field.value); // Verifica se ci sono spazi nel campo
}

// Funzione che controlla specifici campi e mostra un messaggio
function checkFields(valore) {
	if(hasSpaces(valore)){
		document.getElementById("errorP").innerText ="I campi Username e Password non devono contenere spazi";
		valore.value = "";
	}
}
        
function inviaCarattere(carattere, riga, colonna) {
	if (!/^[A-Z]$/.test(carattere)) {
		alert("Sono accettate solo lettere maiuscole!!!");
		return; // Accetta solo lettere maiuscole
	}
	let par = document.getElementById("errorP");
	const xhr = new XMLHttpRequest();
	xhr.open("POST", "CruciverbaServlet", true);
	xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	xhr.send(`riga=${riga}&colonna=${colonna}&carattere=${carattere}`);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4 ) {
			if(xhr.status === 200){
				console.log(xhr.responseText)
				const jsonResponse = JSON.parse(xhr.responseText);
				const rigaCompleta = jsonResponse.riga;
				const colonnaCompleta = jsonResponse.colonna;
				if(rigaCompleta !== undefined){
					for(let col = 0; col < 10; col++){
						document.querySelector(`input[data-riga="${rigaCompleta}"][data-colonna="${col}"]`).disabled = true;
					}
				}
				if(colonnaCompleta !== undefined){
					for(let rig = 0; rig < 10; rig++){
						document.querySelector(`input[data-riga="${rig}"][data-colonna="${colonnaCompleta}"]`).disabled = true;
					}
				}
			} else
				par.innerText="Errore lato server, impossibile eseguire la richiesta. Errore riscontrato: " + xhr.statusText;
		}
	};
}

// Funzione AJAX per ottenere la matrice aggiornata
function aggiornaCruciverba() {
	let par = document.getElementById("errorP");
	const xhr = new XMLHttpRequest();
	xhr.open("GET", "CruciverbaServlet", true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4 ) {
			if(xhr.status === 200){
				const response = JSON.parse(xhr.responseText);
				aggiornaTabella(response.matrice, response.righeCorrette, response.colonneCorrette);
				disabilitaRighe(response.righeComplete);
				disabilitaColonne(response.colonneComplete);
				aggiornaColore(response.righeComplete, response.colonneComplete, response.righeCorrette, response.colonneCorrette)

			} else
				par.innerText="Errore lato server, impossibile eseguire la richiesta. Errore riscontrato: " + xhr.statusText;
		}
	};
	xhr.send();
}
        
function disabilitaRighe(righeComplete) {
	righeComplete.forEach(function(riga) {
		for (let col = 0; col < dimensione; col++) {
			const input = document.querySelector(`input[data-riga="${riga}"][data-colonna="${col}"]`);
			if (!input.disabled) { // Disabilita solo se non è già disabilitato
				input.disabled = true;
			}
		}
	});
}


function disabilitaColonne(colonneComplete) {
	colonneComplete.forEach(function(colonna) {
		for (let riga = 0; riga < dimensione; riga++) {
			const input = document.querySelector(`input[data-riga="${riga}"][data-colonna="${colonna}"]`);
			if (!input.disabled) { // Disabilita solo se non è già disabilitato
				input.disabled = true;
			}
		}
	});
}

// Aggiorna la tabella con i nuovi caratteri
function aggiornaTabella(matrice, righeCorrette, colonneCorrette) {

	console.log("Righe corrette:");
	for (let i = 0; i < righeCorrette.length; i++) {
		console.log( (righeCorrette[i]+1) ); // Log each element
	}
	console.log("Colonne corrette:");
	for (let i = 0; i < colonneCorrette.length; i++) {
		console.log( (colonneCorrette[i] + 1) ); // Log each element
	}

	for (let i = 0; i < dimensione; i++) {
		for (let j = 0; j < dimensione; j++) {
			if(matrice[i][j] === " ")
				document.querySelector(`input[data-riga="${i}"][data-colonna="${j}"]`).value = "";
			else
				document.querySelector(`input[data-riga="${i}"][data-colonna="${j}"]`).value = matrice[i][j] ;
		}
	}
}	

function aggiornaColore(righeComplete, colonneComplete, righeCorrette, colonneCorrette){
	for(let i=0;i<righeComplete.length;i++){
		let found = false;
		for(let j=0;j<righeCorrette.length;j++){
			if(righeComplete[i] == righeCorrette[j]){
				found = true;
			}
		}
		if(found){
			changeRowColor(righeComplete[i], "green");
		}
		else changeRowColor(righeComplete[i], "red");

	}

	for(let i=0;i<colonneComplete.length;i++){
		let found = false;
		for(let j=0;j<colonneCorrette.length;j++){
			if(colonneComplete[i] == colonneCorrette[j]){
				found = true;
			}
		}
		if(found){
			changeColColor(colonneComplete[i], "green");
		}
		else changeColColor(colonneComplete[i], "red");

	}

}

function changeRowColor(rowIndex, color) {
	const row = document.getElementById('cruciverba').rows[rowIndex];
	if (row) {
		row.style.backgroundColor = color;
	}
}

function changeColColor(colIndex, color) {


	
	rows = document.getElementById('cruciverba').rows;
	
	for(let i=0;i<10;i++){
		const cell = rows[i].children[colIndex];
		if(cell && cell.style.backgroundColor !== "red"){
			cell.style.backgroundColor = color;
		}
	}

}