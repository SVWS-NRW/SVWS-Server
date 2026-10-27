import { computed } from "vue";

import type { KlassenListeEintrag } from "@core/asd/data/klassen/KlassenListeEintrag";
import type { EinschulungsartKatalogEintrag } from "@core/asd/data/schueler/EinschulungsartKatalogEintrag";
import type { SchuelerNeu } from "@core/asd/data/schueler/SchuelerNeu";
import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import { Geschlecht } from "@core/asd/types/Geschlecht";
import { Einschulungsart } from "@core/asd/types/schueler/Einschulungsart";
import { ValidatorSsdSchuelerStammdatenGeburtsdatum } from "@core/asd/validate/schueler/ValidatorSsdSchuelerStammdatenGeburtsdatum";
import { ValidatorSsgSchuelerStammdatenGeschlecht } from "@core/asd/validate/schueler/ValidatorSsgSchuelerStammdatenGeschlecht";
import { ValidatorSsnSchuelerStammdatenNachname } from "@core/asd/validate/schueler/ValidatorSsnSchuelerStammdatenNachname";
import { ValidatorSsvSchuelerStammdatenVorname } from "@core/asd/validate/schueler/ValidatorSsvSchuelerStammdatenVorname";
import type { ValidatorKontext } from "@core/asd/validate/ValidatorKontext";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import type { ReligionEintrag } from "@core/core/data/schule/ReligionEintrag";
import { ModelProxy } from "@ui/model/ModelProxy";
import { useJahrgaengeState } from "@ui/states/kataloge/JahrgaengeState";
import { useReligionenState } from "@ui/states/kataloge/ReligionenState";
import type { SchuelerNeuManager } from "@ui/ui/manager/schueler/SchuelerNeuManager";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorSchuelerGeburtsdatum } from "@ui/validation/ValidatorSchuelerGeburtsdatum";

import { ValidatorSchuelerAlleVornamen } from "../../individualdaten/modelproxy/validation/ValidatorSchuelerAlleVornamen";
import { ValidatorSchuelerNachname } from "../../individualdaten/modelproxy/validation/ValidatorSchuelerNachname";
import { ValidatorSchuelerVorname } from "../../individualdaten/modelproxy/validation/ValidatorSchuelerVorname";

export class SchuelerNeuModelProxy extends ModelProxy<SchuelerNeu> {

	private readonly _religionenState = useReligionenState();
	private readonly _jahrgaengeState = useJahrgaengeState();
	private readonly _manager: () => SchuelerNeuManager;

	/**
	 * ModelProxy für die Schüler-Neuanlage.
	 *
	 * @param data      		 Lambda für den Zugriff auf die Original-Daten
	 * @param validatorKontext   Kontext für die ASD-Validatoren
	 * @param manager   		 Lambda für den Zugriff auf den SchuelerNeuManager
	 */
	constructor(
		data: () => SchuelerNeu,
		validatorKontext: () => ValidatorKontext,
		manager: () => SchuelerNeuManager
	) {
		super({ data });
		this._manager = manager;
		this.addAsdValidatoren(validatorKontext());
		this.addUiValidatoren();
		this.validate();
	}

	private addAsdValidatoren(vk: ValidatorKontext) {
		this.addValidator(new ValidatorSsnSchuelerStammdatenNachname({ get: () => this.proxy.nachname }, vk), "nachname");
		this.addValidator(new ValidatorSsvSchuelerStammdatenVorname({ get: () => this.proxy.vorname }, vk), "vorname");
		this.addValidator(new ValidatorSsgSchuelerStammdatenGeschlecht({ get: () => this.proxy.geschlecht }, vk), "geschlecht");
		this.addValidator(new ValidatorSsdSchuelerStammdatenGeburtsdatum({ get: () => this.proxy.geburtsdatum }, vk), "geburtsdatum");
	}

	private addUiValidatoren() {
		// Schuljahresabschnitt
		this.addValidator(new ValidatorInputRequired(() => this.schuljahresabschnitt.value), "idSchuljahresabschnitt");

		// Jahrgang
		this.addValidator(new ValidatorInputRequired(() => this.proxy.idJahrgang), "idJahrgang");

		// Nachname
		this.addBlockingValidator(new ValidatorSchuelerNachname(() => this.proxy), "nachname");

		// Rufname (vorname)
		this.addBlockingValidator(new ValidatorSchuelerVorname(() => this.proxy), "vorname");

		// Alle Vornamen
		this.addBlockingValidator(new ValidatorSchuelerAlleVornamen(() => this.proxy), "alleVornamen");

		// Geschlecht
		this.addBlockingValidator(new ValidatorInputRequired(() => this.geschlecht.value), "geschlecht");

		// Geburtsdatum
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.geburtsdatum), "geburtsdatum");
		this.addBlockingValidator(new ValidatorSchuelerGeburtsdatum(() => this.proxy.geburtsdatum, () => null, false), "geburtsdatum");

		// Konfession
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.idReligion), "idReligion");

	}

	schuljahresabschnitt = computed<Schuljahresabschnitt | null>({
		get: () => this._manager().schuljahresabschnitteFilteredById.get(this.proxy.idSchuljahresabschnitt) ?? null,
		set: (value: Schuljahresabschnitt | null) => {
			this.proxy.idSchuljahresabschnitt = value?.id ?? -1;
			this.proxy.idJahrgang = null;
			this.proxy.idKlasse = null;
		},
	});

	jahrgang = computed<JahrgangsDaten | null>({
		get: () => this._jahrgaengeState.jahrgaenge.byId.get(this.proxy.idJahrgang ?? -1) ?? null,
		set: (value: JahrgangsDaten | null) => {
			this.proxy.idJahrgang = value?.id ?? null;
			this.proxy.idKlasse = null;
		},
	});

	klasse = computed<KlassenListeEintrag | null>({
		get: () => this._manager().getKlassenByIdFuerAbschnitt(this.proxy.idSchuljahresabschnitt).get(this.proxy.idKlasse ?? -1) ?? null,
		set: (value: KlassenListeEintrag | null) => this.proxy.idKlasse = value?.id ?? null,
	});

	anmeldedatum = computed<string | null>({
		get: () => this.proxy.anmeldedatum,
		set: (value: string | null) => {
			this.proxy.anmeldedatum = value;
			this.proxy.aufnahmedatum = null;
		},
	});

	aufnahmedatum = computed<string | null>({
		get: () => this.proxy.aufnahmedatum,
		set: (value: string | null) => {
			this.proxy.aufnahmedatum = value;
			this.proxy.beginnBildungsgang = null;
		},
	});

	einschulungsart = computed<EinschulungsartKatalogEintrag | null>({
		get: () => Einschulungsart.data().getEintragByID(this.proxy.idGrundschuleEinschulungsart ?? -1),
		set: (value: EinschulungsartKatalogEintrag | null) => this.proxy.idGrundschuleEinschulungsart = value?.id ?? null,
	});

	geschlecht = computed<Geschlecht | null>({
		get: () => Geschlecht.fromValue(this.proxy.geschlecht) ?? null,
		set: (value: Geschlecht | null) => this.proxy.geschlecht = value?.id ?? -1,
	});

	religion = computed<ReligionEintrag | null>({
		get: () => this._religionenState.religionen.byId.get(this.proxy.idReligion ?? -1) ?? null,
		set: (value: ReligionEintrag | null) => this.proxy.idReligion = value?.id ?? null,
	});

	klassen = computed<KlassenListeEintrag[]>(() => {
		const alleKlassenAbschnitt = this._manager().klassenByIdAbschnitt.get(this.proxy.idSchuljahresabschnitt) ?? [];
		const result: KlassenListeEintrag[] = [];
		for (const klasse of alleKlassenAbschnitt) {
			if (klasse.idJahrgang === this.proxy.idJahrgang) {
				result.push(klasse);
			}
		}
		return result;
	});

}
