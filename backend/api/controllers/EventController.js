/**
 * EventController
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

module.exports = {
    
  


  /**
   * Overrides for the settings in `config/controllers.js`
   * (specific to EventController)
   */
  _config: {},

  query: function (req,res) {  
    var body = req.body;
  	if (body) {
      var requestID = body.id;
      if (requestID) {
        body.id = Number(requestID);
      }
  		Event.find(body).done(function(err, events) { 
  			if (err) res.send(500, {error: err});
  			else res.send(events);
  		 });
  	} else {
      var requestID = req.param("id");
      if (requestID) {
        Event.find({ id: requestID }).done(function(err, ev) { 
        if (err) res.send(500, {error: err});
        else res.send(ev);
       });
      } else {
        Event.find().done(function(err, events) { 
        if (err) res.send(500, {error: err});
        else res.send(events);
       });
      }
  		
  	}
  }

  
};
