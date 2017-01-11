package com.example.ahmed.ui.Welcome;

import android.os.Bundle;

import com.example.ahmed.therapiodatafordepression.R;
import com.honu.aloha.BaseWelcomeActivity;
import com.honu.aloha.PageDescriptor;

/**
 * Created by ahmed on 24/06/16.
 */
public class WelcomeActivity extends BaseWelcomeActivity {

    private static final String LOG_TAG = BaseWelcomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void createPages() {
        addPage(new PageDescriptor(R.string.welcome_header_0, R.string.welcome_content_0, R.drawable.logotherapiowhite, R.color.blue_grey_900));
        addPage(new PageDescriptor(R.string.welcome_header_1, R.string.welcome_content_1, R.drawable.logotherapiowhite, R.color.blue_grey_700));
        addPage(new PageDescriptor(R.string.welcome_header_2, R.string.welcome_content_2, R.drawable.logotherapiowhite, R.color.blue_grey_600));
        addPage(new PageDescriptor(R.string.welcome_header_3, R.string.welcome_content_3, R.drawable.logotherapiowhite, R.color.cyan_800));

    }

}
