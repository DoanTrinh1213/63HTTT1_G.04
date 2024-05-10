package space.app.UI;

import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class filterPurpose extends Fragment {
    public static void handleCheckBoxEvents(CheckBox cBoxAlone, CheckBox cBoxFriends,
                                            CheckBox cBoxWork, CheckBox cBoxDate,
                                            CheckBox cBoxSelfie, CheckBox cBoxDrink,
                                            CheckBox cBoxBar, CheckBox cBoxTime,
                                            CheckBox cBoxReadBook) {
        cBoxAlone.setOnCheckedChangeListener((buttonView, isChecked) -> {

        });

        cBoxFriends.setOnCheckedChangeListener((buttonView, isChecked) -> {

        });

        cBoxWork.setOnCheckedChangeListener((buttonView, isChecked) -> {

        });

        cBoxDate.setOnCheckedChangeListener((buttonView, isChecked) -> {

        });
        cBoxSelfie.setOnCheckedChangeListener((buttonView, isChecked) -> {

        });
        cBoxDrink.setOnCheckedChangeListener((buttonView, isChecked) -> {

        });
        cBoxBar.setOnCheckedChangeListener((buttonView, isChecked) -> {

        });
        cBoxTime.setOnCheckedChangeListener((buttonView, isChecked) -> {

        });
        cBoxReadBook.setOnCheckedChangeListener((buttonView, isChecked) -> {

        });


    }
}
