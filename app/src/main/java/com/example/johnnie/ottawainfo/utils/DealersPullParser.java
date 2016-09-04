package com.example.johnnie.ottawainfo.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.example.johnnie.ottawainfo.R;
import com.example.johnnie.ottawainfo.model.DealerModel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnnie on 2016-08-23.
 */
public class DealersPullParser {

    private static final String LOGTAG = "XMLPULLPARSER";
    private static final String DEALER_ADDRESS = "address";
    private static final String DEALER_CATEGORY = "category";
    private static final String DEALER_NAME = "name";
    private static final String DEALER_INFO = "information";
    private static final String DEALER_FAQ = "faq";
    private static final String DEALER_IMG = "imageUri";

    private DealerModel currentDealer = null;
    private String currentTag = null;
    List<DealerModel> dealers = new ArrayList<>();

    public List<DealerModel> parseXML(Context context){


        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            InputStream stream = context.getResources().openRawResource(R.raw.dealers);
            xpp.setInput(stream, null);

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT){
                if (eventType == XmlPullParser.START_TAG){
                    handleStartTag(xpp.getName());
                } else if (eventType == XmlPullParser.END_TAG){
                    currentTag = null;
                } else if(eventType == XmlPullParser.TEXT){
                     handleText(xpp.getText());
                }
                eventType = xpp.next();
            }


        } catch (Resources.NotFoundException e){
            Log.d(LOGTAG, e.getMessage());
        } catch (XmlPullParserException e) {
            Log.d(LOGTAG, e.getMessage());
        } catch (IOException e) {
            Log.d(LOGTAG, e.getMessage());
        }

        return dealers;
    }

    private  void handleText(String text){
        String xmlText = text;
        if ( currentDealer != null && currentTag != null){
            if(currentTag.equals(DEALER_ADDRESS)){
                currentDealer.setAddress(xmlText);
            }else if(currentTag.equals(DEALER_NAME)){
                currentDealer.setName(xmlText);
            }else if(currentTag.equals(DEALER_CATEGORY)){
                currentDealer.setCategory(xmlText);
            }else if(currentTag.equals(DEALER_INFO)){
                currentDealer.setInformation(xmlText);
            }else if(currentTag.equals(DEALER_FAQ)){
                currentDealer.setFAQ(xmlText);
            }else if(currentTag.equals(DEALER_IMG)){
                currentDealer.setImageUri(xmlText);
            }
        }
    }


    private void handleStartTag(String name){
        if (name.equals("dealer")){
            currentDealer = new DealerModel();
            dealers.add(currentDealer);
        }else {
            currentTag = name;
        }
    }
}
