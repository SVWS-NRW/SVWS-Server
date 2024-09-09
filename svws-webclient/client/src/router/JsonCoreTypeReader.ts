import type { List } from "@core";
import { HashMap, BaseApi, DeveloperNotificationException, HashSet, ArrayList, SchulformKatalogEintrag, CoreTypeDataManager, Schulform, BerufskollegAnlageKatalogEintrag, BerufskollegAnlage, OrganisationsformKatalogEintrag, AllgemeinbildendOrganisationsformen, BerufskollegOrganisationsformen, WeiterbildungskollegOrganisationsformen, SchulabschlussAllgemeinbildendKatalogEintrag, SchulabschlussAllgemeinbildend, SchulabschlussBerufsbildendKatalogEintrag, SchulabschlussBerufsbildend, HerkunftBildungsgangKatalogEintrag, HerkunftBildungsgang, HerkunftBildungsgangTypKatalogEintrag, HerkunftBildungsgangTyp, JahrgaengeKatalogEintrag, Jahrgaenge, PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag, PrimarstufeSchuleingangsphaseBesuchsjahre, ReligionKatalogEintrag, Religion, KindergartenbesuchKatalogEintrag, Kindergartenbesuch, SchuelerStatusKatalogEintrag, SchuelerStatus, NoteKatalogEintrag, Note, SprachreferenzniveauKatalogEintrag, Sprachreferenzniveau, BildungsgangTypKatalogEintrag, BerufskollegBildungsgangTyp, WeiterbildungskollegBildungsgangTyp, SchulgliederungKatalogEintrag, Schulgliederung, FachgruppeKatalogEintrag, Fachgruppe, FachKatalogEintrag, Fach, LehrerAbgangsgrundKatalogEintrag, LehrerAbgangsgrund, LehrerBeschaeftigungsartKatalogEintrag, LehrerBeschaeftigungsart, LehrerEinsatzstatusKatalogEintrag, LehrerEinsatzstatus, LehrerFachrichtungKatalogEintrag, LehrerFachrichtung, LehrerLehrbefaehigungKatalogEintrag, LehrerLehrbefaehigung, LehrerFachrichtungAnerkennungKatalogEintrag, LehrerFachrichtungAnerkennung, LehrerLehramtKatalogEintrag, LehrerLehramt, LehrerLehramtAnerkennungKatalogEintrag, LehrerLehramtAnerkennung, LehrerLehrbefaehigungAnerkennungKatalogEintrag, LehrerLehrbefaehigungAnerkennung, LehrerLeitungsfunktionKatalogEintrag, LehrerLeitungsfunktion, LehrerRechtsverhaeltnisKatalogEintrag, LehrerRechtsverhaeltnis, LehrerZugangsgrundKatalogEintrag, LehrerZugangsgrund, BilingualeSpracheKatalogEintrag, BilingualeSprache, KAOABerufsfeldKatalogEintrag, KAOABerufsfeld, KAOAMerkmaleOptionsartenKatalogEintrag, KAOAMerkmaleOptionsarten, KAOAZusatzmerkmaleOptionsartenKatalogEintrag, KAOAZusatzmerkmaleOptionsarten, KAOAEbene4KatalogEintrag, KAOAEbene4, KAOAZusatzmerkmalKatalogEintrag, KAOAZusatzmerkmal, KAOAAnschlussoptionenKatalogEintrag, KAOAAnschlussoptionen, KAOAKategorieKatalogEintrag, KAOAKategorie, KAOAMerkmalKatalogEintrag, KAOAMerkmal, KlassenartKatalogEintrag, Klassenart, UebergangsempfehlungKatalogEintrag, Uebergangsempfehlung, ZulaessigeKursartKatalogEintrag, ZulaessigeKursart, FoerderschwerpunktKatalogEintrag, Foerderschwerpunkt, LehrerAnrechnungsgrundKatalogEintrag, LehrerAnrechnungsgrund, LehrerMehrleistungsartKatalogEintrag, LehrerMehrleistungsarten, LehrerMinderleistungsartKatalogEintrag, LehrerMinderleistungsarten, ValidatorFehlerartKontext, ValidatorManager, CoreTypeSimple } from "@core";

interface JsonCoreTypeEntry<T> {
	bezeichner: string;
	idHistorie: number;
	historie: T[];
}

interface JsonCoreTypeData<T> {
	version: number;
	daten: JsonCoreTypeEntry<T>[];
}

interface JsonCoreTypeDataResult<T> {
	version: number;
	mapData: HashMap<string, List<T>>;
	mapHistorienIDs: HashMap<string, number>;
}

export class JsonCoreTypeReader {

	private readonly api: BaseApi;

	public constructor(url: string) {
		this.api = new BaseApi(url, "", "");
	}

	private async read<T>(name: string, mapper: (json: string) => T) : Promise<JsonCoreTypeDataResult<T>> {
		if (name === '')
			throw new DeveloperNotificationException("Für das Einlesen eines Core-Types muss ein gültiger Name angegeben werden");
		const json = await this.api.getJSON('/types/' + name + '.json');
		const data = JSON.parse(json) as JsonCoreTypeData<T>;
		const idsHistorien = new HashSet<number>();
		const result = <JsonCoreTypeDataResult<T>>{
			version: data.version,
			mapData: new HashMap<string, List<T>>(),
			mapHistorienIDs: new HashMap<string, number>(),
		};
		for (const eintrag of data.daten) {
			const bezeichner = eintrag.bezeichner;
			const idHistorie = eintrag.idHistorie;
			const historie = eintrag.historie;
			if (idsHistorien.contains(idHistorie))
				throw new DeveloperNotificationException("Fehler beim Einlesen der Core-Type-Daten für den Core-Type " + name);
			idsHistorien.add(idHistorie);
			result.mapHistorienIDs.put(bezeichner, idHistorie);
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

	public async readSchulform() {
		const data = await this.read('Schulform', (json) => SchulformKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<SchulformKatalogEintrag, Schulform>(data.version, Schulform.class, Schulform.values(), data.mapData, data.mapHistorienIDs);
		Schulform.init(manager);
	}

	public async readBerufskollegAnlage() {
		const data = await this.read('BerufskollegAnlage', (json) => BerufskollegAnlageKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<BerufskollegAnlageKatalogEintrag, BerufskollegAnlage>(data.version, BerufskollegAnlage.class, BerufskollegAnlage.values(), data.mapData, data.mapHistorienIDs);
		BerufskollegAnlage.init(manager);
	}

	public async readAllgemeinbildendOrganisationsformen() {
		const data = await this.read('AllgemeinbildendOrganisationsformen', (json) => OrganisationsformKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<OrganisationsformKatalogEintrag, AllgemeinbildendOrganisationsformen>(data.version, AllgemeinbildendOrganisationsformen.class, AllgemeinbildendOrganisationsformen.values(), data.mapData, data.mapHistorienIDs);
		AllgemeinbildendOrganisationsformen.init(manager);
	}

	public async readBerufskollegOrganisationsformen() {
		const data = await this.read('BerufskollegOrganisationsformen', (json) => OrganisationsformKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<OrganisationsformKatalogEintrag, BerufskollegOrganisationsformen>(data.version, BerufskollegOrganisationsformen.class, BerufskollegOrganisationsformen.values(), data.mapData, data.mapHistorienIDs);
		BerufskollegOrganisationsformen.init(manager);
	}

	public async readWeiterbildungskollegOrganisationsformen() {
		const data = await this.read('WeiterbildungskollegOrganisationsformen', (json) => OrganisationsformKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<OrganisationsformKatalogEintrag, WeiterbildungskollegOrganisationsformen>(data.version, WeiterbildungskollegOrganisationsformen.class,
			WeiterbildungskollegOrganisationsformen.values(), data.mapData, data.mapHistorienIDs);
		WeiterbildungskollegOrganisationsformen.init(manager);
	}


	public async readSchulabschlussAllgemeinbildend() {
		const data = await this.read('SchulabschlussAllgemeinbildend', (json) => SchulabschlussAllgemeinbildendKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<SchulabschlussAllgemeinbildendKatalogEintrag, SchulabschlussAllgemeinbildend>(data.version, SchulabschlussAllgemeinbildend.class, SchulabschlussAllgemeinbildend.values(), data.mapData, data.mapHistorienIDs);
		SchulabschlussAllgemeinbildend.init(manager);
	}

	public async readSchulabschlussBerufsbildend() {
		const data = await this.read('SchulabschlussBerufsbildend', (json) => SchulabschlussBerufsbildendKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<SchulabschlussBerufsbildendKatalogEintrag, SchulabschlussBerufsbildend>(data.version, SchulabschlussBerufsbildend.class, SchulabschlussBerufsbildend.values(), data.mapData, data.mapHistorienIDs);
		SchulabschlussBerufsbildend.init(manager);
	}

	public async readHerkunftBildungsgang() {
		const data = await this.read('HerkunftBildungsgang', (json) => HerkunftBildungsgangKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<HerkunftBildungsgangKatalogEintrag, HerkunftBildungsgang>(data.version, HerkunftBildungsgang.class, HerkunftBildungsgang.values(), data.mapData, data.mapHistorienIDs);
		HerkunftBildungsgang.init(manager);
	}

	public async readHerkunftBildungsgangTyp() {
		const data = await this.read('HerkunftBildungsgangTyp', (json) => HerkunftBildungsgangTypKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<HerkunftBildungsgangTypKatalogEintrag, HerkunftBildungsgangTyp>(data.version, HerkunftBildungsgangTyp.class, HerkunftBildungsgangTyp.values(), data.mapData, data.mapHistorienIDs);
		HerkunftBildungsgangTyp.init(manager);
	}


	public async readJahrgaenge() {
		const data = await this.read('Jahrgaenge', (json) => JahrgaengeKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<JahrgaengeKatalogEintrag, Jahrgaenge>(data.version, Jahrgaenge.class, Jahrgaenge.values(), data.mapData, data.mapHistorienIDs);
		Jahrgaenge.init(manager);
	}

	public async readPrimarstufeSchuleingangsphaseBesuchsjahre() {
		const data = await this.read('PrimarstufeSchuleingangsphaseBesuchsjahre', (json) => PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag, PrimarstufeSchuleingangsphaseBesuchsjahre>(data.version, PrimarstufeSchuleingangsphaseBesuchsjahre.class, PrimarstufeSchuleingangsphaseBesuchsjahre.values(), data.mapData, data.mapHistorienIDs);
		PrimarstufeSchuleingangsphaseBesuchsjahre.init(manager);
	}

	public async readReligion() {
		const data = await this.read('Religion', (json) => ReligionKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<ReligionKatalogEintrag, Religion>(data.version, Religion.class, Religion.values(), data.mapData, data.mapHistorienIDs);
		Religion.init(manager);
	}

	public async readKindergartenbesuch() {
		const data = await this.read('Kindergartenbesuch', (json) => KindergartenbesuchKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<KindergartenbesuchKatalogEintrag, Kindergartenbesuch>(data.version, Kindergartenbesuch.class, Kindergartenbesuch.values(), data.mapData, data.mapHistorienIDs);
		Kindergartenbesuch.init(manager);
	}

	public async readSchuelerStatus() {
		const data = await this.read('SchuelerStatus', (json) => SchuelerStatusKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<SchuelerStatusKatalogEintrag, SchuelerStatus>(data.version, SchuelerStatus.class, SchuelerStatus.values(), data.mapData, data.mapHistorienIDs);
		SchuelerStatus.init(manager);
	}

	public async readNote() {
		const data = await this.read('Note', (json) => NoteKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<NoteKatalogEintrag, Note>(data.version, Note.class, Note.values(), data.mapData, data.mapHistorienIDs);
		Note.init(manager);
	}

	public async readSprachreferenzniveau() {
		const data = await this.read('Sprachreferenzniveau', (json) => SprachreferenzniveauKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<SprachreferenzniveauKatalogEintrag, Sprachreferenzniveau>(data.version, Sprachreferenzniveau.class, Sprachreferenzniveau.values(), data.mapData, data.mapHistorienIDs);
		Sprachreferenzniveau.init(manager);
	}

	public async readBerufskollegBildungsgangTyp() {
		const data = await this.read('BerufskollegBildungsgangTyp', (json) => BildungsgangTypKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<BildungsgangTypKatalogEintrag, BerufskollegBildungsgangTyp>(data.version, BerufskollegBildungsgangTyp.class, BerufskollegBildungsgangTyp.values(), data.mapData, data.mapHistorienIDs);
		BerufskollegBildungsgangTyp.init(manager);
	}

	public async readWeiterbildungskollegBildungsgangTyp() {
		const data = await this.read('WeiterbildungskollegBildungsgangTyp', (json) => BildungsgangTypKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<BildungsgangTypKatalogEintrag, WeiterbildungskollegBildungsgangTyp>(data.version, WeiterbildungskollegBildungsgangTyp.class, WeiterbildungskollegBildungsgangTyp.values(), data.mapData, data.mapHistorienIDs);
		WeiterbildungskollegBildungsgangTyp.init(manager);
	}

	public async readSchulgliederung() {
		const data = await this.read('Schulgliederung', (json) => SchulgliederungKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<SchulgliederungKatalogEintrag, Schulgliederung>(data.version, Schulgliederung.class, Schulgliederung.values(), data.mapData, data.mapHistorienIDs);
		Schulgliederung.init(manager);
	}

	public async readFachgruppe() {
		const data = await this.read('Fachgruppe', (json) => FachgruppeKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<FachgruppeKatalogEintrag, Fachgruppe>(data.version, Fachgruppe.class, Fachgruppe.values(), data.mapData, data.mapHistorienIDs);
		Fachgruppe.init(manager);
	}

	public async readFach() {
		const data = await this.read('Fach', (json) => FachKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<FachKatalogEintrag, Fach>(data.version, Fach.class, Fach.values(), data.mapData, data.mapHistorienIDs);
		Fach.init(manager);
	}

	public async readLehrerAbgangsgrund() {
		const data = await this.read('LehrerAbgangsgrund', (json) => LehrerAbgangsgrundKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<LehrerAbgangsgrundKatalogEintrag, LehrerAbgangsgrund>(data.version, LehrerAbgangsgrund.class, LehrerAbgangsgrund.values(), data.mapData, data.mapHistorienIDs);
		LehrerAbgangsgrund.init(manager);
	}

	public async readLehrerBeschaeftigungsart() {
		const data = await this.read('LehrerBeschaeftigungsart', (json) => LehrerBeschaeftigungsartKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<LehrerBeschaeftigungsartKatalogEintrag, LehrerBeschaeftigungsart>(data.version, LehrerBeschaeftigungsart.class, LehrerBeschaeftigungsart.values(), data.mapData, data.mapHistorienIDs);
		LehrerBeschaeftigungsart.init(manager);
	}

	public async readLehrerEinsatzstatus() {
		const data = await this.read('LehrerEinsatzstatus', (json) => LehrerEinsatzstatusKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<LehrerEinsatzstatusKatalogEintrag, LehrerEinsatzstatus>(data.version, LehrerEinsatzstatus.class, LehrerEinsatzstatus.values(), data.mapData, data.mapHistorienIDs);
		LehrerEinsatzstatus.init(manager);
	}

	public async readLehrerFachrichtung() {
		const data = await this.read('LehrerFachrichtung', (json) => LehrerFachrichtungKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<LehrerFachrichtungKatalogEintrag, LehrerFachrichtung>(data.version, LehrerFachrichtung.class, LehrerFachrichtung.values(), data.mapData, data.mapHistorienIDs);
		LehrerFachrichtung.init(manager);
	}

	public async readLehrerLehrbefaehigung() {
		const data = await this.read('LehrerLehrbefaehigung', (json) => LehrerLehrbefaehigungKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<LehrerLehrbefaehigungKatalogEintrag, LehrerLehrbefaehigung>(data.version, LehrerLehrbefaehigung.class, LehrerLehrbefaehigung.values(), data.mapData, data.mapHistorienIDs);
		LehrerLehrbefaehigung.init(manager);
	}

	public async readLehrerFachrichtungAnerkennung() {
		const data = await this.read('LehrerFachrichtungAnerkennung', (json) => LehrerFachrichtungAnerkennungKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<LehrerFachrichtungAnerkennungKatalogEintrag, LehrerFachrichtungAnerkennung>(data.version, LehrerFachrichtungAnerkennung.class, LehrerFachrichtungAnerkennung.values(), data.mapData, data.mapHistorienIDs);
		LehrerFachrichtungAnerkennung.init(manager);
	}

	public async readLehrerLehramt() {
		const data = await this.read('LehrerLehramt', (json) => LehrerLehramtKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<LehrerLehramtKatalogEintrag, LehrerLehramt>(data.version, LehrerLehramt.class, LehrerLehramt.values(), data.mapData, data.mapHistorienIDs);
		LehrerLehramt.init(manager);
	}

	public async readLehrerLehramtAnerkennung() {
		const data = await this.read('LehrerLehramtAnerkennung', (json) => LehrerLehramtAnerkennungKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<LehrerLehramtAnerkennungKatalogEintrag, LehrerLehramtAnerkennung>(data.version, LehrerLehramtAnerkennung.class, LehrerLehramtAnerkennung.values(), data.mapData, data.mapHistorienIDs);
		LehrerLehramtAnerkennung.init(manager);
	}

	public async readLehrerLehrbefaehigungAnerkennung() {
		const data = await this.read('LehrerLehrbefaehigungAnerkennung', (json) => LehrerLehrbefaehigungAnerkennungKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<LehrerLehrbefaehigungAnerkennungKatalogEintrag, LehrerLehrbefaehigungAnerkennung>(data.version, LehrerLehrbefaehigungAnerkennung.class, LehrerLehrbefaehigungAnerkennung.values(), data.mapData, data.mapHistorienIDs);
		LehrerLehrbefaehigungAnerkennung.init(manager);
	}

	public async readLehrerLeitungsfunktion() {
		const data = await this.read('LehrerLeitungsfunktion', (json) => LehrerLeitungsfunktionKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<LehrerLeitungsfunktionKatalogEintrag, LehrerLeitungsfunktion>(data.version, LehrerLeitungsfunktion.class, LehrerLeitungsfunktion.values(), data.mapData, data.mapHistorienIDs);
		LehrerLeitungsfunktion.init(manager);
	}

	public async readLehrerRechtsverhaeltnis() {
		const data = await this.read('LehrerRechtsverhaeltnis', (json) => LehrerRechtsverhaeltnisKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<LehrerRechtsverhaeltnisKatalogEintrag, LehrerRechtsverhaeltnis>(data.version, LehrerRechtsverhaeltnis.class, LehrerRechtsverhaeltnis.values(), data.mapData, data.mapHistorienIDs);
		LehrerRechtsverhaeltnis.init(manager);
	}

	public async readLehrerZugangsgrund() {
		const data = await this.read('LehrerZugangsgrund', (json) => LehrerZugangsgrundKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<LehrerZugangsgrundKatalogEintrag, LehrerZugangsgrund>(data.version, LehrerZugangsgrund.class, LehrerZugangsgrund.values(), data.mapData, data.mapHistorienIDs);
		LehrerZugangsgrund.init(manager);
	}

	public async readBilingualeSprache() {
		const data = await this.read('BilingualeSprache', (json) => BilingualeSpracheKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<BilingualeSpracheKatalogEintrag, BilingualeSprache>(data.version, BilingualeSprache.class, BilingualeSprache.values(), data.mapData, data.mapHistorienIDs);
		BilingualeSprache.init(manager);
	}


	public async readKAOABerufsfeld() {
		const data = await this.read('KAOABerufsfeld', (json) => KAOABerufsfeldKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<KAOABerufsfeldKatalogEintrag, KAOABerufsfeld>(data.version, KAOABerufsfeld.class, KAOABerufsfeld.values(), data.mapData, data.mapHistorienIDs);
		KAOABerufsfeld.init(manager);
	}

	public async readKAOAMerkmaleOptionsarten() {
		const data = await this.read('KAOAMerkmaleOptionsarten', (json) => KAOAMerkmaleOptionsartenKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<KAOAMerkmaleOptionsartenKatalogEintrag, KAOAMerkmaleOptionsarten>(data.version, KAOAMerkmaleOptionsarten.class, KAOAMerkmaleOptionsarten.values(), data.mapData, data.mapHistorienIDs);
		KAOAMerkmaleOptionsarten.init(manager);
	}

	public async readKAOAZusatzmerkmaleOptionsarten() {
		const data = await this.read('KAOAZusatzmerkmaleOptionsarten', (json) => KAOAZusatzmerkmaleOptionsartenKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<KAOAZusatzmerkmaleOptionsartenKatalogEintrag, KAOAZusatzmerkmaleOptionsarten>(data.version, KAOAZusatzmerkmaleOptionsarten.class, KAOAZusatzmerkmaleOptionsarten.values(), data.mapData, data.mapHistorienIDs);
		KAOAZusatzmerkmaleOptionsarten.init(manager);
	}

	public async readKAOAEbene4() {
		const data = await this.read('KAOAEbene4', (json) => KAOAEbene4KatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<KAOAEbene4KatalogEintrag, KAOAEbene4>(data.version, KAOAEbene4.class, KAOAEbene4.values(), data.mapData, data.mapHistorienIDs);
		KAOAEbene4.init(manager);
	}

	public async readKAOAZusatzmerkmal() {
		const data = await this.read('KAOAZusatzmerkmal', (json) => KAOAZusatzmerkmalKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<KAOAZusatzmerkmalKatalogEintrag, KAOAZusatzmerkmal>(data.version, KAOAZusatzmerkmal.class, KAOAZusatzmerkmal.values(), data.mapData, data.mapHistorienIDs);
		KAOAZusatzmerkmal.init(manager);
	}

	public async readKAOAAnschlussoptionen() {
		const data = await this.read('KAOAAnschlussoptionen', (json) => KAOAAnschlussoptionenKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<KAOAAnschlussoptionenKatalogEintrag, KAOAAnschlussoptionen>(data.version, KAOAAnschlussoptionen.class, KAOAAnschlussoptionen.values(), data.mapData, data.mapHistorienIDs);
		KAOAAnschlussoptionen.init(manager);
	}

	public async readKAOAKategorie() {
		const data = await this.read('KAOAKategorie', (json) => KAOAKategorieKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<KAOAKategorieKatalogEintrag, KAOAKategorie>(data.version, KAOAKategorie.class, KAOAKategorie.values(), data.mapData, data.mapHistorienIDs);
		KAOAKategorie.init(manager);
	}

	public async readKAOAMerkmal() {
		const data = await this.read('KAOAMerkmal', (json) => KAOAMerkmalKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<KAOAMerkmalKatalogEintrag, KAOAMerkmal>(data.version, KAOAMerkmal.class, KAOAMerkmal.values(), data.mapData, data.mapHistorienIDs);
		KAOAMerkmal.init(manager);
	}


	public async readKlassenart() {
		const data = await this.read('Klassenart', (json) => KlassenartKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<KlassenartKatalogEintrag, Klassenart>(data.version, Klassenart.class, Klassenart.values(), data.mapData, data.mapHistorienIDs);
		Klassenart.init(manager);
	}

	public async readUebergangsempfehlung() {
		const data = await this.read('Uebergangsempfehlung', (json) => UebergangsempfehlungKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<UebergangsempfehlungKatalogEintrag, Uebergangsempfehlung>(data.version, Uebergangsempfehlung.class, Uebergangsempfehlung.values(), data.mapData, data.mapHistorienIDs);
		Uebergangsempfehlung.init(manager);
	}

	public async readZulaessigeKursart() {
		const data = await this.read('ZulaessigeKursart', (json) => ZulaessigeKursartKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<ZulaessigeKursartKatalogEintrag, ZulaessigeKursart>(data.version, ZulaessigeKursart.class, ZulaessigeKursart.values(), data.mapData, data.mapHistorienIDs);
		ZulaessigeKursart.init(manager);
	}

	public async readFoerderschwerpunkt() {
		const data = await this.read('Foerderschwerpunkt', (json) => FoerderschwerpunktKatalogEintrag.transpilerFromJSON(json));
		const manager = new CoreTypeDataManager<FoerderschwerpunktKatalogEintrag, Foerderschwerpunkt>(data.version, Foerderschwerpunkt.class, Foerderschwerpunkt.values(), data.mapData, data.mapHistorienIDs);
		Foerderschwerpunkt.init(manager);
	}

	public async readLehrerAnrechnungsgrund() {
		const data = await this.read('LehrerAnrechnungsgrund', (json) => LehrerAnrechnungsgrundKatalogEintrag.transpilerFromJSON(json));
		CoreTypeSimple.initValues(new LehrerAnrechnungsgrund(), LehrerAnrechnungsgrund.class, data.mapData);
		const manager = new CoreTypeDataManager<LehrerAnrechnungsgrundKatalogEintrag, LehrerAnrechnungsgrund>(data.version, LehrerAnrechnungsgrund.class, LehrerAnrechnungsgrund.values(), data.mapData, data.mapHistorienIDs);
		LehrerAnrechnungsgrund.init(manager);
	}

	public async readLehrerMehrleistungsarten() {
		const data = await this.read('LehrerMehrleistungsarten', (json) => LehrerMehrleistungsartKatalogEintrag.transpilerFromJSON(json));
		CoreTypeSimple.initValues(new LehrerMehrleistungsarten(), LehrerMehrleistungsarten.class, data.mapData);
		const manager = new CoreTypeDataManager<LehrerMehrleistungsartKatalogEintrag, LehrerMehrleistungsarten>(data.version, LehrerMehrleistungsarten.class, LehrerMehrleistungsarten.values(), data.mapData, data.mapHistorienIDs);
		LehrerMehrleistungsarten.init(manager);
	}

	public async readLehrerMinderleistungsarten() {
		const data = await this.read('LehrerMinderleistungsarten', (json) => LehrerMinderleistungsartKatalogEintrag.transpilerFromJSON(json));
		CoreTypeSimple.initValues(new LehrerMinderleistungsarten(), LehrerMinderleistungsarten.class, data.mapData);
		const manager = new CoreTypeDataManager<LehrerMinderleistungsartKatalogEintrag, LehrerMinderleistungsarten>(data.version, LehrerMinderleistungsarten.class, LehrerMinderleistungsarten.values(), data.mapData, data.mapHistorienIDs);
		LehrerMinderleistungsarten.init(manager);
	}

	public async readValidatorenFehlerartKontext() {
		// TODO
		// const data = await this.readValidator('ValidatorenFehlerartKontext', (json) => ValidatorFehlerartKontext.transpilerFromJSON(json));
		// ValidatorManager.init(data.version, data.mapData);
	}

	public async readAll() {
		try {
			await this.readSchulform();
			await this.readBerufskollegAnlage();
			await this.readAllgemeinbildendOrganisationsformen();
			await this.readBerufskollegOrganisationsformen();
			await this.readWeiterbildungskollegOrganisationsformen();
			await this.readSchulabschlussAllgemeinbildend();
			await this.readSchulabschlussBerufsbildend();
			await this.readHerkunftBildungsgang();
			await this.readHerkunftBildungsgangTyp();
			await this.readJahrgaenge();
			await this.readPrimarstufeSchuleingangsphaseBesuchsjahre();
			await this.readReligion();
			await this.readKindergartenbesuch();
			await this.readSchuelerStatus();
			await this.readNote();
			await this.readSprachreferenzniveau();
			await this.readBerufskollegBildungsgangTyp();
			await this.readWeiterbildungskollegBildungsgangTyp();
			await this.readSchulgliederung();
			await this.readFachgruppe();
			await this.readFach();
			await this.readLehrerAbgangsgrund();
			await this.readLehrerBeschaeftigungsart();
			await this.readLehrerEinsatzstatus();
			await this.readLehrerFachrichtung();
			await this.readLehrerLehrbefaehigung();
			await this.readLehrerFachrichtungAnerkennung();
			await this.readLehrerLehramt();
			await this.readLehrerLehramtAnerkennung();
			await this.readLehrerLehrbefaehigungAnerkennung();
			await this.readLehrerLeitungsfunktion();
			await this.readLehrerRechtsverhaeltnis();
			await this.readLehrerZugangsgrund();
			await this.readBilingualeSprache();
			await this.readKAOABerufsfeld();
			await this.readKAOAMerkmaleOptionsarten();
			await this.readKAOAZusatzmerkmaleOptionsarten();
			await this.readKAOAEbene4();
			await this.readKAOAZusatzmerkmal();
			await this.readKAOAAnschlussoptionen();
			await this.readKAOAKategorie();
			await this.readKAOAMerkmal();
			await this.readKlassenart();
			await this.readUebergangsempfehlung();
			await this.readZulaessigeKursart();
			await this.readFoerderschwerpunkt();
			await this.readLehrerAnrechnungsgrund();
			await this.readLehrerMehrleistungsarten();
			await this.readLehrerMinderleistungsarten();
			await this.readValidatorenFehlerartKontext();
		} catch(e) {
			console.log(e)
		}
	}

}
