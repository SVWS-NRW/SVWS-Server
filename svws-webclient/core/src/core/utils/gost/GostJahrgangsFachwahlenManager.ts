import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap2D } from '../../../core/adt/map/HashMap2D';
import { GostAbiturFach } from '../../../core/types/gost/GostAbiturFach';
import { GostJahrgangFachwahlenHalbjahr } from '../../../core/data/gost/GostJahrgangFachwahlenHalbjahr';
import { GostJahrgangFachwahlen } from '../../../core/data/gost/GostJahrgangFachwahlen';
import { GostHalbjahr } from '../../../core/types/gost/GostHalbjahr';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { GostKursart } from '../../../core/types/gost/GostKursart';

export class GostJahrgangsFachwahlenManager extends JavaObject {

	/**
	 * (Fach-ID, Abifach [1=LK,3,4]) --> ArrayList<Schueler-ID> = Eine Liste der Schüler, welche das angegeben Fach als Abiturfach den entsprechenden Typs (siehe ID von {@link GostAbiturFach}) haben.
	 */
	private readonly _map2D_fachID_abifachID_schuelerID : HashMap2D<number, number, List<number>> = new HashMap2D();

	/**
	 * (Fach-ID, Halbjahres-ID [0..5]) --> ArrayList<Schueler-ID> = Eine Liste der Schüler, welche das angegeben Fach in dem Halbjahr als Leistungskurs gewählt haben.
	 */
	private readonly _map2D_lk_fachID_halbjahrID_schuelerID : HashMap2D<number, number, List<number>> = new HashMap2D();

	/**
	 * (Fach-ID, Halbjahres-ID [0..5]) --> ArrayList<Schueler-ID> = Eine Liste der Schüler, welche das angegeben Fach in dem Halbjahr als Grundkurs (oder PJK oder VTF) gewählt haben.
	 */
	private readonly _map2D_gk_fachID_halbjahrID_schuelerID : HashMap2D<number, number, List<number>> = new HashMap2D();

	/**
	 * (Fach-ID, Halbjahres-ID [0..5]) --> ArrayList<Schueler-ID> = Eine Liste der Schüler, welche das angegeben Fach in dem Halbjahr als schriftlichen Grundkurs gewählt haben.
	 */
	private readonly _map2D_gk_schriftlich_fachID_halbjahrID_schuelerID : HashMap2D<number, number, List<number>> = new HashMap2D();

	/**
	 * (Fach-ID, Halbjahres-ID [0..5]) --> ArrayList<Schueler-ID> = Eine Liste der Schüler, welche das angegeben Fach in dem Halbjahr als mündlichen Grundkurs (oder PJK oder VTF) gewählt haben.
	 */
	private readonly _map2D_gk_muendlich_fachID_halbjahrID_schuelerID : HashMap2D<number, number, List<number>> = new HashMap2D();

	/**
	 * (Fach-ID, Halbjahres-ID [0..5]) --> ArrayList<Schueler-ID> = Eine Liste der Schüler, welche das angegeben Fach in dem Halbjahr als Zusatzkurs gewählt haben.
	 */
	private readonly _map2D_zk_fachID_halbjahrID_schuelerID : HashMap2D<number, number, List<number>> = new HashMap2D();


	/**
	 * Erstellt einen neuen Manager mit den übergebenen Fachwahlen.
	 *
	 * @param jgFachwahlen   die Fachwahlen des Abiturjahrgangs
	 */
	public constructor(jgFachwahlen : GostJahrgangFachwahlen) {
		super();
		this.init(jgFachwahlen);
	}

	/**
	 * Initialisiert den Manager mit den übergebenen Fachwahlen
	 *
	 * @param jgFachwahlen   die Fachwahlen des Abiturjahrgangs
	 */
	private init(jgFachwahlen : GostJahrgangFachwahlen) : void {
		for (const halbjahr of GostHalbjahr.values()) {
			const fwHalbjahr : GostJahrgangFachwahlenHalbjahr | null = jgFachwahlen.halbjahr[halbjahr.id];
			if (fwHalbjahr !== null) {
				for (const fw of fwHalbjahr.fachwahlen) {
					const kursart : GostKursart | null = GostKursart.fromID(fw.kursartID);
					if (kursart as unknown === GostKursart.LK as unknown) {
						let schuelerListe : List<number> | null = this._map2D_lk_fachID_halbjahrID_schuelerID.getOrNull(fw.fachID, halbjahr.id);
						if (schuelerListe === null) {
							schuelerListe = new ArrayList();
							this._map2D_lk_fachID_halbjahrID_schuelerID.put(fw.fachID, halbjahr.id, schuelerListe);
						}
						schuelerListe.add(fw.schuelerID);
					} else
						if ((kursart as unknown === GostKursart.GK as unknown) || (kursart as unknown === GostKursart.PJK as unknown) || (kursart as unknown === GostKursart.VTF as unknown)) {
							let schuelerListe : List<number> | null = this._map2D_gk_fachID_halbjahrID_schuelerID.getOrNull(fw.fachID, halbjahr.id);
							if (schuelerListe === null) {
								schuelerListe = new ArrayList();
								this._map2D_gk_fachID_halbjahrID_schuelerID.put(fw.fachID, halbjahr.id, schuelerListe);
							}
							schuelerListe.add(fw.schuelerID);
							if (fw.istSchriftlich) {
								schuelerListe = this._map2D_gk_schriftlich_fachID_halbjahrID_schuelerID.getOrNull(fw.fachID, halbjahr.id);
								if (schuelerListe === null) {
									schuelerListe = new ArrayList();
									this._map2D_gk_schriftlich_fachID_halbjahrID_schuelerID.put(fw.fachID, halbjahr.id, schuelerListe);
								}
								schuelerListe.add(fw.schuelerID);
							} else {
								schuelerListe = this._map2D_gk_muendlich_fachID_halbjahrID_schuelerID.getOrNull(fw.fachID, halbjahr.id);
								if (schuelerListe === null) {
									schuelerListe = new ArrayList();
									this._map2D_gk_muendlich_fachID_halbjahrID_schuelerID.put(fw.fachID, halbjahr.id, schuelerListe);
								}
								schuelerListe.add(fw.schuelerID);
							}
						} else
							if (kursart as unknown === GostKursart.ZK as unknown) {
								let schuelerListe : List<number> | null = this._map2D_zk_fachID_halbjahrID_schuelerID.getOrNull(fw.fachID, halbjahr.id);
								if (schuelerListe === null) {
									schuelerListe = new ArrayList();
									this._map2D_zk_fachID_halbjahrID_schuelerID.put(fw.fachID, halbjahr.id, schuelerListe);
								}
								schuelerListe.add(fw.schuelerID);
							}
				}
			}
		}
		for (const fw of jgFachwahlen.abitur.fachwahlen) {
			const kursart : GostKursart | null = GostKursart.fromID(fw.kursartID);
			let abiFach : GostAbiturFach | null = GostAbiturFach.LK1;
			if (kursart as unknown === GostKursart.GK as unknown)
				abiFach = fw.istSchriftlich ? GostAbiturFach.AB3 : GostAbiturFach.AB4;
			let schuelerListe : List<number> | null = this._map2D_fachID_abifachID_schuelerID.getOrNull(fw.fachID, abiFach.id);
			if (schuelerListe === null) {
				schuelerListe = new ArrayList();
				this._map2D_fachID_abifachID_schuelerID.put(fw.fachID, abiFach.id, schuelerListe);
			}
			schuelerListe.add(fw.schuelerID);
		}
	}

	/**
	 * Gibt die Schüler-Menge für das angegebene Fach zurück, welche das Fach als das angebene Abiturfach gewählt haben.
	 * Bei der Anfrage bezüglich der Wahl als erstes oder zweites Fach werden immer alle LK-Fachwahlen zurückgegeben.
	 *
	 * @param idFach    die ID des Faches
	 * @param abifach   das Abiturfach (bei LKs werden jeweils alle Fachwahlen genommen)
	 *
	 * @return die Menge der Schüler, welche das Fach als Abiturfach des angegebenen Typs gewählt haben
	 */
	public schuelerGetMengeByFachAndAbifachAsListOrException(idFach : number, abifach : GostAbiturFach) : List<number> {
		let idAbifach : number = abifach.id;
		if (idAbifach === 2)
			idAbifach = 1;
		const schuelerListe : List<number> | null = this._map2D_fachID_abifachID_schuelerID.getOrNull(idFach, idAbifach);
		if (schuelerListe !== null)
			return schuelerListe;
		return new ArrayList();
	}

	/**
	 * Gibt die Schüler-Menge für das angegebene Fach in dem angegeben Halbjahr zurück, welche das Fach
	 * als Leistungskurs gewählt haben.
	 *
	 * @param idFach     die ID des Faches
	 * @param halbjahr   das Halbjahr der gymnasialen Oberstufe
	 *
	 * @return die Menge der Schüler, welche das Fach in dem Halbjahr als Leistungskurs gewählt haben
	 */
	public schuelerGetMengeLKByFachAndHalbjahrAsListOrException(idFach : number, halbjahr : GostHalbjahr) : List<number> {
		const schuelerListe : List<number> | null = this._map2D_lk_fachID_halbjahrID_schuelerID.getOrNull(idFach, halbjahr.id);
		if (schuelerListe !== null)
			return schuelerListe;
		return new ArrayList();
	}

	/**
	 * Gibt die Schüler-Menge für das angegebene Fach in dem angegeben Halbjahr zurück, welche das Fach
	 * als Grundkurs gewählt haben.
	 * Dabei werden auch Projektkurse und Vertiefungskurse mitgezählt.
	 *
	 * @param idFach     die ID des Faches
	 * @param halbjahr   das Halbjahr der gymnasialen Oberstufe
	 *
	 * @return die Menge der Schüler, welche das Fach in dem Halbjahr als Grundkurs gewählt haben
	 */
	public schuelerGetMengeGKByFachAndHalbjahrAsListOrException(idFach : number, halbjahr : GostHalbjahr) : List<number> {
		const schuelerListe : List<number> | null = this._map2D_gk_fachID_halbjahrID_schuelerID.getOrNull(idFach, halbjahr.id);
		if (schuelerListe !== null)
			return schuelerListe;
		return new ArrayList();
	}

	/**
	 * Gibt die Schüler-Menge für das angegebene Fach in dem angegeben Halbjahr zurück, welche das Fach
	 * als schriftlichen Grundkurs gewählt haben.
	 *
	 * @param idFach     die ID des Faches
	 * @param halbjahr   das Halbjahr der gymnasialen Oberstufe
	 *
	 * @return die Menge der Schüler, welche das Fach in dem Halbjahr als schriftlichen Grundkurs gewählt haben
	 */
	public schuelerGetMengeGKSchriftlichByFachAndHalbjahrAsListOrException(idFach : number, halbjahr : GostHalbjahr) : List<number> {
		const schuelerListe : List<number> | null = this._map2D_gk_schriftlich_fachID_halbjahrID_schuelerID.getOrNull(idFach, halbjahr.id);
		if (schuelerListe !== null)
			return schuelerListe;
		return new ArrayList();
	}

	/**
	 * Gibt die Schüler-Menge für das angegebene Fach in dem angegeben Halbjahr zurück, welche das Fach
	 * als mündlichen Grundkurs gewählt haben.
	 * Dabei werden auch Projektkurse und Vertiefungskurse mitgezählt.
	 *
	 * @param idFach     die ID des Faches
	 * @param halbjahr   das Halbjahr der gymnasialen Oberstufe
	 *
	 * @return die Menge der Schüler, welche das Fach in dem Halbjahr als mündlichen Grundkurs gewählt haben
	 */
	public schuelerGetMengeGKMuendlichByFachAndHalbjahrAsListOrException(idFach : number, halbjahr : GostHalbjahr) : List<number> {
		const schuelerListe : List<number> | null = this._map2D_gk_muendlich_fachID_halbjahrID_schuelerID.getOrNull(idFach, halbjahr.id);
		if (schuelerListe !== null)
			return schuelerListe;
		return new ArrayList();
	}

	/**
	 * Gibt die Schüler-Menge für das angegebene Fach in dem angegeben Halbjahr zurück, welche das Fach
	 * als Zusatzkurs gewählt haben.
	 *
	 * @param idFach     die ID des Faches
	 * @param halbjahr   das Halbjahr der gymnasialen Oberstufe
	 *
	 * @return die Menge der Schüler, welche das Fach in dem Halbjahr als Zusatzkurs gewählt haben
	 */
	public schuelerGetMengeZKByFachAndHalbjahrAsListOrException(idFach : number, halbjahr : GostHalbjahr) : List<number> {
		const schuelerListe : List<number> | null = this._map2D_zk_fachID_halbjahrID_schuelerID.getOrNull(idFach, halbjahr.id);
		if (schuelerListe !== null)
			return schuelerListe;
		return new ArrayList();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.gost.GostJahrgangsFachwahlenManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.gost.GostJahrgangsFachwahlenManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_gost_GostJahrgangsFachwahlenManager(obj : unknown) : GostJahrgangsFachwahlenManager {
	return obj as GostJahrgangsFachwahlenManager;
}
