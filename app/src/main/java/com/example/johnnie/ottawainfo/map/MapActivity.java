package com.example.johnnie.ottawainfo.map;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;
import android.widget.Toast;


import com.example.johnnie.ottawainfo.R;
import com.example.johnnie.ottawainfo.list.RecyclerListAdapter;
import com.example.johnnie.ottawainfo.model.DealerModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements MapFragment.OnMapFragmentClicked{


    private ArrayList<DealerModel> models;
    private MapFragment mMapFragment;
    private static final String TAG_MAPACTIVITY = "MAPACTIVITY";
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        mMapFragment = new MapFragment();
        models = (ArrayList<DealerModel>)getIntent().getSerializableExtra("DealerModels");
        mMapFragment.setModels(models);
        mMapFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction()
                .add(R.id.mapactivity_container_map, mMapFragment).commit();



        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView_items);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerListAdapter(models);
        mRecyclerView.setAdapter(mAdapter);


        // SnapHelper helper = new LinearSnapHelper();
        LinearSnapHelper snapHelper = new LinearSnapHelper() {
            @Override
            public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
                View centerView = findSnapView(layoutManager);
                if (centerView == null) {
                    return RecyclerView.NO_POSITION;
                }

                int position = layoutManager.getPosition(centerView);
                int targetPosition = -1;
                if (layoutManager.canScrollHorizontally()) {
                    if (velocityX > 0) {
                        targetPosition = position - 1;
                    } else {
                        targetPosition = position + 1;
                    }
                }


                final int firstItem = 0;
                final int lastItem = layoutManager.getItemCount() - 1;
                targetPosition = Math.min(lastItem, Math.max(targetPosition, firstItem));
                Toast.makeText(getApplication(),models.get(targetPosition).getAddress(),Toast.LENGTH_SHORT).show();
                try {
                    mMapFragment.markersFresh();
                    mMapFragment.geoLocateBySingleModel(models.get(targetPosition));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return targetPosition;
            }
        };
        snapHelper.attachToRecyclerView(mRecyclerView);

    }


  //implement mapFragment interface
    @Override
    public void GoToMapActivity() {

    }


    @Override
    public void onStop() {
        super.onStop();
    }
}





