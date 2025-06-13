import type { RouteRecord } from "vue-router";
import { StateManager } from "./StateManager";
import router from "~/router";

export interface ColorPreset { label: string, color: string, contrastColor: string };

interface Variant {
	id: string;
};

interface Story {
	id: string,
	mapVariants: Map<string, Variant>;
	variant?: Variant;
	color?: ColorPreset;
};

interface Stories {
	mapStories: Map<string, Story>;
	story?: Story;
}

const defaultState = <Stories> {
	mapStories: new Map(),
	story: undefined,
};

export class StoryManager extends StateManager<Stories> {
	public constructor(routes: RouteRecord[]) {
		super(defaultState);
	}

	get story() {
		return this._state.value.story;
	}
	setStory(props: {id: string}) {
		const mapStories = this._state.value.mapStories;
		if (!mapStories.has(props.id)) {
			mapStories.set(props.id, {id: props.id, mapVariants: new Map()});
		}
		this.setPatchedState({ story: mapStories.get(props.id), mapStories });
	}

	get variant() {
		if (this._state.value.story === undefined)
			throw new Error("Keine Story gesetzt");
		return this._state.value.story.variant;
	}
	setVariant(props: {id: string}) {
		const story = this.story;
		if (story === undefined)
			throw new Error("Keine Story gesetzt");
		const mapVariants = story.mapVariants;
		if (!mapVariants.has(props.id))
			mapVariants.set(props.id, props);
		story.variant = mapVariants.get(props.id);
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
}

const storyManager = new StoryManager(router.getRoutes());

export default storyManager;