import { computed } from "vue";
import { ModelProxy, StringPattern, ValidatorInputRequired, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import type { BilingualeSpracheKatalogEintrag, FoerderschwerpunktEintrag, JahrgangsDaten, KlassenartKatalogEintrag, KlassenDaten, LehrerListeEintrag, OrganisationsformKatalogEintrag, PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag, SchuelerLernabschnittsdaten, SchulgliederungKatalogEintrag } from "@core";
import { AllgemeinbildendOrganisationsformen, BerufskollegOrganisationsformen, BilingualeSprache, Klassenart, PrimarstufeSchuleingangsphaseBesuchsjahre, Schulform, Schulgliederung, WeiterbildungskollegOrganisationsformen } from "@core";
import type { SchuelerLernabschnittManager } from "~/components/schueler/lernabschnitte/SchuelerLernabschnittManager";
import { ValidatorSchuelerLernabschnittKlasseUndJahrgang } from "~/components/schueler/lernabschnitte/allgemein/modelproxy/validation/ValidatorSchuelerLernabschnittKlasseUndJahrgang";

export class SchuelerLernabschnittAllgemeinModelProxy extends ModelProxy<SchuelerLernabschnittsdaten> {

	private readonly manager: () => SchuelerLernabschnittManager;
	private readonly schulform: () => Schulform;
	private readonly schuljahr: () => number;

	constructor(
		data: () => SchuelerLernabschnittsdaten,
		manager: () => SchuelerLernabschnittManager,
		schulform: () => Schulform,
		schuljahr: () => number,
		patch?: (data: Partial<SchuelerLernabschnittsdaten>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof SchuelerLernabschnittsdaten> = [
			"klassenID", "jahrgangID", "tutorID", "sonderpaedagogeID",
			"idSchulgliederung", "idOrganisationsform", "idKlassenart",
			"bilingualerZweig", "foerderschwerpunkt1ID", "foerderschwerpunkt2ID",
			"hatAOSF", "hatAutismus", "hatSchwerbehinderungsNachweis",
			"hatZieldifferentenUnterricht", "datumAnfang", "datumEnde", "idEpJahre",
		];
		super({ data, patch, listOfAutopatchProps });
		this.manager = manager;
		this.schulform = schulform;
		this.schuljahr = schuljahr;

		this.addBlockingValidator(new ValidatorSchuelerLernabschnittKlasseUndJahrgang(
			() => this.manager().klasseGetByIdOrNull(this.proxy.klassenID ?? -1),
			() => this.manager().jahrgangGetByIdOrNull(this.proxy.jahrgangID ?? -1)),
		"klassenID", "jahrgangID");
		this.addBlockingValidator(new ValidatorSchuelerLernabschnittKlasseUndJahrgang(
			() => this.manager().klasseGetByIdOrNull(this.proxy.klassenID ?? -1),
			() => this.manager().jahrgangGetByIdOrNull(this.proxy.jahrgangID ?? -1)),
		"jahrgangID", "klassenID");
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.idEpJahre), "idEpJahre");
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.idSchulgliederung), "idSchulgliederung");
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.pruefungsOrdnung), "pruefungsOrdnung");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.pruefungsOrdnung, StringPattern.NO_WHITESPACES), "pruefungsOrdnung");
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.pruefungsOrdnung, null, 20), "pruefungsOrdnung");
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.idOrganisationsform), "idOrganisationsform");
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.idKlassenart), "idKlassenart");

		this.validate();
	}

	klasse = computed<KlassenDaten | null>({
		get: () => (this.proxy.klassenID === null) ? null : this.manager().klasseGetByIdOrNull(this.proxy.klassenID),
		set: (v: KlassenDaten | null) => this.proxy.klassenID = v?.id ?? null,
	});

	jahrgang = computed<JahrgangsDaten | null>({
		get: () => (this.proxy.jahrgangID === null) ? null : this.manager().jahrgangGetByIdOrNull(this.proxy.jahrgangID),
		set: (v: JahrgangsDaten | null) => this.proxy.jahrgangID = v?.id ?? null,
	});

	epJahre = computed<PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag | null>({
		get: () => (this.proxy.idEpJahre === null) ? null : PrimarstufeSchuleingangsphaseBesuchsjahre.data().getWertByIDOrNull(this.proxy.idEpJahre)?.daten(this.schuljahr()) ?? null,
		set: (v: PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag | null) => this.proxy.idEpJahre = v?.id ?? null,
	});

	tutor = computed<LehrerListeEintrag | null>({
		get: () => (this.proxy.tutorID === null) ? null : this.manager().lehrerGetByIdOrException(this.proxy.tutorID),
		set: (v: LehrerListeEintrag | null) => this.proxy.tutorID = v?.id ?? null,
	});

	sonderpaedagoge = computed<LehrerListeEintrag | null>({
		get: () => (this.proxy.sonderpaedagogeID === null) ? null : this.manager().lehrerGetByIdOrException(this.proxy.sonderpaedagogeID),
		set: (v: LehrerListeEintrag | null) => this.proxy.sonderpaedagogeID = v?.id ?? null,
	});

	foerderschwerpunkt = computed<FoerderschwerpunktEintrag | null>({
		get: () => (this.proxy.foerderschwerpunkt1ID === null) ? null : this.manager().foerderschwerpunktGetByIdOrException(this.proxy.foerderschwerpunkt1ID),
		set: (v: FoerderschwerpunktEintrag | null) => this.proxy.foerderschwerpunkt1ID = v?.id ?? null,
	});

	foerderschwerpunkt2 = computed<FoerderschwerpunktEintrag | null>({
		get: () => (this.proxy.foerderschwerpunkt2ID === null) ? null : this.manager().foerderschwerpunktGetByIdOrException(this.proxy.foerderschwerpunkt2ID),
		set: (v: FoerderschwerpunktEintrag | null) => this.proxy.foerderschwerpunkt2ID = v?.id ?? null,
	});

	klassenart = computed<KlassenartKatalogEintrag | null>({
		get: () => {
			const klassenart = Klassenart.data().getWertByIDOrNull(this.proxy.idKlassenart);
			if (klassenart?.hatSchulform(this.schuljahr(), this.schulform()) !== true) {
				return null;
			}
			return Klassenart.data().getEintragBySchuljahrUndWert(this.schuljahr(), klassenart);
		},
		set: (v: KlassenartKatalogEintrag | null) => this.proxy.idKlassenart = v?.id ?? null,
	});

	gliederung = computed<SchulgliederungKatalogEintrag | null>({
		get: () => {
			const schulgliederung = Schulgliederung.data().getWertByIDOrNull(this.proxy.idSchulgliederung);
			if (schulgliederung?.hatSchulform(this.schuljahr(), this.schulform()) !== true) {
				return null;
			}
			return Schulgliederung.data().getEintragBySchuljahrUndWert(this.schuljahr(), schulgliederung);
		},
		set: (v: SchulgliederungKatalogEintrag | null) => this.proxy.idSchulgliederung = v?.id ?? null,
	});

	organisationsform = computed<OrganisationsformKatalogEintrag | null>({
		get: () => {
			const idOrga = this.proxy.idOrganisationsform;
			if (idOrga === null) {
				return null;
			}
			if (this.schulform() === Schulform.WB) {
				return WeiterbildungskollegOrganisationsformen.data().getWertByIDOrNull(idOrga)?.daten(this.schuljahr()) ?? null;
			}
			if ((this.schulform() === Schulform.BK) || (this.schulform() === Schulform.SB)) {
				return BerufskollegOrganisationsformen.data().getWertByIDOrNull(idOrga)?.daten(this.schuljahr()) ?? null;
			}
			return AllgemeinbildendOrganisationsformen.data().getWertByIDOrNull(idOrga)?.daten(this.schuljahr()) ?? null;
		},
		set: (v: OrganisationsformKatalogEintrag | null) => this.proxy.idOrganisationsform = v?.id ?? null,
	});

	bilingualerZweig = computed<BilingualeSpracheKatalogEintrag | null>({
		get: () => {
			const sprache = BilingualeSprache.data().getWertByKuerzel(this.proxy.bilingualerZweig ?? "");
			if (sprache?.hatSchulform(this.schuljahr(), this.schulform()) !== true) {
				return null;
			}
			return BilingualeSprache.data().getEintragBySchuljahrUndWert(this.schuljahr(), sprache);
		},
		set: (v: BilingualeSpracheKatalogEintrag | null) => this.proxy.bilingualerZweig = v?.kuerzel ?? null,
	});

	organisationsformen = computed<Iterable<OrganisationsformKatalogEintrag>>(() => {
		if (this.schulform() === Schulform.WB) {
			return [...WeiterbildungskollegOrganisationsformen.data().getWerteBySchulform(this.schulform())]
				.map(e => e.daten(this.schuljahr()))
				.filter(e => e !== null);
		}

		if ((this.schulform() === Schulform.BK) || (this.schulform() === Schulform.SB)) {
			return [...BerufskollegOrganisationsformen.data().getWerteBySchulform(this.schulform())]
				.map(e => e.daten(this.schuljahr()))
				.filter(e => e !== null);
		}

		return [...AllgemeinbildendOrganisationsformen.data().getWerteBySchulform(this.schulform())]
			.map(e => e.daten(this.schuljahr()))
			.filter(e => e !== null);
	});

}
