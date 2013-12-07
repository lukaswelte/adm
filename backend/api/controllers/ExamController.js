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
  _config: {}

};

function nextPrime(n) {
  var smaller;
  n = Math.floor(n);

  if (n >= 2) {
    smaller = 1;
    while (smaller * smaller <= n) {
      n++;
      smaller = 2;
      while ((n % smaller > 0) && (smaller * smaller <= n)) {
        smaller++;
      }
    }
    return n;
  } else {
    return 2;
  }
}

function add(n,a) {
	return n+a;
}
module.exports.add = add;
module.exports.nextPrime = nextPrime;