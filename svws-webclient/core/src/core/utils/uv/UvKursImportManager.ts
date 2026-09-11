import { JavaObject } from '../../../java/lang/JavaObject';
import { UvSchuelergruppe } from '../../../core/data/uv/UvSchuelergruppe';
import { UvLehrer } from '../../../core/data/uv/UvLehrer';
import type { JavaSet } from '../../../java/util/JavaSet';
import { StringBuilder } from '../../../java/lang/StringBuilder';
import { HashMap } from '../../../java/util/HashMap';
import { UvPlanungsabschnittSchueler } from '../../../core/data/uv/UvPlanungsabschnittSchueler';
import { ArrayList } from '../../../java/util/ArrayList';
import { GostBlockungsergebnisKurs } from '../../../core/data/gost/GostBlockungsergebnisKurs';
import { JahrgangsDaten } from '../../../core/data/jahrgang/JahrgangsDaten';
import { JavaString } from '../../../java/lang/JavaString';
import { JavaMath } from '../../../java/lang/JavaMath';
import { UvKursImportDaten } from '../../../core/data/uv/UvKursImportDaten';
import { GostKursart } from '../../../core/types/gost/GostKursart';
import type { Comparator } from '../../../java/util/Comparator';
import { KursDaten } from '../../../asd/data/kurse/KursDaten';
import { UvLerngruppeCreateRequest } from '../../../core/data/uv/UvLerngruppeCreateRequest';
import type { List } from '../../../java/util/List';
import { cast_java_util_List } from '../../../java/util/List';
import { UvSchuelergruppeCreateRequest } from '../../../core/data/uv/UvSchuelergruppeCreateRequest';
import { HashSet } from '../../../java/util/HashSet';
import { GostBlockungKurs } from '../../../core/data/gost/GostBlockungKurs';
import { UvPlanungsabschnitt, cast_de_svws_nrw_core_data_uv_UvPlanungsabschnitt } from '../../../core/data/uv/UvPlanungsabschnitt';
import { UvKursImportQuellkurs } from '../../../core/utils/uv/UvKursImportQuellkurs';
import { UvManager, cast_de_svws_nrw_core_utils_uv_UvManager } from '../../../core/utils/uv/UvManager';
import { UvSchiene } from '../../../core/data/uv/UvSchiene';
import { UvKursCreateRequest } from '../../../core/data/uv/UvKursCreateRequest';
import { UvFach } from '../../../core/data/uv/UvFach';
import { UvLerngruppenLehrerCreateRequest } from '../../../core/data/uv/UvLerngruppenLehrerCreateRequest';
import { UvLerngruppenSchieneCreateRequest } from '../../../core/data/uv/UvLerngruppenSchieneCreateRequest';
import { JavaInteger } from '../../../java/lang/JavaInteger';
import { GostBlockungsergebnis, cast_de_svws_nrw_core_data_gost_GostBlockungsergebnis } from '../../../core/data/gost/GostBlockungsergebnis';
import { GostBlockungsdaten, cast_de_svws_nrw_core_data_gost_GostBlockungsdaten } from '../../../core/data/gost/GostBlockungsdaten';
import { Schueler } from '../../../asd/data/schueler/Schueler';
import { UvSchieneCreateRequest } from '../../../core/data/uv/UvSchieneCreateRequest';
import { JavaLong } from '../../../java/lang/JavaLong';
import { UvSchuelergruppeSchuelerCreateRequest } from '../../../core/data/uv/UvSchuelergruppeSchuelerCreateRequest';
import { Class } from '../../../java/lang/Class';
import type { JavaMap } from '../../../java/util/JavaMap';

export class UvKursImportManager extends JavaObject {

	private readonly compJahrgangsdaten: Comparator<JahrgangsDaten> = { compare: (a: JahrgangsDaten, b: JahrgangsDaten) => {
		const cmp: number = JavaInteger.compare(a.sortierung, b.sortierung);
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };

	private readonly planungsabschnitt: UvPlanungsabschnitt;

	private readonly uvManager: UvManager;

	private readonly idSchuljahresabschnitt: number;

	private readonly kurse: List<UvKursImportQuellkurs> = new ArrayList<UvKursImportQuellkurs>();

	private readonly schienenBezeichnungById: JavaMap<number, string> = new HashMap<number, string>();

	private readonly fehlendeFaecher: JavaSet<string> = new HashSet<string>();

	private readonly fehlendeLehrer: JavaSet<string> = new HashSet<string>();

	private readonly fehlendeSchueler: JavaSet<string> = new HashSet<string>();

	private nextTemporaereId: number = 0;

	private nextSchienenNummer: number = 0;


	/**
	 * Erstellt einen Importmanager für eine GOSt-Blockung.
	 *
	 * @param blockung der Quellblockung
	 * @param ergebnis das zu importierende Blockungsergebnis
	 * @param planungsabschnitt der Zielplanungsabschnitt
	 * @param uvManager die Daten des Zielplanungsabschnitts
	 * @param idSchuljahresabschnitt die ID des Schuljahresabschnitts der anzulegenden Kurse
	 */
	public constructor(blockung: GostBlockungsdaten, ergebnis: GostBlockungsergebnis, planungsabschnitt: UvPlanungsabschnitt, uvManager: UvManager, idSchuljahresabschnitt: number);

	/**
	 * Erstellt einen Importmanager für persistierte Kurse eines Schuljahresabschnitts.
	 *
	 * @param kurse die Quellkurse
	 * @param planungsabschnitt der Zielplanungsabschnitt
	 * @param uvManager die Daten des Zielplanungsabschnitts
	 */
	public constructor(kurse: List<KursDaten>, planungsabschnitt: UvPlanungsabschnitt, uvManager: UvManager);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0: GostBlockungsdaten | List<KursDaten>, __param1: GostBlockungsergebnis | UvPlanungsabschnitt, __param2: UvManager | UvPlanungsabschnitt, __param3?: UvManager, __param4?: number) {
		super();
		if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.core.data.gost.GostBlockungsdaten')))) && ((__param1 !== undefined) && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('de.svws_nrw.core.data.gost.GostBlockungsergebnis')))) && ((__param2 !== undefined) && ((__param2 instanceof JavaObject) && (__param2.isTranspiledInstanceOf('de.svws_nrw.core.data.uv.UvPlanungsabschnitt')))) && ((__param3 !== undefined) && ((__param3 instanceof JavaObject) && (__param3.isTranspiledInstanceOf('de.svws_nrw.core.utils.uv.UvManager')))) && ((__param4 !== undefined) && typeof __param4 === "number")) {
			const blockung: GostBlockungsdaten = cast_de_svws_nrw_core_data_gost_GostBlockungsdaten(__param0);
			const ergebnis: GostBlockungsergebnis = cast_de_svws_nrw_core_data_gost_GostBlockungsergebnis(__param1);
			const planungsabschnitt: UvPlanungsabschnitt = cast_de_svws_nrw_core_data_uv_UvPlanungsabschnitt(__param2);
			const uvManager: UvManager = cast_de_svws_nrw_core_utils_uv_UvManager(__param3);
			const idSchuljahresabschnitt: number = __param4 as number;
			this.planungsabschnitt = planungsabschnitt;
			this.uvManager = uvManager;
			this.idSchuljahresabschnitt = idSchuljahresabschnitt;
			for (const schiene of blockung.schienen) {
				this.schienenBezeichnungById.put(schiene.id, schiene.bezeichnung);
			}
			const ergebnisKurse: JavaMap<number, GostBlockungsergebnisKurs> = new HashMap<number, GostBlockungsergebnisKurs>();
			for (const schiene of ergebnis.schienen) {
				for (const kurs of schiene.kurse) {
					ergebnisKurse.put(kurs.id, kurs);
				}
			}
			for (const kurs of blockung.kurse) {
				const ergebnisKurs: GostBlockungsergebnisKurs | null = ergebnisKurse.get(kurs.id);
				if (ergebnisKurs !== null) {
					this.addGostKurs(kurs, ergebnisKurs);
				}
			}
			this.analyse();
		} else if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('java.util.List'))) || (__param0 === null)) && ((__param1 !== undefined) && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('de.svws_nrw.core.data.uv.UvPlanungsabschnitt')))) && ((__param2 !== undefined) && ((__param2 instanceof JavaObject) && (__param2.isTranspiledInstanceOf('de.svws_nrw.core.utils.uv.UvManager')))) && (__param3 === undefined) && (__param4 === undefined)) {
			const kurse: List<KursDaten> = cast_java_util_List(__param0);
			const planungsabschnitt: UvPlanungsabschnitt = cast_de_svws_nrw_core_data_uv_UvPlanungsabschnitt(__param1);
			const uvManager: UvManager = cast_de_svws_nrw_core_utils_uv_UvManager(__param2);
			this.planungsabschnitt = planungsabschnitt;
			this.uvManager = uvManager;
			this.idSchuljahresabschnitt = kurse.isEmpty() ? -1 : kurse.getFirst().idSchuljahresabschnitt;
			for (const kurs of kurse) {
				const quellkurs: UvKursImportQuellkurs = new UvKursImportQuellkurs(kurs.id, kurs.idFach, kurs.kursartAllg, UvKursImportManager.getKursnummer(kurs.kuerzel), kurs.wochenstunden);
				for (const schueler of kurs.schueler) {
					quellkurs.schuelerIds.add(schueler.id);
				}
				if (kurs.lehrer !== null) {
					quellkurs.lehrerIds.add(kurs.lehrer);
				}
				for (const lehrer of kurs.weitereLehrer) {
					quellkurs.lehrerIds.add(lehrer.idLehrer);
				}
				for (const schiene of kurs.schienen) {
					quellkurs.schienenIds.add(schiene);
					this.schienenBezeichnungById.put(schiene, "Schiene " + schiene);
				}
				this.kurse.add(quellkurs);
			}
			this.analyse();
		} else throw new Error('invalid method overload');
	}

	/**
	 *@return die fehlenden Fächer als lesbare Liste.
	 */
	public getFehlendeFaecher(): List<string> {
		return new ArrayList<string>(this.fehlendeFaecher);
	}

	/**
	 *@return die fehlenden Lehrer als lesbare Liste.
	 */
	public getFehlendeLehrer(): List<string> {
		return new ArrayList<string>(this.fehlendeLehrer);
	}

	/**
	 *@return die fehlenden Schüler als lesbare Liste.
	 */
	public getFehlendeSchueler(): List<string> {
		return new ArrayList<string>(this.fehlendeSchueler);
	}

	/**
	 * Erstellt die bestätigten Importdaten. Kurse ohne Ziel-Fach sowie nicht
	 * auflösbare Lehrer- und Schülerzuordnungen werden dabei ausgelassen.
	 * Schülergruppennamen enthalten die sortierten Jahrgänge der übernommenen Schüler.
	 * Bereits belegte Namen im Zielplanungsabschnitt erhalten einen freien Nummernsuffix ab -2.
	 *
	 * @return die anzulegenden UV-Daten
	 */
	public createImportDaten(): UvKursImportDaten {
		const result: UvKursImportDaten = new UvKursImportDaten();
		const schieneByBezeichnung: JavaMap<string, number> = new HashMap<string, number>();
		const gruppenbezeichnungen: JavaSet<string> = new HashSet<string>();
		for (const gruppe of this.uvManager.schuelergruppeGetMengeByPlanungsabschnitt(this.planungsabschnitt)) {
			gruppenbezeichnungen.add(gruppe.bezeichnung);
		}
		this.nextTemporaereId = -1;
		this.nextSchienenNummer = 1;
		for (const schiene of this.uvManager.schieneGetMengeByPlanungsabschnitt(this.planungsabschnitt)) {
			this.nextSchienenNummer = Math.max(this.nextSchienenNummer, schiene.nummer + 1);
			if (schiene.bezeichnung !== null) {
				schieneByBezeichnung.put(schiene.bezeichnung, schiene.id);
			}
		}
		for (const quellkurs of this.kurse) {
			const fach: UvFach | null = this.uvManager.fachGetByIdFachAndPlanungsabschnittOrNull(quellkurs.idFach, this.planungsabschnitt);
			if (fach === null) {
				continue;
			}
			this.addKurs(result, quellkurs, fach, schieneByBezeichnung, gruppenbezeichnungen);
		}
		return result;
	}

	private addKurs(daten: UvKursImportDaten, quellkurs: UvKursImportQuellkurs, fach: UvFach, schieneByBezeichnung: JavaMap<string, number>, gruppenbezeichnungen: JavaSet<string>): void {
		const schuelergruppe: UvSchuelergruppeCreateRequest = new UvSchuelergruppeCreateRequest();
		schuelergruppe.id = this.nextTemporaereId--;
		schuelergruppe.idPlanungsabschnitt = this.planungsabschnitt.id;
		daten.schuelergruppen.add(schuelergruppe);
		const kurs: UvKursCreateRequest = new UvKursCreateRequest();
		kurs.id = this.nextTemporaereId--;
		kurs.idPlanungsabschnitt = this.planungsabschnitt.id;
		kurs.idSchuljahresabschnitt = this.idSchuljahresabschnitt;
		kurs.idFach = fach.id;
		kurs.kursart = quellkurs.kursart;
		kurs.kursnummer = quellkurs.nummer;
		kurs.idSchuelergruppe = schuelergruppe.id;
		daten.kurse.add(kurs);
		const lerngruppe: UvLerngruppeCreateRequest = new UvLerngruppeCreateRequest();
		lerngruppe.id = this.nextTemporaereId--;
		lerngruppe.idPlanungsabschnitt = this.planungsabschnitt.id;
		lerngruppe.idKurs = kurs.id;
		lerngruppe.wochenstunden = quellkurs.wochenstunden;
		lerngruppe.wochenstundenUnterrichtet = quellkurs.wochenstunden;
		daten.lerngruppen.add(lerngruppe);
		this.addLehrer(daten, quellkurs, lerngruppe);
		this.addSchueler(daten, quellkurs, schuelergruppe);
		schuelergruppe.bezeichnung = this.createGruppenbezeichnung(quellkurs, schuelergruppe, gruppenbezeichnungen);
		this.addSchienen(daten, quellkurs, lerngruppe.id, schieneByBezeichnung);
	}

	private createGruppenbezeichnung(quellkurs: UvKursImportQuellkurs, schuelergruppe: UvSchuelergruppeCreateRequest, gruppenbezeichnungen: JavaSet<string>): string {
		const jahrgaenge: List<JahrgangsDaten> = new ArrayList<JahrgangsDaten>();
		for (const idJahrgang of schuelergruppe.idsJahrgaengeErlaubt) {
			jahrgaenge.add(this.uvManager.jahrgangsdatenGetById(idJahrgang));
		}
		jahrgaenge.sort(this.compJahrgangsdaten);
		const jahrgangsbezeichnung: StringBuilder = new StringBuilder();
		for (const jahrgang of jahrgaenge) {
			if (!jahrgangsbezeichnung.isEmpty()) {
				jahrgangsbezeichnung.append("/");
			}
			jahrgangsbezeichnung.append((jahrgang.kuerzel === null) ? ("JG " + jahrgang.id) : jahrgang.kuerzel);
		}
		const basis: string = (jahrgangsbezeichnung.isEmpty() ? "" : (jahrgangsbezeichnung.toString() + " ")) + this.getFachKuerzel(quellkurs.idFach) + "-" + quellkurs.kursart + quellkurs.nummer;
		let bezeichnung: string = basis;
		let nummer: number = 2;
		while (gruppenbezeichnungen.contains(bezeichnung)) {
			bezeichnung = basis + "-" + nummer++;
		}
		gruppenbezeichnungen.add(bezeichnung);
		return bezeichnung;
	}

	private addSchueler(daten: UvKursImportDaten, quellkurs: UvKursImportQuellkurs, schuelergruppe: UvSchuelergruppeCreateRequest): void {
		for (const idSchueler of quellkurs.schuelerIds) {
			const schueler: UvPlanungsabschnittSchueler | null = this.uvManager.planungsabschnittSchuelerGetByIdOrNull(this.planungsabschnitt.id, idSchueler);
			if (schueler === null) {
				continue;
			}
			if (!schuelergruppe.idsJahrgaengeErlaubt.contains(schueler.idJahrgang)) {
				schuelergruppe.idsJahrgaengeErlaubt.add(schueler.idJahrgang);
			}
			const zuordnung: UvSchuelergruppeSchuelerCreateRequest = new UvSchuelergruppeSchuelerCreateRequest();
			zuordnung.idPlanungsabschnitt = this.planungsabschnitt.id;
			zuordnung.idSchuelergruppe = schuelergruppe.id;
			zuordnung.idSchueler = idSchueler;
			daten.schuelergruppenschueler.add(zuordnung);
		}
	}

	private addSchienen(daten: UvKursImportDaten, quellkurs: UvKursImportQuellkurs, idLerngruppe: number, schieneByBezeichnung: JavaMap<string, number>): void {
		for (const idQuellschiene of quellkurs.schienenIds) {
			const bezeichnung: string | null = this.schienenBezeichnungById.get(idQuellschiene);
			if (bezeichnung === null) {
				continue;
			}
			let idSchiene: number | null = schieneByBezeichnung.get(bezeichnung);
			if (idSchiene === null) {
				const schiene: UvSchieneCreateRequest = new UvSchieneCreateRequest();
				idSchiene = this.nextTemporaereId--;
				schiene.id = idSchiene;
				schiene.idPlanungsabschnitt = this.planungsabschnitt.id;
				schiene.nummer = this.nextSchienenNummer++;
				schiene.bezeichnung = bezeichnung;
				daten.schienen.add(schiene);
				schieneByBezeichnung.put(bezeichnung, idSchiene);
			}
			const zuordnung: UvLerngruppenSchieneCreateRequest = new UvLerngruppenSchieneCreateRequest();
			zuordnung.idPlanungsabschnitt = this.planungsabschnitt.id;
			zuordnung.idLerngruppe = idLerngruppe;
			zuordnung.idSchiene = idSchiene;
			daten.lerngruppenschienen.add(zuordnung);
		}
	}

	private addGostKurs(kurs: GostBlockungKurs, ergebnisKurs: GostBlockungsergebnisKurs): void {
		const quellkurs: UvKursImportQuellkurs = new UvKursImportQuellkurs(kurs.id, kurs.fach_id, GostKursart.fromID(kurs.kursart).kuerzel, kurs.nummer, kurs.wochenstunden);
		for (const lehrer of kurs.lehrer) {
			quellkurs.lehrerIds.add(lehrer.id);
		}
		quellkurs.schuelerIds.addAll(ergebnisKurs.schueler);
		quellkurs.schienenIds.addAll(ergebnisKurs.schienen);
		this.kurse.add(quellkurs);
	}

	private analyse(): void {
		for (const kurs of this.kurse) {
			if (this.uvManager.fachGetByIdFachAndPlanungsabschnittOrNull(kurs.idFach, this.planungsabschnitt) === null) {
				this.fehlendeFaecher.add(this.getFachKuerzel(kurs.idFach));
			}
			for (const idLehrer of kurs.lehrerIds) {
				const uvLehrer: UvLehrer | null = this.uvManager.lehrerGetByKLehrerIdOrNull(idLehrer);
				if ((uvLehrer === null) || !this.uvManager.lehrerIsInPlanungsabschnitt(uvLehrer, this.planungsabschnitt)) {
					this.fehlendeLehrer.add((uvLehrer === null) ? ("Lehrer-ID " + idLehrer) : uvLehrer.kuerzel);
				}
			}
			for (const idSchueler of kurs.schuelerIds) {
				if (this.uvManager.planungsabschnittSchuelerGetByIdOrNull(this.planungsabschnitt.id, idSchueler) === null) {
					this.fehlendeSchueler.add("Schüler-ID " + idSchueler);
				}
			}
		}
	}

	private addLehrer(daten: UvKursImportDaten, quellkurs: UvKursImportQuellkurs, lerngruppe: UvLerngruppeCreateRequest): void {
		let reihenfolge: number = 1;
		for (const idKLehrer of quellkurs.lehrerIds) {
			const lehrer: UvLehrer | null = this.uvManager.lehrerGetByKLehrerIdOrNull(idKLehrer);
			if ((lehrer === null) || !this.uvManager.lehrerIsInPlanungsabschnitt(lehrer, this.planungsabschnitt)) {
				continue;
			}
			const zuordnung: UvLerngruppenLehrerCreateRequest = new UvLerngruppenLehrerCreateRequest();
			zuordnung.idPlanungsabschnitt = this.planungsabschnitt.id;
			zuordnung.idLerngruppe = lerngruppe.id;
			zuordnung.idLehrer = lehrer.id;
			zuordnung.reihenfolge = reihenfolge++;
			zuordnung.wochenstunden = quellkurs.wochenstunden;
			zuordnung.wochenstundenAngerechnet = quellkurs.wochenstunden;
			daten.lerngruppenlehrer.add(zuordnung);
		}
	}

	private getFachKuerzel(idFach: number): string {
		for (const fach of this.uvManager.fachdatenGetMenge()) {
			if (fach.id === idFach) {
				return fach.kuerzel;
			}
		}
		return "Fach-ID " + idFach;
	}

	private static getKursnummer(kuerzel: string): number {
		let index: number = kuerzel.length - 1;
		while ((index >= 0) && (JavaString.indexOf("0123456789", kuerzel.charAt(index)) >= 0)) {
			index--;
		}
		if (index === (kuerzel.length - 1)) {
			return 1;
		}
		return JavaInteger.parseInt(kuerzel.substring(index + 1));
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.uv.UvKursImportManager';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.uv.UvKursImportManager'].includes(name);
	}

	public static readonly class = new Class<UvKursImportManager>('de.svws_nrw.core.utils.uv.UvKursImportManager');

}

export function cast_de_svws_nrw_core_utils_uv_UvKursImportManager(obj: unknown): UvKursImportManager {
	return obj as UvKursImportManager;
}
