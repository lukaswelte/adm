/**
 * ExamController
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
   * (specific to ExamController)
   */
  _config: {},
  
  create: function(req, res) {
	  Exam.create({
		  "name": req.param("name"),
		  "date": req.param("date"),
		  "userid": req.user.id,
		  "notifydate": req.param("notifydate")
	  }).done(function(err, exam) {

  // Error handling
  if (err) {
    res.json(err);

  // The User was created successfully!
  } else {
	  res.json(exam);
  }
  });
},

read: function(req,res) {
  Exam.find({"userid":req.user.id}).done(function(err,exams){
	  if (err){

		  res.json(err);
	  } else {
		  res.json(exams);
	  }
  });
}

};