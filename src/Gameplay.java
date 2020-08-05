import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.Timer;

import javax.swing.JPanel;

public class Gameplay extends JPanel implements KeyListener, ActionListener{
	private boolean play = false;
	private int score = 0;
	
	private int totalBricks = 21;
	
	private Timer timer;
	private int delay = 8;
	private int hours = 0, minutes = 0, seconds = 0, mileseconds = 0;
	
	
	private MapGenerator map;
	final int nCube = 5;
	Random r = new Random();
	
	private Cube[] cubes = new Cube[9];
	private Dist[] dists = new Dist[9];
	
	public Gameplay() {
		map = new MapGenerator(20, 20);
		
		this.addKeyListener(this);
		this.setFocusable(true);
		this.setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
		
		this.genQuestion();
	}
	
	public void paint(Graphics g) {
		// background
		g.setColor(Color.black);
		g.fillRect(1, 1, 700, 700);
		
		// border
		g.setColor(Color.yellow);
		g.fillRect(0, 0, 3, 700);
		g.fillRect(0, 0, 700, 3);
		g.fillRect(697, 0, 3, 700);
		g.fillRect(0, 667, 700, 3);
		
		// score
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString("Returned: "+score, 450, 30);
		
		// timer
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString("Time: "+hours+":"+minutes+":"+seconds+":"+mileseconds, 100, 30);
		
		map.draw((Graphics2D)g);
		
		if(score == nCube) {
			g.setColor(Color.GREEN);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("All returned", 190, 300);
			
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter to Restart", 230, 350);
			play = false;
		}
		
		g.dispose();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		timer.start();
		if(play) {
			score = 0;
			mileseconds += delay;
			seconds += mileseconds/1000;
			mileseconds %= 1000;
			minutes += seconds/60;
			seconds %= 60;
			hours += minutes/60;
			minutes %= 60;
			
			for(int i=0; i<nCube; i++) {
				for(int j=0; j<nCube; j++) {
					if(cubes[i].id == dists[j].id &&
						cubes[i].curX == dists[j].dx&&
						cubes[i].curY == dists[j].dy) {
						score+=1;
					}
				}
			}
		}
		
		this.repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub 
		if(e.getKeyCode() == KeyEvent.VK_UP){
			play = true;
        	for(int i=0,cx,cy; i<nCube; i++) {
        		cx = cubes[i].curX; cy = cubes[i].curY;
        		if(cx-1>=0) {
    	    		if(map.map[cx-1][cy]==1) {
    	    			map.map[cx][cy] = 1;
        	    		map.map[cx-1][cy] = i+3;
        	    		cubes[i].curX-=1;
    	    		}else if(map.map[cx-1][cy]!=2) {
    	    			Cube t = cubes[i];
    	    			cubes[i] = cubes[map.map[cx-1][cy]-3];
    	    			cubes[map.map[cx-1][cy]-3] = t;
    	    			map.swapColors(map.map[cx][cy], map.map[cx-1][cy]);
    	    			int tmp = map.map[cx][cy];
    	    			map.map[cx][cy] = map.map[cx-1][cy];
    	    			map.map[cx-1][cy] = tmp;
    	    			i--;
    	    		}
    	    	}
    	    }
        }else if(e.getKeyCode() == KeyEvent.VK_DOWN){
        	play = true;
        	for(int i=0,cx,cy; i<nCube; i++) {
        		cx = cubes[i].curX; cy = cubes[i].curY;
    	    	if(cx+1<map.map.length) {
    	    		if(map.map[cx+1][cy]==1) {
    	    			map.map[cx][cy] = 1;
        	    		map.map[cx+1][cy] = i+3;
        	    		cubes[i].curX+=1;
    	    		}else if(map.map[cx+1][cy]!=2) {
    	    			Cube t = cubes[i];
    	    			cubes[i] = cubes[map.map[cx+1][cy]-3];
    	    			cubes[map.map[cx+1][cy]-3] = t;
    	    			map.swapColors(map.map[cx][cy], map.map[cx+1][cy]);
    	    			int tmp = map.map[cx][cy];
    	    			map.map[cx][cy] = map.map[cx+1][cy];
    	    			map.map[cx+1][cy] = tmp;
    	    			i--;
    	    		}
    	    	}
    	    }
        }else if(e.getKeyCode() == KeyEvent.VK_LEFT){
        	play = true;
        	for(int i=0,cx,cy; i<nCube; i++) {
        		cx = cubes[i].curX; cy = cubes[i].curY;
    	    	if(cy-1>=0) {
    	    		if(map.map[cx][cy-1]==1) {
        	    		map.map[cx][cy] = 1;
        	    		map.map[cx][cy-1] = i+3;
        	    		cubes[i].curY-=1;
    	    		}else if(map.map[cx][cy-1]!=2) {
    	    			Cube t = cubes[i];
    	    			cubes[i] = cubes[map.map[cx][cy-1]-3];
    	    			cubes[map.map[cx][cy-1]-3] = t;
    	    			map.swapColors(map.map[cx][cy], map.map[cx][cy-1]);
    	    			int tmp = map.map[cx][cy];
    	    			map.map[cx][cy] = map.map[cx][cy-1];
    	    			map.map[cx][cy-1] = tmp;
    	    			i--;
    	    		}
    	    	}
    	    }
        }else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
        	play = true;
        	for(int i=0,cx,cy; i<nCube; i++) {
        		cx = cubes[i].curX; cy = cubes[i].curY;
    	    	if(cy+1<map.map[0].length) {
    	    		if(map.map[cx][cy+1]==1) {
        	    		map.map[cx][cy] = 1;
        	    		map.map[cx][cy+1] = i+3;
        	    		cubes[i].curY+=1;
    	    		}else if(map.map[cx][cy+1]!=2) {
    	    			Cube t = cubes[i];
    	    			cubes[i] = cubes[map.map[cx][cy+1]-3];
    	    			cubes[map.map[cx][cy+1]-3] = t;
    	    			map.swapColors(map.map[cx][cy], map.map[cx][cy+1]);
    	    			int tmp = map.map[cx][cy];
    	    			map.map[cx][cy] = map.map[cx][cy+1];
    	    			map.map[cx][cy+1] = tmp;
    	    			i--;
    	    		}
    	    	}
    	    }
        }else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
        	if(!play) {
        		score = hours = minutes = seconds = mileseconds = 0;
        		map = new MapGenerator(20,20);
        		this.genQuestion();
            	this.repaint();
        	}
        	
        }
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void genQuestion() {
		for(int i=0, nxtX, nxtY; i<nCube; i++) {
	    	do {
	    		nxtX = r.nextInt(map.map.length); 
	    		nxtY = r.nextInt(map.map[0].length);
	    	}while(map.map[nxtX][nxtY]!=1);
	    	cubes[i] = new Cube(i, nxtX, nxtY);
	    	map.setBrickValue(i+3, nxtX, nxtY);
	    }
		
		for(int i=0, nxtX, nxtY, rate; i<nCube; i++) {
	    	do {
	    		nxtX = r.nextInt(map.map.length); 
	    		nxtY = r.nextInt(map.map[0].length);
	    		rate = r.nextInt(20);
	    	}while(map.isDist[nxtX][nxtY] || (!((( map.avail[nxtX][nxtY]==1 && 
	    			((map.up && nxtX-1>=0 && map.map[nxtX-1][nxtY]!=2) || 
	    			(!map.up && nxtX+1<map.map.length && map.map[nxtX+1][nxtY]!=2)||
	    			(map.left && nxtY-1>=0 && map.map[nxtX][nxtY-1]!=2) ||
	    			(!map.left && nxtY+1<map.map[0].length && map.map[nxtX][nxtY+1]!=2)))))));
	    	dists[i] = new Dist(i, nxtX, nxtY);
			map.setDistance(i, nxtX, nxtY);
	    }
	}
}
