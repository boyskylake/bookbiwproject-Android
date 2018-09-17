package site.skylake.project_cs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static site.skylake.project_cs.SendFragment.Extra_id;
import static site.skylake.project_cs.SendFragment.EXtra_name;
import static site.skylake.project_cs.SendFragment.Extra_detail;

public class DetailActivity extends AppCompatActivity {

    Button revice;
    String id;
    private RecyclerView recyclerView;
    private AdapterDetail adapterDetail;
    private ArrayList<Itemdetail> itemdetail;

    private static final String loginURL = "http://10.0.0.103/bookbiwproject/staff/api/show_detailsend.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        recyclerView = findViewById(R.id.recycler_detail);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemdetail = new ArrayList<>();

        final Intent intent = getIntent();
        String name = intent.getStringExtra(EXtra_name);
        String detail = intent.getStringExtra(Extra_detail);
        id = intent.getStringExtra(Extra_id);

        TextView textViewname = findViewById(R.id.name_detail);
        TextView textViewscore = findViewById(R.id.score_detail);
        TextView detailview = findViewById(R.id.detail);

        textViewname.setText(name);
        textViewscore.setText(id);
        detailview.setText("ข้อมูลการจอง "+detail);

        revice = findViewById(R.id.reciveadd);

        showdata();

        revice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inrev = new Intent(getApplicationContext(), SentActivity.class);
                inrev.putExtra("id", id);
                startActivity(inrev);


            }
        });
    }

    private void showdata() {

        StringRequest request = new StringRequest(Request.Method.POST, loginURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("content");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject content = jsonArray.getJSONObject(i);

                        String namebo = content.getString("namebo");
                        String bo_img = content.getString("img");


                        String urlimg =  "http://10.0.0.103/bookbiwproject/img/"+bo_img;

                        itemdetail.add(new Itemdetail(urlimg,namebo));
                    }
                    adapterDetail = new AdapterDetail(DetailActivity.this, itemdetail);
                    recyclerView.setAdapter(adapterDetail);

                    Toast.makeText(getApplicationContext(),"success", Toast.LENGTH_LONG).show();

                }
                catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"NOT ACCESS", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params=new HashMap<>();
                params.put("id", id);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }


}
