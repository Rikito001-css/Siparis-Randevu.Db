import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Gecmis extends javax.swing.JFrame {

    // PREMIUM RENK PALETİ
    private final Color kremZemin = new Color(250, 245, 236);
    private final Color koyuYesil = new Color(20, 51, 44);
    private final Color altinBronz = new Color(191, 149, 80);
    private final Color beyaz = Color.WHITE;
    private final Color yaziRengi = new Color(50, 50, 50);

    private JTable siparisTablosu;
    private JTable randevuTablosu;
    private DefaultTableModel siparisModeli;
    private DefaultTableModel randevuModeli;

    public Gecmis() {
        initComponents();
        tasarimiYukle();
        verileriDoldur(); // Veritabanından hem siparişleri hem randevuları çeker
    }

    private void tasarimiYukle() {
        setTitle("Leziz Lezzetler - Geçmiş İşlemlerim");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(kremZemin);
        setLayout(new BorderLayout(10, 10));

        // ==========================================
        // 1. ÜST PANEL (BAŞLIK VE GERİ BUTONU)
        // ==========================================
        JPanel ustPanel = new JPanel(new BorderLayout());
        ustPanel.setBackground(kremZemin);
        ustPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JButton btnGeri = new JButton("⬅ Ana Sayfaya Dön");
        btnGeri.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGeri.setForeground(altinBronz);
        btnGeri.setContentAreaFilled(false);
        btnGeri.setBorderPainted(false);
        btnGeri.setFocusPainted(false);
        btnGeri.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGeri.addActionListener(e -> {
            this.dispose();
            new Anasayfa().setVisible(true); 
        });

        JLabel baslikLabel = new JLabel("Geçmiş İşlemlerim", SwingConstants.CENTER);
        baslikLabel.setFont(new Font("Serif", Font.ITALIC | Font.BOLD, 28));
        baslikLabel.setForeground(koyuYesil);

        ustPanel.add(btnGeri, BorderLayout.WEST);
        ustPanel.add(baslikLabel, BorderLayout.CENTER);
        ustPanel.add(Box.createRigidArea(new Dimension(150, 0)), BorderLayout.EAST); // Dengeleyici

        // ==========================================
        // 2. ORTA PANEL (SEKMELİ MENÜ VE TABLOLAR)
        // ==========================================
        JTabbedPane sekmeliMenu = new JTabbedPane();
        sekmeliMenu.setFont(new Font("SansSerif", Font.BOLD, 14));
        sekmeliMenu.setBackground(kremZemin);
        sekmeliMenu.setForeground(koyuYesil);

        // --- SİPARİŞLER TABLOSU ---
        String[] siparisSutunlar = {"Sipariş No", "Ürünler", "Tutar", "Ödeme Türü", "Durum"};
        siparisModeli = new DefaultTableModel(siparisSutunlar, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        siparisTablosu = tabloOlustur(siparisModeli);
        JScrollPane siparisScroll = new JScrollPane(siparisTablosu);
        siparisScroll.getViewport().setBackground(beyaz);

        // --- RANDEVULAR TABLOSU ---
        // Not: Veritabanındaki sütun adlarına göre burayı güncelleyebilirsin
        String[] randevuSutunlar = {"Randevu No", "Tarih", "Saat", "Durum"}; 
        randevuModeli = new DefaultTableModel(randevuSutunlar, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        randevuTablosu = tabloOlustur(randevuModeli);
        JScrollPane randevuScroll = new JScrollPane(randevuTablosu);
        randevuScroll.getViewport().setBackground(beyaz);

        sekmeliMenu.addTab("🛒 Geçmiş Siparişlerim", siparisScroll);
        sekmeliMenu.addTab("📅 Geçmiş Randevularım", randevuScroll);

        JPanel ortaPanel = new JPanel(new BorderLayout());
        ortaPanel.setBackground(kremZemin);
        ortaPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        ortaPanel.add(sekmeliMenu, BorderLayout.CENTER);

        // ANA EKRANA EKLEME
        add(ustPanel, BorderLayout.NORTH);
        add(ortaPanel, BorderLayout.CENTER);
    }

    // Ortak Tablo Tasarım Ayarları (Kod tekrarını önlemek için)
    private JTable tabloOlustur(DefaultTableModel model) {
        JTable tablo = new JTable(model);
        tablo.setRowHeight(40);
        tablo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tablo.setForeground(yaziRengi);
        tablo.setSelectionBackground(new Color(230, 240, 235));
        tablo.setSelectionForeground(koyuYesil);
        tablo.setShowGrid(true);
        tablo.setGridColor(new Color(230, 230, 230));

        JTableHeader header = tablo.getTableHeader();
        header.setBackground(koyuYesil);
        header.setForeground(beyaz);
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(100, 40));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tablo.getColumnCount(); i++) {
            tablo.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        return tablo;
    }

    // ==========================================
    // 3. VERİTABANINDAN BİLGİLERİ ÇEKME
    // ==========================================
    private void verileriDoldur() {
        siparisModeli.setRowCount(0);
        randevuModeli.setRowCount(0);

        int guvenliKullaniciId = (Oturum.aktifKullaniciId > 0) ? Oturum.aktifKullaniciId : 1;
        String url = "jdbc:mysql://localhost:3306/siparisrandevudb";

        try (Connection conn = DriverManager.getConnection(url, "root", "")) {
            
            // 1. SİPARİŞLERİ ÇEK (Sadece Tamamlananları ve İptal Edilenleri getirmek istersen WHERE kısmına ekleyebilirsin)
            String sqlSiparis = "SELECT siparis_id, urun_adi, toplam_fiyat, odeme_turu, durum FROM siparisler WHERE kullanici_id = ? ORDER BY siparis_id DESC";
            try (PreparedStatement psSiparis = conn.prepareStatement(sqlSiparis)) {
                psSiparis.setInt(1, guvenliKullaniciId);
                ResultSet rs = psSiparis.executeQuery();
                while (rs.next()) {
                    siparisModeli.addRow(new Object[]{
                        "SL-" + rs.getString("siparis_id"),
                        rs.getString("urun_adi"),
                        rs.getString("toplam_fiyat") + " TL",
                        rs.getString("odeme_turu"),
                        rs.getString("durum")
                    });
                }
            } catch (Exception ex) {
                System.out.println("Siparişler Çekilemedi: " + ex.getMessage());
            }

            // 2. RANDEVULARI ÇEK
            // DİKKAT: Veritabanındaki 'randevular' tablosunun sütun isimleri farklıysa buraları ona göre düzeltmelisin!
            String sqlRandevu = "SELECT randevu_id, tarih, saat, durum FROM randevular WHERE kullanici_id = ? ORDER BY randevu_id DESC";
            try (PreparedStatement psRandevu = conn.prepareStatement(sqlRandevu)) {
                psRandevu.setInt(1, guvenliKullaniciId);
                ResultSet rs = psRandevu.executeQuery();
                while (rs.next()) {
                    randevuModeli.addRow(new Object[]{
                        rs.getString("randevu_id"),
                        rs.getString("tarih"),
                        rs.getString("saat"),
                        rs.getString("durum")
                    });
                }
            } catch (Exception ex) {
                System.out.println("Randevular Çekilemedi (Tablo veya sütun eksik olabilir): " + ex.getMessage());
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Veritabanı Bağlantı Hatası: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==========================================
    // NETBEANS ZORUNLU KODLARI
    // ==========================================
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
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch(Exception e){}
        java.awt.EventQueue.invokeLater(() -> new Gecmis().setVisible(true));
    }
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

