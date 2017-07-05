package com.mineprogramming.nbook;

import android.app.*;
import android.os.*;
import android.text.*;
import android.view.*;
import android.widget.*;
import android.view.animation.*;
import android.graphics.*;
import android.view.View.*;
import android.widget.ActionMenuView.*;
import android.content.*;
import android.graphics.drawable.*;
import android.content.res.*;
import java.io.*;
import org.xml.sax.*;
import android.text.style.*;

public class MainActivity extends Activity 
{
	String[] lessons=new String[256];
	String[] buildcodes=new String[256];
	String[] files=new String[256];
	String divider="===/===";
	int count=0;
	boolean location=false;
	boolean animating=false;
	void defineLessons()
	{
		newLesson("Введение","mr_1.txt");
		newLesson("Начало работы","how_to_start.txt");
		newLesson("JavaScript. Переменные","mr_2.txt");
		newLesson("JavaScript. Массивы","mr_3.txt");
		newLesson("JavaScript. Условия и boolean","mr_4.txt");
		newLesson("JavaScript. Функции","mr_5.txt");
		newLesson("JavaScript. Строки","mr_6.txt");
		newLesson("JavaScript. Объекты","mr_7.txt");
		
		newLesson("NIDE. Введение","mr_nide_1.txt");
		newLesson("NIDE. Система библиотек","im_nide_1.txt");
		
	}
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
		try{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		setAnimView(R.layout.lessons);
		ActionBar actionBar=getActionBar();
		actionBar.setTitle(Html.fromHtml("<font color=\"#ffffff\">N ModPE Book</font>"));
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#c2185b")));
		LayoutInflater actioninflater=getLayoutInflater();
		View action=actioninflater.inflate(R.layout.searchbar,null);
		final EditText search=(EditText)action.findViewById(R.id.searchbar);
			search.addTextChangedListener(new TextWatcher(){

					@Override
					public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4)
					{
						// TODO: Implement this method
					}

					@Override
					public void onTextChanged(CharSequence p1, int p2, int p3, int p4)
					{
						// TODO: Implement this method
						searchLesson(search.getText().toString());
					}

					@Override
					public void afterTextChanged(Editable p1)
					{
						// TODO: Implement this method
					}

			
		});
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(action);
		defineLessons();
		//parseLesson("<~code>Этот код должен быть в print()</code><~image>ic_launcher.png</image>");
		//updateLessonsList();
		searchLesson("");
		location=true;
		}catch(Exception e){print(e.toString());}
	}
	void searchLesson(String what)
	{
		try{
		
		LinearLayout lessons_layout=(LinearLayout) findViewById(R.id.lessons);
		lessons_layout.removeAllViews();
		
		for(int i=0;i<count;i++)
		{
			String name="Урок "+i+" : "+lessons[i];
			if(name.toLowerCase().indexOf(what.toLowerCase())!=-1)
			{
				int index1=name.toLowerCase().indexOf(what.toLowerCase());
				int length=what.split("").length-1;
				
				Spannable span=new SpannableString(name);
				span.setSpan(new ForegroundColorSpan(Color.parseColor("#b388ff")),index1,index1+length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				//Toast.makeText(this,span,Toast.LENGTH_LONG).show();
				//print(span+"");
				final int index=i;
				//final Context contextThemeWrapper = new ContextThemeWrapper(this, R.style.button);
				//Button button=new Button(contextThemeWrapper);
				/*LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
				button.setLayoutParams(params);
				//button.setBackgroundDrawable(getResources().getDrawable(R.drawable.ripple_2));*/
				 
				View button=createItem(span);
				button.setOnClickListener(new OnClickListener()
					{

						@Override
						public void onClick(View p1)
						{
							// TODO: Implement this methods
							try{
								if(!animating)
								{
								setAnimView(R.layout.lesson_layout);
								LinearLayout les_layout=(LinearLayout)findViewById(R.id.custom_lesson_layout);
								parseLesson(files[index],les_layout);
								TextView text=(TextView) findViewById(R.id.lesson_name);
								text.setText(lessons[index]);
								getActionBar().setDisplayShowCustomEnabled(false);
								location=false;
								}
							}catch(Exception e){print(e.toString());}
						}


					});
				lessons_layout.addView(button);
			}
		}
		}catch(Exception e){print(e.toString());}
	}
	String getStringFromAssetFile(String filename)
	{
		/*AssetManager am = activity.getAssets();
		try
		{
			InputStream is = am.open("test.txt");
		}
		catch (IOException e)
		{}
		String s = convertStreamToString(is);
		is.close();
		return s;*/
		String text = filename;
		byte[] buffer = null;
		InputStream is;
		try {
			is = getAssets().open(text);
			int size = is.available();
			buffer = new byte[size];
			is.read(buffer);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String str_data = new String(buffer);
		return str_data;
	}
	/*AssetManager myAssetManager = getApplicationContext().getAssets();

	try {
		String[] Files = myAssetManager.list(""); // массив имен файлов
		Toast.makeText(getApplicationContext(), Files[0] + ", " + Files[1],
					   Toast.LENGTH_LONG).show();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/
	String parseLesson(String lessonfile, LinearLayout layout)
	{
		//getResources().getAssets().
		String lessoncode=getStringFromAssetFile(lessonfile);
		String[] tags=lessoncode.split("<~");
		for(int i=0;i<tags.length;i++)
		{
			String current_tag=tags[i];
			if(current_tag.indexOf("code>")!=-1&&current_tag.indexOf("</code>")!=-1)
			{
				String code=current_tag.substring(current_tag.indexOf("code>")+5,current_tag.indexOf("</code>")-current_tag.indexOf("code>"));
				//print(current_tag.indexOf("code>")+"\n"+current_tag.indexOf("/code>"));
				final Context contextThemeWrapper = new ContextThemeWrapper(this, R.style.lesson_code);
				TextView textview=new TextView(contextThemeWrapper);
				textview.setText(code);
				layout.addView(textview);
				if(current_tag.split("</code>").length>1)
				{
					//print(current_tag.split("</code>").length+" length code");
					if(!current_tag.split("</code>")[1].equals(""))
				{
					final Context contextThemeWrapper1 = new ContextThemeWrapper(this, R.style.lesson_text);
					TextView textview1=new TextView(contextThemeWrapper1);
					textview1.setText(current_tag.split("</code>")[1]);
					layout.addView(textview1);
				}}
				//print(code);
			}else{
			if(current_tag.indexOf("image>")!=-1&&current_tag.indexOf("</image>")!=-1)
			{
				try{
				String imagename=current_tag.substring(current_tag.indexOf("image>")+6,current_tag.indexOf("</image>")-current_tag.indexOf("image>"));
				//print(imagename);
				InputStream ims = getAssets().open(imagename);
				//Context context=new ContextThemeWrapper(this,R.style.lesson_image);
				Drawable d = Drawable.createFromStream(ims, null);
				ImageView image=new ImageView(this);
				//LayoutParams params=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
				//image.setLayoutParams(params);
				
				image.setImageDrawable(d);
				layout.addView(image);
					if(current_tag.split("</image>").length>1)
					{
						//print(current_tag.split("</image>").length+" length image");
						if(!current_tag.split("</image>")[1].equals(""))
					{
						final Context contextThemeWrapper1 = new ContextThemeWrapper(this, R.style.lesson_text);
						TextView textview1=new TextView(contextThemeWrapper1);
						textview1.setText(current_tag.split("</image>")[1]);
						layout.addView(textview1);
					}}
				}catch(Exception e){print(e.toString());}
			}else{
				final Context contextThemeWrapper = new ContextThemeWrapper(this, R.style.lesson_text);
				TextView textview=new TextView(contextThemeWrapper);
				textview.setText(current_tag);
				layout.addView(textview);
			}
			}
			}
			return "";
		}
		
	
	
	void setAnimView(int resId)
	{
		if(!animating){
		if(resId!=R.layout.lessons)
		{
			location=false;
		}
		final FrameLayout layout=(FrameLayout) findViewById(R.id.main_frame);
		Animation in_animation=AnimationUtils.loadAnimation(this,R.anim.fade_in);
		Animation out_animation=AnimationUtils.loadAnimation(this,R.anim.fade_out);
		final View out_view=layout.getChildAt(0);
		animating=true;
		View in_view=getLayoutInflater().inflate(resId,null);
		out_view.startAnimation(out_animation);
		out_animation.setAnimationListener(new Animation.AnimationListener(){

				@Override
				public void onAnimationStart(Animation p1)
				{
					// TODO: Implement this method
				}

				@Override
				public void onAnimationEnd(Animation p1)
				{
					// TODO: Implement this method
					layout.removeView(out_view);
					animating=false;
				}

				@Override
				public void onAnimationRepeat(Animation p1)
				{
					// TODO: Implement this method
				}

			
		});
		layout.addView(in_view);
		in_view.startAnimation(in_animation);
		}
	}
	public void test(View v)
	{
		try{
		//updateLessonsList();
		Button b=(Button)v;
		Spannable span=new SpannableString("1234567890");
		span.setSpan(new ForegroundColorSpan(Color.parseColor("#b388ff")),3,7,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			
		b.setText(span);
		}catch(Exception e){print(e.toString());}
	}
	int newLesson(String name,String file)
	{
		lessons[count]=name;
		files[count]=file;
		count++;
		return count-1;
	}
	void addText(int i, String text)
	{
		buildcodes[i]=buildcodes[i]+"<t>"+text+divider;
	}
	void addCode(int i, String text)
	{
		buildcodes[i]=buildcodes[i]+"<c>"+text+divider;
	}
	void addImage(int i, int src)
	{
		buildcodes[i]=buildcodes[i]+"<i>"+src+divider;
	}
	void addToLayout(String buildcode,LinearLayout layout)
	{
		if(buildcode!=null)
		{
		if(!buildcode.equals(""))
		{
		String[] tags=buildcode.split(divider);
		for(int i=0;i<tags.length;i++)
		{
			String current_tag=tags[i];
			
			if(current_tag.indexOf("<t>")!=-1)
			{
				final Context contextThemeWrapper = new ContextThemeWrapper(this, R.style.lesson_text);
				String[] text=tags[i].split(">");
				TextView textview=new TextView(contextThemeWrapper);
				String t=text[1];
				textview.setText(t);
				layout.addView(textview);
				//print("
			}
			if(current_tag.indexOf("<i>")!=-1)
			{
				String[] text=tags[i].split(">");
				ImageView image=new ImageView(this);
				image.setImageResource(Integer.parseInt(text[1]));
				layout.addView(image);
				//print("
			}
			if(current_tag.indexOf("<c>")!=-1)
			{
				final Context contextThemeWrapper = new ContextThemeWrapper(this, R.style.lesson_code);
				String[] text=tags[i].split(">");
				TextView textview=new TextView(contextThemeWrapper);
				String t=text[1];
				textview.setText(t);
				layout.addView(textview);
			}
		}
		}
		}
	}
	/*void updateLessonsList()
	{
		try{
		for(int i=0;i<count;i++)
		{
			final int index=i;
			final Context contextThemeWrapper = new ContextThemeWrapper(this, R.style.button);
			Button button=new Button(contextThemeWrapper);
			button.setTextColor(Color.parseColor("#000000"));
			/*LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			button.setLayoutParams(params);
			button.setBackgroundDrawable(getResources().getDrawable(R.drawable.ripple_2));
			
			button.setText("Урок "+i+" : "+lessons[i]);
			button.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View p1)
					{
						// TODO: Implement this methods
						try{
						setAnimView(R.layout.lesson_layout);
						LinearLayout les_layout=(LinearLayout)findViewById(R.id.custom_lesson_layout);
						parseLesson(files[index],les_layout);
						TextView text=(TextView) findViewById(R.id.lesson_name);
						text.setText(lessons[index]);
						getActionBar().setDisplayShowCustomEnabled(false);
						location=false;
						}catch(Exception e){print(e.toString());}
					}

				
			});
			LinearLayout lessons_layout=(LinearLayout) findViewById(R.id.lessons);
			lessons_layout.addView(button);
			//TextView place=new TextView(this);
			//lessons_layout.addView(place);
		}
		}catch(Exception e){print(e.getMessage());}
	}*/
	void print(String msg)
	{
		Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
	}
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{     
		switch (keyCode)
		{     
		case KeyEvent.KEYCODE_BACK:
			 if (android.os.Build.VERSION.SDK_INT<android.os.Build.VERSION_CODES.ECLAIR &&event.getRepeatCount() == 0)
				 {     
				 onBackPressed(); 
				 }
			}
		return super.onKeyDown(keyCode, event);
	}    
	public void onBackPressed()
	{
		if(!location)
		{
			location=true;
			setAnimView(R.layout.lessons);
			//updateLessonsList();
			searchLesson("");
			getActionBar().setDisplayShowCustomEnabled(true);
			EditText searchbar=(EditText)((RelativeLayout)getActionBar().getCustomView()).getChildAt(0);
			searchbar.setText("");
		}else{
			finish();
		}
	}
	View createItem(CharSequence name)
	{
		LayoutInflater inflater=getLayoutInflater();
		View v=inflater.inflate(R.layout.item,null);
		TextView text=(TextView)v.findViewById(R.id.item_name);
		text.setText(name);
		return v;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.info:{
				setAnimView(R.layout.info);
				initInfo();
				getActionBar().setDisplayShowCustomEnabled(false);
			}
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	void initInfo()
	{
		
	}
}
