//CommentActivity.java
package com.example.instaclone;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instaclone.adapters.CommentAdapter;
import com.example.instaclone.models.Comment;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText editComment;
    Button btnSend;
    CommentAdapter adapter;
    List<Comment> commentList = new ArrayList<>();
    FirebaseFirestore db;
    String postId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        recyclerView = findViewById(R.id.recyclerViewComments);
        editComment = findViewById(R.id.editComment);
        btnSend = findViewById(R.id.btnSend);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommentAdapter(commentList, this);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        postId = getIntent().getStringExtra("postId");

        loadComments();

        btnSend.setOnClickListener(v -> {
            String text = editComment.getText().toString().trim();
            if (!text.isEmpty()) {
                Map<String, Object> commentMap = new HashMap<>();
                commentMap.put("username", "CurrentUser"); // Replace with logged-in user's name
                commentMap.put("commentText", text);

                db.collection("posts").document(postId)
                        .collection("comments")
                        .add(commentMap)
                        .addOnSuccessListener(documentReference -> {
                            editComment.setText("");
                            loadComments();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to comment", Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }

    private void loadComments() {
        db.collection("posts").document(postId)
                .collection("comments")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    commentList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        commentList.add(new Comment(
                                doc.getString("username"),
                                doc.getString("commentText")));
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}
