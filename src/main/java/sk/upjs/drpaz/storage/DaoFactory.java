package sk.upjs.drpaz.storage;

import org.springframework.jdbc.core.JdbcTemplate;

import com.mysql.cj.jdbc.MysqlDataSource;

public enum DaoFactory {

	INSTANCE;

	private JdbcTemplate jdbcTemplate;
	private ProductDao productDao;
	private PurchaseDao purchaseDao;
	private EmployeeDao employeeDao;
	private CategoryDao categoryDao;
	private boolean testing = false;

	public void setTesting() {
		this.testing = true;
	}

	public JdbcTemplate getJdbcTemplate() {
		if (jdbcTemplate == null) {
			MysqlDataSource dataSource = new MysqlDataSource();
			if (testing) {
				dataSource.setDatabaseName("mydbtest");
			} else {
				dataSource.setDatabaseName("mydb");
			}
			dataSource.setUser("drpaz");
			dataSource.setPassword("drpaz");
			jdbcTemplate = new JdbcTemplate(dataSource);
		}
		return jdbcTemplate;
	}

	public ProductDao getProductDao() {
		if (productDao == null)
			productDao = new MysqlProductDao(getJdbcTemplate());
		return productDao;
	}

	public PurchaseDao getPurchaseDao() {
		if (purchaseDao == null)
			purchaseDao = new MysqlPurchaseDao(getJdbcTemplate());
		return purchaseDao;
	}

	public EmployeeDao getEmployeeDao() {
		if (employeeDao == null)
			employeeDao = new MysqlEmployeeDao(getJdbcTemplate());
		return employeeDao;
	}

	public CategoryDao getCategoryDao() {
		if (categoryDao == null)
			categoryDao = new MysqlCategoryDao(getJdbcTemplate());
		return categoryDao;
	}

	public boolean isTesting() {
		return testing;
	}

}
