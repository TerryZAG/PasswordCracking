var http = require('http');
var qs = require('querystring');
const {
    isMainThread, parentPort, workerData, threadId,
    MessageChannel, MessagePort, Worker
  } = require('worker_threads');
var data = {  
    url: 'http://localhost:8081'
};//这是需要提交的数据  
var content = qs.stringify(data);
  
var options = {  
    hostname: '127.0.0.1',  
    port: 80,  
    path: '/pc_hearttask/breath?' + content,  
    method: 'GET'  
};

function intervalFunc() {
    // http.get("http://127.0.0.1:80/pc_hearttask/breath?name=http://localhost:8081",function(data){
    // })
    var req = http.request(options, function (res) {  
        console.log('心跳get'); 
    });
    req.end();
}

// req.on('error', function (e) {  
//     console.log('problem with request: ' + e.message);  
// });  
parentPort.on('message', msg => {
    console.log('worker: receive ${msg}');
    if (msg === 1) { process.exit(); }
})
    setInterval(intervalFunc, 1000);
  //setInterval(intervalFunc, 290000);