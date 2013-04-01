package com.yapu.archive.dao.itf;

import java.util.List;

import com.yapu.archive.entity.SysAccountTree;
import com.yapu.archive.entity.SysAccountTreeExample;

public interface SysAccountTreeDAO {
    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ACCOUNT_TREE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    int countByExample(SysAccountTreeExample example);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ACCOUNT_TREE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    int deleteByExample(SysAccountTreeExample example);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ACCOUNT_TREE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    int deleteByPrimaryKey(String accountTreeId);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ACCOUNT_TREE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    void insert(SysAccountTree record);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ACCOUNT_TREE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    void insertSelective(SysAccountTree record);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ACCOUNT_TREE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    List selectByExample(SysAccountTreeExample example);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ACCOUNT_TREE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    SysAccountTree selectByPrimaryKey(String accountTreeId);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ACCOUNT_TREE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    int updateByExampleSelective(SysAccountTree record, SysAccountTreeExample example);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ACCOUNT_TREE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    int updateByExample(SysAccountTree record, SysAccountTreeExample example);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ACCOUNT_TREE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    int updateByPrimaryKeySelective(SysAccountTree record);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ACCOUNT_TREE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    int updateByPrimaryKey(SysAccountTree record);
}