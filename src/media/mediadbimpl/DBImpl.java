package media.mediadbimpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import media.definitions.Book;
import media.definitions.Category;
import media.definitions.DVD;
import media.definitions.MediaDbInterface;
import media.definitions.Music;
import media.definitions.Offer;
import media.definitions.Person;
import media.definitions.Product;
import media.definitions.Review;
import media.definitions.SQLResult;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

public class DBImpl implements MediaDbInterface {

	private Properties prop = null;
	private DatabaseConnection dbCon = null;
	private SessionFactory sessionFactory;
	
	@Override
	public void init(Properties prop) {
		this.prop = prop;
		
		System.out.println("Einstellungen:");
		prop.list(System.out);

		/*
		 * Herstellung der Datenbankverbindung.
		 */
		try {
			this.dbCon = new DatabaseConnection();
			this.dbCon.connect("con", prop);

			sessionFactory = new Configuration().configure().buildSessionFactory();
			System.out.println("Connection established!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Product> getProducts(String namePattern) {
		Session session = null;
		Transaction trx = null;
		List results = new ArrayList<Product>();

		try {
			session = sessionFactory.openSession();
			trx = session.beginTransaction();
			Criteria cr = session.createCriteria(Product.class);
			cr.add(Restrictions.like("title", namePattern));
			System.out.println("here!");
			results.addAll(cr.list());
			System.out.println("----------------------------");
			
			System.out.println("QUERY FOR PRODUCTS LIKE : " + namePattern);
			System.out.println("RESULTS: ");
			
			//Iterator it = results.iterator();
			/*while(it.hasNext()) {
				System.out.println("bla");
				System.out.println(it.next());
			}*/
			
			// TEST
			/*Person pers = new Person();
			pers.setName("TESTTEST");
			System.out.println("new object created");
			session.save(pers);*/
			
			trx.commit();
		} catch(HibernateException e) {
			if(trx != null) {
				try { trx.rollback(); } catch(HibernateException he) {}
			}
		} finally {
			try { if( session != null ) session.close(); } catch( Exception exCl ) {}
		}
		
		return results;
	}

	@Override
	public SQLResult executeSqlQuery(String query) throws Exception {
		Session session = null;
		Transaction trx = null;
		SQLResult result = new SQLResult();
		
		try {
			session = sessionFactory.openSession();
			trx = session.beginTransaction();
			
			PreparedStatement statement = session.connection().prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			
			//create header
			String[] header = new String[rsmd.getColumnCount()];
			for(int i=1; i<=rsmd.getColumnCount(); i++) {
				header[i-1] = (rsmd.getColumnName(i));
			}
			result.setHeader(header);
			
			//create body
			List<String[]> body = new ArrayList<String[]>();
			while(rs.next()) {
				String[] values = new String[rsmd.getColumnCount()];
				for(int i=1; i<=rsmd.getColumnCount(); i++) {
					values[i-1] = rs.getString(i);
				}
				body.add(values);
			}
			result.setBody(body);
		} catch(HibernateException e) {
			if(trx != null) {
				try {trx.rollback(); } catch(HibernateException he) {}
			}
		} finally {
			try { if( session != null ) session.close(); } catch( Exception exC1 ) {}
		}
		return result;
	}

	@Override
	public SQLResult executeHqlQuery(String query, boolean deref) {
		Session session = null;
		Transaction trx = null;
		SQLResult result = new SQLResult();

		try {

			session = sessionFactory.openSession();
			trx = session.beginTransaction();

			Query hqlQuery = session.createQuery(query);
			/*hqlQuery.setResultTransformer(new AliasToEntityMapResultTransformer());
			List<Map <String, Object>> aliasToValueMap = hqlQuery.list();

			Iterator it = aliasToValueMap.iterator();*/

			Iterator it = hqlQuery.list().iterator();
			while(it.hasNext()) {
				System.out.println(it.next());
			}
			
		} catch(HibernateException e) {
			if(trx != null) {
				try {trx.rollback(); } catch(HibernateException he) {}
			}
		} finally {
			try { if( session != null ) session.close(); } catch( Exception exC1 ) {}
		}
		return result;
	}

	@Override
	public Category getCategoryTree() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> getProductsByCategoryPath(Category[] categoriesPath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> getReviewProducts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addNewReview(Review review) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Product getProduct(String id) {
		Product prod;
		Session session = null;
		Transaction trx = null;
		
		try {
			session = sessionFactory.openSession();
			trx = session.beginTransaction();

			Criteria cr = session.createCriteria(Product.class);
			cr.add(Restrictions.eq("asin", id));
			
			List result = cr.list();
			Iterator it = result.iterator();
			if(it.hasNext()) return (Product)it.next();
			else return null;
		} catch(HibernateException e) {
			if(trx != null) {
				try {trx.rollback(); } catch(HibernateException he) {}
			}
		} finally {
			try { if( session != null ) session.close(); } catch( Exception exC1 ) {}
		}
		
		return null;
	}

	@Override
	public DVD getDVD(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Music getMusic(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Book getBook(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Offer> getOffers(Product product) {
		// TODO Auto-generated method stub
		return null;
	}

}
