package com.project.my.inz.app.help;

import android.util.Log;

import com.project.my.inz.Model.DataModel;
import com.project.my.inz.Model.QuestM;
import com.project.my.inz.adapter.AchiveArrayAdapter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Luki on 2014-07-03.
 */
public class XmlLoader {
    // All static variables
    static final String URL = "http://inzquest.url.ph/quest.xml";
    // XML node keys
    static final String KEY_TASK = "task"; // parent node
    static final String KEY_ID = "id";
    static final String KEY_NAME = "name";
    static final String KEY_DESC = "desc";
    static final String KEY_POINTS = "points";


    public List<DataModel> LoadXML() {

        List<DataModel> entries = new ArrayList<DataModel>();
        XMLParser parser = new XMLParser();
        String xml = parser.getXmlFromUrl(URL); // getting XML from URL
        Document doc = parser.getDomElement(xml); // getting DOM element

        NodeList nl = doc.getElementsByTagName(KEY_TASK);

        for (int i = 0; i < nl.getLength(); i++) {
            Element e = (Element) nl.item(i);
            entries.add(new QuestM(parser.getValue(e, KEY_ID),
                    parser.getValue(e, KEY_NAME),
                    parser.getValue(e, KEY_DESC),
                    parser.getValue(e, KEY_POINTS),
                    Long.toString(System.currentTimeMillis()),//date
                    "20"  ));//state
        }


        return entries;

    }

}
