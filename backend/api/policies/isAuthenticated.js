/**
 * isAuthenticated
 *
 * @module      :: Policy
 * @description :: Simple policy to allow any authenticated user
 *                 Assumes that your login action in one of your controllers sets `req.session.authenticated = true;`
 * @docs        :: http://sailsjs.org/#!documentation/policies
 *
 */

  var users = [
      { id: 1, username: 'bob', token: '123456789', email: 'bob@example.com' }
    , { id: 2, username: 'joe', token: 'abcdefghi', email: 'joe@example.com' }
  ]; 

  function findByToken(token, fn) {
	  User.findOneByToken(token).done(function(err, user) {

	    // Error handling
	    if (err) {
	      return fn(err,null);  

	    // The User was found successfully!
	    } else if (!user || user.length<=0) {
	    	return fn(null,null);
	    } else {
	      return fn(null,user);
	    }
	  });
  }

module.exports = function(req, res, next) {
    findByToken(req.param("token"), function(err, user) {
      if (err) { 
		  return res.forbidden('You are not permitted to perform this action.');
	  }
      if (!user) { 
		  return res.forbidden('You are not permitted to perform this action.'); 
	  }
	  delete req.query["token"];
	  req.user = user;
      return next();
    });
};
