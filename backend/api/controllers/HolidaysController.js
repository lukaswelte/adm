/**
 * HolidaysController
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
   * (specific to HolidaysController)
   */
  _config: {},

  // GET
  index: function(req, resource) {
    var year = req.param("year");

    var request = require("request");
    request.get("http://kayaposoft.com/enrico/json/v1.0/index.php?country=ger&action=getPublicHolidaysForYear&year="+year, function (err, res, body) {
        if (!err) {
            var resultsObj = JSON.parse(body);
            //Just an example of how to access properties:
            console.log(resultsObj.MRData);
            resource.send(resultsObj);
        } else {
          resource.err(500);
        }
    });
  }

  
};
