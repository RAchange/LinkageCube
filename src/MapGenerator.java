import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class MapGenerator {
	public int map[][];
	public int avail[][];
	public int dis[][][][];
	public boolean isDist[][];
	public int brickWidth;
	public int brickHeight;
	private Random r = new Random();
	final int nCube = 9;
	private Dist[] dists = new Dist[nCube];
	
	private Color[] colorArr = {Color.BLACK, Color.WHITE, Color.DARK_GRAY, Color.BLUE, 
								Color.CYAN, Color.LIGHT_GRAY, Color.GREEN, Color.MAGENTA,
								Color.ORANGE, Color.PINK, Color.RED, Color.YELLOW};
	public boolean up, left;
	
	public MapGenerator(int row, int col) {
		map = new int[row][col];
		avail = new int[row][col];
		isDist = new boolean[row][col];
		brickWidth = brickHeight = (540/col > 540/row)? 540/row: 540/col;
		dis = new int[row][col][row][col];
		
		do {
			for(int y=0; y<row; y++){
		        for(int x=0; x<col; x++){;
			        int i = r.nextInt(4);
					if(i==0) {
						map[x][y] = 2;
					}else {
						map[x][y] = 1;
					}
		        }
		    }
			for(int x=0; x<map.length; x++) {
				for(int y=0; y<map[0].length; y++) {
					if(map[x][y] != 2) {
						avail[x][y] = 4;
						if(x-1 < 0 || map[x-1][y]==2) {
							avail[x][y] -= 1;
						}
						if(y-1 < 0 || map[x][y-1]==2) {
							avail[x][y] -= 1;
						}
						if(x+1 >= map.length || map[x+1][y]==2) {
							avail[x][y] -= 1;
						}
						if(y+1 >= map[0].length || map[x][y+1]==2) {
							avail[x][y] -= 1;
						}
					}else {
						avail[x][y] = 0;
					}
				}
			}
			this.update();
		}while(!this.check());
	}
	
	public void draw(Graphics2D g) {
		for(int i=0; i<map.length; i++) {
			for(int j=0; j<map[0].length; j++) {
				if(map[i][j] > 0) {
					g.setColor(colorArr[map[i][j]]);
					g.fillRect(brickWidth*j+80, brickHeight*i+50, brickWidth, brickHeight);
				
					g.setStroke(new BasicStroke(3));
					g.setColor(Color.black);
					g.drawRect(brickWidth*j+80, brickHeight*i+50, brickWidth, brickHeight);
					
				}
			}
		}
		
		for(int i=0; i<nCube; i++) {
			int x = dists[i].dx, y = dists[i].dy;
			g.setColor(colorArr[i+3]);
			g.drawRect(brickWidth*y+83, brickHeight*x+53, brickWidth-6, brickHeight-6);
		}
	}
	
	public void setBrickValue(int value, int row, int col) {
		map[row][col] = value;
	}
	
	public void setDistance(int index, int row, int col) {
		dists[index] = new Dist(index, row, col);
		isDist[row][col] = true;
	}
	
	public void swapColors(int i, int j) {
		Color tmp = colorArr[i];
		colorArr[i] = colorArr[j];
		colorArr[j] = tmp;
		Dist dTmp = dists[i-3];
		dists[i-3] = dists[j-3];
		dists[j-3] = dTmp;
	}
	
	public boolean check() {
		
		int x=0, y=0;
		boolean found=false;
		for(; x<map.length && !found; x++) {
			for(; y<map[0].length && !found; y++) {
				if(map[x][y]==1) {
					found=true;
				}
			}
		}
		
		for(int i=0; i<map.length; i++) {
			for(int j=0; j<map[0].length; j++) {
				for(int k=0; k<map.length; k++) {
					for(int l=0; l<map[0].length; l++) {
						for(int m=0; m<map.length; m++) {
							for(int n=0; n<map[0].length; n++) {
								int prob = (dis[k][l][i][j] == Integer.MAX_VALUE || 
										dis[i][j][m][n] == Integer.MAX_VALUE)?
												Integer.MAX_VALUE:dis[k][l][i][j]+dis[i][j][m][n];
								dis[k][l][m][n] = (dis[k][l][m][n]>prob)?prob:dis[k][l][m][n];
							}
						}
					}
				}
			}
		}
		
		for(int i=0; i<map.length; i++) {
			for(int j=0; j<map[0].length; j++) {
				if(dis[x][y][i][j] == Integer.MAX_VALUE && map[i][j]==1) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public void update() {
		up = r.nextBoolean(); left = r.nextBoolean();
		for(int i=0; i<map.length; i++) {
			for(int j=0; j<map[0].length; j++) {
				for(int k=0; k<map.length; k++) {
					for(int l=0; l<map[0].length; l++) {
						dis[i][j][k][l] = Integer.MAX_VALUE;
					}
				}
				dis[i][j][i][j] = 0;
			}
		}
		for(int x=0; x<map.length; x++) {
			for(int y=0; y<map[0].length; y++) {
				if(x-1 >= 0) {
					dis[x][y][x-1][y] = dis[x-1][y][x][y] = 1;
				}
				if(y-1 >= 0) {
					dis[x][y][x][y-1] = dis[x][y-1][x][y] = 1;
				}
				if(x+1 < map.length) {
					dis[x][y][x+1][y] = dis[x+1][y][x][y] = 1;
				}
				if(y+1 < map[0].length) {
					dis[x][y][x][y+1] = dis[x][y+1][x][y] = 1;
				}
			}
		}
		
		for(int x=0; x<map.length; x++) {
			for(int y=0; y<map[0].length; y++) {
				if(map[x][y] == 2) {
					if(x-1 >= 0) {
						dis[x][y][x-1][y] = dis[x-1][y][x][y] = Integer.MAX_VALUE;
					}
					if(y-1 >= 0) {
						dis[x][y][x][y-1] = dis[x][y-1][x][y] = Integer.MAX_VALUE;
					}
					if(x+1 < map.length) {
						dis[x][y][x+1][y] = dis[x+1][y][x][y] = Integer.MAX_VALUE;
					}
					if(y+1 < map[0].length) {
						dis[x][y][x][y+1] = dis[x][y+1][x][y] = Integer.MAX_VALUE;
					}
				}
			}
		}
		
		for(int i=0; i<map.length; i++) {
			for(int j=0; j<map[0].length; j++) {
				isDist[i][j] = false;
			}
		}
	}
}
