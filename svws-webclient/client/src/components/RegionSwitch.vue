<script setup lang="ts">
	import { onMounted, onUnmounted } from 'vue'

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

	let helpVisible: boolean = false;

	function switchRegion(event: KeyboardEvent) {

		if (event.repeat || !event.altKey)
			return

		if (regionMap.has(event.code))
			document.getElementById(regionMap.get(event.code) ?? "")?.focus();
		else if (event.code === 'Digit0')
			toggleHelp();
	}

	function toggleHelp() {

		for (let i=0; i<= regionBorders.length; i++) {

			const borderElement = document.getElementById(regionBorders[i]);
			const enumerationElement = document.getElementById(regionNumbers[i]);

			if (helpVisible) {
				borderElement?.classList.remove("highlighted");
				enumerationElement?.classList.remove("highlighted");
			} else {
				borderElement?.classList.add("highlighted");
				enumerationElement?.classList.add("highlighted");
			}
		}
		helpVisible = !helpVisible;
	}

	onMounted(() => window.addEventListener('keydown', switchRegion))
	onUnmounted(() => window.removeEventListener('keydown', switchRegion))

</script>
