package com.console.service.impl;

import com.console.mapper.accounts.UserAccountInfoMapper;
import com.console.pojo.AccountsInfo;
import com.console.pojo.ActivityUser;
import com.console.pojo.UserAccountOrder;
import com.console.service.UserAccountInfoService;
import com.console.util.DateUtil;
import org.apache.log4j.chainsaw.Main;
import org.apache.poi.hwpf.usermodel.TableAutoformatLookSpecifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;

/**
 * Created by dell_2 on 2016/7/19.
 */
@Service
@Transactional
public class UserAccountInfoServiceImpl implements UserAccountInfoService {

    @Autowired
    private UserAccountInfoMapper userAccountInfoMapper;


    @Override
    @Transactional(readOnly = true,timeout = 100000)
    public int getActivityUserCount(UserAccountOrder order) {
        return userAccountInfoMapper.selectActivityUserCount(order);
    }

    @Override
    @Transactional(readOnly = true,timeout = 100000)
    public List<ActivityUser> selectActivityUsers(UserAccountOrder order) throws Exception {
               setRecordUserInOutTableNames(order);
               return userAccountInfoMapper.selectActivityUser(order);
    }

    @Override
    public List<AccountsInfo> selectUserAccount(AccountsInfo accountsInfo) {
        return userAccountInfoMapper.selectUserAccount(accountsInfo);
    }

    @Override
    public int searchCount(AccountsInfo accountsInfo) {
        return userAccountInfoMapper.searchCount(accountsInfo);
    }

    public  void setRecordUserInOutTableNames(UserAccountOrder order) throws ParseException {
        Date b = null;
        Date e = null;
        List<String> list2 = new LinkedList<>();
        if(order.getBeginDate()==null ||"".equals(order.getBeginDate())){
            b = new Date();
        }else{
            b = DateUtil.getDate(order.getBeginDate(),"yyyy-MM-dd");
        }
        if(order.getEndDate()==null||"".equals(order.getBeginDate())){
            e = new Date();
        }else{
            e = DateUtil.getDate(order.getEndDate(),"yyyy-MM-dd");
        }
        for (Date date : DateUtil.getDates(b,e)) {
           String tableName = "recorduserinout"+DateUtil.formatDate(date,"yyyyMMdd");
            list2.add(tableName);
        }
        List<String> tableNames = userAccountInfoMapper.existsRecorduserinout(list2);
        if(tableNames!=null && !tableNames.isEmpty()){
            order.setTableNames(tableNames);
        }

    }


}
