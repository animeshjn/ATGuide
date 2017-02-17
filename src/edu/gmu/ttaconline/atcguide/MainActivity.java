package edu.gmu.ttaconline.atcguide;







import edu.gmu.ttaconline.atcguide.R;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
public class MainActivity extends Activity {
	static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        ActionBar action=getActionBar();
        getActionBar().setDisplayUseLogoEnabled(false);
		getActionBar().setDisplayShowHomeEnabled(false);
		action.setDisplayShowCustomEnabled(true);
		context=getApplicationContext();
		getActionBar().setHomeButtonEnabled(false);
		View v=getLayoutInflater().inflate(R.layout.action_main,null);
		v.findViewById(R.id.newrecord).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				actionNew(v);
			}
		});
		v.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,android.widget.LinearLayout.LayoutParams.MATCH_PARENT));
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(v);
        
		setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    public void actionNew(View v)
    {
    	Intent i= new Intent(context,InputForm.class );
    	Toast.makeText(context, "Enter student data", Toast.LENGTH_SHORT).show();
    	startActivity(i);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id==R.id.newrecord) {
        	Intent i= new Intent(context,InputForm.class );
        	Toast.makeText(context, "Enter student data", Toast.LENGTH_SHORT).show();
        	startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            fillStudentData(inflater, rootView);
            return rootView;
        }
      
        public void actionNew(View b)
        {
        	Intent i= new Intent(context,InputForm.class );
        	Toast.makeText(context, "Enter student data", Toast.LENGTH_SHORT).show();
        	startActivity(i);
        }
		private void fillStudentData(LayoutInflater inflater, View mainFragment) {
			TableLayout allStudentsTable = (TableLayout) mainFragment
					.findViewById(R.id.studentData);
			if (allStudentsTable != null) {
				TableRow r = (TableRow) inflater.inflate(R.layout.row, null);
				// r.setLayoutParams(new LayoutParams(
				// LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
				r.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));
				r.setPadding(0, 0, 0,0);
				TextView tv = (TextView) r.findViewById(R.id.textView1);
				tv.setText("Belle Grey, 5th Grade");
				tv.setTag("sample");
				Button open = (Button) r.findViewById(R.id.open);
				open.setOnClickListener(clickListener);
				Button trial1 = (Button) r.findViewById(R.id.trial1);
				trial1.setOnClickListener(clickListener);
				Button trial2 = (Button) r.findViewById(R.id.trial2);
				trial2.setOnClickListener(clickListener);
				Button preview = (Button) r.findViewById(R.id.preview);
				preview.setOnClickListener(clickListener);
				
				Button delete =(Button)r.findViewById(R.id.deleterecordbutton);
				delete.setOnClickListener(clickListener);
				allStudentsTable.addView(r);
				allStudentsTable.bringToFront();
				r.bringToFront();
				//Button x = (Button) mainFragment.findViewById(R.id.);
				//x.setX(100);
				//x.setY(100);
				//x.setBackgroundColor(Color.RED);

			}
		}

		OnClickListener clickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.open:
//					Intent i = new Intent(context,
//							StudentInformationActivity.class);
//					i.putExtra("loadSampData", true);
//					startActivity(i);.
					Toast.makeText(context, "Open ", Toast.LENGTH_SHORT)
					.show();
					break;
				case R.id.trial1:
					Toast.makeText(context, "trial1 ", Toast.LENGTH_SHORT)
							.show();
					break;
				case R.id.trial2:
					Toast.makeText(context, "trial2 ", Toast.LENGTH_SHORT)
							.show();
					break;
				case R.id.preview:
					Toast.makeText(context, "preview report",
							Toast.LENGTH_SHORT).show();
					break;
					
				case R.id.deleterecordbutton:
			
					Toast.makeText(context,"Cannot delete sample data", Toast.LENGTH_SHORT).show();
					
				}
			}

		};
	}
			
		
    }

