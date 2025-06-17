import type { RouteRecord } from "vue-router";
import { StateManager } from "./StateManager";
import router from "~/router";

export interface ColorPreset { label: string, color: string, contrastColor: string };
export type GridView = 'grid'|'single';

export interface Variant {
	id: string;
	title: string;
};

export interface Story {
	id: string,
	mapVariants?: Map<string, Variant>;
	variant?: Variant;
	color?: ColorPreset;
};

interface Stories {
	mapStories: Map<string, Story>;
	story?: Story;
	gridView?: GridView;
}

const defaultState = <Stories> {
	mapStories: new Map(),
	story: undefined,
	gridView: 'grid',
};

export class StoryManager extends StateManager<Stories> {
	public constructor(routes: RouteRecord[]) {
		super(defaultState);
	}

	get story() {
		return this._state.value.story;
	}
	setStory(props: Story) {
		const mapStories = this._state.value.mapStories;
		if (!mapStories.has(props.id))
			mapStories.set(props.id, {id: props.id, mapVariants: new Map()});
		this.setPatchedState({ story: mapStories.get(props.id), mapStories });
	}

	get variant() {
		if (this._state.value.story === undefined)
			throw new Error("Keine Story gesetzt");
		return this._state.value.story.variant;
	}
	setVariant(props: Variant) {
		const story = this.story;
		if (story === undefined)
			throw new Error("Keine Story gesetzt");
		const mapVariants = story.mapVariants;
		if (mapVariants === undefined)
			throw new Error("mapVariants fehlt!")
		if (!mapVariants.has(props.id))
			mapVariants.set(props.id, props);
		story.variant = mapVariants.get(props.id);
		this.setPatchedState({ story });
	}

	registerVariant(props: Variant) {
		const story = this.story;
		if (story === undefined)
			throw new Error("Keine Story gesetzt");
		if (story.mapVariants === undefined)
			throw new Error("mapVariants fehlt!");
		if (story.mapVariants.has(props.id))
			return;
		story.mapVariants.set(props.id, props);
		if (story.mapVariants.size === 1)
			story.variant = props;
		this.setPatchedState({ story });
	}

	get color() {
		return this._state.value.story?.color;
	}
	set color(color) {
		const story = this._state.value.story;
		if (story === undefined)
			return;
		story.color = color;
		this.setPatchedState({ story });
	}

	get gridView() {
		return this._state.value.gridView;
	}
	set gridView(gridView) {
		this._state.value.gridView = gridView;
		this.setPatchedState({ gridView });
	}
}

const storyManager = new StoryManager(router.getRoutes());

export default storyManager;