import java.util.ArrayList;
import java.util.HashMap;

/**
 * Classe IA.
 */
public class IA extends Joueur {
    /**
     * Constructeur de la classe IA.
     *
     * @param id id de l'IA
     */
    public IA(int id) {
        super(id);
    }

    /**
     * Constructeur de la classe IA.
     *
     * @param iaACopier ia à copier
     */
    public IA(IA iaACopier) {
        super(iaACopier);
    }

    //Fonction qui retourne le meilleur coup à jouer pour l'IA.
    public int minimax(Partie e, int c) {
        ArrayList<Partie> S = new ArrayList<>();
        float score, scoreMax;

        //Pour le serveur, j'associe un coup à un état. (De base, minmax sort le meilleur état/succ or, pour jouer avec le serveur, il faut renvoyer un coup à jouer.)
        HashMap<Partie, Integer> correspondance_coup_etat = new HashMap<>();

        ArrayList<Integer> coups = e.coupsCorrects();
        for (Integer i : coups) {
            Partie partieACopier = new Partie(e);
            partieACopier.jouerUnCoup(i);
            S.add(partieACopier);
            correspondance_coup_etat.put(partieACopier, i);
        }

        scoreMax = Integer.MIN_VALUE;
        Partie eSortie = null;

        //J'applique la fonction evaluationAlphaBeta sur tous les successeurs stockés precedement, on prend celle qui possède le score max.
        for (Partie s : S) {
            score = evaluationAlphaBeta(c, s, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (score >= scoreMax) {
                eSortie = s;
                scoreMax = score;
            }
        }

        return eSortie == null ? -1 : correspondance_coup_etat.get(eSortie);
    }

    //S : liste de successeurs, e : l'état actuel (partie).
    public float evaluationAlphaBeta(int c, Partie e, float alpha, float beta) {
        ArrayList<Partie> S = new ArrayList<>();
        float scoreMax, scoreMin;

        if (e.isFinDuJeu()) {
            return (e.isJoueurAGagne() ? Integer.MIN_VALUE : (e.isIaAGagne()) ? Integer.MAX_VALUE : 0);
        }
        if (c == 0) {
            return e.eval0();
        }
        ArrayList<Integer> coups = e.coupsCorrects();
        for (Integer i : coups) {
            Partie partieACopier = new Partie(e);
            partieACopier.jouerUnCoup(i);
            S.add(partieACopier);
        }
        if (!e.isJoueurActuelEstHumain()) {
            scoreMax = Integer.MIN_VALUE;
            for (Partie s : S) {
                scoreMax = Math.max(scoreMax, evaluationAlphaBeta(c - 1, s, alpha, beta));
                if (scoreMax >= beta) {
                    return scoreMax;
                }
            }
            return scoreMax;
        } else {
            scoreMin = Integer.MAX_VALUE;
            for (Partie s : S) {
                scoreMin = Math.min(scoreMin, evaluationAlphaBeta(c - 1, s, alpha, beta));
                if (scoreMin <= alpha) {
                    return scoreMin;
                }
            }
            return scoreMin;
        }
    }
}
