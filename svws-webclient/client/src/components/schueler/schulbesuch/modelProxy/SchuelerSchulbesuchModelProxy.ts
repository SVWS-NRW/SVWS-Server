import type { JahrgaengeKatalogEintrag } from "@core/asd/data/jahrgang/JahrgaengeKatalogEintrag";
import type { PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag } from "@core/asd/data/jahrgang/PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag";
import type { EinschulungsartKatalogEintrag } from "@core/asd/data/schueler/EinschulungsartKatalogEintrag";
import type { HerkunftBildungsgangKatalogEintrag } from "@core/asd/data/schueler/HerkunftBildungsgangKatalogEintrag";
import type { HerkunftsartenKatalogEintrag } from "@core/asd/data/schueler/HerkunftsartenKatalogEintrag";
import type { HerkunftSchulformKatalogEintrag } from "@core/asd/data/schueler/HerkunftSchulformKatalogEintrag";
import type { HerkunftSonstigeKatalogEintrag } from "@core/asd/data/schueler/HerkunftSonstigeKatalogEintrag";
import type { HochschulabschlussKatalogEintrag } from "@core/asd/data/schueler/HochschulabschlussKatalogEintrag";
import type { SchuelerSchulbesuchsdaten } from "@core/asd/data/schueler/SchuelerSchulbesuchsdaten";
import type { UebergangsempfehlungKatalogEintrag } from "@core/asd/data/schueler/UebergangsempfehlungKatalogEintrag";
import type { FachklasseKatalogEintrag } from "@core/asd/data/schule/FachklasseKatalogEintrag";
import type { KindergartenbesuchKatalogEintrag } from "@core/asd/data/schule/KindergartenbesuchKatalogEintrag";
import type { SchulabschlussAllgemeinbildendKatalogEintrag } from "@core/asd/data/schule/SchulabschlussAllgemeinbildendKatalogEintrag";
import type { SchulabschlussBerufsbildendKatalogEintrag } from "@core/asd/data/schule/SchulabschlussBerufsbildendKatalogEintrag";
import type { SchulformKatalogEintrag } from "@core/asd/data/schule/SchulformKatalogEintrag";
import { Jahrgaenge } from "@core/asd/types/jahrgang/Jahrgaenge";
import { PrimarstufeSchuleingangsphaseBesuchsjahre } from "@core/asd/types/jahrgang/PrimarstufeSchuleingangsphaseBesuchsjahre";
import { Einschulungsart } from "@core/asd/types/schueler/Einschulungsart";
import { HerkunftBildungsgang } from "@core/asd/types/schueler/HerkunftBildungsgang";
import { Herkunftsarten } from "@core/asd/types/schueler/Herkunftsarten";
import { HerkunftSchulform } from "@core/asd/types/schueler/HerkunftSchulform";
import { HerkunftSonstige } from "@core/asd/types/schueler/HerkunftSonstige";
import { Hochschulabschluss } from "@core/asd/types/schueler/Hochschulabschluss";
import { Uebergangsempfehlung } from "@core/asd/types/schueler/Uebergangsempfehlung";
import { Fachklasse } from "@core/asd/types/schule/Fachklasse";
import { Kindergartenbesuch } from "@core/asd/types/schule/Kindergartenbesuch";
import { SchulabschlussAllgemeinbildend } from "@core/asd/types/schule/SchulabschlussAllgemeinbildend";
import { SchulabschlussBerufsbildend } from "@core/asd/types/schule/SchulabschlussBerufsbildend";
import { Schulform } from "@core/asd/types/schule/Schulform";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import type { KatalogEntlassgrund } from "@core/core/data/kataloge/KatalogEntlassgrund";
import type { SchulEintrag } from "@core/core/data/kataloge/SchulEintrag";
import type { Kindergarten } from "@core/core/data/schule/Kindergarten";
import { ModelProxy } from "@ui/model/ModelProxy";
import type { SchuelerSchulbesuchManager } from "@ui/ui/manager/schueler/SchuelerSchulbesuchManager";
import { ValidatorNumberRange } from "@ui/validation/common/ValidatorNumberRange";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
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
				"idHerkunftSchulformVorherigeSchule", "idHerkunftSonstigeVorherigeSchule", "idHochschulabschluss"];
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
			if (this.isSonstigeSchule(v?.schulnummerStatistik ?? '')) {
				this.herkunftSchulformVorherigeSchule.value = HerkunftSchulform.data().getEintragByID(v?.idSchulform ?? -1) ?? null;
			} else {
				this.herkunftSchulformVorherigeSchule.value = null;
			}
		},
	});

	herkunftSchulformVorherigeSchule = computed<HerkunftSchulformKatalogEintrag | null>({
		get: () => HerkunftSchulform.data().getEintragByID(this.proxy.idHerkunftSchulformVorherigeSchule ?? -1) ?? null,
		set: (v: HerkunftSchulformKatalogEintrag | null) => {
			this.proxy.idHerkunftSchulformVorherigeSchule = v?.id ?? null;
		},
	});

	herkunftSonstigeKeinSchulbesuch = computed<HerkunftSonstigeKatalogEintrag | null>({
		get: () => HerkunftSonstige.data().getEintragByID(this.proxy.idHerkunftSonstigeVorherigeSchule ?? -1) ?? null,
		set: (v: HerkunftSonstigeKatalogEintrag | null) => {
			this.proxy.idHerkunftSonstigeVorherigeSchule = v?.id ?? null;
			this.proxy.idVorherigeSchule = null;
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
			return Schulform.data().getWertByKuerzel(this.herkunftSchulformVorherigeSchule.value?.kuerzel ?? '');
		}
		return null;
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

	idHochschulabschluss = computed<HochschulabschlussKatalogEintrag | null>({
		get: () => Hochschulabschluss.data().getEintragByID(this.proxy.idHochschulabschluss ?? -1),
		set: (v: HochschulabschlussKatalogEintrag | null) => this.proxy.idHochschulabschluss = v?.id ?? null,
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
