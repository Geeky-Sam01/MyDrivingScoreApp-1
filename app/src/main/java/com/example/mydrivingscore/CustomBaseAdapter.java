package com.example.mydrivingscore;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import at.grabner.circleprogress.CircleProgressView;

public class CustomBaseAdapter extends BaseAdapter
{

    Context context;
    int trip_no[];
    int score[];
    String distance[];
    String time[];
    LayoutInflater inflater;

    public CustomBaseAdapter(Context ctx,int trip[],int score[],String distance[],String time[])
    {
        this.trip_no=trip;
        this.context=ctx;
        this.score=score;
        this.distance=distance;
        this.time=time;
        inflater=LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return score.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        view=inflater.inflate(R.layout.activity_custom_list_view,null);
        TextView trip_number=(TextView) view.findViewById(R.id.trip2);
        CircleProgressView circleProgressView=(CircleProgressView) view.findViewById(R.id.l2p2);
        TextView dist=(TextView) view.findViewById(R.id.dist2);
        TextView timee=(TextView) view.findViewById(R.id.time2);

        trip_number.setText(String.valueOf("Trip "+trip_no[i]));
        circleProgressView.setText(String.valueOf(score[i]));
        circleProgressView.setValueAnimated(Float.parseFloat(String.valueOf(score[i])), 400);
        selectColor(circleProgressView,String.valueOf(score[i]));
        dist.setText(distance[i]);
        timee.setText(time[i]);

        return view;
    }
    public void selectColor(CircleProgressView cp,String point)
    {
        if(Float.parseFloat(point)>=0 && Float.parseFloat(point)<=25)
        {
            cp.setTextColor(Color.RED);
            cp.setBarColor(Color.RED);
        }
        else if(Float.parseFloat(point)>=26 && Float.parseFloat(point)<=50)
        {
            cp.setTextColor(Color.parseColor("#FF9800"));
            cp.setBarColor(Color.parseColor("#FF9800"));
        }
        else if(Float.parseFloat(point)>=51 && Float.parseFloat(point)<=75)
        {
            cp.setTextColor(Color.parseColor("#A6C12E"));
            cp.setBarColor(Color.parseColor("#A6C12E"));
        }
        else
        {
            cp.setTextColor(Color.parseColor("#039F17"));
            cp.setBarColor(Color.parseColor("#039F17"));
        }
    }


}
