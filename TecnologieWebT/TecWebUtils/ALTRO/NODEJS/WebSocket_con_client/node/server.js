const WebSocket = require('ws');
const fs = require('fs');
const path = require('path');

const wss = new WebSocket.Server({ port: 3000 });

const sessions = {}; // Per tenere traccia delle sessioni degli utenti
const chatLogs = {}; // Per tenere traccia dei log delle chat

wss.on('connection', (ws) => {
    let userId = null;
    let groupId = null;
    let sessionTimeout = null;

    ws.on('message', (message) => {
        const data = JSON.parse(message);
        console.log("Received message: " + message + (userId?" from "+ userId:"") );

        // Fase di inizio sessione
        if (data.type === 'join') {
            groupId = data.groupId;
            userId = ws._socket.remoteAddress + ':' + ws._socket.remotePort; // ID utente basato su IP e porta

            if (!sessions[groupId]) {
                sessions[groupId] = {
                    //users: {},
                    messageCount: 0, // Contatore dei messaggi per il gruppo
                    startTime: new Date().toISOString(),
                    endTime: 0,
                };
            }

            sessions[groupId][userId] = { socket: ws, timestamp: Date.now() };
            //console.log("sessions[groupId][userId]: " + JSON.stringify(sessions[groupId][userId]));

            //console.log("sessions[groupId]: " + JSON.stringify(sessions[groupId]));
            //console.log("sessions[groupId][userId].socket: " + JSON.stringify(sessions[groupId][userId].socket));


            // Imposta un timeout per la sessione
            if (sessionTimeout) {
                clearTimeout(sessionTimeout);
            }
            sessionTimeout = setTimeout(() => {
                console.log("Timeout expired for user " + userId);
                ws.send(JSON.stringify({ type: 'session', message: 'Session timeout for user ' + userId }));
                delete sessions[groupId][userId];
                let noMoreUser = true;

                Object.values(sessions[groupId]).forEach(session => {
                    if (session.socket) {
                        noMoreUser = false;
                    }
                });

                if(noMoreUser){
                    console.log("No more user for group " + groupId);
                    console.log("Saving chat log...");
                    sessions[groupId].endTime = new Date().toISOString();
                    saveChatLog(groupId);
                    delete sessions[groupId];
                }

                /*if (Object.keys(sessions[groupId]).length === 0) {
                    console.log("No more user for group " + groupId);
                    console.log("Saving chat log...");
                    sessions[groupId].endTime = new Date().toISOString();
                    saveChatLog(groupId);
                    delete sessions[groupId];
                }*/
            }, 1 * 30 * 1000); // 

            ws.send(JSON.stringify({ type: 'info', message: 'Joined group ' + groupId }));
            return;
        }

        // Invia messaggio
        if (data.type === 'message' && groupId) {
            console.log("Invio del messaggio '" + data.message + "'...");
            //console.log("Aumento il counter...");
            sessions[groupId].messageCount++; 
            //console.log("Counter: " + sessions[groupId].messageCount);
            if (!/\d/.test(data.message)) { // Controlla se il messaggio contiene numeri
                Object.values(sessions[groupId]).forEach(session => {
                    //console.log("session: " + JSON.stringify(session));
                    if (session.socket && session.socket.readyState === WebSocket.OPEN) {
                        session.socket.send(JSON.stringify({ type: 'message', message: data.message, from: userId }));
                        console.log("Messaggio inviato con successo");
                    }
                });
            }
        }




    });

    ws.on('close', () => {
        if (groupId && sessions[groupId]) {
            delete sessions[groupId][userId];

            if (Object.keys(sessions[groupId].userId).length === 0) {
                console.log("No more user for group " + groupId);
                console.log("Saving chat log...");
                sessions[groupId].endTime = new Date().toISOString();
                saveChatLog(groupId);
                delete sessions[groupId];
            }
        }
    });
});

function saveChatLog(groupId) {
    const logData = {
        groupId,
        messageCount: sessions[groupId].messageCount,
        startTime: sessions[groupId].startTime,
        endTime: sessions[groupId].endTime,
    };

    console.log("Data to be logged: " + JSON.stringify(logData));

    const logFilePath = path.join(__dirname, 'chat_logs.json');
    fs.appendFileSync(logFilePath, JSON.stringify(logData) + '\n', 'utf8');
    console.log(`Chat log saved for group ${groupId}`);
}

console.log('Server is running on ws://localhost:3000');
