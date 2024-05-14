package space.app.UI.Fragment;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import space.app.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentBookmark#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentBookmark extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentBookmark() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentBookmark.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentBookmark newInstance(String param1, String param2) {
        FragmentBookmark fragment = new FragmentBookmark();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_bookmark, container, false);
        int i=1;
        if(i==0){
            View layout1 = inflater.inflate(R.layout.bookmark_none_compo, (FrameLayout) view.findViewById(R.id.mainBookmark), true);
        }
        else{
            FrameLayout layoutmain = view.findViewById(R.id.mainBookmark);
            LayoutInflater inflater1 = LayoutInflater.from(container.getContext());
            View bookmarkNoneView = inflater.inflate(R.layout.bookmark_none_compo, null);
            layoutmain.addView(bookmarkNoneView);
        }
        return view;
    }
}