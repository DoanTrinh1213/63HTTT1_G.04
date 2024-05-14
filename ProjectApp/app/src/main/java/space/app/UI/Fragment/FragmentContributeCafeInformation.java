package space.app.UI.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import androidx.fragment.app.Fragment;

import space.app.R;

public class FragmentContributeCafeInformation extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contribute_cafe_information, container, false);

        Spinner spinner = rootView.findViewById(R.id.spinner);
        String[] items = {"1 mình", "Bar/Pub ", "Bạn bè", "Hẹn hò","làm việc","Mở muộn",
                "sống ảo", "đọc sách","đồ uống ngon"}; //

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getContext(), items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        return rootView;
    }
}
