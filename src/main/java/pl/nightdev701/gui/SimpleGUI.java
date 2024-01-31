package pl.nightdev701.gui;

/*

Lukas - 17:49
31.01.2024
https://github.com/NightDev701

© SunLightScorpion 2020 - 2024

*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

public class SimpleGUI extends JFrame {

    String name;
    int width;
    int height;

    public SimpleGUI(String name, int width, int height) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setName(name);
        setTitle(name);
        setSize(width, height);
    }

    public SimpleGUI resizeable(boolean value) {
        setResizable(value);
        return this;
    }

    public SimpleGUI registerMouse(MouseListener mouse) {
        addMouseListener(mouse);
        return this;
    }

    public SimpleGUI registerKeyboard(KeyListener key) {
        addKeyListener(key);
        return this;
    }

    public SimpleGUI addComponent(Component c) {
        add(c);
        return this;
    }

    public SimpleGUI defaultClose(int code) {
        setDefaultCloseOperation(code);
        return this;
    }

    public void build() {
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
