<template>
	<template v-if="auswahl">
		<svws-ui-header>
			<div>
				<span class="inline-block mr-3">{{ auswahl.text }}</span>
				<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
					ID: {{ auswahl.id }}
				</svws-ui-badge>
			</div>
			<div>
				<span class="opacity-40">{{ auswahl.kuerzel }}</span>
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

	import type { FoerderschwerpunkteAppProps } from "./SFoerderschwerpunkteAppProps";
	import { useRegionSwitch } from "~/components/useRegionSwitch";

	const props = defineProps<FoerderschwerpunkteAppProps>();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

</script>
