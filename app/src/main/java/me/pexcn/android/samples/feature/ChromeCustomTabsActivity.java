package me.pexcn.android.samples.feature;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.widget.Button;

import me.pexcn.android.samples.R;
import me.pexcn.android.samples.base.BaseActivity;

/**
 * Created by pexcn on 2016-10-04.
 */
public class ChromeCustomTabsActivity extends BaseActivity {
    @SuppressWarnings("FieldCanBeLocal")
    private Button mButton;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feature_chrome_custom_tabs;
    }

    @Override
    protected boolean isSubActivity() {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void init() {
        super.init();
        mButton = (Button) findViewById(R.id.open);
        mButton.setOnClickListener(v -> {
            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                    .setToolbarColor(getResources().getColor(R.color.colorPrimary))
                    .setShowTitle(true)
                    .addMenuItem("测试菜单", PendingIntent.getActivity(this, 1, new Intent(), 0))
                    .setActionButton(BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_menu_search), "测试", PendingIntent.getActivity(this, 1, new Intent(), 0))
                    .addDefaultShareMenuItem()
                    .build();
            customTabsIntent.intent.setPackage("com.android.chrome");
            customTabsIntent.launchUrl(this, Uri.parse("https://source.android.com"));
        });
    }
}
