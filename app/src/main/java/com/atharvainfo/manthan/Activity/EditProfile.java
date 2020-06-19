package com.atharvainfo.manthan.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.PermissionRequest;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atharvainfo.manthan.Class.MyConfig;
import com.atharvainfo.manthan.Model.UserData;
import com.atharvainfo.manthan.R;
import com.atharvainfo.manthan.utils.HttpRequest;
import com.atharvainfo.manthan.utils.RequestHandler;
import com.atharvainfo.manthan.utils.Tools;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

public class EditProfile extends AppCompatActivity {

    FloatingActionButton btn_edit_profile ;
    TextView txt_stud_id;
    ImageView imageview, imageView1;
    EditText edit_first_name,edit_last_name,edit_email,et_gender,et_department,et_class, et_village, et_school;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    String student_id,first_name,last_name,gender,department,student_class,email,role,avator,dept_name,class_name,stid,student_village,studentimage;

    private Uri mImageCaptureUri;
    private AlertDialog dialog;

    private final int PICK_FROM_CAMERA = 1;
    private final int PICK_FROM_FILE = 2;

    String Image;
    Uri imageUri;
    private final static int ALL_PERMISSIONS_RESULT = 107;

    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();

    Bitmap myBitmap;
    private Bitmap bitmap;
    Uri picUri;
    private InputStream inputStreamImg;
    private String imgPath = null;
    private File destination = null;
    private final int jsoncode = 1;
    private static ProgressDialog mProgressDialog;

    public static final String UPLOAD_URL = "http://manthanpublication.com/manthanapp/uploadphoto.php";
    public static final String UPLOAD_KEY = "image";
    public static final String TAG = "MY MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        btn_edit_profile = (FloatingActionButton) findViewById(R.id.btn_edit_profile);

        txt_stud_id= (TextView) findViewById(R.id.txt_stud_id);
        imageview = (ImageView) findViewById(R.id.imageview);
        imageView1 = (ImageView) findViewById(R.id.imageview1);
        edit_first_name = (EditText) findViewById(R.id.edit_first_name);
        edit_last_name = (EditText) findViewById(R.id.edit_last_name);
        edit_email = (EditText) findViewById(R.id.edit_email);
        et_gender = (EditText) findViewById(R.id.et_gender);
        et_village = (EditText) findViewById(R.id.et_village);
        et_class = (EditText) findViewById(R.id.et_class);
        et_school = (EditText)findViewById(R.id.et_schoolname);

        //RoundedBitmapDrawable mDrawable = RoundedBitmapDrawableFactory.create(getResources(), String.valueOf(R.drawable.photo_male_7));
        //mDrawable.setCircular(true);
        //imageview.setImageDrawable(mDrawable);


        fetchJSON();

        (findViewById(R.id.bt_create_account)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               UpdateProfileData();
               //Update_Data();
            }
        });

        captureImageInitialization();
        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
        sharedPreferences.edit();
        student_id= sharedPreferences.getString("idtag",null);
        first_name= sharedPreferences.getString("firstname",null);
        last_name= sharedPreferences.getString("lastname",null);
        email= sharedPreferences.getString("emailtag",null);
        stid= sharedPreferences.getString("user_name",null);
        gender= sharedPreferences.getString("gender",null);
        student_class = sharedPreferences.getString("studentclass",null);
        class_name = sharedPreferences.getString("studentschool",null);
        student_village = sharedPreferences.getString("studentvillage",null);

        txt_stud_id.setText(stid);
        edit_first_name.setText(first_name);
        edit_last_name.setText(last_name);
        edit_email.setText(email);
        et_class.setText(student_class);
        et_gender.setText(gender);
        et_school.setText(class_name);
        et_village.setText(student_village);

        initToolbar();

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile : " + stid);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorPrimary);
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }


    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    private void captureImageInitialization() {
        /**
         * a selector dialog to display two image source options, from camera
         * ‘Take from camera’ and from existing files ‘Select from gallery’
         */
        /*final String[] items = new String[] { "Take from camera",
                "Select from gallery" };*/
        final String[] items = new String[] {"Take Photo", "Choose From Gallery","Cancel"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select Option");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) { // pick from

                if (item == 0) {
                    dialog.dismiss();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, PICK_FROM_CAMERA);
                } else if (item == 1) {
                    dialog.dismiss();
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, PICK_FROM_FILE);
                } else if (item == 2) {
                    dialog.dismiss();
                }


            }
        });

        dialog = builder.create();
    }


    public class CropOptionAdapter extends ArrayAdapter<CropOption> {
        private ArrayList<CropOption> mOptions;
        private LayoutInflater mInflater;

        public CropOptionAdapter(Context context, ArrayList<CropOption> options) {
            super(context, R.layout.crop_selector, options);

            mOptions = options;

            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup group) {
            if (convertView == null)
                convertView = mInflater.inflate(R.layout.crop_selector, null);

            CropOption item = mOptions.get(position);

            if (item != null) {
                ((ImageView) convertView.findViewById(R.id.iv_icon))
                        .setImageDrawable(item.icon);
                ((TextView) convertView.findViewById(R.id.tv_name))
                        .setText(item.title);

                return convertView;
            }

            return null;
        }
    }

    public class CropOption {
        public CharSequence title;
        public Drawable icon;
        public Intent appIntent;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        inputStreamImg = null;

        if (requestCode == PICK_FROM_CAMERA) {
            try {
                Uri selectedImage = data.getData();
                imageUri = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                Log.e("Activity", "Pick from Camera::>>> ");

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                destination = new File(Environment.getExternalStorageDirectory() + "/" +
                        getString(R.string.app_name), "IMG_" + timeStamp + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imgPath = destination.getAbsolutePath();
                RoundedBitmapDrawable mDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                mDrawable.setCircular(true);
                imageview.setImageDrawable(mDrawable);

                imageView1.setImageBitmap(bitmap);

                uploadImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_FROM_FILE) {
            imageUri = data.getData();
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                Log.e("Activity", "Pick from Gallery::>>> ");

                imgPath = getRealPathFromURI(selectedImage);
                destination = new File(imgPath.toString());
                imageView1.setImageBitmap(bitmap);
                RoundedBitmapDrawable mDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                mDrawable.setCircular(true);
                imageview.setImageDrawable(mDrawable);
                uploadImage();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    /**
     * Get the URI of the selected image from .<br />
     * Will return the correct URI for camera and gallery image.
     *
     * @param data the returned data of the activity result
     */
    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }


        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("pic_uri", picUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        picUri = savedInstanceState.getParcelable("pic_uri");
    }

    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();

        for (Object perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(Object permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission((String) permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (Object perms : permissionsToRequest) {
                    if (hasPermission(perms)) {

                    } else {

                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                                //Log.d("API123", "permisionrejected " + permissionsRejected.size());

                                                requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }



    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String>{

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EditProfile.this, "Uploading Image", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();
                data.put(UPLOAD_KEY, uploadImage);
                data.put("userphone", stid);

                String result = rh.sendPostRequest(UPLOAD_URL,data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void UpdateProfileData(){

        sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
        sharedPreferences.edit();
        stid= sharedPreferences.getString("user_name",null);

       // final Bitmap selectedImage = ((BitmapDrawable) imageView1.getDrawable()).getBitmap();
       // final Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

       // final String studimage = conv_Image_String(selectedImage);
       // final String uploadImage = getStringImage(selectedImage);
       // final long imagename = Long.valueOf(student_id);


        Log.i("UserName", stid);

        showSimpleProgressDialog(this, "Getting User Data...","Please Wait ...",false);

        new AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] params) {
                String response="";
                HashMap<String, String> map=new HashMap<>();
                map.put("username", stid);
                map.put("firstname", edit_first_name.getText().toString());
                map.put("lastname", edit_last_name.getText().toString());
                map.put("gender", et_gender.getText().toString());
                map.put("classname", et_class.getText().toString());
                map.put("schoolname", et_school.getText().toString());
                map.put("village", et_village.getText().toString());

                try {
                    HttpRequest req = new HttpRequest(MyConfig.URL_Update_Photo_Profile);
                    response = req.prepare(HttpRequest.Method.POST).withData(map).sendAndReadString();
                } catch (Exception e) {
                    response=e.getMessage();
                }
                return response;
            }
            protected void onPostExecute(String result) {
                //do something with response
                Log.d("newwwss",result);
                try {
                    onProfileCompleted(result,jsoncode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();

    }

    public void onProfileCompleted(String response, int serviceCode) throws JSONException {
        Log.d("responsejson", response.toString());

        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString("Result").equals("success")) {
                removeSimpleProgressDialog();

                    //Log.e("fetch data", student_id + "=" + first_name + "=" + student_class);

                Toast.makeText(this, "Profile Updated Successfully..", Toast.LENGTH_SHORT).show();
            } else {
                removeSimpleProgressDialog();
                Toast.makeText(this, getErrorCode(response), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }
    private void fetchJSON(){

        sharedPreferences=getApplicationContext().getSharedPreferences("Mydata",MODE_PRIVATE);
        sharedPreferences.edit();
        stid= sharedPreferences.getString("user_name",null);

        Log.i("UserName", stid);

        showSimpleProgressDialog(this, "Getting User Data...","Please Wait ...",false);

        new AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] params) {
                String response="";
                HashMap<String, String> map=new HashMap<>();
                map.put("username", stid);


                try {
                    HttpRequest req = new HttpRequest(MyConfig.URL_PROFILE_DATA);
                    response = req.prepare(HttpRequest.Method.POST).withData(map).sendAndReadString();
                } catch (Exception e) {
                    response=e.getMessage();
                }
                return response;
            }
            protected void onPostExecute(String result) {
                //do something with response
                Log.d("newwwss",result);
                try {
                    onTaskCompleted(result,jsoncode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }


    public void onTaskCompleted(String response, int serviceCode) throws JSONException {
        Log.d("responsejson", response.toString());

        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString("Result").equals("success")) {
                removeSimpleProgressDialog();

                JSONArray json = jsonObject.getJSONArray("Data");
                for (int i = 0; i < json.length(); i++)
                {
                    JSONObject obj = json.getJSONObject(i);
                    UserData userData = new UserData(obj.getString("userphone"),
                            obj.getString("useremail"),
                            obj.getString("userpass"),
                            obj.getString("firstname"),
                            obj.getString("lastname"),
                            obj.getString("gender"),
                            obj.getString("classname"),
                            obj.getString("schoolname"),
                            obj.getString("village"),
                            obj.getString("studentid"),
                            obj.getString("student_image"));


                    txt_stud_id.setText(userData.getUserphone());
                    edit_first_name.setText(userData.getFirstname());
                    edit_last_name.setText(userData.getLastname());
                    edit_email.setText(userData.getUseremail());
                    et_gender.setText(userData.getGender());
                    et_class.setText(userData.getClassname());
                    et_school.setText(userData.getSchoolname());
                    et_village.setText(userData.getVillage());

                    sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString("idtag", student_id);
                    editor.putString("firstname", first_name);
                    editor.putString("lastname", last_name);
                    editor.putString("gender", gender);
                    editor.putString("studentclass", student_class);
                    editor.putString("emailtag", email);
                    editor.putString("studentschool", class_name);
                    editor.putString("studentvillage", student_village);
                    editor.commit();

                    if (userData.getStudent_image() != null && !userData.getStudent_image().isEmpty() && !userData.getStudent_image().equals("null")){

                        byte[] decodedString = Base64.decode(userData.getStudent_image(), Base64.DEFAULT);
                        Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        //Bitmap bm = StringToBitMap(imgBitMap);

                        //image = BitmapFactory.decodeStream(studentimage);
                        RoundedBitmapDrawable mDrawable = RoundedBitmapDrawableFactory.create(getResources(), imgBitMap);
                        mDrawable.setCircular(true);
                        imageview.setImageDrawable(mDrawable);

                        imageView1.setImageBitmap(imgBitMap); //Sets the Bitmap to ImageView
                    }

                    Log.e("fetch data", student_id + "=" + first_name + "=" + student_class);

                }
            } else {
                removeSimpleProgressDialog();
                Toast.makeText(this, getErrorCode(response), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public String getErrorCode(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            removeSimpleProgressDialog();
            return jsonObject.getString("message");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "No data";
    }

    public static void removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();

        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showSimpleProgressDialog(Context context, String title,
                                                String msg, boolean isCancelable) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(context, title, msg);
                mProgressDialog.setCancelable(isCancelable);
            }

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String conv_Image_String(Bitmap imgOrigin)
    {
        if (imgOrigin != null)
        {
            //The image is first converted to byte[] (or Blob), before being encoded to String.
            ByteArrayOutputStream mBOS = new ByteArrayOutputStream();
            imgOrigin.compress(Bitmap.CompressFormat.PNG,100, mBOS);
            byte[] mBytImg = mBOS.toByteArray();

            String mStrImg = Base64.encodeToString(mBytImg, Base64.DEFAULT);
            return mStrImg;
        }
        else
        {
            return null;
        }
    }//*/

    class DataPart {
        private String fileName;
        private byte[] content;
        private String type;

        public DataPart() {
        }

        DataPart(String name, byte[] data) {
            fileName = name;
            content = data;
        }

        String getFileName() {
            return fileName;
        }

        byte[] getContent() {
            return content;
        }

        String getType() {
            return type;
        }

    }
}
