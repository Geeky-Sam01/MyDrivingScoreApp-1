package com.example.mydrivingscore;

import static android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import at.grabner.circleprogress.CircleProgressView;

public class dashboard extends AppCompatActivity implements LocationListener,DatePickerDialog.OnDateSetListener
{
    private TextView  trip1, trip2, trip3, trip4,datetv,behaviour,total_max;
    static private LocationManager locationManager;
    boolean d=false;
    static SensorManager sensorManager;
    static Sensor sensor,ac,gyro;
    static float x_acceleration,y_acceleration,z_acceleration,Magnitude,x_gyro,y_gyro,z_gyro;
    static String previous="",currentTime,s,speed,device_ID,dateInString,pattern,dateeee,url,lat,lon,alti;
    boolean m=false,a=false,g=false;
    CircleProgressView cp,dp;
    Button datepicker;
    ListView listView;
    static float nCurrentSpeed=0;
    int flag=0;
    Intent i;
    API obj;

    int trip[]={1,2,3,4,5,6,7,8,9,10};
    int trip_score[]={10,80,30,90,20,60,70,80,90,100};
    String trip_distance[]={"45 kmph","55 kmph","67 kmph","71 kmph","75 kmph","76 kmph","77 kmph","78 kmph","79 kmph","80 kmph"};
    String trip_time[]={"12 min","10 min","30 min","47 min","25 min","26 min","27 min","28 min","29 min","30  min"};

    int tripm[]={1,2,3,4,5,6,7,8};
    int trip_scorem[]={98,21,31,71,51,61,71,81};
    String trip_distancem[]={"54 kmph","65 kmph","69 kmph","71 kmph","75 kmph","76 kmph","77 kmph","78 kmph"};
    String trip_timem[]={"1 hr 9 min","1 hr 10 min","37 min","1 hr 04 min","44 min","55 min","39 min","1 hr 01 min"};

    int tripl[]={1,2,3,4,5};
    int trip_scorel[]={15,85,35,45,55};
    String trip_distancel[]={"40 kmph","59 kmph","61 kmph","71 kmph","79 kmph"};
    String trip_timel[]={"2 hr 17 min","3 hr 5 min","59 min","1 hr 41 min","2 hr 27 min",};

    String date_to_be_sent="",previousTime="";

    boolean trip_start_flag=false;
    boolean first=true;
    static RetryPolicy retryPolicy;
    static RequestQueue queue;
    static SensorEventListener sensorEventListener;
    static StringRequest stringRequest;
    Button st,mt,lt;

    class SensorvaluesUploader extends Thread
    {
        @Override
        public void run()
        {
            sensorEventListener = new SensorEventListener()
            {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                    if (sensorEvent.sensor == sensor) {
                        m = true;
                        float azimuth = Math.round(sensorEvent.values[0]);
                        float pitch = Math.round(sensorEvent.values[1]);
                        float roll = Math.round(sensorEvent.values[2]);

                        double tesla = Math.sqrt((azimuth * azimuth) + (pitch * pitch) + (roll * roll));

                        s = String.format("%.1f", tesla);
                    }

                    if (sensorEvent.sensor == ac) {
                        a = true;
                        x_acceleration = sensorEvent.values[0];
                        y_acceleration = sensorEvent.values[1];
                        z_acceleration = sensorEvent.values[2];

                        Magnitude = (float) Math.sqrt(x_acceleration * x_acceleration + y_acceleration * y_acceleration + z_acceleration * z_acceleration);
                    }

                    if(sensorEvent.sensor==gyro)
                    {
                        g=true;
                        x_gyro=sensorEvent.values[0];
                        y_gyro=sensorEvent.values[1];
                        z_gyro=sensorEvent.values[2];
                    }

                    if (m && a && g)
                    {
                        stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbzcifI8bv-tmmUpwjf4ZmAv3-9VBxnrBEroocAy3lUseJjP0VapKKGf-5o35ji62zpZ/exec", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }
                        ) {
                            @Nullable
                            @Override
                            protected Map<String, String> getParams() {

                                if (!previous.equals(currentTime)) {
                                    Map<String, String> params = new HashMap<>();
                                    try
                                    {
                                        params.put("action", "addItem");
                                        params.put("date", dateInString);
                                        params.put("t", currentTime);
                                        params.put("id", device_ID);
                                        params.put("strength", s);
                                        params.put("x", String.valueOf(x_acceleration));
                                        params.put("y", String.valueOf(y_acceleration));
                                        params.put("z", String.valueOf(z_acceleration));
                                        params.put("magn", String.valueOf(Magnitude));
                                        params.put("speed", speed);
                                        params.put("lat", lat);
                                        params.put("lon", lon);
                                        params.put("alti", alti);
                                        params.put("x_gyro", String.valueOf(x_gyro));
                                        params.put("y_gyro", String.valueOf(y_gyro));
                                        params.put("z_gyro", String.valueOf(z_gyro));

                                        previous = currentTime;
                                    }
                                    catch (Exception e)
                                    {
                                        Toast.makeText(dashboard.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                    return params;
                                }
                                else
                                {
                                    return null;
                                }
                            }
                        };
                        int timeout = 500000;

                        retryPolicy = new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                        stringRequest.setRetryPolicy(retryPolicy);

                        queue = Volley.newRequestQueue(dashboard.this);
                        queue.add(stringRequest);

                        m=false;
                        g=false;
                        a=false;
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {
                }
            };
            sensorManager.registerListener(sensorEventListener,sensor,SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(sensorEventListener,ac,SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(sensorEventListener,gyro,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        total_max=findViewById(R.id.tv_totalMax);

        cp=findViewById(R.id.circleView);
        cp.setMaxValue(100);
        cp.setVisibility(View.VISIBLE);
        st=findViewById(R.id.short_trip);
        mt=findViewById(R.id.medium_trip);
        dp=findViewById(R.id.daily_progress);
        lt=findViewById(R.id.long_trip);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        datepicker=findViewById(R.id.datepicker);

        trip_start_flag=false;
        first=true;

        Calendar c=Calendar.getInstance();
        int year=c.get(Calendar.YEAR);
        int month=c.get(Calendar.MONTH)+1;
        int day=c.get(Calendar.DAY_OF_MONTH);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        ac=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyro=sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED);

        listView=findViewById(R.id.listView);

        String datee=String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);
        dateeee=datee;

        String dayNames[] = new DateFormatSymbols().getWeekdays();
        Calendar date = Calendar.getInstance();
        String dd=dayNames[date.get(Calendar.DAY_OF_WEEK)];
        String dayNamess[] = new DateFormatSymbols().getMonths();
        dd=dd+", "+day+" "+dayNamess[date.get(Calendar.MONTH)]+", "+year;

        datepicker.setText(dd);

        behaviour=findViewById(R.id.behaviour);

        device_ID= Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);


        url="https://msd.samannwaysil.repl.co/driving-score?userID="+device_ID;
        String extra_url="&date="+dateeee;
        url=url+extra_url;

        obj = new API(url);
        obj.start();
        obj=null;

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker=new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });

        Toast.makeText(this, "Recording your driving behaviour...", Toast.LENGTH_SHORT).show();

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder alertDialogueBuilder = new AlertDialog.Builder(dashboard.this);
            alertDialogueBuilder.setMessage("Please turn on GPS Location Services so that we can get your driving data.Your data will be fully secured and will not be shared to any third party sources.")
                    .setCancelable(true)
                    .setTitle("Allow Location Services")
                    .setIcon(R.drawable.mydrivingscorelogo)
                    .setPositiveButton("Turn On", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            d=true;
                            startActivity(new Intent(ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogueBuilder.create();
            alertDialog.show();
        }

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            Toast.makeText(this, "Please grant permission for speedometer", Toast.LENGTH_SHORT).show();
        }
        else
        {
            doStuff();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);
        }

        pattern = "dd-MM-yyyy";
        dateInString =new SimpleDateFormat(pattern).format(new Date());

        if(flag==0)
        {
            SensorvaluesUploader thread = new SensorvaluesUploader();
            thread.start();
            flag = 1;
        }
        total_max.setVisibility(View.INVISIBLE);

        dp.setValueAnimated(Float.parseFloat("78"), 1500);
        dp.setTextColor(Color.parseColor("#039F17"));
        dp.setBarColor(Color.parseColor("#039F17"));


        st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                st.setBackgroundColor(Color.parseColor("#0A98E6"));
                mt.setBackgroundColor(Color.parseColor("#8A909A"));
                lt.setBackgroundColor(Color.parseColor("#8A909A"));

                CustomBaseAdapter customBaseAdapter=new CustomBaseAdapter(getApplicationContext(),trip,trip_score,trip_distance,trip_time);
                listView.setAdapter(customBaseAdapter);

            }
        });

        listView.setSmoothScrollbarEnabled(true);
        listView.setScrollBarSize(15);

        mt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mt.setBackgroundColor(Color.parseColor("#0A98E6"));
                st.setBackgroundColor(Color.parseColor("#8A909A"));
                lt.setBackgroundColor(Color.parseColor("#8A909A"));

                CustomBaseAdapter customBaseAdapter=new CustomBaseAdapter(getApplicationContext(),tripm,trip_scorem,trip_distancem,trip_timem);
                listView.setAdapter(customBaseAdapter);
            }
        });

        lt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lt.setBackgroundColor(Color.parseColor("#0A98E6"));
                st.setBackgroundColor(Color.parseColor("#8A909A"));
                mt.setBackgroundColor(Color.parseColor("#8A909A"));

                CustomBaseAdapter customBaseAdapter=new CustomBaseAdapter(getApplicationContext(),tripl,trip_scorel,trip_distancel,trip_timel);
                listView.setAdapter(customBaseAdapter);
            }
        });

    }


    public class API extends Thread {

        String URL;
        API(String URL)
        {
            this.URL=URL;
        }
        @Override
        public void run() {

            RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, URL , null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try
                    {
                        double driving_score  = response.getDouble("Overall Driving Score");
                        String point=String.valueOf(driving_score);


                        if(driving_score!=-1)
                        {
                            total_max.setVisibility(View.VISIBLE);
                            cp.setVisibility(View.VISIBLE);
                            cp.setValueAnimated(Float.parseFloat(point), 1500);
                            selectColor(cp, point);
                            behaviour.setVisibility(View.VISIBLE);
                            total_max.setText("/100");
                            select_remark(point);
                        }
                        else
                        {
                            total_max.setVisibility(View.VISIBLE);
                            cp.setVisibility(View.INVISIBLE);
                            total_max.setText("Sorry 🙂 Data Not Found !");
                            behaviour.setText("");
                            behaviour.setVisibility(View.INVISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(dashboard.this, "Failed to get data ! 😐", Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(jsonObjectRequest);
        }
    }



    public void select_remark(String point)
    {
        float pointI=Float.parseFloat(point);

        if(pointI>=0 && pointI<=25)
        {
            behaviour.setText("Got very bad driving reports 😖");
        }
        else if(pointI>=26 && pointI<=50)
        {
            behaviour.setText("Average driving 😐 Drive safely");
        }
        else if(pointI>=51 && pointI<=75)
        {
            behaviour.setText("Congratulations.You are a good driver ☺");
        }
        else
        {
            behaviour.setText("Exceptionally well driving records 🤩 ");
        }
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
            cp.setTextColor(Color.parseColor("#FFE500"));
            cp.setBarColor(Color.parseColor("#FFE500"));
        }
        else
        {
            cp.setTextColor(Color.parseColor("#039F17"));
            cp.setBarColor(Color.parseColor("#039F17"));
        }
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {

        if(location!=null) {

            CLocation myLocation = new CLocation(location, this.useMetricUnits());
            this.updateSpeed(myLocation);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    @SuppressLint("MissingPermission")
    private void doStuff()
    {
        LocationManager locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if(locationManager!=null)
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
        }
    }

    private void updateSpeed(CLocation location)
    {
        if(location != null)
        {
            location.setUserMetricUnits(this.useMetricUnits());
            lat= String.valueOf(location.getLatitude());
            lon=String.valueOf(location.getLongitude());
            alti=String.valueOf(location.getAltitude());
            nCurrentSpeed=location.getSpeed();
        }

        Formatter fmt=new Formatter(new StringBuilder());
        fmt.format(Locale.US,"%.0f",nCurrentSpeed);
        String strCurrentSpeed=fmt.toString();
        strCurrentSpeed=strCurrentSpeed.replace(" ","0");

        if(this.useMetricUnits())
        {
            speed=strCurrentSpeed;
        }
    }

    private boolean useMetricUnits()
    {
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doStuff();
            }
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2)
    {
        Calendar c=Calendar.getInstance();
        c.set(Calendar.YEAR,i);
        c.set(Calendar.MONTH,i1);
        c.set(Calendar.DAY_OF_MONTH,i2);

        String currentDateString= DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        date_to_be_sent=DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.getTime());


        int yearr=c.get(Calendar.YEAR);
        int monthh=c.get(Calendar.MONTH)+1;
        int dayy=c.get(Calendar.DAY_OF_MONTH);

        dateeee=String.valueOf(yearr)+"-"+String.valueOf(monthh)+"-"+String.valueOf(dayy);

        url="https://msd.samannwaysil.repl.co/driving-score?userID="+device_ID;
        String extra_url="&date="+dateeee;
        url=url+extra_url;

        obj = new API(url);
        obj.start();
        obj=null;
        System.gc();

        datepicker.setText(currentDateString);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Your driving behaviour recording stopped.", Toast.LENGTH_SHORT).show();
    }
}


