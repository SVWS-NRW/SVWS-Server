<template>
	<button v-if="text" @click="select()" class="svws-ui-tab-button" :class="{'svws-active': isSelected}" :disabled="hidden">
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
.svws-ui-tab-button {
  @apply inline-flex items-center justify-center;
  @apply py-1.5 px-2.5;
  @apply rounded;
  @apply select-none;
  @apply text-sm font-bold text-black dark:text-white;
  @apply whitespace-nowrap;
  @apply relative border border-transparent;

  .router-tab-bar--subnav & {
    @apply py-1.5 px-2.5;
  }

  .svws-ui-spinner {
    @apply w-4 h-4 absolute top-1.5 right-0.5;
  }

  &:hover {
    @apply bg-black/10 dark:bg-white/10;
  }

  &:focus-visible {
    @apply ring-svws/50;

    .page--statistik & {
      @apply ring-violet-500/50;
    }
  }

  &:focus,
  &.svws-active,
  &.svws-active:hover {
    @apply outline-none;
    @apply text-svws bg-white dark:bg-black shadow;

    .router-tab-bar--subnav & {
      @apply text-svws bg-svws/10 dark:bg-svws/10;
    }

    .page--statistik & {
      @apply text-violet-500;
    }
  }

  &.svws-active {
    .svws-api--pending & {
      span {
        @apply animate-pulse;
      }
    }
  }

  &:disabled {
    @apply bg-transparent dark:bg-transparent text-black dark:text-white;
    @apply opacity-20;
    @apply cursor-not-allowed pointer-events-none;
  }
}
</style>
