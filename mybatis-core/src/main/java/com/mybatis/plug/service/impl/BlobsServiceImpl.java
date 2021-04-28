package com.mybatis.plug.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mybatis.plug.annotation.Logic;
import com.mybatis.plug.enums.SqlMethod;
import com.mybatis.plug.exception.MybatisBoostException;
import com.mybatis.plug.mapper.BaseMapper;
import com.mybatis.plug.mapper.BlobsMapper;
import com.mybatis.plug.query.BaseExample;
import com.mybatis.plug.service.IService;
import com.mybatis.plug.tool.SqlHelper;
import com.mybatis.plug.request.PageRequest;
import org.apache.ibatis.reflection.ExceptionUtil;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.MyBatisExceptionTranslator;
import org.mybatis.spring.SqlSessionHolder;
import org.mybatis.spring.SqlSessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author coco
 * @date 2020-07-31 10:22
 **/
public class BlobsServiceImpl<T,M extends BaseExample,K extends BlobsMapper<T,M>> implements IService<T,M> {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceImpl.class);

    @Autowired
    protected K baseMapper;


    @Deprecated
    protected SqlSession sqlSessionBatch() {
        return SqlHelper.sqlSessionBatch();
    }
    @Deprecated
    protected void closeSqlSession(SqlSession sqlSession) {
        SqlSessionUtils.closeSqlSession(sqlSession,SqlHelper.FACTORY);
    }

    protected String currentMapperName(){
        Class<? extends BaseMapper> aClass = this.baseMapper.getClass();
        Type[] genericInterfaces = aClass.getGenericInterfaces();
        return genericInterfaces[0].getTypeName();
    }
    @Override
    public BaseMapper<T, M> getBaseMapper() {
        return this.baseMapper;
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public boolean save(T entity) {
        if (Objects.isNull(entity)) {
            LOG.warn("保存对象为空!");
            return false;
        }
        int i = this.baseMapper.insertSelective(entity);
        if (i<1) {
            return false;
        }
        return true;
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public boolean saveList(Collection<T> entityList, int listSize) {
        if (CollectionUtils.isEmpty(entityList)) {
            LOG.warn("保存对象列表为空!");
            return false;
        }
        return executeBatch(entityList, listSize, (sqlSession, entity) ->
                sqlSession.insert(currentMapperName()+"."+ SqlMethod.INSERT_SELECTIVE.getMethod(),entity));
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public boolean saveOrUpdateList(Collection<T> entityList, int listSize) {
        if (CollectionUtils.isEmpty(entityList)) {
            LOG.warn("对象列表为空!");
            return false;

        }
        return executeBatch(entityList, listSize, (sqlSession, entity) -> {
         /*   MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
            param.put(Constants.RECORD, entity);
            int update = sqlSession.update(currentMapperName() + "." + SqlMethod.UPDATE_BY_PRIMARY_KEY_SELECTIVE.getMethod(), param);
            if (update <1 ) {
                sqlSession.insert(currentMapperName() + "." + SqlMethod.INSERT_SELECTIVE.getMethod(),entity);
            }*/
            this.saveOrUpdate(entity);
        });
    }

    @Override
    public boolean deleteByKey(Serializable id) {
        this.baseMapper.deleteByPrimaryKey(id);
        return true;
    }

    @Override
    public boolean deleteByExample(M example) {
        if (Objects.isNull(example)) {
            return false;
        }
        this.baseMapper.deleteByExample(example);
        return true;
    }

    @Override
    public boolean deleteByKeys(Collection<? extends Serializable> keyList) {
        return executeBatch(keyList, keyList.size(), (sqlSession, key) -> {
            sqlSession.delete(currentMapperName()+"."+ SqlMethod.DELETE_BY_PRIMARY_KEY.getMethod(),key);
        });
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public boolean updateByKey(T entity) {
        if (Objects.isNull(entity)) {
            LOG.warn("对象为空!");
            return false;
        }
        int i = this.baseMapper.updateByPrimaryKeySelective(entity);
        if (i < 1) {
            return false;
        }
        return true;
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public boolean update(T entity, M example) {
        if (Objects.isNull(entity) || Objects.isNull(example) ) {
            LOG.warn("对象为空!");
            return false;
        }
        int i = this.baseMapper.updateByExampleSelective(entity, example);
        if (i < 1) {
            return false;
        }
        return true;
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public boolean updateBykeys(Collection<T> entityList, int listSize) {
        if (CollectionUtils.isEmpty(entityList)) {
            LOG.warn("对象列表为空!");
            return false;
        }
        return executeBatch(entityList, listSize, (sqlSession, entity) -> {
            this.updateByKey(entity);
        });
    }

    @Override
    public Optional<T> queryByKey(Serializable key) {
        T o = (T) this.baseMapper.selectByPrimaryKey(key);
        return Optional.ofNullable(o);
    }

    @Override
    public Optional<Collection<T>> queryByExample(M example) {
        if (Objects.isNull(example)) {
            return Optional.empty();
        }
        List<T> list = this.baseMapper.selectByExampleWithBLOBs(example);
        return Optional.ofNullable(list);
    }

    @Override
    public Optional<T> queryOneByExample(M example) {
        if (Objects.isNull(example)) {
            return Optional.empty();
        }
        if (example.isLogic()) {
            // this.getClass().
        }
        T o = (T) this.baseMapper.selectOneByExampleWithBLOBs(example);
        return Optional.ofNullable(o);
    }

    @Override
    public long count(M example) {
        if (Objects.isNull(example)) {
            return 0;
        }
        return this.baseMapper.countByExample(example);
    }


    @Override
    public PageInfo<T> selectPageByExample(M example, PageRequest request) {
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<T> list = this.baseMapper.selectByExampleWithBLOBs(example);
        PageInfo<T> page = new PageInfo(list);
        return page;
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public boolean saveOrUpdate(T entity) {
        if (Objects.isNull(entity)) {
            return false;
        }
        int i = this.baseMapper.updateByPrimaryKeySelective(entity);
        if (i<1) {
            int j = this.baseMapper.insertSelective(entity);
            if (j<1){
                return false;
            }
        }
        return true;
    }



    protected boolean executeBatch(Consumer<SqlSession> consumer) {
        SqlSessionFactory sqlSessionFactory = SqlHelper.FACTORY;
        SqlSessionHolder sqlSessionHolder = (SqlSessionHolder) TransactionSynchronizationManager.getResource(sqlSessionFactory);
        boolean transaction = TransactionSynchronizationManager.isSynchronizationActive();
        if (sqlSessionHolder != null) {
            SqlSession sqlSession = sqlSessionHolder.getSqlSession();
            //原生无法支持执行器切换，当存在批量操作时，会嵌套两个session的，优先commit上一个session
            //按道理来说，这里的值应该一直为false。
            sqlSession.commit(!transaction);
        }
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        if (!transaction) {
            LOG.warn("SqlSession [" + sqlSession + "] was not registered for synchronization because DataSource is not transactional");
        }
        try {
            consumer.accept(sqlSession);
            //非事物情况下，强制commit。
            sqlSession.commit(!transaction);
            return true;
        } catch (Throwable t) {
            sqlSession.rollback();
            Throwable unwrapped = ExceptionUtil.unwrapThrowable(t);
            if (unwrapped instanceof RuntimeException) {
                MyBatisExceptionTranslator myBatisExceptionTranslator
                        = new MyBatisExceptionTranslator(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(), true);
                throw Objects.requireNonNull(myBatisExceptionTranslator.translateExceptionIfPossible((RuntimeException) unwrapped));
            }
            throw new MybatisBoostException(unwrapped);
        } finally {
            sqlSession.close();
        }
    }
    /**
     * 执行批量操作
     *
     * @param list      数据集合
     * @param batchSize 批量大小
     * @param consumer  执行方法
     * @param <E>       泛型
     * @return 操作结果
     */
    protected <E> boolean executeBatch(Collection<E> list, int batchSize, BiConsumer<SqlSession, E> consumer) {
        return !CollectionUtils.isEmpty(list) && executeBatch(sqlSession -> {
            int size = list.size();
            int i = 1;
            for (E element : list) {
                consumer.accept(sqlSession, element);
                if ((i % batchSize == 0) || i == size) {
                    sqlSession.flushStatements();
                }
                i++;
            }
        });
    }

    /**
     * 执行批量操作（默认批次提交数量{@link IService#}）
     *
     * @param list     数据集合
     * @param consumer 执行方法
     * @param <E>      泛型
     * @return 操作结果
     */
    protected <E> boolean executeBatch(Collection<E> list, BiConsumer<SqlSession, E> consumer) {
        return executeBatch(list, 1000, consumer);
    }


    /**
     * 当前服务实现类的 entity类型
     * @return
     */
    protected Class<?> currentEntityClass() {
        //返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
        Type genType = getClass().getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        //返回表示此类型实际类型参数的 Type 对象的数组。
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (0 >= params.length) {
            return Object.class;
        }
        if (!(params[0] instanceof Class)) {
            return Object.class;
        }

        return (Class) params[0];

    }


    /**
     * 获取实体类 @Logic 的其中一个属性名称
     *
     * @param clazz
     * @return
     */
    protected String getLogicName(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Logic.class)) {
                /**
                 * 获取字段名
                 */
                return field.getName();
            }
        }
        return null;
    }


}

