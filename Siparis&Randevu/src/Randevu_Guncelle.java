
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Randevu_Guncelle extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Randevu_Guncelle.class.getName());
    private int guncellenecekId; // Tablodan gelen randevu numarası
    private final Color kremZemin = new Color(250, 245, 236);
    private final Color koyuYesil = new Color(20, 51, 44);
    private final Color altinBronz = new Color(191, 149, 80);
    private final Color beyaz = Color.WHITE;
    private final Color yaziRengi = new Color(50, 50, 50);
    private final Color GriKenarlik = new Color(220, 215, 205);

    private JTextField txtTarih, txtNotlar;
    private JComboBox<String> cmbSaat, cmbMasaBolgesi, cmbKisiSayisi;
    public Randevu_Guncelle(int id) {
        initComponents();
        this.guncellenecekId = id;
        tasarimiYukle();
        eskiVerileriDoldur();
    }

private void tasarimiYukle() {
        setTitle("Leziz Lezzetler - Randevu Güncelle");
        setSize(480, 680); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(kremZemin);
        setLayout(new GridBagLayout()); 

        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.dispose();
            }
        };
        cardPanel.setOpaque(false);
        cardPanel.setBackground(beyaz);
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(235, 230, 220), 1, true),
                BorderFactory.createEmptyBorder(25, 40, 25, 40)
        ));
        cardPanel.setPreferredSize(new Dimension(400, 600));

        JLabel titleLabel = new JLabel("Randevu Güncelle");
        titleLabel.setFont(new Font("Serif", Font.ITALIC | Font.BOLD, 28)); 
        titleLabel.setForeground(altinBronz); // Güncellemeye özel bronz başlık
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subTitleLabel = new JLabel("ID: #" + guncellenecekId + " numaralı kaydı düzenliyorsunuz");
        subTitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subTitleLabel.setForeground(yaziRengi);
        subTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtTarih = new JTextField();
        kutuyuModernlestir(txtTarih);

        String[] saatler = {"Seçiniz...", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00"};
        cmbSaat = new JComboBox<>(saatler);
        acilirMenuyuModernlestir(cmbSaat);

        String[] bolgeler = {"Seçiniz...", "Cam Kenarı", "Teras (Açık Alan)", "İç Salon", "VIP Loca"};
        cmbMasaBolgesi = new JComboBox<>(bolgeler);
        acilirMenuyuModernlestir(cmbMasaBolgesi);

        String[] kisiler = {"Seçiniz...", "1 Kişi", "2 Kişi", "3 Kişi", "4 Kişi", "5 Kişi", "6+ Kişi (Grup)"};
        cmbKisiSayisi = new JComboBox<>(kisiler);
        acilirMenuyuModernlestir(cmbKisiSayisi);

        txtNotlar = new JTextField();
        kutuyuModernlestir(txtNotlar);

        JButton btnGuncelleKaydet = new JButton("Değişiklikleri Kaydet");
        btnGuncelleKaydet.setMaximumSize(new Dimension(320, 42));
        btnGuncelleKaydet.setBackground(altinBronz);
        btnGuncelleKaydet.setForeground(beyaz);
        btnGuncelleKaydet.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGuncelleKaydet.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnGuncelleKaydet.setFocusPainted(false);
        btnGuncelleKaydet.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnIptal = new JButton("İptal Et ve Geri Dön");
        btnIptal.setMaximumSize(new Dimension(320, 30));
        btnIptal.setForeground(new Color(180, 60, 60)); 
        btnIptal.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnIptal.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIptal.setContentAreaFilled(false); 
        btnIptal.setBorderPainted(false);
        btnIptal.setFocusPainted(false);
        btnIptal.setCursor(new Cursor(Cursor.HAND_CURSOR));

        cardPanel.add(titleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(subTitleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20))); 
        
        alaniEkle(cardPanel, "Randevu Tarihi", txtTarih);
        alaniEkleAcilirMenu(cardPanel, "Randevu Saati", cmbSaat);
        alaniEkleAcilirMenu(cardPanel, "Masa Bölgesi", cmbMasaBolgesi);
        alaniEkleAcilirMenu(cardPanel, "Kişi Sayısı", cmbKisiSayisi);
        alaniEkle(cardPanel, "Özel İstek / aciklama", txtNotlar);
        
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cardPanel.add(btnGuncelleKaydet);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(btnIptal);

        add(cardPanel);

        // --- AKSİYONLAR ---
        btnIptal.addActionListener(e -> {
            this.dispose();
            new Randevu().setVisible(true); 
        });

        // ==========================================
        // GÜNCELLEME MOTORU (UPDATE SORGUSU)
        // ==========================================
        btnGuncelleKaydet.addActionListener(e -> {
            String url = "jdbc:mysql://localhost:3306/siparisrandevudb";
            String user = "root";
            String password = "";

            try {
                Connection conn = DriverManager.getConnection(url, user, password);
                
                // DİKKAT: Veritabanındaki kimlik sütununun adını (id, randevu_id vb.) WHERE kısmına doğru yaz!
                String sql = "UPDATE randevular SET randevu_tarihi=?, randevu_saati=?, masa_bolgesi=?, kisi_sayisi=?, aciklama=? WHERE randevu_id=?"; // <--- 'id' ismini veritabanındakiyle aynı yap
                PreparedStatement pstmt = conn.prepareStatement(sql);

                pstmt.setString(1, txtTarih.getText());
                pstmt.setString(2, cmbSaat.getSelectedItem().toString());
                pstmt.setString(3, cmbMasaBolgesi.getSelectedItem().toString());
                pstmt.setString(4, cmbKisiSayisi.getSelectedItem().toString());
                pstmt.setString(5, txtNotlar.getText());
                pstmt.setInt(6, guncellenecekId); // WHERE şartı için seçili ID'yi veriyoruz

                int etkilenen = pstmt.executeUpdate();
                if (etkilenen > 0) {
                    JOptionPane.showMessageDialog(null, "Randevu başarıyla güncellendi!");
                    this.dispose();
                    new Randevu().setVisible(true); // Güncel tabloyu aç
                }

                pstmt.close();
                conn.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Güncelleme Hatası: " + ex.getMessage());
            }
        });
    }

    // ==========================================
    // ESKİ VERİLERİ VERİTABANINDAN ÇEKME MOTORU
    // ==========================================
    private void eskiVerileriDoldur() {
        String url = "jdbc:mysql://localhost:3306/siparisrandevudb";
        String user = "root";
        String password = "";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            // DİKKAT: Veritabanındaki kimlik sütununun adını WHERE kısmına doğru yaz!
            String sql = "SELECT * FROM randevular WHERE randevu_id = ?"; // <--- 'id' ismini veritabanındakiyle aynı yap
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, guncellenecekId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                txtTarih.setText(rs.getString("randevu_tarihi"));
                cmbSaat.setSelectedItem(rs.getString("randevu_saati"));
                cmbMasaBolgesi.setSelectedItem(rs.getString("masa_bolgesi"));
                cmbKisiSayisi.setSelectedItem(rs.getString("kisi_sayisi"));
                txtNotlar.setText(rs.getString("aciklama"));
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Veriler çekilirken hata oluştu: " + ex.getMessage());
        }
    }

    // --- YARDIMCI METOTLAR ---
    private void alaniEkle(JPanel panel, String baslik, JComponent kutu) {
        JLabel etiket = new JLabel(baslik);
        etiket.setFont(new Font("SansSerif", Font.BOLD, 12));
        etiket.setForeground(yaziRengi);
        etiket.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(etiket);
        panel.add(Box.createRigidArea(new Dimension(0, 4)));
        panel.add(kutu);
        panel.add(Box.createRigidArea(new Dimension(0, 12))); 
    }
    private void alaniEkleAcilirMenu(JPanel panel, String baslik, JComboBox<String> menu) {
        alaniEkle(panel, baslik, menu);
    }
    private void kutuyuModernlestir(JTextField field) {
        field.setMaximumSize(new Dimension(320, 32));
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBackground(new Color(248, 248, 246)); 
        field.setForeground(yaziRengi);
        field.setCaretColor(koyuYesil);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GriKenarlik, 1, true),
                BorderFactory.createEmptyBorder(2, 8, 2, 8) 
        ));
    }
    private void acilirMenuyuModernlestir(JComboBox<String> combo) {
        combo.setMaximumSize(new Dimension(320, 32));
        combo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        combo.setBackground(beyaz);
        combo.setForeground(yaziRengi);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 495, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 364, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
