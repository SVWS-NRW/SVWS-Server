import { describe, test, expect, beforeEach, afterEach, vi } from "vitest";
import { mount } from "@vue/test-utils";
import UiCard from "./UiCard.vue";
import SvwsUiButton from "../controls/SvwsUiButton.vue";
import { fail } from "assert";

beforeEach(() => {
	const el = document.createElement("body");
	document.body.appendChild(el);
});

afterEach(() => {
	document.body.innerHTML = "";
});

describe("Komponente kann gemounted werden", () => {
	test("HTML wird erzeugt", () => {
		const wrapper = mount(UiCard);
		expect(wrapper.html()).includes("ui-card");
	});
});

describe.concurrent("PropHandhabung läuft korrekt", () => {

	test("Korrekte Eigenschaft bei einem mount mit default Props", () => {
		const wrapper = mount(UiCard);

		// Prüfe richtige Eigenschaft der Props
		const props = wrapper.props();
		expect(props.compact === false).toBeTruthy();
		expect(props.isOpen === undefined).toBeTruthy();
		expect(props.collapsible === true).toBeTruthy();
		expect(props.collapseIconPosition === 'right').toBeTruthy();
		expect(props.collapseIconOpened === 'i-ri-arrow-up-s-line').toBeTruthy();
		expect(props.collapseIconClosed === 'i-ri-arrow-down-s-line').toBeTruthy();
		expect(props.icon === undefined).toBeTruthy();
		expect(props.title === undefined).toBeTruthy();
		expect(props.subtitle === undefined).toBeTruthy();
		expect(props.info === undefined).toBeTruthy();
		expect(props.content === undefined).toBeTruthy();
		expect(props.showDivider === false).toBeTruthy();
		expect(props.footer === undefined).toBeTruthy();
		expect(props.buttonMode === 'text').toBeTruthy();
		expect(props.buttonContainer === 'content').toBeTruthy();
		expect(props.buttonPosition === 'right').toBeTruthy();
		expect(props.buttonOrientation === 'horizontal').toBeTruthy();
		expect(props.onEdit === undefined).toBeTruthy();
		expect(props.onSave === undefined).toBeTruthy();
		expect(props.onDelete === undefined).toBeTruthy();
		expect(props.onCancel === undefined).toBeTruthy();
		expect(props.editButtonDisabled === false).toBeTruthy();
		expect(props.saveButtonDisabled === false).toBeTruthy();
		expect(props.deleteButtonDisabled === false).toBeTruthy();
		expect(props.cancelButtonDisabled === false).toBeTruthy();
		expect(props.editButtonDisabledReason === undefined).toBeTruthy();
		expect(props.saveButtonDisabledReason === undefined).toBeTruthy();
		expect(props.deleteButtonDisabledReason === undefined).toBeTruthy();
		expect(props.cancelButtonDisabledReason === undefined).toBeTruthy();

		// Prüfer Rendering der Card
		// Header
		expect(wrapper.find(".ui-card--header").exists()).toBeTruthy();
		expect(wrapper.find(".ui-card--header--icon").exists()).toBeFalsy();
		expect(wrapper.find(".ui-card--header--title-wrapper").exists()).toBeFalsy();
		expect(wrapper.find(".ui-card--header--title").exists()).toBeFalsy();
		expect(wrapper.find(".ui-card--header--subtitle").exists()).toBeFalsy();
		expect(wrapper.find(".ui-card--header--right-section").exists()).toBeTruthy();
		expect(wrapper.find(".ui-card--header--info").exists()).toBeFalsy();
		const collapseIcon = wrapper.findAll(".ui-card--header--collapse-icon");
		expect(collapseIcon.length === 1).toBeTruthy();
		expect(wrapper.find(".ui-card--header--right-section").find(".ui-card--header--collapse-icon")).toBeTruthy();

		// Body
		expect(wrapper.find(".ui-card--body-wrapper").exists()).toBeTruthy();
		expect(wrapper.find(".ui-card--body").exists()).toBeTruthy();
		expect(wrapper.find(".ui-card--body--content").exists()).toBeTruthy();
		expect(wrapper.find("#ui-card--button-content-left").exists()).toBeFalsy();
		expect(wrapper.find(".ui-card--content--main").exists()).toBeFalsy();
		expect(wrapper.find("#ui-card--button-content-right").exists()).toBeFalsy();
		expect(wrapper.find("hr").exists()).toBeFalsy();
		expect(wrapper.find(".ui-card--body--footer").exists()).toBeFalsy();
		expect(wrapper.find(".ui-card--button-footer-left").exists()).toBeFalsy();
		expect(wrapper.find(".ui-card--footer--main").exists()).toBeFalsy();
		expect(wrapper.find(".ui-card--button-footer-right").exists()).toBeFalsy();
	});

	test("Mit Prop 'compact = true' wird die Komponente kompakter dargestellt", () => {
		const wrapper = mount(UiCard, { props: { compact: true } });
		expect(wrapper.find(".ui-card").classes()).toContain("ui-card--compact");
	});

	test("Mit Prop 'compact = false' wird die Komponente nicht kompakt dargestellt", () => {
		const wrapper = mount(UiCard, { props: { compact: false } });
		expect(wrapper.find(".ui-card").classes()).not.toContain("ui-card--compact");
	});

	test("Mit Prop 'collapsible = true' wird die Komponente klappbar dargestellt", async () => {
		const setActive = vi.fn();
		const wrapper = mount(UiCard, { props: { collapsible: true }, global: { mocks: { setActive } } });
		const header = wrapper.find(".ui-card--header");
		expect(wrapper.find(".ui-card--header").element.tagName).toBe("BUTTON");
		expect(wrapper.find(".ui-card--header--collapse-icon").exists()).toBeTruthy();
		await header.trigger("click");
		expect(setActive).toHaveBeenCalled();

	});

	test("Mit Prop 'collapsible = false' wird die Komponente nicht klappbar dargestellt", async () => {
		const setActive = vi.fn();
		const wrapper = mount(UiCard, { props: { collapsible: false }, global: { mocks: { setActive } } });
		const header = wrapper.find(".ui-card--header");
		expect(header.element.tagName).toBe("DIV");
		expect(wrapper.find(".ui-card--header--collapse-icon").exists()).toBeFalsy();
		await header.trigger("click");
		expect(setActive).not.toHaveBeenCalled();
	});

	test("Mit Prop 'collapseIconPosition = left' wird die Komponente klappbar mit Collapse-Icon links dargestellt", () => {
		const wrapper = mount(UiCard, { props: { collapsible: true, collapseIconPosition: 'left' } });
		expect(wrapper.find(".ui-card--header").find(".ui-card--header--collapse-icon").exists()).toBeTruthy();
		expect((wrapper.find(".ui-card--header--right-section")).find(".ui-card--header--collapse-icon").exists()).toBeFalsy();
	});

	test("Mit Prop 'collapseIconPosition = right' wird die Komponente klappbar mit Collapse-Icon rechts dargestellt", () => {
		const wrapper = mount(UiCard, { props: { collapsible: true, collapseIconPosition: 'right' } });
		expect(wrapper.find(".ui-card--header").findAll(".ui-card--header--collapse-icon").length === 1).toBeTruthy();
		expect((wrapper.find(".ui-card--header--right-section")).find(".ui-card--header--collapse-icon").exists()).toBeTruthy();
	});

	test("Mit Prop 'collapseIconClosed = i-ri-add-line' wird das Collapse-Icon im geschlossenen Zustand auf ein Plus gesetzt ", () => {
		const wrapper = mount(UiCard, { props: { collapsible: true, collapseIconClosed: 'i-ri-add-line' } });
		expect(wrapper.find(".ui-card--header--collapse-icon").find("span").classes()).toContain("i-ri-add-line");
	});

	test("Mit Prop 'collapseIconOpened = i-ri-subtract-line' wird das Collapse-Icon im offenen Zustand auf ein Minus gesetzt ", async () => {
		const wrapper = mount(UiCard, { props: { collapsible: true, collapseIconOpened: 'i-ri-subtract-line', isOpen: true } });
		await wrapper.findComponent({ name: "UiCard" }).vm.$nextTick();
		expect(wrapper.find(".ui-card--header--collapse-icon").find("span").classes()).toContain("i-ri-subtract-line");
	});

	test("Mit Prop 'icon = i-ri-add-line' wird das Header-Icon angezeigt und auf ein Plus gesetzt ", () => {
		const wrapper = mount(UiCard, { props: { icon: 'i-ri-add-line' } });
		expect(wrapper.find(".ui-card--header--icon").find("span").classes()).toContain("i-ri-add-line");
	});

	test("Mit Prop 'title = Some Title' wird der Titel im Header gesetzt ", () => {
		const wrapper = mount(UiCard, { props: { title: 'Some Title' } });
		expect(wrapper.find(".ui-card--header--title").text()).toBe("Some Title");
	});

	test("Mit Prop 'subtitle = Some Subtitle' wird der Untertitel im Header gesetzt ", () => {
		const wrapper = mount(UiCard, { props: { subtitle: 'Some Subtitle' } });
		expect(wrapper.find(".ui-card--header--subtitle").text()).toBe("Some Subtitle");
	});

	test("Mit Prop 'info = Some Info' wird der Infotext im Header gesetzt ", () => {
		const wrapper = mount(UiCard, { props: { info: 'Some Info' } });
		expect(wrapper.find(".ui-card--header--info").text()).toBe("Some Info");
	});

	test("Mit Prop 'content = Some Content' wird der Content gesetzt ", () => {
		const wrapper = mount(UiCard, { props: { content: 'Some Content' } });
		expect(wrapper.find(".ui-card--content--main").text()).toBe("Some Content");
	});

	test("Mit Prop 'showDivider = true' wird der Divider zwischen dem Footer und dem Content angezeigt ", () => {
		const wrapper = mount(UiCard, { props: { showDivider: true } });
		expect(wrapper.findAll("hr").length).toBe(1);
	});

	test("Mit Prop 'showDivider = false' wird der Divider zwischen dem Footer und dem Content nicht angezeigt ", () => {
		const wrapper = mount(UiCard, { props: { showDivider: false } });
		expect(wrapper.find("hr").exists()).toBeFalsy();
	});

	test("Mit Prop 'footer = Some Footer' wird der Footer gesetzt ", () => {
		const wrapper = mount(UiCard, { props: { footer: 'Some Footer' } });
		expect(wrapper.find(".ui-card--footer--main").text()).toBe("Some Footer");
	});

});

/**
 * Button Tests und Tests, die von Transitions beeinflusst werden können, dürfen nicht concurrent ausgeführt werden, da diese sich gegenseitig beeinflussen
 * können.
 */
describe("PropHandhabung läuft korrekt für Buttons und Transitions", () => {

	// Prop isOpen
	test("Mit Prop 'isOpen = true' wird die Card geöffnet dargestellt", async () => {
		const wrapper = mount(UiCard, { props: { isOpen: true } });
		await wrapper.findComponent({ name: "UiCard" }).vm.$nextTick();
		expect(wrapper.find(".ui-card--header").classes()).toContain("ui-card--active");
		const style = wrapper.find(".ui-card--body-wrapper").attributes("style");
		expect(style === undefined || !style.includes("display: none;")).toBeTruthy();
		expect((wrapper.vm as any).isActive).toBe(true);
		const body = wrapper.find(".ui-card--body-wrapper").element as HTMLElement;
		expect(body.style.maxHeight).toBe("fit-content");
		const collapseIcon = wrapper.find(".ui-card--header--collapse-icon").find("span");
		expect(collapseIcon.classes()).toContain("i-ri-arrow-up-s-line");
	});

	test("Mit Prop 'isOpen = false' wird die Card geschlossen dargestellt", () => {
		const wrapper = mount(UiCard, { props: { isOpen: false } });
		expect(wrapper.find(".ui-card--header").classes()).not.toContain("svws-active");
		const style = wrapper.find(".ui-card--body-wrapper").attributes("style");
		expect(style !== undefined).toBeTruthy();
		expect(style).toContain("display: none;");
		expect((wrapper.vm as any).isActive).toBe(false);
		const body = wrapper.find(".ui-card--body-wrapper").element as HTMLElement;
		expect(body.style.maxHeight).toBe("0px");
		const collapseIcon = wrapper.find(".ui-card--header--collapse-icon").find("span");
		expect(collapseIcon.classes()).toContain("i-ri-arrow-down-s-line");

	});

	// Button Positionen Tests

	test("Mit Prop 'buttonContainer = content', 'buttonPosition = left' und Funktionen für alle 4 Buttons, werden diese auf der linken Seite des Contentbereichs "
		+ "angezeigt", () => {
		const wrapper = mount(UiCard, { props: { buttonContainer: "content", buttonPosition: "left", onEdit : () => {},	onSave : () => {},
			onCancel : () => {}, onDelete : () => {} }, attachTo: document.body });
		expect(wrapper.find("#ui-card--button-content-left").exists()).toBeTruthy();
		expect(wrapper.find(".ui-card--buttons").classes()).not.toContain("ml-auto");
		wrapper.unmount();
	});

	test("Mit Prop 'buttonContainer = content', 'buttonPosition = right' und Funktionen für alle 4 Buttons, werden diese auf der rechten Seite des Contentbereichs "
		+ "angezeigt", () => {
		const wrapper = mount(UiCard, { props: { buttonContainer: "content", buttonPosition: "right", onEdit : () => {},	onSave : () => {},
			onCancel : () => {}, onDelete : () => {} }, attachTo: document.body });
		expect(wrapper.find("#ui-card--button-content-right").exists()).toBeTruthy();
		expect(wrapper.find(".ui-card--buttons").classes()).toContain("ml-auto");
		wrapper.unmount();
	});

	test("Mit Prop 'buttonContainer = footer', 'buttonPosition = left' und Funktionen für alle 4 Buttons, werden diese auf der linken Seite des Footers "
		+ "angezeigt", () => {
		const wrapper = mount(UiCard, { props: { buttonContainer: "footer", buttonPosition: "left", onEdit : () => {},	onSave : () => {},
			onCancel : () => {}, onDelete : () => {} }, attachTo: document.body });
		expect(wrapper.find("#ui-card--button-footer-left").exists()).toBeTruthy();
		expect(wrapper.find(".ui-card--buttons").classes()).not.toContain("ml-auto");
		wrapper.unmount();
	});

	test("Mit Prop 'buttonContainer = footer', 'buttonPosition = right' und Funktionen für alle 4 Buttons, werden diese auf der rechten Seite des Footers "
		+ "angezeigt", () => {
		const wrapper = mount(UiCard, { props: { buttonContainer: "footer", buttonPosition: "right", onEdit : () => {},	onSave : () => {},
			onCancel : () => {}, onDelete : () => {} }, attachTo: document.body });
		expect(wrapper.find("#ui-card--button-footer-right").exists()).toBeTruthy();
		expect(wrapper.find(".ui-card--buttons").classes()).toContain("ml-auto");
		wrapper.unmount();
	});

	test("Mit Prop 'buttonMode = text' und Funktionen für alle 4 Buttons erhält man Textbuttons" , () => {
		const wrapper = mount(UiCard, { props: { buttonMode: "text", onEdit : () => {},	onSave : () => {}, onCancel : () => {}, onDelete : () => {} }
			, attachTo: document.body });
		const buttonLabels = ["Speichern", "Bearbeiten", "Löschen", "Abbrechen"];
		const buttonWrapper = wrapper.find(".ui-card--buttons");
		buttonLabels.forEach(label => {
			expect(buttonWrapper.findAll('span').find(span => span.text() === label)?.exists()).toBeTruthy();
		})
		wrapper.unmount();
	});

	test("Mit Prop 'buttonMode = icon' und Funktionen für alle 4 Buttons erhält man die Textbuttons Bearbeiten, Speichern, Löschen und Abbrechen" , () => {
		const wrapper = mount(UiCard, { props: { buttonMode: "icon", onEdit : () => {},	onSave : () => {}, onCancel : () => {}, onDelete : () => {} }
			, attachTo: document.body });
		const buttonIcons = ["i-ri-check-line", "i-ri-close-line", "i-ri-edit-2-line", "i-ri-delete-bin-line"];
		const buttonWrapper = wrapper.find(".ui-card--buttons");
		buttonIcons.forEach(icon => {
			expect(buttonWrapper.findAll('span').find(span => span.classes().includes(icon))?.exists()).toBeTruthy();
		})
		buttonWrapper.findAll(".ui-card--button").forEach(button => {
			expect(button.classes()).toContain('button--icon');
		});
		wrapper.unmount();
	});

	test("Mit Prop 'buttonOrientation = vertical' und Funktionen für alle 4 Buttons erhält vertikal ausgerichtete Buttons" , () => {
		const wrapper = mount(UiCard, { props: { buttonOrientation: "vertical", onEdit : () => {},	onSave : () => {}, onCancel : () => {}, onDelete : () => {} }
			, attachTo: document.body });
		const buttons = wrapper.find(".ui-card--buttons");
		expect(buttons.classes()).toContain('flex-col');
		wrapper.unmount();
	});

	test("Mit Prop 'buttonOrientation = horizontal' und Funktionen für alle 4 Buttons erhält vertikal ausgerichtete Buttons" , () => {
		const wrapper = mount(UiCard, { props: { buttonOrientation: "horizontal", onEdit : () => {},	onSave : () => {}, onCancel : () => {}, onDelete : () => {} }
			, attachTo: document.body });
		const buttons = wrapper.find(".ui-card--buttons");
		expect(buttons.classes()).not.toContain('flex-col');
		wrapper.unmount();
	});

	// Disabled Button Tests

	test("Mit Prop 'editButtonDisabled = true' und 'onEdit = () => {}' wird der Bearbeiten-Button disabled" , () => {
		const wrapper = mount(UiCard, { props: { editButtonDisabled: true, onEdit : () => {} }, attachTo: document.body });
		const button = wrapper.find(".ui-card--buttons").findAll("button").find(button => button.text() === "Bearbeiten");
		expect(button?.element.hasAttribute("disabled")).toBeTruthy();
		wrapper.unmount();
	});

	test("Mit Prop 'saveButtonDisabled = true' und 'onSave = () => {}' wird der Speichern-Button disabled" , () => {
		const wrapper = mount(UiCard, { props: { saveButtonDisabled: true, onSave : () => {} }, attachTo: document.body });
		const button = wrapper.find(".ui-card--buttons").findAll("button").find(button => button.text() === "Speichern");
		expect(button?.element.hasAttribute("disabled")).toBeTruthy();
		wrapper.unmount();
	});

	test("Mit Prop 'deleteButtonDisabled = true' und 'onDelete = () => {}' wird der Löschen-Button disabled" , () => {
		const wrapper = mount(UiCard, { props: { deleteButtonDisabled: true, onDelete : () => {} }, attachTo: document.body });
		const button = wrapper.find(".ui-card--buttons").findAll("button").find(button => button.text() === "Löschen");
		expect(button?.element.hasAttribute("disabled")).toBeTruthy();
		wrapper.unmount();
	});

	test("Mit Prop 'cancelButtonDisabled = true' und 'onCancel = () => {}' wird der Abbrechen-Button disabled" , () => {
		const wrapper = mount(UiCard, { props: { cancelButtonDisabled: true, onCancel : () => {} }, attachTo: document.body });
		const button = wrapper.find(".ui-card--buttons").findAll("button").find(button => button.text() === "Abbrechen");
		expect(button?.element.hasAttribute("disabled")).toBeTruthy();
		wrapper.unmount();
	});

	test("Mit den Props 'editButtonDisabled = true' und 'onEdit = () => {}' und 'editButtonDisabledReason = Test' wird der Bearbeiten-Button disabled " +
			"und ein Tooltip mit der Begründung gerendert" , async () => {
		const wrapper = mount(UiCard, { props: { editButtonDisabled: true, onEdit : () => {}, editButtonDisabledReason: "Test" }, attachTo: document.body });
		const tooltipTrigger = wrapper.findAll(".tooltip-trigger").find(tooltip => tooltip.find("button").text() === "Bearbeiten");
		expect(tooltipTrigger?.exists()).toBeTruthy();
		await tooltipTrigger?.trigger("mouseenter");
		const editTooltip = Array.from(document.body.querySelectorAll(".tooltip-content"))
			.filter(element => element.textContent === "Bearbeiten: Test");
		expect(editTooltip.length === 1).toBeTruthy();

		wrapper.unmount();
	});

	test("Mit den Props 'saveButtonDisabled = true' und 'onSave = () => {}' und 'saveButtonDisabledReason = Test' wird der Speichern-Button disabled " +
			"und ein Tooltip mit der Begründung gerendert" , async () => {
		const wrapper = mount(UiCard, { props: { saveButtonDisabled: true, onSave : () => {}, saveButtonDisabledReason: "Test" }, attachTo: document.body });
		const tooltipTrigger = wrapper.findAll(".tooltip-trigger").find(tooltip => tooltip.find("button").text() === "Speichern");
		expect(tooltipTrigger?.exists()).toBeTruthy();
		await tooltipTrigger?.trigger("mouseenter");
		const saveTooltip = Array.from(document.body.querySelectorAll(".tooltip-content"))
			.filter(element => element.textContent === "Speichern: Test");
		expect(saveTooltip.length === 1).toBeTruthy();

		wrapper.unmount();
	});

	test("Mit den Props 'deleteButtonDisabled = true' und 'onDelete = () => {}' und 'deleteButtonDisabledReason = Test' wird der Löschen-Button disabled " +
		"und ein Tooltip mit der Begründung gerendert" , async () => {
		const wrapper = mount(UiCard, { props: { deleteButtonDisabled: true, onDelete : () => {}, deleteButtonDisabledReason: "Test" }, attachTo: document.body });
		const tooltipTrigger = wrapper.findAll(".tooltip-trigger").find(tooltip => tooltip.find("button").text() === "Löschen");
		expect(tooltipTrigger?.exists()).toBeTruthy();
		await tooltipTrigger?.trigger("mouseenter");
		const deleteTooltip = Array.from(document.body.querySelectorAll(".tooltip-content"))
			.filter(element => element.textContent === "Löschen: Test");
		expect(deleteTooltip.length === 1).toBeTruthy();

		wrapper.unmount();
	});

	test("Mit den Props 'cancelButtonDisabled = true' und 'onCancel = () => {}' und 'cancelButtonDisabledReason = Test' wird der Abbrechen-Button disabled " +
	"und ein Tooltip mit der Begründung gerendert" , async () => {
		const wrapper = mount(UiCard, { props: { cancelButtonDisabled: true, onCancel : () => {}, cancelButtonDisabledReason: "Test" }, attachTo: document.body });
		await wrapper.findComponent({ name: "UiCard" }).vm.$nextTick();
		const tooltipTrigger = wrapper.findAll(".tooltip-trigger").find(tooltip => tooltip.find("button").text() === "Abbrechen");
		expect(tooltipTrigger?.exists()).toBeTruthy();
		await tooltipTrigger?.trigger("mouseenter");
		const cancelTooltip = Array.from(document.body.querySelectorAll(".tooltip-content"))
			.filter(element => element.textContent === "Abbrechen: Test");
		expect(cancelTooltip.length === 1).toBeTruthy();

		wrapper.unmount();
	});

	// Button Tooltip Anzeige Tests
	// Icon Buttons
	test("Mit den Props 'buttonMode = icon' und  'onEdit = () => {}' wird um den Bearbeiten-Button ein Tooltip gerendert", () => {
		const wrapper = mount(UiCard, { props: { buttonMode: 'icon', onEdit : () => {} }, attachTo: document.body });
		expect(wrapper.findAll(".tooltip-trigger").find(tooltip => tooltip.find("span").classes().includes("i-ri-edit-2-line"))?.exists()).toBeTruthy();
		wrapper.unmount();
	});

	test("Mit den Props 'buttonMode = icon' und  'onSave = () => {}' wird um den Speichern-Button ein Tooltip gerendert", () => {
		const wrapper = mount(UiCard, { props: { buttonMode: 'icon', onSave : () => {} }, attachTo: document.body });
		expect(wrapper.findAll(".tooltip-trigger").find(tooltip => tooltip.find("span").classes().includes("i-ri-check-line"))?.exists()).toBeTruthy();
		wrapper.unmount();
	});

	test("Mit den Props 'buttonMode = icon' und  'onDelete = () => {}' wird um den Löschen-Button ein Tooltip gerendert", () => {
		const wrapper = mount(UiCard, { props: { buttonMode: 'icon', onDelete : () => {} }, attachTo: document.body });
		expect(wrapper.findAll(".tooltip-trigger").find(tooltip => tooltip.find("span").classes().includes("i-ri-delete-bin-line"))?.exists()).toBeTruthy();
		wrapper.unmount();
	});

	test("Mit den Props 'buttonMode = icon' und  'onCancel = () => {}' wird um den Abbrechen-Button ein Tooltip gerendert", () => {
		const wrapper = mount(UiCard, { props: { buttonMode: 'icon', onCancel : () => {} }, attachTo: document.body });
		expect(wrapper.findAll(".tooltip-trigger").find(tooltip => tooltip.find("span").classes().includes("i-ri-close-line"))?.exists()).toBeTruthy();
		wrapper.unmount();
	});

	test("Mit den Props 'buttonMode = icon' und  'onEdit = () => {}' und 'editButtonDisabled: true' wird um den Bearbeiten-Button ein Tooltip gerendert", () => {
		const wrapper = mount(UiCard, { props: { buttonMode: 'icon', onEdit : () => {}, editButtonDisabled: true }, attachTo: document.body });
		expect(wrapper.findAll(".tooltip-trigger").find(tooltip => tooltip.find("span").classes().includes("i-ri-edit-2-line"))?.exists()).toBeTruthy();
		wrapper.unmount();
	});

	test("Mit den Props 'buttonMode = icon' und  'onSave = () => {}' und 'saveButtonDisabled: true' wird um den Speichern-Button ein Tooltip gerendert", () => {
		const wrapper = mount(UiCard, { props: { buttonMode: 'icon', onSave : () => {}, saveButtonDisabled: true }, attachTo: document.body });
		expect(wrapper.findAll(".tooltip-trigger").find(tooltip => tooltip.find("span").classes().includes("i-ri-check-line"))?.exists()).toBeTruthy();
		wrapper.unmount();
	});

	test("Mit den Props 'buttonMode = icon' und  'onDelete = () => {}' und 'deleteButtonDisabled: true' wird um den Löschen-Button ein Tooltip gerendert", () => {
		const wrapper = mount(UiCard, { props: { buttonMode: 'icon', onDelete : () => {}, deleteButtonDisabled: true }, attachTo: document.body });
		expect(wrapper.findAll(".tooltip-trigger").find(tooltip => tooltip.find("span").classes().includes("i-ri-delete-bin-line"))?.exists()).toBeTruthy();
		wrapper.unmount();
	});

	test("Mit den Props 'buttonMode = icon' und  'onCancel = () => {}' und 'cancelButtonDisabled: true' wird um den Abbrechen-Button ein Tooltip gerendert", () => {
		const wrapper = mount(UiCard, { props: { buttonMode: 'icon', onCancel : () => {}, cancelButtonDisabled: true }, attachTo: document.body });
		expect(wrapper.findAll(".tooltip-trigger").find(tooltip => tooltip.find("span").classes().includes("i-ri-close-line"))?.exists()).toBeTruthy();
		wrapper.unmount();
	});

	// Text Buttons
	test("Mit den Props 'buttonMode = text' und  'onEdit = () => {}' wird um den Bearbeiten-Button kein Tooltip gerendert", () => {
		const wrapper = mount(UiCard, { props: { buttonMode: 'text', onEdit : () => {} }, attachTo: document.body });
		expect(wrapper.findAll(".tooltip-trigger").find(tooltip => tooltip.find("button").text() === "Bearbeiten")?.exists()).toBeFalsy();
		wrapper.unmount();
	});

	test("Mit den Props 'buttonMode = text' und  'onSave = () => {}' wird um den Speichern-Button kein Tooltip gerendert", () => {
		const wrapper = mount(UiCard, { props: { buttonMode: 'text', onSave : () => {} }, attachTo: document.body });
		expect(wrapper.findAll(".tooltip-trigger").find(tooltip => tooltip.find("button").text() === "Speichern")?.exists()).toBeFalsy();
		wrapper.unmount();
	});

	test("Mit den Props 'buttonMode = text' und  'onDelete = () => {}' wird um den Löschen-Button kein Tooltip gerendert", () => {
		const wrapper = mount(UiCard, { props: { buttonMode: 'text', onDelete : () => {} }, attachTo: document.body });
		expect(wrapper.findAll(".tooltip-trigger").find(tooltip => tooltip.find("button").text() === "Löschen")?.exists()).toBeFalsy();
		wrapper.unmount();
	});

	test("Mit den Props 'buttonMode = text' und  'onCancel = () => {}' wird um den Abbrechen-Button kein Tooltip gerendert", () => {
		const wrapper = mount(UiCard, { props: { buttonMode: 'text', onCancel : () => {} }, attachTo: document.body });
		expect(wrapper.findAll(".tooltip-trigger").find(tooltip => tooltip.find("button").text() === "Abbrechen")?.exists()).toBeFalsy();
		wrapper.unmount();
	});

	test("Mit den Props 'buttonMode = text' und  'onEdit = () => {}' und 'editButtonDisabled: true' wird um den Bearbeiten-Button kein Tooltip gerendert", () => {
		const wrapper = mount(UiCard, { props: { buttonMode: 'text', onEdit : () => {}, editButtonDisabled: true }, attachTo: document.body });
		expect(wrapper.findAll(".tooltip-trigger").find(tooltip => tooltip.find("button").text() === "Bearbeiten")?.exists()).toBeFalsy();
		wrapper.unmount();
	});

	test("Mit den Props 'buttonMode = text' und  'onSave = () => {}' und 'saveButtonDisabled: true' wird um den Speichern-Button kein Tooltip gerendert", () => {
		const wrapper = mount(UiCard, { props: { buttonMode: 'text', onSave : () => {}, saveButtonDisabled: true }, attachTo: document.body });
		expect(wrapper.findAll(".tooltip-trigger").find(tooltip => tooltip.find("button").text() === "Speichern")?.exists()).toBeFalsy();
		wrapper.unmount();
	});

	test("Mit den Props 'buttonMode = text' und  'onDelete = () => {}' und 'deleteButtonDisabled: true' wird um den Löschen-Button kein Tooltip gerendert", () => {
		const wrapper = mount(UiCard, { props: { buttonMode: 'text', onDelete : () => {}, deleteButtonDisabled: true }, attachTo: document.body });
		expect(wrapper.findAll(".tooltip-trigger").find(tooltip => tooltip.find("button").text() === "Löschen")?.exists()).toBeFalsy();
		wrapper.unmount();
	});

	test("Mit den Props 'buttonMode = text' und  'onCancel = () => {}' und 'cancelButtonDisabled: true' wird um den Abbrechen-Button kein Tooltip gerendert", () => {
		const wrapper = mount(UiCard, { props: { buttonMode: 'text', onCancel : () => {}, cancelButtonDisabled: true }, attachTo: document.body });
		expect(wrapper.findAll(".tooltip-trigger").find(tooltip => tooltip.find("button").text() === "Abbrechen")?.exists()).toBeFalsy();
		wrapper.unmount();
	});

	test("Mit den Props 'buttonMode = text' und  'onEdit = () => {}' und 'editButtonDisabled: true' und 'editButtonDisabledReason: Test' wird um den "
		+ "Bearbeiten-Button ein Tooltip gerendert", () => {
		const wrapper = mount(UiCard, { props: { buttonMode: 'text', onEdit : () => {}, editButtonDisabled: true, editButtonDisabledReason: 'Test' }, attachTo: document.body });
		expect(wrapper.findAll(".tooltip-trigger").find(tooltip => tooltip.find("button").text() === "Bearbeiten")?.exists()).toBeTruthy();
		wrapper.unmount();
	});

	test("Mit den Props 'buttonMode = text' und  'onSave = () => {}' und 'saveButtonDisabled: true' und 'saveButtonDisabledReason: Test' wird um den "
		+ "Speichern-Button ein Tooltip gerendert", () => {
		const wrapper = mount(UiCard, { props: { buttonMode: 'text', onSave : () => {}, saveButtonDisabled: true, saveButtonDisabledReason: 'Test' }, attachTo: document.body });
		expect(wrapper.findAll(".tooltip-trigger").find(tooltip => tooltip.find("button").text() === "Speichern")?.exists()).toBeTruthy();
		wrapper.unmount();
	});

	test("Mit den Props 'buttonMode = text' und  'onDelete = () => {}' und 'deleteButtonDisabled: true' und 'deleteButtonDisabledReason: Test' wird um den "
		+ "Löschen-Button ein Tooltip gerendert", () => {
		const wrapper = mount(UiCard, { props: { buttonMode: 'text', onDelete : () => {}, deleteButtonDisabled: true, deleteButtonDisabledReason: 'Test' }, attachTo: document.body });
		expect(wrapper.findAll(".tooltip-trigger").find(tooltip => tooltip.find("button").text() === "Löschen")?.exists()).toBeTruthy();
		wrapper.unmount();
	});

	test("Mit den Props 'buttonMode = text' und  'onCancel = () => {}' und 'cancelButtonDisabled: true' und 'cancelButtonDisabledReason: Test' wird um den "
		+ "Abbrechen-Button ein Tooltip gerendert", () => {
		const wrapper = mount(UiCard, { props: { buttonMode: 'text', onCancel : () => {}, cancelButtonDisabled: true, cancelButtonDisabledReason: 'Test' }, attachTo: document.body });
		expect(wrapper.findAll(".tooltip-trigger").find(tooltip => tooltip.find("button").text() === "Abbrechen")?.exists()).toBeTruthy();
		wrapper.unmount();
	});

	const buttonTypes: Array<[string, string, Record<string, () => void>]> = [
		["onEdit", "primary", { 'on-edit': () => {} }],
		["onSave", "primary", { 'on-save': () => {} }],
		["onDelete", "danger", { 'on-delete': () => {} }],
		["onCancel", "secondary", { 'on-cancel': () => {} }],
	];

	test.each(buttonTypes)("Mit Props Button Definitionen für %s und 'buttonMode' = 'text' hat der Button folgenden type: %s",
		async (functions, type, functionProps) => {
			const wrapper = mount(UiCard, {	props: { buttonMode: 'text', ...functionProps }, attachTo: document.body });
			const button = wrapper.findComponent(SvwsUiButton);
			expect(button.props().type).toBe(type);
		}
	);

	const buttonIconTypes: Array<[string, string, Record<string, () => void>]> = [
		["onEdit", "icon-primary", { 'on-edit': () => {} }],
		["onSave", "icon-primary", { 'on-save': () => {} }],
		["onDelete", "icon-error", { 'on-delete': () => {} }],
		["onCancel", "icon-primary", { 'on-cancel': () => {} }],
	];

	test.each(buttonIconTypes)("Mit Props Button Definitionen für %s und 'buttonMode' = 'icon' hat der Button den type 'icon' und span die Klasse: %s",
		async (functions, type, functionProps) => {
			const wrapper = mount(UiCard, {	props: { buttonMode: 'icon', ...functionProps }, attachTo: document.body });
			const button = wrapper.findComponent(SvwsUiButton);
			expect(button.props().type).toBe("icon");
			const span = button.find("span");
			expect(span.classes()).toContain(type);
		}
	);

	const simpleButtons: Array<[string, Record<string, () => void>]> = [
		["onEdit", { 'on-edit': () => {} }],
		["onSave", { 'on-save': () => {} }],
		["onDelete", { 'on-delete': () => {} }],
		["onCancel", { 'on-cancel': () => {} }],
	];

	test.each(simpleButtons)("Mit Button Definitionen für %s, 'buttonMode' = 'text' und 'compact' = true ist die size des Buttons gleich small", () => {
		const wrapper = mount(UiCard, {	props: { buttonMode: 'text', onEdit: () => {}, compact: true}, attachTo: document.body });
		const button = wrapper.findComponent(SvwsUiButton);
		expect(button.props().size).toBe('small');
	});

	test.each(simpleButtons)("Mit Button Definitionen für %s, 'buttonMode' = 'text' und 'compact' = false ist die size des Buttons gleich normal", () => {
		const wrapper = mount(UiCard, {	props: { buttonMode: 'text', onEdit: () => {}, compact: false}, attachTo: document.body });
		const button = wrapper.findComponent(SvwsUiButton);
		expect(button.props().size).toBe('normal');
	});

	test.each(simpleButtons)("Mit Button Definitionen für %s, 'buttonMode' = 'icon' und 'compact' = false ist die size des Buttons gleich normal", () => {
		const wrapper = mount(UiCard, {	props: { buttonMode: 'icon', onEdit: () => {}, compact: false}, attachTo: document.body });
		const button = wrapper.findComponent(SvwsUiButton);
		expect(button.props().size).toBe('normal');
	});

	test.each(simpleButtons)("Mit Button Definitionen für %s, 'buttonMode' = 'icon' und 'compact' = true ist die size des Buttons gleich normal", () => {
		const wrapper = mount(UiCard, {	props: { buttonMode: 'icon', onEdit: () => {}, compact: true}, attachTo: document.body });
		const button = wrapper.findComponent(SvwsUiButton);
		expect(button.props().size).toBe('normal');
	});
});

// Tests dürfen aufgrund der Transitions nicht parallel laufen, da sie sich sonst beeinflussen
describe("Events laufen korrekt", () => {
	test("Klick auf Header (geschlossene Card) öffnet die Card", async () => {
		const wrapper = mount(UiCard, { props: { isOpen: false }, global: { stubs: { transition: false} } });
		const header = wrapper.find(".ui-card--header");
		const body = wrapper.find(".ui-card--body-wrapper")
		expect(header.classes()).not.toContain("svws-active");
		expect(body.attributes("style")).toContain("display: none");
		expect((body.element as HTMLElement).style.maxHeight).toBe("0px");
		await header.trigger("click");
		// Schließt die Transition ab
		(body.element as HTMLElement).dispatchEvent(new Event('transitionend'));
		expect(body.attributes("style")).toContain("max-height: fit-content");
		expect((wrapper.vm as any).isActive).toBe(true);
		expect(header.classes()).toContain("ui-card--active");
		expect(body.attributes("style")).not.toContain("display: none");
	});

	test("Klick auf Header (geöffnete Card) schließt die Card", async () => {
		const wrapper = mount(UiCard, { props: { isOpen: true }, global: { stubs: { transition: false} } });
		const header = wrapper.find(".ui-card--header");
		const body = wrapper.find(".ui-card--body-wrapper")
		await wrapper.findComponent({ name: "UiCard" }).vm.$nextTick();
		expect(header.classes()).toContain("ui-card--active");
		expect(body.attributes("style")).not.toContain("display: none");
		expect((body.element as HTMLElement).style.maxHeight).toBe("fit-content");
		await header.trigger("click");
		// Schließt die Transition ab
		(body.element as HTMLElement).dispatchEvent(new Event('transitionend'));
		expect((body.element as HTMLElement).style.maxHeight).toBe("0px");
		expect((wrapper.vm as any).isActive).toBe(false);
		expect(header.classes()).not.toContain("ui-card--active");
		expect(body.attributes("style")).toContain("display: none");
	});

	test("Vor dem Öffnen der Card wird 'isOpen: true' emittet", async () => {
		const wrapper = mount(UiCard, {	props: { isOpen: false }, global: { stubs: { transition: false} } });
		let emittedEvents = wrapper.emitted("update:isOpen");
		expect(emittedEvents).toBeFalsy();
		await wrapper.setProps({ isOpen: true});
		emittedEvents = wrapper.emitted("update:isOpen");
		expect(emittedEvents).toBeTruthy();
		if ((emittedEvents === undefined) || (emittedEvents[0].length === 0))
			fail("Kein Event wurde emitted");
		expect(emittedEvents[0][0]).toBe(true);
		expect((wrapper.vm as any).isActive).toBe(true);
	});

	test("Nach dem Schließen der Card wird 'isOpen: false' emittet", async () => {
		const wrapper = mount(UiCard, {	props: { isOpen: true }, global: { stubs: { transition: false} } });
		const body = wrapper.find(".ui-card--body-wrapper").element as HTMLElement;
		await wrapper.findComponent({ name: "UiCard" }).vm.$nextTick();
		// update:isOpen = true wird beim Mounten emittet
		let emittedEvents = wrapper.emitted("update:isOpen");
		expect(emittedEvents).toBeTruthy();
		if ((emittedEvents === undefined) || (emittedEvents[0].length === 0))
			fail("Kein Event wurde emitted");
		expect(emittedEvents[0][0]).toBe(true);

		// Card schließen
		await wrapper.setProps({isOpen: false});
		// Schließt die Transition ab
		body.dispatchEvent(new Event('transitionend'));
		emittedEvents = wrapper.emitted("update:isOpen");
		expect(emittedEvents).toBeTruthy();
		if ((emittedEvents === undefined) || (emittedEvents[0].length === 0))
			fail("Kein Event wurde emitted");
		// prüft, ob das zweite Event überhaupt emittet wurde
		expect(emittedEvents[1]).toBeDefined();
		// prüft, dass beim zweiten mal update:isOpen = false emittet wurde
		expect(emittedEvents[1][0]).toBe(false);
	});

});

describe.concurrent("Alle computeds berechnen korrekt", () => {
	// showCollapseIconLeft
	test("computed->showCollapseIconLeft liefert true, wenn kein collapseLeft-slot gesetzt ist, die Card collapsible ist und das Collapse-icon links ist", async () => {
		const wrapper = mount(UiCard, { props: { collapsible: true, collapseIconPosition: 'left' } });
		expect( await wrapper.findComponent({ name: "UiCard" }).vm.showCollapseIconLeft).toBeTruthy();
	});

	test("computed->showCollapseIconLeft liefert false, wenn die Card nicht collapsible ist", async () => {
		const wrapper = mount(UiCard, { props: { collapsible: false, collapseIconPosition: 'left' } });
		expect( await wrapper.findComponent({ name: "UiCard" }).vm.showCollapseIconLeft).toBeFalsy();
	});

	test("computed->showCollapseIconLeft liefert false, wenn das Collapse-icon nicht links ist", async () => {
		const wrapper = mount(UiCard, { props: { collapsible: true, collapseIconPosition: 'right' } });
		expect( await wrapper.findComponent({ name: "UiCard" }).vm.showCollapseIconLeft).toBeFalsy();
	});

	test("computed->showCollapseIconLeft liefert false, wenn ein collapseLeft-slot gesetzt ist", async () => {
		const wrapper = mount(UiCard, {props: { collapsible: true }, slots:{ collapseLeft: "Custom Slot Inhalt" }});
		expect( await wrapper.findComponent({ name: "UiCard" }).vm.showCollapseIconLeft).toBeFalsy();
	});

	// showCollapseIconRight
	test("computed->showCollapseIconRight liefert true, wenn kein collapseRight-slot gesetzt ist, die Card collapsible ist und das Collapse-icon rechts ist", async () => {
		const wrapper = mount(UiCard, { props: { collapsible: true, collapseIconPosition: 'right' } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showCollapseIconRight).toBeTruthy();
	});

	test("computed->showCollapseIconRight liefert false, wenn die Card nicht collapsible ist", async () => {
		const wrapper = mount(UiCard, { props: { collapsible: false, collapseIconPosition: 'right' } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showCollapseIconRight).toBeFalsy();
	});

	test("computed->showCollapseIconRight liefert false, wenn das Collapse-icon nicht rechts ist", async () => {
		const wrapper = mount(UiCard, { props: { collapsible: true, collapseIconPosition: 'left' } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showCollapseIconRight).toBeFalsy();
	});

	test("computed->showCollapseIconRight liefert false, wenn ein collapseRight-slot gesetzt ist", async () => {
		const wrapper = mount(UiCard, { props: { collapsible: true }, slots: { collapseRight: "Custom Slot Inhalt" } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showCollapseIconRight).toBeFalsy();
	});

	// showIcon
	test("computed->showIcon liefert true, wenn kein icon-Slot gesetzt ist, ein Icon über Props definiert ist und die Länge des Icons nicht 0 ist", async () => {
		const wrapper = mount(UiCard, { props: { icon: 'some-icon' } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showIcon).toBeTruthy();
	});

	test("computed->showIcon liefert false, wenn ein icon-Slot gesetzt ist", async () => {
		const wrapper = mount(UiCard, { props: { icon: 'some-icon' }, slots: { icon: "Custom Icon Inhalt" }});
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showIcon).toBeFalsy();
	});

	test("computed->showIcon liefert false, wenn das Icon in den Props nicht definiert ist", async () => {
		const wrapper = mount(UiCard);
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showIcon).toBeFalsy();
	});

	test("computed->showIcon liefert false, wenn die Länge des Icons 0 ist", async () => {
		const wrapper = mount(UiCard, { props: { icon: '' } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showIcon).toBeFalsy();
	});

	// showTitleWrapper
	test("computed->showTitleWrapper liefert true, wenn ein title-Slot gesetzt ist", async () => {
		const wrapper = mount(UiCard, { slots: { title: "Custom Title Inhalt" } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showTitleWrapper).toBeTruthy();
	});

	test("computed->showTitleWrapper liefert true, wenn ein subtitle-Slot gesetzt ist", async () => {
		const wrapper = mount(UiCard, { slots: { subtitle: "Custom Subtitle Inhalt" } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showTitleWrapper).toBeTruthy();
	});

	test("computed->showTitleWrapper liefert true, wenn ein title-Prop definiert ist und die Länge des Titels nicht 0 ist", async () => {
		const wrapper = mount(UiCard, { props: { title: "A Title" } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showTitleWrapper).toBeTruthy();
	});

	test("computed->showTitleWrapper liefert true, wenn ein subtitle-Prop definiert ist und die Länge des Subtitels nicht 0 ist", async () => {
		const wrapper = mount(UiCard, { props: { subtitle: "A Subtitle" } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showTitleWrapper).toBeTruthy();
	});

	test("computed->showTitleWrapper liefert false, wenn weder Slots noch Props für title oder subtitle gesetzt sind", async () => {
		const wrapper = mount(UiCard);
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showTitleWrapper).toBeFalsy();
	});

	test("computed->showTitleWrapper liefert false, wenn title-Prop gesetzt ist, aber die Länge des Titels 0 ist", async () => {
		const wrapper = mount(UiCard, { props: { title: "" } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showTitleWrapper).toBeFalsy();
	});

	test("computed->showTitleWrapper liefert false, wenn subtitle-Prop gesetzt ist, aber die Länge des Subtitels 0 ist", async () => {
		const wrapper = mount(UiCard, { props: { subtitle: "" } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showTitleWrapper).toBeFalsy();
	});

	// showTitle
	test("computed->showTitle liefert true, wenn kein title-Slot gesetzt ist, das title-Prop definiert ist und die Länge des Titels nicht 0 ist", async () => {
		const wrapper = mount(UiCard, { props: { title: "A Title" } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showTitle).toBeTruthy();
	});

	test("computed->showTitle liefert false, wenn ein title-Slot gesetzt ist", async () => {
		const wrapper = mount(UiCard, { slots: { title: "Custom Title Inhalt" }, props: { title: "A Title" } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showTitle).toBeFalsy();
	});

	test("computed->showTitle liefert false, wenn das title-Prop nicht definiert ist", async () => {
		const wrapper = mount(UiCard);
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showTitle).toBeFalsy();
	});

	test("computed->showTitle liefert false, wenn das title-Prop gesetzt ist, aber die Länge des Titels 0 ist", async () => {
		const wrapper = mount(UiCard, { props: { title: "" } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showTitle).toBeFalsy();
	});

	// showSubtitle
	test("computed->showSubtitle liefert true, wenn kein subtitle-Slot gesetzt ist, das subtitle-Prop definiert ist und die Länge des Subtitels nicht 0 ist", async () => {
		const wrapper = mount(UiCard, { props: { subtitle: "A Subtitle" } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showSubtitle).toBeTruthy();
	});

	test("computed->showSubtitle liefert false, wenn ein subtitle-Slot gesetzt ist", async () => {
		const wrapper = mount(UiCard, { slots: { subtitle: "Custom Subtitle Inhalt" }, props: { subtitle: "A Subtitle" } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showSubtitle).toBeFalsy();
	});

	test("computed->showSubtitle liefert false, wenn das subtitle-Prop nicht definiert ist", async () => {
		const wrapper = mount(UiCard);
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showSubtitle).toBeFalsy();
	});

	test("computed->showSubtitle liefert false, wenn das subtitle-Prop gesetzt ist, aber die Länge des Subtitels 0 ist", async () => {
		const wrapper = mount(UiCard, { props: { subtitle: "" } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showSubtitle).toBeFalsy();
	});

	// showInfo
	test("computed->showInfo liefert true, wenn kein info-Slot gesetzt ist, das info-Prop definiert ist und die Länge des Info-Werts nicht 0 ist", async () => {
		const wrapper = mount(UiCard, { props: { info: "Some Info" } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showInfo).toBeTruthy();
	});

	test("computed->showInfo liefert false, wenn ein info-Slot gesetzt ist", async () => {
		const wrapper = mount(UiCard, { slots: { info: "Custom Info Inhalt" }, props: { info: "Some Info" } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showInfo).toBeFalsy();
	});

	test("computed->showInfo liefert false, wenn das info-Prop nicht definiert ist", async () => {
		const wrapper = mount(UiCard);
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showInfo).toBeFalsy();
	});

	test("computed->showInfo liefert false, wenn das info-Prop gesetzt ist, aber die Länge des Info-Werts 0 ist", async () => {
		const wrapper = mount(UiCard, { props: { info: "" } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showInfo).toBeFalsy();
	});

	// showContentMain
	test("computed->showContentMain liefert true, wenn kein content-Slot gesetzt ist, das content-Prop definiert ist und die Länge des Inhalts nicht 0 ist", async () => {
		const wrapper = mount(UiCard, { props: { content: "Main Content" } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showContentMain).toBeTruthy();
	});

	test("computed->showContentMain liefert false, wenn ein content-Slot gesetzt ist", async () => {
		const wrapper = mount(UiCard, { slots: { default: "Custom Default Inhalt" }, props: { content: "Main Content" } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showContentMain).toBeFalsy();
	});

	test("computed->showContentMain liefert false, wenn das content-Prop nicht definiert ist", async () => {
		const wrapper = mount(UiCard);
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showContentMain).toBeFalsy();
	});

	test("computed->showContentMain liefert false, wenn das content-Prop gesetzt ist, aber die Länge des Inhalts 0 ist", async () => {
		const wrapper = mount(UiCard, { props: { content: "" } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showContentMain).toBeFalsy();
	});

	// showContentLeftButton
	test("computed->showContentLeftButton liefert true, wenn kein buttonContentLeft-Slot gesetzt ist, Buttons definiert sind, die Buttonposition links ist und der Buttoncontainer content ist", async () => {
		const wrapper = mount(UiCard, { props: { buttonPosition: 'left', buttonContainer: 'content', onEdit: () => {} }, attachTo: document.body });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showContentLeftButton).toBeTruthy();
	});

	test("computed->showContentLeftButton liefert false, wenn ein buttonContentLeft-Slot gesetzt ist", async () => {
		const wrapper = mount(UiCard, { slots: { buttonContentLeft: "Custom Button Content Left" }, props: { buttonPosition: 'left', buttonContainer: 'content', onEdit: () => {} }, attachTo: document.body });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showContentLeftButton).toBeFalsy();
	});

	test("computed->showContentLeftButton liefert false, wenn keine Buttons definiert sind", async () => {
		const wrapper = mount(UiCard, { props: { buttonPosition: 'left', buttonContainer: 'content' } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showContentLeftButton).toBeFalsy();
	});

	test("computed->showContentLeftButton liefert false, wenn buttonPosition nicht links ist", async () => {
		const wrapper = mount(UiCard, { props: { buttonPosition: 'right', buttonContainer: 'content', onEdit: () => {} }, attachTo: document.body });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showContentLeftButton).toBeFalsy();
	});

	test("computed->showContentLeftButton liefert false, wenn buttonContainer nicht content ist", async () => {
		const wrapper = mount(UiCard, { props: { buttonPosition: 'left', buttonContainer: 'footer', onEdit: () => {} }, attachTo: document.body });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showContentLeftButton).toBeFalsy();
	});

	// showContentRightButton
	test("computed->showContentRightButton liefert true, wenn kein buttonContentRight-Slot gesetzt ist, Buttons definiert sind, der Buttonposition rechts ist und der Buttoncontainer content ist", async () => {
		const wrapper = mount(UiCard, { props: { buttonPosition: 'right', buttonContainer: 'content', onEdit: () => {} }, attachTo: document.body });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showContentRightButton).toBeTruthy();
	});

	test("computed->showContentRightButton liefert false, wenn ein buttonContentRight-Slot gesetzt ist", async () => {
		const wrapper = mount(UiCard, { slots: { buttonContentRight: "Custom Button Content Right" }, props: { buttonPosition: 'right', buttonContainer: 'content', onEdit: () => {} }, attachTo: document.body });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showContentRightButton).toBeFalsy();
	});

	test("computed->showContentRightButton liefert false, wenn keine Buttons definiert sind", async () => {
		const wrapper = mount(UiCard, { props: { buttonPosition: 'right', buttonContainer: 'content' } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showContentRightButton).toBeFalsy();
	});

	test("computed->showContentRightButton liefert false, wenn buttonPosition nicht rechts ist", async () => {
		const wrapper = mount(UiCard, { props: { buttonPosition: 'left', buttonContainer: 'content', onEdit: () => {} }, attachTo: document.body });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showContentRightButton).toBeFalsy();
	});

	test("computed->showContentRightButton liefert false, wenn buttonContainer nicht content ist", async () => {
		const wrapper = mount(UiCard, { props: { buttonPosition: 'right', buttonContainer: 'footer', onEdit: () => {} }, attachTo: document.body });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showContentRightButton).toBeFalsy();
	});

	// showFooterDivider
	test("computed->showFooterDivider liefert true, wenn kein footerDivider-Slot gesetzt ist und die Prop showDivider true ist", async () => {
		const wrapper = mount(UiCard, { props: { showDivider: true } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showFooterDivider).toBeTruthy();
	});

	test("computed->showFooterDivider liefert false, wenn ein footerDivider-Slot gesetzt ist", async () => {
		const wrapper = mount(UiCard, { slots: { footerDivider: "Custom Divider Content" }, props: { showDivider: true } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showFooterDivider).toBeFalsy();
	});

	test("computed->showFooterDivider liefert false, wenn Props showDivider false ist", async () => {
		const wrapper = mount(UiCard, { props: { showDivider: false } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showFooterDivider).toBeFalsy();
	});

	// showFooter
	test("computed->showFooter liefert true, wenn ein footer-Slot gesetzt ist", async () => {
		const wrapper = mount(UiCard, { slots: { footer: "Custom Footer Content" } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showFooter).toBeTruthy();
	});

	test("computed->showFooter liefert true, wenn ein buttonFooterLeft-Slot gesetzt ist", async () => {
		const wrapper = mount(UiCard, { slots: { buttonFooterLeft: "Custom Button Footer Left" } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showFooter).toBeTruthy();
	});

	test("computed->showFooter liefert true, wenn ein buttonFooterRight-Slot gesetzt ist", async () => {
		const wrapper = mount(UiCard, { slots: { buttonFooterRight: "Custom Button Footer Right" } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showFooter).toBeTruthy();
	});

	test("computed->showFooter liefert true, wenn props.footer gesetzt und nicht leer ist", async () => {
		const wrapper = mount(UiCard, { props: { footer: "Non-empty footer" } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showFooter).toBeTruthy();
	});

	test("computed->showFooter liefert true, wenn props.buttonContainer 'footer' ist und Buttons definiert sind", async () => {
		const wrapper = mount(UiCard, { props: { buttonContainer: 'footer', onEdit: () => {} }, attachTo: document.body });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showFooter).toBeTruthy();
	});

	test("computed->showFooter liefert false, wenn kein footer-Slot und kein buttonFooterSlot gesetzt ist, wenn keine Buttons definiert sind und props.footer leer ist", async () => {
		const wrapper = mount(UiCard, { props: { footer: "" } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showFooter).toBeFalsy();
	});

	test("computed->showFooter liefert false, wenn kein footer-Slot und kein buttonFooterSlot gesetzt ist, wenn Buttons definiert sind, aber im Content angezeigt werden und props.footer leer ist", async () => {
		const wrapper = mount(UiCard, { props: { footer: "", buttonContainer: 'content', onEdit: () => {} }, attachTo: document.body });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showFooter).toBeFalsy();
	});

	test("computed->showFooter liefert false, wenn kein footer-Slot und kein buttonFooterSlot gesetzt ist, wenn keine Buttons definiert sind und props.footer undefiniert ist", async () => {
		const wrapper = mount(UiCard);
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showFooter).toBeFalsy();
	});

	test("computed->showFooter liefert false, wenn kein footer-Slot und kein buttonFooterSlot gesetzt ist, wenn Buttons definiert sind, aber im Content angezeigt werden und props.footer undefiniert ist", async () => {
		const wrapper = mount(UiCard, { props: { buttonContainer: 'content', onEdit: () => {} }, attachTo: document.body });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showFooter).toBeFalsy();
	});

	// showFooterRightButton
	test("computed->showFooterRightButton liefert true, wenn kein buttonFooterRight-Slot gesetzt ist, Buttons definiert sind, der Buttoncontainer footer ist und der Buttonposition rechts ist", async () => {
		const wrapper = mount(UiCard, { props: { buttonContainer: 'footer', buttonPosition: 'right', onEdit: () => {} }, attachTo: document.body });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showFooterRightButton).toBeTruthy();
	});

	test("computed->showFooterRightButton liefert false, wenn ein buttonFooterRight-Slot gesetzt ist", async () => {
		const wrapper = mount(UiCard, { slots: { buttonFooterRight: "Custom Button Footer Right" }, props: { buttonContainer: 'footer', buttonPosition: 'right', onEdit: () => {} }, attachTo: document.body });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showFooterRightButton).toBeFalsy();
	});

	test("computed->showFooterRightButton liefert false, wenn keine Buttons definiert sind", async () => {
		const wrapper = mount(UiCard, { props: { buttonContainer: 'footer', buttonPosition: 'right' } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showFooterRightButton).toBeFalsy();
	});

	test("computed->showFooterRightButton liefert false, wenn buttonContainer nicht footer ist", async () => {
		const wrapper = mount(UiCard, { props: { buttonContainer: 'content', buttonPosition: 'right', onEdit: () => {} }, attachTo: document.body });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showFooterRightButton).toBeFalsy();
	});

	// showFooterMain
	test("computed->showFooterMain liefert true, wenn kein footer-Slot gesetzt ist und props.footer nicht leer ist", async () => {
		const wrapper = mount(UiCard, { props: { footer: "Non-empty footer" } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showFooterMain).toBeTruthy();
	});

	test("computed->showFooterMain liefert false, wenn ein footer-Slot gesetzt ist", async () => {
		const wrapper = mount(UiCard, { slots: { footer: "Custom Footer Content" }, props: { footer: "Non-empty footer" } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showFooterMain).toBeFalsy();
	});

	test("computed->showFooterMain liefert false, wenn props.footer leer ist", async () => {
		const wrapper = mount(UiCard, { props: { footer: "" } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showFooterMain).toBeFalsy();
	});

	test("computed->showFooterMain liefert false, wenn props.footer nicht gesetzt ist", async () => {
		const wrapper = mount(UiCard, { props: {} });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showFooterMain).toBeFalsy();
	});

	// showFooterLeftButton
	test("computed->showFooterLeftButton liefert true, wenn kein buttonFooterLeft-Slot gesetzt ist, Buttons definiert sind, der Buttoncontainer footer ist und der Buttonposition links ist", async () => {
		const wrapper = mount(UiCard, { props: { buttonContainer: 'footer', buttonPosition: 'left', onEdit: () => {} }, attachTo: document.body });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showFooterLeftButton).toBeTruthy();
	});

	test("computed->showFooterLeftButton liefert false, wenn ein buttonFooterLeft-Slot gesetzt ist", async () => {
		const wrapper = mount(UiCard, { slots: { buttonFooterLeft: "Custom Button Footer Left" }, props: { buttonContainer: 'footer', buttonPosition: 'left', onEdit: () => {} }, attachTo: document.body });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showFooterLeftButton).toBeFalsy();
	});

	test("computed->showFooterLeftButton liefert false, wenn keine Buttons definiert sind", async () => {
		const wrapper = mount(UiCard, { props: { buttonContainer: 'footer', buttonPosition: 'left' } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showFooterLeftButton).toBeFalsy();
	});

	test("computed->showFooterLeftButton liefert false, wenn buttonContainer nicht footer ist", async () => {
		const wrapper = mount(UiCard, { props: { buttonContainer: 'content', buttonPosition: 'left', onEdit: () => {} }, attachTo: document.body });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showFooterLeftButton).toBeFalsy();
	});

	// showButtons
	test("computed->showButtons liefert true, wenn Buttons definiert sind", async () => {
		const wrapper = mount(UiCard, { props: { onEdit: () => {} }, attachTo: document.body });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showButtons).toBeTruthy();
	});

	test("computed->showButtons liefert false, wenn keine Buttons definiert sind", async () => {
		const wrapper = mount(UiCard);
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.showButtons).toBeFalsy();
	});

	// buttonContainerId
	test("computed->buttonContainerId liefert undefined, wenn keine Buttons definiert sind", async () => {
		const wrapper = mount(UiCard);
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.buttonContainerId).toBeUndefined();
	});

	test("computed->buttonContainerId liefert '#ui-card--button-content-left', wenn Buttons definiert sind und buttonContainer content und buttonPosition links ist", async () => {
		const wrapper = mount(UiCard, { props: { onEdit: () => {}, buttonContainer: 'content', buttonPosition: 'left' }, attachTo: document.body });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.buttonContainerId).toBe("#ui-card--button-content-left");
	});

	test("computed->buttonContainerId liefert '#ui-card--button-content-right', wenn Buttons definiert sind und buttonContainer content und buttonPosition rechts ist", async () => {
		const wrapper = mount(UiCard, { props: { onEdit: () => {}, buttonContainer: 'content', buttonPosition: 'right' }, attachTo: document.body });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.buttonContainerId).toBe("#ui-card--button-content-right");
	});

	test("computed->buttonContainerId liefert '#ui-card--button-footer-left', wenn Buttons definiert sind und buttonContainer footer und buttonPosition links ist", async () => {
		const wrapper = mount(UiCard, { props: { onEdit: () => {}, buttonContainer: 'footer', buttonPosition: 'left' }, attachTo: document.body });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.buttonContainerId).toBe("#ui-card--button-footer-left");
	});

	test("computed->buttonContainerId liefert '#ui-card--button-footer-right', wenn Buttons definiert sind und buttonContainer footer und buttonPosition rechts ist", async () => {
		const wrapper = mount(UiCard, { props: { onEdit: () => {}, buttonContainer: 'footer', buttonPosition: 'right' }, attachTo: document.body });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.buttonContainerId).toBe("#ui-card--button-footer-right");
	});

	// ariaExpanded
	test("computed->ariaExpanded liefert true, wenn collapsible true ist und isActive true ist", async () => {
		const wrapper = mount(UiCard, { props: { collapsible: true, isOpen: true } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.isActive).toBe(true);
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.ariaExpanded).toBe(true);
	});

	test("computed->ariaExpanded liefert false, wenn collapsible true ist und isActive false ist", async () => {
		const wrapper = mount(UiCard, { props: { collapsible: true, isOpen: false } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.isActive).toBe(false);
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.ariaExpanded).toBe(false);
	});

	test("computed->ariaExpanded liefert undefined, wenn collapsible false ist und isActive true ist", async () => {
		const wrapper = mount(UiCard, { props: { collapsible: false, isOpen: true } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.isActive).toBe(true);
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.ariaExpanded).toBeUndefined();
	});

	test("computed->ariaExpanded liefert undefined, wenn collapsible false ist und isActive false ist", async () => {
		const wrapper = mount(UiCard, { props: { collapsible: false, isOpen: false } });
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.isActive).toBe(false);
		expect(await wrapper.findComponent({ name: "UiCard" }).vm.ariaExpanded).toBeUndefined();
	});

	// buttons
	const buttonCombinations: Array<[string, string[], Record<string, () => void>]> = [
		["onEdit", ["Bearbeiten"], { 'on-edit': () => {} }],
		["onEdit und onSave", ["Bearbeiten", "Speichern"], { 'on-edit': () => {}, 'on-save': () => {} }],
		["onEdit und onCancel", ["Bearbeiten", "Abbrechen"], { 'on-edit': () => {}, 'on-cancel': () => {} }],
		["onEdit und onDelete", ["Bearbeiten", "Löschen"], { 'on-edit': () => {}, 'on-delete': () => {} }],
		["onEdit, onSave und onCancel", ["Bearbeiten", "Speichern", "Abbrechen"], { 'on-edit': () => {}, 'on-save': () => {}, 'on-cancel': () => {} }],
		["onEdit, onSave und onDelete", ["Bearbeiten", "Speichern", "Löschen"], { 'on-edit': () => {}, 'on-save': () => {}, 'on-delete': () => {} }],
		["onEdit, onCancel und onDelete", ["Bearbeiten", "Abbrechen", "Löschen"], { 'on-edit': () => {}, 'on-cancel': () => {}, 'on-delete': () => {} }],
		["onSave", ["Speichern"], { 'on-save': () => {} }],
		["onSave und onCancel", ["Speichern", "Abbrechen"], { 'on-save': () => {}, 'on-cancel': () => {} }],
		["onSave und onDelete", ["Speichern", "Löschen"], { 'on-save': () => {}, 'on-delete': () => {} }],
		["onSave, onCancel und onDelete", ["Speichern", "Abbrechen", "Löschen"], { 'on-save': () => {}, 'on-cancel': () => {}, 'on-delete': () => {} }],
		["onCancel", ["Abbrechen"], { 'on-cancel': () => {} }],
		["onCancel und onDelete", ["Abbrechen", "Löschen"], { 'on-cancel': () => {}, 'on-delete': () => {} }],
		["onDelete", ["Löschen"], { 'on-delete': () => {} }],
		["onEdit, onSave, onCancel und onDelete", ["Bearbeiten", "Speichern", "Abbrechen", "Löschen"], { 'on-edit': () => {}, 'on-save': () => {}, 'on-cancel': () => {}, 'on-delete': () => {} }],
	];

	test.each(buttonCombinations)(
		`Mit Props Button Definitionen für %s beinhaltet computed->buttons folgende Buttons: %s`,
		async (functions, buttonLabels, functionProps) => {
			const wrapper = mount(UiCard, {	props: { ...functionProps }, attachTo: document.body });
			const buttons = await wrapper.findComponent({ name: "UiCard" }).vm.buttons;
			expect(buttons.length).toBe(buttonLabels.length);

			buttonLabels.forEach(label => {
				expect(buttons.some((button: { label: string; }) => button.label === label)).toBeTruthy();
			});
		}
	);

});

describe.concurrent("Slots werden korrekt gerendert", () => {
	const slots = [
		{ name: 'collapseLeft', content: 'Slot Inhalt für collapseLeft' },
		{ name: 'icon', content: 'Slot Inhalt für icon' },
		{ name: 'title', content: 'Slot Inhalt für title' },
		{ name: 'subtitle', content: 'Slot Inhalt für subtitle' },
		{ name: 'info', content: 'Slot Inhalt für info' },
		{ name: 'collapseRight', content: 'Slot Inhalt für collapseRight' },
		{ name: 'buttonContentLeft', content: 'Slot Inhalt für buttonContentLeft' },
		{ name: 'default', content: 'Slot Inhalt für default' },
		{ name: 'buttonContentRight', content: 'Slot Inhalt für buttonContentRight' },
		{ name: 'footerDivider', content: 'Slot Inhalt für footerDivider' },
		{ name: 'buttonFooterLeft', content: 'Slot Inhalt für buttonFooterLeft' },
		{ name: 'footer', content: 'Slot Inhalt für footer' },
		{ name: 'buttonFooterRight', content: 'Slot Inhalt für buttonFooterRight' },
	];

	test.each(slots)("Slot $name wird korrekt gerendert", ({ name, content }) => {
		const wrapper = mount(UiCard, { slots: { [name]: content } });
		expect(wrapper.html()).toContain(content);
	});
});

describe.concurrent("Funktionen werden korrekt ausgeführt", async () => {

	test("function-> openCard (Card noch geschlossen) setzt die maxHeight der Card auf die scrollHeight", async () => {
		const wrapper = mount(UiCard, { props: { isOpen: false } });

		const openCard = await wrapper.findComponent({
			name: "UiCard",
		}).vm.openCard;
		const bodyWrapper = wrapper.find(".ui-card--body-wrapper");
		const bodyWrapperHtml = bodyWrapper.element as HTMLElement;

		/**
		 * Der folgende Test wird durchgeführt, weil in Vitest die scrollHeight immer 0 ist. Dadurch lässt sich nicht unterscheiden, ob nach openCard die
		 * maxHeight einfach noch dem Anfangszustand von 0px beim Mounten entspricht oder ob sie explizit auf die scrollHeight von 0px gesetzt wurde.
		 * Das vorherige setzen der maxHeight auf etwas anderes als 0px stellt sicher, dass der Wert maxHeight: 0px durch openCard gesetzt wurde.
		 */
		expect(bodyWrapper.attributes("style")).toContain("max-height: 0px");
		bodyWrapperHtml.style.maxHeight = "300px";
		expect(bodyWrapper.attributes("style")).toContain("max-height: 300px");

		// Hier wird openCard tatsächlich ausgeführt und geprüft, ob die maxHeight gesetzt wurde
		await openCard(bodyWrapperHtml);
		expect(bodyWrapper.attributes("style")).toContain("max-height: "+ bodyWrapperHtml.scrollHeight + "px");
	});

	test("function-> openCard (Card schon geöffnet) bricht die Funktion ab und tut nichts", async () => {
		const wrapper = mount(UiCard, { props: { isOpen: true } });

		const openCard = await wrapper.findComponent({
			name: "UiCard",
		}).vm.openCard;
		const bodyWrapper = wrapper.find(".ui-card--body-wrapper");
		const bodyWrapperHtml = bodyWrapper.element as HTMLElement;


		expect(bodyWrapper.attributes("style")).toContain("max-height: fit-content");
		await openCard(bodyWrapperHtml);
		expect(bodyWrapper.attributes("style")).toContain("max-height: fit-content");
	});

	test("function-> afterOpenCard setzt die maxHeight des Bodys auf 'fit-content'", async () => {
		const wrapper = mount(UiCard);
		const afterOpenCard = await wrapper.findComponent({
			name: "UiCard",
		}).vm.afterOpenCard;
		const bodyWrapper = wrapper.find(".ui-card--body-wrapper");
		const bodyWrapperHtml = bodyWrapper.element as HTMLElement;

		bodyWrapperHtml.style.maxHeight = "300px";
		expect(bodyWrapper.attributes("style")).toContain("max-height: 300px");
		afterOpenCard(bodyWrapperHtml);
		expect(bodyWrapper.attributes("style")).toContain("max-height: fit-content");
	})

	test("function-> beforeCloseCard setzt die maxHeight des Bodys auf die scrollHeight", async () => {
		const wrapper = mount(UiCard);

		const beforeCloseCard = await wrapper.findComponent({
			name: "UiCard",
		}).vm.beforeCloseCard;
		const bodyWrapper = wrapper.find(".ui-card--body-wrapper");
		const bodyWrapperHtml = bodyWrapper.element as HTMLElement;

		/**
		 * Der folgende Test wird durchgeführt, weil in Vitest die scrollHeight immer 0 ist. Dadurch lässt sich nicht unterscheiden, ob nach openCard die
		 * maxHeight einfach noch dem Anfangszustand von 0px beim Mounten entspricht oder ob sie explizit auf die scrollHeight von 0px gesetzt wurde.
		 * Das vorherige setzen der maxHeight auf etwas anderes als 0px stellt sicher, dass der Wert maxHeight: 0px durch beforeCloseCard gesetzt wurde.
		 */
		expect(bodyWrapper.attributes("style")).toContain("max-height: 0px");
		bodyWrapperHtml.style.maxHeight = "300px";
		expect(bodyWrapper.attributes("style")).toContain("max-height: 300px");

		// Hier wird beforeCloseCard tatsächlich ausgeführt und geprüft, ob die maxHeight gesetzt wurde
		await beforeCloseCard(bodyWrapperHtml);
		expect(bodyWrapper.attributes("style")).toContain("max-height: "+ bodyWrapperHtml.scrollHeight + "px");
	});

	test("function-> closeCard setzt die maxHeight der Card auf 0px", async () => {
		const wrapper = mount(UiCard, { props: { isOpen: false } });

		const closeCard = await wrapper.findComponent({
			name: "UiCard",
		}).vm.closeCard;
		const bodyWrapper = wrapper.find(".ui-card--body-wrapper");
		const bodyWrapperHtml = bodyWrapper.element as HTMLElement;

		/**
		 * Der folgende Test wird durchgeführt, weil in Vitest die scrollHeight immer 0 ist. Dadurch lässt sich nicht unterscheiden, ob nach closeCard die
		 * maxHeight einfach noch der scrollHeight von 0px entspricht oder ob sie explizit auf 0px gesetzt wurde.
		 * Das vorherige setzen der maxHeight auf etwas anderes als 0px stellt sicher, dass der Wert maxHeight: 0px durch closeCard gesetzt wurde.
		 */
		expect(bodyWrapper.attributes("style")).toContain("max-height: 0px");
		bodyWrapperHtml.style.maxHeight = "300px";
		expect(bodyWrapper.attributes("style")).toContain("max-height: 300px");

		// Hier wird openCard tatsächlich ausgeführt und geprüft, ob die maxHeight gesetzt wurde
		await closeCard(bodyWrapperHtml);
		expect(bodyWrapper.attributes("style")).toContain("max-height: 0px");
	});

	test("function-> setActive setzt isActive auf true, wenn der Funktion true mitgegeben wird", async () => {
		const wrapper = mount(UiCard);
		const setActive = await wrapper.findComponent({
			name: "UiCard",
		}).vm.setActive;
		setActive(true);
		expect((wrapper.vm as any).isActive).toBe(true);
	});

	test("function-> setActive setzt isActive auf false, wenn der Funktion false mitgegeben wird", async () => {
		const wrapper = mount(UiCard);
		const setActive = await wrapper.findComponent({
			name: "UiCard",
		}).vm.setActive;
		setActive(false);
		expect((wrapper.vm as any).isActive).toBe(false);
	});

	test("function-> setActive toggelt isActive, wenn kein Parameter mitgegeben wurde", async () => {
		const wrapper = mount(UiCard, { props: { isOpen: true } });
		const setActive = await wrapper.findComponent({
			name: "UiCard",
		}).vm.setActive;
		setActive();
		expect((wrapper.vm as any).isActive).toBe(false);
		setActive();
		expect((wrapper.vm as any).isActive).toBe(true);
	});

	test("function-> tooltipDisabled gibt false zurück, wenn der 'buttonMode: icon' entspricht", async () => {
		const wrapper = mount(UiCard, { props: { buttonMode: 'icon' } });
		const tooltipDisabled = await wrapper.findComponent({
			name: "UiCard",
		}).vm.tooltipDisabled;
		const result = tooltipDisabled({
			type: 'primary', label: 'Speichern', icon: 'i-ri-check-line', iconType: 'icon-primary', disabled: false,
			disabledReason: undefined, click: () => {},
		})

		expect(result).toBe(false);
	});

	test("function-> tooltipDisabled gibt false zurück, wenn der Button disabled ist und ein 'disabledReason' angegeben ist", async () => {
		const wrapper = mount(UiCard, { props: { buttonMode: 'text' } });
		const tooltipDisabled = await wrapper.findComponent({
			name: "UiCard",
		}).vm.tooltipDisabled;
		const result = tooltipDisabled({
			type: 'primary', label: 'Speichern', icon: 'i-ri-check-line', iconType: 'icon-primary', disabled: true,
			disabledReason: 'reason', click: () => {},
		});

		expect(result).toBe(false);
	});

	test("function-> tooltipDisabled gibt false zurück, wenn der Button 'buttonMode: icon' hat, auch wenn disabled = false und 'disabledReason' nicht angegeben ist", async () => {
		const wrapper = mount(UiCard, { props: { buttonMode: 'icon' } });
		const tooltipDisabled = await wrapper.findComponent({
			name: "UiCard",
		}).vm.tooltipDisabled;
		const result = tooltipDisabled({
			type: 'primary', label: 'Speichern', icon: 'i-ri-check-line', iconType: 'icon-primary', disabled: false,
			disabledReason: undefined, click: () => {},
		});

		expect(result).toBe(false);
	});

	test("function-> tooltipDisabled gibt true zurück, wenn der Button disabled ist, aber kein 'disabledReason' angegeben ist", async () => {
		const wrapper = mount(UiCard, { props: { buttonMode: 'text' } });
		const tooltipDisabled = await wrapper.findComponent({
			name: "UiCard",
		}).vm.tooltipDisabled;
		const result = tooltipDisabled({
			type: 'primary', label: 'Speichern', icon: 'i-ri-check-line', iconType: 'icon-primary', disabled: true,
			disabledReason: undefined, click: () => {},
		});

		expect(result).toBe(true);
	});

	test("function-> tooltipDisabled gibt true zurück, wenn disabled = false ist, aber ein 'disabledReason' angegeben ist", async () => {
		const wrapper = mount(UiCard, { props: { buttonMode: 'text' } });
		const tooltipDisabled = await wrapper.findComponent({
			name: "UiCard",
		}).vm.tooltipDisabled;
		const result = tooltipDisabled({
			type: 'primary', label: 'Speichern', icon: 'i-ri-check-line', iconType: 'icon-primary', disabled: false,
			disabledReason: 'reason', click: () => {},
		});

		expect(result).toBe(true);
	});


	test("function-> tooltipDisabled gibt true zurück, wenn disabled = false ist und der 'buttonMode' nicht 'icon' ist", async () => {
		const wrapper = mount(UiCard, { props: { buttonMode: 'text' } });
		const tooltipDisabled = await wrapper.findComponent({
			name: "UiCard",
		}).vm.tooltipDisabled;
		const result = tooltipDisabled({
			type: 'primary', label: 'Speichern', icon: 'i-ri-check-line', iconType: 'icon-primary', disabled: false,
			disabledReason: 'reason', click: () => {},
		});

		expect(result).toBe(true);
	});

	test("function-> tooltipDisabled gibt true zurück, wenn 'disabledReason' undefined ist und 'buttonMode' nicht 'icon' ist", async () => {
		const wrapper = mount(UiCard, { props: { buttonMode: 'text' } });
		const tooltipDisabled = await wrapper.findComponent({
			name: "UiCard",
		}).vm.tooltipDisabled;
		const result = tooltipDisabled({
			type: 'primary', label: 'Speichern', icon: 'i-ri-check-line', iconType: 'icon-primary', disabled: true,
			disabledReason: undefined, click: () => {},
		});

		expect(result).toBe(true);
	});

});

/**
 * Button Tests dürfen nicht concurrent ausgeführt werden, da diese sich gegenseitig beeinflussen können.
 */
describe("Übergebene Button-Funktionen werden korrekt ausgeführt", async () => {
	test("Die übergebene onEdit Funktion wird bei Klick auf den Edit Button ausgeführt", async () => {
		const onEditFunction = vi.fn();

		const wrapper = mount(UiCard, { props: { onEdit: onEditFunction }, attachTo: document.body });
		const button = wrapper.find(".ui-card--buttons").findAll("button").find(button => button.text() === "Bearbeiten");
		if(button === undefined)
			fail("Button darf nicht undefined sein");
		await button.trigger("click");

		expect(onEditFunction).toHaveBeenCalled();
	});

	test("Die übergebene onSave Funktion wird bei Klick auf den Save Button ausgeführt", async () => {
		const onSaveFunction = vi.fn();

		const wrapper = mount(UiCard, { props: { onSave: onSaveFunction }, attachTo: document.body });
		const button = wrapper.find(".ui-card--buttons").findAll("button").find(button => button.text() === "Speichern");
		if(button === undefined)
			fail("Button darf nicht undefined sein");
		await button.trigger("click");

		expect(onSaveFunction).toHaveBeenCalled();
	});

	test("Die übergebene onDelete Funktion wird bei Klick auf den Delete Button ausgeführt", async () => {
		const onDeleteFunction = vi.fn();

		const wrapper = mount(UiCard, { props: { onDelete: onDeleteFunction }, attachTo: document.body });
		const button = wrapper.find(".ui-card--buttons").findAll("button").find(button => button.text() === "Löschen");
		if(button === undefined)
			fail("Button darf nicht undefined sein");
		await button.trigger("click");

		expect(onDeleteFunction).toHaveBeenCalled();
	});

	test("Die übergebene onCancel Funktion wird bei Klick auf den Cancel Button ausgeführt", async () => {
		const onCancelFunction = vi.fn();

		const wrapper = mount(UiCard, { props: { onCancel: onCancelFunction }, attachTo: document.body });
		const button = wrapper.find(".ui-card--buttons").findAll("button").find(button => button.text() === "Abbrechen");
		if(button === undefined)
			fail("Button darf nicht undefined sein");
		await button.trigger("click");

		expect(onCancelFunction).toHaveBeenCalled();
	});

});

/**
 * Button Tests dürfen nicht concurrent ausgeführt werden, da diese sich gegenseitig beeinflussen können.
 */
describe("Aria Attribute werden korrekt gesetzt", async () => {
	const labelButtons: Array<[string, string, Record<string, () => void>]> = [
		["onEdit", "Bearbeiten", { 'on-edit': () => {} }],
		["onSave", "Speichern", { 'on-save': () => {} }],
		["onDelete", "Löschen", { 'on-delete': () => {} }],
		["onCancel", "Abbrechen", { 'on-cancel': () => {} }],
	];

	test.each(labelButtons)("Mit Button Definitionen für %s ist das aria-label= '%s'", async (functions, label: string, functionProps) => {
		const wrapper = mount(UiCard, {	props: { buttonMode: 'icon', compact: true, ...functionProps }, attachTo: document.body });
		const button = wrapper.find(".ui-card--button");
		expect(button.attributes("aria-label")).toBe("Button " + label);
	});

	test("Das Attribut aria-expanded im Header ist true, wenn das computed ariaExpanded true ist", async () => {
		// Der Wert von ariaExpanded wird durch isOpen beeinflusst und in diesem Fall true
		const wrapper = mount(UiCard, { props: { isOpen: true } });
		await wrapper.findComponent({ name: "UiCard" }).vm.$nextTick();
		const ariaExpanded = wrapper.find(".ui-card--header").attributes("aria-expanded");
		expect(ariaExpanded).toBeDefined();
		expect(ariaExpanded).toBe("true");
	});

	test("Das Attribut aria-expanded im Header ist false, wenn das computed ariaExpanded false ist", async () => {
		// Der Wert von ariaExpanded wird durch isOpen beeinflusst und in diesem Fall false
		const wrapper = mount(UiCard, { props: { isOpen: false } });
		await wrapper.findComponent({ name: "UiCard" }).vm.$nextTick();
		const ariaExpanded = wrapper.find(".ui-card--header").attributes("aria-expanded");
		expect(ariaExpanded).toBeDefined();
		expect(ariaExpanded).toBe("false");
	});

	test("Das Attribut aria-controls im Header besteht aus dem String 'cardBody' und der InstanzID, wenn 'collapsible' = true ist", async () => {
		const wrapper = mount(UiCard, { props: { collapsible: true } });
		await wrapper.findComponent({ name: "UiCard" }).vm.$nextTick();
		const ariaControls = wrapper.find(".ui-card--header").attributes("aria-controls");
		const instanceId = await wrapper.findComponent({ name: "UiCard" }).vm.instanceId;
		expect(ariaControls).toBeDefined();
		expect(ariaControls).toContain("cardBody" + instanceId);
	});

	test("Das Attribut aria-controls im Header ist undefined, wenn 'collapsible' = false ist", async () => {
		const wrapper = mount(UiCard, { props: { collapsible: false } });
		await wrapper.findComponent({ name: "UiCard" }).vm.$nextTick();
		const ariaControls = wrapper.find(".ui-card--header").attributes("aria-controls");
		expect(ariaControls).not.toBeDefined();
	});

	test("Das Attribut aria-label im collapse-Icon links ist 'Card geschlossen', wenn 'isActive' = false ist", async () => {
		// Der Wert von isActive wird durch isOpen beeinflusst und in diesem Fall false
		const wrapper = mount(UiCard, { props: { isOpen: false, collapseIconPosition: 'left' } });
		await wrapper.findComponent({ name: "UiCard" }).vm.$nextTick();
		const ariaControls = wrapper.find(".ui-card--header--collapse-icon").find("span").attributes("aria-label");
		expect(ariaControls).toBeDefined();
		expect(ariaControls).toBe("Card geschlossen");
	});

	test("Das Attribut aria-label im collapse-Icon links ist 'Card geöffnet', wenn 'isActive' = true ist ", async () => {
		// Der Wert von isActive wird durch isOpen beeinflusst und in diesem Fall true
		const wrapper = mount(UiCard, { props: { isOpen: true, collapseIconPosition: 'left' } });
		await wrapper.findComponent({ name: "UiCard" }).vm.$nextTick();
		const ariaControls = wrapper.find(".ui-card--header--collapse-icon").find("span").attributes("aria-label");
		expect(ariaControls).toBeDefined();
		expect(ariaControls).toBe("Card geöffnet");
	});

	test("Das Attribut aria-label im collapse-Icon rechts ist 'Card geschlossen', wenn 'isActive' = false ist", async () => {
		// Der Wert von isActive wird durch isOpen beeinflusst und in diesem Fall false
		const wrapper = mount(UiCard, { props: { isOpen: false, collapseIconPosition: 'right' } });
		await wrapper.findComponent({ name: "UiCard" }).vm.$nextTick();
		const ariaControls = wrapper.find(".ui-card--header--collapse-icon").find("span").attributes("aria-label");
		expect(ariaControls).toBeDefined();
		expect(ariaControls).toBe("Card geschlossen");
	});

	test("Das Attribut aria-label im collapse-Icon rechts ist 'Card geöffnet', wenn 'isActive' = true ist ", async () => {
		// Der Wert von isActive wird durch isOpen beeinflusst und in diesem Fall true
		const wrapper = mount(UiCard, { props: { isOpen: true, collapseIconPosition: 'right' } });
		await wrapper.findComponent({ name: "UiCard" }).vm.$nextTick();
		const ariaControls = wrapper.find(".ui-card--header--collapse-icon").find("span").attributes("aria-label");
		expect(ariaControls).toBeDefined();
		expect(ariaControls).toBe("Card geöffnet");
	});

});
