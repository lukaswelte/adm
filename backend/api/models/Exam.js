/**
 * Exam
 *
 * @module      :: Model
 * @description :: A short summary of how this model works and what it represents.
 * @docs		:: http://sailsjs.org/#!documentation/models
 */

module.exports = {

  attributes: {
  	
  	/* e.g.
  	nickname: 'string'
  	*/
	name: {
		type: 'string',
		required: true
	},
    date: {
    	type: 'date',
		required: true
    },
    notifydate: 'date',
	userid: {
		type: 'integer',
		required: true
	}  
    
  }

};
