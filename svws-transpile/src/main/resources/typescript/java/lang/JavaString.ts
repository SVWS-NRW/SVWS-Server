export abstract class JavaString {

    public static replaceFirst(s : String, regex : String, replacement : String) {
        return s.replace(new RegExp(regex.valueOf()), replacement.valueOf());
    }

    public static replaceAll(s : String, regex : String, replacement : String) {
        return s.replace(new RegExp(regex.valueOf(), "g"), replacement.valueOf());
    }

    public static compareToIgnoreCase(a: String, b : String | null) : number {
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

    public static compareTo(a: String, b : String | null) : number {
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


    public static equalsIgnoreCase(a : String, b : String | null) : boolean {
        return (b === null) ? false : a.localeCompare(b.valueOf(), undefined, { sensitivity: 'accent' }) === 0;
    }

}


export function cast_java_lang_String(obj : unknown) : String {
	return obj as String;
}
