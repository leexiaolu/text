package com.lee.lxl.text;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A login screen that offers login via user/password.
 * 通过电子邮件/密码提供登录的登录界面。
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    // UI references.
    private AutoCompleteTextView mUserView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private AbHttpUtil httputil =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        // 设置登录表单。
        mUserView = (AutoCompleteTextView) findViewById(R.id.user);

        // 初始化httoutil
        httputil=AbHttpUtil.getInstance(LoginActivity.this);
        httputil.setTimeout(10000);
        httputil.setEncode("UTF-8");

        mPasswordView = (EditText) findViewById(R.id.password);
        /**
         * setOnEditorActionListener
         * 设置专门的监听器时的文本视图执行的操作被调用。
         * 当输入键被按下，这将被调用，或者当提供给IME的操作由用户选择。
         * 设置这意味着正常硬键事件将不插入一新行到文本视图，
         * 即使它是多线;按住ALT键修改将，但是，允许用户插入一个换行符。
         */
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {

                    return true;
                }
                return false;
            }
        });

        Button muserSignInButton = (Button) findViewById(R.id.user_sign_in_button);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        muserSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isuserValid(mUserView.getText().toString())){
                    if(isPasswordValid(mPasswordView.getText().toString())){
                        final AbRequestParams params = new AbRequestParams();
                        params.put("username",mUserView.getText().toString());
                        params.put("password",mPasswordView.getText().toString());
                        httputil.post("http://www.snriud.com/m.login.handle.php", params, new AbStringHttpResponseListener() {
                            @Override
                            public void onSuccess(int i, String s) {
                                Log.e("-----","onSuccess"+s);
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    if(jsonObject.getString("errorcode").equals("0")){
                                        AbToastUtil.showToast(LoginActivity.this,"登录成功");
                                        JSONObject object = jsonObject.getJSONObject("userinfo");
                                        // 用SharedPreferences保存了用户的一些信息。
                                        SharedPreferences preferences =getSharedPreferences("lxl", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("id",object.getString("id"));
                                        editor.putString("username",object.getString("username"));
                                        editor.putString("email",object.getString("email"));
                                        editor.commit();
                                        // 切记使用SharedPreferences 之后要commit()
                                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                        startActivity(intent);
                                    }else if(jsonObject.getString("errorcode").equals("1")){
                                        AbToastUtil.showToast(LoginActivity.this,"该用户没有注册");
                                    }else if(jsonObject.getString("errorcode").equals("2")){
                                        AbToastUtil.showToast(LoginActivity.this,"请重新输入密码");
                                        mPasswordView.setText("");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onStart() {
                                Log.e("-----","onStart");
                                showProgress(true);
                            }

                            @Override
                            public void onFinish() {
                                Log.e("-----","onFinish");
                                showProgress(false);
                            }

                            @Override
                            public void onFailure(int i, String s, Throwable throwable) {
                               AbToastUtil.showToast(LoginActivity.this,s);
                            }
                        });
                    }else{
                        AbToastUtil.showToast(LoginActivity.this,"密码长度不正确");
                    }
                }else{
                    AbToastUtil.showToast(LoginActivity.this,"请输入用户名");
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
     * @param password
     * @return 判断密码长度是否>4
     */
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     * 显示进程的用户界面并隐藏登录表单。
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
            // viewpropertyanimator API是不可用的，所以简单的显示和隐藏相关的UI组件。
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
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }
}

