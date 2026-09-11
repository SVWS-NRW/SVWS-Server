<template>
	<svws-ui-tab-bar secondary class="h-full min-h-0 flex flex-col" :tab-manager :focus-help-visible :focus-switching-enabled>
		<s-uv-auswahl-layout v-if="tabManager().tab.tabContentHatAuswahl">
			<template #auswahl><router-view name="auswahl" /></template>
			<router-view />
		</s-uv-auswahl-layout>
		<router-view v-else />
	</svws-ui-tab-bar>
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
