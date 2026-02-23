import { JsonCoreTypeReader } from "./JsonCoreTypeReader";

/**
 * Die Klasse dient dem Einlesen der Daten für Core-Types und der Fehlerart-Kontexte von Validatoren.
 * Diese Variante liest die Daten nicht über die API des SVWS-Servers, sondern statisch direkt aus den JSON-Dateien.
 * Dies ist für externe Module ohne direkten Zugang zum SVWS-Servers nötig und für Unit-Tests sinnvoll - dort
 * auch nicht nur aus Performance-Gründen.
 */
export class JsonCoreTypeReaderStatic extends JsonCoreTypeReader {

	constructor() {
		super();
		this.initCoreTypeData();
		this.initValidatorContextData();
	}

	/**
	 * Prüft, ob das JSON auch die Struktur für eine Core-Type hat. Damit kann das unerwünschte Laden
	 * von anderen JSONs vermieden werden.
	 */
	private isValidCoreType(obj: any): boolean {
		// Prüfe, ob eine Version vorhanden ist und daten ein Array mit mindestens einem Eintrag ist
		if ((obj?.version === undefined) || (!Array.isArray(obj?.daten)) || (obj.daten.length === 0)) {
			return false;
		}

		// Prüfe den ersten Eintrag stellvertretend für die Einträge. Aus Performance-Gründen wird eine Prüfung aller Einträge vermieden.
		const ersterEintrag = obj.daten[0];
		if ((ersterEintrag?.bezeichner === undefined) || (ersterEintrag?.idStatistik === undefined)
			|| !Array.isArray(ersterEintrag?.historie) || ersterEintrag.historie.length === 0) {
			return false;
		}

		// Prüfe hier analog auch nur den ersten Historieneintrag
		const h = ersterEintrag.historie[0];
		return (h.id !== undefined) && (h.schluessel !== undefined) && (h.kuerzel !== undefined) && (h.text !== undefined) &&
			(h.gueltigVon !== undefined) && (h.gueltigBis !== undefined);
	}

	private initCoreTypeData() {
		// Lade alle Core-Type-JSONs aus dem Verzeichnis bzw. den Unterverzeichnissen
		const modules = import.meta.glob('../../../../../svws-asd/src/main/resources/de/svws_nrw/asd/types/**/*.json', { eager: true });

		// Erzeuge die Einträge in mapCoreTypeData der Basisklasse aus den JSON-Dateien.
		for (const path in modules) {
			// Lese den Inhalt der Datei aus dem Import ein (bei glob-Imports normalerweise unter .default)
			const content = (modules[path] as any).default ?? modules[path];
			// Extrahiere den Dateinamen ohne Endung als Key (z.B. "Schulform")
			const key = path.split('/').pop()?.replace('.json', '');

			if ((key !== undefined) && this.isValidCoreType(content)) {
				this.mapCoreTypeData.set(key, content);
			}
		}
	}

	private initValidatorContextData() {
		// Importiere die Fehlerart-Kontext-JSONs aus dem Ordner validate und seinen Unterverzeichnissen
		const validatorModules = import.meta.glob('../../../../../svws-asd/src/main/resources/de/svws_nrw/asd/validate/**/*.json', { eager: true });

		const allValidatorEntries: any[] = [];

		for (const path in validatorModules) {
			const content = (validatorModules[path] as any).default ?? validatorModules[path];
			if ((content !== undefined) && (content.version !== undefined) && (content.validator !== undefined) && (content.historie !== undefined)) {
				allValidatorEntries.push(content);
			}
		}

		this.mapCoreTypeData.set("ValidatorenFehlerartKontext", { daten: allValidatorEntries });
	}

}
