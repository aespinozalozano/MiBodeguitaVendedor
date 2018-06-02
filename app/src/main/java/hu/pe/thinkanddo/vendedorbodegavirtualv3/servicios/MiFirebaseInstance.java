package hu.pe.thinkanddo.vendedorbodegavirtualv3.servicios;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import hu.pe.thinkanddo.vendedorbodegavirtualv3.utilidades.Variables;


public class MiFirebaseInstance extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh()
    {
        super.onTokenRefresh();
        Variables.TOKEN = FirebaseInstanceId.getInstance().getToken();
       // Log.d("Token",Variables.TOKEN);

    }


}

