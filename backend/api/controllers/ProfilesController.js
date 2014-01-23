/**
 * ProfilesController
 *
 * @module      :: Controller
 * @description	:: A set of functions called `actions`.
 *
 *                 Actions contain code telling Sails how to respond to a certain type of request.
 *                 (i.e. do stuff, then send some JSON, show an HTML page, or redirect to another URL)
 *
 *                 You can configure the blueprint URLs which trigger these actions (`config/controllers.js`)
 *                 and/or override them with custom routes (`config/routes.js`)
 *
 *                 NOTE: The code you write here supports both HTTP and Socket.io automatically.
 *
 * @docs        :: http://sailsjs.org/#!documentation/controllers
 */

 var geolib = require('geolib');

module.exports = {

  /**
   * Overrides for the settings in `config/controllers.js`
   * (specific to ProfilesController)
   */
  _config: {},
  
  me : function (req,res) {
  	res.json(req.user);
  },
  
  updateme : function (req,res) {
	  User.update({id:req.user.id},req.body,function (err,user) {
		  if (err) {
			  res.json(err);
		  } else {
			  res.json(user);
		  }
	  });
  },
  
  nearyou: function (req,res) {
	  User.find().done(function (err,users) {
		  if (err) {
			  res.json(err);
		  } else {
			  users = users.filter(function (user) {
			    return user.status != null &&
			           user.statuslocation != null && user.id != null;
			  });

              var geopoints = {};
              for (var i = users.length - 1; i >= 0; i--) {
                  var usr = users[i];
                  var loc = JSON.parse(usr.statuslocation);
                  console.log(loc);
                  geopoints[usr.id.toString()] = loc;
              };

              var latitude = req.param("latitude");
              var longitude = req.param("longitude");
              if (latitude == null || longitude == null) {
                res.json({"error":"need to specify latitude and longitude"});
              } else {                  
                  geopoints = geolib.orderByDistance({"latitude": latitude, "longitude": longitude}, geopoints);
                  
                  var points = [];
                  for (var i = users.length - 1; i >= 0; i--) {
                    var usr = users[i];
                    for(var i=0;i<geopoints.length;i++){
                      var obj = geopoints[i];
                      if (usr.id == obj["key"]) {
                        points.push({"user":usr,"latitude":obj["latitude"],"longitude":obj["longitude"],"distance":obj["distance"]});
                        break;
                      }
                    }
                  }

                  res.json(points);
              }
		  }
	  });
  },
  
  profiles: function (req,res) {
    if (req.body != null) {
      User.find(req.body).done(function (err,users) {
      if (err) {
        res.json(err);
      } else {
        res.json(users);
      }
    });
    } else {
      User.find().done(function (err,users) {
      if (err) {
        res.json(err);
      } else {
        res.json(users);
      }
    });
    }
	  
  },

  friends: function(req,res) {
    res.json(req.user.friends);
  },

  addfriend: function(req,res) {
    var friendId = req.param("friendid");
    if (friendid == null) {
      res.send({"error":"need to specifiy friend id to addfriend"});
      return;
    }
    User.findOneById(friendId).done(function (err,user){
        if (err) {
            res.json(err);
        } else {
            var friends = user.friends;
            if (!friends) {
                user.friends = [];
            }
            friends = req.user.friends;
            if (!friends) {req.user.friends = []};
            user.friends.push(req.user.id);
            req.user.friends.push(user.id);
            User.update({id:user.id},{"friends":user.friends}).done(function (err,user) {
                if (err) {
                    res.json(err);
                } else {
                    User.update({id:req.user.id},{"friends":req.user.friends}).done(function (err,user) {
                        if (err) {
                            res.json(err);
                        } else {
                            res.json(req.user.friends);
                        }
                    });
                }
            });
        }
    });
  }
  
};
