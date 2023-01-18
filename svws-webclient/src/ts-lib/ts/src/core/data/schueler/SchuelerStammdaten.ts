import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { JavaBoolean, cast_java_lang_Boolean } from '../../../java/lang/JavaBoolean';

export class SchuelerStammdaten extends JavaObject {

	/**
	 * Die ID des Schülerdatensatzes. 
	 */
	public id : number = 0;

	/**
	 * Das Foto (in Base64 kodiert) des Schülerdatensatzes. 
	 */
	public foto : String | null = null;

	/**
	 * Der Nachname des Schülerdatensatzes. 
	 */
	public nachname : String = "";

	/**
	 * Ggf. Zusatz zum Nachnamen des Schülerdatensatzes. 
	 */
	public zusatzNachname : String = "";

	/**
	 * Der Vorname des Schülerdatensatzes. 
	 */
	public vorname : String = "";

	/**
	 * Alle Vornamen, sofern es mehrere gibt, des Schülerdatensatzes. 
	 */
	public alleVornamen : String = "";

	/**
	 * Die ID des Geschlechtes 
	 */
	public geschlecht : number = 0;

	/**
	 * Das Geburtsdatum des Schülerdatensatzes. 
	 */
	public geburtsdatum : String | null = null;

	/**
	 * Der Geburtsort des Schülerdatensatzes. 
	 */
	public geburtsort : String | null = null;

	/**
	 * Der Geburtsname des Schülerdatensatzes. 
	 */
	public geburtsname : String | null = null;

	/**
	 * Ggf. der Straßenname im Wohnort des Schülers. 
	 */
	public strassenname : String | null = null;

	/**
	 * Ggf. die Hausnummer zur Straße im Wohnort des Schülers. 
	 */
	public hausnummer : String | null = null;

	/**
	 * Ggf. der Hausnummerzusatz zur Straße im Wohnort des Schülers. 
	 */
	public hausnummerZusatz : String | null = null;

	/**
	 * Die ID des Wohnortes des Schülerdatensatzes. 
	 */
	public wohnortID : Number | null = null;

	/**
	 * Die ID des Ortsteils des Schülerdatensatzes. 
	 */
	public ortsteilID : Number | null = null;

	/**
	 * Die Telefonnummer des Schülerdatensatzes. 
	 */
	public telefon : String | null = null;

	/**
	 * Die Mobilnummer des Schülerdatensatzes. 
	 */
	public telefonMobil : String | null = null;

	/**
	 * Die private Email-Adresse des Schülerdatensatzes. 
	 */
	public emailPrivat : String | null = null;

	/**
	 * Die schulische Email-Adresse des Schülerdatensatzes. 
	 */
	public emailSchule : String | null = null;

	/**
	 * Die ID der Staatsangehörigkeit des Schülerdatensatzes. 
	 */
	public staatsangehoerigkeitID : String | null = null;

	/**
	 * Die ID einer zweiten Staatsangehörigkeit des Schülerdatensatzes. 
	 */
	public staatsangehoerigkeit2ID : String | null = null;

	/**
	 * Die ID der Religion des Schülerdatensatzes. 
	 */
	public religionID : Number | null = null;

	/**
	 * Gibt an, ob die Konfession bei dem Schülerdatensatz auf dem Zeugnis erscheinen soll. 
	 */
	public druckeKonfessionAufZeugnisse : boolean = false;

	/**
	 * Das Datum der Religionsabmeldung des Schülerdatensatzes. 
	 */
	public religionabmeldung : String | null = null;

	/**
	 * Das Datum der Religionsanmeldung des Schülerdatensatzes. 
	 */
	public religionanmeldung : String | null = null;

	/**
	 * Gibt an, ob ein Migrationshintergrund bei dems Schülerdatensatz vorhanden ist. 
	 */
	public hatMigrationshintergrund : boolean = false;

	/**
	 * Das Zuzugsjahr des Schülerdatensatzes. 
	 */
	public zuzugsjahr : String | null = null;

	/**
	 * Das Geburtsland des Schülerdatensatzes. 
	 */
	public geburtsland : String | null = null;

	/**
	 * Die Verkehrssprache der Familie des Schülerdatensatzes. 
	 */
	public verkehrspracheFamilie : String | null = null;

	/**
	 * Das Geburtsland des Vaters des Schülerdatensatzes. 
	 */
	public geburtslandVater : String | null = null;

	/**
	 * Das Geburtsland der Mutter des Schülerdatensatzes. 
	 */
	public geburtslandMutter : String | null = null;

	/**
	 * Die ID eines sonderpädagogischen Förderschwerpunnktes des Schülerdatensatzes. 
	 */
	public foerderschwerpunktID : Number | null = null;

	/**
	 * Die ID eines zweiten sonderpädagogischen Förderschwerpunnktes des Schülerdatensatzes. 
	 */
	public foerderschwerpunkt2ID : Number | null = null;

	/**
	 * Gibt an, ob ein AOSF bei dem Schülerdatensatz vorliegt. 
	 */
	public istAOSF : Boolean | null = null;

	/**
	 * Gibt an, ob zieldifferentes Lerne bei dem Schülerdatensatz vorliegt. 
	 */
	public istLernenZieldifferent : Boolean | null = null;

	/**
	 * Die Bezeichnung des Status des Schülerdatensatzes. 
	 */
	public status : String | null = null;

	/**
	 * Die ID der Art des Fahrschülers des Schülerdatensatzes. 
	 */
	public fahrschuelerArtID : Number | null = null;

	/**
	 * Die ID der Haltestelle, ab der der Schüler das Transportmittel nimmt, des Schülerdatensatzes. 
	 */
	public haltestelleID : Number | null = null;

	/**
	 * Das Anmeldedatum des Schülerdatensatzes. 
	 */
	public anmeldedatum : String | null = null;

	/**
	 * Das Aufnahmedatum des Schülerdatensatzes. 
	 */
	public aufnahmedatum : String | null = null;

	/**
	 * Gibt an, ob der Schüler volljährig ist oder nicht. 
	 */
	public istVolljaehrig : Boolean | null = null;

	/**
	 * Gibt an, ob der Schüler die Schulpflicht erfüllt hat oder nicht. 
	 */
	public istSchulpflichtErfuellt : Boolean | null = null;

	/**
	 * Gibt an, ob der Schüler die Berufsschulpflicht erfüllt hat oder nicht. 
	 */
	public istBerufsschulpflichtErfuellt : Boolean | null = null;

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
	 * Ggibt an, ob es sich bei dem Schülerdatensatz um ein Duplikat handelt oder nicht. 
	 */
	public istDuplikat : boolean = false;

	/**
	 * Textfeld mit Bemerkungen zum Schülerdatensatz. 
	 */
	public bemerkungen : String | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schueler.SchuelerStammdaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuelerStammdaten {
		const obj = JSON.parse(json);
		const result = new SchuelerStammdaten();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.foto = typeof obj.foto === "undefined" ? null : obj.foto === null ? null : String(obj.foto);
		if (typeof obj.nachname === "undefined")
			 throw new Error('invalid json format, missing attribute nachname');
		result.nachname = String(obj.nachname);
		if (typeof obj.zusatzNachname === "undefined")
			 throw new Error('invalid json format, missing attribute zusatzNachname');
		result.zusatzNachname = String(obj.zusatzNachname);
		if (typeof obj.vorname === "undefined")
			 throw new Error('invalid json format, missing attribute vorname');
		result.vorname = String(obj.vorname);
		if (typeof obj.alleVornamen === "undefined")
			 throw new Error('invalid json format, missing attribute alleVornamen');
		result.alleVornamen = String(obj.alleVornamen);
		if (typeof obj.geschlecht === "undefined")
			 throw new Error('invalid json format, missing attribute geschlecht');
		result.geschlecht = obj.geschlecht;
		result.geburtsdatum = typeof obj.geburtsdatum === "undefined" ? null : obj.geburtsdatum === null ? null : String(obj.geburtsdatum);
		result.geburtsort = typeof obj.geburtsort === "undefined" ? null : obj.geburtsort === null ? null : String(obj.geburtsort);
		result.geburtsname = typeof obj.geburtsname === "undefined" ? null : obj.geburtsname === null ? null : String(obj.geburtsname);
		result.strassenname = typeof obj.strassenname === "undefined" ? null : obj.strassenname === null ? null : String(obj.strassenname);
		result.hausnummer = typeof obj.hausnummer === "undefined" ? null : obj.hausnummer === null ? null : String(obj.hausnummer);
		result.hausnummerZusatz = typeof obj.hausnummerZusatz === "undefined" ? null : obj.hausnummerZusatz === null ? null : String(obj.hausnummerZusatz);
		result.wohnortID = typeof obj.wohnortID === "undefined" ? null : obj.wohnortID === null ? null : Number(obj.wohnortID);
		result.ortsteilID = typeof obj.ortsteilID === "undefined" ? null : obj.ortsteilID === null ? null : Number(obj.ortsteilID);
		result.telefon = typeof obj.telefon === "undefined" ? null : obj.telefon === null ? null : String(obj.telefon);
		result.telefonMobil = typeof obj.telefonMobil === "undefined" ? null : obj.telefonMobil === null ? null : String(obj.telefonMobil);
		result.emailPrivat = typeof obj.emailPrivat === "undefined" ? null : obj.emailPrivat === null ? null : String(obj.emailPrivat);
		result.emailSchule = typeof obj.emailSchule === "undefined" ? null : obj.emailSchule === null ? null : String(obj.emailSchule);
		result.staatsangehoerigkeitID = typeof obj.staatsangehoerigkeitID === "undefined" ? null : obj.staatsangehoerigkeitID === null ? null : String(obj.staatsangehoerigkeitID);
		result.staatsangehoerigkeit2ID = typeof obj.staatsangehoerigkeit2ID === "undefined" ? null : obj.staatsangehoerigkeit2ID === null ? null : String(obj.staatsangehoerigkeit2ID);
		result.religionID = typeof obj.religionID === "undefined" ? null : obj.religionID === null ? null : Number(obj.religionID);
		if (typeof obj.druckeKonfessionAufZeugnisse === "undefined")
			 throw new Error('invalid json format, missing attribute druckeKonfessionAufZeugnisse');
		result.druckeKonfessionAufZeugnisse = obj.druckeKonfessionAufZeugnisse;
		result.religionabmeldung = typeof obj.religionabmeldung === "undefined" ? null : obj.religionabmeldung === null ? null : String(obj.religionabmeldung);
		result.religionanmeldung = typeof obj.religionanmeldung === "undefined" ? null : obj.religionanmeldung === null ? null : String(obj.religionanmeldung);
		if (typeof obj.hatMigrationshintergrund === "undefined")
			 throw new Error('invalid json format, missing attribute hatMigrationshintergrund');
		result.hatMigrationshintergrund = obj.hatMigrationshintergrund;
		result.zuzugsjahr = typeof obj.zuzugsjahr === "undefined" ? null : obj.zuzugsjahr === null ? null : String(obj.zuzugsjahr);
		result.geburtsland = typeof obj.geburtsland === "undefined" ? null : obj.geburtsland === null ? null : String(obj.geburtsland);
		result.verkehrspracheFamilie = typeof obj.verkehrspracheFamilie === "undefined" ? null : obj.verkehrspracheFamilie === null ? null : String(obj.verkehrspracheFamilie);
		result.geburtslandVater = typeof obj.geburtslandVater === "undefined" ? null : obj.geburtslandVater === null ? null : String(obj.geburtslandVater);
		result.geburtslandMutter = typeof obj.geburtslandMutter === "undefined" ? null : obj.geburtslandMutter === null ? null : String(obj.geburtslandMutter);
		result.foerderschwerpunktID = typeof obj.foerderschwerpunktID === "undefined" ? null : obj.foerderschwerpunktID === null ? null : Number(obj.foerderschwerpunktID);
		result.foerderschwerpunkt2ID = typeof obj.foerderschwerpunkt2ID === "undefined" ? null : obj.foerderschwerpunkt2ID === null ? null : Number(obj.foerderschwerpunkt2ID);
		result.istAOSF = typeof obj.istAOSF === "undefined" ? null : obj.istAOSF === null ? null : Boolean(obj.istAOSF);
		result.istLernenZieldifferent = typeof obj.istLernenZieldifferent === "undefined" ? null : obj.istLernenZieldifferent === null ? null : Boolean(obj.istLernenZieldifferent);
		result.status = typeof obj.status === "undefined" ? null : obj.status === null ? null : String(obj.status);
		result.fahrschuelerArtID = typeof obj.fahrschuelerArtID === "undefined" ? null : obj.fahrschuelerArtID === null ? null : Number(obj.fahrschuelerArtID);
		result.haltestelleID = typeof obj.haltestelleID === "undefined" ? null : obj.haltestelleID === null ? null : Number(obj.haltestelleID);
		result.anmeldedatum = typeof obj.anmeldedatum === "undefined" ? null : obj.anmeldedatum === null ? null : String(obj.anmeldedatum);
		result.aufnahmedatum = typeof obj.aufnahmedatum === "undefined" ? null : obj.aufnahmedatum === null ? null : String(obj.aufnahmedatum);
		result.istVolljaehrig = typeof obj.istVolljaehrig === "undefined" ? null : obj.istVolljaehrig === null ? null : Boolean(obj.istVolljaehrig);
		result.istSchulpflichtErfuellt = typeof obj.istSchulpflichtErfuellt === "undefined" ? null : obj.istSchulpflichtErfuellt === null ? null : Boolean(obj.istSchulpflichtErfuellt);
		result.istBerufsschulpflichtErfuellt = typeof obj.istBerufsschulpflichtErfuellt === "undefined" ? null : obj.istBerufsschulpflichtErfuellt === null ? null : Boolean(obj.istBerufsschulpflichtErfuellt);
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
		if (typeof obj.istDuplikat === "undefined")
			 throw new Error('invalid json format, missing attribute istDuplikat');
		result.istDuplikat = obj.istDuplikat;
		result.bemerkungen = typeof obj.bemerkungen === "undefined" ? null : obj.bemerkungen === null ? null : String(obj.bemerkungen);
		return result;
	}

	public static transpilerToJSON(obj : SchuelerStammdaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"foto" : ' + ((!obj.foto) ? 'null' : '"' + obj.foto.valueOf() + '"') + ',';
		result += '"nachname" : ' + '"' + obj.nachname.valueOf() + '"' + ',';
		result += '"zusatzNachname" : ' + '"' + obj.zusatzNachname.valueOf() + '"' + ',';
		result += '"vorname" : ' + '"' + obj.vorname.valueOf() + '"' + ',';
		result += '"alleVornamen" : ' + '"' + obj.alleVornamen.valueOf() + '"' + ',';
		result += '"geschlecht" : ' + obj.geschlecht + ',';
		result += '"geburtsdatum" : ' + ((!obj.geburtsdatum) ? 'null' : '"' + obj.geburtsdatum.valueOf() + '"') + ',';
		result += '"geburtsort" : ' + ((!obj.geburtsort) ? 'null' : '"' + obj.geburtsort.valueOf() + '"') + ',';
		result += '"geburtsname" : ' + ((!obj.geburtsname) ? 'null' : '"' + obj.geburtsname.valueOf() + '"') + ',';
		result += '"strassenname" : ' + ((!obj.strassenname) ? 'null' : '"' + obj.strassenname.valueOf() + '"') + ',';
		result += '"hausnummer" : ' + ((!obj.hausnummer) ? 'null' : '"' + obj.hausnummer.valueOf() + '"') + ',';
		result += '"hausnummerZusatz" : ' + ((!obj.hausnummerZusatz) ? 'null' : '"' + obj.hausnummerZusatz.valueOf() + '"') + ',';
		result += '"wohnortID" : ' + ((!obj.wohnortID) ? 'null' : obj.wohnortID.valueOf()) + ',';
		result += '"ortsteilID" : ' + ((!obj.ortsteilID) ? 'null' : obj.ortsteilID.valueOf()) + ',';
		result += '"telefon" : ' + ((!obj.telefon) ? 'null' : '"' + obj.telefon.valueOf() + '"') + ',';
		result += '"telefonMobil" : ' + ((!obj.telefonMobil) ? 'null' : '"' + obj.telefonMobil.valueOf() + '"') + ',';
		result += '"emailPrivat" : ' + ((!obj.emailPrivat) ? 'null' : '"' + obj.emailPrivat.valueOf() + '"') + ',';
		result += '"emailSchule" : ' + ((!obj.emailSchule) ? 'null' : '"' + obj.emailSchule.valueOf() + '"') + ',';
		result += '"staatsangehoerigkeitID" : ' + ((!obj.staatsangehoerigkeitID) ? 'null' : '"' + obj.staatsangehoerigkeitID.valueOf() + '"') + ',';
		result += '"staatsangehoerigkeit2ID" : ' + ((!obj.staatsangehoerigkeit2ID) ? 'null' : '"' + obj.staatsangehoerigkeit2ID.valueOf() + '"') + ',';
		result += '"religionID" : ' + ((!obj.religionID) ? 'null' : obj.religionID.valueOf()) + ',';
		result += '"druckeKonfessionAufZeugnisse" : ' + obj.druckeKonfessionAufZeugnisse + ',';
		result += '"religionabmeldung" : ' + ((!obj.religionabmeldung) ? 'null' : '"' + obj.religionabmeldung.valueOf() + '"') + ',';
		result += '"religionanmeldung" : ' + ((!obj.religionanmeldung) ? 'null' : '"' + obj.religionanmeldung.valueOf() + '"') + ',';
		result += '"hatMigrationshintergrund" : ' + obj.hatMigrationshintergrund + ',';
		result += '"zuzugsjahr" : ' + ((!obj.zuzugsjahr) ? 'null' : '"' + obj.zuzugsjahr.valueOf() + '"') + ',';
		result += '"geburtsland" : ' + ((!obj.geburtsland) ? 'null' : '"' + obj.geburtsland.valueOf() + '"') + ',';
		result += '"verkehrspracheFamilie" : ' + ((!obj.verkehrspracheFamilie) ? 'null' : '"' + obj.verkehrspracheFamilie.valueOf() + '"') + ',';
		result += '"geburtslandVater" : ' + ((!obj.geburtslandVater) ? 'null' : '"' + obj.geburtslandVater.valueOf() + '"') + ',';
		result += '"geburtslandMutter" : ' + ((!obj.geburtslandMutter) ? 'null' : '"' + obj.geburtslandMutter.valueOf() + '"') + ',';
		result += '"foerderschwerpunktID" : ' + ((!obj.foerderschwerpunktID) ? 'null' : obj.foerderschwerpunktID.valueOf()) + ',';
		result += '"foerderschwerpunkt2ID" : ' + ((!obj.foerderschwerpunkt2ID) ? 'null' : obj.foerderschwerpunkt2ID.valueOf()) + ',';
		result += '"istAOSF" : ' + ((!obj.istAOSF) ? 'null' : obj.istAOSF.valueOf()) + ',';
		result += '"istLernenZieldifferent" : ' + ((!obj.istLernenZieldifferent) ? 'null' : obj.istLernenZieldifferent.valueOf()) + ',';
		result += '"status" : ' + ((!obj.status) ? 'null' : '"' + obj.status.valueOf() + '"') + ',';
		result += '"fahrschuelerArtID" : ' + ((!obj.fahrschuelerArtID) ? 'null' : obj.fahrschuelerArtID.valueOf()) + ',';
		result += '"haltestelleID" : ' + ((!obj.haltestelleID) ? 'null' : obj.haltestelleID.valueOf()) + ',';
		result += '"anmeldedatum" : ' + ((!obj.anmeldedatum) ? 'null' : '"' + obj.anmeldedatum.valueOf() + '"') + ',';
		result += '"aufnahmedatum" : ' + ((!obj.aufnahmedatum) ? 'null' : '"' + obj.aufnahmedatum.valueOf() + '"') + ',';
		result += '"istVolljaehrig" : ' + ((!obj.istVolljaehrig) ? 'null' : obj.istVolljaehrig.valueOf()) + ',';
		result += '"istSchulpflichtErfuellt" : ' + ((!obj.istSchulpflichtErfuellt) ? 'null' : obj.istSchulpflichtErfuellt.valueOf()) + ',';
		result += '"istBerufsschulpflichtErfuellt" : ' + ((!obj.istBerufsschulpflichtErfuellt) ? 'null' : obj.istBerufsschulpflichtErfuellt.valueOf()) + ',';
		result += '"hatMasernimpfnachweis" : ' + obj.hatMasernimpfnachweis + ',';
		result += '"keineAuskunftAnDritte" : ' + obj.keineAuskunftAnDritte + ',';
		result += '"erhaeltSchuelerBAFOEG" : ' + obj.erhaeltSchuelerBAFOEG + ',';
		result += '"erhaeltMeisterBAFOEG" : ' + obj.erhaeltMeisterBAFOEG + ',';
		result += '"istDuplikat" : ' + obj.istDuplikat + ',';
		result += '"bemerkungen" : ' + ((!obj.bemerkungen) ? 'null' : '"' + obj.bemerkungen.valueOf() + '"') + ',';
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
			result += '"foto" : ' + ((!obj.foto) ? 'null' : '"' + obj.foto.valueOf() + '"') + ',';
		}
		if (typeof obj.nachname !== "undefined") {
			result += '"nachname" : ' + '"' + obj.nachname.valueOf() + '"' + ',';
		}
		if (typeof obj.zusatzNachname !== "undefined") {
			result += '"zusatzNachname" : ' + '"' + obj.zusatzNachname.valueOf() + '"' + ',';
		}
		if (typeof obj.vorname !== "undefined") {
			result += '"vorname" : ' + '"' + obj.vorname.valueOf() + '"' + ',';
		}
		if (typeof obj.alleVornamen !== "undefined") {
			result += '"alleVornamen" : ' + '"' + obj.alleVornamen.valueOf() + '"' + ',';
		}
		if (typeof obj.geschlecht !== "undefined") {
			result += '"geschlecht" : ' + obj.geschlecht + ',';
		}
		if (typeof obj.geburtsdatum !== "undefined") {
			result += '"geburtsdatum" : ' + ((!obj.geburtsdatum) ? 'null' : '"' + obj.geburtsdatum.valueOf() + '"') + ',';
		}
		if (typeof obj.geburtsort !== "undefined") {
			result += '"geburtsort" : ' + ((!obj.geburtsort) ? 'null' : '"' + obj.geburtsort.valueOf() + '"') + ',';
		}
		if (typeof obj.geburtsname !== "undefined") {
			result += '"geburtsname" : ' + ((!obj.geburtsname) ? 'null' : '"' + obj.geburtsname.valueOf() + '"') + ',';
		}
		if (typeof obj.strassenname !== "undefined") {
			result += '"strassenname" : ' + ((!obj.strassenname) ? 'null' : '"' + obj.strassenname.valueOf() + '"') + ',';
		}
		if (typeof obj.hausnummer !== "undefined") {
			result += '"hausnummer" : ' + ((!obj.hausnummer) ? 'null' : '"' + obj.hausnummer.valueOf() + '"') + ',';
		}
		if (typeof obj.hausnummerZusatz !== "undefined") {
			result += '"hausnummerZusatz" : ' + ((!obj.hausnummerZusatz) ? 'null' : '"' + obj.hausnummerZusatz.valueOf() + '"') + ',';
		}
		if (typeof obj.wohnortID !== "undefined") {
			result += '"wohnortID" : ' + ((!obj.wohnortID) ? 'null' : obj.wohnortID.valueOf()) + ',';
		}
		if (typeof obj.ortsteilID !== "undefined") {
			result += '"ortsteilID" : ' + ((!obj.ortsteilID) ? 'null' : obj.ortsteilID.valueOf()) + ',';
		}
		if (typeof obj.telefon !== "undefined") {
			result += '"telefon" : ' + ((!obj.telefon) ? 'null' : '"' + obj.telefon.valueOf() + '"') + ',';
		}
		if (typeof obj.telefonMobil !== "undefined") {
			result += '"telefonMobil" : ' + ((!obj.telefonMobil) ? 'null' : '"' + obj.telefonMobil.valueOf() + '"') + ',';
		}
		if (typeof obj.emailPrivat !== "undefined") {
			result += '"emailPrivat" : ' + ((!obj.emailPrivat) ? 'null' : '"' + obj.emailPrivat.valueOf() + '"') + ',';
		}
		if (typeof obj.emailSchule !== "undefined") {
			result += '"emailSchule" : ' + ((!obj.emailSchule) ? 'null' : '"' + obj.emailSchule.valueOf() + '"') + ',';
		}
		if (typeof obj.staatsangehoerigkeitID !== "undefined") {
			result += '"staatsangehoerigkeitID" : ' + ((!obj.staatsangehoerigkeitID) ? 'null' : '"' + obj.staatsangehoerigkeitID.valueOf() + '"') + ',';
		}
		if (typeof obj.staatsangehoerigkeit2ID !== "undefined") {
			result += '"staatsangehoerigkeit2ID" : ' + ((!obj.staatsangehoerigkeit2ID) ? 'null' : '"' + obj.staatsangehoerigkeit2ID.valueOf() + '"') + ',';
		}
		if (typeof obj.religionID !== "undefined") {
			result += '"religionID" : ' + ((!obj.religionID) ? 'null' : obj.religionID.valueOf()) + ',';
		}
		if (typeof obj.druckeKonfessionAufZeugnisse !== "undefined") {
			result += '"druckeKonfessionAufZeugnisse" : ' + obj.druckeKonfessionAufZeugnisse + ',';
		}
		if (typeof obj.religionabmeldung !== "undefined") {
			result += '"religionabmeldung" : ' + ((!obj.religionabmeldung) ? 'null' : '"' + obj.religionabmeldung.valueOf() + '"') + ',';
		}
		if (typeof obj.religionanmeldung !== "undefined") {
			result += '"religionanmeldung" : ' + ((!obj.religionanmeldung) ? 'null' : '"' + obj.religionanmeldung.valueOf() + '"') + ',';
		}
		if (typeof obj.hatMigrationshintergrund !== "undefined") {
			result += '"hatMigrationshintergrund" : ' + obj.hatMigrationshintergrund + ',';
		}
		if (typeof obj.zuzugsjahr !== "undefined") {
			result += '"zuzugsjahr" : ' + ((!obj.zuzugsjahr) ? 'null' : '"' + obj.zuzugsjahr.valueOf() + '"') + ',';
		}
		if (typeof obj.geburtsland !== "undefined") {
			result += '"geburtsland" : ' + ((!obj.geburtsland) ? 'null' : '"' + obj.geburtsland.valueOf() + '"') + ',';
		}
		if (typeof obj.verkehrspracheFamilie !== "undefined") {
			result += '"verkehrspracheFamilie" : ' + ((!obj.verkehrspracheFamilie) ? 'null' : '"' + obj.verkehrspracheFamilie.valueOf() + '"') + ',';
		}
		if (typeof obj.geburtslandVater !== "undefined") {
			result += '"geburtslandVater" : ' + ((!obj.geburtslandVater) ? 'null' : '"' + obj.geburtslandVater.valueOf() + '"') + ',';
		}
		if (typeof obj.geburtslandMutter !== "undefined") {
			result += '"geburtslandMutter" : ' + ((!obj.geburtslandMutter) ? 'null' : '"' + obj.geburtslandMutter.valueOf() + '"') + ',';
		}
		if (typeof obj.foerderschwerpunktID !== "undefined") {
			result += '"foerderschwerpunktID" : ' + ((!obj.foerderschwerpunktID) ? 'null' : obj.foerderschwerpunktID.valueOf()) + ',';
		}
		if (typeof obj.foerderschwerpunkt2ID !== "undefined") {
			result += '"foerderschwerpunkt2ID" : ' + ((!obj.foerderschwerpunkt2ID) ? 'null' : obj.foerderschwerpunkt2ID.valueOf()) + ',';
		}
		if (typeof obj.istAOSF !== "undefined") {
			result += '"istAOSF" : ' + ((!obj.istAOSF) ? 'null' : obj.istAOSF.valueOf()) + ',';
		}
		if (typeof obj.istLernenZieldifferent !== "undefined") {
			result += '"istLernenZieldifferent" : ' + ((!obj.istLernenZieldifferent) ? 'null' : obj.istLernenZieldifferent.valueOf()) + ',';
		}
		if (typeof obj.status !== "undefined") {
			result += '"status" : ' + ((!obj.status) ? 'null' : '"' + obj.status.valueOf() + '"') + ',';
		}
		if (typeof obj.fahrschuelerArtID !== "undefined") {
			result += '"fahrschuelerArtID" : ' + ((!obj.fahrschuelerArtID) ? 'null' : obj.fahrschuelerArtID.valueOf()) + ',';
		}
		if (typeof obj.haltestelleID !== "undefined") {
			result += '"haltestelleID" : ' + ((!obj.haltestelleID) ? 'null' : obj.haltestelleID.valueOf()) + ',';
		}
		if (typeof obj.anmeldedatum !== "undefined") {
			result += '"anmeldedatum" : ' + ((!obj.anmeldedatum) ? 'null' : '"' + obj.anmeldedatum.valueOf() + '"') + ',';
		}
		if (typeof obj.aufnahmedatum !== "undefined") {
			result += '"aufnahmedatum" : ' + ((!obj.aufnahmedatum) ? 'null' : '"' + obj.aufnahmedatum.valueOf() + '"') + ',';
		}
		if (typeof obj.istVolljaehrig !== "undefined") {
			result += '"istVolljaehrig" : ' + ((!obj.istVolljaehrig) ? 'null' : obj.istVolljaehrig.valueOf()) + ',';
		}
		if (typeof obj.istSchulpflichtErfuellt !== "undefined") {
			result += '"istSchulpflichtErfuellt" : ' + ((!obj.istSchulpflichtErfuellt) ? 'null' : obj.istSchulpflichtErfuellt.valueOf()) + ',';
		}
		if (typeof obj.istBerufsschulpflichtErfuellt !== "undefined") {
			result += '"istBerufsschulpflichtErfuellt" : ' + ((!obj.istBerufsschulpflichtErfuellt) ? 'null' : obj.istBerufsschulpflichtErfuellt.valueOf()) + ',';
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
		if (typeof obj.istDuplikat !== "undefined") {
			result += '"istDuplikat" : ' + obj.istDuplikat + ',';
		}
		if (typeof obj.bemerkungen !== "undefined") {
			result += '"bemerkungen" : ' + ((!obj.bemerkungen) ? 'null' : '"' + obj.bemerkungen.valueOf() + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schueler_SchuelerStammdaten(obj : unknown) : SchuelerStammdaten {
	return obj as SchuelerStammdaten;
}
