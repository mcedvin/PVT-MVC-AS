package com.example.edvin.app.challenges;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.edvin.app.R;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

public class NoPlasticChallengeActivity extends AppCompatActivity {

    ImageButton backButton;
    private Bitmap image;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_plastic_challenge);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(l -> startActivity(new Intent(this, OceanActivity.class)));
        image = image = BitmapFactory.decodeResource(getResources(), R.drawable.bakgrund);

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareButton shareButton = (ShareButton) findViewById(R.id.fb_share_button);
        shareButton.setShareContent(content);
    }

    public void share(View view) {


        shareDialog = new ShareDialog(this);


        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("https://ibb.co/P9RbS11"))
                    .build();
            shareDialog.show(linkContent);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }



}


