package sandeep.project.com.sandeepprojectfinal;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class CategorySelection extends AppCompatActivity {

    private Spinner spQuotes;
    private Spinner spSlides;
    private Context mContext;
    private DBHelper mDbHelper;
    private Button btnSubmit;
    private SharedPreferences sPref;
    private SharedPreferences.Editor mPrefEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection);

        mContext = this;

        spQuotes = (Spinner) findViewById(R.id.spQuotes);
        spSlides = (Spinner) findViewById(R.id.spSlides);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        mDbHelper = new DBHelper(mContext);

        ArrayAdapter<String> a;
        a = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item,
                mDbHelper.getCategories(Sand_Constants.CATESLIDES));
        spSlides.setAdapter(a);
        a = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item,
                mDbHelper.getCategories(Sand_Constants.CATEQUOTES));

        spQuotes.setAdapter(a);
        sPref = getSharedPreferences(Sand_Constants.PREF_NAME, Context.MODE_PRIVATE);

        mPrefEdit = sPref.edit();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mPrefEdit.putString(Sand_Constants.PREF_SLIDES, spSlides.getSelectedItem().toString());
                    mPrefEdit.commit();
                    mPrefEdit.putString(Sand_Constants.PREF_QUOTES, spQuotes.getSelectedItem().toString());
                    mPrefEdit.commit();
                }catch (Exception e){

                }

                Toast.makeText(mContext,"Changes applied",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
