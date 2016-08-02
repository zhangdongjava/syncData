package com.console.sync.service.impl;

import com.console.mapper.accounts.UserAccountInfoMapper;
import com.console.pojo.UserAccountOrder;
import com.console.service.UserAccountInfoService;
import com.console.sync.mapper.ActivityPlayerMapper;
import com.console.sync.service.DataSyncService;
import com.console.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by dell_2 on 2016/8/2.
 */
@Service
public class DataSyncServiceImpl implements DataSyncService {

    ActivityPlayerMapper activityPlayerMapper  = new ActivityPlayerMapper();

    @Autowired
    private UserAccountInfoMapper userAccountInfoMapper;

    @Override
    public String activityPlayerSync(String activityPlayerIp, String activityPlayerUser, String activityPlayerPwd, String beginTime, String endTime) throws Exception {
        userAccountInfoMapper.truncateActivityTables();
        Connection con =  getConnection(activityPlayerIp,activityPlayerUser,activityPlayerPwd);
        try {
            //同步活动用户表数据
            String[] tableNames = {"qpaccountsdb.accountsinfo",
                    "qptreasuredb.sharedetailinfo",
                    "qpaccountsdb.accountsinfoext",
                    "qpaccountsdb.accountsbindinfo",
                    "qptreasuredb.gamescoreinfo"};
            for (String tableName : tableNames) {
                syncTable(con,tableName);
            }
            //同步进出记录
            List<String> tns = getRecordUserInOutTableNames(beginTime, endTime,con);
            if(tns!=null){
                userAccountInfoMapper.dropRecorduserinoutTable(tns);
                userAccountInfoMapper.createRecorduserinoutTable(tns);
                for (String tableName : tns) {
                    syncTable(con,"recorduserinout."+tableName);
                }
            }


        }finally {
            con.close();
        }

        return "success";
    }

    /**
     * 同步table表数据
     * @param con
     * @throws SQLException
     */
    private void syncTable(Connection con,String tableName) throws SQLException {
        int size = 200;
       Map<String, Object> map = activityPlayerMapper.queryTableDatas(tableName,con,0,size);
        boolean b = ((List<Object[]>)map.get("valueList")).size()>0;
        for (int i=1;b;i++){
            userAccountInfoMapper.insetAccountsinfo(map);
            map = activityPlayerMapper.queryTableDatas(tableName,con,i*size,size);
            b = ((List<Object[]>)map.get("valueList")).size()>0;
        }

    }


    private Connection getConnection(String activityPlayerIp, String activityPlayerUser, String activityPlayerPwd) throws SQLException {
        Connection con;
        con = DriverManager.getConnection("jdbc:mysql://"+activityPlayerIp+"/recorduserinout?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true",activityPlayerUser,activityPlayerPwd);
        return con;
    }

    public static void main(String[] args) throws Exception {
        new DataSyncServiceImpl().activityPlayerSync("119.147.137.167","admin","buyu!@#","","");
    }


    public  List<String> getRecordUserInOutTableNames(String beginTime, String endTime, Connection con) throws ParseException, SQLException {
        Date b;
        Date e;
        List<String> list2 = new LinkedList<>();
        if(beginTime==null ||"".equals(beginTime)){
            b = new Date();
        }else{
            b = DateUtil.getDate(beginTime,"yyyy-MM-dd");
        }
        if(endTime==null||"".equals(endTime)){
            e = new Date();
        }else{
            e = DateUtil.getDate(endTime,"yyyy-MM-dd");
        }
        for (Date date : DateUtil.getDates(b,e)) {
            String tableName = "recorduserinout"+DateUtil.formatDate(date,"yyyyMMdd");
            list2.add(tableName);
        }
        List<String> tableNames = existsRecorduserinout(list2,con);
        return tableNames;
    }

    private List<String> existsRecorduserinout(List<String> list2, Connection con) throws SQLException {
        return activityPlayerMapper.existsRecorduserinout(list2,con);
    }
}
