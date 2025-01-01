import { type Ref, ref } from 'vue'

const focusSwitchingEnabled: Ref<boolean> = ref(false);
const focusHelpVisible: Ref<boolean> = ref(false);

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

	function switchRegion(event: KeyboardEvent) {
		if (event.repeat || !event.altKey)
			return
		if (regionMap.has(event.code)) {
			event.preventDefault();
			const htmlElement = document.getElementsByClassName(regionMap.get(event.code) ?? "").item(0);
			if (htmlElement !== null)
				(htmlElement as HTMLElement).focus();
		}
		else if (event.code === 'Digit0') {
			toggleHelp();
		}
	}

	function toggleHelp() {
		focusHelpVisible.value = !focusHelpVisible.value;
	}

	function enable() {
		window.addEventListener('keydown', switchRegion);
		focusSwitchingEnabled.value = true;
	}

	function disable() {
		window.removeEventListener('keydown', switchRegion);
		focusSwitchingEnabled.value = false;
	}

	return {
		focusHelpVisible,
		focusSwitchingEnabled,
		enable,
		disable,
	}
}
