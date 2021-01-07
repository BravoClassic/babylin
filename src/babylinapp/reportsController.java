package babylinapp;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class reportsController {

    @FXML
    private ImageView reportProductsImage;
    @FXML
    private Tab products;
    @FXML
    private Tab revenue;
    @FXML
    private DatePicker endDate;
    @FXML
    private DatePicker startDate;
    @FXML
    private ImageView reportRevenueImage;
    @FXML
    private Button back;
    @FXML
    private Button generateReportButton;
    @FXML
    private ComboBox<String> comboBoxReportType;



    @FXML
    private void generateReport() {
        if (comboBoxReportType.getSelectionModel().getSelectedIndex() == -1) {
            Controller.showAlert(Alert.AlertType.ERROR, generateReportButton.getScene().getWindow(), "Error", "Select Report Type!");
            return;
        }
        if (comboBoxReportType.getValue() == null){
            Controller.showAlert(Alert.AlertType.ERROR,comboBoxReportType.getScene().getWindow(),"Error","Select a value!");
            return;
        }
        if (startDate.getValue() == null) {
            Controller.showAlert(Alert.AlertType.ERROR, comboBoxReportType.getScene().getWindow(), "Error", "Select Start Date!");
            return;
        }
        if (endDate.getValue() == null) {
            Controller.showAlert(Alert.AlertType.ERROR, comboBoxReportType.getScene().getWindow(), "Error", "Select End Date!");
            return;
        }

        if ("Daily".equals(comboBoxReportType.getValue()) && revenue.isSelected() ) {
            try {
                Connection connection = DriverManager.getConnection(jdbcController.url);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT date(RevenueDate), sum(Amount) FROM babylinapp_revenue WHERE RevenueDate => ? AND RevenueDate =< ? group by date(RevenueDate)");
                preparedStatement.setDate(1, java.sql.Date.valueOf(startDate.getValue()));
                preparedStatement.setDate(2, java.sql.Date.valueOf(endDate.getValue()));
                ResultSet resultSet = preparedStatement.executeQuery();
                DefaultCategoryDataset line_chart_chart = new DefaultCategoryDataset();
                while (resultSet.next()) {
                    line_chart_chart.addValue(
                            resultSet.getDouble(2),
                            "Revenue",
                            java.sql.Date.valueOf(resultSet.getString(1)).toLocalDate().getMonth() + "/" + java.sql.Date.valueOf(resultSet.getString(1)).toLocalDate().getDayOfMonth()
                    );
                }

                JFreeChart jFreeChartLine = ChartFactory.createLineChart(
                        "Revenue For " + startDate.getValue() + " to " + endDate.getValue(),
                        "Each day from " + startDate.getValue() + " to " + endDate.getValue(),
                        "Revenue",
                        line_chart_chart,
                        PlotOrientation.VERTICAL,
                        true,
                        true,
                        false);
                CategoryAxis categoryAxis = jFreeChartLine.getCategoryPlot().getDomainAxis();
//                    categoryAxis.setTickUnit(new NumberTickUnit(60));
                int width = 800;
                int height = 480;
                long reportTimeGenerated = LocalDateTime.now().toInstant(ZoneOffset.UTC).getEpochSecond();
                File file = new File(System.getProperty("user.dir") + "\\src\\babylinapp\\reports\\chart.png");
                ChartUtilities.saveChartAsPNG(file, jFreeChartLine, width, height);
                preparedStatement.close();
                resultSet.close();
                connection.close();
                reportRevenueImage.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "\\src\\babylinapp\\reports\\chart.png")));
                createPdf(reportTimeGenerated);
            } catch (SQLException | IOException e) {
                Controller.errBox(e.getMessage() + "\n" + e.getLocalizedMessage() + "\n", "Error with generating reports", "Error");
            }
        }else if ("Daily".equals(comboBoxReportType.getValue()) && products.isSelected()){
            try {
                Connection connection = DriverManager.getConnection(jdbcController.url);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT date(RevenueDate), sum(Amount) FROM babylinapp_revenue WHERE RevenueDate => ? AND RevenueDate =< ? group by date(RevenueDate)");
                preparedStatement.setDate(1, java.sql.Date.valueOf(startDate.getValue()));
                preparedStatement.setDate(2, java.sql.Date.valueOf(endDate.getValue()));
                ResultSet resultSet = preparedStatement.executeQuery();
                DefaultCategoryDataset line_chart_chart = new DefaultCategoryDataset();
                while (resultSet.next()) {
                    line_chart_chart.addValue(
                            resultSet.getDouble(2),
                            "Revenue",
                            java.sql.Date.valueOf(resultSet.getString(1)).toLocalDate().getMonth() + "/" + java.sql.Date.valueOf(resultSet.getString(1)).toLocalDate().getDayOfMonth()
                    );
                }

                JFreeChart jFreeChartLine = ChartFactory.createLineChart(
                        "Products For " + startDate.getValue() + " to " + endDate.getValue(),
                        "Each day from " + startDate.getValue() + " to " + endDate.getValue(),
                        "Quantity Purchased",
                        line_chart_chart,
                        PlotOrientation.VERTICAL,
                        true,
                        true,
                        false);
                CategoryAxis categoryAxis = jFreeChartLine.getCategoryPlot().getDomainAxis();
//                    categoryAxis.setTickUnit(new NumberTickUnit(60));
                int width = 800;
                int height = 480;
                long reportTimeGenerated = LocalDateTime.now().toInstant(ZoneOffset.UTC).getEpochSecond();
                File file = new File(System.getProperty("user.dir") + "\\src\\babylinapp\\reports\\chart.png");
                ChartUtilities.saveChartAsPNG(file, jFreeChartLine, width, height);
                preparedStatement.close();
                resultSet.close();
                connection.close();
                reportProductsImage.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "\\src\\babylinapp\\reports\\chart.png")));
                createPdf(reportTimeGenerated);
            } catch (SQLException | IOException e) {
                Controller.errBox(e.getMessage() + "\n" + e.getLocalizedMessage() + "\n", "Error with generating reports", "Error");
            }
        }
    }

    private void createPdf ( Long t){
            String path = System.getProperty("user.dir");
            String fileName = null;
            if (revenue.isSelected()){
            fileName = path + "\\src\\babylinapp\\reports\\reportsRevenue" + t + ".pdf";
        } else if(products.isSelected()){
            fileName = path + "\\src\\babylinapp\\reports\\reportsProducts" + t + ".pdf";
        }
            PDDocument pdDocument = new PDDocument();
            PDPage pdPage = new PDPage();
            try {
                pdDocument.getDocumentInformation().setTitle("Babylin Consult Report");
                pdDocument.getDocumentInformation().setAuthor("Babylin Consult");
                pdDocument.getDocumentInformation().setCreator("Babylin Consult Stock Management App - PDFBox API");
                pdDocument.getDocumentInformation().setSubject("Report");
                Calendar date = new GregorianCalendar();
                date.setTime(Date.from(Instant.now()));
                pdDocument.getDocumentInformation().setCreationDate(date);
                pdDocument.getDocumentInformation().setModificationDate(date);
                pdDocument.getDocumentInformation().setKeywords("reports");
                pdDocument.addPage(pdPage);
                assert fileName != null;
                pdDocument.save(fileName);
                pdDocument.close();
                insertContent(fileName, t);
            } catch (IOException e) {
                Controller.errBox(e.getMessage()+"\n"+e.getLocalizedMessage()+"\n","Error with generating reports","Error");
            }
        }
    private void insertContent(String pathname, Long t){
            File file = new File(pathname);
            try {
                PDDocument document = PDDocument.load(file);
                PDPage pdPage = document.getPages().get(0);
                PDPageContentStream pdPageContentStream = new PDPageContentStream(document, pdPage);

                PDImageXObject pdImage = PDImageXObject.createFromFile(System.getProperty("user.dir")+"\\src\\babylinapp\\reports\\chart.png", document);
                PDImageXObject pdImage1=PDImageXObject.createFromFile(System.getProperty("user.dir")+"\\src\\babylinapp\\assets\\Logo1000.jpg",document);
                pdPageContentStream.beginText();
                pdPageContentStream.setFont(PDType1Font.TIMES_ROMAN, 20);
                pdPageContentStream.setLeading(14.5f);
                pdPageContentStream.newLineAtOffset(25, 725);
                pdPageContentStream.showText("Reports - "+Date.from(Instant.ofEpochSecond(t)));
                pdPageContentStream.newLine();
                pdPageContentStream.endText();
                pdPageContentStream.drawImage(pdImage1,400,700);
                pdPageContentStream.drawImage(pdImage, 25, 200);
                pdPageContentStream.close();
                document.save(file);
                document.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    @FXML
    public void goMenu() throws IOException {
        Stage stage = (Stage) back.getScene().getWindow();
        new Menu().start(stage);
    }

    }



