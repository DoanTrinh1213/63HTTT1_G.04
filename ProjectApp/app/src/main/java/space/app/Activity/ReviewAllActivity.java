package space.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import space.app.Helper.PostHelper;
import space.app.Model.Cafe;
import space.app.Model.Post;
import space.app.R;
import space.app.Activity.RewriteActivity;

public class ReviewAllActivity extends AppCompatActivity {

    private RecyclerView rcvReview;
    private PostAdapter mPostAdapter;
    private Button btnExperience;
    private List<Post> mListPost;
    private TextView textNumberReviews;

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
        btnExperience = findViewById(R.id.btnExperience);
//        btnExperience.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ReviewAllActivity.this, RewriteActivity.class);
//                startActivity(intent);
//            }
//        });

        Intent intent = getIntent();
        Cafe cafe = (Cafe) intent.getSerializableExtra("rewriteCafe");

        initUi();
        getPostData(cafe);
    }

    private void initUi() {
        rcvReview = findViewById(R.id.rcvReview);
        textNumberReviews =findViewById(R.id.textNumberReviews);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvReview.setLayoutManager(linearLayoutManager);
    }

    private void getPostData(Cafe cafe) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Post");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListPost = PostHelper.getInstance().getAllPostByIdCafe(cafe.getIdCafe());
                mPostAdapter = new PostAdapter(mListPost);
                rcvReview.setAdapter(mPostAdapter);
                textNumberReviews.setText(mListPost.size() + " đánh giá");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ReviewAllActivity.this, "Lỗi hiển thị đánh giá", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
