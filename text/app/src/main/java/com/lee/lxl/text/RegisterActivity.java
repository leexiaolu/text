package com.lee.lxl.text;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbToastUtil;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class RegisterActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private AutoCompleteTextView mRegisterUserView, mEmailView;
    private EditText mPasswordView, mSecondPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private AbHttpUtil httpUtil = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mRegisterUserView = (AutoCompleteTextView) findViewById(R.id.register_username);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.register_email);

        //获取httputil
        httpUtil = AbHttpUtil.getInstance(RegisterActivity.this);
        httpUtil.setTimeout(10000);
        httpUtil.setEncode("UTF-8");

        mPasswordView = (EditText) findViewById(R.id.password);
        mSecondPasswordView = (EditText) findViewById(R.id.second_password);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isuserValid(mRegisterUserView.getText().toString())){
                    if(isEmailValid(mEmailView.getText().toString())){
                        if(isPasswordValid(mPasswordView.getText().toString())){
                            if(mPasswordView.getText().toString().equals(mSecondPasswordView.getText().toString())){
                final AbRequestParams params = new AbRequestParams();
                                params.put("username ",mRegisterUserView.getText().toString());
                                params.put("email",mEmailView.getText().toString());
                                params.put("password ",mSecondPasswordView.getText().toString());
                httpUtil.post("http://www.snriud.com/m.register.handle.php", params, new AbStringHttpResponseListener() {
                    @Override
                    public void onSuccess(int i, String json) {
                        Log.e("-----", "onSuccess-" + i + "---" + json);
                    }

                    @Override
                    public void onStart() {
                        showProgress(true);
                    }

                    @Override
                    public void onFinish() {
                        showProgress(false);
                    }

                    @Override
                    public void onFailure(int i, String s, Throwable throwable) {
                        AbToastUtil.showToast(RegisterActivity.this, s);
                    }
                });
                            }else{
                                AbToastUtil.showToast(RegisterActivity.this,"两次密码设置不一致，请重新设置");
                                mPasswordView.setText("");
                                mSecondPasswordView.setText("");
                            }
                        }else{
                            AbToastUtil.showToast(RegisterActivity.this,"密码必须大于4位");
                        }
                    }else{
                        AbToastUtil.showToast(RegisterActivity.this,"邮箱格式不合法");
                    }
                }else{
                    AbToastUtil.showToast(RegisterActivity.this,"用户名不可以为空");
                }
            }
        });


    }

    /**
     * @param user
     * @return 判断用户名是不是有效
     */
    private boolean isuserValid(String user) {
        //TODO: Replace this with your own logic
        if (user != null && user != "") {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param email
     * @return 判断邮箱是不是有效
     */
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

