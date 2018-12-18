package com.bbel.eatnow.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.bbel.eatnow.R;
import com.bbel.eatnow.bean.PathItem;
import com.bbel.eatnow.utils.PathParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class MapView extends View {

    private Paint mPaint;

    //手势监听器
//    private GestureDetector mDetector;
    //缩放系数
    private float scale = 1f;
    //保存path对象
    private List<PathItem> pathItems = new ArrayList<>();

    private String TAG = "MapView";

    public MapView(Context context) {
        this(context, null);
    }

    public MapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(R.raw.rose_map);
    }

    public void setChoose(String name, int index) {
        init(index);
        for (PathItem pathItem : pathItems) {
            if (pathItem.getName().equals(name)) {
                pathItem.setSelect(true);
            } else {
                pathItem.setSelect(false);
            }
        }
        invalidate();
    }

    private void init(int index) {
        pathItems.clear();
        //关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

//        mDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
//            @Override
//            public boolean onDown(MotionEvent e) {
//                float x = e.getX() / scale;
//                float y = e.getY() / scale;
////                float x = e.getX();
////                float y = e.getY();
//                for (PathItem pathItem : pathItems) {
//                    if (pathItem.isTouch((int) x, (int) y)) {
//                        pathItem.setSelect(true);
//                    } else {
//                        pathItem.setSelect(false);
//                    }
//                }
//                invalidate();
//                return true;
//            }
//        });
//        parserPaths();
        pathItems = getPathItems(index);
    }


    /**
     * 解析path
     */
    private void parserPaths() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 创建DOM工厂对象
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                try {
                    // DocumentBuilder对象
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    //打开输入流
                    InputStream is = getResources().openRawResource(R.raw.rose_map);
//                    getResources().ope
                    // 获取文档对象
                    Document doc = db.parse(is);
                    //获取path元素节点集合
                    NodeList paths = doc.getElementsByTagName("path");
                    PathItem item;
                    for (int i = 0; i < paths.getLength(); i++) {
                        // 取出每一个元素
                        Element personNode = (Element) paths.item(i);
                        //得到android:pathData属性值
                        String nodeValue = personNode.getAttribute("android:pathData");
                        String name = personNode.getAttribute("android:Id");

                        //解析，并创建pathItem
                        item = new PathItem(PathParser.createPathFromPathData(nodeValue));
                        item.setName(name);
                        pathItems.add(item);
                    }
                    Log.e(TAG, "itemsCount  " + pathItems.size());
                } catch (Exception e) {
                    Log.e(TAG, "解析出错 ");
                }
            }
        }).start();
        postInvalidate();
    }


    private void createPathsFromFile(int index) {
        Observable.just(index)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .map(new Function<Integer, List<PathItem>>() {
                    @Override
                    public List<PathItem> apply(Integer integer) throws Exception {
//                        pathItems
                        return null;
                    }
                })
                .subscribe(new Observer<List<PathItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<PathItem> items) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        return mDetector.onTouchEvent(event);
        return false;
    }



    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.scale(scale, scale);
        for (PathItem pathItem : pathItems) {
            pathItem.draw(canvas, mPaint);
        }
        canvas.restore();
    }


    private List<PathItem> getPathItems(int index) {
        List<PathItem> pathItems = new ArrayList<>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // DocumentBuilder对象
            DocumentBuilder db = dbf.newDocumentBuilder();
            //打开输入流
            InputStream is = getResources().openRawResource(index);
//                    getResources().ope
            // 获取文档对象
            Document doc = db.parse(is);
            //获取path元素节点集合
            NodeList paths = doc.getElementsByTagName("path");
            PathItem item;
            for (int i = 0; i < paths.getLength(); i++) {
                // 取出每一个元素
                Element personNode = (Element) paths.item(i);
                //得到android:pathData属性值
                String nodeValue = personNode.getAttribute("android:pathData");
                String name = personNode.getAttribute("android:Id");
                //解析，并创建pathItem
                item = new PathItem(PathParser.createPathFromPathData(nodeValue));
                item.setName(name);
//                Log.d(TAG, item.getName());
                pathItems.add(item);
            }
            Log.e(TAG, "itemsCount  " + pathItems.size());
        } catch (Exception e) {
            Log.e(TAG, "解析出错 ");
        }
        return pathItems;
    }


}

