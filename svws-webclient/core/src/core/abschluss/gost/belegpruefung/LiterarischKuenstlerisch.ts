import { GostFachbereich } from '../../../../core/types/gost/GostFachbereich';
import { AbiturFachbelegung } from '../../../../core/data/gost/AbiturFachbelegung';
import { GostBelegpruefungsArt } from '../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { GostHalbjahr } from '../../../../core/types/gost/GostHalbjahr';
import type { List } from '../../../../java/util/List';
import { GostBelegpruefung } from '../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager } from '../../../../core/abschluss/gost/AbiturdatenManager';
import { GostBelegungsfehler } from '../../../../core/abschluss/gost/GostBelegungsfehler';

export class LiterarischKuenstlerisch extends GostBelegpruefung {

	private kunst_musik : List<AbiturFachbelegung> | null = null;

	private kunst_musik_ersatz : List<AbiturFachbelegung> | null = null;


	/**
	 * Erstellt eine neue Belegprüfung für den literarisch-künstlerischen Bereich.
	 *
	 * @param manager        der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public constructor(manager : AbiturdatenManager, pruefungsArt : GostBelegpruefungsArt) {
		super(manager, pruefungsArt);
	}

	protected init() : void {
		this.kunst_musik = this.manager.getRelevanteFachbelegungen(GostFachbereich.KUNST_MUSIK);
		this.kunst_musik_ersatz = this.manager.getRelevanteFachbelegungen(GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ);
	}

	protected pruefeEF1() : void {
		if (this.manager.zaehleBelegungInHalbjahren(this.kunst_musik, GostHalbjahr.EF1) === 0)
			this.addFehler(GostBelegungsfehler.KU_MU_10);
	}

	/**
	 * Gesamtprüfung Punkte 26-28:
	 * Prüfe, ob ein Kurs in Kunst oder Musik mindestens von EF.1 bis Q1.2 belegt wurde
	 *   oder ob ein Ersatzfach (Literatur, vokal- oder instrumentalpraktischer Grundkurs) in der
	 *           Qualifikationsphase gültig belegt wurde
	 */
	protected pruefeGesamt() : void {
		let hatKuMuErsatz : boolean = false;
		if (this.kunst_musik_ersatz !== null) {
			for (const fach of this.kunst_musik_ersatz) {
				const tmpHatKuMuErsatz : boolean = (this.manager.pruefeBelegung(fach, GostHalbjahr.Q11, GostHalbjahr.Q12) || this.manager.pruefeBelegung(fach, GostHalbjahr.Q12, GostHalbjahr.Q21) || this.manager.pruefeBelegung(fach, GostHalbjahr.Q21, GostHalbjahr.Q22));
				hatKuMuErsatz = hatKuMuErsatz || tmpHatKuMuErsatz;
				if ((!tmpHatKuMuErsatz) || (this.manager.zaehleBelegung(fach) !== 2))
					this.addFehler(GostBelegungsfehler.LI_IV_10);
			}
			if (this.kunst_musik_ersatz.size() > 1)
				this.addFehler(GostBelegungsfehler.LI_IV_11);
		}
		const hatKuMuBisQ12 : boolean = this.manager.pruefeBelegungExistiert(this.kunst_musik, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12);
		const hatKuMuBisEF2 : boolean = this.manager.pruefeBelegungExistiert(this.kunst_musik, GostHalbjahr.EF1, GostHalbjahr.EF2);
		if ((!hatKuMuBisEF2) || ((!hatKuMuBisQ12) && (!hatKuMuErsatz)))
			this.addFehler(GostBelegungsfehler.KU_MU_10);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.gost.belegpruefung.LiterarischKuenstlerisch';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.GostBelegpruefung', 'de.svws_nrw.core.abschluss.gost.belegpruefung.LiterarischKuenstlerisch'].includes(name);
	}

}

export function cast_de_svws_nrw_core_abschluss_gost_belegpruefung_LiterarischKuenstlerisch(obj : unknown) : LiterarischKuenstlerisch {
	return obj as LiterarischKuenstlerisch;
}
