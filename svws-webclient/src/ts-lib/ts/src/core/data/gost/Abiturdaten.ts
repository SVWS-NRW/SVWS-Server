import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { AbiturFachbelegung, cast_de_nrw_schule_svws_core_data_gost_AbiturFachbelegung } from '../../../core/data/gost/AbiturFachbelegung';
import { GostBesondereLernleistung, cast_de_nrw_schule_svws_core_types_gost_GostBesondereLernleistung } from '../../../core/types/gost/GostBesondereLernleistung';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { JavaBoolean, cast_java_lang_Boolean } from '../../../java/lang/JavaBoolean';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { JavaDouble, cast_java_lang_Double } from '../../../java/lang/JavaDouble';
import { Sprachendaten, cast_de_nrw_schule_svws_core_data_schueler_Sprachendaten } from '../../../core/data/schueler/Sprachendaten';

export class Abiturdaten extends JavaObject {

	public schuelerID : number = 0;

	public abiturjahr : number = 0;

	public schuljahrAbitur : number = 0;

	public readonly bewertetesHalbjahr : Array<boolean> = Array(6).fill(false);

	public readonly fachbelegungen : Vector<AbiturFachbelegung> = new Vector();

	public sprachendaten : Sprachendaten = new Sprachendaten();

	public bilingualeSprache : String | null = null;

	public latinum : boolean = false;

	public kleinesLatinum : boolean = false;

	public graecum : boolean = false;

	public hebraicum : boolean = false;

	public block1FehlstundenGesamt : number = 0;

	public block1FehlstundenUnentschuldigt : number = 0;

	public projektKursThema : String | null = null;

	public projektkursLeitfach1Kuerzel : String | null = null;

	public projektkursLeitfach2Kuerzel : String | null = null;

	public besondereLernleistung : String | null = GostBesondereLernleistung.KEINE.kuerzel;

	public besondereLernleistungNotenKuerzel : String | null = null;

	public besondereLernleistungThema : String | null = null;

	public block1AnzahlKurse : Number | null = null;

	public block1DefiziteGesamt : Number | null = null;

	public block1DefiziteLK : Number | null = null;

	public block1PunktSummeGK : Number | null = null;

	public block1PunktSummeLK : Number | null = null;

	public block1PunktSummeNormiert : Number | null = null;

	public block1NotenpunkteDurchschnitt : Number | null = null;

	public block1Zulassung : Boolean | null = null;

	public freiwilligerRuecktritt : boolean = false;

	public block2DefiziteGesamt : Number | null = null;

	public block2DefiziteLK : Number | null = null;

	public block2PunktSumme : Number | null = null;

	public gesamtPunkte : Number | null = null;

	public gesamtPunkteVerbesserung : Number | null = null;

	public gesamtPunkteVerschlechterung : Number | null = null;

	public pruefungBestanden : Boolean | null = null;

	public note : String | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.Abiturdaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): Abiturdaten {
		const obj = JSON.parse(json);
		const result = new Abiturdaten();
		if (typeof obj.schuelerID === "undefined")
			 throw new Error('invalid json format, missing attribute schuelerID');
		result.schuelerID = obj.schuelerID;
		if (typeof obj.abiturjahr === "undefined")
			 throw new Error('invalid json format, missing attribute abiturjahr');
		result.abiturjahr = obj.abiturjahr;
		if (typeof obj.schuljahrAbitur === "undefined")
			 throw new Error('invalid json format, missing attribute schuljahrAbitur');
		result.schuljahrAbitur = obj.schuljahrAbitur;
		for (let i : number = 0; i < obj.bewertetesHalbjahr.length; i++) {
			result.bewertetesHalbjahr[i] = obj.bewertetesHalbjahr[i];
		}
		if (!!obj.fachbelegungen) {
			for (let elem of obj.fachbelegungen) {
				result.fachbelegungen?.add(AbiturFachbelegung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (typeof obj.sprachendaten === "undefined")
			 throw new Error('invalid json format, missing attribute sprachendaten');
		result.sprachendaten = Sprachendaten.transpilerFromJSON(JSON.stringify(obj.sprachendaten));
		result.bilingualeSprache = typeof obj.bilingualeSprache === "undefined" ? null : obj.bilingualeSprache === null ? null : String(obj.bilingualeSprache);
		if (typeof obj.latinum === "undefined")
			 throw new Error('invalid json format, missing attribute latinum');
		result.latinum = obj.latinum;
		if (typeof obj.kleinesLatinum === "undefined")
			 throw new Error('invalid json format, missing attribute kleinesLatinum');
		result.kleinesLatinum = obj.kleinesLatinum;
		if (typeof obj.graecum === "undefined")
			 throw new Error('invalid json format, missing attribute graecum');
		result.graecum = obj.graecum;
		if (typeof obj.hebraicum === "undefined")
			 throw new Error('invalid json format, missing attribute hebraicum');
		result.hebraicum = obj.hebraicum;
		if (typeof obj.block1FehlstundenGesamt === "undefined")
			 throw new Error('invalid json format, missing attribute block1FehlstundenGesamt');
		result.block1FehlstundenGesamt = obj.block1FehlstundenGesamt;
		if (typeof obj.block1FehlstundenUnentschuldigt === "undefined")
			 throw new Error('invalid json format, missing attribute block1FehlstundenUnentschuldigt');
		result.block1FehlstundenUnentschuldigt = obj.block1FehlstundenUnentschuldigt;
		result.projektKursThema = typeof obj.projektKursThema === "undefined" ? null : obj.projektKursThema === null ? null : String(obj.projektKursThema);
		result.projektkursLeitfach1Kuerzel = typeof obj.projektkursLeitfach1Kuerzel === "undefined" ? null : obj.projektkursLeitfach1Kuerzel === null ? null : String(obj.projektkursLeitfach1Kuerzel);
		result.projektkursLeitfach2Kuerzel = typeof obj.projektkursLeitfach2Kuerzel === "undefined" ? null : obj.projektkursLeitfach2Kuerzel === null ? null : String(obj.projektkursLeitfach2Kuerzel);
		result.besondereLernleistung = typeof obj.besondereLernleistung === "undefined" ? null : obj.besondereLernleistung === null ? null : String(obj.besondereLernleistung);
		result.besondereLernleistungNotenKuerzel = typeof obj.besondereLernleistungNotenKuerzel === "undefined" ? null : obj.besondereLernleistungNotenKuerzel === null ? null : String(obj.besondereLernleistungNotenKuerzel);
		result.besondereLernleistungThema = typeof obj.besondereLernleistungThema === "undefined" ? null : obj.besondereLernleistungThema === null ? null : String(obj.besondereLernleistungThema);
		result.block1AnzahlKurse = typeof obj.block1AnzahlKurse === "undefined" ? null : obj.block1AnzahlKurse === null ? null : Number(obj.block1AnzahlKurse);
		result.block1DefiziteGesamt = typeof obj.block1DefiziteGesamt === "undefined" ? null : obj.block1DefiziteGesamt === null ? null : Number(obj.block1DefiziteGesamt);
		result.block1DefiziteLK = typeof obj.block1DefiziteLK === "undefined" ? null : obj.block1DefiziteLK === null ? null : Number(obj.block1DefiziteLK);
		result.block1PunktSummeGK = typeof obj.block1PunktSummeGK === "undefined" ? null : obj.block1PunktSummeGK === null ? null : Number(obj.block1PunktSummeGK);
		result.block1PunktSummeLK = typeof obj.block1PunktSummeLK === "undefined" ? null : obj.block1PunktSummeLK === null ? null : Number(obj.block1PunktSummeLK);
		result.block1PunktSummeNormiert = typeof obj.block1PunktSummeNormiert === "undefined" ? null : obj.block1PunktSummeNormiert === null ? null : Number(obj.block1PunktSummeNormiert);
		result.block1NotenpunkteDurchschnitt = typeof obj.block1NotenpunkteDurchschnitt === "undefined" ? null : obj.block1NotenpunkteDurchschnitt === null ? null : Number(obj.block1NotenpunkteDurchschnitt);
		result.block1Zulassung = typeof obj.block1Zulassung === "undefined" ? null : obj.block1Zulassung === null ? null : Boolean(obj.block1Zulassung);
		if (typeof obj.freiwilligerRuecktritt === "undefined")
			 throw new Error('invalid json format, missing attribute freiwilligerRuecktritt');
		result.freiwilligerRuecktritt = obj.freiwilligerRuecktritt;
		result.block2DefiziteGesamt = typeof obj.block2DefiziteGesamt === "undefined" ? null : obj.block2DefiziteGesamt === null ? null : Number(obj.block2DefiziteGesamt);
		result.block2DefiziteLK = typeof obj.block2DefiziteLK === "undefined" ? null : obj.block2DefiziteLK === null ? null : Number(obj.block2DefiziteLK);
		result.block2PunktSumme = typeof obj.block2PunktSumme === "undefined" ? null : obj.block2PunktSumme === null ? null : Number(obj.block2PunktSumme);
		result.gesamtPunkte = typeof obj.gesamtPunkte === "undefined" ? null : obj.gesamtPunkte === null ? null : Number(obj.gesamtPunkte);
		result.gesamtPunkteVerbesserung = typeof obj.gesamtPunkteVerbesserung === "undefined" ? null : obj.gesamtPunkteVerbesserung === null ? null : Number(obj.gesamtPunkteVerbesserung);
		result.gesamtPunkteVerschlechterung = typeof obj.gesamtPunkteVerschlechterung === "undefined" ? null : obj.gesamtPunkteVerschlechterung === null ? null : Number(obj.gesamtPunkteVerschlechterung);
		result.pruefungBestanden = typeof obj.pruefungBestanden === "undefined" ? null : obj.pruefungBestanden === null ? null : Boolean(obj.pruefungBestanden);
		result.note = typeof obj.note === "undefined" ? null : obj.note === null ? null : String(obj.note);
		return result;
	}

	public static transpilerToJSON(obj : Abiturdaten) : string {
		let result = '{';
		result += '"schuelerID" : ' + obj.schuelerID + ',';
		result += '"abiturjahr" : ' + obj.abiturjahr + ',';
		result += '"schuljahrAbitur" : ' + obj.schuljahrAbitur + ',';
		if (!obj.bewertetesHalbjahr) {
			result += '"bewertetesHalbjahr" : []';
		} else {
			result += '"bewertetesHalbjahr" : [ ';
			for (let i : number = 0; i < obj.bewertetesHalbjahr.length; i++) {
				let elem = obj.bewertetesHalbjahr[i];
				result += JSON.stringify(elem);
				if (i < obj.bewertetesHalbjahr.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.fachbelegungen) {
			result += '"fachbelegungen" : []';
		} else {
			result += '"fachbelegungen" : [ ';
			for (let i : number = 0; i < obj.fachbelegungen.size(); i++) {
				let elem = obj.fachbelegungen.get(i);
				result += AbiturFachbelegung.transpilerToJSON(elem);
				if (i < obj.fachbelegungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"sprachendaten" : ' + Sprachendaten.transpilerToJSON(obj.sprachendaten) + ',';
		result += '"bilingualeSprache" : ' + ((!obj.bilingualeSprache) ? 'null' : '"' + obj.bilingualeSprache.valueOf() + '"') + ',';
		result += '"latinum" : ' + obj.latinum + ',';
		result += '"kleinesLatinum" : ' + obj.kleinesLatinum + ',';
		result += '"graecum" : ' + obj.graecum + ',';
		result += '"hebraicum" : ' + obj.hebraicum + ',';
		result += '"block1FehlstundenGesamt" : ' + obj.block1FehlstundenGesamt + ',';
		result += '"block1FehlstundenUnentschuldigt" : ' + obj.block1FehlstundenUnentschuldigt + ',';
		result += '"projektKursThema" : ' + ((!obj.projektKursThema) ? 'null' : '"' + obj.projektKursThema.valueOf() + '"') + ',';
		result += '"projektkursLeitfach1Kuerzel" : ' + ((!obj.projektkursLeitfach1Kuerzel) ? 'null' : '"' + obj.projektkursLeitfach1Kuerzel.valueOf() + '"') + ',';
		result += '"projektkursLeitfach2Kuerzel" : ' + ((!obj.projektkursLeitfach2Kuerzel) ? 'null' : '"' + obj.projektkursLeitfach2Kuerzel.valueOf() + '"') + ',';
		result += '"besondereLernleistung" : ' + ((!obj.besondereLernleistung) ? 'null' : '"' + obj.besondereLernleistung.valueOf() + '"') + ',';
		result += '"besondereLernleistungNotenKuerzel" : ' + ((!obj.besondereLernleistungNotenKuerzel) ? 'null' : '"' + obj.besondereLernleistungNotenKuerzel.valueOf() + '"') + ',';
		result += '"besondereLernleistungThema" : ' + ((!obj.besondereLernleistungThema) ? 'null' : '"' + obj.besondereLernleistungThema.valueOf() + '"') + ',';
		result += '"block1AnzahlKurse" : ' + ((!obj.block1AnzahlKurse) ? 'null' : obj.block1AnzahlKurse.valueOf()) + ',';
		result += '"block1DefiziteGesamt" : ' + ((!obj.block1DefiziteGesamt) ? 'null' : obj.block1DefiziteGesamt.valueOf()) + ',';
		result += '"block1DefiziteLK" : ' + ((!obj.block1DefiziteLK) ? 'null' : obj.block1DefiziteLK.valueOf()) + ',';
		result += '"block1PunktSummeGK" : ' + ((!obj.block1PunktSummeGK) ? 'null' : obj.block1PunktSummeGK.valueOf()) + ',';
		result += '"block1PunktSummeLK" : ' + ((!obj.block1PunktSummeLK) ? 'null' : obj.block1PunktSummeLK.valueOf()) + ',';
		result += '"block1PunktSummeNormiert" : ' + ((!obj.block1PunktSummeNormiert) ? 'null' : obj.block1PunktSummeNormiert.valueOf()) + ',';
		result += '"block1NotenpunkteDurchschnitt" : ' + ((!obj.block1NotenpunkteDurchschnitt) ? 'null' : obj.block1NotenpunkteDurchschnitt.valueOf()) + ',';
		result += '"block1Zulassung" : ' + ((!obj.block1Zulassung) ? 'null' : obj.block1Zulassung.valueOf()) + ',';
		result += '"freiwilligerRuecktritt" : ' + obj.freiwilligerRuecktritt + ',';
		result += '"block2DefiziteGesamt" : ' + ((!obj.block2DefiziteGesamt) ? 'null' : obj.block2DefiziteGesamt.valueOf()) + ',';
		result += '"block2DefiziteLK" : ' + ((!obj.block2DefiziteLK) ? 'null' : obj.block2DefiziteLK.valueOf()) + ',';
		result += '"block2PunktSumme" : ' + ((!obj.block2PunktSumme) ? 'null' : obj.block2PunktSumme.valueOf()) + ',';
		result += '"gesamtPunkte" : ' + ((!obj.gesamtPunkte) ? 'null' : obj.gesamtPunkte.valueOf()) + ',';
		result += '"gesamtPunkteVerbesserung" : ' + ((!obj.gesamtPunkteVerbesserung) ? 'null' : obj.gesamtPunkteVerbesserung.valueOf()) + ',';
		result += '"gesamtPunkteVerschlechterung" : ' + ((!obj.gesamtPunkteVerschlechterung) ? 'null' : obj.gesamtPunkteVerschlechterung.valueOf()) + ',';
		result += '"pruefungBestanden" : ' + ((!obj.pruefungBestanden) ? 'null' : obj.pruefungBestanden.valueOf()) + ',';
		result += '"note" : ' + ((!obj.note) ? 'null' : '"' + obj.note.valueOf() + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Abiturdaten>) : string {
		let result = '{';
		if (typeof obj.schuelerID !== "undefined") {
			result += '"schuelerID" : ' + obj.schuelerID + ',';
		}
		if (typeof obj.abiturjahr !== "undefined") {
			result += '"abiturjahr" : ' + obj.abiturjahr + ',';
		}
		if (typeof obj.schuljahrAbitur !== "undefined") {
			result += '"schuljahrAbitur" : ' + obj.schuljahrAbitur + ',';
		}
		if (typeof obj.bewertetesHalbjahr !== "undefined") {
			let a = obj.bewertetesHalbjahr;
			if (!a) {
				result += '"bewertetesHalbjahr" : []';
			} else {
				result += '"bewertetesHalbjahr" : [ ';
				for (let i : number = 0; i < a.length; i++) {
					let elem = a[i];
					result += JSON.stringify(elem);
					if (i < a.length - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.fachbelegungen !== "undefined") {
			if (!obj.fachbelegungen) {
				result += '"fachbelegungen" : []';
			} else {
				result += '"fachbelegungen" : [ ';
				for (let i : number = 0; i < obj.fachbelegungen.size(); i++) {
					let elem = obj.fachbelegungen.get(i);
					result += AbiturFachbelegung.transpilerToJSON(elem);
					if (i < obj.fachbelegungen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.sprachendaten !== "undefined") {
			result += '"sprachendaten" : ' + Sprachendaten.transpilerToJSON(obj.sprachendaten) + ',';
		}
		if (typeof obj.bilingualeSprache !== "undefined") {
			result += '"bilingualeSprache" : ' + ((!obj.bilingualeSprache) ? 'null' : '"' + obj.bilingualeSprache.valueOf() + '"') + ',';
		}
		if (typeof obj.latinum !== "undefined") {
			result += '"latinum" : ' + obj.latinum + ',';
		}
		if (typeof obj.kleinesLatinum !== "undefined") {
			result += '"kleinesLatinum" : ' + obj.kleinesLatinum + ',';
		}
		if (typeof obj.graecum !== "undefined") {
			result += '"graecum" : ' + obj.graecum + ',';
		}
		if (typeof obj.hebraicum !== "undefined") {
			result += '"hebraicum" : ' + obj.hebraicum + ',';
		}
		if (typeof obj.block1FehlstundenGesamt !== "undefined") {
			result += '"block1FehlstundenGesamt" : ' + obj.block1FehlstundenGesamt + ',';
		}
		if (typeof obj.block1FehlstundenUnentschuldigt !== "undefined") {
			result += '"block1FehlstundenUnentschuldigt" : ' + obj.block1FehlstundenUnentschuldigt + ',';
		}
		if (typeof obj.projektKursThema !== "undefined") {
			result += '"projektKursThema" : ' + ((!obj.projektKursThema) ? 'null' : '"' + obj.projektKursThema.valueOf() + '"') + ',';
		}
		if (typeof obj.projektkursLeitfach1Kuerzel !== "undefined") {
			result += '"projektkursLeitfach1Kuerzel" : ' + ((!obj.projektkursLeitfach1Kuerzel) ? 'null' : '"' + obj.projektkursLeitfach1Kuerzel.valueOf() + '"') + ',';
		}
		if (typeof obj.projektkursLeitfach2Kuerzel !== "undefined") {
			result += '"projektkursLeitfach2Kuerzel" : ' + ((!obj.projektkursLeitfach2Kuerzel) ? 'null' : '"' + obj.projektkursLeitfach2Kuerzel.valueOf() + '"') + ',';
		}
		if (typeof obj.besondereLernleistung !== "undefined") {
			result += '"besondereLernleistung" : ' + ((!obj.besondereLernleistung) ? 'null' : '"' + obj.besondereLernleistung.valueOf() + '"') + ',';
		}
		if (typeof obj.besondereLernleistungNotenKuerzel !== "undefined") {
			result += '"besondereLernleistungNotenKuerzel" : ' + ((!obj.besondereLernleistungNotenKuerzel) ? 'null' : '"' + obj.besondereLernleistungNotenKuerzel.valueOf() + '"') + ',';
		}
		if (typeof obj.besondereLernleistungThema !== "undefined") {
			result += '"besondereLernleistungThema" : ' + ((!obj.besondereLernleistungThema) ? 'null' : '"' + obj.besondereLernleistungThema.valueOf() + '"') + ',';
		}
		if (typeof obj.block1AnzahlKurse !== "undefined") {
			result += '"block1AnzahlKurse" : ' + ((!obj.block1AnzahlKurse) ? 'null' : obj.block1AnzahlKurse.valueOf()) + ',';
		}
		if (typeof obj.block1DefiziteGesamt !== "undefined") {
			result += '"block1DefiziteGesamt" : ' + ((!obj.block1DefiziteGesamt) ? 'null' : obj.block1DefiziteGesamt.valueOf()) + ',';
		}
		if (typeof obj.block1DefiziteLK !== "undefined") {
			result += '"block1DefiziteLK" : ' + ((!obj.block1DefiziteLK) ? 'null' : obj.block1DefiziteLK.valueOf()) + ',';
		}
		if (typeof obj.block1PunktSummeGK !== "undefined") {
			result += '"block1PunktSummeGK" : ' + ((!obj.block1PunktSummeGK) ? 'null' : obj.block1PunktSummeGK.valueOf()) + ',';
		}
		if (typeof obj.block1PunktSummeLK !== "undefined") {
			result += '"block1PunktSummeLK" : ' + ((!obj.block1PunktSummeLK) ? 'null' : obj.block1PunktSummeLK.valueOf()) + ',';
		}
		if (typeof obj.block1PunktSummeNormiert !== "undefined") {
			result += '"block1PunktSummeNormiert" : ' + ((!obj.block1PunktSummeNormiert) ? 'null' : obj.block1PunktSummeNormiert.valueOf()) + ',';
		}
		if (typeof obj.block1NotenpunkteDurchschnitt !== "undefined") {
			result += '"block1NotenpunkteDurchschnitt" : ' + ((!obj.block1NotenpunkteDurchschnitt) ? 'null' : obj.block1NotenpunkteDurchschnitt.valueOf()) + ',';
		}
		if (typeof obj.block1Zulassung !== "undefined") {
			result += '"block1Zulassung" : ' + ((!obj.block1Zulassung) ? 'null' : obj.block1Zulassung.valueOf()) + ',';
		}
		if (typeof obj.freiwilligerRuecktritt !== "undefined") {
			result += '"freiwilligerRuecktritt" : ' + obj.freiwilligerRuecktritt + ',';
		}
		if (typeof obj.block2DefiziteGesamt !== "undefined") {
			result += '"block2DefiziteGesamt" : ' + ((!obj.block2DefiziteGesamt) ? 'null' : obj.block2DefiziteGesamt.valueOf()) + ',';
		}
		if (typeof obj.block2DefiziteLK !== "undefined") {
			result += '"block2DefiziteLK" : ' + ((!obj.block2DefiziteLK) ? 'null' : obj.block2DefiziteLK.valueOf()) + ',';
		}
		if (typeof obj.block2PunktSumme !== "undefined") {
			result += '"block2PunktSumme" : ' + ((!obj.block2PunktSumme) ? 'null' : obj.block2PunktSumme.valueOf()) + ',';
		}
		if (typeof obj.gesamtPunkte !== "undefined") {
			result += '"gesamtPunkte" : ' + ((!obj.gesamtPunkte) ? 'null' : obj.gesamtPunkte.valueOf()) + ',';
		}
		if (typeof obj.gesamtPunkteVerbesserung !== "undefined") {
			result += '"gesamtPunkteVerbesserung" : ' + ((!obj.gesamtPunkteVerbesserung) ? 'null' : obj.gesamtPunkteVerbesserung.valueOf()) + ',';
		}
		if (typeof obj.gesamtPunkteVerschlechterung !== "undefined") {
			result += '"gesamtPunkteVerschlechterung" : ' + ((!obj.gesamtPunkteVerschlechterung) ? 'null' : obj.gesamtPunkteVerschlechterung.valueOf()) + ',';
		}
		if (typeof obj.pruefungBestanden !== "undefined") {
			result += '"pruefungBestanden" : ' + ((!obj.pruefungBestanden) ? 'null' : obj.pruefungBestanden.valueOf()) + ',';
		}
		if (typeof obj.note !== "undefined") {
			result += '"note" : ' + ((!obj.note) ? 'null' : '"' + obj.note.valueOf() + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_gost_Abiturdaten(obj : unknown) : Abiturdaten {
	return obj as Abiturdaten;
}
