import javax.persistence.*;

@Entity
@Table(name = "Pokedex")
public class Pokedex {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Nome", nullable = false, length = 50)
    private String nome;

    @Column(name = "Peso")
    private Double peso;

    @Column(name = "Misc")
    private String misc;

    public Pokedex() {}

    public Pokedex(String nome, Double peso, String misc) {
        this.nome = nome;
        this.peso = peso;
        this.misc = misc;
    }

    public void setNome(String nome) { this.nome = nome; }

    @Override
    public String toString() {
        return "Pokedex [id=" + id + ", nome=" + nome + ", peso=" + peso + ", misc=" + misc + "]";
    }
}