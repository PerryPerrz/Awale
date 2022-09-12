import java.util.ArrayList;
import java.util.Random;

/**
 * Classe Partie.
 */
public class Partie {
    private final Plateau plateau;
    private final Joueur joueur;
    private final IA ia;
    private boolean joueurActuelEstHumain; //Booléen qui retourne vrai si le joueur actuel est un humain.
    private boolean isFinDuJeu;

    /**
     * Constructeur de la classe Partie.
     *
     * @param plateau le plateau
     * @param joueur  le joueur
     * @param ia      l'IA
     */
    public Partie(Plateau plateau, Joueur joueur, IA ia) {
        this.plateau = plateau;
        this.joueur = joueur;
        this.ia = ia;

        //On fait un nombre random pour savoir lequel de l'IA ou de l'humain commence.
        Random random = new Random();
        int choice = random.nextInt() % 2; //entre 0 et 1
        this.joueurActuelEstHumain = choice == 0;

        this.isFinDuJeu = false;
    }

    /**
     * Second constructeur permettant de faire une copie de la partie actuel. Je ne dois pas modifier la partie de jeu actuel, or passer par des références (Getters) la modifie. Je passe donc par une copie.
     *
     * @param partieACopier la partie à copier
     */
    public Partie(Partie partieACopier) {
        this.plateau = new Plateau(partieACopier.getPlateau());
        this.joueur = new Joueur(partieACopier.getJoueur());
        this.ia = new IA(partieACopier.getIa());
        this.joueurActuelEstHumain = partieACopier.isJoueurActuelEstHumain();
        this.isFinDuJeu = partieACopier.isFinDuJeu();
    }

    /**
     * Procédure qui vérifie si le coup à jouer et correcte, s'il l'est, elle e joue et change l'état de la partie en conséquence.
     *
     * @param numCase le numéro de la case (l'indice) à laquel le joueur actuel veut jouer. (prendre les graines)
     */
    public void jouerUnCoup(int numCase) {
        if (this.joueurActuelEstHumain)
            this.joueur.remplissageGrenier(this.plateau.jouer(numCase));
        else
            this.ia.remplissageGrenier(this.plateau.jouer(numCase));

        //On regarde si quelqu'un à gagné.
        if (this.joueur.getGrenier() >= 25 || this.ia.getGrenier() >= 25)
            this.isFinDuJeu = true;

        //On passe au joueur suivant.
        this.joueurActuelEstHumain = !this.joueurActuelEstHumain;
    }

    public boolean isFinDuJeu() {
        return isFinDuJeu;
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public IA getIa() {
        return ia;
    }

    public String getGagnant() {
        StringBuilder sb = new StringBuilder();
        if (isFinDuJeu) {
            if (this.joueur.getGrenier() > this.ia.getGrenier()) {
                sb.append("Vous avez gagné !");
            } else if (this.joueur.getGrenier() == this.ia.getGrenier()) {
                sb.append("Incroyable, c'est une égalité parfaite !");
            } else {
                sb.append("L'IA à gagné ! Les robots domineront les Hommes !");
            }
        }
        sb.append("\nGrenier du joueur : " + this.joueur.getGrenier() + " | " + "Grenier de l'IA : " + this.ia.getGrenier());
        return sb.toString();
    }

    /**
     * Fonction qui retourne les coups corrects pour le joueur actuel.
     *
     * @return Une ArrayList de coups corrects
     */
    public ArrayList<Integer> coupsCorrects() {
        ArrayList<Integer> coups = new ArrayList<>(5);
        //On init aux valeur correspondant à l'IA
        int x = 0, y = 6;

        if (this.joueurActuelEstHumain) {
            x = 6;
            y = 12;
        }

        for (int i = x; i < y; ++i) {
            //Je créer une copie de la partie.
            Partie partieACopier = new Partie(this);

            //On joue le coup sur la partie à copier.
            partieACopier.jouerUnCoup(i);

            //On check si la ligne de l'adversaire est vide. (Si c'est au tour de l'humain on check si le coup créer une ligne vide à l'IA).
            boolean ligneVides = isJoueurActuelEstHumain() ? partieACopier.getPlateau().verifCasesVides(0, 6) : partieACopier.getPlateau().verifCasesVides(6, 12);

            //On ajoute un coup si les cases ne sont pas vides et si le coup permet de nourrir l'adversaire s'il n'a aucunes graines.
            if (!(this.plateau.getTableau()[i] == 0) && !ligneVides) {
                coups.add(i);
            }
        }

        //Si le joueur actuel n'a pas de coups possibles, cela signifie qu'il a perdu.
        if (coups.size() == 0) {
            isFinDuJeu = true;

            boolean ligneVide = isJoueurActuelEstHumain() ? this.getPlateau().verifCasesVides(0, 6) : this.getPlateau().verifCasesVides(6, 12);
            //Si le joueur actuel n'as plus de graines.
            if (ligneVide) {
                //Alors on fait ramasser à l'adversaire toutes ses graines.
                if (isJoueurActuelEstHumain()) {
                    this.ia.remplissageGrenier(this.getPlateau().viderLigne(0, 6));
                } else {
                    this.joueur.remplissageGrenier(this.plateau.viderLigne(6, 12));
                }
            }//Si le joueur en face n'as plus de graines et que l'on ne peut pas le nourrir.
            else {
                //On fait ramasser au joueur actuel toutes ses graines.
                if (!isJoueurActuelEstHumain()) {
                    this.ia.remplissageGrenier(this.getPlateau().viderLigne(0, 6));
                } else {
                    this.joueur.remplissageGrenier(this.plateau.viderLigne(6, 12));
                }
            }
        }

        return coups;
    }

    public boolean isJoueurActuelEstHumain() {
        return joueurActuelEstHumain;
    }

    //Fonction qui retourne true si le joueur à gagné.
    public boolean isJoueurAGagne() {
        return this.joueur.getGrenier() > this.ia.getGrenier();
    }

    public boolean isIaAGagne() {
        return this.joueur.getGrenier() < this.ia.getGrenier();
    }

    //Fonction qui calcul le score d'un état. (score du point de vue de l'ia)
    public int eval0() {
        return this.ia.getGrenier() - this.joueur.getGrenier();
    }
}
