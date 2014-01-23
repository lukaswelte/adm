/**
 * Routes
 *
 * Sails uses a number of different strategies to route requests.
 * Here they are top-to-bottom, in order of precedence.
 *
 * For more information on routes, check out:
 * http://sailsjs.org/#documentation
 */

module.exports.routes = {

  // By default, your root route (aka home page) points to a view
  // located at `views/home/index.ejs`
  // 
  // (This would also work if you had a file at: `/views/home.ejs`)
  '/': {
    view: 'home/index'
  },
  
  'get /exam': {
      controller: 'exam',
      action: 'read'
    },
	
	 
	'get /profiles': {
		controller: 'profiles',
		action: 'profiles'
	},

	'post /profiles': {
		controller: 'profiles',
		action: 'profiles'
	},
	
	'put /profiles/me': {
		controller: 'profiles',
		action: 'updateme'
	}
	 
};
 
