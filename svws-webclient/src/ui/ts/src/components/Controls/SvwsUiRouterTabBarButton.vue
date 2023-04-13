<template>
	<button @click="select()" :class="[isSelected ? 'router-tab-bar-button--active' : '', props.hidden ? 'hidden' : 'router-tab-bar-button']">
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
        @apply inline-flex items-center justify-center;
        @apply py-2 px-3;
        @apply rounded-md;
        @apply select-none;
        @apply text-sm font-bold text-black;
        @apply whitespace-nowrap;
    }

    .router-tab-bar-button:hover {
        @apply bg-light;
    }

    .router-tab-bar-button:focus {
		@apply text-primary;
		@apply bg-primary bg-opacity-5;
	}

    .router-tab-bar-button:focus-visible {
		@apply ring-primary ring-opacity-50;
	}

    .router-tab-bar-button:focus,
    .router-tab-bar-button--active {
        @apply outline-none;
    }

    .router-tab-bar-button--active {
		@apply relative;
        @apply text-primary;

		&:after {
			@apply absolute w-full;
			@apply -bottom-2 inset-x-0;
			@apply border-b-2 border-primary z-10;
			content: '';
			/*@apply left-3;
			width: calc(100% - 1.5rem);*/
		}
    }

    .router-tab-bar-button:disabled {
		@apply bg-black bg-opacity-25 border-black border-opacity-50 text-black;
		@apply opacity-20;
		@apply cursor-not-allowed pointer-events-none;
    }
</style>
