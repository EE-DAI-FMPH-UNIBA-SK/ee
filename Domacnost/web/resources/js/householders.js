window.onload = function ()
{
  connect();
}

var wsocket;
function connect() {
  wsocket = new WebSocket("ws://localhost:8080/Domacnost/compute");
  wsocket.onmessage = onMessage;
  wsocket.onopen = function (e) {
    console.log("Connection established!");
    userId = document.getElementById("household:userId").value;
    console.log(userId);
    wsocket.send("userId;" + userId);
  };
}

function onMessage(evt) {
  console.log(evt.data);
  var data = evt.data.split(";");
  if (data[0] === "freeTime") {
    for (var i = 1; i < data.length; i++) {
      var lbl = document.createElement('label');
      lbl.innerHTML = data[i];
      document.getElementById('freeTime').appendChild(lbl);
    }
    document.getElementById("freeTimeForm").style.visibility = "visible";
    document.getElementById("household:message").innerHTML = "";
  } else if (data[0] === "event") {
    document.getElementById("freeTimeForm").style.visibility = "hidden";
    document.getElementById("household:message").innerHTML = data[1];
    document.getElementById('freeTime').innerHTML = "";
  }
}