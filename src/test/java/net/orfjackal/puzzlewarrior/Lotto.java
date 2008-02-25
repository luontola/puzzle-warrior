package net.orfjackal.puzzlewarrior;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Esko Luontola
 * @since 26.2.2008
 */
public class Lotto {
    public static void main(String[] args) throws InterruptedException {

        sano("- Tervetuloa seuraamaan lottoarvontaa.");
        sano("  Arvonnan viralliset valvojat ovat Matti Launiainen...");
        sano("- Hyvää iltaa.");
        sano("- ...ja Rauno Sintonen.");
        sano("- Hyvää iltaa.");
        sano("- Arvotaan seitsemän numeroa ja kolme lisänumeroa.");
        tauko();

        ShuffleBag<Integer> lottokone = new ShuffleBag<Integer>();
        for (int pallo = 1; pallo <= 39; pallo++) {
            lottokone.add(pallo);
        }
        lottokonePyörii();

        SortedSet<Integer> numerot = new TreeSet<Integer>();
        for (int i = 0; i < 7; i++) {
            lottokonePyörii();
            Integer numero = lottokone.get();
            numerot.add(numero);
            sano("- " + numero + ".\t" + numerot);
        }

        sano("- Ja lisänumerot...");

        SortedSet<Integer> lisäNumerot = new TreeSet<Integer>();
        for (int i = 0; i < 3; i++) {
            lottokonePyörii();
            Integer numero = lottokone.get();
            lisäNumerot.add(numero);
            sano("- " + numero + ".\t" + numerot + " + " + lisäNumerot);
        }

        tauko();
        sano("- Tulos seitsemän oikein on " + numerot);
        sano("  sekä lisänumerot " + lisäNumerot + ".");
    }

    private static void sano(String repliikki) throws InterruptedException {
        System.out.println(repliikki);
        Thread.sleep(1000);
    }

    private static void tauko() throws InterruptedException {
        System.out.println();
        Thread.sleep(1000);
    }

    private static void lottokonePyörii() throws InterruptedException {
        Thread.sleep(2000);
    }
}
