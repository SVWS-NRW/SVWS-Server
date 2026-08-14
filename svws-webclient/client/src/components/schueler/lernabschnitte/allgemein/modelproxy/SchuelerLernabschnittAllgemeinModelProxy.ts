import { computed } from "vue";
import { ModelProxy, StringPattern, ValidatorInputRequired, ValidatorNumberRange, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import type { BilingualeSpracheKatalogEintrag, FoerderschwerpunktEintrag, JahrgangsDaten, KlassenartKatalogEintrag, KlassenDaten, LehrerListeEintrag, OrganisationsformKatalogEintrag, PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag, SchuelerLernabschnittsdaten, SchulgliederungKatalogEintrag } from "@core";
import { AllgemeinbildendOrganisationsformen, BerufskollegOrganisationsformen, BilingualeSprache, Klassenart, PrimarstufeSchuleingangsphaseBesuchsjahre, Schulform, Schulgliederung, WeiterbildungskollegOrganisationsformen, Note } from "@core";
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
			"noteLernbereichNW", "noteLernbereichGSbzwAL",
		];
		super({ data, patch, listOfAutopatchProps });
		this.manager = manager;
		this.schulform = schulform;
		this.schuljahr = schuljahr;

		this.addValidatoren();

		this.validate();
	}

	private addValidatoren() {
		this.addBlockingValidator(new ValidatorSchuelerLernabschnittKlasseUndJahrgang(
			() => this.proxy.klassenID,
			() => this.proxy.jahrgangID,
			this.manager),
		"klassenID", "jahrgangID");
		this.addBlockingValidator(new ValidatorSchuelerLernabschnittKlasseUndJahrgang(
			() => this.proxy.klassenID,
			() => this.proxy.jahrgangID,
			this.manager),
		"jahrgangID", "klassenID");
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.idEpJahre), "idEpJahre");
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.idSchulgliederung), "idSchulgliederung");
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.pruefungsOrdnung), "pruefungsOrdnung");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.pruefungsOrdnung, StringPattern.NO_WHITESPACES), "pruefungsOrdnung");
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.pruefungsOrdnung, null, 20), "pruefungsOrdnung");
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.idOrganisationsform), "idOrganisationsform");
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.idKlassenart), "idKlassenart");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.noteLernbereichNW, 0, null), "noteLernbereichNW");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.noteLernbereichGSbzwAL, 0, null), "noteLernbereichGSbzwAL");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.fehlstundenGrenzwert, 0, null), "fehlstundenGrenzwert");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.fehlstundenGesamt, 0, null), "fehlstundenGesamt");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.fehlstundenUnentschuldigt, 0, null), "fehlstundenUnentschuldigt");
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

	lernbereichsnoteGSbzwAL = computed<Note | undefined>({
		get: () => {
			const note = Note.fromNoteSekI(this.proxy.noteLernbereichGSbzwAL);
			return ((note === null) || (note === Note.KEINE)) ? undefined : note;
		},
		set: (value: Note | undefined) => this.proxy.noteLernbereichGSbzwAL = value?.getNoteSekI(this.schuljahr()) ?? null,
	});

	lernbereichsnoteNW = computed<Note | undefined>({
		get: () => {
			const note = Note.fromNoteSekI(this.proxy.noteLernbereichNW);
			return ((note === null) || (note === Note.KEINE)) ? undefined : note;
		},
		set: (value: Note | undefined) => this.proxy.noteLernbereichNW = value?.getNoteSekI(this.schuljahr()) ?? null,
	});

}
