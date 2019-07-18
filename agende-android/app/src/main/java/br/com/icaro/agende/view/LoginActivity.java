package br.com.icaro.agende.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import br.com.icaro.agende.R;
import br.com.icaro.agende.model.JWToken;
import br.com.icaro.agende.service.LoginService;
import br.com.icaro.agende.service.listener.AuthenticationListener;
import br.com.icaro.agende.utils.SessionUtils;

public class LoginActivity extends AppCompatActivity {

    private EditText registrationEditText, passwordEditText;
    private Button loginButton;
    private ProgressBar loading;
    private String username, password;
    public static final String EMPTY = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        setViewElementsById();
        login();
    }


    private void login() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPassword(passwordEditText.getText().toString());
                setUsername(registrationEditText.getText().toString());
                if (!getUsername().equals(EMPTY) && !getPassword().equals(EMPTY)) {
                    LoginService userService = new LoginService(getApplicationContext());
                    userService.setAuthenticationListener(getAuthenticationListener());
                    userService.setCredentials(getUsername(), getPassword());
                    userService.authenticate(getApplicationContext());
                } else {
                    loginButton.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), R.string.unfilled_fields, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void setViewElementsById() {
        registrationEditText = findViewById(R.id.login_username);
        passwordEditText = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        loginButton.setVisibility(View.VISIBLE);
        loading =  findViewById(R.id.loading);
        loading.getIndeterminateDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
        loading.setIndeterminate(true);
        loading.setVisibility(View.INVISIBLE);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NonNull
    private AuthenticationListener getAuthenticationListener() {
        return new AuthenticationListener() {
            @Override
            public void onSuccess(JWToken resultToken) {
                SessionUtils.writeToken(resultToken, getApplicationContext());
                SessionUtils.writeUser(getUsername(), getApplicationContext());
                LoginTask loginTask = new LoginTask();
                loginTask.execute();
            }

            @Override
            public void onFailure(String errorMessage) {
                if(errorMessage != null)
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
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
