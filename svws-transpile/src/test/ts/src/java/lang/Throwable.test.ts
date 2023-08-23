import { describe, test, expect } from "vitest";
import { NullPointerException } from "@transpiled";

describe("Different cases of Throwable Errors", ()=>{
	const e = new NullPointerException()
	test("Throwable, plain", () => {
		expect(() => {throw e}).toThrow(NullPointerException);
	});
	test("Throwable, with string", () => {
		expect(() => {throw new NullPointerException('Thrown')}).toThrow(NullPointerException);
	});
	test("Throwable, with String", () => {
		expect(() => {throw new NullPointerException(String('Thrown'))}).toThrow(NullPointerException);
	});
	test("Throwable, with String and Throwable", () => {
		expect(() => {throw new NullPointerException(String("Thrown"), e)}).toThrow(NullPointerException);
	});
	// hier kommt als Fehler: "[RangeError: Maximum call stack size exceeded]"
	test("Throwable, with Throwable", () => {
		expect(() => {throw new NullPointerException(e)}).toThrow(NullPointerException);
	});
	test("Throwable, with string and Throwable", () => {
		expect(() => {throw new NullPointerException("Thrown", e)}).toThrow(NullPointerException);
	});
	test("Throwable.toString() returns a String", () => {
		expect(e.toString()).toBeTypeOf("string")
	});
})