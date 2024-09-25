import { JsonCoreTypeReader } from "./JsonCoreTypeReader";

export class JsonCoreTypeReaderStatic extends JsonCoreTypeReader {

	mapCoreTypeNameJsonDataImport = new Map<string, object>();

	public async loadAll() {
		this.mapCoreTypeNameJsonDataImport = new Map([
			["Schulform", await import("@json/schule/Schulform.json")],
			["BerufskollegAnlage", await import("@json/schule/BerufskollegAnlage.json")],
			["AllgemeinbildendOrganisationsformen", await import("@json/schule/AllgemeinbildendOrganisationsformen.json")],
			["BerufskollegOrganisationsformen", await import("@json/schule/BerufskollegOrganisationsformen.json")],
			["WeiterbildungskollegOrganisationsformen", await import("@json/schule/WeiterbildungskollegOrganisationsformen.json")],
			["SchulabschlussAllgemeinbildend", await import("@json/schule/SchulabschlussAllgemeinbildend.json")],
			["SchulabschlussBerufsbildend", await import("@json/schule/SchulabschlussBerufsbildend.json")],
			["HerkunftBildungsgang", await import("@json/schueler/HerkunftBildungsgang.json")],
			["HerkunftBildungsgangTyp", await import("@json/schueler/HerkunftBildungsgangTyp.json")],
			["Jahrgaenge", await import("@json/jahrgang/Jahrgaenge.json")],
			["PrimarstufeSchuleingangsphaseBesuchsjahre", await import("@json/jahrgang/PrimarstufeSchuleingangsphaseBesuchsjahre.json")],
			["Religion", await import("@json/schule/Religion.json")],
			["Kindergartenbesuch", await import("@json/schule/Kindergartenbesuch.json")],
			["SchuelerStatus", await import("@json/schueler/SchuelerStatus.json")],
			["Note", await import("@json/Note.json")],
			["Sprachreferenzniveau", await import("@json/fach/Sprachreferenzniveau.json")],
			["BerufskollegBildungsgangTyp", await import("@json/schule/BerufskollegBildungsgangTyp.json")],
			["WeiterbildungskollegBildungsgangTyp", await import("@json/schule/WeiterbildungskollegBildungsgangTyp.json")],
			["Schulgliederung", await import("@json/schule/Schulgliederung.json")],
			["Fachgruppe", await import("@json/fach/Fachgruppe.json")],
			["Fach", await import("@json/fach/Fach.json")],
			["LehrerAbgangsgrund", await import("@json/lehrer/LehrerAbgangsgrund.json")],
			["LehrerBeschaeftigungsart", await import("@json/lehrer/LehrerBeschaeftigungsart.json")],
			["LehrerEinsatzstatus", await import("@json/lehrer/LehrerEinsatzstatus.json")],
			["LehrerFachrichtung", await import("@json/lehrer/LehrerFachrichtung.json")],
			["LehrerLehrbefaehigung", await import("@json/lehrer/LehrerLehrbefaehigung.json")],
			["LehrerFachrichtungAnerkennung", await import("@json/lehrer/LehrerFachrichtungAnerkennung.json")],
			["LehrerLehramt", await import("@json/lehrer/LehrerLehramt.json")],
			["LehrerLehramtAnerkennung", await import("@json/lehrer/LehrerLehramtAnerkennung.json")],
			["LehrerLehrbefaehigungAnerkennung", await import("@json/lehrer/LehrerLehrbefaehigungAnerkennung.json")],
			["LehrerLeitungsfunktion", await import("@json/lehrer/LehrerLeitungsfunktion.json")],
			["LehrerRechtsverhaeltnis", await import("@json/lehrer/LehrerRechtsverhaeltnis.json")],
			["LehrerZugangsgrund", await import("@json/lehrer/LehrerZugangsgrund.json")],
			["BilingualeSprache", await import("@json/fach/BilingualeSprache.json")],
			["KAOABerufsfeld", await import("@json/kaoa/KAOABerufsfeld.json")],
			["KAOAMerkmaleOptionsarten", await import("@json/kaoa/KAOAMerkmaleOptionsarten.json")],
			["KAOAZusatzmerkmaleOptionsarten", await import("@json/kaoa/KAOAZusatzmerkmaleOptionsarten.json")],
			["KAOAEbene4", await import("@json/kaoa/KAOAEbene4.json")],
			["KAOAZusatzmerkmal", await import("@json/kaoa/KAOAZusatzmerkmal.json")],
			["KAOAAnschlussoptionen", await import("@json/kaoa/KAOAAnschlussoptionen.json")],
			["KAOAKategorie", await import("@json/kaoa/KAOAKategorie.json")],
			["KAOAMerkmal", await import("@json/kaoa/KAOAMerkmal.json")],
			["Klassenart", await import("@json/klassen/Klassenart.json")],
			["Uebergangsempfehlung", await import("@json/schueler/Uebergangsempfehlung.json")],
			["ZulaessigeKursart", await import("@json/kurse/ZulaessigeKursart.json")],
			["Foerderschwerpunkt", await import("@json/schule/Foerderschwerpunkt.json")],
			["LehrerAnrechnungsgrund", await import("@json/lehrer/LehrerAnrechnungsgrund.json")],
			["LehrerMehrleistungsarten", await import("@json/lehrer/LehrerMehrleistungsarten.json")],
			["LehrerMinderleistungsarten", await import("@json/lehrer/LehrerMinderleistungsarten.json")],
			["ValidatorenFehlerartKontext", await import("@json/../validate/ValidatorenFehlerartKontext.json")],
		])
		await Promise.all([...this.mapCoreTypeNameJsonDataImport.values()]);
		for (const [k,v] of this.mapCoreTypeNameJsonDataImport.entries())
			this.mapCoreTypeNameJsonData.set(k, JSON.stringify(v));
		return this.mapCoreTypeNameJsonData;
	}
}
