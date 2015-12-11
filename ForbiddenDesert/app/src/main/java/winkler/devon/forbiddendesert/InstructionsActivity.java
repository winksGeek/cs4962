package winkler.devon.forbiddendesert;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class InstructionsActivity extends Activity {

    int _index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        _index = 0;
        final int [] drawables = new int[]{R.drawable.rules_1, R.drawable.rules_2,R.drawable.rules_3,R.drawable.rules_4,R.drawable.rules_5,R.drawable.rules_6,R.drawable.rules_7};

        final ImageView ruleView = new ImageView(this);
        ruleView.setImageResource(drawables[_index]);
        mainLayout.addView(ruleView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

        LinearLayout buttonBar = new LinearLayout(this);
        buttonBar.setOrientation(LinearLayout.HORIZONTAL);
        buttonBar.setGravity(Gravity.RIGHT);

        final Button previous = new Button(this);
        previous.setText("Previous Page");
        previous.setTextSize(10);
        previous.setVisibility(View.GONE);
        buttonBar.addView(previous, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

        final Button next = new Button(this);
        next.setText("Next Page");
        next.setTextSize(10);
        next.setVisibility(View.VISIBLE);
        buttonBar.addView(next, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

        mainLayout.addView(buttonBar, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _index--;
                if (_index == 0) {
                    previous.setVisibility(View.GONE);
                }
                next.setVisibility(View.VISIBLE);
                ruleView.setImageResource(drawables[_index]);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _index++;
                if (_index >= drawables.length - 1) {
                    next.setVisibility(View.GONE);
                }
                previous.setVisibility(View.VISIBLE);
                ruleView.setImageResource(drawables[_index]);
            }
        });

        setContentView(mainLayout);
    }
}
