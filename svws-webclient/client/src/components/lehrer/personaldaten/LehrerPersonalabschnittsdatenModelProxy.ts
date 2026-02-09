import type { ValidatorKontext, LehrerRechtsverhaeltnisKatalogEintrag } from "@core";
import { LehrerPersonalabschnittsdaten, LehrerRechtsverhaeltnis, ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten } from "@core";
import { ModelProxy, type LehrerListeManager } from "@ui";
import { computed } from "vue";

/**
 * Der spezielle ModelProxy für die Lehrerstammdaten
 */
export class LehrerPersonalabschnittsdatenModelProxy extends ModelProxy<LehrerPersonalabschnittsdaten> {

	protected readonly manager: () => LehrerListeManager;

	/* ;
	 * Erstellt einen ModelProxy für das Core-DTO LehrerIndividualdaten.
	 *
	 * @param data               ein Lambda für den Zugriff auf die "Original"-Daten
	 * @param validatorKontext   der Validator-Kontext für die Nutzung in den ASD-Validatoren
	 * @param patchMethod        ggf. die Methode zum Patchen der einzelnen Attribute, sofern das automatische Patchen
	 *                           bei Änderungen gewünscht ist
	 */
	constructor(data: () => LehrerPersonalabschnittsdaten | null, validatorKontext: () => ValidatorKontext, manager: () => LehrerListeManager, patch: (data: Partial<LehrerPersonalabschnittsdaten>) => Promise<boolean>) {
		const daten = () => {
			const tmp = data();
			if (tmp !== null) {
				return tmp;
			}
			// Erstelle Pseudo-Daten, die für die Validierung genutzt werden
			const pseudo = new LehrerPersonalabschnittsdaten();
			pseudo.id = -1;
			pseudo.idLehrer = manager().auswahl().id;
			pseudo.idSchuljahresabschnitt = manager().getSchuljahresabschnittAuswahl()?.id ?? -1;
			return pseudo;
		};
		super({ data: daten, patch });

		this.manager = manager;

		this.addValidator(new ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten(
			{ get: () => this.proxy.idSchuljahresabschnitt },
			{ get: () => this.proxy.rechtsverhaeltnis },
			{ get: () => this.proxy.pflichtstundensoll },
			{ get: () => this.proxy.einsatzstatus },
			{ get: () => this.proxy.beschaeftigungsart },
			{ get: () => manager().daten().geburtsdatum }, // Geburtsdatum aus Stammdaten holen
			{ get: () => this.proxy.mehrleistung }, // Listenfeld im DTO heißt 'mehrleistung' (Singular)
			{ get: () => this.proxy.minderleistung }, // Listenfeld im DTO heißt 'minderleistung' (Singular)
			validatorKontext()), "rechtsverhaeltnis");
		this.validate();
	}

	rechtsverhaeltnis = computed<LehrerRechtsverhaeltnisKatalogEintrag | undefined>({
		get: () => LehrerRechtsverhaeltnis.values().map(r => r.daten(this.manager().getSchuljahr()) ?? undefined)
			.find(d => d?.schluessel === this.proxy.rechtsverhaeltnis),
		set: (val) => this.proxy.rechtsverhaeltnis = val?.schluessel ?? null,
	});

}
