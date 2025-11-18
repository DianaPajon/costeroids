var socket = new WebSocket("ws://localhost:8080/ws/game/testclient")
var uuid = null;
var lastMessage = null
document.socket = socket;
socket.onopen = function(e) {
    console.log(e)
};

socket.onmessage = function(event) {
  if(!uuid) uuid = event.data;
  lastMessage = event.data;
};

socket.onclose = function(event) {
  if (event.wasClean) {
    alert(`[close] Conexión cerrada limpiamente, código=${event.code} motivo=${event.reason}`);
  } else {
    // ej. El proceso del servidor se detuvo o la red está caída
    // event.code es usualmente 1006 en este caso
    alert('[close] La conexión se cayó');
  }
};

socket.onerror = function(error) {
  alert(`[error]`);
};