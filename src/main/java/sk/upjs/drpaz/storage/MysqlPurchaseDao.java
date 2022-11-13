package sk.upjs.drpaz.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public class MysqlPurchaseDao implements PurchaseDao {
	private JdbcTemplate jdbcTemplate;

	public MysqlPurchaseDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Purchase getById(long id) throws NoSuchElementException {
		String sql = "SELECT id, employee_id, created_at FROM Purchase";
		return jdbcTemplate.queryForObject(sql, new RowMapper<Purchase>() {

			@Override
			public Purchase mapRow(ResultSet rs, int rowNum) throws SQLException {
				Purchase purchase = new Purchase();
				purchase.setId(rs.getLong("id"));
				purchase.setEmployee(DaoFactory.INSTANCE.getEmployeeDao().getById(rs.getLong("employee_id")));
				purchase.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

				return purchase;
			}

		});
	}

	public Purchase save(Purchase purchase) throws NullPointerException, NoSuchElementException {
		if (purchase == null)
			throw new NullPointerException("Cannot save null purchase");
		if (purchase.getId() == null) {
			SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
			simpleJdbcInsert.withTableName("Purchase");
			simpleJdbcInsert.usingGeneratedKeyColumns("id");
			simpleJdbcInsert.usingColumns("employee_id");
			simpleJdbcInsert.usingColumns("created_at");

			Map<String, Object> values = new HashMap<>();

			values.put("employee_id", purchase.getEmployee().getId());
			values.put("created_at", purchase.getCreatedAt());

			long id = simpleJdbcInsert.executeAndReturnKey(values).longValue();
			Purchase purchase2 = new Purchase(purchase.getId(), purchase.getEmployee(), purchase.getCreatedAt());
			return purchase2;

		} else { // UPDATE
			String sql = "UPDATE purchase SET employee_id=?, created_at=? " + "WHERE id = ?";
			int changed = jdbcTemplate.update(sql, purchase.getEmployee().getId(), purchase.getCreatedAt());
			if (changed == 1) {
				return purchase;
			} else {
				throw new NoSuchElementException("No purchase with id " + purchase.getId() + " in DB");

			}
		}

	}

}
