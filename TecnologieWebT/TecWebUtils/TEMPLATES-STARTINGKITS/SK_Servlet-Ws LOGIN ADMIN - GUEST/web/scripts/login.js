function logout(event)
{
	event.preventDefault();
	fetch("Login?act=logout")
	.then(message => {console.log(message)})
	.then(alert("Succesfully logout!"))
	.then(window.location.replace("login.jsp"))
	.catch(error => console.error('Errore (catch):', error));
	return false;
}

function login(event)
{
	event.preventDefault();
	window.location.replace("login.jsp");
}

function startup(event, message)
{
	if(message !== "")
		alert(message);
	console.log(event, message);
}