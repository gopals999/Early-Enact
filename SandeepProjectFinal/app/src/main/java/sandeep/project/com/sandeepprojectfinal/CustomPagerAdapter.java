package sandeep.project.com.sandeepprojectfinal;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Pupa on 4/30/2016.
 */
public class CustomPagerAdapter extends PagerAdapter {


    private final DBHelper mDBHelper;
    Context mContext;
    ArrayList<String> slides,quotes;
    private LayoutInflater inflater;
    private TextView tvQuote;

    public CustomPagerAdapter(Context mContext){
        this.mContext=mContext;

        SharedPreferences mPref=mContext.getSharedPreferences(Sand_Constants.PREF_NAME,Context.MODE_PRIVATE);
        String selected_slides=mPref.getString(Sand_Constants.PREF_SLIDES,"");
        String selected_quotes=mPref.getString(Sand_Constants.PREF_QUOTES,"");

        mDBHelper=new DBHelper(mContext);

        slides=mDBHelper.getSlideImages(selected_slides);
        quotes=mDBHelper.getSlideImages(selected_quotes);


    }

    @Override
    public int getCount() {
        return slides.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }
    
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imgDisplay;

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.slideshow_item, container, false);

        imgDisplay = (ImageView) viewLayout.findViewById(R.id.imgDisplay);
        tvQuote = (TextView) viewLayout.findViewById(R.id.tvQuote);
        
        
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(slides.get(position), options);
        imgDisplay.setImageBitmap(bitmap);


        if(quotes!=null && position<=quotes.size())
            tvQuote.setText(quotes.get(position));

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }


    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }
}
