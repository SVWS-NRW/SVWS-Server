<template>
	<Teleport to=".svws-sub-nav-target" defer>
		<nav class="svws-ui-secondary-tabs">
			<svws-ui-tab-bar :tab-manager secondary :focus-switching-enabled :focus-help-visible />
		</nav>
	</Teleport>
	<router-view :key="$route.hash" />
</template>


<script setup lang="ts">

	import { useRegionSwitch } from "@ui";
	import type { SchuelerAbiturProps } from "./SchuelerAbiturProps";

	const props = defineProps<SchuelerAbiturProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

</script>
