package campominado.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import campominado.excecao.ExplosaoException;

public class Tabuleiro {
	
	private int quantidadedelinhas;
	private int quantidadedecolunas;
	private int quantidadedeminas;
	
	private final List<Campo> campos = new ArrayList<>();

	public Tabuleiro(int quantidadedelinhas, int quantidadedecolunas, int quantidadedeminas) {
		this.quantidadedelinhas = quantidadedelinhas;
		this.quantidadedecolunas = quantidadedecolunas;
		this.quantidadedeminas = quantidadedeminas;
		
		gerarCampos();
		associarVizinhos();
		sortearMinas();
	}
	
	public void abrir(int linha, int coluna) {
		try {
			campos.parallelStream().filter(c -> c.getLinha() == linha && c.getColuna() == coluna).findFirst().ifPresent(c -> c.abrir());
			
		} catch (ExplosaoException e) {
			campos.forEach(c -> c.setAberto(true));
			throw e;
		}
	}
	
	public void alternarMarcacao(int linha, int coluna) {
		campos.parallelStream().filter(c -> c.getLinha() == linha && c.getColuna() == coluna).findFirst().ifPresent(c -> c.alternarMarcacao());
	}

	private void gerarCampos() {
		for (int linha = 0; linha < quantidadedelinhas; linha++) {
			for (int coluna = 0; coluna < quantidadedecolunas; coluna++) {
				campos.add(new Campo(linha, coluna));
			}
		}
		
	}
	
    private void associarVizinhos() {
    	for(Campo c1: campos) {
    		for(Campo c2: campos) {
    			c1.adicionarVizinho(c2);
    		}
    	}

	}

    private void sortearMinas() {
    	long minasArmadas = 0;
    	Predicate<Campo> minado = c -> c.isMinado();
    	
    	do {
    		int aleatorio = (int) (Math.random() * campos.size());
    		campos.get(aleatorio).minar();
    		minasArmadas = campos.stream().filter(minado).count();
    	} while(minasArmadas < quantidadedeminas);
	
    }
    
    public boolean objetivoAlcancado() {
		return campos.stream().allMatch(c -> c.objetivoAlcancado());
	}
    
    public void reiniciar() {
    	campos.stream().forEach(c -> c.reinicar());
    	sortearMinas();
    }
    
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append("  ");
    	for (int c = 0; c < quantidadedecolunas; c++) {
    		sb.append(" ");
    		sb.append(c);
    		sb.append(" ");
			
		}
    	sb.append("\n");
    	
    	int i = 0;
    	for (int l = 0; l < quantidadedelinhas; l++) {
    		sb.append(l);
    		sb.append(" ");
    		for (int c = 0; c < quantidadedecolunas; c++) {
    			sb.append(" ");
    			sb.append(campos.get(i));
    			sb.append(" ");
    			i++;
			}
    		sb.append("\n");
				
		}
	    return sb.toString();
    	
    }
    
			
}
