package edu.eci.cosw.myapplication;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import static android.app.Activity.RESULT_OK;


public class MainActivity extends AppCompatActivity {
    ImageView image;
    EditText message;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_GALLERY = 2;

    final CharSequence[] options = {"Camera", "Gallery"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = (ImageView)findViewById(R.id.image);
        message=(EditText)findViewById(R.id.enterMessage);
    }



    @NonNull
    public static Dialog createSingleChoiceAlertDialog(
            @NonNull Context context, @Nullable String title, @NonNull CharSequence[] items, @NonNull DialogInterface.OnClickListener optionSelectedListener, @Nullable DialogInterface.OnClickListener cancelListener )
    {
        AlertDialog.Builder builder = new AlertDialog.Builder( context);
        builder.setItems( items, optionSelectedListener );
        if ( cancelListener != null )
        {
            builder.setNegativeButton( "Cancel", cancelListener );
        }
        builder.setTitle( title );
        return builder.create();
    }

    public void addPhoto(View view){
        DialogInterface.OnClickListener optionSelectedListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals("Camera")) {
                    createIntentCamera();
                } else if (options[which].equals("Gallery")) {

                }
            }
        };
        DialogInterface.OnClickListener cancelListener =new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
        Dialog builder=createSingleChoiceAlertDialog(this,"Choose option",options,optionSelectedListener,cancelListener);
        builder.create();
        builder.show();
    }

    public void save(View view) {
        if(validation()){

        }
    }

    private void createIntentCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

    }
    private void createIntentGallery(){
        Intent takePictureIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(takePictureIntent,REQUEST_IMAGE_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            image.setImageBitmap(imageBitmap);
        }

        switch(requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if(resultCode == RESULT_OK){
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    image.setImageBitmap(imageBitmap);
                    /**
                    final float densityMultiplier = this.getResources().getDisplayMetrics().density;
                    int h= (int) (100*densityMultiplier);
                    int w= (int) (h * bitmap.getWidth()/((double) bitmap.getHeight()));
                    bitmap=Bitmap.createScaledBitmap(bitmap, w, h, true);
                    imageSelected=bitmap;**/
                    break;
                }

                break;
            case REQUEST_IMAGE_GALLERY:
                if(resultCode == RESULT_OK){
                    try{
                        Uri imgUri = data.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), imgUri);
                        image.setImageBitmap(bitmap);
                        /**
                        final float densityMultiplier = this.getResources().getDisplayMetrics().density;
                        int h= (int) (100*densityMultiplier);
                        int w= (int) (h * bitmap.getWidth()/((double) bitmap.getHeight()));
                        bitmap=Bitmap.createScaledBitmap(bitmap, w, h, true);

                        imageSelected=bitmap;**/
                        break;
                    }catch(Exception e){}

                }
                break;
        }
    }
    private boolean validation(){
        boolean validation=true;
        if(message.getText().equals(null) && image.getDrawable().equals(null)){
            message.setError("Please enter either a message or select an image.");
            validation=false;
        }if(message.getText().toString().length()<50){
            message.setError("The text field should have a length longer than 50 characters");
            validation=false;
        }
        return validation;
    }




}

