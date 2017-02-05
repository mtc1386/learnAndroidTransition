package com.cuizicheng.exercise.learntransition;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView rv;
    ImageView imgBg;
    TextView titleView;
    TextView subTitleView;
    ViewGroup container;
    FlingHelper flingHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_animation);
        flingHelper = new FlingHelper();
        initViews();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int actionMasked = ev.getActionMasked();
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                flingHelper.onDown(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                flingHelper.onMove(ev);
                break;
            case MotionEvent.ACTION_UP:
                if (flingHelper.onUpOrCancel(ev))
                    return true;
                break;
            case MotionEvent.ACTION_CANCEL:
                if (flingHelper.onUpOrCancel(ev))
                    return true;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void onFlingUp() {
        if (myRecyclerAdapter != null) {
            if (myRecyclerAdapter.expand()) {
                bgLayoutGo();
            }
        }
    }

    private void onFlingDown() {
        if (myRecyclerAdapter != null) {
            if (myRecyclerAdapter.collapse()) {
                bgLayoutGo();
            }
        }
    }


    private void bgLayoutGo() {
        TransitionManager.beginDelayedTransition(container, new ChangeBounds());
        if (!myRecyclerAdapter.expanded) {
            RelativeLayout.LayoutParams lpImgBg = (RelativeLayout.LayoutParams) imgBg.getLayoutParams();
            lpImgBg.bottomMargin = 0;
            imgBg.setLayoutParams(lpImgBg);

            RelativeLayout.LayoutParams lpTitle = (RelativeLayout.LayoutParams) titleView.getLayoutParams();
            lpTitle.topMargin = (int) getResources().getDimension(R.dimen.title_original_margin);
            titleView.setLayoutParams(lpTitle);

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) subTitleView.getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            layoutParams.removeRule(RelativeLayout.ALIGN_LEFT);
            subTitleView.setLayoutParams(layoutParams);
        } else {
            RelativeLayout.LayoutParams lpImgBg = (RelativeLayout.LayoutParams) imgBg.getLayoutParams();
            lpImgBg.bottomMargin = (int) getResources().getDimension(R.dimen.img_bg_transY);
            imgBg.setLayoutParams(lpImgBg);

            RelativeLayout.LayoutParams lpTitle = (RelativeLayout.LayoutParams) titleView.getLayoutParams();
            lpTitle.topMargin = (int) getResources().getDimension(R.dimen.title_margin);
            titleView.setLayoutParams(lpTitle);

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) subTitleView.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.title);
            layoutParams.removeRule(RelativeLayout.CENTER_HORIZONTAL);
            subTitleView.setLayoutParams(layoutParams);
        }
    }

    private class FlingHelper {
        float start;
        float last;
        long lastTime;
        long startTime;
        final float toggleSpeed = 4.5f;
        final float toggleDistance = 600.f;

        void onDown(MotionEvent m) {
            start = m.getY();
            startTime = m.getDownTime();
            last = start;
        }

        void flingUp() {
            onFlingUp();
        }

        void flingDown() {
            onFlingDown();
        }

        void onMove(MotionEvent m) {
            last = m.getY();
            lastTime = m.getEventTime();
        }

        boolean onUpOrCancel(MotionEvent m) {
            if (shouldToggle()) {
                if (last < start) {
                    flingUp();
                } else if (last > start) {
                    flingDown();
                }
                return true;
            }

            return false;
        }

        boolean shouldToggle() {
            float dis = Math.abs(last - start);
            Log.d("czc-demo", "dis:" + dis);
            if (dis <= toggleDistance)
                return false;

            if (getSpeed() <= toggleSpeed)
                return false;

            return true;
        }

        float getSpeed() {
            float dis = Math.abs(last - start);
            long pastTime = lastTime - startTime;
            float speed = dis / pastTime;
            Log.d("czc-demo", "speed:" + speed);
            return speed;
        }
    }

    private List<PostData> buildData() {
        return new ArrayList<PostData>() {
            {
                add(new PostData(R.drawable.t01, "ABOUT US", "Thomson Reuters provides professionals with the intelligence, technology and human expertise they need to find trusted answers.We enable professionals in the financial and risk, legal, tax and accounting, and media markets to make the decisions that matter most, all powered by the world's most trusted news organization. Thomson Reuters shares are listed on the Toronto and New York Stock Exchanges "));
                add(new PostData(R.drawable.t02, "ABOUT US", "Thomson Reuters provides professionals with the intelligence, technology and human expertise they need to find trusted answers.We enable professionals in the financial and risk, legal, tax and accounting, and media markets to make the decisions that matter most, all powered by the world's most trusted news organization. Thomson Reuters shares are listed on the Toronto and New York Stock Exchanges "));
                add(new PostData(R.drawable.t03, "ABOUT US", "Thomson Reuters provides professionals with the intelligence, technology and human expertise they need to find trusted answers.We enable professionals in the financial and risk, legal, tax and accounting, and media markets to make the decisions that matter most, all powered by the world's most trusted news organization. Thomson Reuters shares are listed on the Toronto and New York Stock Exchanges "));
            }
        };
    }

    MyRecyclerAdapter myRecyclerAdapter;

    private void initViews() {
        container = (ViewGroup) findViewById(R.id.container);
        imgBg = (ImageView) findViewById(R.id.imgBg);
        titleView = (TextView) findViewById(R.id.title);
        subTitleView = (TextView) findViewById(R.id.subtitle);
        rv = (RecyclerView) findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        DividerItemDecoration divider = new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL);
        divider.setDrawable(getDrawable(R.drawable.divider));
        rv.addItemDecoration(divider);
        myRecyclerAdapter = new MyRecyclerAdapter(rv, this);
        myRecyclerAdapter.mData = buildData();
        rv.setAdapter(myRecyclerAdapter);
    }


    private static class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {
        boolean expanded;

        RecyclerView mRv;
        List<PostData> mData;
        Activity activity;
        Resources res;

        public MyRecyclerAdapter(RecyclerView rv, Activity activity) {
            mRv = rv;
            this.activity = activity;
            res = activity.getResources();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            return new ViewHolder(inflate);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            if (expanded) {
                holder.expanded();
            } else {
                holder.collapse();
            }


            if (mData != null) {
                final PostData data = mData.get(position);
                holder.img.setImageResource(data.imgRes);
                holder.title.setText(data.title);
                holder.content.setText(data.content);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("czc-demo", "item onClick");
                        if (expanded) {
                            Intent i = new Intent(activity, DetailActivity.class);
                            List<Pair<View, String>> participants = new ArrayList<>(3);
                            participants.add(Pair.create((View) holder.img, res.getString(R.string.img_shared_name)));
                            participants.add(Pair.create((View) holder.title, res.getString(R.string.title_shared_name)));
                            participants.add(Pair.create((View) holder.contentContainer, res.getString(R.string.content_shared_name)));
                            ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, participants.toArray(new Pair[participants.size()]));
                            i.putExtra("title", data.title);
                            i.putExtra("imgs", new int[]{data.imgRes, data.imgRes, data.imgRes});
                            i.putExtra("content", data.content);
                            i.putExtra("content_img", data.imgRes);
                            i.putExtra("content_p", data.content);
                            activity.startActivity(i, transitionActivityOptions.toBundle());
                        }
                    }
                });
            }
        }

        public boolean expand() {
            if (!expanded) {
                expanded = true;
                TransitionManager.beginDelayedTransition(mRv, new ChangeBounds());
                notifyDataSetChanged();
                return true;
            }
            return false;
        }

        public boolean collapse() {
            if (expanded) {
                expanded = false;
                TransitionManager.beginDelayedTransition(mRv, new ChangeBounds());
                notifyDataSetChanged();
                return true;
            }

            return false;
        }

        @Override
        public int getItemCount() {
            if (mData != null)
                return mData.size();
            return 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView img;
            TextView title;
            LinearLayout contentContainer;
            TextView content;

            public ViewHolder(View itemView) {
                super(itemView);
                //指定item的宽度
                ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
                if (layoutParams == null) {
                    layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                final Context ctx = itemView.getContext();
                layoutParams.width = (int) ctx.getResources().getDimension(R.dimen.item_w);
                itemView.setLayoutParams(layoutParams);


                img = (ImageView) itemView.findViewById(R.id.img);
                title = (TextView) itemView.findViewById(R.id.title);
                content = (TextView) itemView.findViewById(R.id.content);
                contentContainer = (LinearLayout) itemView.findViewById(R.id.content_container);

                //初始状态
                collapse();
            }

            public void collapse() {
                Log.d("czc-demo", "collapse");
                final Context ctx = img.getContext();
                ViewGroup.LayoutParams imgLp = img.getLayoutParams();
                if (imgLp == null) {
                    imgLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                imgLp.height = 0;
                img.setLayoutParams(imgLp);


                ViewGroup.LayoutParams contentLp = content.getLayoutParams();
                if (contentLp == null) {
                    contentLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                contentLp.height = (int) ctx.getResources().getDimension(R.dimen.content_h_begin);
                content.setLayoutParams(contentLp);
            }

            public void expanded() {
                Log.d("czc-demo", "expanded");
                final Context ctx = img.getContext();
                ViewGroup.LayoutParams imgLp = img.getLayoutParams();
                if (imgLp == null) {
                    imgLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                imgLp.height = (int) ctx.getResources().getDimension(R.dimen.img_h_after);
                img.setLayoutParams(imgLp);


                ViewGroup.LayoutParams contentLp = content.getLayoutParams();
                if (contentLp == null) {
                    contentLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                contentLp.height = (int) ctx.getResources().getDimension(R.dimen.content_h_after);
                content.setLayoutParams(contentLp);
            }
        }


    }

}
