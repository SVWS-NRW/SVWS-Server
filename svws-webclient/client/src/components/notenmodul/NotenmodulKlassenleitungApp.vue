<template>
	<template v-if="(activeViewType === ViewType.DEFAULT) && enmManager().daten.schulnummer !== 0">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<h2 class="svws-headline">
						Klassenleitung
					</h2>
					<span class="svws-subline">&nbsp;</span>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<svws-ui-tab-bar :tab-manager :focus-switching-enabled :focus-help-visible>
			<router-view />
		</svws-ui-tab-bar>
	</template>
	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-briefcase-line" />
	</div>
</template>

<script setup lang="ts">

	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import type { NotenmodulKlassenleitungAppProps } from "./NotenmodulKlassenleitungAppProps";
	import { ViewType } from "@ui/ui/nav/ViewType";

	const props = defineProps<NotenmodulKlassenleitungAppProps>();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

</script>
