package media.mediadbimpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import media.definitions.Book;
import media.definitions.Category;
import media.definitions.DVD;
import media.definitions.MediaDbInterface;
import media.definitions.Music;
import media.definitions.Offer;
import media.definitions.Product;
import media.definitions.Review;
import media.definitions.SQLResult;

import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.Type;

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

			sessionFactory = new Configuration().configure()
					.buildSessionFactory();
			System.out.println("Connection established!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void finish() {
		try {
			sessionFactory.close();
			dbCon.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<Product> getProducts(String namePattern) {
		Session session = null;
		Transaction trx = null;
		List<Product> results = new ArrayList<Product>();

		try {
			session = sessionFactory.openSession();
			trx = session.beginTransaction();

			/*
			 * Criteria crit =
			 * session.createCriteria(Person.class).add(Restrictions
			 * .like("name", "Mario%")); System.out.println(crit.list().size());
			 */

			/*
			 * Criteria crit =
			 * session.createCriteria(Product.class).add(Restrictions
			 * .like("title", namePattern)); results.addAll(crit.list());
			 */
			// System.out.println(crit.list().size());

			Query q = session.createQuery("from Review");
			System.out.println(q.list().size());
			Iterator it = q.list().iterator();
			while (it.hasNext()) {
				System.out.println(((Review) it.next()).getHelpful_vo());
			}

			// trx.commit();
			return results;
		} catch (HibernateException e) {
			e.printStackTrace();
			if (trx != null) {
				try {
					trx.rollback();
				} catch (HibernateException he) {
				}
			}
		} finally {
			try {
				if (session != null)
					session.close();
			} catch (Exception exCl) {
			}
		}

		return null;
	}

	@Override
	public SQLResult executeSqlQuery(String query) throws Exception {
		Session session = null;
		Transaction trx = null;
		SQLResult result = new SQLResult();

		try {
			session = sessionFactory.openSession();
			trx = session.beginTransaction();

			PreparedStatement statement = session.connection()
					.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();

			// create header
			String[] header = new String[rsmd.getColumnCount()];
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				header[i - 1] = (rsmd.getColumnName(i));
			}
			result.setHeader(header);

			// create body
			List<String[]> body = new ArrayList<String[]>();
			while (rs.next()) {
				String[] values = new String[rsmd.getColumnCount()];
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					values[i - 1] = rs.getString(i);
				}
				body.add(values);
			}
			result.setBody(body);
		} catch (HibernateException e) {
			if (trx != null) {
				try {
					trx.rollback();
				} catch (HibernateException he) {
				}
			}
		} finally {
			try {
				if (session != null)
					session.close();
			} catch (Exception exC1) {
			}
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

			Query q = session.createQuery(query);
			Type singleType = q.getReturnTypes()[0];
			boolean nichtprimitiv = singleType.isAssociationType();
			ClassMetadata metadata = sessionFactory.getClassMetadata(singleType
					.getName());
			if (nichtprimitiv) {
				String[] propertyNames = metadata.getPropertyNames();
				result.setHeader(propertyNames);
				Type[] propertyTypes = metadata.getPropertyTypes();
				List<Object> objectlist = q.list();
				Iterator<Object> it = objectlist.iterator();
				while (it.hasNext()) {
					Object next = it.next();
					Object[] values = metadata.getPropertyValues(next, EntityMode.POJO);
					String[] row = new String[values.length];
					for (int i = 0; i < values.length; i++) {
						//System.out.println(values[i].getClass().toString());
						if (values[i].getClass().toString().startsWith("class java.lang") || deref) {
							row[i] = values[i].toString();
						}
					}
					result.addRow(row);
				}
			}

			/*
			 * result.setHeader(new String[]{"1", "2"}); List<String[]> list =
			 * new ArrayList<String[]>(); list.add(new String[]{"3", "4"});
			 * result.setBody(list);
			 */

		} catch (HibernateException e) {
			e.printStackTrace();
			if (trx != null) {
				try {
					trx.rollback();
				} catch (HibernateException he) {
				}
			}
		} finally {
			try {
				if (session != null)
					session.close();
			} catch (Exception exC1) {
			}
		}
		return result;
	}

	@Override
	public Category getCategoryTree() {
		Session session = null;
		Transaction trx = null;

		try {
			session = sessionFactory.openSession();
			trx = session.beginTransaction();

			Criteria parentNode = session.createCriteria(Category.class).add(
					Restrictions.eq("name", "parent"));

			Category cat = (Category) parentNode.list().get(0);

			return cat;
		} catch (HibernateException e) {
			e.printStackTrace();
			if (trx != null) {
				try {
					trx.rollback();
				} catch (HibernateException he) {
				}
			}
		}
		return null;
	}

	/* klapt is aber langsam */
	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getProductsByCategoryPath(Category[] categoriesPath) {
		Session session = null;
		List<Product> resultList = new ArrayList<Product>();

		try {
			session = sessionFactory.openSession();
			for (int i = 0; i < categoriesPath.length; i++) {
				Query q1 = session
						.createSQLQuery("SELECT ASIN FROM V_TEILVON WHERE NAMEN = '"
								+ categoriesPath[i] + "'");

				for (int j = 0; j < q1.list().size(); j++) {
					resultList.addAll((List<Product>) session.createQuery(
							"from Product where asin='" + q1.list().get(j)
									+ "'").list());
				}
			}

			return resultList;
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Product> getReviewProducts() {
		Session session = null;
		List<Product> productList = new ArrayList<Product>();

		try {
			session = sessionFactory.openSession();
			/*
			 * List list =
			 * session.createQuery("from Product where reviews is not null"
			 * ).list(); //productList.addAll(list); Iterator it =
			 * list.iterator(); System.out.println(list.size());
			 * while(it.hasNext()) { Product p = (Product)it.next();
			 * if(!p.getReviews().isEmpty()) productList.add(p); }
			 */

			return productList;
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void addNewReview(Review review) {
		Session session = null;
		Transaction trx = null;

		try {
			session = sessionFactory.openSession();
			trx = session.beginTransaction();

			Review rev = review;

			trx.commit();
		} catch (HibernateException e) {
			if (trx != null) {
				try {
					trx.rollback();
				} catch (HibernateException he) {
				}
			}
		} finally {
			try {
				if (session != null)
					session.close();
			} catch (Exception exC1) {
			}
		}
	}

	@Override
	public Product getProduct(String id) {
		Session session = null;
		Transaction trx = null;

		try {
			session = sessionFactory.openSession();
			trx = session.beginTransaction();

			Criteria cr = session.createCriteria(Product.class);
			cr.add(Restrictions.eq("asin", id));

			List result = cr.list();
			Iterator it = result.iterator();
			if (it.hasNext())
				return (Product) it.next();
			else
				return null;
		} catch (HibernateException e) {
			if (trx != null) {
				try {
					trx.rollback();
				} catch (HibernateException he) {
				}
			}
		} finally {
			try {
				if (session != null)
					session.close();
			} catch (Exception exC1) {
			}
		}

		return null;
	}

	@Override
	public DVD getDVD(String id) {
		Session session = null;
		Transaction trx = null;

		try {
			session = sessionFactory.openSession();
			trx = session.beginTransaction();

			Criteria cr = session.createCriteria(DVD.class);
			cr.add(Restrictions.eq("asin", id));

			List result = cr.list();
			Iterator it = result.iterator();
			if (it.hasNext())
				return (DVD) it.next();
		} catch (HibernateException e) {
			if (trx != null) {
				try {
					trx.rollback();
				} catch (HibernateException he) {
				}
			}
		} finally {
			try {
				if (session != null)
					session.close();
			} catch (Exception exC1) {
			}
		}
		return null;
	}

	@Override
	public Music getMusic(String id) {
		Session session = null;
		Transaction trx = null;

		try {
			session = sessionFactory.openSession();
			trx = session.beginTransaction();

			Criteria cr = session.createCriteria(Music.class);
			cr.add(Restrictions.eq("asin", id));

			List result = cr.list();
			Iterator it = result.iterator();
			if (it.hasNext())
				return (Music) it.next();
		} catch (HibernateException e) {
			if (trx != null) {
				try {
					trx.rollback();
				} catch (HibernateException he) {
				}
			}
		} finally {
			try {
				if (session != null)
					session.close();
			} catch (Exception exC1) {
			}
		}
		return null;
	}

	@Override
	public Book getBook(String id) {
		Session session = null;
		Transaction trx = null;

		try {
			session = sessionFactory.openSession();
			trx = session.beginTransaction();

			Criteria cr = session.createCriteria(Book.class);
			cr.add(Restrictions.eq("asin", id));

			List result = cr.list();
			Iterator it = result.iterator();
			if (it.hasNext())
				return (Book) it.next();
		} catch (HibernateException e) {
			if (trx != null) {
				try {
					trx.rollback();
				} catch (HibernateException he) {
				}
			}
		} finally {
			try {
				if (session != null)
					session.close();
			} catch (Exception exC1) {
			}
		}
		return null;
	}

	@Override
	public List<Offer> getOffers(Product product) {
		Session session = null;
		Transaction trx = null;
		List<Offer> offers = new ArrayList<Offer>();
		
		try {
			session = sessionFactory.openSession();
			trx = session.beginTransaction();

			Criteria crit = session.createCriteria(Offer.class).add(
					Restrictions.eq("product", product)); // probably not :/
			offers.addAll((List<Offer>) crit.list());

		} catch (HibernateException e) {
			if (trx != null) {
				try {
					trx.rollback();
				} catch (HibernateException he) {
				}
			}
		} finally {
			try {
				if (session != null)
					session.close();
			} catch (Exception exCl) {
			}
		}
		return offers;
	}

}
