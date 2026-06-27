import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
public class Menu extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Menu.class.getName());

  private final Color kremZemin = new Color(250, 245, 236);
    private final Color koyuYesil = new Color(20, 51, 44);
    private final Color altinBronz = new Color(191, 149, 80);
    private final Color beyaz = Color.WHITE;
    private final Color yaziRengi = new Color(50, 50, 50);

    public Menu() {
        tasarimiYukle();
    }

private void tasarimiYukle() {
        setTitle("Leziz Lezzetler - %100 Geleneksel Türk Menüsü");
        setSize(1000, 750); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(kremZemin);
        setLayout(new BorderLayout(10, 10));

        // ==========================================
        // 1. ÜST PANEL
        // ==========================================
        JPanel ustPanel = new JPanel(new BorderLayout());
        ustPanel.setBackground(kremZemin);
        ustPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        JButton btnGeri = new JButton("⬅ Ana Sayfaya Dön");
        btnGeri.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnGeri.setForeground(altinBronz);
        btnGeri.setContentAreaFilled(false);
        btnGeri.setBorderPainted(false);
        btnGeri.setFocusPainted(false);
        btnGeri.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGeri.addActionListener(e -> {
            this.dispose();
            new Anasayfa().setVisible(true);
        });

        JLabel baslikLabel = new JLabel("Geleneksel Türk Mutfağı", SwingConstants.CENTER);
        baslikLabel.setFont(new Font("Serif", Font.ITALIC | Font.BOLD, 28));
        baslikLabel.setForeground(koyuYesil);

        ustPanel.add(btnGeri, BorderLayout.WEST);
        ustPanel.add(baslikLabel, BorderLayout.CENTER);
        ustPanel.add(Box.createRigidArea(new Dimension(150, 0)), BorderLayout.EAST);

        // ==========================================
        // 2. SEKMELİ MENÜ SİSTEMİ
        // ==========================================
        JTabbedPane sekmeliMenu = new JTabbedPane();
        sekmeliMenu.setFont(new Font("SansSerif", Font.BOLD, 13));
        sekmeliMenu.setBackground(beyaz);
        sekmeliMenu.setForeground(koyuYesil);

        // --- %100 TÜRK YEMEKLERİ DEVASA VERİ SETİ ---
        
        String[][] anaYemekler = {
            {"Söğüt Kebabı", "Özel tereyağlı pide üzerinde", "320 TL", "Tarihi Söğüt topraklarından ilham alan 4 saat pişmiş kuzu eti."},
            {"Hünkar Beğendi", "Köz patlıcan yatağında", "280 TL", "Osmanlı saray mutfağının vazgeçilmezi, padişahların favorisi."},
            {"İskender Kebap", "Bursa usulü, bol tereyağlı", "300 TL", "Orijinal Uludağ tereyağı ve ince kesim döner, şıra eşliğinde."},
            {"Adana Kebap", "Zırh kıyması, acılı", "260 TL", "Gerçek kuzu etinden zırh ile çekilmiş orijinal acılı kebap."},
            {"Urfa Kebap", "Zırh kıyması, acısız", "260 TL", "Acı sevmeyenler için özel Urfa usulü zırh kebabı."},
            {"Kuzu Tandır", "Odun ateşinde 8 saat", "340 TL", "Kendi suyunda ağır ağır pişmiş, tel tel dökülen kuzu eti."},
            {"Beyti Sarma", "Lavaş arası zırh kıyması", "290 TL", "Üzerinde sarımsaklı yoğurt ve özel domates sosu ile."},
            {"Testi Kebabı", "Avanos testisinde özel sunum", "450 TL", "Testi masanızda kırılır, içinden etin ve sebzenin şöleni çıkar."},
            {"Ali Nazik", "Yoğurtlu patlıcan ve kuşbaşı", "290 TL", "Gaziantep yöresinin muazzam lezzeti, dumanı üstünde."},
            {"Çökertme Kebabı", "Kibrit patates üzerinde", "270 TL", "Bodrum yöresine ait sarımsaklı yoğurtlu dana eti."},
            {"Orman Kebabı", "Bolu yöresine ait", "250 TL", "Kuşbaşı et, bezelye, havuç ve patatesin harika uyumu."},
            {"Tas Kebabı", "Kıvamlı özel sosunda", "260 TL", "Ağır ateşte pişmiş, lokum gibi dana kuşbaşı."},
            {"Ankara Tava", "Arpa şehriyeli kuzu incik", "320 TL", "Fırınlanmış şehriye pilavı üzerinde lime lime olmuş kuzu."},
            {"Büryan Kebabı", "Kuyu kebabı, Siirt usulü", "350 TL", "Kuyuda odun ateşinde asılarak pişirilen efsanevi lezzet."},
            {"Tokat Kebabı", "Sebzeli ve sarımsaklı fırın", "330 TL", "Özel şişlerde patlıcan, domates ve kuzu eti harmanı."},
            {"Kilis Tava", "Zırh kıyması tepsisi", "280 TL", "Kilis yöresinin meşhur taş fırın tepsisinde pişen kıyma kebabı."},
            {"Patlıcan Kebabı", "Şanlıurfa usulü", "300 TL", "Köz ateşinde patlıcan ve yağlı kuzu kıymasının dansı."},
            {"İslim Kebabı", "Kürdan kebabı", "240 TL", "Kızarmış patlıcan şeritlerine sarılmış nefis köfte."},
            {"Etli Yaprak Sarma", "Tokat yaprağı ile", "220 TL", "Üzerinde sarımsaklı süzme yoğurt ve kızdırılmış tereyağı."},
            {"Kuru Patlıcan Dolması", "Gaziantep usulü ekşili", "230 TL", "Sumak ekşisi ile tatlandırılmış, etli ve pirinçli kurutulmuş dolma."},
            {"Keşkek", "Buğday ve etin dövülmesi", "190 TL", "Anadolu'nun geleneksel düğün yemeği, bol tereyağlı."},
            {"Güveçte Kuru Fasulye", "İspir fasulyesi, pastırmalı", "180 TL", "Taş fırında ağır ağır pişmiş, helmelenmiş kuru fasulye."},
            {"Etli Nohut", "Kuzu kuşbaşılı", "170 TL", "Geleneksel esnaf lokantası usulü, kemik sulu."},
            {"Sac Kavurma", "Kuzu etinden, ince sebzeli", "310 TL", "Özel döküm sacda yüksek ateşte cızırdayarak servis edilir."},
            {"Elbasan Tava", "Yoğurt soslu fırınlanmış et", "270 TL", "Balkan ve Türk mutfağının ortak mirası, sarımsaklı yoğurtlu."},
            {"Hamsili Pilav", "Karadeniz incisi", "250 TL", "Fırınlanmış mısır unlu hamsi altında fıstıklı iç pilav."},
            {"Karadeniz Pidesi", "Kapalı, kavurmalı kaşarlı", "210 TL", "Samsun Bafra usulü çıtır kapalı pide, üzerinde hakiki tereyağı."},
            {"Etli Ekmek", "Konya usulü ince ve uzun", "200 TL", "İncecik açılmış hamurda satır kıyması, odun ateşinde pişmiş."},
            {"Lahmacun", "Gaziantep usulü", "80 TL", "Sarımsaklı, maydanozlu, bol malzemeli ince çıtır lahmacun."},
            {"Cantık", "Bursa usulü kıymalı küçük pide", "90 TL", "Yumuşacık hamuruyla yöresel kıymalı şölen."},
            {"Mantı", "Kayseri usulü, el açması", "200 TL", "Bir kaşığa 40 tane sığan, sumak ve naneyle taçlandırılmış mantı."},
            {"Piliç Topkapı", "İç pilav dolgulu", "210 TL", "Saray mutfağından fıstıklı iç pilavla doldurulmuş fırın tavuk."},
            {"Şeftali Kebabı", "Kıbrıs ve Güney mutfağı", "260 TL", "Kuzu gömleğine sarılmış nefis şiş köfte."},
            {"Kuzu İncik", "Kemikli lokum et", "360 TL", "Kök sebzelerle 6 saat fırınlanmış, kemikten dökülen et."},
            {"Kağıt Kebabı", "Antakya usulü", "280 TL", "Zırh kıyması, sarımsak ve biberle harmanlanıp yanmaz kağıtta fırınlanır."},
            {"Kiremitte Köfte", "Kaşar peyniri kaplamalı", "240 TL", "Toprak güveçte, kendi özel domates sosuyla fırınlanmış."},
            {"Ciğer Şiş", "Edirne usulü tava değil", "230 TL", "Kuyruk yağı ile harmanlanmış, mangalda taze pişirilmiş."},
            {"Tepsi Kebabı", "Hatay esintisi", "290 TL", "Taş fırında domates ve biberle pişirilmiş özel tepsi lezzeti."}
        };

        String[][] yanYemekler = {
            {"Tereyağlı Baldo Pilav", "Et suyu ile demlenmiş", "60 TL", "Geleneksel kemik suyu ile ağır ağır demlenmiş şehriyeli pirinç pilavı."},
            {"Firik Pilavı", "Gaziantep usulü, tütsülü", "75 TL", "Tütsülenmiş firik bulguru, nohut ve kemik suyu ile hazırlanan yöresel lezzet."},
            {"Meyhaneli Bulgur Pilavı", "Domates ve biberli", "55 TL", "Köy bulgurundan bol sebzeli, acılı ana yemek eşlikçisi."},
            {"İç Pilav", "Kuş üzümlü ve fıstıklı", "80 TL", "Tarçın, yenibahar ve dolmalık fıstık ile harmanlanmış padişah pilavı."},
            {"Siyez Bulgur Pilavı", "Kastamonu siyezi", "85 TL", "Bin yıllık ata tohumu siyez bulgurundan sağlıklı ve doyurucu."},
            {"Şehriyeli Pirinç Pilavı", "Klasik anne pilavı", "50 TL", "Tel şehriyesi kavrulmuş, tane tane dökülen sade pilav."},
            {"Su Böreği", "Hakiki peynirli ve tereyağlı", "95 TL", "Elde açılmış, haşlanmış yufkalarla hazırlanan kat kat Erzurum su böreği."},
            {"Paçanga Böreği", "Pastırmalı kaşarlı çıtır", "90 TL", "Çıtır yufka içinde çemenli Kayseri pastırması ve eriyen kaşar."},
            {"Sigara Böreği", "Lor peynirli ve maydanozlu", "60 TL", "İnce sarılmış, nar gibi kızarmış klasik çıtır yufka rulosu."},
            {"Çiğ Börek (Çibörek)", "Eskişehir usulü", "85 TL", "Kızgın yağda saniyeler içinde kabaran, içi sulu kıymalı Tatar böreği."},
            {"Gül Böreği", "Ispanaklı ve peynirli", "70 TL", "Fırında nar gibi kızarmış, el açması ıspanaklı rulo börek."},
            {"Anne Patatesi", "Elma dilim fırın patates", "65 TL", "Kekik, pul biber ve zeytinyağı ile fırınlanmış taze kabuklu patates."},
            {"Kumpir Patates", "Sade fırınlanmış, tereyağlı", "75 TL", "Fırından yeni çıkmış, içi kaşar ve tereyağıyla ezilmiş sade patates."},
            {"Mücver", "Kabak ve dereotlu", "70 TL", "Yanında sarımsaklı süzme yoğurt ile servis edilen kızarmış sebze köftesi."},
            {"Kuru Cacık", "Süzme yoğurtlu", "55 TL", "Küp salatalık, taze nane ve zeytinyağı damlalarıyla yoğun cacık."},
            {"Yoğurtlu Semizotu", "Sarımsaklı ve ferah", "60 TL", "Çıtır taze semizotu yaprakları, süzme yoğurt ve sızma zeytinyağı."},
            {"Otlu Gözleme", "Ege otları ve çökelek", "85 TL", "Odun ateşinde sac üzerinde pişmiş, ince hamurlu gözleme."},
            {"Lavaş ve Balon Pide", "Sıcacık tandır ekmeği", "30 TL", "Kebapların olmazsa olmazı, içi boş puf lavaş ve ince tandır ekmeği."},
            {"Ankara Çubuk Turşusu", "Kıtır salatalık", "45 TL", "Ev yapımı sirke ve sarımsakla kurulmuş coğrafi işaretli çıtır turşu."},
            {"Acı Biber Turşusu", "Süs ve cin biber", "40 TL", "Kebap ve kuru fasulyenin en sadık, acı dostu."}
        };

        String[][] mezeler = {
            {"Haydari", "Süzme yoğurt, nane, dereotu", "80 TL", "Sarımsak, beyaz peynir ezmesi ve sızma zeytinyağı ile çırpılmış enfes kıvam."},
            {"Humus", "Tahin, nohut, pastırmalı", "95 TL", "Üzerinde kızdırılmış tereyağı ve pastırma dilimleriyle sıcak humus."},
            {"Şakşuka", "Kızarmış patlıcan, domates", "75 TL", "Taze domates sosu, biber ve sarımsakla harmanlanmış Akdeniz klasiği."},
            {"Atom", "Süzme yoğurt, köz acı biber", "90 TL", "Acı sevenler için bol tereyağlı, kızarmış Arnavut biberli süzme yoğurt."},
            {"Girit Ezmesi", "Peynirli, fıstıklı, bademli", "95 TL", "Üç çeşit peynir, Antep fıstığı, ceviz ve sızma zeytinyağının muazzam buluşması."},
            {"Fava", "Zeytinyağlı, dereotlu", "70 TL", "Kırmızı soğan eşliğinde sunulan, Ege'nin vazgeçilmez bakla ezmesi."},
            {"Muhammara (Cevizli Ezme)", "Ceviz, biber salçası, nar ekşisi", "85 TL", "Antakya yöresine ait yoğun, hafif acılı ve baharatlı lezzet."},
            {"Zeytinyağlı Yaprak Sarma", "Kuş üzümlü, çam fıstıklı", "85 TL", "Tokat yaprağına incecik sarılmış, limonlu ve zeytinyağlı şölen."},
            {"Çerkez Tavuğu", "Cevizli ve sarımsaklı", "90 TL", "Didilmiş tavuk göğsü, ceviz içi, bayat ekmek içi ve özel baharat harmanıyla."},
            {"Havuç Tarator", "Havuçlu ve yoğurtlu", "75 TL", "Kavrulmuş havuç, süzme yoğurt, sarımsak ve sızma zeytinyağı."},
            {"Acılı Ezme", "İnce kıyım, bol baharatlı", "65 TL", "Kebapların vazgeçilmezi, nar ekşisi, taze nane ve domates ile hazırlanan ferah acı."},
            {"Babagannuş", "Köz patlıcan, tahin ve yoğurt", "80 TL", "Közlenmiş patlıcanın tahin ve sarımsaklı yoğurtla efsanevi buluşması."},
            {"Köz Patlıcan Salatası", "Közlenmiş, zeytinyağlı", "80 TL", "Köz ateşi kokulu, kırmızı kapya biber ve sarımsaklı ferah meze."},
            {"Arnavut Ciğeri", "Küp doğranmış, sumaklı", "120 TL", "Kızarmış küp patates ve bol sumaklı kırmızı soğan piyazı eşliğinde."},
            {"Çiğ Köfte", "Etsiz, cevizli, acılı", "70 TL", "Geleneksel yoğurma usulü, limon ve taze marul yatağında."},
            {"İmam Bayıldı", "Zeytinyağlı patlıcan rüyası", "85 TL", "Bütün patlıcanın bol soğan, domates ve zeytinyağı ile fırınlanmış hali."},
            {"Zeytinyağlı Enginar", "Garnitürlü, limonlu", "95 TL", "Ege'nin taze enginarları, bezelye ve havuçla zeytinyağında pişmiş."},
            {"Zeytinyağlı Barbunya", "Havuçlu ve patatesli", "75 TL", "Zeytinyağının barbunyayla buluştuğu, limon sıkılarak yenen klasik."},
            {"Kısır", "İnce bulgur, nar ekşili", "65 TL", "Bol yeşillikli, Antakya usulü nar ekşili ve salçalı ince bulgur salatası."},
            {"Köpoğlu", "Yoğurtlu kızartma", "80 TL", "Patlıcan, biber kızartması üzerine süzme yoğurt ve domates sosu."},
            {"Zeytinyağlı Pırasa", "Havuçlu ve pirinçli", "70 TL", "Kış aylarının şifası, hafif ekşili ve zeytinyağlı pırasa."},
            {"Pilaki", "Fasulye pilakisi", "70 TL", "Kırmızı soğan, havuç ve maydanoz eşliğinde zeytinyağlı kuru fasulye."},
            {"Lakerda", "Palamut tuzlaması", "140 TL", "Boğazın en güzel mezelerinden, kırmızı soğan halkalarıyla servis edilen balık mezesi."},
            {"Midye Dolma", "Baharatlı iç pilavlı", "90 TL", "Bol baharat, çam fıstığı ve kuş üzümü ile doldurulmuş Ege usulü midye (10 Adet)."},
            {"Deniz Börülcesi", "Sarımsaklı zeytinyağlı", "80 TL", "Ege'den taze toplanmış, nar ekşisi dokunuşuyla ferahlatıcı ot mezesi."}
        };

        String[][] tatlilar = {
            {"Padişah Lokumu", "Fıstık ezmesi ve kaymak", "180 TL", "Saray mutfağının gizli reçetesi. Günlük taze manda kaymağı ile sarılmış fıstık rüyası."},
            {"Künefe", "Özel Antakya peyniri ile", "160 TL", "Odun ateşinde nar gibi kızarmış, peyniri sünerek uzayan sıcak lezzet."},
            {"Havuç Dilimi Baklava", "Bol fıstıklı, iri dilim", "180 TL", "Özel yağlı fıstıklarla hazırlanan, arasına kaymak konularak yenilen dev dilim."},
            {"Gaziantep Baklavası", "İncecik 40 kat yufka", "150 TL", "Coğrafi işaretli fıstıklar ve sade yağ ile fırınlanmış geleneksel şaheser (3 dilim)."},
            {"Şöbiyet", "Kaymaklı ve fıstıklı", "160 TL", "İçi özel irmik kaymağı dolu, çıtır çıtır muska şeklinde baklava türevi."},
            {"Katmer", "İnce hamur, bol fıstık", "170 TL", "Gaziantep'ten gelen özel, taze kaymak ve fıstıkla sıcak servis edilen şölen."},
            {"Dondurmalı İrmik Helvası", "Sıcak ve soğuğun dansı", "110 TL", "Tereyağlı sıcak irmik helvası içine gizlenmiş, eriyen sakızlı Maraş dondurması."},
            {"Fırın Sütlaç", "Kavrulmuş fındık parçalı", "90 TL", "Toprak kasede taş fırında üzeri yanık bırakılmış, bol sütlü anne tatlısı."},
            {"Keşkül", "Bademli Osmanlı tatlısı", "85 TL", "Süt, badem ezmesi ve hindistan cevizi ile pişirilmiş hafif saray tatlısı."},
            {"Kazandibi", "Yanık süt lezzeti", "85 TL", "Tavuk göğsünün tepsi dibinde karamelize edilmesiyle elde edilen, tarçın serpilmiş tatlı."},
            {"Tavuk Göğsü", "Gerçek didiklenmiş etli", "90 TL", "İçinde incecik lifler halinde gerçek tavuk göğsü eti bulunan sakızlı sütlü tatlı."},
            {"Aşure", "Bereketi simgeleyen tatlı", "80 TL", "Buğday, nohut, fasulye, incir ve fındıkla kaynatılmış, üzeri nar taneli Nuh'un tatlısı."},
            {"Zerde", "Safranlı ve pirinçli", "75 TL", "Düğünlerin ve sarayın özel tatlısı; safran, kuş üzümü ve çam fıstıklı jölemsi lezzet."},
            {"Un Helvası", "Cevizli, tereyağlı", "70 TL", "Ağır ağır kavrularak rengini alan, geleneksel Türk ev helvası."},
            {"Höşmerim", "Peynir helvası, Balıkesir usulü", "95 TL", "Taze peynir, irmik ve yumurta sarısıyla hazırlanan efsanevi sarı renkli tatlı."},
            {"Revani", "Şerbetli irmik keki", "80 TL", "Osmanlı'nın Erivan (Revan) fethi şerefine yapılan, üzeri hindistan cevizli yumuşak kek."},
            {"Şekerpare", "Fındıklı, şerbetli kurabiye", "75 TL", "Ağızda dağılan hamuru ve tam kıvamında şerbetiyle geleneksel lezzet."},
            {"Ekmek Kadayıfı", "Afyon usulü, kaymaklı", "130 TL", "Özel mayalı hamurun şerbetle şişirilip, arasına kalın tabaka manda kaymağı konması."},
            {"Kemalpaşa Tatlısı", "Bursa Mustafakemalpaşa", "70 TL", "Özel peynirli hamur toplarının şerbette kaynatılarak sunulduğu hafif tatlı."},
            {"Ayva Tatlısı", "Kaymaklı ve karanfilli", "100 TL", "Kendi çekirdekleriyle kırmızı rengini almış karanfilli ayva ve kalın manda kaymağı."},
            {"Kabak Tatlısı", "Tahin ve cevizli Adapazarı", "95 TL", "Ağır ağır fırınlanmış bal kabağı, üzerine bol tahin ve taze ceviz kırığı dökülerek."},
            {"Güllaç", "Ramazan gülü, cevizli", "110 TL", "Gül suyu ile ıslatılmış ince yufkalar, bol süt, ceviz ve nar taneleriyle hafif şölen."},
            {"Dilber Dudağı", "Cevizli şerbetli", "85 TL", "Dudak şeklinde kapatılmış, çıtır çıtır yufkalı, bol cevizli geleneksel tatlı."},
            {"Bülbül Yuvası", "Ortası fıstıklı", "140 TL", "İncecik açılıp büzülen, ortasına bol miktarda bütün fıstık doldurulan baklava türü."},
            {"Sütlü Nuriye", "Süt şerbetli, bademli", "120 TL", "Baklavaya göre çok daha hafif olan, şerbeti süt ile yapılan bademli ve fıstıklı tatlı."},
            {"Tulumba Tatlısı", "Çıtır şerbetli", "60 TL", "Kızgın yağa dökülüp ardından soğuk şerbete atılarak dışı çıtır, içi sulu bırakılan tatlı."}
        };

        String[][] icecekler = {
            {"Osmanlı Şerbeti", "Demirhindi, tarçın", "75 TL", "İçinizi ferahlatacak, padişah sofralarının asırlık baharatlı şifalı şerbeti."},
            {"Reyhan Şerbeti", "Mor reyhan yapraklı", "70 TL", "Limon tuzu, karanfil ve taze mor reyhanın sıcak suyla demlenmesiyle hazırlanan şifa."},
            {"Kızılcık Şerbeti", "Ekşi ve tatlı dengesi", "75 TL", "Taze kızılcık meyvelerinden kaynatılarak elde edilen yakut renkli içecek."},
            {"Gül Şerbeti", "Isparta güllerinden", "70 TL", "Kokulu pembe gül yapraklarının şekerle ovalanıp damıtılmasıyla yapılan zarif şerbet."},
            {"Hardaliye", "Kırklareli'nin coğrafi işareti", "85 TL", "Üzüm şırası, vişne yaprağı ve siyah hardal tohumuyla fermente edilen alkolsüz üzüm içeceği."},
            {"Şıra", "İskender'in kadim dostu", "60 TL", "Kuru üzümlerin ezilip hafif fermente edilmesiyle hazırlanan, kebapların efsanevi eşlikçisi."},
            {"Susurluk Ayranı", "Bol köpüklü efsane", "45 TL", "Özel makinelerinden süzülerek bardaktan taşan, tuzlu ve inanılmaz köpüklü meşhur ayran."},
            {"Yayık Ayranı", "Köy usulü, bakır tasta", "40 TL", "Gerçek yoğurttan yayıkta dövülerek hazırlanan, üzeri tereyağı tanecikli köy ayranı."},
            {"Acılı Şalgam Suyu", "Adana'dan fıçı şalgamı", "40 TL", "Siyah havuç ve acı süs biberiyle fermente edilmiş, hazmı kolaylaştıran efsane içecek."},
            {"Acısız Şalgam Suyu", "Mideyi yormayan", "40 TL", "Acı sevmeyenler için özel olarak Adana'dan getirtilen hakiki fıçı şalgamı."},
            {"Boza", "Vefa usulü, sarı leblebili", "85 TL", "Darı irmiği, su ve şekerden fermente edilen, üzerine tarçın ve sarı leblebi dökülen kış klasiği."},
            {"Salep", "Gerçek orkide kökünden", "90 TL", "Dağ orkidelerinden elde edilen tozla sütte ağır ağır kaynatılan, bol tarçınlı iç ısıtan lezzet."},
            {"Közde Türk Kahvesi", "Bakır cezvede yavaş yavaş", "60 TL", "Mangal külünde ağır ağır pişen, bol köpüklü, yanında lokum ve suyla servis edilen geleneksel kahve."},
            {"Menengiç Kahvesi", "Güneydoğu Anadolu'dan", "65 TL", "Yabani fıstık (çitlenbik) ağacı meyvelerinden süt ile hazırlanan, kafeinsiz yöresel kahve."},
            {"Dibek Kahvesi", "Dövülerek öğütülmüş", "65 TL", "Kahve çekirdeklerinin değirmende değil, tahta havanlarda tokmakla dövülerek ezilmesiyle elde edilen kahve."},
            {"Mırra", "Arap ve Güneydoğu kültürü", "80 TL", "Kulpsuz fincanda, çok acı ve sert, defalarca kaynatılarak demlenen geleneksel misafir kahvesi."},
            {"Semaver Çayı", "Odun ateşinde demlenmiş", "35 TL", "Rize çayının bakır semaverde odun kömürüyle yavaşça demlendiği tavşan kanı çay."},
            {"İnce Belli Çay", "Klasik demli Türk çayı", "25 TL", "Her yemeğin sonunda ikram edilen, cam ince belli bardakta taze çay."},
            {"Ihlamur", "Kışın şifası", "50 TL", "Ayva yaprağı, tarçın kabuğu ve taze ıhlamur çiçekleriyle sıcak demlenmiş bitki çayı."},
            {"Adaçayı", "Ege dağlarından", "50 TL", "Taze toplanmış saplı adaçayının sıcak suyla buluştuğu ferahlatıcı yudum."},
            {"Kuşburnu Çayı", "C vitamini deposu", "55 TL", "Kurutulmuş dağ kuşburnu meyvelerinden elde edilen mayhoş, kırmızı renkli şifa kaynağı."},
            {"Beypazarı Maden Suyu", "Doğal mineralli", "30 TL", "Türkiye'nin en meşhur doğal mineralli, cam şişe maden suyu."},
            {"Niğde Gazozu", "Frambuaz aromalı", "40 TL", "Geleneksel Türk gazozu kültürünün en güzel örneklerinden, efsanevi ferahlık."}
        };

        // Sekmeleri oluşturup panele ekliyoruz
        sekmeliMenu.addTab("🥩 Ana Yemekler", kategoriSekmesiOlustur(anaYemekler));
        sekmeliMenu.addTab("🍟 Yan Yemekler", kategoriSekmesiOlustur(yanYemekler));
        sekmeliMenu.addTab("🥗 Mezeler", kategoriSekmesiOlustur(mezeler));
        sekmeliMenu.addTab("🍰 Tatlılar", kategoriSekmesiOlustur(tatlilar));
        sekmeliMenu.addTab("🍹 İçecekler", kategoriSekmesiOlustur(icecekler));

        add(ustPanel, BorderLayout.NORTH);
        add(sekmeliMenu, BorderLayout.CENTER);
    }

    // ==========================================
    // KATEGORİ SEKME ÜRETİCİSİ (3 SÜTUNLU YAPI)
    // ==========================================
    private JScrollPane kategoriSekmesiOlustur(String[][] urunler) {
        JPanel kartlarPaneli = new JPanel(new GridLayout(0, 3, 15, 15));
        kartlarPaneli.setBackground(kremZemin);
        kartlarPaneli.setBorder(BorderFactory.createEmptyBorder(15, 15, 20, 15));

        for (String[] urun : urunler) {
            kartlarPaneli.add(urunKartiOlustur(urun[0], urun[1], urun[2], urun[3]));
        }

        JScrollPane scrollPane = new JScrollPane(kartlarPaneli);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(20); 
        return scrollPane;
    }

    // ==========================================
    // KÜÇÜLTÜLMÜŞ KOMPAKT KART ÜRETİCİSİ
    // ==========================================
    private JPanel urunKartiOlustur(String urunAdi, String kisaAciklama, String fiyat, String detayliAciklama) {
        JPanel kart = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(beyaz);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.dispose();
            }
        };
        kart.setOpaque(false);
        kart.setLayout(new BorderLayout(5, 5)); 
        kart.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 225, 215), 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10) 
        ));

        // Metin Bölümü
        JPanel metinPaneli = new JPanel();
        metinPaneli.setLayout(new BoxLayout(metinPaneli, BoxLayout.Y_AXIS));
        metinPaneli.setBackground(beyaz);

        JLabel lblAd = new JLabel(urunAdi);
        lblAd.setFont(new Font("SansSerif", Font.BOLD, 15)); 
        lblAd.setForeground(koyuYesil);

        JLabel lblAciklama = new JLabel("<html><body style='width: 110px'>" + kisaAciklama + "</body></html>");
        lblAciklama.setFont(new Font("SansSerif", Font.ITALIC, 11)); 
        lblAciklama.setForeground(yaziRengi);

        JLabel lblFiyat = new JLabel(fiyat);
        lblFiyat.setFont(new Font("SansSerif", Font.BOLD, 14)); 
        lblFiyat.setForeground(altinBronz);

        metinPaneli.add(lblAd);
        metinPaneli.add(Box.createRigidArea(new Dimension(0, 3)));
        metinPaneli.add(lblAciklama);
        metinPaneli.add(Box.createRigidArea(new Dimension(0, 6)));
        metinPaneli.add(lblFiyat);

        // İncele Butonu
        JButton btnIncele = new JButton("İncele");
        btnIncele.setBackground(altinBronz); 
        btnIncele.setForeground(beyaz);
        btnIncele.setFont(new Font("SansSerif", Font.BOLD, 11)); 
        btnIncele.setFocusPainted(false);
        btnIncele.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIncele.setPreferredSize(new Dimension(70, 25)); 

        btnIncele.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "👨‍🍳 Şefin Notu: \n\n" + detayliAciklama + "\n\nFiyat: " + fiyat, 
                urunAdi + " Detayları", 
                JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel butonPaneli = new JPanel(new BorderLayout());
        butonPaneli.setBackground(beyaz);
        butonPaneli.add(btnIncele, BorderLayout.SOUTH);

        kart.add(metinPaneli, BorderLayout.CENTER);
        kart.add(butonPaneli, BorderLayout.EAST);

        return kart;
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
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        java.awt.EventQueue.invokeLater(() -> new Menu().setVisible(true));
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
