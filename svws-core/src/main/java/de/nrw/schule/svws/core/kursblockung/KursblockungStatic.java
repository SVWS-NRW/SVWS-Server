package de.nrw.schule.svws.core.kursblockung;

import java.util.Random;

import de.nrw.schule.svws.core.data.kursblockung.KursblockungInput;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInputKurs;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInputKursart;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInputRegel;
import de.nrw.schule.svws.core.types.kursblockung.GostKursblockungRegelTyp;
import jakarta.validation.constraints.NotNull;

/** Diese Klasse bietet statische Helfer-Methoden an.
 * 
 * @author Benjamin A. Bartsch */
public class KursblockungStatic {

	/** Erzeugt ein Array der Größe {@code n}, füllt es mit den Zahlen {@code 0 bis n-1} und permutiert das Array dann
	 * zufällig.
	 * 
	 * @param  pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param  n       Die Größe des Arrays.
	 * @return         Eine Array-Referenz. */
	public static @NotNull int[] gibPermutation(@NotNull Random pRandom, int n) {
		@NotNull
		int[] temp = new int[n];
		for (int i = 0; i < n; i++) {
			temp[i] = i;
		}
		aktionPermutiere(pRandom, temp);
		return temp;
	}

	/** Permutiert das Array {@code perm} zufällig.
	 * 
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param perm    Das zu permutierende Array. */
	public static void aktionPermutiere(@NotNull Random pRandom, @NotNull int[] perm) {
		int n = perm.length;
		for (int i1 = 0; i1 < n; i1++) {
			int i2 = pRandom.nextInt(n);
			int s1 = perm[i1];
			int s2 = perm[i2];
			perm[i1] = s2;
			perm[i2] = s1;
		}
	}

	/** Erweitert die Eingabedaten {@link KursblockungInput} um eine Regel, welche bestimmte Schienen für bestimmte
	 * Kursarten sperrt.
	 * 
	 * @param pInput   Das Eingabeobjekt, welchem eine neue Regel hinzugefügt wird.
	 * @param pKursart Eine String-Darstellung der Kursart.
	 * @param pVon     Sperrung aller Schienen von (inklusive, 0-indiziert).
	 * @param pBis     Sperrung aller Schienen bis (inklusive, 0-indiziert). */
	public static void aktionSperreSchieneFuerKursart(@NotNull KursblockungInput pInput, @NotNull String pKursart,
			int pVon, int pBis) {

		for (int i = 0; i < pInput.kursarten.size(); i++) {
			KursblockungInputKursart kursart = pInput.kursarten.get(i);
			if (kursart.representation.equals(pKursart)) {
				KursblockungInputRegel r = new KursblockungInputRegel();
				r.databaseID = -1; // Dummy-Wert
				r.typ = GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ;
				r.daten = new Long[] { kursart.id, (long) pVon, (long) pBis };
				pInput.regeln.add(r);
			}
		}

	}

	/** Erweitert die Eingabedaten {@link KursblockungInput} um Regeln, die bestimmte Kurse in einer Schiene fixieren,
	 * andere Kurse die Schiene verbieten.
	 * 
	 * @param pInput     Das Eingabeobjekt, welchem eine neue Regel hinzugefügt wird.
	 * @param pKursnamen Die zu fixierenden Kursnamen.
	 * @param pSchiene   Die Schiene in der die Kurse (und nur diese) liegen sollen. */
	public static void aktionFixiereKurseInSchieneSonstNichts(@NotNull KursblockungInput pInput,
			@NotNull String @NotNull [] pKursnamen, int pSchiene) {
		for (int i = 0; i < pInput.kurse.size(); i++) {
			KursblockungInputKurs kurs = pInput.kurse.get(i);

			boolean gefunden = false;
			for (int j = 0; j < pKursnamen.length; j++) {
				if (kurs.representation.equals(pKursnamen[j])) {
					gefunden = true;
				}
			}

			@NotNull
			KursblockungInputRegel r = new KursblockungInputRegel();
			if (gefunden) {
				r.databaseID = -1;// Dummy
				r.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
				r.daten = new Long[] { kurs.id, (long) pSchiene };
			} else {
				r.databaseID = -1;// Dummy
				r.typ = GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ;
				r.daten = new Long[] { kurs.id, (long) pSchiene };
			}
			pInput.regeln.add(r);
		}
	}

	/** Erweitert die Eingabedaten {@link KursblockungInput} um eine Regel, die einen bestimmten Kurs in einer
	 * bestimmten Schiene fixiert.
	 * 
	 * @param pInput    Das Eingabeobjekt, welchem eine neue Regel hinzugefügt wird.
	 * @param pKursname Der zu fixierende Kurs.
	 * @param pSchiene  Die Schiene in der der Kurs liegen sollen. */
	public static void aktionFixiereKursInSchiene(@NotNull KursblockungInput pInput, @NotNull String pKursname,
			int pSchiene) {
		for (KursblockungInputKurs kurs : pInput.kurse) {
			if (kurs.representation.equals(pKursname)) {
				@NotNull
				KursblockungInputRegel r = new KursblockungInputRegel();
				r.databaseID = -1; // Dummy-Wert
				r.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
				r.daten = new Long[] { kurs.id, (long) pSchiene };
				pInput.regeln.add(r);
			}

		}
	}

	/** Erweitert die Eingabedaten {@link KursblockungInput} um eine Regel, die einen bestimmten SchülerIn in einen
	 * bestimmten Kurs fixiert (Regel 4).
	 * 
	 * @param pInput      Das Eingabeobjekt, welchem eine neue Regel hinzugefügt wird.
	 * @param pSchuelerID Der zu fixierende Schüler.
	 * @param pKursID     Der Kurs in dem der Schüler fixiert werden soll. */
	public static void aktionFixiereSchuelerInKurs(@NotNull KursblockungInput pInput, long pSchuelerID, long pKursID) {
		@NotNull
		KursblockungInputRegel r = new KursblockungInputRegel();
		r.databaseID = -1; // Dummy-Wert
		r.typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ;
		r.daten = new Long[] { pSchuelerID, pKursID };
		pInput.regeln.add(r);
	}

	/** Erweitert die Eingabedaten {@link KursblockungInput} um eine Regel, die einen bestimmten SchülerIn in einem
	 * bestimmten Kurs verbietet (Regel 5).
	 * 
	 * @param pInput      Das Eingabeobjekt, welchem eine neue Regel hinzugefügt wird.
	 * @param pSchuelerID Der zu fixierende Schüler.
	 * @param pKursID     Der Kurs in dem der Schüler nicht sein darf. */
	public static void aktionVerbieteSchuelerInKurs(@NotNull KursblockungInput pInput, long pSchuelerID, long pKursID) {
		@NotNull
		KursblockungInputRegel r = new KursblockungInputRegel();
		r.databaseID = -1; // Dummy-Wert
		r.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ;
		r.daten = new Long[] { pSchuelerID, pKursID };
		pInput.regeln.add(r);
	}

}
