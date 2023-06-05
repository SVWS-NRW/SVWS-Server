<template>
	<template v-if="bezeichnung_abiturjahr">
		<svws-ui-header>
			<span class="inline-block mr-3">{{ bezeichnung_abiturjahr }}</span>
			<br>
			<span class="opacity-50">{{ jahrgang ? jahrgang : '–' }}</span>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="tabs" :hidden="tabsHidden" :model-value="tab" @update:model-value="setTab" :class="`router--tab--${tab.name}`">
			<router-view />
		</svws-ui-router-tab-bar>
	</template>
	<div v-else class="app--content--placeholder">
		<i-ri-graduation-cap-line />
	</div>
</template>

<script setup lang="ts">

	import type { ComputedRef } from "vue";
	import { computed } from "vue";
	import type { GostAppProps } from "./SGostAppProps";

	const props = defineProps<GostAppProps>();

	const jahrgang: ComputedRef<string | undefined> = computed(() => {
		return props.auswahl?.jahrgang ?? undefined;
	});

	const bezeichnung_abiturjahr: ComputedRef<string | undefined> = computed(() => {
		return props.auswahl?.bezeichnung ?? undefined;
	});

</script>

<style lang="postcss">
.page--wrapper {
	@apply flex flex-col;
}

.router--tab--gost\.kursplanung {
	.router-tab-bar--panel {
		@apply h-full overflow-hidden;
	}
}
</style>
