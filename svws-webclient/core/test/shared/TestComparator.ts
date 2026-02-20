import { LehrerListeEintrag } from "../../src/core/data/lehrer/LehrerListeEintrag";
import type { Comparator } from "../../src/java/util/Comparator";

type ComPres = 1 | -1 | 0;

export class TestMinComparator<T>
implements Comparator<T> {

	public compare(a: unknown, b: unknown): ComPres {
		if ((typeof a === "string" && typeof b === "string") || (typeof a === "number" && typeof b === "number")) {
			if (a.valueOf() < b.valueOf()) {
				return -1;
			} return ((a.valueOf() === b.valueOf()) ? 0 : 1);
		} else if (a instanceof LehrerListeEintrag && b instanceof LehrerListeEintrag) {
			// nachname ist leer...
			if (a.nachname < b.nachname) {
				return -1;
			} return (a.nachname === b.nachname) ? 0 : 1;
		} else {
			return 1;
		}
	}
}

export class TestMaxComparator<T>
implements Comparator<T> {
	public compare(a: unknown, b: unknown): ComPres {
		if ((typeof a === "string" && typeof b === "string") || (typeof a === "number" && typeof b === "number")) {
			if (a.valueOf() > b.valueOf()) {
				return -1;
			}
			return ((a.valueOf() === b.valueOf()) ? 0 : 1);
		} else if (a instanceof LehrerListeEintrag && b instanceof LehrerListeEintrag) {
			// nachname ist leer...
			if (a.nachname > b.nachname) {
				return -1;
			}
			return (a.nachname === b.nachname) ? 0 : 1;
		} else {
			return 1;
		}
	}
}

export class TestRandomComparator implements Comparator<number> {
	public compare(o1: number, o2: number): ComPres {
		if (o1 === o2) {
			return 0;
		}
		return o1 < o2 ? -1 : 1;
	}
}
