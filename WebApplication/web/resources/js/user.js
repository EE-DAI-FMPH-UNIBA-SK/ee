var wsocket = null;

window.onload = function () {
  connect();
};


function connect() {
  wsocket = new WebSocket("ws://localhost:8080/KalendarDomacnost/server");
  wsocket.onopen = function (e) {
    console.log("Connection established!");
  };
}