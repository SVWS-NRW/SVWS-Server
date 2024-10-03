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
		<svws-ui-tab-bar :tab-manager :class="`router--tab--${tabManager().tab.name} router--tab--${tabManager().tab.name.replace('.', '_')}`">
			<router-view />
		</svws-ui-tab-bar>
	</template>
	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-graduation-cap-line" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { GostAppProps } from "./SGostAppProps";

	const props = defineProps<GostAppProps>();

	const bezeichnung_abiturjahr = computed(() => props.auswahl?.bezeichnung);

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
