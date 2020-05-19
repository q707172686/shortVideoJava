package com.aihao.shortvideojava.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MenuItem;

import com.aihao.shortvideojava.R;
import com.aihao.shortvideojava.model.BottomBar;
import com.aihao.shortvideojava.model.Destination;
import com.aihao.shortvideojava.utils.AppConfig;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import java.util.List;

public class AppBottomBar extends BottomNavigationView {
    private static int[] sIcons = new int[]{R.drawable.icon_tab_home, R.drawable.icon_tab_sofa, R.drawable.icon_tab_publish, R.drawable.icon_tab_find, R.drawable.icon_tab_mine};
    private BottomBar bottomBar;
    public AppBottomBar(Context context) {
        this(context, null);
    }

    public AppBottomBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("RestrictedApi")
    public AppBottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs,defStyleAttr);

         bottomBar = AppConfig.getsBottomBar();
        List<BottomBar.Tabs> tabs = bottomBar.getTabs();
        int[][] states = new int [2][];
        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{};

        int[] colors = new int[]{Color.parseColor(bottomBar.getActiveColor()),Color.parseColor(bottomBar.getInActiveColor())};
        ColorStateList colorStateList = new ColorStateList(states,colors);
        setItemIconTintList(colorStateList);
        setItemTextColor(colorStateList);
        setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        setSelectedItemId(bottomBar.getSelectTab());

        for (int i = 0;i < tabs.size();i++) {
            BottomBar.Tabs tab = tabs.get(i);
            if (!tab.isEnable())
                return;;
            int id = getId(tab.getPageUrl());
            Resources res = getResources();
            String title ="";
            if("Home".equals(tab.getTitle())){
                title = res.getString(R.string.home);
            } else if("Sofa".equals(tab.getTitle())) {
                title = res.getString(R.string.sofa);
            } else if("Find".equals(tab.getTitle())) {
                title = res.getString(R.string.find);
            }else if("My".equals(tab.getTitle())) {
                title = res.getString(R.string.my);
            }
            MenuItem item = getMenu().add(0,id,tab.getIndex(),title);
            item.setIcon(sIcons[tab.getIndex()]);

        }
        for (int i = 0;i < tabs.size();i++) {
            BottomBar.Tabs tab = tabs.get(i);
            int iconSize = dp2px(tab.getSize());

            BottomNavigationMenuView menuView = (BottomNavigationMenuView) getChildAt(0);
            BottomNavigationItemView itemView = (BottomNavigationItemView)menuView.getChildAt(tab.getIndex());
            itemView.setIconSize(iconSize);

            if (TextUtils.isEmpty(tab.getTitle())){
                itemView.setIconTintList(ColorStateList.valueOf(Color.parseColor(tab.getTintColor())));
                itemView.setShifting(false);
            }

        }
    }

    private int dp2px(int size) {
        float value = getContext().getResources().getDisplayMetrics().density * size + 0.5f;
        return (int) value;
    }

    private int getId(String pageUrl) {
        Destination destination = AppConfig.getDestConfig().get(pageUrl);
        if (destination == null) {
            return -1;
        }

        return  destination.getId();
    }
}
