import { readFileSync, readdirSync, existsSync } from "node:fs";
import { fileURLToPath } from "node:url";
import path from "node:path";
import { describe, expect, test } from "vitest";
import { ApiServer } from "../../../svws-webclient/core/src/api/ApiServer";
import { backendURL, privilegedApiServer } from "../../utils/APIUtils";
import { ReportingParameter } from "../../../svws-webclient/core/src/core/data/reporting/ReportingParameter";
import { ReportingAusgabeformat } from "../../../svws-webclient/core/src/core/types/reporting/ReportingAusgabeformat";
import { ReportingReportvorlage } from "../../../svws-webclient/core/src/core/types/reporting/ReportingReportvorlage";
import { ReportingReportvorlageKonfigurationBenutzerweit }
	from "../../../svws-webclient/core/src/core/types/reporting/reportvorlagekonfiguration/ReportingReportvorlageKonfigurationBenutzerweit";

/** Konfiguration eines Reporting-Testfalls (config.json je Fall-Ordner). */
interface ReportingTestConfig {
	/** Die beim Testen angezeigte Beschreibung des Falls. */
	beschreibung: string;
	/** Das Schema (die Datenbank), gegen das der Report erzeugt wird. */
	schema: string;
	/** Optionaler Benutzername. Fehlt er, wird der Standard-Admin-Zugang verwendet. */
	benutzer?: string;
	/** Optionales Passwort. Nur relevant, wenn auch ein Benutzer gesetzt ist. */
	passwort?: string;
	/**
	 * Schaltet für diesen Fall die Prüfung der Dateinamen aus der .name.tpl ein. Da die Dateinamen
	 * nur im PDF-Pfad entstehen, kostet die Prüfung je Fall zwei zusätzliche PDF-Erzeugungen –
	 * deshalb genügt ein markierter Fall je Reportvorlage.
	 */
	pruefeDateinamen?: boolean;
	/**
	 * Schaltet für diesen Fall einen zusätzlichen Lauf gegen die Katalog-Defaults ein: Die Parameter stammen
	 * dann nicht aus der params.json, sondern aus dem Vorlagenkatalog und dem benutzerweiten Katalog.
	 * Aus der params.json wird nur die Auswahl übernommen (Abschnitt, Hauptdatenobjekt, IDs) – ein Fall je
	 * Reportvorlage genügt.
	 */
	pruefeDefaults?: boolean;
}

// Wurzelverzeichnis der Reporting-Testfälle. Jeder Unterordner mit einer params.json und einer
// config.json bildet einen eigenen Testfall; der HTML-Snapshot wird daneben als snapshot.html abgelegt.
const reportingDir = path.resolve(path.dirname(fileURLToPath(import.meta.url)), "reporting");

const faelle = readdirSync(reportingDir, { withFileTypes: true })
	.filter((eintrag) => eintrag.isDirectory())
	.map((eintrag) => eintrag.name)
	.filter((name) => existsSync(path.join(reportingDir, name, "params.json")) && existsSync(path.join(reportingDir, name, "config.json")))
	.map((ordner) => {
		const config = JSON.parse(readFileSync(path.join(reportingDir, ordner, "config.json"), "utf-8")) as ReportingTestConfig;
		return { ordner, beschreibung: config.beschreibung, schema: config.schema, benutzer: config.benutzer, passwort: config.passwort,
			pruefeDateinamen: config.pruefeDateinamen === true, pruefeDefaults: config.pruefeDefaults === true };
	});

// Die Fälle, für die zusätzlich ein Lauf gegen die Katalog-Defaults geprüft wird.
const faelleDefaults = faelle.filter((fall) => fall.pruefeDefaults);

// Die Fälle, für die zusätzlich die Dateinamen aus der .name.tpl geprüft werden.
const faelleDateinamen = faelle.filter((fall) => fall.pruefeDateinamen);

// Vom Server tatsächlich bereitgestellte (aktive) Schemata. Fehlt das von einem Testfall benötigte
// Schema (z.B. in der Docker-CI, die nur GymAbi01 hat), wird der Fall nicht geprüft, sondern nur
// protokolliert – der Test bleibt grün. Ist der Server nicht erreichbar, bleibt das Set leer.
async function ladeVerfuegbareSchemata(): Promise<Set<string>> {
	try {
		const liste = await privilegedApiServer.getConfigDBSchemata();
		const namen = new Set<string>();
		for (const eintrag of liste) {
			if ((eintrag.name !== null) && !eintrag.isDeactivated) {
				namen.add(eintrag.name.toLowerCase());
			}
		}
		return namen;
	} catch {
		return new Set<string>();
	}
}

const verfuegbareSchemata = await ladeVerfuegbareSchemata();

// Sammelt die Beschreibungen aller übersprungenen Fälle. Ein Lauf, der Fälle überspringt, ist trotzdem
// grün – vitest wertet ein frühes return als bestanden. Für die Verifikation eines Refactorings ist ein
// solcher Lauf aber wertlos, deshalb wird die Zahl am Ende ausgewiesen und ist dort zu beachten.
const uebersprungeneFaelle: Array<string> = [];

/**
 * Prüft, ob das Schema eines Falls verfügbar ist. Fehlt es, wird der Fall vermerkt und protokolliert.
 *
 * @param beschreibung die Beschreibung des Testfalls
 * @param schema       das benötigte Schema
 *
 * @returns true, wenn der Fall geprüft werden kann
 */
function istPruefbar(beschreibung: string, schema: string): boolean {
	if (verfuegbareSchemata.has(schema.toLowerCase())) {
		return true;
	}
	uebersprungeneFaelle.push(`${beschreibung} (${schema})`);
	console.log(`Testfall "${beschreibung}" nicht geprüft, da notwendiges Schema "${schema}" nicht vorhanden.`);
	return false;
}

/** Die Bezeichnung der Reportvorlage eines Testfalls aus dessen params.json. */
function reportvorlageVonFall(ordner: string): string | undefined {
	const json = JSON.parse(readFileSync(path.join(reportingDir, ordner, "params.json"), "utf-8")) as { reportvorlage?: string };
	return json.reportvorlage;
}

/**
 * Baut die Reporting-Parameter eines Falls aus den Katalog-Defaults statt aus der params.json.
 * Übernommen wird aus der Datei nur die Auswahl – Schuljahresabschnitt, Hauptdatenobjekt und die ID-Listen –,
 * damit derselbe Report wie im regulären Fall erzeugt wird. Alles Übrige (Vorlagenparameter, Sortierung,
 * Filterung) stammt aus dem Vorlagenkatalog. Der Diff zwischen snapshot.html und snapshot-defaults.html zeigt
 * damit genau, was die vom Client übermittelten Parameter bewirken.
 *
 * @param ordner der Ordner des Testfalls
 *
 * @returns die Reporting-Parameter für den Aufruf mit Katalog-Defaults
 */
function ladeParameterAusKatalog(ordner: string): ReportingParameter {
	const json = JSON.parse(readFileSync(path.join(reportingDir, ordner, "params.json"), "utf-8")) as
		{ reportvorlage: string, idSchuljahresabschnitt: number, idHauptdatenObjekt: number,
			idsHauptdaten: Array<number> | null, idsDetaildaten: Array<number> | null };
	const vorlage = ReportingReportvorlage.getByBezeichnung(json.reportvorlage);
	if (vorlage === null) {
		throw new Error(`Zur Bezeichnung "${json.reportvorlage}" aus der params.json existiert keine Reportvorlage.`);
	}
	const katalog = vorlage.getReportingParameter();
	// Die benutzerweiten Parameter bilden einen eigenen, von den Vorlagenparametern disjunkten Katalog und stecken
	// deshalb nicht in getReportingParameter(). Ohne sie greifen die gespeicherten benutzerweiten Einstellungen des
	// angemeldeten Benutzers aus der Client-Konfiguration – der Lauf wäre dann nicht mehr reproduzierbar.
	katalog.reportvorlageParameterGruppen.addAll(ReportingReportvorlageKonfigurationBenutzerweit.getBenutzerweiteParameterGruppen());

	const params = JSON.parse(ReportingParameter.transpilerToJSON(katalog));
	params.ausgabeformat = ReportingAusgabeformat.HTML.getId();
	params.idSchuljahresabschnitt = json.idSchuljahresabschnitt;
	params.idHauptdatenObjekt = json.idHauptdatenObjekt;
	params.idsHauptdaten = json.idsHauptdaten ?? [];
	params.idsDetaildaten = json.idsDetaildaten ?? [];
	return ReportingParameter.transpilerFromJSON(JSON.stringify(params));
}

/**
 * Lädt die params.json eines Falls und liefert sie als ReportingParameter für eine PDF-Ausgabe.
 * Die Datei selbst bleibt unverändert – angepasst wird nur die Kopie für diesen Aufruf.
 *
 * @param ordner      der Ordner des Testfalls
 * @param nurErsteId  true, um die Hauptdaten auf die erste ID zu kürzen (Einzelaufruf)
 *
 * @returns die Reporting-Parameter für den PDF-Aufruf
 */
function ladeParameterFuerPdf(ordner: string, nurErsteId: boolean): ReportingParameter {
	const json = JSON.parse(readFileSync(path.join(reportingDir, ordner, "params.json"), "utf-8")) as
		{ ausgabeformat: number, idsHauptdaten: Array<number> | null };
	json.ausgabeformat = ReportingAusgabeformat.PDF.getId();
	if (nurErsteId && (json.idsHauptdaten !== null) && (json.idsHauptdaten.length > 1)) {
		json.idsHauptdaten = [json.idsHauptdaten[0]];
	}
	return ReportingParameter.transpilerFromJSON(JSON.stringify(json));
}

// Damit die HTML-Snapshots stabil bleiben, muss der getestete Server
//   1. im ServerMode DEV laufen und
//   2. mit gesetzter Umgebungsvariable SVWS_REPORTING_FIXED_DATE=true gestartet sein.
// Nur dann liefert der #aktuell-Dialect ein deterministisches "Gedruckt am ..." im Report.
// In der Docker-/Release-Umgebung wird die Variable automatisch im Container gesetzt; beim manuellen
// lokalen Lauf: SVWS_REPORTING_FIXED_DATE=true ./gradlew :svws-server-app:runServer
describe("Reporting Tests ", () => {
	// Vorbedingung für alle datenabhängigen Fälle: Ohne erreichbaren Server bleibt die Schema-Menge leer, und
	// jeder dieser Fälle kehrt vorzeitig und erfolgreich zurück. Der Lauf meldet dann "alle Tests bestanden",
	// obwohl nichts geprüft wurde. Dieser Test macht das sichtbar, indem er als Einziger fehlschlägt.
	test("Vorbedingung: der Server ist erreichbar und liefert Schemata", () => {
		expect(verfuegbareSchemata.size,
			"Der Server ist nicht erreichbar oder liefert keine aktiven Schemata. Alle datenabhängigen Testfälle "
			+ "würden übersprungen und der Lauf wäre trotz grüner Meldung ohne Aussage. Server starten mit: "
			+ "SVWS_REPORTING_FIXED_DATE=true ./gradlew :svws-server-app:runServer").toBeGreaterThan(0);
	});

	// Ein Test, der sicherstellt, dass jede Reportvorlage durch mindestens einen Fall mit Default-Prüfung abgedeckt
	// ist (reine Datei-/Enum-Prüfung). Nur so ist jeder Vorlagenkatalog mindestens einmal gegen die Ausgabe geprüft.
	test("Vollständigkeit: jede Reportvorlage ist durch mindestens einen Default-Testfall abgedeckt", () => {
		const abgedeckt = new Set(faelleDefaults.map((fall) => reportvorlageVonFall(fall.ordner)));
		const fehlend = ReportingReportvorlage.values()
			.map((vorlage) => vorlage.getBezeichnung())
			.filter((bezeichnung) => !abgedeckt.has(bezeichnung))
			.sort();
		expect(fehlend, `Für folgende Reportvorlagen fehlt ein Testfall mit "pruefeDefaults": ${fehlend.join(", ")}`).toEqual([]);
	});

	// Prüft die Ausgabe mit den Parametern aus dem Vorlagenkatalog und dem benutzerweiten Katalog statt mit den in
	// der params.json hinterlegten Werten. Die Auswahl (Abschnitt, Hauptdatenobjekt, IDs) ist dieselbe wie im
	// regulären Fall, sodass der Vergleich beider Snapshots die Wirkung der übermittelten Parameter zeigt.
	// Damit wird zweierlei abgedeckt, was die regulären Fälle nicht leisten: die Katalog-Defaults selbst und der
	// Fall, dass ein Client keinen individuellen Parametersatz sendet – dann greifen die SOLL-Vorauswahlen der
	// Vorlage, etwa die Filtergruppe zum Schülerstatus.
	test.each(faelleDefaults)("htmlReport (Katalog-Defaults) - $beschreibung ($schema)",
		async ({ ordner, beschreibung, schema, benutzer, passwort }) => {
			if (!istPruefbar(beschreibung, schema)) {
				return;
			}
			const api = (benutzer !== undefined) ? new ApiServer(backendURL, benutzer, passwort ?? "") : privilegedApiServer;
			const html = await api.htmlReport(ladeParameterAusKatalog(ordner), schema);
			await expect(html).toMatchFileSnapshot(path.join(reportingDir, ordner, "snapshot-defaults.html"));
		});

	// Ein Test, der sicherstellt, dass zu jeder Reportvorlage mindestens ein Snapshot-Testfall definiert ist (reine Datei-/Enum-Prüfung).
	// Schlägt der Test fehl, so listet er die noch fehlenden Vorlagen auf.
	test("Vollständigkeit: jede Reportvorlage ist durch mindestens einen Snapshot-Testfall abgedeckt", () => {
		const abgedeckt = new Set(faelle.map((fall) =>
			(JSON.parse(readFileSync(path.join(reportingDir, fall.ordner, "params.json"), "utf-8")) as { reportvorlage?: string }).reportvorlage));
		const fehlend = ReportingReportvorlage.values()
			.map((vorlage) => vorlage.getBezeichnung())
			.filter((bezeichnung) => !abgedeckt.has(bezeichnung))
			.sort();
		expect(fehlend, `Für folgende Reportvorlagen fehlt ein Snapshot-Testfall: ${fehlend.join(", ")}`).toEqual([]);
	});

	test.each(faelle)("htmlReport - $beschreibung ($schema)", async ({ ordner, beschreibung, schema, benutzer, passwort }) => {
		// Prüft, ob das Schema für den Test erreichbar ist. Wenn nicht, wird der Test mit Log-Eintrag übersprungen.
		if (!istPruefbar(beschreibung, schema)) {
			return;
		}
		// Standardmäßig der privilegierte Admin-Zugang; nur wenn die config.json einen Benutzer setzt,
		// wird ein fallspezifischer ApiServer mit den dort hinterlegten Zugangsdaten verwendet.
		const api = (benutzer !== undefined) ? new ApiServer(backendURL, benutzer, passwort ?? "") : privilegedApiServer;
		const json = readFileSync(path.join(reportingDir, ordner, "params.json"), "utf-8");
		const params = ReportingParameter.transpilerFromJSON(json);
		const html = await api.htmlReport(params, schema);
		await expect(html).toMatchFileSnapshot(path.join(reportingDir, ordner, "snapshot.html"));
	});

	// Ein Test, der sicherstellt, dass jede Reportvorlage durch mindestens einen Fall mit Dateinamen-Prüfung
	// abgedeckt ist (reine Datei-/Enum-Prüfung). Nur so ist jede .name.tpl mindestens einmal ausgeführt worden.
	test("Vollständigkeit: jede Reportvorlage ist durch mindestens einen Dateinamen-Testfall abgedeckt", () => {
		const abgedeckt = new Set(faelleDateinamen.map((fall) => reportvorlageVonFall(fall.ordner)));
		const fehlend = ReportingReportvorlage.values()
			.map((vorlage) => vorlage.getBezeichnung())
			.filter((bezeichnung) => !abgedeckt.has(bezeichnung))
			.sort();
		expect(fehlend, `Für folgende Reportvorlagen fehlt ein Testfall mit "pruefeDateinamen": ${fehlend.join(", ")}`).toEqual([]);
	});

	// Prüft die aus der .name.tpl erzeugten Download-Dateinamen. Sie erscheinen nicht im HTML-Snapshot,
	// da die HTML-Antwort kein Content-Disposition setzt – der Name entsteht nur im PDF-Pfad und wird
	// vom API-Client bereits dekodiert in ApiFile.name geliefert. Verglichen wird ausschließlich der
	// Name, nicht der PDF-Inhalt.
	// Geprüft werden beide Zweige der Dateinamensvorlagen, die typischerweise nach der Anzahl der
	// Datensätze unterscheiden: der Gesamtaufruf mit allen IDs und der Einzelaufruf mit nur einer ID.
	// Beide liefern genau eine PDF-Datei, kein ZIP.
	test.each(faelleDateinamen)("dateiname - $beschreibung ($schema)", async ({ ordner, beschreibung, schema, benutzer, passwort }) => {
		if (!istPruefbar(beschreibung, schema)) {
			return;
		}
		const api = (benutzer !== undefined) ? new ApiServer(backendURL, benutzer, passwort ?? "") : privilegedApiServer;

		const alle = await api.pdfReport(ladeParameterFuerPdf(ordner, false), schema);
		await expect(alle.name).toMatchFileSnapshot(path.join(reportingDir, ordner, "dateiname-alle.txt"));

		const einzeln = await api.pdfReport(ladeParameterFuerPdf(ordner, true), schema);
		await expect(einzeln.name).toMatchFileSnapshot(path.join(reportingDir, ordner, "dateiname-einzeln.txt"));
	});

	// Abschluss: Ein übersprungener Fall ist in der vitest-Ausgabe nicht von einem bestandenen zu unterscheiden.
	// Dieser Test steht bewusst am Ende des Blocks – die Fälle laufen sequenziell, die Liste ist hier vollständig –
	// und macht aus dem stillen Überspringen einen sichtbaren Fehlschlag. Für die Verifikation eines Refactorings
	// zählt nur ein Lauf, in dem kein Fall übersprungen wurde.
	test("Abschluss: kein Testfall wurde übersprungen", () => {
		expect(uebersprungeneFaelle,
			`${uebersprungeneFaelle.length} Testfälle wurden übersprungen, weil ihr Schema fehlt. Der Lauf ist damit `
			+ `nicht aussagekräftig. Betroffen: ${uebersprungeneFaelle.join(", ")}`).toEqual([]);
	});
});
