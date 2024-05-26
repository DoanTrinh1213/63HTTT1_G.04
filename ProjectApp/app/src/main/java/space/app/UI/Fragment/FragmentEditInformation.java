package space.app.UI.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.Instant;
import java.util.UUID;

import kotlin.Unit;
import space.app.Activity.MainActivity;
import space.app.R;

public class FragmentEditInformation extends Fragment {

    public FragmentEditInformation() {
        // Required empty public constructor
    }

    private MaterialButton materialButton;
    private ImageView imageView;
    private static final int IMAGE_PICKER_REQUEST_CODE = 101;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_information, container, false);

        ImageView iconBackPerson = view.findViewById(R.id.iconBackPerson);
        iconBackPerson.setOnClickListener(v -> ((MainActivity) getActivity()).replaceFragment(new FragmentMe(), true));

        materialButton = view.findViewById(R.id.imagePersonChange);
        imageView = view.findViewById(R.id.imagePerson);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String imageUrl = sharedPreferences.getString("imageUrl", null);
        if (imageUrl != null) {
            Glide.with(this).load(imageUrl).into(imageView);
        }

        materialButton.setOnClickListener(v -> {
            ImagePicker.with(this)
                    .cropSquare()
                    .compress(1000)
                    .maxResultSize(1000, 1000)
                    .createIntent(intent -> {
                        startActivityForResult(intent, IMAGE_PICKER_REQUEST_CODE);
                        return Unit.INSTANCE;
                    });
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            imageView.setImageURI(uri);
            uploadImageToFirebase(uri);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageToFirebase(Uri uri) {
        if (uri != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();

            String uniqueID = UUID.randomUUID().toString();
            StorageReference imageRef = storageReference.child("images/" + uniqueID);

            UploadTask uploadTask = imageRef.putFile(uri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                imageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                    Toast.makeText(getContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                    saveImageUrl(downloadUri.toString());
                    Glide.with(this).load(downloadUri).into(imageView);
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), "Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void saveImageUrl(String url) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("imageUrl", url);
        editor.apply();
    }
}
