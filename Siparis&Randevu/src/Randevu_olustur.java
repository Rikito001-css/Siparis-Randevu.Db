
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Randevu_olustur extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Randevu_olustur.class.getName());
    private final Color kremZemin = new Color(250, 245, 236);
    private final Color koyuYesil = new Color(20, 51, 44);
    private final Color altinBronz = new Color(191, 149, 80);
    private final Color beyaz = Color.WHITE;
    private final Color yaziRengi = new Color(50, 50, 50);
    private final Color GriKenarlik = new Color(220, 215, 205);

private JTextField txtTarih, txtNotlar;
    private JComboBox<String> cmbSaat, cmbMasaBolgesi, cmbKisiSayisi;

    public Randevu_olustur() {
        initComponents();
        olusturmaTasariminiYukle();
    }
private void olusturmaTasariminiYukle() {
        setTitle("Leziz Lezzetler - Yeni Randevu Oluştur");
        setSize(480, 680); // Yeni alanlar için ekranı biraz daha uzattık
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(kremZemin);
        setLayout(new GridBagLayout()); 

        // ANA KART (Gölgeli ve yuvarlatılmış panel)
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

        // 1. BAŞLIK ALANI 
        JLabel titleLabel = new JLabel("Yeni Randevu");
        titleLabel.setFont(new Font("Serif", Font.ITALIC | Font.BOLD, 28)); 
        titleLabel.setForeground(koyuYesil);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subTitleLabel = new JLabel("Detayları belirleyin, masanızı ayırtın");
        subTitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subTitleLabel.setForeground(altinBronz);
        subTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 2. GİRİŞ ALANLARI (Tüm Eksikler Giderildi)
        txtTarih = new JTextField();
        kutuyuModernlestir(txtTarih, "Örn: 15.06.2026");

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
        kutuyuModernlestir(txtNotlar, "Örn: Doğum günü pastası rica ediyoruz...");

        // 3. BUTONLAR
        JButton btnKaydet = new JButton("Randevuyu Onayla");
        btnKaydet.setMaximumSize(new Dimension(320, 42));
        btnKaydet.setBackground(koyuYesil);
        btnKaydet.setForeground(beyaz);
        btnKaydet.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnKaydet.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnKaydet.setFocusPainted(false);
        btnKaydet.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnKaydet.setBorder(BorderFactory.createEmptyBorder());

        JButton btnIptal = new JButton("İptal Et ve Geri Dön");
        btnIptal.setMaximumSize(new Dimension(320, 30));
        btnIptal.setForeground(new Color(180, 60, 60)); 
        btnIptal.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnIptal.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIptal.setContentAreaFilled(false); 
        btnIptal.setBorderPainted(false);
        btnIptal.setFocusPainted(false);
        btnIptal.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // EKRANA YERLEŞTİRME
        cardPanel.add(titleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(subTitleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20))); 
        
        alaniEkle(cardPanel, "Randevu Tarihi", txtTarih);
        alaniEkleAcilirMenu(cardPanel, "Randevu Saati", cmbSaat);
        alaniEkleAcilirMenu(cardPanel, "Masa Bölgesi", cmbMasaBolgesi);
        alaniEkleAcilirMenu(cardPanel, "Kişi Sayısı", cmbKisiSayisi);
        alaniEkle(cardPanel, "Özel İstek / Notlar", txtNotlar);
        
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cardPanel.add(btnKaydet);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(btnIptal);

        // ==========================================
        // AKSİYONLAR (GERÇEK MOTOR BURADA)
        // ==========================================
        btnIptal.addActionListener(e -> {
            Randevu_olustur.this.dispose();
            new Randevu().setVisible(true); 
        });

        btnKaydet.addActionListener(e -> {
            String secilenTarih = txtTarih.getText();
            String secilenSaat = cmbSaat.getSelectedItem().toString();
            String secilenBolge = cmbMasaBolgesi.getSelectedItem().toString();
            String secilenKisi = cmbKisiSayisi.getSelectedItem().toString();
            String girilenNot = txtNotlar.getText();

            // Placeholder (örnek metin) temizliği
            if (girilenNot.equals("Örn: Doğum günü pastası rica ediyoruz...")) girilenNot = "";

            // Güvenlik Kontrolü
            if(secilenTarih.isEmpty() || secilenTarih.equals("Örn: 15.06.2026") || 
               secilenSaat.equals("Seçiniz...") || secilenBolge.equals("Seçiniz...") || secilenKisi.equals("Seçiniz...")) {
                JOptionPane.showMessageDialog(null, "Lütfen tarih, saat, bölge ve kişi sayısını eksiksiz seçin!", "Eksik Bilgi", JOptionPane.WARNING_MESSAGE);
                return; 
            }

            // Olası Oturum Hatasını Engelleme
            if(Oturum.aktifKullaniciId == -1) {
                JOptionPane.showMessageDialog(null, "Sistem hatası: Oturum bulunamadı. Lütfen tekrar giriş yapın.", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String url = "jdbc:mysql://localhost:3306/siparisrandevudb";
            String user = "root";
            String password = "";

            try {
                java.sql.Connection conn = java.sql.DriverManager.getConnection(url, user, password);
                
                // ==========================================
                // 1. AŞAMA: ÇİFTE REZERVASYON KONTROLÜ
                // ==========================================
                String kontrolSql = "SELECT * FROM randevular WHERE randevu_tarihi = ? AND randevu_saati = ? AND masa_bolgesi = ?";
                java.sql.PreparedStatement kontrolStmt = conn.prepareStatement(kontrolSql);
                kontrolStmt.setString(1, secilenTarih);
                kontrolStmt.setString(2, secilenSaat);
                kontrolStmt.setString(3, secilenBolge);
                
                java.sql.ResultSet rs = kontrolStmt.executeQuery();
                
                // Eğer bu bilgilere sahip bir satır dönerse, o masa o saatte doludur!
                if(rs.next()) {
                    JOptionPane.showMessageDialog(null, 
                        "Üzgünüz, " + secilenTarih + " tarihinde saat " + secilenSaat + " için " + secilenBolge + " zaten dolu!\nLütfen başka bir saat veya masa bölgesi seçiniz.", 
                        "Masa Dolu", 
                        JOptionPane.ERROR_MESSAGE);
                        
                    rs.close();
                    kontrolStmt.close();
                    conn.close();
                    return; // Kodu burada kes, aşağı inip kayıt yapmasına izin verme!
                }
                
                rs.close();
                kontrolStmt.close();

                // ==========================================
                // 2. AŞAMA: MASA BOŞSA KAYIT İŞLEMİ (INSERT)
                // ==========================================
                String sql = "INSERT INTO randevular (kullanici_id, masa_bolgesi, randevu_tarihi, randevu_saati, kisi_sayisi, aciklama) VALUES (?, ?, ?, ?, ?, ?)";
                java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);

                pstmt.setInt(1, Oturum.aktifKullaniciId); 
                pstmt.setString(2, secilenBolge);
                pstmt.setString(3, secilenTarih);
                pstmt.setString(4, secilenSaat);
                pstmt.setString(5, secilenKisi);
                pstmt.setString(6, girilenNot);

                int eklenenSatir = pstmt.executeUpdate();

                if (eklenenSatir > 0) {
                    JOptionPane.showMessageDialog(null, "Randevunuz " + secilenTarih + " " + secilenSaat + " için başarıyla oluşturuldu!");
                    Randevu_olustur.this.dispose();
                    new Randevu().setVisible(true); 
                }

                pstmt.close();
                conn.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Randevu Kayıt Hatası: " + ex.getMessage());
            }
        });

        add(cardPanel);
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
        JLabel etiket = new JLabel(baslik);
        etiket.setFont(new Font("SansSerif", Font.BOLD, 12));
        etiket.setForeground(yaziRengi);
        etiket.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(etiket);
        panel.add(Box.createRigidArea(new Dimension(0, 4)));
        panel.add(menu);
        panel.add(Box.createRigidArea(new Dimension(0, 12))); 
    }

    private void kutuyuModernlestir(JTextField field, String placeholder) {
        field.setMaximumSize(new Dimension(320, 32));
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBackground(new Color(248, 248, 246)); 
        field.setForeground(yaziRengi);
        field.setCaretColor(koyuYesil);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GriKenarlik, 1, true),
                BorderFactory.createEmptyBorder(2, 8, 2, 8) 
        ));
        
        field.setText(placeholder);
        field.setForeground(Color.GRAY);
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(yaziRengi);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });
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
        java.awt.EventQueue.invokeLater(() -> new Randevu_olustur().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
