package site.skylake.project_cs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ReceiveFragment extends Fragment implements AdapterRev.onItemClickListener {

    SessionManager sessionManager;
    public static final String EXtra_namerev = "name";
    public static final String Extra_idrev = "id";
    public static final String Extra_detailrev = "detail";

    TextView welcomeUser_rev;

    private String NAME,ID;
    public String id;
    private RecyclerView mRecyclerView;
    private AdapterRev mAdapterrev;
    private ArrayList<Itemrev> mListrev;

    private static final String loginURL = "http://10.0.0.103/bookbiwproject/staff/api/show_recive.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_receive, container, false);

        welcomeUser_rev = view.findViewById(R.id.welcomeUser_rev);
        welcomeUser_rev.setText("Welcome "+NAME);

        mRecyclerView = view.findViewById(R.id.recycler_viewrev);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        parseJson();

        return view;
    }

    private void parseJson() {
        StringRequest request = new StringRequest(Request.Method.POST, loginURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("content");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject content = jsonArray.getJSONObject(i);

                        String name = content.getString("name");
                        id = content.getString("id");
                        String detail = content.getString("detail");
                        String data = name+" "+id;

                        mListrev.add(new Itemrev(data, id, detail));
                    }

                    mAdapterrev = new AdapterRev(getContext(), mListrev);
                    mRecyclerView.setAdapter(mAdapterrev);
                    mAdapterrev.setListener(ReceiveFragment.this);
                    Toast.makeText(getContext(),"success", Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(),"Error JSON", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getContext(),"Error", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params=new HashMap<>();
                params.put("con", "sus");
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(request);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(getContext());
        sessionManager.checkLogin();

        mListrev = new ArrayList<>();

        HashMap<String, String> user = sessionManager.getUserDetail();
        NAME = user.get(sessionManager.NAME);
        ID = user.get(sessionManager.ID);
    }

    @Override
    public void onItemClick(int position) {
        Intent detailintent = new Intent(getContext(), DetailRevActivity.class);
        Itemrev ClickItem = mListrev.get(position);
        detailintent.putExtra(EXtra_namerev, ClickItem.getMname());
        detailintent.putExtra(Extra_idrev, ClickItem.getMid());
        detailintent.putExtra(Extra_detailrev, ClickItem.getMdetail());

        startActivity(detailintent);
    }
}
