package brdevelopers.com.jobvibe;


import android.app.DownloadManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Edit_EducationFragment extends Fragment implements View.OnClickListener{

    private EditText et_university,et_college,et_cyoc,et_cper;
    private EditText et_12board,et_12school,et_12yoc,et_12per;
    private EditText et_10board,et_10school,et_10yoc,et_10per;
    private TextView tv_btnnext;
    private ProgressBar progressBar;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_edit__education,container,false);
        et_university=view.findViewById(R.id.ET_university);
        et_college=view.findViewById(R.id.ET_college);
        et_cyoc=view.findViewById(R.id.ET_cyoc);
        et_cper=view.findViewById(R.id.ET_cper);
        et_12board=view.findViewById(R.id.ET_12board);
        et_12school=view.findViewById(R.id.ET_12school);
        et_12yoc=view.findViewById(R.id.ET_12yoc);
        et_12per=view.findViewById(R.id.ET_12per);
        et_10board=view.findViewById(R.id.ET_10board);
        et_10school=view.findViewById(R.id.ET_10school);
        et_10yoc=view.findViewById(R.id.ET_10yoc);
        et_10per=view.findViewById(R.id.ET_10per);
        tv_btnnext=view.findViewById(R.id.TV_btnnext);
        progressBar=view.findViewById(R.id.progressbar);


        getCandidateDetals(Home.canemail);
        tv_btnnext.setOnClickListener(this);

        return view;
    }

    private void getCandidateDetals(final String email){
        RequestQueue requstQueue = Volley.newRequestQueue(getActivity());
        Map<String,String> hashMap = new HashMap<String, String>();
        hashMap.put("sessionId",GlobalDetails.sessionId);
        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, GlobalDetails.mainUrl+"/Api/v1/User/Android/en/geteducationdetails",new JSONObject(hashMap),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response.toString());
                            String success=jsonObject.getString("code");
                            System.out.println(""+jsonObject);
                            if(success.equals("S00"))
                            {
                                et_university.setText(jsonObject.getJSONObject("details").getString("cluniversity"));
                                et_college.setText(jsonObject.getJSONObject("details").getString("clcollege"));
                                et_cyoc.setText(jsonObject.getJSONObject("details").getString("clyearcompletion"));
                                et_cper.setText(jsonObject.getJSONObject("details").getString("clpercentage"));

                                et_12board.setText(jsonObject.getJSONObject("details").getString("plusboard"));
                                et_12school.setText(jsonObject.getJSONObject("details").getString("plusschool"));
                                et_12yoc.setText(jsonObject.getJSONObject("details").getString("plusboardyearcompletion"));
                                et_12per.setText(jsonObject.getJSONObject("details").getString("pluspercentage"));

                                et_10board.setText(jsonObject.getJSONObject("details").getString("board"));
                                et_10school.setText(jsonObject.getJSONObject("details").getString("school"));
                                et_10yoc.setText(jsonObject.getJSONObject("details").getString("boardyearcompletion"));
                                et_10per.setText(jsonObject.getJSONObject("details").getString("percentage"));

                            }
                            else{

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("logcheck",""+e);
                            progressBar.setVisibility(View.GONE);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Log.d("logcheck",""+error);
                    }
                }
        ){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", "Basic c2VydmljZW1hbmR1OnNlcnZpY2VtYW5kdUAyMDIw");
                return params;
            }
            //here I want to post data to sever
        };
        requstQueue.add(jsonobj);

    }





    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.TV_btnnext)
        {
            progressBar.setVisibility(View.VISIBLE);

            String university=et_university.getText().toString();
            String college=et_college.getText().toString();
            String cyoc=et_cyoc.getText().toString();
            String cper=et_cper.getText().toString();

            String tboard=et_12board.getText().toString();
            String tschool=et_12school.getText().toString();
            String tyoc=et_12yoc.getText().toString();
            String tper=et_12per.getText().toString();

            String mboard=et_10board.getText().toString();
            String mschool=et_10school.getText().toString();
            String myoc=et_10yoc.getText().toString();
            String mper=et_10per.getText().toString();



            if(Util.isNetworkConnected(getActivity())) {
                String email=Home.canemail;
                editCandidateDetails(email,university,college, cyoc, cper, tboard, tschool, tyoc, tper, mboard, mschool, myoc, mper);
            }
            else{
                Toast toast=new Toast(getActivity());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM|Gravity.FILL_HORIZONTAL,0,0);

                LayoutInflater inf=getActivity().getLayoutInflater();

                View layoutview=inf.inflate(R.layout.custom_toast,(ViewGroup)getActivity().findViewById(R.id.CustomToast_Parent));
                TextView tf=layoutview.findViewById(R.id.CustomToast);
                tf.setText("No Internet Connection "+ Html.fromHtml("&#9995;"));
                toast.setView(layoutview);
                toast.show();
                progressBar.setVisibility(View.GONE);
            }


        }

    }

    private void editCandidateDetails(final String email, final String university, final String college, final String cyoc, final String cper,
                                      final String tboard, final String tschool, final String tyoc, final String tper, final String mboard, final String mschool, final String myoc, final String mper){

        RequestQueue requstQueue = Volley.newRequestQueue(getActivity());
        Map<String,String> hashMap = new HashMap<String, String>();
        hashMap.put("sessionId",GlobalDetails.sessionId);
        hashMap.put("cluniversity",university);
        hashMap.put("clcollege",college);
        hashMap.put("clyearcompletion",cper);
        hashMap.put("clpercentage",cper);

        hashMap.put("plusboard",tboard);
        hashMap.put("plusschool",tschool);
        hashMap.put("plusboardyearcompletion",tyoc);
        hashMap.put("pluspercentage",tper);

        hashMap.put("board",mboard);
        hashMap.put("school",mschool);
        hashMap.put("percentage",myoc);
        hashMap.put("boardyearcompletion",mper);


        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, GlobalDetails.mainUrl+"/Api/v1/User/Android/en/updateeducationdetails",new JSONObject(hashMap),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d("show"," "+response);
                            Toast toast=new Toast(getActivity());
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM|Gravity.FILL_HORIZONTAL,0,0);

                            LayoutInflater inf=getActivity().getLayoutInflater();

                            View layoutview=inf.inflate(R.layout.custom_toast,(ViewGroup)getActivity().findViewById(R.id.CustomToast_Parent));
                            TextView tf=layoutview.findViewById(R.id.CustomToast);
                            tf.setText("Details Updated Sucessfully "+ Html.fromHtml("&#x1f604;"));
                            toast.setView(layoutview);
                            toast.show();
                            progressBar.setVisibility(View.GONE);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("logcheck",""+e);
                            progressBar.setVisibility(View.GONE);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("show",""+error);
                        progressBar.setVisibility(View.GONE);
                    }
                }
        ){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", "Basic c2VydmljZW1hbmR1OnNlcnZpY2VtYW5kdUAyMDIw");
                return params;
            }
            //here I want to post data to sever
        };

        requstQueue.add(jsonobj);
    }
}
