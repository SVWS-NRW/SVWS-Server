import { beforeEach, expect, test, afterEach, describe } from "vitest";
import type { VueWrapper } from "@vue/test-utils";
import { mount } from "@vue/test-utils";
import SvwsUiTextareaInput from "@ui/ui/controls/SvwsUiTextareaInput.vue";
import SvwsUiTooltip from "@ui/ui/SvwsUiTooltip.vue";
import { BasicValidator } from "@core/asd/validate/BasicValidator";
import type { ValidatorFehler } from "@core/asd/validate/ValidatorFehler";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import type { List } from "@core/java/util/List";

type prop_names = "modelValue" | "disabled" | "valid" | "statistics" | "resizeable" | "span" | "required";

let wrapper: VueWrapper<InstanceType<typeof SvwsUiTextareaInput>>;
let idComponent: string;
let idPlaceholder: string;
let idStatistics: string;

beforeEach(() => {

	wrapper = mount(SvwsUiTextareaInput, {
		props: {
			modelValue: null,
			placeholder: "",
			valid: () => true,
			statistics: false,
			required: false,
			disabled: false,
			resizeable: "vertical",
			autoresize: false,
			cols: 80,
			rows: 3,
			maxLen: undefined,
			span: undefined,
		},
	});

	idComponent = "#" + wrapper.findComponent({ name: "SvwsUiTextareaInput" }).vm.idComponent;
	idPlaceholder = "#" + wrapper.findComponent({ name: "SvwsUiTextareaInput" }).vm.idPlaceholder;
	idStatistics = "#" + wrapper.findComponent({ name: "SvwsUiTextareaInput" }).vm.idStatistics;
});

afterEach(() => {
	wrapper.unmount();
	document.body.innerHTML = "";
});

// ─────────────────────────────────────────────────────────────────────────────
// Basic Rendering
// ─────────────────────────────────────────────────────────────────────────────

test("Rendert HTML korrekt", async () => {
	expect(wrapper.find(idComponent).exists()).toBeTruthy();
	expect(wrapper.find("textarea").exists()).toBeTruthy();
});


test("Setzt die textarea-Referenz korrekt", async () => {
	const textareaRef = wrapper.vm.$refs.textarea;
	expect(textareaRef).not.toBeNull();
	expect((textareaRef as HTMLElement).tagName).toBe("TEXTAREA");
	expect((textareaRef as HTMLTextAreaElement).classList).toContain("textarea-input--control");
});

// ─────────────────────────────────────────────────────────────────────────────
// Setzen von CSS Klassen
// ─────────────────────────────────────────────────────────────────────────────

describe.concurrent("Tests für die CSS-Props", () => {
	test.each([
		["modelValue", "textarea-input--filled", "Prop modelValue wird an CSS übergeben"],
		["disabled", "textarea-input--disabled", "Prop disabled wird an CSS übergeben"],
		["statistics", "textarea-input--statistics", "Prop statistics wird an CSS übergeben"],
		["resizeable", "textarea-input--resize-none", "Prop resizeable mit none wird an CSS übergeben"],
		["span", "col-span-full", "Prop span mit full wird an CSS übergeben"],
	])("%s(%s) => %s", async (x, y) => {
		if (["modelValue", "disabled", "statistics"].includes(x)) {
			expect(wrapper.props(x as prop_names)).toBeFalsy();
		} else if (x === "resizeable") {
			expect(wrapper.props("resizeable")).toBe("vertical");
			expect(wrapper.classes()).toContain("textarea-input--resize-vertical");
		} else {
			expect(wrapper.props("span")).toBeUndefined();
		}

		switch (x) {
			case "modelValue":
				await wrapper.setProps({ modelValue: "test" });
				break;
			case "disabled":
				await wrapper.setProps({ disabled: true });
				break;
			case "statistics":
				await wrapper.setProps({ statistics: true });
				break;
			case "resizeable":
				await wrapper.setProps({ resizeable: "none" });
				break;
			case "span":
				await wrapper.setProps({ span: "full" });
				break;
		}
		expect(wrapper.find("label").classes()).toContain(y);
	});

	test("Prop resizeable mit horizontal wird an CSS übergeben", async () => {
		const w = mount(SvwsUiTextareaInput);
		await w.setProps({ resizeable: "horizontal" });
		expect(w.find("label").classes()).toContain("textarea-input--resize-horizontal");
	});

	test("Prop resizeable mit both wird an CSS übergeben", async () => {
		await wrapper.setProps({ resizeable: "both" });
		expect(wrapper.find("label").classes()).toContain("textarea-input--resize-both");
	});

	test("Prop span mit grow wird an CSS übergeben", async () => {
		await wrapper.setProps({ span: "grow" });
		expect(wrapper.find("label").classes()).toContain("grow");
	});

	test("Prop required mit true wird an CSS übergeben", async () => {
		await wrapper.setProps({ placeholder: "Placeholder", required: true });
		const requiredSpan = wrapper.find(`${idPlaceholder} span.i-ri-asterisk`);
		expect(requiredSpan.exists()).toBeTruthy();
		expect(requiredSpan.classes()).toContain("textarea-input--placeholder--required");
	});

	test("Prop modelValue gleich null setzt keine Filled Class", async () => {
		await wrapper.setProps({ modelValue: null });
		expect(wrapper.find("label").classes()).not.toContain("textarea-input--filled");
	});
});

// ─────────────────────────────────────────────────────────────────────────────
// Bedingtes Rendern
// ─────────────────────────────────────────────────────────────────────────────

describe("Bedingtes Rendern der HTML-Elemente", () => {

	describe("Textarea", () => {
		test("hat leeren Wert, wenn modelValue null ist", async () => {
			await wrapper.setProps({ modelValue: null });
			expect(wrapper.get("textarea").element.value.length).toBe(0);
		});

		test("hat leeren Wert, wenn modelValue '' ist", async () => {
			await wrapper.setProps({ modelValue: "" });
			expect(wrapper.get("textarea").element.value.length).toBe(0);
		});

		test("zeigt modelValue korrekt an", async () => {
			await wrapper.setProps({ modelValue: "Test" });
			expect(wrapper.get("textarea").element.value).toBe("Test");
		});

		test("zeigt getippten Wert korrekt an auch ohne modelValue Update (uncontrolled)", async () => {
			await wrapper.find("textarea").setValue("Uncontrolled");
			expect(wrapper.get("textarea").element.value).toBe("Uncontrolled");
		});

		test("required=false setzt kein required-Attribut", () => {
			expect(wrapper.get("textarea").attributes("required")).toBeUndefined();
		});

		test("required=true setzt required-Attribut", async () => {
			await wrapper.setProps({ required: true });
			expect(wrapper.get("textarea").attributes("required")).toBe("");
		});

		test("disabled=false setzt kein disabled-Attribut", () => {
			expect(wrapper.get("textarea").attributes("disabled")).toBeUndefined();
		});

		test("disabled=true setzt disabled-Attribut", async () => {
			await wrapper.setProps({ disabled: true });
			expect(wrapper.get("textarea").attributes("disabled")).toBe("");
		});

		test("rows hat korrekten Defaultwert (3)", () => {
			expect(wrapper.get("textarea").attributes("rows")).toBe("3");
		});

		test("rows wird korrekt gesetzt", async () => {
			await wrapper.setProps({ rows: 4 });
			expect(wrapper.get("textarea").attributes("rows")).toBe("4");
		});
	});

	describe("Span-Placeholder", () => {
		test("wird nicht gerendert, wenn placeholder leer ist", () => {
			expect(wrapper.find(idPlaceholder).exists()).toBeFalsy();
		});

		test("wird gerendert und zeigt Text an", async () => {
			await wrapper.setProps({ placeholder: "Placeholder" });
			expect(wrapper.find(idPlaceholder).exists()).toBeTruthy();
			expect(wrapper.find(idPlaceholder).text()).toBe("Placeholder");
		});

		test("maxLen-Warnung wird nicht gerendert, wenn maxLen undefined ist", async () => {
			await wrapper.setProps({ placeholder: "Placeholder", modelValue: "Test" });
			expect(wrapper.findComponent({ name: "SvwsUiTextareaInput" }).props("maxLen")).toBeUndefined();
			expect(wrapper.find("span.inline-flex.gap-1").exists()).toBeFalsy();
		});

		test("maxLen-Warnung wird gerendert, wenn maxLen gesetzt und modelValue null ist", async () => {
			await wrapper.setProps({ placeholder: "Placeholder", maxLen: 12, modelValue: null });
			const span = wrapper.find("span.inline-flex.gap-1");
			expect(span.exists()).toBeTruthy();
			expect(span.text()).toBe("(max. 12 Zeichen)");
		});

		test("maxLen-Warnung mit aktueller Zeichenlänge wird gerendert, wenn maxLen gesetzt und modelValue nicht null", async () => {
			await wrapper.setProps({ placeholder: "Placeholder", maxLen: 12, modelValue: "Test" });
			const span = wrapper.find("span.inline-flex.gap-1");
			expect(span.exists()).toBeTruthy();
			expect(span.text()).toBe("(4/12 Zeichen)");
		});

		test("span-statistics wird nicht gerendert, wenn statistics false ist", async () => {
			await wrapper.setProps({ placeholder: "Placeholder" });
			expect(wrapper.find(idStatistics).exists()).toBeFalsy();
		});

		test("span-statistics wird gerendert, wenn statistics true ist", async () => {
			await wrapper.setProps({ placeholder: "Placeholder", statistics: true });
			expect(wrapper.find(idStatistics).exists()).toBeTruthy();
		});

		test("Tooltip wird korrekt gerendert, wenn statistics true ist", async () => {
			await wrapper.setProps({ placeholder: "Placeholder", statistics: true });
			const tooltip = wrapper.findComponent(SvwsUiTooltip);
			expect(tooltip.exists()).toBeTruthy();
			expect(tooltip.props("position")).toBe("right");
			expect(tooltip.find("span.icon.i-ri-bar-chart-2-line").classes()).toContain("textarea-input--statistic-icon");
		});
	});
});

// ─────────────────────────────────────────────────────────────────────────────
// Expose
// ─────────────────────────────────────────────────────────────────────────────

describe("defineExpose 'content'", () => {

	test("gibt modelValue zurück", async () => {
		await wrapper.setProps({ modelValue: "Exposed" });
		expect(wrapper.vm.content).toBe("Exposed");
	});

	test("gibt null zurück, wenn Feld leer ist", () => {
		expect(wrapper.vm.content).toBeNull();
	});
});

// ─────────────────────────────────────────────────────────────────────────────
// Events
// ─────────────────────────────────────────────────────────────────────────────

describe("Event-Tests", () => {

	test("onInput emittiert update:modelValue mit eingegebenem Wert", async () => {
		await wrapper.find("textarea").setValue("Test1-?");
		expect(wrapper.emitted("update:modelValue")?.at(0)?.at(0)).toBe("Test1-?");
	});

	test("onInput emittiert 'input' Event mit null bei leerem Feld", async () => {
		await wrapper.find("textarea").setValue("");
		expect(wrapper.emitted("input")?.at(0)?.at(0)).toBeNull();
	});

	test("onBlur emittiert blur mit null, wenn das Feld leer ist", async () => {
		const textarea = wrapper.find("textarea");
		await textarea.trigger("focus");
		await textarea.trigger("blur");
		expect(wrapper.emitted("blur")?.at(0)?.at(0)).toBeNull();
	});

	test("onBlur emittiert blur und kein change, wenn der Wert sich nicht geändert hat (leeres Feld)", async () => {
		const textarea = wrapper.find("textarea");
		await textarea.trigger("focus");
		await textarea.trigger("blur");
		expect(wrapper.emitted("blur")).toBeDefined();
		expect(wrapper.emitted("change")).toBeUndefined();
	});

	test("onBlur emittiert blur und change, wenn der Wert sich seit Fokus geändert hat", async () => {
		await wrapper.setProps({ modelValue: "Test+23" });
		const textarea = wrapper.find("textarea");
		await textarea.trigger("focus");
		textarea.element.value = "222";
		await textarea.trigger("input");
		await textarea.trigger("blur");
		expect(wrapper.emitted("blur")?.at(0)?.at(0)).toBe("222");
		expect(wrapper.emitted("change")?.at(0)?.at(0)).toBe("222");
	});
});

// ─────────────────────────────────────────────────────────────────────────────
// Validierung
// ─────────────────────────────────────────────────────────────────────────────

describe.concurrent("Validierung", () => {

	test("Mit Prop 'validation' wird eine Validierung von außen ausgeführt", () => {
		const w = mount(SvwsUiTextareaInput, { props: { modelValue: "Test", validation: () => getValidatorFehler() } });
		const result = w.findComponent({ name: "SvwsUiTextareaInput" }).vm.validationResult;
		expect(result.fehler.size()).toBe(1);
		expect(result.fehler.getFirst().getFehlermeldung()).toBe("Custom-Validierung fehlgeschlagen");
	});

	test("Klasse 'textarea-input--muss' wird gesetzt, wenn valid() false zurückgibt", () => {
		const w = mount(SvwsUiTextareaInput, { props: { modelValue: "Test", valid: () => false } });
		expect(w.find(".textarea-input--muss").exists()).toBe(true);
	});

	test.each([
		["textarea-input--muss", ValidatorFehlerart.MUSS],
		["textarea-input--kann", ValidatorFehlerart.KANN],
		["textarea-input--hinweis", ValidatorFehlerart.HINWEIS],
	])("Klasse '%s' wird gesetzt bei Fehlerart %s", (expectedClass, fehlerart) => {
		const w = mount(SvwsUiTextareaInput, {
			props: { modelValue: "Test", validation: () => getValidatorFehler(fehlerart) },
		});
		expect(w.find(`.${expectedClass}`).exists()).toBe(true);
	});

	test("Bei Validierungsfehlern wird ein Validation-Icon angezeigt", () => {
		const w = mount(SvwsUiTextareaInput, { props: { placeholder: "Titel", validation: () => getValidatorFehler() } });
		expect(w.find(".validation-tooltip-icon").exists()).toBeTruthy();
	});

	describe.concurrent("required", () => {

		test.each([
			[false, "ohne Prop 'validation'", undefined],
			[false, "mit Prop 'validation'", () => getValidatorFehler()],
			[true, "mit Prop 'validation'", () => getValidatorFehler()],
		])("required=%s, %s → kein Required-Validator", (required, _desc, validation) => {
			const w = mount(SvwsUiTextareaInput, { props: { required, validation } });
			expect(w.findComponent({ name: "SvwsUiTextareaInput" }).vm.validatorRequired).toBeNull();
		});

		test("required=true ohne 'validation' → Required-Validator wird gesetzt", () => {
			const w = mount(SvwsUiTextareaInput, { props: { modelValue: null, required: true } });
			const v = w.findComponent({ name: "SvwsUiTextareaInput" }).vm.validatorRequired;
			expect(v).not.toBeNull();
			expect(v).toBeInstanceOf(BasicValidator);
		});

		test.each([
			["''", ""],
			["null", null],
		])("required=true, Eingabe=%s → Required-Validierungsfehler", (_label, input) => {
			const w = mount(SvwsUiTextareaInput, { props: { modelValue: input, required: true } });
			const result = w.findComponent({ name: "SvwsUiTextareaInput" }).vm.validationResult;
			expect(result.fehler.size()).toBe(1);
			expect(result.fehler.getFirst().getFehlermeldung()).toBe("Bitte geben Sie einen Wert an.");
		});

		test("required=true mit Eingabe → keine Validierungsfehler", () => {
			const w = mount(SvwsUiTextareaInput, { props: { modelValue: "Test", required: true } });
			expect(w.findComponent({ name: "SvwsUiTextareaInput" }).vm.validationResult.fehler.size()).toBe(0);
		});
	});

	describe.concurrent("maxLen", () => {

		test.each([
			[undefined, "ohne Prop 'validation'", undefined],
			[undefined, "mit Prop 'validation'", () => getValidatorFehler()],
			[3, "mit Prop 'validation'", () => getValidatorFehler()],
		])("maxLen=%s, %s → kein Length-Validator", (maxLen, _desc, validation) => {
			const w = mount(SvwsUiTextareaInput, { props: { maxLen, validation } });
			expect(w.findComponent({ name: "SvwsUiTextareaInput" }).vm.validatorLength).toBeNull();
		});

		test("maxLen=3 ohne 'validation' → Length-Validator wird gesetzt", () => {
			const w = mount(SvwsUiTextareaInput, { props: { maxLen: 3 } });
			const v = w.findComponent({ name: "SvwsUiTextareaInput" }).vm.validatorLength;
			expect(v).not.toBeNull();
			expect(v).toBeInstanceOf(BasicValidator);
		});

		test.each([
			[undefined, "'Hallo'", "Hallo"],
			[3, "'Hal'", "Hal"],
			[3, "''", ""],
			[3, "null", null],
		])("maxLen=%s, Eingabe=%s → kein Length-Fehler", (maxLen, _label, modelValue) => {
			const w = mount(SvwsUiTextareaInput, { props: { maxLen, modelValue } });
			expect(w.findComponent({ name: "SvwsUiTextareaInput" }).vm.validationResult.fehler.size()).toBe(0);
		});

		test("maxLen=3, Eingabe='Hallo' → Length-Validierungsfehler", () => {
			const w = mount(SvwsUiTextareaInput, { props: { modelValue: "Hallo", maxLen: 3 } });
			const result = w.findComponent({ name: "SvwsUiTextareaInput" }).vm.validationResult;
			expect(result.fehler.size()).toBe(1);
			expect(result.fehler.getFirst().getFehlermeldung()).toBe("Der Wert darf maximal 3 Zeichen lang sein.");
		});
	});
});

// ─────────────────────────────────────────────────────────────────────────────
// Helpers
// ─────────────────────────────────────────────────────────────────────────────

function getValidatorFehler(haertegrad: ValidatorFehlerart = ValidatorFehlerart.MUSS): List<ValidatorFehler> {
	const customValidator = new CustomValidatorRequired(haertegrad);
	customValidator.run();
	return customValidator.getFehler();
}

class CustomValidatorRequired extends BasicValidator {

	constructor(haertegrad: ValidatorFehlerart) {
		super(haertegrad);
		this.run();
	}

	protected pruefe(): boolean {
		this.addFehler(0, "Custom-Validierung fehlgeschlagen");
		return false;
	}
}
