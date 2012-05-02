/*
 * Jogo 4 em linha
 * 
 * - Jogo criado no âmbito do 3º trabalho da cadeira de Programação - LEETC - ISEL
 * - O jogo foi desenvolvido com duas vertentes:
 * 		-> A didática, uma forma de alargar conhecimentos;
 * 		-> A outra contextualizada num nível pessoal de cada um dos alunos
 * 		integrando o mesmo num portfolio pessoal; daí ter sido criado com os nomes de
 * 		classes/métodos em inglês de forma a, num futuro, inserir o jogo na "cloud" (internet)
 * 		sendo esta a forma fácil de internacionalização do mesmo.  
 * 
 * Autores:
 * 		André Pinela	- 38603
 * 		Filipe Azevedo	- 38057
 * 
 * 
 */

import java.util.Scanner;

public class Connect4 {

	// Número de peças reconhecidas em linha
	private static final int IN_LINE=5;
	// Número de linhas do tabuleiro
	private static final int MAX_LINES=12;
	// Número de colunas do tabuleiro
	private static final int MAX_COLUMNS=12;
	
	
	public static final char[] SYMBOLS = {'+','-','@','#','$','€','%','&','!','?','=','^','~'};
	public static int currentPlayer=0; // Jogador Actual
	public static int firstPlayer=-1;
	public static final String COMMANDS="PNUFTH";
	public static final String topFile="./Top.txt";
	private static Top t = new Top(topFile);
	private static Scanner in = new Scanner(System.in);
	private static Player[] players;
	private static boolean newGame;
	private static boolean terminateGame;
	private static boolean exitGame;
	private static FourInRow Game;
	private static int dificulty=0;

	// Método que inicializa o jogo
	private static void initializeGame() {
		newGame=false;
		terminateGame=false;
		exitGame=false;
		
		if (firstPlayer==-1) {
			if (Math.random()*10>=5) firstPlayer=1;
		else firstPlayer=0;	
		}
		else firstPlayer=++firstPlayer%2;
		
		currentPlayer=firstPlayer;

		Game = new FourInRow(IN_LINE,MAX_LINES,MAX_COLUMNS);
	}

	// Método que inicializa os jogadores
	private static void initializeplayers(boolean fastgame, boolean versusCpu) {

		// Nomes por omissão e simbolos aleatórios
		String n1="A";
		String n2="B";
		if (versusCpu) n2="Computador";
		char c1,c2;
		c1=SYMBOLS[(int)(Math.random()*(SYMBOLS.length-1))];

		// Escolher simbolo aleatório diferente do primeiro
		do {
			c2=SYMBOLS[(int)(Math.random()*(SYMBOLS.length-1))];
		} while (c1==c2);	
		if (!fastgame) {
			//Definir nomes e simbolos
			System.out.println("Introduza o nome do jogador 1: ");
			if (in.hasNextLine()) in.nextLine();
			n1 = in.nextLine();
			if(!versusCpu) {
				System.out.println("Introduza o nome do jogador 2: ");
				n2=in.nextLine();
			}
			
			do {
				System.out.println(n1 + ", escolha um simbolo: ");
				c1=in.nextLine().charAt(0);
				if(!versusCpu) {
					System.out.println(n2 + ", escolha um simbolo: ");
					c2=in.nextLine().charAt(0);
				}
			} while (c1==c2);
		}
		
		players = new Player[2];
		// Criar os dois jogadores
		players[0] = new Player(n1,c1,true);
		players[1] = new Player(n2,c2,!versusCpu);
	}
	
	// Alterna de jogador ou decide o primeiro jogador a 
	public static void nextToPlay() { currentPlayer=++currentPlayer%2;
	}

	// Método para validar a jogada
	private static int validatePlay() {
		String x;
		int a;
		
		//Repetir até que a jogada seja válida
		do {
			a=-1;
			//Imprimir tabuleiro e pedir coluna para fazer a jogada do jogador actual
			System.out.println(Game+"\n"+players[currentPlayer].getName()+"["+players[currentPlayer].getSymbol()+"]"+", Coluna: ");

			//Receber String não vazia
			do {x=in.nextLine().toUpperCase();} while (x.isEmpty()); 
			
			//Verificar se é um comando, se for executa e volta ao inicio do ciclo
			if (COMMANDS.contains(x)) {
				switch (x.charAt(0)) {
				case 'P': {
					showPont();
					waitPressKey();
					break;
				}
				case 'N': {
					players[currentPlayer].GameInfo().incrementLosses();
					exitGame=false;
					newGame=true;
					break;
				}
				case 'U': {
					if (Game.tryUndo())
						nextToPlay();
						if (!players[currentPlayer].isHuman()) Game.tryUndo();
						nextToPlay();
						break;
				}
				case 'F': {
					showPont();
					waitPressKey();
					exitGame=true;
					newGame=false;
					break;
				}
				case 'H': {
					showHelp();
					waitPressKey();
					break;
				}
				case 'T': {
					System.out.println(t.toString());
					waitPressKey();
				}
				}
				
				
			}
			else a=(int)x.charAt(0)-48;
			a--; // Offset de -1 para colocar na escala do array do tabuleiro
			
		}while ((!Game.tryInsert(a,players[currentPlayer].getSymbol())) && !newGame && !exitGame);
		return a;
	}
	
	// Método que verifica se há vencedor após a última jogada
	private static void CheckWin() {
		//Verificar se o jogo terminou com a ultima jogada
		if (Game.CheckInLine()) {
			//Incrementar pontuação do jogador actual
			players[currentPlayer].GameInfo().incrementVictories();
			if (currentPlayer==1) {
				players[0].GameInfo().incrementLosses();
			} else {
				players[1].GameInfo().incrementLosses();
			}
			//Declarar vencedor
			System.out.println(Game+"\nO jogo terminou, o jogador "+players[currentPlayer].getName()+" venceu!\n");
			
			//Mostrar pontuações
			showPont();
			//Terminar jogo
			terminateGame=true;
		}
	}
	
	// Método que, no caso de empate, incrementa os empates nos GameInfo's de cada jogador
	// e mostra a pontuação
	private static void Draw() {
		players[0].GameInfo().incrementDraws();
		players[1].GameInfo().incrementDraws();
		showPont();
		terminateGame=true;
	}

	public static void play(boolean fastGame, boolean versusCpu) {
			
			String f;
			
			initializeplayers(fastGame,versusCpu);
						
			//Criar jogo
			while (true) {
				
			initializeGame();
				
			//Jogadas
			do {

				if (players[currentPlayer].isHuman()) validatePlay();
				else {
					Game.playAI(players[currentPlayer].getSymbol(),dificulty,(currentPlayer==0)?players[1].getSymbol():players[0].getSymbol());
				}
				
				if (!newGame && !exitGame) CheckWin();
				
				//Empate
				else if (Game.checkDraw() ) Draw();
				//Passar ao próximo jogador
				if (!terminateGame && !exitGame) nextToPlay();
				} while (!newGame && !exitGame && !terminateGame);
			
			if (exitGame) {
				addTop(fastGame,versusCpu);
				break;
			}
			if (!newGame) {
			System.out.println("Quer continuar a jogar?");
			f=in.nextLine().toUpperCase();
			
			//Verificar resposta
			if (f.contains("N")) {
				addTop(fastGame,versusCpu);
				break;
			}
			nextToPlay();
			}
			
			}
			
		}
		
		public static void waitPressKey() {
			System.out.println("\n\nPressione ENTER para continuar...");
			in.nextLine();
			in.nextLine();
		}
		
		public static void addTop(boolean fastGame, boolean versusCpu) {
			
			if (players[0].GameInfo().getVictories()>players[1].GameInfo().getVictories() || versusCpu) currentPlayer=0;
			else currentPlayer=1;
			
			if (t.addTop(players[currentPlayer],true)) {
				System.out.println("Parabéns "+players[currentPlayer].getName() +", entrou para o top.");
				if (fastGame) {
					System.out.print("Introduza o seu nome: ");
					players[currentPlayer].setName(in.nextLine());
				}
				t.addTop(players[currentPlayer],false);
				if (!t.saveTop()) System.out.println("Erro ao gravar pontuacoes no ficheiro:" + topFile);
			}
			
		}
		
		// Mostrar menu inicial
		public static int menu() {		
			
			System.out.println("Menu Principal:\n");
			// Definir nomes de jogadores e simbolos
			System.out.println("\t\t1 - Multiplayer(Normal)");
			// User nomes A e B e simbolos aleatórios
			System.out.println("\t\t2 - Multiplayer(Rápido)");
			System.out.println("\t\t3 - Single Player(Normal)");
			System.out.println("\t\t4 - Single Player(Rápido)");
			System.out.println("\t\t5 - Top");
			
			// Sair do jogo
			System.out.println("\t\t0 - Sair");
			System.out.println("\n\nOpção: ");
			return in.nextInt();
			
		}
		
		public static void chooseLevel() {
			int d=0;
			
			if (in.hasNextLine()) in.nextLine();
			do {
				try {
					System.out.println("\n\nDificuldade[1-3]: ");
					d= in.nextInt();
				} catch (Exception e) {}
			} while (d<1 || d>3);
			
			dificulty=d;			
		}
		
		public static void chooseLevel(int level) {
			dificulty=level;			
		}
		
		// Método para mostrar a pontuação
		public static void showPont() {
			
			System.out.println("\n\nPontuação actual:\n");
			System.out.println("Nome\t\tV,E,D");
			for (int i=0;i<players.length;i++)
			System.out.println(
					((players[i].getName().length()>5)?players[i].getName()+"\t":players[i].getName()+"\t\t")
					+players[i].GameInfo().getVictories()+","
					+players[i].GameInfo().getDraws()+","
					+players[i].GameInfo().getLosses());
		}
		
		// Método para mostrar a ajuda
		public static void showHelp() {
				System.out.println("Comandos:\n");
				System.out.println("\tP - Mostra quantidade de jogos e pontuações dos jogadores");
				System.out.println("\tN - Novo jogo (considera-se uma derrota para o jogador actual");
				System.out.println("\tU - Permite desfazer a ultima jogada");
				System.out.println("\tF - Termina o jogo e vai para o menu principal");
				System.out.println("\tT - Apresenta Top");
				System.out.println("\tH - Apresenta este menu");
		}

		public static void main(String[] args) {
			
			boolean fromCommandline=false;
			
			// Manter jogo a correr até que seja escolhida a opção 0
			while(true) {
				try {
				switch ((args.length==0)?menu():commandLineMode(args)) {
					case 1: { play(false,false);break; }
					case 2: { play(true,false);break; }
					case 3: { chooseLevel();play(false,true);break; }
					case 4: { chooseLevel();play(true,true);break; }
					case 5: {
						System.out.println(t.toString());
						waitPressKey();
						break;
					}
					case 98: { chooseLevel(2);play(true,true);fromCommandline=true;break; }
					case 99: { chooseLevel(Integer.parseInt(args[1]));play(true,true);fromCommandline=true;break; }
					case 0: {
						
						if (!in.nextLine().toUpperCase().contains("N"))
						System.exit(0);
					}
				}
				if (fromCommandline) break;
			} catch (Exception e) { in.nextLine();}
			} 
			 
		}

		private static int commandLineMode(String[] args) {
			try {
				if (args.length==1 && args[0].equals("auto")) return 98;
				else if (args.length==2 &&
						 Integer.parseInt(args[1])>0 &&
						 Integer.parseInt(args[1])<4) return 99;
				else return showCmdLineHelp();
			} catch ( Exception e ) {return showCmdLineHelp();}
		}

		private static int showCmdLineHelp() {
			System.out.println("Sintaxe:\n");
			System.out.println("\tConnect4 auto [<level:1,2,3>]\n");
			System.out.println("\tExemplos:");
			System.out.println("\tConnect4 auto (nível predefinido=2)");
			System.out.println("\tConnect4 auto 2\n");
			return 0;
		}
		
}
