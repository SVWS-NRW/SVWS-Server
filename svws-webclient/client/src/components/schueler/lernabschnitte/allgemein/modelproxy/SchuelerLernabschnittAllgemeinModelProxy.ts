import { computed } from "vue";
import { ModelProxy, StringPattern, ValidatorInputRequired, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import type { BilingualeSpracheKatalogEintrag, FoerderschwerpunktEintrag, JahrgangsDaten, KlassenartKatalogEintrag, KlassenDaten, LehrerListeEintrag, List, OrganisationsformKatalogEintrag, PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag, SchuelerLernabschnittsdaten, SchulgliederungKatalogEintrag } from "@core";
import { AllgemeinbildendOrganisationsformen, ArrayList, BerufskollegOrganisationsformen, BilingualeSprache, Klassenart, PrimarstufeSchuleingangsphaseBesuchsjahre, Schulform, Schulgliederung, WeiterbildungskollegOrganisationsformen } from "@core";
import type { SchuelerLernabschnittManager } from "~/components/schueler/lernabschnitte/SchuelerLernabschnittManager";

export class SchuelerLernabschnittAllgemeinModelProxy extends ModelProxy<SchuelerLernabschnittsdaten> {

	private readonly manager: () => SchuelerLernabschnittManager;
	private readonly schulform: () => Schulform;

	constructor(
		data: () => SchuelerLernabschnittsdaten,
		manager: () => SchuelerLernabschnittManager,
		schulform: () => Schulform,
		patch?: (data: Partial<SchuelerLernabschnittsdaten>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof SchuelerLernabschnittsdaten> = [
			"klassenID", "jahrgangID", "tutorID", "sonderpaedagogeID",
			"idSchulgliederung", "pruefungsOrdnung", "idOrganisationsform", "idKlassenart",
			"bilingualerZweig", "foerderschwerpunkt1ID", "foerderschwerpunkt2ID",
			"hatAOSF", "hatAutismus", "hatSchwerbehinderungsNachweis",
			"hatZieldifferentenUnterricht", "datumAnfang", "datumEnde", "idEpJahre",
		];
		super({ data, patch, listOfAutopatchProps });
		this.manager = manager;
		this.schulform = schulform;

		// Pflichtfelder
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.klassenID), "klassenID");
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.jahrgangID), "jahrgangID");
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
		get: () => this.proxy.klassenID === null ? null : this.manager().klasseGetByIdOrException(this.proxy.klassenID),
		set: (v: KlassenDaten | null) => this.proxy.klassenID = v?.id ?? null,
	});

	jahrgang = computed<JahrgangsDaten | null>({
		get: () => this.proxy.jahrgangID === null ? null : this.manager().jahrgangGetByIdOrException(this.proxy.jahrgangID),
		set: (v: JahrgangsDaten | null) => this.proxy.jahrgangID = v?.id ?? null,
	});

	private static readonly primarschulformen = new Set<Schulform>([
		Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.PS, Schulform.S, Schulform.KS, Schulform.V,
	]);

	// TODO Hier gibt es Probleme und muss grundlegend angepasst werden.
	epJahr = computed<PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag | null>({
		get: () => {
			if (!SchuelerLernabschnittAllgemeinModelProxy.primarschulformen.has(this.schulform())) {
				return null;
			}
			const ep = this.proxy.idEpJahre ?? null;
			if (ep === null) {
				return null;
			}
			return PrimarstufeSchuleingangsphaseBesuchsjahre.data().getWertByIDOrNull(ep)?.daten(this.manager().schuljahrGet()) ?? null;
		},
		set: (v: PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag | null) => this.proxy.idEpJahre = v?.id ?? null,
	});

	tutor = computed<LehrerListeEintrag | null>({
		get: () => this.proxy.tutorID === null ? null : this.manager().lehrerGetByIdOrException(this.proxy.tutorID),
		set: (v: LehrerListeEintrag | null) => this.proxy.tutorID = v?.id ?? null,
	});

	sonderpaedagoge = computed<LehrerListeEintrag | null>({
		get: () => this.proxy.sonderpaedagogeID === null ? null : this.manager().lehrerGetByIdOrException(this.proxy.sonderpaedagogeID),
		set: (v: LehrerListeEintrag | null) => this.proxy.sonderpaedagogeID = v?.id ?? null,
	});

	foerderschwerpunkt = computed<FoerderschwerpunktEintrag | null>({
		get: () => this.proxy.foerderschwerpunkt1ID === null ? null : this.manager().foerderschwerpunktGetByIdOrException(this.proxy.foerderschwerpunkt1ID),
		set: (v: FoerderschwerpunktEintrag | null) => this.proxy.foerderschwerpunkt1ID = v?.id ?? null,
	});

	foerderschwerpunkt2 = computed<FoerderschwerpunktEintrag | null>({
		get: () => this.proxy.foerderschwerpunkt2ID === null ? null : this.manager().foerderschwerpunktGetByIdOrException(this.proxy.foerderschwerpunkt2ID),
		set: (v: FoerderschwerpunktEintrag | null) => this.proxy.foerderschwerpunkt2ID = v?.id ?? null,
	});

	klassenart = computed<KlassenartKatalogEintrag | null>({
		get: () => {
			const schuljahr = this.manager().schuljahrGet();
			const wert = Klassenart.data().getWertByIDOrNull(this.proxy.idKlassenart);
			if ((wert === null) || !wert.hatSchulform(schuljahr, this.schulform())) {
				return null;
			}
			return Klassenart.data().getEintragBySchuljahrUndWert(schuljahr, wert);
		},
		set: (v: KlassenartKatalogEintrag | null) => this.proxy.idKlassenart = v?.id ?? null,
	});

	gliederung = computed<SchulgliederungKatalogEintrag | null>({
		get: () => {
			const schuljahr = this.manager().schuljahrGet();
			const wert = Schulgliederung.data().getWertByIDOrNull(this.proxy.idSchulgliederung);
			if ((wert === null) || !wert.hatSchulform(schuljahr, this.schulform())) {
				return null;
			}
			return Schulgliederung.data().getEintragBySchuljahrUndWert(schuljahr, wert);
		},
		set: (v: SchulgliederungKatalogEintrag | null) => this.proxy.idSchulgliederung = v?.id ?? null,
	});

	organisationsform = computed<OrganisationsformKatalogEintrag | null>({
		get: () => {
			const idOrga = this.proxy.idOrganisationsform;
			if (idOrga === null) {
				return null;
			}
			const schuljahr = this.manager().schuljahrGet();
			if (this.schulform() === Schulform.WB) {
				return WeiterbildungskollegOrganisationsformen.data().getWertByIDOrNull(idOrga)?.daten(schuljahr) ?? null;
			}
			if ((this.schulform() === Schulform.BK) || (this.schulform() === Schulform.SB)) {
				return BerufskollegOrganisationsformen.data().getWertByIDOrNull(idOrga)?.daten(schuljahr) ?? null;
			}
			return AllgemeinbildendOrganisationsformen.data().getWertByIDOrNull(idOrga)?.daten(schuljahr) ?? null;
		},
		set: (v: OrganisationsformKatalogEintrag | null) => this.proxy.idOrganisationsform = v?.id ?? null,
	});

	bilingualerZweig = computed<BilingualeSpracheKatalogEintrag | null>({
		get: () => {
			const schuljahr = this.manager().schuljahrGet();
			const wert = BilingualeSprache.data().getWertByKuerzel(this.proxy.bilingualerZweig ?? "");
			if ((wert === null) || !wert.hatSchulform(schuljahr, this.schulform())) {
				return null;
			}
			return BilingualeSprache.data().getEintragBySchuljahrUndWert(schuljahr, wert);
		},
		set: (v: BilingualeSpracheKatalogEintrag | null) => this.proxy.bilingualerZweig = v?.kuerzel ?? null,
	});

	organisationsformen = computed<List<OrganisationsformKatalogEintrag>>(() => {
		const schuljahr = this.manager().schuljahrGet();
		const result = new ArrayList<OrganisationsformKatalogEintrag>();
		if (this.schulform() === Schulform.WB) {
			for (const orgform of WeiterbildungskollegOrganisationsformen.values()) {
				result.add(orgform.daten(schuljahr));
			}
		} else if ((this.schulform() === Schulform.BK) || (this.schulform() === Schulform.SB)) {
			for (const orgform of BerufskollegOrganisationsformen.values()) {
				result.add(orgform.daten(schuljahr));
			}
		} else {
			for (const orgform of AllgemeinbildendOrganisationsformen.values()) {
				result.add(orgform.daten(schuljahr));
			}
		}
		return result;
	});

}
