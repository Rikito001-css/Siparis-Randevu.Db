
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Bize_ulasın extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Bize_ulasın.class.getName());

    private final Color kremZemin = new Color(250, 245, 236);
    private final Color koyuYesil = new Color(20, 51, 44);
    private final Color altinBronz = new Color(191, 149, 80);
    private final Color beyaz = Color.WHITE;
    private final Color yaziRengi = new Color(50, 50, 50);
    private final Color GriKenarlik = new Color(220, 215, 205);

    private JTextField txtKonu;
    private JTextArea txtMesaj;
    public Bize_ulasın() {
        initComponents();
        tasarimiYukle();
    }
private void tasarimiYukle() {
        setTitle("Leziz Lezzetler - Bize Ulaşın");
        setSize(500, 650); 
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
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        cardPanel.setPreferredSize(new Dimension(420, 550));

        // 1. BAŞLIK ALANI 
        JLabel titleLabel = new JLabel("Bize Ulaşın");
        titleLabel.setFont(new Font("Serif", Font.ITALIC | Font.BOLD, 28)); 
        titleLabel.setForeground(koyuYesil);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subTitleLabel = new JLabel("Görüş, öneri ve şikayetleriniz bizim için değerli.");
        subTitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subTitleLabel.setForeground(altinBronz);
        subTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 2. İLETİŞİM BİLGİLERİ (Statik Bilgiler)
        JPanel bilgiPanel = new JPanel();
        bilgiPanel.setLayout(new BoxLayout(bilgiPanel, BoxLayout.Y_AXIS));
        bilgiPanel.setBackground(beyaz);
        bilgiPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        bilgiPanel.add(bilgiSatiriOlustur("📍 Adres: Bilecik Şeyh Edebali Üniversitesi Kampüsü, Merkez / Bilecik"));
        bilgiPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        bilgiPanel.add(bilgiSatiriOlustur("📞 Telefon: +90 (555) 123 45 67"));
        bilgiPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        bilgiPanel.add(bilgiSatiriOlustur("✉️ E-Posta: iletisim@lezizlezzetler.com"));

        // 3. MESAJ FORMU
        txtKonu = new JTextField();
        kutuyuModernlestir(txtKonu);

        txtMesaj = new JTextArea();
        txtMesaj.setLineWrap(true);
        txtMesaj.setWrapStyleWord(true);
        txtMesaj.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtMesaj.setBackground(new Color(248, 248, 246));
        txtMesaj.setForeground(yaziRengi);
        txtMesaj.setCaretColor(koyuYesil);
        txtMesaj.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GriKenarlik, 1, true),
                BorderFactory.createEmptyBorder(8, 8, 8, 8) 
        ));
        
        JScrollPane mesajScroll = new JScrollPane(txtMesaj);
        mesajScroll.setPreferredSize(new Dimension(320, 100));
        mesajScroll.setMaximumSize(new Dimension(320, 100));
        mesajScroll.setBorder(BorderFactory.createEmptyBorder());

        // 4. BUTONLAR
        JButton btnGonder = new JButton("Mesajı Gönder");
        btnGonder.setMaximumSize(new Dimension(320, 42));
        btnGonder.setBackground(altinBronz);
        btnGonder.setForeground(beyaz);
        btnGonder.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGonder.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnGonder.setFocusPainted(false);
        btnGonder.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnGeri = new JButton("Geri Dön");
        btnGeri.setMaximumSize(new Dimension(320, 30));
        btnGeri.setForeground(koyuYesil); 
        btnGeri.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnGeri.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnGeri.setContentAreaFilled(false); 
        btnGeri.setBorderPainted(false);
        btnGeri.setFocusPainted(false);
        btnGeri.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // EKRANA YERLEŞTİRME
        cardPanel.add(titleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(subTitleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20))); 
        
        cardPanel.add(bilgiPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20))); 
        
        alaniEkle(cardPanel, "Mesajın Konusu", txtKonu);
        alaniEkle(cardPanel, "Mesajınız", mesajScroll);
        
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        cardPanel.add(btnGonder);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(btnGeri);

        add(cardPanel);

        // ==========================================
        // AKSİYONLAR
        // ==========================================
        btnGeri.addActionListener(e -> {
            this.dispose();
            // Eğer varsa Anasayfa'ya yönlendir
            new Anasayfa().setVisible(true); 
        });

        btnGonder.addActionListener(e -> {
            String konu = txtKonu.getText().trim();
            String mesaj = txtMesaj.getText().trim();

            if (konu.isEmpty() || mesaj.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Lütfen konu ve mesaj alanlarını boş bırakmayın!", "Eksik Bilgi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // İleride buraya INSERT INTO iletisim (kullanici_id, konu, mesaj) SQL sorgusu eklenebilir.
            JOptionPane.showMessageDialog(this, "Mesajınız başarıyla iletildi. En kısa sürede sizinle iletişime geçeceğiz.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            
            // Gönderdikten sonra kutuları temizle
            txtKonu.setText("");
            txtMesaj.setText("");
        });
    }

    // --- YARDIMCI METOTLAR ---
    private JLabel bilgiSatiriOlustur(String metin) {
        JLabel etiket = new JLabel(metin);
        etiket.setFont(new Font("SansSerif", Font.PLAIN, 12));
        etiket.setForeground(yaziRengi);
        return etiket;
    }

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
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 489, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 419, Short.MAX_VALUE)
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
        java.awt.EventQueue.invokeLater(() -> new Bize_ulasın().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
