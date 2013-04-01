package com.yapu.system.dao.itf;

import java.util.HashMap;
import java.util.List;

import com.yapu.system.entity.SysAccount;
import com.yapu.system.entity.SysAccountExample;

public interface SysAccountDAO {
    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ACCOUNT
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    int countByExample(SysAccountExample example);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ACCOUNT
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    int deleteByExample(SysAccountExample example);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ACCOUNT
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    int deleteByPrimaryKey(String accountid);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ACCOUNT
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    void insert(SysAccount record);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ACCOUNT
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    void insertSelective(SysAccount record);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ACCOUNT
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    List selectByExample(SysAccountExample example);
    
    /**
     * 手动加入的分页语句
     * @param map
     * @return
     */
    List selectByMapPage(SysAccountExample example);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ACCOUNT
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    SysAccount selectByPrimaryKey(String accountid);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ACCOUNT
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    int updateByExampleSelective(SysAccount record, SysAccountExample example);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ACCOUNT
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    int updateByExample(SysAccount record, SysAccountExample example);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ACCOUNT
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    int updateByPrimaryKeySelective(SysAccount record);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ACCOUNT
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    int updateByPrimaryKey(SysAccount record);
}