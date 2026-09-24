import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap2D, cast_de_svws_nrw_core_adt_map_HashMap2D } from '../../../core/adt/map/HashMap2D';
import { GostBeratungslehrer } from '../../../core/data/gost/GostBeratungslehrer';
import { GostLaufbahnplanungGKLKlausurvorgabe } from '../../../core/utils/gost/GostLaufbahnplanungGKLKlausurvorgabe';
import { SchuelerListeEintrag, cast_de_svws_nrw_core_data_schueler_SchuelerListeEintrag } from '../../../core/data/schueler/SchuelerListeEintrag';
import { GostLaufbahnplanungExportV1Sprachbelegung } from '../../../core/data/gost/laufbahnplanung/v1/GostLaufbahnplanungExportV1Sprachbelegung';
import { GostLaufbahnplanungExportV2Fach } from '../../../core/data/gost/laufbahnplanung/v2/GostLaufbahnplanungExportV2Fach';
import { GostFaecherManager, cast_de_svws_nrw_core_utils_gost_GostFaecherManager } from '../../../core/utils/gost/GostFaecherManager';
import { GostLaufbahnplanungExportV1Schueler } from '../../../core/data/gost/laufbahnplanung/v1/GostLaufbahnplanungExportV1Schueler';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import { AbiturFachbelegungHalbjahr } from '../../../core/data/gost/AbiturFachbelegungHalbjahr';
import { GostLaufbahnplanungExportV2SchuelerSprachpruefung } from '../../../core/data/gost/laufbahnplanung/v2/GostLaufbahnplanungExportV2SchuelerSprachpruefung';
import { Sprachpruefung } from '../../../asd/data/schueler/Sprachpruefung';
import { GostLaufbahnplanungExportV1, cast_de_svws_nrw_core_data_gost_laufbahnplanung_v1_GostLaufbahnplanungExportV1 } from '../../../core/data/gost/laufbahnplanung/v1/GostLaufbahnplanungExportV1';
import { GostLaufbahnplanungExportV2Schueler } from '../../../core/data/gost/laufbahnplanung/v2/GostLaufbahnplanungExportV2Schueler';
import { GostLaufbahnplanungExportV2, cast_de_svws_nrw_core_data_gost_laufbahnplanung_v2_GostLaufbahnplanungExportV2 } from '../../../core/data/gost/laufbahnplanung/v2/GostLaufbahnplanungExportV2';
import { GostLaufbahnplanungExportV2SchuelerFachbelegung } from '../../../core/data/gost/laufbahnplanung/v2/GostLaufbahnplanungExportV2SchuelerFachbelegung';
import { GostJahrgangsdaten, cast_de_svws_nrw_core_data_gost_GostJahrgangsdaten } from '../../../core/data/gost/GostJahrgangsdaten';
import { GostHalbjahr } from '../../../core/types/gost/GostHalbjahr';
import { Schulgliederung } from '../../../asd/types/schule/Schulgliederung';
import type { List } from '../../../java/util/List';
import { GostLaufbahnplanungExportV1Beratungslehrer } from '../../../core/data/gost/laufbahnplanung/v1/GostLaufbahnplanungExportV1Beratungslehrer';
import { GostLaufbahnplanungExportV2SchuelerSprachbelegung } from '../../../core/data/gost/laufbahnplanung/v2/GostLaufbahnplanungExportV2SchuelerSprachbelegung';
import { GostFach } from '../../../core/data/gost/GostFach';
import { AbiturFachbelegung } from '../../../core/data/gost/AbiturFachbelegung';
import { GostLaufbahnplanungExportV2GKL } from '../../../core/data/gost/laufbahnplanung/v2/GostLaufbahnplanungExportV2GKL';
import { GostLaufbahnplanungExportV2Beratungslehrer } from '../../../core/data/gost/laufbahnplanung/v2/GostLaufbahnplanungExportV2Beratungslehrer';
import { GostJahrgang, cast_de_svws_nrw_core_data_gost_GostJahrgang } from '../../../core/data/gost/GostJahrgang';
import { GostLaufbahnplanungExportV1Fachkombination } from '../../../core/data/gost/laufbahnplanung/v1/GostLaufbahnplanungExportV1Fachkombination';
import { Abiturdaten, cast_de_svws_nrw_core_data_gost_Abiturdaten } from '../../../core/data/gost/Abiturdaten';
import { SchuleStammdaten, cast_de_svws_nrw_asd_data_schule_SchuleStammdaten } from '../../../asd/data/schule/SchuleStammdaten';
import { GostJahrgangFachkombination } from '../../../core/data/gost/GostJahrgangFachkombination';
import { Sprachbelegung } from '../../../asd/data/schueler/Sprachbelegung';
import { GostKlausurvorgabe } from '../../../core/data/gost/klausuren/GostKlausurvorgabe';
import { GostLaufbahnplanungExportV1Fach } from '../../../core/data/gost/laufbahnplanung/v1/GostLaufbahnplanungExportV1Fach';
import { GostLaufbahnplanungExportV1Sprachpruefung } from '../../../core/data/gost/laufbahnplanung/v1/GostLaufbahnplanungExportV1Sprachpruefung';
import { GostSchuelerGKLWahl, cast_de_svws_nrw_core_data_gost_GostSchuelerGKLWahl } from '../../../core/data/gost/GostSchuelerGKLWahl';
import { GostLaufbahnplanungExportV2Fachkombination } from '../../../core/data/gost/laufbahnplanung/v2/GostLaufbahnplanungExportV2Fachkombination';
import { Class } from '../../../java/lang/Class';
import { GostLaufbahnplanungExportV1Fachbelegung } from '../../../core/data/gost/laufbahnplanung/v1/GostLaufbahnplanungExportV1Fachbelegung';
import type { JavaMap } from '../../../java/util/JavaMap';
import { cast_java_util_Map } from '../../../java/util/JavaMap';

export class GostLaufbahnplanungDataHandler extends JavaObject {

	private readonly schuleStammdaten: SchuleStammdaten;

	private readonly schueler: SchuelerListeEintrag;

	private readonly idSchuelerEncrpyted: string;

	private readonly gostJahrgang: GostJahrgang;

	private readonly gostJahrgangsdaten: GostJahrgangsdaten;

	private readonly faecherManager: GostFaecherManager;

	private abiturdaten: Abiturdaten;

	private readonly gklMoeglich: HashMap2D<number, GostHalbjahr, List<GostLaufbahnplanungGKLKlausurvorgabe>>;

	private readonly mapKlausurvorgaben: JavaMap<number, GostLaufbahnplanungGKLKlausurvorgabe>;

	private gklWahlen: GostSchuelerGKLWahl;


	private constructor(schuleStammdaten: SchuleStammdaten, schueler: SchuelerListeEintrag, idSchuelerEncrpyted: string, gostJahrgang: GostJahrgang, gostJahrgangsdaten: GostJahrgangsdaten, faecherManager: GostFaecherManager, abiturdaten: Abiturdaten, gklMoeglich: HashMap2D<number, GostHalbjahr, List<GostLaufbahnplanungGKLKlausurvorgabe>>, mapKlausurvorgaben: JavaMap<number, GostLaufbahnplanungGKLKlausurvorgabe>, gklWahlen: GostSchuelerGKLWahl);

	private constructor(daten: GostLaufbahnplanungExportV1);

	private constructor(daten: GostLaufbahnplanungExportV2);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	private constructor(__param0: GostLaufbahnplanungExportV1 | GostLaufbahnplanungExportV2 | SchuleStammdaten, __param1?: SchuelerListeEintrag, __param2?: string, __param3?: GostJahrgang, __param4?: GostJahrgangsdaten, __param5?: GostFaecherManager, __param6?: Abiturdaten, __param7?: HashMap2D<number, GostHalbjahr, List<GostLaufbahnplanungGKLKlausurvorgabe>>, __param8?: JavaMap<number, GostLaufbahnplanungGKLKlausurvorgabe>, __param9?: GostSchuelerGKLWahl) {
		super();
		if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.asd.data.schule.SchuleStammdaten')))) && ((__param1 !== undefined) && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('de.svws_nrw.core.data.schueler.SchuelerListeEintrag')))) && ((__param2 !== undefined) && (typeof __param2 === "string")) && ((__param3 !== undefined) && ((__param3 instanceof JavaObject) && (__param3.isTranspiledInstanceOf('de.svws_nrw.core.data.gost.GostJahrgang')))) && ((__param4 !== undefined) && ((__param4 instanceof JavaObject) && (__param4.isTranspiledInstanceOf('de.svws_nrw.core.data.gost.GostJahrgangsdaten')))) && ((__param5 !== undefined) && ((__param5 instanceof JavaObject) && (__param5.isTranspiledInstanceOf('de.svws_nrw.core.utils.gost.GostFaecherManager')))) && ((__param6 !== undefined) && ((__param6 instanceof JavaObject) && (__param6.isTranspiledInstanceOf('de.svws_nrw.core.data.gost.Abiturdaten')))) && ((__param7 !== undefined) && ((__param7 instanceof JavaObject) && (__param7.isTranspiledInstanceOf('de.svws_nrw.core.adt.map.HashMap2D'))) || (__param7 === null)) && ((__param8 !== undefined) && ((__param8 instanceof JavaObject) && (__param8.isTranspiledInstanceOf('java.util.Map'))) || (__param8 === null)) && ((__param9 !== undefined) && ((__param9 instanceof JavaObject) && (__param9.isTranspiledInstanceOf('de.svws_nrw.core.data.gost.GostSchuelerGKLWahl'))))) {
			const schuleStammdaten: SchuleStammdaten = cast_de_svws_nrw_asd_data_schule_SchuleStammdaten(__param0);
			const schueler: SchuelerListeEintrag = cast_de_svws_nrw_core_data_schueler_SchuelerListeEintrag(__param1);
			const idSchuelerEncrpyted: string = __param2;
			const gostJahrgang: GostJahrgang = cast_de_svws_nrw_core_data_gost_GostJahrgang(__param3);
			const gostJahrgangsdaten: GostJahrgangsdaten = cast_de_svws_nrw_core_data_gost_GostJahrgangsdaten(__param4);
			const faecherManager: GostFaecherManager = cast_de_svws_nrw_core_utils_gost_GostFaecherManager(__param5);
			const abiturdaten: Abiturdaten = cast_de_svws_nrw_core_data_gost_Abiturdaten(__param6);
			const gklMoeglich: HashMap2D<number, GostHalbjahr, List<GostLaufbahnplanungGKLKlausurvorgabe>> = cast_de_svws_nrw_core_adt_map_HashMap2D(__param7);
			const mapKlausurvorgaben: JavaMap<number, GostLaufbahnplanungGKLKlausurvorgabe> = cast_java_util_Map(__param8);
			const gklWahlen: GostSchuelerGKLWahl = cast_de_svws_nrw_core_data_gost_GostSchuelerGKLWahl(__param9);
			this.schuleStammdaten = schuleStammdaten;
			this.schueler = schueler;
			this.idSchuelerEncrpyted = idSchuelerEncrpyted;
			this.gostJahrgang = gostJahrgang;
			this.gostJahrgangsdaten = gostJahrgangsdaten;
			this.faecherManager = faecherManager;
			this.abiturdaten = abiturdaten;
			this.gklMoeglich = gklMoeglich;
			this.mapKlausurvorgaben = mapKlausurvorgaben;
			this.gklWahlen = gklWahlen;
		} else if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.core.data.gost.laufbahnplanung.v1.GostLaufbahnplanungExportV1')))) && (__param1 === undefined) && (__param2 === undefined) && (__param3 === undefined) && (__param4 === undefined) && (__param5 === undefined) && (__param6 === undefined) && (__param7 === undefined) && (__param8 === undefined) && (__param9 === undefined)) {
			const daten: GostLaufbahnplanungExportV1 = cast_de_svws_nrw_core_data_gost_laufbahnplanung_v1_GostLaufbahnplanungExportV1(__param0);
			this.schuleStammdaten = GostLaufbahnplanungDataHandler.importV1SchuleStammdaten(daten);
			this.faecherManager = GostLaufbahnplanungDataHandler.importV1GostFaecherManager(daten);
			const planungsdaten: GostLaufbahnplanungExportV1Schueler = daten.schueler.get(0);
			this.schueler = GostLaufbahnplanungDataHandler.importV1Schueler(daten, planungsdaten);
			this.abiturdaten = GostLaufbahnplanungDataHandler.importV1Abiturdaten(daten, planungsdaten, this.faecherManager);
			this.gostJahrgang = GostLaufbahnplanungDataHandler.importV1GostJahrgang(daten);
			this.gostJahrgangsdaten = GostLaufbahnplanungDataHandler.importV1GostJahrgangsdaten(daten, this.gostJahrgang);
			this.gostJahrgangsdaten.beratungslehrer.addAll(GostLaufbahnplanungDataHandler.importV1Beratungslehrer(daten.beratungslehrer));
			this.gostJahrgangsdaten.istBlockungFestgelegt = this.abiturdaten.bewertetesHalbjahr;
			this.idSchuelerEncrpyted = planungsdaten.idEnc;
			this.gklMoeglich = new HashMap2D();
			this.mapKlausurvorgaben = new HashMap();
			this.gklWahlen = new GostSchuelerGKLWahl();
		} else if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.core.data.gost.laufbahnplanung.v2.GostLaufbahnplanungExportV2')))) && (__param1 === undefined) && (__param2 === undefined) && (__param3 === undefined) && (__param4 === undefined) && (__param5 === undefined) && (__param6 === undefined) && (__param7 === undefined) && (__param8 === undefined) && (__param9 === undefined)) {
			const daten: GostLaufbahnplanungExportV2 = cast_de_svws_nrw_core_data_gost_laufbahnplanung_v2_GostLaufbahnplanungExportV2(__param0);
			this.schuleStammdaten = GostLaufbahnplanungDataHandler.importV2SchuleStammdaten(daten);
			this.faecherManager = GostLaufbahnplanungDataHandler.importV2GostFaecherManager(daten);
			const planungsdaten: GostLaufbahnplanungExportV2Schueler = daten.schueler.get(0);
			this.schueler = GostLaufbahnplanungDataHandler.importV2Schueler(daten, planungsdaten);
			this.abiturdaten = GostLaufbahnplanungDataHandler.importV2Abiturdaten(daten, planungsdaten, this.faecherManager);
			this.gostJahrgang = GostLaufbahnplanungDataHandler.importV2GostJahrgang(daten);
			this.gostJahrgangsdaten = GostLaufbahnplanungDataHandler.importV2GostJahrgangsdaten(daten, this.gostJahrgang);
			this.gostJahrgangsdaten.beratungslehrer.addAll(GostLaufbahnplanungDataHandler.importV2Beratungslehrer(daten.beratungslehrer));
			this.gostJahrgangsdaten.istBlockungFestgelegt = this.abiturdaten.bewertetesHalbjahr;
			this.idSchuelerEncrpyted = planungsdaten.idEnc;
			this.gklMoeglich = new HashMap2D();
			this.mapKlausurvorgaben = new HashMap();
			this.gklWahlen = new GostSchuelerGKLWahl();
			this.importV2GKL(planungsdaten, daten.gkl, this.faecherManager);
		} else throw new Error('invalid method overload');
	}

	/**
	 * Gibt die Stammdaten der Schule, welche für die Laufbahnplanung relevant sind, zurück.
	 *
	 * @return die für die Laufbahnplanung relevanten Stammdaten der Schule
	 */
	public getSchuleStammdaten(): SchuleStammdaten {
		return this.schuleStammdaten;
	}

	/**
	 * Gibt den Schüler-Listen-Eintrag für den Schüler zurück, dessen Laufbahndaten angezeigt werden.
	 *
	 * @return der Schüler-Listen-Eintrag
	 */
	public getSchueler(): SchuelerListeEintrag {
		return this.schueler;
	}

	/**
	 * Gibt die verschlüsselte ID des Schülers für die Laufbahnplanungsdatei zurück.
	 *
	 * @return die verschlüsselte ID des Schülers für die Laufbahnplanungsdatei
	 */
	public getIdSchuelerEncrpyted(): string {
		return this.idSchuelerEncrpyted;
	}

	/**
	 * Grundlegende Informationen zum Jahrgang der Gymnasialen Oberstufe
	 *
	 * @return die grundlegenden Informationen zum Jahrgang der Gymnasialen Oberstufe
	 */
	public getGostJahrgang(): GostJahrgang {
		return this.gostJahrgang;
	}

	/**
	 * Ausführlichere Informationen zum Jahrgang der Gymnasialen Oberstufe
	 *
	 * @return die ausführlicheren Informationen zum Jahrgang der Gymnasialen Oberstufe
	 */
	public getGostJahrgangsdaten(): GostJahrgangsdaten {
		return this.gostJahrgangsdaten;
	}

	/**
	 * Der Manager für die Fächer der Gymnasialen Oberstufe
	 *
	 * @return der Fächer-Manager
	 */
	public getFaecherManager(): GostFaecherManager {
		return this.faecherManager;
	}

	/**
	 * Gibt die Abiturdaten des Schülers der Laufbahnplanungsdatei zurück.
	 *
	 * @return die Abiturdaten des Schülers
	 */
	public getAbiturdaten(): Abiturdaten {
		return this.abiturdaten;
	}

	/**
	 * Gibt eine Map mit der Zuordnung von Klausurvorgaben mit Festlegungen zu GKLs zu der ID des zugehörigen Faches und dem Halbjahr der Vorgabeinformationen zurück.
	 *
	 * @return die zweidimensionale Map mit den Klausurvorgaben mit Festlegungen zu GKLs
	 */
	public getGklMoeglich(): HashMap2D<number, GostHalbjahr, List<GostLaufbahnplanungGKLKlausurvorgabe>> {
		return this.gklMoeglich;
	}

	/**
	 * Gibt eine Map mit der Zuordnung von Klausurvorgaben mit Festlegungen zu GKLs zu der ID der Klausurvorgabe zurück.
	 *
	 * @return die Map mit den Klausurvorgaben zugeordnet zu der Vorgabe-ID
	 */
	public getMapKlausurvorgaben(): JavaMap<number, GostLaufbahnplanungGKLKlausurvorgabe> {
		return this.mapKlausurvorgaben;
	}

	/**
	 * Gibt die Informationen zu den GKL-Wahlen des Schülers zurück.
	 *
	 * @return die Informationen zu den GKL-Wahlen des Schülers
	 */
	public getGklWahlen(): GostSchuelerGKLWahl {
		return this.gklWahlen;
	}

	/**
	 * Ersetzt die internen Abiturdaten mit den neuen übergebenen Abiturdaten
	 *
	 * @param abiturdaten   die neuen Abiturdaten
	 */
	public replaceAbiturdaten(abiturdaten: Abiturdaten): void {
		this.abiturdaten = abiturdaten;
	}

	/**
	 * Ersetzt die internen GKL-Wahlen mit den neuen übergebenen GKL-Wahlen
	 *
	 * @param gklWahlen   die neuen GKL-Wahlen
	 */
	public replaceGKLWahlen(gklWahlen: GostSchuelerGKLWahl): void {
		this.gklWahlen = gklWahlen;
	}

	private static importV1SchuleStammdaten(daten: GostLaufbahnplanungExportV1): SchuleStammdaten {
		const schuleStammdaten: SchuleStammdaten = new SchuleStammdaten();
		schuleStammdaten.schulNr = daten.schulNr;
		schuleStammdaten.bezeichnung1 = daten.schulBezeichnung1;
		schuleStammdaten.bezeichnung2 = daten.schulBezeichnung2;
		schuleStammdaten.bezeichnung3 = daten.schulBezeichnung3;
		return schuleStammdaten;
	}

	private static importV1GostJahrgang(daten: GostLaufbahnplanungExportV1): GostJahrgang {
		const gostJahrgang: GostJahrgang = new GostJahrgang();
		gostJahrgang.abiturjahr = daten.abiturjahr;
		gostJahrgang.jahrgang = daten.jahrgang;
		gostJahrgang.bezeichnung = "Abiturjahr " + daten.abiturjahr;
		gostJahrgang.istAbgeschlossen = false;
		return gostJahrgang;
	}

	private static importV1GostJahrgangsdaten(daten: GostLaufbahnplanungExportV1, gostJahrgang: GostJahrgang): GostJahrgangsdaten {
		const gostJahrgangsdaten: GostJahrgangsdaten = new GostJahrgangsdaten();
		gostJahrgangsdaten.abiturjahr = gostJahrgang.abiturjahr;
		gostJahrgangsdaten.jahrgang = gostJahrgang.jahrgang;
		gostJahrgangsdaten.bezeichnung = gostJahrgang.bezeichnung;
		gostJahrgangsdaten.istAbgeschlossen = gostJahrgang.istAbgeschlossen;
		gostJahrgangsdaten.hatZusatzkursGE = daten.hatZusatzkursGE;
		gostJahrgangsdaten.beginnZusatzkursGE = daten.beginnZusatzkursGE;
		gostJahrgangsdaten.hatZusatzkursSW = daten.hatZusatzkursSW;
		gostJahrgangsdaten.beginnZusatzkursSW = daten.beginnZusatzkursSW;
		gostJahrgangsdaten.textBeratungsbogen = daten.textBeratungsbogen;
		gostJahrgangsdaten.textMailversand = null;
		return gostJahrgangsdaten;
	}

	private static importV1Beratungslehrer(listBeratungslehrer: List<GostLaufbahnplanungExportV1Beratungslehrer>): List<GostBeratungslehrer> {
		const result: List<GostBeratungslehrer> = new ArrayList<GostBeratungslehrer>();
		for (const beratungslehrer of listBeratungslehrer) {
			const l: GostBeratungslehrer = new GostBeratungslehrer();
			l.id = beratungslehrer.id;
			l.kuerzel = beratungslehrer.kuerzel;
			l.nachname = beratungslehrer.nachname;
			l.vorname = beratungslehrer.vorname;
			result.add(l);
		}
		return result;
	}

	private static importV1GostFaecher(faecher: List<GostLaufbahnplanungExportV1Fach>): List<GostFach> {
		const result: List<GostFach> = new ArrayList<GostFach>();
		for (const fach of faecher) {
			const f: GostFach = new GostFach();
			f.id = fach.id;
			f.kuerzel = fach.kuerzel;
			f.kuerzelAnzeige = fach.kuerzelAnzeige;
			f.bezeichnung = fach.bezeichnung;
			f.sortierung = fach.sortierung;
			f.istPruefungsordnungsRelevant = fach.istPruefungsordnungsRelevant;
			f.istFremdsprache = fach.istFremdsprache;
			f.istFremdSpracheNeuEinsetzend = fach.istFremdSpracheNeuEinsetzend;
			f.biliSprache = fach.biliSprache;
			f.istMoeglichAbiLK = fach.istMoeglichAbiLK;
			f.istMoeglichAbiGK = fach.istMoeglichAbiGK;
			f.istMoeglichEF1 = fach.istMoeglichEF1;
			f.istMoeglichEF2 = fach.istMoeglichEF2;
			f.istMoeglichQ11 = fach.istMoeglichQ11;
			f.istMoeglichQ12 = fach.istMoeglichQ12;
			f.istMoeglichQ21 = fach.istMoeglichQ21;
			f.istMoeglichQ22 = fach.istMoeglichQ22;
			f.wochenstundenQualifikationsphase = fach.wochenstundenQualifikationsphase;
			f.projektKursLeitfach1ID = fach.projektKursLeitfach1ID;
			f.projektKursLeitfach1Kuerzel = fach.projektKursLeitfach1Kuerzel;
			f.projektKursLeitfach2ID = fach.projektKursLeitfach2ID;
			f.projektKursLeitfach2Kuerzel = fach.projektKursLeitfach2Kuerzel;
			result.add(f);
		}
		return result;
	}

	private static importV1Fachkombinationen(fachkombis: List<GostLaufbahnplanungExportV1Fachkombination>): List<GostJahrgangFachkombination> {
		const result: List<GostJahrgangFachkombination> = new ArrayList<GostJahrgangFachkombination>();
		for (const fachkombi of fachkombis) {
			const k: GostJahrgangFachkombination = new GostJahrgangFachkombination();
			k.id = fachkombi.id;
			k.abiturjahr = fachkombi.abiturjahr;
			k.fachID1 = fachkombi.fachID1;
			k.kursart1 = fachkombi.kursart1;
			k.fachID2 = fachkombi.fachID2;
			k.kursart2 = fachkombi.kursart2;
			k.gueltigInHalbjahr[0] = fachkombi.gueltigInHalbjahr[0];
			k.gueltigInHalbjahr[1] = fachkombi.gueltigInHalbjahr[1];
			k.gueltigInHalbjahr[2] = fachkombi.gueltigInHalbjahr[2];
			k.gueltigInHalbjahr[3] = fachkombi.gueltigInHalbjahr[3];
			k.gueltigInHalbjahr[4] = fachkombi.gueltigInHalbjahr[4];
			k.gueltigInHalbjahr[5] = fachkombi.gueltigInHalbjahr[5];
			k.typ = fachkombi.typ;
			k.hinweistext = fachkombi.hinweistext;
			result.add(k);
		}
		return result;
	}

	private static importV1GostFaecherManager(daten: GostLaufbahnplanungExportV1): GostFaecherManager {
		const faecherManager: GostFaecherManager | null = new GostFaecherManager(daten.abiturjahr - 1, GostLaufbahnplanungDataHandler.importV1GostFaecher(daten.faecher));
		faecherManager.addFachkombinationenAll(GostLaufbahnplanungDataHandler.importV1Fachkombinationen(daten.fachkombinationen));
		return faecherManager;
	}

	private static importV1Schueler(daten: GostLaufbahnplanungExportV1, planungsdaten: GostLaufbahnplanungExportV1Schueler): SchuelerListeEintrag {
		const schueler: SchuelerListeEintrag = new SchuelerListeEintrag();
		schueler.id = planungsdaten.id;
		schueler.vorname = planungsdaten.vorname;
		schueler.nachname = planungsdaten.nachname;
		schueler.geschlecht = planungsdaten.geschlecht;
		schueler.abiturjahrgang = daten.abiturjahr;
		return schueler;
	}

	private static importV1Sprachdaten(planungsdaten: GostLaufbahnplanungExportV1Schueler, abiturdaten: Abiturdaten): void {
		for (const bel of planungsdaten.sprachendaten.belegungen) {
			const mappedBel: Sprachbelegung = new Sprachbelegung();
			mappedBel.sprache = bel.sprache;
			mappedBel.istNachweis = (bel.istNachweis !== null) && bel.istNachweis;
			mappedBel.reihenfolge = bel.reihenfolge;
			mappedBel.belegungVonJahrgang = bel.belegungVonJahrgang;
			mappedBel.belegungVonAbschnitt = bel.belegungVonAbschnitt;
			mappedBel.belegungBisJahrgang = bel.belegungBisJahrgang;
			mappedBel.belegungBisAbschnitt = bel.belegungBisAbschnitt;
			mappedBel.referenzniveau = bel.referenzniveau;
			mappedBel.hatKleinesLatinum = bel.hatKleinesLatinum;
			mappedBel.hatLatinum = bel.hatLatinum;
			mappedBel.hatGraecum = bel.hatGraecum;
			mappedBel.hatHebraicum = bel.hatHebraicum;
			abiturdaten.sprachendaten.belegungen.add(mappedBel);
		}
		for (const pruef of planungsdaten.sprachendaten.pruefungen) {
			const mappedPruef: Sprachpruefung = new Sprachpruefung();
			mappedPruef.sprache = pruef.sprache;
			mappedPruef.jahrgang = pruef.jahrgang;
			mappedPruef.anspruchsniveauId = pruef.anspruchsniveauId;
			mappedPruef.pruefungsdatum = pruef.pruefungsdatum;
			mappedPruef.ersetzteSprache = pruef.ersetzteSprache;
			mappedPruef.istHSUPruefung = pruef.istHSUPruefung;
			mappedPruef.istFeststellungspruefung = pruef.istFeststellungspruefung;
			mappedPruef.kannErstePflichtfremdspracheErsetzen = pruef.kannErstePflichtfremdspracheErsetzen;
			mappedPruef.kannZweitePflichtfremdspracheErsetzen = pruef.kannZweitePflichtfremdspracheErsetzen;
			mappedPruef.kannWahlpflichtfremdspracheErsetzen = pruef.kannWahlpflichtfremdspracheErsetzen;
			mappedPruef.kannBelegungAlsFortgefuehrteSpracheErlauben = pruef.kannBelegungAlsFortgefuehrteSpracheErlauben;
			mappedPruef.referenzniveau = pruef.referenzniveau;
			mappedPruef.note = pruef.note;
			mappedPruef.zeugnisbezeichnung = pruef.zeugnisbezeichnung;
			abiturdaten.sprachendaten.pruefungen.add(mappedPruef);
		}
		abiturdaten.bilingualeSprache = planungsdaten.bilingualeSprache;
	}

	private static importV1Belegungen(planungsdaten: GostLaufbahnplanungExportV1Schueler, abiturdaten: Abiturdaten, faecherManager: GostFaecherManager): void {
		for (const hj of GostHalbjahr.values()) {
			abiturdaten.bewertetesHalbjahr[hj.id] = planungsdaten.bewertetesHalbjahr[hj.id];
		}
		for (let i: number = 0; i < planungsdaten.fachbelegungen.size(); i++) {
			const belegung: AbiturFachbelegung = new AbiturFachbelegung();
			const fb: GostLaufbahnplanungExportV1Fachbelegung = planungsdaten.fachbelegungen.get(i);
			const fach: GostFach | null = faecherManager.get(fb.fachID);
			if (fach === null) {
				continue;
			}
			belegung.fachID = fb.fachID;
			belegung.abiturFach = fb.abiturFach;
			belegung.istFSNeu = fach.istFremdSpracheNeuEinsetzend;
			for (const hj of GostHalbjahr.values()) {
				const kursart: string | null = fb.kursart[hj.id];
				if (kursart === null) {
					continue;
				}
				const hjBelegung: AbiturFachbelegungHalbjahr = new AbiturFachbelegungHalbjahr();
				hjBelegung.halbjahrKuerzel = hj.kuerzel;
				hjBelegung.kursartKuerzel = kursart;
				hjBelegung.schriftlich = fb.schriftlich[hj.id];
				hjBelegung.biliSprache = fach.biliSprache;
				if (JavaObject.equalsTranspiler("PX", (fach.kuerzel))) {
					hjBelegung.wochenstunden = fach.wochenstundenQualifikationsphase;
				}
				if (JavaObject.equalsTranspiler("AT", (kursart))) {
					hjBelegung.notenkuerzel = "AT";
				}
				belegung.belegungen[hj.id] = hjBelegung;
				belegung.letzteKursart = kursart;
			}
			abiturdaten.fachbelegungen.add(belegung);
		}
	}

	private static importV1Abiturdaten(daten: GostLaufbahnplanungExportV1, planungsdaten: GostLaufbahnplanungExportV1Schueler, faecherManager: GostFaecherManager): Abiturdaten {
		const abiturdaten: Abiturdaten = new Abiturdaten();
		abiturdaten.abiturjahr = daten.abiturjahr;
		GostLaufbahnplanungDataHandler.importV1Sprachdaten(planungsdaten, abiturdaten);
		GostLaufbahnplanungDataHandler.importV1Belegungen(planungsdaten, abiturdaten, faecherManager);
		return abiturdaten;
	}

	/**
	 * Import die Laufbahnplanungsdaten in der Version 1 des Datenformates
	 *
	 * @param daten   die Laufbahnplanungsdaten in der Version 1
	 *
	 * @return die geladenen Laufbahnplanungsdaten
	 */
	public static importV1(daten: GostLaufbahnplanungExportV1): GostLaufbahnplanungDataHandler {
		return new GostLaufbahnplanungDataHandler(daten);
	}

	private static importV2SchuleStammdaten(daten: GostLaufbahnplanungExportV2): SchuleStammdaten {
		const schuleStammdaten: SchuleStammdaten = new SchuleStammdaten();
		schuleStammdaten.schulNr = daten.schulNr;
		schuleStammdaten.bezeichnung1 = daten.schulBezeichnung1;
		schuleStammdaten.bezeichnung2 = daten.schulBezeichnung2;
		schuleStammdaten.bezeichnung3 = daten.schulBezeichnung3;
		return schuleStammdaten;
	}

	private static importV2GostJahrgang(daten: GostLaufbahnplanungExportV2): GostJahrgang {
		const gostJahrgang: GostJahrgang = new GostJahrgang();
		gostJahrgang.abiturjahr = daten.abiturjahr;
		gostJahrgang.jahrgang = daten.jahrgang;
		gostJahrgang.bezeichnung = "Abiturjahr " + daten.abiturjahr;
		gostJahrgang.istAbgeschlossen = false;
		return gostJahrgang;
	}

	private static importV2GostJahrgangsdaten(daten: GostLaufbahnplanungExportV2, gostJahrgang: GostJahrgang): GostJahrgangsdaten {
		const gostJahrgangsdaten: GostJahrgangsdaten = new GostJahrgangsdaten();
		gostJahrgangsdaten.abiturjahr = gostJahrgang.abiturjahr;
		gostJahrgangsdaten.jahrgang = gostJahrgang.jahrgang;
		gostJahrgangsdaten.bezeichnung = gostJahrgang.bezeichnung;
		gostJahrgangsdaten.istAbgeschlossen = gostJahrgang.istAbgeschlossen;
		gostJahrgangsdaten.hatZusatzkursGE = daten.hatZusatzkursGE;
		gostJahrgangsdaten.beginnZusatzkursGE = daten.beginnZusatzkursGE;
		gostJahrgangsdaten.hatZusatzkursSW = daten.hatZusatzkursSW;
		gostJahrgangsdaten.beginnZusatzkursSW = daten.beginnZusatzkursSW;
		gostJahrgangsdaten.textBeratungsbogen = daten.textBeratungsbogen;
		gostJahrgangsdaten.textMailversand = null;
		return gostJahrgangsdaten;
	}

	private static importV2Beratungslehrer(listBeratungslehrer: List<GostLaufbahnplanungExportV2Beratungslehrer>): List<GostBeratungslehrer> {
		const result: List<GostBeratungslehrer> = new ArrayList<GostBeratungslehrer>();
		for (const beratungslehrer of listBeratungslehrer) {
			const l: GostBeratungslehrer = new GostBeratungslehrer();
			l.id = beratungslehrer.id;
			l.kuerzel = beratungslehrer.kuerzel;
			l.nachname = beratungslehrer.nachname;
			l.vorname = beratungslehrer.vorname;
			result.add(l);
		}
		return result;
	}

	private static importV2GostFaecher(faecher: List<GostLaufbahnplanungExportV2Fach>): List<GostFach> {
		const result: List<GostFach> = new ArrayList<GostFach>();
		for (const fach of faecher) {
			const f: GostFach = new GostFach();
			f.id = fach.id;
			f.kuerzel = fach.kuerzel;
			f.kuerzelAnzeige = fach.kuerzelAnzeige;
			f.bezeichnung = fach.bezeichnung;
			f.sortierung = fach.sortierung;
			f.istPruefungsordnungsRelevant = fach.istPruefungsordnungsRelevant;
			f.istFremdsprache = fach.istFremdsprache;
			f.istFremdSpracheNeuEinsetzend = fach.istFremdSpracheNeuEinsetzend;
			f.biliSprache = fach.biliSprache;
			f.istMoeglichAbiLK = fach.istMoeglichAbiLK;
			f.istMoeglichAbiGK = fach.istMoeglichAbiGK;
			f.istMoeglichEF1 = fach.istMoeglich[0];
			f.istMoeglichEF2 = fach.istMoeglich[1];
			f.istMoeglichQ11 = fach.istMoeglich[2];
			f.istMoeglichQ12 = fach.istMoeglich[3];
			f.istMoeglichQ21 = fach.istMoeglich[4];
			f.istMoeglichQ22 = fach.istMoeglich[5];
			f.wochenstundenQualifikationsphase = fach.wochenstundenQualifikationsphase;
			f.projektKursLeitfach1ID = fach.referenzfach1ID;
			f.projektKursLeitfach2ID = fach.referenzfach2ID;
			result.add(f);
		}
		return result;
	}

	private static importV2Fachkombinationen(fachkombis: List<GostLaufbahnplanungExportV2Fachkombination>): List<GostJahrgangFachkombination> {
		const result: List<GostJahrgangFachkombination> = new ArrayList<GostJahrgangFachkombination>();
		for (const fachkombi of fachkombis) {
			const k: GostJahrgangFachkombination = new GostJahrgangFachkombination();
			k.id = fachkombi.id;
			k.abiturjahr = fachkombi.abiturjahr;
			k.fachID1 = fachkombi.fachID1;
			k.kursart1 = fachkombi.kursart1;
			k.fachID2 = fachkombi.fachID2;
			k.kursart2 = fachkombi.kursart2;
			k.gueltigInHalbjahr[0] = fachkombi.gueltigInHalbjahr[0];
			k.gueltigInHalbjahr[1] = fachkombi.gueltigInHalbjahr[1];
			k.gueltigInHalbjahr[2] = fachkombi.gueltigInHalbjahr[2];
			k.gueltigInHalbjahr[3] = fachkombi.gueltigInHalbjahr[3];
			k.gueltigInHalbjahr[4] = fachkombi.gueltigInHalbjahr[4];
			k.gueltigInHalbjahr[5] = fachkombi.gueltigInHalbjahr[5];
			k.typ = fachkombi.typ;
			k.hinweistext = fachkombi.hinweistext;
			result.add(k);
		}
		return result;
	}

	private static importV2GostFaecherManager(daten: GostLaufbahnplanungExportV2): GostFaecherManager {
		const faecherManager: GostFaecherManager | null = new GostFaecherManager(daten.abiturjahr - 1, GostLaufbahnplanungDataHandler.importV2GostFaecher(daten.faecher));
		faecherManager.addFachkombinationenAll(GostLaufbahnplanungDataHandler.importV2Fachkombinationen(daten.fachkombinationen));
		return faecherManager;
	}

	private static importV2Schueler(daten: GostLaufbahnplanungExportV2, planungsdaten: GostLaufbahnplanungExportV2Schueler): SchuelerListeEintrag {
		const schueler: SchuelerListeEintrag = new SchuelerListeEintrag();
		schueler.id = planungsdaten.id;
		schueler.vorname = planungsdaten.vorname;
		schueler.nachname = planungsdaten.nachname;
		schueler.geschlecht = planungsdaten.geschlecht;
		schueler.abiturjahrgang = daten.abiturjahr;
		schueler.idSchulgliederung = planungsdaten.istG8 ? Schulgliederung.GY8.historie().getLast().id : Schulgliederung.GY9.historie().getLast().id;
		return schueler;
	}

	private static importV2Sprachdaten(planungsdaten: GostLaufbahnplanungExportV2Schueler, abiturdaten: Abiturdaten): void {
		for (const bel of planungsdaten.sprachbelegungen) {
			const mappedBel: Sprachbelegung = new Sprachbelegung();
			mappedBel.sprache = bel.sprache;
			mappedBel.istNachweis = bel.istNachweis;
			mappedBel.reihenfolge = bel.reihenfolge;
			mappedBel.belegungVonJahrgang = bel.belegungVonJahrgang;
			mappedBel.belegungVonAbschnitt = bel.belegungVonAbschnitt;
			mappedBel.belegungBisJahrgang = bel.belegungBisJahrgang;
			mappedBel.belegungBisAbschnitt = bel.belegungBisAbschnitt;
			mappedBel.referenzniveau = bel.referenzniveau;
			mappedBel.hatKleinesLatinum = bel.hatKleinesLatinum;
			mappedBel.hatLatinum = bel.hatLatinum;
			mappedBel.hatGraecum = bel.hatGraecum;
			mappedBel.hatHebraicum = bel.hatHebraicum;
			abiturdaten.sprachendaten.belegungen.add(mappedBel);
		}
		for (const pruef of planungsdaten.sprachpruefungen) {
			const mappedPruef: Sprachpruefung = new Sprachpruefung();
			mappedPruef.sprache = pruef.sprache;
			mappedPruef.jahrgang = pruef.jahrgang;
			mappedPruef.anspruchsniveauId = pruef.anspruchsniveauId;
			mappedPruef.pruefungsdatum = pruef.pruefungsdatum;
			mappedPruef.ersetzteSprache = pruef.ersetzteSprache;
			mappedPruef.istHSUPruefung = pruef.istHSUPruefung;
			mappedPruef.istFeststellungspruefung = pruef.istFeststellungspruefung;
			mappedPruef.kannErstePflichtfremdspracheErsetzen = pruef.kannErstePflichtfremdspracheErsetzen;
			mappedPruef.kannZweitePflichtfremdspracheErsetzen = pruef.kannZweitePflichtfremdspracheErsetzen;
			mappedPruef.kannWahlpflichtfremdspracheErsetzen = pruef.kannWahlpflichtfremdspracheErsetzen;
			mappedPruef.kannBelegungAlsFortgefuehrteSpracheErlauben = pruef.kannBelegungAlsFortgefuehrteSpracheErlauben;
			mappedPruef.referenzniveau = pruef.referenzniveau;
			mappedPruef.note = pruef.note;
			mappedPruef.zeugnisbezeichnung = pruef.zeugnisbezeichnung;
			abiturdaten.sprachendaten.pruefungen.add(mappedPruef);
		}
		abiturdaten.bilingualeSprache = planungsdaten.bilingualeSprache;
	}

	private static importV2Belegungen(planungsdaten: GostLaufbahnplanungExportV2Schueler, abiturdaten: Abiturdaten, faecherManager: GostFaecherManager): void {
		for (const hj of GostHalbjahr.values()) {
			abiturdaten.bewertetesHalbjahr[hj.id] = planungsdaten.bewertetesHalbjahr[hj.id];
		}
		for (let i: number = 0; i < planungsdaten.fachbelegungen.size(); i++) {
			const belegung: AbiturFachbelegung = new AbiturFachbelegung();
			const fb: GostLaufbahnplanungExportV2SchuelerFachbelegung = planungsdaten.fachbelegungen.get(i);
			const fach: GostFach | null = faecherManager.get(fb.fachID);
			if (fach === null) {
				continue;
			}
			belegung.fachID = fb.fachID;
			belegung.abiturFach = fb.abiturFach;
			belegung.idReferenzfach = fb.idReferenzfach;
			belegung.istFSNeu = fach.istFremdSpracheNeuEinsetzend;
			for (const hj of GostHalbjahr.values()) {
				const kursart: string | null = fb.kursart[hj.id];
				if (kursart === null) {
					continue;
				}
				const hjBelegung: AbiturFachbelegungHalbjahr = new AbiturFachbelegungHalbjahr();
				hjBelegung.halbjahrKuerzel = hj.kuerzel;
				hjBelegung.kursartKuerzel = kursart;
				hjBelegung.schriftlich = fb.schriftlich[hj.id];
				hjBelegung.biliSprache = fach.biliSprache;
				if (JavaObject.equalsTranspiler("PX", (fach.kuerzel))) {
					hjBelegung.wochenstunden = fach.wochenstundenQualifikationsphase;
				}
				if (JavaObject.equalsTranspiler("AT", (kursart))) {
					hjBelegung.notenkuerzel = "AT";
				}
				belegung.belegungen[hj.id] = hjBelegung;
				belegung.letzteKursart = kursart;
			}
			abiturdaten.fachbelegungen.add(belegung);
		}
	}

	private static importV2Abiturdaten(daten: GostLaufbahnplanungExportV2, planungsdaten: GostLaufbahnplanungExportV2Schueler, faecherManager: GostFaecherManager): Abiturdaten {
		const abiturdaten: Abiturdaten = new Abiturdaten();
		abiturdaten.abiturjahr = daten.abiturjahr;
		GostLaufbahnplanungDataHandler.importV2Sprachdaten(planungsdaten, abiturdaten);
		GostLaufbahnplanungDataHandler.importV2Belegungen(planungsdaten, abiturdaten, faecherManager);
		return abiturdaten;
	}

	private importV2GKL(planungsdaten: GostLaufbahnplanungExportV2Schueler, listMoeglich: List<GostLaufbahnplanungExportV2GKL>, faecherManager: GostFaecherManager): void {
		for (const fach of faecherManager.faecher()) {
			for (const halbjahr of GostHalbjahr.values()) {
				this.gklMoeglich.put(fach.id, halbjahr, new ArrayList<GostLaufbahnplanungGKLKlausurvorgabe>());
			}
		}
		for (const moeglich of listMoeglich) {
			const vorgabe: GostKlausurvorgabe = new GostKlausurvorgabe();
			vorgabe.id = moeglich.id;
			vorgabe.idFach = moeglich.idFach;
			vorgabe.halbjahr = moeglich.idHalbjahr;
			vorgabe.quartal = moeglich.quartal;
			const halbjahr: GostHalbjahr = GostHalbjahr.fromIDorException(vorgabe.halbjahr);
			const fach: GostFach | null = faecherManager.get(vorgabe.idFach);
			if (fach === null) {
				continue;
			}
			const eintrag: GostLaufbahnplanungGKLKlausurvorgabe = new GostLaufbahnplanungGKLKlausurvorgabe(fach, halbjahr, vorgabe);
			this.mapKlausurvorgaben.put(vorgabe.id, eintrag);
			this.gklMoeglich.getOrException(vorgabe.idFach, halbjahr).add(eintrag);
		}
		this.gklWahlen.idKlausurvorgabeEF_Sprachen = planungsdaten.gkl[0];
		this.gklWahlen.idKlausurvorgabeEF_GW = planungsdaten.gkl[1];
		this.gklWahlen.idKlausurvorgabeEF_NW = planungsdaten.gkl[2];
		this.gklWahlen.idKlausurvorgabeQ_Sprachen = planungsdaten.gkl[3];
		this.gklWahlen.idKlausurvorgabeQ_GW = planungsdaten.gkl[4];
		this.gklWahlen.idKlausurvorgabeQ_NW = planungsdaten.gkl[5];
	}

	/**
	 * Import die Laufbahnplanungsdaten in der Version 2 des Datenformates
	 *
	 * @param daten   die Laufbahnplanungsdaten in der Version 2
	 *
	 * @return die geladenen Laufbahnplanungsdaten
	 */
	public static importV2(daten: GostLaufbahnplanungExportV2): GostLaufbahnplanungDataHandler {
		return new GostLaufbahnplanungDataHandler(daten);
	}

	private static exportV1Beratungslehrer(listBeratungslehrer: List<GostBeratungslehrer>): List<GostLaufbahnplanungExportV1Beratungslehrer> {
		const result: List<GostLaufbahnplanungExportV1Beratungslehrer> = new ArrayList<GostLaufbahnplanungExportV1Beratungslehrer>();
		for (const beratungslehrer of listBeratungslehrer) {
			const l: GostLaufbahnplanungExportV1Beratungslehrer = new GostLaufbahnplanungExportV1Beratungslehrer();
			l.id = beratungslehrer.id;
			l.kuerzel = beratungslehrer.kuerzel;
			l.nachname = beratungslehrer.nachname;
			l.vorname = beratungslehrer.vorname;
			result.add(l);
		}
		return result;
	}

	private static exportV1GostFaecher(faecher: List<GostFach>): List<GostLaufbahnplanungExportV1Fach> {
		const result: List<GostLaufbahnplanungExportV1Fach> = new ArrayList<GostLaufbahnplanungExportV1Fach>();
		for (const fach of faecher) {
			const f: GostLaufbahnplanungExportV1Fach = new GostLaufbahnplanungExportV1Fach();
			f.id = fach.id;
			f.kuerzel = fach.kuerzel;
			f.kuerzelAnzeige = fach.kuerzelAnzeige;
			f.bezeichnung = fach.bezeichnung;
			f.sortierung = fach.sortierung;
			f.istPruefungsordnungsRelevant = fach.istPruefungsordnungsRelevant;
			f.istFremdsprache = fach.istFremdsprache;
			f.istFremdSpracheNeuEinsetzend = fach.istFremdSpracheNeuEinsetzend;
			f.biliSprache = fach.biliSprache;
			f.istMoeglichAbiLK = fach.istMoeglichAbiLK;
			f.istMoeglichAbiGK = fach.istMoeglichAbiGK;
			f.istMoeglichEF1 = fach.istMoeglichEF1;
			f.istMoeglichEF2 = fach.istMoeglichEF2;
			f.istMoeglichQ11 = fach.istMoeglichQ11;
			f.istMoeglichQ12 = fach.istMoeglichQ12;
			f.istMoeglichQ21 = fach.istMoeglichQ21;
			f.istMoeglichQ22 = fach.istMoeglichQ22;
			f.wochenstundenQualifikationsphase = fach.wochenstundenQualifikationsphase;
			f.projektKursLeitfach1ID = fach.projektKursLeitfach1ID;
			f.projektKursLeitfach1Kuerzel = fach.projektKursLeitfach1Kuerzel;
			f.projektKursLeitfach2ID = fach.projektKursLeitfach2ID;
			f.projektKursLeitfach2Kuerzel = fach.projektKursLeitfach2Kuerzel;
			result.add(f);
		}
		return result;
	}

	private static exportV1Fachkombinationen(listKombis: List<GostJahrgangFachkombination>): List<GostLaufbahnplanungExportV1Fachkombination> {
		const result: List<GostLaufbahnplanungExportV1Fachkombination> = new ArrayList<GostLaufbahnplanungExportV1Fachkombination>();
		for (const kombi of listKombis) {
			const k: GostLaufbahnplanungExportV1Fachkombination = new GostLaufbahnplanungExportV1Fachkombination();
			k.id = kombi.id;
			k.abiturjahr = kombi.abiturjahr;
			k.fachID1 = kombi.fachID1;
			k.kursart1 = kombi.kursart1;
			k.fachID2 = kombi.fachID2;
			k.kursart2 = kombi.kursart2;
			k.gueltigInHalbjahr[0] = kombi.gueltigInHalbjahr[0];
			k.gueltigInHalbjahr[1] = kombi.gueltigInHalbjahr[1];
			k.gueltigInHalbjahr[2] = kombi.gueltigInHalbjahr[2];
			k.gueltigInHalbjahr[3] = kombi.gueltigInHalbjahr[3];
			k.gueltigInHalbjahr[4] = kombi.gueltigInHalbjahr[4];
			k.gueltigInHalbjahr[5] = kombi.gueltigInHalbjahr[5];
			k.typ = kombi.typ;
			k.hinweistext = kombi.hinweistext;
			result.add(k);
		}
		return result;
	}

	private static exportV1Sprachdaten(planungsdaten: GostLaufbahnplanungExportV1Schueler, abiturdaten: Abiturdaten): void {
		for (const bel of abiturdaten.sprachendaten.belegungen) {
			const mappedBel: GostLaufbahnplanungExportV1Sprachbelegung = new GostLaufbahnplanungExportV1Sprachbelegung();
			mappedBel.sprache = bel.sprache;
			mappedBel.istNachweis = bel.istNachweis;
			mappedBel.reihenfolge = bel.reihenfolge;
			mappedBel.belegungVonJahrgang = bel.belegungVonJahrgang;
			mappedBel.belegungVonAbschnitt = bel.belegungVonAbschnitt;
			mappedBel.belegungBisJahrgang = bel.belegungBisJahrgang;
			mappedBel.belegungBisAbschnitt = bel.belegungBisAbschnitt;
			mappedBel.referenzniveau = bel.referenzniveau;
			mappedBel.hatKleinesLatinum = bel.hatKleinesLatinum;
			mappedBel.hatLatinum = bel.hatLatinum;
			mappedBel.hatGraecum = bel.hatGraecum;
			mappedBel.hatHebraicum = bel.hatHebraicum;
			planungsdaten.sprachendaten.belegungen.add(mappedBel);
		}
		for (const pruef of abiturdaten.sprachendaten.pruefungen) {
			const mappedPruef: GostLaufbahnplanungExportV1Sprachpruefung = new GostLaufbahnplanungExportV1Sprachpruefung();
			mappedPruef.sprache = pruef.sprache;
			mappedPruef.jahrgang = pruef.jahrgang;
			mappedPruef.anspruchsniveauId = pruef.anspruchsniveauId;
			mappedPruef.pruefungsdatum = pruef.pruefungsdatum;
			mappedPruef.ersetzteSprache = pruef.ersetzteSprache;
			mappedPruef.istHSUPruefung = pruef.istHSUPruefung;
			mappedPruef.istFeststellungspruefung = pruef.istFeststellungspruefung;
			mappedPruef.kannErstePflichtfremdspracheErsetzen = pruef.kannErstePflichtfremdspracheErsetzen;
			mappedPruef.kannZweitePflichtfremdspracheErsetzen = pruef.kannZweitePflichtfremdspracheErsetzen;
			mappedPruef.kannWahlpflichtfremdspracheErsetzen = pruef.kannWahlpflichtfremdspracheErsetzen;
			mappedPruef.kannBelegungAlsFortgefuehrteSpracheErlauben = pruef.kannBelegungAlsFortgefuehrteSpracheErlauben;
			mappedPruef.referenzniveau = pruef.referenzniveau;
			mappedPruef.note = pruef.note;
			mappedPruef.zeugnisbezeichnung = pruef.zeugnisbezeichnung;
			planungsdaten.sprachendaten.pruefungen.add(mappedPruef);
		}
		planungsdaten.bilingualeSprache = abiturdaten.bilingualeSprache;
	}

	/**
	 * Exportiert die Informationen zu der Laufbahnplanung des Schüler in das Export-Format in Version 1
	 *
	 * @param dateNow   das aktuelle Datum in der Form DD.MM.YYYY
	 *
	 * @return der Laufbahnplanungs-Export in Version 1
	 */
	public exportV1(dateNow: string): GostLaufbahnplanungExportV1 {
		const daten: GostLaufbahnplanungExportV1 = new GostLaufbahnplanungExportV1();
		daten.schulNr = this.schuleStammdaten.schulNr;
		daten.schulBezeichnung1 = this.schuleStammdaten.bezeichnung1;
		daten.schulBezeichnung2 = (this.schuleStammdaten.bezeichnung2 === null) ? "" : this.schuleStammdaten.bezeichnung2;
		daten.schulBezeichnung3 = (this.schuleStammdaten.bezeichnung3 === null) ? "" : this.schuleStammdaten.bezeichnung3;
		daten.anmerkungen = "Letzte Änderung am " + dateNow;
		daten.abiturjahr = this.abiturdaten.abiturjahr;
		daten.jahrgang = this.gostJahrgang.jahrgang;
		daten.hatZusatzkursGE = this.gostJahrgangsdaten.hatZusatzkursGE;
		daten.beginnZusatzkursGE = this.gostJahrgangsdaten.beginnZusatzkursGE;
		daten.hatZusatzkursSW = this.gostJahrgangsdaten.hatZusatzkursSW;
		daten.beginnZusatzkursSW = this.gostJahrgangsdaten.beginnZusatzkursSW;
		daten.beratungslehrer.addAll(GostLaufbahnplanungDataHandler.exportV1Beratungslehrer(this.gostJahrgangsdaten.beratungslehrer));
		daten.textBeratungsbogen = this.gostJahrgangsdaten.textBeratungsbogen;
		daten.fachkombinationen.addAll(GostLaufbahnplanungDataHandler.exportV1Fachkombinationen(this.faecherManager.getFachkombinationen()));
		daten.faecher.addAll(GostLaufbahnplanungDataHandler.exportV1GostFaecher(this.faecherManager.faecher()));
		const s: GostLaufbahnplanungExportV1Schueler | null = new GostLaufbahnplanungExportV1Schueler();
		s.id = this.schueler.id;
		s.idEnc = this.idSchuelerEncrpyted;
		s.vorname = this.schueler.vorname;
		s.nachname = this.schueler.nachname;
		s.geschlecht = this.schueler.geschlecht;
		s.bilingualeSprache = this.abiturdaten.bilingualeSprache;
		GostLaufbahnplanungDataHandler.exportV1Sprachdaten(s, this.abiturdaten);
		for (const hj of GostHalbjahr.values()) {
			s.bewertetesHalbjahr[hj.id] = this.abiturdaten.bewertetesHalbjahr[hj.id];
		}
		for (let i: number = 0; i < this.abiturdaten.fachbelegungen.size(); i++) {
			const belegung: AbiturFachbelegung | null = this.abiturdaten.fachbelegungen.get(i);
			const fb: GostLaufbahnplanungExportV1Fachbelegung = new GostLaufbahnplanungExportV1Fachbelegung();
			fb.fachID = belegung.fachID;
			fb.abiturFach = belegung.abiturFach;
			for (const hj of GostHalbjahr.values()) {
				const hjBelegung: AbiturFachbelegungHalbjahr | null = belegung.belegungen[hj.id];
				if (hjBelegung === null) {
					continue;
				}
				fb.kursart[hj.id] = hjBelegung.kursartKuerzel;
				fb.schriftlich[hj.id] = hjBelegung.schriftlich;
			}
			s.fachbelegungen.add(fb);
		}
		daten.schueler.add(s);
		return daten;
	}

	private static exportV2Beratungslehrer(listBeratungslehrer: List<GostBeratungslehrer>): List<GostLaufbahnplanungExportV2Beratungslehrer> {
		const result: List<GostLaufbahnplanungExportV2Beratungslehrer> = new ArrayList<GostLaufbahnplanungExportV2Beratungslehrer>();
		for (const beratungslehrer of listBeratungslehrer) {
			const l: GostLaufbahnplanungExportV2Beratungslehrer = new GostLaufbahnplanungExportV2Beratungslehrer();
			l.id = beratungslehrer.id;
			l.kuerzel = beratungslehrer.kuerzel;
			l.nachname = beratungslehrer.nachname;
			l.vorname = beratungslehrer.vorname;
			result.add(l);
		}
		return result;
	}

	private static exportV2GostFaecher(faecher: List<GostFach>): List<GostLaufbahnplanungExportV2Fach> {
		const result: List<GostLaufbahnplanungExportV2Fach> = new ArrayList<GostLaufbahnplanungExportV2Fach>();
		for (const fach of faecher) {
			const f: GostLaufbahnplanungExportV2Fach = new GostLaufbahnplanungExportV2Fach();
			f.id = fach.id;
			f.kuerzel = fach.kuerzel;
			f.kuerzelAnzeige = fach.kuerzelAnzeige;
			f.bezeichnung = fach.bezeichnung;
			f.sortierung = fach.sortierung;
			f.istPruefungsordnungsRelevant = fach.istPruefungsordnungsRelevant;
			f.istFremdsprache = fach.istFremdsprache;
			f.istFremdSpracheNeuEinsetzend = fach.istFremdSpracheNeuEinsetzend;
			f.biliSprache = fach.biliSprache;
			f.istMoeglichAbiLK = fach.istMoeglichAbiLK;
			f.istMoeglichAbiGK = fach.istMoeglichAbiGK;
			f.istMoeglich[0] = fach.istMoeglichEF1;
			f.istMoeglich[1] = fach.istMoeglichEF2;
			f.istMoeglich[2] = fach.istMoeglichQ11;
			f.istMoeglich[3] = fach.istMoeglichQ12;
			f.istMoeglich[4] = fach.istMoeglichQ21;
			f.istMoeglich[5] = fach.istMoeglichQ22;
			f.wochenstundenQualifikationsphase = fach.wochenstundenQualifikationsphase;
			f.referenzfach1ID = fach.projektKursLeitfach1ID;
			f.referenzfach2ID = fach.projektKursLeitfach2ID;
			result.add(f);
		}
		return result;
	}

	private static exportV2Fachkombinationen(listKombis: List<GostJahrgangFachkombination>): List<GostLaufbahnplanungExportV2Fachkombination> {
		const result: List<GostLaufbahnplanungExportV2Fachkombination> = new ArrayList<GostLaufbahnplanungExportV2Fachkombination>();
		for (const kombi of listKombis) {
			const k: GostLaufbahnplanungExportV2Fachkombination = new GostLaufbahnplanungExportV2Fachkombination();
			k.id = kombi.id;
			k.abiturjahr = kombi.abiturjahr;
			k.fachID1 = kombi.fachID1;
			k.kursart1 = kombi.kursart1;
			k.fachID2 = kombi.fachID2;
			k.kursart2 = kombi.kursart2;
			k.gueltigInHalbjahr[0] = kombi.gueltigInHalbjahr[0];
			k.gueltigInHalbjahr[1] = kombi.gueltigInHalbjahr[1];
			k.gueltigInHalbjahr[2] = kombi.gueltigInHalbjahr[2];
			k.gueltigInHalbjahr[3] = kombi.gueltigInHalbjahr[3];
			k.gueltigInHalbjahr[4] = kombi.gueltigInHalbjahr[4];
			k.gueltigInHalbjahr[5] = kombi.gueltigInHalbjahr[5];
			k.typ = kombi.typ;
			k.hinweistext = kombi.hinweistext;
			result.add(k);
		}
		return result;
	}

	private static exportV2Sprachdaten(planungsdaten: GostLaufbahnplanungExportV2Schueler, abiturdaten: Abiturdaten): void {
		for (const bel of abiturdaten.sprachendaten.belegungen) {
			const mappedBel: GostLaufbahnplanungExportV2SchuelerSprachbelegung = new GostLaufbahnplanungExportV2SchuelerSprachbelegung();
			mappedBel.sprache = bel.sprache;
			mappedBel.istNachweis = bel.istNachweis;
			mappedBel.reihenfolge = bel.reihenfolge;
			mappedBel.belegungVonJahrgang = bel.belegungVonJahrgang;
			mappedBel.belegungVonAbschnitt = bel.belegungVonAbschnitt;
			mappedBel.belegungBisJahrgang = bel.belegungBisJahrgang;
			mappedBel.belegungBisAbschnitt = bel.belegungBisAbschnitt;
			mappedBel.referenzniveau = bel.referenzniveau;
			mappedBel.hatKleinesLatinum = bel.hatKleinesLatinum;
			mappedBel.hatLatinum = bel.hatLatinum;
			mappedBel.hatGraecum = bel.hatGraecum;
			mappedBel.hatHebraicum = bel.hatHebraicum;
			planungsdaten.sprachbelegungen.add(mappedBel);
		}
		for (const pruef of abiturdaten.sprachendaten.pruefungen) {
			const mappedPruef: GostLaufbahnplanungExportV2SchuelerSprachpruefung = new GostLaufbahnplanungExportV2SchuelerSprachpruefung();
			mappedPruef.sprache = pruef.sprache;
			mappedPruef.jahrgang = pruef.jahrgang;
			mappedPruef.anspruchsniveauId = pruef.anspruchsniveauId;
			mappedPruef.pruefungsdatum = pruef.pruefungsdatum;
			mappedPruef.ersetzteSprache = pruef.ersetzteSprache;
			mappedPruef.istHSUPruefung = pruef.istHSUPruefung;
			mappedPruef.istFeststellungspruefung = pruef.istFeststellungspruefung;
			mappedPruef.kannErstePflichtfremdspracheErsetzen = pruef.kannErstePflichtfremdspracheErsetzen;
			mappedPruef.kannZweitePflichtfremdspracheErsetzen = pruef.kannZweitePflichtfremdspracheErsetzen;
			mappedPruef.kannWahlpflichtfremdspracheErsetzen = pruef.kannWahlpflichtfremdspracheErsetzen;
			mappedPruef.kannBelegungAlsFortgefuehrteSpracheErlauben = pruef.kannBelegungAlsFortgefuehrteSpracheErlauben;
			mappedPruef.referenzniveau = pruef.referenzniveau;
			mappedPruef.note = pruef.note;
			mappedPruef.zeugnisbezeichnung = pruef.zeugnisbezeichnung;
			planungsdaten.sprachpruefungen.add(mappedPruef);
		}
		planungsdaten.bilingualeSprache = abiturdaten.bilingualeSprache;
	}

	private exportV2GKL(daten: GostLaufbahnplanungExportV2, schueler: GostLaufbahnplanungExportV2Schueler): void {
		for (const eintrag of this.mapKlausurvorgaben.values()) {
			const gkl: GostLaufbahnplanungExportV2GKL = new GostLaufbahnplanungExportV2GKL();
			const vorgabe: GostKlausurvorgabe = eintrag.getVorgabe();
			gkl.id = vorgabe.id;
			gkl.idFach = vorgabe.idFach;
			gkl.idHalbjahr = vorgabe.halbjahr;
			gkl.quartal = vorgabe.halbjahr;
			daten.gkl.add(gkl);
		}
		schueler.gkl[0] = this.gklWahlen.idKlausurvorgabeEF_Sprachen;
		schueler.gkl[1] = this.gklWahlen.idKlausurvorgabeEF_GW;
		schueler.gkl[2] = this.gklWahlen.idKlausurvorgabeEF_NW;
		schueler.gkl[3] = this.gklWahlen.idKlausurvorgabeQ_Sprachen;
		schueler.gkl[4] = this.gklWahlen.idKlausurvorgabeQ_GW;
		schueler.gkl[5] = this.gklWahlen.idKlausurvorgabeQ_NW;
	}

	/**
	 * Exportiert die Informationen zu der Laufbahnplanung des Schüler in das Export-Format in Version 2
	 *
	 * @param dateNow   das aktuelle Datum in der Form DD.MM.YYYY
	 *
	 * @return der Laufbahnplanungs-Export in Version 2
	 */
	public exportV2(dateNow: string): GostLaufbahnplanungExportV2 {
		const daten: GostLaufbahnplanungExportV2 = new GostLaufbahnplanungExportV2();
		daten.schulNr = this.schuleStammdaten.schulNr;
		daten.schulBezeichnung1 = this.schuleStammdaten.bezeichnung1;
		daten.schulBezeichnung2 = (this.schuleStammdaten.bezeichnung2 === null) ? "" : this.schuleStammdaten.bezeichnung2;
		daten.schulBezeichnung3 = (this.schuleStammdaten.bezeichnung3 === null) ? "" : this.schuleStammdaten.bezeichnung3;
		daten.anmerkungen = "Letzte Änderung am " + dateNow;
		daten.abiturjahr = this.abiturdaten.abiturjahr;
		daten.jahrgang = this.gostJahrgang.jahrgang;
		daten.hatZusatzkursGE = this.gostJahrgangsdaten.hatZusatzkursGE;
		daten.beginnZusatzkursGE = this.gostJahrgangsdaten.beginnZusatzkursGE;
		daten.hatZusatzkursSW = this.gostJahrgangsdaten.hatZusatzkursSW;
		daten.beginnZusatzkursSW = this.gostJahrgangsdaten.beginnZusatzkursSW;
		daten.beratungslehrer.addAll(GostLaufbahnplanungDataHandler.exportV2Beratungslehrer(this.gostJahrgangsdaten.beratungslehrer));
		daten.textBeratungsbogen = this.gostJahrgangsdaten.textBeratungsbogen;
		daten.fachkombinationen.addAll(GostLaufbahnplanungDataHandler.exportV2Fachkombinationen(this.faecherManager.getFachkombinationen()));
		daten.faecher.addAll(GostLaufbahnplanungDataHandler.exportV2GostFaecher(this.faecherManager.faecher()));
		const s: GostLaufbahnplanungExportV2Schueler | null = new GostLaufbahnplanungExportV2Schueler();
		s.id = this.schueler.id;
		s.idEnc = this.idSchuelerEncrpyted;
		s.vorname = this.schueler.vorname;
		s.nachname = this.schueler.nachname;
		s.geschlecht = this.schueler.geschlecht;
		GostLaufbahnplanungDataHandler.exportV2Sprachdaten(s, this.abiturdaten);
		for (const hj of GostHalbjahr.values()) {
			s.bewertetesHalbjahr[hj.id] = this.abiturdaten.bewertetesHalbjahr[hj.id];
		}
		s.istG8 = (Schulgliederung.data().getWertByIDOrNull(this.schueler.idSchulgliederung) as unknown === Schulgliederung.GY8 as unknown);
		for (let i: number = 0; i < this.abiturdaten.fachbelegungen.size(); i++) {
			const belegung: AbiturFachbelegung | null = this.abiturdaten.fachbelegungen.get(i);
			const fb: GostLaufbahnplanungExportV2SchuelerFachbelegung | null = new GostLaufbahnplanungExportV2SchuelerFachbelegung();
			fb.fachID = belegung.fachID;
			fb.abiturFach = belegung.abiturFach;
			fb.idReferenzfach = belegung.idReferenzfach;
			for (const hj of GostHalbjahr.values()) {
				const hjBelegung: AbiturFachbelegungHalbjahr | null = belegung.belegungen[hj.id];
				if ((hjBelegung === null) || (JavaObject.equalsTranspiler("", (hjBelegung.kursartKuerzel)))) {
					continue;
				}
				fb.kursart[hj.id] = hjBelegung.kursartKuerzel;
				fb.schriftlich[hj.id] = hjBelegung.schriftlich;
			}
			s.fachbelegungen.add(fb);
		}
		daten.schueler.add(s);
		this.exportV2GKL(daten, s);
		return daten;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.gost.GostLaufbahnplanungDataHandler';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.gost.GostLaufbahnplanungDataHandler'].includes(name);
	}

	public static readonly class = new Class<GostLaufbahnplanungDataHandler>('de.svws_nrw.core.utils.gost.GostLaufbahnplanungDataHandler');

}

export function cast_de_svws_nrw_core_utils_gost_GostLaufbahnplanungDataHandler(obj: unknown): GostLaufbahnplanungDataHandler {
	return obj as GostLaufbahnplanungDataHandler;
}
