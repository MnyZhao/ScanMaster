//package com.cardinfolink.qrscanner;
//
//import android.os.Bundle;
//import android.os.Vibrator;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//
//import com.cardinfolink.qrscanner.view.ScanView;
//
//import cn.bingoogolapple.qrcode.core.QRCodeView;
//import cn.bingoogolapple.qrcode.core.ScanBoxView;
//
///**
// * Copyright (C)
// *
// * @author : gongcb
// * @date : 2019/7/8 8:58 PM
// * @desc :
// */
//public class ScanActivity extends AppCompatActivity implements QRCodeView.Delegate{
//
//    private ScanView mScanView;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_scan);
//
//        mScanView = (ScanView) findViewById(R.id.scanView);
//
//        ScanBoxView scanBoxView = mScanView.getScanBoxView();
//        scanBoxView.setMaskColor(getResources().getColor(R.color.viewfinder_mask));
//
//        mScanView.setDelegate(this);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mScanView.startCamera();
//        mScanView.startSpotAndShowRect();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mScanView.stopCamera();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mScanView.onDestroy();
//    }
//
//    /**
//     * 扫码成功震动
//     */
//    private void vibrate() {
//        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//        vibrator.vibrate(200);
//    }
//
//
//    /**
//     * 处理扫描结果
//     *
//     * @param result 摄像头扫码时只要回调了该方法 result 就一定有值，不会为 null。解析本地图片或 Bitmap 时 result 可能为 null
//     */
//    @Override
//    public void onScanQRCodeSuccess(String result) {
//        vibrate();
//        mScanView.startSpot();
//    }
//
//    /**
//     * 摄像头环境亮度发生变化
//     *
//     * @param isDark 是否变暗
//     */
//    @Override
//    public void onCameraAmbientBrightnessChanged(boolean isDark) {
//
//    }
//
//    /**
//     * 处理打开相机出错
//     */
//    @Override
//    public void onScanQRCodeOpenCameraError() {
//
//    }
//}
