<template>
	<div class="svws-ui-select svws-ui-multiselect" :class="{
		'svws-open': showList,
		'svws-has-value': hasSelected(),
		'svws-headless': headless,
		'svws-statistik': statistics,
		'svws-danger': danger,
		'svws-readonly': readonly,
		'svws-disabled': disabled,
		'svws-autocomplete': autocomplete
	}" v-bind="$attrs" ref="inputElTags">
		<div v-if="!headless && (data.size > 0)" class="svws-tags">
			<span v-for="(item, index) in data" :key="index" class="svws-tag">
				<span class="line-clamp-1 leading-tight break-all max-w-[14rem]">{{ itemText(item) }}</span>
				<button v-if="!readonly" role="button" class="svws-remove" @click.stop="removeTag(item)" title="Entfernen">
					<span class="icon i-ri-close-line" />
				</button>
			</span>
		</div>
		<svws-ui-text-input ref="inputEl"
			:model-value="headless ? dynModelValue : (data.size ? ' ' : '')"
			:readonly="!autocomplete || readonly"
			:is-select-input="!readonly"
			:placeholder="label || title"
			:statistics
			:headless
			:disabled
			:required
			:debounce-ms="0"
			role="combobox"
			:aria-label="label || title"
			:aria-expanded="showList"
			aria-haspopup="listbox"
			aria-autocomplete="list"
			:aria-controls="showList ? listIdPrefix : null"
			:aria-activedescendant="refList && refList.activeItemIndex > -1 ? `${listIdPrefix}-${refList.activeItemIndex}` : null"
			@update:model-value="value => searchText = value ?? ''"
			@click.stop="toggleListBox"
			@focus="onInputFocus"
			@methods="methods => methodsTextField = methods"
			@blur="onInputBlur"
			@keyup.down.prevent
			@keyup.up.prevent
			@keydown.down.prevent="onArrowDown"
			@keydown.up.prevent="onArrowUp"
			@keydown.enter.prevent="selectCurrentActiveItem"
			@keydown.backspace="onBackspace"
			@keydown.esc.prevent="toggleListBox"
			@keydown.space.prevent="onSpace"
			@keydown.tab="onTab" />
		<button v-if="!readonly" role="button" class="svws-dropdown-icon" @keydown.enter="toggleListBox" @keydown.down="toggleListBox">
			<span class="icon i-ri-expand-up-down-line" v-if="headless" />
			<span class="icon i-ri-expand-up-down-fill" v-else />
		</button>
	</div>
	<Teleport to="body" v-if="isMounted">
		<svws-ui-dropdown-list v-if="showList" :statistics :filtered-list :item-text :strategy :floating-left :floating-top :selected-item-list="data" :select-item ref="refList" />
	</Teleport>
</template>


<script setup lang="ts" generic="Item">

	import type { ComputedRef, Ref } from "vue";
	import { computed, nextTick, ref, watch, onMounted, shallowRef, toRaw, useId } from "vue";
	import type { ComponentExposed } from "vue-component-type-helpers";
	import type { MaybeElement } from "@floating-ui/vue";
	import { useFloating, autoUpdate, flip, offset, shift, size } from "@floating-ui/vue";
	import SvwsUiDropdownList from "./SvwsUiDropdownList.vue";

	const props = withDefaults(defineProps<{
		label?: string;
		title?: string;
		autocomplete?: boolean;
		disabled?: boolean;
		readonly?: boolean;
		statistics?: boolean;
		danger?: boolean;
		items: Iterable<Item> | Map<number, Item>;
		itemText: (item: Item) => string;
		itemSort?: (a: Item, b: Item) => number;
		itemFilter?: ((items: Iterable<Item>, searchText: string) => Item[]) | ((items: Item[], searchText: string) => Item[]);
		modelValue: Item[];
		useNull?: boolean;
		headless?: boolean;
		required?: boolean;
		autofocus?: boolean;
	}>(), {
		label: '',
		title: '',
		autocomplete: false,
		disabled: false,
		readonly: false,
		statistics: false,
		danger: false,
		itemSort: (a: Item, b: Item) => 0,
		itemFilter: (items: Iterable<Item> | Item[], searchText: string) => Array.isArray(items) ? items : [...items],
		useNull: false,
		headless: false,
	})

	const emit = defineEmits<{
		(e: "update:modelValue", items: Item[]): void;
		(e: "blur"): void;
	}>();

	const methodsTextField = shallowRef<{ focus: () => void } | undefined>(undefined);

	const refList = ref<ComponentExposed<typeof SvwsUiDropdownList>>();

	const showList = ref(false);
	const listIdPrefix = useId();

	const inputEl = ref(null);
	const inputElTags = ref(null);
	const hasFocus = ref(false);
	const searchText = ref("");
	const isMounted = ref(false);

	onMounted(() => {
		isMounted.value = true;
		if (props.autofocus) {
			onInputFocus();
			toggleListBox();
		}
	});

	function onInputFocus() {
		hasFocus.value = true;
	}

	function onInputBlur() {
		hasFocus.value = false;
		closeListbox();
		emit('blur');
	}

	const dynModelValue = computed<string>(() => {
		switch (true) {
			case showList.value && props.autocomplete:
				return searchText.value;
			default:
				return generateInputText();
		}
	});

	function generateInputText() {
		return [...data.value].map(item => props.itemText(item)).join(", ");
	}

	const rawModelValues = computed(() => {
		const set = new Set<Item>();
		for (const item of props.modelValue)
			set.add(toRaw(item));
		return set;
	})

	const data = shallowRef(new Set(rawModelValues.value));

	watch(rawModelValues, (value) => updateData(value), { immediate: false });

	function updateData(newValueSet: Set<Item>) {
		if ((data.value.size === newValueSet.size) && (data.value.difference(newValueSet).size === 0))
			return;
		data.value = newValueSet;
		emit("update:modelValue", [...newValueSet]);
	}

	const selectedItem = computed({
		get: () => {
			const [ e ] = data.value.keys();
			return e;
		},
		set: (item) => {
			if ((item !== null) && (item !== undefined)) {
				const newSelectedItems = new Set(data.value);
				if (newSelectedItems.has(item))
					newSelectedItems.delete(item);
				else
					newSelectedItems.add(item);
				updateData(newSelectedItems);
			}
		},
	});

	function hasSelected(): boolean {
		return (selectedItem.value !== null) && (selectedItem.value !== undefined);
	}

	function selectItem(item: Item) {
		selectedItem.value = item;
		if (props.autocomplete)
			searchText.value = "";
		doFocus();
	}

	function removeTag(item: Item) {
		if (data.value.has(item))
			selectedItem.value = item;
	}

	const sortedList = computed<Item[]>(() => {
		let arr
		if (Array.isArray(props.items))
			arr = props.items;
		else if (props.items instanceof Map)
			arr = [...props.items.values()];
		else
			arr = [...props.items];
		return arr.sort(props.itemSort);
	});

	const filteredList = computed<Item[]>(() => {
		if (props.autocomplete)
			return props.itemFilter(sortedList.value, searchText.value);
		return sortedList.value;
	});

	function doFocus() {
		void nextTick(() => methodsTextField.value?.focus());
	}

	function toggleListBox() {
		if (showList.value)
			closeListbox();
		else
			openListbox();
	}

	function openListbox() {
		showList.value = true;
		if ((selectedItem.value !== null) && (selectedItem.value !== undefined) && (refList.value !== undefined) && (refList.value !== null)) {
			refList.value.activeItemIndex = filteredList.value.findIndex(item => item === selectedItem.value);
			void nextTick(() => scrollToActiveItem());
		}
		void nextTick(() => methodsTextField.value?.focus());
	}

	function closeListbox() {
		if ((refList.value !== undefined) && (refList.value !== null))
			refList.value.activeItemIndex = -1;
		showList.value = false;
	}

	function selectCurrentActiveItem() {
		if (showList.value && (refList.value !== undefined) && (refList.value !== null)) {
			if (filteredList.value.length === 0)
				toggleListBox();
			selectItem(filteredList.value[refList.value.activeItemIndex]);
		}
	}

	// Arrow Navigation
	function onArrowDown() {
		if ((!showList.value) || (refList.value === undefined) || (refList.value === null))
			return openListbox();
		const listLength = filteredList.value.length;
		if (refList.value.activeItemIndex < listLength - 1)
			refList.value.activeItemIndex++;
		else
			refList.value.activeItemIndex = 0;
		scrollToActiveItem();
	}

	function onArrowUp() {
		if ((!showList.value) || (refList.value === undefined) || (refList.value === null))
			return openListbox();
		const listLength = filteredList.value.length;
		if (refList.value.activeItemIndex <= 0)
			refList.value.activeItemIndex = listLength - 1;
		else if (refList.value.activeItemIndex >= 1)
			refList.value.activeItemIndex--;
		scrollToActiveItem();
	}

	function onBackspace() {
		if (showList.value === false)
			openListbox();
	}

	function onSpace() {
		if (!props.autocomplete)
			if (showList.value)
				selectCurrentActiveItem();
			else
				openListbox();
	}

	function onTab() {
		if (props.autocomplete && (refList.value !== undefined) && (refList.value !== null)) {
			refList.value.activeItemIndex = 0;
			selectCurrentActiveItem();
		}
	}

	function scrollToActiveItem() {
		refList.value?.itemRefs[refList.value.activeItemIndex]?.scrollIntoView();
	}

	const { x, y, strategy } = useFloating(
		inputElTags,
		refList as Readonly<Ref<MaybeElement<HTMLElement>>>,
		{
			placement: 'bottom',
			middleware: [flip(), shift(), offset(2), size({
				apply({rects, elements}) {
					Object.assign(elements.floating.style, {
						width: `${rects.reference.width}px`,
					});
				},
			})],
			whileElementsMounted: autoUpdate,
		}
	);

	const floatingTop = computed(() => `${y.value}px`);
	const floatingLeft = computed(() => `${x.value}px`);

	const content = computed<Item[]>(() => [...data.value]);

	defineExpose<{
		content: ComputedRef<Item[]>,
	}>({content});

</script>


<style lang="postcss">

	.svws-ui-select.svws-ui-multiselect {
		@apply flex-none justify-start;
		&:focus-visible,
		&:focus-within {
			@apply ring ring-ui-neutral;
		}
		.svws-dropdown-icon {
			@apply top-[0.175rem];

		}
		.text-input-component .text-input--control:focus-visible {
			@apply ring-0;
			@apply bg-transparent border-none;
		}

		&.svws-open.svws-autocomplete .svws-tags {
			@apply mb-[2rem];
		}

		&:not(.svws-headless) {
			@apply min-h-[2.25rem];
			@apply bg-ui border border-ui-secondary text-ui;
			@apply rounded-md;

			.text-input-component {
				@apply absolute top-0 left-0 w-full h-full cursor-pointer;
			}

			&.svws-open.svws-autocomplete {
				.text-input--control {
					@apply h-[2.25rem] mt-auto pl-3;
				}
			}

			&:not(.svws-open) {
				.text-input--control {
					@apply hidden;
				}
			}
			&:hover {
				@apply border-ui;
			}
		}

		.svws-tags {
			@apply relative z-10 flex flex-wrap gap-0.5 pl-1 pr-7 pointer-events-none m-0.5 mt-2 content-start justify-start;

			.svws-remove {
				@apply relative top-0 left-0.5 -my-1 w-auto h-auto pointer-events-auto py-0;

				.icon {
					@apply mt-0 w-4 h-4;
				}

				&:hover .icon {
					@apply icon-ui-danger;

					.dark & {
						@apply icon-ui-danger--dark;
					}
				}
			}
			.svws-tag {
				@apply bg-ui border border-ui-secondary;
				@apply inline-flex items-center gap-0.5 rounded leading-none pl-2 pr-1 text-base;
			}

		}

		&.svws-disabled {
			@apply pointer-events-none;

			.svws-tags {
				@apply opacity-25;

				.svws-remove {
					@apply invisible -mr-3;
				}
			}
		}
	}
</style>


<style lang="postcss">
	/* TODO: Doppeltes CSS entfernen, dies ist identisch zum select */
	.svws-ui-select {
		@apply relative w-full cursor-pointer flex;

		.svws-ui-table & {
			@apply p-0 -my-0.5;
		}

		.svws-dropdown-icon,
		.svws-remove {
			@apply inline-flex w-5 h-7 absolute text-headline-md top-1 rounded items-center justify-center;
		}

		.svws-dropdown-icon {
			@apply bg-ui-neutral border border-ui-secondary;
			@apply pointer-events-none right-1;
		}

		.text-input-component {
			&:hover,
			&:focus-visible,
			&:focus-within {
				@apply grow;
				& ~ .svws-dropdown-icon {
					@apply bg-ui-neutral-hover border-ui-neutral-hover;
					/* TODO: COLORS icon */
				}
			}
		}

		&.svws-statistik {
			.svws-dropdown-icon {
				@apply bg-ui-statistic-weak text-ui-statistic border-ui-onstatistic-secondary-hover;
				/* TODO: COLORS icon */
			}

			.text-input-component {
				&:hover,
				&:focus-visible,
				&:focus-within {
					~ .svws-dropdown-icon {
						@apply border-ui-statistic bg-ui-statistic-weak;
						/* TODO: COLORS icon */
					}
				}
			}
		}

		.svws-remove {
			@apply right-7 top-2 w-5 h-5 text-ui-secondary;

			.icon {
				margin-top: 0.3rem;
				/* TODO: COLORS icon */
			}

			&:hover {
				@apply text-ui-danger;
			}

			&:focus,
			&:focus-visible {
				@apply outline-none;
			}

			&:focus-visible {
				@apply ring ring-ui-danger bg-ui-danger text-ui-ondanger;
			}
		}

		&.svws-open {
			@apply z-50;
		}

		&:not(.svws-readonly) {
			.text-input--control,
			.text-input--headless {
				@apply text-ellipsis break-all cursor-pointer;
			}
		}

		&.svws-readonly {
			.text-input--control,
			.text-input--headless {
				@apply text-ellipsis break-all cursor-default;
			}
		}

		.text-input--control {
			@apply pr-8;
		}

		.text-input--headless {
			@apply pl-5;
		}

		&.svws-removable&.svws-has-value {
			.text-input--control {
				@apply pr-12;
			}

			.text-input--headless {
				@apply pl-[2.1rem];
			}
		}

		&.svws-headless {
			.svws-dropdown-icon,
			.svws-remove {
				@apply right-auto h-5 top-0;
			}

			.svws-dropdown-icon {
				@apply text-ui-secondary bg-transparent;
				@apply w-4 border-0 text-sm;
				/* TODO: COLORS icon */

				.svws-ui-table .svws-clicked & {
					@apply text-ui-selected;
				}
			}

			.svws-remove {
				@apply right-auto left-4 inline-flex items-center justify-center w-4;

				svg {
					@apply -m-px;
				}
			}

			.text-input-component {
				@apply pr-1 my-0;

				&:hover,
				&:focus-visible,
				&:focus-within {
					~ .svws-dropdown-icon {
						@apply text-ui;
						/* TODO: COLORS icon */
					}
				}
			}

			&:not(.svws-open) {
				.text-input-component {
					&:focus-visible {
						~ .svws-dropdown-icon {
							@apply ring ring-ui;
						}
					}
				}
			}

			&.svws-statistik {
				.svws-dropdown-icon {
					@apply text-ui-statistic;
					/* TODO: COLORS icon */
				}

				.text-input-component {
					&:hover,
					&:focus-visible,
					&:focus-within {
						~ .svws-dropdown-icon {
							@apply bg-ui-statistic-weak;
						}
					}
				}
			}
		}

		&.svws-danger {
			@apply text-ui-danger;

			.text-input--headless {
				@apply font-bold;
			}

			&.svws-headless {
				.svws-dropdown-icon {
					@apply text-ui-danger;
					/* TODO: COLORS icon */
				}
			}
		}

		&.svws-disabled {
			@apply cursor-default pointer-events-none;

			&.svws-headless .text-input--headless,
			&.svws-removable&.svws-has-value .text-input--headless {
				@apply opacity-25 pl-5;
			}

			.svws-dropdown-icon {
				@apply opacity-10;
			}

			.svws-remove {
				@apply hidden;
			}
		}
	}

</style>