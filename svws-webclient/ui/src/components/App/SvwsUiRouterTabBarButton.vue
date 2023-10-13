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
  @apply py-1 px-2;
  @apply rounded;
  @apply select-none;
  @apply text-sm font-bold text-black dark:text-white;
  @apply whitespace-nowrap;
  @apply relative border border-transparent;

	.svws-ui-tabs--vertical & {
		@apply py-1.5 px-2.5;
	}

  .svws-ui-spinner {
    @apply w-4 h-4 absolute top-1.5 right-0.5;
  }

  &:hover {
    @apply bg-black/10 dark:bg-white/10;

	  &:active {
		  @apply bg-black/20 dark:bg-white/20;
	  }
  }

  &:focus-visible {
    @apply ring-2 ring-svws/50;

    .page--statistik & {
      @apply ring-violet-500/50;
    }
  }

  &:focus {
	  @apply outline-none;
  }

  &.svws-active,
  &.svws-active:hover {
    @apply outline-none text-svws;

	  .svws-ui-tabs--vertical & {
		  @apply bg-white dark:bg-black shadow;
	  }

	  .svws-ui-tabs &,
	  .router-tab-bar--subnav & {
		  &:before {
			  @apply absolute left-2 right-2 -bottom-2 h-[2px] bg-svws;
			  content: '';

			  .page--statistik & {
				  @apply bg-violet-500;
			  }
		  }
	  }

	  .router-tab-bar--subnav & {
		  @apply bg-white dark:bg-black;

		  &:before {
			  @apply -bottom-1;
		  }
	  }

    .page--statistik & {
      @apply text-violet-500;
    }
  }

	&.svws-active:hover {
		.svws-ui-tabs & {
			@apply bg-svws/5 dark:bg-svws/10;

			.page--statistik & {
				@apply bg-violet-500/5 dark:bg-violet-500/10;
			}
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
