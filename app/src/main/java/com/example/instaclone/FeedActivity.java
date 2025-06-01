//FeedActivity.java
package com.example.instaclone;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instaclone.adapters.PostAdapter;
import com.example.instaclone.models.Post;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Post> postList = new ArrayList<>();
    PostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new PostAdapter(postList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        FirebaseFirestore.getInstance().collection("Posts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        postList.clear();
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            String postId = doc.getId();
                            String caption = doc.getString("caption");
                            String base64 = doc.getString("imageBase64");
                            String username = doc.getString("username");
                            Long likeCountLong = doc.getLong("likeCount");

                            int likeCount = likeCountLong != null ? likeCountLong.intValue() : 0;

                            // Convert base64 to data URI string
                            String imageUrl = "data:image/png;base64," + base64;

                            postList.add(new Post(postId, imageUrl, username, caption, likeCount));
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
