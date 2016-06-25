package bacakomik.daeng.app.comic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import bacakomik.daeng.app.comic.Adapter.RecyclerAdapterTrailer;

public class DetailActivityFragment extends Fragment {

    private TextView txtJudul;
    private TextView txtDeskripsi;
    private ImageView imgMovie;
    private TextView txtVote;
    public TextView txtTahun;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecyclerAdapterTrailer adapter;

    final FragmentActivity c = getActivity();

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        txtJudul = (TextView) view.findViewById(R.id.txtJudul);
        txtDeskripsi = (TextView) view.findViewById(R.id.txtDeskripsi);
        imgMovie = (ImageView) view.findViewById(R.id.imgMovie);
        txtTahun = (TextView) view.findViewById(R.id.txtTahun);
        imgMovie.setScaleType(ImageView.ScaleType.CENTER_CROP);
        txtVote = (TextView) view.findViewById(R.id.txtVote);

        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(c);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        MenuItem SharedContent = menu.findItem(R.id.mnShared);
        SharedContent.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "[Daeng Movies] \nMovie name : " + txtJudul.getText().toString() + "\n\n" +
                        txtDeskripsi.getText().toString() + "\n\n" +
                        "\u2605" + txtVote.getText().toString() + "\n" +
                        txtTahun.getText().toString() + "\n";

                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Daeng Movies");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share in your friends"));
                return true;
            }
        });
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Image
        String latar = getActivity().getIntent().getStringExtra("latar");

        String jud = getActivity().getIntent().getStringExtra("judul");
        String des = getActivity().getIntent().getStringExtra("deskripsi");
        String thn = getActivity().getIntent().getStringExtra("tahun");
        String vote = getActivity().getIntent().getStringExtra("vote");
        String id_film = getActivity().getIntent().getStringExtra("id");

//        String yt_key = getActivity().getIntent().getStringExtra("key");
//        String Trailername = getActivity().getIntent().getStringExtra("name");
//        String Trailertype = getActivity().getIntent().getStringExtra("type");

        Picasso.with(getContext()).load(latar).placeholder(R.drawable.logo_item).into(imgMovie);
        txtJudul.setText(jud);
        txtDeskripsi.setText(des);
        txtTahun.setText("Release date : " + thn);
        txtVote.setText("Rating : " + vote + " / 10");
        SubRequestData(id_film);
    }


    private void SubRequestData(String i){
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = "";
        url = "http://api.themoviedb.org/3/movie/" + i + "/videos?api_key=9351b653885866a95fcef04c4f0c7426";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                Log.d("TRAILER", "Response " + response);
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                ObjectTrailer a = mGson.fromJson(response, ObjectTrailer.class);
                adapter = new RecyclerAdapterTrailer(c, a.results);
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TRAILER", "Error " + error.getMessage());
            }
        });
        queue.add(stringRequest);
    }
}
