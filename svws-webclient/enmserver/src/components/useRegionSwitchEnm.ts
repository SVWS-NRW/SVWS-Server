import { type Ref, ref } from 'vue'

const focusSwitchingEnabled = ref<boolean>(false);
const focusHelpVisible = ref<boolean>(false);

const currentContentIndex = ref(0);
const currentListIndex = ref(0);

export function useRegionSwitch() {

	const regionMap = new Map<string, string>([
		["Digit1", "navigationFocusField"],
		["Digit2", "menuFocusField"],
		["Digit3", "filterFocusField"],
		["Digit4", "listFocusField"],
		["Digit5", "tabsFirstLevelFocusField"],
		["Digit6", "tabsSecondLevelFocusField"],
		["Digit7", "tabsThirdLevelFocusField"],
		["Digit8", "contentFocusField"],
	]);

	function handleKeyEvent(event: KeyboardEvent) {
		if (event.repeat || !event.altKey)
			return
		if (regionMap.has(event.code))
			switchRegion(event);
		else if (event.code === 'Digit0')
			toggleHelp(event);
	}

	function switchRegion(event: KeyboardEvent) {
		event.preventDefault();
		const htmlElements = document.getElementsByClassName(regionMap.get(event.code) ?? "");
		if (event.code === 'Digit8')
			cycleFocusFields(htmlElements, currentContentIndex);
		else if (event.code === 'Digit4')
			cycleFocusFields(htmlElements, currentListIndex);
		else {
			const focusField = htmlElements.item(0)
			if (focusField !== null)
				(focusField as HTMLElement).focus();
			currentContentIndex.value = 0;
			currentListIndex.value = 0;
		}
	}

	function cycleFocusFields(focusFields: HTMLCollection, usedIdex: Ref<number>) {
		const focusField = focusFields.item(usedIdex.value);
		if (focusField !== null)
			(focusField as HTMLElement).focus();
		usedIdex.value = (usedIdex.value + 1) % focusFields.length;
	}

	function toggleHelp(event: KeyboardEvent) {
		event.preventDefault();
		focusHelpVisible.value = !focusHelpVisible.value;
	}

	function enable() {
		window.addEventListener('keydown', handleKeyEvent);
		focusSwitchingEnabled.value = true;
	}

	function disable() {
		window.removeEventListener('keydown', handleKeyEvent);
		focusSwitchingEnabled.value = false;
	}

	return {
		focusHelpVisible,
		focusSwitchingEnabled,
		enable,
		disable,
	}
}
