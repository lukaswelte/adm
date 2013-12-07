var assert = require('assert')
, exam = require('./../api/models/Exam');


describe('Exam', function(){
    describe('#add()', function(){
      it('should return 4 when the value is not present', function(){
		  exam.name = "Hans";
        assert.equal("Hans", exam.name);
      })
    })
  })