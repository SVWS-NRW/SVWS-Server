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
import { SchuelerStatusKatalogEintrag } from "../data/schueler/SchuelerStatusKatalogEintrag";
import { UebergangsempfehlungKatalogEintrag } from "../data/schueler/UebergangsempfehlungKatalogEintrag";
import { BerufskollegAnlageKatalogEintrag } from "../data/schule/BerufskollegAnlageKatalogEintrag";
import { BildungsgangTypKatalogEintrag } from "../data/schule/BildungsgangTypKatalogEintrag";
import { FoerderschwerpunktKatalogEintrag } from "../data/schule/FoerderschwerpunktKatalogEintrag";
import { KindergartenbesuchKatalogEintrag } from "../data/schule/KindergartenbesuchKatalogEintrag";
import { OrganisationsformKatalogEintrag } from "../data/schule/OrganisationsformKatalogEintrag";
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
import { ArrayList } from "../../java/util/ArrayList";
import { HashMap } from "../../java/util/HashMap";
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
import { BetreuungsartKatalogEintrag } from "../data/schueler/BetreuungsartKatalogEintrag";
import { Betreuungsart } from "../types/schueler/Betreuungsart";
import { FormOffenerGanztag } from "../types/schule/FormOffenerGanztag";
import { FormOffenerGanztagKatalogEintrag } from "../data/schule/FormOffenerGanztagKatalogEintrag";
import { Floskelgruppenart } from "../types/schule/Floskelgruppenart";
import { FloskelgruppenartKatalogEintrag } from "../data/schule/FloskelgruppenartKatalogEintrag";
import { Einwilligungsschluessel } from "../types/schule/Einwilligungsschluessel";
import { EinwilligungsschluesselKatalogEintrag } from "../data/schule/EinwilligungsschluesselKatalogEintrag";
import { Herkunftsarten } from "../types/schueler/Herkunftsarten";
import { HerkunftsartenKatalogEintrag } from "../data/schueler/HerkunftsartenKatalogEintrag";
import { HerkunftSonstige } from "../types/schueler/HerkunftSonstige";
import { HerkunftSonstigeKatalogEintrag } from "../data/schueler/HerkunftSonstigeKatalogEintrag";
import { HerkunftSchulform } from "../types/schueler/HerkunftSchulform";
import { HerkunftSchulformKatalogEintrag } from "../data/schueler/HerkunftSchulformKatalogEintrag";
import { Bildungsstufe } from "../types/schule/Bildungsstufe";
import { BildungsstufeKatalogEintrag } from "../data/schule/BildungsstufeKatalogEintrag";
import { BerufskollegBerufsebene1 } from "../types/schule/BerufskollegBerufsebene1";
import { BerufskollegBerufsebene2 } from "../types/schule/BerufskollegBerufsebene2";
import { BerufskollegBerufsebene3 } from "../types/schule/BerufskollegBerufsebene3";
import { BerufskollegBerufsebeneKatalogEintrag } from "../data/schule/BerufskollegBerufsebeneKatalogEintrag";
import { HerkunftsschulnummerKatalogEintrag } from "../data/schule/HerkunftsschulnummerKatalogEintrag";
import { Herkunftsschulnummer } from "../types/schule/Herkunftsschulnummer";
import { ReformpaedagogikKatalogEintrag } from "../data/schule/ReformpaedagogikKatalogEintrag";
import { Reformpaedagogik } from "../types/schule/Reformpaedagogik";
import { CoreTypeData } from "../data/CoreTypeData";
import { DQRNiveauKatalogEintrag } from "../data/schule/DQRNiveauKatalogEintrag";
import { DQRNiveau } from "../types/schule/DQRNiveau";
import { FachklasseKatalogEintrag } from "../data/schule/FachklasseKatalogEintrag";
import { Fachklasse } from "../types/schule/Fachklasse";
import { FormBilingualerUnterrichtKatalogEintrag } from "../data/schule/FormBilingualerUnterrichtKatalogEintrag";
import { FormBilingualerUnterricht } from "../types/schule/FormBilingualerUnterricht";
import { AnrechnungsantragBKAZVOKatalogEintrag } from "../data/schueler/AnrechnungsantragBKAZVOKatalogEintrag";
import { AnrechnungsantragBKAZVO } from "../types/schueler/AnrechnungsantragBKAZVO";
import { LaenderKatalogEintrag } from "../data/schule/LaenderKatalogEintrag";
import { Laender } from "../types/schule/Laender";
import { OrteKatalogEintrag } from "../data/schule/OrteKatalogEintrag";
import { Orte } from "../types/schule/Orte";
import { Hochschulabschluss } from "../types/schueler/Hochschulabschluss";
import { HochschulabschlussKatalogEintrag } from "../data/schueler/HochschulabschlussKatalogEintrag";

/**
 * Die Klasse dient dem Einlesen der Daten für Core-Types und der Fehlerart-Kontexte von Validatoren.
 * Diese Variante liest die Daten über die API des SVWS-Servers ein. Dies ermöglicht eine dynamische Aktualisierung
 * der Core-Type-Daten zur Laufzeit.
 */
export class JsonCoreTypeReader {

	private readonly api: BaseApi;
	public mapCoreTypeData = new Map<string, any>();

	/** Eine zentrale Registry für die zu ladenden Core-Types */
	private readonly registry: { key: string, entry: any, type: any, simple?: boolean }[] = [
		{ key: "Schulform", entry: SchulformKatalogEintrag, type: Schulform },
		{ key: "BerufskollegAnlage", entry: BerufskollegAnlageKatalogEintrag, type: BerufskollegAnlage },
		{ key: "AllgemeinbildendOrganisationsformen", entry: OrganisationsformKatalogEintrag, type: AllgemeinbildendOrganisationsformen },
		{ key: "BerufskollegOrganisationsformen", entry: OrganisationsformKatalogEintrag, type: BerufskollegOrganisationsformen },
		{ key: "WeiterbildungskollegOrganisationsformen", entry: OrganisationsformKatalogEintrag, type: WeiterbildungskollegOrganisationsformen },
		{ key: "SchulabschlussAllgemeinbildend", entry: SchulabschlussAllgemeinbildendKatalogEintrag, type: SchulabschlussAllgemeinbildend },
		{ key: "SchulabschlussBerufsbildend", entry: SchulabschlussBerufsbildendKatalogEintrag, type: SchulabschlussBerufsbildend },
		{ key: "Einschulungsart", entry: EinschulungsartKatalogEintrag, type: Einschulungsart, simple: true },
		{ key: "HerkunftBildungsgang", entry: HerkunftBildungsgangKatalogEintrag, type: HerkunftBildungsgang },
		{ key: "Jahrgaenge", entry: JahrgaengeKatalogEintrag, type: Jahrgaenge },
		{ key: "PrimarstufeSchuleingangsphaseBesuchsjahre", entry: PrimarstufeSchuleingangsphaseBesuchsjahreKatalogEintrag, type: PrimarstufeSchuleingangsphaseBesuchsjahre },
		{ key: "Religion", entry: CoreTypeData, type: Religion },
		{ key: "Kindergartenbesuch", entry: KindergartenbesuchKatalogEintrag, type: Kindergartenbesuch },
		{ key: "SchuelerStatus", entry: SchuelerStatusKatalogEintrag, type: SchuelerStatus },
		{ key: "Note", entry: NoteKatalogEintrag, type: Note },
		{ key: "Sprachreferenzniveau", entry: SprachreferenzniveauKatalogEintrag, type: Sprachreferenzniveau },
		{ key: "BerufskollegBildungsgangTyp", entry: BildungsgangTypKatalogEintrag, type: BerufskollegBildungsgangTyp },
		{ key: "WeiterbildungskollegBildungsgangTyp", entry: BildungsgangTypKatalogEintrag, type: WeiterbildungskollegBildungsgangTyp },
		{ key: "Schulgliederung", entry: SchulgliederungKatalogEintrag, type: Schulgliederung },
		{ key: "Verkehrssprache", entry: VerkehrsspracheKatalogEintrag, type: Verkehrssprache, simple: true },
		{ key: "Fachgruppe", entry: FachgruppeKatalogEintrag, type: Fachgruppe },
		{ key: "Fach", entry: FachKatalogEintrag, type: Fach },
		{ key: "LehrerAbgangsgrund", entry: LehrerAbgangsgrundKatalogEintrag, type: LehrerAbgangsgrund },
		{ key: "LehrerBeschaeftigungsart", entry: LehrerBeschaeftigungsartKatalogEintrag, type: LehrerBeschaeftigungsart },
		{ key: "LehrerEinsatzstatus", entry: LehrerEinsatzstatusKatalogEintrag, type: LehrerEinsatzstatus },
		{ key: "LehrerFachrichtung", entry: LehrerFachrichtungKatalogEintrag, type: LehrerFachrichtung },
		{ key: "LehrerLehrbefaehigung", entry: LehrerLehrbefaehigungKatalogEintrag, type: LehrerLehrbefaehigung },
		{ key: "LehrerFachrichtungAnerkennung", entry: LehrerFachrichtungAnerkennungKatalogEintrag, type: LehrerFachrichtungAnerkennung },
		{ key: "LehrerLehramt", entry: LehrerLehramtKatalogEintrag, type: LehrerLehramt },
		{ key: "LehrerLehramtAnerkennung", entry: LehrerLehramtAnerkennungKatalogEintrag, type: LehrerLehramtAnerkennung },
		{ key: "LehrerLehrbefaehigungAnerkennung", entry: LehrerLehrbefaehigungAnerkennungKatalogEintrag, type: LehrerLehrbefaehigungAnerkennung },
		{ key: "LehrerLeitungsfunktion", entry: LehrerLeitungsfunktionKatalogEintrag, type: LehrerLeitungsfunktion },
		{ key: "LehrerRechtsverhaeltnis", entry: LehrerRechtsverhaeltnisKatalogEintrag, type: LehrerRechtsverhaeltnis },
		{ key: "LehrerZugangsgrund", entry: LehrerZugangsgrundKatalogEintrag, type: LehrerZugangsgrund },
		{ key: "BilingualeSprache", entry: BilingualeSpracheKatalogEintrag, type: BilingualeSprache },
		{ key: "KAOABerufsfeld", entry: KAOABerufsfeldKatalogEintrag, type: KAOABerufsfeld },
		{ key: "KAOAMerkmaleOptionsarten", entry: KAOAMerkmaleOptionsartenKatalogEintrag, type: KAOAMerkmaleOptionsarten },
		{ key: "KAOAZusatzmerkmaleOptionsarten", entry: KAOAZusatzmerkmaleOptionsartenKatalogEintrag, type: KAOAZusatzmerkmaleOptionsarten },
		{ key: "KAOAEbene4", entry: KAOAEbene4KatalogEintrag, type: KAOAEbene4 },
		{ key: "KAOAZusatzmerkmal", entry: KAOAZusatzmerkmalKatalogEintrag, type: KAOAZusatzmerkmal },
		{ key: "KAOAAnschlussoptionen", entry: KAOAAnschlussoptionenKatalogEintrag, type: KAOAAnschlussoptionen },
		{ key: "KAOAKategorie", entry: KAOAKategorieKatalogEintrag, type: KAOAKategorie },
		{ key: "KAOAMerkmal", entry: KAOAMerkmalKatalogEintrag, type: KAOAMerkmal },
		{ key: "Klassenart", entry: KlassenartKatalogEintrag, type: Klassenart },
		{ key: "Uebergangsempfehlung", entry: UebergangsempfehlungKatalogEintrag, type: Uebergangsempfehlung },
		{ key: "ZulaessigeKursart", entry: ZulaessigeKursartKatalogEintrag, type: ZulaessigeKursart },
		{ key: "Foerderschwerpunkt", entry: FoerderschwerpunktKatalogEintrag, type: Foerderschwerpunkt },
		{ key: "Termin", entry: TerminKatalogEintrag, type: Termin },
		{ key: "Betreuungsart", entry: BetreuungsartKatalogEintrag, type: Betreuungsart },
		{ key: "FormOffenerGanztag", entry: FormOffenerGanztagKatalogEintrag, type: FormOffenerGanztag },
		{ key: "LehrerAnrechnungsgrund", entry: LehrerAnrechnungsgrundKatalogEintrag, type: LehrerAnrechnungsgrund, simple: true },
		{ key: "LehrerMehrleistungsarten", entry: LehrerMehrleistungsartKatalogEintrag, type: LehrerMehrleistungsarten, simple: true },
		{ key: "LehrerMinderleistungsarten", entry: LehrerMinderleistungsartKatalogEintrag, type: LehrerMinderleistungsarten, simple: true },
		{ key: "LehrerPflichtstundensollVollzeit", entry: LehrerPflichtstundensollVollzeitKatalogEintrag, type: LehrerPflichtstundensollVollzeit, simple: true },
		{ key: "Nationalitaeten", entry: NationalitaetenKatalogEintrag, type: Nationalitaeten, simple: true },
		{ key: "Floskelgruppenart", entry: FloskelgruppenartKatalogEintrag, type: Floskelgruppenart, simple: true },
		{ key: "Einwilligungsschluessel", entry: EinwilligungsschluesselKatalogEintrag, type: Einwilligungsschluessel, simple: true },
		{ key: "Herkunftsarten", entry: HerkunftsartenKatalogEintrag, type: Herkunftsarten, simple: true },
		{ key: "HerkunftSonstige", entry: HerkunftSonstigeKatalogEintrag, type: HerkunftSonstige, simple: true },
		{ key: "HerkunftSchulform", entry: HerkunftSchulformKatalogEintrag, type: HerkunftSchulform, simple: true },
		{ key: "Bildungsstufe", entry: BildungsstufeKatalogEintrag, type: Bildungsstufe, simple: true },
		{ key: "BerufskollegBerufsebene1", entry: BerufskollegBerufsebeneKatalogEintrag, type: BerufskollegBerufsebene1, simple: true },
		{ key: "BerufskollegBerufsebene2", entry: BerufskollegBerufsebeneKatalogEintrag, type: BerufskollegBerufsebene2, simple: true },
		{ key: "BerufskollegBerufsebene3", entry: BerufskollegBerufsebeneKatalogEintrag, type: BerufskollegBerufsebene3, simple: true },
		{ key: "Herkunftsschulnummer", entry: HerkunftsschulnummerKatalogEintrag, type: Herkunftsschulnummer, simple: true },
		{ key: "Reformpaedagogik", entry: ReformpaedagogikKatalogEintrag, type: Reformpaedagogik, simple: true },
		{ key: "DQRNiveau", entry: DQRNiveauKatalogEintrag, type: DQRNiveau, simple: true },
		{ key: "Fachklasse", entry: FachklasseKatalogEintrag, type: Fachklasse, simple: true },
		{ key: "FormBilingualerUnterricht", entry: FormBilingualerUnterrichtKatalogEintrag, type: FormBilingualerUnterricht, simple: true },
		{ key: "AnrechnungsantragBKAZVO", entry: AnrechnungsantragBKAZVOKatalogEintrag, type: AnrechnungsantragBKAZVO, simple: true },
		{ key: "Laender", entry: LaenderKatalogEintrag, type: Laender },
		{ key: "Orte", entry: OrteKatalogEintrag, type: Orte, simple: true },
		{ key: "Hochschulabschluss", entry: HochschulabschlussKatalogEintrag, type: Hochschulabschluss },
	];

	/**
	 * Erzeuge einen neuen reader für die übergebene URL.
	 *
	 * @param url   die URL des Servers
	 */
	public constructor(url?: string) {
		this.api = new BaseApi(url ?? "", "", "");
	}

	/**
	 * Lade alle Core-Type-Daten und Fehlerart-Kontexte für die Validatoren über die API
	 */
	public async loadAll(): Promise<void> {
		// Führe den API-Aufruf aus
		const all = JSON.parse(await this.api.getJSON(`/types/allinone.json`));

		// Speichere die Objekte unbekannten Typs direkt in der Map
		for (const entry of this.registry) {
			if (all[entry.key] !== undefined) {
				this.mapCoreTypeData.set(entry.key, all[entry.key]);
			}
		}

		// Spezialfall für die Validatoren
		if (all.ValidatorenFehlerartKontext !== undefined) {
			this.mapCoreTypeData.set("ValidatorenFehlerartKontext", all.ValidatorenFehlerartKontext);
		}
	}

	/**
	 * Initialisiert den Core-Type aus der übergebenen Konfiguration.
	 *
	 * @param config   die Konfiguration des Core-Types
	 */
	private initCoreType(config: any): void {
		const data = this.mapCoreTypeData.get(config.key);
		if (data === undefined) {
			return;
		}

		const mapData = new HashMap<string, List<any>>();
		const mapStatistikIDs = new HashMap<string, string>();
		for (const eintrag of data.daten) {
			mapStatistikIDs.put(eintrag.bezeichner, eintrag.idStatistik);
			const list = new ArrayList<any>();
			for (const h of eintrag.historie) {
				// Nutzt für die Transpiler-Java-Kompatibilität die JSON-Umwandlung des Transpilers
				list.add(config.entry.transpilerFromJSON(JSON.stringify(h)));
			}
			mapData.put(eintrag.bezeichner, list);
		}

		if (config.simple === true) {
			CoreTypeSimple.initValues(new config.type(), config.type.class, mapData);
		}

		const manager = new CoreTypeDataManager(data.version, config.type.class, config.type.values(), mapData, mapStatistikIDs);
		config.type.init(manager);
	}

	/**
	 * Lese alle Daten für die Core-Types und die Fehlerart-Kontexte für die Validatoren ein.
	 */
	public readAll(): void {
		try {
			this.registry.forEach(config => this.initCoreType(config));
			this.readValidatorenFehlerartKontext();
		} catch (e) {
			console.error("Fehler bei der Core-Type Initialisierung:", e);
		}
	}

	/**
	 * Lese die Fehlerart-Kontexte für die Validatoren ein.
	 */
	public readValidatorenFehlerartKontext(): void {
		const data = this.mapCoreTypeData.get("ValidatorenFehlerartKontext");
		if (data === undefined) {
			return;
		}
		const mapVersions = new HashMap<string, number>();
		const mapData = new HashMap<string, List<ValidatorFehlerartKontext>>();
		for (const eintrag of data.daten) {
			const validatorName = eintrag.validator;

			const version = eintrag.version ?? -1;
			mapVersions.put(validatorName, version);

			const list = new ArrayList<ValidatorFehlerartKontext>();
			for (const obj of eintrag.historie) {
				// Nutze für die Transpiler-Java-Kompatibilität die Json-Umwandlung des Transpilers
				list.add(ValidatorFehlerartKontext.transpilerFromJSON(JSON.stringify(obj)));
			}
			mapData.put(eintrag.validator, list);
		}
		ValidatorManager.init(mapVersions, mapData);
	}

}
