package com.yapu.archive.dao.itf;

import java.util.List;

import com.yapu.archive.entity.SysOrgTree;
import com.yapu.archive.entity.SysOrgTreeExample;

public interface SysOrgTreeDAO {
    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ORG_TREE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    int countByExample(SysOrgTreeExample example);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ORG_TREE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    int deleteByExample(SysOrgTreeExample example);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ORG_TREE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    int deleteByPrimaryKey(String orgTreeId);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ORG_TREE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    void insert(SysOrgTree record);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ORG_TREE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    void insertSelective(SysOrgTree record);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ORG_TREE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    List selectByExample(SysOrgTreeExample example);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ORG_TREE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    SysOrgTree selectByPrimaryKey(String orgTreeId);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ORG_TREE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    int updateByExampleSelective(SysOrgTree record, SysOrgTreeExample example);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ORG_TREE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    int updateByExample(SysOrgTree record, SysOrgTreeExample example);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ORG_TREE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    int updateByPrimaryKeySelective(SysOrgTree record);

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ORG_TREE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    int updateByPrimaryKey(SysOrgTree record);
}