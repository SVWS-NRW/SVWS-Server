<template>
	<template v-if="auswahl">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<h2 class="svws-headline">
						<span class="inline-block mr-3">{{ auswahl.beschreibung }}</span>
						<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
							ID: {{ auswahl.id }}
						</svws-ui-badge>
					</h2>
					<span class="svws-subline">{{ auswahl.kuerzel }}</span>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<svws-ui-router-tab-bar :routes="tabs" :hidden="tabsHidden" :model-value="tab" @update:model-value="setTab">
			<router-view />
		</svws-ui-router-tab-bar>
	</template>
	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-archive-line" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { RaeumeAppProps } from "./SRaeumeAppProps";

	const props = defineProps<RaeumeAppProps>();
	const auswahl = computed(() => props.raumListeManager().auswahlID() !== null && props.raumListeManager().auswahl());

</script>
