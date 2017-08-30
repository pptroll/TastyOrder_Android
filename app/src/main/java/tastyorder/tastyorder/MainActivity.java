package tastyorder.tastyorder;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {
    String Test = null;
    String resultXML = null;

    ListView listview ;
    Main_ListViewAdapter adapter;

    DocumentBuilderFactory t_dbf = null;
    DocumentBuilder t_db = null;
    Document t_doc = null;
    NodeList t_nodes = null;
    Node t_node = null;
    Element t_element = null;
    InputSource t_is = new InputSource();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_search).setOnClickListener(ButtonClick);

        // Adapter 생성
        adapter = new Main_ListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);

        try {
            resultXML = new CustomTask().execute("getList").get();

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
                String imageURL= "http://220.72.127.7:8081/TastyOrder/Images/"+t_element.getChildNodes().item(0).getTextContent()+".jpg";
                adapter.addItem(imageURL,
                        t_element.getChildNodes().item(1).getTextContent(), t_element.getChildNodes().item(2).getTextContent()) ;
            }

            //ListView 클릭 이벤트
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), Menu.class);

                    //BusinessNumber 전달
                    intent.putExtra("businessnumber", t_nodes.item(position).getChildNodes().item(0).getTextContent());
                    intent.putExtra("title", t_nodes.item(position).getChildNodes().item(1).getTextContent());

                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            Log.d("E_메인", e.getMessage());
        }
    }

    Button.OnClickListener ButtonClick = new View.OnClickListener(){
        public void onClick(View v) {
//            try {
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
    };

    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://220.72.127.7:8081/TastyOrder/Store.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "type=" + strings[0];
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
                Log.d("E_통신", e.getMessage());
            }
            return receiveMsg;
        }
    }
}
