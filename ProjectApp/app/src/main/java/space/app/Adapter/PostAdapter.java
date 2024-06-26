package space.app.Adapter;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import space.app.Interface.HeightWidthOnSet;
import space.app.Interface.RecyclerViewOnClickItem;
import space.app.Model.Post;
import space.app.Model.User;
import space.app.R;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> mListPost;

    public PostAdapter(List<Post> mListPost) {
        this.mListPost = mListPost;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_evalute, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = mListPost.get((position));
        if (post == null) {
            return;
        }
        holder.bind(post);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        firebaseDatabase.getReference("User").child(post.getIdUser()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                holder.textUser.setText(user.getUserName());
                Glide.with(holder.itemView.getContext()).load(user.getImage()).into(holder.userImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(!post.getImagesCommentsPath().isEmpty()){
            storage.getReference().child(post.getImagesCommentsPath()).listAll().addOnCompleteListener(new OnCompleteListener<ListResult>() {
                @Override
                public void onComplete(@NonNull Task<ListResult> task) {
                    List<StorageReference> items = task.getResult().getItems();
                    MutableLiveData<Integer> count = new MutableLiveData<>();
                    count.setValue(items.size());
                    List<Uri> uris =new ArrayList<>();
                    for(StorageReference item:items){
                        int number = items.size();
                        item.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                uris.add(task.getResult());
                                count.setValue(count.getValue()-1);
                            }
                        });
                    }
                    count.observe((LifecycleOwner) holder.itemView.getContext(), new Observer<Integer>() {
                        @Override
                        public void onChanged(Integer integer) {
                            if(integer==0){
                                holder.rcvReviewImage.setAdapter(new ImageAdapter(holder.itemView.getContext(), uris, new RecyclerViewOnClickItem() {
                                    @Override
                                    public void onItemClickImage(Uri uri) {
                                        RecyclerViewOnClickItem.super.onItemClickImage(uri);
                                        space.app.Helper.Dialog.openDialogImage(holder.itemView.getContext(), uri);
                                    }
                                }, new HeightWidthOnSet() {
                                    @Override
                                    public void setParams(View view) {
                                        HeightWidthOnSet.super.setParams(view);
                                        view.getLayoutParams().height = 200;
                                        view.getLayoutParams().width = 200;
                                    }
                                }));
                                holder.rcvReviewImage.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(),RecyclerView.HORIZONTAL,false));
                            }
                        }
                    });
                }
            });
        }
        else{
            Log.d("Image Path","null");
        }
    }

    @Override
    public int getItemCount() {
        return mListPost.size();
    }


    public class PostViewHolder extends RecyclerView.ViewHolder {

        private TextView textUser, textTimeReview, textReview;
        private RatingBar ratingBar;
        private RecyclerView rcvReviewImage;

        private TextView idUser;

        private ImageView userImage;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            textUser = itemView.findViewById(R.id.textUser);
            idUser = itemView.findViewById(R.id.textDescribeUser);
            textTimeReview = itemView.findViewById(R.id.textTimeReview);
            textReview = itemView.findViewById(R.id.textReview);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            rcvReviewImage = itemView.findViewById(R.id.rcvReviewImage);
            userImage = itemView.findViewById(R.id.userImage);
        }

        public void bind(Post post) {
            idUser.setText(post.getIdUser());
            textTimeReview.setText(post.getTimestamp());
            textReview.setText(post.getComment());
            ratingBar.setRating(Float.parseFloat(post.getStar()));
            ratingBar.setIsIndicator(true);
        }
    }
}
