package malkia.malkiaunesco.example.com.malkia.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import malkia.malkiaunesco.example.com.malkia.R;

public class PostProfileInfo extends AppCompatActivity {

    private ImageButton mSelectImage;
    private EditText mPostName;
    private EditText mPostAge;
    private EditText mPostPhone;

    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;

    private Button mSubmitBtn;

    private Uri mImageUri = null;

    private static final int GALLERY_REQUEST = 1;

    private StorageReference mStorage;
    private DatabaseReference mDatabase;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_profile_info);

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("User Profile");

        mSelectImage = (ImageButton) findViewById(R.id.imageSelect);
        mPostName = (EditText) findViewById(R.id.inputName);
        mPostAge = (EditText) findViewById(R.id.inputAge);
        mPostPhone = (EditText) findViewById(R.id.inputPhone);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        mSubmitBtn = (Button) findViewById(R.id.submitBtn);
        mProgress = new ProgressDialog(this);
        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });


        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startPosting();


            }
        });
    }

    private void startPosting() {

        mProgress.setMessage("Posting, please wait...");
        mProgress.show();
        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        radioSexButton = (RadioButton) findViewById(selectedId);

        final String name_val = mPostName.getText().toString().trim();
        final String phone_val = mPostPhone.getText().toString().trim();
        final String age_val = mPostAge.getText().toString().trim();
        final String userGender = radioSexButton.getText().toString().trim();


        if (!TextUtils.isEmpty(name_val) && !TextUtils.isEmpty(phone_val) && !TextUtils.isEmpty(age_val) && mImageUri != null) {


            StorageReference filepath = mStorage.child("Users Pic").child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    DatabaseReference newPost = mDatabase.push();

                    newPost.child("Name").setValue(name_val);
                    newPost.child("Phone Number").setValue(phone_val);
                    newPost.child("Age").setValue(age_val);
                   newPost.child("Gender").setValue(userGender);
                    newPost.child("image").setValue(downloadUrl.toString());

                    mProgress.dismiss();
                    // startActivity(new Intent(LnfActivity.this, LostAndFoundActivity.class));

                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            mImageUri = data.getData();
            mSelectImage.setImageURI(mImageUri);
        }
    }

    public void RadioButtonClicked(View view) {
        String userGender = "";

        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.radioFemale:
                if (checked)
                    userGender = "female";
                break;
            case R.id.radioMale:
                if (checked)
                    userGender = "male";
                break;
        }

    }
}
