package gzkj.easygroupmeal.activity;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import gzkj.easygroupmeal.R;
import gzkj.easygroupmeal.base.BaseActivity;
import gzkj.easygroupmeal.bean.Cycle;

public class AttendanceDateActivity extends BaseActivity {

    @BindView(R.id.attendance_date_checkbox_mon)
    CheckBox attendanceDateCheckboxMon;
    @BindView(R.id.attendance_date_checkbox_tue)
    CheckBox attendanceDateCheckboxTue;
    @BindView(R.id.attendance_date_checkbox_wed)
    CheckBox attendanceDateCheckboxWed;
    @BindView(R.id.attendance_date_checkbox_thu)
    CheckBox attendanceDateCheckboxThu;
    @BindView(R.id.attendance_date_checkbox_fri)
    CheckBox attendanceDateCheckboxFri;
    @BindView(R.id.attendance_date_checkbox_sat)
    CheckBox attendanceDateCheckboxSat;
    @BindView(R.id.attendance_date_checkbox_sun)
    CheckBox attendanceDateCheckboxSun;
    @BindView(R.id.title)
    TextView titleTv;
    String monState = "", tueState = "", wedState = "", thuState = "", friState = "", satState = "", sunState = "";
    String monName = "", tueName = "", wedName = "", thuName = "", friName = "", satName = "", sunName = "";
    private String title;
    private StringBuilder cycleName, cycleNum;

    @Override
    public int intiLayout() {
        return R.layout.activity_attendance_date;
    }

    @Override
    public void initData() {
        hideTopUIMenu();
        title = getIntent().getStringExtra("title");
        titleTv.setText(title);
        cycleName = new StringBuilder();
        cycleNum = new StringBuilder();
    }

    @Override
    public void setListener() {

    }

    @OnClick({R.id.back, R.id.sure, R.id.attendance_date_linear_mon, R.id.attendance_date_linear_tue, R.id.attendance_date_linear_wed, R.id.attendance_date_linear_thu, R.id.attendance_date_linear_fri, R.id.attendance_date_linear_sat, R.id.attendance_date_linear_sun})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sure:
                cycleName.delete(0, cycleName.length());
                cycleNum.delete(0, cycleNum.length());
                cycleNum.append(monState).append(tueState).append(wedState)
                        .append(thuState).append(friState).append(satState).append(sunState);
                cycleName.append(monName).append(tueName).append(wedName)
                        .append(thuName).append(friName).append(satName).append(sunName);
                if (cycleName.length()==0) {
                    toastShort("请选择周期");
                    return;
                }
                cycleName.delete(cycleName.lastIndexOf(","), cycleName.lastIndexOf(",") + 1);
                cycleNum.delete(cycleNum.lastIndexOf(","), cycleNum.lastIndexOf(",") + 1);
                EventBus.getDefault().postSticky(new Cycle(cycleName.toString(), cycleNum.toString()));
                finish();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.attendance_date_linear_mon:
                if ("".equals(monState)) {
                    monState = "1,";
                    monName = "每周一,";
                    attendanceDateCheckboxMon.setChecked(true);
                } else {
                    monState = "";
                    monName = "";
                    attendanceDateCheckboxMon.setChecked(false);
                }
                break;
            case R.id.attendance_date_linear_tue:
                if ("".equals(tueState)) {
                    tueState = "2,";
                    tueName = "每周二,";
                    attendanceDateCheckboxTue.setChecked(true);
                } else {
                    tueState = "";
                    tueName = "";
                    attendanceDateCheckboxTue.setChecked(false);
                }
                break;
            case R.id.attendance_date_linear_wed:
                if ("".equals(wedState)) {
                    wedState = "3,";
                    wedName = "每周三,";
                    attendanceDateCheckboxWed.setChecked(true);
                } else {
                    wedState = "";
                    wedName = "";
                    attendanceDateCheckboxWed.setChecked(false);
                }
                break;
            case R.id.attendance_date_linear_thu:
                if ("".equals(thuState)) {
                    thuState = "4,";
                    thuName = "每周四,";
                    attendanceDateCheckboxThu.setChecked(true);
                } else {
                    thuState = "";
                    thuName = "";
                    attendanceDateCheckboxThu.setChecked(false);
                }
                break;
            case R.id.attendance_date_linear_fri:
                if ("".equals(friState)) {
                    friState = "5,";
                    friName = "每周五,";
                    attendanceDateCheckboxFri.setChecked(true);
                } else {
                    friState = "";
                    friName = "";
                    attendanceDateCheckboxFri.setChecked(false);
                }
                break;
            case R.id.attendance_date_linear_sat:
                if ("".equals(satState)) {
                    satState = "6,";
                    satName = "每周六,";
                    attendanceDateCheckboxSat.setChecked(true);
                } else {
                    satState = "";
                    satName = "";
                    attendanceDateCheckboxSat.setChecked(false);
                }
                break;
            case R.id.attendance_date_linear_sun:
                if ("".equals(sunState)) {
                    sunState = "0,";
                    sunName = "每周日,";
                    attendanceDateCheckboxSun.setChecked(true);
                } else {
                    sunState = "";
                    sunName = "";
                    attendanceDateCheckboxSun.setChecked(false);
                }
                break;
        }
    }

    @Override
    public void toActivityData(String flag, String object) {

    }

}
