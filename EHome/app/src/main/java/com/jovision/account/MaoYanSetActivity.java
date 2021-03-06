package com.jovision.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.percent.PercentRelativeLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jovision.JVBase;
import com.jovision.JniUtil;
import com.jovision.PlayUtil;
import com.ldl.dialogshow.dialog.listener.OnBtnClickL;
import com.ldl.dialogshow.dialog.widget.NormalDialog;
import com.xingyeda.ehome.ActivityHomepage;
import com.xingyeda.ehome.R;
import com.xingyeda.ehome.base.BaseActivity;
import com.xingyeda.ehome.base.ConnectPath;
import com.xingyeda.ehome.dialog.DialogShow;
import com.xingyeda.ehome.door.DoorFragment;
import com.xingyeda.ehome.http.okhttp.ConciseCallbackHandler;
import com.xingyeda.ehome.http.okhttp.ConciseStringCallback;
import com.xingyeda.ehome.http.okhttp.OkHttp;
import com.xingyeda.ehome.util.BaseUtils;
import com.xingyeda.ehome.util.SharedPreUtil;
import com.xingyeda.ehome.view.SwitchButton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MaoYanSetActivity extends BaseActivity {


    @Bind(R.id.jv_set_title)
    TextView jvSetTitle;//标题
    @Bind(R.id.jv_set_name)
    EditText jvSetName;//呢称输入

    @Bind(R.id.maoyan_set)
    LinearLayout maoyanSet;//猫眼设置布局
    @Bind(R.id.maoyan_set_vocality)
    SwitchButton maoyanSetVocality;//猫眼声音
    @Bind(R.id.maoyan_set_shake)
    SwitchButton maoyanSetShake;//猫眼震动
    @Bind(R.id.maoyan_set_doorbell)
    SwitchButton maoyanSetDoorbell;//猫眼感应门铃
    @Bind(R.id.maoyan_set_nfrared_induction)
    SwitchButton maoyanSetNfraredInduction;//猫眼红外感应
    @Bind(R.id.maoyan_set_motion_detection)
    SwitchButton maoyanSetMotionDetection;//猫眼移动侦测

    @Bind(R.id.jv_camera_set)
    LinearLayout jvCameraSet;//摄像头布局设置
    @Bind(R.id.camera_siren_push)
    SwitchButton cameraSirenPush;//摄像头报警推送
    @Bind(R.id.camera_motion_detection)
    SwitchButton cameraMotionDetection;//摄像头移动侦测
    @Bind(R.id.camera_motion_detection_set)
    SwitchButton cameraMotionDetectionSet;//摄像头移动侦测灵敏度
//    @Bind(R.id.guard_time_show)
//    TextView guardTimeShow;
    @Bind(R.id.jv_siren_set_text)
    TextView jvSirenSetText;
//    @Bind(R.id.guard_time)
//    PercentRelativeLayout guardTime;


    private String type;
    private String id;

    private boolean isMaoyanVocality;
    private boolean isMaoyanhake;
    private boolean isMaoyanDoorbell;
    private boolean isMaoyanNfraredInduction;
    private boolean isMaoyanSetMotionDetection;


    private boolean isCameraCirenPush;
    private boolean isCameraMotionDetection;
    private boolean isCameraMotionDetectionSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mao_yan_set);
        ButterKnife.bind(this);
        type = getIntent().getExtras().getString("type");
        id = getIntent().getExtras().getString("cameraId");
        if (type.equals("camera0")) {
            jvCameraSet.setVisibility(View.VISIBLE);
            jvSetTitle.setText("摄像头设置");
            initCamera();
        } else if (type.equals("camera1")) {
            jvCameraSet.setVisibility(View.VISIBLE);
            jvSetTitle.setText("摇头机设置");
            initCamera();
        } else if (type.equals("camera2")) {
            maoyanSet.setVisibility(View.VISIBLE);
            jvSetTitle.setText("猫眼设置");
            initMaoyan();
        } else if ("on_off".equals(type)) {
            jvSetTitle.setText("开关设置");
            jvSirenSetText.setVisibility(View.GONE);
        }
    }

    private void initCamera() {
        isCameraCirenPush = SharedPreUtil.getBoolean(mContext, "camera_siren_push" , false);
        isCameraMotionDetection =SharedPreUtil.getBoolean(mContext, "camera_motion_detection",false);
        isCameraMotionDetectionSet = SharedPreUtil.getBoolean(mContext, "camera_motion_detection_set",false);
        cameraSirenPush.setmSetSwitch(!isCameraCirenPush);
        cameraMotionDetection.setmSetSwitch(!isCameraMotionDetection);
        cameraMotionDetectionSet.setmSetSwitch(!isCameraMotionDetectionSet);

        cameraSirenPush.setOnChangeListener(changeListener);
        cameraMotionDetection.setOnChangeListener(changeListener);
        cameraMotionDetectionSet.setOnChangeListener(changeListener);


    }

    private void initMaoyan() {
        isMaoyanVocality = SharedPreUtil.getBoolean(mContext, "maoyan_vocality",false);
        isMaoyanhake = SharedPreUtil.getBoolean(mContext, "maoyan_shake",false);
        isMaoyanDoorbell = SharedPreUtil.getBoolean(mContext, "maoyan_doorbell",false);
        isMaoyanNfraredInduction = SharedPreUtil.getBoolean(mContext, "maoyan_nfrared_induction",false);
        isMaoyanSetMotionDetection = SharedPreUtil.getBoolean(mContext, "maoyan_motion_detection",false);
        maoyanSetVocality.setmSetSwitch(!isMaoyanVocality);
        maoyanSetShake.setmSetSwitch(!isMaoyanhake);
        maoyanSetDoorbell.setmSetSwitch(!isMaoyanDoorbell);
        maoyanSetNfraredInduction.setmSetSwitch(!isMaoyanNfraredInduction);
        maoyanSetMotionDetection.setmSetSwitch(!isMaoyanSetMotionDetection);


        maoyanSetVocality.setOnChangeListener(changeListener);
        maoyanSetShake.setOnChangeListener(changeListener);
        maoyanSetDoorbell.setOnChangeListener(changeListener);
        maoyanSetNfraredInduction.setOnChangeListener(changeListener);
        maoyanSetMotionDetection.setOnChangeListener(changeListener);
    }


    private SwitchButton.OnChangeListener changeListener = new SwitchButton.OnChangeListener() {

        @Override
        public void onChange(SwitchButton sb, boolean state) {
            switch (sb.getId()) {
                // 设置声音
                case R.id.maoyan_set_vocality:
                    isMaoyanVocality = !state;
                    break;
                case R.id.maoyan_set_shake:
                    isMaoyanhake = !state;
                    break;
                case R.id.maoyan_set_doorbell://感应门铃按键灯开关
                    isMaoyanDoorbell = !state;
                    break;
                case R.id.maoyan_set_nfrared_induction://红外感应开关
                    isMaoyanNfraredInduction = !state;
                    break;
                case R.id.maoyan_set_motion_detection://移动侦测开关
                    isMaoyanSetMotionDetection = !state;
                    break;
                case R.id.camera_siren_push://摄像头报警推送
                    isCameraCirenPush = !state;
                    break;
                case R.id.camera_motion_detection://摄像头移动侦测
                    isCameraMotionDetection = !state;
                    break;
                case R.id.camera_motion_detection_set://摄像头移动侦测灵敏度
                    isCameraMotionDetectionSet = !state;
                    break;
            }
        }
    };

    @OnClick({R.id.maoyan_set_Back, R.id.jv_save
//            , R.id.guard_time
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.maoyan_set_Back:
                finish();
                break;
//            case R.id.guard_time:
//                BaseUtils.startActivity(mContext, GuardTimeActivity.class);
//                break;
            case R.id.jv_save:
                if (type.equals("camera0")) {
                    cameraSave();
                } else if (type.equals("camera1")) {
                    cameraSave();
                } else if (type.equals("camera2")) {
                    maoyanSave();
                } else if (type.equals("on_off")) {
                    if (jvSetName.getText().toString() != null && !jvSetName.getText().toString().isEmpty()) {
                        onOff(jvSetName.getText().toString());
                    } else {
                        BaseUtils.showShortToast(mContext, "呢称不能为空");
                    }
                }
                break;
        }
    }

    private void onOff(String name) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", SharedPreUtil.getString(mContext, "userId", ""));
        params.put("flag", "update");
        params.put("num", id);
        params.put("name", name);
        OkHttp.get(mContext, ConnectPath.ADD_CAMERA, params,
                new ConciseStringCallback(mContext, new ConciseCallbackHandler<String>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        finish();
                    }
                }));
    }

    private void maoyanSave() {
        SharedPreUtil.put(mContext, "maoyan_vocality", isMaoyanVocality);
        SharedPreUtil.put(mContext, "maoyan_shake", isMaoyanhake);

        if (isMaoyanDoorbell) {
            PlayUtil.setStreamBellLightStatus(0, String.format(Locale.CHINA, "bBellLight=%d", 0));
        } else {
            PlayUtil.setStreamBellLightStatus(0, String.format(Locale.CHINA, "bBellLight=%d", 1));
        }
        SharedPreUtil.put(mContext, "maoyan_doorbell", isMaoyanDoorbell);

        if (isMaoyanNfraredInduction) {
            PlayUtil.setStreamPirEnable(0, String.format(Locale.CHINA, "bPirEnable=%d", 0));
        } else {
            PlayUtil.setStreamPirEnable(0, String.format(Locale.CHINA, "bPirEnable=%d", 1));
        }
        SharedPreUtil.put(mContext, "maoyan_nfrared_induction", isMaoyanNfraredInduction);

        if (isMaoyanSetMotionDetection) {
            PlayUtil.setStreamMDetect(0, String.format(Locale.CHINA, "bMDetect=%d", 0));
        } else {
            PlayUtil.setStreamMDetect(0, String.format(Locale.CHINA, "bMDetect=%d", 1));
        }
        SharedPreUtil.put(mContext, "maoyan_motion_detection", isMaoyanSetMotionDetection);
        if (jvSetName.getText().toString() != null && jvSetName.getText().toString().length() != 0) {
            updateCameraName(mContext, "update", id, jvSetName.getText().toString());
        } else {
            finish();
        }


    }

    private void cameraSave() {
        if (isCameraCirenPush) {
            JniUtil.setDevSafeState(0, 1);
        } else {
            JniUtil.setDevSafeState(0, 0);
        }
        if (isCameraMotionDetection) {
            JniUtil.setMotionDetection(0, 1);
        } else {
            JniUtil.setMotionDetection(0, 0);
        }
        if (isCameraMotionDetectionSet) {
            JniUtil.setMDSensitivity(0, 100);
        } else {
            JniUtil.setMDSensitivity(0, 0);
        }

        SharedPreUtil.put(mContext, "camera_siren_push", isCameraCirenPush);
        SharedPreUtil.put(mContext, "camera_motion_detection", isCameraMotionDetection);
        SharedPreUtil.put(mContext, "camera_motion_detection_set", isCameraMotionDetectionSet);
        if (jvSetName.getText().toString() != null && jvSetName.getText().toString().length() != 0) {
            updateCameraName(mContext, "update", id, jvSetName.getText().toString());
        } else {
            finish();
        }
    }


    //修改摄像头呢称
    @SuppressWarnings("static-access")
    public static void updateCameraName(final Context context, final String type, final String id, String name) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", SharedPreUtil.getString(context, "userId", ""));
        params.put("flag", type);
        params.put("num", id);
        if (type.equals("update")) {
            params.put("name", name);
        }
        OkHttp.get(mEhomeApplication.getmContext(), ConnectPath.ADD_CAMERA, params,
                new ConciseStringCallback(mEhomeApplication.getmContext(), new ConciseCallbackHandler<String>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent mIntent = new Intent(DoorFragment.ACTION_NAME);
                        mIntent.putExtra("yaner", "check");
                        mEhomeApplication.getmContext().sendBroadcast(mIntent);
                        if (type.equals("update")) {
                            final NormalDialog dialog = DialogShow.showSelectDialog(context, "修改成功", 1, new String[]{"确定"});
                            dialog.setOnBtnClickL(new OnBtnClickL() {

                                @Override
                                public void onBtnClick() {
                                    BaseUtils.startActivity(context, ActivityHomepage.class);
                                }

                            });
//							BaseUtils.showShortToast(mEhomeApplication.getmContext(),"修改成功");
                        } else if (type.equals("delete")) {
                            if (id.substring(0, 1).equals("h") || id.substring(0, 1).equals("H")) {
                                JVBase.delDev(id);
                            } else if (id.substring(0, 1).equals("c") || id.substring(0, 1).equals("C")) {
                                JVBase.delDev(id);
                            }
                            BaseUtils.showShortToast(mEhomeApplication.getmContext(), "删除成功");
                        }
                    }
                }));
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if ("".equals(SharedPreUtil.getString(mContext, "guard_time_start"))) {
//            guardTimeShow.setText("全天");
//        } else {
//            if ("全天".equals(SharedPreUtil.getString(mContext, "guard_time_start"))) {
//                guardTimeShow.setText(SharedPreUtil.getString(mContext, "guard_time_start"));
//            } else {
//                guardTimeShow.setText(SharedPreUtil.getString(mContext, "guard_time_start") + "-" + SharedPreUtil.getString(mContext, "guard_time_stop"));
//            }
//        }
    }
    //    public static void maoyanSet(){
//
//        PlayUtil.setStreamBellLightStatus(0,String.format(Locale.CHINA, "bBellLight=%d", 1));//感应门铃按键灯开关
//        PlayUtil.setStreamPirEnable(0,String.format(Locale.CHINA, "bPirEnable=%d", 1));//红外感应开关
//        PlayUtil.setStreamMDetect(0,String.format(Locale.CHINA, "bMDetect=%d", 1));//移动侦测开关
//    }

}
