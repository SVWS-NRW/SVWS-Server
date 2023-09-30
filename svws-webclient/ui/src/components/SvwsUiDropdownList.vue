<template>
	<div class="multiselect--items-wrapper"
		:class="{'multiselect--items-wrapper--statistics': statistics, 'multiselect--items-wrapper--tags': tags}"
		:style="{ position: strategy, top: floatingTop, left: floatingLeft }"
		ref="floating">
		<ul :id="listIdPrefix"
			class="multiselect--items-list"
			:class="{'multiselect--items-list--tags' : tags}"
			role="listbox"
			@mouseenter="activeItemIndex = -1">
			<li v-if="filteredList.length === 0" class="px-1 py-1 text-base opacity-50 inline-block">
				Keine Ergebnisse
			</li>
			<li v-for="(item, index) in filteredList"
				:id="`${listIdPrefix}-${index}`"
				:key="index"
				ref="itemRefs"
				role="option"
				class="multiselect--item"
				:class="{
					active: activeItemIndex === index,
					selected: selectedItemList.has(item)
				}"
				:aria-selected="selectedItemList.has(item) ? 'true' : 'false'"
				@mousedown.prevent
				@click="selectItem(item)">
				<span v-if="itemText?.(item).length === 0" class="opacity-25">—</span>
				<span>{{ itemText(item) }}</span>
				<i-ri-check-line v-if="selectedItemList.has(item)" class="multiselect--check opacity-75" />
			</li>
		</ul>
	</div>
</template>

<script setup lang="ts" generic="Item">

	import type { Strategy } from "@floating-ui/vue";
	import type { Ref } from "vue";
	import { ref, shallowRef, toRef } from "vue";
	import { genId } from "../utils";

	const props = defineProps<{
		statistics: boolean;
		tags: boolean;
		filteredList: Item[];
		itemText: (item: Item) => string;
		selectItem: (item: Item | null | undefined) => void;
		selectedItemList: Set<Item>;
		strategy: Strategy;
		floatingLeft: string;
		floatingTop: string;
	}>()

	const floating = ref(null);
	const tags = toRef(props, 'tags');

	const listIdPrefix = genId();

	const itemRefs = shallowRef<HTMLLIElement[]>([]);
	const activeItemIndex = ref(-1);

	defineExpose<{
		activeItemIndex: Ref<number>,
		floating: Ref<HTMLElement|null>,
	}>({
		activeItemIndex,
		floating
	});


</script>


<style lang="postcss" scoped>

.multiselect--items-wrapper {
	@apply absolute z-50 w-full min-w-[11rem];
	@apply rounded-md border border-black/25 dark:border-white/25 bg-white dark:bg-black;
	@apply shadow-2xl shadow-black/25 dark:shadow-white/5;
	@apply overflow-hidden;
}

.multiselect--items-list {
	@apply overflow-y-auto overflow-x-hidden;
	@apply px-1.5 py-0.5;
	max-height: 40ex;

	.multiselect--item {
		@apply text-black dark:text-white bg-white dark:bg-black rounded border border-transparent;
		@apply text-base;
		@apply py-1 my-1 px-2;

		.multiselect--check {
			@apply hidden -mt-0.5;
		}

		&.active {
			@apply ring ring-svws/50 border-svws bg-svws text-white;
		}

		&:hover {
			@apply cursor-pointer;
			@apply bg-svws text-white;
		}

		&.selected {
			@apply font-bold bg-svws text-white;
		}
	}

	&--tags {
		.multiselect--item .multiselect--check {
			@apply inline-block;
		}

		.multiselect--item.selected {
			@apply bg-transparent text-svws dark:text-svws;

			&.active {
				@apply ring-svws/25 dark:ring-svws/25;
			}

			&:hover {
				@apply bg-svws text-white dark:text-white;
			}
		}
	}
}

.multiselect--items-wrapper--statistics {
	.multiselect--item.active {
		@apply ring-violet-500/50 border-violet-500 bg-violet-500;
	}

	.multiselect--item:hover {
		@apply bg-violet-500;
	}

	.multiselect--item.selected {
		@apply bg-violet-500;
	}

	&.multiselect--items-wrapper--tags {
		.multiselect--item.selected {
			@apply bg-transparent text-violet-500 dark:text-violet-500;

			&.active {
				@apply ring-violet-500/25 dark:ring-violet-500/25;
			}

			&:hover {
				@apply bg-violet-500 text-white dark:text-white;
			}
		}
	}
}

</style>
