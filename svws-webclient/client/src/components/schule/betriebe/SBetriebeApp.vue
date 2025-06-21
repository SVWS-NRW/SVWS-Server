<template>
	<template v-if="auswahl">
		<svws-ui-header>
			<div>
				<span class="inline-block mr-3">{{ auswahl.name1 }}</span>
				<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
					ID: {{ auswahl.id }}
				</svws-ui-badge>
			</div>
		</svws-ui-header>
		<svws-ui-tab-bar :tab-manager :focus-switching-enabled :focus-help-visible>
			<router-view />
		</svws-ui-tab-bar>
	</template>
	<div v-else class="app-content-placeholder">
		<span class="icon i-ri-archive-line" />
	</div>
</template>
<script setup lang="ts">

	import { useRegionSwitch } from '@ui';
	import type { BetriebeAppProps } from './SBetriebeAppProps';

	const props = defineProps<BetriebeAppProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

</script>

