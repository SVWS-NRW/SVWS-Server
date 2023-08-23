import { TestPerson } from "./TestPerson";

export const s = { name: "String", a: new String("a"), b: new String("b"), c: new String("c"), d: new String("d"), e: new String("e") };
export const n = {
	name: "Number",
	a: new Number(1),
	b: new Number(2),
	c: new Number(3),
	d: new Number(4),
	e: new Number(5),
};
export const l = {
	name: "TestPerson",
	a: new TestPerson(),
	b: new TestPerson(),
	c: new TestPerson(),
	d: new TestPerson(),
	e: new TestPerson(),
};