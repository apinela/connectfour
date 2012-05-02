
public class GameInfo {
	
	private int Victories;		// Número de vitórias num jogo
	private int Draws;			// Número de empates num jogo
	private int Losses;			// Número de derrotas num jogo
	private int nGamesPlayed;	// Número de jogos jogados
	
	// Métodos construtores da classe GameInfo
	// Sem argumentos, para ser utilizado pela classe player (novo GameInfo)
	public GameInfo() {
		this.Victories=0;
		this.Draws=0;
		this.Losses=0;
		this.nGamesPlayed=0;
	}
	// Com argumentos, para ser utilizado pela classe Top (carrega GameInfo do Top)
	public GameInfo(int Victories, int Draws, int Losses) {
		this.Victories=Victories;
		this.Draws=Draws;
		this.Losses=Losses;
		this.nGamesPlayed=Victories+Draws+Losses;
	}
	
	// Método para obter o número de vitórias
	public int getVictories() {
		return this.Victories;
	}
	
	// Método para obter o número de empates
	public int getDraws() {
		return this.Draws;
	}
	
	// Método para obter o número de derrotas
	public int getLosses() {
		return this.Losses;
	}
	// Método para obter o número de derrotas
	public int getGamesPlayed() {
		return this.nGamesPlayed;
	}
	
	// Método para incrementar o número de vitórias 
	public void incrementVictories() {
		this.Victories++;
		this.nGamesPlayed++;
	}
	
	// Método para incrementar o número de empates
	public void incrementDraws() {
		this.Draws++;
		this.nGamesPlayed++;
	}
	
	// Método para incrementar o número de derrotas
	public void incrementLosses() {
		this.Losses++;
		this.nGamesPlayed++;
	}
	
}
