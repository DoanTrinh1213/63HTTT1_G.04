package space.app.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import space.app.Model.Post;
import space.app.R;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> mListPost;

    public PostAdapter(List<Post> mListPost) {
        this.mListPost = mListPost;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_review_all, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = mListPost.get((position));
        if (post == null) {
            return;
        }
        holder.bind(post);

    }

    @Override
    public int getItemCount() {
        return mListPost.size();
    }


    public class PostViewHolder extends RecyclerView.ViewHolder {

        private TextView textUser, textTimeReview, textReview;
        private RatingBar ratingBar;
        private RecyclerView rcvReviewImage;

        private ImageView userImage;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            textUser = itemView.findViewById(R.id.textUser);
            textTimeReview = itemView.findViewById(R.id.textTimeReview);
            textReview = itemView.findViewById(R.id.textReview);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            rcvReviewImage = itemView.findViewById(R.id.rcvReviewImage);
        }

        public void bind(Post post) {
            textUser.setText(post.getIdUser());
            textTimeReview.setText(post.getTimestamp());
            textReview.setText(post.getComment());
            ratingBar.setRating(Float.parseFloat(post.getStar()));
        }
    }
}
