package za.co.tms.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.BooleanType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

import za.co.tms.obj.FleetTransferLog;
import za.co.tms.util.HibernateUtil;

public class TMSDAO<T, ID extends Serializable> {
	protected Session getSession() {
		return HibernateUtil.getSession();
	}

	public Object saveAndFlush(Object entity) {
		Session hibernateSession = this.getSession();

		Object object = null;
		try {
			HibernateUtil.beginTransaction();
			object = hibernateSession.save(entity);
			HibernateUtil.commitTransaction();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (hibernateSession != null && hibernateSession.isConnected()) {
				hibernateSession.close();
			}
		}
		return object;
	}

	public void save(Object entity) {
		Session hibernateSession = this.getSession();

		try {
			HibernateUtil.beginTransaction();
			hibernateSession.saveOrUpdate(entity);
			HibernateUtil.commitTransaction();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (hibernateSession != null && hibernateSession.isConnected()) {
				hibernateSession.close();
			}
		}
	}

	public void delete(Object entity) {
		Session hibernateSession = this.getSession();

		try {
			HibernateUtil.beginTransaction();
			hibernateSession.delete(entity);
			hibernateSession.flush();
			HibernateUtil.commitTransaction();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (hibernateSession != null && hibernateSession.isConnected()) {
				hibernateSession.close();
			}
		}
	}
	
	public void save_temp(Object entity) {
		Session hibernateSession = this.getSession();
		
		HibernateUtil.beginTransaction();
		hibernateSession.saveOrUpdate(entity);
		HibernateUtil.commitTransaction();

		if (hibernateSession != null && hibernateSession.isConnected()) {
			hibernateSession.close();
		}
	}
	
	public void delete_temp(Object entity) {
		Session hibernateSession = this.getSession();
		
		HibernateUtil.beginTransaction();
		hibernateSession.delete(entity);
		hibernateSession.flush();
		HibernateUtil.commitTransaction();

		if (hibernateSession != null && hibernateSession.isConnected()) {
			hibernateSession.close();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public T findById(Class clazz, Long id) {
		Session hibernateSession = this.getSession();
		HibernateUtil.beginTransaction();
		T t = null;
		t = (T) hibernateSession.get(clazz, id);
		hibernateSession.clear();
		hibernateSession.flush();
		hibernateSession.close();

		return t;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> getAll(Class clazz) {
		Session hibernateSession = this.getSession();
		HibernateUtil.beginTransaction();
		List<T> objects;

		Criteria criteria = hibernateSession.createCriteria(clazz);

		objects = (List<T>) criteria.list();
		HibernateUtil.commitTransaction();
		
		if (objects != null && objects.size() > 0) {
			objects.removeAll(Collections.singleton(null));
			return objects;
		} else {
			return null;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<T> getAllv2(Class clazz, String orderField, boolean asc) {
		Session hibernateSession = this.getSession();
		HibernateUtil.beginTransaction();

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM `" + clazz.getSimpleName() + "` WHERE id > 0 ");

		if (orderField != null) {
			sb.append("ORDER BY " + orderField);

			if (asc)
				sb.append(" ASC;");
			else
				sb.append(" DESC;");
		}

		List<T> objects = (List<T>) hibernateSession.createSQLQuery(sb.toString()).addEntity(clazz).list();

		hibernateSession.flush();
		HibernateUtil.commitTransaction();

		if (objects != null && objects.size() > 0) {
			objects.removeAll(Collections.singleton(null));
			return objects;
		} else {
			return null;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<T> getAllSortByField(Class clazz, String fieldName, String sortOrder) {
		Session hibernateSession = this.getSession();
		HibernateUtil.beginTransaction();
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT * ");
		sb.append("FROM `" + clazz.getSimpleName() + "` ");
		sb.append("ORDER BY `" + fieldName + "` " + sortOrder + ";");

		List<T> objects = (List<T>) hibernateSession.createSQLQuery(sb.toString()).addEntity(clazz).list();

		hibernateSession.flush();
		HibernateUtil.commitTransaction();

		if (objects != null && objects.size() > 0) {
			objects.removeAll(Collections.singleton(null));
			return objects;
		} else {
			return null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> getAll_Descending(Class clazz) {
		Session hibernateSession = this.getSession();
		HibernateUtil.beginTransaction();
		List<T> objects;

		Criteria criteria = hibernateSession.createCriteria(clazz);
		objects = (List<T>) criteria.list();
		HibernateUtil.commitTransaction();

		if (objects != null && objects.size() > 0) {
			objects.removeAll(Collections.singleton(null));

			List<T> desc_objects = new ArrayList<T>();
			int oSize = objects.size() - 1;

			for (int i = oSize; i >= 0; i--) {
				desc_objects.add(objects.get(i));
			}

			return desc_objects;
		} else {
			return null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findNullFields(Class clazz, String fieldName) {
		Session hibernateSession = this.getSession();
		HibernateUtil.beginTransaction();

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM " + clazz.getSimpleName() + " WHERE ").append(fieldName);

		sb.append(" IS NULL");

		List<T> objects = (List<T>) hibernateSession.createSQLQuery(sb.toString()).addEntity(clazz).list();
		hibernateSession.flush();
		HibernateUtil.commitTransaction();

		if (objects != null && objects.size() > 0) {
			objects.removeAll(Collections.singleton(null));
			return objects;
		} else {
			return null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findByField(Class clazz, String fieldName, Object fieldValue) {
		Session hibernateSession = this.getSession();
		HibernateUtil.beginTransaction();

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM " + clazz.getSimpleName() + " WHERE ").append(fieldName);

		if (fieldValue instanceof String)
			sb.append(" = '").append(fieldValue).append("'");
		else
			sb.append(" = ").append(fieldValue);

		List<T> objects = (List<T>) hibernateSession.createSQLQuery(sb.toString()).addEntity(clazz).list();

		try {
			hibernateSession.flush();
			HibernateUtil.commitTransaction();
		} catch (Exception ex) {
			hibernateSession.close();
			return null;
		}

		if (objects != null && objects.size() > 0) {
			objects.removeAll(Collections.singleton(null));
			return objects;
		} else {
			return null;
		}
	}

	public List<T> findByField_orderByField(@SuppressWarnings("rawtypes") Class clazz, String fieldName, Object fieldValue, String orderField, String orderType) {
		Session hibernateSession = this.getSession();
		HibernateUtil.beginTransaction();

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM " + clazz.getSimpleName() + " WHERE ").append(fieldName);

		if (fieldValue instanceof String)
			sb.append(" = '").append(fieldValue).append("'");
		else
			sb.append(" = ").append(fieldValue);

		sb.append(" ORDER BY " + orderField + " " + orderType + ";");

		@SuppressWarnings("unchecked")
		List<T> objects = (List<T>) hibernateSession.createSQLQuery(sb.toString()).addEntity(clazz).list();
		hibernateSession.flush();
		HibernateUtil.commitTransaction();

		if (objects != null && objects.size() > 0) {
			objects.removeAll(Collections.singleton(null));
			return objects;
		} else {
			return null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findByField_Descending(Class clazz, String fieldName, Object fieldValue) {
		Session hibernateSession = this.getSession();
		HibernateUtil.beginTransaction();

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM " + clazz.getSimpleName() + " WHERE ").append(fieldName);

		if (fieldValue instanceof String)
			sb.append(" = '").append(fieldValue).append("'");
		else
			sb.append(" = ").append(fieldValue);

		sb.append(" ORDER BY id DESC;");

		List<T> objects = (List<T>) hibernateSession.createSQLQuery(sb.toString()).addEntity(clazz).list();
		hibernateSession.flush();
		HibernateUtil.commitTransaction();

		if (objects != null && objects.size() > 0) {
			objects.removeAll(Collections.singleton(null));
			return objects;
		} else {
			return null;
		}
	}

	public String pingTest() {
		Session hibernateSession = this.getSession();
		HibernateUtil.beginTransaction();

		SQLQuery sQuery = hibernateSession.createSQLQuery("select 1 as dbcp_connection_test");
		@SuppressWarnings("rawtypes")
		List list = sQuery.list();
		BigInteger reta = (BigInteger) list.get(0);
		int retb = reta.intValue();
		String ret = "fail";

		if (retb == 1) {
			ret = "success";
		} else {
			ret = "fail";
		}

		hibernateSession.flush();
		HibernateUtil.commitTransaction();

		return ret;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> searchByField(Class clazz, String fieldName, Object fieldValue) {
		Session hibernateSession = this.getSession();
		HibernateUtil.beginTransaction();

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM " + clazz.getSimpleName() + " WHERE ").append(fieldName);

		if (fieldValue instanceof String)
			sb.append(" LIKE '%").append(fieldValue).append("%'");
		else
			sb.append(" LIKE %").append(fieldValue).append("%");

		List<T> objects = (List<T>) hibernateSession.createSQLQuery(sb.toString()).addEntity(clazz).list();
		hibernateSession.flush();
		HibernateUtil.commitTransaction();

		if (objects != null && objects.size() > 0) {
			objects.removeAll(Collections.singleton(null));
			return objects;
		} else {
			return null;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> searchMultipleValues(Class clazz, String fieldName, Object fieldValue) {
		Session hibernateSession = this.getSession();
		HibernateUtil.beginTransaction();

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM " + clazz.getSimpleName() + " WHERE ").append(fieldName);
		sb.append(" IN ").append("(").append(fieldValue).append(")").append(";");

		List<T> objects = (List<T>) hibernateSession.createSQLQuery(sb.toString()).addEntity(clazz).list();
		hibernateSession.flush();
		HibernateUtil.commitTransaction();

		if (objects != null && objects.size() > 0) {
			objects.removeAll(Collections.singleton(null));
			return objects;
		} else {
			return null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> queryConditional(Class clazz, String conditionalQuery) {
		Session hibernateSession = this.getSession();
		HibernateUtil.beginTransaction();

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM " + clazz.getSimpleName() + " WHERE ");
		sb.append(conditionalQuery);

		List<T> objects = (List<T>) hibernateSession.createSQLQuery(sb.toString()).addEntity(clazz).list();
		hibernateSession.flush();
		HibernateUtil.commitTransaction();

		if (objects != null && objects.size() > 0) {
			objects.removeAll(Collections.singleton(null));
			return objects;
		} else {
			return null;
		}
	}

	public int getLastID(@SuppressWarnings("rawtypes") Class clazz) {
		Session hibernateSession = this.getSession();
		HibernateUtil.beginTransaction();
		SQLQuery sQuery = hibernateSession.createSQLQuery("SELECT id FROM `" + clazz.getSimpleName() + "` WHERE id=(select max(id) from `" + clazz.getSimpleName() + "`)");
		@SuppressWarnings("rawtypes")
		List list = sQuery.list();
		int lastID = 0;

		if (list.size() > 0 && list != null) {
			BigInteger bilastID = (BigInteger) list.get(0);
			lastID = bilastID.intValue();
		}

		hibernateSession.flush();
		HibernateUtil.commitTransaction();

		return lastID;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> specialQuery(Class clazz, String query) {
		Session hibernateSession = this.getSession();
		HibernateUtil.beginTransaction();

		List<T> objects = (List<T>) hibernateSession.createSQLQuery(query).addEntity(clazz).list();
		hibernateSession.flush();
		HibernateUtil.commitTransaction();

		if (objects != null && objects.size() > 0) {
			objects.removeAll(Collections.singleton(null));
			return objects;
		} else {
			return null;
		}
	}

	public List<Object> specialQueryCustomObject(String query) {
		Session hibernateSession = this.getSession();
		HibernateUtil.beginTransaction();

		Query q = hibernateSession.createSQLQuery(query);
		@SuppressWarnings("unchecked")
		List<Object> objects = q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

		hibernateSession.flush();
		HibernateUtil.commitTransaction();

		if (objects != null && objects.size() > 0) {
			objects.removeAll(Collections.singleton(null));
			return objects;
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> specialQueryCustomObject(Class<?> clazz, String query) {
		Session hibernateSession = this.getSession();
		HibernateUtil.beginTransaction();

		SQLQuery q = hibernateSession.createSQLQuery(query);

		q.setResultTransformer(Transformers.aliasToBean(clazz));
		addScalars(q, clazz);

		List<T> objects = q.list();
		hibernateSession.flush();
		HibernateUtil.commitTransaction();

		if (objects != null && objects.size() > 0) {
			objects.removeAll(Collections.singleton(null));
			return objects;
		} else {
			return null;
		}
	}

	private void addScalars(SQLQuery q, Class<?> clazz) {
		for (Field field : clazz.getDeclaredFields()) {
			if (!field.getType().isPrimitive()) {
				if (field.getType().equals(Long.class)) {
					q.addScalar(field.getName(), LongType.INSTANCE);
				} else if (field.getType().equals(Integer.class)) {
					q.addScalar(field.getName(), IntegerType.INSTANCE);
				} else if (field.getType().equals(String.class)) {
					q.addScalar(field.getName(), StringType.INSTANCE);
				} else if (field.getType().equals(Double.class)) {
					q.addScalar(field.getName(), DoubleType.INSTANCE);
				} else if (field.getType().equals(BigDecimal.class)) {
					q.addScalar(field.getName(), BigDecimalType.INSTANCE);
				} else if (field.getType().equals(Boolean.class)) {
					q.addScalar(field.getName(), BooleanType.INSTANCE);
				} else {
					throw new RuntimeException("No scalar for type: " + field.getType().getSimpleName() + ". Please add the required scalar in the TMSDAO addScalars method");
				}
			}
		}
	}

	public void executeFutileQuery(String query) {
		Session hibernateSession = this.getSession();

		try {
			HibernateUtil.beginTransaction();
			Query squery = hibernateSession.createSQLQuery(query);
			squery.executeUpdate();
			hibernateSession.flush();
			HibernateUtil.commitTransaction();
		} catch (Exception ex) {
		} finally {
			if (hibernateSession != null && hibernateSession.isConnected()) {
				hibernateSession.close();
			}
		}
	}

	public String getSingleValueQuery(String query) {
		Session hibernateSession = this.getSession();
		HibernateUtil.beginTransaction();
		SQLQuery sQuery = hibernateSession.createSQLQuery(query);
		@SuppressWarnings("rawtypes")
		List list = sQuery.list();
		String ret = null;

		if (list.size() > 0 && list != null) {
			if (list.get(0) != null)
				ret = list.get(0).toString();
		}

		hibernateSession.flush();
		HibernateUtil.commitTransaction();

		return ret;
	}
}