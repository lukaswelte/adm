var assert = require('assert')
, add = require('./../api/controllers/ExamController').add;


describe('ExamController', function(){
    describe('#add()', function(){
      it('should return 4 when the value is not present', function(){
        assert.equal(4, add(2,2));
      })
	  
      it('should return 1 when the value is not present', function(){
        assert.equal(1, add(0,1));
      })
    })
  })