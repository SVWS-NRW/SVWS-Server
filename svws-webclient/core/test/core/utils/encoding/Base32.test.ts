import { Base32 } from '../../../../src/core/utils/encoding/Base32';
import { IllegalArgumentException } from '../../../../src/java/lang/IllegalArgumentException';
import { describe, it, expect } from 'vitest';

describe('Base32 Tests (RFC 4648)', () => {

	describe('encode', () => {

		it('sollte leere Eingaben korrekt als leeren String kodieren', () => {
			expect(Base32.encode(null)).toBe("");
			expect(Base32.encode([])).toBe("");
		});

		it.each([
			{ input: "", expected: "" },
			{ input: "f", expected: "MY======" },
			{ input: "fo", expected: "MZXQ====" },
			{ input: "foo", expected: "MZXW6===" },
			{ input: "foob", expected: "MZXW6YQ=" },
			{ input: "fooba", expected: "MZXW6YTB" },
			{ input: "foobar", expected: "MZXW6YTBOI======" },
		])('RFC 4648 Testvektor: "$input" -> "$expected"', ({ input, expected }) => {
			// Umwandlung des Strings in ein Array von Zahlen (Bytes)
			const data = Array.from(new TextEncoder().encode(input));
			expect(Base32.encode(data)).toBe(expected);
		});
	});

	describe('decode', () => {

		it('sollte leere oder null Strings als leeres Array dekodieren', () => {
			expect(Base32.decode(null)).toEqual([]);
			expect(Base32.decode("")).toEqual([]);
		});

		it.each([
			{ input: "MY======", expected: "f" },
			{ input: "MZXQ====", expected: "fo" },
			{ input: "MZXW6===", expected: "foo" },
			{ input: "MZXW6YQ=", expected: "foob" },
			{ input: "MZXW6YTB", expected: "fooba" },
			{ input: "MZXW6YTBOI======", expected: "foobar" },
		])('RFC 4648 Testvektor: "$input" -> "$expected"', ({ input, expected }) => {
			const result = Base32.decode(input);
			const decodedString = new TextDecoder().decode(new Uint8Array(result));
			expect(decodedString).toBe(expected);
		});

		it('sollte Case-Insensitivity und fehlendes Padding unterstützen', () => {
			const result = Base32.decode("mzxw6ytboi");
			const decodedString = new TextDecoder().decode(new Uint8Array(result));
			expect(decodedString).toBe("foobar");
		});

		it('sollte bei ungültigen Zeichen eine IllegalArgumentException werfen', () => {
			// '1' ist nicht im Alphabet
			expect(() => Base32.decode("MZXW61")).toThrow(IllegalArgumentException);
			// Sonderzeichen sind nicht im Alphabet
			expect(() => Base32.decode("MZXW6!")).toThrow(IllegalArgumentException);
		});
	});
});
