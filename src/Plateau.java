/**
 * Classe Plateau.
 */
public class Plateau {
    private final int[] plateau;

    /**
     * Constructeur de la classe Plateau.
     */
    public Plateau() {
        this.plateau = new int[12];
        for (int i = 0; i <= this.plateau.length - 1; ++i) {
            this.plateau[i] = 4;
        }
    }

    public Plateau(Plateau plateauACopier) {
        this.plateau = new int[12];
        for (int i = 0; i <= this.plateau.length - 1; ++i) {
            this.plateau[i] = plateauACopier.getNbGraine(i);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.plateau.length; ++i) {
            sb.append("(").append(this.plateau[i]).append(") ");
            if (i == (this.plateau.length / 2) - 1)
                sb.append("\n");
        }
        return sb.toString();
    }

    //Retourne le nombre de graines capturées.
    public int jouer(int numCase) {
        int nbGraines = 0;
        int numJoueur = 1;
        nbGraines = this.plateau[numCase];
        this.plateau[numCase] = 0;

        //On regarde quel joueur joue actuellement (celui qui a vidé la case correspond à celui qui joue)
        if (numCase >= 0 && numCase < 6) {
            //Le joueur 2 correspond au joueur en haut du plateau
            numJoueur = 2;
        }

        int caseActuelle = sensJeu(numCase);
        //Dépôt dans le sens anti-horaire des graines par le joueur.
        while (nbGraines != 0) {
            //Sauter : Lorsque l'on fait plus d'un tour et que l'on veut égrener, on saute la case de départ, celle-ci doit rester vide. On égrène seulement si la case de départ est différente de la case actuelle.
            if (numCase != caseActuelle) {
                this.plateau[caseActuelle] = this.plateau[caseActuelle] + 1;
                nbGraines--;
            }
            //On passe à la suite
            caseActuelle = sensJeu(caseActuelle);
        }
        //Affamer, on teste d'abord si le coup n'affame pas l'adversaire sur une copie du plateau, si c'est le cas, on joue le coup.
        Plateau plateauACopier = new Plateau(this);

        plateauACopier.raffler(caseActuelle, numJoueur);

        boolean ligneVides = (numCase >= 6 && numCase <= 11) ? plateauACopier.verifCasesVides(0, 6) : plateauACopier.verifCasesVides(6, 12);

        //Rafle, tant que l'on peut raffler, on raffle, dès que l'on ne peut pas raffler une case, on stop la raffle.
        return ligneVides ? 0 : raffler(caseActuelle, numJoueur);

    }

    public int sensJeu(int caseActuelle) {
        int indiceCaseASeDeplacer = 0;
        if (caseActuelle == 0) {
            indiceCaseASeDeplacer = 6;
        } else {
            if (caseActuelle == 11)
                indiceCaseASeDeplacer = 5;
            else if (caseActuelle > 0 && caseActuelle < 6) {
                indiceCaseASeDeplacer = caseActuelle - 1;
            } else
                indiceCaseASeDeplacer = caseActuelle + 1;
        }
        return indiceCaseASeDeplacer;
    }

    //Fonction utile à l'application de la règle "Raffler"
    public int sensInverseJeu(int caseActuelle) {
        int indiceCaseASeDeplacer = 0;
        if (caseActuelle == 6)
            indiceCaseASeDeplacer = 0;
        else {
            if (caseActuelle == 5)
                indiceCaseASeDeplacer = 11;
            else if (caseActuelle >= 0 && caseActuelle < 6) {
                indiceCaseASeDeplacer = caseActuelle + 1;
            } else
                indiceCaseASeDeplacer = caseActuelle - 1;
        }
        return indiceCaseASeDeplacer;
    }

    public int[] getTableau() {
        return plateau;
    }

    //Fonction qui retourne le nombre de graines d'une case d'indice i dans le plateau.
    public int getNbGraine(int i) {
        return this.plateau[i];
    }

    public int raffler(int caseActuelle, int numJoueur) {
        boolean peutOnRaffler = true;
        int grenierJoueur = 0;
        boolean caseAdversaire = false;
        while (peutOnRaffler) {
            caseActuelle = sensInverseJeu(caseActuelle);
            //Le joueur prend des graines seulement dans l'un des 6 trous de l'adversaire et si le trou contient 2 ou 3 graines.
            caseAdversaire = ((caseActuelle >= 0 && caseActuelle < 6) && numJoueur == 1) || ((caseActuelle >= 6 && caseActuelle <= 11) && numJoueur == 2);
            if ((this.plateau[caseActuelle] == 2 || this.plateau[caseActuelle] == 3) && caseAdversaire) {
                //Capturer
                grenierJoueur += this.plateau[caseActuelle];
                this.plateau[caseActuelle] = 0;
                //(Rafle) On passe à la case précédente.
            } else {
                peutOnRaffler = false;
            }
        }
        return grenierJoueur;
    }

    /**
     * Fonction qui vérifie si les cases/trous de l'IA sont vides ou non. (Retourne vrai, si ce n'est pas vide == S'il n'y a pas besoin de le nourrir).
     *
     * @param x indice de départ (6 pour un humain, 0 pour une IA)
     * @param y indice d'arrivé (12 pour un humain, 6 pour une IA)
     * @return Faux s'il y a au moins une case non-vide, Vrai si la ligne est vide.
     */
    public boolean verifCasesVides(int x, int y) {
        boolean caseVide = true;
        for (int i = x; i < y; ++i) {
            if (this.plateau[i] != 0)
                caseVide = false;
        }
        return caseVide;
    }

    //Fonction qui fait la somme des trous d'une ligne.
    public int viderLigne(int x, int y) {
        int cpt = 0;
        for (int i = x; i < y; ++i) {
            cpt += this.plateau[i];
            plateau[i] = 0;
        }
        return cpt;
    }
}
