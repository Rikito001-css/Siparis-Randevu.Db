import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Randevular_Admin extends javax.swing.JFrame {
    
private final Color bgKoyu = new Color(30, 30, 30);
    private final Color panelGri = new Color(45, 45, 45);
    private final Color altinBronz = new Color(191, 149, 80);
    private final Color yaziBeyaz = new Color(240, 240, 240);

    private final String DB_URL = "jdbc:mysql://localhost:3306/siparisrandevudb";
    private final String DB_USER = "root";
    private final String DB_PASS = "";

    private JTable randevuTablosu;
    private DefaultTableModel tabloModeli;

    public Randevular_Admin() {
        setTitle("Leziz Lezzetler - Yönetim Paneli (Randevular)");
        setSize(1200, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(bgKoyu);

        // --- SOL TARAF: NAVİGASYON MENÜSÜ ---
        JPanel solMenuPaneli = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        solMenuPaneli.setBackground(panelGri);
        solMenuPaneli.setPreferredSize(new Dimension(200, 0));
        solMenuPaneli.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, altinBronz));

        JLabel lblLogo = new JLabel("<html><center>YÖNETİM<br>PANELİ</center></html>", SwingConstants.CENTER);
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblLogo.setForeground(altinBronz);
        lblLogo.setPreferredSize(new Dimension(200, 60));

        JButton btnSiparisler = butonOlustur("🍔 Siparişler", bgKoyu);
        btnSiparisler.setPreferredSize(new Dimension(180, 45));
        btnSiparisler.addActionListener(e -> {
            new Admin_Paneli().setVisible(true);
            this.dispose(); // Mevcut pencereyi kapat, siparişlere dön
        });
        JButton btnMenuYonetimi = butonOlustur("📋 Menü Yönetimi", bgKoyu);
        btnMenuYonetimi.setPreferredSize(new Dimension(180, 45));
        btnMenuYonetimi.addActionListener(e -> {
            new Menu_Yonetimi_Admin().setVisible(true);
            this.dispose(); // Mevcut sayfayı kapat, menü yönetimine geç
        });
        JButton btnkasa=butonOlustur("💵💵 Kasa", bgKoyu);
        btnkasa.setPreferredSize(new Dimension(180,45));
        btnkasa.addActionListener(e-> {this.dispose(); new Kasa_Admin().setVisible(true);});

        JButton btnRandevular = butonOlustur("📅 Randevular", altinBronz);
        btnRandevular.setForeground(Color.BLACK); // Aktif sekme belli olsun
        btnRandevular.setPreferredSize(new Dimension(180, 45));

        solMenuPaneli.add(lblLogo);
        solMenuPaneli.add(btnSiparisler);
        solMenuPaneli.add(btnRandevular);
        solMenuPaneli.add(btnMenuYonetimi);
        solMenuPaneli.add(btnkasa);
        add(solMenuPaneli, BorderLayout.WEST);

        // --- ÜST BAŞLIK ---
        JPanel merkezPanel = new JPanel(new BorderLayout(10, 10));
        merkezPanel.setBackground(bgKoyu);

        JLabel lblBaslik = new JLabel("Masa Rezervasyonu & Randevu Yönetimi", SwingConstants.LEFT);
        lblBaslik.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblBaslik.setForeground(yaziBeyaz);
        lblBaslik.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        merkezPanel.add(lblBaslik, BorderLayout.NORTH);

        // --- ORTA: RANDEVU TABLOSU ---
        String[] kolonlar = {"Randevu ID", "Kullanıcı Adı", "randevu_tarihi", "randevu_saati", "Durum"};
        tabloModeli = new DefaultTableModel(kolonlar, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        randevuTablosu = new JTable(tabloModeli);
        randevuTablosu.setRowHeight(30);
        randevuTablosu.setFont(new Font("SansSerif", Font.PLAIN, 14));
        randevuTablosu.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        randevuTablosu.setSelectionBackground(altinBronz);
        randevuTablosu.setSelectionForeground(Color.BLACK);

        JScrollPane scrollTablo = new JScrollPane(randevuTablosu);
        merkezPanel.add(scrollTablo, BorderLayout.CENTER);

        // --- SAĞ TARAF: KONTROL BUTONLARI ---
        JPanel sagButonPaneli = new JPanel(new GridLayout(4, 1, 0, 20));
        sagButonPaneli.setBackground(bgKoyu);
        sagButonPaneli.setBorder(BorderFactory.createEmptyBorder(50, 15, 20, 20));
        sagButonPaneli.setPreferredSize(new Dimension(220, 0));

        JButton btnOnayla = butonOlustur("✅ Onayla", new Color(60, 140, 60));
        btnOnayla.addActionListener(e -> durumuGuncelle("Onaylandı"));

        JButton btnIptal = butonOlustur("❌ İptal Et", new Color(180, 60, 60));
        btnIptal.addActionListener(e -> randevuyuIptalEt());

        sagButonPaneli.add(btnOnayla);
        sagButonPaneli.add(btnIptal);

        merkezPanel.add(sagButonPaneli, BorderLayout.EAST);
        add(merkezPanel, BorderLayout.CENTER);

        tabloyuYenile();
    }

    private JButton butonOlustur(String metin, Color arkaPlan) {
        JButton btn = new JButton(metin);
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setBackground(arkaPlan);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }

   private void tabloyuYenile() {
        tabloModeli.setRowCount(0); 
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            // İki tabloyu (randevular ve kullanicilar) birleştirip ismi çeken SQL sorgusu
            String sql = "SELECT r.randevu_id, CONCAT(k.isim, ' ', k.soyisim) AS musteri_adi, r.randevu_tarihi, r.randevu_saati, r.durum " +
                         "FROM randevular r " +
                         "INNER JOIN kullanicilar k ON r.kullanici_id = k.kullanici_id " +
                         "ORDER BY r.randevu_id DESC";
                         
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                tabloModeli.addRow(new Object[]{
                        rs.getInt("randevu_id"), 
                        rs.getString("musteri_adi"), // Artık ID değil, direkt isim ve soyisim geliyor
                        rs.getString("randevu_tarihi"),
                        rs.getString("randevu_saati"), 
                        rs.getString("durum")
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Tablo yenilenirken hata oluştu: " + ex.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void durumuGuncelle(String yeniDurum) {
        int seciliSatir = randevuTablosu.getSelectedRow();
        if (seciliSatir == -1) {
            JOptionPane.showMessageDialog(this, "Tablodan bir randevu seçin!", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int randevuId = (int) tabloModeli.getValueAt(seciliSatir, 0);
        veritabanindaDurumDegistir(randevuId, yeniDurum);
    }

    private void randevuyuIptalEt() {
        int seciliSatir = randevuTablosu.getSelectedRow();
        if (seciliSatir == -1) {
            JOptionPane.showMessageDialog(this, "Tablodan iptal edilecek randevuyu seçin!", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Yanlışlıkla tıklamalara karşı sadece basit bir evet/hayır onayı alıyoruz
        int onay = JOptionPane.showConfirmDialog(this, "Bu randevuyu iptal etmek istediğinize emin misiniz?", "İptal Onayı", JOptionPane.YES_NO_OPTION);
        
        if (onay == JOptionPane.YES_OPTION) {
            int randevuId = (int) tabloModeli.getValueAt(seciliSatir, 0);
            veritabanindaDurumDegistir(randevuId, "İptal Edildi");
        }
    }

    private void veritabanindaDurumDegistir(int randevuId, String durumMetni) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "UPDATE randevular SET durum = ? WHERE randevu_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, durumMetni);
            ps.setInt(2, randevuId);
            ps.executeUpdate();
            tabloyuYenile();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
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
        java.awt.EventQueue.invokeLater(() -> new Randevular_Admin().setVisible(true));
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
