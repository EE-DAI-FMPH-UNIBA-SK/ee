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
  userId = document.getElementById("form:userId").value;
  var data = evt.data.split(";");
  if (data[0] === "calendarEvents") {
    jsonData = data[1];
    showEvents(jsonData);
  } else if (data[0] === "calendars") {
    xmlData = data[1];
    showCalendars(xmlData);
  } else if (data[0] === "calendar") {
    if (data[1] != 0) {
      wsocket.send("calendars;" + userId);
    }
  } else if (data[0] === "importXML") {
    if (data[1] != 0) {
      wsocket.send("refresh;" + userId);
    }
  } else if (data[0] === "importJson") {
    if (data[1] != 0) {
      wsocket.send("calendars;" + userId);
    }
  }
}

function initCalendars() {
  userId = document.getElementById("form:userId").value;
  wsocket.send("calendars;" + userId);
}

function showCalendarsEvents(id, name) {
  userId = document.getElementById("form:userId").value;
  wsocket.send("events;" + id + ";" + userId);
  document.getElementById("calendarName").innerHTML = name;
}

function showEvents(events) {
  $('#events').remove();
  event = document.createElement("div");
  event.setAttribute("id", "events");


  document.getElementById("showCalendar").appendChild(event);
  var obj = JSON.parse(events);

  $('#events').fullCalendar({
    header: {
      left: 'prevYear,prev,next today',
      center: 'title',
      right: 'month,agendaWeek,agendaDay,listWeek'
    },
    navLinks: true, // can click day/week names to navigate views
    editable: true,
    eventLimit: true, // allow "more" link when too many events
    eventRender: function (event, element, view) {
      if (event.ranges != null) {
        return (event.ranges.filter(function (range) {
          return (event.start.isBefore(range.end) &&
                  event.end.isAfter(range.start));
        }).length) > 0;
      }
    },
    eventClick: function (calEvent, jsEvent, view) {

      wsocket.send("showEvent;" + calEvent.id);
      console.log(calEvent.id)

    },
    events: function (start, end, timezone, callback) {
      var events = obj.events;
      callback(events);
    }
  });
}

function showCalendars(dataXML) {
  var parser, xmlDoc;
  var txt = "";
  document.getElementById("calendars").innerHTML = "";

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

    document.getElementById("calendars").appendChild(input);
  }
}

function addCalendar() {
  name = document.getElementById("form:name").value;
  visible = document.getElementById("form:public").value;
  userId = document.getElementById("form:userId").value;
  wsocket.send("addCalendar;name:" + name + ";visible:" + visible + ";" + userId);
  document.getElementById("form:name").innerHTML = "";
  document.getElementById("form:public").innerHTML = "";
}

function importEvents() {
  file = document.getElementById("form:importXML:xmlFile").files[0];
  userId = document.getElementById("form:userId").value;
  wsocket.send("importXML;" + file.name + ";" + userId);
}

function importJson() {
  file = document.getElementById("form:importJson:jsonFile").files[0];
  userId = document.getElementById("form:userId").value;
  wsocket.send("importJson;" + file.name + ";" + userId);
}