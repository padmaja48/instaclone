//UploadActivity.java
package com.example.instaclone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity {

    ImageView imagePreview;
    EditText usernameEt, descriptionEt;
    Button pickImageBtn, uploadBtn, viewFeedBtn;
    Uri imageUri;
    Bitmap selectedBitmap;
    final int IMAGE_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        // Linking UI components
        usernameEt = findViewById(R.id.usernameEt);
        descriptionEt = findViewById(R.id.descriptionEt);
        imagePreview = findViewById(R.id.imagePreview);
        pickImageBtn = findViewById(R.id.pickImageBtn);
        uploadBtn = findViewById(R.id.uploadBtn);
        viewFeedBtn = findViewById(R.id.viewFeedBtn);

        // Choose image button
        pickImageBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, IMAGE_PICK);
        });

        // Upload post button
        uploadBtn.setOnClickListener(v -> {
            String username = usernameEt.getText().toString().trim();
            String caption = descriptionEt.getText().toString().trim();

            if (username.isEmpty() || caption.isEmpty() || selectedBitmap == null) {
                Toast.makeText(this, "Please enter all fields and select an image", Toast.LENGTH_SHORT).show();
                return;
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] bytes = baos.toByteArray();
            String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);

            // Create post data
            Map<String, Object> post = new HashMap<>();
            post.put("username", username);
            post.put("caption", caption);
            post.put("imageBase64", base64Image);
            post.put("timestamp", new Date());
            post.put("likeCount", 0);

            // Upload to Firestore
            FirebaseFirestore.getInstance().collection("Posts")
                    .add(post)
                    .addOnSuccessListener(ref -> {
                        Toast.makeText(this, "Post uploaded successfully", Toast.LENGTH_SHORT).show();
                        // Reset UI
                        usernameEt.setText("");
                        descriptionEt.setText("");
                        imagePreview.setImageResource(android.R.color.transparent);
                        selectedBitmap = null;
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    });
        });

        // View feed
        viewFeedBtn.setOnClickListener(v -> startActivity(new Intent(this, FeedActivity.class)));
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        super.onActivityResult(reqCode, resCode, data);
        if (reqCode == IMAGE_PICK && resCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imagePreview.setImageBitmap(selectedBitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
