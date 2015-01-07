/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import shapes.*;
import json.*;

/**
 *
 * @author Ahmed Nouh
 */
public class UserInterface extends JFrame {

    JButton drawCircle, drawEllipse, drawLine, drawRec, drawSq, fillAll, save, load, drawTriangle, palette, undo, redo, edit;
    JRadioButton fill, withOutFill;
    final JPopupMenu popMenu;
    final JFileChooser browser;
    ListenToButtons buttonsListener;
    boolean isFilled = false;
    Color fillColor = Color.BLACK;
    Operation currentOperation = Operation.NONE;
    DrawingBoard drawingBoard;

    public UserInterface() throws CloneNotSupportedException {

        browser = new JFileChooser();
        Class ui = UserInterface.class;
        this.setLayout(new BorderLayout());
        this.setTitle("Paint v1.0");
        this.setSize(1200, 640);
        this.setLocation(500, 300); // use toolkit
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(true);
        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        buttonsListener = new ListenToButtons();
        drawingBoard = new DrawingBoard();

        drawCircle = makeButton("/circle48.png", "Draw a circle");
        drawEllipse = makeButton("/elipse48.png", "Draw an ellipse");
        drawLine = makeButton("/exacto48.png", "Draw a line");
        drawRec = makeButton("/rectangle48.png", "Draw a rectangle");
        drawSq = makeButton("/square48.png", "Draw a square");
        fillAll = makeButton("/painbucket48.png", "Paint bucket");
        drawTriangle = makeButton("/triangle48.png", "Draw a triangle");
        redo = makeButton("/Redo-icon.png", "Redo");
        undo = makeButton("/Undo-icon.png", "Undo");
        palette = makeButton("/palette48.png", "Pick a color");
        save = makeButton("/Save-icon.png", "Save");
        load = makeButton("/open-file48.png", "Load");

        fill = makeRadioButton("/rounded-rectangle48-unfilled.png", "/rounded-rectangle48.png", "Unfill/Fill");
        buttonsPanel.add(fill);
        buttonsPanel.add(drawCircle);
        buttonsPanel.add(drawEllipse);
        buttonsPanel.add(drawRec);
        buttonsPanel.add(drawSq);
        buttonsPanel.add(drawTriangle);
        buttonsPanel.add(drawLine);
        buttonsPanel.add(fillAll);
        buttonsPanel.add(palette);
        popMenu = makePopupMenu("Editing");
        JButton editTools = new JButton(new ImageIcon(getClass().getResource("/edit48.png")));
        editTools.setToolTipText("Edit");
        editTools.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                popMenu.show(e.getComponent(), e.getX() - 100, e.getY() - 100);
            }

        });
        buttonsPanel.add(editTools);

        buttonsPanel.add(undo);
        buttonsPanel.add(redo);
        buttonsPanel.add(save);
        buttonsPanel.add(load);
        this.add(buttonsPanel, BorderLayout.SOUTH);
        this.add(drawingBoard);
        this.setVisible(true);

    }

    private JButton makeButton(String icon, String tip) {
        JButton b = new JButton(new ImageIcon(getClass().getResource(icon)));
        b.setToolTipText(tip);
        b.addActionListener(buttonsListener);
        return b;
    }

    private JRadioButton makeRadioButton(String icon, String selectedIcon, String tip) {
        JRadioButton b = new JRadioButton(new ImageIcon(getClass().getResource(icon)));
        b.setSelectedIcon(new ImageIcon(getClass().getResource(selectedIcon)));
        b.setToolTipText(tip);
        b.addActionListener(buttonsListener);
        return b;
    }

    private JPopupMenu makePopupMenu(String label) {
        JPopupMenu pop = new JPopupMenu(label);
        pop.add(new JMenuItem(new AbstractAction("Recolor", new ImageIcon(getClass().getResource("/Oil-Paint-icon.png"))) {

            @Override
            public void actionPerformed(ActionEvent ae) {
                currentOperation = Operation.RECOLOR;
                for (Figure f : drawingBoard.getEditGroup()) {
                    f.setColor(fillColor);
                }
                drawingBoard.clearSelected();
                drawingBoard.repaint();
                try {
                    drawingBoard.addHistory(drawingBoard.getFigures());
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }));
        pop.add(new JMenuItem(new AbstractAction("Delete", new ImageIcon(getClass().getResource("/delete.png"))) {

            @Override
            public void actionPerformed(ActionEvent ae) {
                currentOperation = Operation.DELETE;
                drawingBoard.getFigures().removeAll(drawingBoard.getEditGroup());
                drawingBoard.clearSelected();
                drawingBoard.repaint();
                try {
                    drawingBoard.addHistory(drawingBoard.getFigures());
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }));
        pop.add(new JMenuItem(new AbstractAction("Move", new ImageIcon(getClass().getResource("/Cursor-Move-2-icon.png"))) {

            @Override
            public void actionPerformed(ActionEvent ae) {
                currentOperation = Operation.MOVE;
            }
        }));
        pop.add(new JMenuItem(new AbstractAction("Resize", new ImageIcon(getClass().getResource("/Resize-icon.png"))) {

            @Override
            public void actionPerformed(ActionEvent ae) {
                currentOperation = Operation.RESIZE;
                drawingBoard.clearSelected();
                drawingBoard.repaint();
            }
        }));
        return pop;
    }

    private class ListenToButtons implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {

            isFilled = fill.isSelected();
            if (ae.getSource() == palette) {
                fillColor = JColorChooser.showDialog(null, "Pick a Color", Color.BLACK);
            } else if (ae.getSource() == drawCircle) {
                currentOperation = Operation.CIRCLE;
            } else if (ae.getSource() == drawEllipse) {
                currentOperation = Operation.ELLIPSE;
            } else if (ae.getSource() == drawLine) {
                currentOperation = Operation.LINE;
            } else if (ae.getSource() == drawRec) {
                currentOperation = Operation.RECTANGLE;
            } else if (ae.getSource() == drawSq) {
                currentOperation = Operation.SQUARE;
            } else if (ae.getSource() == fillAll) {
                currentOperation = Operation.FILL;
            } else if (ae.getSource() == drawTriangle) {
                currentOperation = Operation.TRIANGLE;
            } else if (ae.getSource() == undo) {
                drawingBoard.undo();
            } else if (ae.getSource() == redo) {
                drawingBoard.redo();
            } else if (ae.getSource() == save) {

                int returnVal = browser.showOpenDialog(drawingBoard);
                String temp = "";
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    temp = browser.getSelectedFile().getAbsolutePath();

                    Color color = drawingBoard.getBackground();
                    new JsonWriter(color.getRGB(), drawingBoard.figures, temp);
                    BufferedImage image = new BufferedImage(drawingBoard.getWidth(), drawingBoard.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics g = image.getGraphics();
                    drawingBoard.paint(g);
                    try {
                        ImageIO.write(image, "png", new File(temp));
                    } catch (IOException ex) {
                        Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            } else if (ae.getSource() == load) {
                int returnVal = browser.showOpenDialog(drawingBoard);
                String temp = "";
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    temp = browser.getSelectedFile().getAbsolutePath();
                    if (temp.contains(".json")) {
                        JsonReader j = new JsonReader(drawingBoard.figures, temp);
                        drawingBoard.setBackground(new Color(j.getRgb()));
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Invalid File", "Load", JOptionPane.INFORMATION_MESSAGE);
                    }
                }

                repaint();
            }
            drawingBoard.clearSelected();
        }

    }

    private class DrawingBoard extends JPanel {

        private ArrayList<Figure> figures = new ArrayList<>();
        private ArrayList<Figure> dummies = new ArrayList<>();
        private ArrayList<ArrayList<Figure>> history = new ArrayList<>();
        private ArrayList<Figure> editGroup = new ArrayList<>();
        private Point start, end, upperLeft;
        private int HISTORY_IDX = 0;
        private ArrayList<Color> backgroundHistory = new ArrayList<>();
        private ArrayList<Rectangle2D> points = new ArrayList<>();
        private int sq = -1;

        public DrawingBoard() {
            super.setBackground(Color.white);
            history.add(new ArrayList<Figure>());
            backgroundHistory.add(super.getBackground());
            this.addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1 && currentOperation == Operation.RESIZE) {
                        for (Figure f : editGroup) {
                            if (f.toString().equals("Triangle")) {
                                if (points.get(0).contains(e.getPoint())) {
                                    sq = 0;
                                    f.resize(e.getX(), e.getY(), sq);
                                } else if (points.get(1).contains(e.getPoint())) {
                                    sq = 1;
                                    f.resize(e.getX(), e.getY(), sq);
                                } else if (points.get(2).contains(e.getPoint())) {
                                    sq = 2;
                                    f.resize(e.getX(), e.getY(), sq);
                                }

                            } else if (f.toString().equals("Line")) {
                                if (points.get(0).contains(e.getPoint())) {
                                    sq = 0;
                                    f.resize(e.getX(), e.getY(), sq);
                                } else if (points.get(1).contains(e.getPoint())) {
                                    sq = 1;
                                    f.resize(e.getX(), e.getY(), sq);
                                }
                            } else if (points.get(0).contains(e.getPoint())) {
                                sq = 0;
                                f.resize(e.getX() - f.getX(), e.getY() - f.getY());
                            } else if (points.get(1).contains(e.getPoint())) {
                                sq = 1;
                                f.resize(e.getX(), e.getY(), f.getcX() * 2 - f.getX() - e.getX(), f.getcY() * 2 - f.getY() - e.getY());
                            }
                            resizing(f);
                        }
                        repaint();
                    } else if (e.getButton() == MouseEvent.BUTTON1 && currentOperation == Operation.MOVE) {

                        int x0 = 5000, y0 = 5000;

                        for (Figure f : editGroup) {
                            x0 = (x0 < f.getcX()) ? x0 : f.getcX();
                            y0 = (y0 < f.getcY()) ? y0 : f.getcY();
                        }
                        for (Figure f : editGroup) {
                            f.move(e.getX() + f.getcX() - x0, e.getY() + f.getcY() - y0);
                        }
                        repaint();
                    } else if (e.getButton() == MouseEvent.BUTTON1 && currentOperation != Operation.NONE
                            && currentOperation != Operation.FILL && editGroup.isEmpty()) {
                        start = new Point(e.getX(), e.getY());
                        end = start;
                        repaint();
                        try {
                            drawShape(true); //added a dummy ( th first point) into figures make mouse dragged works propely
                        } catch (CloneNotSupportedException ex) {
                            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1 && (currentOperation == Operation.MOVE)) {

                        clearSelected();
                        currentOperation = Operation.NONE;
                        try {
                            addHistory(figures);
                        } catch (CloneNotSupportedException ex) {
                            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        repaint();

                    } else if (e.getButton() == MouseEvent.BUTTON1 && currentOperation == Operation.RESIZE) {
                        sq = -1;
                        resizing(null);
                        clearSelected();
                        try {
                            addHistory(figures);
                        } catch (CloneNotSupportedException ex) {
                            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        repaint();
                    } else if (e.getButton() == MouseEvent.BUTTON1 && currentOperation != Operation.NONE && currentOperation != Operation.FILL
                            && currentOperation != Operation.SELECTING && currentOperation != Operation.RESIZE
                            && currentOperation != Operation.MOVE) {
                        end = new Point(e.getX(), e.getY());
                        try {
                            drawShape(false);
                        } catch (CloneNotSupportedException ex) {

                        }
                        start = end = null;
                        dummies.clear(); // to delete the last dummy which the acutal shape overwriten it
                        repaint();
                    }
                }

                @Override
                public void mouseClicked(MouseEvent e) {

                    if (e.getButton() == MouseEvent.BUTTON1 && currentOperation == Operation.FILL) {
                        boolean background = true;

                        for (Figure f : figures) {
                            if (f.contains(e.getPoint())) {
                                background = false;
                                f.fill();
                                f.setColor(fillColor);
                            }
                        }
                        if (background) {
                            drawingBoard.setBackground(fillColor);
                            drawingBoard.clear();

                        }
                        try {
                            addHistory(figures);
                        } catch (CloneNotSupportedException ex) {
                            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        repaint();

                    } else if (e.getButton() == MouseEvent.BUTTON1) {
                        clearSelected();
                        resizing(null);
                        currentOperation = Operation.NONE;
                        repaint();
                    }

                    if (e.getButton() == MouseEvent.BUTTON3 && currentOperation == Operation.RESIZE) {
                        clearSelected();
                        resizing(null);
                        repaint();
                        for (Figure f : figures) {
                            if (f.contains(e.getPoint())) {
                                if (!f.isSelected()) {
                                    f.setSelected(true);
                                    editGroup.add(f);
                                    resizing(f);
                                    break;
                                }
                            }
                        }

                        repaint();
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        currentOperation = Operation.SELECTING;
                        for (Figure f : figures) {
                            if (f.contains(e.getPoint())) {
                                if (!f.isSelected()) {
                                    f.setSelected(true);
                                    editGroup.add(f);
                                }
                            }
                        }
                        repaint();
                    }
                }
            });
            this.addMouseMotionListener(new MouseMotionAdapter() {

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (currentOperation == Operation.RESIZE) {
                        for (Figure f : editGroup) {
                            if (f.toString().equals("Triangle") || f.toString().equals("Line")) {
                                f.resize(e.getX(), e.getY(), sq);
                            } else if (sq == 0) {

                                f.resize(e.getX() - f.getX(), e.getY() - f.getY());
                            } else if (sq == 1) {
                                f.resize(e.getX(), e.getY(), f.getcX() * 2 - f.getX() - e.getX(), f.getcY() * 2 - f.getY() - e.getY());
                            }
                            resizing(f);
                        }
                        repaint();
                    } else if (currentOperation == Operation.MOVE) {
                        int x0 = 5000, y0 = 5000;

                        for (Figure f : editGroup) {
                            x0 = (x0 < f.getcX()) ? x0 : f.getcX();
                            y0 = (y0 < f.getcY()) ? y0 : f.getcY();
                        }
                        for (Figure f : editGroup) {
                            f.move(e.getX() + f.getcX() - x0, e.getY() + f.getcY() - y0);
                        }
                        repaint();
                    } else if (e.getButton() == MouseEvent.BUTTON3) {

                    } else {
                        if (!dummies.isEmpty()) {
                            dummies.remove(dummies.size() - 1);
                        }
                        repaint();
                        if (end == null) {
                            return;
                        }
                        end = new Point(e.getX(), e.getY());
                        try {
                            drawShape(true);
                        } catch (CloneNotSupportedException ex) {
                            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        repaint();
                    }
                }

            });

        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D graphics = (Graphics2D) g;
            super.paintComponent(graphics);
            for (Figure f : figures) {
                f.draw(graphics);
            }
            if (!dummies.isEmpty()) {
                for (Figure f : dummies) {
                    f.draw(graphics);
                }
            }
            if (!points.isEmpty()) {
                graphics.setColor(Color.CYAN);
                if (fillColor == Color.CYAN) {
                    graphics.setColor(Color.BLACK);
                }
                resizing(editGroup.get(0));
                for (Rectangle2D p : points) {
                    graphics.fill(p);
                }

                graphics.setColor(fillColor);
            }
        }

        private void drawShape(boolean isDummy) throws CloneNotSupportedException {
            int width, height;
            upperLeft = new Point(Math.min(start.x, end.x), Math.min(start.y, end.y));
            width = Math.abs(start.x - end.x);
            height = Math.abs(start.y - end.y);
            switch (currentOperation) {
                case CIRCLE:
                    if (isDummy) {
                        dummies.add(new Circle(upperLeft.x, upperLeft.y, width, isFilled, fillColor));
                    } else {
                        figures.add(new Circle(upperLeft.x, upperLeft.y, width, isFilled, fillColor));
                    }
                    break;
                case ELLIPSE:
                    if (isDummy) {
                        dummies.add(new Ellipse(upperLeft.x, upperLeft.y, width, height, isFilled, fillColor));
                    } else {
                        figures.add(new Ellipse(upperLeft.x, upperLeft.y, width, height, isFilled, fillColor));
                    }
                    break;
                case LINE:
                    if (isDummy) {
                        dummies.add(new Line(start.x, start.y, end.x, end.y, fillColor));
                    } else {
                        figures.add(new Line(start.x, start.y, end.x, end.y, fillColor));
                    }
                    break;
                case RECTANGLE:
                    if (isDummy) {
                        dummies.add(new Rect(upperLeft.x, upperLeft.y, width, height, isFilled, fillColor));
                    } else {
                        figures.add(new Rect(upperLeft.x, upperLeft.y, width, height, isFilled, fillColor));
                    }
                    break;
                case SQUARE:
                    if (isDummy) {
                        dummies.add(new Square(upperLeft.x, upperLeft.y, width, isFilled, fillColor));
                    } else {
                        figures.add(new Square(upperLeft.x, upperLeft.y, width, isFilled, fillColor));
                    }
                    break;
                case TRIANGLE:
                    int[] xAxis = {upperLeft.x, upperLeft.x + width / 2, upperLeft.x - width / 2};
                    int[] yAxis = {upperLeft.y, upperLeft.y + height, upperLeft.y + height};
                    if (isDummy) {
                        dummies.add(new Triangle(xAxis, yAxis, isFilled, fillColor));
                    } else {
                        figures.add(new Triangle(xAxis, yAxis, isFilled, fillColor));
                    }
                    break;
                case NONE:
                    this.setToolTipText("Select a shape!");
                    break;
                default:
                    break;
            }
            if (!isDummy) {
                addHistory(figures);
            }
        }

        public void clear() {
            figures.clear();
        }

        public ArrayList<Figure> getFigures() {
            return figures;
        }

        public ArrayList<Figure> getEditGroup() {
            return editGroup;
        }

        public ArrayList<Figure> getDummies() {
            return dummies;
        }

        public int getHISTORY_IDX() {
            return HISTORY_IDX;
        }

        public int getHistorySize() {
            return history.size();
        }

        public void resizing(Figure f) {
            points.clear();
            if (f == null) {
                return;
            }
            if (f.toString().equals("Triangle")) {
                Triangle temp = (Triangle) f;
                Polygon p = temp.getPol();
                for (int i = 0; i < 3; i++) {
                    points.add(i, new Rectangle(p.xpoints[i] - 5, p.ypoints[i] - 5, 10, 10));
                }
                return;
            }
            if (f.toString().equals("Line")) {
                points.add(0, new Rectangle2D.Double(f.getcX() - 5, f.getcY() - 5, 10, 10));
                points.add(1, new Rectangle2D.Double(f.getX() - 5, f.getY() - 5, 10, 10));
                return;
            }
            points.add(0, new Rectangle2D.Double(f.getcX() * 2 - f.getX() - 5, f.getcY() * 2 - f.getY() - 5, 10, 10));
            points.add(1, new Rectangle2D.Double(f.getX() - 5, f.getY() - 5, 10, 10));

        }

        private void addHistory(ArrayList<Figure> figures) throws CloneNotSupportedException {
            ArrayList<Figure> temp = new ArrayList<>();
            HISTORY_IDX=history.size()-1;
            for (Figure f : figures) {
                temp.add((Figure) f.clone());
            }

            HISTORY_IDX++;
            history.add(HISTORY_IDX, temp);
            backgroundHistory.add(HISTORY_IDX, super.getBackground());

        }

        public void undo() {
            if (HISTORY_IDX > 0) {
                HISTORY_IDX--;
                figures.clear();
                figures.addAll(history.get(HISTORY_IDX));
                super.setBackground(backgroundHistory.get(HISTORY_IDX));
                repaint();
            }
        }

        public void redo() {
            if (HISTORY_IDX < history.size() - 1) {
                HISTORY_IDX++;
                figures.clear();
                figures.addAll(history.get(HISTORY_IDX));
                super.setBackground(backgroundHistory.get(HISTORY_IDX));
                repaint();
            }
        }

        public void clearSelected() {
            for (Figure f : editGroup) {
                f.setSelected(false);
            }
            editGroup.clear();
        }

    }
}
