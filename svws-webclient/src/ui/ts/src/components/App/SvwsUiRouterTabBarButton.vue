<template>
	<button @click="select()" :class="[isSelected ? 'router-tab-bar-button--active' : '', props.hidden ? 'hidden' : 'router-tab-bar-button']" role="button">
		{{ text }}
	</button>
</template>

<script lang="ts" setup>
	import type { ComputedRef } from 'vue';
	import { computed } from 'vue';
	import type { RouteRecordRaw } from "vue-router";
	import type { AuswahlChildData } from '../../types';

	const emit = defineEmits<{
		(e: 'select', value: RouteRecordRaw | AuswahlChildData) : void
	}>();

	const props = defineProps<{
		route: RouteRecordRaw | AuswahlChildData
		hidden: boolean
		selected: RouteRecordRaw | AuswahlChildData
	}>();

	const isSelected: ComputedRef<boolean> = computed(() => {
		return props.route.name?.toString() === props.selected.name?.toString();
	});

	function select() {
		emit('select', props.route);
	}

	const record = () => props.route as RouteRecordRaw;
	const auswahl = () => props.route as AuswahlChildData;
	const text: ComputedRef<string> = computed(()=> {
		if ("meta" in props.route)
			return record().meta?.text as string || "";
		else
			return auswahl().text;
	})

</script>


<style lang="postcss">
    .router-tab-bar-button {
        @apply inline-flex items-center justify-center py-2 px-3 rounded-md;
        @apply text-sm font-bold text-black dark:text-white whitespace-nowrap select-none;

		&:hover {
			@apply bg-light dark:bg-white/5;
		}

		&:focus {
			@apply outline-none;
		}

		&:focus-visible {
			@apply ring ring-inset ring-svws/50;

			.page--statistik & {
				@apply ring-violet-500/50;
			}
		}

		&:focus,
		&--active {
			@apply text-svws bg-svws/5 dark:bg-svws/10;

			.page--statistik & {
				@apply text-violet-500 bg-violet-500/5 dark:bg-violet-500/10;
			}
		}

		&--active {
			@apply relative;

			&:after {
				@apply absolute w-full;
				@apply -bottom-2 inset-x-0;
				@apply border-b-2 border-svws z-10;
				content: '';

				.page--statistik & {
					@apply border-violet-500;
				}
			}
		}

		&:disabled {
			@apply bg-black/25 dark:bg-white/25 border-black/50 dark:border-white/50 text-black dark:text-white;
			@apply opacity-20 cursor-not-allowed pointer-events-none;
		}
    }
</style>
