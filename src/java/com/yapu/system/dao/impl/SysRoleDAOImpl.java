package com.yapu.system.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.yapu.system.dao.itf.SysRoleDAO;
import com.yapu.system.entity.SysRole;
import com.yapu.system.entity.SysRoleExample;

public class SysRoleDAOImpl extends SqlMapClientDaoSupport implements SysRoleDAO {

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ROLE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public SysRoleDAOImpl() {
        super();
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ROLE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public int countByExample(SysRoleExample example) {
        Integer count = (Integer)  getSqlMapClientTemplate().queryForObject("SYS_ROLE.ibatorgenerated_countByExample", example);
        return count.intValue();
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ROLE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public int deleteByExample(SysRoleExample example) {
        int rows = getSqlMapClientTemplate().delete("SYS_ROLE.ibatorgenerated_deleteByExample", example);
        return rows;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ROLE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public int deleteByPrimaryKey(String roleid) {
        SysRole key = new SysRole();
        key.setRoleid(roleid);
        int rows = getSqlMapClientTemplate().delete("SYS_ROLE.ibatorgenerated_deleteByPrimaryKey", key);
        return rows;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ROLE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public void insert(SysRole record) {
        getSqlMapClientTemplate().insert("SYS_ROLE.ibatorgenerated_insert", record);
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ROLE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public void insertSelective(SysRole record) {
        getSqlMapClientTemplate().insert("SYS_ROLE.ibatorgenerated_insertSelective", record);
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ROLE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public List selectByExample(SysRoleExample example) {
        List list = getSqlMapClientTemplate().queryForList("SYS_ROLE.ibatorgenerated_selectByExample", example);
        return list;
    }

    public List selectByMapPage(HashMap map) {
		List list = getSqlMapClientTemplate().queryForList("SYS_ROLE.ibatorgenerated_selectByExamplePage", map);
		return list;
	}
    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ROLE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public SysRole selectByPrimaryKey(String roleid) {
        SysRole key = new SysRole();
        key.setRoleid(roleid);
        SysRole record = (SysRole) getSqlMapClientTemplate().queryForObject("SYS_ROLE.ibatorgenerated_selectByPrimaryKey", key);
        return record;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ROLE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public int updateByExampleSelective(SysRole record, SysRoleExample example) {
        UpdateByExampleParms parms = new UpdateByExampleParms(record, example);
        int rows = getSqlMapClientTemplate().update("SYS_ROLE.ibatorgenerated_updateByExampleSelective", parms);
        return rows;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ROLE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public int updateByExample(SysRole record, SysRoleExample example) {
        UpdateByExampleParms parms = new UpdateByExampleParms(record, example);
        int rows = getSqlMapClientTemplate().update("SYS_ROLE.ibatorgenerated_updateByExample", parms);
        return rows;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ROLE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public int updateByPrimaryKeySelective(SysRole record) {
        int rows = getSqlMapClientTemplate().update("SYS_ROLE.ibatorgenerated_updateByPrimaryKeySelective", record);
        return rows;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ROLE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public int updateByPrimaryKey(SysRole record) {
        int rows = getSqlMapClientTemplate().update("SYS_ROLE.ibatorgenerated_updateByPrimaryKey", record);
        return rows;
    }

    /**
     * This class was generated by Apache iBATIS ibator.
     * This class corresponds to the database table SYS_ROLE
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    private static class UpdateByExampleParms extends SysRoleExample {
        private Object record;

        public UpdateByExampleParms(Object record, SysRoleExample example) {
            super(example);
            this.record = record;
        }

        public Object getRecord() {
            return record;
        }
    }
}