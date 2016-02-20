var myFirebaseRef = 
	new Firebase("https://gun-loc.firebaseio.com/");

var email = document.getElementById('email');
var password = document.getElementById('password');

myFirebaseRef.on("value", function(snapshot) {
  console.log('updated');
}, function (errorObject) {
  console.log("The read failed: " + errorObject.code);
});

myFirebaseRef.set({
  ping: null,
});

var ping = function() {
  alert('ping');
  myFirebaseRef.set({
    ping: true,
  });
}

var checkAuth = function() {
  var authData = myFirebaseRef.getAuth();
  if (authData) {
    console.log("User " + authData.uid + " is logged in with " + authData.provider);
  } else {
    console.log("User is logged out");
  }
}

function authHandler(error, authData) {
  if (error) {
    console.log("Login Failed!", error);
  } else {
    console.log("Authenticated successfully with payload:", authData);
  }
}

var login = function() {
  myFirebaseRef.authWithPassword({
    email    : email.value,
    password : password.value
  }, authHandler);
}

var logout = function() {
  myFirebaseRef.unauth();
}

var createUser = function() {
  myFirebaseRef.createUser({
  email    : email.value,
  password : password.value
  }, function(error, userData) {
    if (error) {
      console.log("Error creating user:", error);
    } else {
      console.log("Successfully created user account with uid:", userData.uid);
    }
  });
}

document.getElementById('suh').onclick = ping;
document.getElementById('log').onclick = checkAuth;
document.getElementById('login').onclick = login;
document.getElementById('register').onclick = createUser;
document.getElementById('logout').onclick = logout;