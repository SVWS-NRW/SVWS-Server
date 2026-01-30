import { describe, test, expect, beforeEach, afterEach } from "vitest";
import { mount } from "@vue/test-utils";
import UiValidationTooltip from "../../src/ui/UiValidationTooltip.vue";
import { ValidationResult } from "../../src/validation/ValidationResult";
import { ArrayList } from "../../../core/src/java/util/ArrayList";
import type { ValidatorFehler } from "../../../core/src/asd/validate/ValidatorFehler";
import { BasicValidator } from "../../../core/src/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "../../../core/src/asd/validate/ValidatorFehlerart";

beforeEach(() => {
	document.body.appendChild(document.createElement("body"));
});

afterEach(() => {
	document.body.innerHTML = "";
});

describe("UiValidationTooltip", () => {
	test("Validation Tooltip wird mit default props korrekt gerendert", () => {
		const validationResult = createValidationResult([MussValidatorMock]);
		const wrapper = mount(UiValidationTooltip, {
			props: { validationResult: validationResult },
		});

		const icon = wrapper.find(".validation-tooltip-icon");
		expect(icon.exists()).toBe(true);
		expect(icon.classes()).not.toContain("disabled");
		expect(wrapper.find(".icon-ui-disabled").exists()).toBe(false);
		expect(wrapper.findAll(".icon-ui-danger").length).toBe(1);
	});

	test("Validation Tooltip wird nicht gerendert, wenn keine Fehler da sind", () => {
		const validationResult = createValidationResult([]);
		const wrapper = mount(UiValidationTooltip, {
			props: { validationResult: validationResult },
		});

		expect(wrapper.find(".validation-tooltip").exists()).toBe(false);
	});

	test("Validation Tooltip wird gerendert, wenn Fehler da sind", () => {
		const validationResult = createValidationResult([ASDMussValidatorMock, MussValidatorMock]);
		const wrapper = mount(UiValidationTooltip, {
			props: { validationResult: validationResult },
		});
		expect(wrapper.find(".validation-tooltip-icon").exists()).toBe(true);
	});

	test("Tooltip Icon wird korrekt angezeigt, wenn deaktiviert", () => {
		const validationResult = createValidationResult([MussValidatorMock]);
		const wrapper = mount(UiValidationTooltip, {
			props: { validationResult: validationResult, disabled: true },
		});

		expect(wrapper.find(".validation-tooltip-icon.disabled").exists()).toBe(true);
		expect(wrapper.find(".icon-ui-disabled").exists()).toBe(true);
		expect(wrapper.findAll(".icon-ui-danger, .icon-ui-caution, .icon-ui-warning").length).toBe(0);
	});

	test("Tooltip Icon wird korrekt angezeigt (MUSS Fehler)", () => {
		const validationResult = createValidationResult([MussValidatorMock]);
		const wrapper = mount(UiValidationTooltip, {
			props: { validationResult: validationResult },
		});

		expect(wrapper.find(".i-ri-alert-fill").exists()).toBe(true);
		expect(wrapper.find(".icon-ui-danger").exists()).toBe(true);
	});

	test("Tooltip Icon wird korrekt angezeigt (KANN Fehler)", () => {
		const validationResult = createValidationResult([KannValidatorMock]);
		const wrapper = mount(UiValidationTooltip, {
			props: { validationResult: validationResult },
		});

		expect(wrapper.find(".i-ri-error-warning-fill").exists()).toBe(true);
		expect(wrapper.find(".icon-ui-caution").exists()).toBe(true);
	});

	test("Tooltip Icon wird korrekt angezeigt (HINWEIS Fehler)", () => {
		const validationResult = createValidationResult([HinweisValidatorMock]);
		const wrapper = mount(UiValidationTooltip, {
			props: { validationResult: validationResult },
		});

		expect(wrapper.find(".i-ri-question-fill").exists()).toBe(true);
		expect(wrapper.find(".icon-ui-warning").exists()).toBe(true);
	});

	test("Zeige Statistik Headline, wenn Statistik Fehler existieren", async () => {
		const validationResult = createValidationResult([ASDMussValidatorMock]);
		const wrapper = mount(UiValidationTooltip, {
			props: { validationResult: validationResult },
			global: { stubs: { 'svws-ui-tooltip': TooltipStub } },
		});

		await wrapper.find('.tooltip-trigger').trigger('mouseenter');


		const statistikHeadline = wrapper.find(".statistic-headline");
		expect(statistikHeadline.exists()).toBeTruthy();

		const statistikText = statistikHeadline.find(".text-headline-md.font-medium");
		expect(statistikText.exists()).toBeTruthy();
		expect(statistikText.text()).toBe("Statistik");
		expect(statistikHeadline.find(String.raw`.icon.i-ri-bar-chart-2-line.icon-ui-statistic.pointer-events-auto.mt-0\.5`).exists()).toBeTruthy();
	});

	test("Zeige keine Statistik Headline, wenn keine Statistik Fehler existieren", async () => {
		const validationResult = createValidationResult([MussValidatorMock]);
		const wrapper = mount(UiValidationTooltip, {
			props: { validationResult: validationResult },
			global: { stubs: { 'svws-ui-tooltip': TooltipStub } },
		});

		await wrapper.find('.tooltip-trigger').trigger('mouseenter');


		const statistikHeadline = wrapper.find(".statistic-headline");
		expect(statistikHeadline.exists()).toBeFalsy();
	});

	test("Fehler werden in korrekter Gruppierung und Reihenfolge angezeigt", async () => {
		const validationResult = createValidationResult(
			[ASDMussValidatorMock, ASDKannValidatorMock, ASDHinweisValidatorMock, MussValidatorMock, KannValidatorMock, HinweisValidatorMock]
		);
		const wrapper = mount(UiValidationTooltip, {
			props: { validationResult: validationResult },
			global: { stubs: { 'svws-ui-tooltip': TooltipStub } },
		});

		await wrapper.find('.tooltip-trigger').trigger('mouseenter');

		// Prüfe Fehlerüberschriften
		const fehlergruppenHeadlines = wrapper.findAll(".fehlergruppe-headline-muss, .fehlergruppe-headline-kann, .fehlergruppe-headline-hinweis");
		expect(fehlergruppenHeadlines.length).toBe(6);
		expect(fehlergruppenHeadlines.map(t => t.text())).toEqual(
			['MUSS', 'KANN', 'HINWEIS', 'MUSS', 'KANN', 'HINWEIS']
		);

		// Prüfe Fehlertexte
		const fehlergruppenItems = wrapper.findAll(".fehlergruppe-item");
		expect(fehlergruppenItems.length).toBe(12);
		expect(fehlergruppenItems.flatMap(t => t.findAll('.fehler-icon')).length).toBe(6);
		expect(fehlergruppenItems.map(t => t.find(".fehler-text").text())).toEqual(
			[
				"Dies ist ein Test Muss Fehler (1)",
				"Dies ist ein Test Muss Fehler (2)",
				"Dies ist ein Test Kann Fehler (1)",
				"Dies ist ein Test Kann Fehler (2)",
				"Dies ist ein Test Hinweis Fehler (1)",
				"Dies ist ein Test Hinweis Fehler (2)",
				"Dies ist ein Test ASD Muss Fehler (1)",
				"Dies ist ein Test ASD Muss Fehler (2)",
				"Dies ist ein Test ASD Kann Fehler (1)",
				"Dies ist ein Test ASD Kann Fehler (2)",
				"Dies ist ein Test ASD Hinweis Fehler (1)",
				"Dies ist ein Test ASD Hinweis Fehler (2)",
			]
		);

		// Prüfe Prüfcodes
		expect(fehlergruppenItems.map(t => {
			const badge = t.find(".fehler-badge");
			const badgeText = badge.exists() ? badge.text() : "";
			return badgeText;
		})).toEqual([
			"",
			"",
			"",
			"",
			"",
			"",
			"MV0",
			"MV1",
			"KV0",
			"KV1",
			"HV0",
			"HV1",
		]);

	});

});

// Simuliert ein simples Tooltip ohne Transition, Teleport, etc...
const TooltipStub = {
	template: `
    <div class="tooltip-stub">
      <div
        class="tooltip-trigger"
        @mouseenter="open = true"
        @mouseleave="open = false"
      >
        <slot />
      </div>
      <div v-if="open" class="tooltip">
        <slot name="content" />
      </div>
    </div>
  `,
	data() {
		return { open: false };
	},
};

function createValidationResult(classes: (new () => BasicValidator)[]): ValidationResult {
	const fehler = new ArrayList<ValidatorFehler>();
	classes.map(cls => new cls()).forEach(validator => fehler.addAll(validator.getFehler()));
	return new ValidationResult(fehler);
}

class ASDMussValidatorMock extends BasicValidator {
	constructor() {
		super(ValidatorFehlerart.MUSS);
		this.run();
	}

	getFehlercodePraefix(): string {
		return "MV";
	}

	protected pruefe(): boolean {
		this.addFehler(0, "Dies ist ein Test ASD Muss Fehler (1)");
		this.addFehler(1, "Dies ist ein Test ASD Muss Fehler (2)");
		return false;
	}
}

class ASDKannValidatorMock extends BasicValidator {
	constructor() {
		super(ValidatorFehlerart.KANN);
		this.run();
	}

	getFehlercodePraefix(): string {
		return "KV";
	}

	protected pruefe(): boolean {
		this.addFehler(0, "Dies ist ein Test ASD Kann Fehler (1)");
		this.addFehler(1, "Dies ist ein Test ASD Kann Fehler (2)");
		return false;
	}
}

class ASDHinweisValidatorMock extends BasicValidator {
	constructor() {
		super(ValidatorFehlerart.HINWEIS);
		this.run();
	}

	getFehlercodePraefix(): string {
		return "HV";
	}

	protected pruefe(): boolean {
		this.addFehler(0, "Dies ist ein Test ASD Hinweis Fehler (1)");
		this.addFehler(1, "Dies ist ein Test ASD Hinweis Fehler (2)");
		return false;
	}
}

class MussValidatorMock extends BasicValidator {
	constructor() {
		super(ValidatorFehlerart.MUSS);
		this.run();
	}

	protected pruefe(): boolean {
		this.addFehler(0, "Dies ist ein Test Muss Fehler (1)");
		this.addFehler(1, "Dies ist ein Test Muss Fehler (2)");
		return false;
	}
}

class KannValidatorMock extends BasicValidator {
	constructor() {
		super(ValidatorFehlerart.KANN);
		this.run();
	}

	protected pruefe(): boolean {
		this.addFehler(0, "Dies ist ein Test Kann Fehler (1)");
		this.addFehler(1, "Dies ist ein Test Kann Fehler (2)");
		return false;
	}
}

class HinweisValidatorMock extends BasicValidator {
	constructor() {
		super(ValidatorFehlerart.HINWEIS);
		this.run();
	}

	protected pruefe(): boolean {
		this.addFehler(0, "Dies ist ein Test Hinweis Fehler (1)");
		this.addFehler(1, "Dies ist ein Test Hinweis Fehler (2)");
		return false;
	}
}
