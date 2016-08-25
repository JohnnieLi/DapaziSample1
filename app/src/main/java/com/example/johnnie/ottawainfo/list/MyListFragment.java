package com.example.johnnie.ottawainfo.list;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.johnnie.ottawainfo.R;
import com.example.johnnie.ottawainfo.model.DealerModel;

import java.util.List;

/**
 * Created by Johnnie on 2016-08-17.
 */
public class MyListFragment extends Fragment {

    final static String ARG_POSITION = "position";
    int mCurrentPosition = -1;
    private View rootView;
    private  OnListItemClicked mCallbacks;


    public interface OnListItemClicked {
         void onListItemClicked();
    }


    @Override
    public void onStart(){
        super.onStart();

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
        }

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.list_frag, container, false);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallbacks = (OnListItemClicked) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ARG_POSITION, mCurrentPosition);
    }



    public void displayListView(List<DealerModel> models){


        ArrayAdapter<DealerModel> dataAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,models);

        ListView listView = (ListView) rootView.findViewById(R.id.list_items);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
    }
}
