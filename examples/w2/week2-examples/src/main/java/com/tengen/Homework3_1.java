package com.tengen;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class Homework3_1 {

	public static void main(String[] args) throws Exception {
		MongoClient client = new MongoClient();
		DB db = client.getDB("school");
		DBCollection collection = db.getCollection("students");


		DBCursor cursor = collection.find();
		
		try {
			while (cursor.hasNext()) {
				DBObject nextItem = cursor.next();
				System.out.println(nextItem);
				BasicDBList scores = (BasicDBList)nextItem.get("scores");
				int lowIndex = 0;
				int i = 0;
				double lowestScore = 100.0;
				for(Object scoreObject : scores) {
					BasicDBObject score = (BasicDBObject)scoreObject;
					if(score.get("type").equals("homework")) {
						System.out.println(score);
						if(score.getDouble("score") < lowestScore) {
							lowestScore = score.getDouble("score");
							lowIndex = i;
							System.out.println(" ** New low score " + lowestScore);
						}
					}
					i++;
				}
				System.out.println("lowest score for " + nextItem.get("name") + " was " + lowestScore);
				scores.remove(lowIndex);
				System.out.println("New object: " + nextItem);
				collection.update(new BasicDBObject("_id", nextItem.get("_id")), nextItem);
			}
		} finally {
			cursor.close();
		}

	}

}

