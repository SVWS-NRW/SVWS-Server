<template>
	<button @click="select()" :class="[isSelected ? 'router-tab-bar-button--active' : '', props.hidden ? 'hidden' : 'router-tab-bar-button']">
		<span>{{ text }}</span>
		<!--<i-ri-loader-4-line v-if="isSelected" class="svws-ui-spinner" />-->
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
  @apply py-2 px-3.5;
  @apply rounded-md;
  @apply select-none;
  @apply text-sm font-bold text-black dark:text-white;
  @apply whitespace-nowrap;
  @apply relative;

  .router-tab-bar--subnav & {
    @apply py-1.5 px-2.5;
  }

  .svws-ui-spinner {
    @apply w-4 h-4 absolute top-1.5 right-0.5;
  }

  &:hover {
    @apply bg-light dark:bg-white/5;
  }

  &:focus-visible {
    @apply ring-svws/50;

    .page--statistik & {
      @apply ring-violet-500/50;
    }
  }

  &:focus,
  &--active,
  &--active:hover {
    @apply outline-none;
    @apply text-svws bg-svws/5 dark:bg-svws/10;

    .router-tab-bar--subnav & {
      @apply text-svws bg-svws/5 dark:bg-svws/10;
    }

    .page--statistik & {
      @apply text-violet-500 bg-violet-500/5 dark:bg-violet-500/10;
    }
  }
}

.router-tab-bar-button--active {
  @apply relative;

  &:hover {
    @apply bg-svws/5 dark:bg-svws/10;
  }

  .svws-api--pending & {
    span {
      @apply animate-pulse;
    }
  }

  &:after {
    @apply absolute w-full;
    @apply -bottom-2 inset-x-0;
    @apply border-b-2 border-svws z-10;
    content: '';

    .router-tab-bar--subnav & {
      @apply border-svws;
    }

    .page--statistik & {
      @apply border-violet-500;
    }
  }
}

.router-tab-bar-button:disabled {
  @apply bg-black/25 dark:bg-white/25 border-black/50 dark:border-white/50 text-black dark:text-white;
  @apply opacity-20;
  @apply cursor-not-allowed pointer-events-none;
}
</style>
