let ws;
let isJoined = false; // Flag to track if the user has joined a group



function join(){
    let groupIdInput = document.getElementById('groupId');
    const messagesDiv = document.getElementById('messages');
    const groupId = groupIdInput.value;
    console.log("Request to join group " + groupId);
    if (groupId && !isJoined) {
        ws = new WebSocket(`ws://localhost:3000`);

        ws.onopen = () => {
            ws.send(JSON.stringify({ type: 'join', groupId }));
            isJoined = true;
            
        };

        ws.onmessage = (event) => {
            const data = JSON.parse(event.data);
            if (data.type === 'message') {
                console.log("Received message: " + event.data);
                messagesDiv.innerHTML += `<div><strong>${data.from}:</strong> ${data.message}</div>`;
            } else if (data.type === 'info') {
                console.log("Received info: " + event.data);
                messagesDiv.innerHTML += `<div>${data.message}</div>`;
            }
            else if (data.type === 'session'){
                console.log("Received session: " + event.data);
                alert("Session expired");
                messagesDiv.innerHTML += `<div>Disconnected from group ${groupId}</div>`;
                isJoined = false;
                clearAllInputFields();
            }
        };

        ws.onclose = () => {
            messagesDiv.innerHTML += `<div>Disconnected from group ${groupId}</div>`;
            isJoined = false; // Reset the flag when disconnected
        };
    }
}

function send(){
    if(!isJoined){
        alert("Please, join a group");
    }
    else{
        let messageInput = document.getElementById('messageInput');
        const message = messageInput.value;
        if (message && ws && ws.readyState === WebSocket.OPEN) {
            ws.send(JSON.stringify({ type: 'message', message }));
            messageInput.value = '';
        }
    }
    
}

function clearAllInputFields() {

    // Select all input fields except buttons
    const inputFields = document.querySelectorAll('input[type="text"], input[type="password"], input[type="email"], input[type="number"], input[type="search"], input[type="tel"], input[type="url"], textarea');

    // Iterate through each input field and set its value to an empty string
    inputFields.forEach(input => {
        input.value = '';
    });
}
