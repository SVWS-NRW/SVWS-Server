<template>
	<Teleport to=".svws-sub-nav-target" defer>
		<nav class="svws-ui-secondary-tabs">
			<svws-ui-tab-bar :tab-manager secondary :focus-switching-enabled :focus-help-visible />
		</nav>
	</Teleport>
	<router-view :key="$route.hash" />
</template>


<script setup lang="ts">

	import type { SchuelerSonstigesProps } from "./SchuelerSonstigesProps";
	import { useRegionSwitch } from "~/components/useRegionSwitch";

	const props = defineProps<SchuelerSonstigesProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

</script>
