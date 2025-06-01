//MainActivity.java
package com.example.instaclone;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instaclone.adapters.PostAdapter;
import com.example.instaclone.models.Post;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PostAdapter postAdapter;
    ArrayList<Post> postList;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList, this);
        recyclerView.setAdapter(postAdapter);

        db = FirebaseFirestore.getInstance();

        loadPostsFromFirestore();
    }

    private void loadPostsFromFirestore() {
        db.collection("posts")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    postList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        String postId = doc.getId();
                        String username = doc.getString("username"); // ✅ fetched username
                        String description = doc.getString("description");
                        String base64Image = doc.getString("image");
                        Long likeCountLong = doc.getLong("likeCount");
                        int likeCount = (likeCountLong != null) ? likeCountLong.intValue() : 0;

                        // Convert base64 to Data URI for Glide
                        String imageUrl = "data:image/png;base64," + base64Image;

                        Post post = new Post(postId, imageUrl, username, description, likeCount);
                        postList.add(post);
                    }
                    postAdapter.notifyDataSetChanged();
                });
    }
}
