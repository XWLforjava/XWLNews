package com.oushangfeng.ounews.share;

import java.io.File;  
import java.util.Date;
  
import android.content.ComponentName;  
import android.content.Context;  
import android.content.Intent;  
import android.content.pm.PackageManager;  
import android.content.pm.PackageManager.NameNotFoundException;  
import android.net.Uri;  
import android.text.TextUtils;  
import android.widget.Toast;

/**
 *分享到朋友圈接口：public void shareToWXCircle(String content, File file)
 * content 为文字，file 为图片（本地）地址生成的 File, 使用url 的接口未实现
 */

public class ShareUtil {  
    private Context context;  
    /**
	 *使用接口时在对应的activity中声明 shareUtil = new ShareUtil(this);
	 */
    public ShareUtil(Context context) {  
        this.context = context;  
    }  
	
	public static final String IMG_SAVE_PATH = "/storage/emulated/legacy/display-client/picture/";
    
	/**
	 *常用包名 package name
	 */	
    public static final String WEIXIN_PACKAGE_NAME = "com.tencent.mm";  
    public static final String QQ_PACKAGE_NAME = "com.tencent.mobileqq";  
	public static final String WEIBO_PACKAGE_NAME = "com.sina.weibo";
    /**
     * 常用类名 classname
      */
    public static final String WEIXIN_FRIEND_CLASSNAME = "com.tencent.mm.ui.tools.ShareImgUI";
	public static final String WEIXIN_FRIENDCIRCLE_CLASSNAME = "com.tencent.mm.ui.tools.ShareToTimeLineUI";
	public static final String QQ_FRIEND_CLASSNAME = "com.tencent.mobileqq.activity.JumpActivity";
    /**
     * 下载地址
     */
	public static final String WEIXIN_INSTALL_WEBVIEW = "http://weixin.qq.com/download";
	public static final String QQ_INSTALL_WEBVIEW = "http://im.qq.com/mobileqq/";
	
    /** 
     * 分享文字 
     * @param packageName 
     * @param content 
     * @param title 
     * @param subject 
     */  
    public void shareText(String packageName,String className,String content,String title,String subject){  
            Intent intent =new Intent();  
            intent.setAction(Intent.ACTION_SEND);  
            intent.setType("text/plain");  
            if(stringCheck(className) && stringCheck(packageName)){  
                ComponentName componentName = new ComponentName(packageName, className);  
                intent.setComponent(componentName);  
            }else if(stringCheck(packageName)){  
                intent.setPackage(packageName);  
            }  
              
            intent.putExtra(Intent.EXTRA_TEXT, content);  
            if(null != title && !TextUtils.isEmpty(title)){           
                intent.putExtra(Intent.EXTRA_TITLE, title);  
            }  
            if(null != subject && !TextUtils.isEmpty(subject)){  
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);  
            }  
            intent.putExtra(Intent.EXTRA_TITLE, title);  
            Intent chooserIntent = Intent.createChooser(intent, "分享到：");  //dialogTitle
            context.startActivity(chooserIntent);  
        }  
	/**	
	 *前两个参数可用于直接指定分享方向，专用于分享给好友时，第二个参数须设置classname，均置null则列表出现所有，下同
	 *使用已经定义的常量来进行指定packagename和classname
     *除content外均可省略
	 *下面为直接调用原生分享接口，等同于上面方法前两个参数置null
	public void shareTextOrigin(String contentTitle,String contentSubject,String content){
			Intent share_intent = new Intent();
			share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
			share_intent.setType("text/plain");//设置分享内容的类型
			if(null != title && !TextUtils.isEmpty(title)){           
                intent.putExtra(Intent.EXTRA_TITLE, title);  
            }  
            if(null != subject && !TextUtils.isEmpty(subject)){  
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);  
            }//添加分享内容title以及subject，可以不填
			share_intent.putExtra(Intent.EXTRA_TEXT, content);//添加分享内容
			//创建分享的Dialog
			share_intent = Intent.createChooser(share_intent, "分享到：");//dialogTitle
			activity.startActivity(share_intent);
		}
	 */	
		
    /** 
     * 分享网页 
     */  
    public void shareUrl(String packageName,String className,String content,String title,String subject){  
        Intent intent =new Intent();  
        intent.setAction(Intent.ACTION_SEND);  
        intent.setType("text/plain");   
        if(stringCheck(className) && stringCheck(packageName)){  
            ComponentName componentName = new ComponentName(packageName, className);  
            intent.setComponent(componentName);  
        }else if(stringCheck(packageName)){  
            intent.setPackage(packageName);  
        }  
          
        intent.putExtra(Intent.EXTRA_TEXT, content);  
        if(null != title && !TextUtils.isEmpty(title)){           
            intent.putExtra(Intent.EXTRA_TITLE, title);  
        }  
        if(null != subject && !TextUtils.isEmpty(subject)){  
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);  
        }  
        intent.putExtra(Intent.EXTRA_TITLE, title);  
        Intent chooserIntent = Intent.createChooser(intent, "分享到：");  
        context.startActivity(chooserIntent);  
    }  
    
    /** 
     * 分享图片 
	 * 所有的File为图片/音频/视频对应路径生成的文件，（在收藏缓存中找？）
     */  
    public void shareImg(String packageName,String className,File file){  
        if(file.exists()){  
            Uri uri = Uri.fromFile(file);  
            Intent intent = new Intent();  
            intent.setAction(Intent.ACTION_SEND);  
            intent.setType("image/*");  
            if(stringCheck(packageName) && stringCheck(className)){  
                intent.setComponent(new ComponentName(packageName, className));  
            }else if (stringCheck(packageName)) {  
                intent.setPackage(packageName);  
            }  
            intent.putExtra(Intent.EXTRA_STREAM, uri);  
            Intent chooserIntent = Intent.createChooser(intent, "分享到:");  
            context.startActivity(chooserIntent);  
        }else {  
            Toast.makeText(context, "文件不存在", Toast.LENGTH_LONG).show();
        }  
    }  
      
    /**  
     * 分享音乐  
     */  
    public void shareAudio(String packageName,String className,File file){  
        if(file.exists()){  
            Uri uri = Uri.fromFile(file);  
            Intent intent = new Intent();  
            intent.setAction(Intent.ACTION_SEND);  
            intent.setType("audio/*");  
            if(stringCheck(packageName) && stringCheck(className)){  
                intent.setComponent(new ComponentName(packageName, className));  
            }else if (stringCheck(packageName)) {  
                intent.setPackage(packageName);  
            }  
            intent.putExtra(Intent.EXTRA_STREAM, uri);  
            Intent chooserIntent = Intent.createChooser(intent, "分享到:");  
            context.startActivity(chooserIntent);  
        }else {  
            Toast.makeText(context, "文件不存在", Toast.LENGTH_LONG).show();
        }  
    }  
      
    /**  
     * 分享视频  
     */  
    public void shareVideo(String packageName,String className,File file){  
        setIntent("video/*", packageName, className, file);  
    }  
      
    public void setIntent(String type,String packageName,String className,File file){  
        if(file.exists()){  
            Uri uri = Uri.fromFile(file);  
            Intent intent = new Intent();  
            intent.setAction(Intent.ACTION_SEND);  
            intent.setType(type);  
            if(stringCheck(packageName) && stringCheck(className)){  
                intent.setComponent(new ComponentName(packageName, className));  
            }else if (stringCheck(packageName)) {  
                intent.setPackage(packageName);  
            }  
            intent.putExtra(Intent.EXTRA_STREAM, uri);  
            Intent chooserIntent = Intent.createChooser(intent, "分享到:");  
            context.startActivity(chooserIntent);  
        }else {  
            Toast.makeText(context, "文件不存在", Toast.LENGTH_LONG).show();
        }  
    }  
       
    /**  
     * 分享多张图片和文字至朋友圈  
     * @param title 即文字部分
     * @param packageName  
     * @param className  
     * @param file 图片文件  
     */  
    public void shareImgToWXCircle(String title,String packageName,String className, File file){  
        if(file.exists()){  
            Uri uri = Uri.fromFile(file);  
            Intent intent = new Intent();  
            ComponentName comp = new ComponentName(packageName, className);  
            intent.setComponent(comp);  
            intent.setAction(Intent.ACTION_SEND);  
            intent.setType("image/*");  
            intent.putExtra(Intent.EXTRA_STREAM, uri);  
            intent.putExtra("Kdescription", title);  
            context.startActivity(intent);  
        }else{  
            Toast.makeText(context, "文件不存在", Toast.LENGTH_LONG).show();  
        }  
          
          
    }  
	
	public void shareToWXCircle(String content, File file){
		if (checkInstall(WEIXIN_PACKAGE_NAME)) {  
                shareImgToWXCircle(content, WEIXIN_PACKAGE_NAME,  
                    WEIXIN_FRIENDCIRCLE_CLASSNAME, file);  
            } else {  
                toInstallWebView("http://weixin.qq.com/download");  
            }  
	}	
	
    /**  
     * 是否安装分享app  
     * @param packageName  
     */  
    public boolean checkInstall(String packageName){  
        try {  
            context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);  
            return true;  
        } catch (NameNotFoundException e) {  
            e.printStackTrace();  
            Toast.makeText(context, "请先安装应用app", Toast.LENGTH_LONG).show();
            return false;  
        }  
    }  
      
    /** 
     * 跳转官方安装网址
	 * 用于提醒安装相应应用
     */  
    public void toInstallWebView(String url){  
        Intent intent = new Intent();  
        intent.setAction(Intent.ACTION_VIEW);  
        intent.setData(Uri.parse(url));  
        context.startActivity(intent);  
    }  
      
    public static boolean stringCheck(String str){  
        if(null != str && !TextUtils.isEmpty(str)){  
            return true;  
        }else {  
            return false;  
        }  
    }  
}