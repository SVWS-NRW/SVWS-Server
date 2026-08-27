import type { ValidatorKontext, LehrerLehramtEintrag, LehrerRechtsverhaeltnisKatalogEintrag, LehrerEinsatzstatusKatalogEintrag, LehrerBeschaeftigungsartKatalogEintrag } from "@core";
import { ArrayList, LehrerBeschaeftigungsart, LehrerEinsatzstatus, LehrerPersonalabschnittsdaten, LehrerRechtsverhaeltnis, ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart, ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten, ValidatorLpppLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll } from "@core";
import { ModelProxy, type LehrerListeManager } from "@ui";
import { computed } from "vue";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";

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
			pseudo.idSchuljahresabschnitt = abschnittStateImpl.auswahl.id;
			return pseudo;
		};
		const listOfAutopatchProps: Iterable<keyof LehrerPersonalabschnittsdaten> = ["idRechtsverhaeltnis", "idBeschaeftigungsart", "idEinsatzstatus", "stammschulnummer"];
		super({ data: daten, patch, listOfAutopatchProps });

		this.manager = manager;

		this.addValidator(new ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten(
			{ get: () => this.proxy.idSchuljahresabschnitt },
			{ get: () => manager().daten().idStaatsangehoerigkeit },
			{ get: () => this.proxy.idRechtsverhaeltnis },
			{ get: () => this.proxy.pflichtstundensoll },
			{ get: () => this.proxy.anrechnungen },
			{ get: () => this.proxy.idEinsatzstatus },
			{ get: () => this.proxy.idBeschaeftigungsart },
			{ get: () => manager().daten().geburtsdatum }, // Geburtsdatum aus Stammdaten holen
			{ get: () => manager().hasPersonalDaten() ? manager().personalDaten().lehraemter : new ArrayList<LehrerLehramtEintrag>() },
			{ get: () => this.proxy.mehrleistung }, // Listenfeld im DTO heißt 'mehrleistung' (Singular)
			{ get: () => this.proxy.minderleistung }, // Listenfeld im DTO heißt 'minderleistung' (Singular)
			validatorKontext()), "idRechtsverhaeltnis");
		this.addValidator(new ValidatorLpppLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(
			{ get: () => this.proxy.pflichtstundensoll },
			{ get: () => this.proxy.idEinsatzstatus },
			{ get: () => this.proxy.idBeschaeftigungsart },
			validatorKontext()), "pflichtstundensoll");
		this.addValidator(new ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(
			{ get: () => this.proxy.idBeschaeftigungsart ?? -1 },
			{ get: () => this.proxy.idEinsatzstatus ?? -1 },
			{ get: () => this.proxy.pflichtstundensoll },
			validatorKontext()), "idBeschaeftigungsart");
		this.validate();
	}

	rechtsverhaeltnis = computed<LehrerRechtsverhaeltnisKatalogEintrag | null>({
		get: () => LehrerRechtsverhaeltnis.values().map(r => r.daten(schuleStateImpl.schuljahr) ?? undefined)
			.find(d => d?.id === this.proxy.idRechtsverhaeltnis) ?? null,
		set: (value) => this.proxy.idRechtsverhaeltnis = value?.id ?? null,
	});

	einsatzstatus = computed<LehrerEinsatzstatusKatalogEintrag | null>({
		get: () => (this.proxy.idEinsatzstatus === null) ? null : LehrerEinsatzstatus.data().getEintragByID(this.proxy.idEinsatzstatus),
		set: (value: LehrerEinsatzstatusKatalogEintrag | null) => this.proxy.idEinsatzstatus = value?.id ?? null,
	});

	beschaeftigungsart = computed<LehrerBeschaeftigungsartKatalogEintrag | null>({
		get: () => (this.proxy.idBeschaeftigungsart === null) ? null : LehrerBeschaeftigungsart.data().getEintragByID(this.proxy.idBeschaeftigungsart),
		set: (value: LehrerBeschaeftigungsartKatalogEintrag | null) => this.proxy.idBeschaeftigungsart = value?.id ?? null,
	});

}
