package helperapp.chenchik.helprapp;

/**
 * Author :Raj Amal
 * Email  :raj.amalw@example.android.loginapp.com
 * Website:www.example.android.loginapp.com
 **/

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

import helperapp.chenchik.helprapp.library.DatabaseHandler;
import helperapp.chenchik.helprapp.library.UserFunctions;

public class Main extends Activity {
    Button btnLogout;
    Button changepas;




    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        changepas = (Button) findViewById(R.id.btchangepass);
        btnLogout = (Button) findViewById(R.id.logout);

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        /**
         * Hashmap to load data from the Sqlite database
         **/
         HashMap<String,String> user = new HashMap<String, String>();
         user = db.getUserDetails();


        /**
         * Change Password Activity Started
         **/
        changepas.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){

                Intent chgpass = new Intent(getApplicationContext(), helperapp.chenchik.helprapp.ChangePassword.class);

                startActivity(chgpass);
            }

        });

       /**
        *Logout from the User Panel which clears the data in Sqlite database
        **/
        btnLogout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                UserFunctions logout = new UserFunctions();
                logout.logoutUser(getApplicationContext());
                Intent login = new Intent(getApplicationContext(), Login.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);
                finish();
            }
        });
/**
 * Sets user first name and last name in text view.
 **/
        final TextView welcome  = (TextView) findViewById(R.id.textwelcome);
        welcome.setText("Welcome  " + user.get("fname") + " " + user.get("lname"));
        final TextView email = (TextView) findViewById(R.id.textemail);
        email.setText(user.get("email"));
        final TextView created_at = (TextView) findViewById(R.id.textcreated_at);
        created_at.setText(user.get("created_at"));



    }}