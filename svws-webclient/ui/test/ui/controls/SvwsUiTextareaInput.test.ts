import { beforeEach, expect, test, afterEach, describe } from "vitest";
import type { VueWrapper } from "@vue/test-utils";
import { mount } from "@vue/test-utils";
import SvwsUiTextareaInput from "../../../src/ui/controls/SvwsUiTextareaInput.vue";
import SvwsUiTooltip from "../../../src/ui/SvwsUiTooltip.vue";
import { ValidatorFehlerart } from "../../../../core/src/asd/validate/ValidatorFehlerart";
import type { ValidatorFehler } from "../../../../core/src/asd/validate/ValidatorFehler";
import type { List } from "../../../../core/src/java/util/List";
import { BasicValidator } from "../../../../core/src/asd/validate/BasicValidator";

type prop_names = "modelValue" | "disabled" | "valid" | "statistics" | "resizeable" | "span" | "required";

let wrapper: VueWrapper<InstanceType<typeof SvwsUiTextareaInput>>;
let idComponent: string;
let idPlaceholder: string;
let idStatistics: string;


beforeEach(() => {
	wrapper = mount(SvwsUiTextareaInput, {
		props: {
			modelValue: "",
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

test("Rendert HTML korrekt", async () => {
	expect(wrapper.find(idComponent).exists()).toBeTruthy();
	expect(wrapper.find("textarea").exists()).toBeTruthy();
});

// CSS-Props data, disabled, statisctics, resizeable-none, ,

describe.concurrent("Tests für die CSS-Props", () => {
	// [Propname, Klassen- oder Stylename, Beschreibung, Class(0)Style(1)]
	test.each([
		["modelValue", "textarea-input--filled", "Prop modelValue wird an CSS übergeben"],
		["disabled", "textarea-input--disabled", "Prop disabled wird an CSS übergeben"],
		["statistics", "textarea-input--statistics", "Prop statistics wird an CSS übergeben"],
		["resizeable", "textarea-input--resize-none", "Prop resizeable mit none wird an CSS übergeben"],
		["span", "col-span-full", "Prop span mit full wird an CSS übergeben"],
	])("%s(%s) => %s", async (x, y) => {
		// Testen der default-Werte
		if (["modelValue", "disabled", "statistics"].includes(x)) {
			expect(wrapper.props(x as prop_names)).toBeFalsy();
		} else if (x === "resizeable") {
			expect(wrapper.props("resizeable")).toBe("vertical");
			expect(wrapper.classes()).toContain("textarea-input--resize-vertical");
		} else {
			expect(wrapper.props("span")).toBeUndefined();
		}

		// Vorbereiten
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
			default:
				break;
		}
		expect(wrapper.find("label").classes()).toContain(y);
	});

	test("Prop resizeable mit horizontal wird an CSS übergeben", async () => {
		// Vorbereiten
		// Erneute Definiton des Wrappers, da es Probleme bei parallelen Tests gibt.
		const w = mount(SvwsUiTextareaInput);
		await w.setProps({ resizeable: "horizontal" });

		// Testen
		expect(w.find("label").classes()).toContain("textarea-input--resize-horizontal");
	});

	test("Prop resizeable mit both wird an CSS übergeben", async () => {
		// Vorbereiten
		await wrapper.setProps({ resizeable: "both" });

		// Testen
		expect(wrapper.find("label").classes()).toContain("textarea-input--resize-both");
	});

	test("Prop span mit grow wird an CSS übergeben", async () => {
		// Vorbereiten
		await wrapper.setProps({ span: "grow" });

		// Testen
		expect(wrapper.find("label").classes()).toContain("grow");
	});

	test("Prop required mit false wird an CSS übergeben", async () => {
		// Vorbereiten
		await wrapper.setProps({ placeholder: "Placeholder" });

		// Testen
		expect(wrapper.find(idPlaceholder).classes().includes("textarea-input--placeholder--required"));
	});

	test("Prop required mit true wird an CSS übergeben", async () => {
		// Vorbereiten
		await wrapper.setProps({ placeholder: "Placeholder", required: true });
		const requiredSpan = wrapper.find(`${idPlaceholder} span.i-ri-asterisk`);

		// Testen
		expect(requiredSpan.exists()).toBeTruthy();
		expect(requiredSpan.classes()).toContain("textarea-input--placeholder--required");
	});
});

describe("Bedingtes Rendern der HTML-Elemenete", () => {
	describe("Textarea->tests", () => {
		test("rendert Textarea mit modelValue richtig, wenn modelValue null ist.", async () => {
			// Vorbereiten
			await wrapper.setProps({ modelValue: null });

			// Testen
			expect(wrapper.get("textarea").element.value.length).toBe(0);
		});

		test("rendert Textarea mit modelValue richtig, wenn modelValue gleich '' ist.", async () => {
			// Vorbereiten
			await wrapper.setProps({ modelValue: "" });

			// Testen
			expect(wrapper.get("textarea").element.value.length).toBe(0);
		});

		test("rendert Textarea mit modelValue richtig, wenn modelValue definiert ist.", async () => {
			// Vorbereiten
			await wrapper.setProps({ modelValue: "Test" });

			// Testen
			expect(wrapper.get("textarea").element.value).toBe("Test");
		});

		test("rendert Textarea mit prop required richtig, wenn required false ist.", async () => {
			// Testen
			expect(wrapper.get("textarea").attributes("required")).toBeUndefined();
		});

		test("rendert Textarea mit prop required richtig, wenn required true ist.", async () => {
			// Vorbereiten
			await wrapper.setProps({ required: true });
			// Testen
			expect(wrapper.get("textarea").attributes("required")).toBe("");
		});

		test("rendert Textarea mit prop disabled richtig, wenn disabled false ist.", async () => {
			// Testen
			expect(wrapper.get("textarea").attributes("disabled")).toBeUndefined();
		});

		test("rendert Textarea mit prop disabled richtig, wenn disabled true ist.", async () => {
			// Vorbereiten
			await wrapper.setProps({ disabled: true });
			// Testen
			expect(wrapper.get("textarea").attributes("disabled")).toBe("");
		});

		test("rendert Textarea mit prop rows(defaultwert) richtig.", async () => {
			// Testen
			expect(wrapper.get("textarea").attributes("rows")).toBe("3");
		});

		test("rendert Textarea mit prop rows richtig, wenn rows geändert wird", async () => {
			// Vorbereiten
			await wrapper.setProps({ rows: 4 });
			// Testen
			expect(wrapper.get("textarea").attributes("rows")).toBe("4");
		});
	});

	describe("Span-Placeholder->tests", () => {
		test("Html-Element span wird nicht gerendert, wenn placeholder nicht gesetzt ist", () => {
			// Testen
			expect(wrapper.find(idPlaceholder).exists()).toBeFalsy();
		});

		test("Html-Element span wird gerendert, wenn placeholder gesetzt ist", async () => {
			// Vorbereiten
			await wrapper.setProps({ placeholder: "Placeholder" });

			// Testen
			expect(wrapper.find(idPlaceholder).exists()).toBeTruthy();
		});

		test("Html-Element span wird gerendert, wenn placeholder gesetzt ist", async () => {
			// Vorbereiten
			await wrapper.setProps({ placeholder: "Placeholder" });

			// Testen
			expect(wrapper.find(idPlaceholder).exists()).toBeTruthy();
			expect(wrapper.find(idPlaceholder).text()).toBe("Placeholder");
		});

		test("Bei Validierungsfehlern wird ein Validation-Icon angezeigt", async () => {
			// Vorbereiten
			await wrapper.setProps({ required: true, modelValue: "", placeholder: "Placeholder" });

			// Testen
			expect(wrapper.find(".validation-tooltip-icon").exists()).toBeTruthy();
		});

		test("Bei keinen Validierungsfehlern wird kein Validation-Icon angezeigt", async () => {
			// Vorbereiten
			await wrapper.setProps({ required: true, modelValue: "Test", placeholder: "Placeholder" });

			// Testen
			expect(wrapper.find(".validation-tooltip-icon").exists()).toBeFalsy();
		});

		test("Warnung für maxLen wird nicht gerendert, wenn maxLen undefiniert ist", async () => {
			// Vorbereiten
			await wrapper.setProps({ placeholder: "Placeholder" });
			expect(await wrapper.findComponent({ name: "SvwsUiTextareaInput" }).props('maxLen')).toBeUndefined();
			// Testen
			expect(wrapper.find("span.inline-flex.ml-1.gap-1").exists()).toBeFalsy();
		});

		test("Warnung für maxLen wird gerendert, wenn maxLen definiert ist", async () => {
			// Vorbereiten
			await wrapper.setProps({ placeholder: "Placeholder", maxLen: 12 });
			expect(await wrapper.findComponent({ name: "SvwsUiTextareaInput" }).props('maxLen')).toBeDefined();
			// Testen
			expect(wrapper.find("span.inline-flex.gap-1").exists()).toBeTruthy();
		});

		// TODO - Tests für den angezeigten Text der Warnung

		test("span-statistics wird nicht gerendert, wenn statistics false ist", async () => {
			// Vorbereiten
			await wrapper.setProps({ placeholder: "Placeholder" });
			expect(await wrapper.findComponent({ name: "SvwsUiTextareaInput" }).props('statisctics')).toBeFalsy();

			// Testen
			expect(wrapper.find(idStatistics).exists()).toBeFalsy();
		});

		test("span-statistics wird gerendert, wenn statistics true ist", async () => {
			// Vorbereiten
			await wrapper.setProps({ placeholder: "Placeholder", statistics: true });
			expect(await wrapper.findComponent({ name: "SvwsUiTextareaInput" }).props('statistics')).toBeTruthy();

			// Testen
			expect(wrapper.find(idStatistics).exists()).toBeTruthy();
		});

		test("Komponente tooltip wird gerendert, wenn statistics true ist", async () => {
			// Vorbereiten
			await wrapper.setProps({ placeholder: "Placeholder", statistics: true });
			const tooltip = wrapper.findComponent(SvwsUiTooltip);

			// Testen
			expect(tooltip.exists()).toBeTruthy();
			expect(tooltip.props('position')).toBe("right");
			const statistic_icon = tooltip.find("span.icon.i-ri-bar-chart-2-line");
			expect(statistic_icon.classes()).toContain("textarea-input--statistic-icon");
			// TODO #content-Slot testen ?????
		});
	});
});

describe("computed/functions tests", () => {
	test("computed->dataOrEmpty->get liefert '', wenn data null ist", async () => {
		// Vorereiten
		await wrapper.setProps({ modelValue: null });
		expect(await wrapper.findComponent({ name: "SvwsUiTextareaInput" }).vm.data).toBeNull();

		// Testen
		expect(await wrapper.findComponent({ name: "SvwsUiTextareaInput" }).vm.dataOrEmpty).toBe("");
	});

	test("computed->dataOrEmpty->get liefert den Wert, wenn data nicht null ist", async () => {
		// Vorereiten
		await wrapper.setProps({ modelValue: "Test" });
		expect(await wrapper.findComponent({ name: "SvwsUiTextareaInput" }).vm.data).toBeDefined();

		// Testen
		expect(await wrapper.findComponent({ name: "SvwsUiTextareaInput" }).vm.dataOrEmpty).toBe("Test");
	});

	test("computed->dataOrEmpty->set setzt den Wert von dataOrEmpty auf null, wenn value leer ist", async () => {
		// Vorereiten
		await wrapper.setProps({ modelValue: "" });

		// Aktion
		wrapper.findComponent({ name: "SvwsUiTextareaInput" }).vm.dataOrEmpty = "";

		// Testen
		expect(await wrapper.findComponent({ name: "SvwsUiTextareaInput" }).vm.dataOrEmpty).toBe("");
	});

	test("computed->dataOrEmpty->set setzt den Wert von dataOrEmpty , wenn value nicht leer ist", async () => {
		// Vorereiten
		await wrapper.setProps({ modelValue: "Test" });

		// Aktion
		wrapper.findComponent({ name: "SvwsUiTextareaInput" }).vm.dataOrEmpty = "Test2";
		// Testen
		expect(await wrapper.findComponent({ name: "SvwsUiTextareaInput" }).vm.dataOrEmpty).toBe("Test2");
	});

	test("function->updateData aktualisiert data und modelValue nicht, wenn der übergebene Wert gleich ist", async () => {
		// Vorbereiten
		await wrapper.setProps({ modelValue: "Test" });
		expect(await wrapper.findComponent({ name: "SvwsUiTextareaInput" }).vm.data).toBe("Test");

		// Aktion
		wrapper.findComponent({ name: "SvwsUiTextareaInput" }).vm.updateData("Test");

		// Testen
		// Länge ist deswegen 1, weil durch watch updateData bereits bei der Konstruktion einmal aufgerufen wird
		// Jedoch wird die Methode beim nächsten Aufruf mit dem gleichen Wert nicht aufgerufen.
		expect(wrapper.emitted("update:modelValue")).toHaveLength(1);
	});

	test("function->updateData aktualisiert data und modelValue(mit emit) mit dem übergebenen Wert", async () => {
		// Vorbereiten
		await wrapper.setProps({ modelValue: "Test" });
		expect(await wrapper.findComponent({ name: "SvwsUiTextareaInput" }).vm.data).toBe("Test");

		// Aktion
		wrapper.findComponent({ name: "SvwsUiTextareaInput" }).vm.updateData("Test2");
		await wrapper.vm.$nextTick();
		// Testen
		expect(await wrapper.findComponent({ name: "SvwsUiTextareaInput" }).vm.data).toBe("Test2");
		expect(wrapper.emitted("update:modelValue")?.at(0)?.at(0)).toBe("Test");
		expect(wrapper.emitted("update:modelValue")?.at(1)?.at(0)).toBe("Test2");
	});

	test("functions->onInput aktualisiert den data-Wert beim Eintippen in textarea.", async () => {
		// Vorbereiten
		const textarea = wrapper.find("textarea");

		// Aktion
		await textarea.setValue("Test1-?");

		// Testen
		expect(wrapper.findComponent({ name: "SvwsUiTextareaInput" }).vm.data).toBe("Test1-?");
	});

	test("functions->onBlur emittiert nur blur-Event, weil props-modelValue sich von data nicht unterscheidet. modelValue ist leer.", async () => {
		// Vorbereiten
		const textarea = wrapper.find("textarea");
		expect(wrapper.props("modelValue")).toBe(wrapper.findComponent({ name: "SvwsUiTextareaInput" }).vm.data);

		// Aktion
		await textarea.trigger("blur");

		// Testen
		expect(wrapper.emitted("unchanged")).toBeUndefined();
		expect(wrapper.emitted("blur")).toBeDefined();
		expect(wrapper.emitted("blur")?.at(0)?.at(0)).toBe('');
	});

	test("functions->onBlur emittiert nur blue-Event, weil props-modelValue sich von data nicht unterscheidet. modelValue ist beleget.", async () => {
		// Vorbereiten
		await wrapper.setProps({ modelValue: "Test+23" });
		const textarea = wrapper.find("textarea");
		expect(wrapper.props("modelValue")).toBe(wrapper.findComponent({ name: "SvwsUiTextareaInput" }).vm.data);
		await wrapper.vm.$nextTick();

		// Aktion
		await textarea.trigger("blur");

		// Testen
		expect(wrapper.emitted("unchanged")).toBeUndefined();
		expect(wrapper.emitted("blur")).toBeDefined();
		expect(wrapper.emitted("blur")?.at(0)?.at(0)).toBe("Test+23");
	});

	test("functions->onBlur emittiert change- und blur-Event, weil props-modelValue sich von data unterscheidet.", async () => {
		// Vorbereiten
		await wrapper.setProps({ modelValue: "Test+23" });
		const textarea = wrapper.find("textarea");
		textarea.element.value = "222";

		// Aktion
		await wrapper.get("textarea").trigger("input");
		await wrapper.get("textarea").trigger("blur");

		// Testen
		expect(wrapper.emitted("change")).toBeDefined();
		expect(wrapper.emitted("blur")).toBeDefined();
		expect(wrapper.emitted("blur")?.at(0)?.at(0)).toBe("222");
		expect(wrapper.emitted("change")?.at(0)?.at(0)).toBe("222");
	});

	test("functions->onKeyTab emittiert kein change-Event, weil props-modelValue sich von data nicht unterscheidet ", async () => {
		// Vorbereiten
		await wrapper.setProps({ modelValue: "Test+23" });
		const textarea = wrapper.find("textarea");
		textarea.element.value = "Test+23";

		// Aktion
		await textarea.trigger("input");
		await textarea.trigger("keydown.enter");

		// Testen
		expect(wrapper.emitted("change")).toBeUndefined();
	});

});

test('sollte die textarea-Referenz korrekt setzen', async () => {
	// Zugriff auf die Referenz über $refs
	const textareaRef = wrapper.vm.$refs.textarea;

	// Überprüfen, ob die Referenz existiert
	expect(textareaRef).not.toBeNull();
	expect((textareaRef as HTMLElement).tagName).toBe("TEXTAREA");

	// Überprüfe, ob das HTML-Element mit ref richtig verknüpfpt ist
	const textareaElement = textareaRef as HTMLTextAreaElement;
	expect(textareaElement.classList).toContain("textarea-input--control");
});

describe.concurrent("Validierung", () => {

	test("Mit Prop 'validation' wird eine Validierung von außen ausgeführt", () => {
		const wrapper = mount(SvwsUiTextareaInput, { props: { modelValue: "Test", validation: () => getValidatorFehler() } });
		const validatorResult = wrapper.findComponent({ name: "SvwsUiTextareaInput" }).vm.validationResult;

		expect(validatorResult.fehler.size()).toBe(1);
		expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Custom-Validierung fehlgeschlagen");
	});

	test.each([
		["textarea-input--muss", ValidatorFehlerart.MUSS],
		["textarea-input--kann", ValidatorFehlerart.KANN],
		["textarea-input--hinweis", ValidatorFehlerart.HINWEIS],
	])(
		"Wird mit der Klasse '%s' wiedergegeben, wenn Validierungsfehler vom Härtegrad %s vorhanden sind",
		(expectedClass, fehlerart) => {
			const wrapper = mount(SvwsUiTextareaInput, {
				props: { modelValue: "Test", validation: () => getValidatorFehler(fehlerart) },
			});
			expect(wrapper.find(`.${expectedClass}`).exists()).toBe(true);
		}
	);

	test("Bei Validierungsfehlern wird ein Validation-Icon angezeigt", () => {
		const wrapper = mount(SvwsUiTextareaInput, { props: { placeholder: "Titel", validation: () => getValidatorFehler() } });
		expect(wrapper.find(".validation-tooltip-icon").exists()).toBeTruthy();
	});

	describe.concurrent("required", () => {

		test("Mit Prop 'required = false' und ohne Prop 'validation' wird kein Required-Validator hinzugefügt", () => {
			const wrapper = mount(SvwsUiTextareaInput, { props: { modelValue: null, required: false } });
			const validatorRequired = wrapper.findComponent({ name: "SvwsUiTextareaInput" }).vm.validatorRequired;

			expect(validatorRequired).toBeNull();
		});

		test("Mit Prop 'required = false' und Prop 'validation' wird kein Required-Validator hinzugefügt", () => {
			const wrapper = mount(SvwsUiTextareaInput, {
				props: { modelValue: null, required: false, validation: () => getValidatorFehler() },
			});
			const validatorRequired = wrapper.findComponent({ name: "SvwsUiTextareaInput" }).vm.validatorRequired;

			expect(validatorRequired).toBeNull();
		});

		test("Mit Prop 'required = true' und Prop 'validation' wird kein Required-Validator hinzugefügt", () => {
			const wrapper = mount(SvwsUiTextareaInput, {
				props: { modelValue: null, required: true, validation: () => getValidatorFehler() },
			});
			const validatorRequired = wrapper.findComponent({ name: "SvwsUiTextareaInput" }).vm.validatorRequired;

			expect(validatorRequired).toBeNull();
		});

		test("Mit Prop 'required = true' und ohne Prop 'validation' wird ein Required-Validator hinzugefügt", () => {
			const wrapper = mount(SvwsUiTextareaInput, { props: { modelValue: null, required: true } });
			const validatorRequired = wrapper.findComponent({ name: "SvwsUiTextareaInput" }).vm.validatorRequired;

			expect(validatorRequired).not.toBeNull();
			expect(validatorRequired).toBeInstanceOf(BasicValidator);
		});

		test("Mit Prop 'required = true' ohne Eingabe wird ein Fehler für die Required-Validierung generiert", () => {
			const wrapper = mount(SvwsUiTextareaInput, { props: { modelValue: null, required: true } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextareaInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(1);
			expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Bitte geben Sie einen Wert an.");
		});

		test("Mit Prop 'required = true' mit Eingabe ergibt die Validierung keine Fehler", () => {
			const wrapper = mount(SvwsUiTextareaInput, { props: { modelValue: "Test", required: true } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextareaInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});
	});
	describe.concurrent("maxLen", () => {

		test("Mit Prop 'maxLen = undefined' mit Eingabe = 'Test' und ohne Prop 'validation' wird kein Length-Validator hinzugefügt", () => {
			const wrapper = mount(SvwsUiTextareaInput, { props: { modelValue: "Test", maxLen: undefined } });
			const validatorLength = wrapper.findComponent({ name: "SvwsUiTextareaInput" }).vm.validatorLength;

			expect(validatorLength).toBeNull();
		});

		test("Mit Prop 'maxLen = undefined' mit Eingabe = 'Test' und Prop 'validation' wird kein Length-Validator hinzugefügt", () => {
			const wrapper = mount(SvwsUiTextareaInput, {
				props: { modelValue: "Test", maxLen: undefined, validation: () => getValidatorFehler() },
			});
			const validatorLength = wrapper.findComponent({ name: "SvwsUiTextareaInput" }).vm.validatorLength;

			expect(validatorLength).toBeNull();
		});

		test("Mit Prop 'maxLen = 3' mit Eingabe = 'Test' und Prop 'validation' wird kein Length-Validator hinzugefügt", () => {
			const wrapper = mount(SvwsUiTextareaInput, {
				props: { modelValue: "Test", maxLen: 3, validation: () => getValidatorFehler() },
			});
			const validatorLength = wrapper.findComponent({ name: "SvwsUiTextareaInput" }).vm.validatorLength;

			expect(validatorLength).toBeNull();
		});

		test("Mit Prop 'maxLen = 3' mit Eingabe = 'Test' und ohne Prop 'validation' wird ein Length-Validator hinzugefügt", () => {
			const wrapper = mount(SvwsUiTextareaInput, { props: { modelValue: "Test", maxLen: 3 } });
			const validatorLength = wrapper.findComponent({ name: "SvwsUiTextareaInput" }).vm.validatorLength;

			expect(validatorLength).not.toBeNull();
		});

		test("Mit Prop 'maxLen = 3' mit Eingabe = 'Test' wird ein Fehler für die maxLen-Validierung generiert", () => {
			const wrapper = mount(SvwsUiTextareaInput, { props: { modelValue: "Test", maxLen: 3 } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextareaInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(1);
			expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Der Wert darf maximal 3 Zeichen lang sein.");
		});

		test("Mit Prop 'maxLen = 8' mit Eingabe = 'Test' wird kein Fehler für die maxLen-Validierung generiert", () => {
			const wrapper = mount(SvwsUiTextareaInput, { props: { modelValue: "Test", maxLen: 8 } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextareaInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});
	});
});

afterEach(() => {
	wrapper.unmount();
	document.body.innerHTML = "";
});

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
