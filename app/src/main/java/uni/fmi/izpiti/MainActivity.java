package uni.fmi.izpiti;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText et_name;
    EditText et_age;
    Button btn_add;
    Button btn_viewAll;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch sw_activeCustomer;
    ListView lv_customerList;

    ArrayAdapter customerArrayAdapter;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_name = findViewById(R.id.et_name);
        et_age = findViewById(R.id.et_age);
        btn_add = findViewById(R.id.btn_add);
        btn_viewAll = findViewById(R.id.btn_viewAll);
        sw_activeCustomer = findViewById(R.id.sw_active);
        lv_customerList = findViewById(R.id.lv_customerList);

        dataBaseHelper = new DataBaseHelper(MainActivity.this);

        ShowCustomerOnListView(dataBaseHelper);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CustomerModel customerModel;

                try {
                    customerModel = new CustomerModel(-1,et_name.getText().toString(), Integer.parseInt(et_age.getText().toString()), sw_activeCustomer.isChecked());
                    Toast.makeText(MainActivity.this,customerModel.toString(), Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(MainActivity.this,"Error creating customer", Toast.LENGTH_SHORT).show();
                    customerModel = new CustomerModel(-1,"error", 0, false);
                }

                DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);

                boolean success = dataBaseHelper.addOne(customerModel);
                Toast.makeText(MainActivity.this,"Success " + success, Toast.LENGTH_SHORT).show();

                clearEditText();

                ShowCustomerOnListView(dataBaseHelper);

            }

        });

        btn_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dataBaseHelper = new DataBaseHelper(MainActivity.this);

                ShowCustomerOnListView(dataBaseHelper);

                //Toast.makeText(MainActivity.this,everyone.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        lv_customerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                CustomerModel clickedCustomer = (CustomerModel) adapterView.getItemAtPosition(i);
                dataBaseHelper.updateOne(clickedCustomer);
                ShowCustomerOnListView(dataBaseHelper);

                setNewDataValues(clickedCustomer);

                Toast.makeText(MainActivity.this, "Deleted " + clickedCustomer.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setNewDataValues(CustomerModel clickedCustomer) {
        et_name.setText(clickedCustomer.getName());
        et_age.setText(Integer.parseInt(String.valueOf(clickedCustomer.getAge())));
    }


    private void clearEditText() {
        et_name.setText("");
        et_age.setText("");
    }


    private void ShowCustomerOnListView(DataBaseHelper dataBaseHelper2) {
        customerArrayAdapter = new ArrayAdapter<CustomerModel>(MainActivity.this, android.R.layout.simple_list_item_1, dataBaseHelper2.getEveryone());
        lv_customerList.setAdapter(customerArrayAdapter);
    }
}