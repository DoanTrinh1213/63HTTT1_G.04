package space.app.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import space.app.Adapter.PostAdapter;
import space.app.Model.Post;
import space.app.R;

public class ReviewAllActivity extends AppCompatActivity {

    private RecyclerView rcvReview;
    private PostAdapter mPostAdapter;
    private List<Post> mListPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.fragment_review_all);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ReviewAll), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        initUi();
        getPostData();
    }

    private void initUi() {
        rcvReview = findViewById(R.id.rcvReview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvReview.setLayoutManager(linearLayoutManager);

        mListPost = new ArrayList<>();
        mPostAdapter = new PostAdapter(mListPost);
        rcvReview.setAdapter(mPostAdapter);
    }

    private void getPostData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Post");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListPost.clear(); // Clear the list before adding new data
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    if (post != null) {
                        mListPost.add(post);
                        Log.d("AAAAAAAA","dfgg");
                    }
                }
                mPostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ReviewAllActivity.this, "Lỗi hiển thị đánh giá", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
