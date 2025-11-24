import { JavaObject } from '../../../java/lang/JavaObject';
import { LehrerBeschaeftigungsart } from '../../../asd/types/lehrer/LehrerBeschaeftigungsart';
import { LehrerPersonalabschnittsdaten } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdaten';
import { LehrerPersonalabschnittsdatenAnrechnungsstunden } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdatenAnrechnungsstunden';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { LehrerEinsatzstatus } from '../../../asd/types/lehrer/LehrerEinsatzstatus';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppbb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell extends Validator {

	/**
	 * Die Lehrer-Personalabschnittsdaten, die geprüft werden.
	 */
	private readonly daten: LehrerPersonalabschnittsdaten;


	/**
	 * Erstellt einen neuen Validator.
	 *
	 * @param daten    die Abschnittsdaten der Lehrkraft
	 * @param kontext  der Kontext der Validierung
	 */
	public constructor(daten: LehrerPersonalabschnittsdaten, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
	}

	/**
	 * Prüft, ob eine Liste von Anrechnungsstunden einen bestimmten Grund enthält.
	 *
	 * @param liste    die zu prüfende Liste (kann {@code null} sein)
	 * @param idGrund  die gesuchte Grund-ID
	 *
	 * @return {@code true}, wenn ein Eintrag mit {@code idGrund} enthalten ist; sonst {@code false}
	 */
	private static hatGrund(liste: List<LehrerPersonalabschnittsdatenAnrechnungsstunden> | null, idGrund: number): boolean {
		if (liste === null || liste.isEmpty())
			return false;
		for (const lpa of liste)
			if (lpa !== null && lpa.idGrund === idGrund)
				return true;
		return false;
	}

	/**
	 * Prüft die Regel für Teilzeit im Blockmodell (Beschäftigungsart = "TS"):
	 * <p>
	 * Gilt nur, wenn das Pflichtstundensoll > 0 ist und der Einsatzstatus
	 * " " (Leerzeichen) oder "A" lautet. Die Prüfung ist erfüllt, wenn
	 * mindestens einer der folgenden Gründe gesetzt ist:
	 * <ul>
	 *   <li>Mehrleistung: 100</li>
	 *   <li>Minderleistung: 240 oder 290</li>
	 * </ul>
	 *
	 * @return {@code true}, wenn die Regel erfüllt oder nicht anwendbar ist, sonst {@code false}
	 */
	protected pruefe(): boolean {
		const pss: number | null = this.daten.pflichtstundensoll;
		if (pss === null || pss <= 0.0)
			return true;
		const ba: string | null = this.daten.beschaeftigungsart === null ? "" : this.daten.beschaeftigungsart.trim();
		if (LehrerBeschaeftigungsart.data().getWertBySchluessel(ba) as unknown !== LehrerBeschaeftigungsart.TS as unknown)
			return true;
		const es: string | null = this.daten.einsatzstatus === null ? "" : this.daten.einsatzstatus;
		if (!JavaObject.equalsTranspiler("", (es.trim())) && LehrerEinsatzstatus.data().getWertBySchluessel(es) as unknown !== LehrerEinsatzstatus.A as unknown)
			return true;
		const hatMehr100: boolean = ValidatorLppbb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell.hatGrund(this.daten.mehrleistung, 100);
		const hatMinder240: boolean = ValidatorLppbb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell.hatGrund(this.daten.minderleistung, 240);
		const hatMinder290: boolean = ValidatorLppbb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell.hatGrund(this.daten.minderleistung, 290);
		const hatMehrMinderGrund: boolean = hatMehr100 || hatMinder240 || hatMinder290;
		const fehlertext: string | null = "\"Bei einer Lehrkraft mit 'Beschäftigungsart' = TS (Teilzeitbeschäftigung im Blockmodell) muss entweder der Mehrleistungsgrund '100' Ansparphase, Phase mit erhöhter Arbeitszeit \"Teilzeitbeschäftigung im Blockmodell\" (§ 65 LBG) (vormals Sabbatjahr) oder der Minderleistungsgrund '290' (Ermäßigungs-/Freistellungsphase 'Teilzeitbeschäftigung im Blockmodell') eingetragen sein.\"))";
		if (!hatMehrMinderGrund) {
			this.addFehler(1, fehlertext);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppbb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppbb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorLppbb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell>('de.svws_nrw.asd.validate.lehrer.ValidatorLppbb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppbb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell(obj: unknown): ValidatorLppbb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell {
	return obj as ValidatorLppbb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell;
}
