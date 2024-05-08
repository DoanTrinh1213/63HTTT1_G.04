package space.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean("isLoggedIn", false); // Lưu trạng thái đăng nhập là true
//        editor.apply();

        // Kiểm tra trạng thái đăng nhập
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false); // Mặc định là false nếu không có giá trị được lưu trữ
        Log.d("Message", String.valueOf(isLoggedIn));
        if (isLoggedIn == false) {
            setContentView(R.layout.activity_main);
            sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", true); // Lưu trạng thái đăng nhập là true
            editor.apply();
            findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Chuyển sang trang đăng nhập
                    openLoginActivity();
                }
            });
        } else {
            setContentView(R.layout.fragment_home);
            sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", false); // Lưu trạng thái đăng nhập là false khi đăng xuất
            editor.apply();
        }
    }

    private void openLoginActivity() {
        Intent intent = new Intent(MainActivity.this, AuthorizeActivity.class);
        // Bắt đầu Activity mới
        startActivity(intent);
    }

    ;

    //        FrameLayout frameLayout = findViewById(R.id.menuFrame);
//        LayoutInflater inflater = LayoutInflater.from(this);
//        View menuView = inflater.inflate(R.layout.fragment_menu, frameLayout, false);
//        frameLayout.addView(menuView);
//        EdgeToEdge.enable(this);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//           DatabaseReference    firebaseDatabase = FirebaseDatabase.getInstance().getReference("Cafe");
////            Cafe cafe1 = new Cafe("Nana", "Khâm Thiên", "Bánh", 20F, "", "",
////                "", "", "", "");
////            firebaseDatabase.setValue("Hello,world!");
////            firebaseDatabase.setValue(cafe1);
////            FirebaseStorage storage = FirebaseStorage.getInstance();
////            StorageReference storageRef = storage.getReference();
////            String path = "hinh-anh-chill-hoa-bo-hoa_081428881.jpg";
////            StorageReference imageRef = storageRef.child(path);
////            final ImageView imageView = findViewById(R.id.imageView);
////            imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
////                @Override
////                public void onSuccess(byte[] bytes) {
////                    // Chuyển đổi mảng byte thành Bitmap
////                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
////                    // Hiển thị Bitmap lên ImageView
////                    imageView.setImageBitmap(bitmap);
////                }
////            }).addOnFailureListener(new OnFailureListener() {
////                @Override
////                public void onFailure(@NonNull Exception e) {
////                    // Xử lý khi có lỗi xảy ra
////                    Toast.makeText(MainActivity.this, "Không thể tải ảnh từ Firebase Storage", Toast.LENGTH_SHORT).show();
////                }
////            });
//            return insets;
//        });

}