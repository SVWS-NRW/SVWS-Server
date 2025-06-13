import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchuelerStammdaten extends JavaObject {

	/**
	 * Die ID des Schülerdatensatzes.
	 */
	public id : number = 0;

	/**
	 * Das Foto (in Base64 kodiert) des Schülerdatensatzes.
	 */
	public foto : string | null = null;

	/**
	 * Der Nachname des Schülerdatensatzes.
	 */
	public nachname : string = "";

	/**
	 * Der Vorname des Schülerdatensatzes.
	 */
	public vorname : string = "";

	/**
	 * Alle Vornamen, sofern es mehrere gibt, des Schülerdatensatzes.
	 */
	public alleVornamen : string = "";

	/**
	 * Die ID des Geschlechtes
	 */
	public geschlecht : number = 0;

	/**
	 * Das Geburtsdatum des Schülerdatensatzes.
	 */
	public geburtsdatum : string | null = null;

	/**
	 * Der Geburtsort des Schülerdatensatzes.
	 */
	public geburtsort : string | null = null;

	/**
	 * Der Geburtsname des Schülerdatensatzes.
	 */
	public geburtsname : string | null = null;

	/**
	 * Ggf. der Straßenname im Wohnort des Schülers.
	 */
	public strassenname : string | null = null;

	/**
	 * Ggf. die Hausnummer zur Straße im Wohnort des Schülers.
	 */
	public hausnummer : string | null = null;

	/**
	 * Ggf. der Hausnummerzusatz zur Straße im Wohnort des Schülers.
	 */
	public hausnummerZusatz : string | null = null;

	/**
	 * Die ID des Wohnortes des Schülerdatensatzes.
	 */
	public wohnortID : number | null = null;

	/**
	 * Die ID des Ortsteils des Schülerdatensatzes.
	 */
	public ortsteilID : number | null = null;

	/**
	 * Die Telefonnummer des Schülerdatensatzes.
	 */
	public telefon : string | null = null;

	/**
	 * Die Mobilnummer des Schülerdatensatzes.
	 */
	public telefonMobil : string | null = null;

	/**
	 * Die private Email-Adresse des Schülerdatensatzes.
	 */
	public emailPrivat : string | null = null;

	/**
	 * Die schulische Email-Adresse des Schülerdatensatzes.
	 */
	public emailSchule : string | null = null;

	/**
	 * Die ID der Staatsangehörigkeit des Schülerdatensatzes.
	 */
	public staatsangehoerigkeitID : string | null = null;

	/**
	 * Die ID einer zweiten Staatsangehörigkeit des Schülerdatensatzes.
	 */
	public staatsangehoerigkeit2ID : string | null = null;

	/**
	 * Die ID der Religion des Schülerdatensatzes.
	 */
	public religionID : number | null = null;

	/**
	 * Gibt an, ob die Konfession bei dem Schülerdatensatz auf dem Zeugnis erscheinen soll.
	 */
	public druckeKonfessionAufZeugnisse : boolean = false;

	/**
	 * Das Datum der Religionsabmeldung des Schülerdatensatzes.
	 */
	public religionabmeldung : string | null = null;

	/**
	 * Das Datum der Religionsanmeldung des Schülerdatensatzes.
	 */
	public religionanmeldung : string | null = null;

	/**
	 * Gibt an, ob ein Migrationshintergrund bei dems Schülerdatensatz vorhanden ist.
	 */
	public hatMigrationshintergrund : boolean = false;

	/**
	 * Das Zuzugsjahr des Schülerdatensatzes.
	 */
	public zuzugsjahr : number | null = null;

	/**
	 * Das Geburtsland des Schülerdatensatzes.
	 */
	public geburtsland : string | null = null;

	/**
	 * Die Verkehrssprache der Familie des Schülerdatensatzes.
	 */
	public verkehrspracheFamilie : string | null = null;

	/**
	 * Das Geburtsland des Vaters des Schülerdatensatzes.
	 */
	public geburtslandVater : string | null = null;

	/**
	 * Das Geburtsland der Mutter des Schülerdatensatzes.
	 */
	public geburtslandMutter : string | null = null;

	/**
	 * Die ID des Status des Schülerdatensatzes.
	 */
	public status : number = 0;

	/**
	 * Gibt an, ob es sich bei dem Schülerdatensatz um ein Duplikat handelt oder nicht.
	 */
	public istDuplikat : boolean = false;

	/**
	 * Das Schulnummer bei einem externen Schüler oder null, wenn der Schüler kein externer Schüler ist.
	 */
	public externeSchulNr : string | null = null;

	/**
	 * Die ID der Art des Fahrschülers des Schülerdatensatzes.
	 */
	public fahrschuelerArtID : number | null = null;

	/**
	 * Die ID der Haltestelle, ab der der Schüler das Transportmittel nimmt, des Schülerdatensatzes.
	 */
	public haltestelleID : number | null = null;

	/**
	 * Das Anmeldedatum des Schülerdatensatzes.
	 */
	public anmeldedatum : string | null = null;

	/**
	 * Das Aufnahmedatum des Schülerdatensatzes.
	 */
	public aufnahmedatum : string | null = null;

	/**
	 * Gibt an, ob der Schüler volljährig ist oder nicht.
	 */
	public istVolljaehrig : boolean = false;

	/**
	 * Gibt an, ob der Schüler die Schulpflicht erfüllt hat oder nicht.
	 */
	public istSchulpflichtErfuellt : boolean = false;

	/**
	 * Gibt an, ob der Schüler die Berufsschulpflicht erfüllt hat oder nicht.
	 */
	public istBerufsschulpflichtErfuellt : boolean = false;

	/**
	 * Gibt an, ob der Schüler einen Nachweis über die Maserimpfpflicht erbracht hat.
	 */
	public hatMasernimpfnachweis : boolean = false;

	/**
	 * Gibt an, ob über den Schüler eine Auskunft an dritte erteilt werden darf oder dies unter allen Umständen vermieden werden sollte.
	 */
	public keineAuskunftAnDritte : boolean = false;

	/**
	 * Gibt an, ob der Schüler BAFÖG erhält oder nicht.
	 */
	public erhaeltSchuelerBAFOEG : boolean = false;

	/**
	 * Gibt an, ob der Schüler Meister-BAFÖG erhält oder nicht.
	 */
	public erhaeltMeisterBAFOEG : boolean = false;

	/**
	 * Der Beginn des Bildungsgangs eines Schülers.
	 */
	public beginnBildungsgang : string | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.schueler.SchuelerStammdaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.schueler.SchuelerStammdaten'].includes(name);
	}

	public static class = new Class<SchuelerStammdaten>('de.svws_nrw.asd.data.schueler.SchuelerStammdaten');

	public static transpilerFromJSON(json : string): SchuelerStammdaten {
		const obj = JSON.parse(json) as Partial<SchuelerStammdaten>;
		const result = new SchuelerStammdaten();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.foto = (obj.foto === undefined) ? null : obj.foto === null ? null : obj.foto;
		if (obj.nachname === undefined)
			throw new Error('invalid json format, missing attribute nachname');
		result.nachname = obj.nachname;
		if (obj.vorname === undefined)
			throw new Error('invalid json format, missing attribute vorname');
		result.vorname = obj.vorname;
		if (obj.alleVornamen === undefined)
			throw new Error('invalid json format, missing attribute alleVornamen');
		result.alleVornamen = obj.alleVornamen;
		if (obj.geschlecht === undefined)
			throw new Error('invalid json format, missing attribute geschlecht');
		result.geschlecht = obj.geschlecht;
		result.geburtsdatum = (obj.geburtsdatum === undefined) ? null : obj.geburtsdatum === null ? null : obj.geburtsdatum;
		result.geburtsort = (obj.geburtsort === undefined) ? null : obj.geburtsort === null ? null : obj.geburtsort;
		result.geburtsname = (obj.geburtsname === undefined) ? null : obj.geburtsname === null ? null : obj.geburtsname;
		result.strassenname = (obj.strassenname === undefined) ? null : obj.strassenname === null ? null : obj.strassenname;
		result.hausnummer = (obj.hausnummer === undefined) ? null : obj.hausnummer === null ? null : obj.hausnummer;
		result.hausnummerZusatz = (obj.hausnummerZusatz === undefined) ? null : obj.hausnummerZusatz === null ? null : obj.hausnummerZusatz;
		result.wohnortID = (obj.wohnortID === undefined) ? null : obj.wohnortID === null ? null : obj.wohnortID;
		result.ortsteilID = (obj.ortsteilID === undefined) ? null : obj.ortsteilID === null ? null : obj.ortsteilID;
		result.telefon = (obj.telefon === undefined) ? null : obj.telefon === null ? null : obj.telefon;
		result.telefonMobil = (obj.telefonMobil === undefined) ? null : obj.telefonMobil === null ? null : obj.telefonMobil;
		result.emailPrivat = (obj.emailPrivat === undefined) ? null : obj.emailPrivat === null ? null : obj.emailPrivat;
		result.emailSchule = (obj.emailSchule === undefined) ? null : obj.emailSchule === null ? null : obj.emailSchule;
		result.staatsangehoerigkeitID = (obj.staatsangehoerigkeitID === undefined) ? null : obj.staatsangehoerigkeitID === null ? null : obj.staatsangehoerigkeitID;
		result.staatsangehoerigkeit2ID = (obj.staatsangehoerigkeit2ID === undefined) ? null : obj.staatsangehoerigkeit2ID === null ? null : obj.staatsangehoerigkeit2ID;
		result.religionID = (obj.religionID === undefined) ? null : obj.religionID === null ? null : obj.religionID;
		if (obj.druckeKonfessionAufZeugnisse === undefined)
			throw new Error('invalid json format, missing attribute druckeKonfessionAufZeugnisse');
		result.druckeKonfessionAufZeugnisse = obj.druckeKonfessionAufZeugnisse;
		result.religionabmeldung = (obj.religionabmeldung === undefined) ? null : obj.religionabmeldung === null ? null : obj.religionabmeldung;
		result.religionanmeldung = (obj.religionanmeldung === undefined) ? null : obj.religionanmeldung === null ? null : obj.religionanmeldung;
		if (obj.hatMigrationshintergrund === undefined)
			throw new Error('invalid json format, missing attribute hatMigrationshintergrund');
		result.hatMigrationshintergrund = obj.hatMigrationshintergrund;
		result.zuzugsjahr = (obj.zuzugsjahr === undefined) ? null : obj.zuzugsjahr === null ? null : obj.zuzugsjahr;
		result.geburtsland = (obj.geburtsland === undefined) ? null : obj.geburtsland === null ? null : obj.geburtsland;
		result.verkehrspracheFamilie = (obj.verkehrspracheFamilie === undefined) ? null : obj.verkehrspracheFamilie === null ? null : obj.verkehrspracheFamilie;
		result.geburtslandVater = (obj.geburtslandVater === undefined) ? null : obj.geburtslandVater === null ? null : obj.geburtslandVater;
		result.geburtslandMutter = (obj.geburtslandMutter === undefined) ? null : obj.geburtslandMutter === null ? null : obj.geburtslandMutter;
		if (obj.status === undefined)
			throw new Error('invalid json format, missing attribute status');
		result.status = obj.status;
		if (obj.istDuplikat === undefined)
			throw new Error('invalid json format, missing attribute istDuplikat');
		result.istDuplikat = obj.istDuplikat;
		result.externeSchulNr = (obj.externeSchulNr === undefined) ? null : obj.externeSchulNr === null ? null : obj.externeSchulNr;
		result.fahrschuelerArtID = (obj.fahrschuelerArtID === undefined) ? null : obj.fahrschuelerArtID === null ? null : obj.fahrschuelerArtID;
		result.haltestelleID = (obj.haltestelleID === undefined) ? null : obj.haltestelleID === null ? null : obj.haltestelleID;
		result.anmeldedatum = (obj.anmeldedatum === undefined) ? null : obj.anmeldedatum === null ? null : obj.anmeldedatum;
		result.aufnahmedatum = (obj.aufnahmedatum === undefined) ? null : obj.aufnahmedatum === null ? null : obj.aufnahmedatum;
		if (obj.istVolljaehrig === undefined)
			throw new Error('invalid json format, missing attribute istVolljaehrig');
		result.istVolljaehrig = obj.istVolljaehrig;
		if (obj.istSchulpflichtErfuellt === undefined)
			throw new Error('invalid json format, missing attribute istSchulpflichtErfuellt');
		result.istSchulpflichtErfuellt = obj.istSchulpflichtErfuellt;
		if (obj.istBerufsschulpflichtErfuellt === undefined)
			throw new Error('invalid json format, missing attribute istBerufsschulpflichtErfuellt');
		result.istBerufsschulpflichtErfuellt = obj.istBerufsschulpflichtErfuellt;
		if (obj.hatMasernimpfnachweis === undefined)
			throw new Error('invalid json format, missing attribute hatMasernimpfnachweis');
		result.hatMasernimpfnachweis = obj.hatMasernimpfnachweis;
		if (obj.keineAuskunftAnDritte === undefined)
			throw new Error('invalid json format, missing attribute keineAuskunftAnDritte');
		result.keineAuskunftAnDritte = obj.keineAuskunftAnDritte;
		if (obj.erhaeltSchuelerBAFOEG === undefined)
			throw new Error('invalid json format, missing attribute erhaeltSchuelerBAFOEG');
		result.erhaeltSchuelerBAFOEG = obj.erhaeltSchuelerBAFOEG;
		if (obj.erhaeltMeisterBAFOEG === undefined)
			throw new Error('invalid json format, missing attribute erhaeltMeisterBAFOEG');
		result.erhaeltMeisterBAFOEG = obj.erhaeltMeisterBAFOEG;
		result.beginnBildungsgang = (obj.beginnBildungsgang === undefined) ? null : obj.beginnBildungsgang === null ? null : obj.beginnBildungsgang;
		return result;
	}

	public static transpilerToJSON(obj : SchuelerStammdaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"foto" : ' + ((obj.foto === null) ? 'null' : JSON.stringify(obj.foto)) + ',';
		result += '"nachname" : ' + JSON.stringify(obj.nachname) + ',';
		result += '"vorname" : ' + JSON.stringify(obj.vorname) + ',';
		result += '"alleVornamen" : ' + JSON.stringify(obj.alleVornamen) + ',';
		result += '"geschlecht" : ' + obj.geschlecht.toString() + ',';
		result += '"geburtsdatum" : ' + ((obj.geburtsdatum === null) ? 'null' : JSON.stringify(obj.geburtsdatum)) + ',';
		result += '"geburtsort" : ' + ((obj.geburtsort === null) ? 'null' : JSON.stringify(obj.geburtsort)) + ',';
		result += '"geburtsname" : ' + ((obj.geburtsname === null) ? 'null' : JSON.stringify(obj.geburtsname)) + ',';
		result += '"strassenname" : ' + ((obj.strassenname === null) ? 'null' : JSON.stringify(obj.strassenname)) + ',';
		result += '"hausnummer" : ' + ((obj.hausnummer === null) ? 'null' : JSON.stringify(obj.hausnummer)) + ',';
		result += '"hausnummerZusatz" : ' + ((obj.hausnummerZusatz === null) ? 'null' : JSON.stringify(obj.hausnummerZusatz)) + ',';
		result += '"wohnortID" : ' + ((obj.wohnortID === null) ? 'null' : obj.wohnortID.toString()) + ',';
		result += '"ortsteilID" : ' + ((obj.ortsteilID === null) ? 'null' : obj.ortsteilID.toString()) + ',';
		result += '"telefon" : ' + ((obj.telefon === null) ? 'null' : JSON.stringify(obj.telefon)) + ',';
		result += '"telefonMobil" : ' + ((obj.telefonMobil === null) ? 'null' : JSON.stringify(obj.telefonMobil)) + ',';
		result += '"emailPrivat" : ' + ((obj.emailPrivat === null) ? 'null' : JSON.stringify(obj.emailPrivat)) + ',';
		result += '"emailSchule" : ' + ((obj.emailSchule === null) ? 'null' : JSON.stringify(obj.emailSchule)) + ',';
		result += '"staatsangehoerigkeitID" : ' + ((obj.staatsangehoerigkeitID === null) ? 'null' : JSON.stringify(obj.staatsangehoerigkeitID)) + ',';
		result += '"staatsangehoerigkeit2ID" : ' + ((obj.staatsangehoerigkeit2ID === null) ? 'null' : JSON.stringify(obj.staatsangehoerigkeit2ID)) + ',';
		result += '"religionID" : ' + ((obj.religionID === null) ? 'null' : obj.religionID.toString()) + ',';
		result += '"druckeKonfessionAufZeugnisse" : ' + obj.druckeKonfessionAufZeugnisse.toString() + ',';
		result += '"religionabmeldung" : ' + ((obj.religionabmeldung === null) ? 'null' : JSON.stringify(obj.religionabmeldung)) + ',';
		result += '"religionanmeldung" : ' + ((obj.religionanmeldung === null) ? 'null' : JSON.stringify(obj.religionanmeldung)) + ',';
		result += '"hatMigrationshintergrund" : ' + obj.hatMigrationshintergrund.toString() + ',';
		result += '"zuzugsjahr" : ' + ((obj.zuzugsjahr === null) ? 'null' : obj.zuzugsjahr.toString()) + ',';
		result += '"geburtsland" : ' + ((obj.geburtsland === null) ? 'null' : JSON.stringify(obj.geburtsland)) + ',';
		result += '"verkehrspracheFamilie" : ' + ((obj.verkehrspracheFamilie === null) ? 'null' : JSON.stringify(obj.verkehrspracheFamilie)) + ',';
		result += '"geburtslandVater" : ' + ((obj.geburtslandVater === null) ? 'null' : JSON.stringify(obj.geburtslandVater)) + ',';
		result += '"geburtslandMutter" : ' + ((obj.geburtslandMutter === null) ? 'null' : JSON.stringify(obj.geburtslandMutter)) + ',';
		result += '"status" : ' + obj.status.toString() + ',';
		result += '"istDuplikat" : ' + obj.istDuplikat.toString() + ',';
		result += '"externeSchulNr" : ' + ((obj.externeSchulNr === null) ? 'null' : JSON.stringify(obj.externeSchulNr)) + ',';
		result += '"fahrschuelerArtID" : ' + ((obj.fahrschuelerArtID === null) ? 'null' : obj.fahrschuelerArtID.toString()) + ',';
		result += '"haltestelleID" : ' + ((obj.haltestelleID === null) ? 'null' : obj.haltestelleID.toString()) + ',';
		result += '"anmeldedatum" : ' + ((obj.anmeldedatum === null) ? 'null' : JSON.stringify(obj.anmeldedatum)) + ',';
		result += '"aufnahmedatum" : ' + ((obj.aufnahmedatum === null) ? 'null' : JSON.stringify(obj.aufnahmedatum)) + ',';
		result += '"istVolljaehrig" : ' + obj.istVolljaehrig.toString() + ',';
		result += '"istSchulpflichtErfuellt" : ' + obj.istSchulpflichtErfuellt.toString() + ',';
		result += '"istBerufsschulpflichtErfuellt" : ' + obj.istBerufsschulpflichtErfuellt.toString() + ',';
		result += '"hatMasernimpfnachweis" : ' + obj.hatMasernimpfnachweis.toString() + ',';
		result += '"keineAuskunftAnDritte" : ' + obj.keineAuskunftAnDritte.toString() + ',';
		result += '"erhaeltSchuelerBAFOEG" : ' + obj.erhaeltSchuelerBAFOEG.toString() + ',';
		result += '"erhaeltMeisterBAFOEG" : ' + obj.erhaeltMeisterBAFOEG.toString() + ',';
		result += '"beginnBildungsgang" : ' + ((obj.beginnBildungsgang === null) ? 'null' : JSON.stringify(obj.beginnBildungsgang)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerStammdaten>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.foto !== undefined) {
			result += '"foto" : ' + ((obj.foto === null) ? 'null' : JSON.stringify(obj.foto)) + ',';
		}
		if (obj.nachname !== undefined) {
			result += '"nachname" : ' + JSON.stringify(obj.nachname) + ',';
		}
		if (obj.vorname !== undefined) {
			result += '"vorname" : ' + JSON.stringify(obj.vorname) + ',';
		}
		if (obj.alleVornamen !== undefined) {
			result += '"alleVornamen" : ' + JSON.stringify(obj.alleVornamen) + ',';
		}
		if (obj.geschlecht !== undefined) {
			result += '"geschlecht" : ' + obj.geschlecht.toString() + ',';
		}
		if (obj.geburtsdatum !== undefined) {
			result += '"geburtsdatum" : ' + ((obj.geburtsdatum === null) ? 'null' : JSON.stringify(obj.geburtsdatum)) + ',';
		}
		if (obj.geburtsort !== undefined) {
			result += '"geburtsort" : ' + ((obj.geburtsort === null) ? 'null' : JSON.stringify(obj.geburtsort)) + ',';
		}
		if (obj.geburtsname !== undefined) {
			result += '"geburtsname" : ' + ((obj.geburtsname === null) ? 'null' : JSON.stringify(obj.geburtsname)) + ',';
		}
		if (obj.strassenname !== undefined) {
			result += '"strassenname" : ' + ((obj.strassenname === null) ? 'null' : JSON.stringify(obj.strassenname)) + ',';
		}
		if (obj.hausnummer !== undefined) {
			result += '"hausnummer" : ' + ((obj.hausnummer === null) ? 'null' : JSON.stringify(obj.hausnummer)) + ',';
		}
		if (obj.hausnummerZusatz !== undefined) {
			result += '"hausnummerZusatz" : ' + ((obj.hausnummerZusatz === null) ? 'null' : JSON.stringify(obj.hausnummerZusatz)) + ',';
		}
		if (obj.wohnortID !== undefined) {
			result += '"wohnortID" : ' + ((obj.wohnortID === null) ? 'null' : obj.wohnortID.toString()) + ',';
		}
		if (obj.ortsteilID !== undefined) {
			result += '"ortsteilID" : ' + ((obj.ortsteilID === null) ? 'null' : obj.ortsteilID.toString()) + ',';
		}
		if (obj.telefon !== undefined) {
			result += '"telefon" : ' + ((obj.telefon === null) ? 'null' : JSON.stringify(obj.telefon)) + ',';
		}
		if (obj.telefonMobil !== undefined) {
			result += '"telefonMobil" : ' + ((obj.telefonMobil === null) ? 'null' : JSON.stringify(obj.telefonMobil)) + ',';
		}
		if (obj.emailPrivat !== undefined) {
			result += '"emailPrivat" : ' + ((obj.emailPrivat === null) ? 'null' : JSON.stringify(obj.emailPrivat)) + ',';
		}
		if (obj.emailSchule !== undefined) {
			result += '"emailSchule" : ' + ((obj.emailSchule === null) ? 'null' : JSON.stringify(obj.emailSchule)) + ',';
		}
		if (obj.staatsangehoerigkeitID !== undefined) {
			result += '"staatsangehoerigkeitID" : ' + ((obj.staatsangehoerigkeitID === null) ? 'null' : JSON.stringify(obj.staatsangehoerigkeitID)) + ',';
		}
		if (obj.staatsangehoerigkeit2ID !== undefined) {
			result += '"staatsangehoerigkeit2ID" : ' + ((obj.staatsangehoerigkeit2ID === null) ? 'null' : JSON.stringify(obj.staatsangehoerigkeit2ID)) + ',';
		}
		if (obj.religionID !== undefined) {
			result += '"religionID" : ' + ((obj.religionID === null) ? 'null' : obj.religionID.toString()) + ',';
		}
		if (obj.druckeKonfessionAufZeugnisse !== undefined) {
			result += '"druckeKonfessionAufZeugnisse" : ' + obj.druckeKonfessionAufZeugnisse.toString() + ',';
		}
		if (obj.religionabmeldung !== undefined) {
			result += '"religionabmeldung" : ' + ((obj.religionabmeldung === null) ? 'null' : JSON.stringify(obj.religionabmeldung)) + ',';
		}
		if (obj.religionanmeldung !== undefined) {
			result += '"religionanmeldung" : ' + ((obj.religionanmeldung === null) ? 'null' : JSON.stringify(obj.religionanmeldung)) + ',';
		}
		if (obj.hatMigrationshintergrund !== undefined) {
			result += '"hatMigrationshintergrund" : ' + obj.hatMigrationshintergrund.toString() + ',';
		}
		if (obj.zuzugsjahr !== undefined) {
			result += '"zuzugsjahr" : ' + ((obj.zuzugsjahr === null) ? 'null' : obj.zuzugsjahr.toString()) + ',';
		}
		if (obj.geburtsland !== undefined) {
			result += '"geburtsland" : ' + ((obj.geburtsland === null) ? 'null' : JSON.stringify(obj.geburtsland)) + ',';
		}
		if (obj.verkehrspracheFamilie !== undefined) {
			result += '"verkehrspracheFamilie" : ' + ((obj.verkehrspracheFamilie === null) ? 'null' : JSON.stringify(obj.verkehrspracheFamilie)) + ',';
		}
		if (obj.geburtslandVater !== undefined) {
			result += '"geburtslandVater" : ' + ((obj.geburtslandVater === null) ? 'null' : JSON.stringify(obj.geburtslandVater)) + ',';
		}
		if (obj.geburtslandMutter !== undefined) {
			result += '"geburtslandMutter" : ' + ((obj.geburtslandMutter === null) ? 'null' : JSON.stringify(obj.geburtslandMutter)) + ',';
		}
		if (obj.status !== undefined) {
			result += '"status" : ' + obj.status.toString() + ',';
		}
		if (obj.istDuplikat !== undefined) {
			result += '"istDuplikat" : ' + obj.istDuplikat.toString() + ',';
		}
		if (obj.externeSchulNr !== undefined) {
			result += '"externeSchulNr" : ' + ((obj.externeSchulNr === null) ? 'null' : JSON.stringify(obj.externeSchulNr)) + ',';
		}
		if (obj.fahrschuelerArtID !== undefined) {
			result += '"fahrschuelerArtID" : ' + ((obj.fahrschuelerArtID === null) ? 'null' : obj.fahrschuelerArtID.toString()) + ',';
		}
		if (obj.haltestelleID !== undefined) {
			result += '"haltestelleID" : ' + ((obj.haltestelleID === null) ? 'null' : obj.haltestelleID.toString()) + ',';
		}
		if (obj.anmeldedatum !== undefined) {
			result += '"anmeldedatum" : ' + ((obj.anmeldedatum === null) ? 'null' : JSON.stringify(obj.anmeldedatum)) + ',';
		}
		if (obj.aufnahmedatum !== undefined) {
			result += '"aufnahmedatum" : ' + ((obj.aufnahmedatum === null) ? 'null' : JSON.stringify(obj.aufnahmedatum)) + ',';
		}
		if (obj.istVolljaehrig !== undefined) {
			result += '"istVolljaehrig" : ' + obj.istVolljaehrig.toString() + ',';
		}
		if (obj.istSchulpflichtErfuellt !== undefined) {
			result += '"istSchulpflichtErfuellt" : ' + obj.istSchulpflichtErfuellt.toString() + ',';
		}
		if (obj.istBerufsschulpflichtErfuellt !== undefined) {
			result += '"istBerufsschulpflichtErfuellt" : ' + obj.istBerufsschulpflichtErfuellt.toString() + ',';
		}
		if (obj.hatMasernimpfnachweis !== undefined) {
			result += '"hatMasernimpfnachweis" : ' + obj.hatMasernimpfnachweis.toString() + ',';
		}
		if (obj.keineAuskunftAnDritte !== undefined) {
			result += '"keineAuskunftAnDritte" : ' + obj.keineAuskunftAnDritte.toString() + ',';
		}
		if (obj.erhaeltSchuelerBAFOEG !== undefined) {
			result += '"erhaeltSchuelerBAFOEG" : ' + obj.erhaeltSchuelerBAFOEG.toString() + ',';
		}
		if (obj.erhaeltMeisterBAFOEG !== undefined) {
			result += '"erhaeltMeisterBAFOEG" : ' + obj.erhaeltMeisterBAFOEG.toString() + ',';
		}
		if (obj.beginnBildungsgang !== undefined) {
			result += '"beginnBildungsgang" : ' + ((obj.beginnBildungsgang === null) ? 'null' : JSON.stringify(obj.beginnBildungsgang)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_schueler_SchuelerStammdaten(obj : unknown) : SchuelerStammdaten {
	return obj as SchuelerStammdaten;
}
