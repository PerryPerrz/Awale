import java.util.ArrayList;
import java.util.Scanner;

/**
 * Classe Awale.
 */
public class Awale {

    public static void main(String[] args) {
        Partie partie = new Partie(new Plateau(), new Joueur(1), new IA(2));
        System.out.println(partie.getPlateau());

        System.out.println("Quel profondeur pour l'IA ?");
        Scanner sc = new Scanner(System.in);
        int profondeurIA = sc.nextInt();

        while (!partie.isFinDuJeu()) {
            System.out.println("C'est le tour " + (partie.isJoueurActuelEstHumain() ? "du joueur !" : "de l'IA !"));
            //Si c'est le tour du joueur
            if (partie.isJoueurActuelEstHumain()) {
                System.out.println("Indiquez l'indice de la case que vous voulez prendre : ");
                Scanner scanner = new Scanner(System.in);
                int i = scanner.nextInt();

                //On test la validité des coups donnés
                ArrayList<Integer> listeDeCoups = partie.coupsCorrects();
                while (!listeDeCoups.contains(i)) {
                    System.out.println("Coup incorrect : veuillez recommencer ! " + (partie.isJoueurActuelEstHumain() ? "Coups corrects du joueur : " : "Coups correctes de l'IA : ") + listeDeCoups);
                    i = sc.nextInt();
                }

                System.out.println("Grenier du joueur : " + partie.getJoueur().getGrenier() + " | Grenier de l'IA : " + partie.getIa().getGrenier());
                partie.jouerUnCoup(i);
            }
            //Si c'est le tour de l'IA
            else {
                //On donne la partie est la profondeur.
                int coupAJouer = partie.getIa().minimax(partie, profondeurIA);
                partie.jouerUnCoup(coupAJouer);
            }
            System.out.println(partie.getPlateau() + "\n");
        }
        System.out.println(partie.getGagnant());
    }
}
