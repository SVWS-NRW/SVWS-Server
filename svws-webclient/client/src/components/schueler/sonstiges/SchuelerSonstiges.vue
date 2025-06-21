<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<svws-ui-modal-hilfe> <hilfe-schueler-sonstiges /> </svws-ui-modal-hilfe>
	</Teleport>
	<Teleport to=".svws-sub-nav-target" defer>
		<nav class="svws-ui-secondary-tabs">
			<svws-ui-tab-bar :tab-manager secondary :focus-switching-enabled :focus-help-visible />
		</nav>
	</Teleport>
	<router-view :key="$route.hash" />
</template>


<script setup lang="ts">

	import { useRegionSwitch } from "@ui";
	import type { SchuelerSonstigesProps } from "./SchuelerSonstigesProps";

	const props = defineProps<SchuelerSonstigesProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

</script>
