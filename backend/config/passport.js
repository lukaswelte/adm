var passport = require('passport'),
BearerStrategy = require('passport-http-bearer').Strategy;
module.exports = {
  express: {
    customMiddleware: function(app){
      console.log('Express midleware for passport');
      app.use(passport.initialize());
      //app.use(passport.session());
    }
  }
};