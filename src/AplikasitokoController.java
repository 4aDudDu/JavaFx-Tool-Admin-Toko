import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;



public class AplikasitokoController {

    Koneksi koneksi = new Koneksi();
    Connection connection = koneksi.getConnection();

    @FXML
    private TableView<Barang> tableView;
    @FXML
    private TableColumn<Barang, String> colNamaBarang;
    @FXML
    private TableColumn<Barang, Integer> colID;
    @FXML
    private TableColumn<Barang, String> colKategori;
    @FXML
    private TableColumn<Barang, String> colSupplier;
    @FXML
    private TableColumn<Barang, Integer> colQty;
    @FXML
    private TableColumn<Barang, Integer> colHarga;

    private ObservableList<Barang> listBarang = FXCollections.observableArrayList();

    
    @FXML
    private TextField tfNamaProdukNB;
    @FXML
    private TextField tfQtyNB;
    @FXML
    private TextField tfHargaNB;
    @FXML
    private TextField tfSupplierNB;
    @FXML
    private TextField tfIdNB;
    @FXML
    private TextField tfKategoriNB;

    
    @FXML
    private TextField tfNamaProdukSM;
    @FXML
    private TextField tfQtySM;
    @FXML
    private TextField tfIDSM;

    
    @FXML
    private TextField tfNamaProdukSK;
    @FXML
    private TextField tfQtySK;
    @FXML
    private TextField tfIDSK;
    
 
    @FXML
    private Button btEditLB;
    @FXML
    private Button btHapusLB;
    @FXML
    private TextField tfHapus;
    
    
    @FXML
    private TextField tfHargaED;

    @FXML
    private TextField tfIdED;

    @FXML
    private TextField tfKategoriED;

    @FXML
    private TextField tfNamaProdukED;

    @FXML
    private TextField tfQtyED;

    @FXML
    private TextField tfSupplierED;


    @FXML
    void btSimpanED(ActionEvent event) {
    String id = tfIdED.getText();
    String namaProduk = tfNamaProdukED.getText();
    String qty = tfQtyED.getText();
    String harga = tfHargaED.getText();
    String supplier = tfSupplierED.getText();
    String kategori = tfKategoriED.getText();

    if (!isIDNamaProdukMatch(id, namaProduk)) {
        showErrorNotification("Data Tidak Sesuai", "ID dan Nama Produk tidak cocok.");
        return;
    }

    
    String updateQuery = "UPDATE tokodatabase SET qty = ?, harga = ?, supplier = ?, kategori = ? WHERE ID = ? AND nama_barang = ?";
    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
        updateStatement.setInt(1, Integer.parseInt(qty));
        updateStatement.setInt(2, Integer.parseInt(harga));
        updateStatement.setString(3, supplier);
        updateStatement.setString(4, kategori);
        updateStatement.setInt(5, Integer.parseInt(id));
        updateStatement.setString(6, namaProduk);

        int rowsAffected = updateStatement.executeUpdate();

        if (rowsAffected > 0) {
            showSuccessNotification("Data berhasil diupdate!");
            clearFieldsED();
            loadDataAndSetToTable();
        } else {
            showErrorNotification("Data dengan ID " + id + " dan Nama Produk " + namaProduk + " tidak ditemukan!", "ID dan Nama Produk tidak cocok.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        showErrorNotification("Gagal melakukan update data!", "ID dan Nama Produk tidak cocok.");
    }
}

    private boolean isIDNamaProdukMatch(String id, String namaProduk) {
    String query = "SELECT * FROM tokodatabase WHERE ID = ? AND nama_barang = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setInt(1, Integer.parseInt(id));
        preparedStatement.setString(2, namaProduk);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            return resultSet.next();
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    private void clearFieldsED() {
    tfIdED.clear();
    tfNamaProdukED.clear();
    tfQtyED.clear();
    tfHargaED.clear();
    tfSupplierED.clear();
    tfKategoriED.clear();
}

    @FXML
    private void btSimpanNB(ActionEvent event) {
        if (isInputValid()) {
            String query = "INSERT INTO tokodatabase (nama_barang, qty, harga, supplier, kategori, ID) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, tfNamaProdukNB.getText());
                preparedStatement.setInt(2, Integer.parseInt(tfQtyNB.getText()));
                preparedStatement.setInt(3, Integer.parseInt(tfHargaNB.getText()));
                preparedStatement.setString(4, tfSupplierNB.getText());
                preparedStatement.setString(5, tfKategoriNB.getText());
                preparedStatement.setInt(6, Integer.parseInt(tfIdNB.getText()));

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    showSuccessNotification("Data sudah disimpan!");
                    clearFields();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void btSimpanSM(ActionEvent event) {
        if (validateInput()) {
            if (isProductExists(tfIDSM.getText(), tfNamaProdukSM.getText())) {
                String updateQuery = "UPDATE tokodatabase SET qty = qty + ? WHERE ID = ? AND nama_barang = ?";
                try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                    updateStatement.setInt(1, Integer.parseInt(tfQtySM.getText()));
                    updateStatement.setInt(2, Integer.parseInt(tfIDSM.getText()));
                    updateStatement.setString(3, tfNamaProdukSM.getText());

                    int rowsAffected = updateStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        showSuccessNotification("Data sudah disimpan!");
                        clearFieldsSM();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    showErrorNotification("Gagal melakukan update stok!", "ID dan Nama Produk tidak cocok.");
                }
            } else {
                showErrorNotification("Data tidak ada dalam database! Mohon tambah produk terlebih dahulu!", "ID dan Nama Produk tidak cocok.");
            }
        } else {
            showErrorNotification("Isi data terlebih dahulu!", "ID dan Nama Produk tidak cocok.");
        }
    }

    @FXML
    private void btSimpanSK(ActionEvent event) {
        if (validateInputSK()) {
            if (isProductExists(tfIDSK.getText(), tfNamaProdukSK.getText())) {
                String updateQuery = "UPDATE tokodatabase SET qty = qty - ? WHERE ID = ? AND nama_barang = ?";
                try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                    updateStatement.setInt(1, Integer.parseInt(tfQtySK.getText()));
                    updateStatement.setInt(2, Integer.parseInt(tfIDSK.getText()));
                    updateStatement.setString(3, tfNamaProdukSK.getText());

                    int rowsAffected = updateStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        showSuccessNotification("Data sudah disimpan!");
                        clearFieldsSK();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    showErrorNotification("Gagal melakukan update stok!", "ID dan Nama Produk tidak cocok.");
                }
            } else {
                showErrorNotification("Data tidak ada dalam database! Mohon tambah produk terlebih dahulu!", "ID dan Nama Produk tidak cocok.");
            }
        } else {
            showErrorNotification("Isi data terlebih dahulu!", "ID dan Nama Produk tidak cocok.");
        }
    }

    @FXML
    private void EditBarangLB(ActionEvent event) {
        openNewWindow("/view/editFX.fxml", "Edit Barang");
    }

    @FXML
    private void HapusBarangLB(ActionEvent event) {
        openNewWindow("/view/hapusFX.fxml", "Hapus Barang");
    }
    
    @FXML
    void btRes(ActionEvent event) {
        loadDataAndSetToTable();
    }
    
    @FXML
    private void btHapus(ActionEvent event) {
    String idToDelete = tfHapus.getText();
    if (idToDelete.isEmpty()) {
        showErrorNotification("Isi ID yang akan dihapus!", "ID dan Nama Produk tidak cocok.");
        return;
    }

    // Konfirmasi penghapusan
    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    confirmationAlert.setTitle("Konfirmasi Hapus");
    confirmationAlert.setHeaderText(null);
    confirmationAlert.setContentText("Anda yakin ingin menghapus data dengan ID " + idToDelete + "?");

    Optional<ButtonType> result = confirmationAlert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
        // Lakukan penghapusan data dari database
        deleteDataFromDatabase(idToDelete);
        }
    }

    private void deleteDataFromDatabase(String idToDelete) {
    String deleteQuery = "DELETE FROM tokodatabase WHERE ID = ?";

    try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
        preparedStatement.setInt(1, Integer.parseInt(idToDelete));

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            showSuccessNotification("Data berhasil dihapus!");
            loadDataAndSetToTable();
            tfHapus.clear();
        } else {
            showErrorNotification("Data dengan ID " + idToDelete + " tidak ditemukan!", "ID dan Nama Produk tidak cocok.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        showErrorNotification("Gagal menghapus data!", "ID dan Nama Produk tidak cocok.");
    }
}

    private boolean isProductExists(String id, String productName) {
        String query = "SELECT * FROM tokodatabase WHERE ID = ? AND nama_barang = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, Integer.parseInt(id));
            preparedStatement.setString(2, productName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean validateInputSK() {
        if (tfNamaProdukSK.getText().isEmpty() || tfQtySK.getText().isEmpty() || tfIDSK.getText().isEmpty()) {
            showErrorNotification("Isi data terlebih dahulu!", "ID dan Nama Produk tidak cocok.");
            return false;
        }

        try {
            Integer.parseInt(tfQtySK.getText());
            Integer.parseInt(tfIDSK.getText());
            return true;
        } catch (NumberFormatException e) {
            showErrorNotification("Isi sesuai data!", "ID dan Nama Produk tidak cocok.");
            return false;
        }
    }

    private void clearFieldsSK() {
        tfNamaProdukSK.clear();
        tfQtySK.clear();
        tfIDSK.clear();
    }

    private void clearFieldsSM() {
        tfNamaProdukSM.clear();
        tfQtySM.clear();
        tfIDSM.clear();
    }

    private boolean validateInput() {
        if (tfNamaProdukSM.getText().isEmpty() || tfQtySM.getText().isEmpty()) {
            showErrorNotification("Isi data terlebih dahulu!", "ID dan Nama Produk tidak cocok.");
            return false;
        }

        try {
            Integer.parseInt(tfQtySM.getText());
            return true;
        } catch (NumberFormatException e) {
            showErrorNotification("Isi sesuai data!", "ID dan Nama Produk tidak cocok.");
            return false;
        }
    }

    private boolean isInputValid() {
        if (tfNamaProdukNB.getText().isEmpty() || tfQtyNB.getText().isEmpty()
                || tfHargaNB.getText().isEmpty() || tfSupplierNB.getText().isEmpty()
                || tfKategoriNB.getText().isEmpty() || tfIdNB.getText().isEmpty()) {
            showErrorNotification("Isi data terlebih dahulu!", "ID dan Nama Produk tidak cocok.");
            return false;
        }

        try {
            Integer.parseInt(tfQtyNB.getText());
            Integer.parseInt(tfHargaNB.getText());
            Integer.parseInt(tfIdNB.getText());
            return true;
        } catch (NumberFormatException e) {
            showErrorNotification("Isi sesuai data!", "ID dan Nama Produk tidak cocok.");
            return false;
        }
    }

    private void clearFields() {
        tfNamaProdukNB.clear();
        tfQtyNB.clear();
        tfHargaNB.clear();
        tfSupplierNB.clear();
        tfKategoriNB.clear();
        tfIdNB.clear();
        tfQtySM.clear();
        tfIDSM.clear();
        tfNamaProdukSM.clear();
    }

    private void showSuccessNotification(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sukses");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorNotification(String message, String id_dan_Nama_Produk_tidak_cocok) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    
    
    @FXML
    private void initialize() {
        setupUIComponents();
 
        Platform.runLater(() -> {
            performHeavyInitialization();
            setupTableColumns();
            loadDataAndSetToTable();
            
        });
    }

    private void setupUIComponents() {
        
    }

    private void performHeavyInitialization() {
        
    }

    private void setupTableColumns() {
    colNamaBarang.setCellValueFactory(cellData -> cellData.getValue().namaBarangProperty());
    colID.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
    colKategori.setCellValueFactory(cellData -> cellData.getValue().kategoriProperty());
    colSupplier.setCellValueFactory(cellData -> cellData.getValue().supplierProperty());
    colQty.setCellValueFactory(cellData -> cellData.getValue().qtyProperty().asObject());
    colHarga.setCellValueFactory(cellData -> cellData.getValue().hargaProperty().asObject());

    }

    private void loadDataAndSetToTable() {
        listBarang.clear();

        String query = "SELECT * FROM tokodatabase";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String namaBarang = resultSet.getString("nama_barang");
                int id = resultSet.getInt("ID");
                String kategori = resultSet.getString("kategori");
                String supplier = resultSet.getString("supplier");
                int qty = resultSet.getInt("qty");
                int harga = resultSet.getInt("harga");

                Barang barang = new Barang(namaBarang, id, kategori, supplier, qty, harga);
                listBarang.add(barang);
            }

            tableView.setItems(listBarang);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openNewWindow(String fxmlFileName, String windowTitle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent root = loader.load();
            
            Stage newStage = new Stage();
            newStage.setTitle(windowTitle);
            newStage.setScene(new Scene(root));
            newStage.setResizable(false);
            newStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Error loading FXML file: " + fxmlFileName);
        }
    }

    public void handleNewBarangButtonClick() {
        openNewWindow("/view/newBarangFXML.fxml", "New Barang");
    }

    public void handleStokMasukButtonClick() {
        openNewWindow("/view/StokMasukFXML.fxml", "Stok Masuk");
    }

    public void handleStokKeluarButtonClick() {
        openNewWindow("/view/StokKeluarFXML.fxml", "Stok Keluar");
    }

    public void handleListBarangButtonClick() {
        openNewWindow("/view/listBarangFXML.fxml", "List Barang");
    }

    public void handleCreditButtonClick() {
        openNewWindow("/view/creditFXML.fxml", "Credit");
    }

    public void setPrimaryStage(Stage primaryStage) {
        primaryStage.setResizable(false);
    }

    public AplikasitokoController() {
     
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
