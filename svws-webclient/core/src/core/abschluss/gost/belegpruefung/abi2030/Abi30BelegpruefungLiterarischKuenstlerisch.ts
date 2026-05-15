import { GostFachbereich } from '../../../../../core/types/gost/GostFachbereich';
import { AbiturFachbelegung } from '../../../../../core/data/gost/AbiturFachbelegung';
import { GostBelegpruefungsArt } from '../../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { GostHalbjahr } from '../../../../../core/types/gost/GostHalbjahr';
import type { List } from '../../../../../java/util/List';
import { Class } from '../../../../../java/lang/Class';
import { GostBelegpruefung } from '../../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager } from '../../../../../core/abschluss/gost/AbiturdatenManager';
import { GostBelegungsfehler } from '../../../../../core/abschluss/gost/GostBelegungsfehler';

export class Abi30BelegpruefungLiterarischKuenstlerisch extends GostBelegpruefung {

	/**
	 * Die Belegungen für Kunst und Musik.
	 */
	private kunst_musik: List<AbiturFachbelegung> | null = null;

	/**
	 * Die Belegungen für die Ersatzfächer aus dem literarisch-künstlerischen Bereich.
	 */
	private literatur: List<AbiturFachbelegung> | null = null;


	/**
	 * Erstellt eine neue Belegprüfung für den literarisch-künstlerischen Bereich.
	 *
	 * @param manager        der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public constructor(manager: AbiturdatenManager, pruefungsArt: GostBelegpruefungsArt) {
		super(manager, pruefungsArt);
	}

	protected init(): void {
		this.kunst_musik = this.manager.getRelevanteFachbelegungen(GostFachbereich.KUNST_MUSIK);
		this.literatur = this.manager.getRelevanteFachbelegungen(GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ);
	}

	protected pruefeEF1(): void {
		if (this.manager.zaehleBelegungInHalbjahren(this.kunst_musik, GostHalbjahr.EF1) === 0) {
			this.addFehler(GostBelegungsfehler.GOST30_KU_MU_10);
		}
	}

	/**
	 * Gesamtprüfung Punkte 26-28:
	 * Prüfe, ob ein Kurs in Kunst oder Musik mindestens von EF.1 bis Q1.2 belegt wurde
	 *   oder ob das Ersatzfach Literatur in der Qualifikationsphase gültig belegt wurde
	 */
	protected pruefeGesamt(): void {
		let hatLi: boolean = false;
		if (this.literatur !== null) {
			for (const fach of this.literatur) {
				hatLi = hatLi || (this.manager.pruefeBelegung(fach, GostHalbjahr.Q11, GostHalbjahr.Q12) || this.manager.pruefeBelegung(fach, GostHalbjahr.Q12, GostHalbjahr.Q21) || this.manager.pruefeBelegung(fach, GostHalbjahr.Q21, GostHalbjahr.Q22));
			}
		}
		const hatKuMuBisQ12: boolean = this.manager.pruefeBelegungExistiert(this.kunst_musik, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12);
		const hatKuMuBisEF2: boolean = this.manager.pruefeBelegungExistiert(this.kunst_musik, GostHalbjahr.EF1, GostHalbjahr.EF2);
		if ((!hatKuMuBisEF2) || ((!hatKuMuBisQ12) && (!hatLi))) {
			this.addFehler(GostBelegungsfehler.GOST30_KU_MU_10);
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungLiterarischKuenstlerisch';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungLiterarischKuenstlerisch', 'de.svws_nrw.core.abschluss.gost.GostBelegpruefung'].includes(name);
	}

	public static readonly class = new Class<Abi30BelegpruefungLiterarischKuenstlerisch>('de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungLiterarischKuenstlerisch');

}

export function cast_de_svws_nrw_core_abschluss_gost_belegpruefung_abi2030_Abi30BelegpruefungLiterarischKuenstlerisch(obj: unknown): Abi30BelegpruefungLiterarischKuenstlerisch {
	return obj as Abi30BelegpruefungLiterarischKuenstlerisch;
}
