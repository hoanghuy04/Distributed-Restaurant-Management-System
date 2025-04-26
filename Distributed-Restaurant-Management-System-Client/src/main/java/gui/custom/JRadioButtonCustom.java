/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui.custom;

import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JRadioButton;

/**
 *
 * @author Trần Ngọc Huyền
 */
public class JRadioButtonCustom extends JRadioButton {

    private String text;
    private Object object;

    public JRadioButtonCustom(String text, Object object) {
        super(text, null, false);
        this.text = text;
        this.object = object;
    }

    @Override
    public String toString() {
        return text;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
    
    public ButtonModel getButtonModel() {
        return this.getModel();  // Sử dụng phương thức getModel() của JRadioButton
    }

    public static JRadioButtonCustom getSelectedButton(ButtonGroup group, ButtonModel model) {
        for (Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.getModel() == model && button instanceof JRadioButtonCustom) {
                return (JRadioButtonCustom) button;  // Trả về JRadioButtonCustom
            }
        }
        return null;  // Nếu không tìm thấy nút nào khớp
    }

}
