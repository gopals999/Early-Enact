package sandeep.project.com.sandeepprojectfinal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;

public class SlidesScrollingActivity extends AppCompatActivity {


    ListView lvCategories;
    private Context mContext;
    private ArrayList<String> categories;
    private DBHelper mDBHelper;
    private String selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slides_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = this;

        Bundle b = getIntent().getExtras();
        selectedCategory = b.getString(Sand_Constants.PREF_NAME);
            mDBHelper = new DBHelper(mContext);
        lvCategories = (ListView) findViewById(R.id.lvCategories);
        getSupportActionBar().setTitle("Categories");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                // add new category by starting  new activity


                if(selectedCategory.equalsIgnoreCase(Sand_Constants.PREF_SLIDES)) {
                    Intent in = new Intent(mContext, SlideContentActivity.class);
                    in.putExtra("Category", "");
                    in.putExtra(Sand_Constants.PREF_NAME, Sand_Constants.PREF_SLIDES);
                    startActivity(in);
                }else {
                    Intent in = new Intent(mContext, SlideContentActivity.class);
                    in.putExtra("Category", "");
                    in.putExtra(Sand_Constants.PREF_NAME, Sand_Constants.PREF_QUOTES);
                    startActivity(in);
                }
            }
        });

        categories = new ArrayList<String>();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (selectedCategory.equalsIgnoreCase(Sand_Constants.PREF_SLIDES)) {
            categories = mDBHelper.getCategories(Sand_Constants.CATESLIDES);
        } else categories = mDBHelper.getCategories(Sand_Constants.CATEQUOTES);

        CateAdapter cateAdapter = new CateAdapter(categories, mContext);
        lvCategories.setAdapter(cateAdapter);
    }

    private class CateAdapter extends BaseAdapter {
        private final ArrayList<String> categories;
        private final Context mContext;
        private final LayoutInflater inflater;

        public CateAdapter(ArrayList<String> categories, Context mContext) {
            this.categories = categories;
            this.mContext = mContext;

            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return categories.size();
        }

        @Override
        public Object getItem(int position) {
            return categories.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.categories_textlayout, null);
            TextView tvCategoryName = (TextView) convertView.findViewById(R.id.tvCategoryName);
            tvCategoryName.setText(categories.get(position));
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectedCategory.equalsIgnoreCase(Sand_Constants.PREF_SLIDES)) {
                        Intent in = new Intent(mContext, SlideContentActivity.class);
                        in.putExtra("Category", categories.get(position));
                        in.putExtra(Sand_Constants.PREF_NAME, Sand_Constants.PREF_SLIDES);
                        startActivity(in);
                    }else {
                        Intent in = new Intent(mContext, SlideContentActivity.class);
                        in.putExtra("Category", categories.get(position));
                        in.putExtra(Sand_Constants.PREF_NAME, Sand_Constants.PREF_QUOTES);
                        startActivity(in);
                    }
                }
            });
            return convertView;
        }
    }
}
