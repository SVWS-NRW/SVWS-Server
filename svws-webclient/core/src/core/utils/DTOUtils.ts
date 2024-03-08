import { JavaObject } from '../../java/lang/JavaObject';
import { GostBlockungsergebnisKursSchuelerZuordnung } from '../../core/data/gost/GostBlockungsergebnisKursSchuelerZuordnung';
import { GostBlockungsergebnisKursSchienenZuordnung } from '../../core/data/gost/GostBlockungsergebnisKursSchienenZuordnung';
import { GostBlockungRegel } from '../../core/data/gost/GostBlockungRegel';

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

	/**
	 * Liefert ein neues {@link GostBlockungsergebnisKursSchienenZuordnung}-Objekt eines bestimmten Typs, mit genau einem Parameter.
	 *
	 * @param typ     Der {@link GostKursblockungRegelTyp}.
	 * @param param1  Der 1. Parameter.
	 *
	 * @return ein neues {@link GostBlockungsergebnisKursSchienenZuordnung}-Objekt eines bestimmten Typs, mit genau einem Parameter.
	 */
	public static newGostBlockungRegel1(typ : number, param1 : number) : GostBlockungRegel {
		const r : GostBlockungRegel = new GostBlockungRegel();
		r.id = -1;
		r.typ = typ;
		r.parameter.add(param1);
		return r;
	}

	/**
	 * Liefert ein neues {@link GostBlockungsergebnisKursSchienenZuordnung}-Objekt eines bestimmten Typs, mit genau zwei Parametern.
	 *
	 * @param typ     Der {@link GostKursblockungRegelTyp}.
	 * @param param1  Der 1. Parameter.
	 * @param param2  Der 2. Parameter.
	 *
	 * @return ein neues {@link GostBlockungsergebnisKursSchienenZuordnung}-Objekt eines bestimmten Typs, mit genau zwei Parametern.
	 */
	public static newGostBlockungRegel2(typ : number, param1 : number, param2 : number) : GostBlockungRegel {
		const r : GostBlockungRegel = new GostBlockungRegel();
		r.id = -1;
		r.typ = typ;
		r.parameter.add(param1);
		r.parameter.add(param2);
		return r;
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
