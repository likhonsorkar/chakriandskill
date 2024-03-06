package com.likhonsoftware.chakrikhuji;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
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

public class chakrilist_fragment extends Fragment {
    public static String chakritype = "";
    RecyclerView chakrilist;
    LottieAnimationView noresult;
    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
    HashMap<String,String> hashMap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container!=null){
            container.removeAllViews();
        }
        // Inflate the layout for this fragment
        View inflateview =  inflater.inflate(R.layout.chakrilist_fragment, container, false);
        String url = "";
        noresult = inflateview.findViewById(R.id.noresult);
        if (chakritype.contains("govtjobs")){
            url = "https://alljobs.teletalk.com.bd/api/v1/govt-jobs/org-list";
        } else if (chakritype.contains("privatejobs")) {
            url = "https://alljobs.teletalk.com.bd/api/v1/private-jobs/recruiter-list";
        }

        JsonObjectRequest joblistrequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (chakritype.contains("govtjobs")){
                        JSONArray govtOrgJobs = response.getJSONArray("govtOrgJobs");
                        for (int i=0; i<govtOrgJobs.length(); i++){
                            JSONObject jobdetails = govtOrgJobs.getJSONObject(i);
                            JSONArray jobcountarray = jobdetails.getJSONArray("govt_jobs");
                            int count = 0;
                            for (int j= 0; j<jobcountarray.length(); j++){
                                count = j;
                            }
                            count++;
                            String jobname = jobdetails.getString("name");
                            String jobid = jobdetails.getString("id");
                            String logo = "https://alljobs.teletalk.com.bd/media/"+jobdetails.getString("logo");
                            String website = jobdetails.getString("website");
                            hashMap = new HashMap<>();
                            hashMap.put("jobname", jobname);
                            hashMap.put("jobid", jobid);
                            hashMap.put("logo", logo);
                            hashMap.put("website", website);
                            hashMap.put("jobcount", String.valueOf(count));
                            arrayList.add(hashMap);
                        }
                    } else if (chakritype.contains("privatejobs")) {
                        JSONArray privateRecruiterJobs = response.getJSONArray("privateRecruiterJobs");
                        for (int i=0; i<privateRecruiterJobs.length(); i++){
                            JSONObject jobdetails = privateRecruiterJobs.getJSONObject(i);
                            JSONArray jobcountarray = jobdetails.getJSONArray("govt_jobs");
                            int count = 0;
                            for (int j= 0; j<jobcountarray.length(); j++){
                                count = j;
                            }
                            count++;
                            String jobname = jobdetails.getString("name");
                            String jobid = jobdetails.getString("id");
                            String logo = "https://alljobs.teletalk.com.bd/media/"+jobdetails.getString("logo");
                            String website = jobdetails.getString("website");
                            hashMap = new HashMap<>();
                            hashMap.put("jobname", jobname);
                            hashMap.put("jobid", jobid);
                            hashMap.put("logo", logo);
                            hashMap.put("website", website);
                            hashMap.put("jobcount", String.valueOf(count));
                            arrayList.add(hashMap);
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                if (arrayList.size()<=0){
                    noresult.setVisibility(View.VISIBLE);
                }else{
                    chakrilist = inflateview.findViewById(R.id.chakrilist);
                    chakrilist.setAdapter(new chakrilistadapter());
                    chakrilist.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(joblistrequest);
        return inflateview;
    }
    public class chakrilistadapter extends RecyclerView.Adapter<chakrilistadapter.chakrilistholder>{
        public class chakrilistholder extends RecyclerView.ViewHolder{
            ImageView logo;
            TextView companyname, countjob;
            LinearLayout orginaztionclick;
            public chakrilistholder(@NonNull View itemView) {
                super(itemView);
                logo = itemView.findViewById(R.id.logo);
                companyname = itemView.findViewById(R.id.companyname);
                countjob = itemView.findViewById(R.id.countjob);
                orginaztionclick = itemView.findViewById(R.id.orginaztionclick);
            }
        }
        @NonNull
        @Override
        public chakrilistholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.chakrilist_item, parent, false);
            return new chakrilistholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull chakrilistholder holder, int position) {
            HashMap<String,String> joblist = arrayList.get(position);
            String logo = joblist.get("logo");
            String name = joblist.get("jobname");
            String jobcount = "Available Total: "+joblist.get("jobcount")+" Jobs";
            String orgid = joblist.get("jobid");
            holder.companyname.setText(name);
            holder.countjob.setText(jobcount);
            Picasso.get().load(logo).into(holder.logo);
            holder.orginaztionclick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    organizationjobs_fragement.mainlogourl = logo;
                    organizationjobs_fragement.mainname = name;
                    organizationjobs_fragement.maincount = jobcount;
                    organizationjobs_fragement.orgid = orgid ;
                    FragmentManager fragmentManager = getParentFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.fragmentcontainer, new organizationjobs_fragement());
                    fragmentTransaction.commit();
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

    }
}