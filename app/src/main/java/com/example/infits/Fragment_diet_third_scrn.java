   package com.example.infits;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

   /**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_diet_third_scrn#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_diet_third_scrn extends Fragment {

    ImageView back;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_diet_third_scrn () {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_diet_third_scrn.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_diet_third_scrn newInstance ( String param1 , String param2 ) {
        Fragment_diet_third_scrn fragment = new Fragment_diet_third_scrn ();
        Bundle args = new Bundle ();
        args.putString ( ARG_PARAM1 , param1 );
        args.putString ( ARG_PARAM2 , param2 );
        fragment.setArguments ( args );
        return fragment;
    }

    @Override
    public void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        if (getArguments () != null) {
            mParam1 = getArguments ().getString ( ARG_PARAM1 );
            mParam2 = getArguments ().getString ( ARG_PARAM2 );
        }
    }

    @Override
    public View onCreateView ( LayoutInflater inflater , ViewGroup container ,
                               Bundle savedInstanceState ) {
        View view = inflater.inflate ( R.layout.fragment_diet_third_scrn , container , false );

        back = view.findViewById(R.id.back_button);

        back.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_fragment_diet_third_scrn_to_diet_fourth2));

        return view;

    }
}