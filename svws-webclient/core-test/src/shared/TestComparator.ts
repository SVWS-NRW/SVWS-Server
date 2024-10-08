import type { Comparator} from "@core";
import { LehrerListeEintrag } from "@core";

export class TestMinComparator
implements Comparator<string | number | LehrerListeEintrag>
{
	public compare(a: unknown, b: unknown): 1 | -1 | 0 {
		if ((typeof a === "string" && typeof b === "string") || (typeof a === "number" && typeof b === "number")) {
			return (a.valueOf() < b.valueOf()) ? -1 : ((a.valueOf() === b.valueOf()) ? 0 : 1);
		} else if (a instanceof LehrerListeEintrag && b instanceof LehrerListeEintrag) {
			// nachname ist leer...
			return (a.nachname < b.nachname) ? -1 : (a.nachname === b.nachname) ? 0 : 1;
		} else {
			return 1;
		}
	}
}
export class TestMaxComparator
implements Comparator<string | number | LehrerListeEintrag>
{
	public compare(a: unknown, b: unknown): 1 | -1 | 0 {
		if ((typeof a === "string" && typeof b === "string") || (typeof a === "number" && typeof b === "number")) {
			return (a.valueOf() > b.valueOf()) ? -1 : ((a.valueOf() === b.valueOf()) ? 0 : 1);
		} else if (a instanceof LehrerListeEintrag && b instanceof LehrerListeEintrag) {
			// nachname ist leer...
			return (a.nachname > b.nachname) ? -1 : (a.nachname === b.nachname) ? 0 : 1;
		} else {
			return 1;
		}
	}
}

export class TestRandomComparator implements Comparator<number> {
	public compare(o1: number, o2: number): 1 | -1 | 0 {
		return o1 === o2 ? 0 : o1 < o2 ? -1 : 1;
	}
}