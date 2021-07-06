var stompClient = null;

window.onload = () => {
    connect();
}

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {

    // nếu dùng cookie thì nó sẽ đc gửi tự động nên k cần lo khoản này
    let headers = {
        "token": localStorage.getItem("token")
    };
    let socket = new SockJS('http://localhost:8080/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect(headers, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/chat/listen', function (greeting) {
            createReceiverMessage(JSON.parse(greeting.body));
        });

        stompClient.subscribe('/user/notice/listen', function (notice) {
            showGreeting(JSON.parse(notice).content);
        });

    }, function (error) {
        console.log(error)
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName({username, content}) {
    stompClient.send("/app/send-chat", {}, JSON.stringify(
        {
            'receiver': username,
            'message': content
        }
    ));
}

function createZoom() {
    stompClient.send("/app/create-zoom", {}, JSON.stringify(
        {
            'receiver': $("#name").val(),
            'message': $("#content").val()
        }
    ));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});