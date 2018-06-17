package ru.rpuxa.progersimulator.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ru.rpuxa.progersimulator.R;
import ru.rpuxa.progersimulator.gameplay.Player;


public class DrawerList extends LinearLayout implements View.OnClickListener {

    public int level = 0;

    public DrawerList(@NonNull Context context) {
        super(context);
        set();
    }

    public DrawerList(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        set();
    }

    public DrawerList(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        set();
    }

    private void set() {
        setOrientation(VERTICAL);
        View v = LayoutInflater.from(getContext()).inflate(R.layout.drawer_list, this, false);
        super.addView(v);
        v.findViewById(R.id.drawer_drawer_list).setOnClickListener(this);
    }

    @Override
    public void setBackground(Drawable background) {
        getChildAt(0).setBackground(background);
    }

    public void setIcon(int drawable) {
        ((ImageView) getChildAt(0).findViewById(R.id.icon_drawer_list)).setImageResource(drawable);
    }

    public void setTextColor(int color) {
        ((TextView) getChildAt(0).findViewById(R.id.text_drawer_list)).setTextColor(getResources().getColor(color));
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        try {
            getChildAt(1).setVisibility(GONE);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void setText(String text) {
        ((TextView) findViewById(R.id.text_drawer_list)).setText(text);
    }

    @Override
    public void onClick(View view) {
        if (Player.player.getLevel() < level) {
            Toast.makeText(getContext(), "Раздел не доступен. Требуется " + level + " уровень", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean flag = getChildAt(1).getVisibility() == GONE;
        getChildAt(1).setVisibility(flag ? VISIBLE : GONE);
        if (flag)
            ((ImageView) findViewById(R.id.arrow_drawer_list)).setImageDrawable(getResources().getDrawable(R.drawable.arrow_up));
        else
            ((ImageView) findViewById(R.id.arrow_drawer_list)).setImageDrawable(getResources().getDrawable(R.drawable.arrow_down));
    }

    public void setLevel(int level) {
        this.level = level;
        if (level > Player.player.getLevel())
            ((ImageView) findViewById(R.id.arrow_drawer_list)).setImageDrawable(getResources().getDrawable(R.drawable.lock));
    }

    public void unlock() {
        ((ImageView) findViewById(R.id.arrow_drawer_list)).setImageDrawable(getResources().getDrawable(R.drawable.arrow_up));

    }
}
