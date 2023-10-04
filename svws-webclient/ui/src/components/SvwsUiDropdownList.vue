<template>
	<div class="svws-ui-dropdown-list"
		:class="{'svws-statistik': statistics, 'svws-type-tags': tags}"
		:style="{ position: strategy, top: floatingTop, left: floatingLeft }"
		ref="floating">
		<slot name="items">
			<ul :id="listIdPrefix"
				class="svws-ui-dropdown-list--items"
				role="listbox"
				@mouseenter="activeItemIndex = -1">
				<li v-if="listEmpty" class="px-2 py-1.5 text-base opacity-50 inline-block">
					<template v-if="!searchText">
						Keine Einträge gefunden
					</template>
					<template v-else>
						Keine Ergebnisse für "{{ searchText }}"
					</template>
				</li>
				<li v-for="(item, index) in filteredList"
					:id="`${listIdPrefix}-${index}`"
					:key="index"
					ref="itemRefs"
					role="option"
					class="svws-ui-dropdown-list--item"
					:class="{
						'svws-active': activeItemIndex === index,
						'svws-selected': selectedItemList?.has(item)
					}"
					:aria-selected="selectedItemList?.has(item) ? 'true' : 'false'"
					@mousedown.prevent
					@click="selectItem?.(item)">
					<span v-if="itemText?.(item).length === 0" class="opacity-25">—</span>
					<span v-else>{{ itemText?.(item) }}</span>
					<i-ri-check-line v-if="selectedItemList?.has(item)" class="w-5 flex-shrink-0 -mr-1 -my-1 relative top-1.5" />
				</li>
			</ul>
		</slot>
	</div>
</template>

<script setup lang="ts" generic="Item">

	import type { Strategy } from "@floating-ui/vue";
	import type { Ref } from "vue";
	import { ref, shallowRef, toRef } from "vue";
	import { genId } from "../utils";
	import { computed } from "vue";

	const props = defineProps<{
		statistics?: boolean;
		tags?: boolean;
		filteredList?: Item[] | Iterable<Item>;
		itemText?: (item: Item) => string;
		selectItem?: (item: Item | null | undefined) => void;
		selectedItemList?: Set<Item>;
		strategy?: Strategy;
		floatingLeft?: string;
		floatingTop?: string;
		searchText?: string;
	}>()

	const floating = ref(null);
	const tags = toRef(props, 'tags');

	const listIdPrefix = genId();

	const itemRefs = shallowRef<HTMLLIElement[]>([]);
	const activeItemIndex = ref(-1);

	const listEmpty = computed(()=> {
		if (props.filteredList === undefined)
			return false;
		for (const _ of props.filteredList)
			return false;
		return true;
	})

	defineExpose<{
		activeItemIndex: Ref<number>,
		floating: Ref<HTMLElement|null>,
	}>({
		activeItemIndex,
		floating
	});


</script>


<style lang="postcss">

.svws-ui-dropdown-list {
	@apply w-full z-50 min-w-[11rem];
	@apply rounded-lg border border-black/25 dark:border-white/25 bg-white dark:bg-black;
  @apply shadow-xl;
}

.svws-ui-dropdown-list--items {
	@apply overflow-y-auto overflow-x-hidden flex flex-col pt-1 px-1 pb-1;
	max-height: 24rem;
}

.svws-ui-dropdown-list--item {
  @apply rounded px-2 py-1.5 inline-flex items-start justify-between gap-0.5 text-base font-medium cursor-pointer;

  &.svws-selected {
    @apply bg-svws/5 dark:bg-svws/10 text-svws font-bold;

    .svws-statistik & {
      @apply bg-violet-500/5 dark:bg-violet-500/10 text-violet-500;
    }
  }

  &:not(.svws-selected) {
    &.svws-active,
    &:hover,
    &:focus-visible {
      @apply bg-black/10 dark:bg-white/10;
    }
  }

  &.svws-active {
    @apply ring ring-black/25 dark:ring-white/25;

    &.svws-selected {
      @apply ring-svws/25 dark:ring-svws/25;

      .svws-statistik & {
        @apply ring-violet-500/25 dark:ring-violet-500/25;
      }
    }
  }
}

</style>
