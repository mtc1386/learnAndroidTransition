package com.cuizicheng.exercise.learntransition;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by cuizicheng on 2017/2/5.
 */

public class DetailActivity extends AppCompatActivity {
    ViewPager vp;
    MViewPagerAdapter mPagerAdapter;
    LinearLayout contentContainer;
    TextView title;
    ImageView contentImg;
    ImageView imgConver;
    TextView contentP;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initViews();
        setEnterTransitionAnimation();
    }

    private void initViews() {
        vp = (ViewPager) findViewById(R.id.viewpager);
        int[] imgses = getIntent().getIntArrayExtra("imgs");
        mPagerAdapter = new MViewPagerAdapter();
        mPagerAdapter.imgResList = imgses;
        vp.setAdapter(mPagerAdapter);

        imgConver = (ImageView) findViewById(R.id.img_cover);
        imgConver.setImageResource(imgses[0]);

        String titleStr = getIntent().getStringExtra("title");
        contentContainer = (LinearLayout) findViewById(R.id.content_container);
        title = (TextView) findViewById(R.id.title);
        title.setText(titleStr);

        contentImg = (ImageView) findViewById(R.id.content_img);
        contentP = (TextView) findViewById(R.id.content_p);

        int contentImgRes = getIntent().getIntExtra("content_img", -1);
        contentImg.setImageResource(contentImgRes);
        String contentPStr = getIntent().getStringExtra("content_p");
        contentP.setText(contentPStr);

    }

    private void setData() {

    }

    private void setEnterTransitionAnimation() {
        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(400);
        changeBounds.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                Log.d("czc-demo", "onTransitionEnd");
                setBannerImg();
                getWindow().getSharedElementEnterTransition().removeListener(this);
            }

            @Override
            public void onTransitionCancel(Transition transition) {
                Log.d("czc-demo", "onTransitionCancel");
                setBannerImg();
                getWindow().getSharedElementEnterTransition().removeListener(this);
            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
        getWindow().setSharedElementEnterTransition(changeBounds);
    }

    private void setBannerImg() {
        imgConver.post(new Runnable() {
            @Override
            public void run() {
                Log.d("czc-demo", "set Banner");
                vp.setVisibility(View.VISIBLE);
                imgConver.setVisibility(View.GONE);
            }
        });
    }


    class MViewPagerAdapter extends PagerAdapter {
        int[] imgResList;

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int imgRes = imgResList[position];
            ImageView view = (ImageView) LayoutInflater.from(container.getContext()).inflate(R.layout.vp_img, container, false);
            view.setImageResource(imgRes);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            if (imgResList != null)
                return imgResList.length;
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

}
