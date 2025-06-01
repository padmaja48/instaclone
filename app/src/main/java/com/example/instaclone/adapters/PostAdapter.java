//adapters(PostAdapter.java)
package com.example.instaclone.adapters;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instaclone.CommentActivity;
import com.example.instaclone.R;
import com.example.instaclone.models.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private final List<Post> postList;
    private final Context context;

    public PostAdapter(List<Post> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        holder.textUsername.setText(post.getUsername());
        holder.textDescription.setText(post.getDescription());
        holder.textLikeCount.setText(String.valueOf(post.getLikeCount()));

        Glide.with(context)
                .load(post.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imagePost);

        DocumentReference likeRef = db.collection("posts")
                .document(post.getPostId())
                .collection("likes")
                .document(userId);

        // Check if user already liked
        likeRef.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                holder.btnLike.setImageResource(R.drawable.baseline_favorite_24); // Liked
                holder.btnLike.setTag("liked");
            } else {
                holder.btnLike.setImageResource(R.drawable.baseline_favorite_border_24); // Not liked
                holder.btnLike.setTag("not_liked");
            }
        });

        holder.btnLike.setOnClickListener(v -> {
            boolean isLiked = "liked".equals(holder.btnLike.getTag());

            DocumentReference postRef = db.collection("posts").document(post.getPostId());

            if (isLiked) {
                // Unlike the post
                likeRef.delete();
                postRef.update("likeCount", post.getLikeCount() - 1);
                post.setLikeCount(post.getLikeCount() - 1);
                holder.textLikeCount.setText(String.valueOf(post.getLikeCount()));
                holder.btnLike.setImageResource(R.drawable.baseline_favorite_border_24);
                holder.btnLike.setTag("not_liked");
            } else {
                // Like the post
                likeRef.set(new java.util.HashMap<>());
                postRef.update("likeCount", post.getLikeCount() + 1);
                post.setLikeCount(post.getLikeCount() + 1);
                holder.textLikeCount.setText(String.valueOf(post.getLikeCount()));
                holder.btnLike.setImageResource(R.drawable.baseline_favorite_24);
                holder.btnLike.setTag("liked");
            }
        });

        holder.btnComment.setOnClickListener(v -> {
            Intent intent = new Intent(context, CommentActivity.class);
            intent.putExtra("postId", post.getPostId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {

        ImageView imagePost;
        TextView textUsername, textDescription, textLikeCount;
        ImageButton btnLike, btnComment;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePost = itemView.findViewById(R.id.imagePost);
            textUsername = itemView.findViewById(R.id.textUsername);
            textDescription = itemView.findViewById(R.id.textDescription);
            textLikeCount = itemView.findViewById(R.id.textLikeCount);
            btnLike = itemView.findViewById(R.id.btnLike);
            btnComment = itemView.findViewById(R.id.btnComment);
        }
    }
}
