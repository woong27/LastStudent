package student;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DBConnection {
	private Connection connection = null;
	private Statement statement = null;
	private ResultSet rs = null;

	// connection
	public void connect() {

		Properties properties = new Properties();
		// properties
		FileInputStream fis;
		try {
			fis = new FileInputStream("C:/java_test/student/src/student/db.properties");
			properties.load(fis);
		} catch (FileNotFoundException e) {
			System.out.println("FileInputStream error" + e.getMessage());
		} catch (IOException e) {
			System.out.println("properties error" + e.getStackTrace());
		}

		try {
			Class.forName(properties.getProperty("driver"));
			connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("userid"),
					properties.getProperty("password"));
		} catch (ClassNotFoundException e) {
			System.out.println("Class.forName error" + e.getMessage());

		} catch (SQLException e) {
			System.out.println("connection error" + e.getStackTrace());
		}
	}

	// insert statement
	public int insert(Student student) {
		PreparedStatement ps = null;
		int insertReturnValue = -1;

		String insertQuery = "call procedure_insert_student( ?, ?, ?, ?, ?)";
		try {
			ps = connection.prepareStatement(insertQuery);
			ps.setString(1, student.getNo());
			ps.setString(2, student.getName());
			ps.setInt(3, student.getKor());
			ps.setInt(4, student.getEng());
			ps.setInt(5, student.getMath());

			insertReturnValue = ps.executeUpdate();
		} catch (Exception e1) {
			System.out.println("Exception error" + e1.getMessage());

		} finally {
			try {
				if (ps != null) {
					ps.close();
				}

			} catch (SQLException e) {
				System.out.println("PreparedStatement close error" + e.getStackTrace());
			}
		}

		return insertReturnValue;
	}

	// delete statement
	public int delete(String no) {
		PreparedStatement ps = null;
		int deleteReturnValue = -1;
		String deleteQuery = "delete from Student where no = ?";
		try {
			ps = connection.prepareStatement(deleteQuery);
			ps.setString(1, no);

			deleteReturnValue = ps.executeUpdate();
		} catch (Exception e1) {
			System.out.println("Exception error " + e1.getMessage());

		} finally {
			try {
				if (ps != null) {
					ps.close();
				}

			} catch (SQLException e) {
				System.out.println("PreparedStatement close error" + e.getStackTrace());
			}
		}

		return deleteReturnValue;
	}

	// selete no or name serach Statement
	public List<Student> selectSearch(String data, int type) {
		PreparedStatement ps = null;

		ResultSet rs = null;
		List<Student> list = new ArrayList<Student>();
		String selectSearchQuery = "select * from student where ";

		try {
			switch (type) {
			case 1:
				selectSearchQuery += "no like ? ;";
				break;
			case 2:
				selectSearchQuery += "name like ? ;";
				break;
			default:
				System.out.println("잘못 입력하였습니다. ");
				return list;
			}

			ps = connection.prepareStatement(selectSearchQuery);

			// ps.executeQuery (query)
			String namePattern = "%" + data + "%";
			ps.setString(1, namePattern);
			rs = ps.executeQuery();

			if (!(rs == null || rs.isBeforeFirst())) {
				return list;
			}

			while (rs.next()) {
				String no = rs.getString("no");
				String name = rs.getString("name");
				int kor = rs.getInt("kor");
				int eng = rs.getInt("eng");
				int math = rs.getInt("math");
				int total = rs.getInt("total");
				double avr = rs.getDouble("avr");
				String grade = rs.getString("grade");
				int rate = rs.getInt("rate");

				list.add(new Student(no, name, kor, eng, math, total, avr, grade, rate));
			}

		} catch (Exception e) {
			System.out.println("Exception error" + e.getStackTrace());
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				System.out.println("PreparedStatement close error" + e.getStackTrace());
			}
		}

		return list;
	}

	// select statement
	public List<Student> select() {
		List<Student> list = new ArrayList<Student>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String selectQuery = "select * from Student";
		try {
			ps = connection.prepareStatement(selectQuery);

			// ps.executeQuery(query)
			rs = ps.executeQuery(selectQuery);

			if (!(rs == null || rs.isBeforeFirst())) {
				return list;
			}

			while (rs.next()) {
				String no = rs.getString("no");
				String name = rs.getString("name");
				int kor = rs.getInt("kor");
				int eng = rs.getInt("eng");
				int math = rs.getInt("math");
				int total = rs.getInt("total");
				double avr = rs.getDouble("avr");
				String grade = rs.getString("grade");
				int rate = rs.getInt("rate");

				list.add(new Student(no, name, kor, eng, math, total, avr, grade, rate));
			}

		} catch (Exception e1) {
			System.out.println("select error " + e1.getMessage());

		} finally {
			try {
				if (ps != null) {
					ps.close();
				}

			} catch (SQLException e) {
				System.out.println("PreparedStatement close error" + e.getStackTrace());
			}
		}

		return list;
	}

	// Update Statement
	public int update(Student st) {
		PreparedStatement ps = null;
		String insertQuery = "call procedure_update_student(?, ?, ?, ?, ?)";
		int updateReturnValue = -1;

		try {
			ps = connection.prepareStatement(insertQuery);
			ps.setString(1, st.getNo());
			ps.setString(2, st.getName());
			ps.setInt(3, st.getKor());
			ps.setInt(4, st.getEng());
			ps.setInt(5, st.getMath());
			updateReturnValue = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException error" + e.getMessage());
		} catch (Exception e) {
			System.out.println("Exception error" + e.getStackTrace());
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				System.out.println("PreparedStatement close error" + e.getStackTrace());
			}
		}

		return updateReturnValue;
	}

	// select order by statement
	public List<Student> selectOrderBy(int type) {
		List<Student> list = new ArrayList<Student>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String selectOrderByQuery = "select * from Student order by ";
		try {
			switch (type) {
			case 1:
				selectOrderByQuery += "no asc ";
				break;
			case 2:
				selectOrderByQuery += "name asc ";
				break;
			case 3:
				selectOrderByQuery += "total desc ";
				break;
			default:
				System.out.println("정렬 타입 에러");
				return list;

			}
			ps = connection.prepareStatement(selectOrderByQuery);

			// ps.executeQuery(query)
			rs = ps.executeQuery();

			if (!(rs == null || rs.isBeforeFirst())) {
				return list;
			}

			int rank = 0;
			while (rs.next()) {
				String no = rs.getString("no");
				String name = rs.getString("name");
				int kor = rs.getInt("kor");
				int eng = rs.getInt("eng");
				int math = rs.getInt("math");
				int total = rs.getInt("total");
				double avr = rs.getDouble("avr");
				String grade = rs.getString("grade");
				int rate = rs.getInt("rate");
				if (type == 3) {
					rate = ++rank;

				}

				list.add(new Student(no, name, kor, eng, math, total, avr, grade, rate));
			}

		} catch (Exception e1) {
			System.out.println("select error " + e1.getMessage());

		} finally {
			try {
				if (ps != null) {
					ps.close();
				}

			} catch (SQLException e) {
				System.out.println("PreparedStatement close error" + e.getStackTrace());
			}
		}

		return list;

	}

	// select Max, Min Statement
	public List<Student> selectMaxMin(int type) {
		List<Student> list = new ArrayList<Student>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String selectMaxMinQuery = "select * from student where total = ";
		try {
			switch (type) {
			case 1:
				selectMaxMinQuery += "(select max(total) from student)";
				break;
			case 2:
				selectMaxMinQuery += "(select min(total) from student)";
				break;
			default:
				System.out.println("Max Min 타입 에러");
				return list;

			}
			ps = connection.prepareStatement(selectMaxMinQuery);

			// ps.executeQuery(query)
			rs = ps.executeQuery();

			if (!(rs == null || rs.isBeforeFirst())) {
				return list;
			}

			while (rs.next()) {
				String no = rs.getString("no");
				String name = rs.getString("name");
				int kor = rs.getInt("kor");
				int eng = rs.getInt("eng");
				int math = rs.getInt("math");
				int total = rs.getInt("total");
				double avr = rs.getDouble("avr");
				String grade = rs.getString("grade");
				int rate = rs.getInt("rate");
				list.add(new Student(no, name, kor, eng, math, total, avr, grade, rate));
			}

		} catch (Exception e1) {
			System.out.println("select Max Min error" + e1.getMessage());

		} finally {
			try {
				if (ps != null) {
					ps.close();
				}

			} catch (SQLException e) {
				System.out.println("PreparedStatement close error" + e.getStackTrace());
			}
		}

		return list;
	}

	// connection close
	public void close() {
		try {
			if (statement != null) {
				statement.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			System.out.println("connection error" + e.getStackTrace());

		}
	}

}
