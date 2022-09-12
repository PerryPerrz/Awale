/**
 * Classe Joueur.
 */
public class Joueur {
    private int id;
    private int grenier;

    /**
     * Constructeur de la classe Joueur.
     *
     * @param id id de l'IA
     */
    public Joueur(int id) {
        this.id = id;
        this.grenier = 0;
    }

    /**
     * Constructeur de la classe Joueur.
     *
     * @param joueurACopier joueur à copier
     */
    public Joueur(Joueur joueurACopier) {
        this.id = joueurACopier.getId();
        this.grenier = joueurACopier.getGrenier();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGrenier() {
        return grenier;
    }

    public void setGrenier(int grenier) {
        this.grenier = grenier;
    }

    /**
     * Procédure qui ajoute la valeur à ajouter au grenier.
     *
     * @param valeurAAjouter la valeur à ajouter au grenier
     */
    public void remplissageGrenier(int valeurAAjouter) {
        this.grenier = this.grenier + valeurAAjouter;
    }
}
