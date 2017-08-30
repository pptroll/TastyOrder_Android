package tastyorder.tastyorder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by pptroll on 2017-08-18.
 */
public class Order extends AppCompatActivity implements View.OnClickListener {
    ListView listview ;
    Order_ListViewAdapter adapter;

    Integer allprice = 0;

    Button button_back, button_payment;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        try{
            //Data 받기
            Intent intent = getIntent();
            ArrayList<Order_ListViewItem> orderlist = (ArrayList<Order_ListViewItem>) intent.getSerializableExtra("orderlist");

            button_back = (Button) findViewById(R.id.button_back);
            button_back.setOnClickListener(this);

            button_payment = (Button) findViewById(R.id.button_payment);
            button_payment.setOnClickListener(this);

            for(int i=0; i<orderlist.size(); i++){
                allprice += Integer.parseInt(orderlist.get(i).getPrice());
            }

            button_payment.setText(allprice.toString()+"원 결재하기");

            // Adapter 생성
            adapter = new Order_ListViewAdapter() ;

            // 리스트뷰 참조 및 Adapter달기
            listview = (ListView) findViewById(R.id.listview1);
            listview.setAdapter(adapter);

            for (int i=0; i < orderlist.size(); i++)
            {
                adapter.addItem(orderlist.get(i).getName(), orderlist.get(i).getSize(),
                        orderlist.get(i).getNumber(), orderlist.get(i).getPrice()) ;
            }
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.button_back){
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}
