package space.app;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class text_cs extends AppCompatActivity {
    TextView textView1;
    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_text_cs);


        textView1 = findViewById(R.id.textView11);
        textView2 = findViewById(R.id.textView22);

        String text1 = "Bằng việc tiếp tục, bạn đồng ý với <font color='#FF0000'>Điều khoản dịch vụ </font>và";



        String text2 = "<font color='#FF0000'>Chính sách bảo mật</font> của chúng tôi.";
        textView1.setText(Html.fromHtml(text1));
        textView2.setText(Html.fromHtml(text2));

    }


}