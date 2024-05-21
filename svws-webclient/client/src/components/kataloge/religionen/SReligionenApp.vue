<template>
	<template v-if="religionListeManager().hasDaten()">
		<svws-ui-header>
			<div>
				<span class="inline-block mr-3 capitalize">{{ religionListeManager().auswahl().text }}</span>
				<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
					ID: {{ religionListeManager().auswahl().id }}
				</svws-ui-badge>
			</div>
			<div>
				<span class="opacity-40">{{ religionListeManager().auswahl().kuerzel }}</span>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="tabs" :hidden="tabsHidden" :model-value="tab" @update:model-value="setTab">
			<router-view />
		</svws-ui-router-tab-bar>
	</template>
	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-archive-line" />
	</div>
</template>

<script setup lang="ts">

	import type { ReligionenAppProps } from "./SReligionenAppProps";

	defineProps<ReligionenAppProps>();

</script>
