//package com.cardinfolink.qrscanner.view;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Rect;
//import android.text.TextUtils;
//import android.util.AttributeSet;
//import android.util.Log;
//
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.BinaryBitmap;
//import com.google.zxing.DecodeHintType;
//import com.google.zxing.MultiFormatReader;
//import com.google.zxing.PlanarYUVLuminanceSource;
//import com.google.zxing.Result;
//import com.google.zxing.ResultPoint;
//import com.google.zxing.ResultPointCallback;
//import com.google.zxing.common.HybridBinarizer;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.EnumMap;
//import java.util.Map;
//
//import cn.bingoogolapple.qrcode.core.CameraPreview;
//import cn.bingoogolapple.qrcode.core.DecodeFormatManager;
//import cn.bingoogolapple.qrcode.core.QRCodeDecoder;
//import cn.bingoogolapple.qrcode.core.QRCodeView;
//import cn.bingoogolapple.qrcode.core.ScanBoxView;
//import cn.bingoogolapple.qrcode.core.ScanResult;
//import opencv.ImagePreProcess;
//import zbar.ZbarController;
//
//
///**
// * Copyright (C)
// *
// * @author : gongcb
// * @date : 2019/7/9 9:05 AM
// * @desc :
// */
//public class ScanView extends QRCodeView {
//
//    private static final String TAG = ScanBoxView.class.getSimpleName();
//
//    private MultiFormatReader multiFormatReader;
//
//    public ScanView(Context context, AttributeSet attributeSet) {
//        super(context, attributeSet);
//    }
//
//    @Override
//    protected void setupReader() {
//        Log.i(TAG,"setupReader");
//
//        Map<DecodeHintType, Object> hints = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
//
//        Collection<BarcodeFormat> decodeFormats = new ArrayList<BarcodeFormat>();
//        decodeFormats.addAll(DecodeFormatManager.getBarCodeFormats());
//        decodeFormats.addAll(DecodeFormatManager.getQrCodeFormats());
//
//        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
//        hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK, new ResultPointCallback() {
//            @Override
//            public void foundPossibleResultPoint(ResultPoint point) {
//            }
//        });
//
//        multiFormatReader = new MultiFormatReader();
//        multiFormatReader.setHints(hints);
//
//    }
//
//    @Override
//    protected ScanResult processData(byte[] data, int width, int height, boolean isRetry) {
//        Log.i(TAG,"processData");
//
//        CameraPreview cameraPreview = getCameraPreview();
//        int cameraWidth = cameraPreview.getCameraResolution().x;
//        int cameraHeight = cameraPreview.getCameraResolution().y;
//
//        Rect mCropRect = mScanBoxView.getScanBoxAreaRect(cameraWidth, cameraHeight);
//        Result result = decode(data,width,height,null,true);
//
//        if (result == null) {
//            return null;
//        }
//
//        if (TextUtils.isEmpty(result.getText())) {
//            return null;
//        }
//
//        return new ScanResult(result.getText());
//    }
//
//    @Override
//    protected ScanResult processBitmapData(Bitmap bitmap) {
//        Log.i(TAG,"processBitmapData");
//        return new ScanResult(QRCodeDecoder.syncDecodeQRCode(bitmap));
//    }
//
//
//    private byte[] getMatrix(byte[] src, int oldWidth, int oldHeight, Rect rect) {
//        byte[] matrix = new byte[rect.width() * rect.height() * 3 / 2];
//        ImagePreProcess.getYUVCropRect(src, oldWidth, oldHeight, matrix, rect.left, rect.top, rect.width(), rect.height());
//        return matrix;
//    }
//
//    private static byte[] rotateYUV420Degree90(byte[] data, int imageWidth, int imageHeight) {
//        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
//        int i = 0;
//        for (int x = 0; x < imageWidth; x++) {
//            for (int y = imageHeight - 1; y >= 0; y--) {
//                yuv[i] = data[y * imageWidth + x];
//                i++;
//            }
//        }
//        i = imageWidth * imageHeight * 3 / 2 - 1;
//        for (int x = imageWidth - 1; x > 0; x = x - 2) {
//            for (int y = 0; y < imageHeight / 2; y++) {
//                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + x];
//                i--;
//                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth)
//                        + (x - 1)];
//                i--;
//            }
//        }
//        return yuv;
//    }
//
//    public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {
//        Rect rect = new Rect(0, 0, width, height);
//        return new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top, rect.width(), rect
//                .height(), false);
//    }
//
//    private Result decode(byte[] data, int width, int height, Rect cropRect, boolean needRotate90) {
//        // 这里需要将获取的data翻转一下，因为相机默认拿的的横屏的数据
//        byte[] rotatedData = needRotate90 ? rotateYUV420Degree90(data, width, height) : data;
//
//        if (needRotate90) {
//            // 宽高也要调整
//            int tmp = width;
//            width = height;
//            height = tmp;
//        }
//
//        //截取scan的选择框中数据
//        byte[] processSrc = rotatedData;
//        int processWidth = width;
//        int processHeight = height;
//        if (cropRect != null) {
//            processWidth = cropRect.width();
//            processHeight = cropRect.height();
//
//            if (processWidth % 6 != 0) {
//                processWidth -= processWidth % 6;
//
//                cropRect.right = cropRect.left + processWidth;
//            }
//            if (processHeight % 6 != 0) {
//                processHeight -= processHeight % 6;
//                cropRect.bottom = cropRect.top + processHeight;
//            }
//
//            processSrc = getMatrix(rotatedData, width, height, cropRect);
//        }
//
//        Result rawResult = null;
//
//        //1.使用zxing
//        PlanarYUVLuminanceSource source = buildLuminanceSource(processSrc, processWidth, processHeight);
//        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));//new GlobalHistogramBinarizer(source)
//        try {
//            rawResult = multiFormatReader.decodeWithState(bitmap);
//            Log.v(TAG, "zxing success");
//        } catch (Exception re) {
//            re.printStackTrace();
//        } finally {
//            multiFormatReader.reset();
//        }
//
//        //2.使用zbar
//        if (rawResult == null) {
//            String zbarResult = ZbarController.getInstance().scan(processSrc, processWidth, processHeight);
//            if (zbarResult != null) {
//                rawResult = new Result(zbarResult, null, null, null);
//                Log.v(TAG, "zbar success");
//            }
//        }
//
//        if (rawResult == null) {
//            byte[] processData = new byte[processWidth * processHeight * 3 / 2];
//            ImagePreProcess.preProcess(processSrc, processWidth, processHeight, processData);
//
////            if (isDebugMode) {
////                lastPreviewData = processSrc;
////                lastPreProcessData = processData;
////                lastPreProcessWidth = processWidth;
////                lastPreProcessHeight = processHeight;
////            }
//
//            //3. opencv+zxing
//            source = buildLuminanceSource(processData, processWidth, processHeight);
//            bitmap = new BinaryBitmap(new HybridBinarizer(source));
//            try {
//                rawResult = multiFormatReader.decodeWithState(bitmap);
//                Log.v(TAG, "zxing success with opencv");
//            } catch (Exception re) {
//                re.printStackTrace();
//                // continue
//            } finally {
//                multiFormatReader.reset();
//            }
//
//            //4. opencv+zbar
//            if (rawResult == null) {
//                String zbarResult = ZbarController.getInstance().scan(processData, processWidth, processHeight);
//                if (zbarResult != null) {
//                    rawResult = new Result(zbarResult, null, null, null);
//                    Log.v(TAG, "zbar success with opencv");
//                }
//            }
//        }
//
//        return rawResult;
//    }
//
//
//}
