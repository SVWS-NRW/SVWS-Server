import type { Slots } from "vue";
import { StateManager } from "../ui/StateManager";

export interface ColorPreset { label: string, color: string, contrastColor: string };
export type GridView = 'grid'|'single';
export interface VariantProps { id: string, title: string, source?: string }


export class Variant {
	readonly id: string;
	readonly title: string;
	readonly source?: string;
	readonly slots: Readonly<Slots>;

	constructor(props: VariantProps = { id: 'default', title: 'Default' }, slots: Readonly<Slots> = {}) {
		this.id = props.id;
		this.title = props.title;
		this.source = props.source;
		this.slots = slots;
	}

	hasSlot(name: string) {
		return name in this.slots;
	}

	get hasSource() {
		return this.hasSlot('source') || (this.source !== undefined);
	}
}

export class Story {
	readonly id: string;
	readonly mapVariants: Map<string, Variant> = new Map();
	protected _variant: Variant = new Variant();
	protected _color: ColorPreset|undefined;
	protected _events: string[] = [];

	constructor(id: string = 'default') {
		this.id = id;
	}

	get variant() {
		return this._variant;
	}
	setVariantById(id: string) {
		const variant = this.mapVariants.get(id);
		if (variant !== undefined)
			this._variant = variant;
		else
			this._variant = new Variant();
	}

	registerVariant(variant: Variant) {
		if (!this.mapVariants.has(variant.id))
			this.mapVariants.set(variant.id, variant);
	}

	get color() {
		return this._color;
	}
	set color(value) {
		this._color = value;
	}

	get events() {
		return this._events;
	}
	set events(value) {
		this._events = value;
	}
}


interface Stories {
	mapStories: Map<string, Story>;
	story: Story;
	gridView: GridView;
}

const defaultState = <Stories>{
	mapStories: new Map(),
	story: new Story(),
	gridView: 'grid',
	events: [],
};

export class StoryManager extends StateManager<Stories> {

	readonly mapStories: Map<string, Story> = new Map();

	public constructor() {
		super(defaultState);
	}

	get story() {
		return this._state.value.story;
	}

	setStoryByID(id: string) {
		if (!this.mapStories.has(id))
			this.mapStories.set(id, new Story(id));
		const story = this.mapStories.get(id);
		this.setPatchedState({ story });
	}

	get variant() {
		return this._state.value.story.variant;
	}
	setVariantById(id: string) {
		const story = this.story;
		story.setVariantById(id);
		this.setPatchedState({ story });
	}

	registerVariant(props: VariantProps, slots: Readonly<Slots>) {
		// console.log(props.id)
		const story = this.story;
		story.registerVariant(new Variant(props, slots));
		this.setPatchedState({ story });
	}

	get color() {
		return this._state.value.story.color;
	}
	set color(color) {
		const story = this._state.value.story;
		story.color = color;
		this.setPatchedState({ story });
	}

	get gridView() {
		return this._state.value.gridView;
	}
	set gridView(gridView) {
		this.setPatchedState({ gridView });
	}

	get events() {
		return this._state.value.story.events;
	}
	set events(events) {
		const story =	this.story;
		story.events = events;
		this.setPatchedState({ story });
	}
}

const storyManager = new StoryManager();

export default storyManager;