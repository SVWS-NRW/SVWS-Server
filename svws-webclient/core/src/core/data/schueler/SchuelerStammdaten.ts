import { JavaObject } from '../../../java/lang/JavaObject';

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
	 * Textfeld mit Bemerkungen zum Schülerdatensatz.
	 */
	public bemerkungen : string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schueler.SchuelerStammdaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schueler.SchuelerStammdaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuelerStammdaten {
		const obj = JSON.parse(json);
		const result = new SchuelerStammdaten();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.foto = typeof obj.foto === "undefined" ? null : obj.foto === null ? null : obj.foto;
		if (typeof obj.nachname === "undefined")
			 throw new Error('invalid json format, missing attribute nachname');
		result.nachname = obj.nachname;
		if (typeof obj.vorname === "undefined")
			 throw new Error('invalid json format, missing attribute vorname');
		result.vorname = obj.vorname;
		if (typeof obj.alleVornamen === "undefined")
			 throw new Error('invalid json format, missing attribute alleVornamen');
		result.alleVornamen = obj.alleVornamen;
		if (typeof obj.geschlecht === "undefined")
			 throw new Error('invalid json format, missing attribute geschlecht');
		result.geschlecht = obj.geschlecht;
		result.geburtsdatum = typeof obj.geburtsdatum === "undefined" ? null : obj.geburtsdatum === null ? null : obj.geburtsdatum;
		result.geburtsort = typeof obj.geburtsort === "undefined" ? null : obj.geburtsort === null ? null : obj.geburtsort;
		result.geburtsname = typeof obj.geburtsname === "undefined" ? null : obj.geburtsname === null ? null : obj.geburtsname;
		result.strassenname = typeof obj.strassenname === "undefined" ? null : obj.strassenname === null ? null : obj.strassenname;
		result.hausnummer = typeof obj.hausnummer === "undefined" ? null : obj.hausnummer === null ? null : obj.hausnummer;
		result.hausnummerZusatz = typeof obj.hausnummerZusatz === "undefined" ? null : obj.hausnummerZusatz === null ? null : obj.hausnummerZusatz;
		result.wohnortID = typeof obj.wohnortID === "undefined" ? null : obj.wohnortID === null ? null : obj.wohnortID;
		result.ortsteilID = typeof obj.ortsteilID === "undefined" ? null : obj.ortsteilID === null ? null : obj.ortsteilID;
		result.telefon = typeof obj.telefon === "undefined" ? null : obj.telefon === null ? null : obj.telefon;
		result.telefonMobil = typeof obj.telefonMobil === "undefined" ? null : obj.telefonMobil === null ? null : obj.telefonMobil;
		result.emailPrivat = typeof obj.emailPrivat === "undefined" ? null : obj.emailPrivat === null ? null : obj.emailPrivat;
		result.emailSchule = typeof obj.emailSchule === "undefined" ? null : obj.emailSchule === null ? null : obj.emailSchule;
		result.staatsangehoerigkeitID = typeof obj.staatsangehoerigkeitID === "undefined" ? null : obj.staatsangehoerigkeitID === null ? null : obj.staatsangehoerigkeitID;
		result.staatsangehoerigkeit2ID = typeof obj.staatsangehoerigkeit2ID === "undefined" ? null : obj.staatsangehoerigkeit2ID === null ? null : obj.staatsangehoerigkeit2ID;
		result.religionID = typeof obj.religionID === "undefined" ? null : obj.religionID === null ? null : obj.religionID;
		if (typeof obj.druckeKonfessionAufZeugnisse === "undefined")
			 throw new Error('invalid json format, missing attribute druckeKonfessionAufZeugnisse');
		result.druckeKonfessionAufZeugnisse = obj.druckeKonfessionAufZeugnisse;
		result.religionabmeldung = typeof obj.religionabmeldung === "undefined" ? null : obj.religionabmeldung === null ? null : obj.religionabmeldung;
		result.religionanmeldung = typeof obj.religionanmeldung === "undefined" ? null : obj.religionanmeldung === null ? null : obj.religionanmeldung;
		if (typeof obj.hatMigrationshintergrund === "undefined")
			 throw new Error('invalid json format, missing attribute hatMigrationshintergrund');
		result.hatMigrationshintergrund = obj.hatMigrationshintergrund;
		result.zuzugsjahr = typeof obj.zuzugsjahr === "undefined" ? null : obj.zuzugsjahr === null ? null : obj.zuzugsjahr;
		result.geburtsland = typeof obj.geburtsland === "undefined" ? null : obj.geburtsland === null ? null : obj.geburtsland;
		result.verkehrspracheFamilie = typeof obj.verkehrspracheFamilie === "undefined" ? null : obj.verkehrspracheFamilie === null ? null : obj.verkehrspracheFamilie;
		result.geburtslandVater = typeof obj.geburtslandVater === "undefined" ? null : obj.geburtslandVater === null ? null : obj.geburtslandVater;
		result.geburtslandMutter = typeof obj.geburtslandMutter === "undefined" ? null : obj.geburtslandMutter === null ? null : obj.geburtslandMutter;
		if (typeof obj.status === "undefined")
			 throw new Error('invalid json format, missing attribute status');
		result.status = obj.status;
		if (typeof obj.istDuplikat === "undefined")
			 throw new Error('invalid json format, missing attribute istDuplikat');
		result.istDuplikat = obj.istDuplikat;
		result.externeSchulNr = typeof obj.externeSchulNr === "undefined" ? null : obj.externeSchulNr === null ? null : obj.externeSchulNr;
		result.fahrschuelerArtID = typeof obj.fahrschuelerArtID === "undefined" ? null : obj.fahrschuelerArtID === null ? null : obj.fahrschuelerArtID;
		result.haltestelleID = typeof obj.haltestelleID === "undefined" ? null : obj.haltestelleID === null ? null : obj.haltestelleID;
		result.anmeldedatum = typeof obj.anmeldedatum === "undefined" ? null : obj.anmeldedatum === null ? null : obj.anmeldedatum;
		result.aufnahmedatum = typeof obj.aufnahmedatum === "undefined" ? null : obj.aufnahmedatum === null ? null : obj.aufnahmedatum;
		if (typeof obj.istVolljaehrig === "undefined")
			 throw new Error('invalid json format, missing attribute istVolljaehrig');
		result.istVolljaehrig = obj.istVolljaehrig;
		if (typeof obj.istSchulpflichtErfuellt === "undefined")
			 throw new Error('invalid json format, missing attribute istSchulpflichtErfuellt');
		result.istSchulpflichtErfuellt = obj.istSchulpflichtErfuellt;
		if (typeof obj.istBerufsschulpflichtErfuellt === "undefined")
			 throw new Error('invalid json format, missing attribute istBerufsschulpflichtErfuellt');
		result.istBerufsschulpflichtErfuellt = obj.istBerufsschulpflichtErfuellt;
		if (typeof obj.hatMasernimpfnachweis === "undefined")
			 throw new Error('invalid json format, missing attribute hatMasernimpfnachweis');
		result.hatMasernimpfnachweis = obj.hatMasernimpfnachweis;
		if (typeof obj.keineAuskunftAnDritte === "undefined")
			 throw new Error('invalid json format, missing attribute keineAuskunftAnDritte');
		result.keineAuskunftAnDritte = obj.keineAuskunftAnDritte;
		if (typeof obj.erhaeltSchuelerBAFOEG === "undefined")
			 throw new Error('invalid json format, missing attribute erhaeltSchuelerBAFOEG');
		result.erhaeltSchuelerBAFOEG = obj.erhaeltSchuelerBAFOEG;
		if (typeof obj.erhaeltMeisterBAFOEG === "undefined")
			 throw new Error('invalid json format, missing attribute erhaeltMeisterBAFOEG');
		result.erhaeltMeisterBAFOEG = obj.erhaeltMeisterBAFOEG;
		result.bemerkungen = typeof obj.bemerkungen === "undefined" ? null : obj.bemerkungen === null ? null : obj.bemerkungen;
		return result;
	}

	public static transpilerToJSON(obj : SchuelerStammdaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"foto" : ' + ((!obj.foto) ? 'null' : JSON.stringify(obj.foto)) + ',';
		result += '"nachname" : ' + JSON.stringify(obj.nachname!) + ',';
		result += '"vorname" : ' + JSON.stringify(obj.vorname!) + ',';
		result += '"alleVornamen" : ' + JSON.stringify(obj.alleVornamen!) + ',';
		result += '"geschlecht" : ' + obj.geschlecht + ',';
		result += '"geburtsdatum" : ' + ((!obj.geburtsdatum) ? 'null' : JSON.stringify(obj.geburtsdatum)) + ',';
		result += '"geburtsort" : ' + ((!obj.geburtsort) ? 'null' : JSON.stringify(obj.geburtsort)) + ',';
		result += '"geburtsname" : ' + ((!obj.geburtsname) ? 'null' : JSON.stringify(obj.geburtsname)) + ',';
		result += '"strassenname" : ' + ((!obj.strassenname) ? 'null' : JSON.stringify(obj.strassenname)) + ',';
		result += '"hausnummer" : ' + ((!obj.hausnummer) ? 'null' : JSON.stringify(obj.hausnummer)) + ',';
		result += '"hausnummerZusatz" : ' + ((!obj.hausnummerZusatz) ? 'null' : JSON.stringify(obj.hausnummerZusatz)) + ',';
		result += '"wohnortID" : ' + ((!obj.wohnortID) ? 'null' : obj.wohnortID) + ',';
		result += '"ortsteilID" : ' + ((!obj.ortsteilID) ? 'null' : obj.ortsteilID) + ',';
		result += '"telefon" : ' + ((!obj.telefon) ? 'null' : JSON.stringify(obj.telefon)) + ',';
		result += '"telefonMobil" : ' + ((!obj.telefonMobil) ? 'null' : JSON.stringify(obj.telefonMobil)) + ',';
		result += '"emailPrivat" : ' + ((!obj.emailPrivat) ? 'null' : JSON.stringify(obj.emailPrivat)) + ',';
		result += '"emailSchule" : ' + ((!obj.emailSchule) ? 'null' : JSON.stringify(obj.emailSchule)) + ',';
		result += '"staatsangehoerigkeitID" : ' + ((!obj.staatsangehoerigkeitID) ? 'null' : JSON.stringify(obj.staatsangehoerigkeitID)) + ',';
		result += '"staatsangehoerigkeit2ID" : ' + ((!obj.staatsangehoerigkeit2ID) ? 'null' : JSON.stringify(obj.staatsangehoerigkeit2ID)) + ',';
		result += '"religionID" : ' + ((!obj.religionID) ? 'null' : obj.religionID) + ',';
		result += '"druckeKonfessionAufZeugnisse" : ' + obj.druckeKonfessionAufZeugnisse + ',';
		result += '"religionabmeldung" : ' + ((!obj.religionabmeldung) ? 'null' : JSON.stringify(obj.religionabmeldung)) + ',';
		result += '"religionanmeldung" : ' + ((!obj.religionanmeldung) ? 'null' : JSON.stringify(obj.religionanmeldung)) + ',';
		result += '"hatMigrationshintergrund" : ' + obj.hatMigrationshintergrund + ',';
		result += '"zuzugsjahr" : ' + ((!obj.zuzugsjahr) ? 'null' : obj.zuzugsjahr) + ',';
		result += '"geburtsland" : ' + ((!obj.geburtsland) ? 'null' : JSON.stringify(obj.geburtsland)) + ',';
		result += '"verkehrspracheFamilie" : ' + ((!obj.verkehrspracheFamilie) ? 'null' : JSON.stringify(obj.verkehrspracheFamilie)) + ',';
		result += '"geburtslandVater" : ' + ((!obj.geburtslandVater) ? 'null' : JSON.stringify(obj.geburtslandVater)) + ',';
		result += '"geburtslandMutter" : ' + ((!obj.geburtslandMutter) ? 'null' : JSON.stringify(obj.geburtslandMutter)) + ',';
		result += '"status" : ' + obj.status + ',';
		result += '"istDuplikat" : ' + obj.istDuplikat + ',';
		result += '"externeSchulNr" : ' + ((!obj.externeSchulNr) ? 'null' : JSON.stringify(obj.externeSchulNr)) + ',';
		result += '"fahrschuelerArtID" : ' + ((!obj.fahrschuelerArtID) ? 'null' : obj.fahrschuelerArtID) + ',';
		result += '"haltestelleID" : ' + ((!obj.haltestelleID) ? 'null' : obj.haltestelleID) + ',';
		result += '"anmeldedatum" : ' + ((!obj.anmeldedatum) ? 'null' : JSON.stringify(obj.anmeldedatum)) + ',';
		result += '"aufnahmedatum" : ' + ((!obj.aufnahmedatum) ? 'null' : JSON.stringify(obj.aufnahmedatum)) + ',';
		result += '"istVolljaehrig" : ' + obj.istVolljaehrig + ',';
		result += '"istSchulpflichtErfuellt" : ' + obj.istSchulpflichtErfuellt + ',';
		result += '"istBerufsschulpflichtErfuellt" : ' + obj.istBerufsschulpflichtErfuellt + ',';
		result += '"hatMasernimpfnachweis" : ' + obj.hatMasernimpfnachweis + ',';
		result += '"keineAuskunftAnDritte" : ' + obj.keineAuskunftAnDritte + ',';
		result += '"erhaeltSchuelerBAFOEG" : ' + obj.erhaeltSchuelerBAFOEG + ',';
		result += '"erhaeltMeisterBAFOEG" : ' + obj.erhaeltMeisterBAFOEG + ',';
		result += '"bemerkungen" : ' + ((!obj.bemerkungen) ? 'null' : JSON.stringify(obj.bemerkungen)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerStammdaten>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.foto !== "undefined") {
			result += '"foto" : ' + ((!obj.foto) ? 'null' : JSON.stringify(obj.foto)) + ',';
		}
		if (typeof obj.nachname !== "undefined") {
			result += '"nachname" : ' + JSON.stringify(obj.nachname!) + ',';
		}
		if (typeof obj.vorname !== "undefined") {
			result += '"vorname" : ' + JSON.stringify(obj.vorname!) + ',';
		}
		if (typeof obj.alleVornamen !== "undefined") {
			result += '"alleVornamen" : ' + JSON.stringify(obj.alleVornamen!) + ',';
		}
		if (typeof obj.geschlecht !== "undefined") {
			result += '"geschlecht" : ' + obj.geschlecht + ',';
		}
		if (typeof obj.geburtsdatum !== "undefined") {
			result += '"geburtsdatum" : ' + ((!obj.geburtsdatum) ? 'null' : JSON.stringify(obj.geburtsdatum)) + ',';
		}
		if (typeof obj.geburtsort !== "undefined") {
			result += '"geburtsort" : ' + ((!obj.geburtsort) ? 'null' : JSON.stringify(obj.geburtsort)) + ',';
		}
		if (typeof obj.geburtsname !== "undefined") {
			result += '"geburtsname" : ' + ((!obj.geburtsname) ? 'null' : JSON.stringify(obj.geburtsname)) + ',';
		}
		if (typeof obj.strassenname !== "undefined") {
			result += '"strassenname" : ' + ((!obj.strassenname) ? 'null' : JSON.stringify(obj.strassenname)) + ',';
		}
		if (typeof obj.hausnummer !== "undefined") {
			result += '"hausnummer" : ' + ((!obj.hausnummer) ? 'null' : JSON.stringify(obj.hausnummer)) + ',';
		}
		if (typeof obj.hausnummerZusatz !== "undefined") {
			result += '"hausnummerZusatz" : ' + ((!obj.hausnummerZusatz) ? 'null' : JSON.stringify(obj.hausnummerZusatz)) + ',';
		}
		if (typeof obj.wohnortID !== "undefined") {
			result += '"wohnortID" : ' + ((!obj.wohnortID) ? 'null' : obj.wohnortID) + ',';
		}
		if (typeof obj.ortsteilID !== "undefined") {
			result += '"ortsteilID" : ' + ((!obj.ortsteilID) ? 'null' : obj.ortsteilID) + ',';
		}
		if (typeof obj.telefon !== "undefined") {
			result += '"telefon" : ' + ((!obj.telefon) ? 'null' : JSON.stringify(obj.telefon)) + ',';
		}
		if (typeof obj.telefonMobil !== "undefined") {
			result += '"telefonMobil" : ' + ((!obj.telefonMobil) ? 'null' : JSON.stringify(obj.telefonMobil)) + ',';
		}
		if (typeof obj.emailPrivat !== "undefined") {
			result += '"emailPrivat" : ' + ((!obj.emailPrivat) ? 'null' : JSON.stringify(obj.emailPrivat)) + ',';
		}
		if (typeof obj.emailSchule !== "undefined") {
			result += '"emailSchule" : ' + ((!obj.emailSchule) ? 'null' : JSON.stringify(obj.emailSchule)) + ',';
		}
		if (typeof obj.staatsangehoerigkeitID !== "undefined") {
			result += '"staatsangehoerigkeitID" : ' + ((!obj.staatsangehoerigkeitID) ? 'null' : JSON.stringify(obj.staatsangehoerigkeitID)) + ',';
		}
		if (typeof obj.staatsangehoerigkeit2ID !== "undefined") {
			result += '"staatsangehoerigkeit2ID" : ' + ((!obj.staatsangehoerigkeit2ID) ? 'null' : JSON.stringify(obj.staatsangehoerigkeit2ID)) + ',';
		}
		if (typeof obj.religionID !== "undefined") {
			result += '"religionID" : ' + ((!obj.religionID) ? 'null' : obj.religionID) + ',';
		}
		if (typeof obj.druckeKonfessionAufZeugnisse !== "undefined") {
			result += '"druckeKonfessionAufZeugnisse" : ' + obj.druckeKonfessionAufZeugnisse + ',';
		}
		if (typeof obj.religionabmeldung !== "undefined") {
			result += '"religionabmeldung" : ' + ((!obj.religionabmeldung) ? 'null' : JSON.stringify(obj.religionabmeldung)) + ',';
		}
		if (typeof obj.religionanmeldung !== "undefined") {
			result += '"religionanmeldung" : ' + ((!obj.religionanmeldung) ? 'null' : JSON.stringify(obj.religionanmeldung)) + ',';
		}
		if (typeof obj.hatMigrationshintergrund !== "undefined") {
			result += '"hatMigrationshintergrund" : ' + obj.hatMigrationshintergrund + ',';
		}
		if (typeof obj.zuzugsjahr !== "undefined") {
			result += '"zuzugsjahr" : ' + ((!obj.zuzugsjahr) ? 'null' : obj.zuzugsjahr) + ',';
		}
		if (typeof obj.geburtsland !== "undefined") {
			result += '"geburtsland" : ' + ((!obj.geburtsland) ? 'null' : JSON.stringify(obj.geburtsland)) + ',';
		}
		if (typeof obj.verkehrspracheFamilie !== "undefined") {
			result += '"verkehrspracheFamilie" : ' + ((!obj.verkehrspracheFamilie) ? 'null' : JSON.stringify(obj.verkehrspracheFamilie)) + ',';
		}
		if (typeof obj.geburtslandVater !== "undefined") {
			result += '"geburtslandVater" : ' + ((!obj.geburtslandVater) ? 'null' : JSON.stringify(obj.geburtslandVater)) + ',';
		}
		if (typeof obj.geburtslandMutter !== "undefined") {
			result += '"geburtslandMutter" : ' + ((!obj.geburtslandMutter) ? 'null' : JSON.stringify(obj.geburtslandMutter)) + ',';
		}
		if (typeof obj.status !== "undefined") {
			result += '"status" : ' + obj.status + ',';
		}
		if (typeof obj.istDuplikat !== "undefined") {
			result += '"istDuplikat" : ' + obj.istDuplikat + ',';
		}
		if (typeof obj.externeSchulNr !== "undefined") {
			result += '"externeSchulNr" : ' + ((!obj.externeSchulNr) ? 'null' : JSON.stringify(obj.externeSchulNr)) + ',';
		}
		if (typeof obj.fahrschuelerArtID !== "undefined") {
			result += '"fahrschuelerArtID" : ' + ((!obj.fahrschuelerArtID) ? 'null' : obj.fahrschuelerArtID) + ',';
		}
		if (typeof obj.haltestelleID !== "undefined") {
			result += '"haltestelleID" : ' + ((!obj.haltestelleID) ? 'null' : obj.haltestelleID) + ',';
		}
		if (typeof obj.anmeldedatum !== "undefined") {
			result += '"anmeldedatum" : ' + ((!obj.anmeldedatum) ? 'null' : JSON.stringify(obj.anmeldedatum)) + ',';
		}
		if (typeof obj.aufnahmedatum !== "undefined") {
			result += '"aufnahmedatum" : ' + ((!obj.aufnahmedatum) ? 'null' : JSON.stringify(obj.aufnahmedatum)) + ',';
		}
		if (typeof obj.istVolljaehrig !== "undefined") {
			result += '"istVolljaehrig" : ' + obj.istVolljaehrig + ',';
		}
		if (typeof obj.istSchulpflichtErfuellt !== "undefined") {
			result += '"istSchulpflichtErfuellt" : ' + obj.istSchulpflichtErfuellt + ',';
		}
		if (typeof obj.istBerufsschulpflichtErfuellt !== "undefined") {
			result += '"istBerufsschulpflichtErfuellt" : ' + obj.istBerufsschulpflichtErfuellt + ',';
		}
		if (typeof obj.hatMasernimpfnachweis !== "undefined") {
			result += '"hatMasernimpfnachweis" : ' + obj.hatMasernimpfnachweis + ',';
		}
		if (typeof obj.keineAuskunftAnDritte !== "undefined") {
			result += '"keineAuskunftAnDritte" : ' + obj.keineAuskunftAnDritte + ',';
		}
		if (typeof obj.erhaeltSchuelerBAFOEG !== "undefined") {
			result += '"erhaeltSchuelerBAFOEG" : ' + obj.erhaeltSchuelerBAFOEG + ',';
		}
		if (typeof obj.erhaeltMeisterBAFOEG !== "undefined") {
			result += '"erhaeltMeisterBAFOEG" : ' + obj.erhaeltMeisterBAFOEG + ',';
		}
		if (typeof obj.bemerkungen !== "undefined") {
			result += '"bemerkungen" : ' + ((!obj.bemerkungen) ? 'null' : JSON.stringify(obj.bemerkungen)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schueler_SchuelerStammdaten(obj : unknown) : SchuelerStammdaten {
	return obj as SchuelerStammdaten;
}
