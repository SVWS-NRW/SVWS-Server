import { Base45 } from '../../../../src/core/utils/encoding/Base45';
import { IllegalArgumentException } from '../../../../src/java/lang/IllegalArgumentException';
import { describe, it, expect } from 'vitest';

describe('Base45 Tests (RFC 9285)', () => {

	describe('encode', () => {

		it('sollte leere Eingaben korrekt als leeren String kodieren', () => {
			expect(Base45.encode(null)).toBe("");
			expect(Base45.encode([])).toBe("");
		});

		it.each([
			{ input: "AB", expected: "BB8" },
			{ input: "Hello!!", expected: "%69 VD92EX0" },
			{ input: "base-45", expected: "UJCLQE7W581" },
		])('RFC 9285 Testvektor: "$input" -> "$expected"', ({ input, expected }) => {
			const data = Array.from(new TextEncoder().encode(input));
			expect(Base45.encode(data)).toBe(expected);
		});

		it('sollte ungerade Anzahl an Bytes (Ein-Byte-Block) korrekt kodieren', () => {
			const data = Array.from(new TextEncoder().encode("J"));
			expect(Base45.encode(data)).toBe("T1");
		});
	});

	describe('decode', () => {

		it('sollte leere oder null Strings als leeres Array dekodieren', () => {
			expect(Base45.decode(null)).toEqual([]);
			expect(Base45.decode("")).toEqual([]);
		});

		it.each([
			{ input: "BB8", expected: "AB" },
			{ input: "%69 VD92EX0", expected: "Hello!!" },
			{ input: "UJCLQE7W581", expected: "base-45" },
		])('RFC 9285 Testvektor: "$input" -> "$expected"', ({ input, expected }) => {
			const result = Base45.decode(input);
			const decodedString = new TextDecoder().decode(new Uint8Array(result));
			expect(decodedString).toBe(expected);
		});

		it('sollte 2-Zeichen-Blöcke (einzelne Bytes) korrekt dekodieren', () => {
			const result = Base45.decode("T1");
			const decodedString = new TextDecoder().decode(new Uint8Array(result));
			expect(decodedString).toBe("J");
		});

		it('sollte bei ungültiger Länge (len % 3 === 1) eine IllegalArgumentException werfen', () => {
			expect(() => Base45.decode("A")).toThrow(IllegalArgumentException);
		});

		it('sollte bei ungültigen Zeichen (z.B. Kleinbuchstaben) werfen', () => {
			expect(() => Base45.decode("abc")).toThrow(IllegalArgumentException);
		});

		it('sollte mathematische Überlauf-Werte (> 0xFFFF) abfangen', () => {
			expect(() => Base45.decode("ZZZ")).toThrow(IllegalArgumentException);
		});
	});

});
