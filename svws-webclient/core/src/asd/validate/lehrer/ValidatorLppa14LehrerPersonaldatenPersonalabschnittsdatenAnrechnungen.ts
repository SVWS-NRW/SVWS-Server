import { LehrerBeschaeftigungsart } from '../../../asd/types/lehrer/LehrerBeschaeftigungsart';
import { LehrerPersonalabschnittsdatenAnrechnungsstunden } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdatenAnrechnungsstunden';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { LehrerEinsatzstatus } from '../../../asd/types/lehrer/LehrerEinsatzstatus';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { JavaMath } from '../../../java/lang/JavaMath';
import { LehrerMehrleistungsarten } from '../../../asd/types/lehrer/LehrerMehrleistungsarten';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppa14LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen extends Validator {

	/**
	 * Die Liste der Mehrleistungsstunden.
	 */
	private readonly _mehrleistung: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>;

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
	 * @param mehrleistung           die Liste der Mehrleistungsstunden
	 * @param idBeschaeftigungsart   die ID der Beschäftigungsart
	 * @param idEinsatzstatus        die ID des Einsatzstatus
	 * @param kontext                der Kontext des Validators
	 */
	public constructor(mehrleistung: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>, idBeschaeftigungsart: Supplier<number | null>, idEinsatzstatus: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._mehrleistung = mehrleistung;
		this._idBeschaeftigungsart = idBeschaeftigungsart;
		this._idEinsatzstatus = idEinsatzstatus;
	}

	protected pruefe(): boolean {
		const listeMehrleistung: List<LehrerPersonalabschnittsdatenAnrechnungsstunden> | null = this._mehrleistung.get();
		const idBeschaeftigung: number | null = this._idBeschaeftigungsart.get();
		const idEinsatz: number | null = this._idEinsatzstatus.get();
		if ((listeMehrleistung === null) || (idBeschaeftigung === null)) {
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
		const grund100: LehrerMehrleistungsarten | null = LehrerMehrleistungsarten.data().getWertByBezeichner("ID_100");
		for (const eintrag of listeMehrleistung) {
			if (eintrag.idGrund === null) {
				continue;
			}
			const art: LehrerMehrleistungsarten | null = LehrerMehrleistungsarten.data().getWertByIDOrNull(eintrag.idGrund);
			if ((art as unknown === grund100 as unknown) && (Math.abs(eintrag.anzahl) < 0.001)) {
				this.addFehler(0, "Anrechnungsgründe des Lehrers: Den Angaben nach befindet sich die ausgewählte Lehrkraft in der 'Ansparphase' bzw. 'Phase mit erhöhter Arbeitszeit' der 'Teilzeitbeschäftigung im Blockmodell'. Bitte erfassen Sie bei der Mehrleistung 'Ansparphase, Phase mit erhöhter Arbeitszeit 'Teilzeitbeschäftigung im Blockmodell' im Feld 'Stunden' die Differenz aus der tatsächlich geleisteten Arbeitszeit und dem Pflichtstundensoll. Das Pflichtstundensoll ist in jeder Phase der Teilzeitbeschäftigung im Blockmodell gleich.");
				return false;
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppa14LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppa14LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppa14LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen>('de.svws_nrw.asd.validate.lehrer.ValidatorLppa14LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppa14LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(obj: unknown): ValidatorLppa14LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen {
	return obj as ValidatorLppa14LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen;
}
