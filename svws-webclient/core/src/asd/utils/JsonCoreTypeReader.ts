import { BilingualeSpracheKatalogEintrag } from "../data/fach/BilingualeSpracheKatalogEintrag";
import { FachgruppeKatalogEintrag } from "../data/fach/FachgruppeKatalogEintrag";
import { FachKatalogEintrag } from "../data/fach/FachKatalogEintrag";
import { SprachreferenzniveauKatalogEintrag } from "../data/fach/SprachreferenzniveauKatalogEintrag";
import { JahrgaengeKatalogEintrag } from "../data/jahrgang/JahrgaengeKatalogEintrag";
import { PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag } from "../data/jahrgang/PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag";
import { KAOAAnschlussoptionenKatalogEintrag } from "../data/kaoa/KAOAAnschlussoptionenKatalogEintrag";
import { KAOABerufsfeldKatalogEintrag } from "../data/kaoa/KAOABerufsfeldKatalogEintrag";
import { KAOAEbene4KatalogEintrag } from "../data/kaoa/KAOAEbene4KatalogEintrag";
import { KAOAKategorieKatalogEintrag } from "../data/kaoa/KAOAKategorieKatalogEintrag";
import { KAOAMerkmaleOptionsartenKatalogEintrag } from "../data/kaoa/KAOAMerkmaleOptionsartenKatalogEintrag";
import { KAOAMerkmalKatalogEintrag } from "../data/kaoa/KAOAMerkmalKatalogEintrag";
import { KAOAZusatzmerkmaleOptionsartenKatalogEintrag } from "../data/kaoa/KAOAZusatzmerkmaleOptionsartenKatalogEintrag";
import { KAOAZusatzmerkmalKatalogEintrag } from "../data/kaoa/KAOAZusatzmerkmalKatalogEintrag";
import { KlassenartKatalogEintrag } from "../data/klassen/KlassenartKatalogEintrag";
import { ZulaessigeKursartKatalogEintrag } from "../data/kurse/ZulaessigeKursartKatalogEintrag";
import { LehrerAbgangsgrundKatalogEintrag } from "../data/lehrer/LehrerAbgangsgrundKatalogEintrag";
import { LehrerAnrechnungsgrundKatalogEintrag } from "../data/lehrer/LehrerAnrechnungsgrundKatalogEintrag";
import { LehrerBeschaeftigungsartKatalogEintrag } from "../data/lehrer/LehrerBeschaeftigungsartKatalogEintrag";
import { LehrerEinsatzstatusKatalogEintrag } from "../data/lehrer/LehrerEinsatzstatusKatalogEintrag";
import { LehrerFachrichtungAnerkennungKatalogEintrag } from "../data/lehrer/LehrerFachrichtungAnerkennungKatalogEintrag";
import { LehrerFachrichtungKatalogEintrag } from "../data/lehrer/LehrerFachrichtungKatalogEintrag";
import { LehrerLehramtAnerkennungKatalogEintrag } from "../data/lehrer/LehrerLehramtAnerkennungKatalogEintrag";
import { LehrerLehramtKatalogEintrag } from "../data/lehrer/LehrerLehramtKatalogEintrag";
import { LehrerLehrbefaehigungAnerkennungKatalogEintrag } from "../data/lehrer/LehrerLehrbefaehigungAnerkennungKatalogEintrag";
import { LehrerLehrbefaehigungKatalogEintrag } from "../data/lehrer/LehrerLehrbefaehigungKatalogEintrag";
import { LehrerLeitungsfunktionKatalogEintrag } from "../data/lehrer/LehrerLeitungsfunktionKatalogEintrag";
import { LehrerMehrleistungsartKatalogEintrag } from "../data/lehrer/LehrerMehrleistungsartKatalogEintrag";
import { LehrerMinderleistungsartKatalogEintrag } from "../data/lehrer/LehrerMinderleistungsartKatalogEintrag";
import { LehrerRechtsverhaeltnisKatalogEintrag } from "../data/lehrer/LehrerRechtsverhaeltnisKatalogEintrag";
import { LehrerZugangsgrundKatalogEintrag } from "../data/lehrer/LehrerZugangsgrundKatalogEintrag";
import { NoteKatalogEintrag } from "../data/NoteKatalogEintrag";
import { HerkunftBildungsgangKatalogEintrag } from "../data/schueler/HerkunftBildungsgangKatalogEintrag";
import { HerkunftBildungsgangTypKatalogEintrag } from "../data/schueler/HerkunftBildungsgangTypKatalogEintrag";
import { SchuelerStatusKatalogEintrag } from "../data/schueler/SchuelerStatusKatalogEintrag";
import { UebergangsempfehlungKatalogEintrag } from "../data/schueler/UebergangsempfehlungKatalogEintrag";
import { BerufskollegAnlageKatalogEintrag } from "../data/schule/BerufskollegAnlageKatalogEintrag";
import { BildungsgangTypKatalogEintrag } from "../data/schule/BildungsgangTypKatalogEintrag";
import { FoerderschwerpunktKatalogEintrag } from "../data/schule/FoerderschwerpunktKatalogEintrag";
import { KindergartenbesuchKatalogEintrag } from "../data/schule/KindergartenbesuchKatalogEintrag";
import { OrganisationsformKatalogEintrag } from "../data/schule/OrganisationsformKatalogEintrag";
import { ReligionKatalogEintrag } from "../data/schule/ReligionKatalogEintrag";
import { SchulabschlussAllgemeinbildendKatalogEintrag } from "../data/schule/SchulabschlussAllgemeinbildendKatalogEintrag";
import { SchulabschlussBerufsbildendKatalogEintrag } from "../data/schule/SchulabschlussBerufsbildendKatalogEintrag";
import { SchulformKatalogEintrag } from "../data/schule/SchulformKatalogEintrag";
import { SchulgliederungKatalogEintrag } from "../data/schule/SchulgliederungKatalogEintrag";
import { CoreTypeSimple } from "../types/CoreTypeSimple";
import { BilingualeSprache } from "../types/fach/BilingualeSprache";
import { Fach } from "../types/fach/Fach";
import { Fachgruppe } from "../types/fach/Fachgruppe";
import { Sprachreferenzniveau } from "../types/fach/Sprachreferenzniveau";
import { Jahrgaenge } from "../types/jahrgang/Jahrgaenge";
import { PrimarstufeSchuleingangsphaseBesuchsjahre } from "../types/jahrgang/PrimarstufeSchuleingangsphaseBesuchsjahre";
import { KAOAAnschlussoptionen } from "../types/kaoa/KAOAAnschlussoptionen";
import { KAOABerufsfeld } from "../types/kaoa/KAOABerufsfeld";
import { KAOAEbene4 } from "../types/kaoa/KAOAEbene4";
import { KAOAKategorie } from "../types/kaoa/KAOAKategorie";
import { KAOAMerkmal } from "../types/kaoa/KAOAMerkmal";
import { KAOAMerkmaleOptionsarten } from "../types/kaoa/KAOAMerkmaleOptionsarten";
import { KAOAZusatzmerkmal } from "../types/kaoa/KAOAZusatzmerkmal";
import { KAOAZusatzmerkmaleOptionsarten } from "../types/kaoa/KAOAZusatzmerkmaleOptionsarten";
import { Klassenart } from "../types/klassen/Klassenart";
import { ZulaessigeKursart } from "../types/kurse/ZulaessigeKursart";
import { LehrerAbgangsgrund } from "../types/lehrer/LehrerAbgangsgrund";
import { LehrerAnrechnungsgrund } from "../types/lehrer/LehrerAnrechnungsgrund";
import { LehrerBeschaeftigungsart } from "../types/lehrer/LehrerBeschaeftigungsart";
import { LehrerEinsatzstatus } from "../types/lehrer/LehrerEinsatzstatus";
import { LehrerFachrichtung } from "../types/lehrer/LehrerFachrichtung";
import { LehrerFachrichtungAnerkennung } from "../types/lehrer/LehrerFachrichtungAnerkennung";
import { LehrerLehramt } from "../types/lehrer/LehrerLehramt";
import { LehrerLehramtAnerkennung } from "../types/lehrer/LehrerLehramtAnerkennung";
import { LehrerLehrbefaehigung } from "../types/lehrer/LehrerLehrbefaehigung";
import { LehrerLehrbefaehigungAnerkennung } from "../types/lehrer/LehrerLehrbefaehigungAnerkennung";
import { LehrerLeitungsfunktion } from "../types/lehrer/LehrerLeitungsfunktion";
import { LehrerMehrleistungsarten } from "../types/lehrer/LehrerMehrleistungsarten";
import { LehrerMinderleistungsarten } from "../types/lehrer/LehrerMinderleistungsarten";
import { LehrerRechtsverhaeltnis } from "../types/lehrer/LehrerRechtsverhaeltnis";
import { LehrerZugangsgrund } from "../types/lehrer/LehrerZugangsgrund";
import { Note } from "../types/Note";
import { Einschulungsart } from "../types/schueler/Einschulungsart";
import { HerkunftBildungsgang } from "../types/schueler/HerkunftBildungsgang";
import { HerkunftBildungsgangTyp } from "../types/schueler/HerkunftBildungsgangTyp";
import { SchuelerStatus } from "../types/schueler/SchuelerStatus";
import { Uebergangsempfehlung } from "../types/schueler/Uebergangsempfehlung";
import { AllgemeinbildendOrganisationsformen } from "../types/schule/AllgemeinbildendOrganisationsformen";
import { BerufskollegAnlage } from "../types/schule/BerufskollegAnlage";
import { BerufskollegBildungsgangTyp } from "../types/schule/BerufskollegBildungsgangTyp";
import { BerufskollegOrganisationsformen } from "../types/schule/BerufskollegOrganisationsformen";
import { Foerderschwerpunkt } from "../types/schule/Foerderschwerpunkt";
import { Kindergartenbesuch } from "../types/schule/Kindergartenbesuch";
import { Religion } from "../types/schule/Religion";
import { SchulabschlussAllgemeinbildend } from "../types/schule/SchulabschlussAllgemeinbildend";
import { SchulabschlussBerufsbildend } from "../types/schule/SchulabschlussBerufsbildend";
import { Schulform } from "../types/schule/Schulform";
import { Schulgliederung } from "../types/schule/Schulgliederung";
import { Verkehrssprache } from "../types/schule/Verkehrssprache";
import { WeiterbildungskollegBildungsgangTyp } from "../types/schule/WeiterbildungskollegBildungsgangTyp";
import { WeiterbildungskollegOrganisationsformen } from "../types/schule/WeiterbildungskollegOrganisationsformen";
import { CoreTypeDataManager } from "./CoreTypeDataManager";
import { DeveloperNotificationException } from "../../core/exceptions/DeveloperNotificationException";
import { ArrayList } from "../../java/util/ArrayList";
import { HashMap } from "../../java/util/HashMap";
import { HashSet } from "../../java/util/HashSet";
import type { List } from "../../java/util/List";
import { BaseApi } from "../../api/BaseApi";
import { ValidatorFehlerartKontext } from "../validate/ValidatorFehlerartKontext";
import { ValidatorManager } from "../validate/ValidatorManager";
import { NationalitaetenKatalogEintrag } from "../data/schule/NationalitaetenKatalogEintrag";
import { Nationalitaeten } from "../types/schule/Nationalitaeten";
import { EinschulungsartKatalogEintrag } from "../data/schueler/EinschulungsartKatalogEintrag";
import { VerkehrsspracheKatalogEintrag } from "../data/schule/VerkehrsspracheKatalogEintrag";
import { LehrerPflichtstundensollVollzeit } from "../types/lehrer/LehrerPflichtstundensollVollzeit";
import { LehrerPflichtstundensollVollzeitKatalogEintrag } from "../data/lehrer/LehrerPflichtstundensollVollzeitKatalogEintrag";
import { TerminKatalogEintrag } from "../data/schule/TerminKatalogEintrag";
import { Termin } from "../types/schule/Termin";

interface JsonCoreTypeEntry<T> {
	bezeichner: string;
	idStatistik: string;
	historie: T[];
}

interface JsonCoreTypeData<T> {
	version: number;
	daten: JsonCoreTypeEntry<T>[];
}

interface JsonCoreTypeDataResult<T> {
	version: number;
	mapData: HashMap<string, List<T>>;
	mapStatistikIDs: HashMap<string, string>;
}

interface JsonValidatorFehlerartEntry {
	validator: string;
	historie: ValidatorFehlerartKontext[];
}

interface JsonValidatorFehlerartData {
	version: number;
	daten: JsonValidatorFehlerartEntry[];
}



export class JsonCoreTypeReader {

	private readonly api: BaseApi;

	private keys = [
		"Schulform", "BerufskollegAnlage", "AllgemeinbildendOrganisationsformen", "BerufskollegOrganisationsformen", "WeiterbildungskollegOrganisationsformen",
		"SchulabschlussAllgemeinbildend", "SchulabschlussBerufsbildend", "Einschulungsart", "HerkunftBildungsgang", "HerkunftBildungsgangTyp", "Jahrgaenge", "PrimarstufeSchuleingangsphaseBesuchsjahre", "Religion",
		"Kindergartenbesuch", "SchuelerStatus", "Note", "Sprachreferenzniveau", "BerufskollegBildungsgangTyp", "WeiterbildungskollegBildungsgangTyp", "Schulgliederung", "Verkehrssprache", "Fachgruppe", "Fach",
		"LehrerAbgangsgrund", "LehrerBeschaeftigungsart", "LehrerEinsatzstatus", "LehrerFachrichtung", "LehrerLehrbefaehigung", "LehrerFachrichtungAnerkennung", "LehrerLehramt",
		"LehrerLehramtAnerkennung", "LehrerLehrbefaehigungAnerkennung", "LehrerLeitungsfunktion", "LehrerRechtsverhaeltnis", "LehrerZugangsgrund", "BilingualeSprache", "KAOABerufsfeld",
		"KAOAMerkmaleOptionsarten", "KAOAZusatzmerkmaleOptionsarten", "KAOAEbene4", "KAOAZusatzmerkmal", "KAOAAnschlussoptionen", "KAOAKategorie", "KAOAMerkmal", "Klassenart", "Uebergangsempfehlung",
		"ZulaessigeKursart", "Foerderschwerpunkt", "Termin", "LehrerAnrechnungsgrund", "LehrerMehrleistungsarten", "LehrerMinderleistungsarten", "LehrerPflichtstundensollVollzeit", "Nationalitaeten", "ValidatorenFehlerartKontext",
	] as const;

	public constructor(url?: string) {
		this.api = new BaseApi(url ?? "", "", "");
	}

	mapCoreTypeNameJsonData = new Map<string, string>();

	protected async loadJson(coreTypeName: string) {
		const jsonData = await this.api.getJSON(`/types/${coreTypeName}.json`);
		this.mapCoreTypeNameJsonData.set(coreTypeName, jsonData);
	}

	private read<T>(name: string, mapper: (json: string) => T): JsonCoreTypeDataResult<T> {
		if (name === "")
			throw new DeveloperNotificationException("Für das Einlesen eines Core-Types muss ein gültiger Name angegeben werden");
		const json: string | undefined = this.mapCoreTypeNameJsonData.get(name);
		if (json === undefined)
			throw new DeveloperNotificationException(`Für den Core-Type "${name}" liegen keine JSON Daten vor. Wurde die Map mit den CoreType-Daten gefüllt?`);
		const data: JsonCoreTypeData<T> = JSON.parse(json);
		const idsStatistik = new HashSet<string>();
		const result = <JsonCoreTypeDataResult<T>>{
			version: data.version,
			mapData: new HashMap<string, List<T>>(),
			mapStatistikIDs: new HashMap<string, string>(),
		};
		for (const eintrag of data.daten) {
			const bezeichner = eintrag.bezeichner;
			const idStatistik = eintrag.idStatistik;
			const historie = eintrag.historie;
			if (idsStatistik.contains(idStatistik) === true)
				throw new DeveloperNotificationException("Fehler beim Einlesen der Core-Type-Daten für den Core-Type " + name);
			idsStatistik.add(idStatistik);
			result.mapStatistikIDs.put(bezeichner, idStatistik);
			const list = new ArrayList<T>();
			result.mapData.put(bezeichner, list);
			for (const obj of historie) {
				const tmpJson = JSON.stringify(obj);
				const deserialized = mapper(tmpJson);
				list.add(deserialized);
			}
		}
		return result;
	}

	public readSchulform() {
		const data = this.read('Schulform', (json) => SchulformKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<SchulformKatalogEintrag, Schulform>(data.version, Schulform.class, Schulform.values(), data.mapData, data.mapStatistikIDs);
		Schulform.init(manager);
	}

	public readBerufskollegAnlage() {
		const data = this.read('BerufskollegAnlage', (json) => BerufskollegAnlageKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<BerufskollegAnlageKatalogEintrag, BerufskollegAnlage>(data.version, BerufskollegAnlage.class, BerufskollegAnlage.values(), data.mapData, data.mapStatistikIDs);
		BerufskollegAnlage.init(manager);
	}

	public readAllgemeinbildendOrganisationsformen() {
		const data = this.read('AllgemeinbildendOrganisationsformen', (json) => OrganisationsformKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<OrganisationsformKatalogEintrag, AllgemeinbildendOrganisationsformen>(data.version, AllgemeinbildendOrganisationsformen.class, AllgemeinbildendOrganisationsformen.values(), data.mapData, data.mapStatistikIDs);
		AllgemeinbildendOrganisationsformen.init(manager);
	}

	public readBerufskollegOrganisationsformen() {
		const data = this.read('BerufskollegOrganisationsformen', (json) => OrganisationsformKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<OrganisationsformKatalogEintrag, BerufskollegOrganisationsformen>(data.version, BerufskollegOrganisationsformen.class, BerufskollegOrganisationsformen.values(), data.mapData, data.mapStatistikIDs);
		BerufskollegOrganisationsformen.init(manager);
	}

	public readWeiterbildungskollegOrganisationsformen() {
		const data = this.read('WeiterbildungskollegOrganisationsformen', (json) => OrganisationsformKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<OrganisationsformKatalogEintrag, WeiterbildungskollegOrganisationsformen>(data.version, WeiterbildungskollegOrganisationsformen.class,
			WeiterbildungskollegOrganisationsformen.values(), data.mapData, data.mapStatistikIDs);
		WeiterbildungskollegOrganisationsformen.init(manager);
	}

	public readSchulabschlussAllgemeinbildend() {
		const data = this.read('SchulabschlussAllgemeinbildend', (json) => SchulabschlussAllgemeinbildendKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<SchulabschlussAllgemeinbildendKatalogEintrag, SchulabschlussAllgemeinbildend>(data.version, SchulabschlussAllgemeinbildend.class, SchulabschlussAllgemeinbildend.values(), data.mapData, data.mapStatistikIDs);
		SchulabschlussAllgemeinbildend.init(manager);
	}

	public readSchulabschlussBerufsbildend() {
		const data = this.read('SchulabschlussBerufsbildend', (json) => SchulabschlussBerufsbildendKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<SchulabschlussBerufsbildendKatalogEintrag, SchulabschlussBerufsbildend>(data.version, SchulabschlussBerufsbildend.class, SchulabschlussBerufsbildend.values(), data.mapData, data.mapStatistikIDs);
		SchulabschlussBerufsbildend.init(manager);
	}

	public readEinschulungsart() {
		const data = this.read('Einschulungsart', (json) => EinschulungsartKatalogEintrag.transpilerFromJSON(json));
		CoreTypeSimple.initValues(new Einschulungsart(), Einschulungsart.class, data.mapData);
		const manager = new CoreTypeDataManager<EinschulungsartKatalogEintrag, Einschulungsart>(data.version, Einschulungsart.class, Einschulungsart.values(), data.mapData, data.mapStatistikIDs);
		Einschulungsart.init(manager);
	}

	public readHerkunftBildungsgang() {
		const data = this.read('HerkunftBildungsgang', (json) => HerkunftBildungsgangKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<HerkunftBildungsgangKatalogEintrag, HerkunftBildungsgang>(data.version, HerkunftBildungsgang.class, HerkunftBildungsgang.values(), data.mapData, data.mapStatistikIDs);
		HerkunftBildungsgang.init(manager);
	}

	public readHerkunftBildungsgangTyp() {
		const data = this.read('HerkunftBildungsgangTyp', (json) => HerkunftBildungsgangTypKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<HerkunftBildungsgangTypKatalogEintrag, HerkunftBildungsgangTyp>(data.version, HerkunftBildungsgangTyp.class, HerkunftBildungsgangTyp.values(), data.mapData, data.mapStatistikIDs);
		HerkunftBildungsgangTyp.init(manager);
	}

	public readJahrgaenge() {
		const data = this.read('Jahrgaenge', (json) => JahrgaengeKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<JahrgaengeKatalogEintrag, Jahrgaenge>(data.version, Jahrgaenge.class, Jahrgaenge.values(), data.mapData, data.mapStatistikIDs);
		Jahrgaenge.init(manager);
	}

	public readPrimarstufeSchuleingangsphaseBesuchsjahre() {
		const data = this.read('PrimarstufeSchuleingangsphaseBesuchsjahre', (json) => PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag, PrimarstufeSchuleingangsphaseBesuchsjahre>(data.version, PrimarstufeSchuleingangsphaseBesuchsjahre.class, PrimarstufeSchuleingangsphaseBesuchsjahre.values(), data.mapData, data.mapStatistikIDs);
		PrimarstufeSchuleingangsphaseBesuchsjahre.init(manager);
	}

	public readReligion() {
		const data = this.read('Religion', (json) => ReligionKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<ReligionKatalogEintrag, Religion>(data.version, Religion.class, Religion.values(), data.mapData, data.mapStatistikIDs);
		Religion.init(manager);
	}

	public readKindergartenbesuch() {
		const data = this.read('Kindergartenbesuch', (json) => KindergartenbesuchKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<KindergartenbesuchKatalogEintrag, Kindergartenbesuch>(data.version, Kindergartenbesuch.class, Kindergartenbesuch.values(), data.mapData, data.mapStatistikIDs);
		Kindergartenbesuch.init(manager);
	}

	public readSchuelerStatus() {
		const data = this.read('SchuelerStatus', (json) => SchuelerStatusKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<SchuelerStatusKatalogEintrag, SchuelerStatus>(data.version, SchuelerStatus.class, SchuelerStatus.values(), data.mapData, data.mapStatistikIDs);
		SchuelerStatus.init(manager);
	}

	public readNote() {
		const data = this.read('Note', (json) => NoteKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<NoteKatalogEintrag, Note>(data.version, Note.class, Note.values(), data.mapData, data.mapStatistikIDs);
		Note.init(manager);
	}

	public readSprachreferenzniveau() {
		const data = this.read('Sprachreferenzniveau', (json) => SprachreferenzniveauKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<SprachreferenzniveauKatalogEintrag, Sprachreferenzniveau>(data.version, Sprachreferenzniveau.class, Sprachreferenzniveau.values(), data.mapData, data.mapStatistikIDs);
		Sprachreferenzniveau.init(manager);
	}

	public readBerufskollegBildungsgangTyp() {
		const data = this.read('BerufskollegBildungsgangTyp', (json) => BildungsgangTypKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<BildungsgangTypKatalogEintrag, BerufskollegBildungsgangTyp>(data.version, BerufskollegBildungsgangTyp.class, BerufskollegBildungsgangTyp.values(), data.mapData, data.mapStatistikIDs);
		BerufskollegBildungsgangTyp.init(manager);
	}

	public readWeiterbildungskollegBildungsgangTyp() {
		const data = this.read('WeiterbildungskollegBildungsgangTyp', (json) => BildungsgangTypKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<BildungsgangTypKatalogEintrag, WeiterbildungskollegBildungsgangTyp>(data.version, WeiterbildungskollegBildungsgangTyp.class, WeiterbildungskollegBildungsgangTyp.values(), data.mapData, data.mapStatistikIDs);
		WeiterbildungskollegBildungsgangTyp.init(manager);
	}

	public readSchulgliederung() {
		const data = this.read('Schulgliederung', (json) => SchulgliederungKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<SchulgliederungKatalogEintrag, Schulgliederung>(data.version, Schulgliederung.class, Schulgliederung.values(), data.mapData, data.mapStatistikIDs);
		Schulgliederung.init(manager);
	}

	public readVerkehrssprache() {
		const data = this.read('Verkehrssprache', (json) => VerkehrsspracheKatalogEintrag.transpilerFromJSON(json));
		CoreTypeSimple.initValues(new Verkehrssprache(), Verkehrssprache.class, data.mapData);
		const manager = new CoreTypeDataManager<VerkehrsspracheKatalogEintrag, Verkehrssprache>(data.version, Verkehrssprache.class, Verkehrssprache.values(), data.mapData, data.mapStatistikIDs);
		Verkehrssprache.init(manager);
	}

	public readFachgruppe() {
		const data = this.read('Fachgruppe', (json) => FachgruppeKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<FachgruppeKatalogEintrag, Fachgruppe>(data.version, Fachgruppe.class, Fachgruppe.values(), data.mapData, data.mapStatistikIDs);
		Fachgruppe.init(manager);
	}

	public readFach() {
		const data = this.read('Fach', (json) => FachKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<FachKatalogEintrag, Fach>(data.version, Fach.class, Fach.values(), data.mapData, data.mapStatistikIDs);
		Fach.init(manager);
	}

	public readLehrerAbgangsgrund() {
		const data = this.read('LehrerAbgangsgrund', (json) => LehrerAbgangsgrundKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<LehrerAbgangsgrundKatalogEintrag, LehrerAbgangsgrund>(data.version, LehrerAbgangsgrund.class, LehrerAbgangsgrund.values(), data.mapData, data.mapStatistikIDs);
		LehrerAbgangsgrund.init(manager);
	}

	public readLehrerBeschaeftigungsart() {
		const data = this.read('LehrerBeschaeftigungsart', (json) => LehrerBeschaeftigungsartKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<LehrerBeschaeftigungsartKatalogEintrag, LehrerBeschaeftigungsart>(data.version, LehrerBeschaeftigungsart.class, LehrerBeschaeftigungsart.values(), data.mapData, data.mapStatistikIDs);
		LehrerBeschaeftigungsart.init(manager);
	}

	public readLehrerEinsatzstatus() {
		const data = this.read('LehrerEinsatzstatus', (json) => LehrerEinsatzstatusKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<LehrerEinsatzstatusKatalogEintrag, LehrerEinsatzstatus>(data.version, LehrerEinsatzstatus.class, LehrerEinsatzstatus.values(), data.mapData, data.mapStatistikIDs);
		LehrerEinsatzstatus.init(manager);
	}

	public readLehrerFachrichtung() {
		const data = this.read('LehrerFachrichtung', (json) => LehrerFachrichtungKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<LehrerFachrichtungKatalogEintrag, LehrerFachrichtung>(data.version, LehrerFachrichtung.class, LehrerFachrichtung.values(), data.mapData, data.mapStatistikIDs);
		LehrerFachrichtung.init(manager);
	}

	public readLehrerLehrbefaehigung() {
		const data = this.read('LehrerLehrbefaehigung', (json) => LehrerLehrbefaehigungKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<LehrerLehrbefaehigungKatalogEintrag, LehrerLehrbefaehigung>(data.version, LehrerLehrbefaehigung.class, LehrerLehrbefaehigung.values(), data.mapData, data.mapStatistikIDs);
		LehrerLehrbefaehigung.init(manager);
	}

	public readLehrerFachrichtungAnerkennung() {
		const data = this.read('LehrerFachrichtungAnerkennung', (json) => LehrerFachrichtungAnerkennungKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<LehrerFachrichtungAnerkennungKatalogEintrag, LehrerFachrichtungAnerkennung>(data.version, LehrerFachrichtungAnerkennung.class, LehrerFachrichtungAnerkennung.values(), data.mapData, data.mapStatistikIDs);
		LehrerFachrichtungAnerkennung.init(manager);
	}

	public readLehrerLehramt() {
		const data = this.read('LehrerLehramt', (json) => LehrerLehramtKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<LehrerLehramtKatalogEintrag, LehrerLehramt>(data.version, LehrerLehramt.class, LehrerLehramt.values(), data.mapData, data.mapStatistikIDs);
		LehrerLehramt.init(manager);
	}

	public readLehrerLehramtAnerkennung() {
		const data = this.read('LehrerLehramtAnerkennung', (json) => LehrerLehramtAnerkennungKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<LehrerLehramtAnerkennungKatalogEintrag, LehrerLehramtAnerkennung>(data.version, LehrerLehramtAnerkennung.class, LehrerLehramtAnerkennung.values(), data.mapData, data.mapStatistikIDs);
		LehrerLehramtAnerkennung.init(manager);
	}

	public readLehrerLehrbefaehigungAnerkennung() {
		const data = this.read('LehrerLehrbefaehigungAnerkennung', (json) => LehrerLehrbefaehigungAnerkennungKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<LehrerLehrbefaehigungAnerkennungKatalogEintrag, LehrerLehrbefaehigungAnerkennung>(data.version, LehrerLehrbefaehigungAnerkennung.class, LehrerLehrbefaehigungAnerkennung.values(), data.mapData, data.mapStatistikIDs);
		LehrerLehrbefaehigungAnerkennung.init(manager);
	}

	public readLehrerLeitungsfunktion() {
		const data = this.read('LehrerLeitungsfunktion', (json) => LehrerLeitungsfunktionKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<LehrerLeitungsfunktionKatalogEintrag, LehrerLeitungsfunktion>(data.version, LehrerLeitungsfunktion.class, LehrerLeitungsfunktion.values(), data.mapData, data.mapStatistikIDs);
		LehrerLeitungsfunktion.init(manager);
	}

	public readLehrerRechtsverhaeltnis() {
		const data = this.read('LehrerRechtsverhaeltnis', (json) => LehrerRechtsverhaeltnisKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<LehrerRechtsverhaeltnisKatalogEintrag, LehrerRechtsverhaeltnis>(data.version, LehrerRechtsverhaeltnis.class, LehrerRechtsverhaeltnis.values(), data.mapData, data.mapStatistikIDs);
		LehrerRechtsverhaeltnis.init(manager);
	}

	public readLehrerZugangsgrund() {
		const data = this.read('LehrerZugangsgrund', (json) => LehrerZugangsgrundKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<LehrerZugangsgrundKatalogEintrag, LehrerZugangsgrund>(data.version, LehrerZugangsgrund.class, LehrerZugangsgrund.values(), data.mapData, data.mapStatistikIDs);
		LehrerZugangsgrund.init(manager);
	}

	public readBilingualeSprache() {
		const data = this.read('BilingualeSprache', (json) => BilingualeSpracheKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<BilingualeSpracheKatalogEintrag, BilingualeSprache>(data.version, BilingualeSprache.class, BilingualeSprache.values(), data.mapData, data.mapStatistikIDs);
		BilingualeSprache.init(manager);
	}

	public readKAOABerufsfeld() {
		const data = this.read('KAOABerufsfeld', (json) => KAOABerufsfeldKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<KAOABerufsfeldKatalogEintrag, KAOABerufsfeld>(data.version, KAOABerufsfeld.class, KAOABerufsfeld.values(), data.mapData, data.mapStatistikIDs);
		KAOABerufsfeld.init(manager);
	}

	public readKAOAMerkmaleOptionsarten() {
		const data = this.read('KAOAMerkmaleOptionsarten', (json) => KAOAMerkmaleOptionsartenKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<KAOAMerkmaleOptionsartenKatalogEintrag, KAOAMerkmaleOptionsarten>(data.version, KAOAMerkmaleOptionsarten.class, KAOAMerkmaleOptionsarten.values(), data.mapData, data.mapStatistikIDs);
		KAOAMerkmaleOptionsarten.init(manager);
	}

	public readKAOAZusatzmerkmaleOptionsarten() {
		const data = this.read('KAOAZusatzmerkmaleOptionsarten', (json) => KAOAZusatzmerkmaleOptionsartenKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<KAOAZusatzmerkmaleOptionsartenKatalogEintrag, KAOAZusatzmerkmaleOptionsarten>(data.version, KAOAZusatzmerkmaleOptionsarten.class, KAOAZusatzmerkmaleOptionsarten.values(), data.mapData, data.mapStatistikIDs);
		KAOAZusatzmerkmaleOptionsarten.init(manager);
	}

	public readKAOAEbene4() {
		const data = this.read('KAOAEbene4', (json) => KAOAEbene4KatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<KAOAEbene4KatalogEintrag, KAOAEbene4>(data.version, KAOAEbene4.class, KAOAEbene4.values(), data.mapData, data.mapStatistikIDs);
		KAOAEbene4.init(manager);
	}

	public readKAOAZusatzmerkmal() {
		const data = this.read('KAOAZusatzmerkmal', (json) => KAOAZusatzmerkmalKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<KAOAZusatzmerkmalKatalogEintrag, KAOAZusatzmerkmal>(data.version, KAOAZusatzmerkmal.class, KAOAZusatzmerkmal.values(), data.mapData, data.mapStatistikIDs);
		KAOAZusatzmerkmal.init(manager);
	}

	public readKAOAAnschlussoptionen() {
		const data = this.read('KAOAAnschlussoptionen', (json) => KAOAAnschlussoptionenKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<KAOAAnschlussoptionenKatalogEintrag, KAOAAnschlussoptionen>(data.version, KAOAAnschlussoptionen.class, KAOAAnschlussoptionen.values(), data.mapData, data.mapStatistikIDs);
		KAOAAnschlussoptionen.init(manager);
	}

	public readKAOAKategorie() {
		const data = this.read('KAOAKategorie', (json) => KAOAKategorieKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<KAOAKategorieKatalogEintrag, KAOAKategorie>(data.version, KAOAKategorie.class, KAOAKategorie.values(), data.mapData, data.mapStatistikIDs);
		KAOAKategorie.init(manager);
	}

	public readKAOAMerkmal() {
		const data = this.read('KAOAMerkmal', (json) => KAOAMerkmalKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<KAOAMerkmalKatalogEintrag, KAOAMerkmal>(data.version, KAOAMerkmal.class, KAOAMerkmal.values(), data.mapData, data.mapStatistikIDs);
		KAOAMerkmal.init(manager);
	}

	public readKlassenart() {
		const data = this.read('Klassenart', (json) => KlassenartKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<KlassenartKatalogEintrag, Klassenart>(data.version, Klassenart.class, Klassenart.values(), data.mapData, data.mapStatistikIDs);
		Klassenart.init(manager);
	}

	public readUebergangsempfehlung() {
		const data = this.read('Uebergangsempfehlung', (json) => UebergangsempfehlungKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<UebergangsempfehlungKatalogEintrag, Uebergangsempfehlung>(data.version, Uebergangsempfehlung.class, Uebergangsempfehlung.values(), data.mapData, data.mapStatistikIDs);
		Uebergangsempfehlung.init(manager);
	}

	public readZulaessigeKursart() {
		const data = this.read('ZulaessigeKursart', (json) => ZulaessigeKursartKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<ZulaessigeKursartKatalogEintrag, ZulaessigeKursart>(data.version, ZulaessigeKursart.class, ZulaessigeKursart.values(), data.mapData, data.mapStatistikIDs);
		ZulaessigeKursart.init(manager);
	}

	public readFoerderschwerpunkt() {
		const data = this.read('Foerderschwerpunkt', (json) => FoerderschwerpunktKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<FoerderschwerpunktKatalogEintrag, Foerderschwerpunkt>(data.version, Foerderschwerpunkt.class, Foerderschwerpunkt.values(), data.mapData, data.mapStatistikIDs);
		Foerderschwerpunkt.init(manager);
	}

	public readTermin() {
		const data = this.read('Termin', (json) => TerminKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<TerminKatalogEintrag, Termin>(data.version, Termin.class, Termin.values(), data.mapData, data.mapStatistikIDs);
		Termin.init(manager);
	}

	public readLehrerAnrechnungsgrund() {
		const data = this.read('LehrerAnrechnungsgrund', (json) => LehrerAnrechnungsgrundKatalogEintrag.transpilerFromJSON(json));
		CoreTypeSimple.initValues(new LehrerAnrechnungsgrund(), LehrerAnrechnungsgrund.class, data.mapData);
		const manager = new CoreTypeDataManager<LehrerAnrechnungsgrundKatalogEintrag, LehrerAnrechnungsgrund>(data.version, LehrerAnrechnungsgrund.class, LehrerAnrechnungsgrund.values(), data.mapData, data.mapStatistikIDs);
		LehrerAnrechnungsgrund.init(manager);
	}

	public readLehrerMehrleistungsarten() {
		const data = this.read('LehrerMehrleistungsarten', (json) => LehrerMehrleistungsartKatalogEintrag.transpilerFromJSON(json));
		CoreTypeSimple.initValues(new LehrerMehrleistungsarten(), LehrerMehrleistungsarten.class, data.mapData);
		const manager = new CoreTypeDataManager<LehrerMehrleistungsartKatalogEintrag, LehrerMehrleistungsarten>(data.version, LehrerMehrleistungsarten.class, LehrerMehrleistungsarten.values(), data.mapData, data.mapStatistikIDs);
		LehrerMehrleistungsarten.init(manager);
	}

	public readLehrerMinderleistungsarten() {
		const data = this.read('LehrerMinderleistungsarten', (json) => LehrerMinderleistungsartKatalogEintrag.transpilerFromJSON(json));
		CoreTypeSimple.initValues(new LehrerMinderleistungsarten(), LehrerMinderleistungsarten.class, data.mapData);
		const manager = new CoreTypeDataManager<LehrerMinderleistungsartKatalogEintrag, LehrerMinderleistungsarten>(data.version, LehrerMinderleistungsarten.class, LehrerMinderleistungsarten.values(), data.mapData, data.mapStatistikIDs);
		LehrerMinderleistungsarten.init(manager);
	}

	public readLehrerPflichtstundensollVollzeit() {
		const data = this.read('LehrerPflichtstundensollVollzeit', (json) => LehrerPflichtstundensollVollzeitKatalogEintrag.transpilerFromJSON(json));
		CoreTypeSimple.initValues(new LehrerPflichtstundensollVollzeit(), LehrerPflichtstundensollVollzeit.class, data.mapData);
		const manager = new CoreTypeDataManager<LehrerPflichtstundensollVollzeitKatalogEintrag, LehrerPflichtstundensollVollzeit>(data.version, LehrerPflichtstundensollVollzeit.class, LehrerPflichtstundensollVollzeit.values(), data.mapData, data.mapStatistikIDs);
		LehrerPflichtstundensollVollzeit.init(manager);
	}

	public readNationalitaeten() {
		const data = this.read('Nationalitaeten', (json) => NationalitaetenKatalogEintrag.transpilerFromJSON(json));
		CoreTypeSimple.initValues(new Nationalitaeten(), Nationalitaeten.class, data.mapData);
		const manager = new CoreTypeDataManager<NationalitaetenKatalogEintrag, Nationalitaeten>(data.version, Nationalitaeten.class, Nationalitaeten.values(), data.mapData, data.mapStatistikIDs);
		Nationalitaeten.init(manager);
	}

	public readValidatorenFehlerartKontext() {
		const name = "ValidatorenFehlerartKontext";
		const json: string | undefined = this.mapCoreTypeNameJsonData.get(name);
		if (json === undefined)
			throw new DeveloperNotificationException(`Für die Validator-Fehlerarten "${name}" liegen keine JSON Daten vor. Wurde die Map mit den Daten gefüllt?`);
		const data: JsonValidatorFehlerartData = JSON.parse(json);
		const nameValidatoren = new HashSet<string>();
		const mapData = new HashMap<string, List<ValidatorFehlerartKontext>>();
		for (const eintrag of data.daten) {
			const validator = eintrag.validator;
			const historie = eintrag.historie;
			if (nameValidatoren.contains(validator) === true)
				throw new DeveloperNotificationException("Fehler beim Einlesen der Fehlerarten für die Validatoren " + name);
			nameValidatoren.add(validator);
			const list = new ArrayList<ValidatorFehlerartKontext>();
			mapData.put(validator, list);
			for (const obj of historie) {
				const tmpJson = JSON.stringify(obj);
				const deserialized = ValidatorFehlerartKontext.transpilerFromJSON(tmpJson);
				list.add(deserialized);
			}
		}
		ValidatorManager.init(data.version, mapData);
	}

	public async loadAll() : Promise<Map<string, string>> {
		const arr = [];
		for (const key of this.keys)
			arr.push(this.loadJson(key));
		await Promise.all(arr);
		return this.mapCoreTypeNameJsonData;
	}

	public readAll() {
		try {
			this.readSchulform();
			this.readBerufskollegAnlage();
			this.readAllgemeinbildendOrganisationsformen();
			this.readBerufskollegOrganisationsformen();
			this.readWeiterbildungskollegOrganisationsformen();
			this.readSchulabschlussAllgemeinbildend();
			this.readSchulabschlussBerufsbildend();
			this.readEinschulungsart();
			this.readHerkunftBildungsgang();
			this.readHerkunftBildungsgangTyp();
			this.readJahrgaenge();
			this.readPrimarstufeSchuleingangsphaseBesuchsjahre();
			this.readReligion();
			this.readKindergartenbesuch();
			this.readSchuelerStatus();
			this.readNote();
			this.readSprachreferenzniveau();
			this.readBerufskollegBildungsgangTyp();
			this.readWeiterbildungskollegBildungsgangTyp();
			this.readSchulgliederung();
			this.readVerkehrssprache();
			this.readFachgruppe();
			this.readFach();
			this.readLehrerAbgangsgrund();
			this.readLehrerBeschaeftigungsart();
			this.readLehrerEinsatzstatus();
			this.readLehrerFachrichtung();
			this.readLehrerLehrbefaehigung();
			this.readLehrerFachrichtungAnerkennung();
			this.readLehrerLehramt();
			this.readLehrerLehramtAnerkennung();
			this.readLehrerLehrbefaehigungAnerkennung();
			this.readLehrerLeitungsfunktion();
			this.readLehrerRechtsverhaeltnis();
			this.readLehrerZugangsgrund();
			this.readBilingualeSprache();
			this.readKAOABerufsfeld();
			this.readKAOAMerkmaleOptionsarten();
			this.readKAOAZusatzmerkmaleOptionsarten();
			this.readKAOAEbene4();
			this.readKAOAZusatzmerkmal();
			this.readKAOAAnschlussoptionen();
			this.readKAOAKategorie();
			this.readKAOAMerkmal();
			this.readKlassenart();
			this.readUebergangsempfehlung();
			this.readZulaessigeKursart();
			this.readFoerderschwerpunkt();
			this.readTermin();
			this.readLehrerAnrechnungsgrund();
			this.readLehrerMehrleistungsarten();
			this.readLehrerMinderleistungsarten();
			this.readLehrerPflichtstundensollVollzeit();
			this.readNationalitaeten();
			this.readValidatorenFehlerartKontext();
		} catch (e) {
			console.log(e)
		}
	}
}
