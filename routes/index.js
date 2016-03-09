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
var client = require('twilio')(accountSid, authToken);

var api_key = 'key-73bcc4a3cf8233071f7471b6579d4426';
var domain = 'sandboxd7db69b069af4337a18b514bb0f7a12d.mailgun.org';
var mailgun = require('mailgun-js')({apiKey: api_key, domain: domain});

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

router.get('/dashboard', function(req, res, next) {
  res.render('dashboard', {});
});

router.get('/register', function(req, res, next) {
  res.render('registration', {});
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
  console.log("Hit post");
  Gun.findOne({gun_num: 0}, function(err, gun) {
    if (err) {
      res.send(err);
    } else {
      console.log("Gun found");
      if (gun.status == 'active') {
        var newHit = new Hit({gun_id: gun._id});
        newHit.save(function(err, hit) {
          if (err) {
            res.send(err);
          } else {
            text();
            email();
            makeAlert(function(something) {
              console.log(something);
            });
            res.json(hit);
          }
        });
      } else {
        res.send("Gun already on alert please reset");
      }
    }
  });
});

router.post('/register', function(req, res, next) {
  var bod = req.body;
  bod.password = hashCode(bod.password);
  User.findOne({email: bod.email}, function(err, foundUser) {
    if (err) {
      res.status(500).send(err);
    }
    if (foundUser) {
      res.json({found: true});
    } else {
      var usr = new User(bod);
      usr.save(function(err, newUser) {
          if (err) {
            res.status(500).send(err);
          } else {
            req.session.userID = newUser._id;
            res.json({found: false});
          }
      });
    }
  });
});

router.post('/login', function(req, res, next) {
  var bod = req.body;
  console.log(bod);
  User.findOne({email: bod.email}, function(err, foundUser) {
    if (foundUser) {
      var curPass = hashCode(bod.password);
      if (curPass == foundUser.password) {
        res.json({gucci: true});
      } else {
        res.json({gucci: false});
      }
    } else {
      res.json({gucci: false});
    }
  });
});

router.get('/reset', function(req, res, next) {
  makeActive(function(something) {
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
    // to: "+17274224360",
    to: "+19546097527",
    from: "+13057832802",
    body: "We've detected someone tampering with your Colt Expanse M4 housed at 201 N Goodwin Ave Urbana, IL 61801. The timestamp of the incident is "+new Date()+".\n\nWe recommend that you get in contact with anyone that might be at that location. If you are unable to reach them, we recommend calling local authorities to that location.\n\nThis is an automated SMS sent by Gun Loc. Please do not respond to this message.",
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
function makeActive(callback) {
  Gun.findOne({gun_num: 0}, function(err, gun) {
      var status = gun.status;
      if (status.toLowerCase() != 'active') {
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

function makeAlert(callback) {
  Gun.findOne({gun_num: 0}, function(err, gun) {
      var status = gun.status;
      if (status.toLowerCase() != 'alert') {
        gun.status = 'alert';
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

function email() {
  var data = {
    from: 'Gun-Loc <me@sandboxd7db69b069af4337a18b514bb0f7a12d.mailgun.org>',
    to: 'gunloctest@outlook.com',
    subject: 'ALERT',
    text: "We've detected someone tampering with your Colt Expanse M4 housed at 201 N Goodwin Ave Urbana, IL 61801. The timestamp of the incident is "+new Date()+".\n\nWe recommend that you get in contact with anyone that might be at that location. If you are unable to reach them, we recommend calling local authorities to that location.\n\nThis is an automated message sent by Gun Loc. Please do not respond to this message."
  };

  mailgun.messages().send(data, function (error, body) {
    console.log(body);
  });
}

function hashCode(str) {
  var hash = 5381;
  for (i = 0; i < str.length; i++) {
    char = str.charCodeAt(i);
    hash = ((hash << 5) + hash) + char; /* hash * 33 + c */
  }
  return hash;
}


module.exports = router;
