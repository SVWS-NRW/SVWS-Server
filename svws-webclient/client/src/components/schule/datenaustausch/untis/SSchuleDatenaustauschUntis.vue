<template>
	<div class="flex flex-col w-full h-full overflow-hidden">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<h2 class="svws-headline">
						<span>Datenaustausch mit Untis</span>
					</h2>
					<span class="svws-subline">			<span class="opacity-50 flex">
						<span class="icon-xl i-ri-download-2-line" />
						<span class="i-ri-upload-2-line icon-xl" />
					</span></span>
				</div>
			</div>
			<div class="svws-ui-header--actions">
				<svws-ui-modal-hilfe> <hilfe-schule-datenaustausch-untis /> </svws-ui-modal-hilfe>
			</div>
		</header>
		<svws-ui-tab-bar :tab-manager :focus-switching-enabled :focus-help-visible>
			<router-view />
		</svws-ui-tab-bar>
	</div>
</template>

<script setup lang="ts">

	import { useRegionSwitch } from '@ui';
	import type { RouteTabProps } from '~/router/RouteTabNode';

	const props = defineProps<RouteTabProps>();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

</script>
