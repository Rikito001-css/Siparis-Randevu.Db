import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class Kuponlar extends javax.swing.JFrame {
    

    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Kuponlar.class.getName());
    // PREMIUM RENK PALETİ
    private final Color kremZemin = new Color(250, 245, 236);
    private final Color koyuYesil = new Color(20, 51, 44);
    private final Color altinBronz = new Color(191, 149, 80);
    private final Color beyaz = Color.WHITE;
    private final Color yaziRengi = new Color(50, 50, 50);

    private JTable kuponTablosu;
    private DefaultTableModel tabloModeli;

    public Kuponlar() {
        initComponents();
        tasarimiYukle();
        kuponlariTabloyaDoldur(); // Sayfa açılır açılmaz MySQL'den kuponları çeker
    }

    private void tasarimiYukle() {
        setTitle("Leziz Lezzetler - Kupon Cüzdanım");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Sadece bu sayfayı kapatsın
        setLocationRelativeTo(null);
        getContentPane().setBackground(kremZemin);
        setLayout(new BorderLayout(20, 20));

        // 1. ÜST PANEL (BAŞLIK VE GERİ BUTONU)
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

        JLabel baslikLabel = new JLabel("Kupon Cüzdanım", SwingConstants.CENTER);
        baslikLabel.setFont(new Font("Serif", Font.ITALIC | Font.BOLD, 28));
        baslikLabel.setForeground(koyuYesil);

        ustPanel.add(btnGeri, BorderLayout.WEST);
        ustPanel.add(baslikLabel, BorderLayout.CENTER);
        ustPanel.add(Box.createRigidArea(new Dimension(130, 0)), BorderLayout.EAST); 

        // 2. ORTA PANEL (TABLO)
        JPanel ortaPanel = new JPanel(new BorderLayout());
        ortaPanel.setBackground(kremZemin);
        ortaPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        String[] sutunlar = {"Kupon Kodu", "İndirim Miktarı", "Durum"};
        tabloModeli = new DefaultTableModel(sutunlar, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        kuponTablosu = new JTable(tabloModeli);
        kuponTablosu.setRowHeight(40); 
        kuponTablosu.setFont(new Font("Monospaced", Font.BOLD, 14)); 
        kuponTablosu.setForeground(yaziRengi);
        kuponTablosu.setSelectionBackground(new Color(230, 240, 235));
        kuponTablosu.setSelectionForeground(koyuYesil);
        kuponTablosu.setShowGrid(false); 
        kuponTablosu.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = kuponTablosu.getTableHeader();
        header.setBackground(koyuYesil);
        header.setForeground(beyaz);
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(100, 40));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < sutunlar.length; i++) {
            kuponTablosu.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(kuponTablosu);
        scrollPane.getViewport().setBackground(beyaz);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 215, 205), 1));

        ortaPanel.add(scrollPane, BorderLayout.CENTER);

        // 3. ALT PANEL
        JPanel altPanel = new JPanel();
        altPanel.setBackground(kremZemin);
        altPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));
        
        JLabel bilgiEtiketi = new JLabel("Mevcut kuponlarınızı sipariş ödeme ekranında kullanabilirsiniz.");
        bilgiEtiketi.setFont(new Font("SansSerif", Font.ITALIC, 12));
        bilgiEtiketi.setForeground(altinBronz);
        altPanel.add(bilgiEtiketi);

        add(ustPanel, BorderLayout.NORTH);
        add(ortaPanel, BorderLayout.CENTER);
        add(altPanel, BorderLayout.SOUTH);

        // AKSİYONLAR
        btnGeri.addActionListener(e -> {
            this.dispose();
             new Anasayfa().setVisible(true); 
        });
    }

    // ==========================================
    // ÇÖZÜLEN KISIM: DOĞRU TABLO VE SÜTUNLARDAN VERİ ÇEKİYOR
    // ==========================================
    private void kuponlariTabloyaDoldur() {
        tabloModeli.setRowCount(0);

        // Test ederken hata vermemesi için güvenlik önlemi
        int guvenliKullaniciId = (Oturum.aktifKullaniciId > 0) ? Oturum.aktifKullaniciId : 1;

        String url = "jdbc:mysql://localhost:3306/siparisrandevudb";

        // TABLO VE SÜTUN İSİMLERİ DÜZELTİLDİ: 'kullanici_kuponlari' ve 'deger'
        String sql = "SELECT kupon_kodu, deger FROM kullanici_kuponlari WHERE kullanici_id = ?";

        try (Connection conn = DriverManager.getConnection(url, "root", "");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setInt(1, guvenliKullaniciId); 
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String kod = rs.getString("kupon_kodu");
                String miktar = rs.getInt("deger") + " TL"; // Değerin yanına TL ekledik
                String durum = "Kullanıma Hazır"; // Tabloda var olduğu sürece kullanıma hazırdır

                Object[] satir = {kod, miktar, durum};
                tabloModeli.addRow(satir);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Kuponlar Listelenirken Hata: " + ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          

 
 

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
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch(Exception e){}
        java.awt.EventQueue.invokeLater(() -> new Kuponlar().setVisible(true));
        }
 }



    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
 
