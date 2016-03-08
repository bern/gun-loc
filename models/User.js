var mongoose = require('mongoose');

var UserSchema = new mongoose.Schema({
  email: String,
  password: String,
  information: {
    firearm_licensee_num: String,
    licensee_phone_num: String,
    street_address: String,
    city: String,
    state: String,
    zip: Number
  }
});

module.exports = mongoose.model('User', UserSchema);
