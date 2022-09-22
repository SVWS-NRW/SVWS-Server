/* eslint-disable @typescript-eslint/ban-types */
import { LehrerListeEintrag, Comparator } from "~/index";

export class TestMinComparator
    implements Comparator<string | Number | LehrerListeEintrag>
{
    public compare(a: unknown, b: unknown): 1 | -1 | 0 {
        if ((a instanceof String && b instanceof String) || (a instanceof Number && b instanceof Number)) {
            return (a.valueOf() < b?.valueOf()) ? -1 : ((a.valueOf() === b?.valueOf()) ? 0 : 1);
        } else if (a instanceof LehrerListeEintrag && b instanceof LehrerListeEintrag) {
            // nachname ist leer...
            return ((a.nachname ?? "") < (b.nachname ?? "")) ? -1 : (((a.nachname ?? "") === (b.nachname ?? "")) ? 0 : 1);
        } else {
          return 1;
        }
    }
}
export class TestMaxComparator
    implements Comparator<string | Number | LehrerListeEintrag>
{
    public compare(a: unknown, b: unknown): 1 | -1 | 0 {
        if ((a instanceof String && b instanceof String) || (a instanceof Number && b instanceof Number)) {
            return (a.valueOf() > b?.valueOf()) ? -1 : ((a.valueOf() === b?.valueOf()) ? 0 : 1);
        } else if (a instanceof LehrerListeEintrag && b instanceof LehrerListeEintrag) {
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