package sandeep.project.com.sandeepprojectfinal;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class SlideContentActivity extends AppCompatActivity {

    private Button btnAddImages;

    private static final int RESULT_LOAD_IMG = 10;
    Context mContext;
    private DBHelper dbHelper;
    private ListView lvContent;
    private String category;
    private ContentAdapter contentAdapter;
    private ArrayList<String> content;
    private Button btnInsertCate;
    private EditText etCategoryName;
    private String selectedCategory;
    private Button btnAddQuote;
    private EditText etQuote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_content);

        mContext=this;
        dbHelper=new DBHelper(mContext);
        btnAddImages=(Button)findViewById(R.id.btnAddImages);
        btnAddQuote=(Button)findViewById(R.id.btnAddQuote);



        btnInsertCate=(Button)findViewById(R.id.btnInsertCate);
        etCategoryName=(EditText)findViewById(R.id.etCategoryName);
        etQuote=(EditText)findViewById(R.id.etQuote);
        lvContent=(ListView)findViewById(R.id.lvContent);

        btnAddImages.setEnabled(false);
        lvContent= (ListView) findViewById(R.id.lvContent);

        Sand_Constants.verifyStoragePermissions(this);

        Bundle b=getIntent().getExtras();
        category=b.getString("Category");
        selectedCategory=b.getString(Sand_Constants.PREF_NAME);

        content=new ArrayList<String>();

        if(selectedCategory.equalsIgnoreCase(Sand_Constants.PREF_SLIDES)){
            btnAddQuote.setVisibility(View.GONE);
            etQuote.setVisibility(View.GONE);
        }
        else {
            btnAddImages.setVisibility(View.GONE);
        }



        btnInsertCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etCategoryName.getText().toString().isEmpty()){

                    boolean queryExe;
                    if(selectedCategory.equalsIgnoreCase(Sand_Constants.PREF_SLIDES))
                        queryExe = dbHelper.insertCategory(etCategoryName.getText().toString(), Sand_Constants.CATESLIDES);
                    else queryExe = dbHelper.insertCategory(etCategoryName.getText().toString(), Sand_Constants.CATEQUOTES);
                    if(queryExe){
                        Toast.makeText(mContext,"Category Inserted",Toast.LENGTH_LONG).show();
                        btnAddImages.setEnabled(true);
                        btnInsertCate.setEnabled(false);
                        btnAddQuote.setEnabled(false);
                    }else Toast.makeText(mContext,"Category not inserted",Toast.LENGTH_LONG).show();
                }else Toast.makeText(mContext,"Please enter category name",Toast.LENGTH_LONG).show();
            }
        });

        btnAddImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImagefromGallery();
            }
        });

        btnAddQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if( dbHelper.insertSlides("ig",etQuote.getText().toString(),etCategoryName.getText().toString())){
                   Toast.makeText(mContext,"Quote Added",Toast.LENGTH_LONG).show();
               }else Toast.makeText(mContext,"Quote not added",Toast.LENGTH_LONG).show();
            }
        });

        // load images if cate exists

        if(!category.isEmpty()){
            // get data from db

            btnInsertCate.setEnabled(false);
            btnAddImages.setEnabled(true);
            btnAddQuote.setEnabled(true);
            etCategoryName.setText(category);
            etCategoryName.setEnabled(false);
            content=dbHelper.getSlideImages(category);
          //  Toast.makeText(this, ""+content.size(),
            //        Toast.LENGTH_LONG).show();

            if(content!=null){
                contentAdapter = new ContentAdapter(content,mContext);
                lvContent.setAdapter(contentAdapter);
            }
        }
    }

    public void loadImagefromGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);



                dbHelper.insertSlides("ig",imgDecodableString,etCategoryName.getText().toString());
                cursor.close();


                content.add(imgDecodableString);
                contentAdapter = new ContentAdapter(content,mContext);
                lvContent.setAdapter(contentAdapter);


               // Toast.makeText(this, imgDecodableString,
                 //       Toast.LENGTH_LONG).show();


            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

    private class ContentAdapter extends BaseAdapter {

        private final ArrayList<String> content;
        private final Context mContext;
        private final LayoutInflater inflater;


        public ContentAdapter(ArrayList<String> content, Context mContext){
            this.content=content;
            this.mContext=mContext;

            inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return content.size();
        }

        @Override
        public Object getItem(int position) {
            return content.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView=inflater.inflate(R.layout.categories_textlayout,null);
            TextView tvCategoryName=(TextView)convertView.findViewById(R.id.tvCategoryName);

            ImageView imgPreview=(ImageView)convertView.findViewById(R.id.imgPreview);
            tvCategoryName.setVisibility(View.INVISIBLE);
            imgPreview.setVisibility(View.VISIBLE);
          //  Toast.makeText(mContext,"Category Inserted"+position,Toast.LENGTH_LONG).show();
            File imgFile=new File(content.get(position));
            if(imgFile.exists()){
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),options);
                imgPreview.setImageBitmap(myBitmap);
               // Toast.makeText(mContext,"Category Inserted"+imgFile.getAbsolutePath(),Toast.LENGTH_LONG).show();
            }else Log.e("test","file not found");
            return convertView;
        }
    }
}
