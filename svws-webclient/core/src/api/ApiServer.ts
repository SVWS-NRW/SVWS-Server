import { BaseApi, type ApiFile } from '../api/BaseApi';
import { AbgangsartKatalog } from '../core/data/schule/AbgangsartKatalog';
import { Abiturdaten } from '../core/data/gost/Abiturdaten';
import { AllgemeineMerkmaleKatalogEintrag } from '../core/data/schule/AllgemeineMerkmaleKatalogEintrag';
import { ArrayList } from '../java/util/ArrayList';
import { Aufsichtsbereich } from '../core/data/schule/Aufsichtsbereich';
import { BenutzerAllgemeinCredentials } from '../core/data/benutzer/BenutzerAllgemeinCredentials';
import { BenutzerConfig } from '../core/data/benutzer/BenutzerConfig';
import { BenutzerDaten } from '../core/data/benutzer/BenutzerDaten';
import { BenutzergruppeDaten } from '../core/data/benutzer/BenutzergruppeDaten';
import { BenutzergruppeListeEintrag } from '../core/data/benutzer/BenutzergruppeListeEintrag';
import { BenutzerKompetenzGruppenKatalogEintrag } from '../core/data/benutzer/BenutzerKompetenzGruppenKatalogEintrag';
import { BenutzerKompetenzKatalogEintrag } from '../core/data/benutzer/BenutzerKompetenzKatalogEintrag';
import { BenutzerListeEintrag } from '../core/data/benutzer/BenutzerListeEintrag';
import { BerufskollegAnlageKatalogEintrag } from '../core/data/schule/BerufskollegAnlageKatalogEintrag';
import { BerufskollegBerufsebeneKatalogEintrag } from '../core/data/schule/BerufskollegBerufsebeneKatalogEintrag';
import { BerufskollegFachklassenKatalog } from '../core/data/schule/BerufskollegFachklassenKatalog';
import { BetriebAnsprechpartner } from '../core/data/betrieb/BetriebAnsprechpartner';
import { BetriebListeEintrag } from '../core/data/betrieb/BetriebListeEintrag';
import { BetriebStammdaten } from '../core/data/betrieb/BetriebStammdaten';
import { BilingualeSpracheKatalogEintrag } from '../core/data/fach/BilingualeSpracheKatalogEintrag';
import { DatenbankVerbindungsdaten } from '../core/data/schema/DatenbankVerbindungsdaten';
import { DBSchemaListeEintrag } from '../core/data/db/DBSchemaListeEintrag';
import { EinschulungsartKatalogEintrag } from '../core/data/schule/EinschulungsartKatalogEintrag';
import { ENMDaten } from '../core/data/enm/ENMDaten';
import { Erzieherart } from '../core/data/erzieher/Erzieherart';
import { ErzieherListeEintrag } from '../core/data/erzieher/ErzieherListeEintrag';
import { ErzieherStammdaten } from '../core/data/erzieher/ErzieherStammdaten';
import { FachDaten } from '../core/data/fach/FachDaten';
import { FachgruppenKatalogEintrag } from '../core/data/fach/FachgruppenKatalogEintrag';
import { FachKatalogEintrag } from '../core/data/fach/FachKatalogEintrag';
import { FaecherListeEintrag } from '../core/data/fach/FaecherListeEintrag';
import { FoerderschwerpunktEintrag } from '../core/data/schule/FoerderschwerpunktEintrag';
import { FoerderschwerpunktKatalogEintrag } from '../core/data/schule/FoerderschwerpunktKatalogEintrag';
import { GEAbschlussFaecher } from '../core/data/abschluss/GEAbschlussFaecher';
import { GostBelegpruefungErgebnis } from '../core/abschluss/gost/GostBelegpruefungErgebnis';
import { GostBelegpruefungsErgebnisse } from '../core/data/gost/GostBelegpruefungsErgebnisse';
import { GostBeratungslehrer } from '../core/data/gost/GostBeratungslehrer';
import { GostBlockungKurs } from '../core/data/gost/GostBlockungKurs';
import { GostBlockungKursAufteilung } from '../core/data/gost/GostBlockungKursAufteilung';
import { GostBlockungKursLehrer } from '../core/data/gost/GostBlockungKursLehrer';
import { GostBlockungListeneintrag } from '../core/data/gost/GostBlockungListeneintrag';
import { GostBlockungRegel } from '../core/data/gost/GostBlockungRegel';
import { GostBlockungSchiene } from '../core/data/gost/GostBlockungSchiene';
import { GostBlockungsdaten } from '../core/data/gost/GostBlockungsdaten';
import { GostBlockungsergebnis } from '../core/data/gost/GostBlockungsergebnis';
import { GostFach } from '../core/data/gost/GostFach';
import { GostJahrgang } from '../core/data/gost/GostJahrgang';
import { GostJahrgangFachkombination } from '../core/data/gost/GostJahrgangFachkombination';
import { GostJahrgangFachwahlen } from '../core/data/gost/GostJahrgangFachwahlen';
import { GostJahrgangFachwahlenHalbjahr } from '../core/data/gost/GostJahrgangFachwahlenHalbjahr';
import { GostJahrgangsdaten } from '../core/data/gost/GostJahrgangsdaten';
import { GostKlausurenCollectionSkrsKrs } from '../core/data/gost/klausurplanung/GostKlausurenCollectionSkrsKrs';
import { GostKlausurenKalenderinformation } from '../core/data/gost/klausurplanung/GostKlausurenKalenderinformation';
import { GostKlausurraum } from '../core/data/gost/klausurplanung/GostKlausurraum';
import { GostKlausurtermin } from '../core/data/gost/klausurplanung/GostKlausurtermin';
import { GostKlausurterminblockungDaten } from '../core/data/gost/klausurplanung/GostKlausurterminblockungDaten';
import { GostKlausurvorgabe } from '../core/data/gost/klausurplanung/GostKlausurvorgabe';
import { GostKursklausur } from '../core/data/gost/klausurplanung/GostKursklausur';
import { GostLaufbahnplanungBeratungsdaten } from '../core/data/gost/GostLaufbahnplanungBeratungsdaten';
import { GostLaufbahnplanungDaten } from '../core/data/gost/GostLaufbahnplanungDaten';
import { GostLeistungen } from '../core/data/gost/GostLeistungen';
import { GostSchuelerFachwahl } from '../core/data/gost/GostSchuelerFachwahl';
import { GostSchuelerklausur } from '../core/data/gost/klausurplanung/GostSchuelerklausur';
import { GostStatistikFachwahl } from '../core/data/gost/GostStatistikFachwahl';
import { HerkunftKatalogEintrag } from '../core/data/schule/HerkunftKatalogEintrag';
import { HerkunftsartKatalogEintrag } from '../core/data/schule/HerkunftsartKatalogEintrag';
import { HerkunftsschulnummerKatalogEintrag } from '../core/data/schule/HerkunftsschulnummerKatalogEintrag';
import { JahrgangsDaten } from '../core/data/jahrgang/JahrgangsDaten';
import { JahrgangsKatalogEintrag } from '../core/data/jahrgang/JahrgangsKatalogEintrag';
import { JahrgangsListeEintrag } from '../core/data/jahrgang/JahrgangsListeEintrag';
import { KAOAAnschlussoptionEintrag } from '../core/data/kaoa/KAOAAnschlussoptionEintrag';
import { KAOABerufsfeldEintrag } from '../core/data/kaoa/KAOABerufsfeldEintrag';
import { KAOAEbene4Eintrag } from '../core/data/kaoa/KAOAEbene4Eintrag';
import { KAOAKategorieEintrag } from '../core/data/kaoa/KAOAKategorieEintrag';
import { KAOAMerkmalEintrag } from '../core/data/kaoa/KAOAMerkmalEintrag';
import { KAOAZusatzmerkmalEintrag } from '../core/data/kaoa/KAOAZusatzmerkmalEintrag';
import { KatalogEintrag } from '../core/data/kataloge/KatalogEintrag';
import { KatalogEintragOrte } from '../core/data/kataloge/KatalogEintragOrte';
import { KatalogEintragOrtsteile } from '../core/data/kataloge/KatalogEintragOrtsteile';
import { KatalogEintragStrassen } from '../core/data/kataloge/KatalogEintragStrassen';
import { KindergartenbesuchKatalogEintrag } from '../core/data/schule/KindergartenbesuchKatalogEintrag';
import { KlassenartKatalogEintrag } from '../core/data/klassen/KlassenartKatalogEintrag';
import { KlassenDaten } from '../core/data/klassen/KlassenDaten';
import { KlassenListeEintrag } from '../core/data/klassen/KlassenListeEintrag';
import { KursartKatalogEintrag } from '../core/data/kurse/KursartKatalogEintrag';
import { KursDaten } from '../core/data/kurse/KursDaten';
import { KursListeEintrag } from '../core/data/kurse/KursListeEintrag';
import { LehrerKatalogAbgangsgrundEintrag } from '../core/data/lehrer/LehrerKatalogAbgangsgrundEintrag';
import { LehrerKatalogAnrechnungsgrundEintrag } from '../core/data/lehrer/LehrerKatalogAnrechnungsgrundEintrag';
import { LehrerKatalogBeschaeftigungsartEintrag } from '../core/data/lehrer/LehrerKatalogBeschaeftigungsartEintrag';
import { LehrerKatalogEinsatzstatusEintrag } from '../core/data/lehrer/LehrerKatalogEinsatzstatusEintrag';
import { LehrerKatalogFachrichtungAnerkennungEintrag } from '../core/data/lehrer/LehrerKatalogFachrichtungAnerkennungEintrag';
import { LehrerKatalogFachrichtungEintrag } from '../core/data/lehrer/LehrerKatalogFachrichtungEintrag';
import { LehrerKatalogLehramtAnerkennungEintrag } from '../core/data/lehrer/LehrerKatalogLehramtAnerkennungEintrag';
import { LehrerKatalogLehramtEintrag } from '../core/data/lehrer/LehrerKatalogLehramtEintrag';
import { LehrerKatalogLehrbefaehigungAnerkennungEintrag } from '../core/data/lehrer/LehrerKatalogLehrbefaehigungAnerkennungEintrag';
import { LehrerKatalogLehrbefaehigungEintrag } from '../core/data/lehrer/LehrerKatalogLehrbefaehigungEintrag';
import { LehrerKatalogLeitungsfunktionenEintrag } from '../core/data/lehrer/LehrerKatalogLeitungsfunktionenEintrag';
import { LehrerKatalogMehrleistungsartEintrag } from '../core/data/lehrer/LehrerKatalogMehrleistungsartEintrag';
import { LehrerKatalogMinderleistungsartEintrag } from '../core/data/lehrer/LehrerKatalogMinderleistungsartEintrag';
import { LehrerKatalogRechtsverhaeltnisEintrag } from '../core/data/lehrer/LehrerKatalogRechtsverhaeltnisEintrag';
import { LehrerKatalogZugangsgrundEintrag } from '../core/data/lehrer/LehrerKatalogZugangsgrundEintrag';
import { LehrerListeEintrag } from '../core/data/lehrer/LehrerListeEintrag';
import { LehrerPersonalabschnittsdaten } from '../core/data/lehrer/LehrerPersonalabschnittsdaten';
import { LehrerPersonaldaten } from '../core/data/lehrer/LehrerPersonaldaten';
import { LehrerStammdaten } from '../core/data/lehrer/LehrerStammdaten';
import { List } from '../java/util/List';
import { NationalitaetenKatalogEintrag } from '../core/data/schule/NationalitaetenKatalogEintrag';
import { NotenKatalogEintrag } from '../core/data/schule/NotenKatalogEintrag';
import { OrganisationsformKatalogEintrag } from '../core/data/schule/OrganisationsformKatalogEintrag';
import { OrtKatalogEintrag } from '../core/data/kataloge/OrtKatalogEintrag';
import { OrtsteilKatalogEintrag } from '../core/data/kataloge/OrtsteilKatalogEintrag';
import { PruefungsordnungKatalogEintrag } from '../core/data/schule/PruefungsordnungKatalogEintrag';
import { Raum } from '../core/data/schule/Raum';
import { ReformpaedagogikKatalogEintrag } from '../core/data/schule/ReformpaedagogikKatalogEintrag';
import { ReligionEintrag } from '../core/data/schule/ReligionEintrag';
import { ReligionKatalogEintrag } from '../core/data/schule/ReligionKatalogEintrag';
import { Schild3KatalogEintragAbiturInfos } from '../core/data/schild3/Schild3KatalogEintragAbiturInfos';
import { Schild3KatalogEintragDatenart } from '../core/data/schild3/Schild3KatalogEintragDatenart';
import { Schild3KatalogEintragDQRNiveaus } from '../core/data/schild3/Schild3KatalogEintragDQRNiveaus';
import { Schild3KatalogEintragExportCSV } from '../core/data/schild3/Schild3KatalogEintragExportCSV';
import { Schild3KatalogEintragFilterFehlendeEintraege } from '../core/data/schild3/Schild3KatalogEintragFilterFehlendeEintraege';
import { Schild3KatalogEintragLaender } from '../core/data/schild3/Schild3KatalogEintragLaender';
import { Schild3KatalogEintragPruefungsordnung } from '../core/data/schild3/Schild3KatalogEintragPruefungsordnung';
import { Schild3KatalogEintragPruefungsordnungOption } from '../core/data/schild3/Schild3KatalogEintragPruefungsordnungOption';
import { Schild3KatalogEintragUnicodeUmwandlung } from '../core/data/schild3/Schild3KatalogEintragUnicodeUmwandlung';
import { Schild3KatalogEintragVersetzungsvermerke } from '../core/data/schild3/Schild3KatalogEintragVersetzungsvermerke';
import { SchuelerBetriebsdaten } from '../core/data/schueler/SchuelerBetriebsdaten';
import { SchuelerKAoADaten } from '../core/data/schueler/SchuelerKAoADaten';
import { SchuelerLeistungsdaten } from '../core/data/schueler/SchuelerLeistungsdaten';
import { SchuelerLernabschnittBemerkungen } from '../core/data/schueler/SchuelerLernabschnittBemerkungen';
import { SchuelerLernabschnittListeEintrag } from '../core/data/schueler/SchuelerLernabschnittListeEintrag';
import { SchuelerLernabschnittsdaten } from '../core/data/schueler/SchuelerLernabschnittsdaten';
import { SchuelerListeEintrag } from '../core/data/schueler/SchuelerListeEintrag';
import { SchuelerSchulbesuchsdaten } from '../core/data/schueler/SchuelerSchulbesuchsdaten';
import { SchuelerStammdaten } from '../core/data/schueler/SchuelerStammdaten';
import { SchuelerstatusKatalogEintrag } from '../core/data/schule/SchuelerstatusKatalogEintrag';
import { SchulabschlussAllgemeinbildendKatalogEintrag } from '../core/data/schule/SchulabschlussAllgemeinbildendKatalogEintrag';
import { SchulabschlussBerufsbildendKatalogEintrag } from '../core/data/schule/SchulabschlussBerufsbildendKatalogEintrag';
import { SchulenKatalogEintrag } from '../core/data/schule/SchulenKatalogEintrag';
import { SchuleStammdaten } from '../core/data/schule/SchuleStammdaten';
import { SchulformKatalogEintrag } from '../core/data/schule/SchulformKatalogEintrag';
import { SchulgliederungKatalogEintrag } from '../core/data/schule/SchulgliederungKatalogEintrag';
import { SchulstufeKatalogEintrag } from '../core/data/schule/SchulstufeKatalogEintrag';
import { SchultraegerKatalogEintrag } from '../core/data/schule/SchultraegerKatalogEintrag';
import { SimpleOperationResponse } from '../core/data/SimpleOperationResponse';
import { Sprachbelegung } from '../core/data/schueler/Sprachbelegung';
import { Sprachpruefung } from '../core/data/schueler/Sprachpruefung';
import { SprachpruefungsniveauKatalogEintrag } from '../core/data/fach/SprachpruefungsniveauKatalogEintrag';
import { SprachreferenzniveauKatalogEintrag } from '../core/data/fach/SprachreferenzniveauKatalogEintrag';
import { Stundenplan } from '../core/data/stundenplan/Stundenplan';
import { StundenplanAufsichtsbereich } from '../core/data/stundenplan/StundenplanAufsichtsbereich';
import { StundenplanKalenderwochenzuordnung } from '../core/data/stundenplan/StundenplanKalenderwochenzuordnung';
import { StundenplanKomplett } from '../core/data/stundenplan/StundenplanKomplett';
import { StundenplanLehrer } from '../core/data/stundenplan/StundenplanLehrer';
import { StundenplanListeEintrag } from '../core/data/stundenplan/StundenplanListeEintrag';
import { StundenplanPausenaufsicht } from '../core/data/stundenplan/StundenplanPausenaufsicht';
import { StundenplanPausenzeit } from '../core/data/stundenplan/StundenplanPausenzeit';
import { StundenplanRaum } from '../core/data/stundenplan/StundenplanRaum';
import { StundenplanSchiene } from '../core/data/stundenplan/StundenplanSchiene';
import { StundenplanUnterricht } from '../core/data/stundenplan/StundenplanUnterricht';
import { StundenplanUnterrichtsverteilung } from '../core/data/stundenplan/StundenplanUnterrichtsverteilung';
import { StundenplanZeitraster } from '../core/data/stundenplan/StundenplanZeitraster';
import { UebergangsempfehlungKatalogEintrag } from '../core/data/schueler/UebergangsempfehlungKatalogEintrag';
import { VerkehrsspracheKatalogEintrag } from '../core/data/schule/VerkehrsspracheKatalogEintrag';

export class ApiServer extends BaseApi {

	/**
	 *
	 * Erstellt eine neue API mit der übergebenen Konfiguration.
	 *
	 * @param {string} url - die URL des Servers: Alle Pfadangaben sind relativ zu dieser URL
	 * @param {string} username - der Benutzername für den API-Zugriff
	 * @param {string} password - das Kennwort des Benutzers für den API-Zugriff
	 */
	public constructor(url : string, username : string, password : string) {
		super(url, username, password);
	}

	/**
	 * Implementierung der GET-Methode getConfigCertificate für den Zugriff auf die URL https://{hostname}/config/certificate
	 *
	 * Gibt das Zertifikat des Server zurück.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Das Zertifikat des Servers
	 *     - Mime-Type: text/plain
	 *     - Rückgabe-Typ: String
	 *   Code 500: Das Zertifikat wurde nicht gefunden
	 *
	 * @returns Das Zertifikat des Servers
	 */
	public async getConfigCertificate() : Promise<string | null> {
		const path = "/config/certificate";
		const text : string = await super.getText(path);
		return text;
	}


	/**
	 * Implementierung der GET-Methode getConfigCertificateBase64 für den Zugriff auf die URL https://{hostname}/config/certificate_base64
	 *
	 * Gibt das Zertifikat des Server in Base64-Kodierung zurück.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Das Base-64-kodierte Zertifikat des Servers
	 *     - Mime-Type: text/plain
	 *     - Rückgabe-Typ: String
	 *   Code 500: Das Zertifikat wurde nicht gefunden
	 *
	 * @returns Das Base-64-kodierte Zertifikat des Servers
	 */
	public async getConfigCertificateBase64() : Promise<string | null> {
		const path = "/config/certificate_base64";
		const text : string = await super.getText(path);
		return text;
	}


	/**
	 * Implementierung der GET-Methode getConfigDBSchemata für den Zugriff auf die URL https://{hostname}/config/db/schemata
	 *
	 * Gibt eine sortierte Übersicht von allen konfigurierten DB-Schemata zurück.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von DB-Schema-Listen-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<DBSchemaListeEintrag>
	 *
	 * @returns Eine Liste von DB-Schema-Listen-Einträgen
	 */
	public async getConfigDBSchemata() : Promise<List<DBSchemaListeEintrag>> {
		const path = "/config/db/schemata";
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<DBSchemaListeEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(DBSchemaListeEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getConfigPublicKeyBase64 für den Zugriff auf die URL https://{hostname}/config/publickey_base64
	 *
	 * Gibt den öffentlichen Schlüssel des Server in Base64-Kodierung zurück.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Base-64-kodierte, öffentliche Schlüssel des Servers
	 *     - Mime-Type: text/plain
	 *     - Rückgabe-Typ: String
	 *   Code 500: Der öffentliche Schlüssel wurde nicht gefunden
	 *
	 * @returns Der Base-64-kodierte, öffentliche Schlüssel des Servers
	 */
	public async getConfigPublicKeyBase64() : Promise<string | null> {
		const path = "/config/publickey_base64";
		const text : string = await super.getText(path);
		return text;
	}


	/**
	 * Implementierung der GET-Methode getKatalogOrte für den Zugriff auf die URL https://{hostname}/db/{schema}/allgemein/orte
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhandenen Orte. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Orts-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<KatalogEintragOrte>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Orts-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Orts-Katalog-Einträgen
	 */
	public async getKatalogOrte(schema : string) : Promise<List<KatalogEintragOrte>> {
		const path = "/db/{schema}/allgemein/orte"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KatalogEintragOrte>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KatalogEintragOrte.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogOrtsteile für den Zugriff auf die URL https://{hostname}/db/{schema}/allgemein/ortsteile
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhandenen Ortsteile. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Ortsteil-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<KatalogEintragOrtsteile>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Ortsteil-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Ortsteil-Katalog-Einträgen
	 */
	public async getKatalogOrtsteile(schema : string) : Promise<List<KatalogEintragOrtsteile>> {
		const path = "/db/{schema}/allgemein/ortsteile"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KatalogEintragOrtsteile>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KatalogEintragOrtsteile.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogStrassen für den Zugriff auf die URL https://{hostname}/db/{schema}/allgemein/strassen
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhandenen Strassen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Straßen-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<KatalogEintragStrassen>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Straßen-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Straßen-Katalog-Einträgen
	 */
	public async getKatalogStrassen(schema : string) : Promise<List<KatalogEintragStrassen>> {
		const path = "/db/{schema}/allgemein/strassen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KatalogEintragStrassen>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KatalogEintragStrassen.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getBenutzerliste für den Zugriff auf die URL https://{hostname}/db/{schema}/benutzer/
	 *
	 * Erstellt eine Liste aller Benutzer.Es wird geprüft, ob der SVWS-Benutzer die notwendige Administrator-Berechtigung besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Benutzer-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<BenutzerListeEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Benutzerdaten anzusehen.
	 *   Code 404: Keine Benutzer-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Benutzer-Einträgen
	 */
	public async getBenutzerliste(schema : string) : Promise<List<BenutzerListeEintrag>> {
		const path = "/db/{schema}/benutzer/"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<BenutzerListeEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(BenutzerListeEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getBenutzerDaten für den Zugriff auf die URL https://{hostname}/db/{schema}/benutzer/{id : \d+}
	 *
	 * Liest die Daten des Benutzers zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Benutzerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Daten des Benutzers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: BenutzerDaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Benutzerdaten anzusehen.
	 *   Code 404: Kein Benutzer-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Daten des Benutzers
	 */
	public async getBenutzerDaten(schema : string, id : number) : Promise<BenutzerDaten> {
		const path = "/db/{schema}/benutzer/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return BenutzerDaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode addBenutzerAdmin für den Zugriff auf die URL https://{hostname}/db/{schema}/benutzer/{id : \d+}/addAdmin
	 *
	 * Setzt Admin-Berechtigung für den Benutzer.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die Information wurde erfolgreich gesetzt.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Benutzer als administrativer Benutzer zu setzen
	 *   Code 404: Der Benutzer ist nicht vorhanden.
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async addBenutzerAdmin(schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/benutzer/{id : \\d+}/addAdmin"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		await super.postJSON(path, null);
		return;
	}


	/**
	 * Implementierung der POST-Methode setBenutzername für den Zugriff auf die URL https://{hostname}/db/{schema}/benutzer/{id : \d+}/anmeldename
	 *
	 * Setzt den Anmeldenamen eines Benutzers.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Setzen des Anmeldenamenss besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Der Anmeldename wurde erfolgreich gesetzt.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um das Kennwort zu setzen.
	 *   Code 404: Der Anmeldename zu dem Benutzer sind nicht vorhanden.
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string | null} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async setBenutzername(data : string | null, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/benutzer/{id : \\d+}/anmeldename"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = JSON.stringify(data);
		await super.postJSON(path, body);
		return;
	}


	/**
	 * Implementierung der POST-Methode setAnzeigename für den Zugriff auf die URL https://{hostname}/db/{schema}/benutzer/{id : \d+}/anzeigename
	 *
	 * Setzt den Anzeigenamen eines Benutzers.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Setzen des Anzeigenamens besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Der Anzeigename wurde erfolgreich gesetzt.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Anzeigenamen zu setzen.
	 *   Code 404: Der Anzeigename zu dem Benutzer sind nicht vorhanden.
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string | null} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async setAnzeigename(data : string | null, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/benutzer/{id : \\d+}/anzeigename"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = JSON.stringify(data);
		await super.postJSON(path, body);
		return;
	}


	/**
	 * Implementierung der POST-Methode addBenutzerKompetenzen für den Zugriff auf die URL https://{hostname}/db/{schema}/benutzer/{id : \d+}/kompetenz/add
	 *
	 * Fügt Kompetenzen bei einem Benutzer hinzu.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Setzen der Kompetenzen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die Kompetenzen wurden erfolgreich hinzugefügt.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Kompetenzen zu hinzuzufügen.
	 *   Code 404: Benötigte Information zum Benutzer wurden nicht in der DB gefunden.
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async addBenutzerKompetenzen(data : List<number>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/benutzer/{id : \\d+}/kompetenz/add"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = "[" + data.toArray().map(d => JSON.stringify(d)).join() + "]";
		await super.postJSON(path, body);
		return;
	}


	/**
	 * Implementierung der DELETE-Methode removeBenutzerKompetenzen für den Zugriff auf die URL https://{hostname}/db/{schema}/benutzer/{id : \d+}/kompetenz/remove
	 *
	 * Entfernt Kompetenzen bei einem Benutzer.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen der Kompetenzen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die Kompetenzen wurden erfolgreich entfernt.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Kompetenzen zu entfernen.
	 *   Code 404: Benötigte Information zum Benutzer wurden nicht in der DB gefunden.
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async removeBenutzerKompetenzen(data : List<number>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/benutzer/{id : \\d+}/kompetenz/remove"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = "[" + data.toArray().map(d => JSON.stringify(d)).join() + "]";
		await super.deleteJSON(path, body);
		return;
	}


	/**
	 * Implementierung der POST-Methode setPassword für den Zugriff auf die URL https://{hostname}/db/{schema}/benutzer/{id : \d+}/password
	 *
	 * Setzt das neue Passwort eines Benutzers.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Setzen des Kennwortes besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Das Passwort wurde erfolgreich gesetzt.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um das Kennwort zu setzen.
	 *   Code 404: Das Passwort zu dem Benutzer sind nicht vorhanden.
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string | null} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async setPassword(data : string | null, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/benutzer/{id : \\d+}/password"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = JSON.stringify(data);
		await super.postJSON(path, body);
		return;
	}


	/**
	 * Implementierung der DELETE-Methode removeBenutzerAdmin für den Zugriff auf die URL https://{hostname}/db/{schema}/benutzer/{id : \d+}/removeAdmin
	 *
	 * Entfernt die Admin-Berechtigung des Benutzers mit der id.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen  der Admin-Berechtigung hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die Admin-Berechtigung wurde erfolgreich entfernt.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Admin-Berechtigung zu entfernen.
	 *   Code 404: Der Benutzer ist nicht vorhanden.
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async removeBenutzerAdmin(schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/benutzer/{id : \\d+}/removeAdmin"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		await super.deleteJSON(path, null);
		return;
	}


	/**
	 * Implementierung der GET-Methode getKatalogBenutzerkompetenzen für den Zugriff auf die URL https://{hostname}/db/{schema}/benutzer/allgemein/kompetenzen
	 *
	 * Liefert den Katalog der Benutzerkompetenzen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Kataloge besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Katalog der Benutzerkompetenzen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<BenutzerKompetenzKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Katalog anzusehen.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Katalog der Benutzerkompetenzen
	 */
	public async getKatalogBenutzerkompetenzen(schema : string) : Promise<List<BenutzerKompetenzKatalogEintrag>> {
		const path = "/db/{schema}/benutzer/allgemein/kompetenzen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<BenutzerKompetenzKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(BenutzerKompetenzKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogBenutzerkompetenzgruppen für den Zugriff auf die URL https://{hostname}/db/{schema}/benutzer/allgemein/kompetenzgruppen
	 *
	 * Liefert den Katalog der Benutzerkompetenzgruppen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Kataloge besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Katalog der Benutzerkompetenzgruppen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<BenutzerKompetenzGruppenKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Katalog anzusehen.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Katalog der Benutzerkompetenzgruppen
	 */
	public async getKatalogBenutzerkompetenzgruppen(schema : string) : Promise<List<BenutzerKompetenzGruppenKatalogEintrag>> {
		const path = "/db/{schema}/benutzer/allgemein/kompetenzgruppen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<BenutzerKompetenzGruppenKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(BenutzerKompetenzGruppenKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getBenutzerDatenEigene für den Zugriff auf die URL https://{hostname}/db/{schema}/benutzer/angemeldet/daten
	 *
	 * Liefert zu dem angemeldeten Benutzer, der diese Abfrage ausführt, die zugehörigen Daten. Dabei wird geprüft, ob der SVWS-Benutzer angemeldet ist.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Daten des Benutzers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: BenutzerDaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Benutzerdaten anzusehen.
	 *   Code 404: Kein Benutzer-Eintrag gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Daten des Benutzers
	 */
	public async getBenutzerDatenEigene(schema : string) : Promise<BenutzerDaten> {
		const path = "/db/{schema}/benutzer/angemeldet/daten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const text = result;
		return BenutzerDaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode createBenutzergruppe für den Zugriff auf die URL https://{hostname}/db/{schema}/benutzer/benutzergruppe/new
	 *
	 * Erstellt eine neue Benutzergruppe und gibt sie zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Benutzergruppe besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Benutzergruppe wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: BenutzergruppeDaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Benutzer anzulegen.
	 *   Code 409: Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {BenutzergruppeDaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Benutzergruppe wurde erfolgreich angelegt.
	 */
	public async createBenutzergruppe(data : BenutzergruppeDaten, schema : string) : Promise<BenutzergruppeDaten> {
		const path = "/db/{schema}/benutzer/benutzergruppe/new"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = BenutzergruppeDaten.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return BenutzergruppeDaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getBenutzergruppenliste für den Zugriff auf die URL https://{hostname}/db/{schema}/benutzer/gruppe
	 *
	 * Erstellt eine Liste aller Benutzergruppen.Es wird geprüft, ob der SVWS-Benutzer die notwendige Administrator-Berechtigung besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Benutzergruppen-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<BenutzergruppeListeEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Benutzergruppendaten anzusehen.
	 *   Code 404: Keine Benutzergruppen-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Benutzergruppen-Einträgen
	 */
	public async getBenutzergruppenliste(schema : string) : Promise<List<BenutzergruppeListeEintrag>> {
		const path = "/db/{schema}/benutzer/gruppe"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<BenutzergruppeListeEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(BenutzergruppeListeEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getBenutzergruppeDaten für den Zugriff auf die URL https://{hostname}/db/{schema}/benutzer/gruppe/{id : \d+}
	 *
	 * Liest die Daten der Benutzergruppe zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Benutzergruppendaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Daten der Benutzergruppe
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: BenutzergruppeDaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Benutzergruppendaten anzusehen.
	 *   Code 404: Kein Benutzergruppen-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Daten der Benutzergruppe
	 */
	public async getBenutzergruppeDaten(schema : string, id : number) : Promise<BenutzergruppeDaten> {
		const path = "/db/{schema}/benutzer/gruppe/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return BenutzergruppeDaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode addBenutzergruppeAdmin für den Zugriff auf die URL https://{hostname}/db/{schema}/benutzer/gruppe/{id : \d+}/addAdmin
	 *
	 * Setzt ob die Benutzergruppe eine administrative Benutzergruppe ist oder nicht.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die Information wurde erfolgreich gesetzt.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Gruppe als administrative Gruppe zu setzen
	 *   Code 404: Die Benutzergruppe ist nicht vorhanden.
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async addBenutzergruppeAdmin(schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/benutzer/gruppe/{id : \\d+}/addAdmin"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		await super.postJSON(path, null);
		return;
	}


	/**
	 * Implementierung der GET-Methode getBenutzerMitGruppenID für den Zugriff auf die URL https://{hostname}/db/{schema}/benutzer/gruppe/{id : \d+}/benutzer
	 *
	 * Liest die Benutzer der Benutzergruppe zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Benutzergruppendaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Benutzer der Benutzergruppe
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<BenutzerListeEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Benutzergruppendaten anzusehen.
	 *   Code 404: Kein Benutzergruppen-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Benutzer der Benutzergruppe
	 */
	public async getBenutzerMitGruppenID(schema : string, id : number) : Promise<List<BenutzerListeEintrag>> {
		const path = "/db/{schema}/benutzer/gruppe/{id : \\d+}/benutzer"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<BenutzerListeEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(BenutzerListeEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode addBenutzergruppeBenutzer für den Zugriff auf die URL https://{hostname}/db/{schema}/benutzer/gruppe/{id : \d+}/benutzer/add
	 *
	 * Fügt Benutzer bei einer Benutzergruppe hinzu.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Hinzufügen der Benutzer besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Benutzer wurden erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: BenutzergruppeDaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um neue Benutzer hinzuzufügen.
	 *   Code 404: Benötigte Information zum Benutzer wurden nicht in der DB gefunden.
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Benutzer wurden erfolgreich hinzugefügt.
	 */
	public async addBenutzergruppeBenutzer(data : List<number>, schema : string, id : number) : Promise<BenutzergruppeDaten> {
		const path = "/db/{schema}/benutzer/gruppe/{id : \\d+}/benutzer/add"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = "[" + data.toArray().map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.postJSON(path, body);
		const text = result;
		return BenutzergruppeDaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der DELETE-Methode removeBenutzergruppeBenutzer für den Zugriff auf die URL https://{hostname}/db/{schema}/benutzer/gruppe/{id : \d+}/benutzer/remove
	 *
	 * Entfernt Benutzer bei einer Benutzergruppe hinzu.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen der Benutzer besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Benutzer wurden erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: BenutzergruppeDaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um neue Benutzer zu entfernen.
	 *   Code 404: Benötigte Information zum Benutzer wurden nicht in der DB gefunden.
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Benutzer wurden erfolgreich hinzugefügt.
	 */
	public async removeBenutzergruppeBenutzer(data : List<number>, schema : string, id : number) : Promise<BenutzergruppeDaten> {
		const path = "/db/{schema}/benutzer/gruppe/{id : \\d+}/benutzer/remove"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = "[" + data.toArray().map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const text = result;
		return BenutzergruppeDaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode setBenutzergruppeBezeichnung für den Zugriff auf die URL https://{hostname}/db/{schema}/benutzer/gruppe/{id : \d+}/bezeichnung
	 *
	 * Setzt die Bezeichnung der Benutzergruppe.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die Information wurde erfolgreich gesetzt.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Bezeichnung der Gruppe zu setzen
	 *   Code 404: Die Benutzergruppe ist nicht vorhanden.
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string | null} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async setBenutzergruppeBezeichnung(data : string | null, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/benutzer/gruppe/{id : \\d+}/bezeichnung"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = JSON.stringify(data);
		await super.postJSON(path, body);
		return;
	}


	/**
	 * Implementierung der POST-Methode addBenutzergruppeKompetenzen für den Zugriff auf die URL https://{hostname}/db/{schema}/benutzer/gruppe/{id : \d+}/kompetenz/add
	 *
	 * Fügt Kompetenzen bei einer Benutzergruppe hinzu.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Setzen der Kompetenzen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die Kompetenzen wurden erfolgreich hinzugefügt.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Kompetenzen zu hinzuzufügen.
	 *   Code 404: Benötigte Information zum Benutzer wurden nicht in der DB gefunden.
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async addBenutzergruppeKompetenzen(data : List<number>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/benutzer/gruppe/{id : \\d+}/kompetenz/add"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = "[" + data.toArray().map(d => JSON.stringify(d)).join() + "]";
		await super.postJSON(path, body);
		return;
	}


	/**
	 * Implementierung der DELETE-Methode removeBenutzergruppeKompetenzen für den Zugriff auf die URL https://{hostname}/db/{schema}/benutzer/gruppe/{id : \d+}/kompetenz/remove
	 *
	 * Entfernt Kompetenzen bei einer Benutzergruppe hinzu.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entferen der Kompetenzen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die Kompetenzen wurden erfolgreich hinzugefügt.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Kompetenzen zu entfernen.
	 *   Code 404: Benötigte Information zum Benutzer wurden nicht in der DB gefunden.
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async removeBenutzergruppeKompetenzen(data : List<number>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/benutzer/gruppe/{id : \\d+}/kompetenz/remove"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = "[" + data.toArray().map(d => JSON.stringify(d)).join() + "]";
		await super.deleteJSON(path, body);
		return;
	}


	/**
	 * Implementierung der DELETE-Methode removeBenutzergruppeAdmin für den Zugriff auf die URL https://{hostname}/db/{schema}/benutzer/gruppe/{id : \d+}/removeAdmin
	 *
	 * Entfernt die Admin-Berechtigung er Benutzergruppe mit der idDabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen  der Admin-Berechtigung hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die Information wurde erfolgreich gesetzt.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Admin-Berechtigung zu entfernen.
	 *   Code 404: Die Benutzergruppe ist nicht vorhanden.
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async removeBenutzergruppeAdmin(schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/benutzer/gruppe/{id : \\d+}/removeAdmin"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		await super.deleteJSON(path, null);
		return;
	}


	/**
	 * Implementierung der DELETE-Methode removeBenutzerGruppe für den Zugriff auf die URL https://{hostname}/db/{schema}/benutzer/guppe/remove
	 *
	 * Löscht eine oder mehrere Benutzergruppe.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die Benutzergruppen wurden erfolgreich gelöscht.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Benutzergruppen zu löschen.
	 *   Code 404: Benötigte Information zur Benutzergruppe wurden nicht in der DB gefunden.
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 */
	public async removeBenutzerGruppe(data : List<number>, schema : string) : Promise<void> {
		const path = "/db/{schema}/benutzer/guppe/remove"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = "[" + data.toArray().map(d => JSON.stringify(d)).join() + "]";
		await super.deleteJSON(path, body);
		return;
	}


	/**
	 * Implementierung der POST-Methode createBenutzerAllgemein für den Zugriff auf die URL https://{hostname}/db/{schema}/benutzer/new
	 *
	 * Erstellt einen neuen Benutzer und gibt ihn zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Benutzers besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Benutzer wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: BenutzerDaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Benutzer anzulegen.
	 *   Code 409: Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {BenutzerAllgemeinCredentials} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Benutzer wurde erfolgreich angelegt.
	 */
	public async createBenutzerAllgemein(data : BenutzerAllgemeinCredentials, schema : string) : Promise<BenutzerDaten> {
		const path = "/db/{schema}/benutzer/new"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = BenutzerAllgemeinCredentials.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return BenutzerDaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der DELETE-Methode removeBenutzerMenge für den Zugriff auf die URL https://{hostname}/db/{schema}/benutzer/remove
	 *
	 * Löscht einen oder mehrere Benutzer.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Benutzer wurden erfolgreich gelöscht.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Benutzer zu löschen.
	 *   Code 404: Benötigte Information zum Benutzer wurden nicht in der DB gefunden.
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 */
	public async removeBenutzerMenge(data : List<number>, schema : string) : Promise<void> {
		const path = "/db/{schema}/benutzer/remove"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = "[" + data.toArray().map(d => JSON.stringify(d)).join() + "]";
		await super.deleteJSON(path, body);
		return;
	}


	/**
	 * Implementierung der GET-Methode getBetriebe für den Zugriff auf die URL https://{hostname}/db/{schema}/betriebe/
	 *
	 * Erstellt eine Liste aller in der Datenbank vorhandenen Betriebe unter Angabe der ID, der Betriebsart , des Betriebnamens, Kontaktdaten, ob sie in der Anwendung sichtbar bzw. änderbar sein sollen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Betriebsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Betrieb-Listen-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<BetriebListeEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Betriebdaten anzusehen.
	 *   Code 404: Keine Betrieb-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Betrieb-Listen-Einträgen
	 */
	public async getBetriebe(schema : string) : Promise<List<BetriebListeEintrag>> {
		const path = "/db/{schema}/betriebe/"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<BetriebListeEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(BetriebListeEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode createBetriebansprechpartner für den Zugriff auf die URL https://{hostname}/db/{schema}/betriebe/{betrieb_id : \d+}/ansprechpartner/new
	 *
	 * Erstellt einen neuen Betriebansprechpartner und gibt die dazugehörige ID zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Betriebansprechpartners besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Ansprechpartner wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: BetriebAnsprechpartner
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Ansprechpartner anzulegen.
	 *   Code 404: Kein Betrieb  mit der angegebenen ID gefunden
	 *   Code 409: Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {BetriebAnsprechpartner} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} betrieb_id - der Pfad-Parameter betrieb_id
	 *
	 * @returns Ansprechpartner wurde erfolgreich angelegt.
	 */
	public async createBetriebansprechpartner(data : BetriebAnsprechpartner, schema : string, betrieb_id : number) : Promise<BetriebAnsprechpartner> {
		const path = "/db/{schema}/betriebe/{betrieb_id : \\d+}/ansprechpartner/new"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{betrieb_id\s*(:[^}]+)?}/g, betrieb_id.toString());
		const body : string = BetriebAnsprechpartner.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return BetriebAnsprechpartner.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchSchuelerBetriebsdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/betriebe/{id : \d+}/betrieb
	 *
	 * Passt die Schüler-Betriebsdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Schülerbetreibsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Schüler-Betriebsdaten integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Schülerdaten zu ändern.
	 *   Code 404: Kein Schülerbetrieb-Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<SchuelerBetriebsdaten>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchSchuelerBetriebsdaten(data : Partial<SchuelerBetriebsdaten>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/betriebe/{id : \\d+}/betrieb"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = SchuelerBetriebsdaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getSchuelerBetriebsdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/betriebe/{id : \d+}/betrieb
	 *
	 * Liest die Daten des Schülerbetriebs zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen vom Schülerbetriebbesitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Stammdaten des Schülerbetriebs.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SchuelerBetriebsdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Schülerbetreibe anzusehen.
	 *   Code 404: Kein Schülerbetrieb gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Stammdaten des Schülerbetriebs.
	 */
	public async getSchuelerBetriebsdaten(schema : string, id : number) : Promise<SchuelerBetriebsdaten> {
		const path = "/db/{schema}/betriebe/{id : \\d+}/betrieb"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return SchuelerBetriebsdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getBetriebAnsprechpartnerdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/betriebe/{id : \d+}/betriebansprechpartner
	 *
	 * Liest die Daten des Betriebanpsrechpartners zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen vom Betriebanpsrechpartnerbesitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Stammdaten des Betriebanpsrechpartners.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<BetriebAnsprechpartner>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Betriebanpsrechpartner anzusehen.
	 *   Code 404: Kein Betriebanpsrechpartner gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Stammdaten des Betriebanpsrechpartners.
	 */
	public async getBetriebAnsprechpartnerdaten(schema : string, id : number) : Promise<List<BetriebAnsprechpartner>> {
		const path = "/db/{schema}/betriebe/{id : \\d+}/betriebansprechpartner"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<BetriebAnsprechpartner>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(BetriebAnsprechpartner.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der PATCH-Methode patchBetriebanpsrechpartnerdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/betriebe/{id : \d+}/betriebansprechpartner
	 *
	 * Passt die Betriebanpsrechpartner-Daten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern vom Betriebanpsrechpartner besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Betriebanpsrechpartner-Daten integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Betriebanpsrechpartner-Datenn zu ändern.
	 *   Code 404: Kein Betriebanpsrechpartner-Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<BetriebAnsprechpartner>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchBetriebanpsrechpartnerdaten(data : Partial<BetriebAnsprechpartner>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/betriebe/{id : \\d+}/betriebansprechpartner"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = BetriebAnsprechpartner.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getBetriebStammdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/betriebe/{id : \d+}/stammdaten
	 *
	 * Liest die Stammdaten des Betriebs zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Stammdaten eines Betriebs
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: BetriebStammdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Betriebsdaten anzusehen.
	 *   Code 404: Kein Betrieb-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Stammdaten eines Betriebs
	 */
	public async getBetriebStammdaten(schema : string, id : number) : Promise<BetriebStammdaten> {
		const path = "/db/{schema}/betriebe/{id : \\d+}/stammdaten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return BetriebStammdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchBetriebStammdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/betriebe/{id : \d+}/stammdaten
	 *
	 * Passt die Betrieb-Stammdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Erzieherdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Betrieb-Stammdaten integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Betriebdaten zu ändern.
	 *   Code 404: Kein Betrieb-Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<BetriebStammdaten>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchBetriebStammdaten(data : Partial<BetriebStammdaten>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/betriebe/{id : \\d+}/stammdaten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = BetriebStammdaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getBetriebAnsprechpartner für den Zugriff auf die URL https://{hostname}/db/{schema}/betriebe/{id : \d+}betriebansprechpartnerliste
	 *
	 * Erstellt eine Liste aller in der Datenbank vorhandenen Betriebansprechpartner , des Ansprechpartnername, Kontaktdaten, ob sie in der Anwendung sichtbar bzw. änderbar sein sollen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Betriebsansprechpartnern besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Betriebansprechpartnern
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<BetriebAnsprechpartner>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Betriebdaten anzusehen.
	 *   Code 404: Keine Betrieb-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Eine Liste von Betriebansprechpartnern
	 */
	public async getBetriebAnsprechpartner(schema : string, id : number) : Promise<List<BetriebAnsprechpartner>> {
		const path = "/db/{schema}/betriebe/{id : \\d+}betriebansprechpartnerliste"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<BetriebAnsprechpartner>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(BetriebAnsprechpartner.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogBeschaeftigungsart für den Zugriff auf die URL https://{hostname}/db/{schema}/betriebe/beschaeftigungsart
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhandenen Beschäftigungsarten unter Angabe der ID, eines Kürzels und der textuellen Beschreibung sowie der Information, ob der Eintrag in der Anwendung sichtbar bzw. änderbar sein soll, undgibt diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen zu den Beschäftigungsarten.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<KatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen zu den Beschäftigungsarten.
	 */
	public async getKatalogBeschaeftigungsart(schema : string) : Promise<List<KatalogEintrag>> {
		const path = "/db/{schema}/betriebe/beschaeftigungsart"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogBeschaeftigungsartmitID für den Zugriff auf die URL https://{hostname}/db/{schema}/betriebe/beschaeftigungsart/{id : \d+}
	 *
	 * Liest die Daten der Beschäftigunsart zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Beschäftigungsartbesitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Katalog-Eintrag zu den Beschäftigungsarten.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<KatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Katalog-Eintrag zu den Beschäftigungsarten.
	 */
	public async getKatalogBeschaeftigungsartmitID(schema : string, id : number) : Promise<List<KatalogEintrag>> {
		const path = "/db/{schema}/betriebe/beschaeftigungsart/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der PATCH-Methode patchBeschaeftigungsart für den Zugriff auf die URL https://{hostname}/db/{schema}/betriebe/beschaeftigungsart/{id : \d+}
	 *
	 * Passt die Beschäftigungsart-Stammdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern der Daten der Beschäftigungsart besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Beschäftigungsart-Daten integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Beschäftigungsart-Daten zu ändern.
	 *   Code 404: Keine Beschäftigungsart mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<KatalogEintrag>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchBeschaeftigungsart(data : Partial<KatalogEintrag>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/betriebe/beschaeftigungsart/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = KatalogEintrag.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getKatalogBetriebsartmitID für den Zugriff auf die URL https://{hostname}/db/{schema}/betriebe/beschaeftigungsart/{id : \d+}
	 *
	 * Liest die Daten der Betriebsart zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Betriebsartenbesitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Katalog-Eintrag zu den Betriebsarten.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<KatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Katalog-Eintrag zu den Betriebsarten.
	 */
	public async getKatalogBetriebsartmitID(schema : string, id : number) : Promise<List<KatalogEintrag>> {
		const path = "/db/{schema}/betriebe/beschaeftigungsart/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode createBeschaeftigungsArt für den Zugriff auf die URL https://{hostname}/db/{schema}/betriebe/beschaeftigungsart/new
	 *
	 * Erstellt eine neue Beschäftigungsart und gibt sie zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Beschäftigungsart besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Beschäftigungsart wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: KatalogEintrag
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Beschäftigungsart anzulegen.
	 *   Code 409: Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {KatalogEintrag} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Beschäftigungsart wurde erfolgreich angelegt.
	 */
	public async createBeschaeftigungsArt(data : KatalogEintrag, schema : string) : Promise<KatalogEintrag> {
		const path = "/db/{schema}/betriebe/beschaeftigungsart/new"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = KatalogEintrag.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return KatalogEintrag.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getBetriebeAnsprechpartner für den Zugriff auf die URL https://{hostname}/db/{schema}/betriebe/betriebansprechpartner
	 *
	 * Erstellt eine Liste aller in der Datenbank vorhandenen Betriebansprechpartner , des Ansprechpartnername, Kontaktdaten, ob sie in der Anwendung sichtbar bzw. änderbar sein sollen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Betriebsansprechpartnern besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Betriebansprechpartnern
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<BetriebAnsprechpartner>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Betriebdaten anzusehen.
	 *   Code 404: Keine Betrieb-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Betriebansprechpartnern
	 */
	public async getBetriebeAnsprechpartner(schema : string) : Promise<List<BetriebAnsprechpartner>> {
		const path = "/db/{schema}/betriebe/betriebansprechpartner"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<BetriebAnsprechpartner>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(BetriebAnsprechpartner.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der DELETE-Methode removeBetriebansprechpartner für den Zugriff auf die URL https://{hostname}/db/{schema}/betriebe/betriebansprechpartner/remove
	 *
	 * Löscht einen oder mehrere Betriebsansprechpartner.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die Betriebsansprechpartner wurden erfolgreich gelöscht.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Betriebsansprechpartner zu löschen.
	 *   Code 404: Benötigte Information zum Betriebsansprechpartner wurden nicht in der DB gefunden.
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 */
	public async removeBetriebansprechpartner(data : List<number>, schema : string) : Promise<void> {
		const path = "/db/{schema}/betriebe/betriebansprechpartner/remove"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = "[" + data.toArray().map(d => JSON.stringify(d)).join() + "]";
		await super.deleteJSON(path, body);
		return;
	}


	/**
	 * Implementierung der GET-Methode getKatalogBetriebsart für den Zugriff auf die URL https://{hostname}/db/{schema}/betriebe/betriebsart
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhandenen Betriebsarten unter Angabe der ID, eines Kürzels und der textuellen Beschreibung sowie der Information, ob der Eintrag in der Anwendung sichtbar bzw. änderbar sein soll, undgibt diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen zu den Betriebsarten.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<KatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen zu den Betriebsarten.
	 */
	public async getKatalogBetriebsart(schema : string) : Promise<List<KatalogEintrag>> {
		const path = "/db/{schema}/betriebe/betriebsart"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der PATCH-Methode patchBetriebsart für den Zugriff auf die URL https://{hostname}/db/{schema}/betriebe/betriebsart/{id : \d+}
	 *
	 * Passt die Beschäftigungsart-Stammdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern der Daten der Betriebssart besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Beschäftigungsart-Daten integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Beschäftigungsart-Daten zu ändern.
	 *   Code 404: Keine Beschäftigungsart mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<KatalogEintrag>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchBetriebsart(data : Partial<KatalogEintrag>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/betriebe/betriebsart/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = KatalogEintrag.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der POST-Methode createBetriebsart für den Zugriff auf die URL https://{hostname}/db/{schema}/betriebe/betriebsart/new
	 *
	 * Erstellt eine neue Betriebart und gibt den neuen Datensatz zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eine Betriebsart besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Betiebsart wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: KatalogEintrag
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Betriebsart anzulegen.
	 *   Code 404: Kein Betrieb  mit der angegebenen ID gefunden
	 *   Code 409: Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {KatalogEintrag} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Betiebsart wurde erfolgreich angelegt.
	 */
	public async createBetriebsart(data : KatalogEintrag, schema : string) : Promise<KatalogEintrag> {
		const path = "/db/{schema}/betriebe/betriebsart/new"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = KatalogEintrag.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return KatalogEintrag.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode createBetrieb für den Zugriff auf die URL https://{hostname}/db/{schema}/betriebe/new
	 *
	 * Erstellt einen neuen Betrieb und gibt den neuen Datensatz zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Betriebes besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Betieb wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: BetriebStammdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Betrieb anzulegen.
	 *   Code 404: Keine Betriebart oder kein Ort  mit der angegebenen ID gefunden
	 *   Code 409: Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {BetriebStammdaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Betieb wurde erfolgreich angelegt.
	 */
	public async createBetrieb(data : BetriebStammdaten, schema : string) : Promise<BetriebStammdaten> {
		const path = "/db/{schema}/betriebe/new"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = BetriebStammdaten.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return BetriebStammdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der DELETE-Methode removeBetrieb für den Zugriff auf die URL https://{hostname}/db/{schema}/betriebe/remove
	 *
	 * Löscht einen oder mehrere Betriebe.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die Betriebe wurden erfolgreich gelöscht.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Betriebe zu löschen.
	 *   Code 404: Benötigte Information zum Betrieb wurden nicht in der DB gefunden.
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 */
	public async removeBetrieb(data : List<number>, schema : string) : Promise<void> {
		const path = "/db/{schema}/betriebe/remove"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = "[" + data.toArray().map(d => JSON.stringify(d)).join() + "]";
		await super.deleteJSON(path, body);
		return;
	}


	/**
	 * Implementierung der POST-Methode createSchuelerbetrieb für den Zugriff auf die URL https://{hostname}/db/{schema}/betriebe/schuelerbetrieb/new/schueler/{schueler_id : \d+}/betrieb/{betrieb_id: \d+}
	 *
	 * Erstellt einen neuen Schülerbetrieb und gibt ihn zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Schülerbetriebs besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Schülerbetrieb wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SchuelerBetriebsdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Schülerbetrieb anzulegen.
	 *   Code 404: Kein Betrieb  mit der angegebenen ID gefunden
	 *   Code 409: Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {SchuelerBetriebsdaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} schueler_id - der Pfad-Parameter schueler_id
	 * @param {number} betrieb_id - der Pfad-Parameter betrieb_id
	 *
	 * @returns Schülerbetrieb wurde erfolgreich angelegt.
	 */
	public async createSchuelerbetrieb(data : SchuelerBetriebsdaten, schema : string, schueler_id : number, betrieb_id : number) : Promise<SchuelerBetriebsdaten> {
		const path = "/db/{schema}/betriebe/schuelerbetrieb/new/schueler/{schueler_id : \\d+}/betrieb/{betrieb_id: \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{schueler_id\s*(:[^}]+)?}/g, schueler_id.toString())
			.replace(/{betrieb_id\s*(:[^}]+)?}/g, betrieb_id.toString());
		const body : string = SchuelerBetriebsdaten.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SchuelerBetriebsdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getClientConfig für den Zugriff auf die URL https://{hostname}/db/{schema}/client/config/{app}
	 *
	 * Liest die Konfigurationseinträge der angegebenen Client-Anwendung aus.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Key-Value-Paare der Konfigurationseinträge als Liste
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: BenutzerConfig
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {string} app - der Pfad-Parameter app
	 *
	 * @returns Die Key-Value-Paare der Konfigurationseinträge als Liste
	 */
	public async getClientConfig(schema : string, app : string) : Promise<BenutzerConfig> {
		const path = "/db/{schema}/client/config/{app}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{app\s*(:[^}]+)?}/g, app);
		const result : string = await super.getJSON(path);
		const text = result;
		return BenutzerConfig.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PUT-Methode setClientConfigGlobalKey für den Zugriff auf die URL https://{hostname}/db/{schema}/client/config/{app}/global/{key}
	 *
	 * Schreibt den Konfigurationseintrag der angegebenen Anwendung für den angebenen Schlüsselwert in die globale Konfiguration. Dabei wird geprüft, ob der angemeldete Benutzer administrative Rechte hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Der Konfigurationseintrag wurde erfolgreich geschrieben
	 *
	 * @param {string | null} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {string} app - der Pfad-Parameter app
	 * @param {string} key - der Pfad-Parameter key
	 */
	public async setClientConfigGlobalKey(data : string | null, schema : string, app : string, key : string) : Promise<void> {
		const path = "/db/{schema}/client/config/{app}/global/{key}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{app\s*(:[^}]+)?}/g, app)
			.replace(/{key\s*(:[^}]+)?}/g, key);
		const body : string = JSON.stringify(data);
		return super.putJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getClientConfigUserKey für den Zugriff auf die URL https://{hostname}/db/{schema}/client/config/{app}/user/{key}
	 *
	 * Liest den Schlüsselwert aus der Konfiguration für den Client aus. Ist sowohl ein globaler als auch eine benutzerspezifischer Konfigurationseintrag unter den Name vorhanden,so wird der benutzerspezifische Eintrag zurückgegeben. Die benutzerspezifische Konfiguration kann somit globale Einstellungen 'überschreiben'. Ist kein Wert vorhanden, so wird ein leerer String zurückgegeben.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Wert des Konfigurationseintrags
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: String
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {string} app - der Pfad-Parameter app
	 * @param {string} key - der Pfad-Parameter key
	 *
	 * @returns Der Wert des Konfigurationseintrags
	 */
	public async getClientConfigUserKey(schema : string, app : string, key : string) : Promise<string | null> {
		const path = "/db/{schema}/client/config/{app}/user/{key}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{app\s*(:[^}]+)?}/g, app)
			.replace(/{key\s*(:[^}]+)?}/g, key);
		const result : string = await super.getJSON(path);
		const text = result;
		return JSON.parse(text).toString();
	}


	/**
	 * Implementierung der PUT-Methode setClientConfigUserKey für den Zugriff auf die URL https://{hostname}/db/{schema}/client/config/{app}/user/{key}
	 *
	 * Schreibt den Konfigurationseintrag der angegebenen Anwendung für den angebenen Schlüsselwert in die benutzerspezifische Konfiguration.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Der Konfigurationseintrag wurde erfolgreich geschrieben
	 *
	 * @param {string | null} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {string} app - der Pfad-Parameter app
	 * @param {string} key - der Pfad-Parameter key
	 */
	public async setClientConfigUserKey(data : string | null, schema : string, app : string, key : string) : Promise<void> {
		const path = "/db/{schema}/client/config/{app}/user/{key}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{app\s*(:[^}]+)?}/g, app)
			.replace(/{key\s*(:[^}]+)?}/g, key);
		const body : string = JSON.stringify(data);
		return super.putJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getENMDaten für den Zugriff auf die URL https://{hostname}/db/{schema}/enm/alle
	 *
	 * Liest die Daten des Externen Notenmoduls (ENM) aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Notendaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Daten des Externen Notenmoduls (ENM)
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: ENMDaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten des ENM auszulesen.
	 *   Code 404: Es wurden nicht alle benötigten Daten für das Erstellen der ENM-Daten gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Daten des Externen Notenmoduls (ENM)
	 */
	public async getENMDaten(schema : string) : Promise<ENMDaten> {
		const path = "/db/{schema}/enm/alle"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const text = result;
		return ENMDaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getENMDatenGZip für den Zugriff auf die URL https://{hostname}/db/{schema}/enm/alle/gzip
	 *
	 * Liest die Daten des Externen Notenmoduls (ENM) aus der Datenbank und liefert diese GZip-komprimiert zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Notendaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die GZip-komprimierte ENM-JSON-Datei
	 *     - Mime-Type: application/octet-stream
	 *     - Rückgabe-Typ: ApiFile
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten des ENM auszulesen.
	 *   Code 404: Es wurden nicht alle benötigten Daten für das Erstellen der ENM-Daten gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die GZip-komprimierte ENM-JSON-Datei
	 */
	public async getENMDatenGZip(schema : string) : Promise<ApiFile> {
		const path = "/db/{schema}/enm/alle/gzip"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const data : ApiFile = await super.getOctetStream(path);
		return data;
	}


	/**
	 * Implementierung der GET-Methode getLehrerENMDaten für den Zugriff auf die URL https://{hostname}/db/{schema}/enm/lehrer/{id : \d+}
	 *
	 * Liest die Daten des Externen Notenmoduls (ENM) des Lehrers zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Notendaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Daten des Externen Notenmoduls (ENM) des Lehrers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: ENMDaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten des ENM auszulesen.
	 *   Code 404: Kein Lehrer-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Daten des Externen Notenmoduls (ENM) des Lehrers
	 */
	public async getLehrerENMDaten(schema : string, id : number) : Promise<ENMDaten> {
		const path = "/db/{schema}/enm/lehrer/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return ENMDaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getErzieher für den Zugriff auf die URL https://{hostname}/db/{schema}/erzieher/
	 *
	 * Erstellt eine Liste aller in der Datenbank vorhandenen Erzieher unter Angabe der ID, des Kürzels, des Vor- und Nachnamens, Erzieherart, Kontaktdaten, ob sie in der Anwendung sichtbar bzw. änderbar sein sollen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Erzieherdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Erzieher-Listen-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<ErzieherListeEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Erzieherdaten anzusehen.
	 *   Code 404: Keine Erzieher-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Erzieher-Listen-Einträgen
	 */
	public async getErzieher(schema : string) : Promise<List<ErzieherListeEintrag>> {
		const path = "/db/{schema}/erzieher/"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<ErzieherListeEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(ErzieherListeEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getErzieherStammdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/erzieher/{id : \d+}/stammdaten
	 *
	 * Liest die Stammdaten des Erziehers zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Erzieherdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Stammdaten des Erziehers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: ErzieherStammdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Erzieherdaten anzusehen.
	 *   Code 404: Kein Erzieher-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Stammdaten des Erziehers
	 */
	public async getErzieherStammdaten(schema : string, id : number) : Promise<ErzieherStammdaten> {
		const path = "/db/{schema}/erzieher/{id : \\d+}/stammdaten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return ErzieherStammdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchErzieherStammdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/erzieher/{id : \d+}/stammdaten
	 *
	 * Passt die Erzieher-Stammdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Erzieherdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Erzieher-Stammdaten integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Erzieherdaten zu ändern.
	 *   Code 404: Kein Erzieher-Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<ErzieherStammdaten>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchErzieherStammdaten(data : Partial<ErzieherStammdaten>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/erzieher/{id : \\d+}/stammdaten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = ErzieherStammdaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getErzieherArten für den Zugriff auf die URL https://{hostname}/db/{schema}/erzieher/arten
	 *
	 * Erstellt eine Liste aller in der Datenbank vorhandenen Erzieherarten unter Angabe der ID, der Beschreibung, Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Erzieherarten
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Erzieherart>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalogdaten anzusehen.
	 *   Code 404: Keine Erzieherart-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Erzieherarten
	 */
	public async getErzieherArten(schema : string) : Promise<List<Erzieherart>> {
		const path = "/db/{schema}/erzieher/arten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<Erzieherart>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(Erzieherart.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode exportSQLite für den Zugriff auf die URL https://{hostname}/db/{schema}/export/sqlite
	 *
	 * Exportiert das aktuelle Schema in eine neu erstellte SQLite-Datenbank. Der Aufruf erfordert administrative Rechte.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Export der SQLite-Datenbank
	 *     - Mime-Type: application/vnd.sqlite3
	 *     - Rückgabe-Typ: ApiFile
	 *   Code 403: Das Schema darf nicht exportiert werden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Export der SQLite-Datenbank
	 */
	public async exportSQLite(schema : string) : Promise<ApiFile> {
		const path = "/db/{schema}/export/sqlite"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const data : ApiFile = await super.getSQLite(path);
		return data;
	}


	/**
	 * Implementierung der GET-Methode getFaecher für den Zugriff auf die URL https://{hostname}/db/{schema}/faecher/
	 *
	 * Erstellt eine Liste aller in der Datenbank vorhanden Fächer unter Angabe der ID, des Kürzels, des verwendeten Statistik-Kürzels, der Bezeichnung des Faches, ob es ein Fach der Oberstufe ist, einer Sortierreihenfolge und ob sie in der Anwendung sichtbar bzw. änderbar sein sollen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Fächerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Fächer-Listen-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<FaecherListeEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Fächerdaten anzusehen.
	 *   Code 404: Keine Fächer-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Fächer-Listen-Einträgen
	 */
	public async getFaecher(schema : string) : Promise<List<FaecherListeEintrag>> {
		const path = "/db/{schema}/faecher/"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<FaecherListeEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(FaecherListeEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getFach für den Zugriff auf die URL https://{hostname}/db/{schema}/faecher/{id : \d+}
	 *
	 * Liest die Daten des Faches zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Fächerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Daten des Faches
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: FachDaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Fächerdaten anzusehen.
	 *   Code 404: Kein Fach-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Daten des Faches
	 */
	public async getFach(schema : string, id : number) : Promise<FachDaten> {
		const path = "/db/{schema}/faecher/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return FachDaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchFach für den Zugriff auf die URL https://{hostname}/db/{schema}/faecher/{id : \d+}
	 *
	 * Passt das Fach mit der angebenen ID an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Fächern besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 404: Kein Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<FachDaten>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchFach(data : Partial<FachDaten>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/faecher/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = FachDaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteFach für den Zugriff auf die URL https://{hostname}/db/{schema}/faecher/{id : \d+}
	 *
	 * Entfernt ein Fach. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen eines Faches hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Das Fach wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: FachDaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um ein Fach zu löschen.
	 *   Code 404: Kein Fach vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Das Fach wurde erfolgreich entfernt.
	 */
	public async deleteFach(schema : string, id : number) : Promise<FachDaten> {
		const path = "/db/{schema}/faecher/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return FachDaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getKatalogFachgruppenEintrag für den Zugriff auf die URL https://{hostname}/db/{schema}/faecher/allgemein/fachgruppe/{id : \d+}
	 *
	 * Gibt den Fachgruppen-Katalog-Eintrag für die angegebene ID zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Fachgruppen-Katalog-Eintrag für die angegebene ID.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: FachgruppenKatalogEintrag
	 *   Code 403: Der SVWS-Benutzer hat keine gültige Anmeldung.
	 *   Code 404: Kein Fachgruppen-Katalog-Eintrag für die angegebene ID gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Fachgruppen-Katalog-Eintrag für die angegebene ID.
	 */
	public async getKatalogFachgruppenEintrag(schema : string, id : number) : Promise<FachgruppenKatalogEintrag> {
		const path = "/db/{schema}/faecher/allgemein/fachgruppe/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return FachgruppenKatalogEintrag.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getKatalogFachgruppen für den Zugriff auf die URL https://{hostname}/db/{schema}/faecher/allgemein/fachgruppen
	 *
	 * Gibt den Katalog aller Fachgruppen aller Schulformen zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Katalog aller Fachgruppen aller Schulformen.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<FachgruppenKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine gültige Anmeldung.
	 *   Code 404: Keine Fachgruppen gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Katalog aller Fachgruppen aller Schulformen.
	 */
	public async getKatalogFachgruppen(schema : string) : Promise<List<FachgruppenKatalogEintrag>> {
		const path = "/db/{schema}/faecher/allgemein/fachgruppen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<FachgruppenKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(FachgruppenKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogFaecher für den Zugriff auf die URL https://{hostname}/db/{schema}/faecher/allgemein/faecher
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden zulässigen Fächer. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Fächer-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<FachKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Fach-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Fächer-Katalog-Einträgen
	 */
	public async getKatalogFaecher(schema : string) : Promise<List<FachKatalogEintrag>> {
		const path = "/db/{schema}/faecher/allgemein/faecher"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<FachKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(FachKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogBilingualeSprachen für den Zugriff auf die URL https://{hostname}/db/{schema}/faecher/allgemein/sprachen/bilingual
	 *
	 * Gibt den Katalog der bilingualen Sprachen für die Schulform dieser Schule zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Katalog der bilingualen Sprachen für die Schulform dieser Schule.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<BilingualeSpracheKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine gültige Anmeldung.
	 *   Code 404: Keine bilingualen Sprachen für die Schulform dieser Schule gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Katalog der bilingualen Sprachen für die Schulform dieser Schule.
	 */
	public async getKatalogBilingualeSprachen(schema : string) : Promise<List<BilingualeSpracheKatalogEintrag>> {
		const path = "/db/{schema}/faecher/allgemein/sprachen/bilingual"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<BilingualeSpracheKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(BilingualeSpracheKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogBilingualeSprachenEintrag für den Zugriff auf die URL https://{hostname}/db/{schema}/faecher/allgemein/sprachen/bilingual/{id : \d+}
	 *
	 * Gibt den Katalog-Eintrag einer bilingualen Sprache für die angegebene ID zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Katalog-Eintrag einer bilingualen Sprache für die angegebene ID.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: BilingualeSpracheKatalogEintrag
	 *   Code 403: Der SVWS-Benutzer hat keine gültige Anmeldung.
	 *   Code 404: Kein Katalog-Eintrag einer bilingualen Sprache für die angegebene ID gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Katalog-Eintrag einer bilingualen Sprache für die angegebene ID.
	 */
	public async getKatalogBilingualeSprachenEintrag(schema : string, id : number) : Promise<BilingualeSpracheKatalogEintrag> {
		const path = "/db/{schema}/faecher/allgemein/sprachen/bilingual/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return BilingualeSpracheKatalogEintrag.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getKatalogBilingualeSprachenAlle für den Zugriff auf die URL https://{hostname}/db/{schema}/faecher/allgemein/sprachen/bilingual/alle
	 *
	 * Gibt den Katalog aller bilingualen Sprachen aller Schulformen zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Katalog aller bilingualen Sprachen aller Schulformen.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<BilingualeSpracheKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine gültige Anmeldung.
	 *   Code 404: Keine Fachgruppen gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Katalog aller bilingualen Sprachen aller Schulformen.
	 */
	public async getKatalogBilingualeSprachenAlle(schema : string) : Promise<List<BilingualeSpracheKatalogEintrag>> {
		const path = "/db/{schema}/faecher/allgemein/sprachen/bilingual/alle"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<BilingualeSpracheKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(BilingualeSpracheKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogSprachpruefungsniveaus für den Zugriff auf die URL https://{hostname}/db/{schema}/faecher/allgemein/sprachen/pruefungsniveaus
	 *
	 * Gibt den Katalog der Sprachprüfungsniveaus zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Katalog der Sprachprüfungsniveaus.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SprachpruefungsniveauKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine gültige Anmeldung.
	 *   Code 404: Keine Fachgruppen gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Katalog der Sprachprüfungsniveaus.
	 */
	public async getKatalogSprachpruefungsniveaus(schema : string) : Promise<List<SprachpruefungsniveauKatalogEintrag>> {
		const path = "/db/{schema}/faecher/allgemein/sprachen/pruefungsniveaus"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SprachpruefungsniveauKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SprachpruefungsniveauKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogSprachreferenzniveaus für den Zugriff auf die URL https://{hostname}/db/{schema}/faecher/allgemein/sprachen/referenzniveaus
	 *
	 * Gibt den Katalog der Sprachreferenzniveaus zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Katalog der Sprachreferenzniveaus.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SprachreferenzniveauKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine gültige Anmeldung.
	 *   Code 404: Keine Fachgruppen gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Katalog der Sprachreferenzniveaus.
	 */
	public async getKatalogSprachreferenzniveaus(schema : string) : Promise<List<SprachreferenzniveauKatalogEintrag>> {
		const path = "/db/{schema}/faecher/allgemein/sprachen/referenzniveaus"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SprachreferenzniveauKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SprachreferenzniveauKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode addFach für den Zugriff auf die URL https://{hostname}/db/{schema}/faecher/create
	 *
	 * Erstellt ein neues Fach und gibt das zugehörige Objekt zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Faches besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Das Fach wurde erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: FachDaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um ein Fach anzulegen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<FachDaten>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Das Fach wurde erfolgreich hinzugefügt.
	 */
	public async addFach(data : Partial<FachDaten>, schema : string, id : number) : Promise<FachDaten> {
		const path = "/db/{schema}/faecher/create"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = FachDaten.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return FachDaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getFachgruppen für den Zugriff auf die URL https://{hostname}/db/{schema}/faecher/fachgruppen
	 *
	 * Gibt den Katalog der Fachgruppen für die Schulform dieser Schule zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Katalog der Fachgruppen für die Schulform dieser Schule.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<FachgruppenKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine gültige Anmeldung.
	 *   Code 404: Keine Fachgruppen für die Schulform dieser Schule gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Katalog der Fachgruppen für die Schulform dieser Schule.
	 */
	public async getFachgruppen(schema : string) : Promise<List<FachgruppenKatalogEintrag>> {
		const path = "/db/{schema}/faecher/fachgruppen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<FachgruppenKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(FachgruppenKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getGesamtschuleSchuelerPrognoseLeistungsdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/gesamtschule/schueler/{id : \d+}/prognose_leistungsdaten
	 *
	 * Liest die Leistungsdaten des aktuellen Lernabschnittes in Bezug auf die Prognose- bzw. Abschlussberechnung in der Sek I der Gesamtschule des Schülers mit der angegebene ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Leistungsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Leistungsdaten des Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GEAbschlussFaecher
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Leistungsdaten anzusehen.
	 *   Code 404: Kein Schüler-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Leistungsdaten des Schülers
	 */
	public async getGesamtschuleSchuelerPrognoseLeistungsdaten(schema : string, id : number) : Promise<GEAbschlussFaecher> {
		const path = "/db/{schema}/gesamtschule/schueler/{id : \\d+}/prognose_leistungsdaten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GEAbschlussFaecher.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getGesamtschuleSchuelerPrognosLeistungsdatenFuerAbschnitt für den Zugriff auf die URL https://{hostname}/db/{schema}/gesamtschule/schueler/{id : \d+}/prognose_leistungsdaten/abschnitt/{abschnittID : \d+}
	 *
	 * Liest die Leistungsdaten des angegebenen Lernabschnittes in Bezug auf die Prognose- bzw. Abschlussberechnung in der Sek I der Gesamtschule des Schülers mit der angegebene ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Leistungsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Leistungsdaten des Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GEAbschlussFaecher
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Leistungsdaten anzusehen.
	 *   Code 404: Kein Schüler-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 * @param {number} abschnittID - der Pfad-Parameter abschnittID
	 *
	 * @returns Die Leistungsdaten des Schülers
	 */
	public async getGesamtschuleSchuelerPrognosLeistungsdatenFuerAbschnitt(schema : string, id : number, abschnittID : number) : Promise<GEAbschlussFaecher> {
		const path = "/db/{schema}/gesamtschule/schueler/{id : \\d+}/prognose_leistungsdaten/abschnitt/{abschnittID : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString())
			.replace(/{abschnittID\s*(:[^}]+)?}/g, abschnittID.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GEAbschlussFaecher.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode getGostAbiturBelegpruefungEF1 für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abitur/belegpruefung/EF1
	 *
	 * Prüft anhand der übergeben Abiturdaten, ob die Belegung in den Abiturdaten korrekt ist oder nicht. Es werden ggf. auch Belegungsfehler und Hinweise zur Belegung zurückgegeben.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Das Ergebnis der Belegprüfung, ggf. mit Belegungsfehlern
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBelegpruefungErgebnis
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Belegprüfung durchzuführen.
	 *   Code 404: Es wurden keine Fächerdaten zur gymnasialen Oberstufe gefunden
	 *
	 * @param {Abiturdaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Das Ergebnis der Belegprüfung, ggf. mit Belegungsfehlern
	 */
	public async getGostAbiturBelegpruefungEF1(data : Abiturdaten, schema : string) : Promise<GostBelegpruefungErgebnis> {
		const path = "/db/{schema}/gost/abitur/belegpruefung/EF1"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = Abiturdaten.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return GostBelegpruefungErgebnis.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode getGostAbiturBelegpruefungGesamt für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abitur/belegpruefung/gesamt
	 *
	 * Prüft anhand der übergeben Abiturdaten, ob die Belegung in den Abiturdaten korrekt ist oder nicht. Es werden ggf. auch Belegungsfehler und Hinweise zur Belegung zurückgegeben.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Das Ergebnis der Belegprüfung, ggf. mit Belegungsfehlern
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBelegpruefungErgebnis
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Belegprüfung durchzuführen.
	 *   Code 404: Es wurden keine Fächerdaten zur gymnasialen Oberstufe gefunden
	 *
	 * @param {Abiturdaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Das Ergebnis der Belegprüfung, ggf. mit Belegungsfehlern
	 */
	public async getGostAbiturBelegpruefungGesamt(data : Abiturdaten, schema : string) : Promise<GostBelegpruefungErgebnis> {
		const path = "/db/{schema}/gost/abitur/belegpruefung/gesamt"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = Abiturdaten.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return GostBelegpruefungErgebnis.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getGostAbiturjahrgaenge für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgaenge
	 *
	 * Liefert eine Liste aller Abiturjahrgänge, welche in der Datenbank für die Laufbahnplanung angelegt sind.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen von Kataloginformationen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Liste der Abiturjahrgänge.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<GostJahrgang>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Liste der Abiturjahrgänge auszulesen.
	 *   Code 404: Kein Abiturjahrgang gefunden oder keine gymnasiale Oberstufe bei der Schulform vorhanden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Liste der Abiturjahrgänge.
	 */
	public async getGostAbiturjahrgaenge(schema : string) : Promise<List<GostJahrgang>> {
		const path = "/db/{schema}/gost/abiturjahrgaenge"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<GostJahrgang>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(GostJahrgang.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getGostAbiturjahrgang für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\d+}
	 *
	 * Liest die Grunddaten des Jahrgangs der gymnasialen Oberstufe zu dem Jahr, in welchem der Jahrgang Abitur machen wird, aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Jahrgangsinformationen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Grunddaten des Jahrgangs der gymnasialen Oberstufe
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostJahrgangsdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Grunddaten der Gymnasialen Oberstufe anzusehen.
	 *   Code 404: Kein Eintrag für einen Jahrgang der gymnasialen Oberstufe mit dem angegebenen Abiturjahr gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 *
	 * @returns Die Grunddaten des Jahrgangs der gymnasialen Oberstufe
	 */
	public async getGostAbiturjahrgang(schema : string, abiturjahr : number) : Promise<GostJahrgangsdaten> {
		const path = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostJahrgangsdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchGostAbiturjahrgang für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\d+}
	 *
	 * Passt die Daten des Jahrganges der gymnasialen Oberstufe zu dem Jahr an, in welchem der Jahrgang Abitur machen wird. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anpassen der Jahrgangsinformationen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Jahrgangsdaten integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Jahrgangsdaten zu ändern.
	 *   Code 404: Kein Abiturjahrgangs-Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<GostJahrgangsdaten>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 */
	public async patchGostAbiturjahrgang(data : Partial<GostJahrgangsdaten>, schema : string, abiturjahr : number) : Promise<void> {
		const path = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString());
		const body : string = GostJahrgangsdaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getGostAbiturjahrgangBelegpruefungsergebnisseEF1 für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\d+}/belegpruefung/EF1
	 *
	 * Gibt die (Fehler-)Rückmeldungen der EF1-Belegprüfung zu den Schüler-Laufbahnen eines Abitur-Jahrganges der gymnasialen Oberstufe zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung dazu hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste mit den Schülern und den zugehörigen Belegprüfungsfehlern.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<GostBelegpruefungsErgebnisse>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Rückmeldungen abzufragen.
	 *   Code 404: Keine und unvollständige Daten für die Belegprüfung gefunden oder keine gymnasiale Oberstufe bei der Schulform vorhanden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 *
	 * @returns Eine Liste mit den Schülern und den zugehörigen Belegprüfungsfehlern.
	 */
	public async getGostAbiturjahrgangBelegpruefungsergebnisseEF1(schema : string, abiturjahr : number) : Promise<List<GostBelegpruefungsErgebnisse>> {
		const path = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\\d+}/belegpruefung/EF1"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<GostBelegpruefungsErgebnisse>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(GostBelegpruefungsErgebnisse.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getGostAbiturjahrgangBelegpruefungsergebnisseGesamt für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\d+}/belegpruefung/gesamt
	 *
	 * Gibt die (Fehler-)Rückmeldungen der Gesamt-Belegprüfung zu den Schüler-Laufbahnen eines Abitur-Jahrganges der gymnasialen Oberstufe zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung dazu hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste mit den Schülern und den zugehörigen Belegprüfungsfehlern.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<GostBelegpruefungsErgebnisse>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Rückmeldungen abzufragen.
	 *   Code 404: Keine und unvollständige Daten für die Belegprüfung gefunden oder keine gymnasiale Oberstufe bei der Schulform vorhanden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 *
	 * @returns Eine Liste mit den Schülern und den zugehörigen Belegprüfungsfehlern.
	 */
	public async getGostAbiturjahrgangBelegpruefungsergebnisseGesamt(schema : string, abiturjahr : number) : Promise<List<GostBelegpruefungsErgebnisse>> {
		const path = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\\d+}/belegpruefung/gesamt"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<GostBelegpruefungsErgebnisse>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(GostBelegpruefungsErgebnisse.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode addGostAbiturjahrgangBeratungslehrer für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\d+}/beratungslehrer/add
	 *
	 * Fügt einen Lehrer als Beratungslehrer zu einem Abiturjahrgang der Gymnasialen Oberstufe hinzu.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Hinzufügen eines Beratungslehrers hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der hinzugefügte Beratungslehrer
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBeratungslehrer
	 *   Code 400: Der Lehrer ist bereits als Beratungslehrer eingetragen.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Beratungslehrer hinzuzufügen.
	 *   Code 404: Der Abiturjahrgang oder der Lehrer ist nicht vorhanden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {number | null} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 *
	 * @returns Der hinzugefügte Beratungslehrer
	 */
	public async addGostAbiturjahrgangBeratungslehrer(data : number | null, schema : string, abiturjahr : number) : Promise<GostBeratungslehrer> {
		const path = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\\d+}/beratungslehrer/add"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString());
		const body : string = JSON.stringify(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return GostBeratungslehrer.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode removeGostAbiturjahrgangBeratungslehrer für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\d+}/beratungslehrer/remove
	 *
	 * Entfernt einen Lehrer als Beratungslehrer aus einem Abiturjahrgang der Gymnasialen Oberstufe.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen eines Beratungslehrers hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der entfernte Beratungslehrer
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBeratungslehrer
	 *   Code 400: Der Lehrer ist nicht als Beratungslehrer eingetragen.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Beratungslehrer zu entfernen.
	 *   Code 404: Der Abiturjahrgang oder der Lehrer ist nicht vorhanden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {number | null} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 *
	 * @returns Der entfernte Beratungslehrer
	 */
	public async removeGostAbiturjahrgangBeratungslehrer(data : number | null, schema : string, abiturjahr : number) : Promise<GostBeratungslehrer> {
		const path = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\\d+}/beratungslehrer/remove"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString());
		const body : string = JSON.stringify(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return GostBeratungslehrer.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getGostAbiturjahrgangFach für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\d+}/fach/{id : \d+}
	 *
	 * Liefert die Informationen bezüglich eines Abiturjahrgangs der gymnasialen Oberstufe zu dem Fach mit der angegebenen ID. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Fachinformationen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Daten des Faches bezüglich eines Abiturjahrgangs der gymnasialen Oberstufe
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostFach
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Fachinformationen der Gymnasialen Oberstufe anzusehen.
	 *   Code 404: Kein Eintrag für das Fach der gymnasialen Oberstufe mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Daten des Faches bezüglich eines Abiturjahrgangs der gymnasialen Oberstufe
	 */
	public async getGostAbiturjahrgangFach(schema : string, abiturjahr : number, id : number) : Promise<GostFach> {
		const path = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\\d+}/fach/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString())
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostFach.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchGostAbiturjahrgangFach für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\d+}/fach/{id : \d+}
	 *
	 * Passt die Daten eines Faches in Bezug auf den Abiturjahrgang der Gymnasiale Oberstufe an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anpassen der Fachinformationen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Fachinformationen integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Fachdaten zu ändern.
	 *   Code 404: Kein Fach-Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<GostFach>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchGostAbiturjahrgangFach(data : Partial<GostFach>, schema : string, abiturjahr : number, id : number) : Promise<void> {
		const path = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\\d+}/fach/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString())
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = GostFach.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getGostAbiturjahrgangFachkombinationen für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\d+}/fachkombinationen
	 *
	 * Erstellt eine Liste mit den Informationen zu den Fachkombinationen für die Laufbahnplanung des Abitur-Jahrganges der gymnasialen Oberstufe. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Fachkombinationen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Liste der Fachkombinationen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<GostJahrgangFachkombination>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Fachkombinationen anzusehen.
	 *   Code 404: Keine Fachkombinationen gefunden oder keine gymnasiale Oberstufe bei der Schulform vorhanden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 *
	 * @returns Die Liste der Fachkombinationen
	 */
	public async getGostAbiturjahrgangFachkombinationen(schema : string, abiturjahr : number) : Promise<List<GostJahrgangFachkombination>> {
		const path = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\\d+}/fachkombinationen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<GostJahrgangFachkombination>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(GostJahrgangFachkombination.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode addGostAbiturjahrgangFachkombination für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\d+}/fachkombinationen/add/{typ : \d+}
	 *
	 * Fügt eine Regel zu einer Fachkombination der Gymnasialen Oberstufe bei dem angegebenen Abiturjahrgang hinzu.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer solchen Fachkombination hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Regel zur Fachkombination bezüglich der gymnasialen Oberstufe wurde erstellt
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostJahrgangFachkombination
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Regel für eine Fachkombination hinzuzufügen.
	 *   Code 404: Abiturjahr nicht vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 * @param {number} typ - der Pfad-Parameter typ
	 *
	 * @returns Die Regel zur Fachkombination bezüglich der gymnasialen Oberstufe wurde erstellt
	 */
	public async addGostAbiturjahrgangFachkombination(schema : string, abiturjahr : number, typ : number) : Promise<GostJahrgangFachkombination> {
		const path = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\\d+}/fachkombinationen/add/{typ : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString())
			.replace(/{typ\s*(:[^}]+)?}/g, typ.toString());
		const result : string = await super.postJSON(path, null);
		const text = result;
		return GostJahrgangFachkombination.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getGostAbiturjahrgangFachwahl für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\d+}/fachwahl/{fachid : \d+}
	 *
	 * Liest für die gymnasiale Oberstufe die Fachwahlen zu einem Fach von dem angegebenen Abiturjahrgang aus. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Fachwahlen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Fachwahlen der gymnasialen Oberstufe für das angegebene Fach und den angegebenen Abiturjahrgang
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostSchuelerFachwahl
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Fachwahlen der Gymnasialen Oberstufe eines Abiturjahrgang auszulesen.
	 *   Code 404: Kein Eintrag für einen Abiturjahrgang mit Laufbahnplanungsdaten der gymnasialen Oberstufe für die angegebene ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 * @param {number} fachid - der Pfad-Parameter fachid
	 *
	 * @returns Die Fachwahlen der gymnasialen Oberstufe für das angegebene Fach und den angegebenen Abiturjahrgang
	 */
	public async getGostAbiturjahrgangFachwahl(schema : string, abiturjahr : number, fachid : number) : Promise<GostSchuelerFachwahl> {
		const path = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\\d+}/fachwahl/{fachid : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString())
			.replace(/{fachid\s*(:[^}]+)?}/g, fachid.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostSchuelerFachwahl.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchGostAbiturjahrgangFachwahl für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\d+}/fachwahl/{fachid : \d+}
	 *
	 * Passt die Fachwahl der Vorlage des angegebenen Abiturjahrgangs in Bezug auf ein Fach der Gymnasiale Oberstufe an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anpassen der Fachwahlen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 203: Der Patch wurde erfolgreich in die Fachwahlen integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Fachwahlen zu ändern.
	 *   Code 404: Kein Eintrag für einen Abiturjahrgang mit Laufbahnplanungsdaten der gymnasialen Oberstufe oder für das Fach gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<GostSchuelerFachwahl>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 * @param {number} fachid - der Pfad-Parameter fachid
	 */
	public async patchGostAbiturjahrgangFachwahl(data : Partial<GostSchuelerFachwahl>, schema : string, abiturjahr : number, fachid : number) : Promise<void> {
		const path = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\\d+}/fachwahl/{fachid : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString())
			.replace(/{fachid\s*(:[^}]+)?}/g, fachid.toString());
		const body : string = GostSchuelerFachwahl.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der POST-Methode resetGostAbiturjahrgangFachwahlen für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\d+}/fachwahl/reset
	 *
	 * Setzt die Fachwahlen in der Vorlage für eine Laufbahnplanung in dem angegebenen Abiturjahrgang zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Zurücksetzen der Fachwahlen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 203: Die Fachwahlen wurden erfolgreich zurückgesetzt.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Fachwahlen zurückzusetzen.
	 *   Code 404: Der Abiturjahrgang wurde nicht gefunden.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 */
	public async resetGostAbiturjahrgangFachwahlen(schema : string, abiturjahr : number) : Promise<void> {
		const path = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\\d+}/fachwahl/reset"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString());
		await super.postJSON(path, null);
		return;
	}


	/**
	 * Implementierung der POST-Methode resetGostAbiturjahrgangSchuelerFachwahlen für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\d+}/fachwahl/schueler/reset
	 *
	 * Setzt die Fachwahlen aller Schüler des angegebenen Abiturjahrgang zurück. Die Fachwahlen werden von allen Halbjahren ohn Leistungsdaten entfernt. Sollten danach keine Fachwahlen bei einem Schüler vorhanden sein, so wird die Laufbahnplanung mit der Fachwahlen-Vorlage des Abiturjahrgangs initialisiert.Außerdem wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Zurücksetzen der Fachwahlen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 203: Die Fachwahlen wurden erfolgreich zurückgesetzt.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Fachwahlen zurückzusetzen.
	 *   Code 404: Der Abiturjahrgang wurde nicht gefunden.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 */
	public async resetGostAbiturjahrgangSchuelerFachwahlen(schema : string, abiturjahr : number) : Promise<void> {
		const path = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\\d+}/fachwahl/schueler/reset"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString());
		await super.postJSON(path, null);
		return;
	}


	/**
	 * Implementierung der GET-Methode getGostAbiturjahrgangFachwahlen für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\d+}/fachwahlen
	 *
	 * Erstellt eine Liste aller in der Datenbank für den angegebenen Abitur-Jahrgang vorhanden Fachwahlen der gymnasialen Oberstufe. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Fächerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste der Fachwahlen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostJahrgangFachwahlen
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Fachwahlen anzusehen.
	 *   Code 404: Keine Fachwahlen gefunden oder keine gymnasiale Oberstufe bei der Schulform vorhanden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 *
	 * @returns Eine Liste der Fachwahlen
	 */
	public async getGostAbiturjahrgangFachwahlen(schema : string, abiturjahr : number) : Promise<GostJahrgangFachwahlen> {
		const path = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\\d+}/fachwahlen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostJahrgangFachwahlen.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getGostAbiturjahrgangFachwahlstatistik für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\d+}/fachwahlstatistik
	 *
	 * Erstellt eine Liste aller in der Datenbank für den angegebenen Abitur-Jahrgang vorhanden Fachwahlen der gymnasialen Oberstufe. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Fächerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Fachwahlstatistik
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<GostStatistikFachwahl>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Fachwahlen anzusehen.
	 *   Code 404: Keine Fachwahlen gefunden oder keine gymnasiale Oberstufe bei der Schulform vorhanden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 *
	 * @returns Eine Fachwahlstatistik
	 */
	public async getGostAbiturjahrgangFachwahlstatistik(schema : string, abiturjahr : number) : Promise<List<GostStatistikFachwahl>> {
		const path = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\\d+}/fachwahlstatistik"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<GostStatistikFachwahl>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(GostStatistikFachwahl.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getGostAbiturjahrgangFaecher für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\d+}/faecher
	 *
	 * Erstellt eine Liste aller in der Datenbank vorhanden Fächer der gymnasialen Oberstufe, welche für den angegebenen Abitur-Jahrgang festgelegt wurden.. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Fächerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Fächer-Listen-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<GostFach>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Fächerdaten anzusehen.
	 *   Code 404: Keine Fächer-Einträge gefunden oder keine gymnasiale Oberstufe bei der Schulform vorhanden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 *
	 * @returns Eine Liste von Fächer-Listen-Einträgen
	 */
	public async getGostAbiturjahrgangFaecher(schema : string, abiturjahr : number) : Promise<List<GostFach>> {
		const path = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\\d+}/faecher"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<GostFach>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(GostFach.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getGostAbiturjahrgangHalbjahrFachwahlen für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\d+}/halbjahr/{halbjahr : \d+}/fachwahlen
	 *
	 * Erstellt eine Liste aller in der Datenbank für den angegebenen Abitur-Jahrgang vorhanden Fachwahlen der gymnasialen Oberstufe. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Fächerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste der Fachwahlen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostJahrgangFachwahlenHalbjahr
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Fachwahlen anzusehen.
	 *   Code 404: Keine Fachwahlen gefunden oder keine gymnasiale Oberstufe bei der Schulform vorhanden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 * @param {number} halbjahr - der Pfad-Parameter halbjahr
	 *
	 * @returns Eine Liste der Fachwahlen
	 */
	public async getGostAbiturjahrgangHalbjahrFachwahlen(schema : string, abiturjahr : number, halbjahr : number) : Promise<GostJahrgangFachwahlenHalbjahr> {
		const path = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : \\d+}/fachwahlen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString())
			.replace(/{halbjahr\s*(:[^}]+)?}/g, halbjahr.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostJahrgangFachwahlenHalbjahr.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getGostAbiturjahrgangLaufbahnplanung für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\d+}/laufbahnplanung
	 *
	 * Liest die Laufbahnplanungsdaten für die gymnasiale Oberstufe zu dem Abiturjahrgang aus der Datenbank aus und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Laufbahnplanungsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Laufbahnplanungsdaten der gymnasialen Oberstufe des angegebenen Abiturjahrgangs
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Abiturdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Laufbahnplanungsdaten der Gymnasialen Oberstufe eines Abiturjahrgangs auszulesen.
	 *   Code 404: Kein Eintrag für den angegebenen Abiturjahrgangs mit Laufbahnplanungsdaten der gymnasialen Oberstufe gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 *
	 * @returns Die Laufbahnplanungsdaten der gymnasialen Oberstufe des angegebenen Abiturjahrgangs
	 */
	public async getGostAbiturjahrgangLaufbahnplanung(schema : string, abiturjahr : number) : Promise<Abiturdaten> {
		const path = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\\d+}/laufbahnplanung"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return Abiturdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getGostAbiturjahrgangSchueler für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\d+}/schueler
	 *
	 * Erstellt eine Liste aller Schüler, welche zu dem angegebenen Abitur-Jahrgang gehören (ohne Informationen zu Kursen). Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Schülerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Schüler-Listen-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SchuelerListeEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzusehen.
	 *   Code 404: Keine Schüler gefunden oder keine gymnasiale Oberstufe bei der Schulform vorhanden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 *
	 * @returns Eine Liste von Schüler-Listen-Einträgen
	 */
	public async getGostAbiturjahrgangSchueler(schema : string, abiturjahr : number) : Promise<List<SchuelerListeEintrag>> {
		const path = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\\d+}/schueler"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchuelerListeEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchuelerListeEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getGostAbiturjahrgangBlockungsliste für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : \d+}/{halbjahr : \d+}/blockungen
	 *
	 * Erstellt eine Liste aller in der Datenbank vorhanden Blockungen der gymnasialen Oberstufe, welche für den angegebenen Abitur-Jahrgang und das angegebene Halbjahr festgelegt wurden.. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Blockungsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Blockungs-Listen-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<GostBlockungListeneintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Blockungsdaten anzusehen.
	 *   Code 404: Keine Blockungs-Einträge gefunden oder keine gymnasiale Oberstufe bei der Schulform vorhanden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 * @param {number} halbjahr - der Pfad-Parameter halbjahr
	 *
	 * @returns Eine Liste von Blockungs-Listen-Einträgen
	 */
	public async getGostAbiturjahrgangBlockungsliste(schema : string, abiturjahr : number, halbjahr : number) : Promise<List<GostBlockungListeneintrag>> {
		const path = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : \\d+}/{halbjahr : \\d+}/blockungen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString())
			.replace(/{halbjahr\s*(:[^}]+)?}/g, halbjahr.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<GostBlockungListeneintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(GostBlockungListeneintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode createGostAbiturjahrgangBlockung für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : \d+}/{halbjahr : \d+}/blockungen/new
	 *
	 * Erstellt eine neue Blockung und gibt die ID dieser Blockung zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen der Blockungen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Blockung wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBlockungsdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Blockung anzulegen.
	 *   Code 404: Keine Fachwahlinformationen zum Anlegen einer Blockung gefunden
	 *   Code 409: Das Abiturjahr oder das Halbjahr ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 * @param {number} halbjahr - der Pfad-Parameter halbjahr
	 *
	 * @returns Die Blockung wurde erfolgreich angelegt.
	 */
	public async createGostAbiturjahrgangBlockung(schema : string, abiturjahr : number, halbjahr : number) : Promise<GostBlockungsdaten> {
		const path = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : \\d+}/{halbjahr : \\d+}/blockungen/new"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString())
			.replace(/{halbjahr\s*(:[^}]+)?}/g, halbjahr.toString());
		const result : string = await super.postJSON(path, null);
		const text = result;
		return GostBlockungsdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode createGostAbiturjahrgang für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/new/{jahrgangid}
	 *
	 * Erstellt einen neuen Abiturjahrgang und gibt das Abiturjahr zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eine Abiturjahrgangs besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Abiturjahrgang wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Integer
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Abiturjahrgang anzulegen.
	 *   Code 404: Keine Daten beim angegebenen Jahrgang gefunden, um einen Abiturjahrgang anzulegen
	 *   Code 409: Der Abiturjahrgang existiert bereits
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} jahrgangid - der Pfad-Parameter jahrgangid
	 *
	 * @returns Der Abiturjahrgang wurde erfolgreich angelegt.
	 */
	public async createGostAbiturjahrgang(schema : string, jahrgangid : number) : Promise<number | null> {
		const path = "/db/{schema}/gost/abiturjahrgang/new/{jahrgangid}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{jahrgangid\s*(:[^}]+)?}/g, jahrgangid.toString());
		const result : string = await super.postJSON(path, null);
		const text = result;
		return parseInt(JSON.parse(text));
	}


	/**
	 * Implementierung der GET-Methode restauriereGostBlockung für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/{abiturjahr : \d+}/{halbjahr : \d+}/restore
	 *
	 * Restauriert die Blockung aus den Leistungsdaten. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Restaurieren einer Blockung besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Blockungsdaten der gymnasialen Oberstufe der restaurierten Blockung
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBlockungsdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Blockungsdaten der Gymnasialen Oberstufe zu restaurieren.
	 *   Code 404: Keine Daten für das Abiturjahr und das Halbjahr gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 * @param {number} halbjahr - der Pfad-Parameter halbjahr
	 *
	 * @returns Die Blockungsdaten der gymnasialen Oberstufe der restaurierten Blockung
	 */
	public async restauriereGostBlockung(schema : string, abiturjahr : number, halbjahr : number) : Promise<GostBlockungsdaten> {
		const path = "/db/{schema}/gost/blockungen/{abiturjahr : \\d+}/{halbjahr : \\d+}/restore"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString())
			.replace(/{halbjahr\s*(:[^}]+)?}/g, halbjahr.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostBlockungsdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der DELETE-Methode deleteGostBlockung für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/{blockungsid : \d+}
	 *
	 * Entfernt die angegebene Blockung der gymnasialen Oberstufe. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen der Blockungsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Blockungsdaten der gymnasialen Oberstfue für die angegebene ID wurden erfolgreich gelöscht.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Long
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Blockungsdaten der Gymnasialen Oberstufe zu löschen.
	 *   Code 404: Keine Blockung mit der angegebenen ID gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} blockungsid - der Pfad-Parameter blockungsid
	 *
	 * @returns Die Blockungsdaten der gymnasialen Oberstfue für die angegebene ID wurden erfolgreich gelöscht.
	 */
	public async deleteGostBlockung(schema : string, blockungsid : number) : Promise<number | null> {
		const path = "/db/{schema}/gost/blockungen/{blockungsid : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{blockungsid\s*(:[^}]+)?}/g, blockungsid.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return parseFloat(JSON.parse(text));
	}


	/**
	 * Implementierung der GET-Methode getGostBlockung für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/{blockungsid : \d+}
	 *
	 * Liest für die angegebene Blockung der gymnasialen Oberstufe die grundlegenden Daten aus. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Blockungsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Blockungsdaten der gymnasialen Oberstfue für die angegebene ID
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBlockungsdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Blockungsdaten der Gymnasialen Oberstufe auszulesen.
	 *   Code 404: Keine Blockung mit der angebenen ID gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} blockungsid - der Pfad-Parameter blockungsid
	 *
	 * @returns Die Blockungsdaten der gymnasialen Oberstfue für die angegebene ID
	 */
	public async getGostBlockung(schema : string, blockungsid : number) : Promise<GostBlockungsdaten> {
		const path = "/db/{schema}/gost/blockungen/{blockungsid : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{blockungsid\s*(:[^}]+)?}/g, blockungsid.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostBlockungsdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchGostBlockung für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/{blockungsid : \d+}
	 *
	 * Passt die Blockungsdaten der Gymnasiale Oberstufe mit der angegebenen ID an.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anpassen der Fachwahlen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Blockungsdaten integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Blockungsdaten zu ändern.
	 *   Code 404: Kein Blockungsdaten-Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<GostBlockungsdaten>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} blockungsid - der Pfad-Parameter blockungsid
	 */
	public async patchGostBlockung(data : Partial<GostBlockungsdaten>, schema : string, blockungsid : number) : Promise<void> {
		const path = "/db/{schema}/gost/blockungen/{blockungsid : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{blockungsid\s*(:[^}]+)?}/g, blockungsid.toString());
		const body : string = GostBlockungsdaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der POST-Methode addGostBlockungRegel für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/{blockungsid : \d+}/addregel/{regeltyp : \d+}
	 *
	 * Fügt eine Regel zu einer Blockung der Gymnasialen Oberstufe hinzu.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Regel hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Regel der Blockung der gymnasialen Oberstufe
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBlockungRegel
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Regel hinzuzufügen.
	 *   Code 404: Keine Blockung vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} blockungsid - der Pfad-Parameter blockungsid
	 * @param {number} regeltyp - der Pfad-Parameter regeltyp
	 *
	 * @returns Die Regel der Blockung der gymnasialen Oberstufe
	 */
	public async addGostBlockungRegel(data : List<number>, schema : string, blockungsid : number, regeltyp : number) : Promise<GostBlockungRegel> {
		const path = "/db/{schema}/gost/blockungen/{blockungsid : \\d+}/addregel/{regeltyp : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{blockungsid\s*(:[^}]+)?}/g, blockungsid.toString())
			.replace(/{regeltyp\s*(:[^}]+)?}/g, regeltyp.toString());
		const body : string = "[" + data.toArray().map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.postJSON(path, body);
		const text = result;
		return GostBlockungRegel.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode addGostBlockungRegeln für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/{blockungsid : \d+}/addregeln
	 *
	 * Fügt mehrere Regeln zu einer Blockung der Gymnasialen Oberstufe hinzu.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen von Regeln hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Regeln wurden erfolgreich der Blockung hinzugefügt
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<GostBlockungRegel>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Regeln hinzuzufügen.
	 *   Code 404: Keine Blockung vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<GostBlockungRegel>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} blockungsid - der Pfad-Parameter blockungsid
	 *
	 * @returns Die Regeln wurden erfolgreich der Blockung hinzugefügt
	 */
	public async addGostBlockungRegeln(data : List<GostBlockungRegel>, schema : string, blockungsid : number) : Promise<List<GostBlockungRegel>> {
		const path = "/db/{schema}/gost/blockungen/{blockungsid : \\d+}/addregeln"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{blockungsid\s*(:[^}]+)?}/g, blockungsid.toString());
		const body : string = "[" + (data.toArray() as Array<GostBlockungRegel>).map(d => GostBlockungRegel.transpilerToJSON(d)).join() + "]";
		const result : string = await super.postJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<GostBlockungRegel>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(GostBlockungRegel.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode addGostBlockungSchiene für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/{blockungsid : \d+}/addschiene
	 *
	 * Fügt eine Schiene zu einer Blockung der Gymnasialen Oberstufe hinzu.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Schiene hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Schiene der Blockung der gymnasialen Oberstufe
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBlockungSchiene
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Schiene hinzuzufügen.
	 *   Code 404: Keine Blockung vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} blockungsid - der Pfad-Parameter blockungsid
	 *
	 * @returns Die Schiene der Blockung der gymnasialen Oberstufe
	 */
	public async addGostBlockungSchiene(schema : string, blockungsid : number) : Promise<GostBlockungSchiene> {
		const path = "/db/{schema}/gost/blockungen/{blockungsid : \\d+}/addschiene"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{blockungsid\s*(:[^}]+)?}/g, blockungsid.toString());
		const result : string = await super.postJSON(path, null);
		const text = result;
		return GostBlockungSchiene.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der DELETE-Methode deleteGostBlockungSchiene für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/{blockungsid : \d+}/deleteschiene
	 *
	 * Entfernt eine Schiene bei einer Blockung der Gymnasialen Oberstufe.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen einer Schiene hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Schiene wurde wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBlockungSchiene
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Schiene zu entfernen.
	 *   Code 404: Keine Blockung vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} blockungsid - der Pfad-Parameter blockungsid
	 *
	 * @returns Die Schiene wurde wurde erfolgreich entfernt.
	 */
	public async deleteGostBlockungSchiene(schema : string, blockungsid : number) : Promise<GostBlockungSchiene> {
		const path = "/db/{schema}/gost/blockungen/{blockungsid : \\d+}/deleteschiene"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{blockungsid\s*(:[^}]+)?}/g, blockungsid.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return GostBlockungSchiene.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode addGostBlockungKurs für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/{blockungsid : \d+}/fach/{fachid : \d+}/kursart/{kursartid : \d+}/add
	 *
	 * Fügt einen Kurs zu einer Blockung der Gymnasialen Oberstufe hinzu.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Kurses hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Kurs der Blockung der gymnasialen Oberstufe
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBlockungKurs
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Kurs hinzuzufügen.
	 *   Code 404: Keine Blockung vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} blockungsid - der Pfad-Parameter blockungsid
	 * @param {number} fachid - der Pfad-Parameter fachid
	 * @param {number} kursartid - der Pfad-Parameter kursartid
	 *
	 * @returns Der Kurs der Blockung der gymnasialen Oberstufe
	 */
	public async addGostBlockungKurs(schema : string, blockungsid : number, fachid : number, kursartid : number) : Promise<GostBlockungKurs> {
		const path = "/db/{schema}/gost/blockungen/{blockungsid : \\d+}/fach/{fachid : \\d+}/kursart/{kursartid : \\d+}/add"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{blockungsid\s*(:[^}]+)?}/g, blockungsid.toString())
			.replace(/{fachid\s*(:[^}]+)?}/g, fachid.toString())
			.replace(/{kursartid\s*(:[^}]+)?}/g, kursartid.toString());
		const result : string = await super.postJSON(path, null);
		const text = result;
		return GostBlockungKurs.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der DELETE-Methode deleteGostBlockungKurs für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/{blockungsid : \d+}/fach/{fachid : \d+}/kursart/{kursartid : \d+}/delete
	 *
	 * Entfernt einen Kurs bei einer Blockung der Gymnasialen Oberstufe.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen eines Kurses hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Kurs wurde wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBlockungKurs
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Kurs zu entfernen.
	 *   Code 404: Keine Blockung vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} blockungsid - der Pfad-Parameter blockungsid
	 * @param {number} fachid - der Pfad-Parameter fachid
	 * @param {number} kursartid - der Pfad-Parameter kursartid
	 *
	 * @returns Der Kurs wurde wurde erfolgreich entfernt.
	 */
	public async deleteGostBlockungKurs(schema : string, blockungsid : number, fachid : number, kursartid : number) : Promise<GostBlockungKurs> {
		const path = "/db/{schema}/gost/blockungen/{blockungsid : \\d+}/fach/{fachid : \\d+}/kursart/{kursartid : \\d+}/delete"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{blockungsid\s*(:[^}]+)?}/g, blockungsid.toString())
			.replace(/{fachid\s*(:[^}]+)?}/g, fachid.toString())
			.replace(/{kursartid\s*(:[^}]+)?}/g, kursartid.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return GostBlockungKurs.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode rechneGostBlockung für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/{blockungsid : \d+}/rechne/{zeit : \d+}
	 *
	 * Berechnet für die angegebene Blockung der gymnasialen Oberstufe Zwischenergebnisse und speichert diese in der DB. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Rechnen einer Blockung besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste der IDs der Zwischenergebnisse
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Long>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Blockungsdaten der Gymnasialen Oberstufe auf dem Server zu rechnen.
	 *   Code 404: Keine Blockung mit der angegebenen ID gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} blockungsid - der Pfad-Parameter blockungsid
	 * @param {number} zeit - der Pfad-Parameter zeit
	 *
	 * @returns Eine Liste der IDs der Zwischenergebnisse
	 */
	public async rechneGostBlockung(schema : string, blockungsid : number, zeit : number) : Promise<List<number>> {
		const path = "/db/{schema}/gost/blockungen/{blockungsid : \\d+}/rechne/{zeit : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{blockungsid\s*(:[^}]+)?}/g, blockungsid.toString())
			.replace(/{zeit\s*(:[^}]+)?}/g, zeit.toString());
		const result : string = await super.postJSON(path, null);
		const obj = JSON.parse(result);
		const ret = new ArrayList<number>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(parseFloat(JSON.parse(text))); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getGostBlockungKurs für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/kurse/{kursid : \d+}
	 *
	 * Liest den angegebenen Kurs einer Blockung der gymnasialen Oberstufe aus. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Blockungsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Kurs der Blockung der gymnasialen Oberstufe für die angegebene ID
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBlockungKurs
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Blockungsdaten der Gymnasialen Oberstufe auszulesen.
	 *   Code 404: Kein Kurs einer Blockung mit der angebenen ID gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} kursid - der Pfad-Parameter kursid
	 *
	 * @returns Der Kurs der Blockung der gymnasialen Oberstufe für die angegebene ID
	 */
	public async getGostBlockungKurs(schema : string, kursid : number) : Promise<GostBlockungKurs> {
		const path = "/db/{schema}/gost/blockungen/kurse/{kursid : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{kursid\s*(:[^}]+)?}/g, kursid.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostBlockungKurs.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchGostBlockungKurs für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/kurse/{kursid : \d+}
	 *
	 * Passt den angebenene Kurs der Gymnasiale Oberstufe mit der angegebenen ID an.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anpassen von Blockungsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Blockungsdaten integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Blockungsdaten zu ändern.
	 *   Code 404: Kein Kurs einer Blockung mit der angegebenen ID gefunden.
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<GostBlockungKurs>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} kursid - der Pfad-Parameter kursid
	 */
	public async patchGostBlockungKurs(data : Partial<GostBlockungKurs>, schema : string, kursid : number) : Promise<void> {
		const path = "/db/{schema}/gost/blockungen/kurse/{kursid : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{kursid\s*(:[^}]+)?}/g, kursid.toString());
		const body : string = GostBlockungKurs.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteGostBlockungKursByID für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/kurse/{kursid : \d+}
	 *
	 * Entfernt einen Kurs einer Blockung der Gymnasialen Oberstufe. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Kurs wurde wurde erfolgreich gelöscht.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBlockungKurs
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Kurs zu löschen.
	 *   Code 404: Der Kurs wurde nicht bei einer Blockung gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} kursid - der Pfad-Parameter kursid
	 *
	 * @returns Der Kurs wurde wurde erfolgreich gelöscht.
	 */
	public async deleteGostBlockungKursByID(schema : string, kursid : number) : Promise<GostBlockungKurs> {
		const path = "/db/{schema}/gost/blockungen/kurse/{kursid : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{kursid\s*(:[^}]+)?}/g, kursid.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return GostBlockungKurs.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getGostBlockungKurslehrer für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/kurse/{kursid : \d+}/lehrer/{lehrerid : \d+}
	 *
	 * Liest einen Kurs-Lehrer eines Kurses einer Blockung der Gymnasialen Oberstufe aus. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Daten zu dem Kurs-Lehrer.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBlockungKursLehrer
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Kurslehrer auszulesen.
	 *   Code 404: Der Kurs wurde nicht bei einer Blockung gefunden oder der Lehrer mit der ID existiert nicht.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} kursid - der Pfad-Parameter kursid
	 * @param {number} lehrerid - der Pfad-Parameter lehrerid
	 *
	 * @returns Die Daten zu dem Kurs-Lehrer.
	 */
	public async getGostBlockungKurslehrer(schema : string, kursid : number, lehrerid : number) : Promise<GostBlockungKursLehrer> {
		const path = "/db/{schema}/gost/blockungen/kurse/{kursid : \\d+}/lehrer/{lehrerid : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{kursid\s*(:[^}]+)?}/g, kursid.toString())
			.replace(/{lehrerid\s*(:[^}]+)?}/g, lehrerid.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostBlockungKursLehrer.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchGostBlockungKurslehrer für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/kurse/{kursid : \d+}/lehrer/{lehrerid : \d+}
	 *
	 * Passt einen Kurs-Lehrer eines Kurses einer Blockung der Gymnasialen Oberstufe an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anpassen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die Daten wurden erfolgreich angepasst.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zum Kurslehrer anzupassen.
	 *   Code 404: Der Kurs wurde nicht bei einer Blockung gefunden oder der Lehrer mit der ID existiert nicht.
	 *
	 * @param {Partial<GostBlockungKursLehrer>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} kursid - der Pfad-Parameter kursid
	 * @param {number} lehrerid - der Pfad-Parameter lehrerid
	 */
	public async patchGostBlockungKurslehrer(data : Partial<GostBlockungKursLehrer>, schema : string, kursid : number, lehrerid : number) : Promise<void> {
		const path = "/db/{schema}/gost/blockungen/kurse/{kursid : \\d+}/lehrer/{lehrerid : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{kursid\s*(:[^}]+)?}/g, kursid.toString())
			.replace(/{lehrerid\s*(:[^}]+)?}/g, lehrerid.toString());
		const body : string = GostBlockungKursLehrer.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteGostBlockungKurslehrer für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/kurse/{kursid : \d+}/lehrer/{lehrerid : \d+}
	 *
	 * Entfernt einen Kurs-Lehrer eines Kurses einer Blockung der Gymnasialen Oberstufe. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Hinzufügen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die Daten wurden erfolgreich entfernt.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Kurslehrer zu entfernen.
	 *   Code 404: Der Kurs wurde nicht bei einer Blockung gefunden oder der Lehrer mit der ID existiert nicht bei dem Kurs.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} kursid - der Pfad-Parameter kursid
	 * @param {number} lehrerid - der Pfad-Parameter lehrerid
	 */
	public async deleteGostBlockungKurslehrer(schema : string, kursid : number, lehrerid : number) : Promise<void> {
		const path = "/db/{schema}/gost/blockungen/kurse/{kursid : \\d+}/lehrer/{lehrerid : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{kursid\s*(:[^}]+)?}/g, kursid.toString())
			.replace(/{lehrerid\s*(:[^}]+)?}/g, lehrerid.toString());
		await super.deleteJSON(path, null);
		return;
	}


	/**
	 * Implementierung der POST-Methode addGostBlockungKurslehrer für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/kurse/{kursid : \d+}/lehrer/{lehrerid : \d+}/add
	 *
	 * Fügt einen Kurs-Lehrer zu einem Kurs einer Blockung der Gymnasialen Oberstufe hinzu. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Hinzufügen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Daten zu dem hinzugefügten Kurs-Lehrer.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBlockungKursLehrer
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Kurslehrer hinzuzufügen.
	 *   Code 404: Der Kurs wurde nicht bei einer Blockung gefunden oder der Lehrer mit der ID existiert nicht.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} kursid - der Pfad-Parameter kursid
	 * @param {number} lehrerid - der Pfad-Parameter lehrerid
	 *
	 * @returns Die Daten zu dem hinzugefügten Kurs-Lehrer.
	 */
	public async addGostBlockungKurslehrer(schema : string, kursid : number, lehrerid : number) : Promise<GostBlockungKursLehrer> {
		const path = "/db/{schema}/gost/blockungen/kurse/{kursid : \\d+}/lehrer/{lehrerid : \\d+}/add"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{kursid\s*(:[^}]+)?}/g, kursid.toString())
			.replace(/{lehrerid\s*(:[^}]+)?}/g, lehrerid.toString());
		const result : string = await super.postJSON(path, null);
		const text = result;
		return GostBlockungKursLehrer.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode splitGostBlockungKurs für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/kurse/{kursid : \d+}/split
	 *
	 * Teilt einen Kurs einer Blockung der Gymnasialen Oberstufe auf, indem ein zweiter Kurs mit der Hälfte der schüler erzeugt wird.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Teilen eines Kurses hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der zusätzliche Kurs der Blockung der gymnasialen Oberstufe
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBlockungKursAufteilung
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Kurs hinzuzufügen.
	 *   Code 404: Keine Blockung vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} kursid - der Pfad-Parameter kursid
	 *
	 * @returns Der zusätzliche Kurs der Blockung der gymnasialen Oberstufe
	 */
	public async splitGostBlockungKurs(schema : string, kursid : number) : Promise<GostBlockungKursAufteilung> {
		const path = "/db/{schema}/gost/blockungen/kurse/{kursid : \\d+}/split"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{kursid\s*(:[^}]+)?}/g, kursid.toString());
		const result : string = await super.postJSON(path, null);
		const text = result;
		return GostBlockungKursAufteilung.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode combineGostBlockungKurs für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/kurse/{kursid1 : \d+}/combine/{kursid2 : \d+}
	 *
	 * Führt zwei Kurse einer Blockung der Gymnasialen Oberstufe zusammen, sofern Fach und Kursart zusammenpassen.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Zusammenführen der Kurse hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der zusammengeführte Kurs der Blockung der gymnasialen Oberstufe
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBlockungKurs
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um zwei Kurse zusammenzuführen.
	 *   Code 404: Keine Blockung vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} kursid1 - der Pfad-Parameter kursid1
	 * @param {number} kursid2 - der Pfad-Parameter kursid2
	 *
	 * @returns Der zusammengeführte Kurs der Blockung der gymnasialen Oberstufe
	 */
	public async combineGostBlockungKurs(schema : string, kursid1 : number, kursid2 : number) : Promise<GostBlockungKurs> {
		const path = "/db/{schema}/gost/blockungen/kurse/{kursid1 : \\d+}/combine/{kursid2 : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{kursid1\s*(:[^}]+)?}/g, kursid1.toString())
			.replace(/{kursid2\s*(:[^}]+)?}/g, kursid2.toString());
		const result : string = await super.postJSON(path, null);
		const text = result;
		return GostBlockungKurs.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode pdfGostKursplanungKurseMitKursschuelern für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/pdf/kurse_mit kursschuelern/{blockungsergebnisid : \d+}
	 *
	 * Erstellt eine PDF-Datei mit einer Liste von Kursen mit deren Schülern zum angegebenen Ergebnis einer Blockung.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen der Kurse-Liste eines Schülers besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die PDF-Datei mit einer Liste von Kursen mit deren Schülern zum angegebenen Ergebnis einer Blockung
	 *     - Mime-Type: application/pdf
	 *     - Rückgabe-Typ: ApiFile
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Liste der Kurse der Schüler für die gymnasialen Oberstufe zu erstellen.
	 *   Code 404: Kein Eintrag zur Blockung bzw. deren Ergebnissen für die angegebenen IDs gefunden
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} blockungsergebnisid - der Pfad-Parameter blockungsergebnisid
	 *
	 * @returns Die PDF-Datei mit einer Liste von Kursen mit deren Schülern zum angegebenen Ergebnis einer Blockung
	 */
	public async pdfGostKursplanungKurseMitKursschuelern(data : List<number>, schema : string, blockungsergebnisid : number) : Promise<ApiFile> {
		const path = "/db/{schema}/gost/blockungen/pdf/kurse_mit kursschuelern/{blockungsergebnisid : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{blockungsergebnisid\s*(:[^}]+)?}/g, blockungsergebnisid.toString());
		const body : string = "[" + data.toArray().map(d => JSON.stringify(d)).join() + "]";
		const result : ApiFile = await super.postJSONtoPDF(path, body);
		return result;
	}


	/**
	 * Implementierung der POST-Methode pdfGostKursplanungKurseSchienenZuordnung für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/pdf/kurse_schienen_zuordnung/{blockungsergebnisid : \d+}
	 *
	 * Erstellt eine PDF-Datei mit der Kurse-Schienen-Zuordnung zum angegebenen Ergebnis einer Blockung. Sofern Schüler-IDs übergeben werden, werden für diese die Zuordnungen ausgegeben, andernfalls die allgemeine Zuordnung.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen der Kurse-Schienen-Zuordnung besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die PDF-Datei mit der Kurse-Schienen-Zuordnung zum angegebenen Ergebnis einer Blockung
	 *     - Mime-Type: application/pdf
	 *     - Rückgabe-Typ: ApiFile
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Kurse-Schienen-Zuordnung für die gymnasialen Oberstufe zu erstellen.
	 *   Code 404: Kein Eintrag zur Blockung bzw. deren Ergebnissen für die angegebenen IDs gefunden
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} blockungsergebnisid - der Pfad-Parameter blockungsergebnisid
	 *
	 * @returns Die PDF-Datei mit der Kurse-Schienen-Zuordnung zum angegebenen Ergebnis einer Blockung
	 */
	public async pdfGostKursplanungKurseSchienenZuordnung(data : List<number>, schema : string, blockungsergebnisid : number) : Promise<ApiFile> {
		const path = "/db/{schema}/gost/blockungen/pdf/kurse_schienen_zuordnung/{blockungsergebnisid : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{blockungsergebnisid\s*(:[^}]+)?}/g, blockungsergebnisid.toString());
		const body : string = "[" + data.toArray().map(d => JSON.stringify(d)).join() + "]";
		const result : ApiFile = await super.postJSONtoPDF(path, body);
		return result;
	}


	/**
	 * Implementierung der POST-Methode pdfGostKursplanungSchuelerMitKursen für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/pdf/schueler_mit_kursen/{blockungsergebnisid : \d+}
	 *
	 * Erstellt eine PDF-Datei mit einer Liste von Schülern und deren belegten Kursen zum angegebenen Ergebnis einer Blockung.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen der Kurse-Liste eines Schülers besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die PDF-Datei mit einer Liste von Schülern und deren belegten Kursen zum angegebenen Ergebnis einer Blockung
	 *     - Mime-Type: application/pdf
	 *     - Rückgabe-Typ: ApiFile
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Liste der Kurse der Schüler für die gymnasialen Oberstufe zu erstellen.
	 *   Code 404: Kein Eintrag zur Blockung bzw. deren Ergebnissen für die angegebenen IDs gefunden
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} blockungsergebnisid - der Pfad-Parameter blockungsergebnisid
	 *
	 * @returns Die PDF-Datei mit einer Liste von Schülern und deren belegten Kursen zum angegebenen Ergebnis einer Blockung
	 */
	public async pdfGostKursplanungSchuelerMitKursen(data : List<number>, schema : string, blockungsergebnisid : number) : Promise<ApiFile> {
		const path = "/db/{schema}/gost/blockungen/pdf/schueler_mit_kursen/{blockungsergebnisid : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{blockungsergebnisid\s*(:[^}]+)?}/g, blockungsergebnisid.toString());
		const body : string = "[" + data.toArray().map(d => JSON.stringify(d)).join() + "]";
		const result : ApiFile = await super.postJSONtoPDF(path, body);
		return result;
	}


	/**
	 * Implementierung der DELETE-Methode deleteGostBlockungRegelnByID für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/regeln
	 *
	 * Entfernt mehrere Regeln einer Blockung der Gymnasialen Oberstufe. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die Regeln wurde wurden erfolgreich gelöscht.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Regeln zu löschen.
	 *   Code 404: Mindestens eine Regel wurde nicht bei einer Blockung gefunden.
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 */
	public async deleteGostBlockungRegelnByID(data : List<number>, schema : string) : Promise<void> {
		const path = "/db/{schema}/gost/blockungen/regeln"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = "[" + data.toArray().map(d => JSON.stringify(d)).join() + "]";
		await super.deleteJSON(path, body);
		return;
	}


	/**
	 * Implementierung der GET-Methode getGostBlockungRegel für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/regeln/{regelid : \d+}
	 *
	 * Liest die angegebene Regel einer Blockung der gymnasialen Oberstufe aus. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Blockungsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Regel der Blockung der gymnasialen Oberstufe für die angegebene ID
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBlockungRegel
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Blockungsdaten der Gymnasialen Oberstufe auszulesen.
	 *   Code 404: Keine Regel einer Blockung mit der angegebenen ID gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} regelid - der Pfad-Parameter regelid
	 *
	 * @returns Die Regel der Blockung der gymnasialen Oberstufe für die angegebene ID
	 */
	public async getGostBlockungRegel(schema : string, regelid : number) : Promise<GostBlockungRegel> {
		const path = "/db/{schema}/gost/blockungen/regeln/{regelid : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{regelid\s*(:[^}]+)?}/g, regelid.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostBlockungRegel.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchGostBlockungRegel für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/regeln/{regelid : \d+}
	 *
	 * Passt die angegebene Regel der Gymnasiale Oberstufe mit der angegebenen ID an.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anpassen von Blockungsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Blockungsdaten integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Blockungsdaten zu ändern.
	 *   Code 404: Keine Regel einer Blockung mit der angebenen ID gefunden.
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<GostBlockungRegel>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} regelid - der Pfad-Parameter regelid
	 */
	public async patchGostBlockungRegel(data : Partial<GostBlockungRegel>, schema : string, regelid : number) : Promise<void> {
		const path = "/db/{schema}/gost/blockungen/regeln/{regelid : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{regelid\s*(:[^}]+)?}/g, regelid.toString());
		const body : string = GostBlockungRegel.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteGostBlockungRegelByID für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/regeln/{regelid : \d+}
	 *
	 * Entfernt eine Regel einer Blockung der Gymnasialen Oberstufe. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Regel wurde wurde erfolgreich gelöscht.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBlockungRegel
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Regel zu löschen.
	 *   Code 404: Die Regel wurde nicht bei einer Blockung gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} regelid - der Pfad-Parameter regelid
	 *
	 * @returns Die Regel wurde wurde erfolgreich gelöscht.
	 */
	public async deleteGostBlockungRegelByID(schema : string, regelid : number) : Promise<GostBlockungRegel> {
		const path = "/db/{schema}/gost/blockungen/regeln/{regelid : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{regelid\s*(:[^}]+)?}/g, regelid.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return GostBlockungRegel.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getGostBlockungSchiene für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/schiene/{schienenid : \d+}
	 *
	 * Liest die angegebene Schiene einer Blockung der gymnasialen Oberstufe aus. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Blockungsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Schiene der Blockung der gymnasialen Oberstfue für die angegebene ID
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBlockungSchiene
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Blockungsdaten der Gymnasialen Oberstufe auszulesen.
	 *   Code 404: Keine Schiene einer Blockung mit der angebenen ID gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} schienenid - der Pfad-Parameter schienenid
	 *
	 * @returns Die Schiene der Blockung der gymnasialen Oberstfue für die angegebene ID
	 */
	public async getGostBlockungSchiene(schema : string, schienenid : number) : Promise<GostBlockungSchiene> {
		const path = "/db/{schema}/gost/blockungen/schiene/{schienenid : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{schienenid\s*(:[^}]+)?}/g, schienenid.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostBlockungSchiene.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchGostBlockungSchiene für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/schiene/{schienenid : \d+}
	 *
	 * Passt die angegebene Schiene der Gymnasiale Oberstufe mit der angegebenen ID an.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anpassen von Blockungsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Blockungsdaten integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Blockungsdaten zu ändern.
	 *   Code 404: Keine Schiene einer Blockung mit der angegebenen ID gefunden.
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<GostBlockungSchiene>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} schienenid - der Pfad-Parameter schienenid
	 */
	public async patchGostBlockungSchiene(data : Partial<GostBlockungSchiene>, schema : string, schienenid : number) : Promise<void> {
		const path = "/db/{schema}/gost/blockungen/schiene/{schienenid : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{schienenid\s*(:[^}]+)?}/g, schienenid.toString());
		const body : string = GostBlockungSchiene.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteGostBlockungSchieneByID für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/schiene/{schienenid : \d+}
	 *
	 * Entfernt eine Schiene einer Blockung der Gymnasialen Oberstufe. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Schiene wurde wurde erfolgreich gelöscht.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBlockungSchiene
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Schiene zu löschen.
	 *   Code 404: Die Schiene wurde nicht bei einer Blockung gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} schienenid - der Pfad-Parameter schienenid
	 *
	 * @returns Die Schiene wurde wurde erfolgreich gelöscht.
	 */
	public async deleteGostBlockungSchieneByID(schema : string, schienenid : number) : Promise<GostBlockungSchiene> {
		const path = "/db/{schema}/gost/blockungen/schiene/{schienenid : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{schienenid\s*(:[^}]+)?}/g, schienenid.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return GostBlockungSchiene.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getGostBlockungsergebnis für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \d+}
	 *
	 * Liest für das angegebene Blockungsergebnis einer Blockung der gymnasialen Oberstufe die Daten aus. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Blockungsergebnisse besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Blockungsergebnisse der gymnasialen Oberstufe für die angegebene ID
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBlockungsergebnis
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Blockungsergebnisse einer Blockung der Gymnasialen Oberstufe auszulesen.
	 *   Code 404: Keine Blockung mit der angebenen ID gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} ergebnisid - der Pfad-Parameter ergebnisid
	 *
	 * @returns Die Blockungsergebnisse der gymnasialen Oberstufe für die angegebene ID
	 */
	public async getGostBlockungsergebnis(schema : string, ergebnisid : number) : Promise<GostBlockungsergebnis> {
		const path = "/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{ergebnisid\s*(:[^}]+)?}/g, ergebnisid.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostBlockungsergebnis.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der DELETE-Methode deleteGostBlockungsergebnis für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \d+}
	 *
	 * Entfernt das angegebene Zwischenergebnis einer Blockung der gymnasialen Oberstufe. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen des Zwischenergebnisses besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Das Zwischenergebnis einer Blockung der gymnasialen Oberstufe für die angegebene ID wurde erfolgreich gelöscht.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Long
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um das Zwischenergebnis einer Blockung der Gymnasialen Oberstufe zu löschen.
	 *   Code 404: Keine Blockung mit der angegebenen ID gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} ergebnisid - der Pfad-Parameter ergebnisid
	 *
	 * @returns Das Zwischenergebnis einer Blockung der gymnasialen Oberstufe für die angegebene ID wurde erfolgreich gelöscht.
	 */
	public async deleteGostBlockungsergebnis(schema : string, ergebnisid : number) : Promise<number | null> {
		const path = "/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{ergebnisid\s*(:[^}]+)?}/g, ergebnisid.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return parseFloat(JSON.parse(text));
	}


	/**
	 * Implementierung der POST-Methode activateGostBlockungsergebnis für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \d+}/activate
	 *
	 * Aktiviert bzw. persistiert das Blockungsergebnis. Dies ist nur erlaubt, wenn keine aktivierte Blockung in der DB vorliegt. Beim Aktivieren wird die Kursliste und die Leistungsdaten der Schüler entsprechend befüllt.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Aktivieren eines Blockungsergebnisses besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die Zuordnung wurde erfolgreich gelöscht.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um ein Blockungsergebnis zu aktivieren.
	 *   Code 404: Keine oder nicht alle Daten zu dem Ergebnis gefunden, um dieses zu aktiveren
	 *   Code 409: Es wurde bereits eine Blockung aktiviert
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} ergebnisid - der Pfad-Parameter ergebnisid
	 */
	public async activateGostBlockungsergebnis(schema : string, ergebnisid : number) : Promise<void> {
		const path = "/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \\d+}/activate"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{ergebnisid\s*(:[^}]+)?}/g, ergebnisid.toString());
		await super.postJSON(path, null);
		return;
	}


	/**
	 * Implementierung der GET-Methode dupliziereGostBlockungMitErgebnis für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \d+}/dupliziere
	 *
	 * Dupliziert zu dem angegebenen Zwischenergebnis gehörende Blockung der gymnasialen Oberstufe. Das Zwischenergebnis wird als einziges mit dupliziert und dient bei dem Blockungsduplikat. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Duplizieren einer Blockung besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Blockungsdaten der gymnasialen Oberstufe des Duplikats als Vorlage für die Definition von Regeln
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBlockungsdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Blockungsdaten der Gymnasialen Oberstufe zu duplizieren.
	 *   Code 404: Kein Blockungsergebnis mit der angegebenen ID gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} ergebnisid - der Pfad-Parameter ergebnisid
	 *
	 * @returns Die Blockungsdaten der gymnasialen Oberstufe des Duplikats als Vorlage für die Definition von Regeln
	 */
	public async dupliziereGostBlockungMitErgebnis(schema : string, ergebnisid : number) : Promise<GostBlockungsdaten> {
		const path = "/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \\d+}/dupliziere"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{ergebnisid\s*(:[^}]+)?}/g, ergebnisid.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostBlockungsdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode schreibeGostBlockungsErgebnisHoch für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \d+}/hochschreiben
	 *
	 * Schreibt die zum Ergebnis gehörende Blockung mit dem Ergebnis in das nächste Halbjahr hoch. Nicht mehr vorhandene Fachwahlen werden ggf. automatisch entfernt. Es werden aber keine neuen Kurse oder Zuordnung neu generiert. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Hochschreiben einer Blockung besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Blockung und das Ergebnis wurde erfolgreich hochgeschrieben.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBlockungsdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Blockung der Gymnasialen Oberstufe hochzuschreiben.
	 *   Code 404: Kein Blockungsergebnis mit der angebenen ID gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} ergebnisid - der Pfad-Parameter ergebnisid
	 *
	 * @returns Die Blockung und das Ergebnis wurde erfolgreich hochgeschrieben.
	 */
	public async schreibeGostBlockungsErgebnisHoch(schema : string, ergebnisid : number) : Promise<GostBlockungsdaten> {
		const path = "/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \\d+}/hochschreiben"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{ergebnisid\s*(:[^}]+)?}/g, ergebnisid.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostBlockungsdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode createGostBlockungsergebnisKursSchieneZuordnung für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \d+}/schiene/{schienenid : \d+}/kurs/{kursid: \d+}
	 *
	 * Erstellt eine Kurszuordnung zu einer Schiene bei einem Blockungsergebnis einer Blockung der Gymnasialen Oberstufe.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen der Zuordnung besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die Zuordnung wurde erfolgreich angelegt.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Zuordnung anzulegen.
	 *   Code 404: Kein geeignetes Zwischenergebnis, Schiene oder Kurs für die Zuordnung vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} ergebnisid - der Pfad-Parameter ergebnisid
	 * @param {number} schienenid - der Pfad-Parameter schienenid
	 * @param {number} kursid - der Pfad-Parameter kursid
	 */
	public async createGostBlockungsergebnisKursSchieneZuordnung(schema : string, ergebnisid : number, schienenid : number, kursid : number) : Promise<void> {
		const path = "/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \\d+}/schiene/{schienenid : \\d+}/kurs/{kursid: \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{ergebnisid\s*(:[^}]+)?}/g, ergebnisid.toString())
			.replace(/{schienenid\s*(:[^}]+)?}/g, schienenid.toString())
			.replace(/{kursid\s*(:[^}]+)?}/g, kursid.toString());
		await super.postJSON(path, null);
		return;
	}


	/**
	 * Implementierung der DELETE-Methode deleteGostBlockungsergebnisKursSchieneZuordnung für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \d+}/schiene/{schienenid : \d+}/kurs/{kursid: \d+}
	 *
	 * Entfernt eine Kurszuordnung zu einer Schiene bei einem Blockungsergebniss einer Blockung der Gymnasialen Oberstufe. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die Zuordnung wurde erfolgreich gelöscht.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Zuordnung zu löschen.
	 *   Code 404: Das Zwischenergebnis, der Schiene oder der Kurs wurde nicht in einer gültigen Zuordnung gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} ergebnisid - der Pfad-Parameter ergebnisid
	 * @param {number} schienenid - der Pfad-Parameter schienenid
	 * @param {number} kursid - der Pfad-Parameter kursid
	 */
	public async deleteGostBlockungsergebnisKursSchieneZuordnung(schema : string, ergebnisid : number, schienenid : number, kursid : number) : Promise<void> {
		const path = "/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \\d+}/schiene/{schienenid : \\d+}/kurs/{kursid: \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{ergebnisid\s*(:[^}]+)?}/g, ergebnisid.toString())
			.replace(/{schienenid\s*(:[^}]+)?}/g, schienenid.toString())
			.replace(/{kursid\s*(:[^}]+)?}/g, kursid.toString());
		await super.deleteJSON(path, null);
		return;
	}


	/**
	 * Implementierung der POST-Methode updateGostBlockungsergebnisKursSchieneZuordnung für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \d+}/schiene/{schienenid: \d+}/kurs/{kursid: \d+}/zu/{schienenidneu: \d+}
	 *
	 * Verschiebt einen Kurse zwischen zwei Schienen bei einem Blockungsergebnis einer Blockung der Gymnasialen Oberstufe.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Verschieben besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die Zuordnung wurde erfolgreich angelegt.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Kurs zu verschieben.
	 *   Code 404: Kein geeignetes Zwischenergebnis, Schiene oder Kurs für die Zuordnung vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} ergebnisid - der Pfad-Parameter ergebnisid
	 * @param {number} schienenid - der Pfad-Parameter schienenid
	 * @param {number} kursid - der Pfad-Parameter kursid
	 * @param {number} schienenidneu - der Pfad-Parameter schienenidneu
	 */
	public async updateGostBlockungsergebnisKursSchieneZuordnung(schema : string, ergebnisid : number, schienenid : number, kursid : number, schienenidneu : number) : Promise<void> {
		const path = "/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \\d+}/schiene/{schienenid: \\d+}/kurs/{kursid: \\d+}/zu/{schienenidneu: \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{ergebnisid\s*(:[^}]+)?}/g, ergebnisid.toString())
			.replace(/{schienenid\s*(:[^}]+)?}/g, schienenid.toString())
			.replace(/{kursid\s*(:[^}]+)?}/g, kursid.toString())
			.replace(/{schienenidneu\s*(:[^}]+)?}/g, schienenidneu.toString());
		await super.postJSON(path, null);
		return;
	}


	/**
	 * Implementierung der POST-Methode createGostBlockungsergebnisKursSchuelerZuordnung für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \d+}/schueler/{schuelerid : \d+}/kurs/{kursid: \d+}
	 *
	 * Erstellt eine Kurszuordnung zu einem Schüler bei einem Blockungsergebnis einer Blockung der Gymnasialen Oberstufe.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen der Zuordnung besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die Zuordnung wurde erfolgreich angelegt.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Zuordnung anzulegen.
	 *   Code 404: Kein geeignetes Zwischenergebnis, Schüler oder Kurs für die Zuordnung vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} ergebnisid - der Pfad-Parameter ergebnisid
	 * @param {number} schuelerid - der Pfad-Parameter schuelerid
	 * @param {number} kursid - der Pfad-Parameter kursid
	 */
	public async createGostBlockungsergebnisKursSchuelerZuordnung(schema : string, ergebnisid : number, schuelerid : number, kursid : number) : Promise<void> {
		const path = "/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \\d+}/schueler/{schuelerid : \\d+}/kurs/{kursid: \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{ergebnisid\s*(:[^}]+)?}/g, ergebnisid.toString())
			.replace(/{schuelerid\s*(:[^}]+)?}/g, schuelerid.toString())
			.replace(/{kursid\s*(:[^}]+)?}/g, kursid.toString());
		await super.postJSON(path, null);
		return;
	}


	/**
	 * Implementierung der DELETE-Methode deleteGostBlockungsergebnisKursSchuelerZuordnung für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \d+}/schueler/{schuelerid : \d+}/kurs/{kursid: \d+}
	 *
	 * Entfernt eine Kurszuordnung zu einem Schüler bei einem Blockungsergebniss einer Blockung der Gymnasialen Oberstufe. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die Zuordnung wurde erfolgreich gelöscht.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Zuordnung zu löschen.
	 *   Code 404: Das Zwischenergebnis, der Schüler oder der Kurs wurde nicht in einer gültigen Zuordnung gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} ergebnisid - der Pfad-Parameter ergebnisid
	 * @param {number} schuelerid - der Pfad-Parameter schuelerid
	 * @param {number} kursid - der Pfad-Parameter kursid
	 */
	public async deleteGostBlockungsergebnisKursSchuelerZuordnung(schema : string, ergebnisid : number, schuelerid : number, kursid : number) : Promise<void> {
		const path = "/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \\d+}/schueler/{schuelerid : \\d+}/kurs/{kursid: \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{ergebnisid\s*(:[^}]+)?}/g, ergebnisid.toString())
			.replace(/{schuelerid\s*(:[^}]+)?}/g, schuelerid.toString())
			.replace(/{kursid\s*(:[^}]+)?}/g, kursid.toString());
		await super.deleteJSON(path, null);
		return;
	}


	/**
	 * Implementierung der POST-Methode updateGostBlockungsergebnisKursSchuelerZuordnung für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \d+}/schueler/{schuelerid : \d+}/kurs/{kursid: \d+}/zu/{kursidneu: \d+}
	 *
	 * Verschiebt einen Schüler zwischen zwei Kursen bei einem Blockungsergebnis einer Blockung der Gymnasialen Oberstufe.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Verschieben besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die Zuordnung wurde erfolgreich angelegt.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um das Verschieben vorzunehmen.
	 *   Code 404: Kein geeignetes Zwischenergebnis, Schüler oder Kurs für die Zuordnung vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} ergebnisid - der Pfad-Parameter ergebnisid
	 * @param {number} schuelerid - der Pfad-Parameter schuelerid
	 * @param {number} kursid - der Pfad-Parameter kursid
	 * @param {number} kursidneu - der Pfad-Parameter kursidneu
	 */
	public async updateGostBlockungsergebnisKursSchuelerZuordnung(schema : string, ergebnisid : number, schuelerid : number, kursid : number, kursidneu : number) : Promise<void> {
		const path = "/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \\d+}/schueler/{schuelerid : \\d+}/kurs/{kursid: \\d+}/zu/{kursidneu: \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{ergebnisid\s*(:[^}]+)?}/g, ergebnisid.toString())
			.replace(/{schuelerid\s*(:[^}]+)?}/g, schuelerid.toString())
			.replace(/{kursid\s*(:[^}]+)?}/g, kursid.toString())
			.replace(/{kursidneu\s*(:[^}]+)?}/g, kursidneu.toString());
		await super.postJSON(path, null);
		return;
	}


	/**
	 * Implementierung der POST-Methode syncGostBlockungsergebnis für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \d+}/synchronize
	 *
	 * Synchronisiert das Blockungsergebnis mit den Kursen und den Leistungsdaten. Dies ist nur erlaubt, wenn Leistungsdatenin der DB vorliegen. Beim Synchronisieren werden die Kursliste und die Leistungsdaten der Schüler angepasst. Es werden jedoch keine Kurse entfernt und es werden keine Fachwahlen bei Schülern ergänzt.Dies muss ggf. manuell erfolgen.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Aktivieren eines Blockungsergebnisses besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die Zuordnung wurde erfolgreich synchronisiert.
	 *   Code 400: Das Ergebnis ist einem vergangenem Schuljahresabschnitt zugeordnet. Eine Synchronisation wird hier nicht mehr zugelassen.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um ein Blockungsergebnis mit den Leristungsdaten zu synchronisieren.
	 *   Code 404: Keine oder nicht alle Daten zu dem Ergebnis gefunden, um dieses zu synchronisieren
	 *   Code 409: Es sind noch keinerlei Leistungsdaten für eine Synchronisation in dem Schuljahresabschnitt bei den Schülern vorhanden. Verwenden Sie stattdessen das Aktivieren eines Ergebnisses.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} ergebnisid - der Pfad-Parameter ergebnisid
	 */
	public async syncGostBlockungsergebnis(schema : string, ergebnisid : number) : Promise<void> {
		const path = "/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \\d+}/synchronize"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{ergebnisid\s*(:[^}]+)?}/g, ergebnisid.toString());
		await super.postJSON(path, null);
		return;
	}


	/**
	 * Implementierung der PATCH-Methode patchGostFachkombination für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/fachkombination/{id : \d+}
	 *
	 * Passt die Fachkombination mit der angegebenen ID an.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anpassen von Fachkombinationen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Fachkombination integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Fachkombinationen zu ändern.
	 *   Code 404: Keine Fachkombination mit der angegebenen ID gefunden oder es wurden kein gültiges Fach als ID übergeben.
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<GostJahrgangFachkombination>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchGostFachkombination(data : Partial<GostJahrgangFachkombination>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/gost/fachkombination/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = GostJahrgangFachkombination.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteGostFachkombination für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/fachkombination/{id : \d+}
	 *
	 * Entfernt eine Regel zu einer Fachkombination in der Gymnasialen Oberstufe. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Regel wurde wurde erfolgreich gelöscht.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostJahrgangFachkombination
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Regel zu der Fachkombination zu löschen.
	 *   Code 404: Die Regel zu der Fachkombination wurde nicht gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Regel wurde wurde erfolgreich gelöscht.
	 */
	public async deleteGostFachkombination(schema : string, id : number) : Promise<GostJahrgangFachkombination> {
		const path = "/db/{schema}/gost/fachkombination/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return GostJahrgangFachkombination.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getGostKlausurenKalenderinformationen für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/kalenderinformationen
	 *
	 * Liest eine Liste der Klausurvorgaben eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Liste der Klausurvorgaben.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<GostKlausurenKalenderinformation>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Kursklausuren auszulesen.
	 *   Code 404: Der Abiturjahrgang oder das Halbjahr wurde nicht gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Liste der Klausurvorgaben.
	 */
	public async getGostKlausurenKalenderinformationen(schema : string) : Promise<List<GostKlausurenKalenderinformation>> {
		const path = "/db/{schema}/gost/klausuren/kalenderinformationen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<GostKlausurenKalenderinformation>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(GostKlausurenKalenderinformation.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der PATCH-Methode patchGostKlausurenKalenderinformation für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/kalenderinformationen/{id : \d+}
	 *
	 * Patcht einen Gost-Klausurtermin.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen eines Gost-Klausurtermins besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in den Klausurtermin integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Klausurtermine zu ändern.
	 *   Code 404: Kein Klausurtermin-Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<GostKlausurenKalenderinformation>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchGostKlausurenKalenderinformation(data : Partial<GostKlausurenKalenderinformation>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/gost/klausuren/kalenderinformationen/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = GostKlausurenKalenderinformation.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteGostKlausurenKalenderinformation für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/kalenderinformationen/delete/{id : \d+}
	 *
	 * Löscht einen Gost-Klausurtermin.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Gost-Klausurtermins besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Klausurtermin für die angegebene ID wurden erfolgreich gelöscht.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Long
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Gost-Klausurtermin anzulegen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Klausurtermin für die angegebene ID wurden erfolgreich gelöscht.
	 */
	public async deleteGostKlausurenKalenderinformation(schema : string, id : number) : Promise<number | null> {
		const path = "/db/{schema}/gost/klausuren/kalenderinformationen/delete/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return parseFloat(JSON.parse(text));
	}


	/**
	 * Implementierung der POST-Methode createGostKlausurenKalenderinformation für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/kalenderinformationen/new
	 *
	 * Erstellt eine neue Gost-Klausurvorgabe und gibt sie zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Gost-Klausurvorgabe besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Gost-Klausurvorgabe wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostKlausurvorgabe
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Gost-Klausurvorgabe anzulegen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {GostKlausurenKalenderinformation} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Gost-Klausurvorgabe wurde erfolgreich angelegt.
	 */
	public async createGostKlausurenKalenderinformation(data : GostKlausurenKalenderinformation, schema : string) : Promise<GostKlausurvorgabe> {
		const path = "/db/{schema}/gost/klausuren/kalenderinformationen/new"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = GostKlausurenKalenderinformation.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return GostKlausurvorgabe.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode patchGostKlausurenKursklausur für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/kursklausuren/{id : \d+}/abschnitt/{abschnittid : -?\d+}
	 *
	 * Patcht einen Gost-Kursklausur.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen einer Gost-Kursklausur besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Der Patch wurde erfolgreich in die Kursklausur integriert.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostKlausurenCollectionSkrsKrs
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Kursklausuren zu ändern.
	 *   Code 404: Kein Kursklausur-Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<GostKursklausur>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 * @param {number} abschnittid - der Pfad-Parameter abschnittid
	 *
	 * @returns Der Patch wurde erfolgreich in die Kursklausur integriert.
	 */
	public async patchGostKlausurenKursklausur(data : Partial<GostKursklausur>, schema : string, id : number, abschnittid : number) : Promise<GostKlausurenCollectionSkrsKrs> {
		const path = "/db/{schema}/gost/klausuren/kursklausuren/{id : \\d+}/abschnitt/{abschnittid : -?\\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString())
			.replace(/{abschnittid\s*(:[^}]+)?}/g, abschnittid.toString());
		const body : string = GostKursklausur.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return GostKlausurenCollectionSkrsKrs.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getGostKlausurenKursklausurenJahrgang für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/kursklausuren/abiturjahrgang/{abiturjahr : -?\d+}
	 *
	 * Liest eine Liste der Kursklausuren eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Liste der Kursklausuren.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<GostKursklausur>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Kursklausuren auszulesen.
	 *   Code 404: Der Abiturjahrgang oder das Halbjahr wurde nicht gefunden.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 *
	 * @returns Die Liste der Kursklausuren.
	 */
	public async getGostKlausurenKursklausurenJahrgang(schema : string, abiturjahr : number) : Promise<List<GostKursklausur>> {
		const path = "/db/{schema}/gost/klausuren/kursklausuren/abiturjahrgang/{abiturjahr : -?\\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<GostKursklausur>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(GostKursklausur.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getGostKlausurenKursklausurenJahrgangHalbjahr für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/kursklausuren/abiturjahrgang/{abiturjahr : -?\d+}/halbjahr/{halbjahr : \d+}
	 *
	 * Liest eine Liste der Kursklausuren eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Liste der Kursklausuren.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<GostKursklausur>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Kursklausuren auszulesen.
	 *   Code 404: Der Abiturjahrgang oder das Halbjahr wurde nicht gefunden.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 * @param {number} halbjahr - der Pfad-Parameter halbjahr
	 *
	 * @returns Die Liste der Kursklausuren.
	 */
	public async getGostKlausurenKursklausurenJahrgangHalbjahr(schema : string, abiturjahr : number, halbjahr : number) : Promise<List<GostKursklausur>> {
		const path = "/db/{schema}/gost/klausuren/kursklausuren/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString())
			.replace(/{halbjahr\s*(:[^}]+)?}/g, halbjahr.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<GostKursklausur>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(GostKursklausur.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode blockenGostKursklausuren für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/kursklausuren/blocken
	 *
	 * Weist die angegebenen Schülerklausuren dem Klausurraum zu.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Zuweisen eines Klausurraums besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Gost-Klausurraumstunde wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Boolean
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einer Gost-Klausurraumstunde anzulegen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {GostKlausurterminblockungDaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Gost-Klausurraumstunde wurde erfolgreich angelegt.
	 */
	public async blockenGostKursklausuren(data : GostKlausurterminblockungDaten, schema : string) : Promise<boolean | null> {
		const path = "/db/{schema}/gost/klausuren/kursklausuren/blocken"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = GostKlausurterminblockungDaten.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return (text === "true");
	}


	/**
	 * Implementierung der GET-Methode createGostKlausurenKursklausurenJahrgangHalbjahrQuartal für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/kursklausuren/create/abiturjahrgang/{abiturjahr : -?\d+}/halbjahr/{halbjahr : \d+}/quartal/{quartal : \d+}
	 *
	 * Liest eine Liste der Kursklausuren eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Liste der Kursklausuren.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<GostKursklausur>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Kursklausuren auszulesen.
	 *   Code 404: Keine Klausurvorgaben definiert oder der Schuljahresabschnitt wurde nicht gefunden.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 * @param {number} halbjahr - der Pfad-Parameter halbjahr
	 * @param {number} quartal - der Pfad-Parameter quartal
	 *
	 * @returns Die Liste der Kursklausuren.
	 */
	public async createGostKlausurenKursklausurenJahrgangHalbjahrQuartal(schema : string, abiturjahr : number, halbjahr : number, quartal : number) : Promise<List<GostKursklausur>> {
		const path = "/db/{schema}/gost/klausuren/kursklausuren/create/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : \\d+}/quartal/{quartal : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString())
			.replace(/{halbjahr\s*(:[^}]+)?}/g, halbjahr.toString())
			.replace(/{quartal\s*(:[^}]+)?}/g, quartal.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<GostKursklausur>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(GostKursklausur.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der PATCH-Methode patchGostKlausurenRaum für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/raeume/{id : \d+}
	 *
	 * Patcht einen Gost-Klausurraum.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen eines Gost-Klausurraums besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in den Klausurraum integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Klausurräume zu ändern.
	 *   Code 404: Kein Termin mit der übergebenen ID oder Stundenplanraum-Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<GostKlausurraum>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchGostKlausurenRaum(data : Partial<GostKlausurraum>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/gost/klausuren/raeume/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = GostKlausurraum.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getGostKlausurenRaeumeTermin für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/raeume/{termin : -?\d+}
	 *
	 * Liest eine Liste der Klausurräume eines Gost-Klausurtermins aus. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Liste der Klausurräume.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<GostKlausurraum>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Klausurräume auszulesen.
	 *   Code 404: Die Id des Klausurtermins wurde nicht gefunden.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} termin - der Pfad-Parameter termin
	 *
	 * @returns Die Liste der Klausurräume.
	 */
	public async getGostKlausurenRaeumeTermin(schema : string, termin : number) : Promise<List<GostKlausurraum>> {
		const path = "/db/{schema}/gost/klausuren/raeume/{termin : -?\\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{termin\s*(:[^}]+)?}/g, termin.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<GostKlausurraum>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(GostKlausurraum.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der DELETE-Methode deleteGostKlausurenRaum für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/raeume/delete/{id : \d+}
	 *
	 * Löscht einen Gost-Klausurraum.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen eines Gost-Klausurraums besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Klausurraum für die angegebene ID wurden erfolgreich gelöscht.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Boolean
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Gost-Klausurraum zu löschen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Klausurraum für die angegebene ID wurden erfolgreich gelöscht.
	 */
	public async deleteGostKlausurenRaum(schema : string, id : number) : Promise<boolean | null> {
		const path = "/db/{schema}/gost/klausuren/raeume/delete/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return (text === "true");
	}


	/**
	 * Implementierung der POST-Methode createGostKlausurenRaum für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/raeume/new
	 *
	 * Erstellt einen neue Gost-Klausurraum und gibt ihn zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Gost-Klausurraums besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Gost-Klausurraum wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostKlausurraum
	 *   Code 400: Die Daten sind fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Gost-Klausurraum anzulegen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<GostKlausurraum>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Gost-Klausurraum wurde erfolgreich angelegt.
	 */
	public async createGostKlausurenRaum(data : Partial<GostKlausurraum>, schema : string) : Promise<GostKlausurraum> {
		const path = "/db/{schema}/gost/klausuren/raeume/new"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = GostKlausurraum.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return GostKlausurraum.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getGostKlausurenSchuelerraumstundenTermin für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/raumstunden/{termin : -?\d+}
	 *
	 * Liest eine Liste der Klausurraumstunden eines Gost-Klausurtermins aus. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Gost-Klausurraumstunde wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostKlausurenCollectionSkrsKrs
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Klausurraumstunden auszulesen.
	 *   Code 404: Der Termin-ID wurde nicht gefunden.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} termin - der Pfad-Parameter termin
	 *
	 * @returns Gost-Klausurraumstunde wurde erfolgreich angelegt.
	 */
	public async getGostKlausurenSchuelerraumstundenTermin(schema : string, termin : number) : Promise<GostKlausurenCollectionSkrsKrs> {
		const path = "/db/{schema}/gost/klausuren/raumstunden/{termin : -?\\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{termin\s*(:[^}]+)?}/g, termin.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostKlausurenCollectionSkrsKrs.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchGostKlausurenSchuelerklausur für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/schuelerklausuren/{id : \d+}
	 *
	 * Patcht einen Gost-Schuelerklausur.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen einer Gost-Schuelerklausur besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Schuelerklausur integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Schuelerklausuren zu ändern.
	 *   Code 404: Kein Schuelerklausur-Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<GostSchuelerklausur>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchGostKlausurenSchuelerklausur(data : Partial<GostSchuelerklausur>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/gost/klausuren/schuelerklausuren/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = GostSchuelerklausur.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getGostKlausurenSchuelerklausuren für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/schuelerklausuren/termin/{id : -?\d+}
	 *
	 * Liest eine Liste der Schuelerklausuren zu einem Klausurtermin aus. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Liste der Schuelerklausuren.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<GostSchuelerklausur>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Schuelerklausuren auszulesen.
	 *   Code 404: Der Abiturjahrgang oder das Halbjahr wurde nicht gefunden.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Liste der Schuelerklausuren.
	 */
	public async getGostKlausurenSchuelerklausuren(schema : string, id : number) : Promise<List<GostSchuelerklausur>> {
		const path = "/db/{schema}/gost/klausuren/schuelerklausuren/termin/{id : -?\\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<GostSchuelerklausur>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(GostSchuelerklausur.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode setzeGostSchuelerklausurenZuRaum für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/schuelerklausuren/zuraum/{raumid : -?\d+}/abschnitt/{abschnittid : -?\d+}
	 *
	 * Weist die angegebenen Schülerklausuren dem Klausurraum zu.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Zuweisen eines Klausurraums besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Gost-Klausurraumstunde wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostKlausurenCollectionSkrsKrs
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einer Gost-Klausurraumstunde anzulegen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} raumid - der Pfad-Parameter raumid
	 * @param {number} abschnittid - der Pfad-Parameter abschnittid
	 *
	 * @returns Gost-Klausurraumstunde wurde erfolgreich angelegt.
	 */
	public async setzeGostSchuelerklausurenZuRaum(data : List<number>, schema : string, raumid : number, abschnittid : number) : Promise<GostKlausurenCollectionSkrsKrs> {
		const path = "/db/{schema}/gost/klausuren/schuelerklausuren/zuraum/{raumid : -?\\d+}/abschnitt/{abschnittid : -?\\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{raumid\s*(:[^}]+)?}/g, raumid.toString())
			.replace(/{abschnittid\s*(:[^}]+)?}/g, abschnittid.toString());
		const body : string = "[" + data.toArray().map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.postJSON(path, body);
		const text = result;
		return GostKlausurenCollectionSkrsKrs.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchGostKlausurenKlausurtermin für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/termine/{id : \d+}
	 *
	 * Patcht einen Gost-Klausurtermin.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen eines Gost-Klausurtermins besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in den Klausurtermin integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Klausurtermine zu ändern.
	 *   Code 404: Kein Klausurtermin-Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<GostKlausurtermin>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchGostKlausurenKlausurtermin(data : Partial<GostKlausurtermin>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/gost/klausuren/termine/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = GostKlausurtermin.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getGostKlausurenKlausurtermineJahrgangHalbjahr für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/termine/abiturjahrgang/{abiturjahr : -?\d+}/halbjahr/{halbjahr : \d+}
	 *
	 * Liest eine Liste der Kurstermine eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Liste der Klausurtermine.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<GostKlausurtermin>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Klausurtermine auszulesen.
	 *   Code 404: Der Abiturjahrgang oder das Halbjahr wurde nicht gefunden.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 * @param {number} halbjahr - der Pfad-Parameter halbjahr
	 *
	 * @returns Die Liste der Klausurtermine.
	 */
	public async getGostKlausurenKlausurtermineJahrgangHalbjahr(schema : string, abiturjahr : number, halbjahr : number) : Promise<List<GostKlausurtermin>> {
		const path = "/db/{schema}/gost/klausuren/termine/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString())
			.replace(/{halbjahr\s*(:[^}]+)?}/g, halbjahr.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<GostKlausurtermin>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(GostKlausurtermin.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der DELETE-Methode deleteGostKlausurenKlausurtermine für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/termine/delete
	 *
	 * Löscht Gost-Klausurtermine.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Gost-Klausurtermins besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Klausurtermin für die angegebene ID wurden erfolgreich gelöscht.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Long>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Gost-Klausurtermin anzulegen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Klausurtermin für die angegebene ID wurden erfolgreich gelöscht.
	 */
	public async deleteGostKlausurenKlausurtermine(data : List<number>, schema : string) : Promise<List<number>> {
		const path = "/db/{schema}/gost/klausuren/termine/delete"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = "[" + data.toArray().map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<number>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(parseFloat(JSON.parse(text))); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode createGostKlausurenKlausurtermin für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/termine/new
	 *
	 * Erstellt einen neuen Gost-Klausurtermin und gibt ihn zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Gost-Klausurtermins besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Gost-Klausurtermin wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostKlausurtermin
	 *   Code 400: Die Daten sind fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Gost-Klausurtermin anzulegen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<GostKlausurtermin>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Gost-Klausurtermin wurde erfolgreich angelegt.
	 */
	public async createGostKlausurenKlausurtermin(data : Partial<GostKlausurtermin>, schema : string) : Promise<GostKlausurtermin> {
		const path = "/db/{schema}/gost/klausuren/termine/new"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = GostKlausurtermin.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return GostKlausurtermin.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchGostKlausurenVorgabe für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/vorgaben/{id : \d+}
	 *
	 * Patcht eine Gost-Klausurvorgabe.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen eines Gost-Klausurtermins besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Klausurvorgabe integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Klausurvorgaben zu ändern.
	 *   Code 404: Kein Klausurvorgabe-Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<GostKlausurvorgabe>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchGostKlausurenVorgabe(data : Partial<GostKlausurvorgabe>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/gost/klausuren/vorgaben/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = GostKlausurvorgabe.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getGostKlausurenVorgabenJahrgang für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/vorgaben/abiturjahrgang/{abiturjahr : -?\d+}
	 *
	 * Liest eine Liste der Klausurvorgaben eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Liste der Klausurvorgaben.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<GostKlausurvorgabe>
	 *   Code 400: Die Daten sind fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Kursklausuren auszulesen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 *
	 * @returns Die Liste der Klausurvorgaben.
	 */
	public async getGostKlausurenVorgabenJahrgang(schema : string, abiturjahr : number) : Promise<List<GostKlausurvorgabe>> {
		const path = "/db/{schema}/gost/klausuren/vorgaben/abiturjahrgang/{abiturjahr : -?\\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<GostKlausurvorgabe>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(GostKlausurvorgabe.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getGostKlausurenVorgabenJahrgangHalbjahr für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/vorgaben/abiturjahrgang/{abiturjahr : -?\d+}/halbjahr/{halbjahr : \d+}
	 *
	 * Liest eine Liste der Klausurvorgaben eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Liste der Klausurvorgaben.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<GostKlausurvorgabe>
	 *   Code 400: Die Daten sind fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Kursklausuren auszulesen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 * @param {number} halbjahr - der Pfad-Parameter halbjahr
	 *
	 * @returns Die Liste der Klausurvorgaben.
	 */
	public async getGostKlausurenVorgabenJahrgangHalbjahr(schema : string, abiturjahr : number, halbjahr : number) : Promise<List<GostKlausurvorgabe>> {
		const path = "/db/{schema}/gost/klausuren/vorgaben/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString())
			.replace(/{halbjahr\s*(:[^}]+)?}/g, halbjahr.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<GostKlausurvorgabe>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(GostKlausurvorgabe.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getGostKlausurenVorgabenJahrgangSchuljahr für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/vorgaben/abiturjahrgang/{abiturjahr : -?\d+}/schuljahr/{halbjahr : \d+}
	 *
	 * Liest eine Liste der Klausurvorgaben eines Abiturjahrgangs eines Halbjahres der Gymnasialen Oberstufe aus. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Liste der Klausurvorgaben.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<GostKlausurvorgabe>
	 *   Code 400: Die Daten sind fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Kursklausuren auszulesen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 * @param {number} halbjahr - der Pfad-Parameter halbjahr
	 *
	 * @returns Die Liste der Klausurvorgaben.
	 */
	public async getGostKlausurenVorgabenJahrgangSchuljahr(schema : string, abiturjahr : number, halbjahr : number) : Promise<List<GostKlausurvorgabe>> {
		const path = "/db/{schema}/gost/klausuren/vorgaben/abiturjahrgang/{abiturjahr : -?\\d+}/schuljahr/{halbjahr : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString())
			.replace(/{halbjahr\s*(:[^}]+)?}/g, halbjahr.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<GostKlausurvorgabe>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(GostKlausurvorgabe.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode copyGostKlausurenVorgaben für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/vorgaben/copyVorlagen/abiturjahrgang/{abiturjahr : -?\d+}/halbjahr/{halbjahr : -?\d+}/quartal/{quartal : -?\d+}
	 *
	 * Erstellt eine neue Gost-Klausurvorgabe und gibt sie zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Gost-Klausurvorgabe besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Gost-Klausurvorgaben wurden erfolgreich angelegt.
	 *   Code 400: Falsche Parameter
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Gost-Klausurvorgabe anzulegen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 * @param {number} halbjahr - der Pfad-Parameter halbjahr
	 * @param {number} quartal - der Pfad-Parameter quartal
	 */
	public async copyGostKlausurenVorgaben(schema : string, abiturjahr : number, halbjahr : number, quartal : number) : Promise<void> {
		const path = "/db/{schema}/gost/klausuren/vorgaben/copyVorlagen/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : -?\\d+}/quartal/{quartal : -?\\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString())
			.replace(/{halbjahr\s*(:[^}]+)?}/g, halbjahr.toString())
			.replace(/{quartal\s*(:[^}]+)?}/g, quartal.toString());
		await super.postJSON(path, null);
		return;
	}


	/**
	 * Implementierung der GET-Methode createDefaultGostKlausurenVorgaben für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/vorgaben/createDefault/halbjahr/{halbjahr : -?\d+}/quartal/{quartal : -?\d+}
	 *
	 * Erstellt eine neue Gost-Klausurvorgabe und gibt sie zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Gost-Klausurvorgabe besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Liste der neuen Klausurvorgaben.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<GostKlausurvorgabe>
	 *   Code 400: Falsche Parameter
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Gost-Klausurvorgabe anzulegen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} halbjahr - der Pfad-Parameter halbjahr
	 * @param {number} quartal - der Pfad-Parameter quartal
	 *
	 * @returns Die Liste der neuen Klausurvorgaben.
	 */
	public async createDefaultGostKlausurenVorgaben(schema : string, halbjahr : number, quartal : number) : Promise<List<GostKlausurvorgabe>> {
		const path = "/db/{schema}/gost/klausuren/vorgaben/createDefault/halbjahr/{halbjahr : -?\\d+}/quartal/{quartal : -?\\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{halbjahr\s*(:[^}]+)?}/g, halbjahr.toString())
			.replace(/{quartal\s*(:[^}]+)?}/g, quartal.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<GostKlausurvorgabe>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(GostKlausurvorgabe.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der DELETE-Methode deleteGostKlausurenVorgabe für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/vorgaben/delete/{id : \d+}
	 *
	 * Löscht eine Gost-Klausurvorgabe.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen einer Gost-Klausurvorgabe besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Klausurvorgabe für die angegebene ID wurden erfolgreich gelöscht.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Gost-Klausurvorgabe zu löschen.
	 *   Code 404: Die Gost-Klausurvorgabe wurde nicht gefunden.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async deleteGostKlausurenVorgabe(schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/gost/klausuren/vorgaben/delete/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		await super.deleteJSON(path, null);
		return;
	}


	/**
	 * Implementierung der POST-Methode createGostKlausurenVorgabe für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/vorgaben/new
	 *
	 * Erstellt eine neue Gost-Klausurvorgabe und gibt sie zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Gost-Klausurvorgabe besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Gost-Klausurvorgabe wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostKlausurvorgabe
	 *   Code 400: Die Daten sind fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Gost-Klausurvorgabe anzulegen.
	 *   Code 409: Die Gost-Klausurvorgabe ist schon in der Datenbank enthalten.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<GostKlausurvorgabe>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Gost-Klausurvorgabe wurde erfolgreich angelegt.
	 */
	public async createGostKlausurenVorgabe(data : Partial<GostKlausurvorgabe>, schema : string) : Promise<GostKlausurvorgabe> {
		const path = "/db/{schema}/gost/klausuren/vorgaben/new"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = GostKlausurvorgabe.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return GostKlausurvorgabe.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode importKurs42Blockung für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/kurs42/import/zip
	 *
	 * Importiert die Kurs 42-Blockung aus dem übergebenen ZIP-File in das Schema mit dem angegebenen Namen.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Import der Kurs 42-Blockung
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Der Benutzer hat keine Berechtigung, um die Kurs 42-Blockung zu importieren.
	 *   Code 409: Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {FormData} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Log vom Import der Kurs 42-Blockung
	 */
	public async importKurs42Blockung(data : FormData, schema : string) : Promise<SimpleOperationResponse> {
		const path = "/db/{schema}/gost/kurs42/import/zip"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.postMultipart(path, data);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getGostLupoExportMDBFuerJahrgang für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/lupo/export/mdb/jahrgang/{jahrgang}
	 *
	 * Exportiert die Laufbahndaten für den übergebenen Jahrgang in eine LuPO-Lehrerdatei.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die LuPO-Lehrerdatei
	 *     - Mime-Type: application/octet-stream
	 *     - Rückgabe-Typ: ApiFile
	 *   Code 403: Der Benutzer hat keine Rechter zum Export der Laufbahndaten.
	 *   Code 500: Ein interner Server-Fehler beim Erzeugen der LuPO-Datei.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {string} jahrgang - der Pfad-Parameter jahrgang
	 *
	 * @returns Die LuPO-Lehrerdatei
	 */
	public async getGostLupoExportMDBFuerJahrgang(schema : string, jahrgang : string) : Promise<ApiFile> {
		const path = "/db/{schema}/gost/lupo/export/mdb/jahrgang/{jahrgang}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{jahrgang\s*(:[^}]+)?}/g, jahrgang);
		const data : ApiFile = await super.getOctetStream(path);
		return data;
	}


	/**
	 * Implementierung der POST-Methode setGostLupoImportMDBFuerJahrgang für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/lupo/import/mdb/jahrgang/replace/{mode}
	 *
	 * Importiert die Laufbahndaten der übergebenen LuPO-Datenbank in das Schema mit dem angegebenen Namen.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Import der Laufbahndaten
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 400: Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Der Benutzer hat keine Berechtigung, um die Laufbahndaten zu importieren.
	 *   Code 409: Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {FormData} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {string} mode - der Pfad-Parameter mode
	 *
	 * @returns Der Log vom Import der Laufbahndaten
	 */
	public async setGostLupoImportMDBFuerJahrgang(data : FormData, schema : string, mode : string) : Promise<SimpleOperationResponse> {
		const path = "/db/{schema}/gost/lupo/import/mdb/jahrgang/replace/{mode}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{mode\s*(:[^}]+)?}/g, mode);
		const result : string = await super.postMultipart(path, data);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getGostSchuelerAbiturdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/schueler/{id : \d+}/abiturdaten
	 *
	 * Liest die Abiturdaten aus den Abiturtabellen des Schülers mit der angegebene ID und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Leistungsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Abiturdaten des Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Abiturdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Leistungsdaten anzusehen.
	 *   Code 404: Kein Schüler-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Abiturdaten des Schülers
	 */
	public async getGostSchuelerAbiturdaten(schema : string, id : number) : Promise<Abiturdaten> {
		const path = "/db/{schema}/gost/schueler/{id : \\d+}/abiturdaten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return Abiturdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getGostSchuelerAbiturdatenAusLeistungsdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/schueler/{id : \d+}/abiturdatenAusLeistungsdaten
	 *
	 * Liest die Abiturdaten aus den Leistungsdaten der gymnasiale Oberstufe des Schülers mit der angegebene ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Leistungsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Abiturdaten des Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Abiturdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Leistungsdaten anzusehen.
	 *   Code 404: Kein Schüler-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Abiturdaten des Schülers
	 */
	public async getGostSchuelerAbiturdatenAusLeistungsdaten(schema : string, id : number) : Promise<Abiturdaten> {
		const path = "/db/{schema}/gost/schueler/{id : \\d+}/abiturdatenAusLeistungsdaten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return Abiturdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getGostSchuelerLaufbahnplanung für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/schueler/{id : \d+}/laufbahnplanung
	 *
	 * Liest die Laufbahnplanungsdaten für die gymnasiale Oberstufe zu dem Schüler mit der angegebenen ID aus der Datenbank aus und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Laufbahnplanungsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Laufbahnplanungsdaten der gymnasialen Oberstufe des angegebenen Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Abiturdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Laufbahnplanungsdaten der Gymnasialen Oberstufe eines Schülers auszulesen.
	 *   Code 404: Kein Eintrag für einen Schüler mit Laufbahnplanungsdaten der gymnasialen Oberstufe für die angegebene ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Laufbahnplanungsdaten der gymnasialen Oberstufe des angegebenen Schülers
	 */
	public async getGostSchuelerLaufbahnplanung(schema : string, id : number) : Promise<Abiturdaten> {
		const path = "/db/{schema}/gost/schueler/{id : \\d+}/laufbahnplanung"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return Abiturdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getGostSchuelerLaufbahnplanungBeratungsdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/schueler/{id : \d+}/laufbahnplanung/beratungsdaten
	 *
	 * Liest die Beratungsdaten für die Laufbahnplanung der gymnasiale Oberstufe zu dem Schüler mit der angegebenen ID aus der Datenbank aus und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Beratungsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Beratungsdaten der gymnasialen Oberstufe des angegebenen Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostLaufbahnplanungBeratungsdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Beratungsdaten der Gymnasialen Oberstufe eines Schülers auszulesen.
	 *   Code 404: Kein Eintrag für einen Schüler mit Beratungsdaten der gymnasialen Oberstufe für die angegebene ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Beratungsdaten der gymnasialen Oberstufe des angegebenen Schülers
	 */
	public async getGostSchuelerLaufbahnplanungBeratungsdaten(schema : string, id : number) : Promise<GostLaufbahnplanungBeratungsdaten> {
		const path = "/db/{schema}/gost/schueler/{id : \\d+}/laufbahnplanung/beratungsdaten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostLaufbahnplanungBeratungsdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode exportGostSchuelerLaufbahnplanungsdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/schueler/{id : \d+}/laufbahnplanung/daten
	 *
	 * Liest die Laufbahnplanungsdaten der gymnasialen Oberstufe für den angegebenen Schüler aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Daten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Laufbahndaten der gymnasialen Oberstufe
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostLaufbahnplanungDaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Laufbahndaten auszulesen.
	 *   Code 404: Es wurden nicht alle benötigten Daten für das Erstellen der Laufbahn-Daten gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Laufbahndaten der gymnasialen Oberstufe
	 */
	public async exportGostSchuelerLaufbahnplanungsdaten(schema : string, id : number) : Promise<GostLaufbahnplanungDaten> {
		const path = "/db/{schema}/gost/schueler/{id : \\d+}/laufbahnplanung/daten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostLaufbahnplanungDaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode importGostSchuelerLaufbahnplanungsdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/schueler/{id : \d+}/laufbahnplanung/daten
	 *
	 * Importiert die Laufbahndaten aus den übergebenen Laufbahnplanungsdaten
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Import der Laufbahndaten
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Der Benutzer hat keine Berechtigung, um die Laufbahndaten zu importieren.
	 *   Code 409: Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {GostLaufbahnplanungDaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Log vom Import der Laufbahndaten
	 */
	public async importGostSchuelerLaufbahnplanungsdaten(data : GostLaufbahnplanungDaten, schema : string, id : number) : Promise<SimpleOperationResponse> {
		const path = "/db/{schema}/gost/schueler/{id : \\d+}/laufbahnplanung/daten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = GostLaufbahnplanungDaten.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode exportGostSchuelerLaufbahnplanung für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/schueler/{id : \d+}/laufbahnplanung/export
	 *
	 * Liest die Laufbahnplanungsdaten der gymnasialen Oberstufe für den angegebenen Schüler aus der Datenbank und liefert diese GZip-komprimiert zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Daten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die GZip-komprimierten Laufbahndaten der gymnasialen Obertufe
	 *     - Mime-Type: application/octet-stream
	 *     - Rückgabe-Typ: ApiFile
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Laufbahndaten auszulesen.
	 *   Code 404: Es wurden nicht alle benötigten Daten für das Erstellen der Laufbahn-Daten gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die GZip-komprimierten Laufbahndaten der gymnasialen Obertufe
	 */
	public async exportGostSchuelerLaufbahnplanung(schema : string, id : number) : Promise<ApiFile> {
		const path = "/db/{schema}/gost/schueler/{id : \\d+}/laufbahnplanung/export"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const data : ApiFile = await super.getOctetStream(path);
		return data;
	}


	/**
	 * Implementierung der POST-Methode importGostSchuelerLaufbahnplanung für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/schueler/{id : \d+}/laufbahnplanung/import
	 *
	 * Importiert die Laufbahndaten aus der übergebenen Laufbahnplanungsdatei
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Import der Laufbahndaten
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Der Benutzer hat keine Berechtigung, um die Laufbahndaten zu importieren.
	 *   Code 409: Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {FormData} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Log vom Import der Laufbahndaten
	 */
	public async importGostSchuelerLaufbahnplanung(data : FormData, schema : string, id : number) : Promise<SimpleOperationResponse> {
		const path = "/db/{schema}/gost/schueler/{id : \\d+}/laufbahnplanung/import"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.postMultipart(path, data);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getGostSchuelerLeistungsdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/schueler/{id : \d+}/leistungsdaten
	 *
	 * Liest die Leistungsdaten in Bezug auf die gymnasiale Oberstufe des Schülers mit der angegebene ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Leistungsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Leistungsdaten des Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostLeistungen
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Leistungsdaten anzusehen.
	 *   Code 404: Kein Schüler-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Leistungsdaten des Schülers
	 */
	public async getGostSchuelerLeistungsdaten(schema : string, id : number) : Promise<GostLeistungen> {
		const path = "/db/{schema}/gost/schueler/{id : \\d+}/leistungsdaten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostLeistungen.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getGostSchuelerFachwahl für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/schueler/{schuelerid : \d+}/fachwahl/{fachid : \d+}
	 *
	 * Liest für die gymnasiale Oberstufe die Fachwahlen zu einem Fach von dem angegebenen Schüler aus. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Fachwahlen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Fachwahlen der gymnasialen Oberstufe für das angegebene Fach und den angegebenen Schüler
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Abiturdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Fachwahlen der Gymnasialen Oberstufe eines Schülers auszulesen.
	 *   Code 404: Kein Eintrag für einen Schüler mit Laufbahnplanungsdaten der gymnasialen Oberstufe für die angegebene ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} schuelerid - der Pfad-Parameter schuelerid
	 * @param {number} fachid - der Pfad-Parameter fachid
	 *
	 * @returns Die Fachwahlen der gymnasialen Oberstufe für das angegebene Fach und den angegebenen Schüler
	 */
	public async getGostSchuelerFachwahl(schema : string, schuelerid : number, fachid : number) : Promise<Abiturdaten> {
		const path = "/db/{schema}/gost/schueler/{schuelerid : \\d+}/fachwahl/{fachid : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{schuelerid\s*(:[^}]+)?}/g, schuelerid.toString())
			.replace(/{fachid\s*(:[^}]+)?}/g, fachid.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return Abiturdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchGostSchuelerFachwahl für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/schueler/{schuelerid : \d+}/fachwahl/{fachid : \d+}
	 *
	 * Passt die Fachwahl eines Schüler in Bezug auf ein Fach der Gymnasiale Oberstufe an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anpassen der Fachwahlen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Fachwahlen integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Fachwahlen zu ändern.
	 *   Code 404: Kein Schüler- oder Fach-Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<GostSchuelerFachwahl>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} schuelerid - der Pfad-Parameter schuelerid
	 * @param {number} fachid - der Pfad-Parameter fachid
	 */
	public async patchGostSchuelerFachwahl(data : Partial<GostSchuelerFachwahl>, schema : string, schuelerid : number, fachid : number) : Promise<void> {
		const path = "/db/{schema}/gost/schueler/{schuelerid : \\d+}/fachwahl/{fachid : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{schuelerid\s*(:[^}]+)?}/g, schuelerid.toString())
			.replace(/{fachid\s*(:[^}]+)?}/g, fachid.toString());
		const body : string = GostSchuelerFachwahl.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der POST-Methode resetGostSchuelerFachwahlen für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/schueler/{schuelerid : \d+}/fachwahl/reset
	 *
	 * Setzt die Fachwahlen des Schülers mit der angegebenen ID zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Zurücksetzen der Fachwahlen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 203: Die Fachwahlen wurden erfolgreich zurückgesetzt.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Fachwahlen zurückzusetzen.
	 *   Code 404: Der Schüler bzw. der zugehörige Abiturjahrgang wurde nicht gefunden.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} schuelerid - der Pfad-Parameter schuelerid
	 */
	public async resetGostSchuelerFachwahlen(schema : string, schuelerid : number) : Promise<void> {
		const path = "/db/{schema}/gost/schueler/{schuelerid : \\d+}/fachwahl/reset"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{schuelerid\s*(:[^}]+)?}/g, schuelerid.toString());
		await super.postJSON(path, null);
		return;
	}


	/**
	 * Implementierung der PATCH-Methode patchGostSchuelerLaufbahnplanungBeratungsdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/schueler/{schuelerid : \d+}/laufbahnplanung/beratungsdaten
	 *
	 * Passt die Beratungsdaten für die Laufbahnplanung der gymnasiale Oberstufe zu dem Schüler mit der angegebenen ID an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anpassen der Beratungsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Beratungsdaten zu ändern.
	 *   Code 404: Kein Schüler-Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<GostLaufbahnplanungBeratungsdaten>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} schuelerid - der Pfad-Parameter schuelerid
	 */
	public async patchGostSchuelerLaufbahnplanungBeratungsdaten(data : Partial<GostLaufbahnplanungBeratungsdaten>, schema : string, schuelerid : number) : Promise<void> {
		const path = "/db/{schema}/gost/schueler/{schuelerid : \\d+}/laufbahnplanung/beratungsdaten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{schuelerid\s*(:[^}]+)?}/g, schuelerid.toString());
		const body : string = GostLaufbahnplanungBeratungsdaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der POST-Methode pdfGostLaufbahnplanungSchuelerErgebnisuebersicht für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/schueler/pdf/laufbahnplanungergebnisuebersicht/{detaillevel : \d+}
	 *
	 * Erstellt eine Ergebnisübersicht der Laufbahnplanung für die Laufbahnplanung der gymnasialen Oberstufe zu den Schülern mit der angegebenen IDs als PDF-Datei. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen des Wahlbogens besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die PDF-Datei mit der Ergebnisübersicht der Laufbahnplanung der gymnasialen Oberstufe.
	 *     - Mime-Type: application/pdf
	 *     - Rückgabe-Typ: ApiFile
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Ergebnisliste Laufbahnplanung für die gymnasialen Oberstufe zu erstellen.
	 *   Code 404: Kein Eintrag zu den angegebenen IDs gefunden.
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} detaillevel - der Pfad-Parameter detaillevel
	 *
	 * @returns Die PDF-Datei mit der Ergebnisübersicht der Laufbahnplanung der gymnasialen Oberstufe.
	 */
	public async pdfGostLaufbahnplanungSchuelerErgebnisuebersicht(data : List<number>, schema : string, detaillevel : number) : Promise<ApiFile> {
		const path = "/db/{schema}/gost/schueler/pdf/laufbahnplanungergebnisuebersicht/{detaillevel : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{detaillevel\s*(:[^}]+)?}/g, detaillevel.toString());
		const body : string = "[" + data.toArray().map(d => JSON.stringify(d)).join() + "]";
		const result : ApiFile = await super.postJSONtoPDF(path, body);
		return result;
	}


	/**
	 * Implementierung der POST-Methode pdfGostLaufbahnplanungSchuelerWahlbogen für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/schueler/pdf/laufbahnplanungwahlbogen
	 *
	 * Erstellt die Wahlbogen für die Laufbahnplanung der gymnasialen Oberstufe zu den Schülern mit der angegebenen IDs als PDF-Datei. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen des Wahlbogens besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die PDF-Datei mit den Wahlbögen zur Laufbahnplanung der gymnasialen Oberstufe.
	 *     - Mime-Type: application/pdf
	 *     - Rückgabe-Typ: ApiFile
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Wahlbogen für die Gymnasialen Oberstufe eines Schülers zu erstellen.
	 *   Code 404: Kein Eintrag zu den angegebenen IDs gefunden.
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die PDF-Datei mit den Wahlbögen zur Laufbahnplanung der gymnasialen Oberstufe.
	 */
	public async pdfGostLaufbahnplanungSchuelerWahlbogen(data : List<number>, schema : string) : Promise<ApiFile> {
		const path = "/db/{schema}/gost/schueler/pdf/laufbahnplanungwahlbogen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = "[" + data.toArray().map(d => JSON.stringify(d)).join() + "]";
		const result : ApiFile = await super.postJSONtoPDF(path, body);
		return result;
	}


	/**
	 * Implementierung der POST-Methode pdfGostLaufbahnplanungSchuelerWahlbogenNurBelegung für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/schueler/pdf/laufbahnplanungwahlbogennurbelegung
	 *
	 * Erstellt die Wahlbogen, reduziert auf die Belegung des Schülers, für die Laufbahnplanung der gymnasialen Oberstufe zu den Schülern mit der angegebenen IDs als PDF-Datei. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen des Wahlbogens besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die PDF-Datei mit den Wahlbögen, reduziert auf die Belegung des Schülers, zur Laufbahnplanung der gymnasialen Oberstufe.
	 *     - Mime-Type: application/pdf
	 *     - Rückgabe-Typ: ApiFile
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die reduzierten Wahlbögen zur Laufbahnplanung der gymnasialen Oberstufe zu erstellen.
	 *   Code 404: Kein Eintrag zu den angegebenen IDs gefunden.
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die PDF-Datei mit den Wahlbögen, reduziert auf die Belegung des Schülers, zur Laufbahnplanung der gymnasialen Oberstufe.
	 */
	public async pdfGostLaufbahnplanungSchuelerWahlbogenNurBelegung(data : List<number>, schema : string) : Promise<ApiFile> {
		const path = "/db/{schema}/gost/schueler/pdf/laufbahnplanungwahlbogennurbelegung"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = "[" + data.toArray().map(d => JSON.stringify(d)).join() + "]";
		const result : ApiFile = await super.postJSONtoPDF(path, body);
		return result;
	}


	/**
	 * Implementierung der GET-Methode getHaltestellen für den Zugriff auf die URL https://{hostname}/db/{schema}/haltestellen
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Haltestellen unter Angabe der ID, eines Kürzels und der textuellen Beschreibung sowie der Information, ob der Eintrag in der Anwendung sichtbar bzw. änderbar sein soll, undgibt diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen zu den Haltestellen.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<KatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen zu den Haltestellen.
	 */
	public async getHaltestellen(schema : string) : Promise<List<KatalogEintrag>> {
		const path = "/db/{schema}/haltestellen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode importSQLite für den Zugriff auf die URL https://{hostname}/db/{schema}/import/sqlite
	 *
	 * Importiert die übergebene Datenbank in dieses Schema. Das Schema wird dabei zunächst geleert und vorhanden Daten gehen dabei verloren.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Importieren der SQLite-Datenbank
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: In das Schema darf nicht importiert werden.
	 *   Code 500: Fehler beim Importieren mit dem Log des fehlgeschlagenen Imports.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {FormData} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Log vom Importieren der SQLite-Datenbank
	 */
	public async importSQLite(data : FormData, schema : string) : Promise<SimpleOperationResponse> {
		const path = "/db/{schema}/import/sqlite"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.postMultipart(path, data);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getJahrgaenge für den Zugriff auf die URL https://{hostname}/db/{schema}/jahrgaenge/
	 *
	 * Erstellt eine Liste aller in der Datenbank vorhanden Jahrgänge unter Angabe der ID, des Kürzels, des verwendeten Statistik-Kürzels, der Bezeichnung des Jahrgangs, die Schulgliederung zu der der Jahrgang gehört, die ID eines Folgejahrgangs, sofern definiert, einer Sortierreihenfolge und ob sie in der Anwendung sichtbar sein sollen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Jahrgangsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Jahrgangs-Listen-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<JahrgangsListeEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Jahrgangsdaten anzusehen.
	 *   Code 404: Keine Jahrgangs-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Jahrgangs-Listen-Einträgen
	 */
	public async getJahrgaenge(schema : string) : Promise<List<JahrgangsListeEintrag>> {
		const path = "/db/{schema}/jahrgaenge/"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<JahrgangsListeEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(JahrgangsListeEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getJahrgang für den Zugriff auf die URL https://{hostname}/db/{schema}/jahrgaenge/{id : \d+}
	 *
	 * Liest die Daten des Jahrgangs zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Jahrgangsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Daten des Jahrgangs
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: JahrgangsDaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Jahrgangsdaten anzusehen.
	 *   Code 404: Kein Jahrgangs-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Daten des Jahrgangs
	 */
	public async getJahrgang(schema : string, id : number) : Promise<JahrgangsDaten> {
		const path = "/db/{schema}/jahrgaenge/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return JahrgangsDaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getKatalogJahrgaenge für den Zugriff auf die URL https://{hostname}/db/{schema}/jahrgaenge/allgemein/jahrgaenge
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden in den einzelnen Schulformen gültigen Jahrgänge. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Jahrgangs-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<JahrgangsKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Jahrgangs-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Jahrgangs-Katalog-Einträgen
	 */
	public async getKatalogJahrgaenge(schema : string) : Promise<List<JahrgangsKatalogEintrag>> {
		const path = "/db/{schema}/jahrgaenge/allgemein/jahrgaenge"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<JahrgangsKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(JahrgangsKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogKAoAAnschlussoptionen für den Zugriff auf die URL https://{hostname}/db/{schema}/kaoa/allgemein/anschlussoptionen
	 *
	 * Die Liste der Einträge aus dem KAoA-Katalog Anschlussoptionen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen für den KAoA-Katalog Anschlussoptionen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<KAOAAnschlussoptionEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen für den KAoA-Katalog Anschlussoptionen
	 */
	public async getKatalogKAoAAnschlussoptionen(schema : string) : Promise<List<KAOAAnschlussoptionEintrag>> {
		const path = "/db/{schema}/kaoa/allgemein/anschlussoptionen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KAOAAnschlussoptionEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KAOAAnschlussoptionEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogKAoABerufsfelder für den Zugriff auf die URL https://{hostname}/db/{schema}/kaoa/allgemein/berufsfelder
	 *
	 * Die Liste der Einträge aus dem KAoA-Katalog Berufsfelder. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen für den KAoA-Katalog Berufsfelder
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<KAOABerufsfeldEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen für den KAoA-Katalog Berufsfelder
	 */
	public async getKatalogKAoABerufsfelder(schema : string) : Promise<List<KAOABerufsfeldEintrag>> {
		const path = "/db/{schema}/kaoa/allgemein/berufsfelder"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KAOABerufsfeldEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KAOABerufsfeldEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogKAoAEbene4 für den Zugriff auf die URL https://{hostname}/db/{schema}/kaoa/allgemein/ebene4
	 *
	 * Die Liste der Einträge aus dem KAoA-Katalog Einträge der SBO Ebene 4. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen für den KAoA-Katalog Einträge der SBO Ebene 4
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<KAOAEbene4Eintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen für den KAoA-Katalog Einträge der SBO Ebene 4
	 */
	public async getKatalogKAoAEbene4(schema : string) : Promise<List<KAOAEbene4Eintrag>> {
		const path = "/db/{schema}/kaoa/allgemein/ebene4"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KAOAEbene4Eintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KAOAEbene4Eintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogKAoAKategorien für den Zugriff auf die URL https://{hostname}/db/{schema}/kaoa/allgemein/kategorien
	 *
	 * Die Liste der Einträge aus dem KAoA-Katalog Kategorien. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen für den KAoA-Katalog Kategorien
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<KAOAKategorieEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen für den KAoA-Katalog Kategorien
	 */
	public async getKatalogKAoAKategorien(schema : string) : Promise<List<KAOAKategorieEintrag>> {
		const path = "/db/{schema}/kaoa/allgemein/kategorien"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KAOAKategorieEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KAOAKategorieEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogKAoAMerkmale für den Zugriff auf die URL https://{hostname}/db/{schema}/kaoa/allgemein/merkmale
	 *
	 * Die Liste der Einträge aus dem KAoA-Katalog Merkmale. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen für den KAoA-Katalog Merkmale
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<KAOAMerkmalEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen für den KAoA-Katalog Merkmale
	 */
	public async getKatalogKAoAMerkmale(schema : string) : Promise<List<KAOAMerkmalEintrag>> {
		const path = "/db/{schema}/kaoa/allgemein/merkmale"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KAOAMerkmalEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KAOAMerkmalEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogKAoAZusatzmerkmale für den Zugriff auf die URL https://{hostname}/db/{schema}/kaoa/allgemein/zusatzmerkmale
	 *
	 * Die Liste der Einträge aus dem KAoA-Katalog Zusatzmerkmale. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen für den KAoA-Katalog Zusatzmerkmale
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<KAOAZusatzmerkmalEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen für den KAoA-Katalog Zusatzmerkmale
	 */
	public async getKatalogKAoAZusatzmerkmale(schema : string) : Promise<List<KAOAZusatzmerkmalEintrag>> {
		const path = "/db/{schema}/kaoa/allgemein/zusatzmerkmale"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KAOAZusatzmerkmalEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KAOAZusatzmerkmalEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKlasse für den Zugriff auf die URL https://{hostname}/db/{schema}/klassen/{id : \d+}
	 *
	 * Liest die Daten der Klasse zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Klassendaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Daten der Klasse
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: KlassenDaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Klassendaten anzusehen.
	 *   Code 404: Kein Klassen-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Daten der Klasse
	 */
	public async getKlasse(schema : string, id : number) : Promise<KlassenDaten> {
		const path = "/db/{schema}/klassen/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return KlassenDaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getKlassenFuerAbschnitt für den Zugriff auf die URL https://{hostname}/db/{schema}/klassen/abschnitt/{abschnitt : \d+}
	 *
	 * Erstellt eine Liste aller in der Datenbank vorhanden Klassen unter Angabe der ID, des Kürzels, der Parallelität, der Kürzel des Klassenlehrers und des zweiten Klassenlehrers, einer Sortierreihenfolge und ob sie in der Anwendung sichtbar sein sollen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Klassendaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Klassen-Listen-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<KlassenListeEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Klassendaten anzusehen.
	 *   Code 404: Keine Klassen-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abschnitt - der Pfad-Parameter abschnitt
	 *
	 * @returns Eine Liste von Klassen-Listen-Einträgen
	 */
	public async getKlassenFuerAbschnitt(schema : string, abschnitt : number) : Promise<List<KlassenListeEintrag>> {
		const path = "/db/{schema}/klassen/abschnitt/{abschnitt : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abschnitt\s*(:[^}]+)?}/g, abschnitt.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KlassenListeEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KlassenListeEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogKlassenarten für den Zugriff auf die URL https://{hostname}/db/{schema}/klassen/allgemein/klassenarten
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden gültigen Klassenarten. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Klassenart-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<KlassenartKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Klassenart-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Klassenart-Katalog-Einträgen
	 */
	public async getKatalogKlassenarten(schema : string) : Promise<List<KlassenartKatalogEintrag>> {
		const path = "/db/{schema}/klassen/allgemein/klassenarten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KlassenartKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KlassenartKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKurse für den Zugriff auf die URL https://{hostname}/db/{schema}/kurse/
	 *
	 * Erstellt eine Liste aller in der Datenbank vorhanden Kurse unter Angabe der ID, des Kürzels, der Parallelität, der Kürzel des Klassenlehrers und des zweiten Klassenlehrers, einer Sortierreihenfolge und ob sie in der Anwendung sichtbar sein sollen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Klassendaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Kurs-Listen-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<KursListeEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Kursdaten anzusehen.
	 *   Code 404: Keine Kurs-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Kurs-Listen-Einträgen
	 */
	public async getKurse(schema : string) : Promise<List<KursListeEintrag>> {
		const path = "/db/{schema}/kurse/"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KursListeEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KursListeEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKurs für den Zugriff auf die URL https://{hostname}/db/{schema}/kurse/{id : \d+}
	 *
	 * Liest die Daten des Kurses zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Kursdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Daten des Kurses
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: KursDaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Kursdaten anzusehen.
	 *   Code 404: Kein Kurs-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Daten des Kurses
	 */
	public async getKurs(schema : string, id : number) : Promise<KursDaten> {
		const path = "/db/{schema}/kurse/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return KursDaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getKurseFuerAbschnitt für den Zugriff auf die URL https://{hostname}/db/{schema}/kurse/abschnitt/{abschnitt : \d+}
	 *
	 * Erstellt eine Liste aller in der Datenbank vorhanden Kurse eines Schuljahresabschnittes unter Angabe der ID, des Kürzels, der Parallelität, der Kürzel des Klassenlehrers und des zweiten Klassenlehrers, einer Sortierreihenfolge und ob sie in der Anwendung sichtbar sein sollen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Klassendaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Kurs-Listen-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<KursListeEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Kursdaten anzusehen.
	 *   Code 404: Keine Kurs-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abschnitt - der Pfad-Parameter abschnitt
	 *
	 * @returns Eine Liste von Kurs-Listen-Einträgen
	 */
	public async getKurseFuerAbschnitt(schema : string, abschnitt : number) : Promise<List<KursListeEintrag>> {
		const path = "/db/{schema}/kurse/abschnitt/{abschnitt : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abschnitt\s*(:[^}]+)?}/g, abschnitt.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KursListeEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KursListeEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogKursarten für den Zugriff auf die URL https://{hostname}/db/{schema}/kurse/allgemein/kursarten
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden gültigen Kursarten. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Kursart-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<KursartKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Kursart-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Kursart-Katalog-Einträgen
	 */
	public async getKatalogKursarten(schema : string) : Promise<List<KursartKatalogEintrag>> {
		const path = "/db/{schema}/kurse/allgemein/kursarten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KursartKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KursartKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLehrer für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/
	 *
	 * Erstellt eine Liste aller in der Datenbank vorhanden Lehrer unter Angabe der ID, des Kürzels, des Vor- und Nachnamens, der sog. Personentyps, einer Sortierreihenfolge, ob sie in der Anwendung sichtbar bzw. änderbar sein sollen sowie ob sie für die Schulstatistik relevant sein sollen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Lehrer-Listen-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<LehrerListeEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten anzusehen.
	 *   Code 404: Keine Lehrer-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Lehrer-Listen-Einträgen
	 */
	public async getLehrer(schema : string) : Promise<List<LehrerListeEintrag>> {
		const path = "/db/{schema}/lehrer/"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerListeEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerListeEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLehrerPersonaldaten für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/{id : \d+}/personaldaten
	 *
	 * Liest die Personaldaten des Lehrers zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerpersonaldaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Personaldaten des Lehrers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: LehrerPersonaldaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten anzusehen.
	 *   Code 404: Kein Lehrer-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Personaldaten des Lehrers
	 */
	public async getLehrerPersonaldaten(schema : string, id : number) : Promise<LehrerPersonaldaten> {
		const path = "/db/{schema}/lehrer/{id : \\d+}/personaldaten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return LehrerPersonaldaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchLehrerPersonaldaten für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/{id : \d+}/personaldaten
	 *
	 * Passt die Lehrer-Personaldaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrer-Personaldaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Lehrer-Personaldaten integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Lehrer-Personaldaten zu ändern.
	 *   Code 404: Kein Lehrer-Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<LehrerPersonaldaten>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchLehrerPersonaldaten(data : Partial<LehrerPersonaldaten>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/lehrer/{id : \\d+}/personaldaten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = LehrerPersonaldaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getLehrerStammdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/{id : \d+}/stammdaten
	 *
	 * Liest die Stammdaten des Lehrers zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Stammdaten des Lehrers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: LehrerStammdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten anzusehen.
	 *   Code 404: Kein Lehrer-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Stammdaten des Lehrers
	 */
	public async getLehrerStammdaten(schema : string, id : number) : Promise<LehrerStammdaten> {
		const path = "/db/{schema}/lehrer/{id : \\d+}/stammdaten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return LehrerStammdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchLehrerStammdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/{id : \d+}/stammdaten
	 *
	 * Passt die Lehrer-Stammdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Lehrer-Stammdaten integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Lehrerdaten zu ändern.
	 *   Code 404: Kein Lehrer-Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<LehrerStammdaten>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchLehrerStammdaten(data : Partial<LehrerStammdaten>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/lehrer/{id : \\d+}/stammdaten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = LehrerStammdaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getLehrerAbgangsgruende für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/allgemein/abgangsgruende
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Lehrerabgangsgründe unter Angabe der ID, der Bezeichnung und des Statistikschlüssels. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Lehrerabgangsgrund-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<LehrerKatalogAbgangsgrundEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Lehrerabgangsgrund-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Lehrerabgangsgrund-Katalog-Einträgen
	 */
	public async getLehrerAbgangsgruende(schema : string) : Promise<List<LehrerKatalogAbgangsgrundEintrag>> {
		const path = "/db/{schema}/lehrer/allgemein/abgangsgruende"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerKatalogAbgangsgrundEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerKatalogAbgangsgrundEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLehrerAnrechnungsgruende für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/allgemein/anrechnungsgruende
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Gründe für Anrechnungsstunden von Lehrern.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Einträgen für Gründe von Anrechnungsstunden von Lehrern
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<LehrerKatalogAnrechnungsgrundEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Einträgen für Gründe von Anrechnungsstunden von Lehrern
	 */
	public async getLehrerAnrechnungsgruende(schema : string) : Promise<List<LehrerKatalogAnrechnungsgrundEintrag>> {
		const path = "/db/{schema}/lehrer/allgemein/anrechnungsgruende"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerKatalogAnrechnungsgrundEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerKatalogAnrechnungsgrundEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLehrerBeschaeftigungsarten für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/allgemein/beschaeftigungsarten
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Beschäftigungsarten unter Angabe der ID, eines Kürzels und der Bezeichnung. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Beschäftigungsart-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<LehrerKatalogBeschaeftigungsartEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Beschäftigungsart-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Beschäftigungsart-Katalog-Einträgen
	 */
	public async getLehrerBeschaeftigungsarten(schema : string) : Promise<List<LehrerKatalogBeschaeftigungsartEintrag>> {
		const path = "/db/{schema}/lehrer/allgemein/beschaeftigungsarten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerKatalogBeschaeftigungsartEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerKatalogBeschaeftigungsartEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLehrerEinsatzstatus für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/allgemein/einsatzstatus
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Einsatzstatusarten unter Angabe der ID, eines Kürzels und der Bezeichnung. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Einsatzstatus-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<LehrerKatalogEinsatzstatusEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Einsatzstatus-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Einsatzstatus-Katalog-Einträgen
	 */
	public async getLehrerEinsatzstatus(schema : string) : Promise<List<LehrerKatalogEinsatzstatusEintrag>> {
		const path = "/db/{schema}/lehrer/allgemein/einsatzstatus"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerKatalogEinsatzstatusEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerKatalogEinsatzstatusEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLehrerFachrichtungen für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/allgemein/fachrichtungen
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Fachrichtungen von Lehrern. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Fachrichtungens-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<LehrerKatalogFachrichtungEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Fachrichtungs-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Fachrichtungens-Katalog-Einträgen
	 */
	public async getLehrerFachrichtungen(schema : string) : Promise<List<LehrerKatalogFachrichtungEintrag>> {
		const path = "/db/{schema}/lehrer/allgemein/fachrichtungen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerKatalogFachrichtungEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerKatalogFachrichtungEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLehrerFachrichtungAnerkennungen für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/allgemein/fachrichtungen_anerkennungen
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Anerkennungen von Fachrichtungen für Lehrer. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Anerkennungs-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<LehrerKatalogFachrichtungAnerkennungEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Anerkennungs-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Anerkennungs-Katalog-Einträgen
	 */
	public async getLehrerFachrichtungAnerkennungen(schema : string) : Promise<List<LehrerKatalogFachrichtungAnerkennungEintrag>> {
		const path = "/db/{schema}/lehrer/allgemein/fachrichtungen_anerkennungen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerKatalogFachrichtungAnerkennungEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerKatalogFachrichtungAnerkennungEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLehrerLehraemter für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/allgemein/lehraemter
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Lehrämter. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Lehramt-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<LehrerKatalogLehramtEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Lehramt-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Lehramt-Katalog-Einträgen
	 */
	public async getLehrerLehraemter(schema : string) : Promise<List<LehrerKatalogLehramtEintrag>> {
		const path = "/db/{schema}/lehrer/allgemein/lehraemter"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerKatalogLehramtEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerKatalogLehramtEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLehrerLehramtAnerkennungen für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/allgemein/lehraemter_anerkennungen
	 *
	 * Erstellt eine Liste aller Anerkennungen von Lehrämtern. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Anerkennungs-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<LehrerKatalogLehramtAnerkennungEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Anerkennungs-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Anerkennungs-Katalog-Einträgen
	 */
	public async getLehrerLehramtAnerkennungen(schema : string) : Promise<List<LehrerKatalogLehramtAnerkennungEintrag>> {
		const path = "/db/{schema}/lehrer/allgemein/lehraemter_anerkennungen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerKatalogLehramtAnerkennungEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerKatalogLehramtAnerkennungEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLehrerLehrbefaehigungen für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/allgemein/lehrbefaehigungen
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Lehrbefähigungen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Lehrbefähigung-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<LehrerKatalogLehrbefaehigungEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Lehrbefähigung-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Lehrbefähigung-Katalog-Einträgen
	 */
	public async getLehrerLehrbefaehigungen(schema : string) : Promise<List<LehrerKatalogLehrbefaehigungEintrag>> {
		const path = "/db/{schema}/lehrer/allgemein/lehrbefaehigungen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerKatalogLehrbefaehigungEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerKatalogLehrbefaehigungEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLehrerLehrbefaehigungenAnerkennungen für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/allgemein/lehrbefaehigungen_anerkennungen
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Anerkennungen von Lehrbefähigungen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Einsatzstatus-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<LehrerKatalogLehrbefaehigungAnerkennungEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Anerkennungs-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Einsatzstatus-Katalog-Einträgen
	 */
	public async getLehrerLehrbefaehigungenAnerkennungen(schema : string) : Promise<List<LehrerKatalogLehrbefaehigungAnerkennungEintrag>> {
		const path = "/db/{schema}/lehrer/allgemein/lehrbefaehigungen_anerkennungen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerKatalogLehrbefaehigungAnerkennungEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerKatalogLehrbefaehigungAnerkennungEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLehrerMehrleistungsarten für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/allgemein/mehrleistungsarten
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden die Arten von Mehrleistungen durch Lehrer. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Mehrleistungsart-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<LehrerKatalogMehrleistungsartEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Mehrleistungsart-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Mehrleistungsart-Katalog-Einträgen
	 */
	public async getLehrerMehrleistungsarten(schema : string) : Promise<List<LehrerKatalogMehrleistungsartEintrag>> {
		const path = "/db/{schema}/lehrer/allgemein/mehrleistungsarten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerKatalogMehrleistungsartEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerKatalogMehrleistungsartEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLehrerMinderleistungsarten für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/allgemein/minderleistungsarten
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Arten von Minderleistungen durch Lehrer. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Minderleistungsart-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<LehrerKatalogMinderleistungsartEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Minderleistungsart-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Minderleistungsart-Katalog-Einträgen
	 */
	public async getLehrerMinderleistungsarten(schema : string) : Promise<List<LehrerKatalogMinderleistungsartEintrag>> {
		const path = "/db/{schema}/lehrer/allgemein/minderleistungsarten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerKatalogMinderleistungsartEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerKatalogMinderleistungsartEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLehrerRechtsverhaeltnisse für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/allgemein/rechtsverhaeltnisse
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Rechtsverhältnisse unter Angabe der ID, eines Kürzels und der Bezeichnung. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Rechtsverhältnis-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<LehrerKatalogRechtsverhaeltnisEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Rechtsverhältnis-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Rechtsverhältnis-Katalog-Einträgen
	 */
	public async getLehrerRechtsverhaeltnisse(schema : string) : Promise<List<LehrerKatalogRechtsverhaeltnisEintrag>> {
		const path = "/db/{schema}/lehrer/allgemein/rechtsverhaeltnisse"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerKatalogRechtsverhaeltnisEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerKatalogRechtsverhaeltnisEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLehrerZugangsgruende für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/allgemein/zugangsgruende
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Lehrerzugangsgründe unter Angabe der ID, der Bezeichnung und des Statistikschlüssels. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Lehrerzugangsgrund-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<LehrerKatalogZugangsgrundEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Lehrerzugangsgrund-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Lehrerzugangsgrund-Katalog-Einträgen
	 */
	public async getLehrerZugangsgruende(schema : string) : Promise<List<LehrerKatalogZugangsgrundEintrag>> {
		const path = "/db/{schema}/lehrer/allgemein/zugangsgruende"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerKatalogZugangsgrundEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerKatalogZugangsgrundEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLehrerLeitungsfunktionen für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/leitungsfunktionen
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Lehrerleitungsfunktionen unter Angabe der ID und der Bezeichnung. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Lehrerleitungsfunktion-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<LehrerKatalogLeitungsfunktionenEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Lehrerleitungsfunktion-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Lehrerleitungsfunktion-Katalog-Einträgen
	 */
	public async getLehrerLeitungsfunktionen(schema : string) : Promise<List<LehrerKatalogLeitungsfunktionenEintrag>> {
		const path = "/db/{schema}/lehrer/leitungsfunktionen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerKatalogLeitungsfunktionenEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerKatalogLeitungsfunktionenEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLehrerPersonalabschnittsdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/personalabschnittsdaten/{id : \d+}
	 *
	 * Liest die Personalabschnittsdaten zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerpersonaldaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Personalabschnittsdaten
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: LehrerPersonalabschnittsdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Lehrer-Personaldaten anzusehen.
	 *   Code 404: Keine Lehrer-Personalabschnittsdaten mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Personalabschnittsdaten
	 */
	public async getLehrerPersonalabschnittsdaten(schema : string, id : number) : Promise<LehrerPersonalabschnittsdaten> {
		const path = "/db/{schema}/lehrer/personalabschnittsdaten/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return LehrerPersonalabschnittsdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchLehrerPersonalabschnittsdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/personalabschnittsdaten/{id : \d+}
	 *
	 * Passt die Lehrer-Personalabschnittsdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrer-Personalabschnittsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Lehrer-Personalabschnittsdaten integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Lehrer-Personaldaten zu ändern.
	 *   Code 404: Kein Lehrer-Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<LehrerPersonalabschnittsdaten>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchLehrerPersonalabschnittsdaten(data : Partial<LehrerPersonalabschnittsdaten>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/lehrer/personalabschnittsdaten/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = LehrerPersonalabschnittsdaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der POST-Methode migrateMariaDB für den Zugriff auf die URL https://{hostname}/db/{schema}/migrate/mariadb
	 *
	 * Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen. Die Daten in diesem Schema werden ersetzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Migrieren der MariaDB-Datenbank
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Das Schema darf nicht migriert werden.
	 *   Code 500: Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {DatenbankVerbindungsdaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Log vom Migrieren der MariaDB-Datenbank
	 */
	public async migrateMariaDB(data : DatenbankVerbindungsdaten, schema : string) : Promise<SimpleOperationResponse> {
		const path = "/db/{schema}/migrate/mariadb"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = DatenbankVerbindungsdaten.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode migrateMariaDBSchulnummer für den Zugriff auf die URL https://{hostname}/db/{schema}/migrate/mariadb/{schulnummer:\d{6}}
	 *
	 * Migriert die Daten für die übergebene Schulnummer aus der übergebenen Datenbank in das Schema mit dem angegebenen Namen. Die Daten in diesem Schema werden ersetzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Migrieren der MariaDB-Datenbank
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Das Schema darf nicht migriert werden.
	 *   Code 500: Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {DatenbankVerbindungsdaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} schulnummer - der Pfad-Parameter schulnummer
	 *
	 * @returns Der Log vom Migrieren der MariaDB-Datenbank
	 */
	public async migrateMariaDBSchulnummer(data : DatenbankVerbindungsdaten, schema : string, schulnummer : number) : Promise<SimpleOperationResponse> {
		const path = "/db/{schema}/migrate/mariadb/{schulnummer:\\d{6}}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{schulnummer\s*(:[^}]+)?}/g, schulnummer.toString());
		const body : string = DatenbankVerbindungsdaten.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode migrateMDB für den Zugriff auf die URL https://{hostname}/db/{schema}/migrate/mdb
	 *
	 * Migriert die übergebene Datenbank in dieses Schema. Das Schema wird dabei geleert und vorhanden Daten gehen dabei verloren.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Migrieren der Access-MDB-Datenbank
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Das Schema darf nicht migriert werden.
	 *   Code 500: Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {FormData} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Log vom Migrieren der Access-MDB-Datenbank
	 */
	public async migrateMDB(data : FormData, schema : string) : Promise<SimpleOperationResponse> {
		const path = "/db/{schema}/migrate/mdb"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.postMultipart(path, data);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode migrateMsSqlServer für den Zugriff auf die URL https://{hostname}/db/{schema}/migrate/mssql
	 *
	 * Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen. Die Daten in diesem Schema werden ersetzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Migrieren der SQL-Server-Datenbank
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Das Schema darf nicht migriert werden.
	 *   Code 500: Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {DatenbankVerbindungsdaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Log vom Migrieren der SQL-Server-Datenbank
	 */
	public async migrateMsSqlServer(data : DatenbankVerbindungsdaten, schema : string) : Promise<SimpleOperationResponse> {
		const path = "/db/{schema}/migrate/mssql"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = DatenbankVerbindungsdaten.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode migrateMsSqlServerSchulnummer für den Zugriff auf die URL https://{hostname}/db/{schema}/migrate/mssql/{schulnummer:\d{6}}
	 *
	 * Migriert die Daten für die übergebene Schulnummer aus der übergebenen Datenbank in das Schema mit dem angegebenen Namen. Die Daten in diesem Schema werden ersetzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Migrieren der SQL-Server-Datenbank
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Das Schema darf nicht migriert werden.
	 *   Code 500: Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {DatenbankVerbindungsdaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} schulnummer - der Pfad-Parameter schulnummer
	 *
	 * @returns Der Log vom Migrieren der SQL-Server-Datenbank
	 */
	public async migrateMsSqlServerSchulnummer(data : DatenbankVerbindungsdaten, schema : string, schulnummer : number) : Promise<SimpleOperationResponse> {
		const path = "/db/{schema}/migrate/mssql/{schulnummer:\\d{6}}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{schulnummer\s*(:[^}]+)?}/g, schulnummer.toString());
		const body : string = DatenbankVerbindungsdaten.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode migrateMySql für den Zugriff auf die URL https://{hostname}/db/{schema}/migrate/mysql
	 *
	 * Migriert die übergebene Datenbank in das Schema mit dem angegebenen Namen. Die Daten in diesem Schema werden ersetzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Migrieren der MySQL-Datenbank
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Das Schema darf nicht migriert werden.
	 *   Code 500: Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {DatenbankVerbindungsdaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Log vom Migrieren der MySQL-Datenbank
	 */
	public async migrateMySql(data : DatenbankVerbindungsdaten, schema : string) : Promise<SimpleOperationResponse> {
		const path = "/db/{schema}/migrate/mysql"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = DatenbankVerbindungsdaten.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode migrateMySqlSchulnummer für den Zugriff auf die URL https://{hostname}/db/{schema}/migrate/mysql/{schulnummer:\d{6}}
	 *
	 * Migriert die Daten für die übergebene Schulnummer aus der übergebenen Datenbank in das Schema mit dem angegebenen Namen. Die Daten in diesem Schema werden ersetzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Migrieren der MySQL-Datenbank
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Das Schema darf nicht migriert werden.
	 *   Code 500: Fehler bei der Migration mit dem Log der fehlgeschlagenen Migration.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {DatenbankVerbindungsdaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} schulnummer - der Pfad-Parameter schulnummer
	 *
	 * @returns Der Log vom Migrieren der MySQL-Datenbank
	 */
	public async migrateMySqlSchulnummer(data : DatenbankVerbindungsdaten, schema : string, schulnummer : number) : Promise<SimpleOperationResponse> {
		const path = "/db/{schema}/migrate/mysql/{schulnummer:\\d{6}}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{schulnummer\s*(:[^}]+)?}/g, schulnummer.toString());
		const body : string = DatenbankVerbindungsdaten.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getOrte für den Zugriff auf die URL https://{hostname}/db/{schema}/orte
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Orte unter Angabe der ID, der PLZ, des Ortes, ggf. des Kreises, dem Bundesland, einer Sortierreihenfolge und ob sie in der Anwendung sichtbar bzw. änderbar sein sollen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Orts-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<OrtKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Ort-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Orts-Katalog-Einträgen
	 */
	public async getOrte(schema : string) : Promise<List<OrtKatalogEintrag>> {
		const path = "/db/{schema}/orte"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<OrtKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(OrtKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getOrtsteile für den Zugriff auf die URL https://{hostname}/db/{schema}/ortsteile
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Ortsteile unter Angabe der ID, der zugehörigenOrt-ID, dem Namen des Ortsteils, einer Sortierreihenfolge und ob sie in der Anwendung sichtbar bzw. änderbar sein sollen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Ortsteil-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<OrtsteilKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Ortsteil-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Ortsteil-Katalog-Einträgen
	 */
	public async getOrtsteile(schema : string) : Promise<List<OrtsteilKatalogEintrag>> {
		const path = "/db/{schema}/ortsteile"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<OrtsteilKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(OrtsteilKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogSchild3AbiturInfos für den Zugriff auf die URL https://{hostname}/db/{schema}/schild3/abiturinfos
	 *
	 * Die Liste der Einträge aus dem Schild-Katalog AbiturInfos. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen für den Schild-Katalog AbiturInfos
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Schild3KatalogEintragAbiturInfos>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen für den Schild-Katalog AbiturInfos
	 */
	public async getKatalogSchild3AbiturInfos(schema : string) : Promise<List<Schild3KatalogEintragAbiturInfos>> {
		const path = "/db/{schema}/schild3/abiturinfos"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<Schild3KatalogEintragAbiturInfos>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(Schild3KatalogEintragAbiturInfos.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogSchild3Datenarten für den Zugriff auf die URL https://{hostname}/db/{schema}/schild3/datenarten
	 *
	 * Die Liste der Einträge aus dem Schild-Katalog Datenart. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen für den Schild-Katalog Datenart
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Schild3KatalogEintragDatenart>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen für den Schild-Katalog Datenart
	 */
	public async getKatalogSchild3Datenarten(schema : string) : Promise<List<Schild3KatalogEintragDatenart>> {
		const path = "/db/{schema}/schild3/datenarten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<Schild3KatalogEintragDatenart>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(Schild3KatalogEintragDatenart.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogSchild3DQRNiveaus für den Zugriff auf die URL https://{hostname}/db/{schema}/schild3/dqr_niveaus
	 *
	 * Die Liste der Einträge aus dem Schild-Katalog DQR-Niveaus. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen für den Schild-Katalog DQR-Niveaus
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Schild3KatalogEintragDQRNiveaus>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen für den Schild-Katalog DQR-Niveaus
	 */
	public async getKatalogSchild3DQRNiveaus(schema : string) : Promise<List<Schild3KatalogEintragDQRNiveaus>> {
		const path = "/db/{schema}/schild3/dqr_niveaus"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<Schild3KatalogEintragDQRNiveaus>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(Schild3KatalogEintragDQRNiveaus.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogSchild3ExportCSV für den Zugriff auf die URL https://{hostname}/db/{schema}/schild3/export/csv
	 *
	 * Die Liste der Einträge aus dem Schild-Katalog zur Konfiguration des CSV-Exportes von Schild. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen für den Schild-Katalog zur Konfiguration des CSV-Exportes von Schild
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Schild3KatalogEintragExportCSV>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen für den Schild-Katalog zur Konfiguration des CSV-Exportes von Schild
	 */
	public async getKatalogSchild3ExportCSV(schema : string) : Promise<List<Schild3KatalogEintragExportCSV>> {
		const path = "/db/{schema}/schild3/export/csv"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<Schild3KatalogEintragExportCSV>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(Schild3KatalogEintragExportCSV.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogSchild3FilterFehlendeEintraege für den Zugriff auf die URL https://{hostname}/db/{schema}/schild3/filter/fehlende_eintraege
	 *
	 * Die Liste der Einträge aus dem Schild-Katalog Filter Fehlende Einträge. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen für den Schild-Katalog Filter Fehlende Einträge
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Schild3KatalogEintragFilterFehlendeEintraege>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen für den Schild-Katalog Filter Fehlende Einträge
	 */
	public async getKatalogSchild3FilterFehlendeEintraege(schema : string) : Promise<List<Schild3KatalogEintragFilterFehlendeEintraege>> {
		const path = "/db/{schema}/schild3/filter/fehlende_eintraege"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<Schild3KatalogEintragFilterFehlendeEintraege>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(Schild3KatalogEintragFilterFehlendeEintraege.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogSchild3Laender für den Zugriff auf die URL https://{hostname}/db/{schema}/schild3/laender
	 *
	 * Die Liste der Einträge aus dem Schild-Katalog Bundesländer/Nachbarländer. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen für den Schild-Katalog Bundesländer/Nachbarländer
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Schild3KatalogEintragLaender>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen für den Schild-Katalog Bundesländer/Nachbarländer
	 */
	public async getKatalogSchild3Laender(schema : string) : Promise<List<Schild3KatalogEintragLaender>> {
		const path = "/db/{schema}/schild3/laender"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<Schild3KatalogEintragLaender>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(Schild3KatalogEintragLaender.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogSchild3Pruefungsordnungen für den Zugriff auf die URL https://{hostname}/db/{schema}/schild3/pruefungsordnungen
	 *
	 * Die Liste der Einträge aus dem Schild-Katalog Prüfungsordnungen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen für den Schild-Katalog Prüfungsordnungen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Schild3KatalogEintragPruefungsordnung>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen für den Schild-Katalog Prüfungsordnungen
	 */
	public async getKatalogSchild3Pruefungsordnungen(schema : string) : Promise<List<Schild3KatalogEintragPruefungsordnung>> {
		const path = "/db/{schema}/schild3/pruefungsordnungen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<Schild3KatalogEintragPruefungsordnung>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(Schild3KatalogEintragPruefungsordnung.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogSchild3PruefungsordnungOptionen für den Zugriff auf die URL https://{hostname}/db/{schema}/schild3/pruefungsordnungen/optionen
	 *
	 * Die Liste der Einträge aus dem Schild-Katalog Prüfungsordnung-Optionen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen für den Schild-Katalog Prüfungsordnung-Optionen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Schild3KatalogEintragPruefungsordnungOption>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen für den Schild-Katalog Prüfungsordnung-Optionen
	 */
	public async getKatalogSchild3PruefungsordnungOptionen(schema : string) : Promise<List<Schild3KatalogEintragPruefungsordnungOption>> {
		const path = "/db/{schema}/schild3/pruefungsordnungen/optionen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<Schild3KatalogEintragPruefungsordnungOption>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(Schild3KatalogEintragPruefungsordnungOption.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getSchild3ReportingDatenquellen für den Zugriff auf die URL https://{hostname}/db/{schema}/schild3/reporting/
	 *
	 * Die Liste der Einträge im SVWS-Server definierten Schild3-Datenquellen für das Reporting. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung für den Zugriff auf das Reporting besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen der Einträge im SVWS-Server definierten Schild3-Datenquellen für das Reporting
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Schild3KatalogEintragVersetzungsvermerke>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um auf das Reporting zuzigreifen.
	 *   Code 404: Keine Datenquellen gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen der Einträge im SVWS-Server definierten Schild3-Datenquellen für das Reporting
	 */
	public async getSchild3ReportingDatenquellen(schema : string) : Promise<List<Schild3KatalogEintragVersetzungsvermerke>> {
		const path = "/db/{schema}/schild3/reporting/"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<Schild3KatalogEintragVersetzungsvermerke>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(Schild3KatalogEintragVersetzungsvermerke.transpilerFromJSON(text)); });
		return ret;
	}


	// API-Methode getSchild3ReportingDaten konnte nicht nach Typescript transpiliert werden


	/**
	 * Implementierung der GET-Methode getKatalogSchild3UnicodeUmwandlung für den Zugriff auf die URL https://{hostname}/db/{schema}/schild3/unicode/umwandlung
	 *
	 * Die Liste der Einträge aus dem Schild-Katalog für die Unicode-Umwandlung. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen für den Schild-Katalog für die Unicode-Umwandlung
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Schild3KatalogEintragUnicodeUmwandlung>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen für den Schild-Katalog für die Unicode-Umwandlung
	 */
	public async getKatalogSchild3UnicodeUmwandlung(schema : string) : Promise<List<Schild3KatalogEintragUnicodeUmwandlung>> {
		const path = "/db/{schema}/schild3/unicode/umwandlung"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<Schild3KatalogEintragUnicodeUmwandlung>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(Schild3KatalogEintragUnicodeUmwandlung.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogSchild3Versetzungsvermerke für den Zugriff auf die URL https://{hostname}/db/{schema}/schild3/versetzungsvermerke
	 *
	 * Die Liste der Einträge aus dem Schild-Katalog Versetzungsvermerke / PrfSemAbschl. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen für den Schild-Katalog Versetzungsvermerke / PrfSemAbschl
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Schild3KatalogEintragVersetzungsvermerke>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen für den Schild-Katalog Versetzungsvermerke / PrfSemAbschl
	 */
	public async getKatalogSchild3Versetzungsvermerke(schema : string) : Promise<List<Schild3KatalogEintragVersetzungsvermerke>> {
		const path = "/db/{schema}/schild3/versetzungsvermerke"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<Schild3KatalogEintragVersetzungsvermerke>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(Schild3KatalogEintragVersetzungsvermerke.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getSchuelerLernabschnittsdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{id : \d+}/abschnitt/{abschnitt : \d+}/lernabschnittsdaten
	 *
	 * Liest die Lernabschnittsdaten des Schülers zu der angegebenen ID und dem angegeben Schuljahresabschnitt aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Lernabschnittsdaten des Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SchuelerLernabschnittsdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzusehen.
	 *   Code 404: Kein Schüler-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 * @param {number} abschnitt - der Pfad-Parameter abschnitt
	 *
	 * @returns Die Lernabschnittsdaten des Schülers
	 */
	public async getSchuelerLernabschnittsdaten(schema : string, id : number, abschnitt : number) : Promise<SchuelerLernabschnittsdaten> {
		const path = "/db/{schema}/schueler/{id : \\d+}/abschnitt/{abschnitt : \\d+}/lernabschnittsdaten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString())
			.replace(/{abschnitt\s*(:[^}]+)?}/g, abschnitt.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return SchuelerLernabschnittsdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getSchuelerBetriebe für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{id : \d+}/betriebe
	 *
	 * Erstellt eine Liste aller in der Datenbank vorhandenen Betriebe unter Angabe der Schüler-IDdes Vor- und Nachnamens, Erzieherart, Kontaktdaten, ob sie in der Anwendung sichtbar bzw. änderbar sein sollen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Betriebsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Schülerbetrieben
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SchuelerBetriebsdaten>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Erzieherdaten anzusehen.
	 *   Code 404: Keine Erzieher-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Eine Liste von Schülerbetrieben
	 */
	public async getSchuelerBetriebe(schema : string, id : number) : Promise<List<SchuelerBetriebsdaten>> {
		const path = "/db/{schema}/schueler/{id : \\d+}/betriebe"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchuelerBetriebsdaten>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchuelerBetriebsdaten.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getSchuelerBetriebsstammdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{id : \d+}/betriebsstammdaten
	 *
	 * Erstellt eine Liste aller in der Datenbank vorhandenen Betriebe eines Schülers unter Angabe der ID,ob sie in der Anwendung sichtbar bzw. änderbar sein sollen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Betriebsdaten des Schülers besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von von Betriebsstammdaten eines Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<BetriebStammdaten>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Betriebdaten anzusehen.
	 *   Code 404: Keine Betrieb-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Eine Liste von von Betriebsstammdaten eines Schülers
	 */
	public async getSchuelerBetriebsstammdaten(schema : string, id : number) : Promise<List<BetriebStammdaten>> {
		const path = "/db/{schema}/schueler/{id : \\d+}/betriebsstammdaten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<BetriebStammdaten>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(BetriebStammdaten.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getSchuelerErzieher für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{id : \d+}/erzieher
	 *
	 * Erstellt eine Liste aller in der Datenbank vorhandenen Erzieher unter Angabe der Schüler-IDdes Vor- und Nachnamens, Erzieherart, Kontaktdaten, ob sie in der Anwendung sichtbar bzw. änderbar sein sollen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Erzieherdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Erzieherstammdaten
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<ErzieherStammdaten>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Erzieherdaten anzusehen.
	 *   Code 404: Keine Erzieher-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Eine Liste von Erzieherstammdaten
	 */
	public async getSchuelerErzieher(schema : string, id : number) : Promise<List<ErzieherStammdaten>> {
		const path = "/db/{schema}/schueler/{id : \\d+}/erzieher"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<ErzieherStammdaten>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(ErzieherStammdaten.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKAOAdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{id : \d+}/kaoa
	 *
	 * Liest die KAOADaten des Schülers zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die KAOADaten des Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SchuelerKAoADaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzusehen.
	 *   Code 404: Kein Schüler-KAoA-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die KAOADaten des Schülers
	 */
	public async getKAOAdaten(schema : string, id : number) : Promise<SchuelerKAoADaten> {
		const path = "/db/{schema}/schueler/{id : \\d+}/kaoa"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return SchuelerKAoADaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode createKAOAdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{id : \d+}/kaoa
	 *
	 * Erstellt einen neuen SchuelerKAoADaten EintragDabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von SchülerKAoADaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die KAOADaten des Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SchuelerKAoADaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzulegen.
	 *   Code 404: Kein Schüler-KAoA-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {SchuelerKAoADaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die KAOADaten des Schülers
	 */
	public async createKAOAdaten(data : SchuelerKAoADaten, schema : string, id : number) : Promise<SchuelerKAoADaten> {
		const path = "/db/{schema}/schueler/{id : \\d+}/kaoa"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = SchuelerKAoADaten.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SchuelerKAoADaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PUT-Methode putKAOAdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{id : \d+}/kaoa/{skid : \d+}
	 *
	 * Ändert einen SchuelerKAoADaten EintragDabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von SchülerKAoADaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die KAOADaten des Schülers
	 *   Code 400: Fehler bei der Datenvalidierung
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzulegen.
	 *   Code 404: Kein Schüler-KAoA-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {SchuelerKAoADaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 * @param {number} skid - der Pfad-Parameter skid
	 */
	public async putKAOAdaten(data : SchuelerKAoADaten, schema : string, id : number, skid : number) : Promise<void> {
		const path = "/db/{schema}/schueler/{id : \\d+}/kaoa/{skid : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString())
			.replace(/{skid\s*(:[^}]+)?}/g, skid.toString());
		const body : string = SchuelerKAoADaten.transpilerToJSON(data);
		return super.putJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteKAOAdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{id : \d+}/kaoa/{skid : \d+}
	 *
	 * Löscht einen SchuelerKAoADaten EintragDabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von SchülerKAoADaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die KAOADaten des Schülers wurden gelöscht
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzulegen.
	 *   Code 404: Kein Schüler-KAoA-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 * @param {number} skid - der Pfad-Parameter skid
	 */
	public async deleteKAOAdaten(schema : string, id : number, skid : number) : Promise<void> {
		const path = "/db/{schema}/schueler/{id : \\d+}/kaoa/{skid : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString())
			.replace(/{skid\s*(:[^}]+)?}/g, skid.toString());
		await super.deleteJSON(path, null);
		return;
	}


	/**
	 * Implementierung der GET-Methode getSchuelerLernabschnittsliste für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{id : \d+}/lernabschnitte
	 *
	 * Liest eine Lister der Lernabschnitte des Schülers zu der angegebenen ID aus der Datenbank und liefert diese zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Schüler-Lernabschnitt-Listeneinträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SchuelerLernabschnittListeEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzusehen.
	 *   Code 404: Kein Schüler-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Eine Liste von Schüler-Lernabschnitt-Listeneinträgen
	 */
	public async getSchuelerLernabschnittsliste(schema : string, id : number) : Promise<List<SchuelerLernabschnittListeEintrag>> {
		const path = "/db/{schema}/schueler/{id : \\d+}/lernabschnitte"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchuelerLernabschnittListeEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchuelerLernabschnittListeEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getSchuelerSchulbesuch für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{id : \d+}/schulbesuch
	 *
	 * Liest die Schulbesuchsdaten des Schülers zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Schulbesuchsdaten des Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SchuelerSchulbesuchsdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Schülerdaten anzusehen.
	 *   Code 404: Kein Schüler-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Schulbesuchsdaten des Schülers
	 */
	public async getSchuelerSchulbesuch(schema : string, id : number) : Promise<SchuelerSchulbesuchsdaten> {
		const path = "/db/{schema}/schueler/{id : \\d+}/schulbesuch"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return SchuelerSchulbesuchsdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchSchuelerSchulbesuch für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{id : \d+}/schulbesuch
	 *
	 * Passt die Schüler-Schulbesuchsdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Schülerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Schüler-Schulbesuchsdaten integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Schülerdaten zu ändern.
	 *   Code 404: Kein Schüler-Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<SchuelerSchulbesuchsdaten>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchSchuelerSchulbesuch(data : Partial<SchuelerSchulbesuchsdaten>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/schueler/{id : \\d+}/schulbesuch"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = SchuelerSchulbesuchsdaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getSchuelerSprachbelegung für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{id : \d+}/sprache/{sprache : [A-Z]+}/belegung
	 *
	 * Liest die Spachbelegungen zu der Sprache mit dem angegebenen Sprachkürzel des Schülers mit der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Spachbelegung des Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Sprachbelegung
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Spachbelegung anzusehen.
	 *   Code 404: Kein Schüler mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 * @param {string} sprache - der Pfad-Parameter sprache
	 *
	 * @returns Die Spachbelegung des Schülers
	 */
	public async getSchuelerSprachbelegung(schema : string, id : number, sprache : string) : Promise<Sprachbelegung> {
		const path = "/db/{schema}/schueler/{id : \\d+}/sprache/{sprache : [A-Z]+}/belegung"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString())
			.replace(/{sprache\s*(:[^}]+)?}/g, sprache);
		const result : string = await super.getJSON(path);
		const text = result;
		return Sprachbelegung.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchSchuelerSprachbelegung für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{id : \d+}/sprache/{sprache : [A-Z]+}/belegung
	 *
	 * Passt die Sprachbelegung zu der angegebenen Schüler-ID und dem angegebenen Sprachkürzel an und speichert das Ergebnis in der Datenbank. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Sprachbelegungen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Sprachbelegung integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Sprachbelegungen zu ändern.
	 *   Code 404: Kein Schüler-Eintrag mit der angegebenen ID gefunden oder keine Sprachbelegung für die Sprache gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<Sprachbelegung>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 * @param {string} sprache - der Pfad-Parameter sprache
	 */
	public async patchSchuelerSprachbelegung(data : Partial<Sprachbelegung>, schema : string, id : number, sprache : string) : Promise<void> {
		const path = "/db/{schema}/schueler/{id : \\d+}/sprache/{sprache : [A-Z]+}/belegung"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString())
			.replace(/{sprache\s*(:[^}]+)?}/g, sprache);
		const body : string = Sprachbelegung.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteSchuelerSprachbelegung für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{id : \d+}/sprache/{sprache : [A-Z]+}/belegung
	 *
	 * Entfernt eine Sprachbelegung eines Schülers.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Sprachbelegungen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Sprachbelegung wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Sprachbelegung
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Sprachbelegung anzulegen.
	 *   Code 404: Kein Schüler mit der angegebenen ID oder keine Sprache mit dem Kürzel gefunden.
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 * @param {string} sprache - der Pfad-Parameter sprache
	 *
	 * @returns Die Sprachbelegung wurde erfolgreich entfernt.
	 */
	public async deleteSchuelerSprachbelegung(schema : string, id : number, sprache : string) : Promise<Sprachbelegung> {
		const path = "/db/{schema}/schueler/{id : \\d+}/sprache/{sprache : [A-Z]+}/belegung"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString())
			.replace(/{sprache\s*(:[^}]+)?}/g, sprache);
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return Sprachbelegung.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getSchuelerSprachpruefung für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{id : \d+}/sprache/{sprache : [A-Z]+}/pruefung
	 *
	 * Liest die Sprachprüfung zu der Sprache mit dem angegebenen Sprachkürzel des Schülers mit der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Sprachprüfung des Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Sprachpruefung
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Sprachprüfung anzusehen.
	 *   Code 404: Kein Schüler mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 * @param {string} sprache - der Pfad-Parameter sprache
	 *
	 * @returns Die Sprachprüfung des Schülers
	 */
	public async getSchuelerSprachpruefung(schema : string, id : number, sprache : string) : Promise<Sprachpruefung> {
		const path = "/db/{schema}/schueler/{id : \\d+}/sprache/{sprache : [A-Z]+}/pruefung"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString())
			.replace(/{sprache\s*(:[^}]+)?}/g, sprache);
		const result : string = await super.getJSON(path);
		const text = result;
		return Sprachpruefung.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchSchuelerSprachpruefung für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{id : \d+}/sprache/{sprache : [A-Z]+}/pruefung
	 *
	 * Passt die Sprachprüfung zu der angegebenen Schüler-ID und dem angegebenen Sprachkürzel an und speichert das Ergebnis in der Datenbank. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Sprachprüfungen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Sprachprüfung integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Sprachprüfungen zu ändern.
	 *   Code 404: Kein Schüler-Eintrag mit der angegebenen ID gefunden oder keine Sprachprüfung für die Sprache gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<Sprachpruefung>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 * @param {string} sprache - der Pfad-Parameter sprache
	 */
	public async patchSchuelerSprachpruefung(data : Partial<Sprachpruefung>, schema : string, id : number, sprache : string) : Promise<void> {
		const path = "/db/{schema}/schueler/{id : \\d+}/sprache/{sprache : [A-Z]+}/pruefung"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString())
			.replace(/{sprache\s*(:[^}]+)?}/g, sprache);
		const body : string = Sprachpruefung.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteSchuelerSprachpruefung für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{id : \d+}/sprache/{sprache : [A-Z]+}/pruefung
	 *
	 * Entfernt eine Sprachprüfung eines Schülers.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Sprachprüfungen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Sprachprüfung wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Sprachpruefung
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Sprachbelegung anzulegen.
	 *   Code 404: Kein Schüler mit der angegebenen ID oder keine Sprache mit dem Kürzel gefunden.
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 * @param {string} sprache - der Pfad-Parameter sprache
	 *
	 * @returns Die Sprachprüfung wurde erfolgreich entfernt.
	 */
	public async deleteSchuelerSprachpruefung(schema : string, id : number, sprache : string) : Promise<Sprachpruefung> {
		const path = "/db/{schema}/schueler/{id : \\d+}/sprache/{sprache : [A-Z]+}/pruefung"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString())
			.replace(/{sprache\s*(:[^}]+)?}/g, sprache);
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return Sprachpruefung.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getSchuelerSprachbelegungen für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{id : \d+}/sprachen/belegungen
	 *
	 * Liest die Spachbelegungen des Schülers zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Spachbelegungen des Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Sprachbelegung>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Spachbelegungen anzusehen.
	 *   Code 404: Kein Schüler mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Spachbelegungen des Schülers
	 */
	public async getSchuelerSprachbelegungen(schema : string, id : number) : Promise<List<Sprachbelegung>> {
		const path = "/db/{schema}/schueler/{id : \\d+}/sprachen/belegungen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<Sprachbelegung>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(Sprachbelegung.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode addSchuelerSprachbelegung für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{id : \d+}/sprachen/belegungen
	 *
	 * Erstellt eine neuen Sprachbelegung für den Schüler mit der angebenen ID. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Sprachbelegungen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Die Sprachbelegung des Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Sprachbelegung
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Sprachbelegung anzulegen.
	 *   Code 404: Kein Schüler mit der angegebenen ID oder keine Sprache mit dem Kürzel gefunden.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<Sprachbelegung>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Sprachbelegung des Schülers
	 */
	public async addSchuelerSprachbelegung(data : Partial<Sprachbelegung>, schema : string, id : number) : Promise<Sprachbelegung> {
		const path = "/db/{schema}/schueler/{id : \\d+}/sprachen/belegungen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = Sprachbelegung.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return Sprachbelegung.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getSchuelerSprachpruefungen für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{id : \d+}/sprachen/pruefungen
	 *
	 * Liest die Sprachprüfungen des Schülers zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Sprachprüfungen des Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Sprachpruefung>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Sprachprüfungen anzusehen.
	 *   Code 404: Kein Schüler mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Sprachprüfungen des Schülers
	 */
	public async getSchuelerSprachpruefungen(schema : string, id : number) : Promise<List<Sprachpruefung>> {
		const path = "/db/{schema}/schueler/{id : \\d+}/sprachen/pruefungen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<Sprachpruefung>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(Sprachpruefung.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode addSchuelerSprachpruefung für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{id : \d+}/sprachen/pruefungen
	 *
	 * Erstellt eine neuen Sprachprüfung für den Schüler mit der angebenen ID. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Sprachprüfungen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Die Sprachprüfung des Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Sprachpruefung
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Sprachprüfung anzulegen.
	 *   Code 404: Kein Schüler mit der angegebenen ID gefunden.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<Sprachpruefung>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Sprachprüfung des Schülers
	 */
	public async addSchuelerSprachpruefung(data : Partial<Sprachpruefung>, schema : string, id : number) : Promise<Sprachpruefung> {
		const path = "/db/{schema}/schueler/{id : \\d+}/sprachen/pruefungen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = Sprachpruefung.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return Sprachpruefung.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getSchuelerStammdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{id : \d+}/stammdaten
	 *
	 * Liest die Stammdaten des Schülers zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Stammdaten des Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SchuelerStammdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Schülerdaten anzusehen.
	 *   Code 404: Kein Schüler-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Stammdaten des Schülers
	 */
	public async getSchuelerStammdaten(schema : string, id : number) : Promise<SchuelerStammdaten> {
		const path = "/db/{schema}/schueler/{id : \\d+}/stammdaten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return SchuelerStammdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchSchuelerStammdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{id : \d+}/stammdaten
	 *
	 * Passt die Schüler-Stammdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Schülerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Schülerstammdaten integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Schülerdaten zu ändern.
	 *   Code 404: Kein Schüler-Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<SchuelerStammdaten>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchSchuelerStammdaten(data : Partial<SchuelerStammdaten>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/schueler/{id : \\d+}/stammdaten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = SchuelerStammdaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getSchuelerFuerAbschnitt für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/abschnitt/{abschnitt : \d+}
	 *
	 * Erstellt eine Liste aller Schüler des angegebenen Schuljahresabschnitts unter Angabe der ID, des Vor- und Nachnamens, der Klasse, des Jahrgangs, sein Status (z.B. aktiv), einer Sortierreihenfolge, ob sie in der Anwendung sichtbar bzw. änderbar sein sollen. Die schüler sind anhand der Klasse, des Nchnamens und des Vornamens sortiert.Es wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Schüler-Listen-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SchuelerListeEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Schülerdaten anzusehen.
	 *   Code 404: Keine Schüler-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abschnitt - der Pfad-Parameter abschnitt
	 *
	 * @returns Eine Liste von Schüler-Listen-Einträgen
	 */
	public async getSchuelerFuerAbschnitt(schema : string, abschnitt : number) : Promise<List<SchuelerListeEintrag>> {
		const path = "/db/{schema}/schueler/abschnitt/{abschnitt : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abschnitt\s*(:[^}]+)?}/g, abschnitt.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchuelerListeEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchuelerListeEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getSchuelerAktuell für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/aktuell
	 *
	 * Erstellt eine Liste aller im aktuellen Schuljahresabschnitt vorhanden Schüler unter Angabe der ID, des Vor- und Nachnamens, der Klasse, des Jahrgangs, sein Status (z.B. aktiv), einer Sortierreihenfolge, ob sie in der Anwendung sichtbar bzw. änderbar sein sollen. Die schüler sind anhand der Klasse, des Nachnamens und des Vornamens sortiert.Es wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Schüler-Listen-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SchuelerListeEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Schülerdaten anzusehen.
	 *   Code 404: Keine Schüler-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Schüler-Listen-Einträgen
	 */
	public async getSchuelerAktuell(schema : string) : Promise<List<SchuelerListeEintrag>> {
		const path = "/db/{schema}/schueler/aktuell"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchuelerListeEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchuelerListeEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogHerkuenfte für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/allgemein/herkuenfte
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhandenen Herkünfte von Schülern. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<HerkunftKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen
	 */
	public async getKatalogHerkuenfte(schema : string) : Promise<List<HerkunftKatalogEintrag>> {
		const path = "/db/{schema}/schueler/allgemein/herkuenfte"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<HerkunftKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(HerkunftKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogHerkunftsarten für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/allgemein/herkunftsarten
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhandenen Herkunftsarten bei Schülern. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<HerkunftsartKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen
	 */
	public async getKatalogHerkunftsarten(schema : string) : Promise<List<HerkunftsartKatalogEintrag>> {
		const path = "/db/{schema}/schueler/allgemein/herkunftsarten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<HerkunftsartKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(HerkunftsartKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogUebergangsempfehlung für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/allgemein/uebergangsempfehlung
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhandenen Übergangsempfehlungen der Grundschule für die Sekundarstufe I. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<UebergangsempfehlungKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen
	 */
	public async getKatalogUebergangsempfehlung(schema : string) : Promise<List<UebergangsempfehlungKatalogEintrag>> {
		const path = "/db/{schema}/schueler/allgemein/uebergangsempfehlung"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<UebergangsempfehlungKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(UebergangsempfehlungKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getSchuelerFahrschuelerarten für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/fahrschuelerarten
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Fahrschülerarten unter Angabe der ID, eines Kürzels und der Bezeichnung. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Fahrschülerarten-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<KatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Fahrschülerart-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Fahrschülerarten-Katalog-Einträgen
	 */
	public async getSchuelerFahrschuelerarten(schema : string) : Promise<List<KatalogEintrag>> {
		const path = "/db/{schema}/schueler/fahrschuelerarten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getSchuelerLeistungsdatenByID für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/leistungsdaten/{id : \d+}
	 *
	 * Liest die Schülerleistungsdaten zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerleistungsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Leistungsdaten des Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SchuelerLeistungsdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Schülerleistungsdaten anzusehen.
	 *   Code 404: Kein Schülerleistungsdaten-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Leistungsdaten des Schülers
	 */
	public async getSchuelerLeistungsdatenByID(schema : string, id : number) : Promise<SchuelerLeistungsdaten> {
		const path = "/db/{schema}/schueler/leistungsdaten/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return SchuelerLeistungsdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchSchuelerLeistungsdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/leistungsdaten/{id : \d+}
	 *
	 * Passt die Schülerleistungsdaten mit der angebenen ID an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Schülerleistungsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 404: Kein Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<SchuelerLeistungsdaten>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchSchuelerLeistungsdaten(data : Partial<SchuelerLeistungsdaten>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/schueler/leistungsdaten/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = SchuelerLeistungsdaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getSchuelerLernabschnittsdatenByID für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/lernabschnittsdaten/{abschnitt : \d+}
	 *
	 * Liest die Schüler-Lernabschnittsdaten zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Lernabschnittsdaten des Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SchuelerLernabschnittsdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzusehen.
	 *   Code 404: Kein Eintrag mit Schüler-Lernabschnittsdaten mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abschnitt - der Pfad-Parameter abschnitt
	 *
	 * @returns Die Lernabschnittsdaten des Schülers
	 */
	public async getSchuelerLernabschnittsdatenByID(schema : string, abschnitt : number) : Promise<SchuelerLernabschnittsdaten> {
		const path = "/db/{schema}/schueler/lernabschnittsdaten/{abschnitt : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abschnitt\s*(:[^}]+)?}/g, abschnitt.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return SchuelerLernabschnittsdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchSchuelerLernabschnittsdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/lernabschnittsdaten/{abschnitt : \d+}
	 *
	 * Passt die Schülerlernabschnittsdaten mit der angebenen ID an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Schülerlernabschnittsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 404: Kein Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<SchuelerLernabschnittsdaten>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abschnitt - der Pfad-Parameter abschnitt
	 */
	public async patchSchuelerLernabschnittsdaten(data : Partial<SchuelerLernabschnittsdaten>, schema : string, abschnitt : number) : Promise<void> {
		const path = "/db/{schema}/schueler/lernabschnittsdaten/{abschnitt : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abschnitt\s*(:[^}]+)?}/g, abschnitt.toString());
		const body : string = SchuelerLernabschnittsdaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der PATCH-Methode patchSchuelerLernabschnittsdatenBemerkungen für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/lernabschnittsdaten/{abschnitt : \d+}/bemerkungen
	 *
	 * Passt die Bemerkungen von Schülerlernabschnittsdaten mit der angebenen ID an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Schülerlernabschnittsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 404: Kein Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<SchuelerLernabschnittBemerkungen>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abschnitt - der Pfad-Parameter abschnitt
	 */
	public async patchSchuelerLernabschnittsdatenBemerkungen(data : Partial<SchuelerLernabschnittBemerkungen>, schema : string, abschnitt : number) : Promise<void> {
		const path = "/db/{schema}/schueler/lernabschnittsdaten/{abschnitt : \\d+}/bemerkungen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abschnitt\s*(:[^}]+)?}/g, abschnitt.toString());
		const body : string = SchuelerLernabschnittBemerkungen.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getKatalogAbgangsartenAllgemeinbildend für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/allgemein/abgangsarten/allgemeinbildend
	 *
	 * Gibt den Katalog der Abgangsarten für allgemeinbildende Schulformen zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Katalog
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: AbgangsartKatalog
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Katalog anzusehen.
	 *   Code 404: Katalog nicht gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Katalog
	 */
	public async getKatalogAbgangsartenAllgemeinbildend(schema : string) : Promise<AbgangsartKatalog> {
		const path = "/db/{schema}/schule/allgemein/abgangsarten/allgemeinbildend"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const text = result;
		return AbgangsartKatalog.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getKatalogAbgangsartenBerufsbildend für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/allgemein/abgangsarten/berufsbildend
	 *
	 * Gibt den Katalog der Abgangsarten für berufsbildende Schulformen zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Katalog
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: AbgangsartKatalog
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Katalog anzusehen.
	 *   Code 404: Katalog nicht gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Katalog
	 */
	public async getKatalogAbgangsartenBerufsbildend(schema : string) : Promise<AbgangsartKatalog> {
		const path = "/db/{schema}/schule/allgemein/abgangsarten/berufsbildend"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const text = result;
		return AbgangsartKatalog.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getAllgemeineMerkmale für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/allgemein/allgemeine_merkmale
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Allgemeinen Merkmale bei Schulen und Schülern. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Allgemeinen-Merkmal-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<AllgemeineMerkmaleKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Allgemeine-Merkmal-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Allgemeinen-Merkmal-Katalog-Einträgen
	 */
	public async getAllgemeineMerkmale(schema : string) : Promise<List<AllgemeineMerkmaleKatalogEintrag>> {
		const path = "/db/{schema}/schule/allgemein/allgemeine_merkmale"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<AllgemeineMerkmaleKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(AllgemeineMerkmaleKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getBerufskollegAnlagen für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/allgemein/berufskolleg/anlagen
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Anlagen am Berufskolleg. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Berufskolleg-Anlagen-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<BerufskollegAnlageKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Berufskolleg-Anlagen-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Berufskolleg-Anlagen-Katalog-Einträgen
	 */
	public async getBerufskollegAnlagen(schema : string) : Promise<List<BerufskollegAnlageKatalogEintrag>> {
		const path = "/db/{schema}/schule/allgemein/berufskolleg/anlagen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<BerufskollegAnlageKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(BerufskollegAnlageKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getBerufskollegBerufsebenen für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/allgemein/berufskolleg/berufsebenen
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Berufsebenen am Berufskolleg. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Berufskolleg-Berufsebenen-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<BerufskollegBerufsebeneKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Berufskolleg-Berufsebenen-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Berufskolleg-Berufsebenen-Katalog-Einträgen
	 */
	public async getBerufskollegBerufsebenen(schema : string) : Promise<List<BerufskollegBerufsebeneKatalogEintrag>> {
		const path = "/db/{schema}/schule/allgemein/berufskolleg/berufsebenen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<BerufskollegBerufsebeneKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(BerufskollegBerufsebeneKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getBerufskollegFachklassen für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/allgemein/berufskolleg/fachklassen
	 *
	 * Gibt den Katalog der Fachklassen am Berufskolleg zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Fachklassen-Katalog für berufsbildende Schulen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: BerufskollegFachklassenKatalog
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Katalog anzusehen.
	 *   Code 404: Kein Berufskolleg-Fachklassen-Katalog gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Fachklassen-Katalog für berufsbildende Schulen
	 */
	public async getBerufskollegFachklassen(schema : string) : Promise<BerufskollegFachklassenKatalog> {
		const path = "/db/{schema}/schule/allgemein/berufskolleg/fachklassen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const text = result;
		return BerufskollegFachklassenKatalog.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getEinschulungsarten für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/allgemein/einschulungsarten
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Einschulungsarten. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Einschulungsart-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<EinschulungsartKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Einschulungsart-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Einschulungsart-Katalog-Einträgen
	 */
	public async getEinschulungsarten(schema : string) : Promise<List<EinschulungsartKatalogEintrag>> {
		const path = "/db/{schema}/schule/allgemein/einschulungsarten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<EinschulungsartKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(EinschulungsartKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getFoerderschwerpunkte für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/allgemein/foerderschwerpunkte
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Förderschwerpunkte. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Förderschwerpunkt-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<FoerderschwerpunktKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Förderschwerpunkt-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Förderschwerpunkt-Katalog-Einträgen
	 */
	public async getFoerderschwerpunkte(schema : string) : Promise<List<FoerderschwerpunktKatalogEintrag>> {
		const path = "/db/{schema}/schule/allgemein/foerderschwerpunkte"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<FoerderschwerpunktKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(FoerderschwerpunktKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogHerkunftsschulnummern für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/allgemein/herkunftsschulnummern
	 *
	 * Die Liste der Einträge aus dem Katalog der zusätzlichen Herkunftsschulnummern. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen für den Katalog der zusätzlichen Herkunftsschulnummern
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<HerkunftsschulnummerKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen für den Katalog der zusätzlichen Herkunftsschulnummern
	 */
	public async getKatalogHerkunftsschulnummern(schema : string) : Promise<List<HerkunftsschulnummerKatalogEintrag>> {
		const path = "/db/{schema}/schule/allgemein/herkunftsschulnummern"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<HerkunftsschulnummerKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(HerkunftsschulnummerKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogKindergartenbesuchsdauer für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/allgemein/kindergartenbesuch
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Dauern des Kindergartenbesuchs, welche erfasst werden. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<KindergartenbesuchKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen
	 */
	public async getKatalogKindergartenbesuchsdauer(schema : string) : Promise<List<KindergartenbesuchKatalogEintrag>> {
		const path = "/db/{schema}/schule/allgemein/kindergartenbesuch"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KindergartenbesuchKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KindergartenbesuchKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getNationaelitaeten für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/allgemein/nationalitaeten
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Nationalitäten. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Nationalitäten-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<NationalitaetenKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Nationalitäten-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Nationalitäten-Katalog-Einträgen
	 */
	public async getNationaelitaeten(schema : string) : Promise<List<NationalitaetenKatalogEintrag>> {
		const path = "/db/{schema}/schule/allgemein/nationalitaeten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<NationalitaetenKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(NationalitaetenKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogNoten für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/allgemein/noten
	 *
	 * Gibt den Noten-Katalog zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Noten-Katalog.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<NotenKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine gültige Anmeldung.
	 *   Code 404: Keine Noten-Einträge gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Noten-Katalog.
	 */
	public async getKatalogNoten(schema : string) : Promise<List<NotenKatalogEintrag>> {
		const path = "/db/{schema}/schule/allgemein/noten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<NotenKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(NotenKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogOrganisationsformen für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/allgemein/organisationsformen
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden gültigen Organisationsformen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Organisationsform-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<OrganisationsformKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Organisationsform-Katalog-Einträgen
	 */
	public async getKatalogOrganisationsformen(schema : string) : Promise<List<OrganisationsformKatalogEintrag>> {
		const path = "/db/{schema}/schule/allgemein/organisationsformen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<OrganisationsformKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(OrganisationsformKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getPruefungsordnungen für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/allgemein/pruefungsordnungen
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Ausbildungs- und Prüfungsordnungen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<PruefungsordnungKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen
	 */
	public async getPruefungsordnungen(schema : string) : Promise<List<PruefungsordnungKatalogEintrag>> {
		const path = "/db/{schema}/schule/allgemein/pruefungsordnungen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<PruefungsordnungKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(PruefungsordnungKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogReformpaedagogik für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/allgemein/reformpaedagogik
	 *
	 * Gibt den Reformpädagogik-Katalog für die Schulform dieser Schule zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Reformpädagogik-Katalog für die Schulform dieser Schule.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<ReformpaedagogikKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine gültige Anmeldung.
	 *   Code 404: Keine Reformpädagogik-Einträge für die Schulform dieser Schule gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Reformpädagogik-Katalog für die Schulform dieser Schule.
	 */
	public async getKatalogReformpaedagogik(schema : string) : Promise<List<ReformpaedagogikKatalogEintrag>> {
		const path = "/db/{schema}/schule/allgemein/reformpaedagogik"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<ReformpaedagogikKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(ReformpaedagogikKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogReformpaedagogikEintrag für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/allgemein/reformpaedagogik/{id : \d+}
	 *
	 * Gibt den Reformpädagogik-Katalog-Eintrag für die angegebene ID zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Reformpädagogik-Katalog-Eintrag für die angegebene ID.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: ReformpaedagogikKatalogEintrag
	 *   Code 403: Der SVWS-Benutzer hat keine gültige Anmeldung.
	 *   Code 404: Kein Reformpädagogik-Katalog-Eintrag für die angegebene ID gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Reformpädagogik-Katalog-Eintrag für die angegebene ID.
	 */
	public async getKatalogReformpaedagogikEintrag(schema : string, id : number) : Promise<ReformpaedagogikKatalogEintrag> {
		const path = "/db/{schema}/schule/allgemein/reformpaedagogik/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return ReformpaedagogikKatalogEintrag.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getKatalogReformpaedagogikAlle für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/allgemein/reformpaedagogik/alle
	 *
	 * Gibt den Reformpädagogik-Katalog aller Schulformen zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Reformpädagogik-Katalog aller Schulformen.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<ReformpaedagogikKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine gültige Anmeldung.
	 *   Code 404: Keine Fachgruppen gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Reformpädagogik-Katalog aller Schulformen.
	 */
	public async getKatalogReformpaedagogikAlle(schema : string) : Promise<List<ReformpaedagogikKatalogEintrag>> {
		const path = "/db/{schema}/schule/allgemein/reformpaedagogik/alle"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<ReformpaedagogikKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(ReformpaedagogikKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogReligionen für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/allgemein/religionen
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Relgionen bzw. Konfessionen, welche im Rahmen der amtlichen Schulstatistik verwendet werden. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<ReligionKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen
	 */
	public async getKatalogReligionen(schema : string) : Promise<List<ReligionKatalogEintrag>> {
		const path = "/db/{schema}/schule/allgemein/religionen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<ReligionKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(ReligionKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getSchulabschluesseAllgemeinbildend für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/allgemein/schulabschluesse/allgemeinbildend
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden allgemeinbildenden Schulabschlüsse. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von allgemeinbildenden Abschlussart-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SchulabschlussAllgemeinbildendKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Abschlussart-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von allgemeinbildenden Abschlussart-Katalog-Einträgen
	 */
	public async getSchulabschluesseAllgemeinbildend(schema : string) : Promise<List<SchulabschlussAllgemeinbildendKatalogEintrag>> {
		const path = "/db/{schema}/schule/allgemein/schulabschluesse/allgemeinbildend"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchulabschlussAllgemeinbildendKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchulabschlussAllgemeinbildendKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getSchulabschluesseBerufsbildend für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/allgemein/schulabschluesse/berufsbildend
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden berufsbildenden Schulabschlüsse. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von berufsbildenden Abschlussart-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SchulabschlussBerufsbildendKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Abschlussart-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von berufsbildenden Abschlussart-Katalog-Einträgen
	 */
	public async getSchulabschluesseBerufsbildend(schema : string) : Promise<List<SchulabschlussBerufsbildendKatalogEintrag>> {
		const path = "/db/{schema}/schule/allgemein/schulabschluesse/berufsbildend"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchulabschlussBerufsbildendKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchulabschlussBerufsbildendKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogSchulen für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/allgemein/schulen
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhandenen Schulen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Schulen-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SchulenKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Schulen-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Schulen-Katalog-Einträgen
	 */
	public async getKatalogSchulen(schema : string) : Promise<List<SchulenKatalogEintrag>> {
		const path = "/db/{schema}/schule/allgemein/schulen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchulenKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchulenKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getSchulformen für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/allgemein/schulformen
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Schulformen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Schulform-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SchulformKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Schulform-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Schulform-Katalog-Einträgen
	 */
	public async getSchulformen(schema : string) : Promise<List<SchulformKatalogEintrag>> {
		const path = "/db/{schema}/schule/allgemein/schulformen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchulformKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchulformKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getSchulgliederungen für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/allgemein/schulgliederungen
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Schulgliederungen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Schulgliederung-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SchulgliederungKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Schulform-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Schulgliederung-Katalog-Einträgen
	 */
	public async getSchulgliederungen(schema : string) : Promise<List<SchulgliederungKatalogEintrag>> {
		const path = "/db/{schema}/schule/allgemein/schulgliederungen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchulgliederungKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchulgliederungKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getSchulstufen für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/allgemein/schulstufen
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden gültigen Schulstufen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Schulstufen-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SchulstufeKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Schulstufen-Katalog-Einträgen
	 */
	public async getSchulstufen(schema : string) : Promise<List<SchulstufeKatalogEintrag>> {
		const path = "/db/{schema}/schule/allgemein/schulstufen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchulstufeKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchulstufeKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogSchultraeger für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/allgemein/schultraeger
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhandenen Schulträger. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Schulträger-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SchultraegerKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Schulträger-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Schulträger-Katalog-Einträgen
	 */
	public async getKatalogSchultraeger(schema : string) : Promise<List<SchultraegerKatalogEintrag>> {
		const path = "/db/{schema}/schule/allgemein/schultraeger"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchultraegerKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchultraegerKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getVerkehrssprachen für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/allgemein/verkehrssprachen
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden der Verkehrssprachen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<VerkehrsspracheKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen
	 */
	public async getVerkehrssprachen(schema : string) : Promise<List<VerkehrsspracheKatalogEintrag>> {
		const path = "/db/{schema}/schule/allgemein/verkehrssprachen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<VerkehrsspracheKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(VerkehrsspracheKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getAufsichtsbereiche für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/aufsichtsbereiche
	 *
	 * Gibt den Katalog der Aufsichtsbereiche der Schule zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Katalog der Aufsichtsbereiche der Schule.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Aufsichtsbereich>
	 *   Code 403: Der SVWS-Benutzer hat keine gültige Anmeldung.
	 *   Code 404: Keine Aufsichtsbereichs-Einträge gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Katalog der Aufsichtsbereiche der Schule.
	 */
	public async getAufsichtsbereiche(schema : string) : Promise<List<Aufsichtsbereich>> {
		const path = "/db/{schema}/schule/aufsichtsbereiche"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<Aufsichtsbereich>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(Aufsichtsbereich.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getAufsichtsbereich für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/aufsichtsbereiche/{id : \d+}
	 *
	 * Gibt den Aufsichtsbereich der Schule zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Aufsichtsbereich der Schule
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Aufsichtsbereich
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Katalog anzusehen.
	 *   Code 404: Kein Aufsichtsbereich bei der Schule gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Aufsichtsbereich der Schule
	 */
	public async getAufsichtsbereich(schema : string, id : number) : Promise<Aufsichtsbereich> {
		const path = "/db/{schema}/schule/aufsichtsbereiche/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return Aufsichtsbereich.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchAufsichtsbereich für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/aufsichtsbereiche/{id : \d+}
	 *
	 * Passt den Aufsichtsbereich der Schule mit der angebenen ID an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Katalog-Daten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 404: Kein Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<Aufsichtsbereich>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchAufsichtsbereich(data : Partial<Aufsichtsbereich>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/schule/aufsichtsbereiche/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = Aufsichtsbereich.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteAufsichtsbereich für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/aufsichtsbereiche/{id : \d+}
	 *
	 * Entfernt einen Aufsichtsbereich der Schule.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Aufsichtsbereich wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Aufsichtsbereich
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.
	 *   Code 404: Kein Aufsichtsbereich vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Aufsichtsbereich wurde erfolgreich entfernt.
	 */
	public async deleteAufsichtsbereich(schema : string, id : number) : Promise<Aufsichtsbereich> {
		const path = "/db/{schema}/schule/aufsichtsbereiche/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return Aufsichtsbereich.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode addAufsichtsbereich für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/aufsichtsbereiche/create
	 *
	 * Erstellt einen neuen Aufsichtsbereich für die Schule und gibt das zugehörige Objekt zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Katalogs besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Der Aufsichtsbereich wurde erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Aufsichtsbereich
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Aufsichtsbereich für die Schule anzulegen.
	 *   Code 404: Die Katalogdaten wurden nicht gefunden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<Aufsichtsbereich>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Aufsichtsbereich wurde erfolgreich hinzugefügt.
	 */
	public async addAufsichtsbereich(data : Partial<Aufsichtsbereich>, schema : string) : Promise<Aufsichtsbereich> {
		const path = "/db/{schema}/schule/aufsichtsbereiche/create"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = Aufsichtsbereich.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return Aufsichtsbereich.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode addAufsichtsbereiche für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/aufsichtsbereiche/create/multiple
	 *
	 * Erstellt neue Aufsichtsbereiche für die Schule und gibt die zugehörigen Objekte zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Katalogs besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Die Aufsichtsbereiche wurden erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Aufsichtsbereich>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Aufsichtsbereiche für die Schule anzulegen.
	 *   Code 404: Die Katalogdaten wurden nicht gefunden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<Partial<Aufsichtsbereich>>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Aufsichtsbereiche wurden erfolgreich hinzugefügt.
	 */
	public async addAufsichtsbereiche(data : List<Partial<Aufsichtsbereich>>, schema : string) : Promise<List<Aufsichtsbereich>> {
		const path = "/db/{schema}/schule/aufsichtsbereiche/create/multiple"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<Aufsichtsbereich>).map(d => Aufsichtsbereich.transpilerToJSONPatch(d)).join() + "]";
		const result : string = await super.postJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<Aufsichtsbereich>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(Aufsichtsbereich.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der DELETE-Methode deleteAufsichtsbereiche für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/aufsichtsbereiche/delete/multiple
	 *
	 * Entfernt mehrere Aufsichtsbereiche der Schule.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Aufsichtsbereiche wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Aufsichtsbereich>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.
	 *   Code 404: Aufsichtsbereich nicht vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Aufsichtsbereiche wurde erfolgreich entfernt.
	 */
	public async deleteAufsichtsbereiche(data : List<number>, schema : string) : Promise<List<Aufsichtsbereich>> {
		const path = "/db/{schema}/schule/aufsichtsbereiche/delete/multiple"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = "[" + data.toArray().map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<Aufsichtsbereich>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(Aufsichtsbereich.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getSchuelerFoerderschwerpunkt für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/foerderschwerpunkt/{id : \d+}
	 *
	 * Liest die Daten des Förderschwerpunktes zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Daten des Förderschwerpunktes
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: FoerderschwerpunktEintrag
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalogdaten anzusehen.
	 *   Code 404: Kein Förderschwerpunkt-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Daten des Förderschwerpunktes
	 */
	public async getSchuelerFoerderschwerpunkt(schema : string, id : number) : Promise<FoerderschwerpunktEintrag> {
		const path = "/db/{schema}/schule/foerderschwerpunkt/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return FoerderschwerpunktEintrag.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getSchuelerFoerderschwerpunkte für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/foerderschwerpunkte
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Förderschwerpunkte unter Angabe der ID, eines Kürzels und der Bezeichnung. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Förderschwerpunkte-Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<FoerderschwerpunktEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Förderschwerpunkt-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Förderschwerpunkte-Katalog-Einträgen
	 */
	public async getSchuelerFoerderschwerpunkte(schema : string) : Promise<List<FoerderschwerpunktEintrag>> {
		const path = "/db/{schema}/schule/foerderschwerpunkte"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<FoerderschwerpunktEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(FoerderschwerpunktEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode initSchule für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/init/{schulnummer : \d+}
	 *
	 * Legt die Daten für eine neue Schule an und gibt anschließend die Schulstammdaten zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anlegen der Schule besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Schule wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SchuleStammdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Schule anzulegen.
	 *   Code 404: Keine Schule mit der angegebenen Schulnummer gefunden
	 *   Code 409: Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde, dies ist z.B. der Fall, falls zuvor schon eine Schule angelegt wurde.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} schulnummer - der Pfad-Parameter schulnummer
	 *
	 * @returns Die Schule wurde erfolgreich angelegt.
	 */
	public async initSchule(schema : string, schulnummer : number) : Promise<SchuleStammdaten> {
		const path = "/db/{schema}/schule/init/{schulnummer : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{schulnummer\s*(:[^}]+)?}/g, schulnummer.toString());
		const result : string = await super.postJSON(path, null);
		const text = result;
		return SchuleStammdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getSchullogo für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/logo
	 *
	 * Liest das Logo der Schule zum angegebenen Schema aus der Datenbank und liefert dieses zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schuldaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Das Logo der Schule
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: String
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Schuldaten anzusehen.
	 *   Code 404: Kein Eintrag mit dem angegebenen Schema gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Das Logo der Schule
	 */
	public async getSchullogo(schema : string) : Promise<string | null> {
		const path = "/db/{schema}/schule/logo"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const text = result;
		return JSON.parse(text).toString();
	}


	/**
	 * Implementierung der PUT-Methode putSchullogo für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/logo
	 *
	 * Setzt das Logo der Schule. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schuldaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Das Logo der Schule wurde gesetzt
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Schuldaten zu ändern.
	 *   Code 404: Kein Eintrag für die Schule gefunden
	 *
	 * @param {string | null} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 */
	public async putSchullogo(data : string | null, schema : string) : Promise<void> {
		const path = "/db/{schema}/schule/logo"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = JSON.stringify(data);
		return super.putJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getSchuleNummer für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/nummer
	 *
	 * Liefert die Schulnummer der Schule. Es wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Schuldaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Schulnummer
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Integer
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Schuldaten anzusehen.
	 *   Code 404: Keine Schule in der Datenbank vorhanden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Schulnummer
	 */
	public async getSchuleNummer(schema : string) : Promise<number | null> {
		const path = "/db/{schema}/schule/nummer"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const text = result;
		return parseInt(JSON.parse(text));
	}


	/**
	 * Implementierung der GET-Methode getPausenzeiten für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/pausenzeiten
	 *
	 * Gibt den Katalog der Pausenzeiten der Schule zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Katalog der Pausenzeiten der Schule.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<StundenplanPausenzeit>
	 *   Code 403: Der SVWS-Benutzer hat keine gültige Anmeldung.
	 *   Code 404: Keine Pausenzeit-Einträge gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Katalog der Pausenzeiten der Schule.
	 */
	public async getPausenzeiten(schema : string) : Promise<List<StundenplanPausenzeit>> {
		const path = "/db/{schema}/schule/pausenzeiten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<StundenplanPausenzeit>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(StundenplanPausenzeit.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getPausenzeit für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/pausenzeiten/{id : \d+}
	 *
	 * Gibt die Pausenzeit der Schule zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Pausenzeit der Schule
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanPausenzeit
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Katalog anzusehen.
	 *   Code 404: Keine Pausenzeit mit der angegebenen ID bei der Schule gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Pausenzeit der Schule
	 */
	public async getPausenzeit(schema : string, id : number) : Promise<StundenplanPausenzeit> {
		const path = "/db/{schema}/schule/pausenzeiten/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return StundenplanPausenzeit.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchPausenzeit für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/pausenzeiten/{id : \d+}
	 *
	 * Passt die Pausenzeit der Schule mit der angebenen ID an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Katalog-Daten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 404: Kein Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<StundenplanPausenzeit>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchPausenzeit(data : Partial<StundenplanPausenzeit>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/schule/pausenzeiten/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = StundenplanPausenzeit.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deletePausenzeit für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/pausenzeiten/{id : \d+}
	 *
	 * Entfernt eine Pausenzeit der Schule.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Pausenzeit wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanPausenzeit
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.
	 *   Code 404: Keine Pausenzeit vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Pausenzeit wurde erfolgreich entfernt.
	 */
	public async deletePausenzeit(schema : string, id : number) : Promise<StundenplanPausenzeit> {
		const path = "/db/{schema}/schule/pausenzeiten/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return StundenplanPausenzeit.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode addPausenzeit für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/pausenzeiten/create
	 *
	 * Erstellt eine neue Pausenzeit für die Schule und gibt das zugehörige Objekt zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Katalogs besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Die Pausenzeit wurde erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanPausenzeit
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Pausenzeit für die Schule anzulegen.
	 *   Code 404: Die Katalogdaten wurden nicht gefunden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<StundenplanPausenzeit>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Pausenzeit wurde erfolgreich hinzugefügt.
	 */
	public async addPausenzeit(data : Partial<StundenplanPausenzeit>, schema : string) : Promise<StundenplanPausenzeit> {
		const path = "/db/{schema}/schule/pausenzeiten/create"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = StundenplanPausenzeit.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return StundenplanPausenzeit.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode addPausenzeiten für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/pausenzeiten/create/multiple
	 *
	 * Erstellt neue Pausenzeiten für die Schule und gibt die zugehörigen Objekt zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Katalogs besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Die Pausenzeiten wurden erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<StundenplanPausenzeit>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Pausenzeiten für die Schule anzulegen.
	 *   Code 404: Die Katalogdaten wurden nicht gefunden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<Partial<StundenplanPausenzeit>>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Pausenzeiten wurden erfolgreich hinzugefügt.
	 */
	public async addPausenzeiten(data : List<Partial<StundenplanPausenzeit>>, schema : string) : Promise<List<StundenplanPausenzeit>> {
		const path = "/db/{schema}/schule/pausenzeiten/create/multiple"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<StundenplanPausenzeit>).map(d => StundenplanPausenzeit.transpilerToJSONPatch(d)).join() + "]";
		const result : string = await super.postJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<StundenplanPausenzeit>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(StundenplanPausenzeit.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der DELETE-Methode deletePausenzeiten für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/pausenzeiten/delete/multiple
	 *
	 * Entfernt mehrere Pausenzeiten der Schule.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Pausenzeiten wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<StundenplanPausenzeit>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.
	 *   Code 404: Räume nicht vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Pausenzeiten wurde erfolgreich entfernt.
	 */
	public async deletePausenzeiten(data : List<number>, schema : string) : Promise<List<StundenplanPausenzeit>> {
		const path = "/db/{schema}/schule/pausenzeiten/delete/multiple"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = "[" + data.toArray().map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<StundenplanPausenzeit>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(StundenplanPausenzeit.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getRaeume für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/raeume
	 *
	 * Gibt den Katalog der Räume der Schule zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Katalog der Räume der Schule.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Raum>
	 *   Code 403: Der SVWS-Benutzer hat keine gültige Anmeldung.
	 *   Code 404: Keine Raum-Einträge gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Katalog der Räume der Schule.
	 */
	public async getRaeume(schema : string) : Promise<List<Raum>> {
		const path = "/db/{schema}/schule/raeume"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<Raum>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(Raum.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getRaum für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/raeume/{id : \d+}
	 *
	 * Gibt den Raum der Schule zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Raum der Schule
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Raum
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Katalog anzusehen.
	 *   Code 404: Kein Raum bei der Schule gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Raum der Schule
	 */
	public async getRaum(schema : string, id : number) : Promise<Raum> {
		const path = "/db/{schema}/schule/raeume/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return Raum.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchRaum für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/raeume/{id : \d+}
	 *
	 * Passt den Raum der Schule mit der angebenen ID an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Katalog-Daten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 404: Kein Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<Raum>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchRaum(data : Partial<Raum>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/schule/raeume/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = Raum.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteRaum für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/raeume/{id : \d+}
	 *
	 * Entfernt einen Raum der Schule.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Raum wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Raum
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.
	 *   Code 404: Kein Raum vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Raum wurde erfolgreich entfernt.
	 */
	public async deleteRaum(schema : string, id : number) : Promise<Raum> {
		const path = "/db/{schema}/schule/raeume/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return Raum.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode addRaum für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/raeume/create
	 *
	 * Erstellt einen neuen Raum für die Schule und gibt das zugehörige Objekt zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Katalogs besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Der Raum wurde erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Raum
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Raum für die Schule anzulegen.
	 *   Code 404: Die Katalogdaten wurden nicht gefunden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<Raum>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Raum wurde erfolgreich hinzugefügt.
	 */
	public async addRaum(data : Partial<Raum>, schema : string) : Promise<Raum> {
		const path = "/db/{schema}/schule/raeume/create"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = Raum.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return Raum.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode addRaeume für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/raeume/create/multiple
	 *
	 * Erstellt neue Räume für die Schule und gibt die zugehörigen Objekt zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Katalogs besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Die Räume wurden erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Raum>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Räume für die Schule anzulegen.
	 *   Code 404: Die Katalogdaten wurden nicht gefunden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<Partial<Raum>>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Räume wurden erfolgreich hinzugefügt.
	 */
	public async addRaeume(data : List<Partial<Raum>>, schema : string) : Promise<List<Raum>> {
		const path = "/db/{schema}/schule/raeume/create/multiple"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<Raum>).map(d => Raum.transpilerToJSONPatch(d)).join() + "]";
		const result : string = await super.postJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<Raum>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(Raum.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der DELETE-Methode deleteRaeume für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/raeume/delete/multiple
	 *
	 * Entfernt mehrere Räume der Schule.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Räume wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Raum>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.
	 *   Code 404: Räume nicht vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Räume wurde erfolgreich entfernt.
	 */
	public async deleteRaeume(data : List<number>, schema : string) : Promise<List<Raum>> {
		const path = "/db/{schema}/schule/raeume/delete/multiple"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = "[" + data.toArray().map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<Raum>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(Raum.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getReligionen für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/religionen
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Religionen bzw. Konfessionen unter Angabe der ID, der Bezeichnung sowie der Bezeichnung, welche auf dem Zeugnis erscheint, einem Statistik-Kürzel, einer Sortierreihenfolge und ob sie in der Anwendung sichtbar bzw. änderbar sein sollen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<ReligionEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen
	 */
	public async getReligionen(schema : string) : Promise<List<ReligionEintrag>> {
		const path = "/db/{schema}/schule/religionen"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<ReligionEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(ReligionEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getReligion für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/religionen/{id : \d+}
	 *
	 * Liest die Daten der Religion zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Daten der Religion
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: ReligionEintrag
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalogdaten anzusehen.
	 *   Code 404: Keine Religion mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Daten der Religion
	 */
	public async getReligion(schema : string, id : number) : Promise<ReligionEintrag> {
		const path = "/db/{schema}/schule/religionen/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return ReligionEintrag.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchReligion für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/religionen/{id : \d+}
	 *
	 * Passt die Religion-Stammdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern der Daten der Religion besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Religion-Daten integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Religion-Daten zu ändern.
	 *   Code 404: Keine Religion mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<ReligionEintrag>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchReligion(data : Partial<ReligionEintrag>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/schule/religionen/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = ReligionEintrag.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der POST-Methode createReligion für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/religionen/new
	 *
	 * Erstellt eine neue Religion und gibt sie zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Religion besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Religion wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: ReligionEintrag
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Religion anzulegen.
	 *   Code 404: Keine Religion  mit dem eingegebenen Kuerzel gefunden
	 *   Code 409: Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {ReligionEintrag} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Religion wurde erfolgreich angelegt.
	 */
	public async createReligion(data : ReligionEintrag, schema : string) : Promise<ReligionEintrag> {
		const path = "/db/{schema}/schule/religionen/new"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = ReligionEintrag.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return ReligionEintrag.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getKatalogSchuelerStatus für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/schueler/status
	 *
	 * Die Liste der Einträge aus dem Katalog Schüler-Status. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen für den Katalog Schüler-Status
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SchuelerstatusKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen für den Katalog Schüler-Status
	 */
	public async getKatalogSchuelerStatus(schema : string) : Promise<List<SchuelerstatusKatalogEintrag>> {
		const path = "/db/{schema}/schule/schueler/status"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchuelerstatusKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchuelerstatusKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getSchuleStammdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/stammdaten
	 *
	 * Liest die Stammdaten der Schule zum angegebenen Schema aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schuldaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Stammdaten der Schule
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SchuleStammdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Schuldaten anzusehen.
	 *   Code 404: Kein Eintrag mit dem angegebenen Schema gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Stammdaten der Schule
	 */
	public async getSchuleStammdaten(schema : string) : Promise<SchuleStammdaten> {
		const path = "/db/{schema}/schule/stammdaten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const text = result;
		return SchuleStammdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchSchuleStammdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/stammdaten
	 *
	 * Passt die Schul-Stammdaten an und speichert das Ergebnis in der Datenbank. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Schuldaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Schul-Stammdaten integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Schuldaten zu ändern.
	 *   Code 404: Kein Schüler-Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<SchuleStammdaten>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 */
	public async patchSchuleStammdaten(data : Partial<SchuleStammdaten>, schema : string) : Promise<void> {
		const path = "/db/{schema}/schule/stammdaten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = SchuleStammdaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getZeitraster für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/zeitraster
	 *
	 * Gibt den Zeitraster-Katalog der Schule zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Zeitraster-Katalog der Schule.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<StundenplanZeitraster>
	 *   Code 403: Der SVWS-Benutzer hat keine gültige Anmeldung.
	 *   Code 404: Keine Zeitraster-Einträge gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Zeitraster-Katalog der Schule.
	 */
	public async getZeitraster(schema : string) : Promise<List<StundenplanZeitraster>> {
		const path = "/db/{schema}/schule/zeitraster"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<StundenplanZeitraster>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(StundenplanZeitraster.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getZeitrasterEintrag für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/zeitraster/{id : \d+}
	 *
	 * Gibt den Zeitraster-Eintrag der Schule zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Zeitraster-Eintrag der Schule
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanZeitraster
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Katalog anzusehen.
	 *   Code 404: Kein Zeitraster-Eintrag bei der Schule gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Zeitraster-Eintrag der Schule
	 */
	public async getZeitrasterEintrag(schema : string, id : number) : Promise<StundenplanZeitraster> {
		const path = "/db/{schema}/schule/zeitraster/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return StundenplanZeitraster.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchZeitrasterEintrag für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/zeitraster/{id : \d+}
	 *
	 * Passt den Zeitraster-Eintrag der Schule mit der angebenen ID an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Katalog-Daten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 404: Kein Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<StundenplanZeitraster>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchZeitrasterEintrag(data : Partial<StundenplanZeitraster>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/schule/zeitraster/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = StundenplanZeitraster.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteZeitrasterEintrag für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/zeitraster/{id : \d+}
	 *
	 * Entfernt einen Zeitraster-Eintrag der Schule.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Zeitraster-Eintrag wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanZeitraster
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.
	 *   Code 404: Kein Zeitraster-Eintrag vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Zeitraster-Eintrag wurde erfolgreich entfernt.
	 */
	public async deleteZeitrasterEintrag(schema : string, id : number) : Promise<StundenplanZeitraster> {
		const path = "/db/{schema}/schule/zeitraster/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return StundenplanZeitraster.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode addZeitrasterEintrag für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/zeitraster/create
	 *
	 * Erstellt einen neue Zeitraster-Eintrag für die Schule und gibt das zugehörige Objekt zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Katalogs besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Der Zeitraster-Eintrag wurde erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanZeitraster
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Zeitraster-Eintrag für die Schule anzulegen.
	 *   Code 404: Die Katalogdaten wurden nicht gefunden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<StundenplanZeitraster>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Zeitraster-Eintrag wurde erfolgreich hinzugefügt.
	 */
	public async addZeitrasterEintrag(data : Partial<StundenplanZeitraster>, schema : string) : Promise<StundenplanZeitraster> {
		const path = "/db/{schema}/schule/zeitraster/create"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = StundenplanZeitraster.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return StundenplanZeitraster.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode addZeitrasterEintraege für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/zeitraster/create/multiple
	 *
	 * Erstellt neue Zeitraster-Einträge für die Schule und gibt die zugehörigen Objekt zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Katalogs besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Die Pausenzeiten wurden erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<StundenplanZeitraster>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Zeitraster-Einträge für die Schule anzulegen.
	 *   Code 404: Die Katalogdaten wurden nicht gefunden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<Partial<StundenplanZeitraster>>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Pausenzeiten wurden erfolgreich hinzugefügt.
	 */
	public async addZeitrasterEintraege(data : List<Partial<StundenplanZeitraster>>, schema : string) : Promise<List<StundenplanZeitraster>> {
		const path = "/db/{schema}/schule/zeitraster/create/multiple"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<StundenplanZeitraster>).map(d => StundenplanZeitraster.transpilerToJSONPatch(d)).join() + "]";
		const result : string = await super.postJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<StundenplanZeitraster>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(StundenplanZeitraster.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der DELETE-Methode deleteZeitrasterEintraege für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/zeitraster/delete/multiple
	 *
	 * Entfernt mehrere Zeitraster-Einträge der Schule.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Zeitraster-Einträge wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<StundenplanZeitraster>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.
	 *   Code 404: Zeitraster-Einträge nicht vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Zeitraster-Einträge wurde erfolgreich entfernt.
	 */
	public async deleteZeitrasterEintraege(data : List<number>, schema : string) : Promise<List<StundenplanZeitraster>> {
		const path = "/db/{schema}/schule/zeitraster/delete/multiple"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = "[" + data.toArray().map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<StundenplanZeitraster>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(StundenplanZeitraster.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der PATCH-Methode patchZeitrasterEintraege für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/zeitraster/patch/multiple
	 *
	 * Passt die Zeitrastereinträge an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Stundenplandaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 404: Kein Eintrag für mindestens eine der IDs der Daten gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<Partial<StundenplanZeitraster>>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 */
	public async patchZeitrasterEintraege(data : List<Partial<StundenplanZeitraster>>, schema : string) : Promise<void> {
		const path = "/db/{schema}/schule/zeitraster/patch/multiple"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<StundenplanZeitraster>).map(d => StundenplanZeitraster.transpilerToJSONPatch(d)).join() + "]";
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getStundenplan für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/{id : \d+}
	 *
	 * Gibt die grundlegenden Daten des Stundeplans mit der angegebenen ID zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Daten des Stundenplans
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Stundenplan
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Stundenplandaten anzusehen.
	 *   Code 404: Keine Stundenplandaten gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Daten des Stundenplans
	 */
	public async getStundenplan(schema : string, id : number) : Promise<Stundenplan> {
		const path = "/db/{schema}/stundenplan/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return Stundenplan.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchStundenplan für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/{id : \d+}
	 *
	 * Passt die Stundenplandaten des Stundenplans mit der angebenen ID an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Stundenplandaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 404: Kein Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<Stundenplan>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchStundenplan(data : Partial<Stundenplan>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/stundenplan/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = Stundenplan.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteStundenplan für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/{id : \d+}
	 *
	 * Entfernt einen Stundenplan.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen des Stundenplans hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Der Stundenplan wurde erfolgreich entfernt.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Stundenplan zu entfernen.
	 *   Code 404: Der Stundenplan ist nicht vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async deleteStundenplan(schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/stundenplan/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		await super.deleteJSON(path, null);
		return;
	}


	/**
	 * Implementierung der POST-Methode addStundenplanAufsichtsbereich für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/{id : \d+}/aufsichtsbereiche/create
	 *
	 * Erstellt einen neuen Aufsichtsbereich für den angegebenen Stundenplan und gibt das zugehörige Objekt zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Stundenplans besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Der Aufsichtsbereich wurde erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanAufsichtsbereich
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Raum für einen Stundenplan anzulegen.
	 *   Code 404: Die Stundenplandaten wurden nicht gefunden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<StundenplanAufsichtsbereich>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Aufsichtsbereich wurde erfolgreich hinzugefügt.
	 */
	public async addStundenplanAufsichtsbereich(data : Partial<StundenplanAufsichtsbereich>, schema : string, id : number) : Promise<StundenplanAufsichtsbereich> {
		const path = "/db/{schema}/stundenplan/{id : \\d+}/aufsichtsbereiche/create"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = StundenplanAufsichtsbereich.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return StundenplanAufsichtsbereich.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode addStundenplanAufsichtsbereiche für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/{id : \d+}/aufsichtsbereiche/create/multiple
	 *
	 * Erstellt mehrere neue Aufsichtsbereiche für den angegebenen Stundenplan und gibt das zugehörige Objekt zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Stundenplans besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Die Aufsichtsbereiche wurden erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<StundenplanAufsichtsbereich>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Aufsichtsbereiche für einen Stundenplan anzulegen.
	 *   Code 404: Die Stundenplandaten wurden nicht gefunden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<Partial<StundenplanAufsichtsbereich>>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Aufsichtsbereiche wurden erfolgreich hinzugefügt.
	 */
	public async addStundenplanAufsichtsbereiche(data : List<Partial<StundenplanAufsichtsbereich>>, schema : string, id : number) : Promise<List<StundenplanAufsichtsbereich>> {
		const path = "/db/{schema}/stundenplan/{id : \\d+}/aufsichtsbereiche/create/multiple"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = "[" + (data.toArray() as Array<StundenplanAufsichtsbereich>).map(d => StundenplanAufsichtsbereich.transpilerToJSONPatch(d)).join() + "]";
		const result : string = await super.postJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<StundenplanAufsichtsbereich>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(StundenplanAufsichtsbereich.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der DELETE-Methode deleteStundenplanAufsichtsbereiche für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/{id : \d+}/aufsichtsbereiche/delete/multiple
	 *
	 * Entfernt mehrere Aufsichtsbereiche eines Stundenplans.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Stundenplans hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Aufsichtsbereiche wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<StundenplanAufsichtsbereich>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Stundenplan zu bearbeiten.
	 *   Code 404: Aufsichtsbereich nicht vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Aufsichtsbereiche wurde erfolgreich entfernt.
	 */
	public async deleteStundenplanAufsichtsbereiche(data : List<number>, schema : string, id : number) : Promise<List<StundenplanAufsichtsbereich>> {
		const path = "/db/{schema}/stundenplan/{id : \\d+}/aufsichtsbereiche/delete/multiple"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = "[" + data.toArray().map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<StundenplanAufsichtsbereich>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(StundenplanAufsichtsbereich.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der DELETE-Methode deleteStundenplanPausenaufsichten für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/{id : \d+}/pausenaufsicht/delete/multiple
	 *
	 * Entfernt mehrere Pausenaufsichten eines Stundenplans.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Stundenplans hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Pausenaufsichten wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<StundenplanPausenaufsicht>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Stundenplan zu bearbeiten.
	 *   Code 404: Pausenaufsicht nicht vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Pausenaufsichten wurde erfolgreich entfernt.
	 */
	public async deleteStundenplanPausenaufsichten(data : List<number>, schema : string, id : number) : Promise<List<StundenplanPausenaufsicht>> {
		const path = "/db/{schema}/stundenplan/{id : \\d+}/pausenaufsicht/delete/multiple"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = "[" + data.toArray().map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<StundenplanPausenaufsicht>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(StundenplanPausenaufsicht.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getStundenplanPausenaufsichten für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/{id : \d+}/pausenaufsichten
	 *
	 * Gibt die Pausenaufsichten des Stundeplans mit der angegebenen ID zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Pausenaufsichten des Stundenplans
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<StundenplanPausenaufsicht>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Stundenplandaten anzusehen.
	 *   Code 404: Keine Stundenplandaten gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Pausenaufsichten des Stundenplans
	 */
	public async getStundenplanPausenaufsichten(schema : string, id : number) : Promise<List<StundenplanPausenaufsicht>> {
		const path = "/db/{schema}/stundenplan/{id : \\d+}/pausenaufsichten"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<StundenplanPausenaufsicht>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(StundenplanPausenaufsicht.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode addStundenplanPausenzeit für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/{id : \d+}/pausenzeiten/create
	 *
	 * Erstellt eine neue Pausenzeit für den angegebenen Stundenplan und gibt das zugehörige Objekt zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Stundenplans besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Die Pausenzeit wurde erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanPausenzeit
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Pausenzeit für einen Stundenplan anzulegen.
	 *   Code 404: Die Stundenplandaten wurden nicht gefunden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<StundenplanPausenzeit>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Pausenzeit wurde erfolgreich hinzugefügt.
	 */
	public async addStundenplanPausenzeit(data : Partial<StundenplanPausenzeit>, schema : string, id : number) : Promise<StundenplanPausenzeit> {
		const path = "/db/{schema}/stundenplan/{id : \\d+}/pausenzeiten/create"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = StundenplanPausenzeit.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return StundenplanPausenzeit.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode addStundenplanPausenzeiten für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/{id : \d+}/pausenzeiten/create/multiple
	 *
	 * Erstellt mehrere neue Pausenzeiten für den angegebenen Stundenplan und gibt das zugehörige Objekt zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Stundenplans besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Die Pausenzeiten wurden erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<StundenplanPausenzeit>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Pausenzeiten für einen Stundenplan anzulegen.
	 *   Code 404: Die Stundenplandaten wurden nicht gefunden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<Partial<StundenplanPausenzeit>>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Pausenzeiten wurden erfolgreich hinzugefügt.
	 */
	public async addStundenplanPausenzeiten(data : List<Partial<StundenplanPausenzeit>>, schema : string, id : number) : Promise<List<StundenplanPausenzeit>> {
		const path = "/db/{schema}/stundenplan/{id : \\d+}/pausenzeiten/create/multiple"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = "[" + (data.toArray() as Array<StundenplanPausenzeit>).map(d => StundenplanPausenzeit.transpilerToJSONPatch(d)).join() + "]";
		const result : string = await super.postJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<StundenplanPausenzeit>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(StundenplanPausenzeit.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der DELETE-Methode deleteStundenplanPausenzeiten für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/{id : \d+}/pausenzeiten/delete/multiple
	 *
	 * Entfernt mehrere Pausenzeiten eines Stundenplans.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Stundenplans hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Pausenzeiten wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<StundenplanPausenzeit>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Stundenplan zu bearbeiten.
	 *   Code 404: Pausenzeit nicht vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Pausenzeiten wurde erfolgreich entfernt.
	 */
	public async deleteStundenplanPausenzeiten(data : List<number>, schema : string, id : number) : Promise<List<StundenplanPausenzeit>> {
		const path = "/db/{schema}/stundenplan/{id : \\d+}/pausenzeiten/delete/multiple"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = "[" + data.toArray().map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<StundenplanPausenzeit>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(StundenplanPausenzeit.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode addStundenplanRaum für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/{id : \d+}/raeume/create
	 *
	 * Erstellt einen neuen Raum für den angegebenen Stundenplan und gibt das zugehörige Objekt zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Stundenplans besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Der Raum wurde erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanRaum
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Raum für einen Stundenplan anzulegen.
	 *   Code 404: Die Stundenplandaten wurden nicht gefunden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<StundenplanRaum>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Raum wurde erfolgreich hinzugefügt.
	 */
	public async addStundenplanRaum(data : Partial<StundenplanRaum>, schema : string, id : number) : Promise<StundenplanRaum> {
		const path = "/db/{schema}/stundenplan/{id : \\d+}/raeume/create"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = StundenplanRaum.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return StundenplanRaum.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode addStundenplanRaeume für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/{id : \d+}/raeume/create/multiple
	 *
	 * Erstellt mehrere neue Räume für den angegebenen Stundenplan und gibt das zugehörige Objekt zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Stundenplans besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Die Räume wurden erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<StundenplanRaum>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Räume für einen Stundenplan anzulegen.
	 *   Code 404: Die Stundenplandaten wurden nicht gefunden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<Partial<StundenplanRaum>>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Räume wurden erfolgreich hinzugefügt.
	 */
	public async addStundenplanRaeume(data : List<Partial<StundenplanRaum>>, schema : string, id : number) : Promise<List<StundenplanRaum>> {
		const path = "/db/{schema}/stundenplan/{id : \\d+}/raeume/create/multiple"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = "[" + (data.toArray() as Array<StundenplanRaum>).map(d => StundenplanRaum.transpilerToJSONPatch(d)).join() + "]";
		const result : string = await super.postJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<StundenplanRaum>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(StundenplanRaum.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der DELETE-Methode deleteStundenplanRaeume für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/{id : \d+}/raeume/delete/multiple
	 *
	 * Entfernt mehrere Räume eines Stundenplans.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Stundenplans hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Räume wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<StundenplanRaum>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Stundenplan zu bearbeiten.
	 *   Code 404: Raum nicht vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Räume wurde erfolgreich entfernt.
	 */
	public async deleteStundenplanRaeume(data : List<number>, schema : string, id : number) : Promise<List<StundenplanRaum>> {
		const path = "/db/{schema}/stundenplan/{id : \\d+}/raeume/delete/multiple"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = "[" + data.toArray().map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<StundenplanRaum>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(StundenplanRaum.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der DELETE-Methode deleteStundenplanUnterrichte für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/{id : \d+}/unterricht/delete/multiple
	 *
	 * Entfernt mehrere Unterrichte eines Stundenplans.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Stundenplans hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Unterrichte wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<StundenplanUnterricht>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Stundenplan zu bearbeiten.
	 *   Code 404: Unterricht nicht vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Unterrichte wurde erfolgreich entfernt.
	 */
	public async deleteStundenplanUnterrichte(data : List<number>, schema : string, id : number) : Promise<List<StundenplanUnterricht>> {
		const path = "/db/{schema}/stundenplan/{id : \\d+}/unterricht/delete/multiple"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = "[" + data.toArray().map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<StundenplanUnterricht>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(StundenplanUnterricht.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getStundenplanUnterrichte für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/{id : \d+}/unterrichte
	 *
	 * Gibt die Unterrichte des Stundeplans mit der angegebenen ID zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Unterrichte des Stundenplans
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<StundenplanUnterricht>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Stundenplandaten anzusehen.
	 *   Code 404: Keine Stundenplandaten gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Unterrichte des Stundenplans
	 */
	public async getStundenplanUnterrichte(schema : string, id : number) : Promise<List<StundenplanUnterricht>> {
		const path = "/db/{schema}/stundenplan/{id : \\d+}/unterrichte"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<StundenplanUnterricht>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(StundenplanUnterricht.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getStundenplanUnterrichtsverteilung für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/{id : \d+}/unterrichtsverteilung
	 *
	 * Gibt die Daten zur Unterrichtsverteilung des Stundenplans mit der angegebenen ID zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Daten zur Unterrichtsverteilung des Stundenplans
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanUnterrichtsverteilung
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Stundenplandaten anzusehen.
	 *   Code 404: Keine Stundenplandaten gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Daten zur Unterrichtsverteilung des Stundenplans
	 */
	public async getStundenplanUnterrichtsverteilung(schema : string, id : number) : Promise<StundenplanUnterrichtsverteilung> {
		const path = "/db/{schema}/stundenplan/{id : \\d+}/unterrichtsverteilung"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return StundenplanUnterrichtsverteilung.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getStundenplanZeitraster für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/{id : \d+}/zeitraster
	 *
	 * Erstellt eine Liste der Einträge aus dem Zeitraster des angegebenen Stundeplans. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Zeitraster-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<StundenplanZeitraster>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um das Zeitraster anzusehen.
	 *   Code 404: Keine Zeitraster-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Eine Liste von Zeitraster-Einträgen
	 */
	public async getStundenplanZeitraster(schema : string, id : number) : Promise<List<StundenplanZeitraster>> {
		const path = "/db/{schema}/stundenplan/{id : \\d+}/zeitraster"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<StundenplanZeitraster>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(StundenplanZeitraster.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode addStundenplanZeitrasterEintrag für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/{id : \d+}/zeitraster/create
	 *
	 * Erstellt einen neuen Zeitrastereintrag für den angegebenen Stundenplan und gibt das zugehörige Objekt zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Stundenplans besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Der Zeitrastereintrag wurde erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanZeitraster
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Zeitrastereintrag für einen Stundenplan anzulegen.
	 *   Code 404: Die Stundenplandaten wurden nicht gefunden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<StundenplanZeitraster>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Zeitrastereintrag wurde erfolgreich hinzugefügt.
	 */
	public async addStundenplanZeitrasterEintrag(data : Partial<StundenplanZeitraster>, schema : string, id : number) : Promise<StundenplanZeitraster> {
		const path = "/db/{schema}/stundenplan/{id : \\d+}/zeitraster/create"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = StundenplanZeitraster.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return StundenplanZeitraster.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode addStundenplanZeitrasterEintraege für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/{id : \d+}/zeitraster/create/multiple
	 *
	 * Erstellt mehrere neue Zeitrastereinträge für den angegebenen Stundenplan und gibt das zugehörige Objekt zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Stundenplans besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Die Zeitrastereinträge wurden erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<StundenplanZeitraster>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Zeitrastereinträge für einen Stundenplan anzulegen.
	 *   Code 404: Die Stundenplandaten wurden nicht gefunden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<Partial<StundenplanZeitraster>>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Zeitrastereinträge wurden erfolgreich hinzugefügt.
	 */
	public async addStundenplanZeitrasterEintraege(data : List<Partial<StundenplanZeitraster>>, schema : string, id : number) : Promise<List<StundenplanZeitraster>> {
		const path = "/db/{schema}/stundenplan/{id : \\d+}/zeitraster/create/multiple"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = "[" + (data.toArray() as Array<StundenplanZeitraster>).map(d => StundenplanZeitraster.transpilerToJSONPatch(d)).join() + "]";
		const result : string = await super.postJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<StundenplanZeitraster>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(StundenplanZeitraster.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der DELETE-Methode deleteStundenplanZeitrasterEintraege für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/{id : \d+}/zeitraster/delete/multiple
	 *
	 * Entfernt mehrere Zeitrastereinträge eines Stundenplans.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Stundenplans hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Zeitrastereinträge wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<StundenplanZeitraster>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Stundenplan zu bearbeiten.
	 *   Code 404: Kein Zeitrastereintrag vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Zeitrastereinträge wurde erfolgreich entfernt.
	 */
	public async deleteStundenplanZeitrasterEintraege(data : List<number>, schema : string, id : number) : Promise<List<StundenplanZeitraster>> {
		const path = "/db/{schema}/stundenplan/{id : \\d+}/zeitraster/delete/multiple"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = "[" + data.toArray().map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<StundenplanZeitraster>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(StundenplanZeitraster.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getStundenplanLehrer für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/{idStundenplan : \d+}/lehrer/{id : \d+}
	 *
	 * Gibt den Lehrer eines Stundenplans zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Lehrer
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanLehrer
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Stundenplan anzusehen.
	 *   Code 404: Kein Raum eines Stundenplans gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} idStundenplan - der Pfad-Parameter idStundenplan
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Lehrer
	 */
	public async getStundenplanLehrer(schema : string, idStundenplan : number, id : number) : Promise<StundenplanLehrer> {
		const path = "/db/{schema}/stundenplan/{idStundenplan : \\d+}/lehrer/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{idStundenplan\s*(:[^}]+)?}/g, idStundenplan.toString())
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return StundenplanLehrer.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getStundenplanAufsichtsbereich für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/aufsichtsbereich/{id : \d+}
	 *
	 * Gibt den Aufsichtsbereich eines Stundenplans zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Raum
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanAufsichtsbereich
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Stundenplan anzusehen.
	 *   Code 404: Kein Aufsichtsbereich eines Stundenplans gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Raum
	 */
	public async getStundenplanAufsichtsbereich(schema : string, id : number) : Promise<StundenplanAufsichtsbereich> {
		const path = "/db/{schema}/stundenplan/aufsichtsbereich/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return StundenplanAufsichtsbereich.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchStundenplanAufsichtsbereich für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/aufsichtsbereich/{id : \d+}
	 *
	 * Passt den Aufsichtsbereich mit der angebenen ID an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Stundenplandaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 404: Kein Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<StundenplanAufsichtsbereich>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchStundenplanAufsichtsbereich(data : Partial<StundenplanAufsichtsbereich>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/stundenplan/aufsichtsbereich/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = StundenplanAufsichtsbereich.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteStundenplanAufsichtsbereich für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/aufsichtsbereiche/{id : \d+}
	 *
	 * Entfernt einen Aufsichtsbereich eines Stundenplans.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Stundenplans hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Aufsichtsbereich wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanAufsichtsbereich
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Stundenplan zu bearbeiten.
	 *   Code 404: Kein Aufsichtsbereich vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Aufsichtsbereich wurde erfolgreich entfernt.
	 */
	public async deleteStundenplanAufsichtsbereich(schema : string, id : number) : Promise<StundenplanAufsichtsbereich> {
		const path = "/db/{schema}/stundenplan/aufsichtsbereiche/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return StundenplanAufsichtsbereich.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode addStundenplan für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/create/{idSchuljahresabschnitt : \d+}
	 *
	 * Erstellt einen neuen (leeren) Stundenplan für den angegebenen Schuljahresabschnitt und gibt den zugehörigen Listeneintrag zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Stundenplans besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Der Stundenplan wurde erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanListeEintrag
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Stundenplan anzulegen.
	 *   Code 404: Der Schuljahresabschnitt wurde nicht gefunden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} idSchuljahresabschnitt - der Pfad-Parameter idSchuljahresabschnitt
	 *
	 * @returns Der Stundenplan wurde erfolgreich hinzugefügt.
	 */
	public async addStundenplan(schema : string, idSchuljahresabschnitt : number) : Promise<StundenplanListeEintrag> {
		const path = "/db/{schema}/stundenplan/create/{idSchuljahresabschnitt : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{idSchuljahresabschnitt\s*(:[^}]+)?}/g, idSchuljahresabschnitt.toString());
		const result : string = await super.postJSON(path, null);
		const text = result;
		return StundenplanListeEintrag.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getStundenplanKalenderwochenzuordnung für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/kalenderwochen/{id : \d+}
	 *
	 * Gibt die Kalenderwochen-Zuordnung eines Stundeplans zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Kalenderwochen-Zuordnung
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanKalenderwochenzuordnung
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Stundenplan anzusehen.
	 *   Code 404: Keine Kalenderwochen-Zuordnung eines Stundenplans gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Kalenderwochen-Zuordnung
	 */
	public async getStundenplanKalenderwochenzuordnung(schema : string, id : number) : Promise<StundenplanKalenderwochenzuordnung> {
		const path = "/db/{schema}/stundenplan/kalenderwochen/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return StundenplanKalenderwochenzuordnung.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchStundenplanKalenderwochenzuordnung für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/kalenderwochen/{id : \d+}
	 *
	 * Passt die Kalenderwochen-Zuordnung mit der angebenen ID an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Stundenplandaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 404: Kein Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<StundenplanKalenderwochenzuordnung>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchStundenplanKalenderwochenzuordnung(data : Partial<StundenplanKalenderwochenzuordnung>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/stundenplan/kalenderwochen/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = StundenplanKalenderwochenzuordnung.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getStundenplanliste für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/liste
	 *
	 * Erstellt eine Liste der Stundenpläne. Die Stundenpläne sind anhand des Schuljahresabschnitt und der Gültigkeit sortiert.Es wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplanlisten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste der Stundenpläne
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<StundenplanListeEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Stundenplanlisten anzusehen.
	 *   Code 404: Keine Stundenpläne gefunden
	 *     - Mime-Type: text/plain
	 *     - Rückgabe-Typ: String
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste der Stundenpläne
	 */
	public async getStundenplanliste(schema : string) : Promise<List<StundenplanListeEintrag>> {
		const path = "/db/{schema}/stundenplan/liste"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<StundenplanListeEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(StundenplanListeEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getStundenplanlisteFuerAbschnitt für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/liste/{abschnitt : \d+}
	 *
	 * Erstellt eine Liste der Stundenpläne des angegebenen Schuljahresabschnitts. Die Stundenpläne sind anhand der Gültigkeit sortiert.Es wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplanlisten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste der Stundenpläne
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<StundenplanListeEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Stundenplanlisten anzusehen.
	 *   Code 404: Keine Stundenpläne gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abschnitt - der Pfad-Parameter abschnitt
	 *
	 * @returns Eine Liste der Stundenpläne
	 */
	public async getStundenplanlisteFuerAbschnitt(schema : string, abschnitt : number) : Promise<List<StundenplanListeEintrag>> {
		const path = "/db/{schema}/stundenplan/liste/{abschnitt : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{abschnitt\s*(:[^}]+)?}/g, abschnitt.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<StundenplanListeEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(StundenplanListeEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getStundenplanPausenaufsicht für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/pausenaufsicht/{id : \d+}
	 *
	 * Gibt eine Pausenaufsicht eines Stundeplans zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Pausenaufsicht
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanPausenaufsicht
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Stundenplan anzusehen.
	 *   Code 404: Keine Pausenaufsicht eines Stundenplans gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Pausenaufsicht
	 */
	public async getStundenplanPausenaufsicht(schema : string, id : number) : Promise<StundenplanPausenaufsicht> {
		const path = "/db/{schema}/stundenplan/pausenaufsicht/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return StundenplanPausenaufsicht.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchStundenplanPausenaufsicht für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/pausenaufsicht/{id : \d+}
	 *
	 * Passt die Pausenaufsicht mit der angebenen ID an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Stundenplandaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 404: Kein Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<StundenplanPausenaufsicht>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchStundenplanPausenaufsicht(data : Partial<StundenplanPausenaufsicht>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/stundenplan/pausenaufsicht/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = StundenplanPausenaufsicht.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteStundenplanPausenaufsicht für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/pausenaufsicht/{id : \d+}
	 *
	 * Entfernt eine Pausenaufsicht eines Stundenplans.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Stundenplans hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Pausenaufsicht wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanPausenaufsicht
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Stundenplan zu bearbeiten.
	 *   Code 404: Keine Pausenaufsicht vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Pausenaufsicht wurde erfolgreich entfernt.
	 */
	public async deleteStundenplanPausenaufsicht(schema : string, id : number) : Promise<StundenplanPausenaufsicht> {
		const path = "/db/{schema}/stundenplan/pausenaufsicht/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return StundenplanPausenaufsicht.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode addStundenplanPausenaufsicht für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/pausenaufsicht/create
	 *
	 * Erstellt eine neue Pausenaufsicht für die angebene Pausenzeit eines Stundenplans und gibt das zugehörige Objekt zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Stundenplans besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Die Pausenaufsicht wurde erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanPausenaufsicht
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Pausenaufsicht für einen Stundenplan anzulegen.
	 *   Code 404: Die Stundenplandaten wurden nicht gefunden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<StundenplanPausenaufsicht>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Pausenaufsicht wurde erfolgreich hinzugefügt.
	 */
	public async addStundenplanPausenaufsicht(data : Partial<StundenplanPausenaufsicht>, schema : string) : Promise<StundenplanPausenaufsicht> {
		const path = "/db/{schema}/stundenplan/pausenaufsicht/create"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = StundenplanPausenaufsicht.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return StundenplanPausenaufsicht.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode addStundenplanPausenaufsichten für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/pausenaufsicht/create/multiple
	 *
	 * Erstellt neue Pausenaufsichten für einen Stundenplan und gibt die zugehörigen Objekte zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Stundenplans besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Die Pausenaufsichten wurden erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<StundenplanPausenaufsicht>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Pausenaufsichten für einen Stundenplan anzulegen.
	 *   Code 404: Die Stundenplandaten wurden nicht gefunden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<Partial<StundenplanPausenaufsicht>>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Pausenaufsichten wurden erfolgreich hinzugefügt.
	 */
	public async addStundenplanPausenaufsichten(data : List<Partial<StundenplanPausenaufsicht>>, schema : string) : Promise<List<StundenplanPausenaufsicht>> {
		const path = "/db/{schema}/stundenplan/pausenaufsicht/create/multiple"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<StundenplanPausenaufsicht>).map(d => StundenplanPausenaufsicht.transpilerToJSONPatch(d)).join() + "]";
		const result : string = await super.postJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<StundenplanPausenaufsicht>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(StundenplanPausenaufsicht.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getStundenplanPausenzeit für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/pausenzeiten/{id : \d+}
	 *
	 * Gibt die Pausenzeit eines Stundenplans zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Pausenzeit
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanPausenzeit
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Stundenplan anzusehen.
	 *   Code 404: Keine Pausenzeit eines Stundenplans gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Pausenzeit
	 */
	public async getStundenplanPausenzeit(schema : string, id : number) : Promise<StundenplanPausenzeit> {
		const path = "/db/{schema}/stundenplan/pausenzeiten/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return StundenplanPausenzeit.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchStundenplanPausenzeit für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/pausenzeiten/{id : \d+}
	 *
	 * Passt die Pausenzeit mit der angebenen ID an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Stundenplandaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 404: Kein Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<StundenplanPausenzeit>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchStundenplanPausenzeit(data : Partial<StundenplanPausenzeit>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/stundenplan/pausenzeiten/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = StundenplanPausenzeit.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteStundenplanPausenzeit für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/pausenzeiten/{id : \d+}
	 *
	 * Entfernt eine Pausenzeit eines Stundenplans.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Stundenplans hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Pausenzeit wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanPausenzeit
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Stundenplan zu bearbeiten.
	 *   Code 404: Keine Pausenzeit vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Pausenzeit wurde erfolgreich entfernt.
	 */
	public async deleteStundenplanPausenzeit(schema : string, id : number) : Promise<StundenplanPausenzeit> {
		const path = "/db/{schema}/stundenplan/pausenzeiten/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return StundenplanPausenzeit.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getKlassenStundenplan für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/plaene/{id : \d+}/klasse/{klasse_id : \d+}
	 *
	 * Erstellt den angebebenen Stundeplan in Bezug auf die angegebene Klasse. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Klassen-Stundenplan
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanKomplett
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Stundenplan anzusehen.
	 *   Code 404: Keinen Stundenplan gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 * @param {number} klasse_id - der Pfad-Parameter klasse_id
	 *
	 * @returns Der Klassen-Stundenplan
	 */
	public async getKlassenStundenplan(schema : string, id : number, klasse_id : number) : Promise<StundenplanKomplett> {
		const path = "/db/{schema}/stundenplan/plaene/{id : \\d+}/klasse/{klasse_id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString())
			.replace(/{klasse_id\s*(:[^}]+)?}/g, klasse_id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return StundenplanKomplett.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getLehrerStundenplan für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/plaene/{id : \d+}/lehrer/{lehrer_id : \d+}
	 *
	 * Erstellt den angebebenen Stundeplan in Bezug auf den angegebenen Lehrer. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Lehrer-Stundenplan
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanKomplett
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Stundenplan anzusehen.
	 *   Code 404: Keinen Stundenplan gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 * @param {number} lehrer_id - der Pfad-Parameter lehrer_id
	 *
	 * @returns Der Lehrer-Stundenplan
	 */
	public async getLehrerStundenplan(schema : string, id : number, lehrer_id : number) : Promise<StundenplanKomplett> {
		const path = "/db/{schema}/stundenplan/plaene/{id : \\d+}/lehrer/{lehrer_id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString())
			.replace(/{lehrer_id\s*(:[^}]+)?}/g, lehrer_id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return StundenplanKomplett.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getSchuelerStundenplan für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/plaene/{id : \d+}/schueler/{schueler_id : \d+}
	 *
	 * Erstellt den angebebenen Stundeplan in Bezug auf den angegebenen Schüler. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Schüler-Stundenplan
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanKomplett
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Stundenplan anzusehen.
	 *   Code 404: Keinen Stundenplan gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 * @param {number} schueler_id - der Pfad-Parameter schueler_id
	 *
	 * @returns Der Schüler-Stundenplan
	 */
	public async getSchuelerStundenplan(schema : string, id : number, schueler_id : number) : Promise<StundenplanKomplett> {
		const path = "/db/{schema}/stundenplan/plaene/{id : \\d+}/schueler/{schueler_id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString())
			.replace(/{schueler_id\s*(:[^}]+)?}/g, schueler_id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return StundenplanKomplett.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getStundenplanRaum für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/raeume/{id : \d+}
	 *
	 * Gibt den Raum eines Stundenplans zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Raum
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanRaum
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Stundenplan anzusehen.
	 *   Code 404: Kein Raum eines Stundenplans gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Raum
	 */
	public async getStundenplanRaum(schema : string, id : number) : Promise<StundenplanRaum> {
		const path = "/db/{schema}/stundenplan/raeume/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return StundenplanRaum.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchStundenplanRaum für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/raeume/{id : \d+}
	 *
	 * Passt den Raum mit der angebenen ID an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Stundenplandaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 404: Kein Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<StundenplanRaum>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchStundenplanRaum(data : Partial<StundenplanRaum>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/stundenplan/raeume/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = StundenplanRaum.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteStundenplanRaum für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/raeume/{id : \d+}
	 *
	 * Entfernt einen Raum eines Stundenplans.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Stundenplans hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Raum wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanRaum
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Stundenplan zu bearbeiten.
	 *   Code 404: Kein Raum vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Raum wurde erfolgreich entfernt.
	 */
	public async deleteStundenplanRaum(schema : string, id : number) : Promise<StundenplanRaum> {
		const path = "/db/{schema}/stundenplan/raeume/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return StundenplanRaum.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getStundenplanSchiene für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/schienen/{id : \d+}
	 *
	 * Gibt die Schiene eines Stundeplans zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Schiene
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanSchiene
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Stundenplan anzusehen.
	 *   Code 404: Keine Schiene eines Stundenplans gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Schiene
	 */
	public async getStundenplanSchiene(schema : string, id : number) : Promise<StundenplanSchiene> {
		const path = "/db/{schema}/stundenplan/schienen/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return StundenplanSchiene.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchStundenplanSchiene für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/schienen/{id : \d+}
	 *
	 * Passt die Schiene mit der angebenen ID an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Stundenplandaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 404: Kein Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<StundenplanSchiene>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchStundenplanSchiene(data : Partial<StundenplanSchiene>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/stundenplan/schienen/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = StundenplanSchiene.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getStundenplanUnterricht für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/unterricht/{id : \d+}
	 *
	 * Gibt einen Unterricht eines Stundeplans zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Unterricht
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanUnterricht
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Stundenplan anzusehen.
	 *   Code 404: Kein Unterricht eines Stundenplans gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Unterricht
	 */
	public async getStundenplanUnterricht(schema : string, id : number) : Promise<StundenplanUnterricht> {
		const path = "/db/{schema}/stundenplan/unterricht/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return StundenplanUnterricht.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchStundenplanUnterricht für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/unterricht/{id : \d+}
	 *
	 * Passt den Unterricht mit der angebenen ID an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Stundenplandaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 404: Kein Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<StundenplanUnterricht>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchStundenplanUnterricht(data : Partial<StundenplanUnterricht>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/stundenplan/unterricht/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = StundenplanUnterricht.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteStundenplanUnterricht für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/unterricht/{id : \d+}
	 *
	 * Entfernt einen Unterricht eines Stundenplans.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Stundenplans hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Unterricht wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanUnterricht
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Stundenplan zu bearbeiten.
	 *   Code 404: Kein Unterricht mit der ID vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Unterricht wurde erfolgreich entfernt.
	 */
	public async deleteStundenplanUnterricht(schema : string, id : number) : Promise<StundenplanUnterricht> {
		const path = "/db/{schema}/stundenplan/unterricht/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return StundenplanUnterricht.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode addStundenplanUnterricht für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/unterricht/create
	 *
	 * Erstellt einen neue Unterricht für einen Stundenplan und gibt das zugehörige Objekt zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Stundenplans besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Der Unterricht wurde erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanUnterricht
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Unterricht für einen Stundenplan anzulegen.
	 *   Code 404: Die Stundenplandaten wurden nicht gefunden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<StundenplanUnterricht>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Unterricht wurde erfolgreich hinzugefügt.
	 */
	public async addStundenplanUnterricht(data : Partial<StundenplanUnterricht>, schema : string) : Promise<StundenplanUnterricht> {
		const path = "/db/{schema}/stundenplan/unterricht/create"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = StundenplanUnterricht.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return StundenplanUnterricht.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode addStundenplanUnterrichte für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/unterricht/create/multiple
	 *
	 * Erstellt neue Unterrichte für einen Stundenplan und gibt die zugehörigen Objekte zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Stundenplans besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Die Unterrichte wurden erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<StundenplanUnterricht>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Unterrichte für einen Stundenplan anzulegen.
	 *   Code 404: Die Stundenplandaten wurden nicht gefunden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<Partial<StundenplanUnterricht>>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Unterrichte wurden erfolgreich hinzugefügt.
	 */
	public async addStundenplanUnterrichte(data : List<Partial<StundenplanUnterricht>>, schema : string) : Promise<List<StundenplanUnterricht>> {
		const path = "/db/{schema}/stundenplan/unterricht/create/multiple"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<StundenplanUnterricht>).map(d => StundenplanUnterricht.transpilerToJSONPatch(d)).join() + "]";
		const result : string = await super.postJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<StundenplanUnterricht>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(StundenplanUnterricht.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der PATCH-Methode patchStundenplanUnterrichte für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/unterricht/patch/multiple
	 *
	 * Passt die Unterrichte an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Stundenplandaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 404: Kein Eintrag für mindestens eine der IDs der Daten gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<Partial<StundenplanUnterricht>>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 */
	public async patchStundenplanUnterrichte(data : List<Partial<StundenplanUnterricht>>, schema : string) : Promise<void> {
		const path = "/db/{schema}/stundenplan/unterricht/patch/multiple"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<StundenplanUnterricht>).map(d => StundenplanUnterricht.transpilerToJSONPatch(d)).join() + "]";
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getStundenplanZeitrasterEintrag für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/zeitraster/{id : \d+}
	 *
	 * Gibt den Zeitraster-Eintrag eines Stundeplans zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Zeitraster-Eintrag
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanZeitraster
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Stundenplan anzusehen.
	 *   Code 404: Kein Zeitraster-Eintrag eines Stundenplans gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Zeitraster-Eintrag
	 */
	public async getStundenplanZeitrasterEintrag(schema : string, id : number) : Promise<StundenplanZeitraster> {
		const path = "/db/{schema}/stundenplan/zeitraster/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return StundenplanZeitraster.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchStundenplanZeitrasterEintrag für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/zeitraster/{id : \d+}
	 *
	 * Passt den Zeitrastereintrag mit der angebenen ID an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Stundenplandaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 404: Kein Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<StundenplanZeitraster>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchStundenplanZeitrasterEintrag(data : Partial<StundenplanZeitraster>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/stundenplan/zeitraster/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const body : string = StundenplanZeitraster.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteStundenplanZeitrasterEintrag für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/zeitraster/{id : \d+}
	 *
	 * Entfernt einen Zeitrastereintrag eines Stundenplans.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Stundenplans hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Zeitrastereintrag wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanZeitraster
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Stundenplan zu bearbeiten.
	 *   Code 404: Kein Zeitrastereintrag vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Zeitrastereintrag wurde erfolgreich entfernt.
	 */
	public async deleteStundenplanZeitrasterEintrag(schema : string, id : number) : Promise<StundenplanZeitraster> {
		const path = "/db/{schema}/stundenplan/zeitraster/{id : \\d+}"
			.replace(/{schema\s*(:[^}]+)?}/g, schema)
			.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return StundenplanZeitraster.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchStundenplanZeitrasterEintraege für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/zeitraster/patch/multiple
	 *
	 * Passt die Zeitrastereinträge an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Stundenplandaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 404: Kein Eintrag für mindestens eine der IDs der Daten gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<Partial<StundenplanZeitraster>>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 */
	public async patchStundenplanZeitrasterEintraege(data : List<Partial<StundenplanZeitraster>>, schema : string) : Promise<void> {
		const path = "/db/{schema}/stundenplan/zeitraster/patch/multiple"
			.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<StundenplanZeitraster>).map(d => StundenplanZeitraster.transpilerToJSONPatch(d)).join() + "]";
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode isAlive für den Zugriff auf die URL https://{hostname}/status/alive
	 *
	 * Eine Test-Methode zum Prüfen, ob der Server erreichbar ist.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Server ist erreichbar!
	 *     - Mime-Type: text/plain
	 *     - Rückgabe-Typ: String
	 *
	 * @returns Der Server ist erreichbar!
	 */
	public async isAlive() : Promise<string | null> {
		const path = "/status/alive";
		const text : string = await super.getText(path);
		return text;
	}


	/**
	 * Implementierung der GET-Methode getServerModus für den Zugriff auf die URL https://{hostname}/status/mode
	 *
	 * Gibt den Betriebsmodus (stable, alpha, beta oder dev) des SVWS-Servers zurück.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Betriebsmodus (stable, alpha, beta oder dev)
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: String
	 *
	 * @returns Der Betriebsmodus (stable, alpha, beta oder dev)
	 */
	public async getServerModus() : Promise<string | null> {
		const path = "/status/mode";
		const result : string = await super.getJSON(path);
		const text = result;
		return JSON.parse(text).toString();
	}


	/**
	 * Implementierung der GET-Methode getServerVersion für den Zugriff auf die URL https://{hostname}/status/version
	 *
	 * Gibt die Version des SVWS-Servers zurück.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die SVWS-Server-Version
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: String
	 *
	 * @returns Die SVWS-Server-Version
	 */
	public async getServerVersion() : Promise<string | null> {
		const path = "/status/version";
		const result : string = await super.getJSON(path);
		const text = result;
		return JSON.parse(text).toString();
	}


}
