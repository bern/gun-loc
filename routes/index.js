var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');
var request = require('superagent');

var Hit = require('../models/Hit.js');
var Gun = require('../models/Gun.js');
var User = require('../models/User.js');
var FakeHit = require('../models/FakeHit.js');


// Twilio Credentials
var accountSid = 'AC544ce6ea7b29c8e2dea462fa0a21de17';
var authToken = '1395cba102935ce1dacce830c35ab3d7';
//require the Twilio module and create a REST client
var client = require('twilio')('AC544ce6ea7b29c8e2dea462fa0a21de17', '1395cba102935ce1dacce830c35ab3d7');

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

router.get('/dashboard', function(req, res, next) {
  res.render('dashboard', {});
});

router.get('/hit', function(req,res,next) {
  Hit.find(function(err, hits) {
    if (err) {
      res.json(err);
    } else {
      res.json(hits);
    }
  });
});

router.post('/hit', function(req, res, next) {
  Gun.findOne({gun_num: 0}, function(err, gun) {
    if (err) {
      res.send(err);
    } else {
      if (gun.status == 'alert') {
        var newHit = new Hit({gun_id: gun._id});
        newHit.save(function(err, hit) {
          if (err) {
            res.send(err);
          } else {
            text();
            toggleStatus(function(something) {
              console.log(something);
            });
            res.json(hit);
          }
        });
      }
    }
  });
});

router.get('/reset', function(req, res, next) {
  toggleStatus(function(something) {
    res.json(something);
  });
});

router.post('/fakehit', function(req, res, next) {
  Gun.findOne({gun_num: 0}, function(err, gun) {
    if (err) {
      res.send(err);
    } else {
      var newHit = new FakeHit({gun_id: gun._id, hit_at: req.body.hit_at});
      newHit.save(function(err, hit) {
        if (err) {
          res.send(err);
        } else {
          res.json(hit);
        }
      });
    }
  });
});

router.get('/fakehit', function(req,res,next) {
  FakeHit.find(function(err, hits) {
    if (err) {
      res.json(err);
    } else {
      res.json(hits);
    }
  });
});


router.post('/gun', function(req, res, next) {
  var newGun = new Gun(req.body);
  newGun.save(function(err, gun) {
    if (err) {
      res.send(err);
    } else {
      res.json(gun);
    }
  });
});

router.get('/gun', function(req, res, next) {
  Gun.find(function(err, guns) {
    if (err) {
      res.json(err);
    } else {
      res.json(guns);
    }
  });
});

// router.get('/text', function(req, res, next) {
//   client.messages.create({
//     to: "+17274224360",
//     from: "+13057832802",
//     body: "Somebody has touched your gun"
//     // mediaUrl: "http://farm2.static.flickr.com/1075/1404618563_3ed9a44a3a.jpg",
//   }, function(err, message) {
//       if (err) {
//         res.send(err);
//       } else {
//         console.log(message.sid);
//         res.send("Text sent");
//       }
//   });
// });

router.post('/user', function(req,res,next) {
  var newUser = new User(req.body);
  newUser.save(function(err, user) {
    if (err) {
      res.send(err);
    } else {
      res.json(user);
    }
  });
});

router.get('/user', function(req, res, next) {
  User.find(function(err, users) {
    if (err) {
      res.json(err);
    } else {
      res.json(users);
    }
  });
});

function text() {
  client.messages.create({
    to: "+17274224360",
    from: "+13057832802",
    body: "Somebody has touched your gun"
    // mediaUrl: "http://farm2.static.flickr.com/1075/1404618563_3ed9a44a3a.jpg",
  }, function(err, message) {
      if (err) {
        console.log(err);
      } else {
        console.log(message.sid);
        console.log("Text sent");
      }
  });
}
//active, alert, off
function toggleStatus(callback) {
  Gun.findOne({gun_num: 0}, function(err, gun) {
      var status = gun.status;
      if (status.toLowerCase() == 'active') {
        gun.status = 'alert';
      } else if (status.toLowerCase() == 'alert') {
        gun.status = 'active';
      }
      gun.save(function(err, gun) {
        if (err) {
          callback(err);
        } else {
          callback(gun);
        }
      });
  });
}

module.exports = router;
