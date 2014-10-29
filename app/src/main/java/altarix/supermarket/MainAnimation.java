package altarix.supermarket;

import android.graphics.Typeface;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainAnimation {

    TextView text_1, text_2;
    Animation animation_1,animation_2,animation_3;
    int color_1,color_2,color_3;
    RelativeLayout relativeLayout_1,relativeLayout_2,relativeLayout_3;

    public MainAnimation(TextView text_1, TextView text_2, Animation animation_1,Animation animation_2, Animation animation_3,
                RelativeLayout relativeLayout_1, RelativeLayout relativeLayout_2, RelativeLayout relativeLayout_3,
                int color_1, int color_2, int color_3) {
        this.text_1 = text_1;
        this.text_2 = text_2;
        this.animation_1 = animation_1;
        this.animation_3 = animation_3;
        this.color_2 = color_2;
        this.relativeLayout_2 = relativeLayout_2;
        this.relativeLayout_3 = relativeLayout_3;
        this.relativeLayout_1 = relativeLayout_1;
        this.color_3 = color_3;
        this.color_1 = color_1;
        this.animation_2 = animation_2;
    }

    public MainAnimation(TextView text_1, Animation animation_1, RelativeLayout relativeLayout_1, RelativeLayout relativeLayout_2,RelativeLayout relativeLayout_3,
                Animation animation_2, int color_1, int color_2, int color_3, TextView text_2) {
        this.text_1 = text_1;
        this.animation_1 = animation_1;
        this.relativeLayout_1 = relativeLayout_1;
        this.relativeLayout_2 = relativeLayout_2;
        this.animation_2 = animation_2;
        this.color_1 = color_1;
        this.color_2 = color_2;
        this.color_3 = color_3;
        this.text_2 = text_2;
        this.relativeLayout_3 = relativeLayout_3;
    }

    public void startAnimationShort(){
        animation_1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                text_1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                text_1.setTextColor(color_1);
                relativeLayout_1.setBackgroundColor(color_2);
                text_2.setTextColor(color_3);
                text_2.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                relativeLayout_1.setEnabled(true);
                relativeLayout_2.setEnabled(true);
                relativeLayout_3.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        relativeLayout_1.setEnabled(false);
        relativeLayout_2.setEnabled(false);
        relativeLayout_3.setEnabled(false);
        relativeLayout_1.startAnimation(animation_1);
        relativeLayout_2.setBackgroundResource(R.drawable.select_category_product);
        relativeLayout_2.startAnimation(animation_2);

    }

    public void startAnimationLong(){
        animation_2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                relativeLayout_2.setBackgroundColor(color_2);
                text_2.setTextColor(color_3);
                text_2.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                relativeLayout_1.setEnabled(true);
                relativeLayout_2.setEnabled(true);
                relativeLayout_3.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        relativeLayout_1.setEnabled(false);
        relativeLayout_2.setEnabled(false);
        relativeLayout_3.setEnabled(false);
        startAnimationLongUtil();
        relativeLayout_2.setBackgroundResource(R.drawable.select_category_product);
        relativeLayout_2.startAnimation(animation_2);
        relativeLayout_3.setBackgroundResource(R.drawable.select_category_product);
        relativeLayout_3.startAnimation(animation_3);

    }

    private void startAnimationLongUtil() {
        animation_1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                text_1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                text_1.setTextColor(color_1);
                relativeLayout_1.setBackgroundColor(color_2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        relativeLayout_1.startAnimation(animation_1);
    }
}
