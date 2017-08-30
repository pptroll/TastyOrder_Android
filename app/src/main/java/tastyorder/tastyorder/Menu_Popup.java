package tastyorder.tastyorder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by pptroll on 2017-08-01.
 */

public class Menu_Popup extends AppCompatActivity {
    String name, size1, price1, size2, price2, size3, price3, size4, price4, hot_iced;
    String[] arraySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.popup_menu);

        //데이터 가져오기
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        size1 = intent.getStringExtra("size1");
        price1 = intent.getStringExtra("price1");
        size2 = intent.getStringExtra("size2");
        price2 = intent.getStringExtra("price2");
        size3 = intent.getStringExtra("size3");
        price3 = intent.getStringExtra("price3");
        size4 = intent.getStringExtra("size4");
        price4 = intent.getStringExtra("price4");
        hot_iced = intent.getStringExtra("hot_iced");

        //UI 객체생성
        final TextView text_name = (TextView)findViewById(R.id.textView_name);
        final TextView text_price = (TextView)findViewById(R.id.textView_price);
        final TextView text_number = (TextView)findViewById(R.id.textview_number);

        final Spinner spiner_size = (Spinner) findViewById(R.id.Spinner_Size);

        //스피너 어댑터 설정
        arraySpinner = new String[]{size1, size2, size3};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        spiner_size.setAdapter(adapter);


        text_name.setText(name);
        text_price.setText(price1);


        //스피너 이벤트 발생
        spiner_size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String size = spiner_size.getSelectedItem().toString();
                Integer priceall = 0;
                if(size==size1){
                    priceall = Integer.parseInt(text_number.getText().toString()) * Integer.parseInt(price1);
                }else if(size==size2){
                    priceall = Integer.parseInt(text_number.getText().toString()) * Integer.parseInt(price2);
                }else if(size==size3){
                    priceall = Integer.parseInt(text_number.getText().toString()) * Integer.parseInt(price3);
                }else if(size==size4){
                    priceall = Integer.parseInt(text_number.getText().toString()) * Integer.parseInt(price4);
                }
                text_price.setText(priceall.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    //- 버튼 클릭
    public void Button_number_down(View v){
        final TextView text_price = (TextView)findViewById(R.id.textView_price);
        final TextView text_number = (TextView)findViewById(R.id.textview_number);

        final Spinner spiner_size = (Spinner) findViewById(R.id.Spinner_Size);

        Integer number = Integer.parseInt(text_number.getText().toString());
        if(number != 1) {
            number = number -1;
            text_number.setText(number.toString());
        }

        String size = spiner_size.getSelectedItem().toString();
        Integer priceall = 0;
        if(size==size1){
            priceall = Integer.parseInt(text_number.getText().toString()) * Integer.parseInt(price1);
        }else if(size==size2){
            priceall = Integer.parseInt(text_number.getText().toString()) * Integer.parseInt(price2);
        }else if(size==size3){
            priceall = Integer.parseInt(text_number.getText().toString()) * Integer.parseInt(price3);
        }else if(size==size4){
            priceall = Integer.parseInt(text_number.getText().toString()) * Integer.parseInt(price4);
        }
        text_price.setText(priceall.toString());
    }

    //+ 버튼 클릭
    public void Button_number_up(View v){
        final TextView text_price = (TextView)findViewById(R.id.textView_price);
        final TextView text_number = (TextView)findViewById(R.id.textview_number);

        final Spinner spiner_size = (Spinner) findViewById(R.id.Spinner_Size);

        Integer number = Integer.parseInt(text_number.getText().toString());
        number = number +1;
        text_number.setText(number.toString());

        String size = spiner_size.getSelectedItem().toString();
        Integer priceall = 0;
        if(size==size1){
            priceall = Integer.parseInt(text_number.getText().toString()) * Integer.parseInt(price1);
        }else if(size==size2){
            priceall = Integer.parseInt(text_number.getText().toString()) * Integer.parseInt(price2);
        }else if(size==size3){
            priceall = Integer.parseInt(text_number.getText().toString()) * Integer.parseInt(price3);
        }else if(size==size4){
            priceall = Integer.parseInt(text_number.getText().toString()) * Integer.parseInt(price4);
        }
        text_price.setText(priceall.toString());
    }

    //담기 버튼 클릭
    public void mOnClose(View v){
        final TextView text_price = (TextView)findViewById(R.id.textView_price);
        final TextView text_number = (TextView)findViewById(R.id.textview_number);

        final Spinner spiner_size = (Spinner) findViewById(R.id.Spinner_Size);

        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("order_name", name);
        intent.putExtra("order_size", spiner_size.getSelectedItem().toString());
        intent.putExtra("order_number", text_number.getText());
        intent.putExtra("order_price", text_price.getText());

        setResult(2, intent);

        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}
