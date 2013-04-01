package com.yapu.system.dao.impl;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.yapu.system.dao.itf.SysRoleFunctionDAO;
import com.yapu.system.entity.SysRoleFunction;
import com.yapu.system.entity.SysRoleFunctionExample;

public class SysRoleFunctionDAOImpl extends SqlMapClientDaoSupport implements SysRoleFunctionDAO {

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ROLE_FUNCTION
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public SysRoleFunctionDAOImpl() {
        super();
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ROLE_FUNCTION
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public int countByExample(SysRoleFunctionExample example) {
        Integer count = (Integer)  getSqlMapClientTemplate().queryForObject("SYS_ROLE_FUNCTION.ibatorgenerated_countByExample", example);
        return count.intValue();
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ROLE_FUNCTION
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public int deleteByExample(SysRoleFunctionExample example) {
        int rows = getSqlMapClientTemplate().delete("SYS_ROLE_FUNCTION.ibatorgenerated_deleteByExample", example);
        return rows;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ROLE_FUNCTION
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public int deleteByPrimaryKey(String roleFunctionId) {
        SysRoleFunction key = new SysRoleFunction();
        key.setRoleFunctionId(roleFunctionId);
        int rows = getSqlMapClientTemplate().delete("SYS_ROLE_FUNCTION.ibatorgenerated_deleteByPrimaryKey", key);
        return rows;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ROLE_FUNCTION
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public void insert(SysRoleFunction record) {
        getSqlMapClientTemplate().insert("SYS_ROLE_FUNCTION.ibatorgenerated_insert", record);
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ROLE_FUNCTION
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public void insertSelective(SysRoleFunction record) {
        getSqlMapClientTemplate().insert("SYS_ROLE_FUNCTION.ibatorgenerated_insertSelective", record);
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ROLE_FUNCTION
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public List selectByExample(SysRoleFunctionExample example) {
        List list = getSqlMapClientTemplate().queryForList("SYS_ROLE_FUNCTION.ibatorgenerated_selectByExample", example);
        return list;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ROLE_FUNCTION
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public SysRoleFunction selectByPrimaryKey(String roleFunctionId) {
        SysRoleFunction key = new SysRoleFunction();
        key.setRoleFunctionId(roleFunctionId);
        SysRoleFunction record = (SysRoleFunction) getSqlMapClientTemplate().queryForObject("SYS_ROLE_FUNCTION.ibatorgenerated_selectByPrimaryKey", key);
        return record;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ROLE_FUNCTION
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public int updateByExampleSelective(SysRoleFunction record, SysRoleFunctionExample example) {
        UpdateByExampleParms parms = new UpdateByExampleParms(record, example);
        int rows = getSqlMapClientTemplate().update("SYS_ROLE_FUNCTION.ibatorgenerated_updateByExampleSelective", parms);
        return rows;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ROLE_FUNCTION
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public int updateByExample(SysRoleFunction record, SysRoleFunctionExample example) {
        UpdateByExampleParms parms = new UpdateByExampleParms(record, example);
        int rows = getSqlMapClientTemplate().update("SYS_ROLE_FUNCTION.ibatorgenerated_updateByExample", parms);
        return rows;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ROLE_FUNCTION
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public int updateByPrimaryKeySelective(SysRoleFunction record) {
        int rows = getSqlMapClientTemplate().update("SYS_ROLE_FUNCTION.ibatorgenerated_updateByPrimaryKeySelective", record);
        return rows;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table SYS_ROLE_FUNCTION
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    public int updateByPrimaryKey(SysRoleFunction record) {
        int rows = getSqlMapClientTemplate().update("SYS_ROLE_FUNCTION.ibatorgenerated_updateByPrimaryKey", record);
        return rows;
    }

    /**
     * This class was generated by Apache iBATIS ibator.
     * This class corresponds to the database table SYS_ROLE_FUNCTION
     *
     * @ibatorgenerated Fri Nov 12 20:49:06 GMT+08:00 2010
     */
    private static class UpdateByExampleParms extends SysRoleFunctionExample {
        private Object record;

        public UpdateByExampleParms(Object record, SysRoleFunctionExample example) {
            super(example);
            this.record = record;
        }

        public Object getRecord() {
            return record;
        }
    }
}