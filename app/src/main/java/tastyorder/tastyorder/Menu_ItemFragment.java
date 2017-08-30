package tastyorder.tastyorder;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import java.io.Serializable;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by pptroll on 2017-07-25.
 */

public class Menu_ItemFragment extends Fragment implements View.OnClickListener {
    String title, businessnumber, topcategoryname, categoryname;
    String itemname, itemprice;
    String resultXML = null;

    public static ArrayList<Order_ListViewItem> orderlist = new ArrayList<>();;

    Button button_back, button_order;

    ListView listview ;
    Menu_ListViewAdapter adapter;

    DocumentBuilderFactory t_dbf = null;
    DocumentBuilder t_db = null;
    Document t_doc = null;
    NodeList t_nodes = null;
    Node t_node = null;
    Element t_element = null;
    InputSource t_is = new InputSource();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        int resId = R.layout.fragment_menuitem;
        return inflater.inflate(resId, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        businessnumber = getArguments().getString("businessnumber");
        topcategoryname = getArguments().getString("topcategoryname");
        categoryname = getArguments().getString("categoryname");

        // Adapter 생성
        adapter = new Menu_ListViewAdapter() ;

        //주문하기 버튼
        button_order = (Button)getActivity().findViewById(R.id.button_order);
        button_order.setOnClickListener(this);
        button_back = (Button)getActivity().findViewById(R.id.button_back);
        button_back.setOnClickListener(this);

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) getView().findViewById(R.id.listview1);
        listview.setAdapter(adapter);
        try {
            resultXML = new Menu_ItemFragment.CustomTask().execute("type=getItem&businessnumber="+businessnumber+
                    "&topcategoryname="+topcategoryname+"&categoryname="+categoryname).get();

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
                itemname = t_element.getChildNodes().item(3).getTextContent();
                itemprice = t_element.getChildNodes().item(5).getTextContent();
                adapter.addItem(itemname, itemprice);
            }

            //ListView 클릭 이벤트
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), Menu_Popup.class);

                    intent.putExtra("name", t_nodes.item(position).getChildNodes().item(3).getTextContent());
                    intent.putExtra("size1", t_nodes.item(position).getChildNodes().item(4).getTextContent());
                    intent.putExtra("price1", t_nodes.item(position).getChildNodes().item(5).getTextContent());
                    intent.putExtra("size2", t_nodes.item(position).getChildNodes().item(6).getTextContent());
                    intent.putExtra("price2", t_nodes.item(position).getChildNodes().item(7).getTextContent());
                    intent.putExtra("size3", t_nodes.item(position).getChildNodes().item(8).getTextContent());
                    intent.putExtra("price3", t_nodes.item(position).getChildNodes().item(9).getTextContent());
                    intent.putExtra("size4", t_nodes.item(position).getChildNodes().item(10).getTextContent());
                    intent.putExtra("price4", t_nodes.item(position).getChildNodes().item(11).getTextContent());
                    intent.putExtra("hot_iced", t_nodes.item(position).getChildNodes().item(12).getTextContent());

                    startActivityForResult(intent, 1);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == 2) {
                Order_ListViewItem item = new Order_ListViewItem();

                String name = data.getStringExtra("order_name");
                String size = data.getStringExtra("order_size");
                String number = data.getStringExtra("order_number");
                String price = data.getStringExtra("order_price");

                Integer allprice = 0;
                if(orderlist.size()==0){
                    item.setName(name);
                    item.setSize(size);
                    item.setNumber(number);
                    item.setPrice(price);

                    orderlist.add(item);
                }else{
                    for(int i=0; i<orderlist.size(); i++){
                        //이름과 크기가 같은 주문이 있으면 갯수와 가격만 더해준다.
                        if(orderlist.get(i).getName().equals(name)
                                && orderlist.get(i).getSize().equals(size)){
                            orderlist.get(i).setNumber(String.valueOf(Integer.parseInt(orderlist.get(i).getNumber())+Integer.parseInt(number)));
                            orderlist.get(i).setPrice(String.valueOf(Integer.parseInt(orderlist.get(i).getPrice())+Integer.parseInt(price)));
                            break;
                        }else{
                            item.setName(name);
                            item.setSize(size);
                            item.setNumber(number);
                            item.setPrice(price);

                            orderlist.add(item);
                            break;
                        }
                    }
                }
                for(int i=0; i<orderlist.size(); i++){
                    allprice += Integer.parseInt(orderlist.get(i).getPrice());
                }

                //주문하기 버튼 Text변경
                if(allprice!=0){
                    button_order.setText(allprice.toString()+"원 주문하기");
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.button_back){
            //orderlist 초기화
            orderlist.clear();

            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }else if(v.getId()==R.id.button_order){
            if(button_order.getText().toString().equals("주문하기")){
                Toast.makeText(getActivity(), "메뉴를 선택해 주세요", Toast.LENGTH_SHORT).show();
            }else {
                Intent intent = new Intent(getActivity(), Order.class);

                //주문리스트 전달
                intent.putExtra("orderlist", orderlist);

                startActivity(intent);
            }
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
}
