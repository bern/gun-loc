var mongoose = require('mongoose');

var HitSchema = new mongoose.Schema({
  hit_at: { type: Date, default: Date.now },
  gun_id: {type: mongoose.Schema.Types.ObjectId, ref: "Gun"}
});

module.exports = mongoose.model('FakeHit', HitSchema);
