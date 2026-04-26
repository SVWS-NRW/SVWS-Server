package de.svws_nrw.core.kursblockung;

import java.util.Arrays;
import java.util.Random;

import jakarta.validation.constraints.NotNull;

/** Diese Klasse realisiert eine Adjazenzmatrix und implementiert einige Graph-Algorithmen (Maximum-cardinality
 * bipartite matching, maximum/minimum weighted bipartite matching, ...). Die Adjazenzmatrix wird im Folgenden Matrix
 * genannt.
 *
 * @see <a href= "https://en.wikipedia.org/wiki/Matching_(graph_theory)">Wikipedia - Matching_(graph_theory)</a> */
public class KursblockungMatrix {

	/** Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed. */
	private final @NotNull Random rnd;

	/** Die Werte der Matrix [Zeile][Spalte]. */
	private final @NotNull long @NotNull [] @NotNull [] matrix;

	/** Die Anzahl der Zeilen der Matrix. */
	private final int rows;

	/** Die Anzahl der Spalten der Matrix. */
	private final int cols;

	/** Die aktuelle Permutation der Zeilen für potentiellen Nichtdeterminismus. Interne Algorithmen iterieren so über
	 * die Matrix-Zeilen in zufälliger Reihenfolge, damit aus mehreren optimalen Lösungen eine zufällige ausgewählt
	 * wird. Die echten Zeilen der Matrix bleiben unverändert. */
	private final @NotNull int[] permR;

	/** Die aktuelle Permutation der Spalten für potentiellen Nichtdeterminismus. Interne Algorithmen iterieren so über
	 * die Matrix-Spalten in zufälliger Reihenfolge, damit aus mehreren optimalen Lösungen eine zufällige ausgewählt
	 * wird. Die echten Spalten der Matrix bleiben unverändert. */
	private final @NotNull int[] permC;

	/** Die Zuordnung einer Zeile zu einer Spalte (für das bipartite Matching). */
	private final @NotNull int[] r2c;

	/** Die Zuordnung einer Spalte zu einer Zeile (für das bipartite Matching). */
	private final @NotNull int[] c2r;

	/** Definiert, ob eine linker Zeilen-Knoten bereits besucht wurde (für das bipartite Matching). */
	private final @NotNull boolean[] besuchtR;

	/** Definiert, ob eine rechter Spalten-Knoten bereits besucht wurde (für das bipartite Matching). */
	private final @NotNull boolean[] abgearbeitetC;

	/** Definiert, den Zeilen-Vorgänger-Knoten eines rechten Spalten-Knotens (für das bipartite Matching). */
	private final @NotNull int[] vorgaengerCzuR;

	/** Definiert eine Warteschlange mit maximal <i>rows</i> Knoten (für das bipartite Matching). */
	private final @NotNull int[] queueR;

	/** Definiert ein linkes Knotengewicht (Potential) zur Umgewichtung der ausgehenden Kanten (für das bipartite
	 * Matching). */
	private final @NotNull long[] potentialR;

	/** Definiert ein rechtes Knotengewicht (Potential) zur Umgewichtung der eingehenden Kanten (für das bipartite
	 * Matching). */
	private final @NotNull long[] potentialC;

	/** Definiert die Entfernung vom aktuellen Zeilen-Knoten zu den jeweiligen Spalten-Knoten (für das bipartite
	 * Matching). */
	private final @NotNull long[] distanzC;

	/**
	 * Erzeugt eine neue Matrix mit {@code rows} Zeilen und {@code cols} Spalten.
	 *
	 * @param random   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param rows     Die Anzahl der Zeilen der Matrix.
	 * @param cols     Die Anzahl der Spalten der Matrix.
	 */
	public KursblockungMatrix(final @NotNull Random random, final int rows, final int cols) {
		this.rnd = random;
		this.rows = rows;
		this.cols = cols;
		this.matrix = new long[rows][cols];
		this.permR = new int[rows];
		this.permC = new int[cols];
		this.r2c = new int[rows];
		this.c2r = new int[cols];
		this.besuchtR = new boolean[rows];
		this.abgearbeitetC = new boolean[cols];
		this.vorgaengerCzuR = new int[cols];
		this.queueR = new int[rows];
		this.potentialR = new long[rows];
		this.potentialC = new long[cols];
		this.distanzC = new long[cols];
		initialisiere(permR);
		initialisiere(permC);
	}


	/**
	 * Berechnet zur aktuellen Matrix ein maximales bipartites Matching. Die Methode geht davon aus, dass in der Matrix
	 * ausschließlich die Werte 0 und 1 vorkommen. Werte ungleich 0 werden andernfalls als 1 (eine Kante im Graphen)
	 * interpretiert. Nichtquadratische Matrizen sind erlaubt. Das Ergebnis der Methode ist eine größtmögliche Zeilen-
	 * zu Spaltenzuordnung. Der Algorithmus hat eine Laufzeit von O(n³).
	 *
	 * @param  nichtdeterministisch   definiert, ob das Ergebnis zufällig sein soll, falls es mehrere optimale Lösungen gibt.
	 *
	 * @return die Zeilen- zu Spaltenzuordnung, negative Werte entsprechen einer Nichtzuordnung.
	 */
	public @NotNull int[] gibMaximalesBipartitesMatching(final boolean nichtdeterministisch) {
	    Arrays.fill(r2c, -1);
	    Arrays.fill(c2r, -1);

	    initialisierPermRundPermC(nichtdeterministisch);

	    for (int pseudoR = 0; pseudoR < rows; pseudoR++) {
	        findeUndWendeAugmentierendenPfadAn(permR[pseudoR]);
	    }

	    return r2c;
	}


	private void findeUndWendeAugmentierendenPfadAn(final int startR) {
	    Arrays.fill(besuchtR, false);
	    Arrays.fill(vorgaengerCzuR, -1);

	    int queueFirst = 0;
	    int queueLast = 0;
	    queueR[queueLast++] = startR;
	    besuchtR[startR] = true;

	    while (queueFirst < queueLast) {
	        final int vonR = queueR[queueFirst++];

	        for (int pseudoC = 0; pseudoC < cols; pseudoC++) {
	            final int ueberC = permC[pseudoC];

	            // Guard-Clause: Keine Kante oder Kante bereits im aktuellen Matching
	            if (matrix[vonR][ueberC] == 0 || r2c[vonR] == ueberC) {
	                continue;
	            }

	            final int zuR = c2r[ueberC];

	            // Freier Spaltenknoten gefunden -> Ringtausch durchführen
	            if (zuR == -1) {
	                fuehreRingtauschDurch(ueberC, vonR);
	                return; // Breitensuche für diesen Startknoten erfolgreich beenden
	            }

	            // Ist der Weg zurück noch unbesucht?
	            if (!besuchtR[zuR]) {
	                besuchtR[zuR] = true;
	                queueR[queueLast++] = zuR;
	                vorgaengerCzuR[ueberC] = vonR;
	            }
	        }
	    }
	}


	/**
	 * Führt den eigentlichen Ringtausch durch (Augmentierung des Pfades),
	 * sobald ein freier Spaltenknoten gefunden wurde.
	 *
	 * @param zielC   Der gefundene freie Spaltenknoten.
	 * @param vonR    Der Zeilenknoten, über den der Spaltenknoten erreicht wurde.
	 */
	private void fuehreRingtauschDurch(final int zielC, final int vonR) {
	    vorgaengerCzuR[zielC] = vonR;
	    int c2 = zielC;

	    while (c2 >= 0) {
	        final int r2 = vorgaengerCzuR[c2];
	        final int saveC = r2c[r2];
	        c2r[c2] = r2;
	        r2c[r2] = c2;
	        c2 = saveC;
	    }
	}


	/**
	 * Berechnet zur aktuellen Matrix ein minimales gewichtetes Matching. Die Methode geht davon aus, dass in der
	 * Matrix ganzzahlige Werte vorkommen, d.h. es existiert eine Kante von jedem linken Knoten zu jedem rechten Knoten.
	 * Negative Werte und nichtquadratische Matrizen sind erlaubt. Zur Berechnung eines maximalen Matching kann man
	 * vorher alle Zellenwerte negieren. Das Ergebnis der Methode ist eine Zeilen- zu Spaltenzuordnung, deren Summe
	 * minimal ist. Der Algorithmus verwendet mehrere Runden eines SSSP-Algorithmus (Dijkstra). Damit dies bei negativen
	 * Werten funktioniert, werden die Kanten mit Hilfe von Knoten-Potentialen umgewichtet.
	 * <br>Der Algorithmus hat eine Laufzeit von O(n³).
	 *
	 * @see <a href= "https://en.wikipedia.org/wiki/Shortest_path_problem">Wikipedia - Shortest_path_problem</a>
	 * @see <a href= "https://en.wikipedia.org/wiki/Johnson%27s_algorithm">Wikipedia - Johnsons Algorithm</a>
	 *
	 * @param nichtdeterministisch   definiert, ob das Ergebnis zufällig sein soll, falls es mehrere optimale Lösungen gibt.
	 *
	 * @return die Zeilen- zu Spaltenzuordnung, negative Werte entsprechen einer Nichtzuordnung.
	 */
	public @NotNull int[] gibMinimalesBipartitesMatchingGewichtet(final boolean nichtdeterministisch) {
	    // Aktuelle Spalten-Zeilen-Zuordnungen löschen.
	    Arrays.fill(r2c, -1);
	    Arrays.fill(c2r, -1);

	    // Aktuelle Knoten-Potentiale löschen. Die Knoten-Potentiale sind nötig, um negative Matrix-Werte zu vermeiden.
	    // Nur so lässt sich der Dijkstra-Algorithmus anwenden.
	    Arrays.fill(potentialR, 0);
	    Arrays.fill(potentialC, 0);

	    // Initialisiert 'permR' und 'permC'. Die Arrays sind dafür zuständig, dass die Matrix-Zeilen und
	    // Matrix-Spalten in zufälliger Reihenfolge durchgegangen werden. Gibt es mehrere kürzeste Pfade, so
	    // führt dies dazu, dass von allen möglichen Pfaden ein zufälliger gewählt wird.
	    initialisierPermRundPermC(nichtdeterministisch);

	    // Zeilen- und Spalten-Potentiale anpassen
	    passeInitialePotentialeAn();

	    // Bei einer nichtquadratischen Matrix bestimmt die kleinere Dimension, wie
	    // viele Zuordnungen es maximal geben kann.
	    int dijkstraRunden = Math.min(rows, cols);

	    // Starte mit einer Greedy-Zuordnung der Zeilen.
	    // Durch jede erfolgreiche Greedy-Zuordnung verringern sich die nachfolgenden Dijkstra-Runden.
	    dijkstraRunden -= berechneGreedyStartZuordnung();

	    // Pro Runde wird zunächst der kürzeste Pfad von allen r-Knoten zu allen c-Knoten berechnet.
	    for (int dijkstraRunde = 0; dijkstraRunde < dijkstraRunden; dijkstraRunde++) {
	        fuehreDijkstraRundeDurch();
	    }

	    return r2c;
	}

	/**
	 * Passt die Initial-Potentiale der Zeilen und Spalten an,
	 * um negative Kantengewichte für den ersten Dijkstra-Durchlauf zu vermeiden.
	 */
	private void passeInitialePotentialeAn() {

	    // Zeilen-Potentiale anpassen
	    if (rows <= cols) {
	        // Subtrahiere das Zeilen-Minimum von allen Zeilen
	        for (int r = 0; r < rows; r++) {
	            long min = (matrix[r][0] + potentialR[r]) - potentialC[0];
	            for (int c = 0; c < cols; c++) {
	                final long kante = (matrix[r][c] + potentialR[r]) - potentialC[c];
	                min = Math.min(min, kante);
	            }
	            potentialR[r] -= min;
	        }
	    }

	    // Spalten-Potentiale anpassen
	    if (cols <= rows) {
	        // Subtrahiere das Spalten-Minimum von allen Spalten
	        for (int c = 0; c < cols; c++) {
	            long min = (matrix[0][c] + potentialR[0]) - potentialC[c];
	            for (int r = 0; r < rows; r++) {
	                final long kante = (matrix[r][c] + potentialR[r]) - potentialC[c];
	                min = Math.min(min, kante);
	            }
	            potentialC[c] += min;
	        }
	    }
	}


	/**
	 * Ordnet jeder Zeile r einen Partner c zu, wenn in der Matrix der Wert 0 ist
	 * und der c-Knoten nicht bereits zugeordnet wurde.
	 *
	 * @return Die Anzahl der erfolgreich zugewiesenen Paare.
	 */
	private int berechneGreedyStartZuordnung() {
	    Arrays.fill(abgearbeitetC, false); // Speichert, ob der c-Knoten bereits zugeordnet wurde.
	    int erfolgreicheZuordnungen = 0;

	    for (int ir = 0; ir < rows; ir++) {
	        final int r = permR[ir]; // zufällige r-Knoten-Reihenfolge

	        for (int ic = 0; ic < cols; ic++) {
	            final int c = permC[ic]; // zufällige c-Knoten-Reihenfolge

	            // Nur prüfen, wenn der Spaltenknoten noch frei ist.
	            if (!abgearbeitetC[c]) {
	                final long kante = (matrix[r][c] + potentialR[r]) - potentialC[c];

	                if (kante == 0) {
	                    r2c[r] = c;
	                    c2r[c] = r;
	                    abgearbeitetC[c] = true;
	                    erfolgreicheZuordnungen++;
	                    break; // Weiter mit der nächsten Zeile.
	                }
	            }

	        }
	    }

	    return erfolgreicheZuordnungen;
	}

	/**
	 * Führt exakt eine Dijkstra-Runde aus: Initialisierung, Zyklen, Ringtausch und Neugewichtung.
	 */
	private void fuehreDijkstraRundeDurch() {

		// Initialisierung.
	    initialisiereDijkstraRunde();

		// Der Hauptalgorithmus, der den Dijkstra-Pfad findet.
	    final int endknotenC = berechneDijkstraZyklenUndFindeEndknoten();

	    // Ringtausch - Anpassung der row-col-Zuordnungen nach einer Dijkstra-Runde.
	    int currentC = endknotenC;
	    while (currentC >= 0) {
	        final int prevR = vorgaengerCzuR[currentC];
	        final int prevC = r2c[prevR];
	        r2c[prevR] = currentC;
	        c2r[currentC] = prevR;
	        currentC = prevC;
	    }

	    // Neugewichtung der Knoten (nur wenn eine Zuordnung beteiligt ist). Die Potentiale werden so angepasst,
	    // dass zugeordnete Kanten den Wert 0 haben. Damit ist gewährleistet, dass der Dijkstra-Algorithmus diese
	    // Kante rückwärts passieren kann, ohne dass dies zu einem negativen Kantenwert führt.
	    for (int r = 0; r < rows; r++) {
	        final int c = r2c[r];
	        if (c >= 0) {
	            final long kante = (matrix[r][c] + potentialR[r]) - potentialC[c];
	            potentialR[r] += distanzC[c] - kante;
	            potentialC[c] += distanzC[c];
	        }
	    }

	}

	/**
	 * Initialisiert die Dijkstra-Runde:
	 * Alle c-Knoten werden initialisiert mit dem kürzesten Pfad zu einem (noch nicht zugeordneten) r-Knoten.
	 */
	private void initialisiereDijkstraRunde() {
	    for (int ic = 0; ic < cols; ic++) {
	        final int c = permC[ic]; // zufällige c-Knoten-Reihenfolge

	        vorgaengerCzuR[c] = -1;
	        abgearbeitetC[c] = false;
	        distanzC[c] = Long.MAX_VALUE;

	        for (int ir = 0; ir < rows; ir++) {
	            final int r = permR[ir]; // zufällige R-Reihenfolge

	            if (r2c[r] < 0) {
	                final long kante = (matrix[r][c] + potentialR[r]) - potentialC[c];
	                if (kante < distanzC[c]) {
	                    distanzC[c] = kante;
	                    vorgaengerCzuR[c] = r;
	                }
	            }
	        }

	    }
	}

	/**
	 * Berechnet von allen (noch nicht zugeordneten) r-Knoten den kürzesten Pfad zu allen c-Knoten.
	 *
	 * @return der C-Knoten (Endknoten), bei dem der kürzeste Pfad erfolgreich endet
	 */
	private int berechneDijkstraZyklenUndFindeEndknoten() {
	    int endknotenC = -1;

	    for (int dijkstraZyklus = 0; dijkstraZyklus < cols; dijkstraZyklus++) {

	        // Finde den c-Knoten mit der kleinsten Distanz. Ignoriere bereits abgearbeitete Knoten.
	        int fromC = 0;
	        for (int ic = 0; ic < cols; ic++) {
	            final int c = permC[ic]; // zufällige C-Reihenfolge
	            if ((!abgearbeitetC[c]) && ((abgearbeitetC[fromC]) || (distanzC[c] < distanzC[fromC]))) {
	                fromC = c;
	            }
	        }
	        abgearbeitetC[fromC] = true;

	        // Geht es weiter (rückwärts) von "fromC"?
	        final int overR = c2r[fromC];
	        if (overR >= 0) {
	            relaxiereDijkstraNachbarn(fromC, overR);
	        } else {
	            // "fromC" ist ein Endknoten (mit kürzestem Pfad)
	            if (endknotenC < 0) {
	                endknotenC = fromC;
	            }
	        }
	    }

	    return endknotenC;
	}

	/**
	 * Relaxiert die c-Knoten-Nachbarn eines Knotens im Dijkstra-Algorithmus ("fromC" ist kein Endknoten).
	 * Untersucht alle Nachbarn von overR und aktualisiert ggf. deren minimale Distanz.
	 *
	 * @param fromC   Der Spalten-Knoten, von dem aus der Weg rückwärts verfolgt wurde.
	 * @param overR   Der Zeilen-Knoten, der fromC zugeordnet ist und dessen Nachbarn relaxiert werden.
	 */
	private void relaxiereDijkstraNachbarn(final int fromC, final int overR) {
	    // "fromC" ist kein Endknoten --> relaxiere alle C-Nachbarn
	    for (int ic = 0; ic < cols; ic++) {
	        final int toC = permC[ic]; // zufällige C-Reihenfolge

	        // Man muss hier nicht auf Vorwärts-Kante testen, da nur "fromC" falsch herum ist
	        // und das Gewicht ist >= 0, so wird der Pfad nie kürzer.
	        final long kante = (matrix[overR][toC] + potentialR[overR]) - potentialC[toC];
	        final long distance = distanzC[fromC] + kante;
	        if (distance < distanzC[toC]) {
	            distanzC[toC] = distance;
	            vorgaengerCzuR[toC] = overR;
	        }
	    }
	}

	/**
	 * Interne Methode zum Permutieren oder Initialisieren der Arrays
	 * {@link KursblockungMatrix#permR} und
	 * {@link KursblockungMatrix#permC}.
	 *
	 * @param nichtdeterministisch falls {@code true} werden {@link KursblockungMatrix#permR} und
	 *                             {@link KursblockungMatrix#permC} permutiert, sonst initialisiert.
	 */
	private void initialisierPermRundPermC(final boolean nichtdeterministisch) {
		if (nichtdeterministisch) {
			permutiere(permR);
			permutiere(permC);
		} else {
			initialisiere(permR);
			initialisiere(permC);
		}
	}

	/**
	 * Interne Methode zum Initialisieren eines Arrays so, dass das Array mit den Zahlen {@code 0,1,2...} gefüllt wird.
	 *
	 * @param perm Das Array, welches mit den Zahlen {@code 0,1,2...} gefüllt wird.
	 */
	private static void initialisiere(final @NotNull int[] perm) {
		final int laenge = perm.length;
		for (int i = 0; i < laenge; i++) {
			perm[i] = i;
		}
	}

	/**
	 * Interne Methode zum zufälligen Permutieren eines Arrays.
	 *
	 * @param perm Das Array, dessen Inhalt zufällig permutiert wird.
	 */
	private void permutiere(final @NotNull int[] perm) {
		final int laenge = perm.length;
		for (int i = 0; i < laenge; i++) {
			final int j = rnd.nextInt(laenge);
			// Tausche
			final int saveI = perm[i];
			final int saveJ = perm[j];
			perm[i] = saveJ;
			perm[j] = saveI;
		}
	}

	/**
	 * Erlaubt Zugriff auf den Inhalt des Arrays.
	 *
	 * @return Die Array-Referenz.
	 */
	public @NotNull long @NotNull [][] getMatrix() {
		return matrix;
	}

	/** Erzeugt String-Ausgabe des Arrays sowie der Zeilen-zu-Spalten-Zuordnung {@link KursblockungMatrix#r2c}. Diese
	 * Methode ist für Debug-Zwecke gedacht.
	 *
	 * @param  kommentar          Ein Kommentar der über der Matrix angezeigt wird.
	 * @param  zellenbreite       Die Breite bei der Ausgabe der Zelle.
	 * @param  mitKnotenPotential Falls {@code true}, werden die Kantenwerte umgewichtet entsprechenden der
	 *                            Knotenpotentiale, andernfalls bleiben die Kantenwerte unverändert.
	 * @return                    Eine String-Representation der Matrix. */
	public @NotNull String convertToString(final @NotNull String kommentar, final int zellenbreite, final boolean mitKnotenPotential) {
		final @NotNull StringBuilder sb = new StringBuilder(kommentar + System.lineSeparator());
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				final long wert = mitKnotenPotential ? ((matrix[r][c] + potentialR[r]) - potentialC[c]) : matrix[r][c];
				final @NotNull StringBuilder sWert1 = new StringBuilder();          // leading spaces
				final @NotNull StringBuilder sWert2 = new StringBuilder("" + wert); // value
				while ((sWert1.length() + sWert2.length()) < zellenbreite) {
					sWert1.append(" ");
				}
				final @NotNull String sZusatz = (r2c[r] == c) ? "*" : " ";
				sb.append(sWert1);
				sb.append(sWert2);
				sb.append(sZusatz);
			}
			sb.append("\n");
		}
		sb.append("r2c = " + Arrays.toString(r2c));
		sb.append("\n");
		return sb.toString();
	}

	/**
	 * Füllt die Matrix mit ganzzahligen zufälligen Zahlenwerten aus dem Intervall {@code [von;bis]}.
	 *
	 * @param von Der kleinstmögliche zufällige Wert (inklusive).
	 * @param bis Der größtmögliche zufällige Wert (inklusive).
	 */
	public void fuelleMitZufallszahlenVonBis(final int von, final int bis) {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				matrix[r][c] = rnd.nextLong((bis - von) + 1L) + von;
			}
		}
	}

	/**
	 * Füllt die Matrix mit dem übergebenen Wert.
	 *
	 * @param wert  Der Wert, der alle Zellen überschreibt.
	 */
	public void fuelleMitWert(final long wert) {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				matrix[r][c] = wert;
			}
		}
	}


	/** Liefert die Anzahl an Zeilen der Matrix.
	 *
	 * @return die Anzahl an Zeilen der Matrix. */
	public int gibZeilen() {
		return rows;
	}

	/** Liefert die Anzahl an Spalten der Matrix.
	 *
	 * @return die Anzahl an Spalten der Matrix. */
	public int gibSpalten() {
		return cols;
	}


}
