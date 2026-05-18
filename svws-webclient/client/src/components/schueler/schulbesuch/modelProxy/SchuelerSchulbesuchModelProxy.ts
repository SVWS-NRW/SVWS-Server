import type { SchuelerSchulbesuchManager } from "@ui";
import { ModelProxy, ValidatorNumberRange, ValidatorStringLength } from "@ui";
import type { EinschulungsartKatalogEintrag, HerkunftsartenKatalogEintrag, JahrgaengeKatalogEintrag, JahrgangsDaten, KatalogEntlassgrund, Kindergarten,
	KindergartenbesuchKatalogEintrag, PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag, SchuelerSchulbesuchsdaten, SchulEintrag, SchulformKatalogEintrag,
	UebergangsempfehlungKatalogEintrag } from "@core";
import { Einschulungsart, Herkunftsarten, Jahrgaenge, Kindergartenbesuch, PrimarstufeSchuleingangsphaseBesuchsjahre, Schulform, Uebergangsempfehlung } from "@core";
import { computed } from "vue";

export class SchuelerSchulbesuchModelProxy extends ModelProxy<SchuelerSchulbesuchsdaten> {

	private readonly manager: () => SchuelerSchulbesuchManager;

	constructor(
		data: () => SchuelerSchulbesuchsdaten,
		manager: () => SchuelerSchulbesuchManager,
		patch?: (data: Partial<SchuelerSchulbesuchsdaten>) => Promise<boolean>) {
		const listOfAutopatchProps: Iterable<keyof SchuelerSchulbesuchsdaten> =
			["idVorherigeSchule", "vorigeEntlassdatum", "vorigeEntlassjahrgang", "vorigeEntlassgrundID", "vorigeAbschlussartID", "vorigeArtLetzteVersetzung",
				"entlassungDatum", "idEntlassjahrgang", "entlassungGrundID", "entlassungAbschlussartID", "idKindergarten", "idDauerKindergartenbesuch",
				"verpflichtungSprachfoerderkurs", "teilnahmeSprachfoerderkurs", "aufnehmendBestaetigt", "idAufnehmendeSchule", "aufnehmendWechseldatum",
				"grundschuleEinschulungsartID", "idGrundschuleJahreEingangsphase", "idGrundschuleUebergangsempfehlung", "sekIErsteSchulform"];
		super({ data, patch, listOfAutopatchProps, checkValidBeforePatch: true });
		this.manager = manager;
		this.addValidatoren();
		this.validate();
	}

	private addValidatoren() {
		this.addValidator(new ValidatorStringLength(() => this.proxy.vorigeBemerkung, null, 255), "vorigeBemerkung");
		this.addValidator(new ValidatorNumberRange(() => this.proxy.grundschuleEinschulungsjahr, 1900, 2100), "grundschuleEinschulungsjahr");
		this.addValidator(new ValidatorNumberRange(() => this.proxy.sekIWechsel, 1900, 2100), "sekIWechsel");
		this.addValidator(new ValidatorNumberRange(() => this.proxy.sekIIWechsel, 1900, 2100), "sekIIWechsel");
	}

	vorherigeSchule = computed<SchulEintrag | null>({
		get: () => this.manager().schulenById.get(this.proxy.idVorherigeSchule ?? -1) ?? null,
		set: (v: SchulEintrag | null) => this.proxy.idVorherigeSchule = v?.id ?? null,
	});

	vorherigeSchulform = computed<Schulform | null>(() => Schulform.data().getWertByIDOrNull(this.vorherigeSchule.value?.idSchulform ?? -1));

	vorherigeAllgHerkunft = computed<string | null>(
		() => Schulform.data().getEintragByID(this.vorherigeSchule.value?.idSchulform ?? -1)?.text ?? null);

	schulnummerStatistik = computed<string | null>(() => this.vorherigeSchule.value?.schulnummerStatistik ?? null);

	vorigeEntlassjahrgang = computed<JahrgaengeKatalogEintrag | null>({
		get: () => Jahrgaenge.data().getWertByKuerzel(this.proxy.vorigeEntlassjahrgang ?? '')?.daten(this.manager().schuljahr) ?? null,
		set: (v: JahrgaengeKatalogEintrag | null) => this.proxy.vorigeEntlassjahrgang = v?.kuerzel ?? null,
	});

	vorigeEntlassgrundID = computed<KatalogEntlassgrund | null>({
		get: () => this.manager().entlassgruendeById.get(this.proxy.vorigeEntlassgrundID ?? -1) ?? null,
		set: (v: KatalogEntlassgrund | null) => this.proxy.vorigeEntlassgrundID = v?.id ?? null,
	});

	vorigeArtLetzteVersetzung = computed<HerkunftsartenKatalogEintrag | null>({
		get: () => Herkunftsarten.data().getEintragByID(Number(this.proxy.vorigeArtLetzteVersetzung ?? -1)),
		set: (v: HerkunftsartenKatalogEintrag | null) => this.proxy.vorigeArtLetzteVersetzung = v?.id.toString() ?? null,
	});

	idEntlassjahrgang = computed<JahrgangsDaten | null>({
		get: () => this.manager().jahrgaengeById.get(this.proxy.idEntlassjahrgang ?? -1) ?? null,
		set: (v: JahrgangsDaten | null) => this.proxy.idEntlassjahrgang = v?.id ?? null,
	});

	entlassungGrundID = computed<KatalogEntlassgrund | null>({
		get: () => this.manager().entlassgruendeById.get(this.proxy.entlassungGrundID ?? -1) ?? null,
		set: (v: KatalogEntlassgrund | null) => this.proxy.entlassungGrundID = v?.id ?? null,
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

	grundschuleEinschulungsartID = computed<EinschulungsartKatalogEintrag | null>({
		get: () => Einschulungsart.data().getEintragByID(this.proxy.grundschuleEinschulungsartID ?? -1),
		set: (v: EinschulungsartKatalogEintrag | null) => this.proxy.grundschuleEinschulungsartID = v?.id ?? null,
	});

	idGrundschuleJahreEingangsphase = computed<PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag | null>({
		get: () => PrimarstufeSchuleingangsphaseBesuchsjahre.data().getEintragByID(this.proxy.idGrundschuleJahreEingangsphase ?? -1),
		set: (v: PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag | null) => this.proxy.idGrundschuleJahreEingangsphase = v?.id ?? null,
	});

	idGrundschuleUebergangsempfehlung = computed<UebergangsempfehlungKatalogEintrag | null>({
		get: () => Uebergangsempfehlung.data().getEintragByID(this.proxy.idGrundschuleUebergangsempfehlung ?? -1),
		set: (v: UebergangsempfehlungKatalogEintrag | null) => this.proxy.idGrundschuleUebergangsempfehlung = v?.id ?? null,
	});

	sekIErsteSchulform = computed<SchulformKatalogEintrag | null>({
		get: () => Schulform.data().getWertByKuerzel(this.proxy.sekIErsteSchulform ?? '')?.daten(this.manager().schuljahr) ?? null,
		set: (v: SchulformKatalogEintrag | null) => this.proxy.sekIErsteSchulform = v?.kuerzel ?? null,
	});

}
