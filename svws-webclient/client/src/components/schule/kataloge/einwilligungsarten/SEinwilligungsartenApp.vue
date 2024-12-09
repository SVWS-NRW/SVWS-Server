<template>
	<template v-if="art">
		<svws-ui-header>
			<div>
				<span class="inline-block mr-3 capitalize">{{ art.bezeichnung }}</span>
				<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
					ID: {{ art.id }}
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

	import { computed } from "vue";
	import type { SEinwilligungsartenAppProps } from "./SEinwilligungsartenAppProps";
	import { useRegionSwitch } from "~/components/useRegionSwitch";

	const props = defineProps<SEinwilligungsartenAppProps>();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const art = computed(() => props.auswahl())

</script>
