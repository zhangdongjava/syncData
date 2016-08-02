package com.console.sync.service;

import java.sql.SQLException;

/**
 * Created by dell_2 on 2016/8/2.
 */
public interface DataSyncService {

     String activityPlayerSync(String activityPlayerIp, String activityPlayerUser, String activityPlayerPwd, String beginTime, String endTime) throws  Exception;
}
