
public class FourInRow {
	private int inLine;		// Número de peças verificadas em linha
	private int maxLines;		// Número de linhas do tabuleiro
	private int maxColumns;	// Número de colunas do tabuleiro
	private char Grid[][];		// Tabuleiro de jogo
	private int nPlays; 		// Número de jogadas já efectuadas
	private int[] nLines; 		// Número de linhas preenchidas em cada coluna
	private int[] moves; 	// Jogadas Efectuadas
	
	// Método construtor do jogo 4 em linha
	public FourInRow(int inLine, int maxLines, int maxColumns) {
		
		this.inLine=inLine;
		this.maxLines=maxLines;
		this.maxColumns=maxColumns;
		
		nPlays=0;
		nLines = new int[maxColumns];
		moves = new int[maxColumns*maxLines];
		
		for (int i=0;i<maxColumns;i++) nLines[i]=0;
		
		Grid = new char[maxLines][maxColumns];
		initializeGrid();
	}
	
	// Método para inicializar tabuleiro de jogo com espaços em cada coordenada
	public void initializeGrid() {
		for (int i=0;i<maxLines;i++)
			for (int j=0;j<maxColumns;j++)
				Grid[i][j]=' ';
		
		for (int i=0;i<maxColumns*maxLines;i++) moves[i]=-1;
	}
	
	public void playAI(char simbolo,int dificuldade,char simboloadv) {
		int maior=0;
		int xmaior=0;
		int a=0;
		
		if (nPlays<2) {
			int j= (int)(Math.random()*maxColumns);
			if (tryInsert(j,simbolo)) return;
		}
		/* Procurar melhor possibilidade de jogar, caso exista a 
		 * possibilidade de ganhar usar essa posição 
		 */
		for (int j=0;j<maxColumns;j++) { //colunas
			int i=maxLines-nLines[j]-1;
			if (i==maxLines) continue;
				if (this.Grid[i][j]==' ') {
					for (int z=0;z<4;z++) { //Direccao
						a=getPriority(j,i,simbolo,z,inLine,false)+getPriority(j,i,simbolo,z+4,inLine,false);
						if (a==inLine-1)  {
							if (tryInsert(j,simbolo)) return;
						}
					if (a>maior) {
						maior=a;
						xmaior=j;
					}
				}
			}
		}
		
		// Se não houver a possibilidade de ganhar, verificar alternativas
		if (dificuldade>1) {
		for (int j=0;j<maxColumns;j++) { //colunas
			int i=maxLines-nLines[j]-1;
			if (i==maxLines) continue; 
				if (this.Grid[i][j]==' ') {
					if (dificuldade>=2) {
						for (int z=0;z<4;z++) {  //Direccao
							a=getPriority(j,i,simboloadv,z,inLine,false)+getPriority(j,i,simboloadv,z+4,inLine,false);
							if (a==inLine-1) {
								if (tryInsert(j,simbolo)) return;
							}
						}	
					} 
					if (dificuldade>=3) {
						for (int z=0;z<4;z++) {  //Direccao
							a=getPriority(j,i,simboloadv,z,inLine,false)+getPriority(j,i,simboloadv,z+4,inLine,false);
							if (a>=inLine-(inLine/2)) {
								if (a>maior) {	
									maior=a;
									xmaior=j;
								}
							}
						}	
					}
				}
			}
		}
		if (!tryInsert(xmaior,simbolo)) {
			do {
				if (this.nLines[xmaior]==maxLines) xmaior=(int) ((double) (Math.random()*maxColumns));
			} while (!tryInsert(xmaior,simbolo));
		}
	}
	
	int getPriority(int x,int y, char simbolo,int dir,int in_line,boolean rns) {
		/* Método que devolve um numero relativo à "vizinhança", ou seja, quanto maior o numero
		 * maior as probabilidades para construir o quatro em linha
		 * 
		 * Com a flag RNS activa o método apenas conta os simbolos seguidos o que permite assim usar 
		 * este método para procurar jogadas finais (IN_LINE em linha)
		 */
		int n=0;
		--in_line;

		switch (dir) {
		case 0: y--;break;
		case 1: x++;y--;break;
		case 2: x++;break;
		case 3: x++;y++;break;
		case 4: y++;break;
		case 5: x--;y++;break;
		case 6: x--;break;
		case 7: x--;y--;break;
		}
		
		if (in_line<=0) return 0;
		
		if (x<0 || x>=maxColumns ||  y<0 || y>=maxLines) return 0;
		
		if (Grid[y][x]==' ') return 0;

		if (Grid[y][x]!=simbolo && rns) return 0;
		
		if (Grid[y][x]!= simbolo && Grid[y][x]!=' ') return 0;
						
		if (Grid[y][x]==simbolo) n=1;

		return n+getPriority(x,y,simbolo,dir,in_line,rns);
}

	// Método para inserir jogada
	public boolean tryInsert(int x,char s) {

			if (x>=0 && x<maxColumns) {
				
				int y=maxLines-nLines[x]-1;
				
				if (y<0) return false;
				//Definir posição
				Grid[y][x] = s;
				//Guardar ultima coluna
				moves[this.nPlays]=x;
				//Aumentar numero de jogadas
				nPlays++;
				//Aumentar o numero de linhas preenchidas na coluna
				nLines[x]++;
				return true;
			}
		return false;
	}

	
	// Método para verificar existem IN_LINE em linha
	public boolean CheckInLine() {
		int x=getLastX();
		int y=getLastY();
		char symbol=this.Grid[y][x];
		int s;
		
		for (int i=0;i<4;i++) {
			
			s=getPriority(x,y,symbol,i,inLine,true)+getPriority(x,y,symbol,(i+4),inLine,true);
			if (s>=inLine-1) return true;
		}
		return false;
	}
	
	// Método que devolve uma String com o tabuleiro de jogo
	public String toString() {
		String out="";
		
		//Numero das colunas
		for (int i=1;i<=maxLines;i++) {
			if (i<10) out+="  "+(i)+" ";
			else out+=" "+(i)+" ";
		}
		out+="\n";
		
		for (int i=0;i<maxLines;i++) {
			//Separadores verticais e conteudo do tabuleiro
			for (int j=0;j<maxColumns;j++) {
				out+="| "+ this.Grid[i][j] + " ";
				if (j==(maxColumns-1)) out+="|";
			}
			out+="\n";
			//Separadores horizontais
			for (int k=0;k<maxColumns;k++) {
				if (i==(maxLines-1)) {
					if (k==(maxColumns-1)) out+="'---'\n";
					else out+="'---";
				} else {
					if (k==0) out+="|---";
					else if (k==(maxColumns-1)) out+="+---|\n";
					else out+="+---";
				}
				
			}
		}
		for (int i=1;i<=maxLines;i++) {
			if (i<10) out+="  "+(i)+" ";
			else out+=" "+(i)+" ";
		}
		out+="\n";
		
		return out;
	}
	
	// Método para verificar se há empate caso o taboleiro esteja cheio
	public boolean checkDraw() {
		if (nPlays==(maxLines*maxColumns)) return true;
		
		return false;
	}
	
	//Devolver o numero da linha onde foi feita a ultima jogada
	private int getLastY() { return maxLines-nLines[getLastX()];}
	private int getLastX() { return moves[nPlays-1];}
	//Devolver o numero de jogadas efectuadas
	public int getnPlays() { return nPlays; }
	//Devolver o numero de colunas do tabuleiro
	public int getColumns() { return maxColumns; }
	//Devolver o numero de linhas do tabuleiro
	public int getLines() { return maxLines; }
	//Devolver numero maximo de jogadas possíveis
	public int getMaxPlays() { return maxColumns*maxLines; }
	//Devolver tabuleiro
	public char[][] getGrid() { return Grid; }
	
	
	// Método para desfazer a última(s) jogada, se esta não for a primeira
	public boolean tryUndo() {
		if (nPlays>0) {
			int y=getLastY();
			int x=getLastX();
			Grid[y][x]=' ';
			nLines[x]--;
			nPlays--;
			return true;
		}
		return false;
	}
}
