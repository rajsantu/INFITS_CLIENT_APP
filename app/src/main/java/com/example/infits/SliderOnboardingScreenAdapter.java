package com.example.infits;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

public class SliderOnboardingScreenAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

   public SliderOnboardingScreenAdapter(Context context){
       this.context = context;
   }
   int images[] = {R.drawable.infit_transparent1,R.drawable.infit_transparent1,R.drawable.infit_transparent1};
   int headings[] = {R.string.feature_1,R.string.feature_2,R.string.feature_3};
   int descs[] = {R.string.Lorem_Ipsum,R.string.Lorem_Ipsum,R.string.Lorem_Ipsum};

    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_anim_onboarding_screen,container,false);
        // pointing our design
        ImageView image = view.findViewById(R.id.imageView8);
        TextView heading = view.findViewById(R.id.heading);
        TextView desc = view.findViewById(R.id.textview16);
        // setting the sources
        image.setImageResource(images[position]);
        heading.setText(headings[position]);
        desc.setText(descs[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
       container.removeView((ConstraintLayout)object);
    }
}
