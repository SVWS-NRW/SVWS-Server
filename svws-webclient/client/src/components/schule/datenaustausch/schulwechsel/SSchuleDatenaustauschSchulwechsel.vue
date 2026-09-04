<template>
	<svws-ui-header>
		<span class="inline-block mr-3">Datenaustausch für Schulwechsel</span>
		<br>
		<span class="opacity-50 flex">
			<span class="i-ri-download-2-line icon-xl" />
			<span class="i-ri-upload-2-line icon-xl" />
		</span>
	</svws-ui-header>
	<svws-ui-tab-bar :tab-manager :focus-switching-enabled :focus-help-visible>
		<router-view />
	</svws-ui-tab-bar>
</template>

<script setup lang="ts">

	import { useRegionSwitch } from '@ui/ui/composables/useRegionSwitch';
	import type { RouteTabProps } from '~/router/RouteTabNode';

	const props = defineProps<RouteTabProps>();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

</script>
