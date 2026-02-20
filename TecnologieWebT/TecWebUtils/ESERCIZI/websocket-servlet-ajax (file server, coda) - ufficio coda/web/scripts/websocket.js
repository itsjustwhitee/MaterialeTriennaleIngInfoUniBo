const socket = new WebSocket("ws://localhost:8080/Es1/actions");

function send(data) {
    var json = JSON.stringify(data);
	console.log(json)
    socket.send(json);
}

//status di risposta possibili: 
//	-accepted	~> se richiesta accettata
//	-refused	~> se richiesta rifiutata (causa saturazione richieste)
//	-delayed	~> se richiesta ritardata a causa di vip arrivato
//	-serving	~> se richiesta in lavorazione
//	-closed		~> se richiesta chiusa (correttamente)

socket.onmessage =  function (event){
	 window.location.replace("processing.jsp");
	 console.log(event); // Controlla se l'evento è corretto
	 	         var response = JSON.parse(event.data);
	 	         console.log(response);

	 setTimeout(() => {
			updateGui(response);
	     }, 5000); // Attendere 500ms prima di aggiornare l'HTML
}

function updateGui(response)
{
	let newHtml = '';
		         
		         switch(response.status) {
		             case 'accepted':
		                 alert('Richiesta accettata! Ora attendi in coda');
		                 newHtml = 'Richiesta <b>accettata</b>, attesa stimata di <b>' + response.timeToWait
		                     + (response.timeToWait > 1 ? ' minuti' : ' minuto') + '</b>';
		                 break;
		             case 'refused':
		                 alert('Richiesta rifiutata! Torna domani');
		                 newHtml = `Richiesta <b>rifiutata</b>, a causa degli utenti in coda. <br>
		                     Ci scusiamo del disagio, si prega di tornare domani, grazie :D`;
		                 break;
		             case 'delayed':
		                 alert('AVVISO: La sua richiesta é stata ritardata a causa di una richiesta VIP');
		                 newHtml = 'Richiesta <b>ritardata</b>, nuova attesa stimata di <b>' + response.timeToWait
		                     + (response.timeToWait > 1 ? ' minuti' : ' minuto') + '</b>';
		                 break;
		             case 'serving':
		                 newHtml = 'Ottime notizie! La sua richiesta é <b>in lavorazione</b>, nel giro di <b>' +
		                     (response.message === "cambio" ? 8 : 5) + ' minuti</b>';
		                 break;
		             case 'closed':
		                 alert('Elaborazione completata, a presto!');
		                 newHtml = 'Elaborazione richiesta <b>terminata</b>';
		                 break;
		             default:
		                 window.location.replace("/errors/failure.jsp");
		         }
				console.log('Nuovo contenuto di serviceStatus: ',newHtml);
		         // Aggiorna solo se l'elemento esiste
		         let element = document.getElementById('serviceStatus');
		         if (element) {
		             element.innerHTML = newHtml;
		         } else {
		             console.error('Elemento serviceStatus non trovato');
		         }
}

function handleRequest()
{	
	let request = {type: '', priority: 'false', applicant: ''};
	if(document.getElementById('cambio').checked)
		request.type = 'cambio';
	else
		request.type = 'rinnovo'
	
	request.priority = sessionStorage.getItem('priority');
	
	request.applicant = sessionStorage.getItem('username');
	
	console.log('Recupero dati dalla sessione, username=', request.applicant, ' priority=', request.priority);
	
	send(request);
}
