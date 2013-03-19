/*
 * Copyright (c) 2008 - 2013 10gen, Inc. <http://10gen.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package course;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import com.sun.org.apache.bcel.internal.generic.ACONST_NULL;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BlogPostDAO {
    DBCollection postsCollection;

    public BlogPostDAO(final DB blogDatabase) {
        postsCollection = blogDatabase.getCollection("posts");
    }

    // Return a single post corresponding to a permalink
    public DBObject findByPermalink(String permalink) {

        DBObject post = null;
        // XXX HW 3.2,  Work Here
        DBCursor cursor = postsCollection.find(new BasicDBObject("permalink", permalink));
        if(cursor.hasNext()) {
        	post = cursor.next();
        }


        return post;
    }

    // Return a list of posts in descending order. Limit determines
    // how many posts are returned.
    public List<DBObject> findByDateDescending(int limit) {

        List<DBObject> posts = new ArrayList<DBObject>();
		DBCursor cursor = postsCollection.find().sort(new BasicDBObject("date", -1));
		cursor.limit(limit);
		while(cursor.hasNext()) {
			posts.add(cursor.next());
		}
		cursor.close();
        // XXX HW 3.2,  Work Here
        // Return a list of DBObjects, each one a post from the posts collection

        return posts;
    }


    public String addPost(String title, String body, List tags, String username) {

        System.out.println("inserting blog entry " + title + " " + body);

        String permalink = title.replaceAll("\\s", "_"); // whitespace becomes _
        permalink = permalink.replaceAll("\\W", ""); // get rid of non alphanumeric
        permalink = permalink.toLowerCase();


        BasicDBObject post = new BasicDBObject();
        post.put("title", title);
        post.put("body", body);
        BasicDBList tagList = new BasicDBList();
        for(String tag: (List<String>)tags) {
        	tagList.add(tag);
        }
        post.put("tags", new BasicDBList());
        post.put("permalink", permalink);
        post.put("comments", new BasicDBList());
        post.put("author", username);
        post.put("date", new Date());
        // XXX HW 3.2, Work Here
        // Remember that a valid post has the following keys:
        // author, body, permalink, tags, comments, date
        //
        // A few hints:
        // - Don't forget to create an empty list of comments
        // - for the value of the date key, today's datetime is fine.
        // - tags are already in list form that implements suitable interface.
        // - we created the permalink for you above.

        // Build the post object and insert it
        postsCollection.insert(post);

        return permalink;
    }




   // White space to protect the innocent








    // Append a comment to a blog post
    public void addPostComment(final String name, final String email, final String body,
                               final String permalink) {

        DBObject post = findByPermalink(permalink);
        if(post == null) {
        	// can't find the post, so discard output
        	System.err.println("Couldn't find post with permalink " + permalink + " ");
        	return;
        }
        BasicDBList comments = (BasicDBList)post.get("comments");
        BasicDBObject newComment = new BasicDBObject();
        newComment.put("author", name);
        newComment.put("body", body);
        if(email != null) {
        	newComment.put("email", email);
        }
        comments.add(newComment);
        postsCollection.update(new BasicDBObject("_id", post.get("_id")), post);
        // TODO: revisit using an operator to append instead of brute forcing as above ...
        // XXX HW 3.3, Work Here
        // Hints:
        // - email is optional and may come in NULL. Check for that.
        // - best solution uses an update command to the database and a suitable
        //   operator to append the comment on to any existing list of comments



    }


}
