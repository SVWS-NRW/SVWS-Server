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

	const regionBorders = ["navigationFocusBorder", "menuFocusBorder", "filterFocusBorder", "listFocusBorder", "tabsFirstLevelFocusBorder", "tabsSecondLevelFocusBorder", "tabsThirdLevelFocusBorder", "contentFocusBorder"];
	const regionNumbers = ["navigationFocusNumber", "menuFocusNumber", "filterFocusNumber", "listFocusNumber", "tabsFirstLevelFocusNumber", "tabsSecondLevelFocusNumber", "tabsThirdLevelFocusNumber","contentFocusNumber"]

	function switchRegion(event: KeyboardEvent) {
		if (event.repeat || !event.altKey)
			return
		if (regionMap.has(event.code)) {
			document.getElementById(regionMap.get(event.code) ?? "")?.focus();
		}
		else if (event.code === 'Digit0') {
			toggleHelp();
		}
	}

	function toggleHelp() {
		focusHelpVisible.value = !focusHelpVisible.value;
		for (let i=0; i<= regionBorders.length; i++) {
			const borderElement = document.getElementById(regionBorders[i]);
			const enumerationElement = document.getElementById(regionNumbers[i]);
			if (focusHelpVisible.value) {
				borderElement?.classList.add("highlighted");
				enumerationElement?.classList.add("highlighted");
			} else {
				borderElement?.classList.remove("highlighted");
				enumerationElement?.classList.remove("highlighted");
			}
		}
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
