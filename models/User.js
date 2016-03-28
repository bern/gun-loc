var mongoose = require('mongoose');

var UserSchema = new mongoose.Schema({
  name: String,
  email: String,
  password: String,
  information: {
    firearm_licensee_num: String,
    licensee_phone_num: String,
    street_address: String,
    city: String,
    state: String,
    zip: String
  }
});

module.exports = mongoose.model('User', UserSchema);
