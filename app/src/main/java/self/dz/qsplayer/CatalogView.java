package self.dz.qsplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CatalogView extends RelativeLayout {
@BindView(R.id.list)
ListView listView;

    int layoutId = R.layout.catalog_view;
    Context context;

    public CatalogView(@NonNull Context context) {
        this(context, null);
    }

    public CatalogView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CatalogView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LayoutInflater.from(context).inflate(layoutId, this);
        ButterKnife.bind(this);
        showList();
    }

    private void showList() {
         String[] strs = new String[] {
                "first", "second", "third", "fourth", "fifth"
        };
        listView.setAdapter(new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, strs));
    }

}
