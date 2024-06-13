package com.example.beberagua_lembrete;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class FormCadastro extends AppCompatActivity {
    private EditText edit_email,edit_senha;
    private Button button_cadastrar;
    String[] mensagens = {"preencha todos os campos", "cadastro realizado com sucesso"};

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_cadastro);
        IniciarComponents();
        button_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edit_email.getText().toString() ;
                String senha = edit_senha.getText().toString();
                if(email.isEmpty()|| senha.isEmpty()){
                    Snackbar snackbar = Snackbar.make(v,mensagens[0],Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else{
                    CadastrarUsuario(v);
                }
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
    private void CadastrarUsuario(View v){
        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();
        auth.createUserWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Snackbar snackbar = Snackbar.make(v,mensagens[1],Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else{
                    String erro;
                    try {
                        throw  task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        erro = "digita senha com pelo menos 6 digitos";
                    }catch (FirebaseAuthUserCollisionException e){
                        erro = "email já cadastrado";

                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erro = "email invalido";

                    }catch (Exception e){
                        erro = "erro ao cadastrar usuário";
                    }
                    Snackbar snackbar = Snackbar.make(v,erro,Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.show();
                }
            }
        });
    }
    private void IniciarComponents(){
        edit_email =findViewById(R.id.edit_email);
        edit_senha =findViewById(R.id.edit_senha);
        button_cadastrar = findViewById(R.id.button_cadastrar);

    }


}