<template>
	<div v-if="gruppe" class="flex flex-col w-full h-full overflow-hidden">
		<svws-ui-header>
			<div>
				<span class="inline-block mr-3">{{ gruppe.bezeichnung }}</span>
				<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
					ID: {{ gruppe.id }}
				</svws-ui-badge>
			</div>
		</svws-ui-header>
		<svws-ui-tab-bar :tab-manager :focus-switching-enabled :focus-help-visible>
			<router-view />
		</svws-ui-tab-bar>
	</div>
	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-school-line" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { useRegionSwitch } from "@ui";
	import type { BenutzergruppeAppProps } from "./SBenutzergruppeAppProps";

	const props = defineProps<BenutzergruppeAppProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const gruppe = computed(() => props.auswahl());

</script>
