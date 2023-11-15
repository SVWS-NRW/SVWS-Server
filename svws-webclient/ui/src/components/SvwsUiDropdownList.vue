<template>
	<div class="svws-ui-dropdown-list"
		:class="{'svws-statistik': statistics, 'svws-type-tags': tags}"
		:style="{ position: strategy, top: floatingTop, left: floatingLeft }"
		ref="floating">
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
			<template v-for="(item, index) in filteredList" :key="index">
				<slot name="item" :item="item" :index="index">
					<li :id="`${listIdPrefix}-${index}`"
						ref="itemRefs"
						role="option"
						class="svws-ui-dropdown-list--item"
						:class="{
							'svws-active': activeItemIndex === index,
							'svws-selected': selectedItemList.has(item)
						}"
						:aria-selected="selectedItemList.has(item) ? 'true' : 'false'"
						@mousedown.prevent
						@click="selectItem(item)">
						<span v-if="itemText(item).length === 0" class="opacity-25">—</span>
						<span v-else>{{ itemText(item) }}</span>
						<i-ri-check-line v-if="selectedItemList.has(item)" class="w-5 flex-shrink-0 -mr-1 -my-1 relative top-1.5" />
					</li>
				</slot>
			</template>
		</ul>
	</div>
</template>

<script setup lang="ts" generic="Item">

	import type { Strategy } from "@floating-ui/vue";
	import type { Ref, ShallowRef } from "vue";
	import { ref, shallowRef, toRef, computed } from "vue";
	import { genId } from "../utils";

	const props = withDefaults(defineProps<{
		statistics?: boolean;
		selectedItemList?: Set<Item>;
		tags?: boolean;
		filteredList: Item[] | Iterable<Item>;
		itemText?: (item: Item) => string;
		selectItem?: (item: Item | null | undefined) => void;
		strategy: Strategy;
		floatingLeft: string;
		floatingTop: string;
		searchText?: string;
	}>(),{
		statistics: false,
		tags: false,
		itemText: (item: Item) => "",
		selectItem: (item: Item | undefined | null) => undefined,
		searchText: "",
		selectedItemList: () => new Set<Item>(),
	})
	const floating = ref(null);
	const tags = toRef(props, 'tags');

	const listIdPrefix = genId();

	const itemRefs = shallowRef<HTMLLIElement[]>([]);
	const activeItemIndex = ref(-1);

	const listEmpty = computed(()=> {
		if (props.filteredList === undefined)
			return true;
		for (const _ of props.filteredList)
			return false;
		return true;
	})

	defineExpose<{
		activeItemIndex: Ref<number>;
		floating: Ref<HTMLElement|null>;
		itemRefs: ShallowRef<HTMLLIElement[]>;
	}>({
		activeItemIndex, floating, itemRefs
	});


</script>


<style lang="postcss">

.svws-ui-dropdown-list {
	@apply w-full z-50 min-w-[11rem];
	@apply rounded-lg border border-black/25 dark:border-white/25 bg-white dark:bg-black;
  @apply shadow-xl;
}

.svws-ui-dropdown-list--items {
	@apply overflow-y-auto overflow-x-hidden flex flex-col gap-px pt-1 px-1 pb-1;
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
    @apply ring-2 ring-black/25 dark:ring-white/25;

    &.svws-selected {
      @apply ring-svws/25 dark:ring-svws/25;

      .svws-statistik & {
        @apply ring-violet-500/25 dark:ring-violet-500/25;
      }
    }
  }
}

</style>
