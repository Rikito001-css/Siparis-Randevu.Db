
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Profil extends javax.swing.JFrame {

    // Renk Paleti
    private final Color bgKoyu = new Color(30, 30, 30);
    private final Color panelGri = new Color(45, 45, 45);
    private final Color altinBronz = new Color(191, 149, 80);
    private final Color yaziBeyaz = new Color(240, 240, 240);

    // Form Elemanları
    private JTextField txtTcNo, txtIsim, txtSoyisim, txtTelefon, txtGmail;
    private JTextArea txtAdres;

    // Veritabanı Bilgileri (Kendi bilgilerine göre düzenleyebilirsin)
    private final String DB_URL = "jdbc:mysql://localhost:3306/siparisrandevudb";
    private final String DB_USER = "root";
    private final String DB_PASS = "";

    public Profil() {
        setTitle("Leziz Lezzetler - Profilim");
        setSize(500, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(bgKoyu);

        // Başlık
        JLabel lblBaslik = new JLabel("👤 Profil Bilgilerim", SwingConstants.CENTER);
        lblBaslik.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblBaslik.setForeground(altinBronz);
        lblBaslik.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblBaslik, BorderLayout.NORTH);

        // Form Paneli
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 15));
        formPanel.setBackground(bgKoyu);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        txtTcNo = formAlaniOlustur(formPanel, "TC Kimlik No:");
        txtIsim = formAlaniOlustur(formPanel, "İsim:");
        txtSoyisim = formAlaniOlustur(formPanel, "Soyisim:");
        txtTelefon = formAlaniOlustur(formPanel, "Telefon:");

        // Gmail Alanı (Değiştirilemez)
        txtGmail = formAlaniOlustur(formPanel, "E-Posta (Değiştirilemez):");
        txtGmail.setEditable(false);
        txtGmail.setBackground(Color.GRAY);
        txtGmail.setForeground(Color.BLACK);

        txtTcNo.setEditable(false);
        txtTcNo.setBackground(Color.GRAY);
        txtTcNo.setForeground(Color.BLACK);
        // Adres Alanı (JTextArea ile daha geniş)
        JPanel adresPanel = new JPanel(new BorderLayout());
        adresPanel.setBackground(bgKoyu);
        adresPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 10, 30));
        JLabel lblAdres = new JLabel("Adres:");
        lblAdres.setForeground(yaziBeyaz);
        lblAdres.setFont(new Font("SansSerif", Font.BOLD, 14));
        txtAdres = new JTextArea(4, 20);
        txtAdres.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtAdres.setLineWrap(true);
        txtAdres.setWrapStyleWord(true);
        JScrollPane scrollAdres = new JScrollPane(txtAdres);
        adresPanel.add(lblAdres, BorderLayout.NORTH);
        adresPanel.add(scrollAdres, BorderLayout.CENTER);

        // Butonlar
        JPanel butonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        butonPanel.setBackground(bgKoyu);
        butonPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JButton btnGuncelle = new JButton("💾 Bilgileri Güncelle");
        btnGuncelle.setBackground(altinBronz);
        btnGuncelle.setForeground(Color.BLACK);
        btnGuncelle.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGuncelle.addActionListener(e -> bilgileriGuncelle());

        JButton btnSifreDegistir = new JButton("🔑 Şifre Değiştir");
        btnSifreDegistir.setBackground(new Color(180, 60, 60));
        btnSifreDegistir.setForeground(Color.WHITE);
        btnSifreDegistir.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSifreDegistir.addActionListener(e -> sifreDegistirmeIslemi());

        butonPanel.add(btnSifreDegistir);
        butonPanel.add(btnGuncelle);

        // Panelleri Ekle
        JPanel merkezPanel = new JPanel(new BorderLayout());
        merkezPanel.setBackground(bgKoyu);
        merkezPanel.add(formPanel, BorderLayout.NORTH);
        merkezPanel.add(adresPanel, BorderLayout.CENTER);

        add(merkezPanel, BorderLayout.CENTER);
        add(butonPanel, BorderLayout.SOUTH);

        // Verileri Çek
        bilgileriEkranaGetir();
    }

    private JTextField formAlaniOlustur(JPanel panel, String etiket) {
        JLabel lbl = new JLabel(etiket);
        lbl.setForeground(yaziBeyaz);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        JTextField txt = new JTextField();
        txt.setFont(new Font("SansSerif", Font.PLAIN, 14));
        panel.add(lbl);
        panel.add(txt);
        return txt;
    }

    private void bilgileriEkranaGetir() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT tc_no, isim, soyisim, telefon, adres, gmail FROM kullanicilar WHERE kullanici_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Oturum.aktifKullaniciId); // Oturum sınıfından aktif ID'yi alıyoruz
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                txtTcNo.setText(rs.getString("tc_no"));
                txtIsim.setText(rs.getString("isim"));
                txtSoyisim.setText(rs.getString("soyisim"));
                txtTelefon.setText(rs.getString("telefon"));
                txtAdres.setText(rs.getString("adres"));
                txtGmail.setText(rs.getString("gmail"));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Bilgiler yüklenirken hata oluştu: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void bilgileriGuncelle() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "UPDATE kullanicilar SET tc_no=?, isim=?, soyisim=?, telefon=?, adres=? WHERE kullanici_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtTcNo.getText());
            ps.setString(2, txtIsim.getText());
            ps.setString(3, txtSoyisim.getText());
            ps.setString(4, txtTelefon.getText());
            ps.setString(5, txtAdres.getText());
            ps.setInt(6, Oturum.aktifKullaniciId);

            int sonuc = ps.executeUpdate();
            if (sonuc > 0) {
                JOptionPane.showMessageDialog(this, "Profil bilgileriniz başarıyla güncellendi!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Güncelleme hatası: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sifreDegistirmeIslemi() {
        // 1. Aşama: Eski şifreyi sor (Karakterleri gizlemek için JPasswordField kullanıyoruz)
        JPasswordField pfEski = new JPasswordField();
        int onayEski = JOptionPane.showConfirmDialog(this, pfEski, "Güvenlik: Lütfen Mevcut Şifrenizi Girin", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (onayEski == JOptionPane.OK_OPTION) {
            String girilenEskiSifre = new String(pfEski.getPassword());

            // Eski şifreyi veritabanından doğrula
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                String sqlKontrol = "SELECT sifre FROM kullanicilar WHERE kullanici_id = ?";
                PreparedStatement psKontrol = conn.prepareStatement(sqlKontrol);
                psKontrol.setInt(1, Oturum.aktifKullaniciId);
                ResultSet rs = psKontrol.executeQuery();

                if (rs.next()) {
                    String gercekEskiSifre = rs.getString("sifre");

                    if (gercekEskiSifre.equals(girilenEskiSifre)) {
                        // 2. Aşama: Şifreler eşleşti, yeni şifreyi sor
                        JPasswordField pfYeni = new JPasswordField();
                        int onayYeni = JOptionPane.showConfirmDialog(this, pfYeni, "Yeni Şifrenizi Belirleyin", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                        if (onayYeni == JOptionPane.OK_OPTION) {
                            String yeniSifre = new String(pfYeni.getPassword());

                            if (yeniSifre.trim().isEmpty()) {
                                JOptionPane.showMessageDialog(this, "Şifre boş olamaz!", "Uyarı", JOptionPane.WARNING_MESSAGE);
                                return;
                            }

                            // 3. Aşama: Yeni şifreyi veritabanına kaydet
                            String sqlGuncelle = "UPDATE kullanicilar SET sifre = ? WHERE kullanici_id = ?";
                            PreparedStatement psGuncelle = conn.prepareStatement(sqlGuncelle);
                            psGuncelle.setString(1, yeniSifre);
                            psGuncelle.setInt(2, Oturum.aktifKullaniciId);
                            psGuncelle.executeUpdate();

                            JOptionPane.showMessageDialog(this, "Şifreniz başarıyla değiştirildi!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Hatalı şifre girdiniz. İşlem iptal edildi.", "Güvenlik Uyarısı", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Şifre işlemi sırasında hata: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            }
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
        java.awt.EventQueue.invokeLater(() -> new Profil().setVisible(true));
    }
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

