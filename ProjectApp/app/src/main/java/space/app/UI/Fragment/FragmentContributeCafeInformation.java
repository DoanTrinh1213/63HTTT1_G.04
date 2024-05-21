package space.app.UI.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.fragment.app.Fragment;


import space.app.Adapter.CustomSpinnerAdapter;
import space.app.MainActivity;
import space.app.R;

public class FragmentContributeCafeInformation extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contribute_cafe_information, container, false);

        //Spiner Purpose
        Spinner spinner = view.findViewById(R.id.spinner);
        String[] items = {"1 mình", "Bar/Pub ", "Bạn bè", "Hẹn hò","Làm việc","Mở muộn",
                "Sống ảo", "Đọc sách","Đồ uống ngon"}; //

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getContext(), items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //BackMe
        ImageView BackPerson = view.findViewById(R.id.BackPerson);
        BackPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new FragmentMe(), true);
            }
        });
        return view;
    }

}
