package com.moonleaper.timetogo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.StrictMode;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;





public class WeatherAnalysis extends Service{
    public String temperature;
    public String wind;
    public boolean rain;


    public int property;

    public static final String PROPERTY = "property";

    private static final String NAMESPACE = "http://WebXml.com.cn/";
    private static String URL = "http://www.webxml.com.cn/webservices/weatherwebservice.asmx";
    private static final String METHOD_NAME = "getWeatherbyCityName";
    private static String SOAP_ACTION = "http://WebXml.com.cn/getWeatherbyCityName";

    private String weatherToday;
    private SoapObject detail;
    //private final IBinder mBinder = new LocalBinder();

    /*public class LocalBinder extends Binder {
         WeatherAnalysis getService(){
             return WeatherAnalysis.this;
         }
    } */

    @Override
    public IBinder onBind(Intent intent) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
        //设置相关的虚拟机策略
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                        //.detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
        getWeather("南京");
        /*Intent intent = new Intent(WeatherAnalysis.this,TodoListManagerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(PROPERTY, property);
        startActivity(intent);*/
    }



    @Override
    public void onDestroy() {
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void getWeather(String cityName){
        try{
            System.out.println("rpc------");
            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            System.out.println("rpc " + rpc);
            System.out.println("cityName is " + cityName);
            rpc.addProperty("theCityName ", cityName);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE ht = new HttpTransportSE(URL);
            //AndroidHttpTransport ht = new AndroidHttpTransport(URL);
            ht.debug = true;
            ht.call(SOAP_ACTION, envelope);
            //ht.call(null, envelope);
            //SoapObject result = (SoapObject)envelope.bodyIn;
            //detail = (SoapObject) result.getProperty("getWeatherbyCityNameResult");
            detail =(SoapObject) envelope.getResponse();

            //System.out.println("result" + result);
            System.out.println("detail" + detail);

            parseWeather(detail);
            property = analysisWeather(detail);
            System.out.println("Property: " + property);

            return;

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void parseWeather(SoapObject detail) throws UnsupportedEncodingException {
        String date = detail.getProperty(6).toString();
        String if_rain = detail.getProperty(8).toString();
        weatherToday = "今天：" + date.split(" ")[0];
        rain = if_rain.contains("雨");
        weatherToday = weatherToday + "\n天气：" + date.split(" ")[1];
        weatherToday = weatherToday + "\n气温：" + detail.getProperty(5).toString();
        temperature = detail.getProperty(5).toString();
        weatherToday = weatherToday + "\n风力："+ detail.getProperty(7).toString() + "\n";
        wind = detail.getProperty(7).toString();
        System.out.println("weatherToday is " + weatherToday);
    }

    public int analysisWeather(SoapObject detail){
        int property = 0;
        String rain = detail.getProperty(6).toString();
        String wind = detail.getProperty(7).toString();
        if(rain.contains("雨")){
            if(rain.contains("暴") || rain.contains("大") || rain.contains("中")) property = 2;
            else property = 1;
        }
        else if(rain.contains("雪")){
            if(rain.contains("暴") || rain.contains("大")) property = 2;
            else property = 1;
        }
        else if(rain.contains("冰")){
            if(rain.contains("冰雹")) property = 2;
            else property = 1;
        }
        else if(rain.contains("雾")||rain.contains("烟")||rain.contains("沙尘暴")){
            if(rain.contains("霾")||rain.contains("大雾")||rain.contains("浓雾")||rain.contains("沙尘暴")) property = 2;
            else property = 1;
        }
        else
            property = 0;

        Pattern p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(wind);
        while (m.find()) {
            //System.out.println(m.group());
            if(Integer.parseInt(m.group())>6){
                if(Integer.parseInt(m.group())>10)
                    property = 2;
                else
                    property = 1;
            }

        }

        return property;
    }


}


