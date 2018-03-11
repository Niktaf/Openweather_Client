package openweather_based_weather_client;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import openweather_based_weather_client.OW_Collector;
import openweather_based_weather_client.JsonParser;
import org.json.JSONException;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 *
 * @author Παυλίδης Αριστείδης
 * @author Ταφραλίδης Νικόλαος
 * @author Τριανταφυλλίδης Τρύφων
 * ΘΕΣ-2 (2017-2018)
 */ 
    
public class frmMain extends javax.swing.JFrame {
    
    private Connection conn = null; // Δημιουργία αντικειμένου σύνδεσης
    
    // Αντικείμενα τα οποία φέρουν ερωτήματα του χρήστη προς τη βάση δεδομένων
    private PreparedStatement selectAll = null; // Επιλογή όλων των γραμμών
    private PreparedStatement selectCities = null; // Επιλογή δεδομένων με βάση τις πόλεις που επιλέγει ο χρήστης
    private PreparedStatement insertData = null; // Αντικείμενο εισαγωγής δεδομένων στη βάση δεδομένων
    
    
    /*
     * Με την μέθοδο isThere πραγματοποιείται έλεγχος για το αν υπάρχει η ζητούμενη στήλη μέσα
     * στον πίνακα που καθορίζεται μέσα στο ResultSet. Επιστρέφει true αν υπάρχει, ενώ επιστρέφει false αν
     * δεν υπάρχει η στήλη. 
    */
    private boolean isThere(ResultSet rs, String column){
        try {
            rs.findColumn(column);
            return true;
            } 
        catch (SQLException sqlex) {}
        return false;
    }
        
    /*
     * Με τη μέθοδο Sindesi() πραγματοποιείται σύνδεση με τη βάση δεδομένων, αφού πρώτα οριστούν:
     * 1. Ο τρόπος σύνδεσης και το όνομα της βάσης δεδομένων
     * 2. Τα στοιχεία (όνομα και συνθηματικό) του χρήστη 
    */
    public void Sindesi() throws SQLException {
           try {
                /*
                 * Σύνδεση με τη βάση δεδομένων. Αν αυτή δεν υπάρχει, τότε θα δημιουργθεί
                 * και θα διαμορφωθεί ανάλογα.
                */
                String dbURL = "jdbc:derby:dbWeather;create=true";
                conn = DriverManager.getConnection(dbURL);
                if (conn != null) { // Εάν πραγματοποιηθεί σύνδεση με τη βάση δεδομένων
                               
                /*
                 * Έλεγχος για το αν υπάρχουν οι απαιτούμενοι πίνακες στη βάση δεδομένων.
                 * Αν αυτοί δεν υπάρχουν, τότε θα δημιουργηθούν αυτόματα.
                */
                DatabaseMetaData md = conn.getMetaData();
                   
                // Ορισμός του πίνακα αναζήτησης
                ResultSet rsTables = md.getColumns(null, null, "tbl_cities", null);
                Statement createTable = conn.createStatement();
                    
                /*
                 * Έλεγχος για την ύπαρξη του πίνακα tbl_cities. Στην περίπτωση που δεν υπάρχει, 
                 * τότε θα δημιουργηθεί και θα περιέχει τις παρακάτω στήλες
                */
                    
                if (!isThere(rsTables, null)) {
                    createTable.executeUpdate("CREATE TABLE tbl_cities ("
                            + "city_id INTEGER NOT NULL, "
                            + "city VARCHAR(40) NOT NULL, "
                            + "PRIMARY KEY (city_ID))");
                     
                    JOptionPane.showMessageDialog(null, "Ο πίνακας tbl_cities δημιουργήθηκε με επιτυχία.");
                }
                    
                // Ομοίως και για τον πίνακα tbl_weatherdata 
                // Ορισμός του πίνακα αναζήτησης
                rsTables = md.getColumns(null, null, "tbl_weatherdata", null);                 
                 
                // Στην περίπτωση που δεν υπάρχει, τότε θα δημιουργηθεί και θα περιέχει τις παρακάτω στήλες
                if (!isThere(rsTables, null)) {
                    createTable.executeUpdate("CREATE TABLE tbl_weatherdata ("
                            + "id INTEGER NOT NULL PRIMARY KEY, "
                            + "city_id INTEGER NOT NULL, "
                            + "FOREIGN KEY (city_id) REFERENCES tbl_cities (City_ID), "
                            + "city VARCHAR(40) NOT NULL, "
                            + "temperature DECIMAL(4,1) NOT NULL, "
                            + "weather_description VARCHAR(255) NOT NULL, "
                            + "clounds DECIMAL(4,3) NOT NULL, "
                            + "wind_speed DECIMAL(5,1) NOT NULL, "
                            + "dt TIMESTAMP NOT NULL, "
                            + "rain INTEGER NOT NULL, "
                            + "snow INTEGER NOT NULL)");
                       
                    JOptionPane.showMessageDialog(null, "Ο πίνακας tbl_weatherdata δημιουργήθηκε με επιτυχία.");
                }
                    
                // Ομοίως και για τον πίνακα tbl_provlepseis 
                // Ορισμός του πίνακα αναζήτησης
                rsTables = md.getColumns(null, null, "tbl_provlepseis", null);                 
                    
                // Στην περίπτωση που δεν υπάρχει, τότε θα δημιουργηθεί και θα περιέχει τις παρακάτω στήλες
                if (!isThere(rsTables, null)) {
                    createTable.executeUpdate("CREATE TABLE tbl_provlepseis ("
                            + "id BIGINT GENERATED BY DEFAULT AS IDENTITY, "
                            + "city_id INTEGER NOT NULL, "
                            + "FOREIGN KEY (city_id) REFERENCES tbl_cities (City_ID), "
                            + "city VARCHAR(40) NOT NULL, "
                            + "temperature DECIMAL(4,1) NOT NULL, "
                            + "weather_description VARCHAR(255) NOT NULL, "
                            + "clounds DECIMAL(4,3) NOT NULL, "
                            + "wind_speed DECIMAL(5,1) NOT NULL, "
                            + "dt TIMESTAMP NOT NULL, "
                            + "rain INTEGER NOT NULL, "
                            + "snow INTEGER NOT NULL, "
                            + "PRIMARY KEY (id))");
                       
                    JOptionPane.showMessageDialog(null, "Ο πίνακας tbl_provlepseis δημιουργήθηκε με επιτυχία.");
                }
                    
                // Κλείσιμο διάφορων αντικειμένων
                rsTables.close();
                createTable.close();
                }
            conn.close(); // Τερματισμός σύνδεσης με τη βάση δεδομένων
                
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    
    /**
     * Δημιουργία νέας φόρμας frmMain
     */
    public frmMain() throws SQLException, JSONException {
        initComponents();
               
        /*
         * Παρακάτω πραγματοποιείται δημιουργία του URI με το οποίο θα ζητηθούν από το OpenWeather
         * όλες οι απαιτούμενες πληροφορίες, η λήψη αυτών με τη μορφή JSON string, η "αποσυναρμολόγηση"
         * του JSON string και η παράδοση των πληροφοριών του στον χρήστη προς περαιτέρω επεξεργασία.
         *
         * ΑΡΧΗ
         *
        */

        // Καθορισμός URI
        
        // URI τρεχουσών καιρικών συνθηκών
        String forecast = "http://api.openweathermap.org/data/2.5/group?id=2650225&units=metric&appid=fd798713a90f9501121e8dc78d7d0a47";
        
        // URI πρόβλεψης καιρικών συνθηκών για τις επόμενες 5 ημέρες
        //String forecast = "http://api.openweathermap.org/data/2.5/forecast?id=658225&appid=fd798713a90f9501121e8dc78d7d0a47";
        
        OW_Collector ow = new OW_Collector(); // Συλλέκτης δεδομένων
        String prognosis = null; // Πρόγνωση
        
        // Διάφορες μεταβλητές και αρχικοποίησή τους
        double temp=0, snow=0, rain=0; // θερμοκρασία
        int clouds=0; // Νεφοκάλυψη ουρανού
        String name="", desc="", dt=""; // Όνομα πόλης, περιγραφή καιρού, σφραγίδα χρονοσήμανσης
        long cityID=0; // Κωδικός πόλης
        
        try {
            prognosis = ow.HttpGet(forecast); // Λήψη δεδομένων σε μορφή JSON από τον κεντρικό εξυπηρετητή
        } catch (IOException e) { // Διαχείριση εξαιρέσεων 
            e.printStackTrace();
        }
        
        JsonParser jp = new JsonParser(prognosis); // Αποσυνθέτης JSON string
               
        try {
            // Λήψη δεδομένων από το JSON string
            temp = jp.get_Temp();
            name = jp.get_City();
            desc = jp.get_WeatherDesc();
            clouds = jp.get_CloudsAll();
            dt = jp.get_DateTime();
            cityID = jp.get_CityID();
        } catch (JSONException e) { // Διαχείριση εξαιρέσεων
            e.printStackTrace();
        }
        
        /*
         *
         * ΤΕΛΟΣ
         *
        */
                
        KairosTwraPanel.setVisible(false); //Απόκρυψη πάνελ τρέχοντος καιρού από το χρήστη, κατά την εκκίνηση του προγράμματος
        PrognwsiPanel.setVisible(false); //Απόκρυψη πάνελ πρόγνωσης καιρού από το χρήστη, κατά την εκκίνηση του προγράμματος
        StatistikaPanel.setVisible(false); //Απόκρυψη πάνελ στατιστικών από το χρήστη, κατά την εκκίνηση του προγράμματος
        jTable1.setModel(new DefaultTableModel()); // Καθαρισμός του πίνακα προβολής μετεορολογικών δεδομένων
        Sindesi(); // Σύνδεση με τη βάση δεδομένων κατά την έναρξη της εφαρμογής.
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        mainPanel = new javax.swing.JPanel();
        btnOKairosTwra = new javax.swing.JButton();
        btnPrognwsiKairou = new javax.swing.JButton();
        btnStatistika = new javax.swing.JButton();
        btnE3odos = new javax.swing.JButton();
        KairosTwraPanel = new javax.swing.JPanel();
        btnKairosTwra = new javax.swing.JButton();
        btnAnanewsi = new javax.swing.JButton();
        btnEpistrofi_KairosTwra = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstCities = new javax.swing.JList<>();
        PrognwsiPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cbCities_Prognwsi = new javax.swing.JComboBox<>();
        btnProvlepsi1asImeras = new javax.swing.JButton();
        btnProvlepsi5Imerwn = new javax.swing.JButton();
        btnAnanewsiProvlepsis = new javax.swing.JButton();
        btnEpistrofi_Progrnwsi = new javax.swing.JButton();
        StatistikaPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cbCities_Statistika = new javax.swing.JComboBox<>();
        btnMinMaxTemp = new javax.swing.JButton();
        btnTempPerCity = new javax.swing.JButton();
        btnEpistrofi_Statistika = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Open Weather");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"ενα", "", "", ""},
                {"", "", "", ""},
                {"", "", "", ""},
                {"", "", "", ""}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 656, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );

        btnOKairosTwra.setLabel("Ο καιρός τώρα");
        btnOKairosTwra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKairosTwraActionPerformed(evt);
            }
        });

        btnPrognwsiKairou.setLabel("Πρόγνωση καιρού");
        btnPrognwsiKairou.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrognwsiKairouActionPerformed(evt);
            }
        });

        btnStatistika.setLabel("Στατιστικά");
        btnStatistika.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStatistikaActionPerformed(evt);
            }
        });

        btnE3odos.setLabel("Έξοδος");
        btnE3odos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnE3odosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnOKairosTwra)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPrognwsiKairou)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnStatistika)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnE3odos)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOKairosTwra)
                    .addComponent(btnPrognwsiKairou)
                    .addComponent(btnStatistika)
                    .addComponent(btnE3odos))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        KairosTwraPanel.setName(""); // NOI18N

        btnKairosTwra.setText("Καιρός τώρα");
        btnKairosTwra.setMaximumSize(new java.awt.Dimension(170, 25));
        btnKairosTwra.setMinimumSize(new java.awt.Dimension(170, 25));
        btnKairosTwra.setPreferredSize(new java.awt.Dimension(170, 25));
        btnKairosTwra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKairosTwraActionPerformed(evt);
            }
        });

        btnAnanewsi.setText("Ανανέωση καιρού");
        btnAnanewsi.setMaximumSize(new java.awt.Dimension(170, 25));
        btnAnanewsi.setMinimumSize(new java.awt.Dimension(170, 25));
        btnAnanewsi.setPreferredSize(new java.awt.Dimension(170, 25));
        btnAnanewsi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnanewsiActionPerformed(evt);
            }
        });

        btnEpistrofi_KairosTwra.setText("Επιστροφή");
        btnEpistrofi_KairosTwra.setMaximumSize(new java.awt.Dimension(170, 25));
        btnEpistrofi_KairosTwra.setMinimumSize(new java.awt.Dimension(170, 25));
        btnEpistrofi_KairosTwra.setPreferredSize(new java.awt.Dimension(170, 25));
        btnEpistrofi_KairosTwra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEpistrofi_KairosTwraActionPerformed(evt);
            }
        });

        jLabel1.setText("Πόλη");

        lstCities.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Αθήνα", "Θεσσαλονίκη", "Πάτρα", "Λάρισα", "Ηράκλειο" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(lstCities);

        javax.swing.GroupLayout KairosTwraPanelLayout = new javax.swing.GroupLayout(KairosTwraPanel);
        KairosTwraPanel.setLayout(KairosTwraPanelLayout);
        KairosTwraPanelLayout.setHorizontalGroup(
            KairosTwraPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(KairosTwraPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(KairosTwraPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(KairosTwraPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(KairosTwraPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAnanewsi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnEpistrofi_KairosTwra, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnKairosTwra, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(KairosTwraPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        KairosTwraPanelLayout.setVerticalGroup(
            KairosTwraPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(KairosTwraPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(KairosTwraPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(KairosTwraPanelLayout.createSequentialGroup()
                        .addComponent(btnKairosTwra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAnanewsi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                        .addComponent(btnEpistrofi_KairosTwra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel2.setText("Πόλη: ");

        cbCities_Prognwsi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Αθήνα", "Θεσσαλονίκη", "Πάτρα", "Λάρισα", "Ηράκλειο" }));

        btnProvlepsi1asImeras.setText("Πρόβλεψη καιρού 1ας ημέρας");
        btnProvlepsi1asImeras.setMaximumSize(new java.awt.Dimension(170, 25));
        btnProvlepsi1asImeras.setMinimumSize(new java.awt.Dimension(170, 25));
        btnProvlepsi1asImeras.setPreferredSize(new java.awt.Dimension(170, 25));
        btnProvlepsi1asImeras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProvlepsi1asImerasActionPerformed(evt);
            }
        });

        btnProvlepsi5Imerwn.setLabel("Προβλεψη καιρού 5 ημερών");
        btnProvlepsi5Imerwn.setMaximumSize(new java.awt.Dimension(170, 25));
        btnProvlepsi5Imerwn.setMinimumSize(new java.awt.Dimension(170, 25));
        btnProvlepsi5Imerwn.setPreferredSize(new java.awt.Dimension(170, 25));
        btnProvlepsi5Imerwn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProvlepsi5ImerwnActionPerformed(evt);
            }
        });

        btnAnanewsiProvlepsis.setLabel("Ανανέωση πρόβλεψης καιρού");
        btnAnanewsiProvlepsis.setMaximumSize(new java.awt.Dimension(170, 25));
        btnAnanewsiProvlepsis.setMinimumSize(new java.awt.Dimension(170, 25));
        btnAnanewsiProvlepsis.setPreferredSize(new java.awt.Dimension(170, 25));
        btnAnanewsiProvlepsis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnanewsiProvlepsisActionPerformed(evt);
            }
        });

        btnEpistrofi_Progrnwsi.setLabel("Επιστροφή");
        btnEpistrofi_Progrnwsi.setMaximumSize(new java.awt.Dimension(170, 25));
        btnEpistrofi_Progrnwsi.setMinimumSize(new java.awt.Dimension(170, 25));
        btnEpistrofi_Progrnwsi.setPreferredSize(new java.awt.Dimension(170, 25));
        btnEpistrofi_Progrnwsi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEpistrofi_ProgrnwsiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PrognwsiPanelLayout = new javax.swing.GroupLayout(PrognwsiPanel);
        PrognwsiPanel.setLayout(PrognwsiPanelLayout);
        PrognwsiPanelLayout.setHorizontalGroup(
            PrognwsiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PrognwsiPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(PrognwsiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PrognwsiPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(1, 1, 1)
                        .addComponent(cbCities_Prognwsi, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnProvlepsi1asImeras, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnProvlepsi5Imerwn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PrognwsiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnEpistrofi_Progrnwsi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                        .addComponent(btnAnanewsiProvlepsis, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        PrognwsiPanelLayout.setVerticalGroup(
            PrognwsiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PrognwsiPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PrognwsiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cbCities_Prognwsi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnProvlepsi1asImeras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnProvlepsi5Imerwn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAnanewsiProvlepsis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEpistrofi_Progrnwsi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel3.setText("Πόλη: ");

        cbCities_Statistika.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Αθήνα", "Θεσσαλονίκη", "Πάτρα", "Λάρισα", "Ηράκλειο" }));

        btnMinMaxTemp.setText("Θερμοκρασία Min / Max");
        btnMinMaxTemp.setMaximumSize(new java.awt.Dimension(170, 25));
        btnMinMaxTemp.setMinimumSize(new java.awt.Dimension(170, 25));
        btnMinMaxTemp.setPreferredSize(new java.awt.Dimension(170, 25));
        btnMinMaxTemp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMinMaxTempActionPerformed(evt);
            }
        });

        btnTempPerCity.setText("Θερμοκρασία ανά πόλη");
        btnTempPerCity.setMaximumSize(new java.awt.Dimension(170, 25));
        btnTempPerCity.setMinimumSize(new java.awt.Dimension(170, 25));
        btnTempPerCity.setPreferredSize(new java.awt.Dimension(170, 25));
        btnTempPerCity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTempPerCityActionPerformed(evt);
            }
        });

        btnEpistrofi_Statistika.setText("Επιστροφή");
        btnEpistrofi_Statistika.setMaximumSize(new java.awt.Dimension(170, 25));
        btnEpistrofi_Statistika.setMinimumSize(new java.awt.Dimension(170, 25));
        btnEpistrofi_Statistika.setPreferredSize(new java.awt.Dimension(170, 25));
        btnEpistrofi_Statistika.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEpistrofi_StatistikaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout StatistikaPanelLayout = new javax.swing.GroupLayout(StatistikaPanel);
        StatistikaPanel.setLayout(StatistikaPanelLayout);
        StatistikaPanelLayout.setHorizontalGroup(
            StatistikaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StatistikaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(3, 3, 3)
                .addComponent(cbCities_Statistika, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(StatistikaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnEpistrofi_Statistika, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnMinMaxTemp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnTempPerCity, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        StatistikaPanelLayout.setVerticalGroup(
            StatistikaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StatistikaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(StatistikaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbCities_Statistika, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(btnMinMaxTemp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTempPerCity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEpistrofi_Statistika, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(164, 164, 164))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(KairosTwraPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(PrognwsiPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(StatistikaPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(KairosTwraPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PrognwsiPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(StatistikaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(114, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOKairosTwraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKairosTwraActionPerformed
        // TODO add your handling code here:
        mainPanel.setVisible(false); //Απόκρυψη πάνελ κεντρικού μενού
        KairosTwraPanel.setVisible(true); //Εμφάνιση πάνελ τρέχοντος καιρού
    }//GEN-LAST:event_btnOKairosTwraActionPerformed

    private void btnEpistrofi_KairosTwraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEpistrofi_KairosTwraActionPerformed
        // TODO add your handling code here:
        KairosTwraPanel.setVisible(false); //Απόκρυψη πάνελ τρέχοντος καιρού
        mainPanel.setVisible(true); //Εμφάνιση πάνελ κεντρικού μενού
    }//GEN-LAST:event_btnEpistrofi_KairosTwraActionPerformed

    private void btnPrognwsiKairouActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrognwsiKairouActionPerformed
        // TODO add your handling code here:
        mainPanel.setVisible(false); //Απόκρυψη πάνελ κεντρικού μενού
        PrognwsiPanel.setVisible(true); //Εμφάνιση πάνελ πρόγνωσης καιρού
    }//GEN-LAST:event_btnPrognwsiKairouActionPerformed

    private void btnEpistrofi_ProgrnwsiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEpistrofi_ProgrnwsiActionPerformed
        // TODO add your handling code here:
        PrognwsiPanel.setVisible(false); //Απόκρυψη πάνελ πρόγνωσης καιρού
        mainPanel.setVisible(true); //Εμφάνιση πάνελ κεντρικού μενού
    }//GEN-LAST:event_btnEpistrofi_ProgrnwsiActionPerformed

    private void btnProvlepsi5ImerwnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProvlepsi5ImerwnActionPerformed
        // TODO add your handling code here:
        jTable1.setModel(new DefaultTableModel()); // Καθαρισμός του πίνακα προβολής μετεορολογικών δεδομένων
    }//GEN-LAST:event_btnProvlepsi5ImerwnActionPerformed

    private void btnTempPerCityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTempPerCityActionPerformed
        // TODO add your handling code here:
        jTable1.setModel(new DefaultTableModel()); // Καθαρισμός του πίνακα προβολής μετεορολογικών δεδομένων
    }//GEN-LAST:event_btnTempPerCityActionPerformed

    private void btnEpistrofi_StatistikaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEpistrofi_StatistikaActionPerformed
        // TODO add your handling code here:
        StatistikaPanel.setVisible(false); //Απόκρυψη πάνελ στατιστικών
        mainPanel.setVisible(true); //Εμφάνιση πάνελ κεντρικού μενού
    }//GEN-LAST:event_btnEpistrofi_StatistikaActionPerformed

    private void btnStatistikaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStatistikaActionPerformed
        // TODO add your handling code here:
        mainPanel.setVisible(false); //Απόκρυψη πάνελ κεντρικού μενού
        StatistikaPanel.setVisible(true); //Εμφάνιση πάνελ στατιστικών
    }//GEN-LAST:event_btnStatistikaActionPerformed

    private void btnE3odosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnE3odosActionPerformed
        // TODO add your handling code here:
        System.exit(0); //Τερματισμός προγράμματος
    }//GEN-LAST:event_btnE3odosActionPerformed

    private void btnKairosTwraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKairosTwraActionPerformed
        // TODO add your handling code here:
        // Ενημέρωση του χρήστη ότι δεν έχει επιλέξει καμία πόλη
        if(lstCities.getSelectedIndex() == -1){
            JOptionPane.showMessageDialog(null, "Δεν έχετε επιλέξει καμία πόλη.");
        }
        else
        {
            jTable1.setModel(new DefaultTableModel()); // Καθαρισμός του πίνακα προβολής μετεορολογικών δεδομένων
        }
    }//GEN-LAST:event_btnKairosTwraActionPerformed

    private void btnAnanewsiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnanewsiActionPerformed
        // TODO add your handling code here:
        jTable1.setModel(new DefaultTableModel()); // Καθαρισμός του πίνακα προβολής μετεορολογικών δεδομένων
    }//GEN-LAST:event_btnAnanewsiActionPerformed

    private void btnProvlepsi1asImerasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProvlepsi1asImerasActionPerformed
        // TODO add your handling code here:
        jTable1.setModel(new DefaultTableModel()); // Καθαρισμός του πίνακα προβολής μετεορολογικών δεδομένων
    }//GEN-LAST:event_btnProvlepsi1asImerasActionPerformed

    private void btnAnanewsiProvlepsisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnanewsiProvlepsisActionPerformed
        // TODO add your handling code here:
        jTable1.setModel(new DefaultTableModel()); // Καθαρισμός του πίνακα προβολής μετεορολογικών δεδομένων
    }//GEN-LAST:event_btnAnanewsiProvlepsisActionPerformed

    private void btnMinMaxTempActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMinMaxTempActionPerformed
        // TODO add your handling code here:
        jTable1.setModel(new DefaultTableModel()); // Καθαρισμός του πίνακα προβολής μετεορολογικών δεδομένων
    }//GEN-LAST:event_btnMinMaxTempActionPerformed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new frmMain().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(frmMain.class.getName()).log(Level.SEVERE, null, ex);
                } catch (JSONException ex) {
                    Logger.getLogger(frmMain.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel KairosTwraPanel;
    private javax.swing.JPanel PrognwsiPanel;
    private javax.swing.JPanel StatistikaPanel;
    private javax.swing.JButton btnAnanewsi;
    private javax.swing.JButton btnAnanewsiProvlepsis;
    private javax.swing.JButton btnE3odos;
    private javax.swing.JButton btnEpistrofi_KairosTwra;
    private javax.swing.JButton btnEpistrofi_Progrnwsi;
    private javax.swing.JButton btnEpistrofi_Statistika;
    private javax.swing.JButton btnKairosTwra;
    private javax.swing.JButton btnMinMaxTemp;
    private javax.swing.JButton btnOKairosTwra;
    private javax.swing.JButton btnPrognwsiKairou;
    private javax.swing.JButton btnProvlepsi1asImeras;
    private javax.swing.JButton btnProvlepsi5Imerwn;
    private javax.swing.JButton btnStatistika;
    private javax.swing.JButton btnTempPerCity;
    private javax.swing.JComboBox<String> cbCities_Prognwsi;
    private javax.swing.JComboBox<String> cbCities_Statistika;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JList<String> lstCities;
    private javax.swing.JPanel mainPanel;
    // End of variables declaration//GEN-END:variables
}
