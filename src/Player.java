
public class Player {
	
	private String Name;		// Nome do jogador
	private char Symbol;		// Simbolo utilizado pelo jogador no tabuleiro
	private GameInfo GameInfo;	// Informação do jogo associada ao jogador (Vitorias, empates e derrotas)
	private boolean Human;		// Variável boleana que define se o jogador é humano ou computador (AI)
	
	// Método constructor da classe Player
	public Player(String name, char symbol, boolean isHuman) {
		this.Name = name;
		this.Symbol = symbol;
		this.Human = isHuman;
		this.GameInfo = new GameInfo();
	}
	
	// Método para obter o nome do jogador
	public String getName() {
		return this.Name;
	}
	
	// Método para redefinir o nome do jogador
	public void setName(String name) {
		this.Name=name;
	}
	
	// Método para obter o symbolo do jogador
	public char getSymbol() {
		return this.Symbol;
	}
	
	// Método para obter o objecto GameInfo associado ao jogador
	public GameInfo GameInfo() {
		return this.GameInfo;
	}
	
	// Método para obter o tipo de jogador, humano ou computador
	public boolean isHuman() {
		return this.Human;
	}
}
