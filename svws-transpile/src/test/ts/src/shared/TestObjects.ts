import { TestPerson } from "./TestPerson";

export const s = { name: "String", a: "a", b: "b", c: "c", d: "d", e: "e" };
export const n = { name: "Number", a: 1, b: 2, c: 3, d: 4, e: 5, };
export const l = {
	name: "TestPerson",
	a: new TestPerson(),
	b: new TestPerson(),
	c: new TestPerson(),
	d: new TestPerson(),
	e: new TestPerson(),
};