import { describe, test, expect } from "vitest";
import { mount } from "@vue/test-utils";

import SvwsUiTextInput from "../../../src/ui/controls/SvwsUiTextInput.vue";
import { BasicValidator } from "../../../../core/src/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "../../../../core/src/asd/validate/ValidatorFehlerart";
import type { List } from "../../../../core/src/java/util/List";
import type { ValidatorFehler } from "../../../../core/src/asd/validate/ValidatorFehler";

describe("Komponente kann gemounted werden", () => {
	test("HTML wird erzeugt", () => {
		const wrapper = mount(SvwsUiTextInput);
		expect(wrapper.html()).includes("text-input-component");
	});
});

describe.concurrent("PropHandhabung läuft korrekt", () => {
	test.each([
		["text", 'type="text"', "Type Prop as text"],
		["date", 'type="date"', "Type Prop as date"],
		["email", 'type="email"', "Type Prop as email"],
		["search", 'type="search"', "Type Prop as search"],
		["tel", 'type="tel"', "Type Prop as tel"],
		["password", 'type="password"', "Type Prop as password"],
		["text", 'type="text"', "Type Prop as number"],
		["search", "search-icon", "Typ Prop als Suche rendert Icons"],
		["search", '<span class="icon', "Typ Prop als Suche rendert Icons Klasse"],
		["date", '<span class="svws-icon icon i-ri-calendar-2-line">', "Typ Prop als Datum zeigt das Datumssymbol an"],
	])('Mit Props type="%s" HTML enthält "%s" | %s ', async (x, y, _) => {
		const props = { type: x as "text" | "date" | "email" | "search" | "tel" | "password" };

		const wrapper = mount(SvwsUiTextInput, { props: props });

		// trigger Update-Logik welche für manche tests benötigt werden
		await wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.$nextTick();

		expect(wrapper.html()).includes(y);
	});

	test('Mit Prop disabled ist der input disabled', () => {
		const wrapper = mount(SvwsUiTextInput, { props: { disabled: true } });
		expect(wrapper.find('input').attributes('disabled')).toBe("");
	});

	test('Mit Prop disabled = false ist der input nicht disabled', () => {
		const wrapper = mount(SvwsUiTextInput, { props: { disabled: false } });
		expect(wrapper.find('input').html()).not.includes('disabled');
	});

	test('Prop required wird an das Input Feld übergeben', () => {
		const wrapper = mount(SvwsUiTextInput, { props: { required: true } });
		expect(wrapper.find('input').attributes('required')).toBe("");
	});

	test('Mit Prop required = false ist der input nicht required', () => {
		const wrapper = mount(SvwsUiTextInput, { props: { required: false } });
		expect(wrapper.find('input').html()).not.includes('required');
	});

	test('Prop readonly wird an das Input Feld übergeben, sofern es sich um ein Input-Feld für ein Select handelt', () => {
		const wrapper = mount(SvwsUiTextInput, { props: { readonly: true, isSelectInput: true } });
		expect(wrapper.find('input').attributes('readonly')).toBe("");
	});

	test("Wird mit der Klasse 'text-input--readonly' wiedergegeben, wenn readonly true ist", () => {
		const wrapper = mount(SvwsUiTextInput, { props: { readonly: true } });
		expect(wrapper.find(".text-input--readonly").exists()).toBe(true);
	});

	test('Mit Prop readonly = false ist der input nicht readonly', () => {
		const wrapper = mount(SvwsUiTextInput, { props: { required: false } });
		expect(wrapper.find('input').html()).not.includes('readonly');
	});

	test('Prop headless rendert die Komponente im headless mode', () => {
		const wrapper = mount(SvwsUiTextInput, { props: { headless: true } });
		expect(wrapper.find('.text-input--headless').exists()).toBe(true);
	});

	test('Prop focus aktiviert den Fokus des Objekts', async () => {
		const wrapper = mount(SvwsUiTextInput, { props: { focus: true }, attachTo: document.body });
		await wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.$nextTick();
		expect(wrapper.find('input').element).toBe(document.activeElement);
	});

	test('Prop rounded-sm wird an CSS übergeben', () => {
		const wrapper = mount(SvwsUiTextInput, { props: { rounded: true } });
		expect(wrapper.find('.text-input--rounded').exists()).toBe(true);
	});

	test('Prop url wird richtig angezeigt', () => {
		const wrapper = mount(SvwsUiTextInput, { props: { url: true } });
		expect(wrapper.find('.text-input--prefix').exists()).toBe(true);
	});

	test('Prop minLen wird angezeigt', async () => {
		const wrapper = mount(SvwsUiTextInput, { props: { minLen: 3, placeholder: " " } });
		await wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.$nextTick();
		expect(wrapper.find('.text-input--placeholder').text()).toContain('mind. 3 Zeichen');
	});

	test('Prop maxLen wird angezeigt', async () => {
		const wrapper = mount(SvwsUiTextInput, { props: { maxLen: 10, placeholder: " " } });
		await wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.$nextTick();
		expect(wrapper.find('.text-input--placeholder').text()).toContain('max. 10 Zeichen');
	});

	test('Prop minLen und maxLen wird angezeigt', async () => {
		const wrapper = mount(SvwsUiTextInput, { props: { minLen: 3, maxLen: 10, placeholder: " " } });
		await wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.$nextTick();
		expect(wrapper.find('.text-input--placeholder').text()).toContain('zwischen 3 und 10 Zeichen');
	});

	type spanType = "full" | "2" | undefined;

	test.each([
		['full', 'innerspan="full"'],
		['2', 'innerspan="2"'],
	])('Prop span="%s" wird ab CSS übergeben', (span, className) => {
		const innerSpan: spanType = span as spanType;
		const wrapper = mount(SvwsUiTextInput, { props: { innerSpan } });
		expect(wrapper.html()).includes(className);
	});

	test('Prop removable wird an das Input Feld übergeben', () => {
		const wrapper = mount(SvwsUiTextInput, { props: { removable: true, type: 'date' } });
		expect(wrapper.find('.svws-icon--remove').exists()).toBe(true);
	});
});




describe.concurrent("Modelvalue (prop), two-way-binding und Aktualisierungs- und Emitlogik", () => {
	test("Daten Prop werden wiedergegeben", () => {
		const props = { modelValue: "Lorem123" };

		const wrapper = mount(SvwsUiTextInput, { props: props });

		expect(wrapper.html()).includes("Lorem123");
	});

	test("Updating data Prop will be rerendered", async () => {
		const props = { modelValue: "Lorem123" };

		const wrapper = mount(SvwsUiTextInput, { props: props });
		await wrapper.setProps({ modelValue: "Lorem321" });

		expect(wrapper.html()).includes("Lorem321");
	});

	test("Aktualisierung der Daten Prop wird emit updateData ausgelöst", async () => {
		const props = { modelValue: "Lorem123" };

		const wrapper = mount(SvwsUiTextInput, { props: props });

		await wrapper.setProps({ modelValue: "Lorem321" });

		const emittedEvents = wrapper.emitted("update:modelValue");

		expect(emittedEvents).toBeTruthy(); // Überprüft, ob Events emittiert wurden
		if ((emittedEvents !== undefined) && (emittedEvents[0].length > 0)) {
			expect(emittedEvents[0][0]).toEqual("Lorem321");
		} else {
			throw new Error("Keine Ereignisse ausgesendet");
		}
	});

	test("Die Aktualisierung des Eingabewerts für das v-model führt zu einem emit von updateData mit dem neuen Wert", async () => {
		const props = { modelValue: "Lorem123" };

		const wrapper = mount(SvwsUiTextInput, { props: props });

		const input = wrapper.find("input");
		input.element.value = "Lorem321";
		await input.trigger("input");

		const emittedEvents = wrapper.emitted("update:modelValue");

		expect(emittedEvents).toBeTruthy(); // Überprüft, ob Events emittiert wurden
		if ((emittedEvents !== undefined) && (emittedEvents[0].length > 0)) {
			expect(emittedEvents[0][0]).toEqual("Lorem321");
		} else {
			throw new Error("No events emitted");
		}
	});

	test("Die Aktualisierung eines Eingabewerts mit demselben Wert führt nicht zur Ausgabe von updateData", async () => {
		const props = { modelValue: "Lorem123" };

		const wrapper = mount(SvwsUiTextInput, { props: props });

		const input = wrapper.find("input");
		input.element.value = "Lorem123";
		await input.trigger("input");

		const emittedEvents = wrapper.emitted("update:modelValue");
		expect(emittedEvents).toBeUndefined();
	});
});


describe.concurrent("Unit Test für computed validierungFehler", () => {

	test.todo("Benutzerdefinierte Validierung wird nicht akzeptiert", () => {
		// Mithilfe eines benutzerdefinierten Validators testen
		const props = {
			// alte Implementierung: valid: (value: string | null) => value === "valid",
			modelValue: "invalid",
		};
		const wrapper = mount(SvwsUiTextInput, { props: props });

		const validierungFehler = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validierungFehler;
		expect(validierungFehler.size()).toEqual(1);
	});

	test.todo("Benutzerdefinierte Validierung gibt true zurück", () => {
		// Mithilfe eines benutzerdefinierten Validators testen
		const props = {
			// alte Implementierung: valid: (value: string | null) => value === "valid",
			modelValue: "valid",
		};
		const wrapper = mount(SvwsUiTextInput, { props: props });

		const validierungFehler = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validierungFehler;
		expect(validierungFehler.size()).toEqual(0);
	});
});

describe.concurrent("Validierung", () => {
	test("Mit Prop 'validation' wird eine Validierung von außen ausgeführt", () => {
		const wrapper = mount(SvwsUiTextInput, { props: { modelValue: "Text", validation: () => getValidatorFehler() } });
		const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

		expect(validatorResult.fehler.size()).toBe(1);
		expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Custom-Validierung fehlgeschlagen");
	});

	describe.concurrent("required", () => {

		test("Mit Prop 'required = false' wird kein Required-Validator hinzugefügt", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { modelValue: null, required: false } });
			const validatorRequired = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validatorRequired;

			expect(validatorRequired).toBeNull();
		});

		test("Mit Prop 'required = false' wird keine Validierung für 'required' ausgeführt", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { modelValue: null, required: false } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'required = true' wird ein Required-Validator hinzugefügt", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { modelValue: null, required: true } });
			const validatorRequired = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validatorRequired;

			expect(validatorRequired).not.toBeNull();
			expect(validatorRequired).toBeInstanceOf(BasicValidator);
		});

		test("Mit Prop 'required = true' und Eingabe = '' wird ein Fehler für die Required-Validierung generiert", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { modelValue: "", required: true } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(1);
			expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Bitte geben Sie einen Wert an.");
		});

		test("Mit Prop 'required = true' und Eingabe = null wird ein Fehler für die Required-Validierung generiert", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { modelValue: null, required: true } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(1);
			expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Bitte geben Sie einen Wert an.");
		});

		test("Mit Prop 'required = true' mit Eingabe ergibt die Validierung keine Fehler", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { modelValue: "Test", required: true } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'required = true' und 'validation' wird eine Validierung von außen ausgeführt und um die Required-Validierung ergänzt", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { modelValue: null, validation: () => getValidatorFehler(), required: true } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(2);
			expect(validatorResult.fehler.get(0).getFehlermeldung()).toBe("Bitte geben Sie einen Wert an.");
			expect(validatorResult.fehler.get(1).getFehlermeldung()).toBe("Custom-Validierung fehlgeschlagen");
		});

		test("Mit Prop 'required = true' und 'skipDefaultValidation = true' wird keine Validierung für 'required' ausgeführt", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { modelValue: null, required: true, skipDefaultValidation: true } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'required = true' und 'skipDefaultValidation = { required: true }' wird keine Validierung für 'required' ausgeführt", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { modelValue: null, required: true, skipDefaultValidation: { required: true } } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'required = true' und 'skipDefaultValidation = { required: false }' wird eine Validierung für 'required' ausgeführt", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { modelValue: null, required: true, skipDefaultValidation: { required: false } } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(1);
			expect(validatorResult.fehler.get(0).getFehlermeldung()).toBe("Bitte geben Sie einen Wert an.");
		});

		test("Mit Prop 'required = true' und 'skipDefaultValidation = { required: true }' wird keine Validierung für 'required' ausgeführt", () => {
			const wrapper = mount(SvwsUiTextInput, {
				props: { modelValue: null, required: true, skipDefaultValidation: { required: true } },
			});
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});
	});

	describe.concurrent("minLen", () => {
		test("Mit Prop 'minLen = undefined' wird kein length-Validator hinzugefügt", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { minLen: undefined } });
			const validatorLength = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validatorLength;

			expect(validatorLength).toBeNull();
		});

		test("Mit Prop 'minLen = undefined' und Eingabe 'Ha' wird keine Validierung für 'length' ausgeführt", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { modelValue: "Ha", minLen: undefined } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'minLen = 3' wird ein length-Validator hinzugefügt", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { modelValue: "Ha", minLen: 3 } });
			const validatorLength = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validatorLength;

			expect(validatorLength).not.toBeNull();
			expect(validatorLength).toBeInstanceOf(BasicValidator);
		});

		test("Mit Prop 'minLen = 3' und Eingabe 'Ha' wird ein Fehler für die length-Validierung generiert", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { modelValue: "Ha", minLen: 3 } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(1);
			expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Der Wert muss mindestens 3 Zeichen lang sein.");
		});

		test("Mit Prop 'minLen = 3' und Eingabe 'Hal' ergibt die Validierung keine Fehler", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { modelValue: "Hal", minLen: 3 } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'minLen = 3' ohne Eingabe ergibt die Validierung keine Fehler", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { modelValue: null, minLen: 3 } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.isEmpty()).toBe(true);
		});

		test("Mit Prop 'minLen = 3' und 'validation' wird eine Validierung von außen ausgeführt und um die length-Validierung ergänzt", () => {
			const wrapper = mount(SvwsUiTextInput, {
				props: { modelValue: "Ha", validation: () => getValidatorFehler(), minLen: 3 },
			});
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(2);
			expect(validatorResult.fehler.get(0).getFehlermeldung()).toBe("Der Wert muss mindestens 3 Zeichen lang sein.");
			expect(validatorResult.fehler.get(1).getFehlermeldung()).toBe("Custom-Validierung fehlgeschlagen");
		});

		test("Mit Prop 'minLen = 3' und 'skipDefaultValidation = true' wird keine Validierung für 'length' ausgeführt", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { modelValue: "Ha", minLen: 3, skipDefaultValidation: true } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'minLen = 3' und 'skipDefaultValidation = { length: true }' wird keine Validierung für 'length' ausgeführt", () => {
			const wrapper = mount(SvwsUiTextInput, {
				props: { modelValue: "Ha", minLen: 3, skipDefaultValidation: { length: true } },
			});
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'minLen = 3' und 'skipDefaultValidation = { length: false }' wird eine Validierung für 'length' ausgeführt", () => {
			const wrapper = mount(SvwsUiTextInput, {
				props: { modelValue: "Ha", minLen: 3, skipDefaultValidation: { length: false } },
			});
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(1);
			expect(validatorResult.fehler.get(0).getFehlermeldung()).toBe("Der Wert muss mindestens 3 Zeichen lang sein.");
		});

		test("Mit Prop 'minLen = 3' und 'skipDefaultValidation = { length: true }' wird keine Validierung für 'length' ausgeführt", () => {
			const wrapper = mount(SvwsUiTextInput, {
				props: { modelValue: "Ha", minLen: 3, skipDefaultValidation: { length: true } },
			});
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});
	});

	describe.concurrent("maxLen", () => {
		test("Mit Prop 'maxLen = undefined' wird kein length-Validator hinzugefügt", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { maxLen: undefined } });
			const validatorLength = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validatorLength;

			expect(validatorLength).toBeNull();
		});

		test("Mit Prop 'maxLen = undefined' und Eingabe 'Hallo' wird keine Validierung für 'length' ausgeführt", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { modelValue: "Hallo", maxLen: undefined } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'maxLen = 3' wird ein length-Validator hinzugefügt", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { modelValue: "Hallo", maxLen: 3 } });
			const validatorLength = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validatorLength;

			expect(validatorLength).not.toBeNull();
			expect(validatorLength).toBeInstanceOf(BasicValidator);
		});

		test("Mit Prop 'maxLen = 3' und Eingabe 'Hallo' wird ein Fehler für die length-Validierung generiert", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { modelValue: "Hallo", maxLen: 3 } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(1);
			expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Der Wert darf maximal 3 Zeichen lang sein.");
		});

		test("Mit Prop 'maxLen = 3' und Eingabe 'Hal' ergibt die Validierung keine Fehler", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { modelValue: "Hal", maxLen: 3 } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'maxLen = 3' ohne Eingabe wird kein Fehler für die length-Validierung generiert", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { modelValue: null, maxLen: 3 } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'maxLen = 3' und 'validation' wird eine Validierung von außen ausgeführt und um die length-Validierung ergänzt", () => {
			const wrapper = mount(SvwsUiTextInput, {
				props: { modelValue: "Hallo", validation: () => getValidatorFehler(), maxLen: 3 },
			});
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(2);
			expect(validatorResult.fehler.get(0).getFehlermeldung()).toBe("Der Wert darf maximal 3 Zeichen lang sein.");
			expect(validatorResult.fehler.get(1).getFehlermeldung()).toBe("Custom-Validierung fehlgeschlagen");
		});

		test("Mit Prop 'maxLen = 3' und 'skipDefaultValidation = true' wird keine Validierung für 'length' ausgeführt", () => {
			const wrapper = mount(SvwsUiTextInput, {
				props: { modelValue: "Hallo", maxLen: 3, skipDefaultValidation: true },
			});
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'maxLen = 3' und 'skipDefaultValidation = { length: true }' wird keine Validierung für 'length' ausgeführt", () => {
			const wrapper = mount(SvwsUiTextInput, {
				props: { modelValue: "Hallo", maxLen: 3, skipDefaultValidation: { length: true } },
			});
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'maxLen = 3' und 'skipDefaultValidation = { length: false }' wird eine Validierung für 'length' ausgeführt", () => {
			const wrapper = mount(SvwsUiTextInput, {
				props: { modelValue: "Hallo", maxLen: 3, skipDefaultValidation: { length: false } },
			});
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(1);
			expect(validatorResult.fehler.get(0).getFehlermeldung()).toBe("Der Wert darf maximal 3 Zeichen lang sein.");
		});

		test("Mit Prop 'maxLen = 3' und 'skipDefaultValidation = { length: true }' wird keine Validierung für 'length' ausgeführt", () => {
			const wrapper = mount(SvwsUiTextInput, {
				props: { modelValue: "Hallo", maxLen: 3, skipDefaultValidation: { length: true } },
			});
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});
	});

	describe.concurrent("date", () => {
		test.each([
			"button",
			"checkbox",
			"color",
			"file",
			"hidden",
			"image",
			"month",
			"number",
			"password",
			"radio",
			"range",
			"reset",
			"search",
			"submit",
			"tel",
			"text",
			"email",
			"time",
			"url",
			"week",
		])("Mit Prop 'type = %s', 'minDate = 2026-05-05' und 'maxDate = 2026-05-06' wird kein date-Validator hinzugefügt",
			(type) => {
				const wrapper = mount(SvwsUiTextInput, { props: { type, minDate: "2026-05-05", maxDate: "2026-05-06" } });
				const validatorDateRange = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validatorDateRange;

				expect(validatorDateRange).toBeNull();
			}
		);

		test.each(["date", "datetime-local"])(
			"Mit 'type=%s' und Prop 'minDate = undefined' wird kein date-Validator hinzugefügt",
			(type) => {
				const wrapper = mount(SvwsUiTextInput, { props: { minDate: undefined, type } });
				const validatorDateRange = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validatorDateRange;
				expect(validatorDateRange).toBeNull();
			}
		);

		test.each(["date", "datetime-local"])(
			"Mit 'type=%s' und Prop 'minDate = undefined' und Eingabe '2026-05-04' wird keine Validierung für 'date' ausgeführt",
			(type) => {
				const wrapper = mount(SvwsUiTextInput, { props: { modelValue: "2026-05-04", minDate: undefined, type } });
				const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

				expect(validatorResult.fehler.size()).toBe(0);
			}
		);
		test.each(["date", "datetime-local"])(
			"Mit 'type=%s' und Prop 'minDate = 2026-05-05' wird ein date-Validator hinzugefügt",
			(type) => {
				const wrapper = mount(SvwsUiTextInput, { props: { modelValue: "2026-05-04", minDate: "2026-05-06", type } });
				const validatorDateRange = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validatorDateRange;

				expect(validatorDateRange).not.toBeNull();
				expect(validatorDateRange).toBeInstanceOf(BasicValidator);
			}
		);

		test.each(["date", "datetime-local"])(
			"Mit 'type=%s' und Prop 'minDate = 2026-05-05' und Eingabe '2026-05-04' wird ein Fehler für die date-Validierung generiert",
			(type) => {
				const wrapper = mount(SvwsUiTextInput, { props: { modelValue: "2026-05-04", minDate: "2026-05-05", type } });
				const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

				expect(validatorResult.fehler.size()).toBe(1);
				expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Das frühestmögliche Datum ist der 05.05.2026.");
			}
		);

		test.each(["date", "datetime-local"])(
			"Mit 'type=%s' und Prop 'minDate = 2026-05-05' und Eingabe '2026-05-06' ergibt die Validierung keine Fehler",
			(type) => {
				const wrapper = mount(SvwsUiTextInput, { props: { modelValue: "2026-05-06", minDate: "2026-05-05", type } });
				const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

				expect(validatorResult.fehler.size()).toBe(0);
			}
		);

		test.each(["date", "datetime-local"])(
			"Mit 'type=%s' und Prop 'minDate = 2026-05-05' und 'validation' wird eine Validierung von außen ausgeführt und um die date-Validierung ergänzt",
			(type) => {
				const wrapper = mount(SvwsUiTextInput, {
					props: { modelValue: "2026-05-04", validation: () => getValidatorFehler(), minDate: "2026-05-05", type },
				});
				const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

				expect(validatorResult.fehler.size()).toBe(2);
				expect(validatorResult.fehler.get(0).getFehlermeldung()).toBe("Das frühestmögliche Datum ist der 05.05.2026.");
				expect(validatorResult.fehler.get(1).getFehlermeldung()).toBe("Custom-Validierung fehlgeschlagen");
			}
		);

		test.each(["date", "datetime-local"])(
			"Mit 'type=%s' und Prop 'minDate = 2026-05-05' und 'skipDefaultValidation = true' wird keine Validierung für 'date' ausgeführt",
			(type) => {
				const wrapper = mount(SvwsUiTextInput, { props: { modelValue: "2026-05-04", minDate: "2026-05-05", skipDefaultValidation: true, type } });
				const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

				expect(validatorResult.fehler.size()).toBe(0);
			}
		);
		test.each(["date", "datetime-local"])(
			"Mit 'type=%s' und Prop 'minDate = 2026-05-05' und 'skipDefaultValidation = { dateRange: true }' wird keine Validierung für 'date' ausgeführt",
			(type) => {
				const wrapper = mount(SvwsUiTextInput, { props: { modelValue: "2026-05-04", minDate: "2026-05-05", skipDefaultValidation: { dateRange: true }, type } });
				const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

				expect(validatorResult.fehler.size()).toBe(0);
			}
		);

		test.each(["date", "datetime-local"])(
			"Mit 'type=%s' und Prop 'minDate = 2026-05-05' und 'skipDefaultValidation = { dateRange: false }' wird eine Validierung für 'date' ausgeführt",
			(type) => {
				const wrapper = mount(SvwsUiTextInput, { props: { modelValue: "2026-05-04", minDate: "2026-05-05", skipDefaultValidation: { dateRange: false }, type } });
				const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

				expect(validatorResult.fehler.size()).toBe(1);
				expect(validatorResult.fehler.get(0).getFehlermeldung()).toBe("Das frühestmögliche Datum ist der 05.05.2026.");
			}
		);

		test.each(["date", "datetime-local"])(
			"Mit 'type=%s' und Prop 'minDate = 2026-05-05' und 'skipDefaultValidation = true' wird keine Validierung für 'minDate' ausgeführt",
			(type) => {
				const wrapper = mount(SvwsUiTextInput, { props: { modelValue: "2026-05-04", minDate: "2026-05-06", skipDefaultValidation: true }, type });
				const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

				expect(validatorResult.fehler.size()).toBe(0);
			}
		);

		test.each(["date", "datetime-local"])(
			"Mit 'type=%s' und Prop 'minDate = 2026-05-05' und 'skipDefaultValidation = { dateRange: true }' wird keine Validierung für 'date' ausgeführt",
			(type) => {
				const wrapper = mount(SvwsUiTextInput, { props: { modelValue: "2026-05-04", minDate: "2026-05-05", skipDefaultValidation: { dateRange: true }, type } });
				const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

				expect(validatorResult.fehler.size()).toBe(0);
			}
		);

		test.each(["date", "datetime-local"])(
			"Mit 'type=%s' und Prop 'maxDate = undefined' wird kein date-Validator hinzugefügt",
			(type) => {
				const wrapper = mount(SvwsUiTextInput, { props: { maxDate: undefined, type } });
				const validatorDateRange = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validatorDateRange;
				expect(validatorDateRange).toBeNull();
			}
		);

		test.each(["date", "datetime-local"])(
			"Mit 'type=%s' und Prop 'maxDate = undefined' und Eingabe '2026-05-06' wird keine Validierung für 'date' ausgeführt",
			(type) => {
				const wrapper = mount(SvwsUiTextInput, { props: { modelValue: "2026-05-06", maxDate: undefined, type } });
				const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

				expect(validatorResult.fehler.size()).toBe(0);
			}
		);
		test.each(["date", "datetime-local"])(
			"Mit 'type=%s' und Prop 'maxDate = 2026-05-04' wird ein date-Validator hinzugefügt",
			(type) => {
				const wrapper = mount(SvwsUiTextInput, { props: { modelValue: "2026-05-05", maxDate: "2026-05-04", type } });
				const validatorDateRange = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validatorDateRange;

				expect(validatorDateRange).not.toBeNull();
				expect(validatorDateRange).toBeInstanceOf(BasicValidator);
			}
		);

		test.each(["date", "datetime-local"])(
			"Mit 'type=%s' und Prop 'maxDate = 2026-05-04' und Eingabe '2026-05-04' wird ein Fehler für die date-Validierung generiert",
			(type) => {
				const wrapper = mount(SvwsUiTextInput, { props: { modelValue: "2026-05-05", maxDate: "2026-05-04", type } });
				const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

				expect(validatorResult.fehler.size()).toBe(1);
				expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Das spätestmögliche Datum ist der 04.05.2026.");
			}
		);

		test.each(["date", "datetime-local"])(
			"Mit 'type=%s' und Prop 'maxDate = 2026-05-04' und Eingabe '2026-05-03' ergibt die Validierung keine Fehler",
			(type) => {
				const wrapper = mount(SvwsUiTextInput, { props: { modelValue: "2026-05-04", minDate: "2026-05-03", type } });
				const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

				expect(validatorResult.fehler.size()).toBe(0);
			}
		);

		test.each(["date", "datetime-local"])(
			"Mit 'type=%s' und Prop 'maxDate = 2026-05-04' und 'validation' wird eine Validierung von außen ausgeführt und um die date-Validierung ergänzt",
			(type) => {
				const wrapper = mount(SvwsUiTextInput, {
					props: { modelValue: "2026-05-05", validation: () => getValidatorFehler(), maxDate: "2026-05-04", type },
				});
				const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

				expect(validatorResult.fehler.size()).toBe(2);
				expect(validatorResult.fehler.get(0).getFehlermeldung()).toBe("Das spätestmögliche Datum ist der 04.05.2026.");
				expect(validatorResult.fehler.get(1).getFehlermeldung()).toBe("Custom-Validierung fehlgeschlagen");
			}
		);

		test.each(["date", "datetime-local"])(
			"Mit 'type=%s' und Prop 'maxDate = 2026-05-04' und 'skipDefaultValidation = true' wird keine Validierung für 'date' ausgeführt",
			(type) => {
				const wrapper = mount(SvwsUiTextInput, { props: { modelValue: "2026-05-05", maxDate: "2026-05-04", skipDefaultValidation: true, type } });
				const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

				expect(validatorResult.fehler.size()).toBe(0);
			}
		);
		test.each(["date", "datetime-local"])(
			"Mit 'type=%s' und Prop 'maxDate = 2026-05-04' und 'skipDefaultValidation = { dateRange: true }' wird keine Validierung für 'date' ausgeführt",
			(type) => {
				const wrapper = mount(SvwsUiTextInput, { props: { modelValue: "2026-05-05", maxDate: "2026-05-04", skipDefaultValidation: { dateRange: true }, type } });
				const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

				expect(validatorResult.fehler.size()).toBe(0);
			}
		);

		test.each(["date", "datetime-local"])(
			"Mit 'type=%s' und Prop 'maxDate = 2026-05-04' und 'skipDefaultValidation = { dateRange: false }' wird eine Validierung für 'date' ausgeführt",
			(type) => {
				const wrapper = mount(SvwsUiTextInput, { props: { modelValue: "2026-05-05", maxDate: "2026-05-04", skipDefaultValidation: { dateRange: false }, type } });
				const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

				expect(validatorResult.fehler.size()).toBe(1);
				expect(validatorResult.fehler.get(0).getFehlermeldung()).toBe("Das spätestmögliche Datum ist der 04.05.2026.");
			}
		);
	});

	describe.concurrent("email", () => {
		test.each([
			"button",
			"checkbox",
			"color",
			"date",
			"datetime-local",
			"file",
			"hidden",
			"image",
			"month",
			"number",
			"password",
			"radio",
			"range",
			"reset",
			"search",
			"submit",
			"tel",
			"text",
			"time",
			"url",
			"week",
		])("Mit Prop 'type = %s' wird kein email-Validator hinzugefügt",
			(type) => {
				const wrapper = mount(SvwsUiTextInput, { props: { type } });
				const validatorEmail = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validatorEmail;

				expect(validatorEmail).toBeNull();
			}
		);

		test("Mit Prop 'type = %s' ohne Eingabe wird kein Fehler erzeugt", () => {
			const props = {
				type: "email",
			};
			const wrapper = mount(SvwsUiTextInput, { props: props });

			const validierungFehler = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validierungFehler;
			expect(validierungFehler.size()).toEqual(0);
		});

		test("Mit Prop 'type = %s' und Eingabe 'invalid-email@' wird ein Fehler erzeugt", () => {
			const props = {
				type: "email",
				modelValue: "invalid-email@",
			};
			const wrapper = mount(SvwsUiTextInput, { props: props });

			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(1);
			expect(validatorResult.fehler.get(0).getFehlermeldung()).toBe("Die angegebene E-Mail-Adresse hat ein ungültiges Format.");
		});

		test("Mit Prop 'type = %s' und Eingabe 'test@example.com' wird kein Fehler erzeugt", () => {
			const props = {
				type: "email",
				modelValue: "test@example.com",
			};
			const wrapper = mount(SvwsUiTextInput, { props: props });

			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

	});
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
