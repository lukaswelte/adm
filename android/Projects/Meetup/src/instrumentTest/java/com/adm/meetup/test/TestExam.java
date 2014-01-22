package com.adm.meetup.test;

import android.test.AndroidTestCase;

import com.adm.meetup.calendar.Exam;
import com.google.gson.JsonObject;

/**
 * Created by lukas on 22.01.14.
 */
public class TestExam extends AndroidTestCase {
    public void testAsJsonObject() {
        Exam exam = new Exam(null, "test", "01.01.2014 10:10", null);
        assertNotNull(exam.asJsonObject());

        exam = new Exam("319080931das2", "test", "01.01.2014 10:10", null);
        assertNotNull(exam.asJsonObject());
    }


    public void testExamFromNullShouldThrowException() {
        try {
            new Exam(null);
            fail();
        } catch (Exception e) {
            assertNotNull(e);
        }
    }

    public void testExamFromEmptyJsonObjectShouldThrowException() {
        JsonObject jsonObject = new JsonObject();

        try {
            new Exam(jsonObject);
            fail();
        } catch (Exception e) {
            assertNotNull(e);
        }
    }

    public void testExamFromValidExamJsonObjectShouldBeNotNull() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", "789jdas");
        jsonObject.addProperty("name", "daldas");
        jsonObject.addProperty("date", "01.01.2014 15:00");

        assertNotNull(new Exam(jsonObject));
    }
}
