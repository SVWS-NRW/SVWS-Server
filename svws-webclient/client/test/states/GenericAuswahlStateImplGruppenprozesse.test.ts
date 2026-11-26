import { describe, expect, test, vi } from "vitest";

import type { List } from "@core/java/util/List";

import type { GenericAuswahlGruppenprozessHost } from "~/states/GenericAuswahlState";
import type { GenericAuswahlReactiveState } from "~/states/GenericAuswahlStateImpl";
import { GenericAuswahlStateImpl } from "~/states/GenericAuswahlStateImpl";
import type { GenericAuswahlStateRoutingAdapter } from "~/states/GenericAuswahlStateRoutingAdapter";
import type { GenericDatenState } from "~/states/GenericDatenState";
import { GenericGruppenprozessState } from "~/states/GenericGruppenprozessState";

interface Eintrag {
	id: number;
}

type TestReactiveState = GenericAuswahlReactiveState<Eintrag>;

const REACTIVE_STATE: TestReactiveState = { idSchuljahresabschnitt: -1, liste: undefined, idSelected: null };

/** Der Routing-Adapter wird vom Konstruktor nur durchgereicht und hier nicht benötigt */
const ADAPTER = {} as GenericAuswahlStateRoutingAdapter;

/** Die Teile eines Auswahl-States, die für diese Tests nicht benötigt werden */
abstract class TestAuswahlBasis<TGruppenprozesseState extends GenericGruppenprozessState<Eintrag>>
	extends GenericAuswahlStateImpl<Eintrag, Eintrag, TestReactiveState, TGruppenprozesseState> {

	public get stammdaten(): GenericDatenState<Eintrag> {
		throw new Error("nicht benötigt");
	}

	protected async createListe(): Promise<Partial<TestReactiveState>> {
		return {};
	}

	protected getListFiltered(): List<Eintrag> {
		throw new Error("nicht benötigt");
	}

	protected async doDelete(): Promise<never> {
		throw new Error("nicht benötigt");
	}

	protected deleteMessage(): string {
		return "";
	}

}

/** Ein Auswahl-State mit dem Standard-Orchestrator */
class TestAuswahlStandard extends TestAuswahlBasis<GenericGruppenprozessState<Eintrag>> {

	public constructor() {
		super(REACTIVE_STATE, ADAPTER);
	}

}

/** Ein spezialisierter Orchestrator mit einer zusätzlichen Methode */
class SpezialGruppenprozessState extends GenericGruppenprozessState<Eintrag> {

	public spezial(): string {
		return "spezial";
	}

}

/** Ein Auswahl-State mit einem spezialisierten Orchestrator */
class TestAuswahlSpezial extends TestAuswahlBasis<SpezialGruppenprozessState> {

	public constructor(create: (host: GenericAuswahlGruppenprozessHost<Eintrag>) => SpezialGruppenprozessState) {
		super(REACTIVE_STATE, ADAPTER, create);
	}

}

/** Die Pflicht zur Factory bei einem spezialisierten Orchestrator prüft der Compiler (vue-tsc), nicht die Laufzeit */
export class TestAuswahlSpezialOhneFactory extends TestAuswahlBasis<SpezialGruppenprozessState> {

	public constructor() {
		// @ts-expect-error ohne Factory ist ein spezialisierter Orchestrator nicht erlaubt
		super(REACTIVE_STATE, ADAPTER);
	}

}

describe("GenericAuswahlStateImpl: Orchestrator der Gruppenprozesse", () => {

	test("ohne Factory wird der Standard-Orchestrator erzeugt", () => {
		const state = new TestAuswahlStandard();
		expect(state.gruppenprozesse).toBeInstanceOf(GenericGruppenprozessState);
	});

	test("eine übergebene Factory erzeugt den Orchestrator mit dem Auswahl-State als Host", () => {
		const create = vi.fn((host: GenericAuswahlGruppenprozessHost<Eintrag>) => new SpezialGruppenprozessState(host));
		const state = new TestAuswahlSpezial(create);
		expect(create).toHaveBeenCalledTimes(1);
		expect(create.mock.calls[0][0]).toBe(state);
		expect(state.gruppenprozesse).toBe(create.mock.results[0].value);
		expect(state.gruppenprozesse.spezial()).toBe("spezial");
	});

	test("der Orchestrator wird mit dem Zurücksetzen des Auswahl-States zurückgesetzt", () => {
		const state = new TestAuswahlStandard();
		const reset = vi.spyOn(state.gruppenprozesse, "reset");
		state.reset();
		expect(reset).toHaveBeenCalledTimes(1);
	});

	test("der Orchestrator hat Zugriff auf die Mehrfachauswahl des Auswahl-States", () => {
		const state = new TestAuswahlStandard();
		expect(state.getMultiSelection().isEmpty()).toBe(true);
	});

});
