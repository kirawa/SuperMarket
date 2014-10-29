package altarix.supermarket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PageFragment extends Fragment{


    TextView txtCount,txtCost,txtName;
    static  PageFragment pageFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item, null);
        txtName = (TextView) view.findViewById(R.id.txtName);
        txtCost = (TextView) view.findViewById(R.id.txtCost);
        txtCount = (TextView) view.findViewById(R.id.txtCount);
        txtCount.setText(String.valueOf(getArguments().getInt(DBHelper.COUNT)));
        txtName.setText(String.valueOf(getArguments().getString(DBHelper.NAME)));
        txtCost.setText(String.valueOf(getArguments().getString(DBHelper.COST)));
        return view;
    }

    public static Fragment newInstance(Bundle bundle) {
        pageFragment = new PageFragment();
        bundle.getString(DBHelper.NAME);
        bundle.getString(DBHelper.COST);
        bundle.getInt(DBHelper.COUNT);
        pageFragment.setArguments(bundle);
        return pageFragment;
    }
}
