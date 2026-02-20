

var regexMaiuscoli = /^[A-Z]+$/; //per caratteri compresi fra 'A' e 'Z'
var regexNumerico = /^[0-9]+$/; //per numeri
var regexMinuscoli = /^[a-z]+$/; //minuscoli
var regexNumeriMinuscoli = /^[0-9a-z]+$/;
var regexNumeriMaiuscoli = /^[0-9A-Z]+$/;
var regexMinuscoliMaiuscoli = /^[a-zA-Z]+$/;
var regexNumeriMinuscoliMaiuscoli = /^[0-9a-zA-Z]+$/;

// questi sotto consentono al più uno spazio fra un carattere e il prossimo
var regexNumerico = /^(\d+\s?)*\d+$/;
var regexMaiuscoli = /^([A-Z]+\s?)*[A-Z]+$/;
var regexMinuscoli = /^([a-z]+\s?)*[a-z]+$/;
var regexNumeriMinuscoli = /^([0-9a-z]+\s?)*[0-9a-z]+$/i;
var regexNumeriMaiuscoli = /^([0-9A-Z]+\s?)*[0-9A-Z]+$/;
var regexMinuscoliMaiuscoli = /^([a-zA-Z]+\s?)*[a-zA-Z]+$/;
var regexNumeriMinuscoliMaiuscoli = /^([0-9a-zA-Z]+\s?)*[0-9a-zA-Z]+$/;

// senza limiti di spazi
var regexNumerico = /^(\d+\s*)*\d+$/;
var regexMaiuscoli = /^([A-Z]+\s*)*[A-Z]+$/;
var regexMinuscoli = /^([a-z]+\s*)*[a-z]+$/;
var regexNumeriMinuscoli = /^([0-9a-z]+\s*)*[0-9a-z]+$/;
var regexNumeriMaiuscoli = /^([0-9A-Z]+\s*)*[0-9A-Z]+$/;
var regexMinuscoliMaiuscoli = /^([a-zA-Z]+\s*)*[a-zA-Z]+$/;
var regexNumeriMinuscoliMaiuscoli = /^([0-9a-zA-Z]+\s*)*[0-9a-zA-Z]+$/; 

// come controllare se una stringa rispetta il pattern?

var str;
regex.test(str) // true se rispetta il pattern false altrimenti
