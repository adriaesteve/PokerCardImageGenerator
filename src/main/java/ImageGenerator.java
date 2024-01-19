import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.image.BufferedImage;

public class ImageGenerator extends JFrame {

    private JTextField textField;

    public ImageGenerator() {
        // Configurar la ventana
        super("Image generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);

        // Crear componentes
        textField = new JTextField(10);
        textField.setPreferredSize(new Dimension(100, 100));

        // Configurar el diseño
        setLayout(new FlowLayout());
        add(new JLabel("Type cards and press enter. eg: th8s7d :"));
        add(textField);

        // Manejar el clic del botón
        textField.addActionListener(e -> generateImage());

        // Mostrar la ventana
        setVisible(true);
    }

    private void generateImage() {
        // Obtener el texto introducido
        String inputText = textField.getText().toUpperCase();

        // Verificar si el texto tiene al menos dos caracteres
        if (inputText.length() % 2 == 0 && inputText.length() > 0) {
            // Calcular el tamaño de la imagen en función del número de pares de caracteres
            int imageSize = (inputText.length() / 2) * 25;
            BufferedImage finalImage = new BufferedImage(imageSize, 25, BufferedImage.TYPE_INT_RGB);
            Graphics finalGraphics = finalImage.getGraphics();

            // Generar la imagen combinada con las letras y colores de fondo
            for (int i = 0; i < inputText.length(); i += 2) {
                char character = inputText.charAt(i);
                char bgColorCode = inputText.charAt(i + 1);
                Color bgColor = getColorFromCode(bgColorCode);

                // Crear la imagen para el par de caracteres
                BufferedImage image = new BufferedImage(25, 25, BufferedImage.TYPE_INT_RGB);
                Graphics g = image.getGraphics();

                // Configurar el color de fondo
                g.setColor(bgColor);
                g.fillRect(0, 0, 25, 25);

                // Configurar el color del carácter
                g.setColor(Color.BLACK);
                if (bgColor == Color.BLACK) {
                    g.setColor(Color.WHITE);
                }

                // Configurar la fuente y escribir el carácter
                Font font = new Font("Arial", Font.BOLD, 24);
                g.setFont(font);

                FontMetrics fm = g.getFontMetrics();
                int x = (25 - fm.stringWidth(String.valueOf(character))) / 2;
                int y = (25 - fm.getHeight()) / 2 + fm.getAscent();

                g.drawString(String.valueOf(character), x, y);

                // Copiar la imagen al segmento correspondiente de la imagen final
                finalGraphics.drawImage(image, i / 2 * 25, 0, null);
            }

            // Copiar la imagen final al portapapeles
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(new ImageSelection(finalImage), null);
        }

        textField.setText("");
    }

    private Color getColorFromCode(char code) {
        switch (code) {
            case 'S':
                return Color.BLACK;
            case 'H':
                return Color.RED;
            case 'C':
                return Color.GREEN;
            case 'D':
                return Color.CYAN;
            default:
                return Color.WHITE; // Color predeterminado si el código no es reconocido
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ImageGenerator::new);
    }
}

class ImageSelection implements Transferable {

    private final BufferedImage image;

    public ImageSelection(BufferedImage image) {
        this.image = image;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{DataFlavor.imageFlavor};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor == DataFlavor.imageFlavor;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
        if (flavor == DataFlavor.imageFlavor) {
            return image;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }
}