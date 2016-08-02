package com.console.service;

import com.console.pojo.AccountsInfo;
import com.console.pojo.ActivityUser;
import com.console.pojo.UserAccountOrder;

import java.text.ParseException;
import java.util.List;

/**
 * Created by dell_2 on 2016/7/19.
 */
public interface UserAccountInfoService {

    /**
     * 获取活动用户数量
     * @param order 查询条件
     * @return
     */
    int getActivityUserCount(UserAccountOrder order);

    /**
     * 查询活动用户列表
     * @param order
     * @return
     */
    List<ActivityUser> selectActivityUsers(UserAccountOrder order) throws ParseException, Exception;

    /**
     * 条件分页查询用户信息
     * @param accountsInfo
     * @return
     */
    List<AccountsInfo> selectUserAccount(AccountsInfo accountsInfo);

    /**
     * 查询用户数量
     * @param accountsInfo
     * @return
     */
    int searchCount(AccountsInfo accountsInfo);

    /**
     * 根据开始和结束时间获取进出记录表
     * @param order
     * @throws ParseException
     */
    void setRecordUserInOutTableNames(UserAccountOrder order) throws ParseException;
}
