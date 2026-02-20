//risposta da servlet
var result=0;
function substituteInnerHTML(ajax, theElement) {

    // designiamo la formattazione della zona dell'output
    theElement.class = "content";

    // verifica dello stato
    if (ajax.readyState === 2) {
        theElement.innerHTML = "Richiesta inviata...";
    }

    else if (ajax.readyState === 3) {
        theElement.innerHTML = "Ricezione della risposta...";
    }

    else if (ajax.readyState === 4) {

        // verifica della risposta da parte del server
        if (ajax.status === 200) {
            // operazione avvenuta con successo

            // per il formato JSON
            // var res=JSON.parse(ajax.responseText);

            theElement.innerHTML = ajax.responseText;

        }

        else {
            // errore di caricamento
            theElement.innerHTML = "Impossibile effettuare l'operazione richiesta.<br />";
            theElement.innerHTML += "Errore riscontrato: " + ajax.statusText;
        }

    }

}

function prefetchIframe() {
    alert("Non supportiamo iframe");
}

function prefetchAJAX(e,theElement, theXhr) {

    // impostazione della funzione di callback
    theXhr.onreadystatechange = function () {

        substituteInnerHTML(theXhr, theElement);
    };

    console.log("Invio :"+e);
    let uri = "Servlet";

    // impostazione richiesta asincrona in GET
    try {
        theXhr.open("post", uri, true);
    } catch (e) {
        // Exceptions are raised when trying to access cross-domain URIs
        alert(e);
    }

    //setto gli header
    theXhr.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    theXhr.setRequestHeader("connection", "close");

    //per semplcità passo la matrice in json
    theXhr.send("elem="+e);
}

function calculate() {

        var scelta=myGetElementById("event");

        sendAjax(scelta);
}
//funzione che gestisce gli invii delle sottoparti della matrice
function sendAjax(value){
    var xhr = myGetXmlHttpRequest();
    var theElem = myGetElementById("result");

    if (xhr) prefetchAJAX(value,theElem, xhr);

    else prefetchIframe();
}
