package brdevelopers.com.jobvibe;


import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.os.ConditionVariable;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class Edit_PersonalFragment extends Fragment implements View.OnClickListener,View.OnFocusChangeListener,TextWatcher {
    private EditText et_email, et_dob,et_mobile,et_name,et_address,et_pincode,et_city,et_degree,et_fos;
    private RadioButton radio_male,radio_female;
    private ImageView iv_dob;
    private TextView tv_btnnext,tv_age;
    private ProgressBar progressBar;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_edit__personal,container,false);
        //Binding view to references
        et_email= view.findViewById(R.id.ET_email);
        et_name= view.findViewById(R.id.ET_name);
        et_mobile=view.findViewById(R.id.ET_mobie);
        et_address=view.findViewById(R.id.ET_address);
        et_pincode= view.findViewById(R.id.ET_pincode);
        et_city=view.findViewById(R.id.ET_currentcity);
        et_dob=view.findViewById(R.id.ET_dob);
        radio_male=view.findViewById(R.id.Radio_male);
        radio_female=view.findViewById(R.id.Radio_female);
        et_degree=view.findViewById(R.id.ET_degree);
        et_fos=view.findViewById(R.id.ET_fos);
        iv_dob=view.findViewById(R.id.IV_dob);
        tv_btnnext=view.findViewById(R.id.TV_btnnext);
        tv_age=view.findViewById(R.id.TV_age);
        progressBar=view.findViewById(R.id.progressbar);

        String email=Home.canemail;

//        et_email.setText(email);

        if (Util.isNetworkConnected(getActivity())) {
            getCandidateDetail(email);
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
        }

        et_dob.setOnClickListener(this);        //Edit Text dob click
        iv_dob.setOnClickListener(this);        //Image View dob click
        et_dob.setOnFocusChangeListener(this);  //Edit Text dob on focus
        et_mobile.addTextChangedListener(this);//Edit Text mobile on textchange


        tv_btnnext.setOnClickListener(this);

        return view;

    }

    private void getCandidateDetail(final String email){

        RequestQueue requstQueue = Volley.newRequestQueue(getActivity());
        Map<String,String> hashMap = new HashMap<String, String>();
        hashMap.put("sessionId",GlobalDetails.sessionId);

        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, GlobalDetails.mainUrl+"/Api/v1/User/Android/en/getupdateprofiledetails",new JSONObject(hashMap),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response.toString());
                            String success=jsonObject.getString("code");
                            System.out.println(""+jsonObject);
                            if(success.equals("S00"))
                            {

                                String email = jsonObject.getJSONObject("details").getJSONObject("user").getJSONObject("userDetail").getString("email");
                                String name = jsonObject.getJSONObject("details").getJSONObject("user").getJSONObject("userDetail").getString("name");
                                String mobile = jsonObject.getJSONObject("details").getJSONObject("user").getJSONObject("userDetail").getString("mobileNo");
                                et_email.setText(email);
                                et_name.setText(name);
                                et_mobile.setText(mobile);
                                et_address.setText(jsonObject.getJSONObject("details").getString("address"));
                                et_pincode.setText(jsonObject.getJSONObject("details").getString("pincode"));
                                et_city.setText(jsonObject.getJSONObject("details").getString("city"));
                                et_dob.setText(jsonObject.getJSONObject("details").getString("dob"));
                                et_degree.setText(jsonObject.getJSONObject("details").getString("degree"));
                                et_fos.setText(jsonObject.getJSONObject("details").getString("computer"));

                            }
                            else{
                                addupdatedetails();
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


    public void addupdatedetails()
    {
        RequestQueue requstQueue = Volley.newRequestQueue(getActivity());
        Map<String,String> hashMap = new HashMap<String, String>();
        hashMap.put("sessionId",GlobalDetails.sessionId);
        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, GlobalDetails.mainUrl+"/Api/v1/User/Android/en/GetUserDetails",new JSONObject(hashMap),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response.toString());
                            String success=jsonObject.getString("code");
                            if(success.equals("S00"))
                            {

                                String email = jsonObject.getJSONObject("details").getString("email");
                                String name = jsonObject.getJSONObject("details").getString("name");
                                String mobile = jsonObject.getJSONObject("details").getString("mobileNo");
                                et_email.setText(email);
                                et_name.setText(name);
                                et_mobile.setText(mobile);

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

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.TV_btnnext)
        {
            progressBar.setVisibility(View.VISIBLE);

            String email=et_email.getText().toString();
            String name=et_name.getText().toString();
            String mobile=et_mobile.getText().toString();
            String address=et_address.getText().toString();
            String pincode=et_pincode.getText().toString();
            String city=et_city.getText().toString();
            String dob=et_dob.getText().toString();
            String degree=et_degree.getText().toString();
            String fos=et_fos.getText().toString();
            String gender=null;
            boolean bol=radio_male.isChecked();
            if(bol)
                gender="Male";
            else
                gender="Female";


            if(Util.isNetworkConnected(getActivity())) {
                editcandidateDetails(email, name, mobile, address, pincode, city, dob, gender,degree,fos);
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
        else if(v.getId()==R.id.ET_dob)
        {
            //Calling dateOfBirth on click on edittext
            dateOfBirth();
        }
        else if(v.getId()==R.id.IV_dob)
        {
            //Calling dateOfBirth on click on calendar ImageView
            dateOfBirth();
        }

    }

    private void editcandidateDetails(final String email, final String name, final String mobile, final String address, final String pincode, final String city, final String dob, final String gender,final String degree,final String fos) {

         RequestQueue requstQueue = Volley.newRequestQueue(getActivity());
        Map<String,String> hashMap = new HashMap<String, String>();
        hashMap.put("sessionId",GlobalDetails.sessionId);
        hashMap.put("address",address);
        hashMap.put("pincode",pincode);
        hashMap.put("city",city);
        hashMap.put("gender",gender);
        hashMap.put("dob",dob);
        hashMap.put("degree",degree);
        hashMap.put("feildofstudy",fos);


        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, GlobalDetails.mainUrl+"/Api/v1/User/Android/en/updateprofileofjob",new JSONObject(hashMap),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
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
                        Log.d("checklog"," "+error);
                        Toast.makeText(getActivity(), ""+error, Toast.LENGTH_SHORT).show();
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


    private void dateOfBirth() {

        Calendar c=Calendar.getInstance();
        final int dd,mm,yy;
        dd=c.get(Calendar.DAY_OF_MONTH);
        mm=c.get(Calendar.MONTH);
        yy=c.get(Calendar.YEAR);

        DatePickerDialog dpd=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                et_dob.setText(dayOfMonth+"/"+month+"/"+year);
                calculateAge(dd,mm,yy,dayOfMonth,month,year); //calling calculateage to show it on textview age
            }
        },dd,mm,yy);

        dpd.updateDate(1980,0,1);
        dpd.show();
    }

    //On focus on edittext
    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if(v.getId()==R.id.ET_dob && hasFocus)
        {
            dateOfBirth();
        }

    }

// calculating age from datetimepicker and show it on textview age

    public void calculateAge(int cdd,int cmm,int cyy,int bdd,int bmm,int byy)
    {
        cmm++;
        bmm++;
        if(cdd<bdd)
            cmm=cmm-1;
        if(cmm<bmm)
            cyy=cyy-1;

        int age=cyy-byy;

        tv_age.setText("Age : "+age);
        tv_age.setVisibility(View.VISIBLE);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        String phoneno=et_mobile.getText().toString();

        if(phoneno.length()==1) {
            s.replace(0, phoneno.length(),"+91"+phoneno);
        }
        else if(phoneno.length()==3)
        {
            s.replace(0,phoneno.length(),"");
        }

    }



}
