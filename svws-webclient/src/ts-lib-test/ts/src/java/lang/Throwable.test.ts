import { describe, test, expect } from "vitest";
import { NullPointerException, Throwable } from "~/index";

describe("Different cases of Throwable Errors", ()=>{
	test("Throwable, plain", () => {
		expect(() => {throw new NullPointerException()}).toThrow(NullPointerException);
	});
	test("Throwable, with string", () => {
		expect(() => {throw new NullPointerException('Thrown')}).toThrow(NullPointerException);
	});
	test("Throwable, with String", () => {
		expect(() => {throw new NullPointerException(String('Thrown'))}).toThrow(NullPointerException);
	});
	test("Throwable, with String and Throwable", () => {
		try {
			throw new NullPointerException()
		} catch (e: unknown) {
			expect(e).toBeInstanceOf(Throwable)
			expect(() => {throw new NullPointerException(String("Thrown"), e as NullPointerException)}).toThrow(NullPointerException);
		}
	});
	// hier kommt als Fehler: "[RangeError: Maximum call stack size exceeded]"
	test.todo("Throwable, with Throwable", () => {
		try {
			throw new NullPointerException()
		} catch (e: unknown) {
			expect(e).toBeInstanceOf(Throwable)
			expect(() => {throw new NullPointerException(e as NullPointerException)}).toThrow(NullPointerException);
		}
	});
	test("Throwable, with string and Throwable", () => {
		try {
			throw new NullPointerException()
		} catch (e: unknown) {
			expect(e).toBeInstanceOf(Throwable)
			expect(() => {throw new NullPointerException("Thrown", e as NullPointerException)}).toThrow(NullPointerException);
		}
	});
})