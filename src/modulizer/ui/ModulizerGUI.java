package modulizer.ui;

import Test.factory.ProcessModelFactory;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.ParseException;
import java.util.HashMap;
import javax.swing.JFileChooser;

import modulizer.algorithms.DataObjects;
import modulizer.algorithms.ModularizationAlgorithm;
import modulizer.algorithms.SingleEntrySingleExit;
import uflow.data.model.immutable.ProcessModel;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;
import static modulizer.print.Print.printModel;

/**
 *
 * @author @author August, Brigitte, Emanuel, Stefanie
 */
public class ModulizerGUI extends javax.swing.JFrame {

    ProcessModel model = ProcessModelFactory.createSeseEinfach();
    String chosenAlgorithm;
    Map<String, ProcessModel> modularizedMap = new HashMap<>();

    /**
     * Creates new form ModulizerGUI
     */
    public ModulizerGUI() {
        initComponents();
        for (Method m : ProcessModelFactory.class.getMethods()) {
            if (m.getName().contains("create")) {
                jComboBoxModel.addItem(m.getName().substring(6));
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
        jComboBoxModularizedModel = new javax.swing.JComboBox<>();
        jCheckBoxDataFlows = new javax.swing.JCheckBox();
        jSpinnerMinElements = new javax.swing.JSpinner();
        jLabelMinNumber = new javax.swing.JLabel();

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
        jComboBoxAlgorithm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxAlgorithmActionPerformed(evt);
            }
        });

        jTextAreaOutput.setColumns(20);
        jTextAreaOutput.setRows(5);
        jScrollPane1.setViewportView(jTextAreaOutput);

        jButtonStart.setText("Start");
        jButtonStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStartActionPerformed(evt);
            }
        });

        jLabel3.setText("Please choose the model to modularize:");

        jComboBoxModularizedModel.setEnabled(false);
        jComboBoxModularizedModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxModularizedModelActionPerformed(evt);
            }
        });

        jCheckBoxDataFlows.setText("include Data Flows");
        jCheckBoxDataFlows.setToolTipText("");

        jSpinnerMinElements.setModel(new javax.swing.SpinnerNumberModel(4, 4, null, 1));
        jSpinnerMinElements.setToolTipText("4 Elements = modularize every time");

        jLabelMinNumber.setText("min. Number of Elements for SESE Modularization");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jComboBoxModel, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxAlgorithm, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBoxModularizedModel, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldPath)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jSpinnerMinElements, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabelMinNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jCheckBoxDataFlows))
                                    .addComponent(jLabel2))
                                .addGap(0, 152, Short.MAX_VALUE)))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonStart, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jComboBoxAlgorithm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxModularizedModel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButtonStart, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinnerMinElements, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelMinNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxDataFlows))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
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
        ModularizationAlgorithm algorithm = null;
        chosenAlgorithm = jComboBoxAlgorithm.getSelectedItem().toString();
        setProcessModel();
        switch (chosenAlgorithm) {
            case "Single Entry Single Exit":
                try {
                    jSpinnerMinElements.commitEdit();
                } catch (ParseException e) {
                    jTextAreaOutput.setText("Nur Zahlen erlaubt!!!");
                }
                boolean dataObjectFlows = jCheckBoxDataFlows.isSelected();
                int minimalSese = (Integer) jSpinnerMinElements.getValue();
                System.out.println(dataObjectFlows + "   " + minimalSese);
                algorithm = new SingleEntrySingleExit(model, dataObjectFlows, minimalSese);
                break;
            case "Data objects":
                algorithm = new DataObjects(model);
                break;
            case "Repetition":
            case "Clustering":
            default:
                jTextAreaOutput.setText("Sorry! Something bad happend trying to use your chosen algorithm, please call your administrator!");
                break;
        }
        if (algorithm != null) {
            jComboBoxModularizedModel.removeAllItems();
            modularizedMap.clear();
            List<ProcessModel> modularized = algorithm.startModularization();
            jComboBoxModularizedModel.addItem("Print All");
            if (modularized.isEmpty()) {
                jTextAreaOutput.setText("Sorry, there was nothing to modularize!");
            }
            modularized.forEach((x) -> {
                modularizedMap.put(x.getId().getKey(), x);
                jComboBoxModularizedModel.addItem(x.getId().getKey());
                printModel(x, chosenAlgorithm);
            });
            jComboBoxModularizedModel.setEnabled(true);
        }
    }//GEN-LAST:event_jButtonStartActionPerformed

    private void setProcessModel() {
        for (Method m : ProcessModelFactory.class.getMethods()) {
            if (m.getName().equals("create" + jComboBoxModel.getSelectedItem())) {
                try {
                    model = (ProcessModel) m.invoke(null, null);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(ModulizerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }


    private void jComboBoxModularizedModelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxModularizedModelActionPerformed
        if (0 < jComboBoxModularizedModel.getItemCount()) {
            String selectedItem = jComboBoxModularizedModel.getSelectedItem().toString();
            jTextAreaOutput.setText("");
            if (!selectedItem.isEmpty() && !selectedItem.equals("Print All")) {
                printModel(modularizedMap.get(selectedItem), chosenAlgorithm);
            } else {
                modularizedMap.forEach((key, value) -> {
                    printModel(value, chosenAlgorithm);
                });
            }
        }
    }//GEN-LAST:event_jComboBoxModularizedModelActionPerformed

    private void jComboBoxAlgorithmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxAlgorithmActionPerformed
        chosenAlgorithm = jComboBoxAlgorithm.getSelectedItem().toString();
        switch (chosenAlgorithm) {
            case "Single Entry Single Exit":
                jSpinnerMinElements.setVisible(true);
                jCheckBoxDataFlows.setVisible(true);
                jLabelMinNumber.setVisible(true);
                break;
            case "Data objects":
            case "Repetition":
            case "Clustering":
                jSpinnerMinElements.setVisible(false);
                jCheckBoxDataFlows.setVisible(false);
                jLabelMinNumber.setVisible(false);
                break;
            default:
                jTextAreaOutput.setText("Sorry! Something bad happend trying to use your chosen algorithm, please call your administrator!");
                break;
        }
    }//GEN-LAST:event_jComboBoxAlgorithmActionPerformed

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
    private javax.swing.JCheckBox jCheckBoxDataFlows;
    private javax.swing.JComboBox<String> jComboBoxAlgorithm;
    private javax.swing.JComboBox<String> jComboBoxModel;
    private javax.swing.JComboBox<String> jComboBoxModularizedModel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelMinNumber;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinnerMinElements;
    private static javax.swing.JTextArea jTextAreaOutput;
    private javax.swing.JTextField jTextFieldPath;
    // End of variables declaration//GEN-END:variables

    public static JTextArea getjTextAreaOutput() {
        return jTextAreaOutput;
    }

    public static void setjTextAreaOutput(JTextArea jTextAreaOutput) {
        ModulizerGUI.jTextAreaOutput = jTextAreaOutput;
    }

}
