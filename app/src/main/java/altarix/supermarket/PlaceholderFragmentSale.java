package altarix.supermarket;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public  class PlaceholderFragmentSale extends Fragment implements View.OnClickListener {


    Spinner spinner;
    ArrayAdapter arrayAdapter;
    Button btnDone;
    EditText editTextName;
    EditText editTextCost;
    PriceListProvider dataSource;
    List<PriceList> priceLists;

    private  final String ARG_NAME = "name";
    private  final String ARG_COST = "cost";
    private  final String ARG_TYPE = "type";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    private static final String ARG_SECTION_NUMBER = "section_number";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.catigories, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
    }


    public static PlaceholderFragmentSale newInstance(int sectionNumber) {
        PlaceholderFragmentSale fragment = new PlaceholderFragmentSale();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle args) {
        super.onSaveInstanceState(args);
        editor = sharedPreferences.edit();
        editor.putString(ARG_NAME,editTextName.getText().toString());
        editor.putString(ARG_COST,editTextCost.getText().toString());
        editor.putInt(ARG_TYPE,spinner.getSelectedItemPosition());
        editor.commit();
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sale,null);
        spinner = (Spinner)view.findViewById(R.id.spinner);
        btnDone = (Button)view.findViewById(R.id.btnDone);
        editTextName = (EditText)view.findViewById(R.id.editTextName);
        editTextCost = (EditText)view.findViewById(R.id.editTextCost);
        spinner.setAdapter(arrayAdapter);
        btnDone.setOnClickListener(this);
        if (getRetainInstance()){
            editTextName.setText(sharedPreferences.getString(ARG_NAME,""));
            editTextCost.setText(sharedPreferences.getString(ARG_COST,""));
            spinner.setSelection(sharedPreferences.getInt(ARG_TYPE,0));
        }
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((Main) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDone:
                createItem(view.getContext());
        }
    }

    private boolean equalsElement(String name,String cost, int type,PriceListProvider dataSource){
        priceLists = new PriceSource().getPriceList(getActivity());
        for (PriceList priceList : priceLists){
            if (priceList.getName().equals(name)&&
                    priceList.getCost().equals(cost)&&
                    priceList.getType() == type){
                Uri uri = ContentUris.withAppendedId(PriceListProvider.PRICE_CONTENT_URI, priceList.getId());
                getActivity().getContentResolver().update(uri,dataSource.createContentValues(name,type,cost,priceList.getCount()+1),null,null);
                return false;
            }
        }
        return true;
    }


    private void createItem(Context context){
        String name = editTextName.getText().toString().trim();
        String cost = editTextCost.getText().toString().trim();
        int type = spinner.getSelectedItemPosition();
        if (name.length() != 0 && cost.length() != 0){
            dataSource = new PriceListProvider();
            dataSource.onCreate();
            if (equalsElement(name,cost,type,dataSource)){
                getActivity().getContentResolver().insert(PriceListProvider.PRICE_CONTENT_URI, dataSource.createContentValues(name,type,cost,1));
            }
            editTextName.setText("");
            editTextCost.setText("");
            Toast.makeText(context, "Сохраненно", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context,"Заполните поля",Toast.LENGTH_SHORT).show();
        }
    }
}