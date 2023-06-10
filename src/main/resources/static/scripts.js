var stompClient = null;
var notificationCount = 0;

$(document).ready(function () {
  console.log("Index page is ready");
  connect();

  $("#send").click(function () {
    sendMessage();
  });

  $("#send-private").click(function () {
    sendPrivateMessage();
  });

  $("#notifications").click(function () {
    resetNotificationCount();
  });
});

var incr = (function () {
  var i = 0;

  return function () {
    return i++;
  };
})();

function connect() {
  if (incr() < 3) {
    var socket = new SockJS("/file-attente-socket?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0YXJlayIsInNlcnZpY2UtaWQiOiIxZmY4NWFlNy1lY2ZkLTRmYmQtYmYwZi1kYjg4ODA0MTYzNWYiLCJncm91cC1pZCI6ImQ5ZWVmNGM4LTJkOTktNGZiYi05ZGIxLWVmNDEzN2JjOWE5MiIsInJvbGVzIjpbIkFETUlOIl0sInVzZXItaWQiOiI3ZjNlZDJlNy05YzE3LTQwNGUtOWQ2ZS00NWYyMmEyMGRiMDkiLCJpc3MiOiIwOjA6MDowOjA6MDowOjEiLCJuYW1lIjoidGFyZWsgdGFyZWsiLCJleHAiOjE2NzQ1MTU0NTQsImd1aWNoZXQtbnVtYmVyIjoxLCJndWljaGV0LWlkIjoiM2IxMjZmMmYtZDQwOC00ZTJkLTliNDUtMWNhMDkxMDFkOWVhIn0.mCT1oK-wLKXPctJPVRTBaEN8wHstKVZue-ZEWiwlfls");
  }
  if (incr() > 3) {
    var socket = new SockJS("/file-attente-socket?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0YXJlayIsInNlcnZpY2UtaWQiOiIxZmY4NWFlNy1lY2ZkLTRmYmQtYmYwZi1kYjg4ODA0MTYzNWYiLCJncm91cC1pZCI6ImQ5ZWVmNGM4LTJkOTktNGZiYi05ZGIxLWVmNDEzN2JjOWE5MiIsInJvbGVzIjpbIkFETUlOIl0sInVzZXItaWQiOiI3ZjNlZDJlNy05YzE3LTQwNGUtOWQ2ZS00NWYyMmEyMGRiMDkiLCJpc3MiOiIwOjA6MDowOjA6MDowOjEiLCJuYW1lIjoidGFyZWsgdGFyZWsiLCJleHAiOjE2NzQ1MTU0NTQsImd1aWNoZXQtbnVtYmVyIjoxLCJndWljaGV0LWlkIjoiM2IxMjZmMmYtZDQwOC00ZTJkLTliNDUtMWNhMDkxMDFkOWVhIn0.mCT1oK-wLKXPctJPVRTBaEN8wHstKVZue-ZEWiwlfls");
  }
  stompClient = Stomp.over(socket);

  stompClient.connect({}, function (frame) {
    console.log("Connected: " + frame);
    updateNotificationDisplay();
    stompClient.subscribe("/topic/messages", function (message) {
      showMessage(JSON.parse(message.body).content);
    });

    stompClient.subscribe("/user/topic/private-screen", function (message) {
      showMessage(JSON.parse(message.body).content);
    });

    stompClient.subscribe("/topic/global-notifications", function (message) {
      notificationCount = notificationCount + 1;
      updateNotificationDisplay();
    });

    stompClient.subscribe(
      "/user/topic/private-notifications",
      function (message) {
        notificationCount = notificationCount + 1;
        updateNotificationDisplay();
      }
    );
  });
}

function showMessage(message) {
  $("#messages").append("<tr><td>" + message + "</td></tr>");
}

function sendMessage() {
  console.log("sending message");
  stompClient.send(
    "/ws/message",
    {},
    JSON.stringify({ messageContent: $("#message").val() })
  );
}

function sendPrivateMessage() {
  console.log("sending private message");
  stompClient.send(
    "/ws/private-message",
    {},
    JSON.stringify({ messageContent: $("#private-message").val() })
  );
}

function updateNotificationDisplay() {
  if (notificationCount == 0) {
    $("#notifications").hide();
  } else {
    $("#notifications").show();
    $("#notifications").text(notificationCount);
  }
}

function resetNotificationCount() {
  notificationCount = 0;
  updateNotificationDisplay();
}
