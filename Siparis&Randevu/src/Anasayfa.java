
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Anasayfa extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Anasayfa.class.getName());

    private final Color kremZemin = new Color(250, 245, 236);//renkler
    private final Color koyuYesil = new Color(20, 51, 44);
    private final Color altinBronz = new Color(191, 149, 80);
    private final Color beyaz = Color.WHITE;
    private final Color yaziRengi = new Color(50, 50, 50);

    public Anasayfa() {
        initComponents();
        anaSayfaTasariminiYukle();
    }

    private void anaSayfaTasariminiYukle() {
        setTitle("Leziz Lezzetler - Ana Sayfa");
        setSize(900, 650); // Müşterilerin göreceği anasayfa ekranı
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout()); // üst ve alt katmanlar olarak ayırıyoruz
        getContentPane().setBackground(kremZemin);

        // ==========================================
        // 1. ÜST MENÜ (HEADER) KISMI
        // ==========================================
        JPanel headerPanel = new JPanel(new BorderLayout());//Anasayfadaki yeşil border alanı
        headerPanel.setBackground(koyuYesil);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Sol Taraf: Logo veya Marka ismini oluşturan kodlar
        JLabel lblLogo = new JLabel("Leziz Lezzetler");
        lblLogo.setFont(new Font("Serif", Font.ITALIC | Font.BOLD, 26));
        lblLogo.setForeground(altinBronz);
        headerPanel.add(lblLogo, BorderLayout.WEST);

        // Sağ Taraf: Profil ve Logout Butonları
        JPanel sagUstPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        sagUstPanel.setOpaque(false); // Arka planı koyu yeşil kalsın diye şeffaf

        JButton btnProfil = ustMenuButonuOlustur("👤 Profilim");
        JButton btnLogout = ustMenuButonuOlustur("🚪 Çıkış Yap");

        sagUstPanel.add(btnProfil);
        sagUstPanel.add(btnLogout);
        headerPanel.add(sagUstPanel, BorderLayout.EAST);

        // ==========================================
        // 2. ORTA İÇERİK
        // ==========================================
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout()); // Kartları tam ortalamak için
        centerPanel.setBackground(kremZemin);

        // Kartları dizeceğimiz ızgara sistemi 2 satır 3 sütun şeklinde yapıldı
        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 30, 30));
        gridPanel.setOpaque(false);

        // Modern Dashboard Butonlarını Oluşturuyoruz
        JButton btnMenu = kartButonOlustur("🍽️", "Menüyü İncele"); 
        JButton btnSiparis = kartButonOlustur("🛵", "Sipariş Oluştur / Güncelle");
        JButton btnRandevu = kartButonOlustur("📅", "Randevu Al / Güncelle");
        JButton btnKuponlar = kartButonOlustur("🎁", "Kuponlarım");
        JButton btnGecmis = kartButonOlustur("🕒", "Geçmiş Siparişlerim&Randevularım"); 
        JButton btnIletisim = kartButonOlustur("📞", "Bize Ulaşın"); 

        // grid panele oluşturduğumuz butonları sırasıyla ekliyoruz
        gridPanel.add(btnMenu);
        gridPanel.add(btnSiparis);
        gridPanel.add(btnRandevu);
        gridPanel.add(btnKuponlar);
        gridPanel.add(btnGecmis);
        gridPanel.add(btnIletisim);

        centerPanel.add(gridPanel);

        // ==========================================
        // TIKLAMA OLAYLARI 
        // ==========================================
        
        // Çıkış Yap Butonu
        btnLogout.addActionListener(e -> {
            int secim = JOptionPane.showConfirmDialog(null, "Hesabınızdan çıkış yapmak istediğinize emin misiniz?", "Çıkış Yap", JOptionPane.YES_NO_OPTION);
            if (secim == JOptionPane.YES_OPTION) {//eğerki Messagebox da sorulan soruya evet yanıtını verirseniz aşşağıdaki kodları çalıştıracak
                this.dispose(); // Ana sayfayı kapat
                new LoginScreen().setVisible(true); // Giriş ekranına geri dön
            }
        });

        // Profil Butonu
        btnProfil.addActionListener(e -> {
            // müşteri buttona bastığında Profil.java ya gidecek
            JOptionPane.showMessageDialog(null, "Profil Güncelleme ekranına yönlendiriliyorsunuz...");
            this.dispose();
            new Profil().setVisible(true);
        });

        //ızgaramızdaki buttonları ilgili sayfaalrına gitmesi için kullanılmış kod satırları ben dispose kullandım burada çünkü ben sayfadan çıktığımda arkada çalışıp bellek tüketsin istemiyorum
        btnIletisim.addActionListener(e -> {
            this.dispose();
            new Bize_ulasın().setVisible(true);
        });
        btnMenu.addActionListener(e -> {
            this.dispose();
            new Menu().setVisible(true);
        });
        btnSiparis.addActionListener(e -> {
            this.dispose();
            new Siparisler().setVisible(true);
        });
        btnRandevu.addActionListener(e -> {
            Anasayfa.this.dispose();
            new Randevu().setVisible(true);
        });
        btnKuponlar.addActionListener(e -> {
            this.dispose();
            new Kuponlar().setVisible(true);
        });
        btnGecmis.addActionListener(e-> { this.dispose(); new Gecmis().setVisible(true);});

        // Panelleri Ana Frame'e Ekleme
        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
        // ==========================================
        // TASARIM
        // ==========================================
    private JButton ustMenuButonuOlustur(String metin) {//üst menüde bulunan profil ve logout buttonlarının tasarım kodları
        JButton btn = new JButton(metin);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setForeground(beyaz);
        btn.setBackground(altinBronz);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return btn;
    }

    private JButton kartButonOlustur(String ikon, String metin) {//ızgaramızzda bulunan bütün butonların tasarım kodları
        JButton btn = new JButton("<html><center><font size='6'>" + ikon + "</font><br><br>" + metin + "</center></html>");
        btn.setPreferredSize(new Dimension(220, 160)); // Kartların büyüklüğü
        btn.setBackground(beyaz);
        btn.setForeground(koyuYesil);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // karta gölge shadow eklediğimiz yer 
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 225, 215), 2, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // mause ile butonların üstüne gelindiğinde değişmesi Hover yani
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(245, 250, 245));
                btn.setBorder(BorderFactory.createLineBorder(koyuYesil, 2, true));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(beyaz);
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(230, 225, 215), 2, true),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }
        });

        return btn;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 681, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 426, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (Exception ex) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
            }
        }
        java.awt.EventQueue.invokeLater(() -> new Anasayfa().setVisible(true));
    }
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

