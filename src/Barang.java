import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Barang {

    private final SimpleStringProperty namaBarang;
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty kategori;
    private final SimpleStringProperty supplier;
    private final SimpleIntegerProperty qty;
    private final SimpleIntegerProperty harga;

    public Barang(String namaBarang, int id, String kategori, String supplier, int qty, int harga) {
        this.namaBarang = new SimpleStringProperty(namaBarang);
        this.id = new SimpleIntegerProperty(id);
        this.kategori = new SimpleStringProperty(kategori);
        this.supplier = new SimpleStringProperty(supplier);
        this.qty = new SimpleIntegerProperty(qty);
        this.harga = new SimpleIntegerProperty(harga);
    }

    public String getNamaBarang() {
        return namaBarang.get();
    }

    public SimpleStringProperty namaBarangProperty() {
        return namaBarang;
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public String getKategori() {
        return kategori.get();
    }

    public SimpleStringProperty kategoriProperty() {
        return kategori;
    }

    public String getSupplier() {
        return supplier.get();
    }

    public SimpleStringProperty supplierProperty() {
        return supplier;
    }

    public int getQty() {
        return qty.get();
    }

    public SimpleIntegerProperty qtyProperty() {
        return qty;
    }

    public int getHarga() {
        return harga.get();
    }

    public SimpleIntegerProperty hargaProperty() {
        return harga;
    }
}
