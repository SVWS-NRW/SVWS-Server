<template>
	<div class="flex flex-col w-full h-full overflow-hidden">
		<svws-ui-header>
			<span class="inline-block mr-3">Datenaustausch mit Untis</span>
			<br>
			<span class="opacity-50 flex">
				<span class="icon-xl i-ri-download-2-line" />
				<span class="i-ri-upload-2-line icon-xl" />
			</span>
		</svws-ui-header>
		<svws-ui-tab-bar :tab-manager :focus-switching-enabled :focus-help-visible>
			<router-view />
		</svws-ui-tab-bar>
	</div>
</template>

<script setup lang="ts">

	import type { RouteTabProps } from '~/router/RouteTabNode';
	import { useRegionSwitch } from "~/components/useRegionSwitch";

	const props = defineProps<RouteTabProps>();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

</script>
