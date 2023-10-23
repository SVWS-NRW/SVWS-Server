<template>
	<svws-ui-header>
		<div>
			<span class="inline-block mr-3">{{ benutzer.anzeigename }}</span>
			<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
				ID:
				{{ benutzer.id }}
			</svws-ui-badge>
		</div>
	</svws-ui-header>
	<svws-ui-router-tab-bar :routes="tabs" :hidden="tabsHidden" :model-value="tab" @update:model-value="setTab">
		<router-view />
	</svws-ui-router-tab-bar>
</template>

<script setup lang="ts">

	import type { BenutzerprofilAppProps } from "./SBenutzerprofilAppProps";

	const props = defineProps<BenutzerprofilAppProps>();



</script>
