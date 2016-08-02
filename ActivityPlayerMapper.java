package com.console.sync.mapper;

import java.sql.*;
import java.util.*;

/**
 * Created by dell_2 on 2016/8/2.
 */
public class ActivityPlayerMapper {

    public Map<String,Object> queryTableDatas(String tableName,Connection con, Integer start, Integer size) throws SQLException {
        Statement statement = con.createStatement();
        ResultSet res = statement.executeQuery("SELECT * FROM "+tableName+"  limit "+start +","+size);
        ResultSetMetaData m = res.getMetaData();
        int columns=m.getColumnCount();
        Map<String,Object> map = new HashMap<>();
        map.put("tableName",tableName);
        List<String> keys = new ArrayList<>(columns);
        //存key 和对应的下标 以设置Column和对应的值是相同下标
        Map<String,Integer> keyMap = new HashMap<>();
        List<Object[]> valueLists = new LinkedList<>();
        map.put("keys",keys);
        map.put("valueList",valueLists);
        while(res.next()){
            Object[] values = new Object[columns];
            for(int i=1;i<=columns;i++){
                String key = m.getColumnName(i);
                if(!keyMap.containsKey(key)){
                    keyMap.put(key,keys.size());
                    keys.add(key);
                }
                values[keyMap.get(key)] = res.getObject(key);
            }
            valueLists.add(values);
        }
        return map;
    }

    public List<String> existsRecorduserinout(List<String> list2, Connection con) throws SQLException {
        List<String>  tables = new LinkedList<>();
        StringBuffer buffer = new StringBuffer(" select `TABLE_NAME` from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA='recorduserinout' and `TABLE_NAME` in (");
        for (String s : list2) {
            buffer.append("'");
            buffer.append(s);
            buffer.append("'");
            buffer.append(",");
        }
        buffer.deleteCharAt(buffer.length()-1);
        buffer.append(")");
        Statement statement = con.createStatement();
        ResultSet res = statement.executeQuery(buffer.toString());
        while(res.next()){
            tables.add(res.getString("TABLE_NAME"));
        }
        return tables;
    }
}
