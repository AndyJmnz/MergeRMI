import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainCliente extends JFrame implements ActionListener {

    JLabel nombre_original, nombre_resultado, nombre2_resultado, nombre3_resultado, tiempo_label, tiempo2_label, tiempo3_label;
    JTextField Cantidad, TiempoMergeField, TiempoForkField, TiempoExecutorField;
    JTextArea NumerosAOrdenar, ResultadoMerge, ResultadoFork, ResultadoExecutor;
    JButton generarButton, ordenarMergeButton, ordenarForkJoinButton, ordenarExecuteButton, limpiarButton;

    private int[] generatedArray;

    RandomDataGenerator generator = null;
    int puerto = 1099;
    String url = "//192.168.0.28:" + puerto + "/RandomDataGenerator";

    public MainCliente() {
        setTitle("Cliente RMI");
        setSize(520, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        nombre_original = new JLabel("Cantidad de Datos");
        nombre_original.setBounds(50, 20, 150, 25);
        add(nombre_original);

        Cantidad = new JTextField();
        Cantidad.setBounds(50, 45, 100, 25);
        add(Cantidad);

        JScrollPane scrollPane1 = new JScrollPane();
        NumerosAOrdenar = new JTextArea();
        NumerosAOrdenar.setEditable(false);
        scrollPane1.setViewportView(NumerosAOrdenar);
        scrollPane1.setBounds(50, 80, 400, 45);
        add(scrollPane1);

        nombre_resultado = new JLabel("Resultado Secuencial");
        nombre_resultado.setBounds(50, 130, 150, 25);
        add(nombre_resultado);

        JScrollPane scrollPane2 = new JScrollPane();
        ResultadoMerge = new JTextArea();
        ResultadoMerge.setEditable(false);
        scrollPane2.setViewportView(ResultadoMerge);
        scrollPane2.setBounds(50, 150, 400, 45);
        add(scrollPane2);

        nombre2_resultado = new JLabel("Resultado ForkJoin");
        nombre2_resultado.setBounds(50, 210, 100, 25);
        add(nombre2_resultado);

        JScrollPane scrollPane3 = new JScrollPane();
        ResultadoFork = new JTextArea();
        ResultadoFork.setEditable(false);
        scrollPane3.setViewportView(ResultadoFork);
        scrollPane3.setBounds(50, 240, 400, 45);
        add(scrollPane3);

        nombre3_resultado = new JLabel("Resultado Executor S.");
        nombre3_resultado.setBounds(50, 300, 100, 25);
        add(nombre3_resultado);

        JScrollPane scrollPane4 = new JScrollPane();
        ResultadoExecutor = new JTextArea();
        ResultadoExecutor.setEditable(false);
        scrollPane4.setViewportView(ResultadoExecutor);
        scrollPane4.setBounds(50, 330, 400, 45);
        add(scrollPane4);

        tiempo_label = new JLabel("T. Secuencial");
        tiempo_label.setBounds(50, 385, 150, 25);
        add(tiempo_label);

        TiempoMergeField = new JTextField();
        TiempoMergeField.setBounds(50, 410, 100, 25);
        TiempoMergeField.setEditable(false);
        add(TiempoMergeField);

        tiempo2_label = new JLabel("T. ForkJoin");
        tiempo2_label.setBounds(200, 385, 150, 25);
        add(tiempo2_label);

        TiempoForkField = new JTextField();
        TiempoForkField.setBounds(200, 410, 100, 25);
        TiempoForkField.setEditable(false);
        add(TiempoForkField);

        tiempo3_label = new JLabel("T. Executor S.");
        tiempo3_label.setBounds(350, 385, 150, 25);
        add(tiempo3_label);

        TiempoExecutorField = new JTextField();
        TiempoExecutorField.setBounds(350, 410, 100, 25);
        TiempoExecutorField.setEditable(false);
        add(TiempoExecutorField);

        generarButton = new JButton("Enviar");
        generarButton.setBounds(50, 450, 100, 25);
        generarButton.addActionListener(this);
        add(generarButton);

        limpiarButton = new JButton("Limpiar");
        limpiarButton.setBounds(200, 450, 100, 25);
        limpiarButton.addActionListener(this);
        add(limpiarButton);

        ordenarMergeButton = new JButton("Secuencial");
        ordenarMergeButton.setBounds(50, 480, 100, 25);
        ordenarMergeButton.addActionListener(this);
        add(ordenarMergeButton);

        ordenarForkJoinButton = new JButton("ForkJoin");
        ordenarForkJoinButton.setBounds(200, 480, 100, 25);
        ordenarForkJoinButton.addActionListener(this);
        add(ordenarForkJoinButton);

        ordenarExecuteButton = new JButton("Execute");
        ordenarExecuteButton.setBounds(350, 480, 100, 25);
        ordenarExecuteButton.addActionListener(this);
        add(ordenarExecuteButton);

        try {
            generator = (RandomDataGenerator) Naming.lookup(url);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            throw new RuntimeException(e);
        }
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainCliente::new);
    }

    private int[] generateData() {
        int count = Integer.parseInt(Cantidad.getText());
        generatedArray = new Random().ints(count, 0, 100).toArray();
        StringBuilder generatedData = new StringBuilder();
        for (int num : generatedArray) {
            generatedData.append(num).append(" ");
        }
        NumerosAOrdenar.setText(generatedData.toString());
        return generatedArray;
    }

    private void sendData(int[] data) {
        clearServerData();
        try {
            generator.addArray(data);
            JOptionPane.showMessageDialog(this, "Datos enviados al servidor.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al enviar datos al servidor.", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    private void clearServerData() {
        try {
            generator.clearData();
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Error clearing server data.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void sortAndDisplay(int[] data, JTextArea resultArea, JTextField timeField, Runnable sortAlgorithm) {
        long startTime = System.nanoTime();
        sortAlgorithm.run();
        long endTime = System.nanoTime();
        long durationInNano = endTime - startTime;

        double durationInMillis = durationInNano / 1_000_000.0;
        DecimalFormat df = new DecimalFormat("0.000000");
        StringBuilder timeDetail = new StringBuilder();
        timeDetail.append(" ").append(df.format(durationInMillis)).append(" ms");
        StringBuilder sortedData = new StringBuilder();
        for (int num : data) {
            sortedData.append(num).append(" ");
        }
        resultArea.setText(sortedData.toString());
        timeField.setText(timeDetail.toString());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == generarButton) {
            generatedArray = generateData();
        } else if (e.getSource() == ordenarMergeButton) {
            try {
                sendData(generatedArray);
                int[] dataC = generator.combineArrays();
                sortAndDisplay(dataC, ResultadoMerge, TiempoMergeField, () -> {
                    LogicaMerge mergeLogic = new LogicaMerge();
                    mergeLogic.mergeSort(dataC);
                });
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource() == ordenarForkJoinButton) {
            try {
                sendData(generatedArray);
                int[] dataC = generator.combineArrays();
                sortAndDisplay(dataC, ResultadoFork, TiempoForkField, () -> {
                    LogicaForkJoin logicaForkJoin = new LogicaForkJoin();
                    logicaForkJoin.ForkJoin(dataC);
                });
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource() == ordenarExecuteButton) {
            try {
                sendData(generatedArray);
                int[] dataC = generator.combineArrays();
                sortAndDisplay(dataC, ResultadoExecutor, TiempoExecutorField, () -> {
                    LogicaExecuteService logicaExecuteService = new LogicaExecuteService();
                    logicaExecuteService.executorService(dataC);
                });
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource() == limpiarButton) {
            limpiarCampos();
        }
    }

    private void limpiarCampos() {
        Cantidad.setText("");
        NumerosAOrdenar.setText("");
        ResultadoMerge.setText("");
        ResultadoFork.setText("");
        ResultadoExecutor.setText("");
        TiempoMergeField.setText("");
        TiempoForkField.setText("");
        TiempoExecutorField.setText("");
    }
}