package hu.pe.thinkanddo.vendedorbodegavirtualv3.actividades;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.HashMap;

import hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades.MySingleton;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.R;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades.Preferences;
import hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades.Variables;


public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText mEmailField,mPasswordField;
    private ProgressDialog pDialog;

    private SharedPreferences pref;
    private String id_tienda;
    private String comision;
    private String habilitado;
    private String currentVersion;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            currentVersion = pInfo.versionName;
            GetVersionCode versionCode = new GetVersionCode(this);
            versionCode.execute();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        pDialog=new ProgressDialog(this);
        pDialog.setMessage("Espere por favor...");
        pDialog.setCancelable(false);

        Variables.TOKEN = FirebaseInstanceId.getInstance().getToken();
        mAuth = FirebaseAuth.getInstance();
        mEmailField = findViewById(R.id.et_usuario);
        mPasswordField = findViewById(R.id.et_clave);
        //progres = (ProgressBar)findViewById(R.id.progressBar2);
        final Button mLoginBtn = findViewById(R.id.btn_ingresar);
        ImageButton olvideClave = findViewById(R.id.btn_olvide_clave);

        database = FirebaseDatabase.getInstance();



        olvideClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(MainActivity.this,OlvideClave.class);
                startActivity(i);
            }
        });

        pref = getSharedPreferences("preference", Context.MODE_PRIVATE);
        setCredentialsIfExits();

        //Toast.makeText(this,FirebaseInstanceId.getInstance().getToken(),Toast.LENGTH_LONG).show();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


                if(firebaseAuth.getCurrentUser()!=null){

                    Intent i=new Intent(MainActivity.this,PantallaPedidos.class);
                    //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);

                    //subirToken(firebaseAuth.getCurrentUser().getEmail(),Variables.TOKEN,"1");
                    //cambiarEstado("1",id_tienda);



                }else{
                    //cambiarEstado("0",id_tienda);
                }
                //Toast.makeText(getApplicationContext(), "cambio de estado", Toast.LENGTH_SHORT).show();
                //cambiarEstado();

            }
        };

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email = mEmailField.getText().toString().trim();
                String password = mPasswordField.getText().toString().trim();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                    showpDialog();
                    LoginUsuario(email,password, Variables.TOKEN);
                }else{
                    Toast.makeText(MainActivity.this, "Datos no validos", Toast.LENGTH_SHORT).show();

                }


            }
        });



    }



    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(this, "OnResume", Toast.LENGTH_SHORT).show();
        /*cambiarDisponibilidad(Preferences.load_id_tienda(this));
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            Intent i=new Intent(MainActivity.this,PantallaPedidos.class);
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }*/

    }

    private void cambiarDisponibilidad(String s) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        hidepDialog();
    }


    private void setCredentialsIfExits() {
        String mail = Preferences.getUserMailPrefs(pref);
        String pass = Preferences.getUserPassPrefs(pref);
        if(!TextUtils.isEmpty(mail) && !TextUtils.isEmpty(pass)){
            mEmailField.setText(mail);
            mPasswordField.setText(pass);
        }
    }

    private void LoginUsuario(final String maili, final String password, final String tokens) {

        mAuth.signInWithEmailAndPassword(maili, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            saveOnPreference(maili,password);

                            database.getReference("condominios").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                        if (snapshot.child("correo").getValue().equals(maili.toLowerCase())){
                                            if ((boolean)snapshot.child("habilitado").getValue()){
                                                Preferences.save_id_tienda(MainActivity.this,String.valueOf(snapshot.child("id_condominio").getValue()));
                                                Preferences.save_comision(MainActivity.this,String.valueOf(snapshot.child("comision").getValue()));
                                                Intent i=new Intent(MainActivity.this,PantallaPedidos.class);
                                                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(i);
                                                finish();
                                                break;
                                            }else{
                                                Intent in=new Intent(MainActivity.this,TiendaBloqueada.class);
                                                //in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(in);
                                                finish();
                                                break;
                                            }

                                        }else {
                                            Toast.makeText(MainActivity.this, "El correo electrónico no esta registrado correctamente", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            //subirToken(maili,tokens);
                        }else{

                            Toast.makeText(MainActivity.this, "Datos incorrectos ", Toast.LENGTH_SHORT).show();
                            hidepDialog();
                        }
                    }
                });
    }

    private void subirToken(String maili, String tokeno) {

        HashMap<String,String> parau = new HashMap<>();
        parau.put("token", tokeno);
        parau.put("correo", maili);
        parau.put("activo", "1");

        JsonObjectRequest reqi = new JsonObjectRequest(Request.Method.POST,"http://52.67.109.198/ServicioWebVendedor/subirTokenTienda.php",new JSONObject(parau),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {


                            id_tienda = response.getString("ID_TIENDA");
                            comision = response.getString("COMISION");
                            habilitado = response.getString("HABILITADO");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Preferences.save_id_tienda(getApplicationContext(),id_tienda);
                        Preferences.save_comision(getApplicationContext(),comision);

                        if(habilitado.equals("1")){
                            Intent i=new Intent(MainActivity.this,PantallaPedidos.class);
                            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            //finish();
                        }else{
                            Intent in=new Intent(MainActivity.this,TiendaBloqueada.class);
                            //in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(in);
                            //finish();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.toString(), Toast.LENGTH_SHORT).show();
                hidepDialog();
            }
        });


        MySingleton.getInstance(MainActivity.this).addToRequestQueue(reqi);
    }

    private void saveOnPreference(String mail, String clave){

            SharedPreferences.Editor editor = pref.edit();
            editor.putString("mail",mail);
            editor.putString("clave",clave);
            editor.apply();

    }

    /*private  void cambiarDisponibilidad(String idTienda){


        HashMap<String,String> parau = new HashMap<>();
        parau.put("activo", "0");
        parau.put("idTienda", idTienda);

        String URL_CAMBIAR_DISPONIBILIDAD = "http://52.67.109.198/ServicioWebVendedor/cambiar_disponibilidad.php";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_CAMBIAR_DISPONIBILIDAD,new JSONObject(parau),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {




                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });


        MySingleton.getInstance(this).addToRequestQueue(request);

    }*/

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @SuppressLint("StaticFieldLeak")
    private class GetVersionCode extends AsyncTask<Void, String, String> {

        private final Context context;

        GetVersionCode(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... voids) {

            String newVersion;
            try {
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName() + "&hl=it")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div[itemprop=softwareVersion]")
                        .first()
                        .ownText();
                return newVersion;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);
            if (onlineVersion != null && !onlineVersion.isEmpty()) {
                if (!currentVersion.equalsIgnoreCase(onlineVersion)) {
                    //show dialog
                    showForceUpdateDialog(context);
                    //Toast.makeText(Splash.this, "Necesitas actualizar a la ultima version de Mi Bodeguita", Toast.LENGTH_SHORT).show();
                }
            }
            Log.d("update", "Current version " + currentVersion + " playstore version " + onlineVersion);
        }

        void showForceUpdateDialog(final Context ctx){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);

            alertDialogBuilder.setTitle(ctx.getString(R.string.youAreNotUpdatedTitle));
            alertDialogBuilder.setMessage(ctx.getString(R.string.youAreNotUpdatedMessage));
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + ctx.getPackageName())));
                    dialog.cancel();
                }
            });
            alertDialogBuilder.show();
        }
    }



}
