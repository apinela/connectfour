import java.io.*;

public class Top {
	
	// Número máximo de pontuações existente no top
	private static final int MAX_TOP_SCORES=10;
	
	// Definição dos campos que aparecem no top
	private static final int NAME=0;
	private static final int VICTORIES=1;
	private static final int DRAWS=2;
	private static final int LOSSES=3;
	private static final int FIELDS=4; // Numero de campos no total
	
	// Outros parâmetros
	private static final char FIELD_SEPARATOR=';';	// Separador de campos
	private int nTop=0;								// Número de pontuações existentes
	private File TopFile;							// Nome do ficheiro de pontuações
	
	private String[][] Top = new String[MAX_TOP_SCORES][FIELDS];
	
	// Método constructor do top de pontuações
	public Top(String fileName) {
		TopFile = new File(fileName);
		if (!topFileExists()) CreateNew();
		Top=getTop();
	}
	
	// Método que verifica se o ficheiro do top já existe
	private boolean topFileExists() {
		return TopFile.exists();
	}
	
	// Método para criar um novo ficheiro vazio de top
	private boolean CreateNew() {
		try {
			TopFile.createNewFile();
		} catch (Exception e) {return false;}
		return true;
	}
	
	// Método para apagar o ficheiro de top
	public boolean deleteTop() {
		try {
			TopFile.delete();
		} catch(Exception e) { return false; }
		return true;
	}
	
	// Método que adiciona ao top uma nova pontuação caso esta seja maior que a última 
	public boolean addTop(Player player, boolean test) {
		int p=-1;
		
		if (player.GameInfo().getVictories()<=getLastLineGameInfo().getVictories() || nTop==0) {
			if (nTop>=MAX_TOP_SCORES) {
				return false;
			} else {
				if (!test) {
					Top[nTop][NAME]=player.getName();
					Top[nTop][VICTORIES]=Integer.toString(player.GameInfo().getVictories());
					Top[nTop][DRAWS]=Integer.toString(player.GameInfo().getDraws());
					Top[nTop][LOSSES]=Integer.toString(player.GameInfo().getLosses());
					nTop++;
				}
				return true;
			}
		}
		
		for (int i=0;i<nTop;i++) {
			if (player.GameInfo().getVictories()>getLineGameInfo(i).getVictories()) {
				p=i;
				break;
			}
		}
		
		if (p==-1) { 
			return false; //Igual ao ultimo, fica o mais antigo
		} else {
			if (!test) {
				for (int i=MAX_TOP_SCORES-1;i>p;i--) {
					String[] line=getLine(i-1);
					Top[i][NAME]=line[NAME];
					Top[i][VICTORIES]=line[VICTORIES];
					Top[i][DRAWS]=line[DRAWS];
					Top[i][LOSSES]=line[LOSSES];
				}
				Top[p][NAME]=player.getName();;
				Top[p][VICTORIES]=Integer.toString(player.GameInfo().getVictories());
				Top[p][DRAWS]=Integer.toString(player.GameInfo().getDraws());
				Top[p][LOSSES]=Integer.toString(player.GameInfo().getLosses());
				nTop++;
			}
			return true;
		}		
		
	}
	


	// Método para obter o array do top com os campos definidos
	public String[][] getTop() {
		
		String[][] list=new String[MAX_TOP_SCORES][FIELDS];
		try {
		BufferedReader in = new BufferedReader(new FileReader(TopFile));
		
		for (int i=0;i<=MAX_TOP_SCORES;i++)
			if (in.ready()){ 
				String str=in.readLine();
				nTop++;
				int s=0,f=str.indexOf(FIELD_SEPARATOR);
			for (int j=0;j<FIELDS;j++) {
				list[i][j]=str.substring(s, f);
				s=++f;
				f=str.indexOf(FIELD_SEPARATOR, s);
				if (f==-1) f=str.length();
			}
			}
			else return list;
	} catch (Exception e) {}
	
		return list;
	}
	
	// Método que coloca os campos da linha 'l' num array de Strings
	public String[] getLine(int l) { 
		String[] s= new String[FIELDS];
			for (int i=0;i<FIELDS;i++)
				if (Top[l][i]!=null) s[i]=Top[l][i];
				else s[i]="";
		 return s;
		}
	
	// Método que obtem a GameInfo da última linha
	private GameInfo getLastLineGameInfo() {
		return getLineGameInfo(nTop-1);	
	}
	
	// Método que obtem a GameInfo da linha l
	private GameInfo getLineGameInfo(int line) {
		int v = (nTop==0)?0:Integer.parseInt(Top[line][VICTORIES]);
		int d = (nTop==0)?0:Integer.parseInt(Top[line][DRAWS]);
		int l = (nTop==0)?0:Integer.parseInt(Top[line][LOSSES]);
		GameInfo gi = new GameInfo(v, d, l);
		return gi;	
	}
	
	// Método que obtem a GameInfo da linha l
	private String getLineName(int line) {
		return getLine(line)[NAME];	
	}
	
	// Método para gravar no ficheiro do top as pontuações do array top
	public boolean saveTop() {
		try {
			TopFile.delete();
			TopFile.createNewFile();
			PrintWriter out = new PrintWriter(new FileWriter(TopFile));
			for (int i=0;i<nTop;i++)
				for (int j=0;j<FIELDS;j++)
					if (j!=FIELDS-1) out.print(Top[i][j]+FIELD_SEPARATOR);
					else {
						out.print(Top[i][j]);
						out.println();	
					}
			out.close();
		} catch (Exception e) {return false;}
		return true;
	}
	
	// Método que coloca numa string a lista das pontuações existentes no top 
	public String toString() {
		String result="\n\nTOP "+MAX_TOP_SCORES+"\n\n"+"Nome\t\tV,E,D,J\n";
		GameInfo gi;
		for (int i=0;i<nTop;i++) {
			gi=getLineGameInfo(i);
			// Nome do jogador
			result+=(getLineName(i).length()>7)?getLineName(i)+"\t":getLineName(i)+"\t\t";
			// Resultados
			result+=gi.getVictories()+","+gi.getDraws()+","+gi.getLosses()+","+gi.getGamesPlayed()+"\n";
		}
		return result;
	}
}
