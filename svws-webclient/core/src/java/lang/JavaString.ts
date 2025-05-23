import { IllegalFormatException } from '../util/IllegalFormatException';
import { NullPointerException } from './NullPointerException';

// eslint-disable-next-line @typescript-eslint/no-extraneous-class
export abstract class JavaString {

	public static contains(str: string, s: string | null) : boolean {
		if (s === null)
			return false;
		return str.includes(s);
	}

	public static isBlank(s: string | null): boolean {
		if (s === null)
			throw new NullPointerException();
		return s.trim().length === 0;
	}

	public static isEmpty(s: string | null): boolean {
		if (s === null)
			throw new NullPointerException();
		return s.length === 0;
	}

	public static indexOf(s : string, str : string | null, fromIndex? : number) : number {
		if (str === null)
			return -1;
		return s.indexOf(str, fromIndex);
	}

	public static replaceFirst(s : string, regex : string, replacement : string): string {
		return s.replace(new RegExp(regex), replacement);
	}

	public static replaceAll(s : string, regex : string, replacement : string): string {
		return s.replace(new RegExp(regex, "g"), replacement);
	}

	public static replace(s : string, pattern : string, replacement : string): string {
		return s.replaceAll(pattern, replacement);
	}

	public static format(s : string, ...args: any[]): string {
		let i = -1;
		function handleParam(expression: string, ...formatParams: any[]) : string {
			if (expression === '%%')
				return '%';
			// Bestimme den Wert, der für den Parameter eingesetzt wird
			if (args[++i] === undefined)
				throw new IllegalFormatException();
			const replacement = args[i];
			const hasLeftJustifiedResult = formatParams[0] !== undefined;
			const paddingChar = (formatParams[1] !== undefined) && (formatParams[1][0] === '0') ? '0' : ' ';
			const paddingSize = parseInt(formatParams[1]);
			const precision = formatParams[2] === undefined ? undefined : parseInt(formatParams[2].substr(1));
			const base = formatParams[3] === undefined ? undefined : parseInt(formatParams[3].substr(1));
			let result : string = "";
			switch (formatParams[4]) {
				case 's':
					result = typeof(replacement) === 'object' ? JSON.stringify(replacement) : replacement.toString(base);
					break;
				case 'c':
					result = typeof(replacement[0]) === 'object' ? JSON.stringify(replacement[0]) : replacement[0].toString(base);
					break;
				case 'f':
					result = parseFloat(replacement).toFixed(precision);
					break;
				case 'p':
					result = parseFloat(replacement).toPrecision(precision);
					break;
				case 'e':
					result = parseFloat(replacement).toExponential(precision);
					break;
				case 'x':
					result = parseInt(replacement).toString(base === undefined ? 16 : base);
					break;
				case 'd':
					result = parseFloat(parseInt(replacement, base === undefined ? 10 : base).toPrecision(precision)).toFixed(0);
					break;
			}
			while (result.length < paddingSize)
				result = hasLeftJustifiedResult ? result + paddingChar : paddingChar + result;
			return result;
		}
		// TODO Erweiterung der Methode um argument_index und weitere conversion - Möglichkeiten laut https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Formatter.html#syntax
		const regex = /%(-)?(0?\d+)?([.]\d+)?(#\d+)?([scfpexd%])/g;

		return s.replace(regex, handleParam);
	}

	public static compareToIgnoreCase(a: string, b : string | null) : number {
		if (b === null)
			return -1;
		return a.localeCompare(b, undefined, { sensitivity: 'accent' });
		/*
		const ca : string[] = a.split('');
		const cb : string[] = b.split('');
		const len = Math.min(ca.length, cb.length);
		for (let i : number = 0; i < len; i++) {
			const cmp = ca[i].localeCompare(cb[i], undefined, { sensitivity: 'accent' });
			if (cmp !== 0) {
				const cpa = ca[i].toLocaleLowerCase().codePointAt(0);
				if (cpa === undefined)
					return 1;
				const cpb = cb[i].toLocaleLowerCase().codePointAt(0);
				if (cpb === undefined)
					return -1;
				return cpa - cpb;
			}
		}
		return ca.length - cb.length;
		*/
	}

	public static compareTo(a: string, b : string | null) : number {
		if (b === null)
			return -1;
		return a.localeCompare(b, undefined, { sensitivity: 'variant' });
		/*		const ca : string[] = a.split('');
		const cb : string[] = b.split('');
		const len = Math.min(ca.length, cb.length);
		for (let i : number = 0; i < len; i++) {
			const cmp = ca[i].localeCompare(cb[i], undefined, { sensitivity: 'variant' });
			if (cmp !== 0) {
				const cpa = ca[i].codePointAt(0);
				if (cpa === undefined)
					return 1;
				const cpb = cb[i].codePointAt(0);
				if (cpb === undefined)
					return -1;
				return cpa - cpb;
			}
		}
		return ca.length - cb.length;*/
	}


	public static equalsIgnoreCase(a : string, b : string | null) : boolean {
		return (b === null) ? false : a.localeCompare(b.valueOf(), undefined, { sensitivity: 'accent' }) === 0;
	}

}


export function cast_java_lang_String(obj : unknown) : string | null {
	return obj as string | null;
}
