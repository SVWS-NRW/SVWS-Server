<template>
	<div v-if="visible" class="page--flex">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<h2 class="svws-headline">
						<span>{{ anzeigename || '—' }}</span>
						<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
							ID: {{ id }}
						</svws-ui-badge>
					</h2>
					<span class="svws-subline">
						{{ name || '—' }}
					</span>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<svws-ui-router-tab-bar :routes="tabs" :hidden="tabsHidden" @update:model-value="setTab" :model-value="tab">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app--content--placeholder">
		<i-ri-school-line />
	</div>
</template>

<script setup lang="ts">

	import type { ComputedRef } from "vue";
	import { computed } from "vue";

	import type { BenutzerAppProps } from "./SBenutzerAppProps";

	const props = defineProps<BenutzerAppProps>();

	const id: ComputedRef<number | string> = computed(() => props.auswahl()?.id ?? "?");
	const anzeigename: ComputedRef<string> = computed(() => props.auswahl()?.anzeigename ?? "---");
	const name: ComputedRef<string> = computed(() => props.auswahl()?.name ?? "---");

	const visible: ComputedRef<boolean> = computed(() => props.auswahl() !== undefined);

</script>
