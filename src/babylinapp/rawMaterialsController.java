package babylinapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class rawMaterialsController implements Initializable {
    @FXML
     Label rawPrice;

    @FXML
    Label rawQuantity;

    @FXML
    TextArea rawDesc;

    @FXML
    Label rawName;

    @FXML
    ComboBox<String> comboRawName;

//    @FXML
//    TableView<rawMaterialsClass> table;

//    @FXML
//    TableColumn<rawMaterialsClass, String> columnRawMaterials;

    @FXML
    Button back;

    @FXML
    Button next;

    @FXML
    Button previous;

    @FXML
    Button newBtn;

    @FXML
    Button add;

    @FXML
    Button delete;

    @FXML
    Button update;

    ObservableList<rawMaterialsClass> rawMaterialsList = FXCollections.observableArrayList();

    @FXML
    protected void displayMaterial(){
        rawName.setText("Name: "+rawMaterialsList.get(comboRawName.getSelectionModel().getSelectedIndex()).getRawMaterialsName());
        rawPrice.setText("Unit Price: "+rawMaterialsList.get(comboRawName.getSelectionModel().getSelectedIndex()).getRawMaterialsPrice());
        rawQuantity.setText("Quantity Available: "+rawMaterialsList.get(comboRawName.getSelectionModel().getSelectedIndex()).getRawMaterialsQuantity());
        rawDesc.setText(rawMaterialsList.get(comboRawName.getSelectionModel().getSelectedIndex()).getRawMaterialsDescription());
    }

    protected void viewRawMaterials() throws SQLException {

        Connection connection = DriverManager.getConnection(jdbcController.url, jdbcController.user, jdbcController.password);
        PreparedStatement preparedStatement = connection.prepareStatement(jdbcController.SELECT_QUERY_RAW);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String result = resultSet.getString("rawMaterialName");
            Integer result1=resultSet.getInt("quantity");
            Double result2= resultSet.getDouble("unitPrice");
            String result3 = resultSet.getString("rawMaterialDescription");
            rawMaterialsList.add(new rawMaterialsClass(result, result1, result2, result3));
        }
        connection.close();

    }

    public void addRawMaterials() throws IOException {
        Stage stage = (Stage) add.getScene().getWindow();
        new addRawMaterialsPage().start(stage);
    }

    public void deleteRawMaterials() throws IOException{
        Stage stage = (Stage) delete.getScene().getWindow();
        new deleteRawMaterialsPage().start(stage);
    }
    public void newRawMaterials() throws IOException{
        Stage stage = (Stage) newBtn.getScene().getWindow();
        new newRawMaterialsPage().start(stage);
    }

    public void updateRawMaterials() throws IOException {
        Stage stage = (Stage) update.getScene().getWindow();
        new updateRawMaterialsPage().start(stage);
    }

    public void goBack() throws IOException {
        Stage stage = (Stage) back.getScene().getWindow();
        new Menu().start(stage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            viewRawMaterials();
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        columnRawMaterials.setCellValueFactory(new PropertyValueFactory<>("rawMaterialsName"));
//        table.setItems(rawMaterialsList);
//        System.out.println(table.getSelectionModel().getSelectedItem().getProductName());
    }
}
