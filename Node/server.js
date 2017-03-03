var path = require('path');
var http = require('http');
var socketio = require('socket.io');

var express = require('express');
var app = express();
var server = http.createServer(app);
var io = socketio(server);


app.get('/home', function (req, res) {
  //res.send('root non socket page');
})

  io.on('connection', function(socket){
    console.log('Someone joined');
    socket.emit('newMessage', {from: "Admin", message:"Thanks for joining"})
    socket.on('createMessage', function (data) {
      io.to(data).emit('newMessage', {From: data});
      //  socket.emit('newMessage', {sent: data});
    });
    socket.on('join', function(data){
        socket.join(data)
        console.log("joined")
    })
    socket.on('disconnect', function () {
       console.log("User has disconnected");
    })
    socket.on('questionRequest', function (data) {
      io.to(data).emit('newQuestion', {data: "none"})
    })
    socket.on('questionRemoveRequest', function (data) {
      io.to(data).emit('goneQuestion', {data: "none"})
    })

  });

server.listen(3000, function () {
   console.log("Listening on 3000");
});
