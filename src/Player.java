
public class Player {
	
	private String Name;		// Nome do jogador
	private char Symbol;		// Simbolo utilizado pelo jogador no tabuleiro
	private GameInfo GameInfo;	// Informa√ß√£o do jogo associada ao jogador (Vitorias, empates e derrotas)
	private boolean Human;		// Vari√°vel boleana que define se o jogador √© humano ou computador (AI)
	
	// MÈtodo constructor da classe Player
	public Player(String name, char symbol, boolean isHuman) {
		this.Name = name;
		this.Symbol = symbol;
		this.Human = isHuman;
		this.GameInfo = new GameInfo();
	}
	
	// MÈtodo para obter o nome do jogador
	public String getName() {
		return this.Name;
	}
	
	// MÈtodo para redefinir o nome do jogador
	public void setName(String name) {
		this.Name=name;
	}
	
	// MÈtodo para obter o symbolo do jogador
	public char getSymbol() {
		return this.Symbol;
	}
	
	// MÈtodo para obter o objecto GameInfo associado ao jogador
	public GameInfo GameInfo() {
		return this.GameInfo;
	}
	
	// MÈtodo para obter o tipo de jogador, humano ou computador
	public boolean isHuman() {
		return this.Human;
	}
}
