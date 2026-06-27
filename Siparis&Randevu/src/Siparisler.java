import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader; // HATA VEREN KISMIN ÇÖZÜMÜ BURASI
import java.awt.*;
import java.sql.*;
public class Siparisler extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Siparisler.class.getName());

    private final Color kremZemin = new Color(250, 245, 236);
    private final Color koyuYesil = new Color(20, 51, 44);
    private final Color altinBronz = new Color(191, 149, 80);
    private final Color beyaz = Color.WHITE;
    private final Color yaziRengi = new Color(50, 50, 50);

    private JTable siparisTablosu;
    private DefaultTableModel tabloModeli;

    public Siparisler() {
        initComponents();
        tasarimiYukle();
        siparisleriTabloyaDoldur();
    }

    private void tasarimiYukle() {
        setTitle("Leziz Lezzetler - Siparişlerim");
        setSize(850, 600);
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

        JLabel baslikLabel = new JLabel("Mutfaktaki Siparişlerim", SwingConstants.CENTER);
        baslikLabel.setFont(new Font("Serif", Font.ITALIC | Font.BOLD, 28));
        baslikLabel.setForeground(koyuYesil);

        ustPanel.add(btnGeri, BorderLayout.WEST);
        ustPanel.add(baslikLabel, BorderLayout.CENTER);
        ustPanel.add(Box.createRigidArea(new Dimension(130, 0)), BorderLayout.EAST);

        // ==========================================
        // 2. ORTA PANEL (MODERN TABLO)
        // ==========================================
        JPanel ortaPanel = new JPanel(new BorderLayout());
        ortaPanel.setBackground(kremZemin);
        ortaPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        String[] sutunlar = {"Sipariş No", "Yemek Adı", "Adet", "Toplam Fiyat", "Sipariş Zamanı", "Durum"};
        tabloModeli = new DefaultTableModel(sutunlar, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        siparisTablosu = new JTable(tabloModeli);
        siparisTablosu.setRowHeight(45); // Daha ferah ve lüks bir görünüm için yüksek satırlar
        siparisTablosu.setFont(new Font("SansSerif", Font.PLAIN, 14));
        siparisTablosu.setForeground(yaziRengi);
        siparisTablosu.setSelectionBackground(new Color(230, 240, 235));
        siparisTablosu.setSelectionForeground(koyuYesil);
        siparisTablosu.setShowGrid(false);
        siparisTablosu.setIntercellSpacing(new Dimension(0, 0));

        // Tablo Başlığı (Header) Şıklaştırma
        JTableHeader header = siparisTablosu.getTableHeader();
        header.setBackground(koyuYesil);
        header.setForeground(beyaz);
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(100, 45));

        // Hücreleri Ortalama Motoru
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < sutunlar.length - 1; i++) {
            siparisTablosu.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // ==========================================
        // DURUMA GÖRE RENK DEĞİŞTİREN ÖZEL MOTOR (ÇOK MODERN)
        // ==========================================
        siparisTablosu.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                setFont(new Font("SansSerif", Font.BOLD, 13));

                String durum = (value != null) ? value.toString() : "";

                if (durum.equals("Hazırlanıyor")) {
                    setForeground(altinBronz); // Mutfakta pişenler bronz yanar
                } else if (durum.equals("Teslim Edildi")) {
                    setForeground(new Color(34, 139, 34)); // Ulaşanlar parlak yeşil
                } else if (durum.equals("İptal Edildi")) {
                    setForeground(new Color(180, 60, 60)); // İptaller kırmızı
                } else {
                    setForeground(yaziRengi);
                }

                if (isSelected) {
                    setForeground(koyuYesil); // Seçilince renk düzene girer
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(siparisTablosu);
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

        JPanel islemButonlariPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        islemButonlariPanel.setBackground(kremZemin);


        JButton btnİptal = standartButonOlustur("🗑️ Seçili Siparişi İptal Et", new Color(180, 60, 60));


        islemButonlariPanel.add(btnİptal);

        JButton btnYeniSiparis = standartButonOlustur("🍽️ YENİ SİPARİŞ OLUŞTUR", koyuYesil);
        btnYeniSiparis.setPreferredSize(new Dimension(0, 50));
        btnYeniSiparis.setFont(new Font("SansSerif", Font.BOLD, 16));

        altPanel.add(islemButonlariPanel);
        altPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        altPanel.add(btnYeniSiparis);

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

        btnYeniSiparis.addActionListener(e -> {
            this.dispose();
            new Siparis_Olustur().setVisible(true);
        });


        btnİptal.addActionListener(e -> {
            int seciliSatir = siparisTablosu.getSelectedRow();
            if (seciliSatir == -1) {
                JOptionPane.showMessageDialog(this, "Lütfen iptal etmek istediğiniz siparişi seçin!", "Uyarı", JOptionPane.WARNING_MESSAGE);
            } else {
                int secilenId = Integer.parseInt(tabloModeli.getValueAt(seciliSatir, 0).toString());
                int onay = JOptionPane.showConfirmDialog(this, "Siparişi iptal edip silmek istediğinize emin misiniz?", "İptal Onayı", JOptionPane.YES_NO_OPTION);

                if (onay == JOptionPane.YES_OPTION) {
                    siparisiVeritabanindanSil(secilenId);
                    siparisleriTabloyaDoldur(); // Tabloyu yenile
                }
            }
        });
    }

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

    // ==========================================
    // VERİTABANI İŞLEMLERİ
    // ==========================================
    private void siparisleriTabloyaDoldur() {
        tabloModeli.setRowCount(0);

        if (Oturum.aktifKullaniciId == -1) {
            return;
        }

        String url = "jdbc:mysql://localhost:3306/siparisrandevudb";
        String user = "root";
        String password = "";

        // Siparişleri en yeniden eskiye doğru (ORDER BY) çeken sorgu
        String sql = "SELECT siparis_id, urun_adi, adet, toplam_fiyat, siparis_tarihi, durum FROM siparisler WHERE kullanici_id = ? ORDER BY siparis_tarihi DESC";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Oturum.aktifKullaniciId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String id = rs.getString("siparis_id");
                String urun = rs.getString("urun_adi");
                String adet = rs.getString("adet");
                String fiyat = rs.getString("toplam_fiyat");
                String tarih = rs.getString("siparis_tarihi");
                String durum = rs.getString("durum");

                tabloModeli.addRow(new Object[]{id, urun, adet, fiyat, tarih, durum});
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Siparişler Listelenirken Hata: " + ex.getMessage());
        }
    }

    private void siparisiVeritabanindanSil(int siparisId) {
        String url = "jdbc:mysql://localhost:3306/siparisrandevudb";
        String user = "root";
        String password = "";

        String sql = "DELETE FROM siparisler WHERE siparis_id = ?";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, siparisId);
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();
            JOptionPane.showMessageDialog(this, "Sipariş başarıyla iptal edildi ve silindi.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Silme Hatası: " + ex.getMessage());
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
        java.awt.EventQueue.invokeLater(() -> new Siparisler().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
