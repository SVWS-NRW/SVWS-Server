import { JavaObject } from "../../../../main/resources/typescript/java/lang/JavaObject";

export class TestPerson extends JavaObject {

	/** Die ID der Person */
	public id : number = 0;

	/** Ein Kürzel der Person */
	public kuerzel : string = "";

	/** Ggf. ein akademischer Grad der Person */
	public titel : string | null = null;

	/** Der Nachname der Person. */
	public nachname : string = "";

	/** Der Vorname der Person. */
	public vorname : string = "";

	public constructor(id? : number, kuerzel? : string, titel? : string, nachname? : string, vorname?: string) {
		super();
		if (id !== undefined)
			this.id = id;
		if (kuerzel !== undefined)
			this.kuerzel = kuerzel;
		if (titel !== undefined)
			this.titel = titel;
		if (nachname !== undefined)
			this.nachname = nachname;
		if (vorname !== undefined)
			this.vorname = vorname;
	}

	public hashCode() : number {
		const prime = 31;
		let result = this.id;
		result = (prime * result) + JavaObject._hashCode(this.kuerzel);
		result = (prime * result) + ((this.titel === null) ? 0 : JavaObject._hashCode(this.titel));
		result = (prime * result) + JavaObject._hashCode(this.nachname);
		result = (prime * result) + JavaObject._hashCode(this.vorname);
		return result;
	}

	public equals(obj : unknown) : boolean {
		if (obj === null)
			return false;
		if (this as unknown === obj as unknown)
			return true;
		if (!(((obj instanceof JavaObject) && (obj.isTranspiledInstanceOf('de.svws_nrw.transpiler.test.TestPerson') === true))))
			return false;
		const other : TestPerson = (cast_de_svws_nrw_transpiler_test_TestPerson(obj));
		if (this.id !== other.id)
			return false;
		if (this.kuerzel !== other.kuerzel)
			return false;
		if (this.titel !== other.titel)
			return false;
		if (this.nachname !== other.nachname)
			return false;
		if (this.vorname !== other.vorname)
			return false;
		return true;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.transpiler.test.TestPerson'].includes(name);
	}

	public static transpilerFromJSON(json : string): TestPerson {
		const obj = JSON.parse(json);
		const result = new TestPerson();
		if (typeof obj.id === "undefined")
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		result.titel = typeof obj.titel === "undefined" ? null : obj.titel === null ? null : obj.titel;
		if (typeof obj.nachname === "undefined")
			throw new Error('invalid json format, missing attribute nachname');
		result.nachname = obj.nachname;
		if (typeof obj.vorname === "undefined")
			throw new Error('invalid json format, missing attribute vorname');
		result.vorname = obj.vorname;
		return result;
	}

	public static transpilerToJSON(obj : TestPerson) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"titel" : ' + ((obj.titel === null) ? 'null' : JSON.stringify(obj.titel)) + ',';
		result += '"nachname" : ' + JSON.stringify(obj.nachname) + ',';
		result += '"vorname" : ' + JSON.stringify(obj.vorname) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<TestPerson>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (typeof obj.titel !== "undefined") {
			result += '"titel" : ' + ((obj.titel === null) ? 'null' : JSON.stringify(obj.titel)) + ',';
		}
		if (typeof obj.nachname !== "undefined") {
			result += '"nachname" : ' + JSON.stringify(obj.nachname) + ',';
		}
		if (typeof obj.vorname !== "undefined") {
			result += '"vorname" : ' + JSON.stringify(obj.vorname) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_transpiler_test_TestPerson(obj : unknown) : TestPerson {
	return obj as TestPerson;
}
