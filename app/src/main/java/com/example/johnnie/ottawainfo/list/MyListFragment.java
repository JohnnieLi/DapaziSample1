package com.example.johnnie.ottawainfo.list;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



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
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;



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
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView_items);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerListAdapter(models);
        mRecyclerView.setAdapter(mAdapter);
    }
}
