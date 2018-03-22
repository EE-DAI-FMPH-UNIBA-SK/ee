var wsocket;

//window.onload = function () {
connect();
//};

function connect() {
  wsocket = new WebSocket("ws://localhost:8080/KalendarDomacnost/server");
  wsocket.onmessage = onMessage;
  wsocket.onopen = function (e) {
    console.log("Connection established!");
    initCalendars();
  };
}

function onMessage(evt) {
  var data = evt.data.split(";");
  if (data[0] === "calendarEvents") {
    jsonData = data[1];
    showEvents(jsonData);
  } else if (data[0] === "calendars") {
    xmlData = data[1];
    showCalendars(xmlData);
  } else if (data[0] === "calendar") {
    if (data[1] != 0) {
      wsocket.send("calendars");
    }
  } else if (data[0] === "importXML") {
    if (data[1] != 0) {
      wsocket.send("refresh;");
    }
  } else if (data[0] === "importJson") {
    if (data[1] != 0) {
      wsocket.send("calendars;");
    }
  }
}

function initCalendars() {
  wsocket.send("calendars");
}

function showCalendarsEvents(id, name) {
  wsocket.send("events;" + id);
  document.getElementById("calendarName").innerHTML = name;
}

function showEvents(events) {
  $('#events').remove();
  event = document.createElement("div");
  event.setAttribute("id", "events");


  document.getElementById("showCalendar").appendChild(event);
  var obj = JSON.parse(events, function (key, value) {
    if (key == "ranges") {
      value.start = moment(value.start, 'YYYY-MM-DD');
      value.end = moment(value.end, 'YYYY-MM-DD');
      console.log(value);
      return value;
    } else {
      return value;
    }
  });

  console.log(events);

  $('#events').fullCalendar({
    header: {
      left: 'prevYear,prev,next today',
      center: 'title',
      right: 'month,agendaWeek,agendaDay,listWeek'
    },
    navLinks: true, // can click day/week names to navigate views
    editable: true,
    eventLimit: true, // allow "more" link when too many events
    events: obj.events
  });
}

function showCalendars(dataXML) {
  var parser, xmlDoc;
  var txt = "";
  document.getElementById("calendare").innerHTML = "";

  parser = new DOMParser();
  xmlDoc = parser.parseFromString(dataXML, "text/xml");
  calendars = xmlDoc.getElementsByTagName("calendar");

  for (i = 0; i < calendars.length; i++) {
    var input = document.createElement("INPUT");
    input.setAttribute("type", "button");
    id = calendars[i].getAttribute('id');
    name = xmlDoc.getElementsByTagName("name")[i].childNodes[0].nodeValue;

    input.setAttribute("value", name);
    input.setAttribute("onclick", 'showCalendarsEvents(' + id + ', "' + name + '")');

    document.getElementById("calendare").appendChild(input);
  }
}

function addCalendar() {
  name = document.getElementById("form:name").value;
  visible = document.getElementById("form:public").value;
  wsocket.send("addCalendar;name:" + name + ";visible:" + visible);
  document.getElementById("form:name").innerHTML = "";
  document.getElementById("form:public").innerHTML = "";
}

function importEvents() {
  file = document.getElementById("form:xmlUpload").files[0];
  wsocket.send("importXML;" + file.name);
}

function importJson() {
  file = document.getElementById("form:jsonUpload").files[0];
  wsocket.send("importJson;" + file.name);
}