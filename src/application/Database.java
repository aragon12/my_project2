package application;

import java.sql.*;
import java.util.ArrayList;

class Database {
	public int code;

	public String fname;
	public String Lname;
	public int reg;
	public int age;
	public String sec;

	public final String Database_name = "ghosteye";
	public final String Database_user = "root";
	public final String Database_pass = "";

	public Connection con;

	public boolean init() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			try {
				this.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + Database_name, Database_user,
						Database_pass);
			} catch (SQLException e) {

				System.out.println("Error: Database Connection Failed ! Please check the connection Setting");

				return false;

			}

		} catch (ClassNotFoundException e) {

			e.printStackTrace();

			return false;
		}

		return true;
	}

	public void insert() {
		String sql = "INSERT INTO face_bio (code, first_name, last_name, reg, age , section) VALUES (?, ?, ?, ?,?,?)";

		PreparedStatement statement = null;
		try {
			statement = con.prepareStatement(sql);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {

			statement.setInt(1, this.code);
			statement.setString(2, this.fname);

			statement.setString(3, this.Lname);
			statement.setInt(4, this.reg);
			statement.setInt(5, this.age);
			statement.setString(6, this.sec);

			int rowsInserted = statement.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("A new face data was inserted successfully!");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void insert_stu() {
		// Insert a new student data
		// for attendence
		String sql = "INSERT IGNORE INTO att_data (f_name, l_name, rollno, present) VALUES (?, ?, ?, ?)";
		PreparedStatement statement = null;

		try {

			statement = con.prepareStatement(sql);
			statement.setString(1, this.fname);
			statement.setString(2, this.Lname);
			statement.setInt(3, this.reg);
			statement.setInt(4, 0);

			int rowsInserted = statement.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("A new Student was inserted successfully!");
			} else {
				System.out.println("Student already exists");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ResultSet db_get_data() {
		// fetches all records from att_data
		// and returns resultset
		String sql = "SELECT * FROM att_data";

		// Extracting data from Databasee
		ResultSet resultset = null;
		PreparedStatement statement = null;
		try {
			statement = con.prepareStatement(sql);
			resultset = statement.executeQuery();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return resultset;
	}

	public void setPresent(int roll) {
		// updates attendence status to present
		// by roll no
		String sql = "UPDATE att_data SET present=? WHERE rollno=?";
		PreparedStatement statement = null;

		try {

			statement = con.prepareStatement(sql);
			statement.setInt(1, 1);
			statement.setInt(2, roll);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean isStudPresent(int roll) {
		// Returns if student is present or not
		if (getAttStat(roll) == 1) {
			return true;
		}
		return false;
	}

	public int getAttStat(int roll) {
		// Returns the Present/Absent Status of Student
		// By Roll No
		String sql = "SELECT present from att_data WHERE rollno=?";
		int fetch_stat = -1;
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			statement = con.prepareStatement(sql);
			statement.setInt(1, roll);
			rs = statement.executeQuery();

			while (rs.next()) {
				fetch_stat = rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("Roll no: " + roll + " stat: " + fetch_stat);

		return fetch_stat;
	}

	public ArrayList<String> getUser(int inCode) throws SQLException {

		ArrayList<String> user = new ArrayList<String>();

		try {

			String sql = "select * from face_bio where code=" + inCode + " limit 1";
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery(sql);

			while (rs.next()) {

				/*
				 * app.setCode(rs.getInt(2)); app.setFname(rs.getString(3));
				 * app.setLname(rs.getString(4)); app.setReg(rs.getInt(5));
				 * app.setAge(rs.getInt(6)); app.setSec(rs.getString(7));
				 */

				user.add(0, Integer.toString(rs.getInt(2)));
				user.add(1, rs.getString(3));
				user.add(2, rs.getString(4));
				user.add(3, Integer.toString(rs.getInt(5)));
				user.add(4, Integer.toString(rs.getInt(6)));
				user.add(5, rs.getString(7));

				/*
				 * System.out.println(app.getCode()); System.out.println(app.getFname());
				 * System.out.println(app.getLname()); System.out.println(app.getReg());
				 * System.out.println(app.getAge()); System.out.println(app.getSec());
				 */

				// nString="Name:" + rs.getString(3)+" "+rs.getString(4) +
				// "\nReg:" + app.getReg() +"\nAge:"+app.getAge() +"\nSection:"
				// +app.getSec() ;

				// System.out.println(nString);
			}

			con.close(); // closing connection
		} catch (Exception e) {
			e.getStackTrace();
		}
		return user;
	}

	public void db_close() throws SQLException {
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return Lname;
	}

	public void setLname(String lname) {
		Lname = lname;
	}

	public int getReg() {
		return reg;
	}

	public void setReg(int reg) {
		this.reg = reg;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getSec() {
		return sec;
	}

	public void setSec(String sec) {
		this.sec = sec;
	}

}
