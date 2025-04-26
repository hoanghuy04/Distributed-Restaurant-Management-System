/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.awt.Component;

/**
 *
 * @author Trần Ngọc Huyền
 */
public class ReloadComponentUlti {
    public static void reload(Component c) {
        c.revalidate();
        c.repaint();
    }
}
