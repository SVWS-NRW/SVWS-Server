import type { LehrerBeschaeftigungsartKatalogEintrag } from "@core/asd/data/lehrer/LehrerBeschaeftigungsartKatalogEintrag";
import type { LehrerEinsatzstatusKatalogEintrag } from "@core/asd/data/lehrer/LehrerEinsatzstatusKatalogEintrag";
import { LehrerPersonalabschnittsdaten } from "@core/asd/data/lehrer/LehrerPersonalabschnittsdaten";
import type { LehrerRechtsverhaeltnisKatalogEintrag } from "@core/asd/data/lehrer/LehrerRechtsverhaeltnisKatalogEintrag";
import { LehrerBeschaeftigungsart } from "@core/asd/types/lehrer/LehrerBeschaeftigungsart";
import { LehrerEinsatzstatus } from "@core/asd/types/lehrer/LehrerEinsatzstatus";
import { LehrerRechtsverhaeltnis } from "@core/asd/types/lehrer/LehrerRechtsverhaeltnis";
import { DateManager } from "@core/asd/validate/DateManager";
import { ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart } from "@core/asd/validate/lehrer/ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart";
import { ValidatorLpppLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll } from "@core/asd/validate/lehrer/ValidatorLpppLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll";
import { ValidatorLpprLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis } from "@core/asd/validate/lehrer/ValidatorLpprLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis";
import type { ValidatorKontext } from "@core/asd/validate/ValidatorKontext";
import { ModelProxy } from "@ui/model/ModelProxy";
import type { LehrerListeManager } from "@ui/ui/manager/lehrer/LehrerListeManager";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorNumberRange } from "@ui/validation/common/ValidatorNumberRange";
import { computed } from "vue";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";

/**
 * Der spezielle ModelProxy für die Lehrerpersonalabschnittsdaten
 */
export class LehrerPersonalabschnittsdatenModelProxy extends ModelProxy<LehrerPersonalabschnittsdaten> {

	protected readonly manager: () => LehrerListeManager;

	/**
	 * Erstellt einen ModelProxy für das Core-DTO LehrerIndividualdaten.
	 *
	 * @param data               Zugriff auf die "Original"-Daten
	 * @param validatorKontext   der Validator-Kontext für die Nutzung in den ASD-Validatoren
	 * @param manager            Manager der Lehrerliste
	 * @param patch              ggf. die Methode zum Patchen der einzelnen Attribute, sofern das automatische Patchen
	 *                           bei Änderungen gewünscht ist
	 */
	constructor(data: () => LehrerPersonalabschnittsdaten | null, validatorKontext: () => ValidatorKontext, manager: () => LehrerListeManager,
		patch?: (data: Partial<LehrerPersonalabschnittsdaten>) => Promise<boolean>) {
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
		this.addValidatoren(validatorKontext);
		this.validate();
	}

	private addValidatoren(validatorKontext: () => ValidatorKontext) {
		// Rechtsverhältnis
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.idRechtsverhaeltnis), "idRechtsverhaeltnis");
		const geburtsDatum: DateManager = DateManager.from(this.manager().daten().geburtsdatum); // Geburtsdatum aus Stammdaten holen
		this.addValidator(new ValidatorLpprLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(
			{ get: () => this.proxy.idSchuljahresabschnitt },
			{ get: () => this.manager().daten().idStaatsangehoerigkeit },
			{ get: () => this.proxy.idRechtsverhaeltnis },
			{ get: () => geburtsDatum },
			validatorKontext()), "idRechtsverhaeltnis", "pflichtstundensoll");

		// Beschäftigungsart
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.idBeschaeftigungsart), "idBeschaeftigungsart");
		this.addValidator(new ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(
			{ get: () => this.proxy.idBeschaeftigungsart ?? -1 },
			{ get: () => this.proxy.idEinsatzstatus ?? -1 },
			{ get: () => this.proxy.pflichtstundensoll },
			validatorKontext()), "idBeschaeftigungsart");

		// Pflichtstundensoll
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.pflichtstundensoll, 0, null), "pflichtstundensoll");
		this.addValidator(new ValidatorLpppLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(
			{ get: () => this.proxy.pflichtstundensoll },
			{ get: () => this.proxy.idEinsatzstatus },
			{ get: () => this.proxy.idBeschaeftigungsart },
			validatorKontext()), "pflichtstundensoll");

		// Einsatzstatus
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.idEinsatzstatus), "idEinsatzstatus");

		// Stammschule
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.stammschulnummer), "stammschulnummer");

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
