package com.example.tanya.retrofit7days;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import com.example.tanya.retrofit7days.Days.Days;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<Days> daysList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        daysList = new ArrayList<>();

        getData();

    }

    private void getData()
    {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        Call<Data> call = api.getData();

        call.enqueue(new Callback<Data>()
        {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response)
            {
                //Log.i("Response", "onNet: "+  response.toString());
                ArrayList<Days> dlist = response.body().getDays();

                Log.i("Dlist", "onResponse: " + dlist);
                for (int i = 0; i<dlist.size();i++) {

                    Days day = new Days(dlist.get(i).getDate(),
                            dlist.get(i).getTemp_min_c(),dlist.get(i).getTemp_max_c()
                            ,dlist.get(i).getHumid_min_pct(),dlist.get(i).getHumid_max_pct(),
                            dlist.get(i).getPrecip_total_mm());

                    daysList.add(day);
                }
                adapter = new MyAdapter(daysList, MainActivity.this);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<Data> call, Throwable t)
            {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }
}
