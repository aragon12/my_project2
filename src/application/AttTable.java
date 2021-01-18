package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AttTable {
	private TableView<Person> tableView = new TableView<>();
	Database db = new Database();

	public void initialize() {
		// Show window
		try {
			buildData();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Stage stage = new Stage();
        stage.setTitle("Student Data");
        stage.setWidth(350);
        stage.setHeight(500);
        stage.setResizable(false);
        
        // desiging table
        tableView.setMaxWidth(290);
        tableView.setMaxHeight(490);
        
        // start vbox
        VBox vbox = new VBox();
        
        // start vbox2
        VBox vbox2 = new VBox();
        
        // start label
        Label label2 = new Label("List of all Students:");
        label2.setStyle("-fx-padding: 10 0 5 0");
        label2.setFont(new Font("Arial", 20));
        
        // end label
        vbox2.setAlignment(Pos.CENTER);
        vbox2.getChildren().addAll(label2, tableView);
        //end vbox2
        
        vbox.getChildren().addAll(vbox2);
        // end vbox
        
        Scene scene = new Scene(vbox);        
        stage.setScene(scene); 
        stage.show();
	}

	public void buildData() throws SQLException {
		// creates and fills table data
		db.init();
		ResultSet resultSet = db.db_get_data();
		ObservableList dbData = FXCollections.observableArrayList(dataBaseArrayList(resultSet));

		// Giving readable names to columns
		for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
			TableColumn column = new TableColumn<>();
			switch (resultSet.getMetaData().getColumnName(i + 1)) {
			case "f_name":
				column.setText("First Name");
				break;
			case "l_name":
				column.setText("Last Name");
				break;
			case "rollno":
				column.setText("Roll No");
				break;
			case "present":
				column.setText("Marked");
				break;
			default:
				// if column name in SQL Database is not
				// found, then TableView column receive
				// SQL Database current column name (not
				// readable)
				column.setText(resultSet.getMetaData().getColumnName(i + 1));
				break;
			}
			
			// Setting cell property value to correct variable from Person class
			column.setCellValueFactory(new PropertyValueFactory<>(resultSet.getMetaData().getColumnName(i + 1)));
			tableView.getColumns().add(column);
		}

		// Filling up tableView with data
		tableView.setItems(dbData);
	}

	public class Person {

		StringProperty f_name = new SimpleStringProperty();
		StringProperty l_name = new SimpleStringProperty();
		IntegerProperty rollno = new SimpleIntegerProperty();
		StringProperty present = new SimpleStringProperty();

		public StringProperty f_nameProperty() {
			return f_name;
		}

		public StringProperty l_nameProperty() {
			return l_name;
		}

		public IntegerProperty rollnoProperty() {
			return rollno;
		}

		public StringProperty presentProperty() {
			return present;
		}

		public Person(String f_nameValue, String l_nameValue, int rollnoValue, String presentValue) {
			
			f_name.set(f_nameValue);
			l_name.set(l_nameValue);
			rollno.set(rollnoValue);
			present.set(presentValue);
		}

		Person() {}
	}

	private ArrayList dataBaseArrayList(ResultSet resultSet) throws SQLException {
		//extracts data from resultset
		// and stores in array
		// then returns that array		
		ArrayList<Person> data = new ArrayList<>();
		
		while (resultSet.next()) {
			Person person = new Person();
			person.f_name.set(resultSet.getString("f_name"));
			person.l_name.set(resultSet.getString("l_name"));
			person.rollno.set(resultSet.getInt("rollno"));

			if (resultSet.getInt("present") == 0) {
				person.present.set("Absent");
			} else {
				person.present.set("Present");
			}
			
			data.add(person);
		}
		return data;
	}
}