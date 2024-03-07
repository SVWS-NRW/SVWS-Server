import { JavaObject } from '../../java/lang/JavaObject';
import { GostBlockungsergebnisKursSchuelerZuordnung } from '../../core/data/gost/GostBlockungsergebnisKursSchuelerZuordnung';
import { GostBlockungsergebnisKursSchienenZuordnung } from '../../core/data/gost/GostBlockungsergebnisKursSchienenZuordnung';

export class DTOUtils extends JavaObject {


	private constructor() {
		super();
	}

	/**
	 * Liefert ein neues {@link GostBlockungsergebnisKursSchuelerZuordnung}-Objekt.
	 *
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return ein neues {@link GostBlockungsergebnisKursSchuelerZuordnung}-Objekt.
	 */
	public static newGostBlockungsergebnisKursSchuelerZuordnung(idKurs : number, idSchueler : number) : GostBlockungsergebnisKursSchuelerZuordnung {
		const z : GostBlockungsergebnisKursSchuelerZuordnung = new GostBlockungsergebnisKursSchuelerZuordnung();
		z.idKurs = idKurs;
		z.idSchueler = idSchueler;
		return z;
	}

	/**
	 * Liefert ein neues {@link GostBlockungsergebnisKursSchienenZuordnung}-Objekt.
	 *
	 * @param idKurs     Die Datenbank-ID des Kurses.
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return ein neues {@link GostBlockungsergebnisKursSchienenZuordnung}-Objekt.
	 */
	public static newGostBlockungsergebnisKursSchienenZuordnung(idKurs : number, idSchiene : number) : GostBlockungsergebnisKursSchienenZuordnung {
		const z : GostBlockungsergebnisKursSchienenZuordnung = new GostBlockungsergebnisKursSchienenZuordnung();
		z.idKurs = idKurs;
		z.idSchiene = idSchiene;
		return z;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.DTOUtils';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.DTOUtils'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_DTOUtils(obj : unknown) : DTOUtils {
	return obj as DTOUtils;
}
