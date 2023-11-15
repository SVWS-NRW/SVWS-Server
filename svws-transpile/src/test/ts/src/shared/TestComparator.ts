import { type Comparator } from "@transpiled";
import { TestPerson } from "./TestPerson";

export class TestMinComparator implements Comparator<string | number | TestPerson> {
	public compare(a: unknown, b: unknown) : 1 | -1 | 0 {
		if ((typeof a === 'string' && typeof b === 'string') || (typeof a === 'number' && typeof b === 'number')) {
			return (a < b) ? -1 : ((a === b) ? 0 : 1);
		} else if (a instanceof TestPerson && b instanceof TestPerson) {
			// nachname ist leer...
			return ((a.nachname ?? "") < (b.nachname ?? "")) ? -1 : (((a.nachname ?? "") === (b.nachname ?? "")) ? 0 : 1);
		} else {
			return 1;
		}
	}
}

export class TestMaxComparator implements Comparator<string | number | TestPerson> {
	public compare(a: unknown, b: unknown): 1 | -1 | 0 {
		if ((typeof a === 'string' && typeof b === 'string') || (typeof a === 'number' && typeof b === 'number')) {
			return (a > b) ? -1 : ((a === b) ? 0 : 1);
		} else if (a instanceof TestPerson && b instanceof TestPerson) {
			// nachname ist leer...
			return ((a.nachname ?? "") > (b.nachname ?? "")) ? -1 : (((a.nachname ?? "") === (b.nachname ?? "")) ? 0 : 1);
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
