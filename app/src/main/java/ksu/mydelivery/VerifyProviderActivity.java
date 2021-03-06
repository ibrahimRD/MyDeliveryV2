package ksu.mydelivery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import org.json.JSONObject;

import java.util.HashMap;

public class VerifyProviderActivity extends AppCompatActivity {



    private Admin admin;
    private Provider provider;
    private String providerPhone , fName , lName , verify , adminID;
    private EditText EproviderPhone;
    private TextView Tname , Tverify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_provider);


        admin = (Admin) getIntent().getSerializableExtra("Admin");
        provider = (Provider) getIntent().getSerializableExtra("Provider");

        ImageButton searchProvider = (ImageButton) findViewById(R.id.imgbtnSearchProvider);
        searchProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchProvider();

            }
        });


        Button suspendUser = (Button) findViewById(R.id.btnVerify);
        suspendUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyProvider();

            }
        });


        Button unVerifyProvider = (Button) findViewById(R.id.btnUnVerify);
        unVerifyProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unVerifyProvider();

            }
        });

    }


    public void searchProvider(){

        EproviderPhone = (EditText) findViewById(R.id.txtProviderPhone) ;
        providerPhone = String.valueOf(EproviderPhone.getText());


        Tname = (TextView) findViewById(R.id.txtvName) ;
        Tname.setText(" ");
        Tverify = (TextView) findViewById(R.id.txtvIsVerified);
        Tverify.setText(" ");
        

        (findViewById(R.id.txtProviderPhone)).clearFocus();


        HashMap updateProvider = new HashMap();

            updateProvider.put("txtPhone" , providerPhone);

            PostResponseAsyncTask task1 = new PostResponseAsyncTask(VerifyProviderActivity.this, updateProvider, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    if (s.contains("success ")) {
                        s = s.replace("success ", "");

                        try {
                            JSONObject json = new JSONObject(s);

                            provider = new Provider(json.getString("password"), json.getString("email"), json.getString("first_name"), json.getString("last_name")
                                    , json.getString("phone_number"), json.getString("birth_date"), json.getLong("nationalID") , json.getString("is_verified") , json.getDouble("rate_app"));
                            provider.setID(Integer.parseInt(json.getString("providerID")));
                        } catch (org.json.JSONException e) {
                            Toast.makeText(VerifyProviderActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                        }


                        Tname = (TextView) findViewById(R.id.txtvName) ;
                        Tname.setText(provider.getfName() + " " + provider.getlName());
                        Tverify = (TextView) findViewById(R.id.txtvIsVerified);

                        if(provider.isVerified()){
                            Tverify.setText("Verified !");
                        }
                        else if (! provider.isVerified())
                            Tverify.setText("Not Verified !");
                    }

                    else {
                        Toast.makeText(VerifyProviderActivity.this, "Failed", Toast.LENGTH_LONG).show();
                    }
                }
            });

            task1.execute("http://10.0.2.2/provider/searchProvider.php");




    }


    public void verifyProvider(){

        Tname = (TextView) findViewById(R.id.txtvName) ;
        Tname.setText(" ");
        Tverify = (TextView) findViewById(R.id.txtvIsVerified);
        Tverify.setText(" ");

        if(provider != null) {

            providerPhone = providerPhone.replaceFirst("0","");


            (findViewById(R.id.txtProviderPhone)).clearFocus();

            if (!providerPhone.equals(String.valueOf(provider.getPhoneNumber()))) {
                (findViewById(R.id.txtProviderPhone)).requestFocus();
                ((EditText) (findViewById(R.id.txtProviderPhone))).setError("YOU SHOULD SEARCH FOR USER FIRST");
            } else {

                HashMap updateProvider = new HashMap();
                adminID = String.valueOf(admin.getID());

                updateProvider.put("txtPhone", providerPhone);
                updateProvider.put("txtAdminID", adminID);


                PostResponseAsyncTask task1 = new PostResponseAsyncTask(VerifyProviderActivity.this, updateProvider, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if (s.contains("success")) {
                            provider.setVerified(true);

                            Toast.makeText(VerifyProviderActivity.this, "Success", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(VerifyProviderActivity.this, "Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                task1.execute("http://10.0.2.2/provider/verifyProvider.php");

            }
        }
        else {
            (findViewById(R.id.txtProviderPhone)).requestFocus();
            ((EditText) (findViewById(R.id.txtProviderPhone))).setError("YOU SHOULD SEARCH FOR USER FIRST");
        }

    }



    public void unVerifyProvider(){

        Tname = (TextView) findViewById(R.id.txtvName) ;
        Tname.setText(" ");
        Tverify = (TextView) findViewById(R.id.txtvIsVerified);
        Tverify.setText(" ");

        if(provider != null) {

            providerPhone = providerPhone.replaceFirst("0","");

            (findViewById(R.id.txtProviderPhone)).clearFocus();

            if (!providerPhone.equals(String.valueOf(provider.getPhoneNumber()))) {
                (findViewById(R.id.txtProviderPhone)).requestFocus();
                ((EditText) (findViewById(R.id.txtProviderPhone))).setError("YOU SHOULD SEARCH FOR USER FIRST");

            } else {

                HashMap updateProvider = new HashMap();

                updateProvider.put("txtPhone", providerPhone);


                PostResponseAsyncTask task1 = new PostResponseAsyncTask(VerifyProviderActivity.this, updateProvider, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if (s.contains("success")) {
                            provider.setVerified(false);

                            Toast.makeText(VerifyProviderActivity.this, "Success", Toast.LENGTH_LONG).show();

                            (findViewById(R.id.txtProviderPhone)).clearFocus();


                        } else {
                            Toast.makeText(VerifyProviderActivity.this, "Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                task1.execute("http://10.0.2.2/provider/invalidateProvider.php");

            }
        }
        else {
            (findViewById(R.id.txtProviderPhone)).requestFocus();
            ((EditText) (findViewById(R.id.txtProviderPhone))).setError("YOU SHOULD SEARCH FOR USER FIRST");
        }

    }




}
