var mongoose = require('mongoose');

var GunSchema = new mongoose.Schema({
  acquisition_date: { type: Date, default: Date.now },
  firearm_type: String,
  manufacturer: String,
  model: String,
  caliber_gauge: String,
  serial_num: String,
  status: String,
  userID: String
});

module.exports = mongoose.model('Gun', GunSchema);
