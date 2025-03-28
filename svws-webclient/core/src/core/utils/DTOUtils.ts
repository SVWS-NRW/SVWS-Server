import { JavaObject } from '../../java/lang/JavaObject';
import { GostBlockungsergebnisSchiene } from '../../core/data/gost/GostBlockungsergebnisSchiene';
import { GostBlockungsergebnisKursSchuelerZuordnung } from '../../core/data/gost/GostBlockungsergebnisKursSchuelerZuordnung';
import { GostBlockungsergebnisKursSchienenZuordnung } from '../../core/data/gost/GostBlockungsergebnisKursSchienenZuordnung';
import { GostBlockungsergebnisKurs } from '../../core/data/gost/GostBlockungsergebnisKurs';
import { Class } from '../../java/lang/Class';
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
	 * Liefert ein neues {@link GostBlockungsergebnisSchiene}-Objekt.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return ein neues {@link GostBlockungsergebnisSchiene}-Objekt.
	 */
	public static newGostBlockungsergebnisSchiene(idSchiene : number) : GostBlockungsergebnisSchiene {
		const eSchiene : GostBlockungsergebnisSchiene = new GostBlockungsergebnisSchiene();
		eSchiene.id = idSchiene;
		return eSchiene;
	}

	/**
	 * Liefert ein neues {@link GostBlockungsergebnisKurs}-Objekt.
	 * @param idKurs          Die Datenbank-ID des Kurses.
	 * @param idFach          Die Datenbank-ID des Faches.
	 * @param idKursart       Die ID der Kursart.
	 * @param anzahlSchienen  Die Anzahl an Schienen, die der Kurs belegt.
	 *
	 * @return ein neues {@link GostBlockungsergebnisKurs}-Objekt.
	 */
	public static newGostBlockungsergebnisKurs(idKurs : number, idFach : number, idKursart : number, anzahlSchienen : number) : GostBlockungsergebnisKurs {
		const eKurs : GostBlockungsergebnisKurs = new GostBlockungsergebnisKurs();
		eKurs.id = idKurs;
		eKurs.fachID = idFach;
		eKurs.kursart = idKursart;
		eKurs.anzahlSchienen = anzahlSchienen;
		return eKurs;
	}

	/**
	 * Liefert ein neues {@link GostBlockungsergebnisKursSchienenZuordnung}-Objekt eines bestimmten Typs, mit keinem Parameter.
	 *
	 * @param typ     Der {@link GostKursblockungRegelTyp}.
	 *
	 * @return ein neues {@link GostBlockungsergebnisKursSchienenZuordnung}-Objekt eines bestimmten Typs, mit keinem Parameter.
	 */
	public static newGostBlockungRegel0(typ : number) : GostBlockungRegel {
		const r : GostBlockungRegel = new GostBlockungRegel();
		r.id = -1;
		r.typ = typ;
		return r;
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

	/**
	 * Liefert ein neues {@link GostBlockungsergebnisKursSchienenZuordnung}-Objekt eines bestimmten Typs, mit genau drei Parametern.
	 *
	 * @param typ     Der {@link GostKursblockungRegelTyp}.
	 * @param param1  Der 1. Parameter.
	 * @param param2  Der 2. Parameter.
	 * @param param3  Der 3. Parameter.
	 *
	 * @return ein neues {@link GostBlockungsergebnisKursSchienenZuordnung}-Objekt eines bestimmten Typs, mit genau drei Parametern.
	 */
	public static newGostBlockungRegel3(typ : number, param1 : number, param2 : number, param3 : number) : GostBlockungRegel {
		const r : GostBlockungRegel = new GostBlockungRegel();
		r.id = -1;
		r.typ = typ;
		r.parameter.add(param1);
		r.parameter.add(param2);
		r.parameter.add(param3);
		return r;
	}

	/**
	 * Liefert TRUE, falls die Parameter der Regeln sich vom übergebenen Array unterscheiden.
	 *
	 * @param r   Die zu prüfende Regel.
	 * @param a   Die zu prüfenden neuen Parameter.
	 *
	 * @return TRUE, falls die Parameter der Regeln sich vom übergebenen Array unterscheiden.
	 */
	public static testRegelParameterChanged(r : GostBlockungRegel, a : Array<number>) : boolean {
		for (let i : number = 0; i < a.length; i++)
			if (a[i] !== r.parameter.get(i))
				return true;
		return false;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.DTOUtils';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.DTOUtils'].includes(name);
	}

	public static class = new Class<DTOUtils>('de.svws_nrw.core.utils.DTOUtils');

}

export function cast_de_svws_nrw_core_utils_DTOUtils(obj : unknown) : DTOUtils {
	return obj as DTOUtils;
}
