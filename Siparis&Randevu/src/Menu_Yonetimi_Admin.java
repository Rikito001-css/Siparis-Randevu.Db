import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
public class Menu_Yonetimi_Admin extends javax.swing.JFrame {
    
private final Color bgKoyu = new Color(30, 30, 30);
    private final Color panelGri = new Color(45, 45, 45);
    private final Color altinBronz = new Color(191, 149, 80);
    private final Color yaziBeyaz = new Color(240, 240, 240);

    private final String DB_URL = "jdbc:mysql://localhost:3306/siparisrandevudb";
    private final String DB_USER = "root";
    private final String DB_PASS = "";

    private JComboBox<String> cmbTumUrunler;
    private JTable pasifTablosu;
    private DefaultTableModel tabloModeli;

    public Menu_Yonetimi_Admin() {
        setTitle("Leziz Lezzetler - Menü Stok Yönetimi");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(bgKoyu);

        // --- ÜST BAŞLIK ---
        JLabel lblBaslik = new JLabel("Menü & Stok Yönetimi", SwingConstants.CENTER);
        lblBaslik.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblBaslik.setForeground(altinBronz);
        lblBaslik.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(lblBaslik, BorderLayout.NORTH);

        // --- SOL TARAF: ÜRÜN KAPATMA VE GERİ DÖNÜŞ ---
        // Geri dön butonunu eklemek için satır sayısını 3'ten 4'e çıkardık
        JPanel solPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        solPanel.setBackground(bgKoyu);
        solPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 100, 20));
        solPanel.setPreferredSize(new Dimension(350, 0));

        JLabel lblSec = new JLabel("Kapatılacak Ürünü Seçin:");
        lblSec.setForeground(yaziBeyaz);
        lblSec.setFont(new Font("SansSerif", Font.BOLD, 14));

        String[] tumYemekler = {
            "Söğüt Kebabı", "Hünkar Beğendi", "İskender", "Adana", "Urfa", "Kuzu Tandır", "Beyti", "Testi Kebabı", "Ali Nazik", "Çökertme", "Orman Kebabı", "Tas Kebabı", "Ankara Tava", "Büryan", "Tokat Kebabı", "Kilis Tava", "Patlıcan Kebabı", "İslim Kebabı", "Etli Sarma", "Kuru Patlıcan Dolma", "Keşkek", "Güveçte Kuru Fasulye", "Etli Nohut", "Sac Kavurma", "Elbasan Tava", "Hamsili Pilav", "Bafra Pidesi", "Etli Ekmek", "Lahmacun", "Cantık", "Mantı", "Piliç Topkapı", "Şeftali Kebabı", "Kuzu İncik", "Kağıt Kebabı", "Kiremitte Köfte", "Ciğer Şiş", "Tepsi Kebabı",
            "Haydari", "Humus", "Şakşuka", "Atom", "Girit Ezmesi", "Fava", "Muhammara", "Sarma", "Çerkez Tavuğu", "Tarator", "Acılı Ezme", "Babagannuş", "Patlıcan Salatası", "Arnavut Ciğeri", "Çiğ Köfte", "İmam Bayıldı", "Enginar", "Barbunya", "Kısır", "Köpoğlu", "Pırasa", "Pilaki", "Lakerda", "Midye Dolma", "Deniz Börülcesi",
            "Padişah Lokumu", "Künefe", "Havuç Dilimi", "Baklava", "Şöbiyet", "Katmer", "İrmik Helvası", "Sütlaç", "Keşkül", "Kazandibi", "Tavuk Göğsü", "Aşure", "Zerde", "Un Helvası", "Höşmerim", "Revani", "Şekerpare", "Ekmek Kadayıfı", "Kemalpaşa", "Ayva Tatlısı", "Kabak Tatlısı", "Güllaç", "Dilber Dudağı", "Bülbül Yuvası", "Sütlü Nuriye", "Tulumba",
            "Osmanlı Şerbeti", "Reyhan Şerbeti", "Kızılcık Şerbeti", "Gül Şerbeti", "Hardaliye", "Şıra", "Susurluk Ayranı", "Yayık Ayranı", "Acılı Şalgam", "Acısız Şalgam", "Boza", "Salep", "Türk Kahvesi", "Menengiç", "Dibek", "Mırra", "Semaver Çayı", "İnce Belli Çay", "Ihlamur", "Adaçayı", "Kuşburnu", "Maden Suyu", "Niğde Gazozu", "Tereyağlı Pilav", "Firik", "Bulgur", "İç Pilav", "Siyez", "Şehriye Pilavı", "Su Böreği", "Paçanga", "Sigara Böreği", "Çiğ Börek", "Gül Böreği", "Anne Patatesi", "Kumpir", "Mücver", "Kuru Cacık", "Semizotu", "Gözleme", "Lavaş", "Turşu", "Acı Biber"
        };
        
        Arrays.sort(tumYemekler);
        cmbTumUrunler = new JComboBox<>(tumYemekler);

        JButton btnKapat = new JButton("🚫 Satışa Kapat");
        btnKapat.setBackground(new Color(180, 60, 60));
        btnKapat.setForeground(Color.WHITE);
        btnKapat.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnKapat.addActionListener(e -> urunuKapat());

        // --- İŞTE ÇIKIŞ KAPISI ---
        JButton btnGeri = new JButton("⬅️ Admin Paneline Dön");
        btnGeri.setBackground(panelGri);
        btnGeri.setForeground(Color.WHITE);
        btnGeri.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGeri.addActionListener(e -> {
            new Admin_Paneli().setVisible(true); // Admin ana sayfasını aç
            this.dispose(); // Mevcut sayfayı kapat
        });

        solPanel.add(lblSec);
        solPanel.add(cmbTumUrunler);
        solPanel.add(btnKapat);
        solPanel.add(btnGeri); // Geri butonunu en alta ekledik
        add(solPanel, BorderLayout.WEST);

        // --- SAĞ TARAF: KAPALI ÜRÜNLER LİSTESİ ---
        JPanel sagPanel = new JPanel(new BorderLayout(5, 5));
        sagPanel.setBackground(bgKoyu);
        sagPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 30));

        JLabel lblListe = new JLabel("Şu An Satışa Kapalı Olanlar:");
        lblListe.setForeground(altinBronz);
        lblListe.setFont(new Font("SansSerif", Font.BOLD, 14));
        sagPanel.add(lblListe, BorderLayout.NORTH);

        tabloModeli = new DefaultTableModel(new String[]{"ID", "Kapatılan Ürün Adı"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        pasifTablosu = new JTable(tabloModeli);
        pasifTablosu.setRowHeight(25);
        
        pasifTablosu.getColumnModel().getColumn(0).setMinWidth(0);
        pasifTablosu.getColumnModel().getColumn(0).setMaxWidth(0);

        JScrollPane scroll = new JScrollPane(pasifTablosu);
        sagPanel.add(scroll, BorderLayout.CENTER);

        JButton btnGeriAc = new JButton("✅ Seçileni Tekrar Satışa Aç");
        btnGeriAc.setBackground(new Color(60, 140, 60));
        btnGeriAc.setForeground(Color.WHITE);
        btnGeriAc.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGeriAc.addActionListener(e -> urunuGeriAc());
        sagPanel.add(btnGeriAc, BorderLayout.SOUTH);

        add(sagPanel, BorderLayout.CENTER);

        tabloyuYenile();
    }

    private void urunuKapat() {
        String secilenUrun = cmbTumUrunler.getSelectedItem().toString();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sqlKontrol = "SELECT * FROM pasif_urunler WHERE urun_adi = ?";
            PreparedStatement psKontrol = conn.prepareStatement(sqlKontrol);
            psKontrol.setString(1, secilenUrun);
            if (psKontrol.executeQuery().next()) {
                JOptionPane.showMessageDialog(this, "Bu ürün zaten satışa kapalı!", "Uyarı", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String sqlEkle = "INSERT INTO pasif_urunler (urun_adi) VALUES (?)";
            PreparedStatement psEkle = conn.prepareStatement(sqlEkle);
            psEkle.setString(1, secilenUrun);
            psEkle.executeUpdate();
            
            JOptionPane.showMessageDialog(this, secilenUrun + " başarıyla menüden kaldırıldı.");
            tabloyuYenile();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage());
        }
    }

    private void urunuGeriAc() {
        int secili = pasifTablosu.getSelectedRow();
        if (secili == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen tekrar satışa açmak için sağdaki listeden bir ürün seçin.");
            return;
        }

        int id = (int) tabloModeli.getValueAt(secili, 0);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "DELETE FROM pasif_urunler WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Ürün tekrar menüye eklendi ve satışa açıldı!");
            tabloyuYenile();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage());
        }
    }

    private void tabloyuYenile() {
        tabloModeli.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT * FROM pasif_urunler ORDER BY id DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tabloModeli.addRow(new Object[]{rs.getInt("id"), rs.getString("urun_adi")});
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Menu_Yonetimi_Admin().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
