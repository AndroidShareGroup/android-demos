package me.pexcn.android.samples.feature;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import me.pexcn.android.samples.R;
import me.pexcn.android.samples.base.BaseActivity;
import me.pexcn.android.utils.common.AppUtils;
import me.pexcn.android.utils.common.LogUtils;
import me.pexcn.android.utils.common.MD5Utils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by pexcn on 2017-04-01.
 */
public class DiskLruCacheActivity extends BaseActivity {
    private static final String JSON_URL = "http://news-at.zhihu.com/api/4/news/latest";
    private static final String IMAGE_URL = "http://tpwd.texas.gov/state-parks/big-bend-ranch/gallery/BBRSP_4158.jpg";

    private DiskLruCache mDiskLruCache;
    private OkHttpClient mOkHttpClient;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feature_disk_lru_cache;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        super.init(savedInstanceState);

        mOkHttpClient = new OkHttpClient();

        File path = new File(getCacheDir().getAbsoluteFile() + "/lru_cache");
        int version = AppUtils.getVersionCode();
        long maxSize = 1024 * 1024 * 16;
        if (!path.exists()) {
            if (!path.mkdirs()) {
                return;
            }
        }

        try {
            mDiskLruCache = DiskLruCache.open(path, version, 1, maxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Button write = (Button) findViewById(R.id.write);
        Button read = (Button) findViewById(R.id.read);
        Button remove = (Button) findViewById(R.id.remove);
        final TextView size = (TextView) findViewById(R.id.size);

        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final DiskLruCache.Editor editor = mDiskLruCache.edit(MD5Utils.get(JSON_URL));
                    final OutputStream os = editor.newOutputStream(0);
                    mOkHttpClient.newCall(new Request.Builder().url(JSON_URL).build()).enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            byte[] bytes = response.body().bytes();
                            os.write(bytes);
                            os.flush();
                            os.close();
                            editor.commit();
                            LogUtils.d("Cache write successfully");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    size.setText(String.valueOf(mDiskLruCache.size()) + " Bytes");
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call call, IOException e) {
                            LogUtils.d("Cache write failure");
                            try {
                                os.close();
                                editor.abort();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    final DiskLruCache.Editor editor = mDiskLruCache.edit(MD5Utils.get(IMAGE_URL));
                    final OutputStream os = editor.newOutputStream(0);
                    mOkHttpClient.newCall(new Request.Builder().url(IMAGE_URL).build()).enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            byte[] bytes = response.body().bytes();
                            os.write(bytes);
                            os.flush();
                            os.close();
                            editor.commit();
                            LogUtils.d("Cache write successfully");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    size.setText(String.valueOf(mDiskLruCache.size()) + " Bytes");
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call call, IOException e) {
                            LogUtils.d("Cache write failure");
                            try {
                                os.close();
                                editor.abort();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DiskLruCache.Snapshot snapshot = mDiskLruCache.get(MD5Utils.get(JSON_URL));
                    if (snapshot != null) {
                        InputStream is = snapshot.getInputStream(0);
                        LogUtils.d("Cache object: " + is.toString());
                    } else {
                        LogUtils.d("Cache not available");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    DiskLruCache.Snapshot snapshot = mDiskLruCache.get(MD5Utils.get(IMAGE_URL));
                    if (snapshot != null) {
                        InputStream is = snapshot.getInputStream(0);
                        LogUtils.d("Cache object: " + is.toString());
                    } else {
                        LogUtils.d("Cache not available");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mDiskLruCache.remove(MD5Utils.get(JSON_URL));
                    mDiskLruCache.remove(MD5Utils.get(IMAGE_URL));
                    size.setText(String.valueOf(mDiskLruCache.size()) + " Bytes");
                    LogUtils.d("Cache removed");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        size.setText(String.valueOf(mDiskLruCache.size()) + " Bytes");
    }

    @Override
    protected boolean isSubActivity() {
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mDiskLruCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mDiskLruCache.isClosed()) {
            try {
                mDiskLruCache.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
