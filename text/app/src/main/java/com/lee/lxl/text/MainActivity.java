package com.lee.lxl.text;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListListener;
import com.ab.util.AbDialogUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.lee.lxl.adapter.HomeAdapter;
import com.lee.lxl.bean.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.lee.lxl.bean.News.*;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,AbPullToRefreshView.OnHeaderRefreshListener,AbPullToRefreshView.OnFooterLoadListener{

    private AbPullToRefreshView mAbPullToRefreshView = null;
    private ListView mListView;
    private HomeAdapter homeAdapter;
    private ArrayList<News.PostEntity> list2 = new ArrayList<News.PostEntity>();
    private AbHttpUtil abHttpUtil = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAbPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
        mListView = (ListView) findViewById(R.id.mListView);

        getNews();

        mAbPullToRefreshView.setOnFooterLoadListener(MainActivity.this);
        mAbPullToRefreshView.setOnHeaderRefreshListener(MainActivity.this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Replace with your own action 使用你自己的action代替
                Snackbar.make(view, "123456789", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //DrawerLayout 这个是显示侧滑之后的布局，然而在这里是给activity_main.xml的主要布局设置了id来引用的
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //ActionBarDrawerToggle  是 DrawerLayout.DrawerListener实现
        /**
         * 作用：
         *1.改变android.R.id.home返回图标。
         *2.Drawer拉出、隐藏，带有android.R.id.home动画效果。
         *3.监听Drawer拉出、隐藏；
         */

        /**
         * ActionBarDrawerToggle   参数介绍
         * @param activity                  The Activity hosting the drawer.
         * @param toolbar                   The toolbar to use if you have an independent Toolbar.
         * @param drawerLayout              The DrawerLayout to link to the given Activity's ActionBar
         * @param openDrawerContentDescRes  A String resource to describe the "open drawer" action
         *                                  for accessibility
         * @param closeDrawerContentDescRes A String resource to describe the "close drawer" action
         *                                  for accessibility
         */
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        /**
         * NavigationView是一个导航菜单框架，使用menu资源填充数据，
         * 使我们可以更简单高效的实现导航菜单。
         * 它提供了不错的默认样式、选中项高亮、分组单选、分组子标题、以及可选的Header。
         */
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void getNews(){
        //初始化网络请求
        abHttpUtil = AbHttpUtil.getInstance(this);
        abHttpUtil.setTimeout(10000);
        abHttpUtil.setEncode("UTF-8");
        //post请求需要携带的参数
        final AbRequestParams params = new AbRequestParams();
        params.put("action","allposts");
        abHttpUtil.post("http://www.snriud.com/m.showposts.handle.php", params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int code, String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray array = jsonObject.getJSONArray("post");
                    for(int i = 0; i<array.length(); i ++){
                        JSONObject object = array.getJSONObject(i);
                        News news = new News();
                        News.PostEntity postEntity = news.new PostEntity();
                        postEntity.setTitle(object.getString("title"));
                        postEntity.setContent(object.getString("content"));
                        postEntity.setUsername(object.getString("username"));
                        postEntity.setCtime(object.getString("ctime"));
                        list2.add(postEntity);
                    }
                    homeAdapter = new HomeAdapter(MainActivity.this,list2);
                    mListView.setAdapter(homeAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onStart() {
                AbDialogUtil.showAlertDialog(MainActivity.this,"正在加载数据");
            }

            @Override
            public void onFinish() {
                AbDialogUtil.removeDialog(MainActivity.this);
            }

            @Override
            public void onFailure(int i, String s, Throwable throwable) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * onCreateOptionsMenu 这个方法，就是在activity的右上角添加menu  然后设置布局显示出来。
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // 如果是当前的，这将增加行动栏的项目。
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /**
         * 处理动作栏项目点击这里。动作栏将自动处理在家/按钮 点击，只要你指定在AndroidManifest.xml家长活动。
         */
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_login) {
            // Handle the camera action
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_register) {
            Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_userdata) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFooterLoad(AbPullToRefreshView abPullToRefreshView) {
        AbTask mAbTask = AbTask.newInstance();
        final AbTaskItem item = new AbTaskItem();
        item.setListener(new AbTaskListListener() {
            @Override
            public List<?> getList() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void update(List<?> list) {
                homeAdapter.notifyDataSetChanged();
                mAbPullToRefreshView.onHeaderRefreshFinish();
            }
        });
        mAbTask.execute(item);
    }

    @Override
    public void onHeaderRefresh(AbPullToRefreshView abPullToRefreshView) {
        AbTask mAbTask = AbTask.newInstance();
        final AbTaskItem item = new AbTaskItem();
        item.setListener(new AbTaskListListener() {
            @Override
            public List<?> getList() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void update(List<?> list) {
                homeAdapter.notifyDataSetChanged();
                mAbPullToRefreshView.onFooterLoadFinish();
            }
        });
        mAbTask.execute(item);
    }
}
