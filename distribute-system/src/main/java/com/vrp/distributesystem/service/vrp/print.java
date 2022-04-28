package com.vrp.distributesystem.service.vrp;

import lombok.SneakyThrows;

import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class print extends JFrame {
    // 框架起点坐标
    private final int FREAME_X = 10;
    private final int FREAME_Y = 10;
    private final int FREAME_WIDTH = 700;// 横
    private final int FREAME_HEIGHT = 500;// 纵

    // 原点坐标
    private final int Origin_X = FREAME_X + 50;
    private final int Origin_Y = FREAME_Y + FREAME_HEIGHT - 30;

    // X,Y轴终点坐标
    private final int XAxis_X = FREAME_X + FREAME_WIDTH - 30;
    private final int XAxis_Y = Origin_Y;
    private final int YAxis_X = Origin_X;
    private final int YAxis_Y = FREAME_Y + 30;

    // X轴上的时间分度值（1分度=40像素）
    private final int TIME_INTERVAL = 62;
    // Y轴上值
    private final int PRESS_INTERVAL = 70;

    private Sol sol = new Sol();

    public print(Sol sol) {
        super("初始路径：");
        this.sol = sol;
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBounds(300, 200, 900, 600);
        MyCanvas trendChartCanvas = new MyCanvas();
        this.add(trendChartCanvas, BorderLayout.CENTER);
        this.setVisible(true);
        this.dispose();
    }

    // 画布重绘图
    class MyCanvas extends JPanel {
        private static final long serialVersionUID = 1L;

        @SneakyThrows
        public void paintComponent(Graphics g) {
            BufferedImage image = new BufferedImage(760, 600, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2D = image.createGraphics();

            Color[] c = {Color.RED,Color.GREEN,Color.BLACK,Color.BLUE,Color.MAGENTA,
                    Color.DARK_GRAY,Color.ORANGE,Color.YELLOW,Color.PINK};
            g2D.setColor(c[0]);
            super.paintComponent(g2D);

            // 绘制平滑点的曲线
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2D.setStroke(new BasicStroke(Float.parseFloat("1.0F")));

            int num = 0;
            for (int i=0;i<sol.getCust_seq().size();i++){
                if (sol.getCust_seq().get(i).getId() == 0){
                    g2D.setColor(c[++num]);
                }
                int x1 = Origin_X+((int)(sol.getCust_seq().get(i).getX()*10000-1180000)-9000)*62/500;
                int y1 = Origin_Y-((int)(sol.getCust_seq().get(i).getY()*10000-360000)-5500)*70/500;
                int x2,y2;
                if (i == sol.getCust_seq().size()-1){
                    x2 = Origin_X+((int)(sol.getCust_seq().get(0).getX()*10000-1180000)-9000)*62/500;
                    y2 = Origin_Y-((int)(sol.getCust_seq().get(0).getY()*10000-360000)-5500)*70/500;
                }
                else {
                    x2 = Origin_X+((int)(sol.getCust_seq().get(i+1).getX()*10000-1180000)-9000)*62/500;
                    y2 = Origin_Y-((int)(sol.getCust_seq().get(i+1).getY()*10000-360000)-5500)*70/500;
                }
                g2D.drawString(String.valueOf(sol.getCust_seq().get(i).getId()), x1, y1-10);
                g2D.drawLine(x1,y1,x2,y2);
            }

            // 画坐标轴
            g2D.setColor(c[0]);
            g2D.setStroke(new BasicStroke(Float.parseFloat("2.0F")));// 轴线粗度

            // X轴以及方向箭头
            g2D.drawLine(Origin_X, Origin_Y, XAxis_X, XAxis_Y);// x轴线的轴线
            g2D.drawLine(XAxis_X, XAxis_Y, XAxis_X - 5, XAxis_Y - 5);// 上边箭头
            g2D.drawLine(XAxis_X, XAxis_Y, XAxis_X - 5, XAxis_Y + 5);// 下边箭头

            // Y轴以及方向箭头
            g2D.drawLine(Origin_X, Origin_Y, YAxis_X, YAxis_Y);
            g2D.drawLine(YAxis_X, YAxis_Y, YAxis_X - 5, YAxis_Y + 5);
            g2D.drawLine(YAxis_X, YAxis_Y, YAxis_X + 5, YAxis_Y + 5);

            // 画X轴上的时间刻度（从坐标轴原点起，每隔TIME_INTERVAL(时间分度)像素画一时间点，到X轴终点止）
            g2D.setColor(Color.BLUE);
            g2D.setStroke(new BasicStroke(Float.parseFloat("1.0f")));

            // X轴刻度依次变化情况
            double j = 9000;
            for (int i = Origin_X; i < XAxis_X; i += TIME_INTERVAL) {
                g2D.drawString(" " + j, i - 10, Origin_Y + 20);
                j += 500;
            }
            g2D.drawString("客户x轴坐标", XAxis_X + 5, XAxis_Y + 5);

            // 画Y轴上血压刻度（从坐标原点起，每隔10像素画一压力值，到Y轴终点止）
            j = 5500;
            for (int i = Origin_Y; i > YAxis_Y; i -= PRESS_INTERVAL) {
                g2D.drawString(j + " ", Origin_X - 30, i + 3);
                j += 500;
            }
            g2D.drawString("客户y轴坐标", YAxis_X - 5, YAxis_Y - 5);// 血压刻度小箭头值

            ImageIO.write(image,"PNG", new File("C:\\Users\\周哲瑜\\Desktop\\导师制\\第二次\\vrp-system" +
                    "\\distribute-system\\distribution\\src\\assets\\path.png"));
        }
    }
}
