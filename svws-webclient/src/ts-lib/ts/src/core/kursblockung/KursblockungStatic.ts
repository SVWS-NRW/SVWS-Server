import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { GostKursblockungRegelTyp, cast_de_nrw_schule_svws_core_types_kursblockung_GostKursblockungRegelTyp } from '../../core/types/kursblockung/GostKursblockungRegelTyp';
import { KursblockungInputKurs, cast_de_nrw_schule_svws_core_data_kursblockung_KursblockungInputKurs } from '../../core/data/kursblockung/KursblockungInputKurs';
import { KursblockungInput, cast_de_nrw_schule_svws_core_data_kursblockung_KursblockungInput } from '../../core/data/kursblockung/KursblockungInput';
import { Random, cast_java_util_Random } from '../../java/util/Random';
import { KursblockungInputKursart, cast_de_nrw_schule_svws_core_data_kursblockung_KursblockungInputKursart } from '../../core/data/kursblockung/KursblockungInputKursart';
import { KursblockungInputRegel, cast_de_nrw_schule_svws_core_data_kursblockung_KursblockungInputRegel } from '../../core/data/kursblockung/KursblockungInputRegel';
import { JavaLong, cast_java_lang_Long } from '../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../java/lang/JavaString';

export class KursblockungStatic extends JavaObject {


	public constructor() {
		super();
	}

	/**
	 *Erzeugt ein Array der Größe {@code n}, füllt es mit den Zahlen {@code 0 bis n-1} und permutiert das Array dann
	 * zufällig.
	 * 
	 * @param  pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param  n       Die Größe des Arrays.
	 * @return         Eine Array-Referenz. 
	 */
	public static gibPermutation(pRandom : Random, n : number) : Array<number> {
		let temp : Array<number> = Array(n).fill(0);
		for (let i : number = 0; i < n; i++){
			temp[i] = i;
		}
		KursblockungStatic.aktionPermutiere(pRandom, temp);
		return temp;
	}

	/**
	 *Permutiert das Array {@code perm} zufällig.
	 * 
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param perm    Das zu permutierende Array. 
	 */
	public static aktionPermutiere(pRandom : Random, perm : Array<number>) : void {
		let n : number = perm.length;
		for (let i1 : number = 0; i1 < n; i1++){
			let i2 : number = pRandom.nextInt(n);
			let s1 : number = perm[i1];
			let s2 : number = perm[i2];
			perm[i1] = s2;
			perm[i2] = s1;
		}
	}

	/**
	 *Erweitert die Eingabedaten {@link KursblockungInput} um eine Regel, welche bestimmte Schienen für bestimmte
	 * Kursarten sperrt.
	 * 
	 * @param pInput   Das Eingabeobjekt, welchem eine neue Regel hinzugefügt wird.
	 * @param pKursart Eine String-Darstellung der Kursart.
	 * @param pVon     Sperrung aller Schienen von (inklusive, 0-indiziert).
	 * @param pBis     Sperrung aller Schienen bis (inklusive, 0-indiziert). 
	 */
	public static aktionSperreSchieneFuerKursart(pInput : KursblockungInput, pKursart : String, pVon : number, pBis : number) : void {
		for (let i : number = 0; i < pInput.kursarten.size(); i++){
			let kursart : KursblockungInputKursart | null = pInput.kursarten.get(i);
			if (JavaObject.equalsTranspiler(kursart.representation, (pKursart))) {
				let r : KursblockungInputRegel | null = new KursblockungInputRegel();
				r.databaseID = -1;
				r.typ = GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ;
				r.daten = [kursart.id, pVon as number, pBis as number];
				pInput.regeln.add(r);
			}
		}
	}

	/**
	 *Erweitert die Eingabedaten {@link KursblockungInput} um Regeln, die bestimmte Kurse in einer Schiene fixieren,
	 * andere Kurse die Schiene verbieten.
	 * 
	 * @param pInput     Das Eingabeobjekt, welchem eine neue Regel hinzugefügt wird.
	 * @param pKursnamen Die zu fixierenden Kursnamen.
	 * @param pSchiene   Die Schiene in der die Kurse (und nur diese) liegen sollen. 
	 */
	public static aktionFixiereKurseInSchieneSonstNichts(pInput : KursblockungInput, pKursnamen : Array<String>, pSchiene : number) : void {
		for (let i : number = 0; i < pInput.kurse.size(); i++){
			let kurs : KursblockungInputKurs | null = pInput.kurse.get(i);
			let gefunden : boolean = false;
			for (let j : number = 0; j < pKursnamen.length; j++){
				if (JavaObject.equalsTranspiler(kurs.representation, (pKursnamen[j]))) {
					gefunden = true;
				}
			}
			let r : KursblockungInputRegel = new KursblockungInputRegel();
			if (gefunden) {
				r.databaseID = -1;
				r.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
				r.daten = [kurs.id, pSchiene as number];
			} else {
				r.databaseID = -1;
				r.typ = GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ;
				r.daten = [kurs.id, pSchiene as number];
			}
			pInput.regeln.add(r);
		}
	}

	/**
	 *Erweitert die Eingabedaten {@link KursblockungInput} um eine Regel, die einen bestimmten Kurs in einer
	 * bestimmten Schiene fixiert.
	 * 
	 * @param pInput    Das Eingabeobjekt, welchem eine neue Regel hinzugefügt wird.
	 * @param pKursname Der zu fixierende Kurs.
	 * @param pSchiene  Die Schiene in der der Kurs liegen sollen. 
	 */
	public static aktionFixiereKursInSchiene(pInput : KursblockungInput, pKursname : String, pSchiene : number) : void {
		for (let kurs of pInput.kurse) {
			if (JavaObject.equalsTranspiler(kurs.representation, (pKursname))) {
				let r : KursblockungInputRegel = new KursblockungInputRegel();
				r.databaseID = -1;
				r.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
				r.daten = [kurs.id, pSchiene as number];
				pInput.regeln.add(r);
			}
		}
	}

	/**
	 *Erweitert die Eingabedaten {@link KursblockungInput} um eine Regel, die einen bestimmten SchülerIn in einen
	 * bestimmten Kurs fixiert (Regel 4).
	 * 
	 * @param pInput      Das Eingabeobjekt, welchem eine neue Regel hinzugefügt wird.
	 * @param pSchuelerID Der zu fixierende Schüler.
	 * @param pKursID     Der Kurs in dem der Schüler fixiert werden soll. 
	 */
	public static aktionFixiereSchuelerInKurs(pInput : KursblockungInput, pSchuelerID : number, pKursID : number) : void {
		let r : KursblockungInputRegel = new KursblockungInputRegel();
		r.databaseID = -1;
		r.typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ;
		r.daten = [pSchuelerID, pKursID];
		pInput.regeln.add(r);
	}

	/**
	 *Erweitert die Eingabedaten {@link KursblockungInput} um eine Regel, die einen bestimmten SchülerIn in einem
	 * bestimmten Kurs verbietet (Regel 5).
	 * 
	 * @param pInput      Das Eingabeobjekt, welchem eine neue Regel hinzugefügt wird.
	 * @param pSchuelerID Der zu fixierende Schüler.
	 * @param pKursID     Der Kurs in dem der Schüler nicht sein darf. 
	 */
	public static aktionVerbieteSchuelerInKurs(pInput : KursblockungInput, pSchuelerID : number, pKursID : number) : void {
		let r : KursblockungInputRegel = new KursblockungInputRegel();
		r.databaseID = -1;
		r.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ;
		r.daten = [pSchuelerID, pKursID];
		pInput.regeln.add(r);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.kursblockung.KursblockungStatic'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_kursblockung_KursblockungStatic(obj : unknown) : KursblockungStatic {
	return obj as KursblockungStatic;
}
