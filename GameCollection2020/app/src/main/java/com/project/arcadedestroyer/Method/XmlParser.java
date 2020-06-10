package com.project.arcadedestroyer.Method;

import android.util.Log;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class XmlParser {

    private XmlPullParserFactory fc;
    private XmlPullParser parser;

    public XmlParser(InputStream path) throws XmlPullParserException, FileNotFoundException {
        fc=XmlPullParserFactory.newInstance();
        parser= fc.newPullParser();
        parser.setInput(path, "UTF-8");
    }

    public ArrayList<Item> readFile() throws IOException, SAXException, XmlPullParserException {
        ArrayList<Item> list_item = new ArrayList<>();
        while (parser.getEventType()!=XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType()==XmlPullParser.START_TAG) {
                if (parser.getName().equals("item")) {
                    list_item.add(new Item(parser.getAttributeValue(0),parser.getAttributeValue(1)));
                }
            }
            parser.next();
        }
        Log.d("item",list_item.get(0).getImages());
        return list_item;
    }
}
