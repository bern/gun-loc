var express = require('express');
var app = express();
app.get('/', function(req, res) {
  res.send('#yolo\n');
});
app.listen(3001);
console.log('Listening on port 3001...');
