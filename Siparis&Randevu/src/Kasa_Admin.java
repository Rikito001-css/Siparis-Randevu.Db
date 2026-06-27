import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Kasa_Admin extends javax.swing.JFrame {

   private final Color bgKoyu = new Color(30, 30, 30);
    private final Color panelGri = new Color(45, 45, 45);
    private final Color altinBronz = new Color(191, 149, 80);
    private final Color yaziBeyaz = new Color(240, 240, 240);

    private final String DB_URL = "jdbc:mysql://localhost:3306/siparisrandevudb";
    private final String DB_USER = "root";
    private final String DB_PASS = "";

    private JTable kasaTablosu;
    private DefaultTableModel tabloModeli;
    private JLabel lblToplamGelir, lblToplamGider, lblNetDurum;

    public Kasa_Admin() {
        setTitle("Leziz Lezzetler - Kasa ve Finans Yönetimi");
        setSize(1100, 650);
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
            this.dispose();
        });

        JButton btnRandevular = butonOlustur("📅 Randevular", bgKoyu);
        btnRandevular.setPreferredSize(new Dimension(180, 45));
        btnRandevular.addActionListener(e -> {
            new Randevular_Admin().setVisible(true);
            this.dispose();
        });

        JButton btnKasa = butonOlustur("💰 Kasa / Finans", altinBronz);
        btnKasa.setForeground(Color.BLACK);
        btnKasa.setPreferredSize(new Dimension(180, 45));

        solMenuPaneli.add(lblLogo);
        solMenuPaneli.add(btnSiparisler);
        solMenuPaneli.add(btnRandevular);
        solMenuPaneli.add(btnKasa);
        add(solMenuPaneli, BorderLayout.WEST);

        // --- ÜST TARAF: ÖZET KARTLARI (GELİR, GİDER, NET) ---
        JPanel ustPanel = new JPanel(new BorderLayout());
        ustPanel.setBackground(bgKoyu);

        JLabel lblBaslik = new JLabel("Finansal Durum & Kasa Hareketleri", SwingConstants.LEFT);
        lblBaslik.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblBaslik.setForeground(yaziBeyaz);
        lblBaslik.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        ustPanel.add(lblBaslik, BorderLayout.NORTH);

        JPanel ozetPaneli = new JPanel(new GridLayout(1, 3, 20, 0));
        ozetPaneli.setBackground(bgKoyu);
        ozetPaneli.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 20));

        lblToplamGelir = ozetKartiOlustur("Toplam Gelir", "0 TL", new Color(60, 140, 60));
        lblToplamGider = ozetKartiOlustur("Toplam Gider", "0 TL", new Color(180, 60, 60));
        lblNetDurum = ozetKartiOlustur("NET DURUM", "0 TL", altinBronz);

        ozetPaneli.add(lblToplamGelir);
        ozetPaneli.add(lblToplamGider);
        ozetPaneli.add(lblNetDurum);
        ustPanel.add(ozetPaneli, BorderLayout.CENTER);

        // --- ORTA: KASA TABLOSU ---
        JPanel merkezPanel = new JPanel(new BorderLayout(10, 10));
        merkezPanel.setBackground(bgKoyu);
        merkezPanel.add(ustPanel, BorderLayout.NORTH);

        // Tablo Sütunları (Basitleştirildi)
        String[] kolonlar = {"Tür", "Kategori", "Açıklama", "Tutar"};
        tabloModeli = new DefaultTableModel(kolonlar, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        kasaTablosu = new JTable(tabloModeli);
        kasaTablosu.setRowHeight(30);
        kasaTablosu.setFont(new Font("SansSerif", Font.PLAIN, 14));
        kasaTablosu.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        kasaTablosu.setSelectionBackground(altinBronz);
        kasaTablosu.setSelectionForeground(Color.BLACK);

        JScrollPane scrollTablo = new JScrollPane(kasaTablosu);
        scrollTablo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 20));
        merkezPanel.add(scrollTablo, BorderLayout.CENTER);

        add(merkezPanel, BorderLayout.CENTER);

        verileriYukleVeHesapla();
    }

    private JButton butonOlustur(String metin, Color arkaPlan) {
        JButton btn = new JButton(metin);
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setBackground(arkaPlan);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }

    private JLabel ozetKartiOlustur(String baslik, String deger, Color renk) {
        JLabel lbl = new JLabel("<html><center>" + baslik + "<br><b style='font-size:20px;'>" + deger + "</b></center></html>", SwingConstants.CENTER);
        lbl.setOpaque(true);
        lbl.setBackground(renk);
        lbl.setForeground(yaziBeyaz);
        lbl.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        return lbl;
    }

    private void verileriYukleVeHesapla() {
        tabloModeli.setRowCount(0);
        double toplamGelir = 0;
        double toplamGider = 0;

        // 1. GERÇEK VERİLER: İptal edilmemiş siparişleri gelir olarak ekle
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            // İptal edilenler hariç tüm siparişlerin fiyatını çekiyoruz
            String sql = "SELECT siparis_id, toplam_fiyat FROM siparisler WHERE durum NOT LIKE 'İptal%'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                double tutar = rs.getDouble("toplam_fiyat");
                toplamGelir += tutar;
                tabloModeli.addRow(new Object[]{
                        "🟢 Gelir", 
                        "Sipariş Kazancı", 
                        "Sipariş No: " + rs.getInt("siparis_id"), 
                        tutar + " TL"
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gerçek gelirler çekilirken hata: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }

        // 2. SANAL VERİLER: Kafadan attığımız sabit giderler
        Object[][] sanalGiderler = {
                {"🔴 Gider", "Dükkan Kirası", "Aylık Şube Kirası", 25000.0},
                {"🔴 Gider", "Fatura", "Elektrik Faturası", 4500.0},
                {"🔴 Gider", "Fatura", "Su ve Doğalgaz", 1850.0},
                {"🔴 Gider", "Maaş", "Kurye Maaşı (Aylık)", 17002.0},
                {"🔴 Gider", "Yakıt", "Motor Benzin Gideri", 3500.0},
                {"🔴 Gider", "Malzeme", "Mutfak Erzak ve Paketleme", 12500.0}
        };

        // Sanal giderleri tabloya yazdırıp toplam gidere ekliyoruz
        for (Object[] gider : sanalGiderler) {
            double tutar = (double) gider[3];
            toplamGider += tutar;
            tabloModeli.addRow(new Object[]{
                    gider[0], 
                    gider[1], 
                    gider[2], 
                    tutar + " TL"
            });
        }

        // 3. HESAPLAMA VE EKRANA YANSITMA
        lblToplamGelir.setText("<html><center>Toplam Gelir<br><b style='font-size:20px;'>" + toplamGelir + " TL</b></center></html>");
        lblToplamGider.setText("<html><center>Toplam Gider<br><b style='font-size:20px;'>" + toplamGider + " TL</b></center></html>");
        
        double net = toplamGelir - toplamGider;
        lblNetDurum.setText("<html><center>NET DURUM<br><b style='font-size:20px;'>" + net + " TL</b></center></html>");

        // Zarardaysak arka planı kırmızı, kardaysak altın bronz yapalım
        if(net < 0) {
            lblNetDurum.setBackground(new Color(180, 60, 60)); 
        } else {
            lblNetDurum.setBackground(altinBronz);
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
        java.awt.EventQueue.invokeLater(() -> new Kasa_Admin().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
