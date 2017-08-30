package tastyorder.tastyorder;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by pptroll on 2017-07-19.
 */

public class Menu extends AppCompatActivity  {

    String title, businessnumber, topcategoryname, categoryname;
    String resultXML;

    DocumentBuilderFactory t_dbf = null;
    DocumentBuilder t_db = null;
    Document t_doc = null;
    NodeList t_nodes = null;
    Element t_element = null;
    InputSource t_is = new InputSource();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Data 받기
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        businessnumber = intent.getStringExtra("businessnumber");

        TextView text_title= (TextView)findViewById(R.id.text_title);
        text_title.setText(title);

        Menu_ViewPagerAdapter adapter = new Menu_ViewPagerAdapter(getSupportFragmentManager());

        TabLayout mTopTabLayout = (TabLayout) findViewById(R.id.Toptab);
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tab);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);

        try {
            //TopCategory 설정
            resultXML = new CustomTask().execute("type=getTopcategory&businessnumber="+businessnumber).get();

            t_dbf = DocumentBuilderFactory.newInstance();
            t_db = t_dbf.newDocumentBuilder();
            t_is = new InputSource();
            t_is.setCharacterStream(new StringReader(resultXML));
            t_doc = t_db.parse(t_is);
            t_nodes = t_doc.getElementsByTagName("Row");

            for (int i = 0, t_len = t_nodes.getLength();
                 i < t_len; i ++)
            {
                t_element = (Element)t_nodes.item(i);
                mTopTabLayout.addTab(mTopTabLayout.newTab().setText(t_element.getChildNodes().item(1).getTextContent()));
            }

            //Category 설정
            topcategoryname = t_nodes.item(0).getChildNodes().item(1).getTextContent();//첫번째 topcategory
            resultXML = new CustomTask().execute("type=getCategory&businessnumber="+businessnumber+"&topcategoryname="+topcategoryname).get();

            t_dbf = DocumentBuilderFactory.newInstance();
            t_db = t_dbf.newDocumentBuilder();
            t_is = new InputSource();
            t_is.setCharacterStream(new StringReader(resultXML));
            t_doc = t_db.parse(t_is);
            t_nodes = t_doc.getElementsByTagName("Row");

            for (int i = 0, t_len = t_nodes.getLength();
                 i < t_len; i ++)
            {
                t_element = (Element)t_nodes.item(i);
                categoryname = t_element.getChildNodes().item(2).getTextContent();
                adapter.addFragment(businessnumber, topcategoryname, categoryname, new Menu_ItemFragment());
            }

            mViewPager.setAdapter(adapter);
            mTabLayout.setupWithViewPager(mViewPager);

            mTopTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    try{
                        topcategoryname = tab.getText().toString();

                        Menu_ViewPagerAdapter adapter = new Menu_ViewPagerAdapter(getSupportFragmentManager());

                        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tab);
                        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);

                        resultXML = new CustomTask().execute("type=getCategory&businessnumber="+businessnumber+"&topcategoryname="+topcategoryname).get();

                        t_dbf = DocumentBuilderFactory.newInstance();
                        t_db = t_dbf.newDocumentBuilder();
                        t_is = new InputSource();
                        t_is.setCharacterStream(new StringReader(resultXML));
                        t_doc = t_db.parse(t_is);
                        t_nodes = t_doc.getElementsByTagName("Row");

                        for (int i = 0, t_len = t_nodes.getLength();
                             i < t_len; i ++)
                        {
                            t_element = (Element)t_nodes.item(i);
                            categoryname = t_element.getChildNodes().item(2).getTextContent();
                            adapter.addFragment(businessnumber, topcategoryname, categoryname, new Menu_ItemFragment());
                        }

                        mViewPager.setAdapter(adapter);
                        mTabLayout.setupWithViewPager(mViewPager);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://220.72.127.7:8081/TastyOrder/Menu.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = strings[0];
                osw.write(sendMsg);
                osw.flush();
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();

                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg;
        }
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}
