<template>
	<div class="flex h-full min-h-0 flex-col">
		<div class="shrink-0 px-6 pt-3">
			<nav class="svws-ui-secondary-tabs mt-0" aria-label="Räume und Raumgruppen">
				<svws-ui-tab-bar :tab-manager secondary :focus-switching-enabled :focus-help-visible />
			</nav>
		</div>
		<s-uv-auswahl-layout v-if="tabManager().tab.tabContentHatAuswahl" class="min-h-0 flex-1">
			<template #auswahl><router-view name="auswahl" /></template>
			<router-view />
		</s-uv-auswahl-layout>
		<router-view v-else />
	</div>
</template>

<script setup lang="ts">

	import { RouterView } from "vue-router";

	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import type { TabManager } from "@ui/ui/nav/TabManager";

	import SUvAuswahlLayout from "../../SUvAuswahlLayout.vue";

	const props = defineProps<{
		tabManager: () => TabManager;
	}>();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

</script>
