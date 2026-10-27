import { computed } from "vue";

import type { BilingualeSpracheKatalogEintrag } from "@core/asd/data/fach/BilingualeSpracheKatalogEintrag";
import type { PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag } from "@core/asd/data/jahrgang/PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag";
import type { KlassenartKatalogEintrag } from "@core/asd/data/klassen/KlassenartKatalogEintrag";
import type { KlassenDaten } from "@core/asd/data/klassen/KlassenDaten";
import type { SchuelerLernabschnittsdaten } from "@core/asd/data/schueler/SchuelerLernabschnittsdaten";
import type { OrganisationsformKatalogEintrag } from "@core/asd/data/schule/OrganisationsformKatalogEintrag";
import type { SchulgliederungKatalogEintrag } from "@core/asd/data/schule/SchulgliederungKatalogEintrag";
import { BilingualeSprache } from "@core/asd/types/fach/BilingualeSprache";
import { PrimarstufeSchuleingangsphaseBesuchsjahre } from "@core/asd/types/jahrgang/PrimarstufeSchuleingangsphaseBesuchsjahre";
import { Klassenart } from "@core/asd/types/klassen/Klassenart";
import { Note } from "@core/asd/types/Note";
import { AllgemeinbildendOrganisationsformen } from "@core/asd/types/schule/AllgemeinbildendOrganisationsformen";
import { BerufskollegOrganisationsformen } from "@core/asd/types/schule/BerufskollegOrganisationsformen";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { Schulgliederung } from "@core/asd/types/schule/Schulgliederung";
import { WeiterbildungskollegOrganisationsformen } from "@core/asd/types/schule/WeiterbildungskollegOrganisationsformen";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { FoerderschwerpunktEintrag } from "@core/core/data/schule/FoerderschwerpunktEintrag";
import { ModelProxy } from "@ui/model/ModelProxy";
import { useSchuleState } from "@ui/states/SchuleState";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorNumberRange } from "@ui/validation/common/ValidatorNumberRange";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { StringPattern, ValidatorStringMatchesPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

import { ValidatorSchuelerLernabschnittDatumVonBis } from "~/components/schueler/lernabschnitte/allgemein/modelproxy/validation/ValidatorSchuelerLernabschnittDatumVonBis";
import { ValidatorSchuelerLernabschnittKlasseUndJahrgang } from "~/components/schueler/lernabschnitte/allgemein/modelproxy/validation/ValidatorSchuelerLernabschnittKlasseUndJahrgang";

export class SchuelerLernabschnittAllgemeinModelProxy extends ModelProxy<SchuelerLernabschnittsdaten> {

	private readonly lehrer: () => LehrerListeEintrag[];
	private readonly klassen: () => KlassenDaten[];
	private readonly jahrgaenge: () => JahrgangsDaten[];
	private readonly foerderschwerpunkte: () => FoerderschwerpunktEintrag[];
	private readonly schuleState = useSchuleState();

	constructor(
		data: () => SchuelerLernabschnittsdaten,
		lehrer: () => LehrerListeEintrag[],
		klassen: () => KlassenDaten[],
		jahrgaenge: () => JahrgangsDaten[],
		foerderschwerpunkte: () => FoerderschwerpunktEintrag[],
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
		this.lehrer = lehrer;
		this.klassen = klassen;
		this.jahrgaenge = jahrgaenge;
		this.foerderschwerpunkte = foerderschwerpunkte;

		this.addValidatoren();

		this.validate();
	}

	private addValidatoren() {
		this.addBlockingValidator(new ValidatorSchuelerLernabschnittKlasseUndJahrgang(
			() => this.proxy.klassenID,
			() => this.proxy.jahrgangID,
			this.klassen,
			this.jahrgaenge),
		"klassenID", "jahrgangID");
		this.addBlockingValidator(new ValidatorSchuelerLernabschnittKlasseUndJahrgang(
			() => this.proxy.klassenID,
			() => this.proxy.jahrgangID,
			this.klassen,
			this.jahrgaenge),
		"jahrgangID", "klassenID");
		this.addBlockingValidator(new ValidatorSchuelerLernabschnittDatumVonBis(
			() => this.proxy.datumAnfang,
			() => this.proxy.datumEnde),
		"datumAnfang", "datumEnde");
		this.addBlockingValidator(new ValidatorSchuelerLernabschnittDatumVonBis(
			() => this.proxy.datumAnfang,
			() => this.proxy.datumEnde),
		"datumEnde", "datumAnfang");
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
		get: () => this.klassen().find(k => k.id === this.proxy.klassenID) ?? null,
		set: (v: KlassenDaten | null) => this.proxy.klassenID = v?.id ?? null,
	});

	jahrgang = computed<JahrgangsDaten | null>({
		get: () => this.jahrgaenge().find(j => j.id === this.proxy.jahrgangID) ?? null,
		set: (v: JahrgangsDaten | null) => this.proxy.jahrgangID = v?.id ?? null,
	});

	epJahre = computed<PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag | null>({
		get: () => (this.proxy.idEpJahre === null) ?
			null
			: PrimarstufeSchuleingangsphaseBesuchsjahre.data().getWertByIDOrNull(this.proxy.idEpJahre)?.daten(this.schuleState.abschnitt.schuljahr) ?? null,
		set: (v: PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag | null) => this.proxy.idEpJahre = v?.id ?? null,
	});

	tutor = computed<LehrerListeEintrag | null>({
		get: () => this.lehrer().find(l => l.id === this.proxy.tutorID) ?? null,
		set: (v: LehrerListeEintrag | null) => this.proxy.tutorID = v?.id ?? null,
	});

	sonderpaedagoge = computed<LehrerListeEintrag | null>({
		get: () => this.lehrer().find(l => l.id === this.proxy.sonderpaedagogeID) ?? null,
		set: (v: LehrerListeEintrag | null) => this.proxy.sonderpaedagogeID = v?.id ?? null,
	});

	foerderschwerpunkt = computed<FoerderschwerpunktEintrag | null>({
		get: () => this.foerderschwerpunkte().find(f => f.id === this.proxy.foerderschwerpunkt1ID) ?? null,
		set: (v: FoerderschwerpunktEintrag | null) => this.proxy.foerderschwerpunkt1ID = v?.id ?? null,
	});

	foerderschwerpunkt2 = computed<FoerderschwerpunktEintrag | null>({
		get: () => this.foerderschwerpunkte().find(f => f.id === this.proxy.foerderschwerpunkt2ID) ?? null,
		set: (v: FoerderschwerpunktEintrag | null) => this.proxy.foerderschwerpunkt2ID = v?.id ?? null,
	});

	klassenart = computed<KlassenartKatalogEintrag | null>({
		get: () => {
			const klassenart = Klassenart.data().getWertByIDOrNull(this.proxy.idKlassenart);
			if (klassenart?.hatSchulform(this.schuleState.abschnitt.schuljahr, this.schuleState.schulform) !== true) {
				return null;
			}
			return Klassenart.data().getEintragBySchuljahrUndWert(this.schuleState.abschnitt.schuljahr, klassenart);
		},
		set: (v: KlassenartKatalogEintrag | null) => this.proxy.idKlassenart = v?.id ?? null,
	});

	gliederung = computed<SchulgliederungKatalogEintrag | null>({
		get: () => {
			const schulgliederung = Schulgliederung.data().getWertByIDOrNull(this.proxy.idSchulgliederung);
			if (schulgliederung?.hatSchulform(this.schuleState.abschnitt.schuljahr, this.schuleState.schulform) !== true) {
				return null;
			}
			return Schulgliederung.data().getEintragBySchuljahrUndWert(this.schuleState.abschnitt.schuljahr, schulgliederung);
		},
		set: (v: SchulgliederungKatalogEintrag | null) => this.proxy.idSchulgliederung = v?.id ?? null,
	});

	organisationsform = computed<OrganisationsformKatalogEintrag | null>({
		get: () => {
			const idOrga = this.proxy.idOrganisationsform;
			if (idOrga === null) {
				return null;
			}
			if (this.schuleState.schulform === Schulform.WB) {
				return WeiterbildungskollegOrganisationsformen.data().getWertByIDOrNull(idOrga)?.daten(this.schuleState.abschnitt.schuljahr) ?? null;
			}
			if ((this.schuleState.schulform === Schulform.BK) || (this.schuleState.schulform === Schulform.SB)) {
				return BerufskollegOrganisationsformen.data().getWertByIDOrNull(idOrga)?.daten(this.schuleState.abschnitt.schuljahr) ?? null;
			}
			return AllgemeinbildendOrganisationsformen.data().getWertByIDOrNull(idOrga)?.daten(this.schuleState.abschnitt.schuljahr) ?? null;
		},
		set: (v: OrganisationsformKatalogEintrag | null) => this.proxy.idOrganisationsform = v?.id ?? null,
	});

	bilingualerZweig = computed<BilingualeSpracheKatalogEintrag | null>({
		get: () => {
			const sprache = BilingualeSprache.data().getWertByKuerzel(this.proxy.bilingualerZweig ?? "");
			if (sprache?.hatSchulform(this.schuleState.abschnitt.schuljahr, this.schuleState.schulform) !== true) {
				return null;
			}
			return BilingualeSprache.data().getEintragBySchuljahrUndWert(this.schuleState.abschnitt.schuljahr, sprache);
		},
		set: (v: BilingualeSpracheKatalogEintrag | null) => this.proxy.bilingualerZweig = v?.kuerzel ?? null,
	});

	organisationsformen = computed<Iterable<OrganisationsformKatalogEintrag>>(() => {
		if (this.schuleState.schulform === Schulform.WB) {
			return [...WeiterbildungskollegOrganisationsformen.data().getWerteBySchulform(this.schuleState.schulform)]
				.map(e => e.daten(this.schuleState.abschnitt.schuljahr))
				.filter(e => e !== null);
		}

		if ((this.schuleState.schulform === Schulform.BK) || (this.schuleState.schulform === Schulform.SB)) {
			return [...BerufskollegOrganisationsformen.data().getWerteBySchulform(this.schuleState.schulform)]
				.map(e => e.daten(this.schuleState.abschnitt.schuljahr))
				.filter(e => e !== null);
		}

		return [...AllgemeinbildendOrganisationsformen.data().getWerteBySchulform(this.schuleState.schulform)]
			.map(e => e.daten(this.schuleState.abschnitt.schuljahr))
			.filter(e => e !== null);
	});

	lernbereichsnoteGSbzwAL = computed<Note | undefined>({
		get: () => {
			const note = Note.fromNoteSekI(this.proxy.noteLernbereichGSbzwAL);
			return ((note === null) || (note === Note.KEINE)) ? undefined : note;
		},
		set: (value: Note | undefined) => this.proxy.noteLernbereichGSbzwAL = value?.getNoteSekI(this.schuleState.abschnitt.schuljahr) ?? null,
	});

	lernbereichsnoteNW = computed<Note | undefined>({
		get: () => {
			const note = Note.fromNoteSekI(this.proxy.noteLernbereichNW);
			return ((note === null) || (note === Note.KEINE)) ? undefined : note;
		},
		set: (value: Note | undefined) => this.proxy.noteLernbereichNW = value?.getNoteSekI(this.schuleState.abschnitt.schuljahr) ?? null,
	});

}
