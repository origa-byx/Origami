package com.safone.utils;

/**
 * @by: origami
 * @date: {2021/5/6}
 * @info:
 **/
public class XmlUtil {

    private boolean withHead;

    private StringBuilder builder;

    private XmlUtil(boolean withHead){
        if(withHead) {
            builder = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<message_content>\n");
        }else {
            builder = new StringBuilder("");
        }
        this.withHead = withHead;
    }

    public static XmlUtil getInstance(boolean withHead){
        return new XmlUtil(withHead);
    }

    public XmlUtil add_key_value(String key, Object value){
        builder
                .append("<").append(key).append(">")
                .append(value)
                .append("</").append(key).append(">\n");
        return this;
    }

    public XmlUtil add_key_xmlValue(String key, XmlUtil value){
        builder
                .append("<").append(key).append(">\n")
                .append(value.getString())
                .append("</").append(key).append(">\n");
        return this;
    }

    public String getString(){
        if(withHead){
            builder.append("</message_content>");
        }
        return builder.toString();
    }

}
