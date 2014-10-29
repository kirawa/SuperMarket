package altarix.supermarket;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;

public class PlaceholderFragmentBuy extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {


    TextView textFood, textDrink, textHousehold,txtCurrentItem,txtAllItem;

    Animation left,rigth,rigth0,left0,left00,left_rigth,rigth_left,rigth00;
    RelativeLayout relativeFood, relativeDrink, relativeHousehould;

    int black,transparent,grey,color_999,state;

    public  final String PREFERENCES_TYPE = "0";
    public static final String ARG_POSITION = "args";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    PriceListProvider provider;

    ViewPager pager;
    MyFragmentPagerAdapter pagerAdapter;
    List<PriceList> priceLists;
    List<PriceList> sortPriceList;
    Button btnBuy;

    private static final String ARG_SECTION_NUMBER = "section_number";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        left = AnimationUtils.loadAnimation(getActivity(), R.anim.left);
        rigth = AnimationUtils.loadAnimation(getActivity(), R.anim.rigth);
        rigth0 = AnimationUtils.loadAnimation(getActivity(), R.anim.rigth_o);
        left00 = AnimationUtils.loadAnimation(getActivity(), R.anim.left00);
        rigth00 = AnimationUtils.loadAnimation(getActivity(), R.anim.rigth00);
        left0 = AnimationUtils.loadAnimation(getActivity(), R.anim.left_0);
        left_rigth = AnimationUtils.loadAnimation(getActivity(), R.anim.left_rigth);
        rigth_left = AnimationUtils.loadAnimation(getActivity(), R.anim.rigth_left);
        black = getResources().getColor(R.color.black);
        color_999 = getResources().getColor(R.color.color_999);
        transparent = getResources().getColor(android.R.color.transparent);
        grey = getResources().getColor(R.color.grey);
        state = getType();
        provider = new PriceListProvider();
        super.onCreate(savedInstanceState);
    }

    public static PlaceholderFragmentBuy newInstance(int sectionNumber) {
        PlaceholderFragmentBuy fragment = new PlaceholderFragmentBuy();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buy,null);
        textFood = (TextView)view.findViewById(R.id.textFood);
        textDrink = (TextView)view.findViewById(R.id.textDrink);
        textHousehold = (TextView)view.findViewById(R.id.textHousehold);
        txtCurrentItem = (TextView)view.findViewById(R.id.txtCurrentItem);
        txtAllItem = (TextView)view.findViewById(R.id.txtAllItem);
        relativeFood = (RelativeLayout)view.findViewById(R.id.relativeFood);
        relativeDrink = (RelativeLayout)view.findViewById(R.id.relativeDrink);
        relativeHousehould = (RelativeLayout)view.findViewById(R.id.relativeHousehold);
        btnBuy = (Button)view.findViewById(R.id.btnBuy);
        btnBuy.setOnClickListener(this);
        pager = (ViewPager)view.findViewById(R.id.pager);
        relativeFood.setOnClickListener(this);
        relativeDrink.setOnClickListener(this);
        relativeHousehould.setOnClickListener(this);
        pager.setOnPageChangeListener(this);
        startFragmentPagerAdapter();
        switch (state) {
            case 0:
                relativeFood.setBackgroundResource(R.drawable.select_category_product);
                textFood.setTextColor(black);
                textFood.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                break;
            case 1:
                relativeDrink.setBackgroundResource(R.drawable.select_category_product);
                textDrink.setTextColor(black);
                textDrink.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                break;
            case 2:
                relativeHousehould.setBackgroundResource(R.drawable.select_category_product);
                textHousehold.setTextColor(black);
                textHousehold.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                break;
        }
        if (getRetainInstance()){
            pager.setCurrentItem(sharedPreferences.getInt(ARG_POSITION,0));
        }
        return view;
    }


    private boolean getCount(int pos){
        return sortPriceList.get(pos).getCount() > 1;
    }

    private void removeCurrentItem(Context context) {
        int position = pager.getCurrentItem();
        PriceList priceList = sortPriceList.get(position);
        Uri uri = ContentUris.withAppendedId(PriceListProvider.PRICE_CONTENT_URI, priceList.getId());
        if (getCount(position)) {
            String name = priceList.getName();
            String cost = priceList.getCost();
            int type = priceList.getType();
            int count = priceList.getCount()-1;
            context.getContentResolver().update(uri,provider.createContentValues(name, type, cost, count),null,null);
            sortPriceList.get(position).setCount(count);
            pagerAdapter.notifyDataSetChanged();
        }else {
            if (sortPriceList.size()!=0){
                sortPriceList.remove(position);
                txtAllItem.setText(String.valueOf(sortPriceList.size()));
                pagerAdapter.notifyDataSetChanged();
                context.getContentResolver().delete(uri,null,null);
                if (sortPriceList.size()==0){
                    txtCurrentItem.setText("0");
                }
            }
        }
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((Main) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

    private void startFragmentPagerAdapter(){
        sortPriceList = getPriceLists();
        pagerAdapter = new MyFragmentPagerAdapter(getActivity().getSupportFragmentManager(),sortPriceList,pager);
        txtAllItem.setText(String.valueOf(getPriceLists().size()));
        if (getPriceLists().size()==0){
            txtCurrentItem.setText(String.valueOf(pager.getCurrentItem()));
        }else {
            txtCurrentItem.setText(String.valueOf(pager.getCurrentItem()+1));
        }
    }

    private List<PriceList> getPriceLists(){
        int state = getType();
        priceLists = new PriceSource().getPriceList(getActivity());
        Iterator<PriceList> iterator =  priceLists.iterator();
        while (iterator.hasNext()){
            PriceList priceList = iterator.next();
            if (priceList.getType()!=state){
                iterator.remove();
            }
        }
        return priceLists;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        editor = sharedPreferences.edit();
        editor.putInt(ARG_POSITION,pager.getCurrentItem());
        editor.commit();
        setRetainInstance(true);
    }

    private int getType(){
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getInt(PREFERENCES_TYPE,0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.relativeFood:
                if (getType()!=0){
                    selectProductType(0);
                }
                break;
            case R.id.relativeDrink:
                if (getType()!=1){
                    selectProductType(1);
                }
                break;
            case R.id.relativeHousehold:
                if (getType()!=2){
                    selectProductType(2);
                }
                break;
            case R.id.btnBuy:
                if (sortPriceList.size()!=0){
                    removeCurrentItem(view.getContext());
                }
                break;
        }
    }

    public void selectProductType(int type){
        int value = getType();
        editor = sharedPreferences.edit();
        editor.putInt(PREFERENCES_TYPE,type);
        editor.commit();
        startFragmentPagerAdapter();
        switch (type){
            case 0:
                switch (value){
                    case 1:
                        new MainAnimation(textDrink,left,relativeDrink,relativeFood,
                                relativeHousehould,left0,grey,transparent,
                                black,textFood).startAnimationShort();
                        break;
                    case 2:
                        new MainAnimation(textHousehold,textFood,left,
                                left_rigth,left00,relativeHousehould,
                                relativeDrink,relativeFood,grey,transparent,black).startAnimationLong();
                        break;
                }
                break;
            case 1:
                switch (value){
                    case 0:
                        new MainAnimation(textFood,rigth,relativeFood,relativeDrink,
                                relativeHousehould,rigth0,grey,transparent,
                                black,textDrink).startAnimationShort();
                        break;
                    case 2:
                        new MainAnimation(textHousehold,left,relativeHousehould,
                                relativeDrink,relativeFood,left0,grey,
                                transparent,black,textDrink).startAnimationShort();
                        break;
                }
                break;
            case 2:
                switch (value){
                    case 0:
                        new MainAnimation(textFood,textHousehold,rigth,rigth_left,rigth00,
                                relativeFood,relativeDrink,relativeHousehould,
                                grey,transparent,black).startAnimationLong();
                        break;
                    case 1:
                        new MainAnimation(textDrink,rigth,relativeDrink,relativeHousehould,
                                relativeFood,rigth0,grey,transparent,black,
                                textHousehold).startAnimationShort();
                        break;
                }
                break;
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        txtCurrentItem.setText(String.valueOf(position+1));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state!=0){
            btnBuy.setEnabled(false);
        }else {
            btnBuy.setEnabled(true);
        }
    }
}
