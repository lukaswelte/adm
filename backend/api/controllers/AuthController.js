/**
 * AuthController
 *
 */

var rand = function() {
    return Math.random().toString(36).substr(2); // remove `0.`
};

var token = function() {
    return rand() + rand(); // to make it longer
};

module.exports = {
	
	signup: function (req, res) {
	        var email = req.param("email");
	        var password = req.param("password");
         
	        User.findOneByEmail(email).done(function(err, usr){
	            if (err) {
	                res.send(500, { error: "DB Error" });
	            } else if (usr) {
	                res.send(400, {error: "email already Taken"});
	            } else {
					var hasher = require("password-hash");
					password = hasher.generate(password);
	                User.create({email: email, 
						password: password,
						friends: [],
						token: token()
					}).done(function(error, user) {
	                if (error) {
	                    res.send(500, {error: "DB Error"});
	                } else {
	                    req.user = user;
	                    res.send(user);
	                }
	            });
	        }
	    });
	},
	
	login: function (req, res) {
	    var email = req.param("email");
	    var password = req.param("password");
     
	    User.findOneByEmail(email).done(function(err, user) {
	        if (err) {
	            res.send(500, { error: "DB Error" });
	        } else {
	            if (user) {
	                var hasher = require("password-hash");
	                if (hasher.verify(password, user.password)) {
	                    req.user = user;
	                    res.send(user);
	                } else {
	                    res.send(400, { error: "Wrong Password" });
	                }
	            } else {
	                res.send(404, { error: "User not Found" });
	            }
	        }
	    });
	}
};