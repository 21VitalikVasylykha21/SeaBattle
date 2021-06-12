package GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 *Клас, для відображення заокругленої кнопки
 * @author Vitalik
 */
public class JFieldCircle extends JTextField{
    
    /**
     * Відросування заокрулої кнопки
     * @param g графічний параметр
     */
    @Override
     protected void paintComponent(Graphics g) {
        if (!isOpaque()) {
          int w = getWidth() - 1;
          int h = getHeight() - 1;
          Graphics2D g2 = (Graphics2D) g.create();
          g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
          g2.setPaint(UIManager.getColor("TextField.background"));
          g2.fillRoundRect(0, 0, w, h, h, h);
          g2.setPaint(Color.GRAY);
          g2.drawRoundRect(0, 0, w, h, h, h);
          g2.dispose();
        }
        super.paintComponent(g);
      }
      /**
       * Обновлення
       */
      @Override public void updateUI() {
        super.updateUI();
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
      }
      
      public void placeholder(String i) {
          JFieldCircle f = this;
        f.setForeground(Color.gray);
        f.setText(i);
        f.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (f.getText().equals(i)) {
                    f.setText("");
                    f.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (f.getText().isEmpty()) {
                    f.setForeground(Color.GRAY);
                    f.setText(i);
                }
            }
        });
    }
}
