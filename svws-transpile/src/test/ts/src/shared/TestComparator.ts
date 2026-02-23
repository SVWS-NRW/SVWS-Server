import type { Comparator } from "../../../../main/resources/typescript/java/util/Comparator";
import { TestPerson } from "./TestPerson";

type CompRes = 1 | -1 | 0;

export class TestMinComparator implements Comparator<string | number | TestPerson> {
	public compare(a: unknown, b: unknown): CompRes {
		if ((typeof a === 'string' && typeof b === 'string') || (typeof a === 'number' && typeof b === 'number')) {
			if (a < b) {
				return -1;
			}
			return (a === b) ? 0 : 1;
		} else if (a instanceof TestPerson && b instanceof TestPerson) {
			// nachname ist leer...
			if (a.nachname < b.nachname) {
				return -1;
			}
			return (a.nachname === b.nachname) ? 0 : 1;
		} else {
			return 1;
		}
	}
}

export class TestMaxComparator implements Comparator<string | number | TestPerson> {
	public compare(a: unknown, b: unknown): CompRes {
		if ((typeof a === 'string' && typeof b === 'string') || (typeof a === 'number' && typeof b === 'number')) {
			if (a > b) {
				return -1;
			}
			return (a === b) ? 0 : 1;
		} else if (a instanceof TestPerson && b instanceof TestPerson) {
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
	public compare(o1: number, o2: number): CompRes {
		if (o1 === o2) {
			return 0;
		}
		return o1 < o2 ? -1 : 1;
	}
}
