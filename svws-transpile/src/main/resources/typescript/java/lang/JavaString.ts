import { NullPointerException } from "./NullPointerException";

export abstract class JavaString {

    public static contains(str: string, s: string | null) : boolean {
        if (s === null)
            return false;
        return str.includes(s);
    }

    public static indexOf(s : string, str : string | null, fromIndex? : number) : number {
        if (str === null)
            return -1;
        return s.indexOf(str, fromIndex);
    }

    public static replaceFirst(s : string, regex : string, replacement : string) {
        return s.replace(new RegExp(regex), replacement);
    }

    public static replaceAll(s : string, regex : string, replacement : string) {
        return s.replace(new RegExp(regex, "g"), replacement);
    }

    public static compareToIgnoreCase(a: string, b : string | null) : number {
        if (b === null)
            return -1;
        const ca : string[] = a.split('');
        const cb : string[] = b.split('');
        let len = Math.min(ca.length, cb.length);
        for (let i : number = 0; i < len; i++) {
            let cmp = ca[i].localeCompare(cb[i].valueOf(), undefined, { sensitivity: 'accent' });
            if (cmp !== 0) {
                let cpa = ca[i].codePointAt(0);
                if (cpa === undefined)
                    return 1;
                let cpb = cb[i].codePointAt(0);
                if (cpb === undefined)
                    return -1;
                return cpa - cpb;
            }
        }
        return ca.length - cb.length;
    }

    public static compareTo(a: string, b : string | null) : number {
        if (b === null)
            return -1;
        const ca : string[] = a.split('');
        const cb : string[] = b.split('');
        let len = Math.min(ca.length, cb.length);
        for (let i : number = 0; i < len; i++) {
            let cmp = ca[i].localeCompare(cb[i].valueOf(), undefined, { sensitivity: 'variant' });
            if (cmp !== 0) {
                let cpa = ca[i].codePointAt(0);
                if (cpa === undefined)
                    return 1;
                let cpb = cb[i].codePointAt(0);
                if (cpb === undefined)
                    return -1;
                return cpa - cpb;
            }
        }
        return ca.length - cb.length;
    }


    public static equalsIgnoreCase(a : string, b : string | null) : boolean {
        return (b === null) ? false : a.localeCompare(b.valueOf(), undefined, { sensitivity: 'accent' }) === 0;
    }

}


export function cast_java_lang_String(obj : unknown) : string | null {
	return obj as string | null;
}
