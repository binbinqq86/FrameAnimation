package com.binbin.testas;

import android.app.Activity;

/**
 * Created by : tb on 2017/9/27 上午11:16.
 * Description :
 */
//主页目录列表项
public class Catalog {
    
    // 条目名称
    public String name;
    // 点击条目要跳转到的目标Activity的类型
    public Class<? extends Activity> cls;
    
    // 写个带参数的构造方法，方便赋值使用。
    public Catalog(String name, Class<? extends Activity> cls) {
        this.name = name;
        this.cls = cls;
    }
    
    // ListView使用ArrayAdapter<T>时每个列表项显示的内容就是T的toString方法返回的值，
    // 如果这里不重写则会调用父类Object的toString方法。
    @Override
    public String toString() {
        return name;
    }
}
