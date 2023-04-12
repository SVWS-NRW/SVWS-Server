import { JavaObject } from '../../java/lang/JavaObject';
import { Random } from '../../java/util/Random';
import { StringBuilder } from '../../java/lang/StringBuilder';
import { JavaLong } from '../../java/lang/JavaLong';
import { Arrays } from '../../java/util/Arrays';
import { System } from '../../java/lang/System';

export class KursblockungMatrix extends JavaObject {

	/**
	 * Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 */
	private readonly _random : Random;

	/**
	 * Die Werte der Matrix [Zeile][Spalte].
	 */
	private readonly matrix : Array<Array<number>>;

	/**
	 * Die Anzahl der Zeilen der Matrix.
	 */
	private readonly rows : number;

	/**
	 * Die Anzahl der Spalten der Matrix.
	 */
	private readonly cols : number;

	/**
	 * Die aktuelle Permutation der Zeilen für potentiellen Nichtdeterminismus. Interne Algorithmen iterieren so über
	 *  die Matrix-Zeilen in zufälliger Reihenfolge, damit aus mehreren optimalen Lösungen eine zufällige ausgewählt
	 *  wird. Die echten Zeilen der Matrix bleiben unverändert.
	 */
	private readonly permR : Array<number>;

	/**
	 * Die aktuelle Permutation der Spalten für potentiellen Nichtdeterminismus. Interne Algorithmen iterieren so über
	 *  die Matrix-Spalten in zufälliger Reihenfolge, damit aus mehreren optimalen Lösungen eine zufällige ausgewählt
	 *  wird. Die echten Spalten der Matrix bleiben unverändert.
	 */
	private readonly permC : Array<number>;

	/**
	 * Die Zuordnung einer Zeile zu einer Spalte (für das bipartite Matching).
	 */
	private readonly r2c : Array<number>;

	/**
	 * Die Zuordnung einer Spalte zu einer Zeile (für das bipartite Matching).
	 */
	private readonly c2r : Array<number>;

	/**
	 * Definiert, ob eine linker Zeilen-Knoten bereits besucht wurde (für das bipartite Matching).
	 */
	private readonly besuchtR : Array<boolean>;

	/**
	 * Definiert, ob eine rechter Spalten-Knoten bereits besucht wurde (für das bipartite Matching).
	 */
	private readonly abgearbeitetC : Array<boolean>;

	/**
	 * Definiert, den Zeilen-Vorgänger-Knoten eines rechten Spalten-Knotens (für das bipartite Matching).
	 */
	private readonly vorgaengerCzuR : Array<number>;

	/**
	 * Definiert eine Warteschlange mit maximal <i>rows</i> Knoten (für das bipartite Matching).
	 */
	private readonly queueR : Array<number>;

	/**
	 * Definiert ein linkes Knotengewicht (Potential) zur Umgewichtung der ausgehenden Kanten (für das bipartite
	 *  Matching).
	 */
	private readonly potentialR : Array<number>;

	/**
	 * Definiert ein rechtes Knotengewicht (Potential) zur Umgewichtung der eingehenden Kanten (für das bipartite
	 *  Matching).
	 */
	private readonly potentialC : Array<number>;

	/**
	 * Definiert die Entfernung vom aktuellen Zeilen-Knoten zu den jeweiligen Spalten-Knoten (für das bipartite
	 *  Matching).
	 */
	private readonly distanzC : Array<number>;


	/**
	 *Erzeugt eine neue Matrix mit {@code rows} Zeilen und {@code cols} Spalten.
	 *
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param rows    Die Anzahl der Zeilen der Matrix.
	 * @param cols    Die Anzahl der Spalten der Matrix.
	 */
	public constructor(pRandom : Random, rows : number, cols : number) {
		super();
		this._random = pRandom;
		this.rows = rows;
		this.cols = cols;
		this.matrix = [...Array(rows)].map(e => Array(cols).fill(0));
		this.permR = Array(rows).fill(0);
		this.permC = Array(cols).fill(0);
		this.r2c = Array(rows).fill(0);
		this.c2r = Array(cols).fill(0);
		this.besuchtR = Array(rows).fill(false);
		this.abgearbeitetC = Array(cols).fill(false);
		this.vorgaengerCzuR = Array(cols).fill(0);
		this.queueR = Array(rows).fill(0);
		this.potentialR = Array(rows).fill(0);
		this.potentialC = Array(cols).fill(0);
		this.distanzC = Array(cols).fill(0);
		KursblockungMatrix.initialisiere(this.permR);
		KursblockungMatrix.initialisiere(this.permC);
	}

	/**
	 * Berechnet zur aktuellen Matrix ein maximales bipartites Matching. Die Methode geht davon aus, dass in der Matrix
	 * ausschließlich die Werte 0 und 1 vorkommen. Werte ungleich 0 werden andernfalls als 1 (eine Kante im Graphen)
	 * interpretiert. Nichtquadratische Matrizen sind erlaubt. Das Ergebnis der Methode ist eine größtmögliche Zeilen-
	 * zu Spaltenzuordnung. Der Algorithmus hat eine Laufzeit von O(n³).
	 *
	 * @param  nichtdeterministisch definiert, ob das Ergebnis zufällig sein soll, falls es mehrere optimale Lösungen
	 *                              gibt.
	 * @return                      die Zeilen- zu Spaltenzuordnung, negative Werte entsprechen einer Nichtzuordnung.
	 */
	public gibMaximalesBipartitesMatching(nichtdeterministisch : boolean) : Array<number> {
		Arrays.fill(this.r2c, -1);
		Arrays.fill(this.c2r, -1);
		this.initialisierPermRundPermC(nichtdeterministisch);
		for (let pseudoR : number = 0; pseudoR < this.rows; pseudoR++) {
			const r : number = this.permR[pseudoR];
			Arrays.fill(this.besuchtR, false);
			Arrays.fill(this.vorgaengerCzuR, -1);
			let queue_first : number = 0;
			let queue_last : number = 0;
			this.queueR[queue_last] = r;
			queue_last++;
			this.besuchtR[r] = true;
			while (queue_first < queue_last) {
				const vonR : number = this.queueR[queue_first];
				queue_first++;
				for (let pseudoC : number = 0; pseudoC < this.cols; pseudoC++) {
					const ueberC : number = this.permC[pseudoC];
					if ((this.matrix[vonR][ueberC] !== 0) && (this.r2c[vonR] !== ueberC)) {
						const zuR : number = this.c2r[ueberC];
						if (zuR === -1) {
							this.vorgaengerCzuR[ueberC] = vonR;
							for (let c2 : number = ueberC; c2 >= 0; ) {
								const r2 : number = this.vorgaengerCzuR[c2];
								const saveC : number = this.r2c[r2];
								this.c2r[c2] = r2;
								this.r2c[r2] = c2;
								c2 = saveC;
							}
							queue_last = queue_first;
							break;
						}
						if (!this.besuchtR[zuR]) {
							this.besuchtR[zuR] = true;
							this.queueR[queue_last] = zuR;
							queue_last++;
							this.vorgaengerCzuR[ueberC] = vonR;
						}
					}
				}
			}
		}
		return this.r2c;
	}

	/**
	 *Berechnet zur aktuellen Matrix ein minimales gewichtetes Matching. Die Methode geht davon aus, dass in der
	 * Matrix ganzzahlige Werte vorkommen, d.h. es existiert eine Kante von jedem linken Knoten zu jedem rechten Knoten.
	 * Negative Werte und nichtquadratische Matrizen sind erlaubt. Zur Berechnung eines maximalen Matching kann man
	 * vorher alle Zellenwerte negieren. Das Ergebnis der Methode ist eine Zeilen- zu Spaltenzuordnung, deren Summe
	 * minimal ist. Der Algorithmus verwendet mehrere Runden eines SSSP-Algorithmus (Dijkstra). Damit dies bei negativen
	 * Werten funktioniert, werden die Kanten mit Hilfe von Knoten-Potentialen umgewichtet. Der Algorithmus hat eine
	 * Laufzeit von O(n³).
	 *
	 * @see                         <a href= "https://en.wikipedia.org/wiki/Shortest_path_problem">Wikipedia -
	 *                              Shortest_path_problem</a>
	 * @see                         <a href= "https://en.wikipedia.org/wiki/Johnson%27s_algorithm">Wikipedia - Johnsons
	 *                              Algorithm</a>
	 * @param  nichtdeterministisch definiert, ob das Ergebnis zufällig sein soll, falls es mehrere optimale Lösungen
	 *                              gibt.
	 * @return                      die Zeilen- zu Spaltenzuordnung, negative Werte entsprechen einer Nichtzuordnung.
	 */
	public gibMinimalesBipartitesMatchingGewichtet(nichtdeterministisch : boolean) : Array<number> {
		Arrays.fill(this.r2c, -1);
		Arrays.fill(this.c2r, -1);
		Arrays.fill(this.potentialR, 0);
		Arrays.fill(this.potentialC, 0);
		this.initialisierPermRundPermC(nichtdeterministisch);
		if (this.rows <= this.cols) {
			for (let r : number = 0; r < this.rows; r++) {
				let min : number = this.matrix[r][0] + this.potentialR[r] - this.potentialC[0];
				for (let c : number = 0; c < this.cols; c++) {
					const kante : number = this.matrix[r][c] + this.potentialR[r] - this.potentialC[c];
					min = Math.min(min, kante);
				}
				this.potentialR[r] -= min;
			}
		}
		if (this.cols <= this.rows) {
			for (let c : number = 0; c < this.cols; c++) {
				let min : number = this.matrix[0][c] + this.potentialR[0] - this.potentialC[c];
				for (let r : number = 0; r < this.rows; r++) {
					const kante : number = this.matrix[r][c] + this.potentialR[r] - this.potentialC[c];
					min = Math.min(min, kante);
				}
				this.potentialC[c] += min;
			}
		}
		let dijkstraRunden : number = Math.min(this.rows, this.cols);
		Arrays.fill(this.abgearbeitetC, false);
		for (let ir : number = 0; ir < this.rows; ir++) {
			const r : number = this.permR[ir];
			for (let ic : number = 0; ic < this.cols; ic++) {
				const c : number = this.permC[ic];
				if (!this.abgearbeitetC[c]) {
					const kante : number = this.matrix[r][c] + this.potentialR[r] - this.potentialC[c];
					if (kante === 0) {
						this.r2c[r] = c;
						this.c2r[c] = r;
						this.abgearbeitetC[c] = true;
						dijkstraRunden--;
						break;
					}
				}
			}
		}
		for (let dijkstraRunde : number = 0; dijkstraRunde < dijkstraRunden; dijkstraRunde++) {
			for (let ic : number = 0; ic < this.cols; ic++) {
				const c : number = this.permC[ic];
				this.vorgaengerCzuR[c] = -1;
				this.abgearbeitetC[c] = false;
				this.distanzC[c] = JavaLong.MAX_VALUE;
				for (let ir : number = 0; ir < this.rows; ir++) {
					const r : number = this.permR[ir];
					if (this.r2c[r] < 0) {
						const kante : number = this.matrix[r][c] + this.potentialR[r] - this.potentialC[c];
						if (kante < this.distanzC[c]) {
							this.distanzC[c] = kante;
							this.vorgaengerCzuR[c] = r;
						}
					}
				}
			}
			let endknotenC : number = -1;
			for (let dijkstraZyklus : number = 0; dijkstraZyklus < this.cols; dijkstraZyklus++) {
				let fromC : number = 0;
				for (let ic : number = 0; ic < this.cols; ic++) {
					const c : number = this.permC[ic];
					if ((!this.abgearbeitetC[c]) && ((this.abgearbeitetC[fromC]) || (this.distanzC[c] < this.distanzC[fromC])))
						fromC = c;
				}
				this.abgearbeitetC[fromC] = true;
				const overR : number = this.c2r[fromC];
				if (overR >= 0) {
					for (let ic : number = 0; ic < this.cols; ic++) {
						const toC : number = this.permC[ic];
						const kante : number = this.matrix[overR][toC] + this.potentialR[overR] - this.potentialC[toC];
						const distance : number = this.distanzC[fromC] + kante;
						if (distance < this.distanzC[toC]) {
							this.distanzC[toC] = distance;
							this.vorgaengerCzuR[toC] = overR;
						}
					}
				} else {
					if (endknotenC < 0) {
						endknotenC = fromC;
					}
				}
			}
			let currentC : number = endknotenC;
			while (currentC >= 0) {
				const prevR : number = this.vorgaengerCzuR[currentC];
				const prevC : number = this.r2c[prevR];
				this.r2c[prevR] = currentC;
				this.c2r[currentC] = prevR;
				currentC = prevC;
			}
			for (let r : number = 0; r < this.rows; r++) {
				const c : number = this.r2c[r];
				if (c >= 0) {
					const kante : number = this.matrix[r][c] + this.potentialR[r] - this.potentialC[c];
					this.potentialR[r] += this.distanzC[c] - kante;
					this.potentialC[c] += this.distanzC[c];
				}
			}
		}
		return this.r2c;
	}

	/**
	 *Interne Methode zum Permutieren oder Initialisieren der Arrays {@link KursblockungMatrix#permR} und
	 * {@link KursblockungMatrix#permC}.
	 *
	 * @param nichtdeterministisch falls {@code true} werden {@link KursblockungMatrix#permR} und
	 *                             {@link KursblockungMatrix#permC} permutiert, sonst initialisiert.
	 */
	private initialisierPermRundPermC(nichtdeterministisch : boolean) : void {
		if (nichtdeterministisch) {
			this.permutiere(this.permR);
			this.permutiere(this.permC);
		} else {
			KursblockungMatrix.initialisiere(this.permR);
			KursblockungMatrix.initialisiere(this.permC);
		}
	}

	/**
	 *Interne Methode zum Initialisieren eines Arrays so, dass das Array mit den Zahlen {@code 0,1,2...} gefüllt wird.
	 *
	 * @param perm Das Array, welches mit den Zahlen {@code 0,1,2...} gefüllt wird.
	 */
	private static initialisiere(perm : Array<number>) : void {
		const laenge : number = perm.length;
		for (let i : number = 0; i < laenge; i++) {
			perm[i] = i;
		}
	}

	/**
	 *Interne Methode zum zufälligen Permutieren eines Arrays.
	 *
	 * @param perm Das Array, dessen Inhalt zufällig permutiert wird.
	 */
	private permutiere(perm : Array<number>) : void {
		const laenge : number = perm.length;
		for (let i : number = 0; i < laenge; i++) {
			const j : number = this._random.nextInt(laenge);
			const saveI : number = perm[i];
			const saveJ : number = perm[j];
			perm[i] = saveJ;
			perm[j] = saveI;
		}
	}

	/**
	 *Erlaubt Zugriff auf den Inhalt des Arrays.
	 *
	 * @return Die Array-Referenz.
	 */
	public getMatrix() : Array<Array<number>> {
		return this.matrix;
	}

	/**
	 *Erzeugt String-Ausgabe des Arrays sowie der Zeilen-zu-Spalten-Zuordnung {@link KursblockungMatrix#r2c}. Diese
	 * Methode ist für Debug-Zwecke gedacht.
	 *
	 * @param  kommentar          Ein Kommentar der über der Matrix angezeigt wird.
	 * @param  zellenbreite       Die Breite bei der Ausgabe der Zelle.
	 * @param  mitKnotenPotential Falls {@code true}, werden die Kantenwerte umgewichtet entsprechenden der
	 *                            Knotenpotentiale, andernfalls bleiben die Kantenwerte unverändert.
	 * @return                    Eine String-Representation der Matrix.
	 */
	public convertToString(kommentar : string, zellenbreite : number, mitKnotenPotential : boolean) : string {
		const sb : StringBuilder = new StringBuilder();
		sb.append(kommentar! + System.lineSeparator()!);
		for (let r : number = 0; r < this.rows; r++) {
			for (let c : number = 0; c < this.cols; c++) {
				const wert : number = mitKnotenPotential ? this.matrix[r][c] + this.potentialR[r] - this.potentialC[c] : this.matrix[r][c];
				let sWert : string | null = "" + wert;
				while (sWert.length < zellenbreite)
					sWert = " " + sWert!;
				const sZusatz : string = this.r2c[r] === c ? "*" : " ";
				sb.append(sWert! + sZusatz!);
			}
			sb.append("\n");
		}
		sb.append("r2c = " + Arrays.toString(this.r2c)!);
		sb.append("\n");
		return sb.toString();
	}

	/**
	 * Füllt die Matrix mit ganzzahligen zufälligen Zahlenwerten aus dem Intervall {@code [von;bis]}.
	 *
	 * @param von Der kleinstmögliche zufällige Wert (inklusive).
	 * @param bis Der größtmögliche zufällige Wert (inklusive).
	 */
	public fuelleMitZufallszahlenVonBis(von : number, bis : number) : void {
		for (let r : number = 0; r < this.rows; r++) {
			for (let c : number = 0; c < this.cols; c++) {
				this.matrix[r][c] = this._random.nextLong(bis - von + 1) + von;
			}
		}
	}

	/**
	 *Liefert die Anzahl an Zeilen der Matrix.
	 *
	 * @return die Anzahl an Zeilen der Matrix.
	 */
	public gibZeilen() : number {
		return this.rows;
	}

	/**
	 *Liefert die Anzahl an Spalten der Matrix.
	 *
	 * @return die Anzahl an Spalten der Matrix.
	 */
	public gibSpalten() : number {
		return this.cols;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.kursblockung.KursblockungMatrix'].includes(name);
	}

}

export function cast_de_svws_nrw_core_kursblockung_KursblockungMatrix(obj : unknown) : KursblockungMatrix {
	return obj as KursblockungMatrix;
}
