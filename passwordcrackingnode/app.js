var express=require('express');
var bodyParser = require('body-parser');
const {
  isMainThread, parentPort, workerData, threadId,
  MessageChannel, MessagePort, Worker
} = require('worker_threads');
var md5 = require('md5-node');

//var fs=require("fs");
var app=new express();
//配置body-parser中间件
// 创建 application/x-www-form-urlencoded 编码解析
app.use(bodyParser.urlencoded({limit:'500mb', extended: false }))
//处理json数据
app.use(bodyParser.json({limit:'500mb'}));
app.post('/passwordcrackingnodejs', function (req, res) {
    worker = new Worker('./worker.js');
    var resl = '0';//破解失败返回‘0’
    //var point = 1;
    var arr = eval('(' + JSON.parse(req.body.dir) + ')');//字典
    var pc_md5 = req.body.pc_md5;//MD5
    //console.log('开始破解');
    arr.some(function (item) {
      if(pc_md5 == md5(item).substr(8, 16)){
          resl = '1'+item;//破解成功返回‘1’
          //console.log('结束破解，结果为：'+item);
          return false;//break
      }
    });
    worker.postMessage(1);
    //worker.on('exit', code => { console.log(`main: worker stopped with exit code ${code}`); });
    res.send(resl);
 })
// app.post('/reciveFile',function(req,res){
//     var app = new Buffer.from(req.body.appFileStr, 'base64');
//     fs.writeFileSync("D:/upload/writeFileTest.zip",app);//注意：writeFile写入的文件zip打不开，具体原因不明，同道高人如果知晓，希望能够指点一二
//     res.send({retMsg: '收到发送文件流请求，正在接收', retCode: "1"});
// })
// app.listen(8089);
// app.get('/', function (req, res) {
//     res.send('Hello World');
//  })
var server = app.listen(8081, function () {
 
  var host = server.address().address
  var port = server.address().port
 
  console.log("访问地址为 http://%s:%s", host, port)
 
})