package br.com.icaro.agende.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import br.com.icaro.agende.R;
import br.com.icaro.agende.listener.AuthenticationListener;
import br.com.icaro.agende.model.JWToken;
import br.com.icaro.agende.service.LoginService;
import br.com.icaro.agende.utils.SessionUtils;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private ProgressBar loading;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);


        if(SessionUtils.isLogged()) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        } else {
            setViewElementsById();
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    login();
                }
            });
        }
    }

    private void setViewElementsById() {
        usernameEditText = findViewById(R.id.login_username);
        passwordEditText = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        loginButton.setVisibility(View.VISIBLE);
        loading =  findViewById(R.id.loading);
        loading.getIndeterminateDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
        loading.setIndeterminate(true);
        loading.setVisibility(View.INVISIBLE);
    }

    private void login() {
        this.username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (!username.trim().isEmpty() && !password.trim().isEmpty()) {
            LoginService loginService = new LoginService();
            loginService.setAuthenticationListener(getAuthenticationListener());
            loginService.authenticate(username, password);
        } else {
            loginButton.setVisibility(View.VISIBLE);
            loading.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), R.string.unfilled_fields, Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    private AuthenticationListener getAuthenticationListener() {
        return new AuthenticationListener() {
            @Override
            public void onSuccess(JWToken resultToken) {
                SessionUtils.writeToken(resultToken);
                SessionUtils.writeUser(username);
                LoginTask loginTask = new LoginTask();
                loginTask.execute();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        };
    }

    public class LoginTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            loginButton.setVisibility(View.INVISIBLE);
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Intent intent;
            intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
            return null;
        }
    }
}
