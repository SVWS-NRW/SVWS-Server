import { IllegalFormatException } from '../util/IllegalFormatException';
import { NullPointerException } from './NullPointerException';

export abstract class JavaString {

	public static contains(str: string, s: string | null): boolean {
		if (s === null) {
			return false;
		}
		return str.includes(s);
	}

	public static isBlank(s: string | null): boolean {
		if (s === null) {
			throw new NullPointerException();
		}
		return s.trim().length === 0;
	}

	public static isEmpty(s: string | null): boolean {
		if (s === null) {
			throw new NullPointerException();
		}
		return s.length === 0;
	}

	public static indexOf(s: string, str: string | null, fromIndex?: number): number {
		if (str === null) {
			return -1;
		}
		return s.indexOf(str, fromIndex);
	}

	public static matches(s: string, regex: string): boolean {
		const regexp = new RegExp(regex);
		return regexp.test(s);
	}

	public static replaceFirst(s: string, regex: string, replacement: string): string {
		return s.replace(new RegExp(regex), replacement);
	}

	public static replaceAll(s: string, regex: string, replacement: string): string {
		return s.replace(new RegExp(regex, "g"), replacement);
	}

	public static replace(s: string, pattern: string, replacement: string): string {
		return s.replaceAll(pattern, replacement);
	}

	public static format(s: string, ...args: any[]): string {
		let i = -1;
		function handleParam(expression: string, ...formatParams: any[]): string {
			if (expression === '%%') {
				return '%';
			}
			// Bestimme den Wert, der für den Parameter eingesetzt wird
			if (args[++i] === undefined) {
				throw new IllegalFormatException();
			}
			const replacement = args[i];
			const hasLeftJustifiedResult = formatParams[0] !== undefined;
			const paddingChar = (formatParams[1] !== undefined) && (formatParams[1][0] === '0') ? '0' : ' ';
			const paddingSize = Number.parseInt(formatParams[1]);
			const precision = formatParams[2] === undefined ? undefined : Number.parseInt(formatParams[2].substr(1));
			const base = formatParams[3] === undefined ? undefined : Number.parseInt(formatParams[3].substr(1));
			let result: string = "";

			switch (formatParams[4]) {
				case 's':
					result = typeof (replacement) === 'object' ? JSON.stringify(replacement) : replacement.toString(base);
					break;
				case 'c':
					result = typeof (replacement[0]) === 'object' ? JSON.stringify(replacement[0]) : replacement[0].toString(base);
					break;
				case 'f':
					result = Number.parseFloat(replacement).toFixed(precision);
					break;
				case 'p':
					result = Number.parseFloat(replacement).toPrecision(precision);
					break;
				case 'e':
					result = Number.parseFloat(replacement).toExponential(precision);
					break;
				case 'x':
					result = Number.parseInt(replacement).toString(base ?? 16);
					break;
				case 'd':
					result = Number.parseFloat(Number.parseInt(replacement, base ?? 10).toPrecision(precision)).toFixed(0);
					break;
				case 'b':
					result = String(replacement);
					break;
			}
			while (result.length < paddingSize) {
				result = hasLeftJustifiedResult ? result + paddingChar : paddingChar + result;
			}
			return result;
		}
		// TODO Erweiterung der Methode um argument_index und weitere conversion - Möglichkeiten laut https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Formatter.html#syntax
		const regex = /%(-)?(0?\d+)?([.]\d+)?(#\d+)?([scfpexdb%])/g;

		return s.replaceAll(regex, handleParam);
	}

	public static getBytes(s: string | null): Array<number> {
		if (s === null) {
			throw new NullPointerException();
		}
		return Array.from(new TextEncoder().encode(s));
	}

	public static compareToIgnoreCase(a: string, b: string | null): number {
		if (b === null) {
			return -1;
		}
		return a.localeCompare(b, undefined, { sensitivity: 'accent' });
	}

	public static compareTo(a: string, b: string | null): number {
		if (b === null) {
			return -1;
		}
		return a.localeCompare(b, undefined, { sensitivity: 'variant' });
	}


	public static equalsIgnoreCase(a: string, b: string | null): boolean {
		return (b === null) ? false : a.localeCompare(b.valueOf(), undefined, { sensitivity: 'accent' }) === 0;
	}

}


export function cast_java_lang_String(obj: unknown): string | null {
	return obj as string | null;
}
