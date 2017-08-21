package com.db.main.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.db.R;
import com.db.demo_normal.ui.DemoNormalFragment;
import com.db.hot_show.ui.HotShowFragment;
import com.db.weather.ui.WeatherListFragment;
import com.db.widget.fragment.BaseFragment;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * 作者：lyy on 2017/5/9 11:30
 */

public class MainFragment extends BaseFragment {

    private static final String KEY = "key";

//    @BindView(R.id.layout_container)
//    FrameLayout layout_container;

    //    @BindView(R.id.magic_indicator)
    MagicIndicator magic_indicator;

    private final int[] normalResId = new int[]{
            R.drawable.ic_hot_show_normal,
            R.drawable.ic_find_normal,
            R.drawable.ic_personal_normal
    };

    private final int[] pressedResId = new int[]{
            R.drawable.ic_hot_show_pressed,
            R.drawable.ic_find_pressed,
            R.drawable.ic_personal_pressed
    };

    private final int[] resName = new int[]{
            R.string.tab_hot_show,
            R.string.tab_find,
            R.string.tab_personal
    };

    private final SupportFragment[] fragments = new SupportFragment[3];

    private final FragmentContainerHelper fragmentContainerHelper = new FragmentContainerHelper();

    private long exitTime;//用于按两次Back键退出

    private Context context;

    private int current;

    public static MainFragment newInstance() {
        return newInstance("");
    }

    private static MainFragment newInstance(String value) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(KEY, value);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        magic_indicator = (MagicIndicator) view.findViewById(R.id.magic_indicator);

        ButterKnife.bind(this, view);

        context = view.getContext();

        initView();

        return view;
    }

    private void initView() {
        //初始化MVP

        //设置RefreshLayout

        //设置RecyclerView

        /***设置其他View***/
        //Fragment相关
        fragments[0] = HotShowFragment.newInstance();
        fragments[1] = WeatherListFragment.newInstance();
        fragments[2] = DemoNormalFragment.newInstance();

        loadMultipleRootFragment(R.id.layout_container, 0, fragments[0], fragments[1], fragments[2]);

        current = 0;
        //MagicIndicator
        CommonNavigator navigator = new CommonNavigator(context);
        navigator.setAdjustMode(true);
        navigator.setFollowTouch(true);
        navigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return fragments.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, int i) {
                CommonPagerTitleView titleView = new CommonPagerTitleView(context);
                titleView.setContentView(R.layout.item_main_tab_indicator_layout);

                Button tab_btn = (Button) titleView.findViewById(R.id.tab_btn);
                TextView tab_tv = (TextView) titleView.findViewById(R.id.tab_tv);
                tab_tv.setText(resName[i]);

                titleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {
                    @Override
                    public void onSelected(int i, int i1) {
                        tab_btn.setBackgroundResource(pressedResId[i]);
                        tab_tv.setTextColor(ContextCompat.getColor(context, R.color.color_tab_pressed));
                    }

                    @Override
                    public void onDeselected(int i, int i1) {
                        tab_btn.setBackgroundResource(normalResId[i]);
                        tab_tv.setTextColor(ContextCompat.getColor(context, R.color.color_tab_normal));
                    }

                    @Override
                    public void onLeave(int i, int i1, float v, boolean b) {

                    }

                    @Override
                    public void onEnter(int i, int i1, float v, boolean b) {

                    }
                });

                titleView.setOnClickListener(v -> {
                    fragmentContainerHelper.handlePageSelected(i);
                    showHideFragment(fragments[i], fragments[current]);
                    current = i;
                });

                return titleView;
            }

            //指示器下方的线
            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });

        magic_indicator.setNavigator(navigator);
        fragmentContainerHelper.attachMagicIndicator(magic_indicator);
    }

    @Override
    public boolean onBackPressedSupport() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            exitTime = System.currentTimeMillis();
            ToastUtils.showShort(getString(R.string.second_exit));
            return true;
        } else {
            return super.onBackPressedSupport();
        }
    }
}
