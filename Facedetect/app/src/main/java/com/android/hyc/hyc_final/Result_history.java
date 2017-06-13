package com.android.hyc.hyc_final;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.FillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import Face.JavaBean.User;
import Face.JavaBean.Userinfo;
import Face.MyErrorListener;
import Face.MyJsonArrayRequest;
import Face.sortDate;
import lecho.lib.hellocharts.view.LineChartView;

public class Result_history extends AppCompatActivity {
	private PieChart mChart;
	private RequestQueue mQueue;
	private LineChart mlineChart;
	private MaterialSpinner materialSpinner;
	private LineChartView mLineChartView;
	private Typeface tf;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);;
		setContentView(R.layout.activity_result_history);
		android.support.v7.app.ActionBar actionBar =getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle("考场统计");
		materialSpinner=(MaterialSpinner)findViewById(R.id.spinner);
		mQueue= Volley.newRequestQueue(getApplicationContext());
		mChart = (PieChart) findViewById(R.id.pie_chart_result);
		mlineChart=(LineChart)findViewById(R.id.line_chart_result);
		//mLineChartView=(LineChartView)findViewById(R.id.line_chart_result_view);
		mChart.setVisibility(View.INVISIBLE);
		mlineChart.setVisibility(View.INVISIBLE);
		materialSpinner.setItems("请选择查看的统计图","入场率统计", "入场时间统计", "KitKat", "Lollipop", "Marshmallow");
		materialSpinner.animate();
		materialSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
			@Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
				switch (position){
					case 0:
						break;
					case 1:
						mlineChart.setVisibility(View.INVISIBLE);
						Snackbar.make(view, item, Snackbar.LENGTH_LONG).show();
						MyJsonArrayRequest myJsonArrayRequest=new MyJsonArrayRequest(getString(R.string.ipwireless)+"/appp/index.php/Sql_Do/index/select%20*%20from%20t_ass", new Response.Listener<JSONArray>() {
							@Override
							public void onResponse(JSONArray jsonArray) {
								PieData  mPieData = getPieData(jsonArray);
								show_pie_Chart(mChart, mPieData);
							}
						},new MyErrorListener());
						mQueue.add(myJsonArrayRequest);
						mChart.setVisibility(View.VISIBLE);
						break;
					case 2:
						mChart.setVisibility(View.INVISIBLE);
						Snackbar.make(view, item, Snackbar.LENGTH_LONG).show();
						MyJsonArrayRequest mJAR_getEnterTime =new MyJsonArrayRequest(getString(R.string.ipwireless)+
								"/appp/index.php/Sql_Do/index/select%20*%20from%20exam"
								, new Response.Listener<JSONArray>() {
							@Override
							public void onResponse(JSONArray jsonArray) {
								try {
									final Date date_Enter_time = formatter.parse(jsonArray.getJSONObject(0).get("Enter_Time").toString());
									MyJsonArrayRequest myJsonArrayRequest_line_chart = new MyJsonArrayRequest(
											getString(R.string.ipwireless)
													+ "/appp/index.php/Sql_Do/index/select t_ass.ass_Enter_Time from t_ass,exam where exam.Teacher_Id="
													+ User.get_userid() + "&&exam.ExamId=t_ass.examid",
											new Response.Listener<JSONArray>() {
												@Override
												public void onResponse(JSONArray jsonArray) {
													Log.v("tag_volley", "just do not forget login");
													ArrayList<Date> dateliset = new ArrayList<Date>();
													for (int i = 0; i < jsonArray.length(); i++) {
														try {
															Date date = new Date();
															date = formatter.parse(jsonArray.getJSONObject(i).get("ass_Enter_Time").toString());
															dateliset.add(date);
														} catch (ParseException | JSONException e) {
															e.printStackTrace();
														}
													}
													sortDate sortDate = new sortDate();
													Collections.sort(dateliset, sortDate);
													for (int i = 0; i < dateliset.size(); i++) {
														Log.v("result:", dateliset.get(i).toString());
													}
													try {
														init_line_chart(mlineChart, dateliset,date_Enter_time);
													} catch (ParseException e) {
														e.printStackTrace();
													}
												}
											}, new MyErrorListener());
									mQueue.add(myJsonArrayRequest_line_chart);
								}catch (ParseException |JSONException e) {
									e.printStackTrace();
								}
							}
						},new MyErrorListener());
						mQueue.add(mJAR_getEnterTime);
						mlineChart.setVisibility(View.VISIBLE);
						break;
					default:
						break;
				}

			}


		});
	}
	private void init_line_chart(LineChart mlineChart,ArrayList<Date> dlist,Date enter_time) throws ParseException {
        mlineChart.setViewPortOffsets(0, 0, 0, 0);
		//mlineChart.setBackgroundColor(Color.rgb(104, 241, 175));
		// no description text
		mlineChart.setDescription("");

		// enable touch gestures
		mlineChart.setTouchEnabled(true);

		// enable scaling and dragging
		mlineChart.setDragEnabled(true);
		mlineChart.setScaleEnabled(true);

		// if disabled, scaling can be done on x- and y-axis separately
		mlineChart.setPinchZoom(true);

		mlineChart.setDrawGridBackground(false);

		tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

		XAxis x = mlineChart.getXAxis();
		x.setEnabled(true);
		x.setDrawGridLines(false);
		x.setLabelsToSkip(0);
		x.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
		x.setAxisLineColor(Color.BLACK);
		x.setAxisLineWidth(4);

		YAxis y = mlineChart.getAxisLeft();
		y.setTypeface(tf);
		y.setLabelCount(4, true);
		y.setShowOnlyMinMax(true);
		y.mAxisMaximum=10;
		y.setTextColor(Color.BLACK);
		y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
		y.setDrawGridLines(false);
		y.setAxisLineColor(Color.BLACK);
		y.setAxisLineWidth(4);
		mlineChart.setDrawBorders(true);
		mlineChart.setVisibleXRangeMaximum(10);  //一个界面显示多少个点，其他点可以通过滑动看到
		mlineChart.setVisibleXRangeMinimum(1);  //一个界面最少显示多少个点，放大后最多 放大到 剩多少 个点
		mlineChart.getAxisRight().setEnabled(true);
		// add data
		setData(dlist, enter_time);

		mlineChart.getLegend().setEnabled(false);

		mlineChart.animateXY(2000, 2000);


		// dont forget to refresh the drawing
		mlineChart.invalidate();

	}
	private void setData(ArrayList<Date> dlist,Date Enter_time) throws ParseException {

		ArrayList<String> xVals = new ArrayList<String>();
		for (int i = 0; i < dlist.size(); i++) {
			//xVals.add((dlist.get(i)).toString());
            xVals.add(i,i+"");
		}

		ArrayList<Entry> yVals = new ArrayList<Entry>();
        Date date=new Date(System.currentTimeMillis());
		for (int i = 0; i < dlist.size(); i++) {
            float time =dlist.get(i).getTime()-Enter_time.getTime();
			yVals.add(new Entry(time/1000/60/60/24/365,i));

		}

		LineDataSet set1;

		/*if (mlineChart.getData() != null &&
				mlineChart.getData().getDataSetCount() > 0) {
			set1 = (LineDataSet)mlineChart.getData().getDataSetByIndex(0);
			set1.setYVals(yVals);
			mlineChart.getData().setXVals(xVals);
			mlineChart.notifyDataSetChanged();
		} else {*/
			// create a dataset and give it a type
			set1 = new LineDataSet(yVals, "DataSet 1");

			set1.setDrawCubic(false);
			set1.setCubicIntensity(0.2f);
			set1.setDrawFilled(true);
			set1.setDrawCircles(true);
			set1.setLineWidth(1.8f);
			set1.setCircleRadius(4f);
			set1.setCircleColor(Color.WHITE);
			set1.setHighLightColor(Color.rgb(244, 117, 117));
			set1.setColor(Color.RED,200);
			set1.setFillColor(Color.RED);
			set1.setFillAlpha(240);
			set1.setDrawHorizontalHighlightIndicator(true);
			set1.setFillFormatter(new FillFormatter() {
				@Override
				public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
					return -10;
				}
			});

			// create a data object with the datasets
			final LineData data = new LineData(xVals, set1);
			data.setValueTypeface(tf);
			data.setValueTextSize(9f);
			data.setDrawValues(true);

			// set data
		    Thread thread=new Thread(){
				@Override
				public void run() {
					super.run();
					mlineChart.setData(data);
				}
			};
		thread.start();

		//}
	}
	private void show_pie_Chart(PieChart pieChart, PieData pieData) {

		pieChart.setHoleRadius(45f);  //半径
		pieChart.setTransparentCircleRadius(44f); // 半透明圈
		//pieChart.setHoleRadius(0)  //实心圆

		pieChart.setDescription("入场统计图");

		// mChart.setDrawYValues(true);
		pieChart.setDrawCenterText(true);  //饼状图中间可以添加文字

		pieChart.setDrawHoleEnabled(true);

		pieChart.setRotationAngle(90); // 初始旋转角度

		// draws the corresponding description value into the slice
		// mChart.setDrawXValues(true);

		// enable rotation of the chart by touch
		pieChart.setRotationEnabled(true); // 可以手动旋转

		// display percentage values
		pieChart.setUsePercentValues(true);  //显示成百分比
		// mChart.setUnit(" €");
		// mChart.setDrawUnitsInChart(true);

		// add a selection listener
//      mChart.setOnChartValueSelectedListener(this);
		// mChart.setTouchEnabled(false);

//      mChart.setOnAnimationListener(this);

		pieChart.setCenterText("入场率统计");  //饼状图中间的文字
		pieChart.setDescriptionTextSize(20);

		//设置数据
		pieChart.setData(pieData);

		// undo all highlights
//      pieChart.highlightValues(null);
//      pieChart.invalidate();

		Legend mLegend = pieChart.getLegend();  //设置比例图
		mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);  //最右边显示
//      mLegend.setForm(LegendForm.LINE);  //设置比例图的形状，默认是方形
		mLegend.setXEntrySpace(7f);
		mLegend.setYEntrySpace(5f);

		pieChart.animateXY(1000, 1000);  //设置动画
		// mChart.spin(2000, 0, 360);
	}
	private PieData  getPieData(JSONArray jsonArray) {

		ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容

		xValues.add("未到场");
		xValues.add("验证通过");
		xValues.add("验证未通过的");

		ArrayList<Entry> yValues = new ArrayList<Entry>();  //yVals用来表示封装每个饼块的实际数据
		// 饼图数据
		/**
		 * 将一个饼形图分成四部分， 四部分的数值比例为14:14:34:38
		 * 所以 14代表的百分比就是14%
		 */
		float quarterly1 = 0;
		float quarterly2 = 0;
		float quarterly3 = 0;
		for(int i=0;i<jsonArray.length();i++) {
			try {
				switch (jsonArray.getJSONObject(i).getString("ass_status")) {
					case "0":
						quarterly1+=1;
						break;
					case "1":
						quarterly2+=1;
						break;
					case "2":
						quarterly3+=1;
						break;
					default:
						break;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		yValues.add(new Entry(quarterly1, 0));
		yValues.add(new Entry(quarterly2, 1));
		yValues.add(new Entry(quarterly3, 2));
		//y轴的集合
		PieDataSet pieDataSet = new PieDataSet(yValues, "Quarterly Revenue 2014"/*显示在比例图上*/);
		pieDataSet.setSliceSpace(1f); //设置个饼状图之间的距离

		ArrayList<Integer> colors = new ArrayList<Integer>();

		// 饼图颜色
		colors.add(Color.rgb(205, 205, 205));
		colors.add(Color.rgb(114, 188, 223));
		colors.add(Color.rgb(255, 123, 124));
		colors.add(Color.rgb(57, 135, 200));

		pieDataSet.setColors(colors);

		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float px = 5 * (metrics.densityDpi / 160f);
		pieDataSet.setSelectionShift(px); // 选中态多出的长度
		pieDataSet.setValueTextSize(10);

		PieData pieData = new PieData(xValues, pieDataSet);

		return pieData;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


}
