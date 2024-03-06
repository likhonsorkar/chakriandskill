package com.likhonsoftware.chakrikhuji;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class home_fragment extends Fragment {
    ArrayList<HashMap<String,String>> chakri_lis_count_array = new ArrayList<>();
    HashMap<String,String> hashMap;
    RecyclerView chakri_no_list;
    LinearLayout govtchakribtn, privatechakribtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container!=null){
            container.removeAllViews();
        }
        // Inflate the layout for this fragment
        View inflateview = inflater.inflate(R.layout.home_fragment, container, false);
        chakri_no_list = inflateview.findViewById(R.id.chakri_no_list);
        privatechakribtn = inflateview.findViewById(R.id.privatechakribtn);
        govtchakribtn = inflateview.findViewById(R.id.govtchakribtn);
        // Chakri List Array Generate form json
        govtchakribtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fragement Code
                chakrilist_fragment.chakritype = "govtjobs";
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragmentcontainer, new chakrilist_fragment());
                fragmentTransaction.commit();
            }
        });
        privatechakribtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chakrilist_fragment.chakritype = "privatejobs";
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragmentcontainer, new chakrilist_fragment());
                fragmentTransaction.commit();
            }
        });
        chakri_count();
        return inflateview;
    }
    // Chakri Count Code  Start
    // Volley Function
    public void chakri_count(){
        String url = "https://alljobs.teletalk.com.bd/api/v1/published-jobs/total-list";
        JsonObjectRequest chakri_no_count = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.contains("success")){
                        JSONObject message = response.getJSONObject("details");
                        String totalLiveJobs = message.getString("totalLiveJobs");
                        String todayPostedJobs = message.getString("todayPostedJobs");
                        String todayDeadlineJobs = message.getString("todayDeadlineJobs");
                        String tomorrowDeadlineJobs = message.getString("tomorrowDeadlineJobs");
                        String totalGovtJobs = message.getString("totalGovtJobs");
                        String totalPrivateJobs = message.getString("totalPrivateJobs");
                        hashMap = new HashMap<>();
                        hashMap.put("msg", "মোট বর্তমান\nচাকরি" );
                        hashMap.put("count",""+totalLiveJobs);
                        chakri_lis_count_array.add(hashMap);

                        hashMap = new HashMap<>();
                        hashMap.put("msg", "আজকের\nবিজ্ঞপ্তি" );
                        hashMap.put("count",""+todayPostedJobs);
                        chakri_lis_count_array.add(hashMap);

                        hashMap = new HashMap<>();
                        hashMap.put("msg", "আজকে শেষ\nহবে" );
                        hashMap.put("count",""+todayDeadlineJobs);
                        chakri_lis_count_array.add(hashMap);

                        hashMap = new HashMap<>();
                        hashMap.put("msg", "আগামীকাল\nশেষ হবে" );
                        hashMap.put("count",""+tomorrowDeadlineJobs);
                        chakri_lis_count_array.add(hashMap);

                        hashMap = new HashMap<>();
                        hashMap.put("msg", "সরকারি\n চাকরি" );
                        hashMap.put("count",""+totalGovtJobs);
                        chakri_lis_count_array.add(hashMap);

                        hashMap = new HashMap<>();
                        hashMap.put("msg", "বেসরকারি\nচাকরি" );
                        hashMap.put("count",""+totalPrivateJobs);
                        chakri_lis_count_array.add(hashMap);
                        
                        chakri_no_list.setAdapter(new ChakriNoListAdapter());
                        chakri_no_list.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false));
                    }else{
                        Toast.makeText(getActivity(), "Server Response Failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Log.d("check end", String.valueOf(response));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "চাকরির মোট হিসাব আনতে ব্যার্থ হয়েছি। অনুগ্রহ পূর্বক ইন্টারনেট কানেকশন ঠিক আছে কিনা যাচাই করুন", Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(chakri_no_count);
    }
    // Adapter
    public class ChakriNoListAdapter extends RecyclerView.Adapter<ChakriNoListAdapter.ChakriViewHolder> {

        @NonNull
        @Override
        public ChakriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.total_count, parent, false);
            return new ChakriViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ChakriViewHolder holder, int position) {
            // Here you can bind data to your views if needed
            HashMap<String,String> getarray = chakri_lis_count_array.get(position);
            String sdescription = getarray.get("msg");
            String scount = getarray.get("count");
            holder.count.setText(scount);
            holder.description.setText(sdescription);
        }

        @Override
        public int getItemCount() {
            return chakri_lis_count_array.size(); // Return the number of items in your dataset
        }

        public class ChakriViewHolder extends RecyclerView.ViewHolder {
            TextView description, count;
            public ChakriViewHolder(@NonNull View itemView) {
                super(itemView);
                // Initialize your views here if needed
                description = itemView.findViewById(R.id.description);
                count = itemView.findViewById(R.id.count);
            }
        }
    }
    // Chakri Count Code  end

    // =======================================================================================================
              // ----------------------- Another Code -------------------------------------------------
    // =======================================================================================================

    // Chakri Menu Start Here
    // Chakri Menu End Here
}