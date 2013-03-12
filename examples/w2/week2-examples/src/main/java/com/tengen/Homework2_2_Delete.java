package com.tengen;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

public class Homework2_2_Delete {

	public static void main(String[] args) throws UnknownHostException {
		MongoClient client = new MongoClient();
		DB db = client.getDB("students");
		DBCollection collection = db.getCollection("grades");


		DBCursor cursor = collection.find(new BasicDBObject("type", "homework")).sort(new BasicDBObject("student_id", 1).append("score", -1));
		
		int previous_id = -1;
		Object lowestScoreForPreviousId = null;
		Object lastRecordId = null;
		
		try {
			while (cursor.hasNext()) {
				DBObject nextItem = cursor.next();
				int student_id = (Integer)nextItem.get("student_id");
				if(previous_id == -1) {
					System.out.println("*** first id = " + student_id);
					previous_id = student_id;
				}
				System.out.println(nextItem);
				if(student_id != previous_id) {
					System.out.println("**** Drop lowest score " + lowestScoreForPreviousId + " for id " + previous_id);
					previous_id = student_id;
					System.out.println("remove id " + lastRecordId);
					collection.remove(new BasicDBObject("_id", lastRecordId));
				}
				lowestScoreForPreviousId = nextItem.get("score");
				lastRecordId = nextItem.get("_id");
			}
			System.out.println("**** Drop lowest score " + lowestScoreForPreviousId + " for id " + previous_id);
			System.out.println("remove id " + lastRecordId);
			collection.remove(new BasicDBObject("_id", lastRecordId));
		} finally {
			cursor.close();
		}

	}

}
