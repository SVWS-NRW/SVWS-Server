<template>
	<template v-if="bezeichnung_abiturjahr">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<h2 class="svws-headline">
						<span>{{ bezeichnung_abiturjahr }}</span>
					</h2>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<svws-ui-router-tab-bar :routes="tabs" :hidden="tabsHidden" :model-value="tab" @update:model-value="setTab" :class="`router--tab--${tab.name} router--tab--${tab.name.replace('.', '_')}`">
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
		.svws-ui-tab-content {
			@apply h-full overflow-hidden;
		}
	}

</style>
