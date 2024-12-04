<template>
	<template v-if="vermerkartenManager().hasDaten()">
		<svws-ui-header>
			<div>
				<span class="inline-block mr-3 capitalize">{{ vermerkartenManager().auswahl().bezeichnung }}</span>
				<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
					ID: {{ vermerkartenManager().auswahl().id }}
				</svws-ui-badge>
			</div>
		</svws-ui-header>
		<svws-ui-tab-bar :tab-manager :focus-switching-enabled :focus-help-visible>
			<router-view />
		</svws-ui-tab-bar>
	</template>

	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-archive-line" />
	</div>
</template>

<script setup lang="ts">

	import type { VermerkeAppProps } from "./SVermerkeAppProps";
	import { useRegionSwitch } from "~/components/useRegionSwitch";

	defineProps<VermerkeAppProps>();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

</script>
