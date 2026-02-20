//in questo caso il file xml era formato come

/*
	lato server importante settare response.setHeader("Content-Type","application/xml");
	la prima riga del file.jsp o la prima riga della reponse se servlet deve essere:
	<?xml version='1.0' encoding='ISO-8859-1'?>
	
	<rss version='2.0'>
	<channel>
		<title><![CDATA[HomePage - LASTAMPA.it]]></title>
		<link>http://www.lastampa.it/redazione/</link>
		<description><![CDATA[lastampa.it - I titoli della Home Page]]></description>
		<lastBuildDate><![CDATA[Mon, 16 May 2011 15:58:35 +0200]]></lastBuildDate>
		<docs></docs>
		<managingEditor></managingEditor>

		<webMaster>teleservizi s.r.l.</webMaster>
		<language>it-IT</language>
		<image>
  			<url>http://www.lastampa.it/common/images/lastampatop.gif</url> 
  			<title>lastampa.it</title> 
  			<link>http://www.lastampa.it/redazione/</link> 
  			<width></width> 
  			<height></height> 
  		</image>
  		
  		<item>
			<title><![CDATA[<%=feed.getTitle()%>]]></title>
			<description><![CDATA[<%=feed.getDescription()%>]]></description>
			<author><![CDATA[<%=feed.getAuthor()%>]]></author>
			<category><![CDATA[<%=feed.getCategory()%>]]></category>
			<pubDate><![CDATA[<%=feed.getPubDate()%>]]></pubDate>
			<link><![CDATA[<%=feed.getLink()%>]]></link>
		</item>
		
	</channel>
</rss>
	quindi prima prendiamo l'item o la lista di items e poi leggiamo il valore di ogni suo nodo con la funzione leggi contenuto
*/



function leggiContenuto(item, nomeNodo) {
	return item.getElementsByTagName(nomeNodo).item(0).firstChild.nodeValue;
};

function parsificaXml( xml ) {
   
	// variabili di funzione
	var

		// Otteniamo la lista degli item dall'RSS 2.0 di edit
		items = xml.getElementsByTagName("item"),

		// Predisponiamo una struttura dati in cui memorrizzare le informazioni di interesse
		itemNodes = new Array(),

		// la variabile di ritorno, in questo esempio, e' testuale
		risultato = "";

	// ciclo di lettura degli elementi
	for (    var a = 0, b = items.length;    a < b;   a++   ) {
		// [length al posto di push serve per evitare errori con vecchi browser]
		itemNodes[a] = new Object();
		itemNodes[a].title = leggiContenuto(items[a],"title");
		itemNodes[a].description = leggiContenuto(items[a],"description");
		itemNodes[a].link = leggiContenuto(items[a],"link");
	}// for ( items )

	// non resta che popolare la variabile di ritorno
	// con una lista non ordinata di informazioni

	// apertura e chiusura della lista sono esterne al ciclo for 
	// in modo che eseguano anche in assenza di items
	risultato = "<ul>";

	for( var c = 0; c < itemNodes.length; c++ ) {
		risultato += '<li class="item"><strong>' + itemNodes[c].title +'</strong><br/>';
		risultato += itemNodes[c].description +"<br/>";
		risultato += '<a href="' + itemNodes[c].link + '">approfondisci</a><br/></li>';
	};

	// chiudiamo la lista creata
	risultato += "</ul>";

     // restituzione dell'html da aggiungere alla pagina
     return risultato;

}// parsificaXml()

function callback( theXhr, theElement ) {

	// designiamo la formattazione della zona dell'output
	theElement.class = "content";
	
	// verifica dello stato
	if ( theXhr.readyState === 2 ) {
	    	theElement.innerHTML = "Richiesta inviata...";
	}// if 2
	else if ( theXhr.readyState === 3 ) {
    		theElement.innerHTML = "Ricezione della risposta...";
	}// if 3
	else if ( theXhr.readyState === 4 ) {
		// verifica della risposta da parte del server
		if ( theXhr.status === 200 ) {
			// operazione avvenuta con successo
			if ( theXhr.responseXML ) {
				theElement.innerHTML = parsificaXml(theXhr.responseXML);
			}// if XML
			else {
				// visualizzazione contenuto letto
				// evitando di scrivere la risposta 
				// in modo interpretabile dal browser
				theElement.innerHTML = "L'XML restituito dalla richiesta non e' valido.<br />" +
				theXhr.responseText.split('<').join("&lt;").split('>').join("&gt;");
			}// else (if ! XML)
		}// if 200

		else {
			// errore di caricamento
			theElement.innerHTML = "Impossibile effettuare l'operazione richiesta.<br />";
			theElement.innerHTML += "Errore riscontrato: " + theXhr.statusText;
		}// else (if ! 200)
	}// if 4

} // callback();

function caricaFeedIframe(theUri,theHolder) {
	// qui faccio scaricare al browser direttamente il contenuto del feed come src dell'iframe.
	theHolder.innerHTML = '<iframe src="' + theUri + '" width="50%" height="50px">Il tuo browser non supporta gli iframe</iframe>';
	// non riesco tuttavia a intervenire per parsificarlo! è il browser che renderizza il src del iframe!
}// caricaFeedIframe()

function caricaFeedAJAX(theUri, theElement, theXhr) {
    
	// impostazione controllo e stato della richiesta
	theXhr.onreadystatechange = function() { callback(theXhr, theElement); };

	// impostazione richiesta asincrona in GET
	// del file specificato
	try {
		theXhr.open("get", theUri, true);
	}
	catch(e) {
		// Exceptions are raised when trying to access cross-domain URIs 
		alert(e);
	}

	// invio richiesta
	theXhr.send(null);

} // caricaFeedAJAX(


function caricaFeed(uri,e) {

	// variabili di funzione
	var
		// assegnazione oggetto XMLHttpRequest
		xhr = myGetXmlHttpRequest();

	if ( xhr ) 
		caricaFeedAJAX(uri,e,xhr); 
	else 
		caricaFeedIframe(uri,e); 

}// caricaFeed()
