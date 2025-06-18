import { BaseApi, type ApiFile } from '../api/BaseApi';
import { AbgangsartKatalog } from '../core/data/schule/AbgangsartKatalog';
import { Abiturdaten } from '../core/data/gost/Abiturdaten';
import { Abteilung } from '../core/data/schule/Abteilung';
import { AbteilungKlassenzuordnung } from '../core/data/schule/AbteilungKlassenzuordnung';
import { AllgemeineMerkmaleKatalogEintrag } from '../core/data/schule/AllgemeineMerkmaleKatalogEintrag';
import { ArrayList } from '../java/util/ArrayList';
import { Aufsichtsbereich } from '../core/data/schule/Aufsichtsbereich';
import { BenutzerAllgemeinCredentials } from '../core/data/benutzer/BenutzerAllgemeinCredentials';
import { BenutzerConfig } from '../core/data/benutzer/BenutzerConfig';
import { BenutzerDaten } from '../core/data/benutzer/BenutzerDaten';
import { BenutzerEMailDaten } from '../core/data/benutzer/BenutzerEMailDaten';
import { BenutzergruppeDaten } from '../core/data/benutzer/BenutzergruppeDaten';
import { BenutzergruppeListeEintrag } from '../core/data/benutzer/BenutzergruppeListeEintrag';
import { BenutzerKompetenzGruppenKatalogEintrag } from '../core/data/benutzer/BenutzerKompetenzGruppenKatalogEintrag';
import { BenutzerKompetenzKatalogEintrag } from '../core/data/benutzer/BenutzerKompetenzKatalogEintrag';
import { BenutzerLehrerCredentials } from '../core/data/benutzer/BenutzerLehrerCredentials';
import { BenutzerListeEintrag } from '../core/data/benutzer/BenutzerListeEintrag';
import { BerufskollegAnlageKatalogEintrag } from '../asd/data/schule/BerufskollegAnlageKatalogEintrag';
import { BerufskollegBerufsebeneKatalogEintrag } from '../core/data/schule/BerufskollegBerufsebeneKatalogEintrag';
import { BerufskollegFachklassenKatalog } from '../core/data/schule/BerufskollegFachklassenKatalog';
import { BetriebAnsprechpartner } from '../core/data/betrieb/BetriebAnsprechpartner';
import { BetriebListeEintrag } from '../core/data/betrieb/BetriebListeEintrag';
import { BetriebStammdaten } from '../core/data/betrieb/BetriebStammdaten';
import { BilingualeSpracheKatalogEintrag } from '../asd/data/fach/BilingualeSpracheKatalogEintrag';
import { BKGymAbiturdaten } from '../core/abschluss/bk/d/BKGymAbiturdaten';
import { BKGymLeistungen } from '../core/data/bk/abi/BKGymLeistungen';
import { DatenbankVerbindungsdaten } from '../core/data/schema/DatenbankVerbindungsdaten';
import { DBSchemaListeEintrag } from '../core/data/db/DBSchemaListeEintrag';
import { EinschulungsartKatalogEintrag } from '../asd/data/schueler/EinschulungsartKatalogEintrag';
import { Einwilligungsart } from '../core/data/schule/Einwilligungsart';
import { ENMConfigResponse } from '../core/data/enm/ENMConfigResponse';
import { ENMDaten } from '../core/data/enm/ENMDaten';
import { ENMLehrerInitialKennwort } from '../core/data/enm/ENMLehrerInitialKennwort';
import { ENMServerConfigElement } from '../core/data/enm/ENMServerConfigElement';
import { Erzieherart } from '../core/data/erzieher/Erzieherart';
import { ErzieherListeEintrag } from '../core/data/erzieher/ErzieherListeEintrag';
import { ErzieherStammdaten } from '../core/data/erzieher/ErzieherStammdaten';
import { FachDaten } from '../core/data/fach/FachDaten';
import { FachgruppeKatalogEintrag } from '../asd/data/fach/FachgruppeKatalogEintrag';
import { FachKatalogEintrag } from '../asd/data/fach/FachKatalogEintrag';
import { FaecherListeEintrag } from '../core/data/fach/FaecherListeEintrag';
import { FoerderschwerpunktEintrag } from '../core/data/schule/FoerderschwerpunktEintrag';
import { FoerderschwerpunktKatalogEintrag } from '../asd/data/schule/FoerderschwerpunktKatalogEintrag';
import { GEAbschlussFaecher } from '../core/data/abschluss/GEAbschlussFaecher';
import { GostBelegpruefungErgebnis } from '../core/abschluss/gost/GostBelegpruefungErgebnis';
import { GostBelegpruefungsErgebnisse } from '../core/data/gost/GostBelegpruefungsErgebnisse';
import { GostBeratungslehrer } from '../core/data/gost/GostBeratungslehrer';
import { GostBlockungKurs } from '../core/data/gost/GostBlockungKurs';
import { GostBlockungKursAufteilung } from '../core/data/gost/GostBlockungKursAufteilung';
import { GostBlockungKursLehrer } from '../core/data/gost/GostBlockungKursLehrer';
import { GostBlockungListeneintrag } from '../core/data/gost/GostBlockungListeneintrag';
import { GostBlockungRegel } from '../core/data/gost/GostBlockungRegel';
import { GostBlockungRegelUpdate } from '../core/data/gost/GostBlockungRegelUpdate';
import { GostBlockungSchiene } from '../core/data/gost/GostBlockungSchiene';
import { GostBlockungsdaten } from '../core/data/gost/GostBlockungsdaten';
import { GostBlockungsergebnis } from '../core/data/gost/GostBlockungsergebnis';
import { GostBlockungsergebnisKursSchienenZuordnung } from '../core/data/gost/GostBlockungsergebnisKursSchienenZuordnung';
import { GostBlockungsergebnisKursSchuelerZuordnung } from '../core/data/gost/GostBlockungsergebnisKursSchuelerZuordnung';
import { GostBlockungsergebnisKursSchuelerZuordnungUpdate } from '../core/data/gost/GostBlockungsergebnisKursSchuelerZuordnungUpdate';
import { GostFach } from '../core/data/gost/GostFach';
import { GostJahrgang } from '../core/data/gost/GostJahrgang';
import { GostJahrgangFachkombination } from '../core/data/gost/GostJahrgangFachkombination';
import { GostJahrgangFachwahlen } from '../core/data/gost/GostJahrgangFachwahlen';
import { GostJahrgangFachwahlenHalbjahr } from '../core/data/gost/GostJahrgangFachwahlenHalbjahr';
import { GostJahrgangsdaten } from '../core/data/gost/GostJahrgangsdaten';
import { GostKlausurenCollectionAllData } from '../core/data/gost/klausurplanung/GostKlausurenCollectionAllData';
import { GostKlausurenCollectionData } from '../core/data/gost/klausurplanung/GostKlausurenCollectionData';
import { GostKlausurenCollectionHjData } from '../core/data/gost/klausurplanung/GostKlausurenCollectionHjData';
import { GostKlausurenCollectionSkrsKrsData } from '../core/data/gost/klausurplanung/GostKlausurenCollectionSkrsKrsData';
import { GostKlausurenUpdate } from '../core/data/gost/klausurplanung/GostKlausurenUpdate';
import { GostKlausurraum } from '../core/data/gost/klausurplanung/GostKlausurraum';
import { GostKlausurraumRich } from '../core/data/gost/klausurplanung/GostKlausurraumRich';
import { GostKlausurtermin } from '../core/data/gost/klausurplanung/GostKlausurtermin';
import { GostKlausurterminblockungDaten } from '../core/data/gost/klausurplanung/GostKlausurterminblockungDaten';
import { GostKlausurvorgabe } from '../core/data/gost/klausurplanung/GostKlausurvorgabe';
import { GostKursklausur } from '../core/data/gost/klausurplanung/GostKursklausur';
import { GostLaufbahnplanungBeratungsdaten } from '../core/data/gost/GostLaufbahnplanungBeratungsdaten';
import { GostLaufbahnplanungDaten } from '../core/data/gost/GostLaufbahnplanungDaten';
import { GostLeistungen } from '../core/data/gost/GostLeistungen';
import { GostNachschreibterminblockungKonfiguration } from '../core/data/gost/klausurplanung/GostNachschreibterminblockungKonfiguration';
import { GostSchuelerFachwahl } from '../core/data/gost/GostSchuelerFachwahl';
import { GostSchuelerklausur } from '../core/data/gost/klausurplanung/GostSchuelerklausur';
import { GostSchuelerklausurTermin } from '../core/data/gost/klausurplanung/GostSchuelerklausurTermin';
import { GostStatistikFachwahl } from '../core/data/gost/GostStatistikFachwahl';
import { HerkunftKatalogEintrag } from '../core/data/schule/HerkunftKatalogEintrag';
import { HerkunftsartKatalogEintrag } from '../core/data/schule/HerkunftsartKatalogEintrag';
import { HerkunftsschulnummerKatalogEintrag } from '../core/data/schule/HerkunftsschulnummerKatalogEintrag';
import { JahrgaengeKatalogEintrag } from '../asd/data/jahrgang/JahrgaengeKatalogEintrag';
import { JahrgangsDaten } from '../core/data/jahrgang/JahrgangsDaten';
import { KAOAAnschlussoptionenKatalogEintrag } from '../asd/data/kaoa/KAOAAnschlussoptionenKatalogEintrag';
import { KAOABerufsfeldKatalogEintrag } from '../asd/data/kaoa/KAOABerufsfeldKatalogEintrag';
import { KAOAEbene4KatalogEintrag } from '../asd/data/kaoa/KAOAEbene4KatalogEintrag';
import { KAOAKategorieKatalogEintrag } from '../asd/data/kaoa/KAOAKategorieKatalogEintrag';
import { KAOAMerkmalKatalogEintrag } from '../asd/data/kaoa/KAOAMerkmalKatalogEintrag';
import { KAOAZusatzmerkmalKatalogEintrag } from '../asd/data/kaoa/KAOAZusatzmerkmalKatalogEintrag';
import { KatalogEintrag } from '../core/data/kataloge/KatalogEintrag';
import { KatalogEintragOrte } from '../core/data/kataloge/KatalogEintragOrte';
import { KatalogEintragOrtsteile } from '../core/data/kataloge/KatalogEintragOrtsteile';
import { KatalogEintragStrassen } from '../core/data/kataloge/KatalogEintragStrassen';
import { KatalogEntlassgrund } from '../core/data/kataloge/KatalogEntlassgrund';
import { Kindergarten } from '../core/data/schule/Kindergarten';
import { KindergartenbesuchKatalogEintrag } from '../asd/data/schule/KindergartenbesuchKatalogEintrag';
import { KlassenartKatalogEintrag } from '../asd/data/klassen/KlassenartKatalogEintrag';
import { KlassenDaten } from '../asd/data/klassen/KlassenDaten';
import { KursDaten } from '../asd/data/kurse/KursDaten';
import { LehrerAbgangsgrundKatalogEintrag } from '../asd/data/lehrer/LehrerAbgangsgrundKatalogEintrag';
import { LehrerAnrechnungsgrundKatalogEintrag } from '../asd/data/lehrer/LehrerAnrechnungsgrundKatalogEintrag';
import { LehrerBeschaeftigungsartKatalogEintrag } from '../asd/data/lehrer/LehrerBeschaeftigungsartKatalogEintrag';
import { LehrerEinsatzstatusKatalogEintrag } from '../asd/data/lehrer/LehrerEinsatzstatusKatalogEintrag';
import { LehrerEinwilligung } from '../core/data/lehrer/LehrerEinwilligung';
import { LehrerFachrichtungAnerkennungKatalogEintrag } from '../asd/data/lehrer/LehrerFachrichtungAnerkennungKatalogEintrag';
import { LehrerFachrichtungKatalogEintrag } from '../asd/data/lehrer/LehrerFachrichtungKatalogEintrag';
import { LehrerLehramtAnerkennungKatalogEintrag } from '../asd/data/lehrer/LehrerLehramtAnerkennungKatalogEintrag';
import { LehrerLehramtKatalogEintrag } from '../asd/data/lehrer/LehrerLehramtKatalogEintrag';
import { LehrerLehrbefaehigungAnerkennungKatalogEintrag } from '../asd/data/lehrer/LehrerLehrbefaehigungAnerkennungKatalogEintrag';
import { LehrerLehrbefaehigungKatalogEintrag } from '../asd/data/lehrer/LehrerLehrbefaehigungKatalogEintrag';
import { LehrerLeitungsfunktionKatalogEintrag } from '../asd/data/lehrer/LehrerLeitungsfunktionKatalogEintrag';
import { LehrerLernplattform } from '../core/data/lehrer/LehrerLernplattform';
import { LehrerListeEintrag } from '../core/data/lehrer/LehrerListeEintrag';
import { LehrerMehrleistungsartKatalogEintrag } from '../asd/data/lehrer/LehrerMehrleistungsartKatalogEintrag';
import { LehrerMinderleistungsartKatalogEintrag } from '../asd/data/lehrer/LehrerMinderleistungsartKatalogEintrag';
import { LehrerPersonalabschnittsdaten } from '../asd/data/lehrer/LehrerPersonalabschnittsdaten';
import { LehrerPersonalabschnittsdatenAnrechnungsstunden } from '../asd/data/lehrer/LehrerPersonalabschnittsdatenAnrechnungsstunden';
import { LehrerPersonaldaten } from '../asd/data/lehrer/LehrerPersonaldaten';
import { LehrerRechtsverhaeltnisKatalogEintrag } from '../asd/data/lehrer/LehrerRechtsverhaeltnisKatalogEintrag';
import { LehrerStammdaten } from '../asd/data/lehrer/LehrerStammdaten';
import { LehrerZugangsgrundKatalogEintrag } from '../asd/data/lehrer/LehrerZugangsgrundKatalogEintrag';
import { Lernplattform } from '../core/data/schule/Lernplattform';
import { List } from '../java/util/List';
import { LongAndStringLists } from '../core/data/LongAndStringLists';
import { Merkmal } from '../core/data/schule/Merkmal';
import { NationalitaetenKatalogEintrag } from '../asd/data/schule/NationalitaetenKatalogEintrag';
import { NoteKatalogEintrag } from '../asd/data/NoteKatalogEintrag';
import { OAuth2ClientConnection } from '../core/data/oauth2/OAuth2ClientConnection';
import { OrganisationsformKatalogEintrag } from '../asd/data/schule/OrganisationsformKatalogEintrag';
import { OrtKatalogEintrag } from '../core/data/kataloge/OrtKatalogEintrag';
import { OrtsteilKatalogEintrag } from '../core/data/kataloge/OrtsteilKatalogEintrag';
import { PruefungsordnungKatalogEintrag } from '../core/data/schule/PruefungsordnungKatalogEintrag';
import { Raum } from '../core/data/schule/Raum';
import { ReformpaedagogikKatalogEintrag } from '../core/data/schule/ReformpaedagogikKatalogEintrag';
import { ReligionEintrag } from '../core/data/schule/ReligionEintrag';
import { ReligionKatalogEintrag } from '../asd/data/schule/ReligionKatalogEintrag';
import { ReportingParameter } from '../core/data/reporting/ReportingParameter';
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
import { SchuelerBetriebsdaten } from '../asd/data/schueler/SchuelerBetriebsdaten';
import { SchuelerEinwilligung } from '../core/data/schueler/SchuelerEinwilligung';
import { SchuelerEinwilligungsartenZusammenfassung } from '../core/data/schueler/SchuelerEinwilligungsartenZusammenfassung';
import { SchuelerKAoADaten } from '../core/data/schueler/SchuelerKAoADaten';
import { SchuelerLeistungsdaten } from '../asd/data/schueler/SchuelerLeistungsdaten';
import { SchuelerLernabschnittBemerkungen } from '../asd/data/schueler/SchuelerLernabschnittBemerkungen';
import { SchuelerLernabschnittListeEintrag } from '../core/data/schueler/SchuelerLernabschnittListeEintrag';
import { SchuelerLernabschnittsdaten } from '../asd/data/schueler/SchuelerLernabschnittsdaten';
import { SchuelerLernplattform } from '../core/data/schueler/SchuelerLernplattform';
import { SchuelerListeEintrag } from '../core/data/schueler/SchuelerListeEintrag';
import { SchuelerSchulbesuchMerkmal } from '../asd/data/schueler/SchuelerSchulbesuchMerkmal';
import { SchuelerSchulbesuchSchule } from '../asd/data/schueler/SchuelerSchulbesuchSchule';
import { SchuelerSchulbesuchsdaten } from '../asd/data/schueler/SchuelerSchulbesuchsdaten';
import { SchuelerStammdaten } from '../asd/data/schueler/SchuelerStammdaten';
import { SchuelerStatusKatalogEintrag } from '../asd/data/schueler/SchuelerStatusKatalogEintrag';
import { SchuelerTelefon } from '../core/data/schueler/SchuelerTelefon';
import { SchuelerVermerkartZusammenfassung } from '../core/data/schueler/SchuelerVermerkartZusammenfassung';
import { SchuelerVermerke } from '../core/data/schueler/SchuelerVermerke';
import { SchulabschlussAllgemeinbildendKatalogEintrag } from '../asd/data/schule/SchulabschlussAllgemeinbildendKatalogEintrag';
import { SchulabschlussBerufsbildendKatalogEintrag } from '../asd/data/schule/SchulabschlussBerufsbildendKatalogEintrag';
import { SchulEintrag } from '../core/data/kataloge/SchulEintrag';
import { SchulenKatalogEintrag } from '../core/data/schule/SchulenKatalogEintrag';
import { SchuleStammdaten } from '../asd/data/schule/SchuleStammdaten';
import { SchulformKatalogEintrag } from '../asd/data/schule/SchulformKatalogEintrag';
import { SchulgliederungKatalogEintrag } from '../asd/data/schule/SchulgliederungKatalogEintrag';
import { Schulleitung } from '../asd/data/schule/Schulleitung';
import { SchultraegerKatalogEintrag } from '../core/data/schule/SchultraegerKatalogEintrag';
import { SimpleOperationResponse } from '../core/data/SimpleOperationResponse';
import { SMTPServerKonfiguration } from '../core/data/email/SMTPServerKonfiguration';
import { Sprachbelegung } from '../asd/data/schueler/Sprachbelegung';
import { Sprachpruefung } from '../asd/data/schueler/Sprachpruefung';
import { SprachpruefungsniveauKatalogEintrag } from '../core/data/fach/SprachpruefungsniveauKatalogEintrag';
import { SprachreferenzniveauKatalogEintrag } from '../asd/data/fach/SprachreferenzniveauKatalogEintrag';
import { Stundenplan } from '../core/data/stundenplan/Stundenplan';
import { StundenplanAufsichtsbereich } from '../core/data/stundenplan/StundenplanAufsichtsbereich';
import { StundenplanKalenderwochenzuordnung } from '../core/data/stundenplan/StundenplanKalenderwochenzuordnung';
import { StundenplanKomplett } from '../core/data/stundenplan/StundenplanKomplett';
import { StundenplanLehrer } from '../core/data/stundenplan/StundenplanLehrer';
import { StundenplanListeEintrag } from '../core/data/stundenplan/StundenplanListeEintrag';
import { StundenplanPausenaufsicht } from '../core/data/stundenplan/StundenplanPausenaufsicht';
import { StundenplanPausenaufsichtBereich } from '../core/data/stundenplan/StundenplanPausenaufsichtBereich';
import { StundenplanPausenaufsichtBereichUpdate } from '../core/data/stundenplan/StundenplanPausenaufsichtBereichUpdate';
import { StundenplanPausenzeit } from '../core/data/stundenplan/StundenplanPausenzeit';
import { StundenplanRaum } from '../core/data/stundenplan/StundenplanRaum';
import { StundenplanSchiene } from '../core/data/stundenplan/StundenplanSchiene';
import { StundenplanUnterricht } from '../core/data/stundenplan/StundenplanUnterricht';
import { StundenplanUnterrichtsverteilung } from '../core/data/stundenplan/StundenplanUnterrichtsverteilung';
import { StundenplanZeitraster } from '../core/data/stundenplan/StundenplanZeitraster';
import { TelefonArt } from '../core/data/schule/TelefonArt';
import { UebergangsempfehlungKatalogEintrag } from '../asd/data/schueler/UebergangsempfehlungKatalogEintrag';
import { VerkehrsspracheKatalogEintrag } from '../asd/data/schule/VerkehrsspracheKatalogEintrag';
import { VermerkartEintrag } from '../core/data/schule/VermerkartEintrag';
import { WiedervorlageEintrag } from '../core/data/schule/WiedervorlageEintrag';
import { ZulaessigeKursartKatalogEintrag } from '../asd/data/kurse/ZulaessigeKursartKatalogEintrag';

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
	 *     - Mime-Type: application/octet-stream
	 *     - Rückgabe-Typ: ApiFile
	 *   Code 500: Das Zertifikat wurde nicht gefunden
	 *
	 * @returns Das Base-64-kodierte Zertifikat des Servers
	 */
	public async getConfigCertificateBase64() : Promise<ApiFile> {
		const path = "/config/certificate_base64";
		const data : ApiFile = await super.getOctetStream(path);
		return data;
	}


	/**
	 * Implementierung der GET-Methode getConfigCertificateFile für den Zugriff auf die URL https://{hostname}/config/certificate_file
	 *
	 * Gibt die Zertifikatsdatei des Server zurück.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Zertifikatsdatei des Servers
	 *     - Mime-Type: application/octet-stream
	 *     - Rückgabe-Typ: ApiFile
	 *   Code 404: Das Zertifikat wurde nicht gefunden
	 *   Code 500: Es konnte nicht auf das Zertifikat zugegriffen werden
	 *
	 * @returns Die Zertifikatsdatei des Servers
	 */
	public async getConfigCertificateFile() : Promise<ApiFile> {
		const path = "/config/certificate_file";
		const data : ApiFile = await super.getOctetStream(path);
		return data;
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const text = result;
		return BenutzerDaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getBenutzerEmailDaten für den Zugriff auf die URL https://{hostname}/db/{schema}/benutzer/angemeldet/daten/email
	 *
	 * Liefert zu dem angemeldeten Benutzer, der diese Abfrage ausführt, die zugehörigen EMail-Daten. Dabei wird geprüft, ob der SVWS-Benutzer angemeldet ist.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die EMail-Daten des Benutzers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: BenutzerEMailDaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Benutzerdaten anzusehen.
	 *   Code 404: Kein Benutzer-Eintrag gefunden
	 *   Code 500: Ein interner Fehler ist aus dem Server aufgetreten
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die EMail-Daten des Benutzers
	 */
	public async getBenutzerEmailDaten(schema : string) : Promise<BenutzerEMailDaten> {
		const path = "/db/{schema}/benutzer/angemeldet/daten/email"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const text = result;
		return BenutzerEMailDaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchBenutzerEmailDaten für den Zugriff auf die URL https://{hostname}/db/{schema}/benutzer/angemeldet/daten/email
	 *
	 * Passt die EMail-Daten des angemeldeten Benutzers an.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Der Patch wurde erfolgreich integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 404: Kein Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<BenutzerEMailDaten>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 */
	public async patchBenutzerEmailDaten(data : Partial<BenutzerEMailDaten>, schema : string) : Promise<void> {
		const path = "/db/{schema}/benutzer/angemeldet/daten/email"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = BenutzerEMailDaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = BenutzerAllgemeinCredentials.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return BenutzerDaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode createBenutzerLehrer für den Zugriff auf die URL https://{hostname}/db/{schema}/benutzer/new/lehrer
	 *
	 * Erstellt einen neuen Lehrer-Benutzer und gibt ihn zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Benutzers besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Benutzer wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: BenutzerDaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Benutzer anzulegen.
	 *   Code 409: Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {BenutzerLehrerCredentials} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Benutzer wurde erfolgreich angelegt.
	 */
	public async createBenutzerLehrer(data : BenutzerLehrerCredentials, schema : string) : Promise<BenutzerDaten> {
		const path = "/db/{schema}/benutzer/new/lehrer"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = BenutzerLehrerCredentials.transpilerToJSON(data);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{betrieb_id\s*(:[^{}]+({[^{}]+})*)?}/g, betrieb_id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = SchuelerBetriebsdaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getSchuelerBetriebsdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/betriebe/{id : \d+}/betrieb
	 *
	 * Liest die Daten des Schülerbetriebs zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen vom Schülerbetrieb besitzt.
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return SchuelerBetriebsdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getBetriebAnsprechpartnerdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/betriebe/{id : \d+}/betriebansprechpartner
	 *
	 * Liest die Daten des Betriebanpsrechpartners zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen vom Betriebanpsrechpartner besitzt.
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<BetriebAnsprechpartner>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(BetriebAnsprechpartner.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogBeschaeftigungsart für den Zugriff auf die URL https://{hostname}/db/{schema}/betriebe/beschaeftigungsart
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhandenen Beschäftigungsarten unter Angabe der ID, eines Kürzels und der textuellen Beschreibung sowie der Information, ob der Eintrag in der Anwendung sichtbar bzw. änderbar sein soll, und gibt diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKatalogBeschaeftigungsartmitID für den Zugriff auf die URL https://{hostname}/db/{schema}/betriebe/beschaeftigungsart/{id : \d+}
	 *
	 * Liest die Daten der Beschäftigunsart zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Beschäftigungsart besitzt.
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = KatalogEintrag.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getKatalogBetriebsartmitID für den Zugriff auf die URL https://{hostname}/db/{schema}/betriebe/beschaeftigungsart/{id : \d+}
	 *
	 * Liest die Daten der Betriebsart zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Betriebsarten besitzt.
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = KatalogEintrag.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return KatalogEintrag.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getBetriebeAnsprechpartner für den Zugriff auf die URL https://{hostname}/db/{schema}/betriebe/betriebansprechpartner
	 *
	 * Erstellt eine Liste aller in der Datenbank vorhandenen Betriebansprechpartner, des Ansprechpartnername, Kontaktdaten, ob sie in der Anwendung sichtbar bzw. änderbar sein sollen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Betriebsansprechpartnern besitzt.
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		await super.deleteJSON(path, body);
		return;
	}


	/**
	 * Implementierung der GET-Methode getKatalogBetriebsart für den Zugriff auf die URL https://{hostname}/db/{schema}/betriebe/betriebsart
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhandenen Betriebsarten unter Angabe der ID, eines Kürzels und der textuellen Beschreibung sowie der Information, ob der Eintrag in der Anwendung sichtbar bzw. änderbar sein soll, und gibt diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{schueler_id\s*(:[^{}]+({[^{}]+})*)?}/g, schueler_id.toString())
			.replace(/{betrieb_id\s*(:[^{}]+({[^{}]+})*)?}/g, betrieb_id.toString());
		const body : string = SchuelerBetriebsdaten.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SchuelerBetriebsdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getBKGymSchuelerAbiturdatenAusLeistungsdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/bk/gym/schueler/{id : \d+}/laufbahn
	 *
	 * Liest die Abiturdaten des beruflichen Gymnasiums aus den Leistungsdaten des Schülers mit der angegebenen ID aus der Datenbank aus und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Abiturdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Abiturdaten aus der Laufbahn des angegebenen Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: BKGymAbiturdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Abiturdaten eines Schülers auszulesen.
	 *   Code 404: Kein Eintrag für einen Schüler mit Daten des beruflichen Gymnasiums für die angegebene ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Abiturdaten aus der Laufbahn des angegebenen Schülers
	 */
	public async getBKGymSchuelerAbiturdatenAusLeistungsdaten(schema : string, id : number) : Promise<BKGymAbiturdaten> {
		const path = "/db/{schema}/bk/gym/schueler/{id : \\d+}/laufbahn"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return BKGymAbiturdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getBKGymSchuelerLeistungsdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/bk/gym/schueler/{id : \d+}/leistungsdaten
	 *
	 * Liest die Leistungsdaten in Bezug auf das berufliche Gymnasium des Schülers mit der angegebene ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Leistungsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Leistungsdaten des Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: BKGymLeistungen
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Leistungsdaten anzusehen.
	 *   Code 404: Kein Schüler-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Leistungsdaten des Schülers
	 */
	public async getBKGymSchuelerLeistungsdaten(schema : string, id : number) : Promise<BKGymLeistungen> {
		const path = "/db/{schema}/bk/gym/schueler/{id : \\d+}/leistungsdaten"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return BKGymLeistungen.transpilerFromJSON(text);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{app\s*(:[^{}]+({[^{}]+})*)?}/g, app);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{app\s*(:[^{}]+({[^{}]+})*)?}/g, app)
			.replace(/{key\s*(:[^{}]+({[^{}]+})*)?}/g, key);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{app\s*(:[^{}]+({[^{}]+})*)?}/g, app)
			.replace(/{key\s*(:[^{}]+({[^{}]+})*)?}/g, key);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{app\s*(:[^{}]+({[^{}]+})*)?}/g, app)
			.replace(/{key\s*(:[^{}]+({[^{}]+})*)?}/g, key);
		const body : string = JSON.stringify(data);
		return super.putJSON(path, body);
	}


	/**
	 * Implementierung der POST-Methode importKurs42Raeume für den Zugriff auf die URL https://{hostname}/db/{schema}/datenaustausch/gost/kurs42/import/raeume
	 *
	 * Importiert die Räume aus Kurs 42 in das Schema mit dem angegebenen Namen.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Import der Räume aus Kurs 42
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 400: Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Der Benutzer hat keine Berechtigung, um die Räume aus Kurs 42 zu importieren.
	 *   Code 404: Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 409: Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 500: Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {FormData} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Log vom Import der Räume aus Kurs 42
	 */
	public async importKurs42Raeume(data : FormData, schema : string) : Promise<SimpleOperationResponse> {
		const path = "/db/{schema}/datenaustausch/gost/kurs42/import/raeume"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.postMultipart(path, data);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode importKurs42Blockung für den Zugriff auf die URL https://{hostname}/db/{schema}/datenaustausch/gost/kurs42/import/zip
	 *
	 * Importiert die Kurs 42-Blockung aus dem übergebenen ZIP-File in das Schema mit dem angegebenen Namen.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Import der Kurs 42-Blockung
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 400: Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Der Benutzer hat keine Berechtigung, um die Kurs 42-Blockung zu importieren.
	 *   Code 404: Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 409: Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 500: Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {FormData} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Log vom Import der Kurs 42-Blockung
	 */
	public async importKurs42Blockung(data : FormData, schema : string) : Promise<SimpleOperationResponse> {
		const path = "/db/{schema}/datenaustausch/gost/kurs42/import/zip"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.postMultipart(path, data);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getGostLupoExportMDBFuerJahrgang für den Zugriff auf die URL https://{hostname}/db/{schema}/datenaustausch/gost/lupo/export/mdb/jahrgang/{jahrgang}
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
		const path = "/db/{schema}/datenaustausch/gost/lupo/export/mdb/jahrgang/{jahrgang}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{jahrgang\s*(:[^{}]+({[^{}]+})*)?}/g, jahrgang);
		const data : ApiFile = await super.getOctetStream(path);
		return data;
	}


	/**
	 * Implementierung der POST-Methode setGostLupoImportMDBFuerJahrgang für den Zugriff auf die URL https://{hostname}/db/{schema}/datenaustausch/gost/lupo/import/mdb/jahrgang/replace/{mode}
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
		const path = "/db/{schema}/datenaustausch/gost/lupo/import/mdb/jahrgang/replace/{mode}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{mode\s*(:[^{}]+({[^{}]+})*)?}/g, mode);
		const result : string = await super.postMultipart(path, data);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode exportUntisBlockungGPU002GPU015GPU019 für den Zugriff auf die URL https://{hostname}/db/{schema}/datenaustausch/untis/export/blockung/{id : \d+}/{sidvariante : \d+}
	 *
	 * Liefert einen Export für die Blockungsergebnisse als Liste mit drei Strings (GPU002.txt, GPU015.txt, GPU019.txt).Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Exportieren besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Liste mit den Daten der drei GPUs
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<String>
	 *   Code 404: Es wurden nicht alle benötigten Daten für den Export gefunden.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 500: Es ist ein unerwarteter Fehler aufgetreten.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {LongAndStringLists} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 * @param {number} sidvariante - der Pfad-Parameter sidvariante
	 *
	 * @returns Die Liste mit den Daten der drei GPUs
	 */
	public async exportUntisBlockungGPU002GPU015GPU019(data : LongAndStringLists, schema : string, id : number, sidvariante : number) : Promise<List<string>> {
		const path = "/db/{schema}/datenaustausch/untis/export/blockung/{id : \\d+}/{sidvariante : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString())
			.replace(/{sidvariante\s*(:[^{}]+({[^{}]+})*)?}/g, sidvariante.toString());
		const body : string = LongAndStringLists.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<string>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(JSON.parse(text).toString()); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode exportUntisFachwahlenGPU015 für den Zugriff auf die URL https://{hostname}/db/{schema}/datenaustausch/untis/export/fachwahlen/{id : \d+}/{sidvariante : \d+}
	 *
	 * Liefert einen Export für die Fachwahlen eines Schuljahresabschnittes (GPU015.txt).Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Exportieren besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die GPU015.txt
	 *     - Mime-Type: application/octet-stream
	 *     - Rückgabe-Typ: ApiFile
	 *   Code 404: Es wurden nicht alle benötigten Daten für den Export gefunden.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 500: Es ist ein unerwarteter Fehler aufgetreten.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {string | null} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 * @param {number} sidvariante - der Pfad-Parameter sidvariante
	 *
	 * @returns Die GPU015.txt
	 */
	public async exportUntisFachwahlenGPU015(data : string | null, schema : string, id : number, sidvariante : number) : Promise<ApiFile> {
		const path = "/db/{schema}/datenaustausch/untis/export/fachwahlen/{id : \\d+}/{sidvariante : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString())
			.replace(/{sidvariante\s*(:[^{}]+({[^{}]+})*)?}/g, sidvariante.toString());
		const body : string = JSON.stringify(data);
		const result : ApiFile = await super.postJSONtoOctetStream(path, body);
		return result;
	}


	/**
	 * Implementierung der POST-Methode exportUntisFaecherGPU006 für den Zugriff auf die URL https://{hostname}/db/{schema}/datenaustausch/untis/export/faecher/{id : \d+}
	 *
	 * Liefert einen Export für die Fächer- bzw. Kursdaten eines Schuljahresabschnittes (GPU006.txt).Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Exportieren besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die GPU006.txt
	 *     - Mime-Type: application/octet-stream
	 *     - Rückgabe-Typ: ApiFile
	 *   Code 404: Es wurden nicht alle benötigten Daten für den Export gefunden.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 500: Es ist ein unerwarteter Fehler aufgetreten.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die GPU006.txt
	 */
	public async exportUntisFaecherGPU006(schema : string, id : number) : Promise<ApiFile> {
		const path = "/db/{schema}/datenaustausch/untis/export/faecher/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : ApiFile = await super.postJSONtoOctetStream(path, null);
		return result;
	}


	/**
	 * Implementierung der POST-Methode exportUntisKlassenGPU003 für den Zugriff auf die URL https://{hostname}/db/{schema}/datenaustausch/untis/export/klassen/{id : \d+}
	 *
	 * Liefert einen Export für die Klassen eines Schuljahresabschnittes (GPU003.txt).Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Exportieren besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die GPU003.txt
	 *     - Mime-Type: application/octet-stream
	 *     - Rückgabe-Typ: ApiFile
	 *   Code 404: Es wurden nicht alle benötigten Daten für den Export gefunden.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 500: Es ist ein unerwarteter Fehler aufgetreten.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die GPU003.txt
	 */
	public async exportUntisKlassenGPU003(schema : string, id : number) : Promise<ApiFile> {
		const path = "/db/{schema}/datenaustausch/untis/export/klassen/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : ApiFile = await super.postJSONtoOctetStream(path, null);
		return result;
	}


	/**
	 * Implementierung der POST-Methode exportUntisKlausurenGPU017 für den Zugriff auf die URL https://{hostname}/db/{schema}/datenaustausch/untis/export/klausuren/{id : \d+}/{sidvariante : \d+}
	 *
	 * Liefert einen Export für die Klausurdaten eines Schuljahresabschnittes (GPU017.txt).Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Exportieren besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die GPU017.txt
	 *     - Mime-Type: application/octet-stream
	 *     - Rückgabe-Typ: ApiFile
	 *   Code 404: Es wurden nicht alle benötigten Daten für den Export gefunden.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 500: Es ist ein unerwarteter Fehler aufgetreten.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {string | null} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 * @param {number} sidvariante - der Pfad-Parameter sidvariante
	 *
	 * @returns Die GPU017.txt
	 */
	public async exportUntisKlausurenGPU017(data : string | null, schema : string, id : number, sidvariante : number) : Promise<ApiFile> {
		const path = "/db/{schema}/datenaustausch/untis/export/klausuren/{id : \\d+}/{sidvariante : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString())
			.replace(/{sidvariante\s*(:[^{}]+({[^{}]+})*)?}/g, sidvariante.toString());
		const body : string = JSON.stringify(data);
		const result : ApiFile = await super.postJSONtoOctetStream(path, body);
		return result;
	}


	/**
	 * Implementierung der POST-Methode exportUntisLehrerGPU004 für den Zugriff auf die URL https://{hostname}/db/{schema}/datenaustausch/untis/export/lehrer/{id : \d+}
	 *
	 * Liefert einen Export für die Lehrkräfte eines Schuljahresabschnittes (GPU004.txt).Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Exportieren besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die GPU004.txt
	 *     - Mime-Type: application/octet-stream
	 *     - Rückgabe-Typ: ApiFile
	 *   Code 404: Es wurden nicht alle benötigten Daten für den Export gefunden.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 500: Es ist ein unerwarteter Fehler aufgetreten.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die GPU004.txt
	 */
	public async exportUntisLehrerGPU004(schema : string, id : number) : Promise<ApiFile> {
		const path = "/db/{schema}/datenaustausch/untis/export/lehrer/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : ApiFile = await super.postJSONtoOctetStream(path, null);
		return result;
	}


	/**
	 * Implementierung der POST-Methode exportUntisSchienenGPU019 für den Zugriff auf die URL https://{hostname}/db/{schema}/datenaustausch/untis/export/schienen/{id : \d+}
	 *
	 * Liefert einen Export für die Schienenzuordnungen eines Schuljahresabschnittes (GPU019.txt).Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Exportieren besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die GPU019.txt
	 *     - Mime-Type: application/octet-stream
	 *     - Rückgabe-Typ: ApiFile
	 *   Code 404: Es wurden nicht alle benötigten Daten für den Export gefunden.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 500: Es ist ein unerwarteter Fehler aufgetreten.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {string | null} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die GPU019.txt
	 */
	public async exportUntisSchienenGPU019(data : string | null, schema : string, id : number) : Promise<ApiFile> {
		const path = "/db/{schema}/datenaustausch/untis/export/schienen/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = JSON.stringify(data);
		const result : ApiFile = await super.postJSONtoOctetStream(path, body);
		return result;
	}


	/**
	 * Implementierung der POST-Methode exportUntisSchuelerGPU010 für den Zugriff auf die URL https://{hostname}/db/{schema}/datenaustausch/untis/export/schueler/{id : \d+}/{sidvariante : \d+}
	 *
	 * Liefert einen Export für die Schüler eines Schuljahresabschnittes (GPU010.txt).Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Exportieren besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die GPU010.txt
	 *     - Mime-Type: application/octet-stream
	 *     - Rückgabe-Typ: ApiFile
	 *   Code 404: Es wurden nicht alle benötigten Daten für den Export gefunden.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 500: Es ist ein unerwarteter Fehler aufgetreten.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 * @param {number} sidvariante - der Pfad-Parameter sidvariante
	 *
	 * @returns Die GPU010.txt
	 */
	public async exportUntisSchuelerGPU010(schema : string, id : number, sidvariante : number) : Promise<ApiFile> {
		const path = "/db/{schema}/datenaustausch/untis/export/schueler/{id : \\d+}/{sidvariante : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString())
			.replace(/{sidvariante\s*(:[^{}]+({[^{}]+})*)?}/g, sidvariante.toString());
		const result : ApiFile = await super.postJSONtoOctetStream(path, null);
		return result;
	}


	/**
	 * Implementierung der POST-Methode importStundenplanUntisGPU001 für den Zugriff auf die URL https://{hostname}/db/{schema}/datenaustausch/untis/import/gpu001/{ingore_missing:[01]}
	 *
	 * Importiert den Untis-Stundenplan aus der übergebenen GPU001.txt in das Schema mit dem angegebenen Namen.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Import des Untis-Stundenplans
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Der Benutzer hat keine Berechtigung, um den Untis-Stundenplan zu importieren.
	 *   Code 404: Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 409: Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 500: Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {FormData} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} ingore_missing - der Pfad-Parameter ingore_missing
	 *
	 * @returns Der Log vom Import des Untis-Stundenplans
	 */
	public async importStundenplanUntisGPU001(data : FormData, schema : string, ingore_missing : number) : Promise<SimpleOperationResponse> {
		const path = "/db/{schema}/datenaustausch/untis/import/gpu001/{ingore_missing:[01]}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{ingore_missing\s*(:[^{}]+({[^{}]+})*)?}/g, ingore_missing.toString());
		const result : string = await super.postMultipart(path, data);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode importUntisRaeumeGPU005 für den Zugriff auf die URL https://{hostname}/db/{schema}/datenaustausch/untis/import/gpu005
	 *
	 * Importiert die Räume aus der Untis-Datei GPU005.txt in das Schema mit dem angegebenen Namen.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Log vom Import der Räume aus der Untis-Datei GPU005.txt
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 400: Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Der Benutzer hat keine Berechtigung, um die Räume aus der Untis-Datei GPU005.txt zu importieren.
	 *   Code 404: Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 409: Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 500: Es ist ein Fehler beim Import aufgetreten. Ein Log vom Import wird zurückgegeben.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {FormData} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Log vom Import der Räume aus der Untis-Datei GPU005.txt
	 */
	public async importUntisRaeumeGPU005(data : FormData, schema : string) : Promise<SimpleOperationResponse> {
		const path = "/db/{schema}/datenaustausch/untis/import/gpu005"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.postMultipart(path, data);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getSMTPServerKonfiguration für den Zugriff auf die URL https://{hostname}/db/{schema}/email/smtp/server/konfiguration
	 *
	 * Gibt die SMTP-Server-Konfiguration der Schule zurück.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die SMTP-Server-Konfiguration
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SMTPServerKonfiguration
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Schuldaten zu anzusehen.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die SMTP-Server-Konfiguration
	 */
	public async getSMTPServerKonfiguration(schema : string) : Promise<SMTPServerKonfiguration> {
		const path = "/db/{schema}/email/smtp/server/konfiguration"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const text = result;
		return SMTPServerKonfiguration.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchSMTPServerKonfiguration für den Zugriff auf die URL https://{hostname}/db/{schema}/email/smtp/server/konfiguration
	 *
	 * Passt die SMTP-Server-Konfiguration an und speichert das Ergebnis in der Datenbank. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Schuldaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Der Patch wurde erfolgreich in die SMTP-Server-Konfiguration integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Schuldaten zu ändern.
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<SMTPServerKonfiguration>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 */
	public async patchSMTPServerKonfiguration(data : Partial<SMTPServerKonfiguration>, schema : string) : Promise<void> {
		const path = "/db/{schema}/email/smtp/server/konfiguration"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = SMTPServerKonfiguration.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const data : ApiFile = await super.getOctetStream(path);
		return data;
	}


	/**
	 * Implementierung der GET-Methode getENMLehrerInitialKennwoerter für den Zugriff auf die URL https://{hostname}/db/{schema}/enm/alle/initial_kennwoerter
	 *
	 * Liefert eine Liste der Lehrer-IDs mit den zugehörigen Initialkennwörtern für Lehrer zurück, welche bei den Daten für das Externe Datenmodul (ENM) vorkommen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zur Administration der Notenmodul-Daten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Liste mit den Initialkennwörtern
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<ENMLehrerInitialKennwort>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Initialkennwörter des ENM zu verwalten.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Liste mit den Initialkennwörtern
	 */
	public async getENMLehrerInitialKennwoerter(schema : string) : Promise<List<ENMLehrerInitialKennwort>> {
		const path = "/db/{schema}/enm/alle/initial_kennwoerter"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<ENMLehrerInitialKennwort>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(ENMLehrerInitialKennwort.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode checkENMServer für den Zugriff auf die URL https://{hostname}/db/{schema}/enm/check
	 *
	 * Prüft, ob der ENM-Server mit den hinterlegten Verbindungsdaten erreichbar ist.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Notendaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der ENM-Server ist erreichbar.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 401: Die Authorisierung beim ENM-Server ist fehlgeschlagen.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Operation auszuführen.
	 *   Code 404: Keine ENM-Serverdaten gefunden.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 500: Interner Serverfehler
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 502: Fehler bei der Verbindung zum ENM-Server, u.U. auch fehlende OAuth-Daten.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der ENM-Server ist erreichbar.
	 */
	public async checkENMServer(schema : string) : Promise<SimpleOperationResponse> {
		const path = "/db/{schema}/enm/check"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getENMServerConfig für den Zugriff auf die URL https://{hostname}/db/{schema}/enm/config
	 *
	 * Ein Getter für die ENM-Server-Konfiguration.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Konfiguration konnte erfolgreich abgerufen werden.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: ENMConfigResponse
	 *   Code 401: Die Authorisierung beim ENM-Server ist fehlgeschlagen.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: ENMConfigResponse
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Operation auszuführen.
	 *   Code 404: Keine ENM-Serverdaten gefunden.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: ENMConfigResponse
	 *   Code 500: Interner Serverfehler
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: ENMConfigResponse
	 *   Code 502: Fehler bei der Verbindung zum ENM-Server, u.U. auch fehlende OAuth-Daten.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: ENMConfigResponse
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Konfiguration konnte erfolgreich abgerufen werden.
	 */
	public async getENMServerConfig(schema : string) : Promise<ENMConfigResponse> {
		const path = "/db/{schema}/enm/config"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const text = result;
		return ENMConfigResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode setENMServerConfigElement für den Zugriff auf die URL https://{hostname}/db/{schema}/enm/config
	 *
	 * Schreibt den Konfigurationseintrag für den angebenen Schlüsselwert in die Konfiguration.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Der Konfigurationseintrag wurde erfolgreich geschrieben
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 401: Die Authorisierung beim ENM-Server ist fehlgeschlagen.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Operation auszuführen.
	 *   Code 404: Keine ENM-Serverdaten gefunden.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 500: Interner Serverfehler
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 502: Fehler bei der Verbindung zum ENM-Server, u.U. auch fehlende OAuth-Daten.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {ENMServerConfigElement} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Konfigurationseintrag wurde erfolgreich geschrieben
	 */
	public async setENMServerConfigElement(data : ENMServerConfigElement, schema : string) : Promise<SimpleOperationResponse> {
		const path = "/db/{schema}/enm/config"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = ENMServerConfigElement.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode generateENMLehrerCredentials für den Zugriff auf die URL https://{hostname}/db/{schema}/enm/credentials/generate
	 *
	 * Generiert Initial-Kennwörter für Lehrer für das externe Notenmodul, sofern diese noch keine haben.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die Kennwörter und die Password-Hashes wurden erzeugt oder es war nichts zu tun.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte zum Erzeugen der Credentials.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 */
	public async generateENMLehrerCredentials(schema : string) : Promise<void> {
		const path = "/db/{schema}/enm/credentials/generate"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		await super.postJSON(path, null);
		return;
	}


	/**
	 * Implementierung der POST-Methode resetENMLehrerPasswordToInitial für den Zugriff auf die URL https://{hostname}/db/{schema}/enm/credentials/reset/{id : \d+}
	 *
	 * Setzt das Kennwort des Lehrers für das externe Notenmodul auf das Initial-Kennwort zurück. Ist noch kein Initialkennwort gesetzt, so wird ein neues erzeugt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Das Initial-Kennwort wurde gesetzt.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte zum Setzen des Kennwortes.
	 *   Code 404: Die ID des Lehrers ist in der DB nicht vorhanden.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async resetENMLehrerPasswordToInitial(schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/enm/credentials/reset/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		await super.postJSON(path, null);
		return;
	}


	/**
	 * Implementierung der POST-Methode setENMLehrerPassword für den Zugriff auf die URL https://{hostname}/db/{schema}/enm/credentials/set/{id : \d+}
	 *
	 * Setzt das Kennwort des Lehrers für das externe Notenmodul auf das übergebene Kennwort. Ist noch kein Initialkennwort gesetzt, so wird ein neues erzeugt, allerdings das übergebene Kennwort gesetzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Das Kennwort wurde gesetzt.
	 *   Code 400: Das Kennwort ist leer oder entspricht nicht den Minimal-Anforderungen.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte zum Setzen des Kennwortes.
	 *   Code 404: Die ID des Lehrers ist in der DB nicht vorhanden.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string | null} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async setENMLehrerPassword(data : string | null, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/enm/credentials/set/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = JSON.stringify(data);
		await super.postJSON(path, body);
		return;
	}


	/**
	 * Implementierung der GET-Methode downloadENMDaten für den Zugriff auf die URL https://{hostname}/db/{schema}/enm/download
	 *
	 * Importiert die Daten des Externen Notenmoduls und speichert diese in der Datenbank. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Notendaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Daten des Externen Notenmoduls (ENM) wurden heruntergeladen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 401: Die Authorisierung beim ENM-Server ist fehlgeschlagen.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten des ENM auszulesen.
	 *   Code 404: Keine ENM-Serverdaten gefunden.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 500: Interner Serverfehler
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 502: Fehler bei der Verbindung zum ENM-Server
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Daten des Externen Notenmoduls (ENM) wurden heruntergeladen
	 */
	public async downloadENMDaten(schema : string) : Promise<SimpleOperationResponse> {
		const path = "/db/{schema}/enm/download"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getENMDatenLeer für den Zugriff auf die URL https://{hostname}/db/{schema}/enm/empty
	 *
	 * Liefert leere Daten des Externen Notenmoduls (ENM).
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Daten des Externen Notenmoduls (ENM)
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: ENMDaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um auf die API zuzugreifen.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Daten des Externen Notenmoduls (ENM)
	 */
	public async getENMDatenLeer(schema : string) : Promise<ENMDaten> {
		const path = "/db/{schema}/enm/empty"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const text = result;
		return ENMDaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode importENMDaten für den Zugriff auf die URL https://{hostname}/db/{schema}/enm/import
	 *
	 * Importiert die übergebenen ENM-Daten. Dabei wird die Aktualität der zu importierenden Daten anhand der Zeitstempel in den ENM-Daten geprüft.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die ENM-Daten wurden erfolgreich importiert.
	 *   Code 400: Die ENM-Daten sind nicht korrekt.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte zum importieren.
	 *   Code 404: Es wurden nicht alle benötigten Daten für den Abgleich in der DB gefunden.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {ENMDaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 */
	public async importENMDaten(data : ENMDaten, schema : string) : Promise<void> {
		const path = "/db/{schema}/enm/import"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = ENMDaten.transpilerToJSON(data);
		await super.postJSON(path, body);
		return;
	}


	/**
	 * Implementierung der POST-Methode importENMDatenGZip für den Zugriff auf die URL https://{hostname}/db/{schema}/enm/import/gzip
	 *
	 * Importiert die übergebenen ENM-Daten. Dabei wird die Aktualität der zu importierenden Daten anhand der Zeitstempel in den ENM-Daten geprüft.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die ENM-Daten wurden erfolgreich importiert.
	 *   Code 400: Die ENM-Daten sind nicht korrekt.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte zum importieren.
	 *   Code 404: Es wurden nicht alle benötigten Daten für den Abgleich in der DB gefunden.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {FormData} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 */
	public async importENMDatenGZip(data : FormData, schema : string) : Promise<void> {
		const path = "/db/{schema}/enm/import/gzip"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		await super.postMultipart(path, data);
		return;
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return ENMDaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode resetENMServer für den Zugriff auf die URL https://{hostname}/db/{schema}/enm/reset
	 *
	 * Leert die Daten des Externen Notenmoduls (ENM).Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Notendaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Daten des Externen Notenmoduls (ENM) wurden geleert.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 401: Die Authorisierung beim ENM-Server ist fehlgeschlagen.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten des ENM auszulesen.
	 *   Code 404: Keine ENM-Serverdaten gefunden.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 500: Interner Serverfehler
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 502: Fehler bei der Verbindung zum ENM-Server, u.U. auch fehlende OAuth-Daten.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Daten des Externen Notenmoduls (ENM) wurden geleert.
	 */
	public async resetENMServer(schema : string) : Promise<SimpleOperationResponse> {
		const path = "/db/{schema}/enm/reset"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode setupENMServer für den Zugriff auf die URL https://{hostname}/db/{schema}/enm/setup
	 *
	 * Dieser Aufruf initialisert den ENM-Server beim ersten Aufruf. Weitere Aufrufe führen zu einem Fehler.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Stand des Setups, true, wurde initialisiert, false ist bereits initialisiert
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Boolean
	 *   Code 401: Die Authorisierung beim ENM-Server ist fehlgeschlagen.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Operation auszuführen.
	 *   Code 404: Keine ENM-Serverdaten gefunden.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 409: Der TLS-Zertifikat des ENM-Server wird nicht vertraut.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 500: Interner Serverfehler
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 502: Fehler bei der Verbindung zum ENM-Server, u.U. auch fehlende OAuth-Daten.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Stand des Setups, true, wurde initialisiert, false ist bereits initialisiert
	 */
	public async setupENMServer(schema : string) : Promise<boolean | null> {
		const path = "/db/{schema}/enm/setup"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const text = result;
		return (text === "true");
	}


	/**
	 * Implementierung der GET-Methode synchronizeENMDaten für den Zugriff auf die URL https://{hostname}/db/{schema}/enm/synchronize
	 *
	 * Liest die Daten des Externen Notenmoduls (ENM) aller Lehrer aus der Datenbank und lädt diese als ZIP beim ENM hoch, lädt danach die Daten des ENM runter und speichert diese in der Datenbank. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Notendaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Daten des Externen Notenmoduls (ENM) wurden synchronisiert
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 401: Die Authorisierung beim ENM-Server ist fehlgeschlagen.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten des ENM auszulesen.
	 *   Code 404: Keine ENM-Serverdaten gefunden.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 500: Interner Serverfehler
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 502: Fehler bei der Verbindung zum ENM-Server
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Daten des Externen Notenmoduls (ENM) wurden synchronisiert
	 */
	public async synchronizeENMDaten(schema : string) : Promise<SimpleOperationResponse> {
		const path = "/db/{schema}/enm/synchronize"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode truncateENMServer für den Zugriff auf die URL https://{hostname}/db/{schema}/enm/truncate
	 *
	 * Leert die Daten des Externen Notenmoduls (ENM), einschließlich der Benutzerdaten.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Notendaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Daten des Externen Notenmoduls (ENM) wurden geleert.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 401: Die Authorisierung beim ENM-Server ist fehlgeschlagen.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten des ENM auszulesen.
	 *   Code 404: Keine ENM-Serverdaten gefunden.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 500: Interner Serverfehler
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 502: Fehler bei der Verbindung zum ENM-Server, u.U. auch fehlende OAuth-Daten.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Daten des Externen Notenmoduls (ENM) wurden geleert.
	 */
	public async truncateENMServer(schema : string) : Promise<SimpleOperationResponse> {
		const path = "/db/{schema}/enm/truncate"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode uploadENMDaten für den Zugriff auf die URL https://{hostname}/db/{schema}/enm/upload
	 *
	 * Liest die Daten des Externen Notenmoduls (ENM) aller Lehrer aus der Datenbank und lädt diese als ZIP beim ENM hoch.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Notendaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Daten des Externen Notenmoduls (ENM) wurden hochgeladen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 401: Die Authorisierung beim ENM-Server ist fehlgeschlagen.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten des ENM auszulesen.
	 *   Code 404: Keine ENM-Serverdaten gefunden.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 500: Interner Serverfehler
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 502: Fehler bei der Verbindung zum ENM-Server
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Daten des Externen Notenmoduls (ENM) wurden hochgeladen
	 */
	public async uploadENMDaten(schema : string) : Promise<SimpleOperationResponse> {
		const path = "/db/{schema}/enm/upload"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getEntlassgruende für den Zugriff auf die URL https://{hostname}/db/{schema}/entlassgruende
	 *
	 * Gibt die Entlassgründe zurück, insofern der SVWS-Benutzer die erforderliche Berechtigung besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste der Entlassgründe.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<KatalogEntlassgrund>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste der Entlassgründe.
	 */
	public async getEntlassgruende(schema : string) : Promise<List<KatalogEntlassgrund>> {
		const path = "/db/{schema}/entlassgruende"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KatalogEntlassgrund>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KatalogEntlassgrund.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der PATCH-Methode patchEntlassgrund für den Zugriff auf die URL https://{hostname}/db/{schema}/entlassgruende/{id : \d+}
	 *
	 * Patched den Entlassgrund mit der angegebenen ID, insofern die notwendigen Berechtigungen vorliegen.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 404: Kein Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z. B. beim Datenbankzugriff)
	 *
	 * @param {Partial<KatalogEntlassgrund>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchEntlassgrund(data : Partial<KatalogEntlassgrund>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/entlassgruende/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = KatalogEntlassgrund.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der POST-Methode addEntlassgrund für den Zugriff auf die URL https://{hostname}/db/{schema}/entlassgruende/create
	 *
	 * Erstellt einen neuen Entlassgrund, insofern die notwendigen Berechtigungen vorliegen
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Der Entlassgrund wurde erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: KatalogEntlassgrund
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Entlassgründe anzulegen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<KatalogEntlassgrund>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Entlassgrund wurde erfolgreich hinzugefügt.
	 */
	public async addEntlassgrund(data : Partial<KatalogEntlassgrund>, schema : string) : Promise<KatalogEntlassgrund> {
		const path = "/db/{schema}/entlassgruende/create"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = KatalogEntlassgrund.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return KatalogEntlassgrund.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der DELETE-Methode deleteEntlassgruende für den Zugriff auf die URL https://{hostname}/db/{schema}/entlassgruende/delete/multiple
	 *
	 * Entfernt mehrere Entlassgründe, insofern die notwendigen Berechtigungen vorhanden sind.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Lösch-Operationen wurden ausgeführt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SimpleOperationResponse>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Entlassgründe zu entfernen.
	 *   Code 404: Entlassgründe nicht vorhanden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Lösch-Operationen wurden ausgeführt.
	 */
	public async deleteEntlassgruende(data : List<number>, schema : string) : Promise<List<SimpleOperationResponse>> {
		const path = "/db/{schema}/entlassgruende/delete/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SimpleOperationResponse>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SimpleOperationResponse.transpilerFromJSON(text)); });
		return ret;
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = ErzieherStammdaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
	 *     - Rückgabe-Typ: FachgruppeKatalogEintrag
	 *   Code 403: Der SVWS-Benutzer hat keine gültige Anmeldung.
	 *   Code 404: Kein Fachgruppen-Katalog-Eintrag für die angegebene ID gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Fachgruppen-Katalog-Eintrag für die angegebene ID.
	 */
	public async getKatalogFachgruppenEintrag(schema : string, id : number) : Promise<FachgruppeKatalogEintrag> {
		const path = "/db/{schema}/faecher/allgemein/fachgruppe/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return FachgruppeKatalogEintrag.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getKatalogFachgruppen für den Zugriff auf die URL https://{hostname}/db/{schema}/faecher/allgemein/fachgruppen
	 *
	 * Gibt den Katalog aller Fachgruppen aller Schulformen zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Katalog aller Fachgruppen aller Schulformen.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<FachgruppeKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine gültige Anmeldung.
	 *   Code 404: Keine Fachgruppen gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Katalog aller Fachgruppen aller Schulformen.
	 */
	public async getKatalogFachgruppen(schema : string) : Promise<List<FachgruppeKatalogEintrag>> {
		const path = "/db/{schema}/faecher/allgemein/fachgruppen"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<FachgruppeKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(FachgruppeKatalogEintrag.transpilerFromJSON(text)); });
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
	 *
	 * @returns Das Fach wurde erfolgreich hinzugefügt.
	 */
	public async addFach(data : Partial<FachDaten>, schema : string) : Promise<FachDaten> {
		const path = "/db/{schema}/faecher/create"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = FachDaten.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return FachDaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der DELETE-Methode deleteFaecher für den Zugriff auf die URL https://{hostname}/db/{schema}/faecher/delete/multiple
	 *
	 * Entfernt mehrere Fächer. Dabei wird geprüft, ob alle Vorbedingungen zum Entfernender Fächer erfüllt sind und der SVWS-Benutzer die notwendige Berechtigung hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Lösch-Operationen wurden ausgeführt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SimpleOperationResponse>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Fächer zu entfernen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Lösch-Operationen wurden ausgeführt.
	 */
	public async deleteFaecher(data : List<number>, schema : string) : Promise<List<SimpleOperationResponse>> {
		const path = "/db/{schema}/faecher/delete/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SimpleOperationResponse>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SimpleOperationResponse.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getFachgruppen für den Zugriff auf die URL https://{hostname}/db/{schema}/faecher/fachgruppen
	 *
	 * Gibt den Katalog der Fachgruppen für die Schulform dieser Schule zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Katalog der Fachgruppen für die Schulform dieser Schule.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<FachgruppeKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine gültige Anmeldung.
	 *   Code 404: Keine Fachgruppen für die Schulform dieser Schule gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Katalog der Fachgruppen für die Schulform dieser Schule.
	 */
	public async getFachgruppen(schema : string) : Promise<List<FachgruppeKatalogEintrag>> {
		const path = "/db/{schema}/faecher/fachgruppen"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<FachgruppeKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(FachgruppeKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode setFaecherSortierungSekII für den Zugriff auf die URL https://{hostname}/db/{schema}/faecher/sortierung/setSekII
	 *
	 * Setzte eine Sortierung für die Fächer auf eine Standard-Sortierung für die Sekundarstufe II.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anpassen von Fächerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die Sortierung wurde erfolgreich gesetzt.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Fächerdaten anzupassen.
	 *   Code 404: Keine Fächer-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 */
	public async setFaecherSortierungSekII(schema : string) : Promise<void> {
		const path = "/db/{schema}/faecher/sortierung/setSekII"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		await super.postJSON(path, null);
		return;
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString())
			.replace(/{abschnittID\s*(:[^{}]+({[^{}]+})*)?}/g, abschnittID.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<GostJahrgang>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(GostJahrgang.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getGostAbiturjahrgaengeFuerAbschnitt für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgaenge/{idAbschnitt : \d+}
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
	 * @param {number} idAbschnitt - der Pfad-Parameter idAbschnitt
	 *
	 * @returns Die Liste der Abiturjahrgänge.
	 */
	public async getGostAbiturjahrgaengeFuerAbschnitt(schema : string, idAbschnitt : number) : Promise<List<GostJahrgang>> {
		const path = "/db/{schema}/gost/abiturjahrgaenge/{idAbschnitt : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{idAbschnitt\s*(:[^{}]+({[^{}]+})*)?}/g, idAbschnitt.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString());
		const body : string = GostJahrgangsdaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getGostAbiturjahrgangAbiturdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\d+}/abiturdaten
	 *
	 * Liefert zu dem Abiturjahrgang die zugehörigen Abiturdaten aus den Abiturtabellen und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Abiturdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Abiturdaten des Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Abiturdaten>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Abiturdaten anzusehen.
	 *   Code 404: Kein Abiturjahrgang gefunden
	 *     - Mime-Type: text/plain
	 *     - Rückgabe-Typ: String
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 *
	 * @returns Die Abiturdaten des Schülers
	 */
	public async getGostAbiturjahrgangAbiturdaten(schema : string, abiturjahr : number) : Promise<List<Abiturdaten>> {
		const path = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\\d+}/abiturdaten"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<Abiturdaten>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(Abiturdaten.transpilerFromJSON(text)); });
		return ret;
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString())
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString())
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString())
			.replace(/{typ\s*(:[^{}]+({[^{}]+})*)?}/g, typ.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString())
			.replace(/{fachid\s*(:[^{}]+({[^{}]+})*)?}/g, fachid.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString())
			.replace(/{fachid\s*(:[^{}]+({[^{}]+})*)?}/g, fachid.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString())
			.replace(/{halbjahr\s*(:[^{}]+({[^{}]+})*)?}/g, halbjahr.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostJahrgangFachwahlenHalbjahr.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getGostAbiturjahrgangLaufbahndaten für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\d+}/laufbahndaten
	 *
	 * Liefert zu dem Abiturjahrgang die zugehörigen Abiturdaten aus den Laufbahndaten und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der Laufbahndaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Laufbahndaten des Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Abiturdaten>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Laufbahndaten anzusehen.
	 *   Code 404: Kein Abiturjahrgang gefunden
	 *     - Mime-Type: text/plain
	 *     - Rückgabe-Typ: String
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 *
	 * @returns Die Laufbahndaten des Schülers
	 */
	public async getGostAbiturjahrgangLaufbahndaten(schema : string, abiturjahr : number) : Promise<List<Abiturdaten>> {
		const path = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : -?\\d+}/laufbahndaten"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<Abiturdaten>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(Abiturdaten.transpilerFromJSON(text)); });
		return ret;
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchuelerListeEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchuelerListeEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der DELETE-Methode deleteGostAbiturjahrgang für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : \d+}
	 *
	 * Entfernt den Abiturjahrgang, sofern er nicht abgeschlossen ist oder bereits Leistungsdaten bei Schülern eingetragen sind. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Der Abiturjahrgang wurde wurde erfolgreich entfernt.
	 *   Code 400: Es wurde versucht einen Abiturjahrgang, der abgeschlossen ist oder für den bereits Leistungsdaten bei Schülern eingetragen sind, zu entfernen.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Abiturjahrgang zu entfernen.
	 *   Code 404: Der Abiturjahrgang wurde nicht gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 */
	public async deleteGostAbiturjahrgang(schema : string, abiturjahr : number) : Promise<void> {
		const path = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString());
		await super.deleteJSON(path, null);
		return;
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString())
			.replace(/{halbjahr\s*(:[^{}]+({[^{}]+})*)?}/g, halbjahr.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<GostBlockungListeneintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(GostBlockungListeneintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode createGostAbiturjahrgangBlockung für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/{abiturjahr : \d+}/{halbjahr : \d+}/blockungen/new
	 *
	 * Erstellt eine neue Blockung und gibt den Listeneintrag für die Blockung zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen der Blockungen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Blockung wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBlockungListeneintrag
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
	public async createGostAbiturjahrgangBlockung(schema : string, abiturjahr : number, halbjahr : number) : Promise<GostBlockungListeneintrag> {
		const path = "/db/{schema}/gost/abiturjahrgang/{abiturjahr : \\d+}/{halbjahr : \\d+}/blockungen/new"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString())
			.replace(/{halbjahr\s*(:[^{}]+({[^{}]+})*)?}/g, halbjahr.toString());
		const result : string = await super.postJSON(path, null);
		const text = result;
		return GostBlockungListeneintrag.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode createGostAbiturjahrgang für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/abiturjahrgang/new/{schuljahresabschnittsid}/{jahrgangid}
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
	 * @param {number} schuljahresabschnittsid - der Pfad-Parameter schuljahresabschnittsid
	 * @param {number} jahrgangid - der Pfad-Parameter jahrgangid
	 *
	 * @returns Der Abiturjahrgang wurde erfolgreich angelegt.
	 */
	public async createGostAbiturjahrgang(schema : string, schuljahresabschnittsid : number, jahrgangid : number) : Promise<number | null> {
		const path = "/db/{schema}/gost/abiturjahrgang/new/{schuljahresabschnittsid}/{jahrgangid}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{schuljahresabschnittsid\s*(:[^{}]+({[^{}]+})*)?}/g, schuljahresabschnittsid.toString())
			.replace(/{jahrgangid\s*(:[^{}]+({[^{}]+})*)?}/g, jahrgangid.toString());
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
	 *   Code 200: Der Listeneintrag zu der restaurierten Blockung
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBlockungListeneintrag
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Blockungsdaten der Gymnasialen Oberstufe zu restaurieren.
	 *   Code 404: Keine Daten für das Abiturjahr und das Halbjahr gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 * @param {number} halbjahr - der Pfad-Parameter halbjahr
	 *
	 * @returns Der Listeneintrag zu der restaurierten Blockung
	 */
	public async restauriereGostBlockung(schema : string, abiturjahr : number, halbjahr : number) : Promise<GostBlockungListeneintrag> {
		const path = "/db/{schema}/gost/blockungen/{abiturjahr : \\d+}/{halbjahr : \\d+}/restore"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString())
			.replace(/{halbjahr\s*(:[^{}]+({[^{}]+})*)?}/g, halbjahr.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostBlockungListeneintrag.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode revertActivateGostBlockungsergebnis für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/{abiturjahr : \d+}/{halbjahr : \d+}/revertactivate
	 *
	 * Macht das Aktivieren bzw. persistieren in den Leistungsdaten rückgängig. Dies ist nur erlaubt, wenn Kurse der gymnasialen Oberstufe vorhanden sind und bei den Leistungsdaten der Schüler des Abiturjahrgangs in dem Halbjahr der gymnasialen Oberstufe noch keine Noteneinträge für eine Quartalsnode oder Halbjahresnote vorliegen.Es wird auch geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die Blockungsdaten wurden bei den Kursen und Leistungsdaten erfolgreich gelöscht.
	 *   Code 400: Es existieren Schüler mit Noten in den Leistungsdaten.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Persistierung rückgängig zu machen.
	 *   Code 404: Es wurden keine Kurse der gymnasialen Oberstufe gefunden oder der Abiturjahrgang oder die ID des Halbjahres sind fehlerhaft.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 * @param {number} halbjahr - der Pfad-Parameter halbjahr
	 */
	public async revertActivateGostBlockungsergebnis(schema : string, abiturjahr : number, halbjahr : number) : Promise<void> {
		const path = "/db/{schema}/gost/blockungen/{abiturjahr : \\d+}/{halbjahr : \\d+}/revertactivate"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString())
			.replace(/{halbjahr\s*(:[^{}]+({[^{}]+})*)?}/g, halbjahr.toString());
		await super.postJSON(path, null);
		return;
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{blockungsid\s*(:[^{}]+({[^{}]+})*)?}/g, blockungsid.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{blockungsid\s*(:[^{}]+({[^{}]+})*)?}/g, blockungsid.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostBlockungsdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchGostBlockung für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/{blockungsid : \d+}
	 *
	 * Passt die Blockungsdaten der Gymnasiale Oberstufe mit der angegebenen ID an.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anpassen der Blockungsdaten besitzt.
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{blockungsid\s*(:[^{}]+({[^{}]+})*)?}/g, blockungsid.toString());
		const body : string = GostBlockungsdaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der POST-Methode addGostBlockungErgebnisse für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/{blockungsid : \d+}/addergebnisse
	 *
	 * Fügt mehrere Ergebnisse zu einer Blockung der Gymnasialen Oberstufe hinzu.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen von Ergebnissen hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Ergebnisse wurden erfolgreich der Blockung hinzugefügt
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<GostBlockungsergebnis>
	 *   Code 400: Die Daten sind nicht konsistent (z.B. bei einer nicht passenden Blockungs-ID in der Ergebnissen).
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Ergebnisse hinzuzufügen.
	 *   Code 404: Keine Blockung vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<GostBlockungsergebnis>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} blockungsid - der Pfad-Parameter blockungsid
	 *
	 * @returns Die Ergebnisse wurden erfolgreich der Blockung hinzugefügt
	 */
	public async addGostBlockungErgebnisse(data : List<GostBlockungsergebnis>, schema : string, blockungsid : number) : Promise<List<GostBlockungsergebnis>> {
		const path = "/db/{schema}/gost/blockungen/{blockungsid : \\d+}/addergebnisse"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{blockungsid\s*(:[^{}]+({[^{}]+})*)?}/g, blockungsid.toString());
		const body : string = "[" + (data.toArray() as Array<GostBlockungsergebnis>).map(d => GostBlockungsergebnis.transpilerToJSON(d)).join() + "]";
		const result : string = await super.postJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<GostBlockungsergebnis>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(GostBlockungsergebnis.transpilerFromJSON(text)); });
		return ret;
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{blockungsid\s*(:[^{}]+({[^{}]+})*)?}/g, blockungsid.toString())
			.replace(/{regeltyp\s*(:[^{}]+({[^{}]+})*)?}/g, regeltyp.toString());
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{blockungsid\s*(:[^{}]+({[^{}]+})*)?}/g, blockungsid.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{blockungsid\s*(:[^{}]+({[^{}]+})*)?}/g, blockungsid.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{blockungsid\s*(:[^{}]+({[^{}]+})*)?}/g, blockungsid.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{blockungsid\s*(:[^{}]+({[^{}]+})*)?}/g, blockungsid.toString())
			.replace(/{fachid\s*(:[^{}]+({[^{}]+})*)?}/g, fachid.toString())
			.replace(/{kursartid\s*(:[^{}]+({[^{}]+})*)?}/g, kursartid.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{blockungsid\s*(:[^{}]+({[^{}]+})*)?}/g, blockungsid.toString())
			.replace(/{fachid\s*(:[^{}]+({[^{}]+})*)?}/g, fachid.toString())
			.replace(/{kursartid\s*(:[^{}]+({[^{}]+})*)?}/g, kursartid.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return GostBlockungKurs.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getGostBlockungGZip für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/{blockungsid : \d+}/gzip
	 *
	 * Liest für die angegebene Blockung der gymnasialen Oberstufe die grundlegenden Daten aus. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Blockungsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die GZip-komprimierten Blockungsdaten der gymnasialen Oberstfue für die angegebene ID
	 *     - Mime-Type: application/octet-stream
	 *     - Rückgabe-Typ: ApiFile
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Blockungsdaten der Gymnasialen Oberstufe auszulesen.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 404: Keine Blockung mit der angebenen ID gefunden.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 500: Es ist ein unerwarteter interner Fehler aufgetreten.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} blockungsid - der Pfad-Parameter blockungsid
	 *
	 * @returns Die GZip-komprimierten Blockungsdaten der gymnasialen Oberstfue für die angegebene ID
	 */
	public async getGostBlockungGZip(schema : string, blockungsid : number) : Promise<ApiFile> {
		const path = "/db/{schema}/gost/blockungen/{blockungsid : \\d+}/gzip"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{blockungsid\s*(:[^{}]+({[^{}]+})*)?}/g, blockungsid.toString());
		const data : ApiFile = await super.getOctetStream(path);
		return data;
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
	 *   Code 500: Ein unerwarteter Fehler ist beim Blocken aufgetreten.
	 *     - Mime-Type: text/plain
	 *     - Rückgabe-Typ: String
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} blockungsid - der Pfad-Parameter blockungsid
	 * @param {number} zeit - der Pfad-Parameter zeit
	 *
	 * @returns Eine Liste der IDs der Zwischenergebnisse
	 */
	public async rechneGostBlockung(schema : string, blockungsid : number, zeit : number) : Promise<List<number>> {
		const path = "/db/{schema}/gost/blockungen/{blockungsid : \\d+}/rechne/{zeit : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{blockungsid\s*(:[^{}]+({[^{}]+})*)?}/g, blockungsid.toString())
			.replace(/{zeit\s*(:[^{}]+({[^{}]+})*)?}/g, zeit.toString());
		const result : string = await super.postJSON(path, null);
		const obj = JSON.parse(result);
		const ret = new ArrayList<number>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(parseFloat(JSON.parse(text))); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode updateGostBlockungRegeln für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/{blockungsid : \d+}/regeln/update
	 *
	 * Entfernt und fügt mehrere Regeln einer Blockung der Gymnasialen Oberstufe hinzu. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen und Hinzufügen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Regeln wurden erfolgreich gelöscht bzw. hinzugefügt. Das Ergebnis beinhaltet die erstellten Regeln
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<GostBlockungRegel>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Regeln zu löschen oder hinzufügen.
	 *   Code 404: Ein Wert für das Erstellen der Regeln wurde nicht gefunden.
	 *
	 * @param {GostBlockungRegelUpdate} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} blockungsid - der Pfad-Parameter blockungsid
	 *
	 * @returns Die Regeln wurden erfolgreich gelöscht bzw. hinzugefügt. Das Ergebnis beinhaltet die erstellten Regeln
	 */
	public async updateGostBlockungRegeln(data : GostBlockungRegelUpdate, schema : string, blockungsid : number) : Promise<List<GostBlockungRegel>> {
		const path = "/db/{schema}/gost/blockungen/{blockungsid : \\d+}/regeln/update"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{blockungsid\s*(:[^{}]+({[^{}]+})*)?}/g, blockungsid.toString());
		const body : string = GostBlockungRegelUpdate.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<GostBlockungRegel>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(GostBlockungRegel.transpilerFromJSON(text)); });
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{kursid\s*(:[^{}]+({[^{}]+})*)?}/g, kursid.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{kursid\s*(:[^{}]+({[^{}]+})*)?}/g, kursid.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{kursid\s*(:[^{}]+({[^{}]+})*)?}/g, kursid.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{kursid\s*(:[^{}]+({[^{}]+})*)?}/g, kursid.toString())
			.replace(/{lehrerid\s*(:[^{}]+({[^{}]+})*)?}/g, lehrerid.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{kursid\s*(:[^{}]+({[^{}]+})*)?}/g, kursid.toString())
			.replace(/{lehrerid\s*(:[^{}]+({[^{}]+})*)?}/g, lehrerid.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{kursid\s*(:[^{}]+({[^{}]+})*)?}/g, kursid.toString())
			.replace(/{lehrerid\s*(:[^{}]+({[^{}]+})*)?}/g, lehrerid.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{kursid\s*(:[^{}]+({[^{}]+})*)?}/g, kursid.toString())
			.replace(/{lehrerid\s*(:[^{}]+({[^{}]+})*)?}/g, lehrerid.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{kursid\s*(:[^{}]+({[^{}]+})*)?}/g, kursid.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{kursid1\s*(:[^{}]+({[^{}]+})*)?}/g, kursid1.toString())
			.replace(/{kursid2\s*(:[^{}]+({[^{}]+})*)?}/g, kursid2.toString());
		const result : string = await super.postJSON(path, null);
		const text = result;
		return GostBlockungKurs.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der DELETE-Methode deleteGostBlockungKurse für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/kurse/delete/multiple
	 *
	 * Entfernt mehrere Kurse einer Blockung.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Kurse wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<GostBlockungKurs>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Kurse zu löschen.
	 *   Code 404: Einer oder mehrere der Kurse sind nicht vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Kurse wurde erfolgreich entfernt.
	 */
	public async deleteGostBlockungKurse(data : List<number>, schema : string) : Promise<List<GostBlockungKurs>> {
		const path = "/db/{schema}/gost/blockungen/kurse/delete/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<GostBlockungKurs>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(GostBlockungKurs.transpilerFromJSON(text)); });
		return ret;
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{regelid\s*(:[^{}]+({[^{}]+})*)?}/g, regelid.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{regelid\s*(:[^{}]+({[^{}]+})*)?}/g, regelid.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{regelid\s*(:[^{}]+({[^{}]+})*)?}/g, regelid.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{schienenid\s*(:[^{}]+({[^{}]+})*)?}/g, schienenid.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{schienenid\s*(:[^{}]+({[^{}]+})*)?}/g, schienenid.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{schienenid\s*(:[^{}]+({[^{}]+})*)?}/g, schienenid.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{ergebnisid\s*(:[^{}]+({[^{}]+})*)?}/g, ergebnisid.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostBlockungsergebnis.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchGostBlockungsergebnis für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \d+}
	 *
	 * Passt die Daten eines Blockungsergebnisses der Gymnasiale Oberstufe mit der angegebenen ID an.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anpassen der Blockungsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Blockungsdaten integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Blockungsdaten zu ändern.
	 *   Code 404: Kein Blockungsdaten-Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<GostBlockungsergebnis>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} ergebnisid - der Pfad-Parameter ergebnisid
	 */
	public async patchGostBlockungsergebnis(data : Partial<GostBlockungsergebnis>, schema : string, ergebnisid : number) : Promise<void> {
		const path = "/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{ergebnisid\s*(:[^{}]+({[^{}]+})*)?}/g, ergebnisid.toString());
		const body : string = GostBlockungsergebnis.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{ergebnisid\s*(:[^{}]+({[^{}]+})*)?}/g, ergebnisid.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{ergebnisid\s*(:[^{}]+({[^{}]+})*)?}/g, ergebnisid.toString());
		await super.postJSON(path, null);
		return;
	}


	/**
	 * Implementierung der GET-Methode dupliziereGostBlockungMitErgebnis für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \d+}/dupliziere
	 *
	 * Dupliziert zu dem angegebenen Zwischenergebnis gehörende Blockung der gymnasialen Oberstufe. Das Zwischenergebnis wird als einziges mit dupliziert und dient bei dem Blockungsduplikat. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Duplizieren einer Blockung besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Blockungslisteneintrag des Duplikats
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostBlockungListeneintrag
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Blockungsdaten der Gymnasialen Oberstufe zu duplizieren.
	 *   Code 404: Kein Blockungsergebnis mit der angegebenen ID gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} ergebnisid - der Pfad-Parameter ergebnisid
	 *
	 * @returns Der Blockungslisteneintrag des Duplikats
	 */
	public async dupliziereGostBlockungMitErgebnis(schema : string, ergebnisid : number) : Promise<GostBlockungListeneintrag> {
		const path = "/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \\d+}/dupliziere"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{ergebnisid\s*(:[^{}]+({[^{}]+})*)?}/g, ergebnisid.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostBlockungListeneintrag.transpilerFromJSON(text);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{ergebnisid\s*(:[^{}]+({[^{}]+})*)?}/g, ergebnisid.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostBlockungsdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der DELETE-Methode deleteGostBlockungsergebnisKursSchieneZuordnungen für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \d+}/removeKursSchienenZuordnungen
	 *
	 * Entfernt mehrere Kurs-Schienen-Zuordnungen bei einem Blockungsergebniss einer Blockung der Gymnasialen Oberstufe. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die Zuordnungen wurden erfolgreich gelöscht.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Zuordnungen zu löschen.
	 *   Code 404: Das Zwischenergebnis, eine Schiene oder ein Kurs wurde nicht in einer gültigen Zuordnung gefunden.
	 *
	 * @param {List<GostBlockungsergebnisKursSchienenZuordnung>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} ergebnisid - der Pfad-Parameter ergebnisid
	 */
	public async deleteGostBlockungsergebnisKursSchieneZuordnungen(data : List<GostBlockungsergebnisKursSchienenZuordnung>, schema : string, ergebnisid : number) : Promise<void> {
		const path = "/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \\d+}/removeKursSchienenZuordnungen"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{ergebnisid\s*(:[^{}]+({[^{}]+})*)?}/g, ergebnisid.toString());
		const body : string = "[" + (data.toArray() as Array<GostBlockungsergebnisKursSchienenZuordnung>).map(d => GostBlockungsergebnisKursSchienenZuordnung.transpilerToJSON(d)).join() + "]";
		await super.deleteJSON(path, body);
		return;
	}


	/**
	 * Implementierung der DELETE-Methode deleteGostBlockungsergebnisKursSchuelerZuordnungen für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \d+}/removeKursSchuelerZuordnungen
	 *
	 * Entfernt mehrere Kurs-Schüler-Zuordnungen bei einem Blockungsergebniss einer Blockung der Gymnasialen Oberstufe. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die Zuordnungen wurden erfolgreich gelöscht.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Zuordnungen zu löschen.
	 *   Code 404: Das Zwischenergebnis, ein Schüler oder ein Kurs wurde nicht in einer gültigen Zuordnung gefunden.
	 *
	 * @param {List<GostBlockungsergebnisKursSchuelerZuordnung>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} ergebnisid - der Pfad-Parameter ergebnisid
	 */
	public async deleteGostBlockungsergebnisKursSchuelerZuordnungen(data : List<GostBlockungsergebnisKursSchuelerZuordnung>, schema : string, ergebnisid : number) : Promise<void> {
		const path = "/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \\d+}/removeKursSchuelerZuordnungen"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{ergebnisid\s*(:[^{}]+({[^{}]+})*)?}/g, ergebnisid.toString());
		const body : string = "[" + (data.toArray() as Array<GostBlockungsergebnisKursSchuelerZuordnung>).map(d => GostBlockungsergebnisKursSchuelerZuordnung.transpilerToJSON(d)).join() + "]";
		await super.deleteJSON(path, body);
		return;
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{ergebnisid\s*(:[^{}]+({[^{}]+})*)?}/g, ergebnisid.toString())
			.replace(/{schienenid\s*(:[^{}]+({[^{}]+})*)?}/g, schienenid.toString())
			.replace(/{kursid\s*(:[^{}]+({[^{}]+})*)?}/g, kursid.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{ergebnisid\s*(:[^{}]+({[^{}]+})*)?}/g, ergebnisid.toString())
			.replace(/{schienenid\s*(:[^{}]+({[^{}]+})*)?}/g, schienenid.toString())
			.replace(/{kursid\s*(:[^{}]+({[^{}]+})*)?}/g, kursid.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{ergebnisid\s*(:[^{}]+({[^{}]+})*)?}/g, ergebnisid.toString())
			.replace(/{schienenid\s*(:[^{}]+({[^{}]+})*)?}/g, schienenid.toString())
			.replace(/{kursid\s*(:[^{}]+({[^{}]+})*)?}/g, kursid.toString())
			.replace(/{schienenidneu\s*(:[^{}]+({[^{}]+})*)?}/g, schienenidneu.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{ergebnisid\s*(:[^{}]+({[^{}]+})*)?}/g, ergebnisid.toString())
			.replace(/{schuelerid\s*(:[^{}]+({[^{}]+})*)?}/g, schuelerid.toString())
			.replace(/{kursid\s*(:[^{}]+({[^{}]+})*)?}/g, kursid.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{ergebnisid\s*(:[^{}]+({[^{}]+})*)?}/g, ergebnisid.toString())
			.replace(/{schuelerid\s*(:[^{}]+({[^{}]+})*)?}/g, schuelerid.toString())
			.replace(/{kursid\s*(:[^{}]+({[^{}]+})*)?}/g, kursid.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{ergebnisid\s*(:[^{}]+({[^{}]+})*)?}/g, ergebnisid.toString())
			.replace(/{schuelerid\s*(:[^{}]+({[^{}]+})*)?}/g, schuelerid.toString())
			.replace(/{kursid\s*(:[^{}]+({[^{}]+})*)?}/g, kursid.toString())
			.replace(/{kursidneu\s*(:[^{}]+({[^{}]+})*)?}/g, kursidneu.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{ergebnisid\s*(:[^{}]+({[^{}]+})*)?}/g, ergebnisid.toString());
		await super.postJSON(path, null);
		return;
	}


	/**
	 * Implementierung der POST-Methode updateGostBlockungsergebnisKursSchuelerZuordnungen für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \d+}/updateKursSchuelerZuordnungen
	 *
	 * Entfernt und fügt mehrere Kurs-Schüler-Zuordnungen bei einem Blockungsergebniss einer Blockung der Gymnasialen Oberstufe hinzu. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen und Hinzufügen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Zuordnungen wurden erfolgreich gelöscht bzw. hinzugefügt. Das Ergebnis beinhaltet die erstellten Regeln, falls Regeln angepasst wurden, oder eine leere Liste, falls keine angepasst wurden.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<GostBlockungRegel>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Zuordnungen zu löschen oder hinzufügen.
	 *   Code 404: Das Zwischenergebnis, ein Schüler oder ein Kurs wurde nicht in einer gültigen Zuordnung gefunden.
	 *
	 * @param {GostBlockungsergebnisKursSchuelerZuordnungUpdate} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} ergebnisid - der Pfad-Parameter ergebnisid
	 *
	 * @returns Die Zuordnungen wurden erfolgreich gelöscht bzw. hinzugefügt. Das Ergebnis beinhaltet die erstellten Regeln, falls Regeln angepasst wurden, oder eine leere Liste, falls keine angepasst wurden.
	 */
	public async updateGostBlockungsergebnisKursSchuelerZuordnungen(data : GostBlockungsergebnisKursSchuelerZuordnungUpdate, schema : string, ergebnisid : number) : Promise<List<GostBlockungRegel>> {
		const path = "/db/{schema}/gost/blockungen/zwischenergebnisse/{ergebnisid : \\d+}/updateKursSchuelerZuordnungen"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{ergebnisid\s*(:[^{}]+({[^{}]+})*)?}/g, ergebnisid.toString());
		const body : string = GostBlockungsergebnisKursSchuelerZuordnungUpdate.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<GostBlockungRegel>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(GostBlockungRegel.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der DELETE-Methode deleteGostBlockungsergebnisse für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/blockungen/zwischenergebnisse/multiple
	 *
	 * Entfernt die angegebenen Zwischenergebnisse einer Blockung der gymnasialen Oberstufe. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen der Zwischenergebnisse besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Zwischenergebnisse einer Blockung der gymnasialen Oberstufe für die angegebene ID wurden erfolgreich gelöscht.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Long
	 *   Code 400: Die Ergebnisse gehören nicht zu einer Blockung.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Zwischenergebnisse einer Blockung der Gymnasialen Oberstufe zu löschen.
	 *   Code 404: Mindestens ein Ergebnis wurde nicht gefunden.
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Zwischenergebnisse einer Blockung der gymnasialen Oberstufe für die angegebene ID wurden erfolgreich gelöscht.
	 */
	public async deleteGostBlockungsergebnisse(data : List<number>, schema : string) : Promise<number | null> {
		const path = "/db/{schema}/gost/blockungen/zwischenergebnisse/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const text = result;
		return parseFloat(JSON.parse(text));
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return GostJahrgangFachkombination.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode getGostKlausurenCollectionAlldata für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/collection/alldata
	 *
	 * Liefert alle zur Klausurplanung gehörenden Daten in einem GostKlausurenCollectionAllData-Objekt.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Daten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Das GostKlausurenCollectionAllData-Objekt mit den Daten der Klausurplanung für den angegebenen Abiturjahrgang und das Halbjahr.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostKlausurenCollectionAllData
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten auszulesen.
	 *   Code 404: Der Abiturjahrgang oder das GostHalbjahr wurde nicht gefunden.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<GostKlausurenCollectionHjData>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Das GostKlausurenCollectionAllData-Objekt mit den Daten der Klausurplanung für den angegebenen Abiturjahrgang und das Halbjahr.
	 */
	public async getGostKlausurenCollectionAlldata(data : List<GostKlausurenCollectionHjData>, schema : string) : Promise<GostKlausurenCollectionAllData> {
		const path = "/db/{schema}/gost/klausuren/collection/alldata"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<GostKlausurenCollectionHjData>).map(d => GostKlausurenCollectionHjData.transpilerToJSON(d)).join() + "]";
		const result : string = await super.postJSON(path, body);
		const text = result;
		return GostKlausurenCollectionAllData.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode getGostKlausurenCollectionAlldataGZip für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/collection/alldata/gzip
	 *
	 * Liefert alle zur Klausurplanung gehörenden Daten in einem komprimierten GostKlausurenCollectionAllData-Objekt. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Daten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die GZip-komprimierten Daten der Klausurplanung für den angegebenen Abiturjahrgang und das Halbjahr.
	 *     - Mime-Type: application/octet-stream
	 *     - Rückgabe-Typ: ApiFile
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Klausurplanung der Gymnasialen Oberstufe auszulesen.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 500: Es ist ein unerwarteter interner Fehler aufgetreten.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {List<GostKlausurenCollectionHjData>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die GZip-komprimierten Daten der Klausurplanung für den angegebenen Abiturjahrgang und das Halbjahr.
	 */
	public async getGostKlausurenCollectionAlldataGZip(data : List<GostKlausurenCollectionHjData>, schema : string) : Promise<ApiFile> {
		const path = "/db/{schema}/gost/klausuren/collection/alldata/gzip"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<GostKlausurenCollectionHjData>).map(d => GostKlausurenCollectionHjData.transpilerToJSON(d)).join() + "]";
		const result : ApiFile = await super.postJSONtoOctetStream(path, body);
		return result;
	}


	/**
	 * Implementierung der GET-Methode getGostKlausurenCollectionAllIssues für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/collection/issues/abiturjahrgang/{abiturjahr : -?\d+}/halbjahr/{halbjahr : \d+}
	 *
	 * Liefert alle zur Klausurplanung gehörenden Fehler und Probleme in einem GostKlausurenCollectionHjData-Objekt.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Das GostKlausurenCollectionAllData-Objekt mit den Fehlern und Problemen der Klausurplanung für den angegebenen Abiturjahrgang und das Halbjahr.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostKlausurenCollectionHjData
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten auszulesen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 * @param {number} halbjahr - der Pfad-Parameter halbjahr
	 *
	 * @returns Das GostKlausurenCollectionAllData-Objekt mit den Fehlern und Problemen der Klausurplanung für den angegebenen Abiturjahrgang und das Halbjahr.
	 */
	public async getGostKlausurenCollectionAllIssues(schema : string, abiturjahr : number, halbjahr : number) : Promise<GostKlausurenCollectionHjData> {
		const path = "/db/{schema}/gost/klausuren/collection/issues/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString())
			.replace(/{halbjahr\s*(:[^{}]+({[^{}]+})*)?}/g, halbjahr.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostKlausurenCollectionHjData.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getGostKlausurenCollectionAllIssuesGZip für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/collection/issues/abiturjahrgang/{abiturjahr : -?\d+}/halbjahr/{halbjahr : \d+}/gzip
	 *
	 * Liefert alle zur Klausurplanung gehörenden Fehler und Probleme in einem komprimierten GostKlausurenCollectionHjData-Objekt.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Blockungsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Das komprimierte GostKlausurenCollectionHjData-Objekt mit den Fehlern und Problemen der Klausurplanung für den angegebenen Abiturjahrgang und das Halbjahr.
	 *     - Mime-Type: application/octet-stream
	 *     - Rückgabe-Typ: ApiFile
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Klausurdaten der Gymnasialen Oberstufe auszulesen.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *   Code 500: Es ist ein unerwarteter interner Fehler aufgetreten.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 * @param {number} halbjahr - der Pfad-Parameter halbjahr
	 *
	 * @returns Das komprimierte GostKlausurenCollectionHjData-Objekt mit den Fehlern und Problemen der Klausurplanung für den angegebenen Abiturjahrgang und das Halbjahr.
	 */
	public async getGostKlausurenCollectionAllIssuesGZip(schema : string, abiturjahr : number, halbjahr : number) : Promise<ApiFile> {
		const path = "/db/{schema}/gost/klausuren/collection/issues/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : \\d+}/gzip"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString())
			.replace(/{halbjahr\s*(:[^{}]+({[^{}]+})*)?}/g, halbjahr.toString());
		const data : ApiFile = await super.getOctetStream(path);
		return data;
	}


	/**
	 * Implementierung der POST-Methode patchGostKlausurenKursklausur für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/kursklausuren/{id : \d+}
	 *
	 * Patcht eine Gost-Kursklausur und gibt die daraufhin geänderten Raumdaten zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen einer Gost-Kursklausur besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Der Patch wurde erfolgreich in die Kursklausur integriert.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostKlausurenCollectionSkrsKrsData
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Kursklausuren zu ändern.
	 *   Code 404: Kein Kursklausur-Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<GostKursklausur>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Patch wurde erfolgreich in die Kursklausur integriert.
	 */
	public async patchGostKlausurenKursklausur(data : Partial<GostKursklausur>, schema : string, id : number) : Promise<GostKlausurenCollectionSkrsKrsData> {
		const path = "/db/{schema}/gost/klausuren/kursklausuren/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = GostKursklausur.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return GostKlausurenCollectionSkrsKrsData.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode blockenGostKursklausuren für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/kursklausuren/blocken
	 *
	 * Startet den Kursklausur-Blockungsalgorithmus für die übergebenen GostKlausurterminblockungDaten.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Blocken besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Klausurblockung wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostKlausurenCollectionData
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Klausurblockung durchzuführen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {GostKlausurterminblockungDaten} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Klausurblockung wurde erfolgreich angelegt.
	 */
	public async blockenGostKursklausuren(data : GostKlausurterminblockungDaten, schema : string) : Promise<GostKlausurenCollectionData> {
		const path = "/db/{schema}/gost/klausuren/kursklausuren/blocken"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = GostKlausurterminblockungDaten.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return GostKlausurenCollectionData.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode createGostKlausurenKursklausurenJahrgangHalbjahrQuartal für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/kursklausuren/create/abiturjahrgang/{abiturjahr : -?\d+}/halbjahr/{halbjahr : \d+}/quartal/{quartal : \d+}
	 *
	 * Erzeugt die Kursklausuren eines Abiturjahrgangs in einem bestimmten GostHalbjahr und Quartal der Gymnasialen Oberstufe. Schülerklausuren und Haupttermin-Schülerklausurtermine werden ebenfalls erzeugt.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen der Kursklausuren besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Liste der Kursklausuren.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostKlausurenCollectionData
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
	public async createGostKlausurenKursklausurenJahrgangHalbjahrQuartal(schema : string, abiturjahr : number, halbjahr : number, quartal : number) : Promise<GostKlausurenCollectionData> {
		const path = "/db/{schema}/gost/klausuren/kursklausuren/create/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : \\d+}/quartal/{quartal : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString())
			.replace(/{halbjahr\s*(:[^{}]+({[^{}]+})*)?}/g, halbjahr.toString())
			.replace(/{quartal\s*(:[^{}]+({[^{}]+})*)?}/g, quartal.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostKlausurenCollectionData.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der DELETE-Methode deleteGostKlausurenKursklausuren für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/kursklausuren/delete
	 *
	 * Löscht mehrere GostKursklausuren.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen einer GostKursklausur besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Kursklausuren für die angegebenen IDs wurden erfolgreich gelöscht.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Long>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine GostKursklausur zu löschen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Kursklausuren für die angegebenen IDs wurden erfolgreich gelöscht.
	 */
	public async deleteGostKlausurenKursklausuren(data : List<number>, schema : string) : Promise<List<number>> {
		const path = "/db/{schema}/gost/klausuren/kursklausuren/delete"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<number>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(parseFloat(JSON.parse(text))); });
		return ret;
	}


	/**
	 * Implementierung der PATCH-Methode patchGostKlausurenRaum für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/raeume/{id : \d+}
	 *
	 * Patcht einen GostKlausurraum.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen eines GostKlausurraums besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in den GostKlausurraum integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um GostKlausurräume zu ändern.
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<GostKlausurraum>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchGostKlausurenRaum(data : Partial<GostKlausurraum>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/gost/klausuren/raeume/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = GostKlausurraum.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return (text === "true");
	}


	/**
	 * Implementierung der POST-Methode createGostKlausurenRaum für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/raeume/new
	 *
	 * Erstellt einen neue GostKlausurraum und gibt ihn zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines GostKlausurraums besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: GostKlausurraum wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostKlausurraum
	 *   Code 400: Die Daten sind fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen GostKlausurraum anzulegen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<GostKlausurraum>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns GostKlausurraum wurde erfolgreich angelegt.
	 */
	public async createGostKlausurenRaum(data : Partial<GostKlausurraum>, schema : string) : Promise<GostKlausurraum> {
		const path = "/db/{schema}/gost/klausuren/raeume/new"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = GostKlausurraum.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return GostKlausurraum.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getGostKlausurenCollectionBySchuelerid für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/schueler/{sid : -?\d+}/abiturjahrgang/{abiturjahr : -?\d+}/schuljahr/{halbjahr : \d+}
	 *
	 * Fragt die Klausurdaten eines Schülers ab.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Abfrage war erfolgreich.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostKlausurenCollectionData
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten auszulesen.
	 *   Code 404: Der Schüler-ID wurde nicht gefunden.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} sid - der Pfad-Parameter sid
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 * @param {number} halbjahr - der Pfad-Parameter halbjahr
	 *
	 * @returns Abfrage war erfolgreich.
	 */
	public async getGostKlausurenCollectionBySchuelerid(schema : string, sid : number, abiturjahr : number, halbjahr : number) : Promise<GostKlausurenCollectionData> {
		const path = "/db/{schema}/gost/klausuren/schueler/{sid : -?\\d+}/abiturjahrgang/{abiturjahr : -?\\d+}/schuljahr/{halbjahr : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{sid\s*(:[^{}]+({[^{}]+})*)?}/g, sid.toString())
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString())
			.replace(/{halbjahr\s*(:[^{}]+({[^{}]+})*)?}/g, halbjahr.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostKlausurenCollectionData.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchGostKlausurenSchuelerklausur für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/schuelerklausuren/{id : \d+}
	 *
	 * Patcht eine Gost-Schuelerklausur.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen einer Gost-Schuelerklausur besitzt.
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = GostSchuelerklausur.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteGostKlausurenSchuelerklausuren für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/schuelerklausuren/delete
	 *
	 * Löscht mehrere GostSchuelerklausuren.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen einer GostSchuelerklausur besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Schülerklausuren für die angegebenen IDs wurden erfolgreich gelöscht.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Long>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine GostSchuelerklausur zu löschen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Schülerklausuren für die angegebenen IDs wurden erfolgreich gelöscht.
	 */
	public async deleteGostKlausurenSchuelerklausuren(data : List<number>, schema : string) : Promise<List<number>> {
		const path = "/db/{schema}/gost/klausuren/schuelerklausuren/delete"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<number>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(parseFloat(JSON.parse(text))); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode createGostKlausurenSchuelerklausuren für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/schuelerklausuren/new
	 *
	 * Erstellt mehrere neue GostSchuelerklausuren inklusive der zugehörigen GostSchuelerklausurTermine.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen der Daten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Daten wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostKlausurenCollectionData
	 *   Code 400: Die Daten sind fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um GostSchuelerklausuren anzulegen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<Partial<GostSchuelerklausur>>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Daten wurde erfolgreich angelegt.
	 */
	public async createGostKlausurenSchuelerklausuren(data : List<Partial<GostSchuelerklausur>>, schema : string) : Promise<GostKlausurenCollectionData> {
		const path = "/db/{schema}/gost/klausuren/schuelerklausuren/new"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<GostSchuelerklausur>).map(d => GostSchuelerklausur.transpilerToJSONPatch(d)).join() + "]";
		const result : string = await super.postJSON(path, body);
		const text = result;
		return GostKlausurenCollectionData.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode patchGostKlausurenSchuelerklausurtermin für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/schuelerklausuren/termine/{id : \d+}
	 *
	 * Patcht einen GostSchuelerklausurTermin.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen eines GostSchuelerklausurTermin besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Der Patch wurde erfolgreich in den GostKlausurtermin integriert.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostKlausurenCollectionSkrsKrsData
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um GostSchuelerklausurTermine zu ändern.
	 *   Code 404: Kein GostSchuelerklausurTermin-Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<GostSchuelerklausurTermin>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Patch wurde erfolgreich in den GostKlausurtermin integriert.
	 */
	public async patchGostKlausurenSchuelerklausurtermin(data : Partial<GostSchuelerklausurTermin>, schema : string, id : number) : Promise<GostKlausurenCollectionSkrsKrsData> {
		const path = "/db/{schema}/gost/klausuren/schuelerklausuren/termine/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = GostSchuelerklausurTermin.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return GostKlausurenCollectionSkrsKrsData.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode blockenGostSchuelerklausurtermine für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/schuelerklausuren/termine/blocken
	 *
	 * Startet den Nachschreiber-Blockungsalgorithmus für die übergebenen GostNachschreibterminblockungKonfiguration.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Blocken besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Klausurblockung wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostKlausurenCollectionData
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Klausurblockung durchzuführen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {GostNachschreibterminblockungKonfiguration} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Klausurblockung wurde erfolgreich angelegt.
	 */
	public async blockenGostSchuelerklausurtermine(data : GostNachschreibterminblockungKonfiguration, schema : string) : Promise<GostKlausurenCollectionData> {
		const path = "/db/{schema}/gost/klausuren/schuelerklausuren/termine/blocken"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = GostNachschreibterminblockungKonfiguration.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return GostKlausurenCollectionData.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der DELETE-Methode deleteGostKlausurenSchuelerklausurtermin für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/schuelerklausuren/termine/delete/{id : \d+}
	 *
	 * Löscht einen GostSchuelerklausurTermin.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen eines GostSchuelerklausurTermins besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: GostSchuelerklausurTermin wurde erfolgreich gelöscht.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Boolean
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen GostSchuelerklausurTermin zu löschen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns GostSchuelerklausurTermin wurde erfolgreich gelöscht.
	 */
	public async deleteGostKlausurenSchuelerklausurtermin(schema : string, id : number) : Promise<boolean | null> {
		const path = "/db/{schema}/gost/klausuren/schuelerklausuren/termine/delete/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return (text === "true");
	}


	/**
	 * Implementierung der POST-Methode loescheGostSchuelerklausurtermineAusRaum für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/schuelerklausuren/termine/loescheraum
	 *
	 * Löscht die Raumzuweisungen für alle in den GostKlausurraumRich-Objekten übergebene GostSchuelerklausurTermin-IDsDabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen einer Raumzuweisung besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Raumzuweisungen wurde erfolgreich gelöscht.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostKlausurenCollectionSkrsKrsData
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Raumzuweisungen zu löschen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Raumzuweisungen wurde erfolgreich gelöscht.
	 */
	public async loescheGostSchuelerklausurtermineAusRaum(data : List<number>, schema : string) : Promise<GostKlausurenCollectionSkrsKrsData> {
		const path = "/db/{schema}/gost/klausuren/schuelerklausuren/termine/loescheraum"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.postJSON(path, body);
		const text = result;
		return GostKlausurenCollectionSkrsKrsData.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode createGostKlausurenSchuelerklausurtermin für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/schuelerklausuren/termine/new/{id : \d+}
	 *
	 * Erstellt einen neuen GostSchuelerklausurTermin für die als ID übergebene GostSchuelerklausur und gibt ihn zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines GostSchuelerklausurTermins besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: GostSchuelerklausurTermin wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostSchuelerklausurTermin
	 *   Code 400: Die Daten sind fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen GostSchuelerklausurTermin anzulegen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns GostSchuelerklausurTermin wurde erfolgreich angelegt.
	 */
	public async createGostKlausurenSchuelerklausurtermin(schema : string, id : number) : Promise<GostSchuelerklausurTermin> {
		const path = "/db/{schema}/gost/klausuren/schuelerklausuren/termine/new/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostSchuelerklausurTermin.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode setzeGostSchuelerklausurtermineZuRaum für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/schuelerklausuren/termine/zuraum
	 *
	 * Weist die in den GostKlausurraumRich-Objekten übergebenen IDs der GostSchuelerklausurTermine dem jeweiligen GostKlausurraum zu.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Zuweisen eines Klausurraums besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Raumzuweisungen wurden erfolgreich übernommen.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostKlausurenCollectionSkrsKrsData
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Räume zuzuweisen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<GostKlausurraumRich>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Raumzuweisungen wurden erfolgreich übernommen.
	 */
	public async setzeGostSchuelerklausurtermineZuRaum(data : List<GostKlausurraumRich>, schema : string) : Promise<GostKlausurenCollectionSkrsKrsData> {
		const path = "/db/{schema}/gost/klausuren/schuelerklausuren/termine/zuraum"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<GostKlausurraumRich>).map(d => GostKlausurraumRich.transpilerToJSON(d)).join() + "]";
		const result : string = await super.postJSON(path, body);
		const text = result;
		return GostKlausurenCollectionSkrsKrsData.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode patchGostKlausurenKlausurtermin für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/termine/{id : \d+}
	 *
	 * Patcht einen GostKlausurtermin.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen eines GostKlausurtermins besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Der Patch wurde erfolgreich in den GostKlausurtermin integriert.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: GostKlausurenCollectionSkrsKrsData
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um GostKlausurtermine zu ändern.
	 *   Code 404: Kein Kursklausur-Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<GostKlausurtermin>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Patch wurde erfolgreich in den GostKlausurtermin integriert.
	 */
	public async patchGostKlausurenKlausurtermin(data : Partial<GostKlausurtermin>, schema : string, id : number) : Promise<GostKlausurenCollectionSkrsKrsData> {
		const path = "/db/{schema}/gost/klausuren/termine/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = GostKlausurtermin.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return GostKlausurenCollectionSkrsKrsData.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der DELETE-Methode deleteGostKlausurenKlausurtermine für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/termine/delete
	 *
	 * Löscht mehrere GostKlausurtermine.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen eines GostKlausurtermins besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Klausurtermine für die angegebenen IDs wurden erfolgreich gelöscht.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Long>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen GostKlausurtermin zu löschen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Klausurtermine für die angegebenen IDs wurden erfolgreich gelöscht.
	 */
	public async deleteGostKlausurenKlausurtermine(data : List<number>, schema : string) : Promise<List<number>> {
		const path = "/db/{schema}/gost/klausuren/termine/delete"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = GostKlausurtermin.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return GostKlausurtermin.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode updateGostKlausuren für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/update
	 *
	 * Patcht einen Gost-Klausurtermin.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Patchen eines Gost-Klausurtermins besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Gost-Klausurraumstunde wurde erfolgreich angelegt.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einer Gost-Klausurraumstunde anzulegen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {GostKlausurenUpdate} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 */
	public async updateGostKlausuren(data : GostKlausurenUpdate, schema : string) : Promise<void> {
		const path = "/db/{schema}/gost/klausuren/update"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = GostKlausurenUpdate.transpilerToJSON(data);
		await super.postJSON(path, body);
		return;
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = GostKlausurvorgabe.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getGostKlausurenVorgabenJahrgang für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/vorgaben/abiturjahrgang/{abiturjahr : -?\d+}
	 *
	 * Liefert die Klausurvorgaben eines Abiturjahrgangs der gymnasialen Oberstufe. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Liste der Klausurvorgaben.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<GostKlausurvorgabe>
	 *   Code 400: Die Daten sind fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Klausurvorgaben auszulesen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 *
	 * @returns Die Liste der Klausurvorgaben.
	 */
	public async getGostKlausurenVorgabenJahrgang(schema : string, abiturjahr : number) : Promise<List<GostKlausurvorgabe>> {
		const path = "/db/{schema}/gost/klausuren/vorgaben/abiturjahrgang/{abiturjahr : -?\\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<GostKlausurvorgabe>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(GostKlausurvorgabe.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode copyGostKlausurenVorgaben für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/vorgaben/copyVorlagen/abiturjahrgang/{abiturjahr : -?\d+}/halbjahr/{halbjahr : -?\d+}/quartal/{quartal : -?\d+}
	 *
	 * Kopiert die Klausurvorgabe-Vorlagen in einen konkreten Abiturjahrgang und gibt sie zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anlegen der Gost-Klausurvorgaben besitzt.
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
	 * @param {number} abiturjahr - der Pfad-Parameter abiturjahr
	 * @param {number} halbjahr - der Pfad-Parameter halbjahr
	 * @param {number} quartal - der Pfad-Parameter quartal
	 *
	 * @returns Die Liste der neuen Klausurvorgaben.
	 */
	public async copyGostKlausurenVorgaben(schema : string, abiturjahr : number, halbjahr : number, quartal : number) : Promise<List<GostKlausurvorgabe>> {
		const path = "/db/{schema}/gost/klausuren/vorgaben/copyVorlagen/abiturjahrgang/{abiturjahr : -?\\d+}/halbjahr/{halbjahr : -?\\d+}/quartal/{quartal : -?\\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abiturjahr\s*(:[^{}]+({[^{}]+})*)?}/g, abiturjahr.toString())
			.replace(/{halbjahr\s*(:[^{}]+({[^{}]+})*)?}/g, halbjahr.toString())
			.replace(/{quartal\s*(:[^{}]+({[^{}]+})*)?}/g, quartal.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<GostKlausurvorgabe>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(GostKlausurvorgabe.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode createGostKlausurenDefaultVorgaben für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/klausuren/vorgaben/createDefault/halbjahr/{halbjahr : -?\d+}/quartal/{quartal : -?\d+}
	 *
	 * Legt die Default-Klausurvorgaben im Vorlagen-Jahrgang an und gibt sie zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Gost-Klausurvorgabe besitzt.
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
	public async createGostKlausurenDefaultVorgaben(schema : string, halbjahr : number, quartal : number) : Promise<List<GostKlausurvorgabe>> {
		const path = "/db/{schema}/gost/klausuren/vorgaben/createDefault/halbjahr/{halbjahr : -?\\d+}/quartal/{quartal : -?\\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{halbjahr\s*(:[^{}]+({[^{}]+})*)?}/g, halbjahr.toString())
			.replace(/{quartal\s*(:[^{}]+({[^{}]+})*)?}/g, quartal.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = GostKlausurvorgabe.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return GostKlausurvorgabe.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode exportGostSchuelerLaufbahnplanungen für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/laufbahnplanung/export
	 *
	 * Liest die Laufbahnplanungsdaten der gymnasialen Oberstufe für die angegebenen Schüler aus der Datenbank und liefert diese GZip-komprimiert zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Auslesen der Daten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die GZip-komprimierten Laufbahndaten der gymnasialen Obertufe
	 *     - Mime-Type: application/zip
	 *     - Rückgabe-Typ: ApiFile
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Laufbahndaten auszulesen.
	 *   Code 404: Es wurden nicht alle benötigten Daten für das Erstellen der Laufbahn-Daten gefunden.
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die GZip-komprimierten Laufbahndaten der gymnasialen Obertufe
	 */
	public async exportGostSchuelerLaufbahnplanungen(data : List<number>, schema : string) : Promise<ApiFile> {
		const path = "/db/{schema}/gost/laufbahnplanung/export"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : ApiFile = await super.postJSONtoZIP(path, body);
		return result;
	}


	/**
	 * Implementierung der POST-Methode importGostSchuelerLaufbahnplanungen für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/laufbahnplanung/import
	 *
	 * Importiert die Laufbahndaten aus den übergebenen Laufbahnplanungsdatein
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
	public async importGostSchuelerLaufbahnplanungen(data : FormData, schema : string) : Promise<SimpleOperationResponse> {
		const path = "/db/{schema}/gost/laufbahnplanung/import"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
	 *     - Mime-Type: text/plain
	 *     - Rückgabe-Typ: String
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Abiturdaten des Schülers
	 */
	public async getGostSchuelerAbiturdaten(schema : string, id : number) : Promise<Abiturdaten> {
		const path = "/db/{schema}/gost/schueler/{id : \\d+}/abiturdaten"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return Abiturdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchGostSchuelerAbiturdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/schueler/{id : \d+}/abiturdaten
	 *
	 * Passt die Abiturdaten eines Schüler an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Anpassungen durchzuführen.
	 *   Code 404: Keine passenden Daten für den Patch gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<Abiturdaten>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchGostSchuelerAbiturdaten(data : Partial<Abiturdaten>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/gost/schueler/{id : \\d+}/abiturdaten"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = Abiturdaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der POST-Methode copyGostSchuelerAbiturdatenAusLeistungsdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/schueler/{id : \d+}/abiturdaten/uebertragen
	 *
	 * Überträgt die Abitur-relevanten Daten aus den Leistungsdaten in den Abiturbereich.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die Abiturdaten wurden erfolgreich übertragen
	 *   Code 400: Der Schüler hat aktuell nicht alle Leistungen für die Qualifikationsphase
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Leistungsdaten in den Abiturbereich zu übertragen
	 *   Code 404: Es wurden keine Leistungsdaten für die Übertragung gefunden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async copyGostSchuelerAbiturdatenAusLeistungsdaten(schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/gost/schueler/{id : \\d+}/abiturdaten/uebertragen"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		await super.postJSON(path, null);
		return;
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
	 *     - Rückgabe-Typ: GostSchuelerFachwahl
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Fachwahlen der Gymnasialen Oberstufe eines Schülers auszulesen.
	 *   Code 404: Kein Eintrag für einen Schüler mit Laufbahnplanungsdaten der gymnasialen Oberstufe für die angegebene ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} schuelerid - der Pfad-Parameter schuelerid
	 * @param {number} fachid - der Pfad-Parameter fachid
	 *
	 * @returns Die Fachwahlen der gymnasialen Oberstufe für das angegebene Fach und den angegebenen Schüler
	 */
	public async getGostSchuelerFachwahl(schema : string, schuelerid : number, fachid : number) : Promise<GostSchuelerFachwahl> {
		const path = "/db/{schema}/gost/schueler/{schuelerid : \\d+}/fachwahl/{fachid : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{schuelerid\s*(:[^{}]+({[^{}]+})*)?}/g, schuelerid.toString())
			.replace(/{fachid\s*(:[^{}]+({[^{}]+})*)?}/g, fachid.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return GostSchuelerFachwahl.transpilerFromJSON(text);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{schuelerid\s*(:[^{}]+({[^{}]+})*)?}/g, schuelerid.toString())
			.replace(/{fachid\s*(:[^{}]+({[^{}]+})*)?}/g, fachid.toString());
		const body : string = GostSchuelerFachwahl.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der POST-Methode deleteGostSchuelerFachwahlen für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/schueler/{schuelerid : \d+}/fachwahl/delete
	 *
	 * Löscht die Fachwahlen des Schülers mit der angegebenen ID.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen der Fachwahlen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 203: Die Fachwahlen wurden erfolgreich gelöscht.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Fachwahlen zu löschen.
	 *   Code 404: Der Schüler bzw. der zugehörige Abiturjahrgang wurde nicht gefunden.
	 *   Code 409: Es liegen bereits bewertete Abschnitt vor, so dass die Fachwahlen nicht vollständig entfernt werden können.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} schuelerid - der Pfad-Parameter schuelerid
	 */
	public async deleteGostSchuelerFachwahlen(schema : string, schuelerid : number) : Promise<void> {
		const path = "/db/{schema}/gost/schueler/{schuelerid : \\d+}/fachwahl/delete"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{schuelerid\s*(:[^{}]+({[^{}]+})*)?}/g, schuelerid.toString());
		await super.postJSON(path, null);
		return;
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{schuelerid\s*(:[^{}]+({[^{}]+})*)?}/g, schuelerid.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{schuelerid\s*(:[^{}]+({[^{}]+})*)?}/g, schuelerid.toString());
		const body : string = GostLaufbahnplanungBeratungsdaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der POST-Methode deleteGostSchuelerFachwahlenMultiple für den Zugriff auf die URL https://{hostname}/db/{schema}/gost/schueler/fachwahlen/deleteMultiple
	 *
	 * Löscht die Fachwahlen der Schüler mit den angegebenen IDs.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen der Fachwahlen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 203: Die Fachwahlen wurden erfolgreich gelöscht.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Fachwahlen zu löschen.
	 *   Code 404: Ein Schüler bzw. der zugehörige Abiturjahrgang wurde nicht gefunden.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 */
	public async deleteGostSchuelerFachwahlenMultiple(data : List<number>, schema : string) : Promise<void> {
		const path = "/db/{schema}/gost/schueler/fachwahlen/deleteMultiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		await super.postJSON(path, body);
		return;
	}


	/**
	 * Implementierung der GET-Methode getHaltestellen für den Zugriff auf die URL https://{hostname}/db/{schema}/haltestellen
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Haltestellen unter Angabe der ID, eines Kürzels und der textuellen Beschreibung sowie der Information, ob der Eintrag in der Anwendung sichtbar bzw. änderbar sein soll, und gibt diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
	 *     - Rückgabe-Typ: List<JahrgangsDaten>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Jahrgangsdaten anzusehen.
	 *   Code 404: Keine Jahrgangs-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Jahrgangs-Listen-Einträgen
	 */
	public async getJahrgaenge(schema : string) : Promise<List<JahrgangsDaten>> {
		const path = "/db/{schema}/jahrgaenge/"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<JahrgangsDaten>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(JahrgangsDaten.transpilerFromJSON(text)); });
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return JahrgangsDaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchJahrgang für den Zugriff auf die URL https://{hostname}/db/{schema}/jahrgaenge/{id : \d+}
	 *
	 * Passt den Jahrgang mit der angebenen ID an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Jahrgangsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 404: Kein Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<JahrgangsDaten>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchJahrgang(data : Partial<JahrgangsDaten>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/jahrgaenge/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = JahrgangsDaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteJahrgang für den Zugriff auf die URL https://{hostname}/db/{schema}/jahrgaenge/{id : \d+}
	 *
	 * Entfernt einen Jahrgang. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Jahrgänge hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Jahrgang wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: JahrgangsDaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Jahrgang zu bearbeiten.
	 *   Code 404: Kein Jahrgang vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Jahrgang wurde erfolgreich entfernt.
	 */
	public async deleteJahrgang(schema : string, id : number) : Promise<JahrgangsDaten> {
		const path = "/db/{schema}/jahrgaenge/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
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
	 *     - Rückgabe-Typ: List<JahrgaengeKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Jahrgangs-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Jahrgangs-Katalog-Einträgen
	 */
	public async getKatalogJahrgaenge(schema : string) : Promise<List<JahrgaengeKatalogEintrag>> {
		const path = "/db/{schema}/jahrgaenge/allgemein/jahrgaenge"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<JahrgaengeKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(JahrgaengeKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode addJahrgang für den Zugriff auf die URL https://{hostname}/db/{schema}/jahrgaenge/create
	 *
	 * Erstellt einen neuen Jahrgang und gibt das zugehörige Objekt zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten der Jahrgänge besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Der Jahrgang wurde erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Raum
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Jahrgang für die Schule anzulegen.
	 *   Code 404: Die Jahrgangsdaten wurden nicht gefunden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<JahrgangsDaten>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Jahrgang wurde erfolgreich hinzugefügt.
	 */
	public async addJahrgang(data : Partial<JahrgangsDaten>, schema : string) : Promise<Raum> {
		const path = "/db/{schema}/jahrgaenge/create"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = JahrgangsDaten.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return Raum.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der DELETE-Methode deleteJahrgaenge für den Zugriff auf die URL https://{hostname}/db/{schema}/jahrgaenge/delete/multiple
	 *
	 * Entfernt mehrere Jahrgänge. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Jahrgängen hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Jahrgänge wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<JahrgangsDaten>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Jahrgang zu bearbeiten.
	 *   Code 404: Ein Jahrgang oder mehrere Jahrgänge nicht vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Jahrgänge wurde erfolgreich entfernt.
	 */
	public async deleteJahrgaenge(data : List<number>, schema : string) : Promise<List<JahrgangsDaten>> {
		const path = "/db/{schema}/jahrgaenge/delete/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<JahrgangsDaten>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(JahrgangsDaten.transpilerFromJSON(text)); });
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
	 *     - Rückgabe-Typ: List<KAOAAnschlussoptionenKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen für den KAoA-Katalog Anschlussoptionen
	 */
	public async getKatalogKAoAAnschlussoptionen(schema : string) : Promise<List<KAOAAnschlussoptionenKatalogEintrag>> {
		const path = "/db/{schema}/kaoa/allgemein/anschlussoptionen"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KAOAAnschlussoptionenKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KAOAAnschlussoptionenKatalogEintrag.transpilerFromJSON(text)); });
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
	 *     - Rückgabe-Typ: List<KAOABerufsfeldKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen für den KAoA-Katalog Berufsfelder
	 */
	public async getKatalogKAoABerufsfelder(schema : string) : Promise<List<KAOABerufsfeldKatalogEintrag>> {
		const path = "/db/{schema}/kaoa/allgemein/berufsfelder"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KAOABerufsfeldKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KAOABerufsfeldKatalogEintrag.transpilerFromJSON(text)); });
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
	 *     - Rückgabe-Typ: List<KAOAEbene4KatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen für den KAoA-Katalog Einträge der SBO Ebene 4
	 */
	public async getKatalogKAoAEbene4(schema : string) : Promise<List<KAOAEbene4KatalogEintrag>> {
		const path = "/db/{schema}/kaoa/allgemein/ebene4"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KAOAEbene4KatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KAOAEbene4KatalogEintrag.transpilerFromJSON(text)); });
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
	 *     - Rückgabe-Typ: List<KAOAKategorieKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen für den KAoA-Katalog Kategorien
	 */
	public async getKatalogKAoAKategorien(schema : string) : Promise<List<KAOAKategorieKatalogEintrag>> {
		const path = "/db/{schema}/kaoa/allgemein/kategorien"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KAOAKategorieKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KAOAKategorieKatalogEintrag.transpilerFromJSON(text)); });
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
	 *     - Rückgabe-Typ: List<KAOAMerkmalKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen für den KAoA-Katalog Merkmale
	 */
	public async getKatalogKAoAMerkmale(schema : string) : Promise<List<KAOAMerkmalKatalogEintrag>> {
		const path = "/db/{schema}/kaoa/allgemein/merkmale"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KAOAMerkmalKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KAOAMerkmalKatalogEintrag.transpilerFromJSON(text)); });
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
	 *     - Rückgabe-Typ: List<KAOAZusatzmerkmalKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen für den KAoA-Katalog Zusatzmerkmale
	 */
	public async getKatalogKAoAZusatzmerkmale(schema : string) : Promise<List<KAOAZusatzmerkmalKatalogEintrag>> {
		const path = "/db/{schema}/kaoa/allgemein/zusatzmerkmale"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KAOAZusatzmerkmalKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KAOAZusatzmerkmalKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKindergaerten für den Zugriff auf die URL https://{hostname}/db/{schema}/kindergaerten
	 *
	 * Gibt die Kindergärten zurück, insofern der SVWS-Benutzer die erforderliche Berechtigung besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Kindergarten>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen
	 */
	public async getKindergaerten(schema : string) : Promise<List<Kindergarten>> {
		const path = "/db/{schema}/kindergaerten"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<Kindergarten>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(Kindergarten.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der DELETE-Methode deleteKindergaerten für den Zugriff auf die URL https://{hostname}/db/{schema}/kindergaerten/delete/multiple
	 *
	 * Entfernt mehrere Kindergärten, insofern, die notwendigen Berechtigungen vorhanden sind.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Kindergärten wurden erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SimpleOperationResponse>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.
	 *   Code 404: Kindergärten nicht vorhanden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Kindergärten wurden erfolgreich entfernt.
	 */
	public async deleteKindergaerten(data : List<number>, schema : string) : Promise<List<SimpleOperationResponse>> {
		const path = "/db/{schema}/kindergaerten/delete/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SimpleOperationResponse>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SimpleOperationResponse.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der PATCH-Methode patchKindergarten für den Zugriff auf die URL https://{hostname}/db/{schema}/kindergarten/{id : \d+}
	 *
	 * Patched den Kindergarten mit der angegebenen ID, insofern die notwendigen Berechtigungen vorliegen.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Kindergarten-Daten integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Kindergarten-Daten zu ändern.
	 *   Code 404: Kein Kindergarten mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<Kindergarten>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchKindergarten(data : Partial<Kindergarten>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/kindergarten/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = Kindergarten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der POST-Methode addKindergarten für den Zugriff auf die URL https://{hostname}/db/{schema}/kindergarten/create
	 *
	 * Erstellt einen neuen Kindergarten, insofern die notwendigen Berechtigungen vorliegen
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Kindergarten wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Kindergarten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Telefonart anzulegen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<Kindergarten>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Kindergarten wurde erfolgreich angelegt.
	 */
	public async addKindergarten(data : Partial<Kindergarten>, schema : string) : Promise<Kindergarten> {
		const path = "/db/{schema}/kindergarten/create"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = Kindergarten.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return Kindergarten.transpilerFromJSON(text);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return KlassenDaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchKlasse für den Zugriff auf die URL https://{hostname}/db/{schema}/klassen/{id : \d+}
	 *
	 * Passt die Daten der Klasse mit der angebenen ID an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Klassendaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 404: Kein Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<KlassenDaten>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchKlasse(data : Partial<KlassenDaten>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/klassen/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = KlassenDaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getKlassenFuerAbschnitt für den Zugriff auf die URL https://{hostname}/db/{schema}/klassen/abschnitt/{abschnitt : \d+}
	 *
	 * Erstellt eine Liste aller in der Datenbank vorhanden Klassen unter Angabe der ID, des Kürzels, der Parallelität, der Kürzel des Klassenlehrers und des zweiten Klassenlehrers, einer Sortierreihenfolge und ob sie in der Anwendung sichtbar sein sollen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Klassendaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Klassen-Listen-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<KlassenDaten>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Klassendaten anzusehen.
	 *   Code 404: Keine Klassen-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abschnitt - der Pfad-Parameter abschnitt
	 *
	 * @returns Eine Liste von Klassen-Listen-Einträgen
	 */
	public async getKlassenFuerAbschnitt(schema : string, abschnitt : number) : Promise<List<KlassenDaten>> {
		const path = "/db/{schema}/klassen/abschnitt/{abschnitt : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abschnitt\s*(:[^{}]+({[^{}]+})*)?}/g, abschnitt.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KlassenDaten>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KlassenDaten.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode setKlassenSortierungFuerAbschnitt für den Zugriff auf die URL https://{hostname}/db/{schema}/klassen/abschnitt/{abschnitt : \d+}/sortierung/setdefault
	 *
	 * Setzte eine Default-Sortierung für die Klassen anhand der Sortierung der Jahrgänge und der Parallelität der Klassen.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Anpassen von Klassendaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die Default-Sortierung wurde erfolgreich gesetzt.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Klassendaten anzupassen.
	 *   Code 404: Keine Jahrgangs- oder Klassen-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abschnitt - der Pfad-Parameter abschnitt
	 */
	public async setKlassenSortierungFuerAbschnitt(schema : string, abschnitt : number) : Promise<void> {
		const path = "/db/{schema}/klassen/abschnitt/{abschnitt : \\d+}/sortierung/setdefault"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abschnitt\s*(:[^{}]+({[^{}]+})*)?}/g, abschnitt.toString());
		await super.postJSON(path, null);
		return;
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KlassenartKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KlassenartKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode addKlasse für den Zugriff auf die URL https://{hostname}/db/{schema}/klassen/create
	 *
	 * Erstellt eine neue Klasse und gibt die zugehörigen Daten zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Klasse besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Die Klasse wurde erfolgreich erstellt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: KlassenDaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Klasse anzulegen.
	 *   Code 404: Benötigte Daten wurden nicht gefunden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<KlassenDaten>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Klasse wurde erfolgreich erstellt.
	 */
	public async addKlasse(data : Partial<KlassenDaten>, schema : string) : Promise<KlassenDaten> {
		const path = "/db/{schema}/klassen/create"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = KlassenDaten.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return KlassenDaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der DELETE-Methode deleteKlassen für den Zugriff auf die URL https://{hostname}/db/{schema}/klassen/delete/multiple
	 *
	 * Entfernt mehrere Klassen. Dabei wird geprüft, ob alle Vorbedingungen zum Entfernender Klassen erfüllt sind und der SVWS-Benutzer die notwendige Berechtigung hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Lösch-Operationen wurden ausgeführt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SimpleOperationResponse>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Klassen zu entfernen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Lösch-Operationen wurden ausgeführt.
	 */
	public async deleteKlassen(data : List<number>, schema : string) : Promise<List<SimpleOperationResponse>> {
		const path = "/db/{schema}/klassen/delete/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SimpleOperationResponse>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SimpleOperationResponse.transpilerFromJSON(text)); });
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
	 *     - Rückgabe-Typ: List<KursDaten>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Kursdaten anzusehen.
	 *   Code 404: Keine Kurs-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Kurs-Listen-Einträgen
	 */
	public async getKurse(schema : string) : Promise<List<KursDaten>> {
		const path = "/db/{schema}/kurse/"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KursDaten>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KursDaten.transpilerFromJSON(text)); });
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return KursDaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchKurs für den Zugriff auf die URL https://{hostname}/db/{schema}/kurse/{id : \d+}
	 *
	 * Passt die Daten des Kurses mit der angebenen ID an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Kursdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 404: Kein Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<KursDaten>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchKurs(data : Partial<KursDaten>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/kurse/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = KursDaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getKurseFuerAbschnitt für den Zugriff auf die URL https://{hostname}/db/{schema}/kurse/abschnitt/{abschnitt : \d+}
	 *
	 * Erstellt eine Liste aller in der Datenbank vorhanden Kurse eines Schuljahresabschnittes unter Angabe der ID, des Kürzels, der Parallelität, der Kürzel des Klassenlehrers und des zweiten Klassenlehrers, einer Sortierreihenfolge und ob sie in der Anwendung sichtbar sein sollen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Klassendaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Kurs-Listen-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<KursDaten>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Kursdaten anzusehen.
	 *   Code 404: Keine Kurs-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abschnitt - der Pfad-Parameter abschnitt
	 *
	 * @returns Eine Liste von Kurs-Listen-Einträgen
	 */
	public async getKurseFuerAbschnitt(schema : string, abschnitt : number) : Promise<List<KursDaten>> {
		const path = "/db/{schema}/kurse/abschnitt/{abschnitt : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abschnitt\s*(:[^{}]+({[^{}]+})*)?}/g, abschnitt.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<KursDaten>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(KursDaten.transpilerFromJSON(text)); });
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
	 *     - Rückgabe-Typ: List<ZulaessigeKursartKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Kursart-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Kursart-Katalog-Einträgen
	 */
	public async getKatalogKursarten(schema : string) : Promise<List<ZulaessigeKursartKatalogEintrag>> {
		const path = "/db/{schema}/kurse/allgemein/kursarten"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<ZulaessigeKursartKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(ZulaessigeKursartKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode addKurs für den Zugriff auf die URL https://{hostname}/db/{schema}/kurse/create
	 *
	 * Erstellt einen neuen Kurs und gibt die zugehörigen Daten zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Kurses besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Der Kurs wurde erfolgreich erstellt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: KursDaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Kurs anzulegen.
	 *   Code 404: Der Schuljahresabschnitt, das Fach oder der Lehrer wurde nicht gefunden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<KursDaten>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Kurs wurde erfolgreich erstellt.
	 */
	public async addKurs(data : Partial<KursDaten>, schema : string) : Promise<KursDaten> {
		const path = "/db/{schema}/kurse/create"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = KursDaten.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return KursDaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der DELETE-Methode deleteKurse für den Zugriff auf die URL https://{hostname}/db/{schema}/kurse/delete/multiple
	 *
	 * Entfernt mehrere Kurse. Dabei wird geprüft, ob alle Vorbedingungen zum Entfernender Kurse erfüllt sind und der SVWS-Benutzer die notwendige Berechtigung hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Lösch-Operationen wurden ausgeführt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SimpleOperationResponse>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Kurse zu entfernen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Lösch-Operationen wurden ausgeführt.
	 */
	public async deleteKurse(data : List<number>, schema : string) : Promise<List<SimpleOperationResponse>> {
		const path = "/db/{schema}/kurse/delete/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SimpleOperationResponse>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SimpleOperationResponse.transpilerFromJSON(text)); });
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerListeEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerListeEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLehrerLernplattformen für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/{id : \d+}/lernplattformen
	 *
	 * Liest die Lernplattformen des Lehrers zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Lernplattformen des Lehrers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<LehrerLernplattform>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Lehrerdaten anzusehen.
	 *   Code 404: Keine Lernplattform für den Lehrer mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Lernplattformen des Lehrers
	 */
	public async getLehrerLernplattformen(schema : string, id : number) : Promise<List<LehrerLernplattform>> {
		const path = "/db/{schema}/lehrer/{id : \\d+}/lernplattformen"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerLernplattform>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerLernplattform.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der PATCH-Methode patchLehrerLernplattform für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/{id : \d+}/lernplattformen/{idLernplattform : \d+}
	 *
	 * Passt die Einwilligung zu der angegebenen Lehrer- und Einwilligungsart-ID an und speichert das Ergebnis in der Datenbank.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrer-Einwilligungen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Lernplattform integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Lernplattform der Lehrer zu ändern.
	 *   Code 404: Kein Lehrer oder keine Lernplattform der angegebenen Art gefunden.
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde. (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<LehrerLernplattform>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 * @param {number} idLernplattform - der Pfad-Parameter idLernplattform
	 */
	public async patchLehrerLernplattform(data : Partial<LehrerLernplattform>, schema : string, id : number, idLernplattform : number) : Promise<void> {
		const path = "/db/{schema}/lehrer/{id : \\d+}/lernplattformen/{idLernplattform : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString())
			.replace(/{idLernplattform\s*(:[^{}]+({[^{}]+})*)?}/g, idLernplattform.toString());
		const body : string = LehrerLernplattform.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = LehrerStammdaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getLehrerEinwilligungen für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/{idLehrer : \d+}/einwilligungen
	 *
	 * Liest die Einwilligungen des Lehrers zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Einwilligungen des Lehrers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<LehrerEinwilligung>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Lehrerdaten anzusehen.
	 *   Code 404: Keine Einwilligungen für den Lehrer mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} idLehrer - der Pfad-Parameter idLehrer
	 *
	 * @returns Die Einwilligungen des Lehrers
	 */
	public async getLehrerEinwilligungen(schema : string, idLehrer : number) : Promise<List<LehrerEinwilligung>> {
		const path = "/db/{schema}/lehrer/{idLehrer : \\d+}/einwilligungen"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{idLehrer\s*(:[^{}]+({[^{}]+})*)?}/g, idLehrer.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerEinwilligung>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerEinwilligung.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der PATCH-Methode patchLehrerEinwilligung für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/{idLehrer : \d+}/einwilligungen/{idEinwilligungsart : \d+}
	 *
	 * Passt die Einwilligung zu der angegebenen Lehrer- und Einwilligungsart-ID an und speichert das Ergebnis in der Datenbank.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrereinwilligungen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Lehrereinwilligung integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Einwilligungen der Lehrer zu ändern.
	 *   Code 404: Kein Lehrer oder keine Einwilligung der angegebenen Art gefunden.
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde. (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<LehrerEinwilligung>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} idLehrer - der Pfad-Parameter idLehrer
	 * @param {number} idEinwilligungsart - der Pfad-Parameter idEinwilligungsart
	 */
	public async patchLehrerEinwilligung(data : Partial<LehrerEinwilligung>, schema : string, idLehrer : number, idEinwilligungsart : number) : Promise<void> {
		const path = "/db/{schema}/lehrer/{idLehrer : \\d+}/einwilligungen/{idEinwilligungsart : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{idLehrer\s*(:[^{}]+({[^{}]+})*)?}/g, idLehrer.toString())
			.replace(/{idEinwilligungsart\s*(:[^{}]+({[^{}]+})*)?}/g, idEinwilligungsart.toString());
		const body : string = LehrerEinwilligung.transpilerToJSONPatch(data);
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
	 *     - Rückgabe-Typ: List<LehrerAbgangsgrundKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Lehrerabgangsgrund-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Lehrerabgangsgrund-Katalog-Einträgen
	 */
	public async getLehrerAbgangsgruende(schema : string) : Promise<List<LehrerAbgangsgrundKatalogEintrag>> {
		const path = "/db/{schema}/lehrer/allgemein/abgangsgruende"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerAbgangsgrundKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerAbgangsgrundKatalogEintrag.transpilerFromJSON(text)); });
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
	 *     - Rückgabe-Typ: List<LehrerAnrechnungsgrundKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Einträgen für Gründe von Anrechnungsstunden von Lehrern
	 */
	public async getLehrerAnrechnungsgruende(schema : string) : Promise<List<LehrerAnrechnungsgrundKatalogEintrag>> {
		const path = "/db/{schema}/lehrer/allgemein/anrechnungsgruende"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerAnrechnungsgrundKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerAnrechnungsgrundKatalogEintrag.transpilerFromJSON(text)); });
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
	 *     - Rückgabe-Typ: List<LehrerBeschaeftigungsartKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Beschäftigungsart-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Beschäftigungsart-Katalog-Einträgen
	 */
	public async getLehrerBeschaeftigungsarten(schema : string) : Promise<List<LehrerBeschaeftigungsartKatalogEintrag>> {
		const path = "/db/{schema}/lehrer/allgemein/beschaeftigungsarten"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerBeschaeftigungsartKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerBeschaeftigungsartKatalogEintrag.transpilerFromJSON(text)); });
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
	 *     - Rückgabe-Typ: List<LehrerEinsatzstatusKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Einsatzstatus-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Einsatzstatus-Katalog-Einträgen
	 */
	public async getLehrerEinsatzstatus(schema : string) : Promise<List<LehrerEinsatzstatusKatalogEintrag>> {
		const path = "/db/{schema}/lehrer/allgemein/einsatzstatus"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerEinsatzstatusKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerEinsatzstatusKatalogEintrag.transpilerFromJSON(text)); });
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
	 *     - Rückgabe-Typ: List<LehrerFachrichtungKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Fachrichtungs-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Fachrichtungens-Katalog-Einträgen
	 */
	public async getLehrerFachrichtungen(schema : string) : Promise<List<LehrerFachrichtungKatalogEintrag>> {
		const path = "/db/{schema}/lehrer/allgemein/fachrichtungen"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerFachrichtungKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerFachrichtungKatalogEintrag.transpilerFromJSON(text)); });
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
	 *     - Rückgabe-Typ: List<LehrerFachrichtungAnerkennungKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Anerkennungs-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Anerkennungs-Katalog-Einträgen
	 */
	public async getLehrerFachrichtungAnerkennungen(schema : string) : Promise<List<LehrerFachrichtungAnerkennungKatalogEintrag>> {
		const path = "/db/{schema}/lehrer/allgemein/fachrichtungen_anerkennungen"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerFachrichtungAnerkennungKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerFachrichtungAnerkennungKatalogEintrag.transpilerFromJSON(text)); });
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
	 *     - Rückgabe-Typ: List<LehrerLehramtKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Lehramt-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Lehramt-Katalog-Einträgen
	 */
	public async getLehrerLehraemter(schema : string) : Promise<List<LehrerLehramtKatalogEintrag>> {
		const path = "/db/{schema}/lehrer/allgemein/lehraemter"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerLehramtKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerLehramtKatalogEintrag.transpilerFromJSON(text)); });
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
	 *     - Rückgabe-Typ: List<LehrerLehramtAnerkennungKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Anerkennungs-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Anerkennungs-Katalog-Einträgen
	 */
	public async getLehrerLehramtAnerkennungen(schema : string) : Promise<List<LehrerLehramtAnerkennungKatalogEintrag>> {
		const path = "/db/{schema}/lehrer/allgemein/lehraemter_anerkennungen"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerLehramtAnerkennungKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerLehramtAnerkennungKatalogEintrag.transpilerFromJSON(text)); });
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
	 *     - Rückgabe-Typ: List<LehrerLehrbefaehigungKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Lehrbefähigung-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Lehrbefähigung-Katalog-Einträgen
	 */
	public async getLehrerLehrbefaehigungen(schema : string) : Promise<List<LehrerLehrbefaehigungKatalogEintrag>> {
		const path = "/db/{schema}/lehrer/allgemein/lehrbefaehigungen"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerLehrbefaehigungKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerLehrbefaehigungKatalogEintrag.transpilerFromJSON(text)); });
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
	 *     - Rückgabe-Typ: List<LehrerLehrbefaehigungAnerkennungKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Anerkennungs-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Einsatzstatus-Katalog-Einträgen
	 */
	public async getLehrerLehrbefaehigungenAnerkennungen(schema : string) : Promise<List<LehrerLehrbefaehigungAnerkennungKatalogEintrag>> {
		const path = "/db/{schema}/lehrer/allgemein/lehrbefaehigungen_anerkennungen"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerLehrbefaehigungAnerkennungKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerLehrbefaehigungAnerkennungKatalogEintrag.transpilerFromJSON(text)); });
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
	 *     - Rückgabe-Typ: List<LehrerMehrleistungsartKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Mehrleistungsart-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Mehrleistungsart-Katalog-Einträgen
	 */
	public async getLehrerMehrleistungsarten(schema : string) : Promise<List<LehrerMehrleistungsartKatalogEintrag>> {
		const path = "/db/{schema}/lehrer/allgemein/mehrleistungsarten"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerMehrleistungsartKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerMehrleistungsartKatalogEintrag.transpilerFromJSON(text)); });
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
	 *     - Rückgabe-Typ: List<LehrerMinderleistungsartKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Minderleistungsart-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Minderleistungsart-Katalog-Einträgen
	 */
	public async getLehrerMinderleistungsarten(schema : string) : Promise<List<LehrerMinderleistungsartKatalogEintrag>> {
		const path = "/db/{schema}/lehrer/allgemein/minderleistungsarten"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerMinderleistungsartKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerMinderleistungsartKatalogEintrag.transpilerFromJSON(text)); });
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
	 *     - Rückgabe-Typ: List<LehrerRechtsverhaeltnisKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Rechtsverhältnis-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Rechtsverhältnis-Katalog-Einträgen
	 */
	public async getLehrerRechtsverhaeltnisse(schema : string) : Promise<List<LehrerRechtsverhaeltnisKatalogEintrag>> {
		const path = "/db/{schema}/lehrer/allgemein/rechtsverhaeltnisse"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerRechtsverhaeltnisKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerRechtsverhaeltnisKatalogEintrag.transpilerFromJSON(text)); });
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
	 *     - Rückgabe-Typ: List<LehrerZugangsgrundKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Lehrerzugangsgrund-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Lehrerzugangsgrund-Katalog-Einträgen
	 */
	public async getLehrerZugangsgruende(schema : string) : Promise<List<LehrerZugangsgrundKatalogEintrag>> {
		const path = "/db/{schema}/lehrer/allgemein/zugangsgruende"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerZugangsgrundKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerZugangsgrundKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode addLehrerStammdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/create
	 *
	 * Erstellt neue LehrerStammdaten und gibt das erstellte Objekt zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen neuer LehrerStammdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Die LehrerStammdaten wurden erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: LehrerStammdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um LehrerStammdaten anzulegen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<LehrerStammdaten>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die LehrerStammdaten wurden erfolgreich hinzugefügt.
	 */
	public async addLehrerStammdaten(data : Partial<LehrerStammdaten>, schema : string) : Promise<LehrerStammdaten> {
		const path = "/db/{schema}/lehrer/create"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = LehrerStammdaten.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return LehrerStammdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der DELETE-Methode deleteLehrer für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/delete/multiple
	 *
	 * Entfernt mehrere Lehrer. Dabei wird geprüft, ob alle Vorbedingungen zum Entfernender Lehrer erfüllt sind und der SVWS-Benutzer die notwendige Berechtigung hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Lösch-Operationen wurden ausgeführt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SimpleOperationResponse>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Lehrer zu entfernen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Lösch-Operationen wurden ausgeführt.
	 */
	public async deleteLehrer(data : List<number>, schema : string) : Promise<List<SimpleOperationResponse>> {
		const path = "/db/{schema}/lehrer/delete/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SimpleOperationResponse>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SimpleOperationResponse.transpilerFromJSON(text)); });
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
	 *     - Rückgabe-Typ: List<LehrerLeitungsfunktionKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Lehrerleitungsfunktion-Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Lehrerleitungsfunktion-Katalog-Einträgen
	 */
	public async getLehrerLeitungsfunktionen(schema : string) : Promise<List<LehrerLeitungsfunktionKatalogEintrag>> {
		const path = "/db/{schema}/lehrer/leitungsfunktionen"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<LehrerLeitungsfunktionKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(LehrerLeitungsfunktionKatalogEintrag.transpilerFromJSON(text)); });
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
	 *   Code 404: Keine Personalabschnittsdaten mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<LehrerPersonalabschnittsdaten>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchLehrerPersonalabschnittsdaten(data : Partial<LehrerPersonalabschnittsdaten>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/lehrer/personalabschnittsdaten/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = LehrerPersonalabschnittsdaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getLehrerPersonalabschnittsdatenAllgemeineAnrechnung für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/personalabschnittsdaten/anrechnung/{id : \d+}
	 *
	 * Liest die allgemeine Anrechnung zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerpersonaldaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die allgemeine Anrechnung
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: LehrerPersonalabschnittsdatenAnrechnungsstunden
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Lehrer-Personaldaten anzusehen.
	 *   Code 404: Keine allgemeine Anrechnung mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die allgemeine Anrechnung
	 */
	public async getLehrerPersonalabschnittsdatenAllgemeineAnrechnung(schema : string, id : number) : Promise<LehrerPersonalabschnittsdatenAnrechnungsstunden> {
		const path = "/db/{schema}/lehrer/personalabschnittsdaten/anrechnung/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der DELETE-Methode deleteLehrerPersonalabschnittsdatenAllgemeineAnrechnung für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/personalabschnittsdaten/anrechnung/{id : \d+}
	 *
	 * Entfernt die allgemeine Anrechnung zu der angegebenen ID an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen der allgemeinen Anrechnun hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die allgemeine Anrechnung wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: LehrerPersonalabschnittsdatenAnrechnungsstunden
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine allgemeine Anrechnung zu löschen.
	 *   Code 404: Keine allgemeine Anrechnung mit der angegebenen ID gefunden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die allgemeine Anrechnung wurde erfolgreich entfernt.
	 */
	public async deleteLehrerPersonalabschnittsdatenAllgemeineAnrechnung(schema : string, id : number) : Promise<LehrerPersonalabschnittsdatenAnrechnungsstunden> {
		const path = "/db/{schema}/lehrer/personalabschnittsdaten/anrechnung/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchLehrerPersonalabschnittsdatenAllgemeineAnrechnung für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/personalabschnittsdaten/anrechnung/{id : \d+}
	 *
	 * Passt die allgemeine Anrechnung zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrer-Personalabschnittsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die allgemeine Anrechnung integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Lehrer-Personaldaten zu ändern.
	 *   Code 404: Keine allgemeine Anrechnung mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchLehrerPersonalabschnittsdatenAllgemeineAnrechnung(data : Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/lehrer/personalabschnittsdaten/anrechnung/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der POST-Methode addLehrerPersonalabschnittsdatenAllgemeineAnrechnung für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/personalabschnittsdaten/anrechnung/add
	 *
	 * Erstellt einen neuen Datensatz für für eine Mehrleistung in den Personalabschnittsdaten eines Lehrers und gibt das zugehörige Objekt zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen neuer Entlastungsstunden besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Die allgemeine Anrechnung wurde erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: LehrerPersonalabschnittsdatenAnrechnungsstunden
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine allgemeine Anrechnung anzulegen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die allgemeine Anrechnung wurde erfolgreich hinzugefügt.
	 */
	public async addLehrerPersonalabschnittsdatenAllgemeineAnrechnung(data : Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>, schema : string) : Promise<LehrerPersonalabschnittsdatenAnrechnungsstunden> {
		const path = "/db/{schema}/lehrer/personalabschnittsdaten/anrechnung/add"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getLehrerPersonalabschnittsdatenMehrleistung für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/personalabschnittsdaten/mehrleistung/{id : \d+}
	 *
	 * Liest die Mehrleistung zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerpersonaldaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Mehrleistung
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: LehrerPersonalabschnittsdatenAnrechnungsstunden
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Lehrer-Personaldaten anzusehen.
	 *   Code 404: Keine Mehrleistung mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Mehrleistung
	 */
	public async getLehrerPersonalabschnittsdatenMehrleistung(schema : string, id : number) : Promise<LehrerPersonalabschnittsdatenAnrechnungsstunden> {
		const path = "/db/{schema}/lehrer/personalabschnittsdaten/mehrleistung/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der DELETE-Methode deleteLehrerPersonalabschnittsdatenMehrleistung für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/personalabschnittsdaten/mehrleistung/{id : \d+}
	 *
	 * Entfernt die Mehrleistung zu der angegebenen ID an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen der Mehrleistung hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Mehrleistung wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: LehrerPersonalabschnittsdatenAnrechnungsstunden
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Mehrleistung zu löschen.
	 *   Code 404: Keine Mehrleistung mit der angegebenen ID gefunden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Mehrleistung wurde erfolgreich entfernt.
	 */
	public async deleteLehrerPersonalabschnittsdatenMehrleistung(schema : string, id : number) : Promise<LehrerPersonalabschnittsdatenAnrechnungsstunden> {
		const path = "/db/{schema}/lehrer/personalabschnittsdaten/mehrleistung/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchLehrerPersonalabschnittsdatenMehrleistung für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/personalabschnittsdaten/mehrleistung/{id : \d+}
	 *
	 * Passt die Mehrleistung zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrer-Personalabschnittsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Mehrleistung integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Lehrer-Personaldaten zu ändern.
	 *   Code 404: Keine Mehrleistung mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchLehrerPersonalabschnittsdatenMehrleistung(data : Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/lehrer/personalabschnittsdaten/mehrleistung/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der POST-Methode addLehrerPersonalabschnittsdatenMehrleistung für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/personalabschnittsdaten/mehrleistung/add
	 *
	 * Erstellt einen neuen Datensatz für für eine Mehrleistung in den Personalabschnittsdaten eines Lehrers und gibt das zugehörige Objekt zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen neuer Entlastungsstunden besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Die Mehrleistung wurde erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: LehrerPersonalabschnittsdatenAnrechnungsstunden
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Mehrleistungen anzulegen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Mehrleistung wurde erfolgreich hinzugefügt.
	 */
	public async addLehrerPersonalabschnittsdatenMehrleistung(data : Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>, schema : string) : Promise<LehrerPersonalabschnittsdatenAnrechnungsstunden> {
		const path = "/db/{schema}/lehrer/personalabschnittsdaten/mehrleistung/add"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getLehrerPersonalabschnittsdatenMinderleistung für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/personalabschnittsdaten/minderleistung/{id : \d+}
	 *
	 * Liest die Minderleistung zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Lehrerpersonaldaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Minderleistung
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: LehrerPersonalabschnittsdatenAnrechnungsstunden
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Lehrer-Personaldaten anzusehen.
	 *   Code 404: Keine Minderleistung mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Minderleistung
	 */
	public async getLehrerPersonalabschnittsdatenMinderleistung(schema : string, id : number) : Promise<LehrerPersonalabschnittsdatenAnrechnungsstunden> {
		const path = "/db/{schema}/lehrer/personalabschnittsdaten/minderleistung/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der DELETE-Methode deleteLehrerPersonalabschnittsdatenMinderleistung für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/personalabschnittsdaten/minderleistung/{id : \d+}
	 *
	 * Entfernt die Minderleistung zu der angegebenen ID an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen der Minderleistung hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Minderleistung wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: LehrerPersonalabschnittsdatenAnrechnungsstunden
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Minderleistung zu löschen.
	 *   Code 404: Keine Minderleistung mit der angegebenen ID gefunden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Minderleistung wurde erfolgreich entfernt.
	 */
	public async deleteLehrerPersonalabschnittsdatenMinderleistung(schema : string, id : number) : Promise<LehrerPersonalabschnittsdatenAnrechnungsstunden> {
		const path = "/db/{schema}/lehrer/personalabschnittsdaten/minderleistung/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchLehrerPersonalabschnittsdatenMinderleistung für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/personalabschnittsdaten/minderleistung/{id : \d+}
	 *
	 * Passt die Minderleistung zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Lehrer-Personalabschnittsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Minderleistung integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Lehrer-Personaldaten zu ändern.
	 *   Code 404: Keine Minderleistung mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchLehrerPersonalabschnittsdatenMinderleistung(data : Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/lehrer/personalabschnittsdaten/minderleistung/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der POST-Methode addLehrerPersonalabschnittsdatenMinderleistung für den Zugriff auf die URL https://{hostname}/db/{schema}/lehrer/personalabschnittsdaten/minderleistung/add
	 *
	 * Erstellt einen neuen Datensatz für für eine Minderleistung in den Personalabschnittsdaten eines Lehrers und gibt das zugehörige Objekt zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen neuer Entlastungsstunden besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Die Minderleistung wurde erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: LehrerPersonalabschnittsdatenAnrechnungsstunden
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Minderleistungen anzulegen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Minderleistung wurde erfolgreich hinzugefügt.
	 */
	public async addLehrerPersonalabschnittsdatenMinderleistung(data : Partial<LehrerPersonalabschnittsdatenAnrechnungsstunden>, schema : string) : Promise<LehrerPersonalabschnittsdatenAnrechnungsstunden> {
		const path = "/db/{schema}/lehrer/personalabschnittsdaten/minderleistung/add"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return LehrerPersonalabschnittsdatenAnrechnungsstunden.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getMerkmale für den Zugriff auf die URL https://{hostname}/db/{schema}/merkmale
	 *
	 * Gibt die Merkmale zurück, insofern der SVWS-Benutzer die erforderliche Berechtigung besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Merkmalen.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Merkmal>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Merkmale gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Merkmalen.
	 */
	public async getMerkmale(schema : string) : Promise<List<Merkmal>> {
		const path = "/db/{schema}/merkmale"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<Merkmal>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(Merkmal.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der PATCH-Methode patchMerkmal für den Zugriff auf die URL https://{hostname}/db/{schema}/merkmale/{id : \d+}
	 *
	 * Patched das Merkmal mit der angegebenen ID, insofern die notwendigen Berechtigungen vorliegen.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 404: Kein Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z. B. beim Datenbankzugriff)
	 *
	 * @param {Partial<Merkmal>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchMerkmal(data : Partial<Merkmal>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/merkmale/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = Merkmal.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der POST-Methode addMerkmal für den Zugriff auf die URL https://{hostname}/db/{schema}/merkmale/create
	 *
	 * Erstellt ein neues Merkmal, insofern die notwendigen Berechtigungen vorliegen
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Das Merkmal wurden erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Merkmal
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Merkmale anzulegen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<Merkmal>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Das Merkmal wurden erfolgreich hinzugefügt.
	 */
	public async addMerkmal(data : Partial<Merkmal>, schema : string) : Promise<Merkmal> {
		const path = "/db/{schema}/merkmale/create"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = Merkmal.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return Merkmal.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der DELETE-Methode deleteMerkmale für den Zugriff auf die URL https://{hostname}/db/{schema}/merkmale/delete/multiple
	 *
	 * Entfernt mehrere Merkmale, insofern die notwendigen Berechtigungen vorhanden sind.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Lösch-Operationen wurden ausgeführt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SimpleOperationResponse>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Merkmale zu entfernen.
	 *   Code 404: Merkmale nicht vorhanden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Lösch-Operationen wurden ausgeführt.
	 */
	public async deleteMerkmale(data : List<number>, schema : string) : Promise<List<SimpleOperationResponse>> {
		const path = "/db/{schema}/merkmale/delete/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SimpleOperationResponse>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SimpleOperationResponse.transpilerFromJSON(text)); });
		return ret;
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{schulnummer\s*(:[^{}]+({[^{}]+})*)?}/g, schulnummer.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{schulnummer\s*(:[^{}]+({[^{}]+})*)?}/g, schulnummer.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{schulnummer\s*(:[^{}]+({[^{}]+})*)?}/g, schulnummer.toString());
		const body : string = DatenbankVerbindungsdaten.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SimpleOperationResponse.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getOAuthClientSecrets für den Zugriff auf die URL https://{hostname}/db/{schema}/oauth/secrets
	 *
	 * Gibt die OAuth2-Client-Secrets der Schule zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen der OAuth2-Client-Secrets besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste der OAuth2-Client-Secrets der Schule.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<OAuth2ClientConnection>
	 *   Code 403: Der SVWS-Benutzer hat keine Berechtigung zum Ansehen der OAuth2-Client-Secrets.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste der OAuth2-Client-Secrets der Schule.
	 */
	public async getOAuthClientSecrets(schema : string) : Promise<List<OAuth2ClientConnection>> {
		const path = "/db/{schema}/oauth/secrets"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<OAuth2ClientConnection>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(OAuth2ClientConnection.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der PATCH-Methode patchOAuthSecret für den Zugriff auf die URL https://{hostname}/db/{schema}/oauth/secrets/{id : \d+}
	 *
	 * Passt die OAuth2-Client-Secrets zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von OAuth2-Client-Secrets besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die OAuth2-Client-Secrets der Schule integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die OAuth2-Client-Secrets zu ändern.
	 *   Code 404: Kein OAuth2-Client-Secrets mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<OAuth2ClientConnection>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchOAuthSecret(data : Partial<OAuth2ClientConnection>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/oauth/secrets/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = OAuth2ClientConnection.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteOAuthSecret für den Zugriff auf die URL https://{hostname}/db/{schema}/oauth/secrets/{id : \d+}
	 *
	 * Entfernt ein OAuth2-Client-Secrets. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen von OAuth Client Secrets hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Das OAuth2-Client-Secrets wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: OAuth2ClientConnection
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um OAuth2-Client-Secrets zu entfernen.
	 *   Code 404: OAuth2-Client-Secrets nicht vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Das OAuth2-Client-Secrets wurde erfolgreich entfernt.
	 */
	public async deleteOAuthSecret(schema : string, id : number) : Promise<OAuth2ClientConnection> {
		const path = "/db/{schema}/oauth/secrets/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return OAuth2ClientConnection.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getOAuthClientSecret für den Zugriff auf die URL https://{hostname}/db/{schema}/oauth/secrets/{id : \d+}
	 *
	 * Gibt das OAuth2-Client-Secrets der Schule zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von OAuth2-Client-Secrets besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Das OAuth2-Client-Secrets der Schule
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: OAuth2ClientConnection
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die OAuth2-Client-Secrets anzusehen.
	 *   Code 404: Kein OAuth2-Client-Secrets mit der ID bei der Schule gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Das OAuth2-Client-Secrets der Schule
	 */
	public async getOAuthClientSecret(schema : string, id : number) : Promise<OAuth2ClientConnection> {
		const path = "/db/{schema}/oauth/secrets/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return OAuth2ClientConnection.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode addOAuthClientSecret für den Zugriff auf die URL https://{hostname}/db/{schema}/oauth/secrets/create
	 *
	 * Erstellt einen neuen Eintrag für die schulspezifischen OAuth2-Client-Secrets und gibt das zugehörige Objekt zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von OAuth2-Client-Secrets besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Der Eintrag wurde erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: OAuth2ClientConnection
	 *   Code 400: Der Eintrag enthält Fehler, bspw. eine invalide URL.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um ein OAuth2-Client-Secret für die Schule anzulegen.
	 *   Code 409: Es existiert bereits ein Eintrag für den gegebenen OAuth2-Server.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<OAuth2ClientConnection>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Eintrag wurde erfolgreich hinzugefügt.
	 */
	public async addOAuthClientSecret(data : Partial<OAuth2ClientConnection>, schema : string) : Promise<OAuth2ClientConnection> {
		const path = "/db/{schema}/oauth/secrets/create"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = OAuth2ClientConnection.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return OAuth2ClientConnection.transpilerFromJSON(text);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<OrtsteilKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(OrtsteilKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode pdfReport für den Zugriff auf die URL https://{hostname}/db/{schema}/reporting/ausgabe
	 *
	 * Erstellt die Wahlbogen für die Laufbahnplanung der gymnasialen Oberstufe zu den Schülern mit der angegebenen IDs als PDF-Datei. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Reports besitzt. Weitergehende Berechtigungen werden im Vorfeld der Reporterstellung überprüft.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Report mit den übergebenen Daten wurde erfolgreich erstellt.
	 *     - Mime-Type: application/pdf
	 *     - Rückgabe-Typ: ApiFile
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den geforderten Report zu erstellen.
	 *   Code 404: Kein Eintrag zu den übergebenen Daten gefunden.
	 *   Code 500: Es ist ein unbekannter Fehler aufgetreten.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SimpleOperationResponse
	 *
	 * @param {ReportingParameter} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Report mit den übergebenen Daten wurde erfolgreich erstellt.
	 */
	public async pdfReport(data : ReportingParameter, schema : string) : Promise<ApiFile> {
		const path = "/db/{schema}/reporting/ausgabe"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = ReportingParameter.transpilerToJSON(data);
		const result : ApiFile = await super.postJSONtoPDF(path, body);
		return result;
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString())
			.replace(/{abschnitt\s*(:[^{}]+({[^{}]+})*)?}/g, abschnitt.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<ErzieherStammdaten>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(ErzieherStammdaten.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getKAoAdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{id : \d+}/kaoa
	 *
	 * Liest die KAOADaten des Schülers zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die KAOADaten des Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SchuelerKAoADaten>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzusehen.
	 *   Code 404: Kein Schüler-KAoA-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die KAOADaten des Schülers
	 */
	public async getKAoAdaten(schema : string, id : number) : Promise<List<SchuelerKAoADaten>> {
		const path = "/db/{schema}/schueler/{id : \\d+}/kaoa"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchuelerKAoADaten>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchuelerKAoADaten.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der PATCH-Methode patchKAoADaten für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{id : \d+}/kaoa/{skid : \d+}
	 *
	 * Ändert einen SchuelerKAoADaten EintragDabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von SchülerKAoADaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Die KAOADaten des Schülers wurden erfolgreich gepatcht
	 *   Code 400: Fehler bei der Datenvalidierung
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzulegen.
	 *   Code 404: Kein Schüler-KAoA-Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<SchuelerKAoADaten>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 * @param {number} skid - der Pfad-Parameter skid
	 */
	public async patchKAoADaten(data : Partial<SchuelerKAoADaten>, schema : string, id : number, skid : number) : Promise<void> {
		const path = "/db/{schema}/schueler/{id : \\d+}/kaoa/{skid : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString())
			.replace(/{skid\s*(:[^{}]+({[^{}]+})*)?}/g, skid.toString());
		const body : string = SchuelerKAoADaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteKAoAdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{id : \d+}/kaoa/{skid : \d+}
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
	public async deleteKAoAdaten(schema : string, id : number, skid : number) : Promise<void> {
		const path = "/db/{schema}/schueler/{id : \\d+}/kaoa/{skid : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString())
			.replace(/{skid\s*(:[^{}]+({[^{}]+})*)?}/g, skid.toString());
		await super.deleteJSON(path, null);
		return;
	}


	/**
	 * Implementierung der POST-Methode addKAoAdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{id : \d+}/kaoa/create
	 *
	 * Erstellt einen neuen SchuelerKAoADaten EintragDabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von SchülerKAoADaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Die KAOADaten des Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SchuelerKAoADaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzulegen.
	 *   Code 404: Kein Schüler-KAoA-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {Partial<SchuelerKAoADaten>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die KAOADaten des Schülers
	 */
	public async addKAoAdaten(data : Partial<SchuelerKAoADaten>, schema : string, id : number) : Promise<SchuelerKAoADaten> {
		const path = "/db/{schema}/schueler/{id : \\d+}/kaoa/create"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = SchuelerKAoADaten.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SchuelerKAoADaten.transpilerFromJSON(text);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchuelerLernabschnittListeEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchuelerLernabschnittListeEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getSchuelerLernplattformen für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{id : \d+}/lernplattformen
	 *
	 * Liest die Lernplattformen des Schülers zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Lernplattformen des Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SchuelerLernplattform>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzusehen.
	 *   Code 404: Keine Lernplattform für den Schüler mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Lernplattformen des Schülers
	 */
	public async getSchuelerLernplattformen(schema : string, id : number) : Promise<List<SchuelerLernplattform>> {
		const path = "/db/{schema}/schueler/{id : \\d+}/lernplattformen"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchuelerLernplattform>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchuelerLernplattform.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der PATCH-Methode patchSchuelerLernplattform für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{id : \d+}/lernplattformen/{idLernplattform : \d+}
	 *
	 * Passt die Einwilligung zu der angegebenen Schüler- und Einwilligungsart-ID an und speichert das Ergebnis in der Datenbank.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Schüler-Einwilligungen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Lernplattform integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Lernplattform der Schüler zu ändern.
	 *   Code 404: Kein Schüler oder keine Lernplattform der angegebenen Art gefunden.
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde. (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<SchuelerLernplattform>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 * @param {number} idLernplattform - der Pfad-Parameter idLernplattform
	 */
	public async patchSchuelerLernplattform(data : Partial<SchuelerLernplattform>, schema : string, id : number, idLernplattform : number) : Promise<void> {
		const path = "/db/{schema}/schueler/{id : \\d+}/lernplattformen/{idLernplattform : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString())
			.replace(/{idLernplattform\s*(:[^{}]+({[^{}]+})*)?}/g, idLernplattform.toString());
		const body : string = SchuelerLernplattform.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
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
	 *   Code 404: Kein Schüler-Eintrag mit der angegebenen ID gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Schulbesuchsdaten des Schülers
	 */
	public async getSchuelerSchulbesuch(schema : string, id : number) : Promise<SchuelerSchulbesuchsdaten> {
		const path = "/db/{schema}/schueler/{id : \\d+}/schulbesuch"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString())
			.replace(/{sprache\s*(:[^{}]+({[^{}]+})*)?}/g, sprache);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString())
			.replace(/{sprache\s*(:[^{}]+({[^{}]+})*)?}/g, sprache);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString())
			.replace(/{sprache\s*(:[^{}]+({[^{}]+})*)?}/g, sprache);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString())
			.replace(/{sprache\s*(:[^{}]+({[^{}]+})*)?}/g, sprache);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString())
			.replace(/{sprache\s*(:[^{}]+({[^{}]+})*)?}/g, sprache);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString())
			.replace(/{sprache\s*(:[^{}]+({[^{}]+})*)?}/g, sprache);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = SchuelerStammdaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getSchuelerTelefone für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{id : \d+}/telefone
	 *
	 * Liest die Telefoneinträge des Schülers zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Telefoneinträge des Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SchuelerTelefon>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzusehen.
	 *   Code 404: Keine Telefon-Eintrag für den Schüler mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Telefoneinträge des Schülers
	 */
	public async getSchuelerTelefone(schema : string, id : number) : Promise<List<SchuelerTelefon>> {
		const path = "/db/{schema}/schueler/{id : \\d+}/telefone"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchuelerTelefon>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchuelerTelefon.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getVermerkdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{id : \d+}/vermerke
	 *
	 * Liest die Vermerkdaten des Schülers zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten und Vermerke besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Vermerkdaten des Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SchuelerVermerke>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzusehen.
	 *   Code 404: Kein Schüler-Vermerk-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Vermerkdaten des Schülers
	 */
	public async getVermerkdaten(schema : string, id : number) : Promise<List<SchuelerVermerke>> {
		const path = "/db/{schema}/schueler/{id : \\d+}/vermerke"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchuelerVermerke>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchuelerVermerke.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der DELETE-Methode deleteSchuelerVermerk für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{id : \d+}/vermerke/{idVermerk : \d+}
	 *
	 * Löscht einen Schueler-VermerkDabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Schüler-Vermerken besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Der Vermerk des Schülers wurde gelöscht
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzulegen.
	 *   Code 404: Kein Schüler-Vermerk mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 * @param {number} idVermerk - der Pfad-Parameter idVermerk
	 */
	public async deleteSchuelerVermerk(schema : string, id : number, idVermerk : number) : Promise<void> {
		const path = "/db/{schema}/schueler/{id : \\d+}/vermerke/{idVermerk : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString())
			.replace(/{idVermerk\s*(:[^{}]+({[^{}]+})*)?}/g, idVermerk.toString());
		await super.deleteJSON(path, null);
		return;
	}


	/**
	 * Implementierung der POST-Methode addBisherigeSchule für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{idSchueler : \d+}/bisherigeSchule
	 *
	 * Erstellt eine bisherige Schule, insofern der SVWS-Benutzer die erforderliche Berechtigung besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Die bisher besuchte Schule wurde erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SchuelerSchulbesuchSchule
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um bisherige Schulen hinzuzufügen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<SchuelerSchulbesuchSchule>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} idSchueler - der Pfad-Parameter idSchueler
	 *
	 * @returns Die bisher besuchte Schule wurde erfolgreich hinzugefügt.
	 */
	public async addBisherigeSchule(data : Partial<SchuelerSchulbesuchSchule>, schema : string, idSchueler : number) : Promise<SchuelerSchulbesuchSchule> {
		const path = "/db/{schema}/schueler/{idSchueler : \\d+}/bisherigeSchule"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{idSchueler\s*(:[^{}]+({[^{}]+})*)?}/g, idSchueler.toString());
		const body : string = SchuelerSchulbesuchSchule.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SchuelerSchulbesuchSchule.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getSchuelerEinwilligungen für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{idSchueler : \d+}/einwilligungen
	 *
	 * Liest die Einwilligungen des Schülers zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Einwilligungen des Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SchuelerEinwilligung>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzusehen.
	 *   Code 404: Keine Einwilligungen für den Schüler mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} idSchueler - der Pfad-Parameter idSchueler
	 *
	 * @returns Die Einwilligungen des Schülers
	 */
	public async getSchuelerEinwilligungen(schema : string, idSchueler : number) : Promise<List<SchuelerEinwilligung>> {
		const path = "/db/{schema}/schueler/{idSchueler : \\d+}/einwilligungen"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{idSchueler\s*(:[^{}]+({[^{}]+})*)?}/g, idSchueler.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchuelerEinwilligung>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchuelerEinwilligung.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der PATCH-Methode patchSchuelerEinwilligung für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{idSchueler : \d+}/einwilligungen/{idEinwilligungsart : \d+}
	 *
	 * Passt die Einwilligung zu der angegebenen Schüler- und Einwilligungsart-ID an und speichert das Ergebnis in der Datenbank.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Schüler-Einwilligungen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Einwilligung integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Einwilligungen der Schüler zu ändern.
	 *   Code 404: Kein Schüler oder keine Einwilligung der angegebenen Art gefunden.
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde. (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<SchuelerEinwilligung>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} idSchueler - der Pfad-Parameter idSchueler
	 * @param {number} idEinwilligungsart - der Pfad-Parameter idEinwilligungsart
	 */
	public async patchSchuelerEinwilligung(data : Partial<SchuelerEinwilligung>, schema : string, idSchueler : number, idEinwilligungsart : number) : Promise<void> {
		const path = "/db/{schema}/schueler/{idSchueler : \\d+}/einwilligungen/{idEinwilligungsart : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{idSchueler\s*(:[^{}]+({[^{}]+})*)?}/g, idSchueler.toString())
			.replace(/{idEinwilligungsart\s*(:[^{}]+({[^{}]+})*)?}/g, idEinwilligungsart.toString());
		const body : string = SchuelerEinwilligung.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der POST-Methode addSchuelerMerkmal für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{idSchueler : \d+}/merkmal
	 *
	 * Erstellt neue SchuelerMerkmale, insofern der SVWS-Benutzer die erforderliche Berechtigung besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Das SchuelerMerkmal wurde erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SchuelerSchulbesuchMerkmal
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um SchuelerMerkmale hinzuzufügen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<SchuelerSchulbesuchMerkmal>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} idSchueler - der Pfad-Parameter idSchueler
	 *
	 * @returns Das SchuelerMerkmal wurde erfolgreich hinzugefügt.
	 */
	public async addSchuelerMerkmal(data : Partial<SchuelerSchulbesuchMerkmal>, schema : string, idSchueler : number) : Promise<SchuelerSchulbesuchMerkmal> {
		const path = "/db/{schema}/schueler/{idSchueler : \\d+}/merkmal"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{idSchueler\s*(:[^{}]+({[^{}]+})*)?}/g, idSchueler.toString());
		const body : string = SchuelerSchulbesuchMerkmal.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SchuelerSchulbesuchMerkmal.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode addSchuelerTelefon für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{idSchueler : \d+}/telefon
	 *
	 * Erstellt einen neuen Schülertelefoneintrag. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Schülerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Der neue Telefoneintrag für den Schüler
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SchuelerTelefon
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Telefoneinträge für Schüler anzulegen.
	 *
	 * @param {Partial<SchuelerTelefon>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} idSchueler - der Pfad-Parameter idSchueler
	 *
	 * @returns Der neue Telefoneintrag für den Schüler
	 */
	public async addSchuelerTelefon(data : Partial<SchuelerTelefon>, schema : string, idSchueler : number) : Promise<SchuelerTelefon> {
		const path = "/db/{schema}/schueler/{idSchueler : \\d+}/telefon"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{idSchueler\s*(:[^{}]+({[^{}]+})*)?}/g, idSchueler.toString());
		const body : string = SchuelerTelefon.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SchuelerTelefon.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode addSchuelerStammdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/{idSchuljahresabschnitt : \d+}/stammdaten/create
	 *
	 * Erstellt neue SchülerStammdaten und gibt das erstellte Objekt zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen neuer SchülerStammdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Die SchülerStammdaten wurden erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SchuelerStammdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um SchülerStammdaten anzulegen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<SchuelerStammdaten>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} idSchuljahresabschnitt - der Pfad-Parameter idSchuljahresabschnitt
	 *
	 * @returns Die SchülerStammdaten wurden erfolgreich hinzugefügt.
	 */
	public async addSchuelerStammdaten(data : Partial<SchuelerStammdaten>, schema : string, idSchuljahresabschnitt : number) : Promise<SchuelerStammdaten> {
		const path = "/db/{schema}/schueler/{idSchuljahresabschnitt : \\d+}/stammdaten/create"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{idSchuljahresabschnitt\s*(:[^{}]+({[^{}]+})*)?}/g, idSchuljahresabschnitt.toString());
		const body : string = SchuelerStammdaten.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SchuelerStammdaten.transpilerFromJSON(text);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abschnitt\s*(:[^{}]+({[^{}]+})*)?}/g, abschnitt.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchuelerListeEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchuelerListeEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getSchuelerAuswahllisteFuerAbschnitt für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/abschnitt/{abschnitt : \d+}/auswahlliste
	 *
	 * Gibt die Informationen zur Verwaltung einer Schüler-Auswahlliste mit Filterfunktionen in Bezug auf einen Schuljahresabschnitt zurück.Es wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die GZip-komprimierten Daten zur Schüler-Auswahlliste
	 *     - Mime-Type: application/octet-stream
	 *     - Rückgabe-Typ: ApiFile
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Schülerdaten anzusehen.
	 *   Code 404: Nicht alle Daten wurden gefunden, z.B. Schüler-Einträge
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abschnitt - der Pfad-Parameter abschnitt
	 *
	 * @returns Die GZip-komprimierten Daten zur Schüler-Auswahlliste
	 */
	public async getSchuelerAuswahllisteFuerAbschnitt(schema : string, abschnitt : number) : Promise<ApiFile> {
		const path = "/db/{schema}/schueler/abschnitt/{abschnitt : \\d+}/auswahlliste"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abschnitt\s*(:[^{}]+({[^{}]+})*)?}/g, abschnitt.toString());
		const data : ApiFile = await super.getOctetStream(path);
		return data;
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<UebergangsempfehlungKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(UebergangsempfehlungKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getBisherigeSchule für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/bisherigeSchule/{id : \d+}
	 *
	 * Gibt die bisher besuchte Schule zurück, insofern der SVWS-Benutzer die erforderliche Berechtigung besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die bisher besuchte Schule
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SchuelerSchulbesuchSchule
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Schülerdaten anzusehen.
	 *   Code 404: Keine bisher besuchte Schule mit der angegebenen ID gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die bisher besuchte Schule
	 */
	public async getBisherigeSchule(schema : string, id : number) : Promise<SchuelerSchulbesuchSchule> {
		const path = "/db/{schema}/schueler/bisherigeSchule/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return SchuelerSchulbesuchSchule.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchBisherigeSchule für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/bisherigeSchule/{id : \d+}
	 *
	 * Patcht und Persistiert eine bisher besuchte Schule, insofern der SVWS-Benutzer die erforderliche Berechtigung besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Der Patch wurde erfolgreich ausgeführt.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um bisher besuchte Schulen zu ändern.
	 *   Code 404: Keine bisher besuchte Schule mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<SchuelerSchulbesuchSchule>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchBisherigeSchule(data : Partial<SchuelerSchulbesuchSchule>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/schueler/bisherigeSchule/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = SchuelerSchulbesuchSchule.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteBisherigeSchulen für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/bisherigeSchule/multiple
	 *
	 * Entfernt bisher besuchte Schulen, insofern der SVWS-Benutzer die erforderliche Berechtigung besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine bisher besuchte Schule wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SchuelerSchulbesuchSchule>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um bisher besuchte Schulen zu entfernen.
	 *   Code 404: Die bisher besuchten Schulen sind nicht vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine bisher besuchte Schule wurde erfolgreich entfernt.
	 */
	public async deleteBisherigeSchulen(data : List<number>, schema : string) : Promise<List<SchuelerSchulbesuchSchule>> {
		const path = "/db/{schema}/schueler/bisherigeSchule/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchuelerSchulbesuchSchule>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchuelerSchulbesuchSchule.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der DELETE-Methode deleteSchueler für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/delete/multiple
	 *
	 * Entfernt mehrere Schüler durch setzen eines Löschvermerks. Dabei wird geprüft, ob alle Vorbedingungen zum Entfernender Schüler erfüllt sind und der SVWS-Benutzer die notwendige Berechtigung hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Lösch-Operationen wurden ausgeführt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SimpleOperationResponse>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Schüler zu entfernen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Lösch-Operationen wurden ausgeführt.
	 */
	public async deleteSchueler(data : List<number>, schema : string) : Promise<List<SimpleOperationResponse>> {
		const path = "/db/{schema}/schueler/delete/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SimpleOperationResponse>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SimpleOperationResponse.transpilerFromJSON(text)); });
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = SchuelerLeistungsdaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteSchuelerLeistungsdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/leistungsdaten/{id : \d+}
	 *
	 * Entfernt Leistungsdaten. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen von Leistungsdaten hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Leistungsdaten wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SchuelerLeistungsdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Leistungsdaten zu entfernen.
	 *   Code 404: Die Leistungsdaten sind nicht vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Leistungsdaten wurde erfolgreich entfernt.
	 */
	public async deleteSchuelerLeistungsdaten(schema : string, id : number) : Promise<SchuelerLeistungsdaten> {
		const path = "/db/{schema}/schueler/leistungsdaten/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return SchuelerLeistungsdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode addSchuelerLeistungsdaten für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/leistungsdaten/create
	 *
	 * Erstellt neue Leistungsdaten und gibt das zugehörige Objekt zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Hinzufügen von Leistungsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Die Leistungsdaten wurden erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SchuelerLeistungsdaten
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Leistungsdaten hinzuzufügen.
	 *   Code 404: Daten für die Leistungsdaten (z.B. Fächer) wurden nicht gefunden und konnten nicht zugeordnet werden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<SchuelerLeistungsdaten>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Leistungsdaten wurden erfolgreich hinzugefügt.
	 */
	public async addSchuelerLeistungsdaten(data : Partial<SchuelerLeistungsdaten>, schema : string) : Promise<SchuelerLeistungsdaten> {
		const path = "/db/{schema}/schueler/leistungsdaten/create"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = SchuelerLeistungsdaten.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SchuelerLeistungsdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode addSchuelerLeistungsdatenMultiple für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/leistungsdaten/create/multiple
	 *
	 * Erstellt mehrere Leistungsdaten und gibt die zugehörigen Objekte zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum zum Hinzufügen von Leistungsdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Die Leistungsdaten wurden erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SchuelerLeistungsdaten>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Leistungsdaten hinzuzufügen.
	 *   Code 404: Daten für die Leistungsdaten (z.B. Fächer) wurden nicht gefunden und konnten nicht zugeordnet werden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<Partial<SchuelerLeistungsdaten>>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Leistungsdaten wurden erfolgreich hinzugefügt.
	 */
	public async addSchuelerLeistungsdatenMultiple(data : List<Partial<SchuelerLeistungsdaten>>, schema : string) : Promise<List<SchuelerLeistungsdaten>> {
		const path = "/db/{schema}/schueler/leistungsdaten/create/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<SchuelerLeistungsdaten>).map(d => SchuelerLeistungsdaten.transpilerToJSONPatch(d)).join() + "]";
		const result : string = await super.postJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchuelerLeistungsdaten>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchuelerLeistungsdaten.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der DELETE-Methode deleteSchuelerLeistungsdatenMultiple für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/leistungsdaten/delete/multiple
	 *
	 * Entfernt mehrere Leistungsdaten.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen von Leistungsdaten hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Leistungsdaten wurden erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SchuelerLeistungsdaten>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Leistungsdaten zu entfernen.
	 *   Code 404: Die Leistungsdaten sind zumindest nicht alle vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Leistungsdaten wurden erfolgreich entfernt.
	 */
	public async deleteSchuelerLeistungsdatenMultiple(data : List<number>, schema : string) : Promise<List<SchuelerLeistungsdaten>> {
		const path = "/db/{schema}/schueler/leistungsdaten/delete/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchuelerLeistungsdaten>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchuelerLeistungsdaten.transpilerFromJSON(text)); });
		return ret;
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abschnitt\s*(:[^{}]+({[^{}]+})*)?}/g, abschnitt.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abschnitt\s*(:[^{}]+({[^{}]+})*)?}/g, abschnitt.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abschnitt\s*(:[^{}]+({[^{}]+})*)?}/g, abschnitt.toString());
		const body : string = SchuelerLernabschnittBemerkungen.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getSchuelerMerkmal für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/merkmal/{id : \d+}
	 *
	 * Gibt das SchuelerMerkmal zurück, insofern der SVWS-Benutzer die erforderliche Berechtigung besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Das SchuelerMerkmal
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SchuelerSchulbesuchMerkmal
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Schülerdaten anzusehen.
	 *   Code 404: Kein SchuelerMerkmal mit der angegebenen ID gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Das SchuelerMerkmal
	 */
	public async getSchuelerMerkmal(schema : string, id : number) : Promise<SchuelerSchulbesuchMerkmal> {
		const path = "/db/{schema}/schueler/merkmal/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return SchuelerSchulbesuchMerkmal.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchSchuelerMerkmal für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/merkmal/{id : \d+}
	 *
	 * Patcht und Persistiert SchuelerMerkmale, insofern der SVWS-Benutzer die erforderliche Berechtigung besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 204: Der Patch wurde erfolgreich ausgeführt.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um SchuelerMerkmale zu ändern.
	 *   Code 404: Kein SchuelerMerkmal mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<SchuelerSchulbesuchMerkmal>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchSchuelerMerkmal(data : Partial<SchuelerSchulbesuchMerkmal>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/schueler/merkmal/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = SchuelerSchulbesuchMerkmal.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteSchuelerMerkmale für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/merkmal/multiple
	 *
	 * Entfernt SchuelerMerkmale, insofern der SVWS-Benutzer die erforderliche Berechtigung besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Ein SchuelerMerkmal wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SchuelerSchulbesuchMerkmal>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um SchuelerMerkmale zu entfernen.
	 *   Code 404: Die SchuelerMerkmale sind nicht vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Ein SchuelerMerkmal wurde erfolgreich entfernt.
	 */
	public async deleteSchuelerMerkmale(data : List<number>, schema : string) : Promise<List<SchuelerSchulbesuchMerkmal>> {
		const path = "/db/{schema}/schueler/merkmal/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchuelerSchulbesuchMerkmal>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchuelerSchulbesuchMerkmal.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode getSchuelerStammdatenMultiple für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/stammdaten
	 *
	 * Liest die Stammdaten der Schüler zu der angegebenen IDs aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Stammdaten des Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SchuelerStammdaten>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Schülerdaten anzusehen.
	 *   Code 404: Kein Schüler-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Stammdaten des Schülers
	 */
	public async getSchuelerStammdatenMultiple(data : List<number>, schema : string) : Promise<List<SchuelerStammdaten>> {
		const path = "/db/{schema}/schueler/stammdaten"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.postJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchuelerStammdaten>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchuelerStammdaten.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der PATCH-Methode patchSchuelerStammdatenMultiple für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/stammdaten
	 *
	 * Passt die Schüler-Stammdaten zu den angegebenen IDs an und speichert das Ergebnis in der Datenbank. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Schülerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Schülerstammdaten integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Schülerdaten zu ändern.
	 *   Code 404: Ein Schüler-Eintrag mit den angegebenen IDs wurde nicht gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<Partial<SchuelerStammdaten>>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 */
	public async patchSchuelerStammdatenMultiple(data : List<Partial<SchuelerStammdaten>>, schema : string) : Promise<void> {
		const path = "/db/{schema}/schueler/stammdaten"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<SchuelerStammdaten>).map(d => SchuelerStammdaten.transpilerToJSONPatch(d)).join() + "]";
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getSchuelerTelefon für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/telefon/{id : \d+}
	 *
	 * Liest die Daten des Telefoneintrags zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Daten des Schülertelefons
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SchuelerTelefon
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Schülerdaten anzusehen.
	 *   Code 404: Kein Telefoneintrag mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Daten des Schülertelefons
	 */
	public async getSchuelerTelefon(schema : string, id : number) : Promise<SchuelerTelefon> {
		const path = "/db/{schema}/schueler/telefon/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return SchuelerTelefon.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchSchuelerTelefon für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/telefon/{id : \d+}
	 *
	 * Passt die Telefoneintrag mit der angegeben Telefon-ID an und speichert das Ergebnis in der Datenbank.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Telefoneinträgen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in den Telefoneintrag integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Telefondaten der Schüler zu ändern.
	 *   Code 404: Kein Schülertelefoneintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<SchuelerTelefon>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchSchuelerTelefon(data : Partial<SchuelerTelefon>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/schueler/telefon/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = SchuelerTelefon.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteSchuelerTelefone für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/telefon/multiple
	 *
	 * Entfernt einen oder mehrerer Telefoneinträge bei Schülern, insofern der SVWS-Benutzer die erforderliche Berechtigung besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Telefoneinträge wurden erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Long>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Telefoneinträge zu entfernen.
	 *   Code 404: Mindestens ein Telefoneintrag ist nicht vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Telefoneinträge wurden erfolgreich entfernt.
	 */
	public async deleteSchuelerTelefone(data : List<number>, schema : string) : Promise<List<number>> {
		const path = "/db/{schema}/schueler/telefon/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<number>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(parseFloat(JSON.parse(text))); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode addVermerk für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/vermerke
	 *
	 * Erstellt einen neuen Vermerk EintragDabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Vermerkdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Die Vermerke des Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SchuelerVermerke
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Vermerke anzulegen.
	 *
	 * @param {Partial<SchuelerVermerke>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Vermerke des Schülers
	 */
	public async addVermerk(data : Partial<SchuelerVermerke>, schema : string) : Promise<SchuelerVermerke> {
		const path = "/db/{schema}/schueler/vermerke"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = SchuelerVermerke.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SchuelerVermerke.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchSchuelerVermerke für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/vermerke/{vid : \d+}
	 *
	 * Passt die Vermerke zu der angegebenen Schüler-ID und der angegeben VermerkeId an und speichert das Ergebnis in der Datenbank.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Sprachbelegungen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Vermerke integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Vermerkdaten der Schüler zu ändern.
	 *   Code 404: Kein Schüler-Eintrag mit der angegebenen ID gefunden oder keine Sprachbelegung für die Sprache gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<SchuelerVermerke>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} vid - der Pfad-Parameter vid
	 */
	public async patchSchuelerVermerke(data : Partial<SchuelerVermerke>, schema : string, vid : number) : Promise<void> {
		const path = "/db/{schema}/schueler/vermerke/{vid : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{vid\s*(:[^{}]+({[^{}]+})*)?}/g, vid.toString());
		const body : string = SchuelerVermerke.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getVermerkdatenByVermerkArt für den Zugriff auf die URL https://{hostname}/db/{schema}/schueler/vermerke/vermerkart/{vermerkArt : \d+}
	 *
	 * Liest die Vermerkdaten des Schülers zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten und Vermerke besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Vermerkdaten des Schülers
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SchuelerVermerke>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Schülerdaten anzusehen.
	 *   Code 404: Kein Schüler-Vermerk-Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} vermerkArt - der Pfad-Parameter vermerkArt
	 *
	 * @returns Die Vermerkdaten des Schülers
	 */
	public async getVermerkdatenByVermerkArt(schema : string, vermerkArt : number) : Promise<List<SchuelerVermerke>> {
		const path = "/db/{schema}/schueler/vermerke/vermerkart/{vermerkArt : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{vermerkArt\s*(:[^{}]+({[^{}]+})*)?}/g, vermerkArt.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchuelerVermerke>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchuelerVermerke.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der PATCH-Methode patchAbteilung für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/abteilungen/{id : \d+}
	 *
	 * Passt die Abteilung mit der angegebenen ID an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Abteilungen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 404: Kein Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z. B. beim Datenbankzugriff)
	 *
	 * @param {Partial<Abteilung>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchAbteilung(data : Partial<Abteilung>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/schule/abteilungen/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = Abteilung.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteAbteilung für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/abteilungen/{id : \d+}
	 *
	 * Entfernt eine Abteilung. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen eines Abteilung hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Abteilung wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Abteilung
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 404: Keine Abteilung vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft)
	 *   Code 500: Unspezifizierter Fehler (z. B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Abteilung wurde erfolgreich entfernt.
	 */
	public async deleteAbteilung(schema : string, id : number) : Promise<Abteilung> {
		const path = "/db/{schema}/schule/abteilungen/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return Abteilung.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getAbteilungenByIdJahresAbschnitt für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/abteilungen/{idSchuljahresabschnitt : \d+}
	 *
	 * Erstellt eine Liste aller in der Datenbank vorhanden Abteilungen für die angegebene Id des Schuljahresabschnittes unter Angabe der ID, der Bezeichnung, der ID des Schuljahresabschnitts, der Lehrer-ID des Abteilungsleiters, die Bezeichnung des Raums des Abteilungsleiters, die eMail-Adresse des Abteilungsleiters, die interne telefonische Durchwahl des Abteilungsleiters, einer Sortierreihenfolge und und die Zuordnungen der Klassen zu der Abteilung.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Abteilungen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Abteilung-Listen-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Abteilung>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Abteilungen anzusehen.
	 *   Code 404: Keine Abteilung-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} idSchuljahresabschnitt - der Pfad-Parameter idSchuljahresabschnitt
	 *
	 * @returns Eine Liste von Abteilung-Listen-Einträgen
	 */
	public async getAbteilungenByIdJahresAbschnitt(schema : string, idSchuljahresabschnitt : number) : Promise<List<Abteilung>> {
		const path = "/db/{schema}/schule/abteilungen/{idSchuljahresabschnitt : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{idSchuljahresabschnitt\s*(:[^{}]+({[^{}]+})*)?}/g, idSchuljahresabschnitt.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<Abteilung>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(Abteilung.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode addAbteilung für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/abteilungen/{idSchuljahresabschnitt : \d+}
	 *
	 * Erstellt eine neue Abteilung und gibt das zugehörige Objekt zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Abteilungen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Die Abteilung wurde erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Abteilung
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 500: Unspezifizierter Fehler (z. B. beim Datenbankzugriff)
	 *
	 * @param {Partial<Abteilung>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} idSchuljahresabschnitt - der Pfad-Parameter idSchuljahresabschnitt
	 *
	 * @returns Die Abteilung wurde erfolgreich hinzugefügt.
	 */
	public async addAbteilung(data : Partial<Abteilung>, schema : string, idSchuljahresabschnitt : number) : Promise<Abteilung> {
		const path = "/db/{schema}/schule/abteilungen/{idSchuljahresabschnitt : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{idSchuljahresabschnitt\s*(:[^{}]+({[^{}]+})*)?}/g, idSchuljahresabschnitt.toString());
		const body : string = Abteilung.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return Abteilung.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der DELETE-Methode deleteAbteilungen für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/abteilungen/delete/multiple
	 *
	 * Entfernt Abteilungen, insofern die Berechtigungen vorhanden sind
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Abteilungen wurden erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SimpleOperationResponse>
	 *   Code 400: Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Abteilungen zu löschen.
	 *   Code 404: Es wurden keine Entitäten zu den IDs gefunden.
	 *   Code 500: Unspezifizierter Fehler (z. B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Abteilungen wurden erfolgreich entfernt.
	 */
	public async deleteAbteilungen(data : List<number>, schema : string) : Promise<List<SimpleOperationResponse>> {
		const path = "/db/{schema}/schule/abteilungen/delete/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SimpleOperationResponse>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SimpleOperationResponse.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode addAbteilungKlassenzuordnung für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/abteilungen/klassenzuordnung
	 *
	 * Erstellt eine neue AbteilungenKlassenzuordnungen und gibt das zugehörige Objekt zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von AbteilungKlassenzuordnungen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Die AbteilungenKlassenzuordnungen wurde erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<AbteilungKlassenzuordnung>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 500: Unspezifizierter Fehler (z. B. beim Datenbankzugriff)
	 *
	 * @param {List<Partial<AbteilungKlassenzuordnung>>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die AbteilungenKlassenzuordnungen wurde erfolgreich hinzugefügt.
	 */
	public async addAbteilungKlassenzuordnung(data : List<Partial<AbteilungKlassenzuordnung>>, schema : string) : Promise<List<AbteilungKlassenzuordnung>> {
		const path = "/db/{schema}/schule/abteilungen/klassenzuordnung"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<AbteilungKlassenzuordnung>).map(d => AbteilungKlassenzuordnung.transpilerToJSONPatch(d)).join() + "]";
		const result : string = await super.postJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<AbteilungKlassenzuordnung>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(AbteilungKlassenzuordnung.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der DELETE-Methode deleteAbteilungKlassenzuordnung für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/abteilungen/klassenzuordnung/delete/multiple
	 *
	 * Entfernt AbteilungenKlassenzuordnungen, insofern der SVWS-Benutzer die notwendige Berechtigung hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die AbteilungenKlassenzuordnung wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<AbteilungKlassenzuordnung>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 404: Keine AbteilungenKlassenzuordnung vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft)
	 *   Code 500: Unspezifizierter Fehler (z. B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die AbteilungenKlassenzuordnung wurde erfolgreich entfernt.
	 */
	public async deleteAbteilungKlassenzuordnung(data : List<number>, schema : string) : Promise<List<AbteilungKlassenzuordnung>> {
		const path = "/db/{schema}/schule/abteilungen/klassenzuordnung/delete/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<AbteilungKlassenzuordnung>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(AbteilungKlassenzuordnung.transpilerFromJSON(text)); });
		return ret;
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
	 *     - Rückgabe-Typ: List<NoteKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine gültige Anmeldung.
	 *   Code 404: Keine Noten-Einträge gefunden.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Noten-Katalog.
	 */
	public async getKatalogNoten(schema : string) : Promise<List<NoteKatalogEintrag>> {
		const path = "/db/{schema}/schule/allgemein/noten"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<NoteKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(NoteKatalogEintrag.transpilerFromJSON(text)); });
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchulgliederungKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchulgliederungKatalogEintrag.transpilerFromJSON(text)); });
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchultraegerKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchultraegerKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getVerkehrssprachen für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/allgemein/verkehrssprachen
	 *
	 * DEPRECATED: Erstellt eine Liste aller in dem Katalog vorhanden der Verkehrssprachen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<Aufsichtsbereich>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(Aufsichtsbereich.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getSchuelerByEinwilligungsartID für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/einwilligungsart/{einwilligungsart : \d+}/schuelerinfos
	 *
	 * Erstellt eine Liste aller Schüler der angegebenen Einwilligungsart unter Angabe der ID.Es wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Schüler-Listen-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SchuelerEinwilligungsartenZusammenfassung>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Schülerdaten anzusehen.
	 *   Code 404: Keine Schüler-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} einwilligungsart - der Pfad-Parameter einwilligungsart
	 *
	 * @returns Eine Liste von Schüler-Listen-Einträgen
	 */
	public async getSchuelerByEinwilligungsartID(schema : string, einwilligungsart : number) : Promise<List<SchuelerEinwilligungsartenZusammenfassung>> {
		const path = "/db/{schema}/schule/einwilligungsart/{einwilligungsart : \\d+}/schuelerinfos"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{einwilligungsart\s*(:[^{}]+({[^{}]+})*)?}/g, einwilligungsart.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchuelerEinwilligungsartenZusammenfassung>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchuelerEinwilligungsartenZusammenfassung.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getEinwilligungsarten für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/einwilligungsarten
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Einwilligungsarten unter Angabe der ID, der Bezeichnung sowie des Schlüssels, einer Sortierreihenfolge und des zugehörigen Personentyps (Schüler, Lehrer, Erzieher). Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Einwilligungsart>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen
	 */
	public async getEinwilligungsarten(schema : string) : Promise<List<Einwilligungsart>> {
		const path = "/db/{schema}/schule/einwilligungsarten"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<Einwilligungsart>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(Einwilligungsart.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getEinwilligungsart für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/einwilligungsarten/{id : \d+}
	 *
	 * Liest die Daten der Einwilligungsart zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Daten der Einwilligungsart
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Einwilligungsart
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalogdaten anzusehen.
	 *   Code 404: Keine Einwilligungsart mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Daten der Einwilligungsart
	 */
	public async getEinwilligungsart(schema : string, id : number) : Promise<Einwilligungsart> {
		const path = "/db/{schema}/schule/einwilligungsarten/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return Einwilligungsart.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchEinwilligungsart für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/einwilligungsarten/{id : \d+}
	 *
	 * Passt die Einwilligungsart-Stammdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern der Daten der Einwilligungsart besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Einwilligungsart-Daten integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Einwilligungsart-Daten zu ändern.
	 *   Code 404: Keine Einwilligungsart mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<Einwilligungsart>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchEinwilligungsart(data : Partial<Einwilligungsart>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/schule/einwilligungsarten/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = Einwilligungsart.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteEinwilligungsart für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/einwilligungsarten/{id : \d+}
	 *
	 * Entfernt eine Einwilligungsart der Schule.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Einwilligungsart wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Einwilligungsart
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.
	 *   Code 404: Einwilligungsart nicht vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Einwilligungsart wurde erfolgreich entfernt.
	 */
	public async deleteEinwilligungsart(schema : string, id : number) : Promise<Einwilligungsart> {
		const path = "/db/{schema}/schule/einwilligungsarten/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return Einwilligungsart.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der DELETE-Methode deleteEinwilligungsarten für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/einwilligungsarten/delete/multiple
	 *
	 * Entfernt mehrere Einwilligungsarten der Schule.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Einwilligungsarten wurden erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SimpleOperationResponse>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.
	 *   Code 404: Einwilligungsarten nicht vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Einwilligungsarten wurden erfolgreich entfernt.
	 */
	public async deleteEinwilligungsarten(data : List<number>, schema : string) : Promise<List<SimpleOperationResponse>> {
		const path = "/db/{schema}/schule/einwilligungsarten/delete/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SimpleOperationResponse>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SimpleOperationResponse.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode createEinwilligungsart für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/einwilligungsarten/new
	 *
	 * Erstellt eine neue Einwilligungsart und gibt sie zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Einwilligungsart besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Einwilligungsart wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Einwilligungsart
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Einwilligungsart anzulegen.
	 *   Code 409: Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<Einwilligungsart>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Einwilligungsart wurde erfolgreich angelegt.
	 */
	public async createEinwilligungsart(data : Partial<Einwilligungsart>, schema : string) : Promise<Einwilligungsart> {
		const path = "/db/{schema}/schule/einwilligungsarten/new"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = Einwilligungsart.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return Einwilligungsart.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getErzieherart für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/erzieherart/{id : \d+}
	 *
	 * Liest die Daten der Erzieherart zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Daten der Erzieherart
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Erzieherart
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalogdaten anzusehen.
	 *   Code 404: Keine Erzieherart mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Daten der Erzieherart
	 */
	public async getErzieherart(schema : string, id : number) : Promise<Erzieherart> {
		const path = "/db/{schema}/schule/erzieherart/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return Erzieherart.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchErzieherart für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/erzieherart/{id : \d+}
	 *
	 * Passt die Erzieherart-Stammdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern der Daten der Erzieherart besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Erzieherart-Daten integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Erzieherart-Daten zu ändern.
	 *   Code 404: Keine Erzieherart mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<Erzieherart>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchErzieherart(data : Partial<Erzieherart>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/schule/erzieherart/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = Erzieherart.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der POST-Methode addErzieherart für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/erzieherart/new
	 *
	 * Erstellt eine neue Erzieherart und gibt sie zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Erzieherart besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Telefonart wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Erzieherart
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Erzieherart anzulegen.
	 *   Code 409: Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<Erzieherart>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Telefonart wurde erfolgreich angelegt.
	 */
	public async addErzieherart(data : Partial<Erzieherart>, schema : string) : Promise<Erzieherart> {
		const path = "/db/{schema}/schule/erzieherart/new"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = Erzieherart.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return Erzieherart.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getErzieherArten für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/erzieherarten
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
		const path = "/db/{schema}/schule/erzieherarten"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<Erzieherart>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(Erzieherart.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der DELETE-Methode deleteErzieherarten für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/erzieherarten/delete/multiple
	 *
	 * Entfernt mehrere Erzieherarten der Schule.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Erzieherarten wurden erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SimpleOperationResponse>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.
	 *   Code 404: Erzieherarten nicht vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Erzieherarten wurden erfolgreich entfernt.
	 */
	public async deleteErzieherarten(data : List<number>, schema : string) : Promise<List<SimpleOperationResponse>> {
		const path = "/db/{schema}/schule/erzieherarten/delete/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SimpleOperationResponse>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SimpleOperationResponse.transpilerFromJSON(text)); });
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{schulnummer\s*(:[^{}]+({[^{}]+})*)?}/g, schulnummer.toString());
		const result : string = await super.postJSON(path, null);
		const text = result;
		return SchuleStammdaten.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getSchulleitungsfunktion für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/leitungsfunktion/{id : \d+}
	 *
	 * Gibt die Leitungsfunktion der Schule zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Leitungsfunktion
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Schulleitung
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Leitungsfunktion der Schule anzusehen.
	 *   Code 404: Keine Leitungsfunktion der Schule gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Leitungsfunktion
	 */
	public async getSchulleitungsfunktion(schema : string, id : number) : Promise<Schulleitung> {
		const path = "/db/{schema}/schule/leitungsfunktion/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return Schulleitung.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchSchulleitungsfunktion für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/leitungsfunktion/{id : \d+}
	 *
	 * Passt die Leitungsfunktion der Schule mit der angebenen ID an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 404: Kein Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<Schulleitung>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchSchulleitungsfunktion(data : Partial<Schulleitung>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/schule/leitungsfunktion/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = Schulleitung.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteSchulleitungsfunktion für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/leitungsfunktion/{id : \d+}
	 *
	 * Entfernt eine Leitungsfunktion der Schule.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Leitungsfunktion der Schule wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Schulleitung
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Leitungsfunktion der Schule zu löschen.
	 *   Code 404: Die Leitungsfunktion der Schule ist nicht vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Leitungsfunktion der Schule wurde erfolgreich entfernt.
	 */
	public async deleteSchulleitungsfunktion(schema : string, id : number) : Promise<Schulleitung> {
		const path = "/db/{schema}/schule/leitungsfunktion/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return Schulleitung.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getSchulleitungsfunktionen für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/leitungsfunktion/alle
	 *
	 * Gibt die Leitungsfunktionen der Schule zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Leitungsfunktionen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Schulleitung>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Leitungsfunktionen der Schule anzusehen.
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Leitungsfunktionen
	 */
	public async getSchulleitungsfunktionen(schema : string) : Promise<List<Schulleitung>> {
		const path = "/db/{schema}/schule/leitungsfunktion/alle"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<Schulleitung>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(Schulleitung.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode addSchulleitungsfunktion für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/leitungsfunktion/create
	 *
	 * Erstellt einen neue Leitungsfunktion der Schule und gibt das zugehörige Objekt zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Hinzufügen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Die Leitungsfunktion der Schule wurde erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Schulleitung
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Leitungsfunktion für die Schule anzulegen.
	 *   Code 404: Der Lehrer wurde nichtgefunden.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<Schulleitung>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Leitungsfunktion der Schule wurde erfolgreich hinzugefügt.
	 */
	public async addSchulleitungsfunktion(data : Partial<Schulleitung>, schema : string) : Promise<Schulleitung> {
		const path = "/db/{schema}/schule/leitungsfunktion/create"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = Schulleitung.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return Schulleitung.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode addSchulleitungsfunktionen für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/leitungsfunktion/create/multiple
	 *
	 * Erstellt mehrere neue Leitungsfunktion für die Schule und gibt das zugehörige Objekt zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Hinzufügen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Die Leitungsfunktionen wurden erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Schulleitung>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Leitungsfunktion für die Schule anzulegen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<Partial<Schulleitung>>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Leitungsfunktionen wurden erfolgreich hinzugefügt.
	 */
	public async addSchulleitungsfunktionen(data : List<Partial<Schulleitung>>, schema : string) : Promise<List<Schulleitung>> {
		const path = "/db/{schema}/schule/leitungsfunktion/create/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<Schulleitung>).map(d => Schulleitung.transpilerToJSONPatch(d)).join() + "]";
		const result : string = await super.postJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<Schulleitung>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(Schulleitung.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der DELETE-Methode deleteSchulleitungsfunktionen für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/leitungsfunktion/delete/multiple
	 *
	 * Entfernt mehrere Leitungsfunktionen der Schule.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Löschen hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Leitungsfunktionen der Schule wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Schulleitung>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Leitungsfunktion der Schule zu löschen.
	 *   Code 404: Mindestens eine Leitungsfunktion der Schule ist nicht vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Leitungsfunktionen der Schule wurde erfolgreich entfernt.
	 */
	public async deleteSchulleitungsfunktionen(data : List<number>, schema : string) : Promise<List<Schulleitung>> {
		const path = "/db/{schema}/schule/leitungsfunktion/delete/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<Schulleitung>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(Schulleitung.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getLernplattform für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/lernplattform/{id : \d+}
	 *
	 * Liest die Daten der Lernplattform zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Daten der Lernplattform
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Lernplattform
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalogdaten anzusehen.
	 *   Code 404: Keine Lernplattform mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Daten der Lernplattform
	 */
	public async getLernplattform(schema : string, id : number) : Promise<Lernplattform> {
		const path = "/db/{schema}/schule/lernplattform/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return Lernplattform.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchLernplattform für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/lernplattform/{id : \d+}
	 *
	 * Passt die Lernplattform-Stammdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern der Daten der Lernplattform besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Lernplattform-Daten integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Lernplattform-Daten zu ändern.
	 *   Code 404: Keine Lernplattform mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<Lernplattform>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchLernplattform(data : Partial<Lernplattform>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/schule/lernplattform/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = Lernplattform.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteLernplattform für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/lernplattform/{id : \d+}
	 *
	 * Entfernt eine Lernplattform der Schule.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Lernplattform wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Lernplattform
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.
	 *   Code 404: Lernplattform nicht vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Lernplattform wurde erfolgreich entfernt.
	 */
	public async deleteLernplattform(schema : string, id : number) : Promise<Lernplattform> {
		const path = "/db/{schema}/schule/lernplattform/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return Lernplattform.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode addLernplattform für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/lernplattform/new
	 *
	 * Erstellt eine neue Lernplattform und gibt sie zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Lernplattform besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Lernplattform wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Lernplattform
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Lernplattform anzulegen.
	 *   Code 409: Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<Lernplattform>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Lernplattform wurde erfolgreich angelegt.
	 */
	public async addLernplattform(data : Partial<Lernplattform>, schema : string) : Promise<Lernplattform> {
		const path = "/db/{schema}/schule/lernplattform/new"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = Lernplattform.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return Lernplattform.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getLernplattformen für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/lernplattformen
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Lernplattformen unter Angabe der ID und der Bezeichnung. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<Lernplattform>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen
	 */
	public async getLernplattformen(schema : string) : Promise<List<Lernplattform>> {
		const path = "/db/{schema}/schule/lernplattformen"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<Lernplattform>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(Lernplattform.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der DELETE-Methode deleteLernplattformen für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/lernplattformen/delete/multiple
	 *
	 * Entfernt mehrere Lernplattformen der Schule.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Lernplattformen wurden erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SimpleOperationResponse>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.
	 *   Code 404: Lernplattformen nicht vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Lernplattformen wurden erfolgreich entfernt.
	 */
	public async deleteLernplattformen(data : List<number>, schema : string) : Promise<List<SimpleOperationResponse>> {
		const path = "/db/{schema}/schule/lernplattformen/delete/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SimpleOperationResponse>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SimpleOperationResponse.transpilerFromJSON(text)); });
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
	public async getSchullogo(schema : string) : Promise<string | null> {
		const path = "/db/{schema}/schule/logo"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde(z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<Raum>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchRaum(data : Partial<Raum>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/schule/raeume/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = ReligionEintrag.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteReligionEintrag für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/religionen/{id : \d+}
	 *
	 * Entfernt einen Religion-Katalog-Eintrag der Schule. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Religion-Katalog-Eintrag wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: ReligionEintrag
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.
	 *   Code 404: Kein Religion-Katalog-Eintrag vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Religion-Katalog-Eintrag wurde erfolgreich entfernt.
	 */
	public async deleteReligionEintrag(schema : string, id : number) : Promise<ReligionEintrag> {
		const path = "/db/{schema}/schule/religionen/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return ReligionEintrag.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der DELETE-Methode deleteReligionEintraege für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/religionen/delete/multiple
	 *
	 * Entfernt mehrere Religion-Katalog-Einträge der Schule.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Religion-Katalog-Einträge wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SimpleOperationResponse>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.
	 *   Code 404: Religion-Katalog-Einträge nicht vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Religion-Katalog-Einträge wurde erfolgreich entfernt.
	 */
	public async deleteReligionEintraege(data : List<number>, schema : string) : Promise<List<SimpleOperationResponse>> {
		const path = "/db/{schema}/schule/religionen/delete/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SimpleOperationResponse>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SimpleOperationResponse.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode createReligion für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/religionen/new
	 *
	 * Erstellt eine neue Religion und gibt sie zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Religion besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Religion wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: ReligionEintrag
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Religion anzulegen.
	 *   Code 404: Keine Religion  mit dem eingegebenen Kuerzel gefunden
	 *   Code 409: Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<ReligionEintrag>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Religion wurde erfolgreich angelegt.
	 */
	public async createReligion(data : Partial<ReligionEintrag>, schema : string) : Promise<ReligionEintrag> {
		const path = "/db/{schema}/schule/religionen/new"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = ReligionEintrag.transpilerToJSONPatch(data);
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
	 *     - Rückgabe-Typ: List<SchuelerStatusKatalogEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen für den Katalog Schüler-Status
	 */
	public async getKatalogSchuelerStatus(schema : string) : Promise<List<SchuelerStatusKatalogEintrag>> {
		const path = "/db/{schema}/schule/schueler/status"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchuelerStatusKatalogEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchuelerStatusKatalogEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getSchulen für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/schulen
	 *
	 * Erstellt eine Liste aller in dem schul-spezifischen Katalog vorhanden Schulen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SchulEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen
	 */
	public async getSchulen(schema : string) : Promise<List<SchulEintrag>> {
		const path = "/db/{schema}/schule/schulen"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchulEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchulEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getSchuleAusKatalog für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/schulen/{id : \d+}
	 *
	 * Gibt den Eintrag im schulspezifischen Katalog der Schulen zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Eintrag im schulspezifischen Katalog der Schulen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SchulEintrag
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Katalog anzusehen.
	 *   Code 404: Kein Eintrag mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Eintrag im schulspezifischen Katalog der Schulen
	 */
	public async getSchuleAusKatalog(schema : string, id : number) : Promise<SchulEintrag> {
		const path = "/db/{schema}/schule/schulen/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return SchulEintrag.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchSchuleAusKatalog für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/schulen/{id : \d+}
	 *
	 * Passt den Eintrag des schulspezifischen Kataloges der Schulen mit der angebenen ID an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Katalog-Daten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 404: Kein Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<SchulEintrag>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchSchuleAusKatalog(data : Partial<SchulEintrag>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/schule/schulen/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = SchulEintrag.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteSchuleVonKatalog für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/schulen/{id : \d+}
	 *
	 * aus dem schulspezifischen Katalog der Schulen.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Eintrag wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SchulEintrag
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.
	 *   Code 404: Kein Eintrag vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Eintrag wurde erfolgreich entfernt.
	 */
	public async deleteSchuleVonKatalog(schema : string, id : number) : Promise<SchulEintrag> {
		const path = "/db/{schema}/schule/schulen/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return SchulEintrag.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode addSchuleZuKatalog für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/schulen/create
	 *
	 * Erstellt einen neuen Eintrag für den schulspezifischen Katalog der Schulen und gibt das zugehörige Objekt zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Katalogs besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Der Eintrag wurde erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: SchulEintrag
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Eintrag für die Schule anzulegen.
	 *   Code 404: Die Katalogdaten wurden nicht gefunden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<SchulEintrag>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Eintrag wurde erfolgreich hinzugefügt.
	 */
	public async addSchuleZuKatalog(data : Partial<SchulEintrag>, schema : string) : Promise<SchulEintrag> {
		const path = "/db/{schema}/schule/schulen/create"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = SchulEintrag.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return SchulEintrag.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der DELETE-Methode deleteSchulenVonKatalog für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/schulen/delete/multiple
	 *
	 * Entfernt mehrere Einträge aus dem schulspezifischen Katalog der Schulen.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Einträge wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SchulEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.
	 *   Code 404: Räume nicht vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Einträge wurde erfolgreich entfernt.
	 */
	public async deleteSchulenVonKatalog(data : List<number>, schema : string) : Promise<List<SchulEintrag>> {
		const path = "/db/{schema}/schule/schulen/delete/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchulEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchulEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getSchulenMitKuerzel für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/schulen/kuerzel
	 *
	 * Erstellt eine Liste aller in dem schul-spezifischen Katalog vorhanden Schulen, welche ein Kürzel gesetzt haben. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SchulEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen
	 */
	public async getSchulenMitKuerzel(schema : string) : Promise<List<SchulEintrag>> {
		const path = "/db/{schema}/schule/schulen/kuerzel"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchulEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchulEintrag.transpilerFromJSON(text)); });
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = SchuleStammdaten.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getTelefonart für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/telefonart/{id : \d+}
	 *
	 * Liest die Daten der Telefonart zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Daten der Telefonart
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: TelefonArt
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalogdaten anzusehen.
	 *   Code 404: Keine Telefonart mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Daten der Telefonart
	 */
	public async getTelefonart(schema : string, id : number) : Promise<TelefonArt> {
		const path = "/db/{schema}/schule/telefonart/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return TelefonArt.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchTelefonart für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/Telefonart/{id : \d+}
	 *
	 * Passt die Telefonart-Stammdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern der Daten der Telefonart besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Telefonart-Daten integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Telefonart-Daten zu ändern.
	 *   Code 404: Keine Telefonart mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<TelefonArt>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchTelefonart(data : Partial<TelefonArt>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/schule/Telefonart/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = TelefonArt.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der POST-Methode addTelefonart für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/telefonart/new
	 *
	 * Erstellt eine neue Telefonart und gibt sie zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Telefonart besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Telefonart wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: TelefonArt
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Telefonart anzulegen.
	 *   Code 409: Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<TelefonArt>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Telefonart wurde erfolgreich angelegt.
	 */
	public async addTelefonart(data : Partial<TelefonArt>, schema : string) : Promise<TelefonArt> {
		const path = "/db/{schema}/schule/telefonart/new"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = TelefonArt.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return TelefonArt.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der GET-Methode getTelefonarten für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/Telefonarten
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Telefonarten unter Angabe der ID und der Bezeichnung. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<TelefonArt>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen
	 */
	public async getTelefonarten(schema : string) : Promise<List<TelefonArt>> {
		const path = "/db/{schema}/schule/Telefonarten"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<TelefonArt>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(TelefonArt.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der DELETE-Methode deleteTelefonarten für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/telefonarten/delete/multiple
	 *
	 * Entfernt mehrere Telefonarten der Schule.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Telefonarten wurden erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SimpleOperationResponse>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.
	 *   Code 404: Telefonarten nicht vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Telefonarten wurden erfolgreich entfernt.
	 */
	public async deleteTelefonarten(data : List<number>, schema : string) : Promise<List<SimpleOperationResponse>> {
		const path = "/db/{schema}/schule/telefonarten/delete/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SimpleOperationResponse>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SimpleOperationResponse.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getSchuelerByVermerkartID für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/vermerkart/{vermerkart : \d+}/schuelerinfos
	 *
	 * Erstellt eine Liste aller Schüler der angegebenen Vermerkart unter Angabe der ID.Es wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Schülerdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Schüler-Listen-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SchuelerVermerkartZusammenfassung>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Schülerdaten anzusehen.
	 *   Code 404: Keine Schüler-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} vermerkart - der Pfad-Parameter vermerkart
	 *
	 * @returns Eine Liste von Schüler-Listen-Einträgen
	 */
	public async getSchuelerByVermerkartID(schema : string, vermerkart : number) : Promise<List<SchuelerVermerkartZusammenfassung>> {
		const path = "/db/{schema}/schule/vermerkart/{vermerkart : \\d+}/schuelerinfos"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{vermerkart\s*(:[^{}]+({[^{}]+})*)?}/g, vermerkart.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SchuelerVermerkartZusammenfassung>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SchuelerVermerkartZusammenfassung.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getVermerkarten für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/vermerkarten
	 *
	 * Erstellt eine Liste aller in dem Katalog vorhanden Vermerkarten unter Angabe der ID, der Bezeichnung sowie der Bezeichnung, einer Sortierreihenfolge und ob sie in der Anwendung sichtbar bzw. änderbar sein sollen. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste von Katalog-Einträgen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<VermerkartEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalog-Einträge anzusehen.
	 *   Code 404: Keine Katalog-Einträge gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste von Katalog-Einträgen
	 */
	public async getVermerkarten(schema : string) : Promise<List<VermerkartEintrag>> {
		const path = "/db/{schema}/schule/vermerkarten"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<VermerkartEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(VermerkartEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getVermerkart für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/vermerkarten/{id : \d+}
	 *
	 * Liest die Daten der Vermerkart zu der angegebenen ID aus der Datenbank und liefert diese zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Katalogdaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Daten der Vermerkart
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: VermerkartEintrag
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Katalogdaten anzusehen.
	 *   Code 404: Keine Vermerkart mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Daten der Vermerkart
	 */
	public async getVermerkart(schema : string, id : number) : Promise<VermerkartEintrag> {
		const path = "/db/{schema}/schule/vermerkarten/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return VermerkartEintrag.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchVermerkart für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/vermerkarten/{id : \d+}
	 *
	 * Passt die Vermerkart-Stammdaten zu der angegebenen ID an und speichert das Ergebnis in der Datenbank. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern der Daten der Vermerkart besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in die Vermerkart-Daten integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Vermerkart-Daten zu ändern.
	 *   Code 404: Keine Vermerkart mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<VermerkartEintrag>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchVermerkart(data : Partial<VermerkartEintrag>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/schule/vermerkarten/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = VermerkartEintrag.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteVermerkartEintrag für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/vermerkarten/{id : \d+}
	 *
	 * Entfernt einen Vermerkart-Katalog-Eintrag der Schule.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Vermerkart-Katalog-Eintrag wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: VermerkartEintrag
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.
	 *   Code 404: Kein Vermerkart-Katalog-Eintrag vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Vermerkart-Katalog-Eintrag wurde erfolgreich entfernt.
	 */
	public async deleteVermerkartEintrag(schema : string, id : number) : Promise<VermerkartEintrag> {
		const path = "/db/{schema}/schule/vermerkarten/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return VermerkartEintrag.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der DELETE-Methode deleteVermerkartEintraege für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/vermerkarten/delete/multiple
	 *
	 * Entfernt mehrere Vermerkart-Katalog-Einträge der Schule.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten von Katalogen hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Lösch-Operationen wurden ausgeführt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SimpleOperationResponse>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Katalog zu bearbeiten.
	 *   Code 404: Vermerkart-Katalog-Einträge nicht vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Lösch-Operationen wurden ausgeführt.
	 */
	public async deleteVermerkartEintraege(data : List<number>, schema : string) : Promise<List<SimpleOperationResponse>> {
		const path = "/db/{schema}/schule/vermerkarten/delete/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SimpleOperationResponse>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SimpleOperationResponse.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode createVermerkart für den Zugriff auf die URL https://{hostname}/db/{schema}/schule/vermerkarten/new
	 *
	 * Erstellt eine neue Vermerkart und gibt sie zurück.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen einer Vermerkart besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Vermerkart wurde erfolgreich angelegt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: VermerkartEintrag
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Vermerkart anzulegen.
	 *   Code 404: Keine Vermerkart  mit dem eingegebenen Kuerzel gefunden
	 *   Code 409: Fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<VermerkartEintrag>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Vermerkart wurde erfolgreich angelegt.
	 */
	public async createVermerkart(data : Partial<VermerkartEintrag>, schema : string) : Promise<VermerkartEintrag> {
		const path = "/db/{schema}/schule/vermerkarten/new"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = VermerkartEintrag.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return VermerkartEintrag.transpilerFromJSON(text);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<StundenplanAufsichtsbereich>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(StundenplanAufsichtsbereich.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode addStundenplanKalenderwochenzuordnung für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/{id : \d+}/kalenderwochen/create
	 *
	 * Erstellt eine neue Kalenderwochenzuordnung für den angegebenen Stundenplan und gibt das zugehörige Objekt zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Stundenplans besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Die Kalenderwochenzuordnung wurde erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanKalenderwochenzuordnung
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um eine Kalenderwochenzuordnung für einen Stundenplan anzulegen.
	 *   Code 404: Die Stundenplandaten wurden nicht gefunden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<StundenplanKalenderwochenzuordnung>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Kalenderwochenzuordnung wurde erfolgreich hinzugefügt.
	 */
	public async addStundenplanKalenderwochenzuordnung(data : Partial<StundenplanKalenderwochenzuordnung>, schema : string, id : number) : Promise<StundenplanKalenderwochenzuordnung> {
		const path = "/db/{schema}/stundenplan/{id : \\d+}/kalenderwochen/create"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = StundenplanKalenderwochenzuordnung.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return StundenplanKalenderwochenzuordnung.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode addStundenplanKalenderwochenzuordnungen für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/{id : \d+}/kalenderwochen/create/multiple
	 *
	 * Erstellt mehrere neue Kalenderwochenzuordnungen für den angegebenen Stundenplan und gibt die zugehörigen Objekte zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Stundenplans besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Die Kalenderwochenzuordnungen wurden erfolgreich hinzugefügt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<StundenplanKalenderwochenzuordnung>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Kalenderwochenzuordnungen für einen Stundenplan anzulegen.
	 *   Code 404: Die Stundenplandaten wurden nicht gefunden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<Partial<StundenplanKalenderwochenzuordnung>>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Kalenderwochenzuordnungen wurden erfolgreich hinzugefügt.
	 */
	public async addStundenplanKalenderwochenzuordnungen(data : List<Partial<StundenplanKalenderwochenzuordnung>>, schema : string, id : number) : Promise<List<StundenplanKalenderwochenzuordnung>> {
		const path = "/db/{schema}/stundenplan/{id : \\d+}/kalenderwochen/create/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = "[" + (data.toArray() as Array<StundenplanKalenderwochenzuordnung>).map(d => StundenplanKalenderwochenzuordnung.transpilerToJSONPatch(d)).join() + "]";
		const result : string = await super.postJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<StundenplanKalenderwochenzuordnung>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(StundenplanKalenderwochenzuordnung.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der DELETE-Methode deleteStundenplanKalenderwochenzuordnungen für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/{id : \d+}/kalenderwochen/delete/multiple
	 *
	 * Entfernt mehrere Kalenderwochenzuordnungen eines Stundenplans.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Stundenplans hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Kalenderwochenzuordnungen wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<StundenplanKalenderwochenzuordnung>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Stundenplan zu bearbeiten.
	 *   Code 404: Mindestens eine Kalenderwochenzuordnung ist nicht vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Kalenderwochenzuordnungen wurde erfolgreich entfernt.
	 */
	public async deleteStundenplanKalenderwochenzuordnungen(data : List<number>, schema : string, id : number) : Promise<List<StundenplanKalenderwochenzuordnung>> {
		const path = "/db/{schema}/stundenplan/{id : \\d+}/kalenderwochen/delete/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<StundenplanKalenderwochenzuordnung>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(StundenplanKalenderwochenzuordnung.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der PATCH-Methode patchStundenplanKalenderwochenzuordnungen für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/{id : \d+}/kalenderwochen/patch/multiple
	 *
	 * Passt die Kalenderwochen-Zuordnungen eines Stundenplans an. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ändern von Stundenplandaten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Daten zu ändern.
	 *   Code 404: Kein Eintrag für mindestens eine der IDs der Daten gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<Partial<StundenplanKalenderwochenzuordnung>>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchStundenplanKalenderwochenzuordnungen(data : List<Partial<StundenplanKalenderwochenzuordnung>>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/stundenplan/{id : \\d+}/kalenderwochen/patch/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = "[" + (data.toArray() as Array<StundenplanKalenderwochenzuordnung>).map(d => StundenplanKalenderwochenzuordnung.transpilerToJSONPatch(d)).join() + "]";
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der POST-Methode addStundenplanPausenaufsicht für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/{id : \d+}/pausenaufsicht/create
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
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Pausenaufsicht wurde erfolgreich hinzugefügt.
	 */
	public async addStundenplanPausenaufsicht(data : Partial<StundenplanPausenaufsicht>, schema : string, id : number) : Promise<StundenplanPausenaufsicht> {
		const path = "/db/{schema}/stundenplan/{id : \\d+}/pausenaufsicht/create"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = StundenplanPausenaufsicht.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return StundenplanPausenaufsicht.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode addStundenplanPausenaufsichten für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/{id : \d+}/pausenaufsicht/create/multiple
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
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Pausenaufsichten wurden erfolgreich hinzugefügt.
	 */
	public async addStundenplanPausenaufsichten(data : List<Partial<StundenplanPausenaufsicht>>, schema : string, id : number) : Promise<List<StundenplanPausenaufsicht>> {
		const path = "/db/{schema}/stundenplan/{id : \\d+}/pausenaufsicht/create/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = "[" + (data.toArray() as Array<StundenplanPausenaufsicht>).map(d => StundenplanPausenaufsicht.transpilerToJSONPatch(d)).join() + "]";
		const result : string = await super.postJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<StundenplanPausenaufsicht>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(StundenplanPausenaufsicht.transpilerFromJSON(text)); });
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<StundenplanPausenaufsicht>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(StundenplanPausenaufsicht.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode updateStundenplanPausenaufsichtenBereiche für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/{id : \d+}/pausenaufsichtenbereiche/update
	 *
	 * Entfernt und fügt mehrere Zuordungen von Pausenaufsichten zu Aufsichtsbereichen ggf. in Abhängigkeit von einem Wochentyp hinzu. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Entfernen und Hinzufügen besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Zuordnungen wurden erfolgreich gelöscht bzw. hinzugefügt. Das Ergebnis beinhaltet die erstellten Zuordnungen
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<StundenplanPausenaufsichtBereich>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Zuordnungen zu löschen oder hinzufügen.
	 *   Code 404: Ein Wert für das Erstellen der Zuordnungen wurde nicht gefunden.
	 *
	 * @param {StundenplanPausenaufsichtBereichUpdate} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Zuordnungen wurden erfolgreich gelöscht bzw. hinzugefügt. Das Ergebnis beinhaltet die erstellten Zuordnungen
	 */
	public async updateStundenplanPausenaufsichtenBereiche(data : StundenplanPausenaufsichtBereichUpdate, schema : string, id : number) : Promise<List<StundenplanPausenaufsichtBereich>> {
		const path = "/db/{schema}/stundenplan/{id : \\d+}/pausenaufsichtenbereiche/update"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = StundenplanPausenaufsichtBereichUpdate.transpilerToJSON(data);
		const result : string = await super.postJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<StundenplanPausenaufsichtBereich>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(StundenplanPausenaufsichtBereich.transpilerFromJSON(text)); });
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{idStundenplan\s*(:[^{}]+({[^{}]+})*)?}/g, idStundenplan.toString())
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return StundenplanLehrer.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchStundenplanPausenaufsicht für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/{idStundenplan : \d+}/pausenaufsicht/{id : \d+}
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
	 * @param {number} idStundenplan - der Pfad-Parameter idStundenplan
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchStundenplanPausenaufsicht(data : Partial<StundenplanPausenaufsicht>, schema : string, idStundenplan : number, id : number) : Promise<void> {
		const path = "/db/{schema}/stundenplan/{idStundenplan : \\d+}/pausenaufsicht/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{idStundenplan\s*(:[^{}]+({[^{}]+})*)?}/g, idStundenplan.toString())
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = StundenplanPausenaufsicht.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getStundenplanlisteAktivFuerAbschnitt für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/aktiv/liste/{abschnitt : \d+}
	 *
	 * Erstellt eine Liste der aktiven Stundenpläne des angegebenen Schuljahresabschnitts. Die Stundenpläne sind anhand der Gültigkeit sortiert.Es wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Ansehen von Stundenplanlisten besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste der aktiven Stundenpläne
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<StundenplanListeEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Stundenplanlisten anzusehen.
	 *   Code 404: Keine Stundenpläne gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} abschnitt - der Pfad-Parameter abschnitt
	 *
	 * @returns Eine Liste der aktiven Stundenpläne
	 */
	public async getStundenplanlisteAktivFuerAbschnitt(schema : string, abschnitt : number) : Promise<List<StundenplanListeEintrag>> {
		const path = "/db/{schema}/stundenplan/aktiv/liste/{abschnitt : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abschnitt\s*(:[^{}]+({[^{}]+})*)?}/g, abschnitt.toString());
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<StundenplanListeEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(StundenplanListeEintrag.transpilerFromJSON(text)); });
		return ret;
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return StundenplanAufsichtsbereich.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode addStundenplan für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/create
	 *
	 * Erstellt einen neuen Stundenplan und gibt die zugehörigen Daten zurück. Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Erstellen eines Stundenplans besitzt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Der Stundenplan wurde erfolgreich erstellt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Stundenplan
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Stundenplan anzulegen.
	 *   Code 404: Benötigte Daten wurden nicht gefunden
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<Stundenplan>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Stundenplan wurde erfolgreich erstellt.
	 */
	public async addStundenplan(data : Partial<Stundenplan>, schema : string) : Promise<Stundenplan> {
		const path = "/db/{schema}/stundenplan/create"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = Stundenplan.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return Stundenplan.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der DELETE-Methode deleteStundenplaene für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/delete/multiple
	 *
	 * Entfernt mehrere Stundenpläne. Dabei wird geprüft, ob alle Vorbedingungen zum Entfernender Stundenpläne erfüllt sind und der SVWS-Benutzer die notwendige Berechtigung hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Lösch-Operationen wurden ausgeführt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<SimpleOperationResponse>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um Stundenpläne zu entfernen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Lösch-Operationen wurden ausgeführt.
	 */
	public async deleteStundenplaene(data : List<number>, schema : string) : Promise<List<SimpleOperationResponse>> {
		const path = "/db/{schema}/stundenplan/delete/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<SimpleOperationResponse>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(SimpleOperationResponse.transpilerFromJSON(text)); });
		return ret;
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = StundenplanKalenderwochenzuordnung.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteStundenplanKalenderwochenzuordnung für den Zugriff auf die URL https://{hostname}/db/{schema}/stundenplan/kalenderwochen/{id : \d+}
	 *
	 * Entfernt eine Kalenderwochenzuordnung eines Stundenplans.Dabei wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Bearbeiten eines Stundenplans hat.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Kalenderwochenzuordnung wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: StundenplanKalenderwochenzuordnung
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um einen Stundenplan zu bearbeiten.
	 *   Code 404: Keine Kalenderwochenzuordnung vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Die Kalenderwochenzuordnung wurde erfolgreich entfernt.
	 */
	public async deleteStundenplanKalenderwochenzuordnung(schema : string, id : number) : Promise<StundenplanKalenderwochenzuordnung> {
		const path = "/db/{schema}/stundenplan/kalenderwochen/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return StundenplanKalenderwochenzuordnung.transpilerFromJSON(text);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{abschnitt\s*(:[^{}]+({[^{}]+})*)?}/g, abschnitt.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return StundenplanPausenaufsicht.transpilerFromJSON(text);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return StundenplanPausenaufsicht.transpilerFromJSON(text);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString())
			.replace(/{klasse_id\s*(:[^{}]+({[^{}]+})*)?}/g, klasse_id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString())
			.replace(/{lehrer_id\s*(:[^{}]+({[^{}]+})*)?}/g, lehrer_id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString())
			.replace(/{schueler_id\s*(:[^{}]+({[^{}]+})*)?}/g, schueler_id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
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
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<StundenplanZeitraster>).map(d => StundenplanZeitraster.transpilerToJSONPatch(d)).join() + "]";
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der GET-Methode getWiedervorlageEintrag für den Zugriff auf die URL https://{hostname}/db/{schema}/wiedervorlage/{id : \d+}
	 *
	 * Liefert zu der ID den zugehörigen Wiedervorlage-Eintrag. Dabei wird geprüft, ob der Benutzer auf den Eintrag zugreifen darf.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Wiedervorlage-Eintrag
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: WiedervorlageEintrag
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den WiedervorlageEintrag anzusehen.
	 *   Code 404: Kein WiedervorlageEintrag mit der angegebenen ID gefunden
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Wiedervorlage-Eintrag
	 */
	public async getWiedervorlageEintrag(schema : string, id : number) : Promise<WiedervorlageEintrag> {
		const path = "/db/{schema}/wiedervorlage/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.getJSON(path);
		const text = result;
		return WiedervorlageEintrag.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der PATCH-Methode patchWiedervorlageEintrag für den Zugriff auf die URL https://{hostname}/db/{schema}/wiedervorlage/{id : \d+}
	 *
	 * Passt den Wiedervorlage-Eintrag mit der angegebenen ID an. Dabei wird geprüft, ob der Benutzer auf den Eintrag zugreifen darf.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Patch wurde erfolgreich in den Wiedervorlage-Eintrag integriert.
	 *   Code 400: Der Patch ist fehlerhaft aufgebaut.
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Wiedervorlage-Eintrag zu ändern.
	 *   Code 404: Kein Wiedervorlage-Eintrag mit der angegebenen ID gefunden
	 *   Code 409: Der Patch ist fehlerhaft, da zumindest eine Rahmenbedingung für einen Wert nicht erfüllt wurde (z.B. eine negative ID)
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<WiedervorlageEintrag>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 */
	public async patchWiedervorlageEintrag(data : Partial<WiedervorlageEintrag>, schema : string, id : number) : Promise<void> {
		const path = "/db/{schema}/wiedervorlage/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const body : string = WiedervorlageEintrag.transpilerToJSONPatch(data);
		return super.patchJSON(path, body);
	}


	/**
	 * Implementierung der DELETE-Methode deleteWiedervorlageEintrag für den Zugriff auf die URL https://{hostname}/db/{schema}/wiedervorlage/{id : \d+}
	 *
	 * Entfernt einen Wiedervorlage-Eintrag. Dabei wird geprüft, ob der Benutzer auf den Eintrag zugreifen darf.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Wiedervorlage-Eintrag wurde erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: WiedervorlageEintrag
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um dem Wiedervorlage-Eintrag zu löschen.
	 *   Code 404: Kein Wiedervorlage-Eintrag mit der angegebenen ID gefunden.
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Wiedervorlage-Eintrag wurde erfolgreich entfernt.
	 */
	public async deleteWiedervorlageEintrag(schema : string, id : number) : Promise<WiedervorlageEintrag> {
		const path = "/db/{schema}/wiedervorlage/{id : \\d+}"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.deleteJSON(path, null);
		const text = result;
		return WiedervorlageEintrag.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der POST-Methode setWiedervorlageEintragErledigt für den Zugriff auf die URL https://{hostname}/db/{schema}/wiedervorlage/{id : \d+}/erledigt
	 *
	 * Markiert den Wiedervorlage-Eintrag mit der angegebenen ID als erledigt.Dabei wird geprüft, ob der Benutzer auf den Eintrag zugreifen darf.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Der Wiedervorlage-Eintrag
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: WiedervorlageEintrag
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Wiedervorlage-Eintrag als erledigt zu markieren.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 * @param {number} id - der Pfad-Parameter id
	 *
	 * @returns Der Wiedervorlage-Eintrag
	 */
	public async setWiedervorlageEintragErledigt(schema : string, id : number) : Promise<WiedervorlageEintrag> {
		const path = "/db/{schema}/wiedervorlage/{id : \\d+}/erledigt"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema)
			.replace(/{id\s*(:[^{}]+({[^{}]+})*)?}/g, id.toString());
		const result : string = await super.postJSON(path, null);
		const text = result;
		return WiedervorlageEintrag.transpilerFromJSON(text);
	}


	/**
	 * Implementierung der DELETE-Methode deleteWiedervorlageEintraege für den Zugriff auf die URL https://{hostname}/db/{schema}/wiedervorlage/delete/multiple
	 *
	 * Entfernt mehrere Wiedervorlage-Einträge. Dabei wird geprüft, ob der Benutzer auf die Einträge zugreifen darf.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Wiedervorlage-Einträge wurden erfolgreich entfernt.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<WiedervorlageEintrag>
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um die Wiedervorlage-Einträge zu entfernen.
	 *   Code 404: Wiedervorlage-Eintrag nicht vorhanden
	 *   Code 409: Die übergebenen Daten sind fehlerhaft
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {List<number>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Die Wiedervorlage-Einträge wurden erfolgreich entfernt.
	 */
	public async deleteWiedervorlageEintraege(data : List<number>, schema : string) : Promise<List<WiedervorlageEintrag>> {
		const path = "/db/{schema}/wiedervorlage/delete/multiple"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = "[" + (data.toArray() as Array<number>).map(d => JSON.stringify(d)).join() + "]";
		const result : string = await super.deleteJSON(path, body);
		const obj = JSON.parse(result);
		const ret = new ArrayList<WiedervorlageEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(WiedervorlageEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der GET-Methode getWiedervorlageListe für den Zugriff auf die URL https://{hostname}/db/{schema}/wiedervorlage/liste
	 *
	 * Gibt die Liste der Wiedervorlage des angemeldeteten Benutzers zurück. Dabei werden auch die Einträge berücksichtigt, wo der angemeldete Benutzer in einer zugeordeten Benutzergruppe des Wiedervorlage-Eintrags enthalten ist.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Eine Liste mit den Einträgen der Wiedervorlage.
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: List<WiedervorlageEintrag>
	 *
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Eine Liste mit den Einträgen der Wiedervorlage.
	 */
	public async getWiedervorlageListe(schema : string) : Promise<List<WiedervorlageEintrag>> {
		const path = "/db/{schema}/wiedervorlage/liste"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const result : string = await super.getJSON(path);
		const obj = JSON.parse(result);
		const ret = new ArrayList<WiedervorlageEintrag>();
		obj.forEach((elem: any) => { const text : string = JSON.stringify(elem); ret.add(WiedervorlageEintrag.transpilerFromJSON(text)); });
		return ret;
	}


	/**
	 * Implementierung der POST-Methode addWiedervorlageEintrag für den Zugriff auf die URL https://{hostname}/db/{schema}/wiedervorlage/neu
	 *
	 * Erstellt einen Wiedervorlage-Eintrag.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 201: Der Wiedervorlage-Eintrag
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: WiedervorlageEintrag
	 *   Code 403: Der SVWS-Benutzer hat keine Rechte, um den Wiedervorlage-Eintrag anzulegen.
	 *   Code 500: Unspezifizierter Fehler (z.B. beim Datenbankzugriff)
	 *
	 * @param {Partial<WiedervorlageEintrag>} data - der Request-Body für die HTTP-Methode
	 * @param {string} schema - der Pfad-Parameter schema
	 *
	 * @returns Der Wiedervorlage-Eintrag
	 */
	public async addWiedervorlageEintrag(data : Partial<WiedervorlageEintrag>, schema : string) : Promise<WiedervorlageEintrag> {
		const path = "/db/{schema}/wiedervorlage/neu"
			.replace(/{schema\s*(:[^{}]+({[^{}]+})*)?}/g, schema);
		const body : string = WiedervorlageEintrag.transpilerToJSONPatch(data);
		const result : string = await super.postJSON(path, body);
		const text = result;
		return WiedervorlageEintrag.transpilerFromJSON(text);
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
	 * Implementierung der GET-Methode isAlivePrivileged für den Zugriff auf die URL https://{hostname}/status/alive/privileged
	 *
	 * Eine Test-Methode zum Prüfen, ob die Privileged-API des Serves erreichbar ist oder nicht.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Der Server ist über die Privileged API erreichbar!
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Boolean
	 *
	 * @returns Der Server ist über die Privileged API erreichbar!
	 */
	public async isAlivePrivileged() : Promise<boolean | null> {
		const path = "/status/alive/privileged";
		const result : string = await super.getJSON(path);
		const text = result;
		return (text === "true");
	}


	/**
	 * Implementierung der GET-Methode getServerDBRevision für den Zugriff auf die URL https://{hostname}/status/db/revision
	 *
	 * Gibt Datenbank-Revision zurück, welche der SVWS-Server unterstützt.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die Datenbank-Revision
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: Long
	 *
	 * @returns Die Datenbank-Revision
	 */
	public async getServerDBRevision() : Promise<number | null> {
		const path = "/status/db/revision";
		const result : string = await super.getJSON(path);
		const text = result;
		return parseFloat(JSON.parse(text));
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
	 * Implementierung der GET-Methode getSchildMinVersion für den Zugriff auf die URL https://{hostname}/status/schild/minversion
	 *
	 * Gibt die erste kompatible Schild-Version zurück.
	 *
	 * Mögliche HTTP-Antworten:
	 *   Code 200: Die erste kompatible Schild-Version
	 *     - Mime-Type: application/json
	 *     - Rückgabe-Typ: String
	 *
	 * @returns Die erste kompatible Schild-Version
	 */
	public async getSchildMinVersion() : Promise<string | null> {
		const path = "/status/schild/minversion";
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
