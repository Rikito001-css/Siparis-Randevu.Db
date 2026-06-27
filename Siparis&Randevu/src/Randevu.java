
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class Randevu extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Randevu.class.getName());

    private final Color kremZemin = new Color(250, 245, 236);
    private final Color koyuYesil = new Color(20, 51, 44);
    private final Color altinBronz = new Color(191, 149, 80);
    private final Color beyaz = Color.WHITE;
    private final Color yaziRengi = new Color(50, 50, 50);

    private JTable randevuTablosu;
    private DefaultTableModel tabloModeli;

    public Randevu() {
        initComponents();
        randevuTasariminiYukle();
        randevulariTabloyaDoldur();
    }

    private void randevuTasariminiYukle() {
        setTitle("Leziz Lezzetler - Randevu İşlemleri");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(kremZemin);
        setLayout(new BorderLayout(20, 20));

        // ==========================================
        // 1. ÜST PANEL (BAŞLIK VE GERİ BUTONU)
        // ==========================================
        JPanel ustPanel = new JPanel(new BorderLayout());
        ustPanel.setBackground(kremZemin);
        ustPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JButton btnGeri = new JButton("⬅ Ana Sayfaya Dön");
        btnGeri.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnGeri.setForeground(altinBronz);
        btnGeri.setContentAreaFilled(false);
        btnGeri.setBorderPainted(false);
        btnGeri.setFocusPainted(false);
        btnGeri.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel baslikLabel = new JLabel("Randevularım", SwingConstants.CENTER);
        baslikLabel.setFont(new Font("Serif", Font.ITALIC | Font.BOLD, 28));
        baslikLabel.setForeground(koyuYesil);

        ustPanel.add(btnGeri, BorderLayout.WEST);
        ustPanel.add(baslikLabel, BorderLayout.CENTER);
        // Sağ tarafa boşluk koyuyoruz ki başlık tam ortalansın
        ustPanel.add(Box.createRigidArea(new Dimension(130, 0)), BorderLayout.EAST);

        // ==========================================
        // 2. ORTA PANEL (TABLO)
        // ==========================================
        JPanel ortaPanel = new JPanel(new BorderLayout());
        ortaPanel.setBackground(kremZemin);
        ortaPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        // Tablo Sütunları
        String[] sutunlar = {"Randevu No", "Tarih", "Saat","bölge" ,"kişi sayısı", "notlar"};
        tabloModeli = new DefaultTableModel(sutunlar, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Kullanıcı tablodaki yazıları çift tıklayıp değiştiremesin
            }
        };

        randevuTablosu = new JTable(tabloModeli);
        randevuTablosu.setRowHeight(35); // Satır yüksekliği ferah olsun
        randevuTablosu.setFont(new Font("SansSerif", Font.PLAIN, 13));
        randevuTablosu.setForeground(yaziRengi);
        randevuTablosu.setSelectionBackground(new Color(230, 240, 235));
        randevuTablosu.setSelectionForeground(koyuYesil);
        randevuTablosu.setShowGrid(false); // Çirkin çizgileri gizle
        randevuTablosu.setIntercellSpacing(new Dimension(0, 0));

        // Tablo Başlığı (Header) Tasarımı
        JTableHeader header = randevuTablosu.getTableHeader();
        header.setBackground(koyuYesil);
        header.setForeground(beyaz);
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(100, 40));

        // Tablodaki yazıları ortalama
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < sutunlar.length; i++) {
            randevuTablosu.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(randevuTablosu);
        scrollPane.getViewport().setBackground(beyaz);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 215, 205), 1));

        ortaPanel.add(scrollPane, BorderLayout.CENTER);


        // ==========================================
        // 3. ALT PANEL (BUTONLAR)
        // ==========================================
        JPanel altPanel = new JPanel();
        altPanel.setLayout(new BoxLayout(altPanel, BoxLayout.Y_AXIS));
        altPanel.setBackground(kremZemin);
        altPanel.setBorder(BorderFactory.createEmptyBorder(15, 50, 30, 50));

        // 3.1. Güncelle ve Sil Butonları (Yan Yana)
        JPanel islemButonlariPanel = new JPanel(new GridLayout(1, 2, 20, 0)); // 20px boşlukla yan yana
        islemButonlariPanel.setBackground(kremZemin);

        JButton btnGuncelle = standartButonOlustur("✏️ Seçili Randevuyu Güncelle", altinBronz);
        JButton btnSil = standartButonOlustur("🗑️ Seçili Randevuyu İptal Et", new Color(180, 60, 60)); // Tatlı bir kırmızı

        islemButonlariPanel.add(btnGuncelle);
        islemButonlariPanel.add(btnSil);

        // 3.2. Yeni Randevu Al Butonu (Tek Başına Büyük)
        JButton btnYeniRandevu = standartButonOlustur("📅 YENİ RANDEVU AL", koyuYesil);
        btnYeniRandevu.setPreferredSize(new Dimension(0, 50)); // Daha kalın
        btnYeniRandevu.setFont(new Font("SansSerif", Font.BOLD, 16));

        altPanel.add(islemButonlariPanel);
        altPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Aralarına 15px boşluk
        altPanel.add(btnYeniRandevu);
        btnYeniRandevu.addActionListener(e -> {
            Randevu.this.dispose();
            new Randevu_olustur().setVisible(true);

        });

        // ==========================================
        // EKRANA YERLEŞTİRME
        // ==========================================
        add(ustPanel, BorderLayout.NORTH);
        add(ortaPanel, BorderLayout.CENTER);
        add(altPanel, BorderLayout.SOUTH);

        // ==========================================
        // AKSİYONLAR
        // ==========================================
        btnGeri.addActionListener(e -> {
            this.dispose();
            new Anasayfa().setVisible(true);
        });

        btnGuncelle.addActionListener(e -> {
            int seciliSatir = randevuTablosu.getSelectedRow();
            if (seciliSatir == -1) {
                JOptionPane.showMessageDialog(null, "Lütfen güncellemek istediğiniz randevuyu tablodan seçin!", "Uyarı", JOptionPane.WARNING_MESSAGE);
            } else {
                // Tablodaki "Randevu No" (0. Sütun) değerini alıyoruz
                int secilenId = Integer.parseInt(tabloModeli.getValueAt(seciliSatir, 0).toString());

                // Mevcut pencereyi kapatıp, Güncelleme penceresini bu ID ile açıyoruz
                Randevu.this.dispose();
                new Randevu_Guncelle(secilenId).setVisible(true);
            }
        });

        btnSil.addActionListener(e -> {
            int seciliSatir = randevuTablosu.getSelectedRow();
            if (seciliSatir == -1) {
                JOptionPane.showMessageDialog(null, "Lütfen iptal etmek istediğiniz randevuyu tablodan seçin!", "Uyarı", JOptionPane.WARNING_MESSAGE);
            } else {
                String randevuNo = tabloModeli.getValueAt(seciliSatir, 0).toString();
                int onay = JOptionPane.showConfirmDialog(null, randevuNo + " numaralı randevuyu iptal etmek istediğinize emin misiniz?", "İptal Onayı", JOptionPane.YES_NO_OPTION);
                if (onay == JOptionPane.YES_OPTION) {
                    tabloModeli.removeRow(seciliSatir);
                    JOptionPane.showMessageDialog(null, "Randevu başarıyla iptal edildi.");
                    // İleride buraya DELETE FROM randevular WHERE ... SQL kodunu yazacağız.
                }
            }
        });

        btnYeniRandevu.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Yeni Randevu oluşturma ekranı açılacak.");
            // İleride buraya yeni randevu alma frame'ine (örn: YeniRandevuEkrani) gidiş kodunu ekleyeceğiz.
        });
    }

    // Butonları hızlıca şıklaştıran yardımcı metot
    private JButton standartButonOlustur(String metin, Color arkaplan) {
        JButton btn = new JButton(metin);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBackground(arkaplan);
        btn.setForeground(beyaz);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 40));
        return btn;
    }

    private void randevulariTabloyaDoldur() {
        // ÖNEMLİ: NetBeans tasarım ekranındaki JTable'ının değişken adı neyse onu yazmalısın.
        // Eğer adını değiştirmediysen muhtemelen 'jTable1'dir. Aşağıdaki 'jTable1' yazısını ona göre düzenle.
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) randevuTablosu.getModel();
        model.setRowCount(0); // Sayfa her yenilendiğinde eski veriler üst üste binmesin diye tabloyu sıfırlıyoruz

        // Güvenlik Kontrolü: Oturum açılmamışsa veri çekmeye çalışma
        if (Oturum.aktifKullaniciId == -1) {
            return;
        }

        String url = "jdbc:mysql://localhost:3306/siparisrandevudb";
        String user = "root";
        String password = "";

        // Sadece giriş yapan kullanıcının randevularını çeken o sihirli sorgu
        String sql = "SELECT randevu_id, randevu_tarihi, randevu_saati, masa_bolgesi, kisi_sayisi, aciklama FROM randevular WHERE kullanici_id = ?";

        try {
            java.sql.Connection conn = java.sql.DriverManager.getConnection(url, user, password);
            java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, Oturum.aktifKullaniciId); // Hafızadaki kullanıcı ID'sini buraya bağlıyoruz
            java.sql.ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // Veritabanındaki sütun isimlerine göre verileri tek tek ayıklıyoruz
                int randevuNo = rs.getInt("randevu_id");
                String tarih = rs.getString("randevu_tarihi");
                String saat = rs.getString("randevu_saati"); // Eğer adını değiştirdiysen burayı da güncelle (örn: saat)
                String bolge = rs.getString("masa_bolgesi");
                String kisi = rs.getString("kisi_sayisi");
                String not = rs.getString("aciklama");

                // Aldığımız bu verileri senin o şık tablonun satırlarına döküyoruz
                Object[] satir = {randevuNo, tarih, saat, bolge, kisi, not, "Onaylandı"};
                model.addRow(satir);
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Randevular Listelenirken Hata Oluştu: " + ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
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
        java.awt.EventQueue.invokeLater(() -> new Randevu().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
