package com.example.autgmail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.util.Date;

public class PerfilActivity extends AppCompatActivity {

    ImageView imgFoto;
    TextView txtNombre, txtCorreo, txtProveedor, txtUltimoIngreso, txtUid;
    Button btnCerrarSesion;

    FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Referencias a la vista
        imgFoto = findViewById(R.id.imgFoto);
        txtNombre = findViewById(R.id.txtNombre);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtProveedor = findViewById(R.id.txtProveedor);
        txtUltimoIngreso = findViewById(R.id.txtUltimoIngreso);
        txtUid = findViewById(R.id.txtUid);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        cargarFotoPerfil(user);
        // Si NO hay usuario, volver al login
        if (user == null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        // Nombre
        txtNombre.setText("Bienvenido, " + user.getDisplayName());
        // Correo
        txtCorreo.setText("Correo: " + user.getEmail());
        // Proveedor
        txtProveedor.setText("Proveedor: Google");
        // Último inicio de sesión
        Date lastSignIn = new Date(user.getMetadata().getLastSignInTimestamp());
        String fecha = DateFormat.getDateTimeInstance().format(lastSignIn);
        txtUltimoIngreso.setText("Último ingreso: " + fecha);
        // UID
        txtUid.setText("UID: " + user.getUid());
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }
     public void CerrarSesion(View view)
    {
        FirebaseAuth.getInstance().signOut();
        googleSignInClient.signOut();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void cargarFotoPerfil(FirebaseUser user) {
        if (user.getPhotoUrl() != null) {
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .into(imgFoto);
        }
    }
}