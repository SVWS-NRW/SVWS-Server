<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<svws-ui-button type="secondary" @click="statistikState.reload"><span class="icon i-ri-loop-left-line" /> Daten aktualisieren</svws-ui-button>
	</Teleport>
	<header class="svws-ui-header">
		<div class="svws-ui-header--title">
			<div class="svws-headline-wrapper">
				<h2 class="svws-headline">
					{{ schuleState.stammdaten.bezeichnung1 }}
					<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
						{{ schuleState.stammdaten.schulNr }}
					</svws-ui-badge>
				</h2>
				<span class="svws-subline">
					{{ schuleState.stammdaten.schulform }}
				</span>
			</div>
		</div>
		<div class="svws-ui-header--actions" />
	</header>

	<svws-ui-tab-bar :tab-manager :focus-switching-enabled :focus-help-visible>
		<router-view />
	</svws-ui-tab-bar>
</template>

<script setup lang="ts">

	import { useSchuleState } from "@ui/states/SchuleState";
	import { useStatistikState } from "@ui/states/statistik/StatistikState";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";

	import type { StatistikAppProps } from "./StatistikAppProps";

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const props = defineProps<StatistikAppProps>();
	const schuleState = useSchuleState();
	const statistikState = useStatistikState();

</script>
