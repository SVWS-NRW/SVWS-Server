import { TestPerson } from "./TestPerson";

export const s = {
	name: "String",
	a: "a",
	b: "b",
	c: "c",
	d: "d",
	e: "e",
};

export const n = {
	name: "Number",
	a: 1,
	b: 2,
	c: 3,
	d: 4,
	e: 5,
};

export const l = {
	name: "TestPerson",
	a: new TestPerson(1, "A", "", "Albert", "Alfons"),
	b: new TestPerson(2, "B", "Dr.", "Bertram", "Bert"),
	c: new TestPerson(3, "C", "Prof. Dr.", "Christopher", "Chris"),
	d: new TestPerson(4, "D", "", "Dunkel", "Dark"),
	e: new TestPerson(5, "E", "Dr.", "Emil", "Emilia"),
};