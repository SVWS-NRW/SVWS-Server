import { describe, test, expect, beforeEach, afterEach } from "vitest";
import { mount } from "@vue/test-utils";
import SvwsUiTooltip from "./SvwsUiTooltip.vue";

// beforeEach und afterEach wird hier benötigt um die Teleports mit zu testen
beforeEach(() => {
	const el = document.createElement("body");
	el.id = "modal";
	document.body.appendChild(el);
});

afterEach(() => {
	document.body.innerHTML = "";
});

describe("SvwsUiTooltip", () => {
	test("HTML wird erzeugt.", () => {
		const wrapper = mount(SvwsUiTooltip);
		expect(wrapper.html()).toContain("<span");
	});

	test("Korrekte Eigenschaft bei einem mount mit default Props", () => {
		const wrapper = mount(SvwsUiTooltip);

		const spanClasses = wrapper.find("span").classes();

		expect(spanClasses).toContain("tooltip-trigger");
		expect(spanClasses).toContain("cursor-default");
		expect(spanClasses).toContain("tooltip-trigger--underline");
		expect(spanClasses).not.toContain("cursor-help");
		expect(spanClasses).not.toContain("tooltip-trigger--triggered");
		expect(spanClasses).not.toContain("tooltip-trigger--danger");
	});

	test("Mit Color prop 'danger' wird danger class verwendet", () => {
		const wrapper = mount(SvwsUiTooltip, {
			props: { color: "danger" },
		});
		expect(wrapper.find("span").classes()).toContain(
			"tooltip-trigger--danger"
		);
	});

	test("Mit indicator prop 'info' wird info Icon gerendert", () => {
		const wrapper = mount(SvwsUiTooltip, {
			props: { indicator: "info" },
		});
		expect(wrapper.find(".i-ri-information-fill").exists()).toBe(true);
	});

	test("Mit indicator prop 'danger' wird alert Icon gerendert", () => {
		const wrapper = mount(SvwsUiTooltip, {
			props: { indicator: "danger" },
		});
		expect(wrapper.find(".i-ri-alert-fill").exists()).toBe(true);
	});

	test("Mit indicator prop 'danger' wird alert Icon gerendert", () => {
		const wrapper = mount(SvwsUiTooltip, {
			props: { indicator: " " },
		});
		expect(wrapper.find(".i-ri-question-line").exists()).toBe(true);
	});

	test("Mit indicator prop 'underline' wird kein Icon gerendert", () => {
		const wrapper = mount(SvwsUiTooltip, {
			props: { indicator: "underline" },
		});
		expect(wrapper.find(".i-ri-alert-fill").exists()).toBe(false);
		expect(wrapper.find(".i-ri-information-fill").exists()).toBe(false);
		expect(wrapper.find(".i-ri-question-line").exists()).toBe(false);
	});

	test("renders with hover prop", () => {
		const wrapper = mount(SvwsUiTooltip, {
			props: { hover: true },
		});
		expect(wrapper.find("span").classes()).toContain("cursor-default");
	});

	test("Tooltip toggles visibility beim Klick", async () => {
		const wrapper = mount(SvwsUiTooltip);
		await wrapper.trigger("click");
		expect(wrapper.findComponent({ name: "SvwsUiTooltip" }).vm.isOpen).toBe(
			false
		);
		await wrapper.find("span").trigger("click");

		expect(wrapper.findComponent({ name: "SvwsUiTooltip" }).vm.isOpen).toBe(
			true
		);
	});

	test("Slot Inhalt wird gerendert", () => {
		const wrapper = mount(SvwsUiTooltip, {
			slots: {
				default: "<h1>Ich bin ein Slot</h1>",
			},
		});
		expect(wrapper.html()).toContain("<h1>Ich bin ein Slot</h1>");
	});

	test("renders with position prop 'top'", () => {
		const wrapper = mount(SvwsUiTooltip, {
			props: { position: "top" },
		});
		expect(wrapper.vm.placement).toBe("top");
	});

	test("renders with keepOpen prop", () => {
		const wrapper = mount(SvwsUiTooltip, {
			props: { keepOpen: true },
		});
		expect(wrapper.vm.isOpen).toBe(true);
	});

	test("renders with initOpen prop", () => {
		const wrapper = mount(SvwsUiTooltip, {
			props: { initOpen: true },
		});
		expect(wrapper.vm.isOpen).toBe(true);
	});

	test("Beim Mouseover wird Tooltip angezeigt", async () => {
		const wrapper = mount(SvwsUiTooltip, {
			props: { initOpen: false },
		});

		const span = wrapper.find("span");
		await span.trigger("mouseenter");

		expect(wrapper.vm.isOpen).toBe(true);
	});

	test("Beim Mouse Leave wird Tooltip nicht mehr angezeigt", async () => {
		const wrapper = mount(SvwsUiTooltip, {
			props: { initOpen: false },
		});

		const span = wrapper.find("span");
		await span.trigger("mouseenter");
		await span.trigger("mouseleave");

		expect(wrapper.vm.isOpen).toBe(false);
	});

	test("Beim Mouse Leave wird Tooltip immer noch angezeigt wenn keepOpen= true ist", async () => {
		const wrapper = mount(SvwsUiTooltip, {
			props: { initOpen: false, keepOpen: true },
		});

		const span = wrapper.find("span");
		await span.trigger("mouseenter");
		await span.trigger("mouseleave");

		expect(wrapper.vm.isOpen).toBe(true);
	});

	test("Close event wird emitted wenn der Tooltip schließt", async () => {
		const wrapper = mount(SvwsUiTooltip);
		await wrapper.findComponent({ name: "SvwsUiTooltip" }).vm.hideTooltip();
		expect(wrapper.emitted("close")).toBeTruthy();
	});

	test("Inhalts slot im Tooltip wird angezeigt", async () => {
		const wrapper = mount(SvwsUiTooltip, {
			slots: {
				content: "<span class='tooltip-content'>Lorem123</span>",
			},
		});
		await wrapper.vm.showTooltip();
		expect(document.body.outerHTML).contains("Lorem123");
	});

	test("Inhalts slot im Tooltip wird von Anfang an angezeigt wenn initOpen = true ist", async () => {
		mount(SvwsUiTooltip, {
			props: { initOpen: true },
			slots: {
				content: "<span class='tooltip-content'>Lorem123</span>",
			},
		});
		expect(document.body.outerHTML).contains("Lorem123");
	});

	test("ShowArrow = true rendert Pfeil", async () => {
		mount(SvwsUiTooltip, {
			props: { initOpen: true, showArrow:true},
			slots: {
				content: "<span class='tooltip-content'>Lorem123</span>",
			},
		});
		console.log(document.body.outerHTML)
		expect(document.body.outerHTML).contains("absolute rotate-45 bg-inherit aspect-square w-2");
	});
});
