package com.yapu.system.dao.impl;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.yapu.system.dao.itf.SysOrgRoleDAO;
import com.yapu.system.entity.SysOrgRole;
import com.yapu.system.entity.SysOrgRoleExample;

public class SysOrgRoleDAOImpl extends SqlMapClientDaoSupport implements SysOrgRoleDAO {

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ORG_ROLE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public SysOrgRoleDAOImpl() {
        super();
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ORG_ROLE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public int countByExample(SysOrgRoleExample example) {
        Integer count = (Integer)  getSqlMapClientTemplate().queryForObject("SYS_ORG_ROLE.ibatorgenerated_countByExample", example);
        return count.intValue();
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ORG_ROLE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public int deleteByExample(SysOrgRoleExample example) {
        int rows = getSqlMapClientTemplate().delete("SYS_ORG_ROLE.ibatorgenerated_deleteByExample", example);
        return rows;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ORG_ROLE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public int deleteByPrimaryKey(String orgRoleId) {
        SysOrgRole key = new SysOrgRole();
        key.setOrgRoleId(orgRoleId);
        int rows = getSqlMapClientTemplate().delete("SYS_ORG_ROLE.ibatorgenerated_deleteByPrimaryKey", key);
        return rows;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ORG_ROLE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public void insert(SysOrgRole record) {
        getSqlMapClientTemplate().insert("SYS_ORG_ROLE.ibatorgenerated_insert", record);
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ORG_ROLE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public void insertSelective(SysOrgRole record) {
        getSqlMapClientTemplate().insert("SYS_ORG_ROLE.ibatorgenerated_insertSelective", record);
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ORG_ROLE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public List selectByExample(SysOrgRoleExample example) {
        List list = getSqlMapClientTemplate().queryForList("SYS_ORG_ROLE.ibatorgenerated_selectByExample", example);
        return list;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ORG_ROLE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public SysOrgRole selectByPrimaryKey(String orgRoleId) {
        SysOrgRole key = new SysOrgRole();
        key.setOrgRoleId(orgRoleId);
        SysOrgRole record = (SysOrgRole) getSqlMapClientTemplate().queryForObject("SYS_ORG_ROLE.ibatorgenerated_selectByPrimaryKey", key);
        return record;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ORG_ROLE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public int updateByExampleSelective(SysOrgRole record, SysOrgRoleExample example) {
        UpdateByExampleParms parms = new UpdateByExampleParms(record, example);
        int rows = getSqlMapClientTemplate().update("SYS_ORG_ROLE.ibatorgenerated_updateByExampleSelective", parms);
        return rows;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ORG_ROLE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public int updateByExample(SysOrgRole record, SysOrgRoleExample example) {
        UpdateByExampleParms parms = new UpdateByExampleParms(record, example);
        int rows = getSqlMapClientTemplate().update("SYS_ORG_ROLE.ibatorgenerated_updateByExample", parms);
        return rows;
    }

    
    public int updateByPrimaryKeySelective(SysOrgRole record) {
    	int rows = 0;
    	try {
    		rows = getSqlMapClientTemplate().update("SYS_ORG_ROLE.ibatorgenerated_updateByPrimaryKeySelective", record);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
        
        return rows;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ORG_ROLE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public int updateByPrimaryKey(SysOrgRole record) {
        int rows = getSqlMapClientTemplate().update("SYS_ORG_ROLE.ibatorgenerated_updateByPrimaryKey", record);
        return rows;
    }

    /**
     * This class was generated by Apache iBATIS ibator.
     * This class corresponds to the database table SYS_ORG_ROLE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    private static class UpdateByExampleParms extends SysOrgRoleExample {
        private Object record;

        public UpdateByExampleParms(Object record, SysOrgRoleExample example) {
            super(example);
            this.record = record;
        }

        public Object getRecord() {
            return record;
        }
    }
}