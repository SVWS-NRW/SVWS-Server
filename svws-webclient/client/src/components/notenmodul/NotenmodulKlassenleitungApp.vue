<template>
	<template v-if="(activeViewType === ViewType.DEFAULT)">
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

	import type { NotenmodulKlassenleitungAppProps } from "./NotenmodulKlassenleitungAppProps";
	import { useRegionSwitch, ViewType } from "@ui";

	const props = defineProps<NotenmodulKlassenleitungAppProps>();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

</script>
