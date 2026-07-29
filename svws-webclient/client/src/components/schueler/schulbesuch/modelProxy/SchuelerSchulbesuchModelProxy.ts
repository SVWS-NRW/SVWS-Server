import type { SchuelerSchulbesuchManager } from "@ui";
import { ModelProxy, ValidatorNumberRange, ValidatorStringLength } from "@ui";
import type { EinschulungsartKatalogEintrag, HerkunftsartenKatalogEintrag, JahrgaengeKatalogEintrag, JahrgangsDaten, KatalogEntlassgrund, Kindergarten,
	KindergartenbesuchKatalogEintrag, PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag, SchuelerSchulbesuchsdaten,
	SchulabschlussAllgemeinbildendKatalogEintrag, HerkunftSonstigeKatalogEintrag, SchulabschlussBerufsbildendKatalogEintrag, SchulEintrag,
	SchulformKatalogEintrag, UebergangsempfehlungKatalogEintrag, FachklasseKatalogEintrag, HerkunftBildungsgangKatalogEintrag } from "@core";
import { Einschulungsart, Herkunftsarten, Jahrgaenge, Kindergartenbesuch, PrimarstufeSchuleingangsphaseBesuchsjahre, Schulform, Uebergangsempfehlung,
	SchulabschlussAllgemeinbildend, SchulabschlussBerufsbildend, HerkunftSchulform, HerkunftSonstige, Fachklasse, HerkunftBildungsgang } from "@core";
import { computed } from "vue";

export class SchuelerSchulbesuchModelProxy extends ModelProxy<SchuelerSchulbesuchsdaten> {

	private readonly manager: () => SchuelerSchulbesuchManager;

	constructor(
		data: () => SchuelerSchulbesuchsdaten,
		manager: () => SchuelerSchulbesuchManager,
		patch?: (data: Partial<SchuelerSchulbesuchsdaten>) => Promise<boolean>) {
		const listOfAutopatchProps: Iterable<keyof SchuelerSchulbesuchsdaten> =
			["idVorherigeSchule", "entlassdatumVorherigeSchule", "kuerzelEntlassjahrgangVorherigeSchule", "idEntlassgrundVorherigeSchule",
				"idHerkunftsartVersetzungVorherigeSchule", "entlassdatumDieseSchule", "idEntlassjahrgangDieseSchule",
				"idEntlassgrundDieseSchule", "idAbschlussartDieseSchule", "idKindergarten", "idDauerKindergartenbesuch", "verpflichtungSprachfoerderkurs",
				"teilnahmeSprachfoerderkurs", "wechselBestaetigtAufnehmendeSchule", "idAufnehmendeSchule", "wechseldatumAufnehmendeSchule",
				"idEinschulungsartGrundschule", "idEingangsphaseGrundschule", "idUebergangsempfehlungGrundschule", "kuerzelErsteSchulformSek1",
				"berufsabschlussVorhanden", "schluesselHoechsterSchulabschluss", "schluesselAbschlussartAllgemeinbildendVorherigeSchule",
				"schluesselAbschlussartBerufsbildendVorherigeSchule", "idSchulgliederungVorherigeSchule", "schluesselCoreTypeFachklasseVorherigeSchule",
				"idHerkunftSonstigeVorherigeSchule"];
		super({ data, patch, listOfAutopatchProps });
		this.manager = manager;
		this.addValidatoren();
		this.validate();
	}

	private addValidatoren() {
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.bemerkungVorherigeSchule, null, 255), "bemerkungVorherigeSchule");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.einschulungsjahrGrundschule, 1900, 2100), "einschulungsjahrGrundschule");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.wechseljahrSekI, 1900, 2100), "wechseljahrSekI");
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.wechseljahrSekII, 1900, 2100), "wechseljahrSekII");
	}

	vorherigeSchule = computed<SchulEintrag | null>({
		get: () => this.manager().schulenById.get(this.proxy.idVorherigeSchule ?? -1) ?? null,
		set: (v: SchulEintrag | null) => {
			this.proxy.idHerkunftSonstigeVorherigeSchule = null;
			this.proxy.idVorherigeSchule = v?.id ?? null;
		},
	});

	private isOeffentlicheOderErsatzschuleInNRW(schulnummer: string | null): boolean {
		return (schulnummer?.startsWith("1")) === true;
	}

	private isSonstigeSchule(schulnummer: string | null): boolean {
		return (schulnummer?.startsWith("9")) === true;
	}

	schulformVorherigeSchule = computed<Schulform | null>(() => {
		if (this.proxy.idVorherigeSchule === null) {
			return null;
		}
		const schulnummer = this.manager().schulenById.get(this.proxy.idVorherigeSchule)?.schulnummerStatistik ?? null;
		if (this.isOeffentlicheOderErsatzschuleInNRW(schulnummer)) {
			return Schulform.data().getWertByIDOrNull(this.vorherigeSchule.value?.idSchulform ?? -1);
		}
		if (this.isSonstigeSchule(schulnummer)) {
			const eintrag = HerkunftSchulform.data().getEintragByID(this.vorherigeSchule.value?.idSchulform ?? -1);
			return Schulform.data().getWertByKuerzel(eintrag?.kuerzel ?? '');
		}
		return null;
	});

	schulformVorherigeSchuleKeinSchulbesuch = computed<HerkunftSonstigeKatalogEintrag | null>({
		get: () => HerkunftSonstige.data().getEintragByID(this.proxy.idHerkunftSonstigeVorherigeSchule ?? -1) ?? null,
		set: (v: HerkunftSonstigeKatalogEintrag | null) => this.proxy.idHerkunftSonstigeVorherigeSchule = v?.id ?? null,
	});

	schulgliederungVorherigeSchule = computed<HerkunftBildungsgangKatalogEintrag | null>({
		get: () => HerkunftBildungsgang.data().getEintragByID(this.proxy.idSchulgliederungVorherigeSchule ?? -1) ?? null,
		set: (v: HerkunftBildungsgangKatalogEintrag | null) => this.proxy.idSchulgliederungVorherigeSchule = v?.id ?? null,
	});

	fachklasseVorherigeSchule = computed<FachklasseKatalogEintrag | null>({
		get: () => Fachklasse.data().getEintragBySchuljahrUndSchluessel(this.manager().schuljahr, this.proxy.schluesselCoreTypeFachklasseVorherigeSchule ?? '') ?? null,
		set: (v: FachklasseKatalogEintrag | null) => this.proxy.schluesselCoreTypeFachklasseVorherigeSchule = v?.schluessel ?? null,
	});

	bezeichnungSchulformVorherigeSchule = computed<string | null>(
		() => Schulform.data().getEintragByID(this.vorherigeSchule.value?.idSchulform ?? -1)?.text ?? null);

	bezeichnungHerkunftSchulformVorherigeSchule = computed<string | null>(
		() => HerkunftSchulform.data().getEintragByID(this.vorherigeSchule.value?.idSchulform ?? -1)?.text ?? null);

	schulnummerStatistik = computed<string | null>(() => this.vorherigeSchule.value?.schulnummerStatistik ?? null);


	hoechsterSchulabschluss = computed<SchulabschlussAllgemeinbildendKatalogEintrag | null>({
		get: () => SchulabschlussAllgemeinbildend.data().getEintragBySchuljahrUndSchluessel(this.manager().schuljahr, this.proxy.schluesselHoechsterSchulabschluss ?? '') ?? null,
		set: (v: SchulabschlussAllgemeinbildendKatalogEintrag | null) => this.proxy.schluesselHoechsterSchulabschluss = v?.schluessel ?? null,
	});

	kuerzelEntlassjahrgangVorherigeSchule = computed<JahrgaengeKatalogEintrag | null>({
		get: () => Jahrgaenge.data().getWertByKuerzel(this.proxy.kuerzelEntlassjahrgangVorherigeSchule ?? '')?.daten(this.manager().schuljahr) ?? null,
		set: (v: JahrgaengeKatalogEintrag | null) => this.proxy.kuerzelEntlassjahrgangVorherigeSchule = v?.kuerzel ?? null,
	});

	idEntlassgrundVorherigeSchule = computed<KatalogEntlassgrund | null>({
		get: () => this.manager().entlassgruendeById.get(this.proxy.idEntlassgrundVorherigeSchule ?? -1) ?? null,
		set: (v: KatalogEntlassgrund | null) => this.proxy.idEntlassgrundVorherigeSchule = v?.id ?? null,
	});

	idHerkunftsartVersetzungVorherigeSchule = computed<HerkunftsartenKatalogEintrag | null>({
		get: () => Herkunftsarten.data().getEintragByID(Number(this.proxy.idHerkunftsartVersetzungVorherigeSchule ?? -1)),
		set: (v: HerkunftsartenKatalogEintrag | null) => this.proxy.idHerkunftsartVersetzungVorherigeSchule = v?.id.toString() ?? null,
	});

	abschlussartAllgemeinbildendVorherigeSchule = computed<SchulabschlussAllgemeinbildendKatalogEintrag | null>({
		get: () => SchulabschlussAllgemeinbildend.data().getEintragBySchuljahrUndSchluessel(this.manager().schuljahr, this.proxy.schluesselAbschlussartAllgemeinbildendVorherigeSchule ?? ''),
		set: (v: SchulabschlussAllgemeinbildendKatalogEintrag | null) => this.proxy.schluesselAbschlussartAllgemeinbildendVorherigeSchule = v?.schluessel ?? null,
	});

	abschlussartBerufsbildendVorherigeSchule = computed<SchulabschlussAllgemeinbildendKatalogEintrag | null>({
		get: () => SchulabschlussBerufsbildend.data().getEintragBySchuljahrUndSchluessel(this.manager().schuljahr, this.proxy.schluesselAbschlussartBerufsbildendVorherigeSchule ?? ''),
		set: (v: SchulabschlussBerufsbildendKatalogEintrag | null) => this.proxy.schluesselAbschlussartBerufsbildendVorherigeSchule = v?.schluessel ?? null,
	});

	idEntlassjahrgangDieseSchule = computed<JahrgangsDaten | null>({
		get: () => this.manager().jahrgaengeById.get(this.proxy.idEntlassjahrgangDieseSchule ?? -1) ?? null,
		set: (v: JahrgangsDaten | null) => this.proxy.idEntlassjahrgangDieseSchule = v?.id ?? null,
	});

	idEntlassgrundDieseSchule = computed<KatalogEntlassgrund | null>({
		get: () => this.manager().entlassgruendeById.get(this.proxy.idEntlassgrundDieseSchule ?? -1) ?? null,
		set: (v: KatalogEntlassgrund | null) => this.proxy.idEntlassgrundDieseSchule = v?.id ?? null,
	});

	idKindergarten = computed<Kindergarten | null>({
		get: () => this.manager().kindergaertenById.get(this.proxy.idKindergarten ?? -1) ?? null,
		set: (v: Kindergarten | null) => this.proxy.idKindergarten = v?.id ?? null,
	});

	idDauerKindergartenbesuch = computed<KindergartenbesuchKatalogEintrag | null>({
		get: () => Kindergartenbesuch.data().getEintragByID(this.proxy.idDauerKindergartenbesuch ?? -1),
		set: (v: KindergartenbesuchKatalogEintrag | null) => this.proxy.idDauerKindergartenbesuch = v?.id ?? null,
	});

	idAufnehmendeSchule = computed<SchulEintrag | null>({
		get: () => this.manager().schulenById.get(this.proxy.idAufnehmendeSchule ?? -1) ?? null,
		set: (v: SchulEintrag | null) => this.proxy.idAufnehmendeSchule = v?.id ?? null,
	});

	idEinschulungsartGrundschule = computed<EinschulungsartKatalogEintrag | null>({
		get: () => Einschulungsart.data().getEintragByID(this.proxy.idEinschulungsartGrundschule ?? -1),
		set: (v: EinschulungsartKatalogEintrag | null) => this.proxy.idEinschulungsartGrundschule = v?.id ?? null,
	});

	idEingangsphaseGrundschule = computed<PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag | null>({
		get: () => PrimarstufeSchuleingangsphaseBesuchsjahre.data().getEintragByID(this.proxy.idEingangsphaseGrundschule ?? -1),
		set: (v: PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag | null) => this.proxy.idEingangsphaseGrundschule = v?.id ?? null,
	});

	idUebergangsempfehlungGrundschule = computed<UebergangsempfehlungKatalogEintrag | null>({
		get: () => Uebergangsempfehlung.data().getEintragByID(this.proxy.idUebergangsempfehlungGrundschule ?? -1),
		set: (v: UebergangsempfehlungKatalogEintrag | null) => this.proxy.idUebergangsempfehlungGrundschule = v?.id ?? null,
	});

	kuerzelErsteSchulformSek1 = computed<SchulformKatalogEintrag | null>({
		get: () => Schulform.data().getWertByKuerzel(this.proxy.kuerzelErsteSchulformSek1 ?? '')?.daten(this.manager().schuljahr) ?? null,
		set: (v: SchulformKatalogEintrag | null) => this.proxy.kuerzelErsteSchulformSek1 = v?.kuerzel ?? null,
	});

}
