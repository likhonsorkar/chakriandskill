package com.likhonsoftware.chakrikhuji;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class organizationjobs_fragement extends Fragment {
    RecyclerView organizationjobsrecycle;
    public static String orgid = "";
    public static String mainlogourl = "";
    public static String mainname = "";
    public static String maincount = "";
    ImageView mainlogo;
    TextView companyname, count;
    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
    HashMap<String,String> hashMap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container!=null){
            container.removeAllViews();
        }
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.organizationjobs_fragement, container, false);
        organizationjobsrecycle = inflate.findViewById(R.id.organizationjobsrecycle);
        mainlogo = inflate.findViewById(R.id.mainlogo);
        companyname = inflate.findViewById(R.id.companyname);
        count = inflate.findViewById(R.id.count);

        count.setText(maincount);
        companyname.setText(""+mainname);
        Picasso.get().load(mainlogourl).into(mainlogo);
        String url = "https://alljobs.teletalk.com.bd/api/v1/govt-jobs/list?orgId="+orgid+"&skipLimit=YES";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray govtJobs = response.getJSONArray("govtJobs");
                    for (int i=0; govtJobs.length()>i;i++){
                        JSONObject jobinfo = govtJobs.getJSONObject(i);
                        String jobid = jobinfo.getString("id");
                        String jobname = jobinfo.getString("job_title");
                        String vacancy = jobinfo.getString("vacancy");
                        String created_at = jobinfo.getString("created_at");
                        String deadline_date = jobinfo.getString("deadline_date");
                        String application_site = jobinfo.getString("application_site");
                        hashMap = new HashMap<>();
                        hashMap.put("jobid", jobid);
                        hashMap.put("jobname", jobname);
                        hashMap.put("vacancy", vacancy);
                        hashMap.put("created_at", created_at);
                        hashMap.put("deadline_date", deadline_date);
                        hashMap.put("application_site", application_site);
                        arrayList.add(hashMap);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                organizationjobsrecycle.setAdapter(new organizationlist());
                organizationjobsrecycle.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(jsonObjectRequest);

        return inflate;
    }
    public class organizationlist extends RecyclerView.Adapter<organizationlist.ChakriViewHolder> {

        @NonNull
        @Override
        public organizationlist.ChakriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.orgjoblist_item, parent, false);
            return new ChakriViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull organizationlist.ChakriViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return arrayList.size(); // Return the number of items in your dataset
        }

        public class ChakriViewHolder extends RecyclerView.ViewHolder {
            public ChakriViewHolder(@NonNull View itemView) {
                super(itemView);

            }
        }
    }
}