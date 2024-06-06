import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.rmi.RemoteException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends JFrame implements ActionListener {
    JButton limpiarListaButton;
    RandomDataGeneratorImpl generator;

    public Main() {
        setTitle("Server RMI");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        limpiarListaButton = new JButton("Limpiar Lista");
        limpiarListaButton.setBounds(70, 70, 150, 25);
        limpiarListaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    generator.clearCombinedArrays();
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });
        add(limpiarListaButton);

        try {
            java.rmi.registry.LocateRegistry.createRegistry(1099);

            generator = new RandomDataGeneratorImpl(2);

            Naming.rebind("//192.168.0.28/RandomDataGenerator", generator);

            System.out.println("Servidor RMI está listo.");
        } catch (Exception ex) {
            System.err.println("Error al iniciar el servidor RMI: " + ex.getMessage());
            ex.printStackTrace();
        }

        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}