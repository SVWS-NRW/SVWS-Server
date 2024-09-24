
import { readFile } from "node:fs/promises";
import { resolve } from "node:path";

import { JsonCoreTypeReader } from "../client/src/router/JsonCoreTypeReader";
import { HashMap } from "../core/src/java/util/HashMap";
import { DeveloperNotificationException } from "../core/src/core/exceptions/DeveloperNotificationException";

export class JsonCoreTypeReaderNode extends JsonCoreTypeReader {

	private readonly pathPrefix : string = "./../../svws-asd/src/main/resources/de/svws_nrw/asd";

	public constructor() {
		super("");
		this.fillPathMap();
	}

	mapCoreTypeNameJsonPath = new HashMap<string, string>();

	private buildPath(suffixPathToJason : string) : string {
		return resolve(__dirname, this.pathPrefix + suffixPathToJason);
	}

	private fillPathMap() {
		this.mapCoreTypeNameJsonPath.put("Schulform", this.buildPath("/types/schule/Schulform.json"));
		this.mapCoreTypeNameJsonPath.put("BerufskollegAnlage", this.buildPath("/types/schule/BerufskollegAnlage.json"));
		this.mapCoreTypeNameJsonPath.put("AllgemeinbildendOrganisationsformen", this.buildPath("/types/schule/AllgemeinbildendOrganisationsformen.json"));
		this.mapCoreTypeNameJsonPath.put("BerufskollegOrganisationsformen", this.buildPath("/types/schule/BerufskollegOrganisationsformen.json"));
		this.mapCoreTypeNameJsonPath.put("WeiterbildungskollegOrganisationsformen", this.buildPath("/types/schule/WeiterbildungskollegOrganisationsformen.json"));
		this.mapCoreTypeNameJsonPath.put("SchulabschlussAllgemeinbildend", this.buildPath("/types/schule/SchulabschlussAllgemeinbildend.json"));
		this.mapCoreTypeNameJsonPath.put("SchulabschlussBerufsbildend", this.buildPath("/types/schule/SchulabschlussBerufsbildend.json"));
		this.mapCoreTypeNameJsonPath.put("HerkunftBildungsgang", this.buildPath("/types/schueler/HerkunftBildungsgang.json"));
		this.mapCoreTypeNameJsonPath.put("HerkunftBildungsgangTyp", this.buildPath("/types/schueler/HerkunftBildungsgangTyp.json"));
		this.mapCoreTypeNameJsonPath.put("Jahrgaenge", this.buildPath("/types/jahrgang/Jahrgaenge.json"));
		this.mapCoreTypeNameJsonPath.put("PrimarstufeSchuleingangsphaseBesuchsjahre", this.buildPath("/types/jahrgang/PrimarstufeSchuleingangsphaseBesuchsjahre.json"));
		this.mapCoreTypeNameJsonPath.put("Religion", this.buildPath("/types/schule/Religion.json"));
		this.mapCoreTypeNameJsonPath.put("Kindergartenbesuch", this.buildPath("/types/schule/Kindergartenbesuch.json"));
		this.mapCoreTypeNameJsonPath.put("SchuelerStatus", this.buildPath("/types/schueler/SchuelerStatus.json"));
		this.mapCoreTypeNameJsonPath.put("Note", this.buildPath("/types/Note.json"));
		this.mapCoreTypeNameJsonPath.put("Sprachreferenzniveau", this.buildPath("/types/fach/Sprachreferenzniveau.json"));
		this.mapCoreTypeNameJsonPath.put("BerufskollegBildungsgangTyp", this.buildPath("/types/schule/BerufskollegBildungsgangTyp.json"));
		this.mapCoreTypeNameJsonPath.put("WeiterbildungskollegBildungsgangTyp", this.buildPath("/types/schule/WeiterbildungskollegBildungsgangTyp.json"));
		this.mapCoreTypeNameJsonPath.put("Schulgliederung", this.buildPath("/types/schule/Schulgliederung.json"));
		this.mapCoreTypeNameJsonPath.put("Fachgruppe", this.buildPath("/types/fach/Fachgruppe.json"));
		this.mapCoreTypeNameJsonPath.put("Fach", this.buildPath("/types/fach/Fach.json"));
		this.mapCoreTypeNameJsonPath.put("LehrerAbgangsgrund", this.buildPath("/types/lehrer/LehrerAbgangsgrund.json"));
		this.mapCoreTypeNameJsonPath.put("LehrerBeschaeftigungsart", this.buildPath("/types/lehrer/LehrerBeschaeftigungsart.json"));
		this.mapCoreTypeNameJsonPath.put("LehrerEinsatzstatus", this.buildPath("/types/lehrer/LehrerEinsatzstatus.json"));
		this.mapCoreTypeNameJsonPath.put("LehrerFachrichtung", this.buildPath("/types/lehrer/LehrerFachrichtung.json"));
		this.mapCoreTypeNameJsonPath.put("LehrerLehrbefaehigung", this.buildPath("/types/lehrer/LehrerLehrbefaehigung.json"));
		this.mapCoreTypeNameJsonPath.put("LehrerFachrichtungAnerkennung", this.buildPath("/types/lehrer/LehrerFachrichtungAnerkennung.json"));
		this.mapCoreTypeNameJsonPath.put("LehrerLehramt", this.buildPath("/types/lehrer/LehrerLehramt.json"));
		this.mapCoreTypeNameJsonPath.put("LehrerLehramtAnerkennung", this.buildPath("/types/lehrer/LehrerLehramtAnerkennung.json"));
		this.mapCoreTypeNameJsonPath.put("LehrerLehrbefaehigungAnerkennung", this.buildPath("/types/lehrer/LehrerLehrbefaehigungAnerkennung.json"));
		this.mapCoreTypeNameJsonPath.put("LehrerLeitungsfunktion", this.buildPath("/types/lehrer/LehrerLeitungsfunktion.json"));
		this.mapCoreTypeNameJsonPath.put("LehrerRechtsverhaeltnis", this.buildPath("/types/lehrer/LehrerRechtsverhaeltnis.json"));
		this.mapCoreTypeNameJsonPath.put("LehrerZugangsgrund", this.buildPath("/types/lehrer/LehrerZugangsgrund.json"));
		this.mapCoreTypeNameJsonPath.put("BilingualeSprache", this.buildPath("/types/fach/BilingualeSprache.json"));
		this.mapCoreTypeNameJsonPath.put("KAOABerufsfeld", this.buildPath("/types/kaoa/KAOABerufsfeld.json"));
		this.mapCoreTypeNameJsonPath.put("KAOAMerkmaleOptionsarten", this.buildPath("/types/kaoa/KAOAMerkmaleOptionsarten.json"));
		this.mapCoreTypeNameJsonPath.put("KAOAZusatzmerkmaleOptionsarten", this.buildPath("/types/kaoa/KAOAZusatzmerkmaleOptionsarten.json"));
		this.mapCoreTypeNameJsonPath.put("KAOAEbene4", this.buildPath("/types/kaoa/KAOAEbene4.json"));
		this.mapCoreTypeNameJsonPath.put("KAOAZusatzmerkmal", this.buildPath("/types/kaoa/KAOAZusatzmerkmal.json"));
		this.mapCoreTypeNameJsonPath.put("KAOAAnschlussoptionen", this.buildPath("/types/kaoa/KAOAAnschlussoptionen.json"));
		this.mapCoreTypeNameJsonPath.put("KAOAKategorie", this.buildPath("/types/kaoa/KAOAKategorie.json"));
		this.mapCoreTypeNameJsonPath.put("KAOAMerkmal", this.buildPath("/types/kaoa/KAOAMerkmal.json"));
		this.mapCoreTypeNameJsonPath.put("Klassenart", this.buildPath("/types/klassen/Klassenart.json"));
		this.mapCoreTypeNameJsonPath.put("Uebergangsempfehlung", this.buildPath("/types/schueler/Uebergangsempfehlung.json"));
		this.mapCoreTypeNameJsonPath.put("ZulaessigeKursart", this.buildPath("/types/kurse/ZulaessigeKursart.json"));
		this.mapCoreTypeNameJsonPath.put("Foerderschwerpunkt", this.buildPath("/types/schule/Foerderschwerpunkt.json"));
		this.mapCoreTypeNameJsonPath.put("LehrerAnrechnungsgrund", this.buildPath("/types/lehrer/LehrerAnrechnungsgrund.json"));
		this.mapCoreTypeNameJsonPath.put("LehrerMehrleistungsarten", this.buildPath("/types/lehrer/LehrerMehrleistungsarten.json"));
		this.mapCoreTypeNameJsonPath.put("LehrerMinderleistungsarten", this.buildPath("/types/lehrer/LehrerMinderleistungsarten.json"));
		this.mapCoreTypeNameJsonPath.put("ValidatorenFehlerartKontext", this.buildPath("/validate/ValidatorenFehlerartKontext.json"));
	}

	private async loadJsonFromFileSystem(coreTypeName : string) {
		if (coreTypeName === "")
			throw new DeveloperNotificationException("Für das Einlesen eines Core-Types muss ein gültiger Name angegeben werden");
		const jsonFilePath : string | null = this.mapCoreTypeNameJsonPath.get(coreTypeName);
		if (jsonFilePath === null)
			throw new DeveloperNotificationException("Kein Pfad für den angegebenen Core-Type gefunden: " + coreTypeName);
		try {
			const jsonData = await readFile(jsonFilePath, 'utf-8');
			this.mapCoreTypeNameJsonData.put(coreTypeName, jsonData);
		} catch(e) {
			throw new DeveloperNotificationException(`Die JSON-Datei ${jsonFilePath} konnte nicht eingelesen werden. Pfad ungültig?`)
		}
	}

	public async loadAllFromFileSystem() : Promise<HashMap<string, string>> {
		console.log("Lade die Json Files");
		const keys = this.mapCoreTypeNameJsonPath.keySet();
		const arr = [];
		for (const key of keys)
			arr.push(this.loadJsonFromFileSystem(key));
		await Promise.all(arr);
		return this.mapCoreTypeNameJsonData;
	}

}
