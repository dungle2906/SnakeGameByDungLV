package snake_game;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class snakeGame extends JFrame implements ActionListener, KeyListener {       
	boolean loss = false;
	int maxXY = 50;
	int m = 20, n = 35;
	int start = 0;    
        /*
        a[i][j] = 0: Là khoảng trống.
        a[i][j] = 1: Là thân rắn.
        a[i][j] = 2: Là đầu rắn.
        a[i][j] = 3: Là tao xanh.
        a[i][j] = 4: Là tao vang.	
        */
	Color background_cl[] = {Color.WHITE, Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW};       
        /*
        khi rắn đi lên trên thì tọa độ đầu rắn là (x-1, y)
        khi rắn đi qua phải thì tọa độ đầu rắn là (x, y+1)
        khi rắn đi xuống dưới thì tọa độ đầu rắn là (x+1, y)
        khi rắn đi qua trái thì tọa độ đầu rắn là (x, y-1)
        */
	int convertX[] = {-1, 0, 1, 0};
	int convertY[] = {0, 1, 0, -1};   
        int slowSpeed=30;
        int speedIndex=0;
	int speed[] = {400, 350, 280, 200, 100};
	private JButton bt[][] = new JButton[maxXY][maxXY];
	private JComboBox lv = new JComboBox();
        
	private int a[][] = new int[maxXY][maxXY];        
	private int xSnake[] = new int[maxXY * maxXY];
	private int ySnake[] = new int[maxXY * maxXY];
        
        private int xFood2, yFood2;
	private int xFood, yFood;
	private int sizeSnake = 0;
        
	private int direction = 2;
	private JButton newGame_bt, score_bt;
	
	private JPanel pn, pn2;
	Container cn;
	Timer timer;
        
	public snakeGame(String s, int k) {
		super(s);
		cn = init(k);  
		timer = new Timer(slowSpeed, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				runSnake(direction);
			}
		});
	}
        
	public Container init(int k) {
		Container cn = this.getContentPane();
		pn = new JPanel();
		pn.setLayout(new GridLayout(m,n));
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++){
				bt[i][j] = new JButton();
				pn.add(bt[i][j]);
				bt[i][j].setActionCommand(i + " " + j);
				bt[i][j].addActionListener(this);
				bt[i][j].addKeyListener(this);
				bt[i][j].setBorder(null);
				a[i][j] = 0;
			}
		
		pn2 = new JPanel();
		pn2.setLayout(new FlowLayout());
		
		newGame_bt = new JButton("NEW GAME");
		newGame_bt.addActionListener(this);
		newGame_bt.addKeyListener(this);
		newGame_bt.setFont(new Font("UTM Micra", 1, 15));
		newGame_bt.setBackground(Color.white);
		
		score_bt = new JButton("3");
		score_bt.addActionListener(this);
		score_bt.addKeyListener(this);
		score_bt.setFont(new Font("UTM Micra", 1, 15));
		score_bt.setBackground(Color.white);

//		for (int i = 1; i <= speed.length; i++)
//		lv.addItem("LEVEL " + i);
//		lv.setSelectedIndex(k);
//		lv.addKeyListener(this);
//		lv.setFont(new Font("UTM Micra", 1, 15));
//		lv.setBackground(Color.white);
		
		pn2.add(newGame_bt);
		//pn2.add(lv);
		pn2.add(score_bt);
                
		
                
                // thân rắn
		a[m / 2][n / 2 - 1] = 1;
                // thân rắn
		a[m / 2][n / 2] = 1;
                // đầu rắn
		a[m / 2][n / 2 + 1] = 2;
		xSnake[0] = m / 2;
		ySnake[0] = n / 2 - 1;
		xSnake[1] = m / 2;
		ySnake[1] = n / 2;
		xSnake[2] = m / 2;
		ySnake[2] = n / 2 + 1;
		sizeSnake = 3;
		
		creatGreenAplle();
		updateColor();
		cn.add(pn);
		cn.add(pn2, "South");
		this.setVisible(true);
		this.setSize(n * 30, m * 30);
		this.setLocationRelativeTo(null);
		return cn;
	}
        
	public void updateColor() {
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
				bt[i][j].setBackground(background_cl[a[i][j]]);
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	public void runSnake(int k) {
		a[xSnake[sizeSnake - 1]][ySnake[sizeSnake - 1]] = 1;

		xSnake[sizeSnake] = xSnake[sizeSnake - 1] + convertX[k - 1];
		ySnake[sizeSnake] = ySnake[sizeSnake - 1] + convertY[k - 1];
		               
		if (xSnake[sizeSnake] < 0)
			xSnake[sizeSnake] = m - 1;
                // khi rắn xuống đáy thì cho xuất hiện ở biên đối diện
		if (xSnake[sizeSnake] == m)
			xSnake[sizeSnake] = 0;
		if (ySnake[sizeSnake] < 0)
			ySnake[sizeSnake] = n - 1;
		if (ySnake[sizeSnake] == n)
			ySnake[sizeSnake] = 0;
		                
		if (a[xSnake[sizeSnake]][ySnake[sizeSnake]] == 1) {
			timer.stop();
			JOptionPane.showMessageDialog(null, "GAME OVER!");
			loss = true;
			return;
		}
                // cta có start dùng để di chuyển
		a[xSnake[start]][ySnake[start]] = 0; // cắt đuôi rắn khi di chuyển
		if (a[xSnake[sizeSnake]][ySnake[sizeSnake]] == 3) {
                        a[xSnake[start]][ySnake[start]] = 1;
                        start--;
                        removeApple(xFood2, yFood2); 
                        if(sizeSnake%5==0){
                            speedIndex++;
                            if (speedIndex < speed.length) {
                            timer.setDelay(speed[speedIndex]);
                            }
                        creatGreenAplle();
                        createYellowApple();
                        score_bt.setText(String.valueOf(Integer.parseInt(score_bt.getText())+1));      
                        }else{
			creatGreenAplle();
			score_bt.setText(String.valueOf(Integer.parseInt(score_bt.getText())+1));       
                        }
                }                      
                if(a[xSnake[sizeSnake]][ySnake[sizeSnake]] == 4){
                        a[xSnake[start]][ySnake[start]] = 1;
                        start++;
                        score_bt.setText(String.valueOf(Integer.parseInt(score_bt.getText())-1));        
                        a[xSnake[start-1]][ySnake[start-1]] = 0;
                        a[xSnake[start]][ySnake[start]] = 0;
                        removeApple(xFood, yFood);
                        creatGreenAplle();
                }
                
		a[xSnake[sizeSnake]][ySnake[sizeSnake]] = 2; // nối tiếp đầu rắn
		start++; // dùng để di chuyển rắn
                sizeSnake++; // tăng chiều dài con rắn
		updateColor();
		for (int i = start; i < sizeSnake; i++) {
			xSnake[i - start] = xSnake[i];
			ySnake[i - start] = ySnake[i];
		}
		sizeSnake -= start;
		start = 0;
	}
        public void removeApple(int x, int y){
            a[x][y]=0;
            updateColor();
        }
        
	public void creatGreenAplle() {
        int k = 0;
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                if (a[i][j] == 0)
                    k++;

        int h = (int) ((k - 1) * Math.random() + 1);
        k = 0;

        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++) {
                if (a[i][j] == 0) {
                    k++;
                    if (k == h) {
                        xFood = i;
                        yFood = j;
                        a[i][j] = 3;
                        return;
                    }
                }
            }
        }
        
        public void createYellowApple() {
            int k = 0;
            for (int i = 0; i < m; i++)
                for (int j = 0; j < n; j++)
                    if (a[i][j] == 0)
                        k++;

            int h = (int) ((k - 1) * Math.random() + 1);
            k = 0;

            for (int i = 0; i < m; i++)
                for (int j = 0; j < n; j++) {
                    if (a[i][j] == 0) {
                        k++;
                        if (k == h) {
                            xFood2=i;
                            yFood2=j;
                            a[i][j] = 4;
                            return;
                        }
                    }
                }
        }

	@Override
	public void keyPressed(KeyEvent e) {
		if (!loss) {
			// TODO Auto-generated method stub
			if (e.getKeyCode() == e.VK_UP && direction != 3) {
				direction = 1;
				timer.start();
			}
			if (e.getKeyCode() == e.VK_RIGHT && direction != 4) {
				direction = 2;
				timer.start();
			}
			if (e.getKeyCode() == e.VK_DOWN && direction != 1) {
				direction = 3;
				timer.start();
			}
			if (e.getKeyCode() == e.VK_LEFT && direction != 2) {
				direction = 4;
				timer.start();
			}
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand() == newGame_bt.getText()) {
			new snakeGame("SNAKE GAME - VERSION 1 - SE1704 - GROUP 4 - GAMEPLAY", lv.getSelectedIndex());
			this.dispose();
		}
		
	}
}