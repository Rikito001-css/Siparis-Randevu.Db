
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Random;

public class Siparis_Olustur extends javax.swing.JFrame {

    private final Color bgKoyu = new Color(30, 30, 30);
    private final Color panelGri = new Color(45, 45, 45);
    private final Color altinBronz = new Color(191, 149, 80);
    private final Color yaziBeyaz = new Color(240, 240, 240);
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Siparis_Olustur.class.getName());
    private ArrayList<String> pasifUrunler = new ArrayList<>();
    private DefaultListModel<String> sepetModel = new DefaultListModel<>();
    private JLabel lblToplam, lblKdv, lblSiparisKod;
    private int araToplam = 0;
    private final int KDV_ORAN = 20;
    private final int GETIRME_BEDELI = 40;
    private String guncelSiparisKodu;

    private String kullanilanKuponKodu = null;
    private int indirimMiktari = 0;

    public Siparis_Olustur() {
        setTitle("Leziz Lezzetler - Sipariş ve Ödeme Merkezi");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        guncelSiparisKodu = "SL-" + (new Random().nextInt(9000) + 1000);

        JTabbedPane sekmeliMenu = new JTabbedPane();
        sekmeliMenu.addTab("🥩 Ana Yemekler", kategoriOlustur(anaYemekListesi()));
        sekmeliMenu.addTab("🥗 Mezeler", kategoriOlustur(mezeListesi()));
        sekmeliMenu.addTab("🍰 Tatlılar", kategoriOlustur(tatliListesi()));
        sekmeliMenu.addTab("🍹 İçecekler", kategoriOlustur(icecekListesi()));
        sekmeliMenu.addTab("🍟 Yan Yemekler", kategoriOlustur(yanYemekListesi()));

        JPanel sagPanel = new JPanel(new BorderLayout());
        sagPanel.setPreferredSize(new Dimension(350, 0));
        sagPanel.setBackground(panelGri);
        sagPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JList<String> liste = new JList<>(sepetModel);
        liste.setBackground(new Color(60, 60, 60));
        liste.setForeground(yaziBeyaz);
        liste.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane scrollSepet = new JScrollPane(liste);

        lblSiparisKod = new JLabel("Sipariş No: " + guncelSiparisKodu, SwingConstants.CENTER);
        lblSiparisKod.setForeground(Color.LIGHT_GRAY);

        lblKdv = new JLabel("KDV (%20): 0 TL", SwingConstants.CENTER);
        lblKdv.setForeground(Color.LIGHT_GRAY);

        lblToplam = new JLabel("Genel Toplam: 40 TL", SwingConstants.CENTER);
        lblToplam.setForeground(altinBronz);
        lblToplam.setFont(new Font("SansSerif", Font.BOLD, 18));

        JButton btnKuponKullan = new JButton("🎟️ Kupon Kullan");
        btnKuponKullan.setBackground(new Color(60, 100, 60));
        btnKuponKullan.setForeground(Color.WHITE);
        btnKuponKullan.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnKuponKullan.addActionListener(e -> kuponPenceresiAc());

        JButton btnCikar = new JButton("🗑️ Seçileni Çıkar");
        btnCikar.setBackground(new Color(180, 60, 60));
        btnCikar.setForeground(Color.WHITE);
        btnCikar.addActionListener(e -> {
            int index = liste.getSelectedIndex();
            if (index != -1) {
                araToplam -= Integer.parseInt(sepetModel.getElementAt(index).split(" - ")[1].replace(" TL", ""));
                sepetModel.remove(index);
                guncelle();
            }
        });

        JButton btnTamamla = new JButton("SİPARİŞİ TAMAMLA");
        btnTamamla.setBackground(altinBronz);
        btnTamamla.setForeground(Color.BLACK);
        btnTamamla.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnTamamla.addActionListener(e -> odemeFormuAc());

        JPanel ozetPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        ozetPanel.setBackground(panelGri);
        ozetPanel.add(lblSiparisKod);
        ozetPanel.add(lblKdv);
        ozetPanel.add(lblToplam);
        ozetPanel.add(btnKuponKullan);
        ozetPanel.add(btnCikar);
        ozetPanel.add(btnTamamla);

        sagPanel.add(new JLabel("🛒 Sepetiniz", SwingConstants.CENTER) {
            {
                setForeground(yaziBeyaz);
                setFont(new Font("SansSerif", Font.BOLD, 16));
            }
        }, BorderLayout.NORTH);
        sagPanel.add(scrollSepet, BorderLayout.CENTER);
        sagPanel.add(ozetPanel, BorderLayout.SOUTH);

        add(sekmeliMenu, BorderLayout.CENTER);
        add(sagPanel, BorderLayout.EAST);
    }

    private void kuponPenceresiAc() {
        ArrayList<String> kuponListesi = new ArrayList<>();
        kuponListesi.add("Kupon Seçmeyin");

        String url = "jdbc:mysql://localhost:3306/siparisrandevudb";
        try (Connection conn = DriverManager.getConnection(url, "root", "")) {
            String sql = "SELECT kupon_kodu, deger FROM kullanici_kuponlari WHERE kullanici_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Oturum.aktifKullaniciId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                kuponListesi.add(rs.getString("kupon_kodu") + " (" + rs.getInt("deger") + " TL İndirim)");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Kuponlar yüklenirken hata oluştu: " + ex.getMessage());
        }

        if (kuponListesi.size() == 1) {
            JOptionPane.showMessageDialog(this, "Şu anda hesabınıza tanımlı aktif bir kupon bulunmuyor.");
            return;
        }

        String[] aktifKuponlar = kuponListesi.toArray(new String[0]);
        String secilenKupon = (String) JOptionPane.showInputDialog(
                this, "Lütfen kullanmak istediğiniz kuponu seçin:", "Kupon Seçimi",
                JOptionPane.QUESTION_MESSAGE, null, aktifKuponlar, aktifKuponlar[0]
        );

        if (secilenKupon != null && !secilenKupon.equals("Kupon Seçmeyin")) {
            String[] parcalar = secilenKupon.split(" \\(");
            kullanilanKuponKodu = parcalar[0];
            indirimMiktari = Integer.parseInt(parcalar[1].replaceAll("[^0-9]", ""));

            JOptionPane.showMessageDialog(this, "Tebrikler! " + indirimMiktari + " TL indirim uygulandı.");
            guncelle();
        } else if (secilenKupon != null && secilenKupon.equals("Kupon Seçmeyin")) {
            indirimMiktari = 0;
            kullanilanKuponKodu = null;
            guncelle();
        }
    }

    private void odemeFormuAc() {
        if (sepetModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Sepetiniz boş!");
            return;
        }

        // 3 Seçenekli yeni ödeme listemiz
        String[] ops = {"💵 Kapıda Nakit", "💳 Kapıda Kart", "🌐 Online Ödeme"};
        int secim = JOptionPane.showOptionDialog(this, "Lütfen Ödeme Yönteminizi Seçiniz:", "Ödeme Onayı", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, ops, ops[0]);

        if (secim == 0) {
            kaydetVeKasayaGonder("Kapıda Nakit");
        } else if (secim == 1) {
            kaydetVeKasayaGonder("Kapıda Kart");
        } else if (secim == 2) {
            onlineOdemeEkraniAc(); // Online ödeme seçilirse kart ekranını aç
        }
    }

    private void onlineOdemeEkraniAc() {
        JPanel kartPaneli = new JPanel(new GridLayout(4, 2, 10, 15));

        JLabel lblIsim = new JLabel("Kart Üzerindeki İsim:");
        JTextField txtIsim = new JTextField();

        JLabel lblKartNo = new JLabel("Kart Numarası (16 Hane):");
        JTextField txtKartNo = new JTextField();

        JLabel lblSonKullanma = new JLabel("Son Kullanma (AA/YY):");
        JTextField txtSonKullanma = new JTextField();

        JLabel lblCvv = new JLabel("CVV (3 Hane):");
        JTextField txtCvv = new JTextField();

        kartPaneli.add(lblIsim);
        kartPaneli.add(txtIsim);
        kartPaneli.add(lblKartNo);
        kartPaneli.add(txtKartNo);
        kartPaneli.add(lblSonKullanma);
        kartPaneli.add(txtSonKullanma);
        kartPaneli.add(lblCvv);
        kartPaneli.add(txtCvv);

        int sonuc = JOptionPane.showConfirmDialog(this, kartPaneli, "Güvenli Online Ödeme (3D Secure)", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (sonuc == JOptionPane.OK_OPTION) {
            // Boş bırakma kontrolü
            if (txtKartNo.getText().trim().isEmpty() || txtCvv.getText().trim().isEmpty() || txtIsim.getText().trim().isEmpty() || txtSonKullanma.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Lütfen kart bilgilerinizi eksiksiz giriniz!", "Ödeme Reddedildi", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Bankanızla iletişim kuruldu.\nKartınızdan " + lblToplam.getText().replaceAll("[^0-9]", "") + " TL başarıyla çekildi!", "Ödeme Onaylandı", JOptionPane.INFORMATION_MESSAGE);
                kaydetVeKasayaGonder("Online Ödeme");
            }
        }
    }

    private void guncelle() {
        int kdv = (araToplam * KDV_ORAN) / 100;
        int genel = araToplam + kdv + GETIRME_BEDELI - indirimMiktari;

        if (genel < 0) {
            genel = 0;
        }

        lblKdv.setText("KDV (%20): " + kdv + " TL");
        lblToplam.setText("Genel Toplam: " + genel + " TL" + (indirimMiktari > 0 ? " (Kuponlu)" : ""));
    }

    private JScrollPane kategoriOlustur(String[][] yemekler) {
        
        // Menüyü ekrana çizmeden hemen önce veritabanından satışa kapalıları çeker
        if(pasifUrunler.isEmpty()) {
            pasifUrunleriCek(); 
        }

        JPanel panel = new JPanel(new GridLayout(0, 3, 10, 10));
        for (String[] y : yemekler) {
            
            // EĞER YEMEK KARA LİSTEDEYSE BUTONUNU OLUŞTURMA (PAS GEÇ)
            if (pasifUrunler.contains(y[0])) {
                continue; 
            }

            JButton btn = new JButton("<html><center>" + y[0] + "<br>" + y[1] + " TL</center></html>");
            btn.addActionListener(e -> {
                sepetModel.addElement(y[0] + " - " + y[1] + " TL");
                araToplam += Integer.parseInt(y[1]);
                guncelle();
            });
            panel.add(btn);
        }
        return new JScrollPane(panel);
    }

    private void kaydetVeKasayaGonder(String odemeTuru) {
        String url = "jdbc:mysql://localhost:3306/siparisrandevudb";

        try (Connection conn = DriverManager.getConnection(url, "root", "")) {
            // 1. Siparişler Tablosuna Yaz
            String sqlSiparis = "INSERT INTO siparisler (siparis_id, urun_adi, adet, toplam_fiyat, odeme_turu, kullanici_id, durum) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement psSiparis = conn.prepareStatement(sqlSiparis);

            int sadeceSayiKodu = Integer.parseInt(guncelSiparisKodu.replace("SL-", ""));
            psSiparis.setInt(1, sadeceSayiKodu);
            psSiparis.setString(2, sepetModel.toString());
            psSiparis.setString(3, String.valueOf(sepetModel.getSize()));
            psSiparis.setString(4, lblToplam.getText().replaceAll("[^0-9]", ""));
            psSiparis.setString(5, odemeTuru);

            int guvenliKullaniciId = (Oturum.aktifKullaniciId > 0) ? Oturum.aktifKullaniciId : 1;
            psSiparis.setInt(6, guvenliKullaniciId);
            psSiparis.setString(7, "Hazırlanıyor");

            psSiparis.executeUpdate();

            // 2. Kasaya Gelir Yaz
            String sqlKasa = "INSERT INTO kasa (siparis_kodu, gelatin_tutari) VALUES (?,?)";
            try {
                PreparedStatement psKasa = conn.prepareStatement(sqlKasa);
                psKasa.setString(1, guncelSiparisKodu);
                psKasa.setDouble(2, Double.parseDouble(lblToplam.getText().replaceAll("[^0-9]", "")));
                psKasa.executeUpdate();
            } catch (Exception eKasa) {
                // Eğer sütun adı gelatin_tutari değil de gelir_tutari ise diye yedek önlem
                String sqlKasa2 = "INSERT INTO kasa (siparis_kodu, gelir_tutari) VALUES (?,?)";
                PreparedStatement psKasa2 = conn.prepareStatement(sqlKasa2);
                psKasa2.setString(1, guncelSiparisKodu);
                psKasa2.setDouble(2, Double.parseDouble(lblToplam.getText().replaceAll("[^0-9]", "")));
                psKasa2.executeUpdate();
            }

            // 3. Eski Kupon Kullanıldıysa Hesaptan Sil
            if (kullanilanKuponKodu != null) {
                String sqlKuponSil = "DELETE FROM kullanici_kuponlari WHERE kupon_kodu = ?";
                PreparedStatement psKupon = conn.prepareStatement(sqlKuponSil);
                psKupon.setString(1, kullanilanKuponKodu);
                psKupon.executeUpdate();
            }

            // ==========================================
            //  KUPON KAZANMA VE KAYDETME MOTORU
            // ==========================================
            String finalMesaj = "Ödemeniz Tamamlandı! \nSipariş Kodunuz: " + guncelSiparisKodu;
            // Ara Toplam yerine kullanıcının ödediği asıl Genel Toplamı alıyoruz:
            double odenenTutar = Double.parseDouble(lblToplam.getText().replaceAll("[^0-9]", ""));

            if (odenenTutar >= 500) {
                // Benzersiz bir kupon kodu ve 100 TL değerinde hediye üretiyoruz
                int rastgeleSayi = new Random().nextInt(8999) + 1000;
                String yeniKuponKodu = "IKRAM-" + rastgeleSayi;
                int kuponDegeri = 100;

                String sqlKuponEkle = "INSERT INTO kullanici_kuponlari (kupon_kodu, deger, kullanici_id) VALUES (?, ?, ?)";
                try (PreparedStatement psKuponEkle = conn.prepareStatement(sqlKuponEkle)) {
                    psKuponEkle.setString(1, yeniKuponKodu);
                    psKuponEkle.setInt(2, kuponDegeri);
                    psKuponEkle.setInt(3, guvenliKullaniciId);

                    int sonuc = psKuponEkle.executeUpdate();
                    if (sonuc > 0) {
                        finalMesaj += "\n\n🎉 Tebrikler! 100 TL'lik yeni kuponunuz: " + yeniKuponKodu;
                    }
                } catch (Exception ex) {
                    // Hata artık sessiz değil! Ekrana fırlatıyoruz ki sorunun DB kaynaklı mı olduğunu görelim:
                    JOptionPane.showMessageDialog(this,
                            "Sipariş alındı fakat kupon oluşturulurken veritabanı hatası çıktı!\nHata Detayı: " + ex.getMessage(),
                            "Kupon Hatası",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

            JOptionPane.showMessageDialog(this, finalMesaj);
            this.dispose();
            new Siparisler().setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "İşlem başarısız! Hata: " + e.getMessage());
        }
    }

    private String[][] anaYemekListesi() {
        return new String[][]{{"Söğüt Kebabı", "320"}, {"Hünkar Beğendi", "280"}, {"İskender", "300"}, {"Adana", "260"}, {"Urfa", "260"}, {"Kuzu Tandır", "340"}, {"Beyti", "290"}, {"Testi Kebabı", "450"}, {"Ali Nazik", "290"}, {"Çökertme", "270"}, {"Orman Kebabı", "250"}, {"Tas Kebabı", "260"}, {"Ankara Tava", "320"}, {"Büryan", "350"}, {"Tokat Kebabı", "330"}, {"Kilis Tava", "280"}, {"Patlıcan Kebabı", "300"}, {"İslim Kebabı", "240"}, {"Etli Sarma", "220"}, {"Kuru Patlıcan Dolma", "230"}, {"Keşkek", "190"}, {"Güveçte Kuru Fasulye", "180"}, {"Etli Nohut", "170"}, {"Sac Kavurma", "310"}, {"Elbasan Tava", "270"}, {"Hamsili Pilav", "250"}, {"Bafra Pidesi", "210"}, {"Etli Ekmek", "200"}, {"Lahmacun", "80"}, {"Cantık", "90"}, {"Mantı", "200"}, {"Piliç Topkapı", "210"}, {"Şeftali Kebabı", "260"}, {"Kuzu İncik", "360"}, {"Kağıt Kebabı", "280"}, {"Kiremitte Köfte", "240"}, {"Ciğer Şiş", "230"}, {"Tepsi Kebabı", "290"}};
    }

    private String[][] mezeListesi() {
        return new String[][]{{"Haydari", "80"}, {"Humus", "95"}, {"Şakşuka", "75"}, {"Atom", "90"}, {"Girit Ezmesi", "95"}, {"Fava", "70"}, {"Muhammara", "85"}, {"Sarma", "85"}, {"Çerkez Tavuğu", "90"}, {"Tarator", "75"}, {"Acılı Ezme", "65"}, {"Babagannuş", "80"}, {"Patlıcan Salatası", "80"}, {"Arnavut Ciğeri", "120"}, {"Çiğ Köfte", "70"}, {"İmam Bayıldı", "85"}, {"Enginar", "95"}, {"Barbunya", "75"}, {"Kısır", "65"}, {"Köpoğlu", "80"}, {"Pırasa", "70"}, {"Pilaki", "70"}, {"Lakerda", "140"}, {"Midye Dolma", "90"}, {"Deniz Börülcesi", "80"}};
    }

    private String[][] tatliListesi() {
        return new String[][]{{"Padişah Lokumu", "180"}, {"Künefe", "160"}, {"Havuç Dilimi", "180"}, {"Baklava", "150"}, {"Şöbiyet", "160"}, {"Katmer", "170"}, {"İrmik Helvası", "110"}, {"Sütlaç", "90"}, {"Keşkül", "85"}, {"Kazandibi", "85"}, {"Tavuk Göğsü", "90"}, {"Aşure", "80"}, {"Zerde", "75"}, {"Un Helvası", "70"}, {"Höşmerim", "95"}, {"Revani", "80"}, {"Şekerpare", "75"}, {"Ekmek Kadayıfı", "130"}, {"Kemalpaşa", "70"}, {"Ayva Tatlısı", "100"}, {"Kabak Tatlısı", "95"}, {"Güllaç", "110"}, {"Dilber Dudağı", "85"}, {"Bülbül Yuvası", "140"}, {"Sütlü Nuriye", "120"}, {"Tulumba", "60"}};
    }

    private String[][] icecekListesi() {
        return new String[][]{{"Osmanlı Şerbeti", "75"}, {"Reyhan Şerbeti", "70"}, {"Kızılcık Şerbeti", "75"}, {"Gül Şerbeti", "70"}, {"Hardaliye", "85"}, {"Şıra", "60"}, {"Susurluk Ayranı", "45"}, {"Yayık Ayranı", "40"}, {"Acılı Şalgam", "40"}, {"Acısız Şalgam", "40"}, {"Boza", "85"}, {"Salep", "90"}, {"Türk Kahvesi", "60"}, {"Menengiç", "65"}, {"Dibek", "65"}, {"Mırra", "80"}, {"Semaver Çayı", "35"}, {"İnce Belli Çay", "25"}, {"Ihlamur", "50"}, {"Adaçayı", "50"}, {"Kuşburnu", "55"}, {"Maden Suyu", "30"}, {"Niğde Gazozu", "40"}};
    }

    private String[][] yanYemekListesi() {
        return new String[][]{{"Tereyağlı Pilav", "60"}, {"Firik", "75"}, {"Bulgur", "55"}, {"İç Pilav", "80"}, {"Siyez", "85"}, {"Şehriye Pilavı", "50"}, {"Su Böreği", "95"}, {"Paçanga", "90"}, {"Sigara Böreği", "60"}, {"Çiğ Börek", "85"}, {"Gül Böreği", "70"}, {"Anne Patatesi", "65"}, {"Kumpir", "75"}, {"Mücver", "70"}, {"Kuru Cacık", "55"}, {"Semizotu", "60"}, {"Gözleme", "85"}, {"Lavaş", "30"}, {"Turşu", "45"}, {"Acı Biber", "40"}};
    }

// --- YENİ EKLENEN METOD ---
    private void pasifUrunleriCek() {
        String url = "jdbc:mysql://localhost:3306/siparisrandevudb";
        try (Connection conn = DriverManager.getConnection(url, "root", "")) {
            String sql = "SELECT urun_adi FROM pasif_urunler";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                pasifUrunler.add(rs.getString("urun_adi"));
            }
        } catch (Exception e) {
            System.out.println("Pasif ürünler çekilemedi: " + e.getMessage());
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
        java.awt.EventQueue.invokeLater(() -> new Siparis_Olustur().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
