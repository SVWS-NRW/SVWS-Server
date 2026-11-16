import { LehrerBeschaeftigungsart } from '../../../asd/types/lehrer/LehrerBeschaeftigungsart';
import { LehrerMinderleistungsarten } from '../../../asd/types/lehrer/LehrerMinderleistungsarten';
import { LehrerPersonalabschnittsdatenAnrechnungsstunden } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdatenAnrechnungsstunden';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { LehrerEinsatzstatus } from '../../../asd/types/lehrer/LehrerEinsatzstatus';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { JavaMath } from '../../../java/lang/JavaMath';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppa15LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen extends Validator {

	/**
	 * Die Liste der Minderleistungsstunden.
	 */
	private readonly _minderleistung: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>;

	/**
	 * Die Beschäftigungsart der Lehrkraft.
	 */
	private readonly _idBeschaeftigungsart: Supplier<number | null>;

	/**
	 * Der Einsatzstatus der Lehrkraft.
	 */
	private readonly _idEinsatzstatus: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param minderleistung         die Liste der Minderleistungsstunden
	 * @param idBeschaeftigungsart   die ID der Beschäftigungsart
	 * @param idEinsatzstatus        die ID des Einsatzstatus
	 * @param kontext                der Kontext des Validators
	 */
	public constructor(minderleistung: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>, idBeschaeftigungsart: Supplier<number | null>, idEinsatzstatus: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._minderleistung = minderleistung;
		this._idBeschaeftigungsart = idBeschaeftigungsart;
		this._idEinsatzstatus = idEinsatzstatus;
	}

	protected pruefe(): boolean {
		const listeMinderleistung: List<LehrerPersonalabschnittsdatenAnrechnungsstunden> | null = this._minderleistung.get();
		const idBeschaeftigung: number | null = this._idBeschaeftigungsart.get();
		const idEinsatz: number | null = this._idEinsatzstatus.get();
		if ((listeMinderleistung === null) || (idBeschaeftigung === null)) {
			return true;
		}
		const beschaeftigung: LehrerBeschaeftigungsart | null = LehrerBeschaeftigungsart.data().getWertByIDOrNull(idBeschaeftigung);
		if (beschaeftigung as unknown !== LehrerBeschaeftigungsart.TS as unknown) {
			return true;
		}
		const einsatz: LehrerEinsatzstatus | null = (idEinsatz === null) ? null : LehrerEinsatzstatus.data().getWertByIDOrNull(idEinsatz);
		if ((idEinsatz !== null) && (einsatz as unknown !== LehrerEinsatzstatus.A as unknown)) {
			return true;
		}
		const grund290: LehrerMinderleistungsarten | null = LehrerMinderleistungsarten.data().getWertByBezeichner("ID_290");
		for (const eintrag of listeMinderleistung) {
			if (eintrag.idGrund === null) {
				continue;
			}
			const art: LehrerMinderleistungsarten | null = LehrerMinderleistungsarten.data().getWertByIDOrNull(eintrag.idGrund);
			if ((art as unknown === grund290 as unknown) && (Math.abs(eintrag.anzahl) < 0.001)) {
				this.addFehler(0, "Anrechnungsgründe des Lehrers: Den Angaben nach befindet sich die ausgewählte Lehrkraft in der 'Ermäßigungs-' bzw. 'Freistellungsphase' der 'Teilzeitbeschäftigung im Blockmodell'. Bitte erfassen Sie bei der Minderleistung 'Ermäßigungs-/Freistellungsphase 'Teilzeitbeschäftigung im Blockmodell' im Feld 'Stunden' die Differenz aus dem Pflichtstundensoll und der tatsächlich geleisteten Arbeitszeit. Das Pflichtstundensoll ist in jeder Phase der Teilzeitbeschäftigung im Blockmodell gleich.");
				return false;
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppa15LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppa15LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppa15LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen>('de.svws_nrw.asd.validate.lehrer.ValidatorLppa15LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppa15LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(obj: unknown): ValidatorLppa15LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen {
	return obj as ValidatorLppa15LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen;
}
