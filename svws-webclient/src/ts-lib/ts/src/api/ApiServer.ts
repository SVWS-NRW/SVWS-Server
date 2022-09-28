import { BaseApi } from '../api/BaseApi';
import { AbgangsartKatalog, cast_de_nrw_schule_svws_core_data_schule_AbgangsartKatalog } from '../core/data/schule/AbgangsartKatalog';
import { Abiturdaten, cast_de_nrw_schule_svws_core_data_gost_Abiturdaten } from '../core/data/gost/Abiturdaten';
import { AllgemeineMerkmaleKatalogEintrag, cast_de_nrw_schule_svws_core_data_schule_AllgemeineMerkmaleKatalogEintrag } from '../core/data/schule/AllgemeineMerkmaleKatalogEintrag';
import { BenutzerDaten, cast_de_nrw_schule_svws_core_data_benutzer_BenutzerDaten } from '../core/data/benutzer/BenutzerDaten';
import { BenutzergruppeDaten, cast_de_nrw_schule_svws_core_data_benutzer_BenutzergruppeDaten } from '../core/data/benutzer/BenutzergruppeDaten';
import { BenutzergruppeListeEintrag, cast_de_nrw_schule_svws_core_data_benutzer_BenutzergruppeListeEintrag } from '../core/data/benutzer/BenutzergruppeListeEintrag';
import { BenutzerKompetenzGruppenKatalogEintrag, cast_de_nrw_schule_svws_core_data_benutzer_BenutzerKompetenzGruppenKatalogEintrag } from '../core/data/benutzer/BenutzerKompetenzGruppenKatalogEintrag';
import { BenutzerKompetenzKatalogEintrag, cast_de_nrw_schule_svws_core_data_benutzer_BenutzerKompetenzKatalogEintrag } from '../core/data/benutzer/BenutzerKompetenzKatalogEintrag';
import { BenutzerListeEintrag, cast_de_nrw_schule_svws_core_data_benutzer_BenutzerListeEintrag } from '../core/data/benutzer/BenutzerListeEintrag';
import { BerufskollegAnlageKatalogEintrag, cast_de_nrw_schule_svws_core_data_schule_BerufskollegAnlageKatalogEintrag } from '../core/data/schule/BerufskollegAnlageKatalogEintrag';
import { BerufskollegBerufsebeneKatalogEintrag, cast_de_nrw_schule_svws_core_data_schule_BerufskollegBerufsebeneKatalogEintrag } from '../core/data/schule/BerufskollegBerufsebeneKatalogEintrag';
import { BerufskollegFachklassenKatalog, cast_de_nrw_schule_svws_core_data_schule_BerufskollegFachklassenKatalog } from '../core/data/schule/BerufskollegFachklassenKatalog';
import { BetriebAnsprechpartner, cast_de_nrw_schule_svws_core_data_betrieb_BetriebAnsprechpartner } from '../core/data/betrieb/BetriebAnsprechpartner';
import { BetriebListeEintrag, cast_de_nrw_schule_svws_core_data_betrieb_BetriebListeEintrag } from '../core/data/betrieb/BetriebListeEintrag';
import { BetriebStammdaten, cast_de_nrw_schule_svws_core_data_betrieb_BetriebStammdaten } from '../core/data/betrieb/BetriebStammdaten';
import { DBSchemaListeEintrag, cast_de_nrw_schule_svws_core_data_db_DBSchemaListeEintrag } from '../core/data/db/DBSchemaListeEintrag';
import { EinschulungsartKatalogEintrag, cast_de_nrw_schule_svws_core_data_schule_EinschulungsartKatalogEintrag } from '../core/data/schule/EinschulungsartKatalogEintrag';
import { ENMDaten, cast_de_nrw_schule_svws_core_data_enm_ENMDaten } from '../core/data/enm/ENMDaten';
import { Erzieherart, cast_de_nrw_schule_svws_core_data_erzieher_Erzieherart } from '../core/data/erzieher/Erzieherart';
import { ErzieherListeEintrag, cast_de_nrw_schule_svws_core_data_erzieher_ErzieherListeEintrag } from '../core/data/erzieher/ErzieherListeEintrag';
import { ErzieherStammdaten, cast_de_nrw_schule_svws_core_data_erzieher_ErzieherStammdaten } from '../core/data/erzieher/ErzieherStammdaten';
import { FachDaten, cast_de_nrw_schule_svws_core_data_fach_FachDaten } from '../core/data/fach/FachDaten';
import { FachKatalogEintrag, cast_de_nrw_schule_svws_core_data_fach_FachKatalogEintrag } from '../core/data/fach/FachKatalogEintrag';
import { FaecherListeEintrag, cast_de_nrw_schule_svws_core_data_fach_FaecherListeEintrag } from '../core/data/fach/FaecherListeEintrag';
import { FoerderschwerpunktEintrag, cast_de_nrw_schule_svws_core_data_schule_FoerderschwerpunktEintrag } from '../core/data/schule/FoerderschwerpunktEintrag';
import { FoerderschwerpunktKatalogEintrag, cast_de_nrw_schule_svws_core_data_schule_FoerderschwerpunktKatalogEintrag } from '../core/data/schule/FoerderschwerpunktKatalogEintrag';
import { GEAbschlussFaecher, cast_de_nrw_schule_svws_core_data_abschluss_GEAbschlussFaecher } from '../core/data/abschluss/GEAbschlussFaecher';
import { GostBelegpruefungErgebnis, cast_de_nrw_schule_svws_core_abschluss_gost_GostBelegpruefungErgebnis } from '../core/abschluss/gost/GostBelegpruefungErgebnis';
import { GostBlockungKurs, cast_de_nrw_schule_svws_core_data_gost_GostBlockungKurs } from '../core/data/gost/GostBlockungKurs';
import { GostBlockungListeneintrag, cast_de_nrw_schule_svws_core_data_gost_GostBlockungListeneintrag } from '../core/data/gost/GostBlockungListeneintrag';
import { GostBlockungRegel, cast_de_nrw_schule_svws_core_data_gost_GostBlockungRegel } from '../core/data/gost/GostBlockungRegel';
import { GostBlockungSchiene, cast_de_nrw_schule_svws_core_data_gost_GostBlockungSchiene } from '../core/data/gost/GostBlockungSchiene';
import { GostBlockungsdaten, cast_de_nrw_schule_svws_core_data_gost_GostBlockungsdaten } from '../core/data/gost/GostBlockungsdaten';
import { GostBlockungsergebnis, cast_de_nrw_schule_svws_core_data_gost_GostBlockungsergebnis } from '../core/data/gost/GostBlockungsergebnis';
import { GostBlockungsergebnisListeneintrag, cast_de_nrw_schule_svws_core_data_gost_GostBlockungsergebnisListeneintrag } from '../core/data/gost/GostBlockungsergebnisListeneintrag';
import { GostFach, cast_de_nrw_schule_svws_core_data_gost_GostFach } from '../core/data/gost/GostFach';
import { GostJahrgang, cast_de_nrw_schule_svws_core_data_gost_GostJahrgang } from '../core/data/gost/GostJahrgang';
import { GostJahrgangsdaten, cast_de_nrw_schule_svws_core_data_gost_GostJahrgangsdaten } from '../core/data/gost/GostJahrgangsdaten';
import { GostLeistungen, cast_de_nrw_schule_svws_core_data_gost_GostLeistungen } from '../core/data/gost/GostLeistungen';
import { GostSchuelerFachwahl, cast_de_nrw_schule_svws_core_data_gost_GostSchuelerFachwahl } from '../core/data/gost/GostSchuelerFachwahl';
import { GostStatistikFachwahl, cast_de_nrw_schule_svws_core_data_gost_GostStatistikFachwahl } from '../core/data/gost/GostStatistikFachwahl';
import { JavaInteger, cast_java_lang_Integer } from '../java/lang/JavaInteger';
import { JahrgangsDaten, cast_de_nrw_schule_svws_core_data_jahrgang_JahrgangsDaten } from '../core/data/jahrgang/JahrgangsDaten';
import { JahrgangsKatalogEintrag, cast_de_nrw_schule_svws_core_data_jahrgang_JahrgangsKatalogEintrag } from '../core/data/jahrgang/JahrgangsKatalogEintrag';
import { JahrgangsListeEintrag, cast_de_nrw_schule_svws_core_data_jahrgang_JahrgangsListeEintrag } from '../core/data/jahrgang/JahrgangsListeEintrag';
import { KatalogEintrag, cast_de_nrw_schule_svws_core_data_kataloge_KatalogEintrag } from '../core/data/kataloge/KatalogEintrag';
import { KatalogEintragStrassen, cast_de_nrw_schule_svws_core_data_kataloge_KatalogEintragStrassen } from '../core/data/kataloge/KatalogEintragStrassen';
import { KlassenartKatalogEintrag, cast_de_nrw_schule_svws_core_data_klassen_KlassenartKatalogEintrag } from '../core/data/klassen/KlassenartKatalogEintrag';
import { KlassenDaten, cast_de_nrw_schule_svws_core_data_klassen_KlassenDaten } from '../core/data/klassen/KlassenDaten';
import { KlassenListeEintrag, cast_de_nrw_schule_svws_core_data_klassen_KlassenListeEintrag } from '../core/data/klassen/KlassenListeEintrag';
import { KursartKatalogEintrag, cast_de_nrw_schule_svws_core_data_kurse_KursartKatalogEintrag } from '../core/data/kurse/KursartKatalogEintrag';
import { KursDaten, cast_de_nrw_schule_svws_core_data_kurse_KursDaten } from '../core/data/kurse/KursDaten';
import { KursListeEintrag, cast_de_nrw_schule_svws_core_data_kurse_KursListeEintrag } from '../core/data/kurse/KursListeEintrag';
import { LehrerKatalogAbgangsgrundEintrag, cast_de_nrw_schule_svws_core_data_lehrer_LehrerKatalogAbgangsgrundEintrag } from '../core/data/lehrer/LehrerKatalogAbgangsgrundEintrag';
import { LehrerKatalogAnrechnungsgrundEintrag, cast_de_nrw_schule_svws_core_data_lehrer_LehrerKatalogAnrechnungsgrundEintrag } from '../core/data/lehrer/LehrerKatalogAnrechnungsgrundEintrag';
import { LehrerKatalogBeschaeftigungsartEintrag, cast_de_nrw_schule_svws_core_data_lehrer_LehrerKatalogBeschaeftigungsartEintrag } from '../core/data/lehrer/LehrerKatalogBeschaeftigungsartEintrag';
import { LehrerKatalogEinsatzstatusEintrag, cast_de_nrw_schule_svws_core_data_lehrer_LehrerKatalogEinsatzstatusEintrag } from '../core/data/lehrer/LehrerKatalogEinsatzstatusEintrag';
import { LehrerKatalogFachrichtungAnerkennungEintrag, cast_de_nrw_schule_svws_core_data_lehrer_LehrerKatalogFachrichtungAnerkennungEintrag } from '../core/data/lehrer/LehrerKatalogFachrichtungAnerkennungEintrag';
import { LehrerKatalogFachrichtungEintrag, cast_de_nrw_schule_svws_core_data_lehrer_LehrerKatalogFachrichtungEintrag } from '../core/data/lehrer/LehrerKatalogFachrichtungEintrag';
import { LehrerKatalogLehramtAnerkennungEintrag, cast_de_nrw_schule_svws_core_data_lehrer_LehrerKatalogLehramtAnerkennungEintrag } from '../core/data/lehrer/LehrerKatalogLehramtAnerkennungEintrag';
import { LehrerKatalogLehramtEintrag, cast_de_nrw_schule_svws_core_data_lehrer_LehrerKatalogLehramtEintrag } from '../core/data/lehrer/LehrerKatalogLehramtEintrag';
import { LehrerKatalogLehrbefaehigungAnerkennungEintrag, cast_de_nrw_schule_svws_core_data_lehrer_LehrerKatalogLehrbefaehigungAnerkennungEintrag } from '../core/data/lehrer/LehrerKatalogLehrbefaehigungAnerkennungEintrag';
import { LehrerKatalogLehrbefaehigungEintrag, cast_de_nrw_schule_svws_core_data_lehrer_LehrerKatalogLehrbefaehigungEintrag } from '../core/data/lehrer/LehrerKatalogLehrbefaehigungEintrag';
import { LehrerKatalogLeitungsfunktionEintrag, cast_de_nrw_schule_svws_core_data_lehrer_LehrerKatalogLeitungsfunktionEintrag } from '../core/data/lehrer/LehrerKatalogLeitungsfunktionEintrag';
import { LehrerKatalogMehrleistungsartEintrag, cast_de_nrw_schule_svws_core_data_lehrer_LehrerKatalogMehrleistungsartEintrag } from '../core/data/lehrer/LehrerKatalogMehrleistungsartEintrag';
import { LehrerKatalogMinderleistungsartEintrag, cast_de_nrw_schule_svws_core_data_lehrer_LehrerKatalogMinderleistungsartEintrag } from '../core/data/lehrer/LehrerKatalogMinderleistungsartEintrag';
import { LehrerKatalogRechtsverhaeltnisEintrag, cast_de_nrw_schule_svws_core_data_lehrer_LehrerKatalogRechtsverhaeltnisEintrag } from '../core/data/lehrer/LehrerKatalogRechtsverhaeltnisEintrag';
import { LehrerKatalogZugangsgrundEintrag, cast_de_nrw_schule_svws_core_data_lehrer_LehrerKatalogZugangsgrundEintrag } from '../core/data/lehrer/LehrerKatalogZugangsgrundEintrag';
import { LehrerListeEintrag, cast_de_nrw_schule_svws_core_data_lehrer_LehrerListeEintrag } from '../core/data/lehrer/LehrerListeEintrag';
import { LehrerPersonaldaten, cast_de_nrw_schule_svws_core_data_lehrer_LehrerPersonaldaten } from '../core/data/lehrer/LehrerPersonaldaten';
import { LehrerStammdaten, cast_de_nrw_schule_svws_core_data_lehrer_LehrerStammdaten } from '../core/data/lehrer/LehrerStammdaten';
import { List, cast_java_util_List } from '../java/util/List';
import { JavaLong, cast_java_lang_Long } from '../java/lang/JavaLong';
import { NationalitaetenKatalogEintrag, cast_de_nrw_schule_svws_core_data_schule_NationalitaetenKatalogEintrag } from '../core/data/schule/NationalitaetenKatalogEintrag';
import { OrganisationsformKatalogEintrag, cast_de_nrw_schule_svws_core_data_schule_OrganisationsformKatalogEintrag } from '../core/data/schule/OrganisationsformKatalogEintrag';
import { OrtKatalogEintrag, cast_de_nrw_schule_svws_core_data_kataloge_OrtKatalogEintrag } from '../core/data/kataloge/OrtKatalogEintrag';
import { OrtsteilKatalogEintrag, cast_de_nrw_schule_svws_core_data_kataloge_OrtsteilKatalogEintrag } from '../core/data/kataloge/OrtsteilKatalogEintrag';
import { PruefungsordnungKatalogEintrag, cast_de_nrw_schule_svws_core_data_schule_PruefungsordnungKatalogEintrag } from '../core/data/schule/PruefungsordnungKatalogEintrag';
import { ReligionEintrag, cast_de_nrw_schule_svws_core_data_schule_ReligionEintrag } from '../core/data/schule/ReligionEintrag';
import { ReligionKatalogEintrag, cast_de_nrw_schule_svws_core_data_schule_ReligionKatalogEintrag } from '../core/data/schule/ReligionKatalogEintrag';
import { SchuelerBetriebsdaten, cast_de_nrw_schule_svws_core_data_schueler_SchuelerBetriebsdaten } from '../core/data/schueler/SchuelerBetriebsdaten';
import { SchuelerLernabschnittListeEintrag, cast_de_nrw_schule_svws_core_data_schueler_SchuelerLernabschnittListeEintrag } from '../core/data/schueler/SchuelerLernabschnittListeEintrag';
import { SchuelerLernabschnittsdaten, cast_de_nrw_schule_svws_core_data_schueler_SchuelerLernabschnittsdaten } from '../core/data/schueler/SchuelerLernabschnittsdaten';
import { SchuelerListeEintrag, cast_de_nrw_schule_svws_core_data_schueler_SchuelerListeEintrag } from '../core/data/schueler/SchuelerListeEintrag';
import { SchuelerSchulbesuchsdaten, cast_de_nrw_schule_svws_core_data_schueler_SchuelerSchulbesuchsdaten } from '../core/data/schueler/SchuelerSchulbesuchsdaten';
import { SchuelerStammdaten, cast_de_nrw_schule_svws_core_data_schueler_SchuelerStammdaten } from '../core/data/schueler/SchuelerStammdaten';
import { SchuelerStundenplan, cast_de_nrw_schule_svws_core_data_stundenplan_SchuelerStundenplan } from '../core/data/stundenplan/SchuelerStundenplan';
import { SchulabschlussAllgemeinbildendKatalogEintrag, cast_de_nrw_schule_svws_core_data_schule_SchulabschlussAllgemeinbildendKatalogEintrag } from '../core/data/schule/SchulabschlussAllgemeinbildendKatalogEintrag';
import { SchulabschlussBerufsbildendKatalogEintrag, cast_de_nrw_schule_svws_core_data_schule_SchulabschlussBerufsbildendKatalogEintrag } from '../core/data/schule/SchulabschlussBerufsbildendKatalogEintrag';
import { SchuleStammdaten, cast_de_nrw_schule_svws_core_data_schule_SchuleStammdaten } from '../core/data/schule/SchuleStammdaten';
import { SchulformKatalogEintrag, cast_de_nrw_schule_svws_core_data_schule_SchulformKatalogEintrag } from '../core/data/schule/SchulformKatalogEintrag';
import { SchulgliederungKatalogEintrag, cast_de_nrw_schule_svws_core_data_schule_SchulgliederungKatalogEintrag } from '../core/data/schule/SchulgliederungKatalogEintrag';
import { SimpleOperationResponse, cast_de_nrw_schule_svws_core_data_SimpleOperationResponse } from '../core/data/SimpleOperationResponse';
import { StundenplanListeEintrag, cast_de_nrw_schule_svws_core_data_stundenplan_StundenplanListeEintrag } from '../core/data/stundenplan/StundenplanListeEintrag';
import { StundenplanZeitraster, cast_de_nrw_schule_svws_core_data_stundenplan_StundenplanZeitraster } from '../core/data/stundenplan/StundenplanZeitraster';
import { Vector, cast_java_util_Vector } from '../java/util/Vector';
import { VerkehrsspracheKatalogEintrag, cast_de_nrw_schule_svws_core_data_schule_VerkehrsspracheKatalogEintrag } from '../core/data/schule/VerkehrsspracheKatalogEintrag';

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
	public async getConfigCertificate() : Promise<String> {
		let path : string = "/config/certificate";
		const text : string = await super.getText(path);
		return String(text);
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
	public async getConfigCertificateBase64() : Promise<String> {
		let path : string = "/config/certificate_base64";
		const text : string = await super.getText(path);
		return String(text);
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
		let path : string = "/config/db/schemata";
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<DBSchemaListeEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(DBSchemaListeEintrag.transpilerFromJSON(text)); });
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
	public async getConfigPublicKeyBase64() : Promise<String> {
		let path : string = "/config/publickey_base64";
		const text : string = await super.getText(path);
		return String(text);
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
		let path : string = "/db/{schema}/benutzer/"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<BenutzerListeEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(BenutzerListeEintrag.transpilerFromJSON(text)); });
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
		let path : string = "/db/{schema}/benutzer/{id : \d+}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return BenutzerDaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode setBenutzerPasswort für den Zugriff auf die URL https://{hostname}/db/{schema}/benutzer/{id : \d+}/set_password
	 * 
	 * Setzt das Kennwort eines Benutzers.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Setzen des Kennwortes besitzt.
	 * 
	 * Mögliche HTTP-Antworten: 
	 *   Code 204: Das Kennwort wurde erfolgreich gesetzt.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um das Kennwort zu setzen.
	 *   Code 404: Die Kennwortinformationen zu dem Benutzer sind nicht vorhanden.
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 * 
	 * @param {String} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async setBenutzerPasswort(data : String, schema : string, id : number) : Promise<void> {
		let path : string = "/db/{schema}/benutzer/{id : \d+}/set_password"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		let body : string = JSON.stringify(data);
		await super.postJSON(path, body);
		return;
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
		let path : string = "/db/{schema}/benutzer/gruppe"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<BenutzergruppeListeEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(BenutzergruppeListeEintrag.transpilerFromJSON(text)); });
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
		let path : string = "/db/{schema}/benutzer/gruppe/{id : \d+}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return BenutzergruppeDaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getKatalogBenutzerkompetenzen für den Zugriff auf die URL https://{hostname}/db/{schema}/benutzer/kompetenzen
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
		let path : string = "/db/{schema}/benutzer/kompetenzen"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<BenutzerKompetenzKatalogEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(BenutzerKompetenzKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogBenutzerkompetenzgruppen für den Zugriff auf die URL https://{hostname}/db/{schema}/benutzer/kompetenzgruppen
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
		let path : string = "/db/{schema}/benutzer/kompetenzgruppen"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<BenutzerKompetenzGruppenKatalogEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(BenutzerKompetenzGruppenKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
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
		let path : string = "/db/{schema}/betriebe/"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<BetriebListeEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(BetriebListeEintrag.transpilerFromJSON(text)); });
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
		let path : string = "/db/{schema}/betriebe/{betrieb_id : \d+}/ansprechpartner/new"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{betrieb_id\s*(:[^}]+)?}/g, betrieb_id.toString());
		let body : string = BetriebAnsprechpartner.transpilerToJSON(data);
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
	 * @param {SchuelerBetriebsdaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchSchuelerBetriebsdaten(data : Partial<SchuelerBetriebsdaten>, schema : string, id : number) : Promise<void> {
		let path : string = "/db/{schema}/betriebe/{id : \d+}/betrieb"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		let body : string = SchuelerBetriebsdaten.transpilerToJSONPatch(data);
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
	 *     - Rückgabe-Typ: List<SchuelerBetriebsdaten>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Schülerbetreibe anzusehen.
	 *   Code 404: Kein Schülerbetrieb gefunden
	 * 
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 * 
	 * @returns Stammdaten des Schülerbetriebs.
	 */
	public async getSchuelerBetriebsdaten(schema : string, id : number) : Promise<List<SchuelerBetriebsdaten>> {
		let path : string = "/db/{schema}/betriebe/{id : \d+}/betrieb"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<SchuelerBetriebsdaten>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(SchuelerBetriebsdaten.transpilerFromJSON(text)); });
		return ret;
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
		let path : string = "/db/{schema}/betriebe/{id : \d+}/betriebansprechpartner"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<BetriebAnsprechpartner>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(BetriebAnsprechpartner.transpilerFromJSON(text)); });
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
	 * @param {BetriebAnsprechpartner} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchBetriebanpsrechpartnerdaten(data : Partial<BetriebAnsprechpartner>, schema : string, id : number) : Promise<void> {
		let path : string = "/db/{schema}/betriebe/{id : \d+}/betriebansprechpartner"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		let body : string = BetriebAnsprechpartner.transpilerToJSONPatch(data);
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
		let path : string = "/db/{schema}/betriebe/{id : \d+}/stammdaten"
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
	 * @param {BetriebStammdaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchBetriebStammdaten(data : Partial<BetriebStammdaten>, schema : string, id : number) : Promise<void> {
		let path : string = "/db/{schema}/betriebe/{id : \d+}/stammdaten"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		let body : string = BetriebStammdaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getBetriebAnsprechpartner für den Zugriff auf die URL https://{hostname}/db/{schema}/betriebe/betriebansprechpartner
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
	public async getBetriebAnsprechpartner(schema : string) : Promise<List<BetriebAnsprechpartner>> {
		let path : string = "/db/{schema}/betriebe/betriebansprechpartner"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<BetriebAnsprechpartner>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(BetriebAnsprechpartner.transpilerFromJSON(text)); });
		return ret;
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
		let path : string = "/db/{schema}/betriebe/schuelerbetrieb/new/schueler/{schueler_id : \d+}/betrieb/{betrieb_id: \d+}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{schueler_id\s*(:[^}]+)?}/g, schueler_id.toString())
				.replace(/{betrieb_id\s*(:[^}]+)?}/g, betrieb_id.toString());
		let body : string = SchuelerBetriebsdaten.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SchuelerBetriebsdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PUT-Methode setClientConfigGlobalKey für den Zugriff auf die URL https://{hostname}/db/{schema}/client/config/{app}/global/{key}
	 * 
	 * Schreibt den Konfigurationseintrag der angegebenen Anwendung für den angebenen Schlüsselwert in die globale Konfiguration. Dabei wird geprüft, ob der angemeldete Benutzer administrative Rechte hat.
	 * 
	 * Mögliche HTTP-Antworten: 
	 *   Code 200: Der Konfigurationseintrag wurde erfolgreich geschrieben
	 * 
	 * @param {String} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {string} app - der Pfad-Parameter app
	 * @param {string} key - der Pfad-Parameter key
	 */
	public async setClientConfigGlobalKey(data : String, schema : string, app : string, key : string) : Promise<void> {
		let path : string = "/db/{schema}/client/config/{app}/global/{key}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{app\s*(:[^}]+)?}/g, app)
				.replace(/{key\s*(:[^}]+)?}/g, key);
		let body : string = JSON.stringify(data);
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
	public async getClientConfigUserKey(schema : string, app : string, key : string) : Promise<String> {
		let path : string = "/db/{schema}/client/config/{app}/user/{key}"
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
	 *   Code 200: Der Konfigurationseintrag wurde erfolgreich geschrieben
	 * 
	 * @param {String} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {string} app - der Pfad-Parameter app
	 * @param {string} key - der Pfad-Parameter key
	 */
	public async setClientConfigUserKey(data : String, schema : string, app : string, key : string) : Promise<void> {
		let path : string = "/db/{schema}/client/config/{app}/user/{key}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{app\s*(:[^}]+)?}/g, app)
				.replace(/{key\s*(:[^}]+)?}/g, key);
		let body : string = JSON.stringify(data);
		return super.putJSON(path, body);
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
		let path : string = "/db/{schema}/enm/lehrer/{id : \d+}"
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
		let path : string = "/db/{schema}/erzieher/"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<ErzieherListeEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(ErzieherListeEintrag.transpilerFromJSON(text)); });
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
		let path : string = "/db/{schema}/erzieher/{id : \d+}/stammdaten"
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
	 * @param {ErzieherStammdaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchErzieherStammdaten(data : Partial<ErzieherStammdaten>, schema : string, id : number) : Promise<void> {
		let path : string = "/db/{schema}/erzieher/{id : \d+}/stammdaten"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		let body : string = ErzieherStammdaten.transpilerToJSONPatch(data);
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
		let path : string = "/db/{schema}/erzieher/arten"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<Erzieherart>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(Erzieherart.transpilerFromJSON(text)); });
		return ret;
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
		let path : string = "/db/{schema}/faecher/"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<FaecherListeEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(FaecherListeEintrag.transpilerFromJSON(text)); });
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
		let path : string = "/db/{schema}/faecher/{id : \d+}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return FachDaten.transpilerFromJSON(text);
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
		let path : string = "/db/{schema}/gesamtschule/schueler/{id : \d+}/prognose_leistungsdaten"
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
		let path : string = "/db/{schema}/gesamtschule/schueler/{id : \d+}/prognose_leistungsdaten/abschnitt/{abschnittID : \d+}"
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
		let path : string = "/db/{schema}/gost/abitur/belegpruefung/EF1"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		let body : string = Abiturdaten.transpilerToJSON(data);
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
		let path : string = "/db/{schema}/gost/abitur/belegpruefung/gesamt"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		let body : string = Abiturdaten.transpilerToJSON(data);
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
		let path : string = "/db/{schema}/gost/abiturjahrgaenge"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<GostJahrgang>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(GostJahrgang.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getGostAbiturjahrgang für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : \d+}
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
		let path : string = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : \d+}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostJahrgangsdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchGostAbiturjahrgang für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : \d+}
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
	 * @param {GostJahrgangsdaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 */
	public async patchGostAbiturjahrgang(data : Partial<GostJahrgangsdaten>, schema : string, abiturjahr : number) : Promise<void> {
		let path : string = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : \d+}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString());
		let body : string = GostJahrgangsdaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getGostAbiturjahrgangBlockungsliste für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : \d+}/{halbjahr : \d+}/blockungen
	 * 
	 * Erstellt eine Liste aller in der Datenbank vorhanden Blockungen der gymnasialen Oberstufe, welche für den angebenen Abitur-Jahrgang und das angegebene Halbjahr festgelegt wurden.. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Blockungsdaten besitzt.
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
		let path : string = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : \d+}/{halbjahr : \d+}/blockungen"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString())
				.replace(/{halbjahr\s*(:[^}]+)?}/g, halbjahr.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<GostBlockungListeneintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(GostBlockungListeneintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode createGostAbiturjahrgangBlockung für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : \d+}/{halbjahr : \d+}/blockungen/new
	 * 
	 * Erstellt eine neue Blockung und gibt die ID dieser Blockung zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen der Fachwahlen besitzt.
	 * 
	 * Mögliche HTTP-Antworten: 
	 *   Code 200: Die Blockung wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBlockungsdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Blockung anzulegen.
	 *   Code 404: Keine Fachwahlinformationen zum Anlegen einer Blockung gefunden
	 *   Code 409: Das Abitujahr oder das Halbjahr ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 * 
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 * @param {number} halbjahr - der Pfad-Parameter halbjahr
	 * 
	 * @returns Die Blockung wurde erfolgreich angelegt.
	 */
	public async createGostAbiturjahrgangBlockung(schema : string, abiturjahr : number, halbjahr : number) : Promise<GostBlockungsdaten> {
		let path : string = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : \d+}/{halbjahr : \d+}/blockungen/new"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString())
				.replace(/{halbjahr\s*(:[^}]+)?}/g, halbjahr.toString());
		const result : string = await super.postJSON(path, null);
		const text = result;
		return GostBlockungsdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getGostAbiturjahrgangFach für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : \d+}/fach/{id : \d+}
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
		let path : string = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : \d+}/fach/{id : \d+}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString())
				.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostFach.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchGostAbiturjahrgangFach für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : \d+}/fach/{id : \d+}
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
	 * @param {GostFach} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchGostAbiturjahrgangFach(data : Partial<GostFach>, schema : string, abiturjahr : number, id : number) : Promise<void> {
		let path : string = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : \d+}/fach/{id : \d+}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString())
				.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		let body : string = GostFach.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getGostAbiturjahrgangFachwahlen für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : \d+}/fachwahlen
	 * 
	 * Erstellt eine Liste aller in der Datenbank für den angebenen Abitur-Jahrgang vorhanden Fachwahlen der gymnasialen Oberstufe. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Fächerdaten besitzt.
	 * 
	 * Mögliche HTTP-Antworten: 
	 *   Code 200: Eine Liste der Fachwahlen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<GostStatistikFachwahl>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Fachwahlen anzusehen.
	 *   Code 404: Keine Fachwahlen gefunden oder keine gymnasiale Oberstufe bei der Schulform vorhanden
	 * 
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 * 
	 * @returns Eine Liste der Fachwahlen
	 */
	public async getGostAbiturjahrgangFachwahlen(schema : string, abiturjahr : number) : Promise<List<GostStatistikFachwahl>> {
		let path : string = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : \d+}/fachwahlen"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<GostStatistikFachwahl>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(GostStatistikFachwahl.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getGostAbiturjahrgangFaecher für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : \d+}/faecher
	 * 
	 * Erstellt eine Liste aller in der Datenbank vorhanden Fächer der gymnasialen Oberstufe, welche für den angebenen Abitur-Jahrgang festgelegt wurden.. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Fächerdaten besitzt.
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
		let path : string = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : \d+}/faecher"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<GostFach>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(GostFach.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getGostAbiturjahrgangSchueler für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : \d+}/schueler
	 * 
	 * Erstellt eine Liste aller Schüler, welche zu dem angebenen Abitur-Jahrgang gehören (ohne Informationen zu Kursen). Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Schülerdaten besitzt.
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
		let path : string = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : \d+}/schueler"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{abiturjahr\s*(:[^}]+)?}/g, abiturjahr.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<SchuelerListeEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(SchuelerListeEintrag.transpilerFromJSON(text)); });
		return ret;
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
	public async createGostAbiturjahrgang(schema : string, jahrgangid : number) : Promise<Number> {
		let path : string = "/db/{schema}/gost/abiturjahrgang/new/{jahrgangid}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{jahrgangid\s*(:[^}]+)?}/g, jahrgangid.toString());
		const result : string = await super.postJSON(path, null);
		const text = result;
		return parseInt(JSON.parse(text));
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
	 *   Code 404: Keine Blockung mit der angebenen ID gefunden.
	 * 
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} blockungsid - der Pfad-Parameter blockungsid
	 * 
	 * @returns Die Blockungsdaten der gymnasialen Oberstfue für die angegebene ID wurden erfolgreich gelöscht.
	 */
	public async deleteGostBlockung(schema : string, blockungsid : number) : Promise<Number> {
		let path : string = "/db/{schema}/gost/blockungen/{blockungsid : \d+}"
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
		let path : string = "/db/{schema}/gost/blockungen/{blockungsid : \d+}"
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
	 * @param {GostBlockungsdaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} blockungsid - der Pfad-Parameter blockungsid
	 */
	public async patchGostBlockung(data : Partial<GostBlockungsdaten>, schema : string, blockungsid : number) : Promise<void> {
		let path : string = "/db/{schema}/gost/blockungen/{blockungsid : \d+}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{blockungsid\s*(:[^}]+)?}/g, blockungsid.toString());
		let body : string = GostBlockungsdaten.transpilerToJSONPatch(data);
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
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} blockungsid - der Pfad-Parameter blockungsid
	 * @param {number} regeltyp - der Pfad-Parameter regeltyp
	 * 
	 * @returns Die Regel der Blockung der gymnasialen Oberstufe
	 */
	public async addGostBlockungRegel(schema : string, blockungsid : number, regeltyp : number) : Promise<GostBlockungRegel> {
		let path : string = "/db/{schema}/gost/blockungen/{blockungsid : \d+}/addregel/{regeltyp : \d+}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{blockungsid\s*(:[^}]+)?}/g, blockungsid.toString())
				.replace(/{regeltyp\s*(:[^}]+)?}/g, regeltyp.toString());
		const result : string = await super.postJSON(path, null);
		const text = result;
		return GostBlockungRegel.transpilerFromJSON(text);
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
		let path : string = "/db/{schema}/gost/blockungen/{blockungsid : \d+}/addschiene"
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
		let path : string = "/db/{schema}/gost/blockungen/{blockungsid : \d+}/deleteschiene"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{blockungsid\s*(:[^}]+)?}/g, blockungsid.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return GostBlockungSchiene.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getGostBlockungsergebnisliste für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/{blockungsid : \d+}/ergebnisse
	 * 
	 * Erstellt eine Liste aller Ergebnisse der angegebenen Blockung der gymnasialen Oberstufe Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Blockungsdaten besitzt.
	 * 
	 * Mögliche HTTP-Antworten: 
	 *   Code 200: Eine Liste von Blockungsergebnis-Listen-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<GostBlockungsergebnisListeneintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Blockungsdaten anzusehen.
	 *   Code 404: Keine Blockungsergebnis-Einträge gefunden, keine gymnasiale Oberstufe bei der Schulform vorhanden oder die Daten für die Blockung sind unvollständig
	 * 
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} blockungsid - der Pfad-Parameter blockungsid
	 * 
	 * @returns Eine Liste von Blockungsergebnis-Listen-Einträgen
	 */
	public async getGostBlockungsergebnisliste(schema : string, blockungsid : number) : Promise<List<GostBlockungsergebnisListeneintrag>> {
		let path : string = "/db/{schema}/gost/blockungen/{blockungsid : \d+}/ergebnisse"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{blockungsid\s*(:[^}]+)?}/g, blockungsid.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<GostBlockungsergebnisListeneintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(GostBlockungsergebnisListeneintrag.transpilerFromJSON(text)); });
		return ret;
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
		let path : string = "/db/{schema}/gost/blockungen/{blockungsid : \d+}/fach/{fachid : \d+}/kursart/{kursartid : \d+}/add"
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
		let path : string = "/db/{schema}/gost/blockungen/{blockungsid : \d+}/fach/{fachid : \d+}/kursart/{kursartid : \d+}/delete"
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
	 *   Code 404: Keine Blockung mit der angebenen ID gefunden.
	 * 
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} blockungsid - der Pfad-Parameter blockungsid
	 * @param {number} zeit - der Pfad-Parameter zeit
	 * 
	 * @returns Eine Liste der IDs der Zwischenergebnisse
	 */
	public async rechneGostBlockung(schema : string, blockungsid : number, zeit : number) : Promise<List<Number>> {
		let path : string = "/db/{schema}/gost/blockungen/{blockungsid : \d+}/rechne/{zeit : \d+}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{blockungsid\s*(:[^}]+)?}/g, blockungsid.toString())
				.replace(/{zeit\s*(:[^}]+)?}/g, zeit.toString());
		const result : string = await super.postJSON(path, null);
		const obj = JSON.parse(result);
		let ret = new Vector<Number>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(parseFloat(JSON.parse(text))); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getGostBlockungKurs für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/kurse/{kursid : \d+}
	 * 
	 * Liest den angegebenen Kurs einer Blockung der gymnasialen Oberstufe aus. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Blockungsdaten besitzt.
	 * 
	 * Mögliche HTTP-Antworten: 
	 *   Code 200: Der Kurs der Blockung der gymnasialen Oberstfue für die angegebene ID
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBlockungKurs
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Blockungsdaten der Gymnasialen Oberstufe auszulesen.
	 *   Code 404: Kein Kurs einer Blockung mit der angebenen ID gefunden.
	 * 
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} kursid - der Pfad-Parameter kursid
	 * 
	 * @returns Der Kurs der Blockung der gymnasialen Oberstfue für die angegebene ID
	 */
	public async getGostBlockungKurs(schema : string, kursid : number) : Promise<GostBlockungKurs> {
		let path : string = "/db/{schema}/gost/blockungen/kurse/{kursid : \d+}"
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
	 *   Code 404: Kein Kurs einer Blockung mit der angebenen ID gefunden.
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 * 
	 * @param {GostBlockungKurs} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} kursid - der Pfad-Parameter kursid
	 */
	public async patchGostBlockungKurs(data : Partial<GostBlockungKurs>, schema : string, kursid : number) : Promise<void> {
		let path : string = "/db/{schema}/gost/blockungen/kurse/{kursid : \d+}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{kursid\s*(:[^}]+)?}/g, kursid.toString());
		let body : string = GostBlockungKurs.transpilerToJSONPatch(data);
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
		let path : string = "/db/{schema}/gost/blockungen/kurse/{kursid : \d+}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{kursid\s*(:[^}]+)?}/g, kursid.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return GostBlockungKurs.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getGostBlockungRegel für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/regeln/{regelid : \d+}
	 * 
	 * Liest die angegebene Regel einer Blockung der gymnasialen Oberstufe aus. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Blockungsdaten besitzt.
	 * 
	 * Mögliche HTTP-Antworten: 
	 *   Code 200: Die Regel der Blockung der gymnasialen Oberstfue für die angegebene ID
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBlockungRegel
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Blockungsdaten der Gymnasialen Oberstufe auszulesen.
	 *   Code 404: Keine Regel einer Blockung mit der angebenen ID gefunden.
	 * 
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} regelid - der Pfad-Parameter regelid
	 * 
	 * @returns Die Regel der Blockung der gymnasialen Oberstfue für die angegebene ID
	 */
	public async getGostBlockungRegel(schema : string, regelid : number) : Promise<GostBlockungRegel> {
		let path : string = "/db/{schema}/gost/blockungen/regeln/{regelid : \d+}"
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
	 * @param {GostBlockungRegel} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} regelid - der Pfad-Parameter regelid
	 */
	public async patchGostBlockungRegel(data : Partial<GostBlockungRegel>, schema : string, regelid : number) : Promise<void> {
		let path : string = "/db/{schema}/gost/blockungen/regeln/{regelid : \d+}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{regelid\s*(:[^}]+)?}/g, regelid.toString());
		let body : string = GostBlockungRegel.transpilerToJSONPatch(data);
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
		let path : string = "/db/{schema}/gost/blockungen/regeln/{regelid : \d+}"
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
		let path : string = "/db/{schema}/gost/blockungen/schiene/{schienenid : \d+}"
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
	 *   Code 404: Keine Schiene einer Blockung mit der angebenen ID gefunden.
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 * 
	 * @param {GostBlockungSchiene} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} schienenid - der Pfad-Parameter schienenid
	 */
	public async patchGostBlockungSchiene(data : Partial<GostBlockungSchiene>, schema : string, schienenid : number) : Promise<void> {
		let path : string = "/db/{schema}/gost/blockungen/schiene/{schienenid : \d+}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{schienenid\s*(:[^}]+)?}/g, schienenid.toString());
		let body : string = GostBlockungSchiene.transpilerToJSONPatch(data);
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
		let path : string = "/db/{schema}/gost/blockungen/schiene/{schienenid : \d+}"
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
	 *   Code 200: Die Blockungsergebnisse der gymnasialen Oberstfue für die angegebene ID
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBlockungsergebnis
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Blockungsergebnisse einer Blockung der Gymnasialen Oberstufe auszulesen.
	 *   Code 404: Keine Blockung mit der angebenen ID gefunden.
	 * 
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} ergebnisid - der Pfad-Parameter ergebnisid
	 * 
	 * @returns Die Blockungsergebnisse der gymnasialen Oberstfue für die angegebene ID
	 */
	public async getGostBlockungsergebnis(schema : string, ergebnisid : number) : Promise<GostBlockungsergebnis> {
		let path : string = "/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \d+}"
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
	 *   Code 200: Das Zwischenergebnis einer Blockung der gymnasialen Oberstfue für die angegebene ID wurde erfolgreich gelöscht.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Long
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um das Zwischenergebnis einer Blockung der Gymnasialen Oberstufe zu löschen.
	 *   Code 404: Keine Blockung mit der angebenen ID gefunden.
	 * 
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} ergebnisid - der Pfad-Parameter ergebnisid
	 * 
	 * @returns Das Zwischenergebnis einer Blockung der gymnasialen Oberstfue für die angegebene ID wurde erfolgreich gelöscht.
	 */
	public async deleteGostBlockungsergebnis(schema : string, ergebnisid : number) : Promise<Number> {
		let path : string = "/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \d+}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{ergebnisid\s*(:[^}]+)?}/g, ergebnisid.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return parseFloat(JSON.parse(text));
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
		let path : string = "/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \d+}/schueler/{schuelerid : \d+}/kurs/{kursid: \d+}"
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
	 * Entfernt eine Kurszuordnung zu einem Scüler bei einem Blockungsergebniss einer Blockung der Gymnasialen Oberstufe. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen besitzt.
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
		let path : string = "/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \d+}/schueler/{schuelerid : \d+}/kurs/{kursid: \d+}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{ergebnisid\s*(:[^}]+)?}/g, ergebnisid.toString())
				.replace(/{schuelerid\s*(:[^}]+)?}/g, schuelerid.toString())
				.replace(/{kursid\s*(:[^}]+)?}/g, kursid.toString());
		await super.deleteJSON(path, null);
		return;
	}


	/**
	 * Implementierung der GET-Methode getGostFach für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/fach/{id : \d+}
	 * 
	 * Liefert die Informationen bezüglich der gymnasialen Oberstufe zu dem Fach mit der angegebenen ID. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Fachinformationen besitzt.
	 * 
	 * Mögliche HTTP-Antworten: 
	 *   Code 200: Die Daten des Faches bezüglich der gymnasialen Oberstufe
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostFach
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Fachiformationen der Gymnasialen Oberstufe anzusehen.
	 *   Code 404: Kein Eintrag für das Fach der gymnasialen Oberstufe mit der angegebenen ID gefunden
	 * 
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 * 
	 * @returns Die Daten des Faches bezüglich der gymnasialen Oberstufe
	 */
	public async getGostFach(schema : string, id : number) : Promise<GostFach> {
		let path : string = "/db/{schema}/gost/fach/{id : \d+}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostFach.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchGostFach für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/fach/{id : \d+}
	 * 
	 * Passt die Daten eines Faches in Bezug auf die Gymnasiale Oberstufe an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anpassen der Fachinformationen besitzt.
	 * 
	 * Mögliche HTTP-Antworten: 
	 *   Code 200: Der Patch wurde erfolgreich in die Fachinformationen integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Fachdaten zu ändern.
	 *   Code 404: Kein Fach-Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 * 
	 * @param {GostFach} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchGostFach(data : Partial<GostFach>, schema : string, id : number) : Promise<void> {
		let path : string = "/db/{schema}/gost/fach/{id : \d+}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		let body : string = GostFach.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getGostFaecher für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/faecher
	 * 
	 * Erstellt eine Liste aller in der Datenbank vorhanden Fächer der gymnasialen Oberstufe. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Fächerdaten besitzt.
	 * 
	 * Mögliche HTTP-Antworten: 
	 *   Code 200: Eine Liste von Fächer-Listen-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<GostFach>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Fächerdaten anzusehen.
	 *   Code 404: Keine Fächer-Einträge gefunden oder keine gymnasiale Oberstufe bei der Schulform vorhanden
	 * 
	 * @param {string} schema - der Pfad-Parameter schema
	 * 
	 * @returns Eine Liste von Fächer-Listen-Einträgen
	 */
	public async getGostFaecher(schema : string) : Promise<List<GostFach>> {
		let path : string = "/db/{schema}/gost/faecher"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<GostFach>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(GostFach.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getGostLupoExportMDBFuerJahrgang für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/lupo/export/mdb/jahrgang/{jahrgang}
	 * 
	 * Exportiert die Laufbahndaten für den übergebenen Jahrgang in eine LuPO-Lehrerdatei.
	 * 
	 * Mögliche HTTP-Antworten: 
	 *   Code 200: Die LuPO-Lehrerdatei
	 *     - Mime-Type: application/octet-stream
	 *     - Rückgabe-Typ: Blob
	 *   Code 403: Der Benutzer hat keine Rechter zum Export der Laufbahndaten.
	 *   Code 500: Ein interner Server-Fehler beim Erzeugen der LuPO-Datei.
	 * 
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {string} jahrgang - der Pfad-Parameter jahrgang
	 * 
	 * @returns Die LuPO-Lehrerdatei
	 */
	public async getGostLupoExportMDBFuerJahrgang(schema : string, jahrgang : string) : Promise<Blob> {
		let path : string = "/db/{schema}/gost/lupo/export/mdb/jahrgang/{jahrgang}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{jahrgang\s*(:[^}]+)?}/g, jahrgang);
		const data : Blob = await super.getOctetStream(path);
		return data;
	}


	/**
	 * Implementierung der POST-Methode setGostLupoImportMDBFuerJahrgang für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/lupo/import/mdb/jahrgang
	 * 
	 * Importiert die Laufbahndaten der übergebenen LuPO-Datenbank in das Schema mit dem angegebenen Namen.
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
	 * 
	 * @returns Der Log vom Import der Laufbahndaten
	 */
	public async setGostLupoImportMDBFuerJahrgang(data : FormData, schema : string) : Promise<SimpleOperationResponse> {
		let path : string = "/db/{schema}/gost/lupo/import/mdb/jahrgang"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
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
		let path : string = "/db/{schema}/gost/schueler/{id : \d+}/abiturdaten"
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
		let path : string = "/db/{schema}/gost/schueler/{id : \d+}/abiturdatenAusLeistungsdaten"
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
	 *   Code 200: Die Laufbahnplanungsdaten der gymnasialen Oberstfue des angegebenen Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Abiturdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Laufbahnplanungsdaten der Gymnasialen Oberstufe eines Schülers auszulesen.
	 *   Code 404: Kein Eintrag für einen Schüler mit Laufbahnplanungsdaten der gymnasialen Oberstufe für die angegebene ID gefunden
	 * 
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 * 
	 * @returns Die Laufbahnplanungsdaten der gymnasialen Oberstfue des angegebenen Schülers
	 */
	public async getGostSchuelerLaufbahnplanung(schema : string, id : number) : Promise<Abiturdaten> {
		let path : string = "/db/{schema}/gost/schueler/{id : \d+}/laufbahnplanung"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return Abiturdaten.transpilerFromJSON(text);
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
		let path : string = "/db/{schema}/gost/schueler/{id : \d+}/leistungsdaten"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostLeistungen.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getGostSchuelerPDFWahlbogen für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/schueler/{id : \d+}/wahlbogen
	 * 
	 * Erstellt den PDF-Wahlbogen für die gymnasiale Oberstufe zu dem Schüler mit der angegebenen ID. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen des Wahlbogens besitzt.
	 * 
	 * Mögliche HTTP-Antworten: 
	 *   Code 200: Der PDF-Wahlbogen für die gymnasialen Oberstufe des angegebenen Schülers
	 *     - Mime-Type: application/pdf
	 *     - Rückgabe-Typ: Blob
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Wahlbogen für die Gymnasialen Oberstufe eines Schülers zu erstellen.
	 *   Code 404: Kein Eintrag für einen Schüler mit Laufbahnplanungsdaten der gymnasialen Oberstufe für die angegebene ID gefunden
	 * 
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 * 
	 * @returns Der PDF-Wahlbogen für die gymnasialen Oberstufe des angegebenen Schülers
	 */
	public async getGostSchuelerPDFWahlbogen(schema : string, id : number) : Promise<Blob> {
		let path : string = "/db/{schema}/gost/schueler/{id : \d+}/wahlbogen"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const data : Blob = await super.getPDF(path);
		return data;
	}


	/**
	 * Implementierung der GET-Methode getGostSchuelerFachwahl für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/schueler/{schuelerid : \d+}/fachwahl/{fachid : \d+}
	 * 
	 * Liest für die gymnasiale Oberstufe die Fachwahlen zu einem Fach von dem angegebenen Schüler aus. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Fachwahlen besitzt.
	 * 
	 * Mögliche HTTP-Antworten: 
	 *   Code 200: Die Fachwahlen der gymnasialen Oberstfue für das angegebene Fach und den angegebenen Schüler
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Abiturdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Fachwahlen der Gymnasialen Oberstufe eines Schülers auszulesen.
	 *   Code 404: Kein Eintrag für einen Schüler mit Laufbahnplanungsdaten der gymnasialen Oberstufe für die angegebene ID gefunden
	 * 
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} schuelerid - der Pfad-Parameter schuelerid
	 * @param {number} fachid - der Pfad-Parameter fachid
	 * 
	 * @returns Die Fachwahlen der gymnasialen Oberstfue für das angegebene Fach und den angegebenen Schüler
	 */
	public async getGostSchuelerFachwahl(schema : string, schuelerid : number, fachid : number) : Promise<Abiturdaten> {
		let path : string = "/db/{schema}/gost/schueler/{schuelerid : \d+}/fachwahl/{fachid : \d+}"
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
	 * @param {GostSchuelerFachwahl} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} schuelerid - der Pfad-Parameter schuelerid
	 * @param {number} fachid - der Pfad-Parameter fachid
	 */
	public async patchGostSchuelerFachwahl(data : Partial<GostSchuelerFachwahl>, schema : string, schuelerid : number, fachid : number) : Promise<void> {
		let path : string = "/db/{schema}/gost/schueler/{schuelerid : \d+}/fachwahl/{fachid : \d+}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{schuelerid\s*(:[^}]+)?}/g, schuelerid.toString())
				.replace(/{fachid\s*(:[^}]+)?}/g, fachid.toString());
		let body : string = GostSchuelerFachwahl.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
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
		let path : string = "/db/{schema}/jahrgaenge/"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<JahrgangsListeEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(JahrgangsListeEintrag.transpilerFromJSON(text)); });
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
		let path : string = "/db/{schema}/jahrgaenge/{id : \d+}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return JahrgangsDaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getKatalogBeschaeftigungsart für den Zugriff auf die URL https://{hostname}/db/{schema}/kataloge/beschaeftigungsart
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
		let path : string = "/db/{schema}/kataloge/beschaeftigungsart"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<KatalogEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(KatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogBeschaeftigungsartmitID für den Zugriff auf die URL https://{hostname}/db/{schema}/kataloge/beschaeftigungsart/{id : \d+}
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
		let path : string = "/db/{schema}/kataloge/beschaeftigungsart/{id : \d+}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<KatalogEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(KatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der PATCH-Methode patchBeschaeftigungsart für den Zugriff auf die URL https://{hostname}/db/{schema}/kataloge/beschaeftigungsart/{id : \d+}
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
	 * @param {KatalogEintrag} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchBeschaeftigungsart(data : Partial<KatalogEintrag>, schema : string, id : number) : Promise<void> {
		let path : string = "/db/{schema}/kataloge/beschaeftigungsart/{id : \d+}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		let body : string = KatalogEintrag.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getKatalogBetriebsartmitID für den Zugriff auf die URL https://{hostname}/db/{schema}/kataloge/beschaeftigungsart/{id : \d+}
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
		let path : string = "/db/{schema}/kataloge/beschaeftigungsart/{id : \d+}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<KatalogEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(KatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogBetriebsart für den Zugriff auf die URL https://{hostname}/db/{schema}/kataloge/betriebsart
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
		let path : string = "/db/{schema}/kataloge/betriebsart"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<KatalogEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(KatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der PATCH-Methode patchBetriebsart für den Zugriff auf die URL https://{hostname}/db/{schema}/kataloge/betriebsart/{id : \d+}
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
	 * @param {KatalogEintrag} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchBetriebsart(data : Partial<KatalogEintrag>, schema : string, id : number) : Promise<void> {
		let path : string = "/db/{schema}/kataloge/betriebsart/{id : \d+}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		let body : string = KatalogEintrag.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getKatalogFaecher für den Zugriff auf die URL https://{hostname}/db/{schema}/kataloge/faecher
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
		let path : string = "/db/{schema}/kataloge/faecher"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<FachKatalogEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(FachKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogHaltestellen für den Zugriff auf die URL https://{hostname}/db/{schema}/kataloge/haltestellen
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
	public async getKatalogHaltestellen(schema : string) : Promise<List<KatalogEintrag>> {
		let path : string = "/db/{schema}/kataloge/haltestellen"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<KatalogEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(KatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogJahrgaenge für den Zugriff auf die URL https://{hostname}/db/{schema}/kataloge/jahrgaenge
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
		let path : string = "/db/{schema}/kataloge/jahrgaenge"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<JahrgangsKatalogEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(JahrgangsKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogKlassenarten für den Zugriff auf die URL https://{hostname}/db/{schema}/kataloge/klassenarten
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
		let path : string = "/db/{schema}/kataloge/klassenarten"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<KlassenartKatalogEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(KlassenartKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogKursarten für den Zugriff auf die URL https://{hostname}/db/{schema}/kataloge/kursarten
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
		let path : string = "/db/{schema}/kataloge/kursarten"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<KursartKatalogEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(KursartKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogOrganisationsformen für den Zugriff auf die URL https://{hostname}/db/{schema}/kataloge/organisationsformen
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
		let path : string = "/db/{schema}/kataloge/organisationsformen"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<OrganisationsformKatalogEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(OrganisationsformKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogOrte für den Zugriff auf die URL https://{hostname}/db/{schema}/kataloge/orte
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
	public async getKatalogOrte(schema : string) : Promise<List<OrtKatalogEintrag>> {
		let path : string = "/db/{schema}/kataloge/orte"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<OrtKatalogEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(OrtKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogOrtsteile für den Zugriff auf die URL https://{hostname}/db/{schema}/kataloge/ortsteile
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
	public async getKatalogOrtsteile(schema : string) : Promise<List<OrtsteilKatalogEintrag>> {
		let path : string = "/db/{schema}/kataloge/ortsteile"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<OrtsteilKatalogEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(OrtsteilKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogStrassen für den Zugriff auf die URL https://{hostname}/db/{schema}/kataloge/strassen
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
		let path : string = "/db/{schema}/kataloge/strassen"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<KatalogEintragStrassen>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(KatalogEintragStrassen.transpilerFromJSON(text)); });
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
		let path : string = "/db/{schema}/klassen/{id : \d+}"
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
		let path : string = "/db/{schema}/klassen/abschnitt/{abschnitt : \d+}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{abschnitt\s*(:[^}]+)?}/g, abschnitt.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<KlassenListeEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(KlassenListeEintrag.transpilerFromJSON(text)); });
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
		let path : string = "/db/{schema}/kurse/"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<KursListeEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(KursListeEintrag.transpilerFromJSON(text)); });
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
		let path : string = "/db/{schema}/kurse/{id : \d+}"
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
		let path : string = "/db/{schema}/kurse/abschnitt/{abschnitt : \d+}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{abschnitt\s*(:[^}]+)?}/g, abschnitt.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<KursListeEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(KursListeEintrag.transpilerFromJSON(text)); });
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
		let path : string = "/db/{schema}/lehrer/"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<LehrerListeEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(LehrerListeEintrag.transpilerFromJSON(text)); });
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
		let path : string = "/db/{schema}/lehrer/{id : \d+}/personaldaten"
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
	 * @param {LehrerPersonaldaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchLehrerPersonaldaten(data : Partial<LehrerPersonaldaten>, schema : string, id : number) : Promise<void> {
		let path : string = "/db/{schema}/lehrer/{id : \d+}/personaldaten"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		let body : string = LehrerPersonaldaten.transpilerToJSONPatch(data);
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
		let path : string = "/db/{schema}/lehrer/{id : \d+}/stammdaten"
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
	 * @param {LehrerStammdaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchLehrerStammdaten(data : Partial<LehrerStammdaten>, schema : string, id : number) : Promise<void> {
		let path : string = "/db/{schema}/lehrer/{id : \d+}/stammdaten"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		let body : string = LehrerStammdaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getLehrerAbgangsgruende für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/abgangsgruende
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
		let path : string = "/db/{schema}/lehrer/abgangsgruende"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<LehrerKatalogAbgangsgrundEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(LehrerKatalogAbgangsgrundEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLehrerAnrechnungsgruende für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/anrechnungsgruende
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
		let path : string = "/db/{schema}/lehrer/anrechnungsgruende"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<LehrerKatalogAnrechnungsgrundEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(LehrerKatalogAnrechnungsgrundEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLehrerBeschaeftigungsarten für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/beschaeftigungsarten
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
		let path : string = "/db/{schema}/lehrer/beschaeftigungsarten"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<LehrerKatalogBeschaeftigungsartEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(LehrerKatalogBeschaeftigungsartEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLehrerEinsatzstatus für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/einsatzstatus
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
		let path : string = "/db/{schema}/lehrer/einsatzstatus"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<LehrerKatalogEinsatzstatusEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(LehrerKatalogEinsatzstatusEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLehrerFachrichtungen für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/fachrichtungen
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
		let path : string = "/db/{schema}/lehrer/fachrichtungen"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<LehrerKatalogFachrichtungEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(LehrerKatalogFachrichtungEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLehrerFachrichtungAnerkennungen für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/fachrichtungen_anerkennungen
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
		let path : string = "/db/{schema}/lehrer/fachrichtungen_anerkennungen"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<LehrerKatalogFachrichtungAnerkennungEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(LehrerKatalogFachrichtungAnerkennungEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLehrerLehraemter für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/lehraemter
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
		let path : string = "/db/{schema}/lehrer/lehraemter"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<LehrerKatalogLehramtEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(LehrerKatalogLehramtEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLehrerLehramtAnerkennungen für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/lehraemter_anerkennungen
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
		let path : string = "/db/{schema}/lehrer/lehraemter_anerkennungen"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<LehrerKatalogLehramtAnerkennungEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(LehrerKatalogLehramtAnerkennungEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLehrerLehrbefaehigungen für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/lehrbefaehigungen
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
		let path : string = "/db/{schema}/lehrer/lehrbefaehigungen"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<LehrerKatalogLehrbefaehigungEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(LehrerKatalogLehrbefaehigungEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLehrerLehrbefaehigungenAnerkennungen für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/lehrbefaehigungen_anerkennungen
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
		let path : string = "/db/{schema}/lehrer/lehrbefaehigungen_anerkennungen"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<LehrerKatalogLehrbefaehigungAnerkennungEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(LehrerKatalogLehrbefaehigungAnerkennungEintrag.transpilerFromJSON(text)); });
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
	 *     - Rückgabe-Typ: List<LehrerKatalogLeitungsfunktionEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Lehrerleitungsfunktion-Katalog-Einträge gefunden
	 * 
	 * @param {string} schema - der Pfad-Parameter schema
	 * 
	 * @returns Eine Liste von Lehrerleitungsfunktion-Katalog-Einträgen
	 */
	public async getLehrerLeitungsfunktionen(schema : string) : Promise<List<LehrerKatalogLeitungsfunktionEintrag>> {
		let path : string = "/db/{schema}/lehrer/leitungsfunktionen"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<LehrerKatalogLeitungsfunktionEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(LehrerKatalogLeitungsfunktionEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLehrerMehrleistungsarten für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/mehrleistungsarten
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
		let path : string = "/db/{schema}/lehrer/mehrleistungsarten"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<LehrerKatalogMehrleistungsartEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(LehrerKatalogMehrleistungsartEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLehrerMinderleistungsarten für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/minderleistungsarten
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
		let path : string = "/db/{schema}/lehrer/minderleistungsarten"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<LehrerKatalogMinderleistungsartEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(LehrerKatalogMinderleistungsartEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLehrerRechtsverhaeltnisse für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/rechtsverhaeltnisse
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
		let path : string = "/db/{schema}/lehrer/rechtsverhaeltnisse"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<LehrerKatalogRechtsverhaeltnisEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(LehrerKatalogRechtsverhaeltnisEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLehrerZugangsgruende für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/zugangsgruende
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
		let path : string = "/db/{schema}/lehrer/zugangsgruende"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<LehrerKatalogZugangsgrundEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(LehrerKatalogZugangsgrundEintrag.transpilerFromJSON(text)); });
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
		let path : string = "/db/{schema}/schueler/{id : \d+}/abschnitt/{abschnitt : \d+}/lernabschnittsdaten"
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
		let path : string = "/db/{schema}/schueler/{id : \d+}/betriebe"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<SchuelerBetriebsdaten>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(SchuelerBetriebsdaten.transpilerFromJSON(text)); });
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
		let path : string = "/db/{schema}/schueler/{id : \d+}/betriebsstammdaten"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<BetriebStammdaten>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(BetriebStammdaten.transpilerFromJSON(text)); });
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
		let path : string = "/db/{schema}/schueler/{id : \d+}/erzieher"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<ErzieherStammdaten>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(ErzieherStammdaten.transpilerFromJSON(text)); });
		return ret;
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
		let path : string = "/db/{schema}/schueler/{id : \d+}/lernabschnitte"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<SchuelerLernabschnittListeEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(SchuelerLernabschnittListeEintrag.transpilerFromJSON(text)); });
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
		let path : string = "/db/{schema}/schueler/{id : \d+}/schulbesuch"
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
	 * @param {SchuelerSchulbesuchsdaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchSchuelerSchulbesuch(data : Partial<SchuelerSchulbesuchsdaten>, schema : string, id : number) : Promise<void> {
		let path : string = "/db/{schema}/schueler/{id : \d+}/schulbesuch"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		let body : string = SchuelerSchulbesuchsdaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
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
		let path : string = "/db/{schema}/schueler/{id : \d+}/stammdaten"
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
	 * @param {SchuelerStammdaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchSchuelerStammdaten(data : Partial<SchuelerStammdaten>, schema : string, id : number) : Promise<void> {
		let path : string = "/db/{schema}/schueler/{id : \d+}/stammdaten"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		let body : string = SchuelerStammdaten.transpilerToJSONPatch(data);
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
		let path : string = "/db/{schema}/schueler/abschnitt/{abschnitt : \d+}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{abschnitt\s*(:[^}]+)?}/g, abschnitt.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<SchuelerListeEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(SchuelerListeEintrag.transpilerFromJSON(text)); });
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
		let path : string = "/db/{schema}/schueler/aktuell"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<SchuelerListeEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(SchuelerListeEintrag.transpilerFromJSON(text)); });
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
		let path : string = "/db/{schema}/schueler/fahrschuelerarten"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<KatalogEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(KatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogAbgangsartenAllgemeinbildend für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/abgangsarten/allgemeinbildend
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
		let path : string = "/db/{schema}/schule/abgangsarten/allgemeinbildend"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const text = result;
		return AbgangsartKatalog.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getKatalogAbgangsartenBerufsbildend für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/abgangsarten/berufsbildend
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
		let path : string = "/db/{schema}/schule/abgangsarten/berufsbildend"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const text = result;
		return AbgangsartKatalog.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getAllgemeineMerkmale für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/allgemeine_merkmale
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
		let path : string = "/db/{schema}/schule/allgemeine_merkmale"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<AllgemeineMerkmaleKatalogEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(AllgemeineMerkmaleKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getBerufskollegAnlagen für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/berufskolleg/anlagen
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
		let path : string = "/db/{schema}/schule/berufskolleg/anlagen"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<BerufskollegAnlageKatalogEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(BerufskollegAnlageKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getBerufskollegBerufsebenen für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/berufskolleg/berufsebenen
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
		let path : string = "/db/{schema}/schule/berufskolleg/berufsebenen"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<BerufskollegBerufsebeneKatalogEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(BerufskollegBerufsebeneKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getBerufskollegFachklassen für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/berufskolleg/fachklassen
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
		let path : string = "/db/{schema}/schule/berufskolleg/fachklassen"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const text = result;
		return BerufskollegFachklassenKatalog.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getEinschulungsarten für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/einschulungsarten
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
		let path : string = "/db/{schema}/schule/einschulungsarten"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<EinschulungsartKatalogEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(EinschulungsartKatalogEintrag.transpilerFromJSON(text)); });
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
		let path : string = "/db/{schema}/schule/foerderschwerpunkt/{id : \d+}"
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
		let path : string = "/db/{schema}/schule/foerderschwerpunkte"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<FoerderschwerpunktEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(FoerderschwerpunktEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getFoerderschwerpunkte für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/foerderschwerpunkte/statistik
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
		let path : string = "/db/{schema}/schule/foerderschwerpunkte/statistik"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<FoerderschwerpunktKatalogEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(FoerderschwerpunktKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
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
	public async getSchullogo(schema : string) : Promise<String> {
		let path : string = "/db/{schema}/schule/logo"
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
	 * @param {String} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 */
	public async putSchullogo(data : String, schema : string) : Promise<void> {
		let path : string = "/db/{schema}/schule/logo"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		let body : string = JSON.stringify(data);
		return super.putJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getNationaelitaeten für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/nationalitaeten
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
		let path : string = "/db/{schema}/schule/nationalitaeten"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<NationalitaetenKatalogEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(NationalitaetenKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
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
	public async getSchuleNummer(schema : string) : Promise<Number> {
		let path : string = "/db/{schema}/schule/nummer"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const text = result;
		return parseInt(JSON.parse(text));
	}


	/**
	 * Implementierung der GET-Methode getPruefungsordnungen für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/pruefungsordnungen
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
		let path : string = "/db/{schema}/schule/pruefungsordnungen"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<PruefungsordnungKatalogEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(PruefungsordnungKatalogEintrag.transpilerFromJSON(text)); });
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
		let path : string = "/db/{schema}/schule/religionen"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<ReligionEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(ReligionEintrag.transpilerFromJSON(text)); });
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
		let path : string = "/db/{schema}/schule/religionen/{id : \d+}"
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
	 * @param {ReligionEintrag} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchReligion(data : Partial<ReligionEintrag>, schema : string, id : number) : Promise<void> {
		let path : string = "/db/{schema}/schule/religionen/{id : \d+}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		let body : string = ReligionEintrag.transpilerToJSONPatch(data);
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
		let path : string = "/db/{schema}/schule/religionen/new"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		let body : string = ReligionEintrag.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return ReligionEintrag.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getKatalogReligionen für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/religionen/statistik
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
		let path : string = "/db/{schema}/schule/religionen/statistik"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<ReligionKatalogEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(ReligionKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getSchulabschluesseAllgemeinbildend für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/schulabschluesse/allgemeinbildend
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
		let path : string = "/db/{schema}/schule/schulabschluesse/allgemeinbildend"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<SchulabschlussAllgemeinbildendKatalogEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(SchulabschlussAllgemeinbildendKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getSchulabschluesseBerufsbildend für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/schulabschluesse/berufsbildend
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
		let path : string = "/db/{schema}/schule/schulabschluesse/berufsbildend"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<SchulabschlussBerufsbildendKatalogEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(SchulabschlussBerufsbildendKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getSchulformen für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/schulformen
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
		let path : string = "/db/{schema}/schule/schulformen"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<SchulformKatalogEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(SchulformKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getSchulgliederungen für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/schulgliederungen
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
		let path : string = "/db/{schema}/schule/schulgliederungen"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<SchulgliederungKatalogEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(SchulgliederungKatalogEintrag.transpilerFromJSON(text)); });
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
		let path : string = "/db/{schema}/schule/stammdaten"
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
	 * @param {SchuleStammdaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 */
	public async patchSchuleStammdaten(data : Partial<SchuleStammdaten>, schema : string) : Promise<void> {
		let path : string = "/db/{schema}/schule/stammdaten"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		let body : string = SchuleStammdaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getVerkehrssprachen für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/verkehrssprachen
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
		let path : string = "/db/{schema}/schule/verkehrssprachen"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<VerkehrsspracheKatalogEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(VerkehrsspracheKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getSchuelerStundenplan für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/{id : \d+}/schueler/{schueler_id : \d+}
	 * 
	 * Erstellt den angebebenen Stundeplan in Bezug auf den angegebenen Schüler. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplandaten besitzt.
	 * 
	 * Mögliche HTTP-Antworten: 
	 *   Code 200: Der Schüler-Stundenplan
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SchuelerStundenplan
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Stundenplan anzusehen.
	 *   Code 404: Keinen Stundenplan gefunden
	 * 
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 * @param {number} schueler_id - der Pfad-Parameter schueler_id
	 * 
	 * @returns Der Schüler-Stundenplan
	 */
	public async getSchuelerStundenplan(schema : string, id : number, schueler_id : number) : Promise<SchuelerStundenplan> {
		let path : string = "/db/{schema}/stundenplan/{id : \d+}/schueler/{schueler_id : \d+}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{id\s*(:[^}]+)?}/g, id.toString())
				.replace(/{schueler_id\s*(:[^}]+)?}/g, schueler_id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return SchuelerStundenplan.transpilerFromJSON(text);
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
		let path : string = "/db/{schema}/stundenplan/{id : \d+}/zeitraster"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{id\s*(:[^}]+)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<StundenplanZeitraster>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(StundenplanZeitraster.transpilerFromJSON(text)); });
		return ret;
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
		let path : string = "/db/{schema}/stundenplan/liste"
				.replace(/{schema\s*(:[^}]+)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<StundenplanListeEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(StundenplanListeEintrag.transpilerFromJSON(text)); });
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
		let path : string = "/db/{schema}/stundenplan/liste/{abschnitt : \d+}"
				.replace(/{schema\s*(:[^}]+)?}/g, schema)
				.replace(/{abschnitt\s*(:[^}]+)?}/g, abschnitt.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		let ret = new Vector<StundenplanListeEintrag>();
		obj.forEach((elem: any) => { let text : string = JSON.stringify(elem); ret.add(StundenplanListeEintrag.transpilerFromJSON(text)); });
		return ret;
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
	public async isAlive() : Promise<String> {
		let path : string = "/status/alive";
		const text : string = await super.getText(path);
		return String(text);
	}


}
