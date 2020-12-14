package com.example.speechtotext;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductTool implements Serializable {

    private Map<String,String> product_id;
    private Map<String,String> tool_type;
    private ArrayList<String> taskList;
    private ArrayList<String> locationList;

    public ProductTool(HashMap<String,String> product, ArrayList<String> task, ArrayList<String> location,HashMap<String,String> tool){
        product_id = new HashMap<>(product);
        taskList = new ArrayList<>(task);
        locationList = new ArrayList<>(location);
        tool_type = new HashMap<>(tool);
    }

    public String getProductId(String product_name){
        if(product_id.containsKey(product_name)){
            return product_id.get(product_name);
        }else{
            return "";
        }
    }

    public String[] getProduct() {
        String[] strs = product_id.keySet().toArray(new String[product_id.size()]);
        return strs;
    }

    public String[] getTool() {
        String[] strs = tool_type.keySet().toArray(new String[tool_type.size()]);
        return strs;
    }

    public String getToolType(String toolName){
        if (tool_type.containsKey(toolName)){
            return tool_type.get(toolName);
        }else{
            return "其他";
        }
    }

    public String[] getTask(){
        return taskList.toArray(new String[taskList.size()]);
    }

    public String[] getLocation(){
        return locationList.toArray(new String[locationList.size()]);
    }
}