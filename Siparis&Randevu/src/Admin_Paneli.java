
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Admin_Paneli extends javax.swing.JFrame {

    // Renk Paleti (Senin konseptine uygun)
    private final Color bgKoyu = new Color(30, 30, 30);
    private final Color panelGri = new Color(45, 45, 45);
    private final Color altinBronz = new Color(191, 149, 80);
    private final Color yaziBeyaz = new Color(240, 240, 240);

    private final String DB_URL = "jdbc:mysql://localhost:3306/siparisrandevudb";
    private final String DB_USER = "root";
    private final String DB_PASS = "";

    private JTable siparisTablosu;
    private DefaultTableModel tabloModeli;

    public Admin_Paneli() {
        setTitle("Leziz Lezzetler - Yönetim Paneli");
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

        JButton btnSiparisler = butonOlustur("🍔 Siparişler", altinBronz);
        btnSiparisler.setForeground(Color.BLACK); // Aktif sekme belli olsun
        btnSiparisler.setPreferredSize(new Dimension(180, 45));
        JButton btnMenuYonetimi = butonOlustur("📋 Menü Yönetimi", bgKoyu);
        btnMenuYonetimi.setPreferredSize(new Dimension(180, 45));
        btnMenuYonetimi.addActionListener(e -> {
            new Menu_Yonetimi_Admin().setVisible(true);
            this.dispose(); // Mevcut sayfayı kapat, menü yönetimine geç
        });
        JButton btnkasa = butonOlustur("💵 Kasa", bgKoyu);
        btnkasa.setPreferredSize(new Dimension(180, 45));
        btnkasa.addActionListener(e -> {
            this.dispose();
            new Kasa_Admin().setVisible(true);
        });

        JButton btnRandevular = butonOlustur("📅 Randevular", bgKoyu);
        btnRandevular.setPreferredSize(new Dimension(180, 45));
        btnRandevular.addActionListener(e -> {
            new Randevular_Admin().setVisible(true);
            this.dispose(); // Mevcut pencereyi kapat, randevulara geç
        });

        solMenuPaneli.add(lblLogo);
        solMenuPaneli.add(btnSiparisler);
        solMenuPaneli.add(btnRandevular);
        solMenuPaneli.add(btnMenuYonetimi);
        solMenuPaneli.add(btnkasa);
        add(solMenuPaneli, BorderLayout.WEST);

        // --- ÜST BAŞLIK ---
        JPanel merkezPanel = new JPanel(new BorderLayout(10, 10));
        merkezPanel.setBackground(bgKoyu);

        JLabel lblBaslik = new JLabel("Sipariş Yönetim Merkezi", SwingConstants.LEFT);
        lblBaslik.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblBaslik.setForeground(yaziBeyaz);
        lblBaslik.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        merkezPanel.add(lblBaslik, BorderLayout.NORTH);

        // --- ORTA: SİPARİŞ TABLOSU ---
        String[] kolonlar = {"Sipariş No", "Kullanıcı ID", "Sipariş İçeriği", "Tutar", "Ödeme Türü", "Durum"};
        tabloModeli = new DefaultTableModel(kolonlar, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        siparisTablosu = new JTable(tabloModeli);
        siparisTablosu.setRowHeight(30);
        siparisTablosu.setFont(new Font("SansSerif", Font.PLAIN, 14));
        siparisTablosu.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        siparisTablosu.setSelectionBackground(altinBronz);
        siparisTablosu.setSelectionForeground(Color.BLACK);

        JScrollPane scrollTablo = new JScrollPane(siparisTablosu);
        merkezPanel.add(scrollTablo, BorderLayout.CENTER);

        // --- SAĞ TARAF: KONTROL BUTONLARI ---
        JPanel sagButonPaneli = new JPanel(new GridLayout(4, 1, 0, 20));
        sagButonPaneli.setBackground(bgKoyu);
        sagButonPaneli.setBorder(BorderFactory.createEmptyBorder(50, 15, 20, 20));
        sagButonPaneli.setPreferredSize(new Dimension(220, 0));

        JButton btnYolda = butonOlustur("🚚 Yolda", new Color(60, 120, 180));
        btnYolda.addActionListener(e -> durumuGuncelle("Yolda"));

        JButton btnTeslimEdildi = butonOlustur("✅ Teslim Edildi", new Color(60, 140, 60));
        btnTeslimEdildi.addActionListener(e -> durumuGuncelle("Teslim Edildi"));

        JButton btnIptal = butonOlustur("❌ İptal Et", new Color(180, 60, 60));
        btnIptal.addActionListener(e -> siparisiIptalEt());

        sagButonPaneli.add(btnYolda);
        sagButonPaneli.add(btnTeslimEdildi);
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
            String sql = "SELECT siparis_id, kullanici_id, urun_adi, toplam_fiyat, odeme_turu, durum FROM siparisler ORDER BY siparis_id DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tabloModeli.addRow(new Object[]{
                    rs.getInt("siparis_id"), rs.getInt("kullanici_id"), rs.getString("urun_adi"),
                    rs.getString("toplam_fiyat") + " TL", rs.getString("odeme_turu"), rs.getString("durum")
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void durumuGuncelle(String yeniDurum) {
        int seciliSatir = siparisTablosu.getSelectedRow();
        if (seciliSatir == -1) {
            JOptionPane.showMessageDialog(this, "Tablodan bir sipariş seçin!", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int siparisId = (int) tabloModeli.getValueAt(seciliSatir, 0);
        veritabanindaDurumDegistir(siparisId, yeniDurum);
    }

    private void siparisiIptalEt() {
        int seciliSatir = siparisTablosu.getSelectedRow();
        if (seciliSatir == -1) {
            JOptionPane.showMessageDialog(this, "Tablodan iptal edilecek siparişi seçin!", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int onay = JOptionPane.showConfirmDialog(this, "Bu siparişi iptal etmek istediğinize emin misiniz?", "İptal Onayı", JOptionPane.YES_NO_OPTION);

        if (onay == JOptionPane.YES_OPTION) {
            int siparisId = (int) tabloModeli.getValueAt(seciliSatir, 0);
            veritabanindaDurumDegistir(siparisId, "İptal Edildi");
        }
    }

    private void veritabanindaDurumDegistir(int siparisId, String durumMetni) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "UPDATE siparisler SET durum = ? WHERE siparis_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, durumMetni);
            ps.setInt(2, siparisId);
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
            .addGap(0, 542, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 381, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Admin_Paneli().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
