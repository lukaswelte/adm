/**
 * User
 *
 * @module      :: Model
 * @description :: A short summary of how this model works and what it represents.
 *
 */ 
module.exports = {
 
  attributes: {
    email: {
      type: 'string',
      required: true,
      unique: true
    },
    password: {
      type: 'string',
      required: true
    },
	token: {
		type: 'string'
	},
  friends: 'array',
	status: 'string',
	statuslocation: 'json', //e.g. {"latitude": 51.5103, "longitude": 7.49347} 
	appartmentlocation: 'json', //e.g. {"latitude": 51.5103, "longitude": 7.49347} 
    toJSON: function() {
      var obj = this.toObject();
      delete obj.password;
      return obj;
    }
  }
};