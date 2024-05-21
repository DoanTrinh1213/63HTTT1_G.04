package space.app.UI.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import space.app.MainActivity;
import space.app.R;

public class FragmentPerson extends Fragment {

    public FragmentPerson() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Any initialization that does not involve the view hierarchy goes here
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_person, container, false);

        // Initialize views here
        ImageView arrowEditInformation = view.findViewById(R.id.arrowEditInformation);
        arrowEditInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).openFragmentEditInformation();
            }
        });

        return view;
    }
}
