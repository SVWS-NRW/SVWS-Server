import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap2D } from '../../../core/adt/map/HashMap2D';
import { GostAbiturFach } from '../../../core/types/gost/GostAbiturFach';
import { GostJahrgangFachwahlen } from '../../../core/data/gost/GostJahrgangFachwahlen';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { GostKursart } from '../../../core/types/gost/GostKursart';

export class GostJahrgangsFachwahlenManager extends JavaObject {

	/**
	 * (Fach-ID, Abifach [1=LK,3,4]) --> ArrayList<Schueler-ID> = Eine Liste der Schüler, welche das angegeben Fach als Abiturfach den entsprechenden Typs (siehe ID von {@link GostAbiturFach}) haben.
	 */
	private readonly _map2D_fachID_abifachID_schuelerID : HashMap2D<number, number, List<number>> = new HashMap2D();


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

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.gost.GostJahrgangsFachwahlenManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_gost_GostJahrgangsFachwahlenManager(obj : unknown) : GostJahrgangsFachwahlenManager {
	return obj as GostJahrgangsFachwahlenManager;
}
