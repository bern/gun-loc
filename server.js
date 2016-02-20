var express = require('express');
var app = express();

require('./routes')(app);

/*app.configure(function(){
  app.use(express.bodyParser());
});

app.get('/', function(req, res) {
  res.send('#yolo\n');
});

app.get('/musician/:name', function(req, res) {
  
  // Get /musician/Matt
  console.log(req.params.name);
  // => Matt

  res.send('{"id": 1,"name": "Matt",
    "band": "BBQ Brawlers"}');

});*/

app.listen(3001);
console.log('Listening on port 3001...');
