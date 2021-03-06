package com.example.countbackday;

//添加倒数日项目

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.NumberPicker.Formatter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class TaskCreateFragment extends DialogFragment implements OnClickListener {

	private EditText mEtName;
	private Button mBtnDate;
	private Spinner mSpCategory;
	private Spinner mSpRepeat;
	private Switch mSwTop;
	private Button mBtnDone;
	private Date mDate;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.choose, null);
		mEtName = (EditText)v.findViewById(R.id.et_name);
		mBtnDate = (Button)v.findViewById(R.id.btn_date);
		mSpCategory = (Spinner)v.findViewById(R.id.sp_category);
		mSpRepeat = (Spinner)v.findViewById(R.id.sp_repeat);
		mSwTop = (Switch)v.findViewById(R.id.sw_top);
		mBtnDone = (Button)v.findViewById(R.id.btn_done);

        getDialog().setTitle("添加倒数日");

		mBtnDate.setOnClickListener(this);
		mBtnDone.setOnClickListener(this);
		return v;
	}

	public final class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			processDateSet(year, month, day);
		}
	}
	
	private void processDateSet(int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(year, month, day, 0, 0, 1);
		mDate = c.getTime();
		mBtnDate.setText(new SimpleDateFormat("yyyy年MM月dd日").format(mDate));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_date:
			new DatePickerFragment().show(getFragmentManager(), "datePicker");
			break;
		case R.id.btn_done:
			saveTaskAndClose();
			break;

		default:
			break;
		}
	}

	private void saveTaskAndClose() {
		Task t = new Task();
		if (!TextUtils.isEmpty(mEtName.getText())) {
			t.setName(mEtName.getText().toString());
		} 
		String[] categories = getResources().getStringArray(R.array.task_categories);
		if (categories.length > mSpCategory.getSelectedItemPosition()) {
			t.setCategory(categories[mSpCategory.getSelectedItemPosition()]);
		} 
		t.setRepeatMode(mSpRepeat.getSelectedItemPosition()+1);
		if (mDate != null) {
                        t.setDate(false, mDate.getYear(), mDate.getMonth(), mDate.getDay());
		    //t.setDate(mDate);
		} 
		t.setId("tid_"+SystemClock.currentThreadTimeMillis());
		TaskBiz.saveTask(getActivity(), t);
		this.dismiss();
	}

}
