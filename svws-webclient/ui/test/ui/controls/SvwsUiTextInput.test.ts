import { describe, test, expect } from "vitest";
import { mount } from "@vue/test-utils";
import SvwsUiTextInput from "@ui/ui/controls/SvwsUiTextInput.vue";
import { BasicValidator } from "@core/asd/validate/BasicValidator";
import type { ValidatorFehler } from "@core/asd/validate/ValidatorFehler";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import type { List } from "@core/java/util/List";

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
		const wrapper = mount(SvwsUiTextInput, { props: { modelValue: "Test", validation: () => getValidatorFehler() } });
		const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

		expect(validatorResult.fehler.size()).toBe(1);
		expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Custom-Validierung fehlgeschlagen");
	});

	test("Wird mit der Klasse 'text-input--muss' wiedergegeben, wenn valid falsch ist", () => {
		const wrapper = mount(SvwsUiTextInput, {
			props: { modelValue: "Test", valid: () => false },
		});
		expect(wrapper.find(".text-input--muss").exists()).toBe(true);
	});

	test.each([
		["text-input--muss", ValidatorFehlerart.MUSS],
		["text-input--kann", ValidatorFehlerart.KANN],
		["text-input--hinweis", ValidatorFehlerart.HINWEIS],
	])(
		"Wird mit der Klasse '%s' wiedergegeben, wenn Validierungsfehler vom Härtegrad %s vorhanden sind",
		(expectedClass, fehlerart) => {
			const wrapper = mount(SvwsUiTextInput, {
				props: { modelValue: "Test", validation: () => getValidatorFehler(fehlerart) },
			});
			expect(wrapper.find(`.${expectedClass}`).exists()).toBe(true);
		}
	);

	test("Bei Validierungsfehlern wird ein Validation-Icon angezeigt", () => {
		const wrapper = mount(SvwsUiTextInput, {
			props: { modelValue: "Test", placeholder: "Enter Number", validation: () => getValidatorFehler() },
		});
		expect(wrapper.find(".validation-tooltip-icon").exists()).toBeTruthy();
	});

	describe.concurrent("required", () => {

		test.each([
			[false, "ohne Prop 'validation'", undefined],
			[false, "mit Prop 'validation'", () => getValidatorFehler()],
			[true, "mit Prop 'validation'", () => getValidatorFehler()],
		])(
			"Mit Prop 'required = %s' und %s wird kein Required-Validator hinzugefügt",
			(required, _vString, validation) => {
				const wrapper = mount(SvwsUiTextInput, { props: { required, validation } });
				const validatorRequired = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validatorRequired;

				expect(validatorRequired).toBeNull();
			}
		);

		test("Mit Prop 'required = true' und ohne Prop 'validation' wird ein Required-Validator hinzugefügt", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { modelValue: null, required: true } });
			const validatorRequired = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validatorRequired;

			expect(validatorRequired).not.toBeNull();
			expect(validatorRequired).toBeInstanceOf(BasicValidator);
		});

		test.each([
			["''", ""],
			[null, null],
		])("Mit Prop 'required = true' und Eingabe = %s wird ein Fehler für die Required-Validierung generiert", (_, input) => {
			const wrapper = mount(SvwsUiTextInput, { props: { modelValue: input, required: true } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(1);
			expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Bitte geben Sie einen Wert an.");
		});

		test("Mit Prop 'required = true' mit Eingabe ergibt die Validierung keine Fehler", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { modelValue: "Test", required: true } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});
	});

	describe.concurrent("minLen", () => {
		test.each([
			[undefined, "ohne Prop 'validation'", undefined],
			[undefined, "mit Prop 'validation'", () => getValidatorFehler()],
			[3, "mit Prop 'validation'", () => getValidatorFehler()],
		])("Mit Prop 'minLen = %s' und %s wird kein Length-Validator hinzugefügt", (minLen, _validationString, validation) => {
			const wrapper = mount(SvwsUiTextInput, { props: { minLen, validation } });
			const validatorLength = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validatorLength;

			expect(validatorLength).toBeNull();
		});

		test("Mit Prop 'minLen = 3' und ohne Prop 'validation' wird ein Length-Validator hinzugefügt", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { minLen: 3 } });
			const validatorLength = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validatorLength;

			expect(validatorLength).not.toBeNull();
			expect(validatorLength).toBeInstanceOf(BasicValidator);
		});

		test.each([
			[undefined, "'Ha'", "Ha"],
			[3, "'Hal'", "Hal"],
			[3, "''", ""],
			[3, null, null],
		])("Mit Prop 'minLen = %s' und Eingabe = %s wird kein Fehler für die Length-Validierung generiert", (minLen, _inputString, modelValue) => {
			const wrapper = mount(SvwsUiTextInput, { props: { minLen, modelValue } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'minLen = 3' und Eingabe = 'Ha' wird ein Fehler für die Length-Validierung generiert", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { modelValue: "Ha", minLen: 3 } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(1);
			expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Der Wert muss mindestens 3 Zeichen lang sein.");

		});
	});

	describe.concurrent("maxLen", () => {
		test.each([
			[undefined, "ohne Prop 'validation'", undefined],
			[undefined, "mit Prop 'validation'", () => getValidatorFehler()],
			[3, "mit Prop 'validation'", () => getValidatorFehler()],
		])("Mit Prop 'maxLen = %s' und %s wird kein Length-Validator hinzugefügt", (maxLen, _validationString, validation) => {
			const wrapper = mount(SvwsUiTextInput, { props: { maxLen, validation } });
			const validatorLength = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validatorLength;

			expect(validatorLength).toBeNull();
		});

		test("Mit Prop 'maxLen = 3' und ohne Prop 'validation' wird ein Length-Validator hinzugefügt", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { maxLen: 3 } });
			const validatorLength = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validatorLength;

			expect(validatorLength).not.toBeNull();
			expect(validatorLength).toBeInstanceOf(BasicValidator);
		});

		test.each([
			[undefined, "'Hallo'", "Hallo"],
			[3, "'Hal'", "Hal"],
			[3, "''", ""],
			[3, null, null],
		])("Mit Prop 'maxLen = %s' und Eingabe = %s wird kein Fehler für die Length-Validierung generiert", (maxLen, _inputString, modelValue) => {
			const wrapper = mount(SvwsUiTextInput, { props: { maxLen, modelValue } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(0);
		});

		test("Mit Prop 'maxLen = 3' und Eingabe = 'Hallo' wird ein Fehler für die Length-Validierung generiert", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { modelValue: "Hallo", maxLen: 3 } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(1);
			expect(validatorResult.fehler.getFirst().getFehlermeldung()).toBe("Der Wert darf maximal 3 Zeichen lang sein.");

		});
	});

	describe.concurrent("date und datetime-local", () => {
		test.each([
			"button", "checkbox", "color", "file", "hidden", "image", "month", "number", "password", "radio", "range", "reset", "search", "submit", "tel",
			"text", "email", "time", "url", "week",
		])("Mit Prop 'type = %s', 'minDate = 2026-05-05' und 'maxDate = 2026-05-06' wird kein date-Validator hinzugefügt",
			(type) => {
				const wrapper = mount(SvwsUiTextInput, { props: { type, minDate: "2026-05-05", maxDate: "2026-05-06" } });
				const validatorDateRange = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validatorDateRange;

				expect(validatorDateRange).toBeNull();
			}
		);

		test.each([
			["minDate = undefined", "ohne Prop 'validation'", { minDate: undefined, validation: undefined }],
			["minDate = undefined", "mit Prop 'validation'", { minDate: undefined, validation: () => getValidatorFehler() }],
			["minDate = 2026-05-06", "mit Prop 'validation'", { minDate: "2026-05-06", validation: () => getValidatorFehler() }],
			["maxDate = undefined", "ohne Prop 'validation'", { maxDate: undefined, validation: undefined }],
			["maxDate = undefined", "mit Prop 'validation'", { maxDate: undefined, validation: () => getValidatorFehler() }],
			["maxDate = 2026-05-06", "mit Prop 'validation'", { maxDate: "2026-05-06", validation: () => getValidatorFehler() }],
		])("Mit 'type = date', Prop '%s' und %s wird kein Date-Validator hinzugefügt", (_dateString, _validationString, props) => {
			const wrapper = mount(SvwsUiTextInput, {
				props: { type: "date", ...props },
			});
			const validatorDateRange = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validatorDateRange;

			expect(validatorDateRange).toBeNull();
		});

		test.each([
			["minDate = undefined", "ohne Prop 'validation'", { minDate: undefined, validation: undefined }],
			["minDate = undefined", "mit Prop 'validation'", { minDate: undefined, validation: () => getValidatorFehler() }],
			["minDate = 2026-05-06", "mit Prop 'validation'", { minDate: "2026-05-06", validation: () => getValidatorFehler() }],
			["maxDate = undefined", "ohne Prop 'validation'", { maxDate: undefined, validation: undefined }],
			["maxDate = undefined", "mit Prop 'validation'", { maxDate: undefined, validation: () => getValidatorFehler() }],
			["maxDate = 2026-05-06", "mit Prop 'validation'", { maxDate: "2026-05-06", validation: () => getValidatorFehler() }],
		])("Mit 'type = datetime-local', Prop '%s' und %s wird kein Date-Validator hinzugefügt", (_dateString, _validationString, props) => {
			const wrapper = mount(SvwsUiTextInput, {
				props: { type: "datetime-local", ...props },
			});
			const validatorDateRange = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validatorDateRange;

			expect(validatorDateRange).toBeNull();
		});

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
	});

	describe.concurrent("email", () => {
		test.each([
			"button", "checkbox", "color", "date", "datetime-local", "file", "hidden", "image", "month", "number", "password", "radio", "range", "reset",
			"search", "submit", "tel", "text", "time", "url", "week",
		])("Mit Prop 'type = %s' wird kein email-Validator hinzugefügt",
			(type) => {
				const wrapper = mount(SvwsUiTextInput, { props: { type } });
				const validatorEmail = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validatorEmail;

				expect(validatorEmail).toBeNull();
			}
		);

		test("Mit Prop 'type = email' und ohne Prop 'validation' wird ein Email-Validator hinzugefügt", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { type: "email" } });
			const validatorEmail = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validatorEmail;

			expect(validatorEmail).not.toBeNull();
			expect(validatorEmail).toBeInstanceOf(BasicValidator);
		});

		test("Mit Prop 'type = email' und mit Prop 'validation' wird kein Email-Validator hinzugefügt", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { type: "email", validation: () => getValidatorFehler() } });
			const validatorEmail = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validatorEmail;

			expect(validatorEmail).toBeNull();
		});

		test("Mit Prop 'type = email' ohne Eingabe wird kein Fehler erzeugt", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { type: "email" } });

			const validierungFehler = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validierungFehler;
			expect(validierungFehler.size()).toEqual(0);
		});

		test("Mit Prop 'type = email' und Eingabe 'invalid-email@' wird ein Fehler erzeugt", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { type: "email", modelValue: "invalid-email@" } });
			const validatorResult = wrapper.findComponent({ name: "SvwsUiTextInput" }).vm.validationResult;

			expect(validatorResult.fehler.size()).toBe(1);
			expect(validatorResult.fehler.get(0).getFehlermeldung()).toBe("Die angegebene E-Mail-Adresse hat ein ungültiges Format.");
		});

		test("Mit Prop 'type = %s' und Eingabe 'test@example.com' wird kein Fehler erzeugt", () => {
			const wrapper = mount(SvwsUiTextInput, { props: { type: "email", modelValue: "test@example.com" } });
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
