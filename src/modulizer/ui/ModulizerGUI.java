package modulizer.ui;

import Test.factory.ProcessModelFactory;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import javax.swing.JFileChooser;
import static modulizer.print.Print.printProcessModel;

import modulizer.algorithms.ModularizationAlgorithm;
import modulizer.algorithms.SingleEntrySingleExit;
import uflow.data.model.immutable.ProcessModel;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author @author August, Brigitte, Emanuel, Stefanie
 */
public class ModulizerGUI extends javax.swing.JFrame {

    ProcessModel model = ProcessModelFactory.createBspSeseEinfach();

    /**
     * Creates new form ModulizerGUI
     */
    public ModulizerGUI() {
        initComponents();
        for (Method m : ProcessModelFactory.class.getMethods()) {
            if (m.getName().contains("create")) {
                jComboBoxModel.addItem(m.getName());
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldPath = new javax.swing.JTextField();
        jButtonBrowse = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jComboBoxAlgorithm = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaOutput = new javax.swing.JTextArea();
        jButtonStart = new javax.swing.JButton();
        jComboBoxModel = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Modulizer");
        setMinimumSize(new java.awt.Dimension(800, 600));

        jPanel1.setMinimumSize(new java.awt.Dimension(640, 480));

        jLabel1.setText("Please select a Process Model:");

        jTextFieldPath.setEditable(false);
        jTextFieldPath.setToolTipText("Path to a Process Model");
        jTextFieldPath.setEnabled(false);
        jTextFieldPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldPathActionPerformed(evt);
            }
        });

        jButtonBrowse.setText("Browse...");
        jButtonBrowse.setEnabled(false);
        jButtonBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBrowseActionPerformed(evt);
            }
        });

        jLabel2.setText("Please select a modularization algorithm:");

        jComboBoxAlgorithm.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Single Entry Single Exit", "Data objects", "Repetition", "Clustering" }));

        jTextAreaOutput.setColumns(20);
        jTextAreaOutput.setRows(5);
        jScrollPane1.setViewportView(jTextAreaOutput);

        jButtonStart.setText("Start");
        jButtonStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStartActionPerformed(evt);
            }
        });

        jComboBoxModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxModelActionPerformed(evt);
            }
        });

        jLabel3.setText("Please choose the model to modularize:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jTextFieldPath)
                            .addComponent(jComboBoxAlgorithm, 0, 669, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonStart, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jComboBoxModel, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonBrowse))
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBoxModel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxAlgorithm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonStart))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldPathActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldPathActionPerformed

    private void jButtonBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBrowseActionPerformed

        final JFileChooser jFileChooser1 = new JFileChooser();
        FileNameExtensionFilter classfilter = new FileNameExtensionFilter("Class Files (*.class)", "class");

        jFileChooser1.setDialogTitle("Open Class File");
// set selected filter
        jFileChooser1.setFileFilter(classfilter);

        int returnVal = jFileChooser1.showOpenDialog(jPanel1);

        if (returnVal == 0) {
            File classFile = jFileChooser1.getSelectedFile();
            String fileName = classFile.getName().split("\\.")[0];
            String filePath = classFile.getPath().split(fileName)[0];
            System.out.println(filePath);
            System.out.println("Filename = " + fileName);
            if (classFile.getName().endsWith(".class")) {
                try {
                    jTextFieldPath.setText(jFileChooser1.getSelectedFile().getAbsolutePath());
                    URL url = new File(filePath).toURI().toURL();
//                    URL url = new URL("file:///" + filePath.replaceAll("\\", "/"));
                    URL[] urls = new URL[]{url};

                    URLClassLoader cl = new URLClassLoader(urls);
                    Class cls = null;
                    try {
                        cls = cl.loadClass(fileName);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ModulizerGUI.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NoClassDefFoundError e) {
                        int startIndex = e.getMessage().lastIndexOf(" ") + 1;
                        int endIndex = e.getMessage().length() - 1;
                        String classPackage = e.getMessage().substring(startIndex, endIndex).replaceAll("/", "\\.");
                        try {
                            cls = cl.loadClass(classPackage);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(ModulizerGUI.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                    if (cls != null) {
                        Object o = cls.newInstance();

                        for (Method m : cls.getMethods()) {
                            if (m.getName().substring(0, 6).equals("create")) {
                                jComboBoxModel.addItem(m.getName());
                                jComboBoxModel.setEnabled(true);
                            }
                        }
                    }
                    cl.close();
                } catch (InstantiationException | IllegalAccessException | IOException ex) {
                    Logger.getLogger(ModulizerGUI.class.getName()).log(Level.SEVERE, null, ex);
                } 
            } else {
                jTextFieldPath.setText("The selected File is not a *.class File. \n Please select a *.class File for modularization!");
            }

        }

    }//GEN-LAST:event_jButtonBrowseActionPerformed

    private void jButtonStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStartActionPerformed
        ModulizerGUI.jTextAreaOutput.setText("");
        ModularizationAlgorithm algorithm = new SingleEntrySingleExit(model);
        List<ProcessModel> modularized = algorithm.startModularization();
        modularized.forEach((x) -> {
            printProcessModel(x);
        }); //printProcessModel(model);
    }//GEN-LAST:event_jButtonStartActionPerformed

    private void jComboBoxModelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxModelActionPerformed
        for (Method m : ProcessModelFactory.class.getMethods()) {
            if (m.getName().equals(jComboBoxModel.getSelectedItem())) {
                try {
                    model = (ProcessModel) m.invoke(null, null);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(ModulizerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_jComboBoxModelActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ModulizerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new ModulizerGUI().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonBrowse;
    private javax.swing.JButton jButtonStart;
    private javax.swing.JComboBox<String> jComboBoxAlgorithm;
    private javax.swing.JComboBox<String> jComboBoxModel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTextArea jTextAreaOutput;
    private javax.swing.JTextField jTextFieldPath;
    // End of variables declaration//GEN-END:variables
}
