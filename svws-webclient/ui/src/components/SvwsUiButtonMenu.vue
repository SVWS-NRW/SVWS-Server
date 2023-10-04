<template>
	<div class="flex" ref="button">
		<button class="button" @click="selectDefault" :class="{
			'button--primary': type === 'primary' || !type,
			'button--secondary': type === 'secondary',
			'button--danger': type === 'error' || type === 'danger',
			'button--transparent': type === 'transparent',
			'button--icon': type === 'icon',
			'button--trash': type === 'trash',
			'button--small': size === 'small',
			'button--big': size === 'big',
		}" :disabled="disabled">
			<slot />
		</button>
		<button class="button" @click="showList = !showList">{{ showList === true ? 'A':'V' }}</button>
	</div>
	<Teleport to="body">
		<svws-ui-dropdown-list v-if="showList" :filtered-list="items" :item-text="itemText"
			:strategy="strategy" :floating-left="floatingLeft" :floating-top="floatingTop"
			:tags="false" :statistics="false" :selected-item-list="new Set()"
			:select-item="selectItem" ref="refList" />
	</Teleport>
</template>

<script lang="ts" setup generic="Item">
	import type { ComponentExposed } from 'vue-component-type-helpers';
	import type { ButtonType } from '../types';
	import type { Ref} from 'vue';
	import type { MaybeElement} from '@floating-ui/vue';
	import { useFloating, flip, shift, offset, size as floatSize, autoUpdate } from '@floating-ui/vue';
	import { onClickOutside } from '@vueuse/core'
	import { computed, ref } from 'vue';
	import SvwsUiDropdownList from "./SvwsUiDropdownList.vue";

	const props = withDefaults(defineProps<{
		itemText: (item: Item) => string;
		items: Iterable<Item> | Item[];
		default?: Item;
		type?: ButtonType;
		disabled?: boolean;
		size?: 'small' | 'normal' | 'big';
	}>(),{
		default: undefined,
		type: 'primary',
		disabled: false,
		size: 'normal'
	});

	const emit = defineEmits<{
		selected: [item: Item];
	}>();

	const refList = ref<ComponentExposed<typeof SvwsUiDropdownList> | null>(null);
	const button = ref(null);
	const showList = ref(false);

	const {x, y, strategy} = useFloating(
		button,
		refList as Readonly<Ref<MaybeElement<HTMLElement>>>,
		{
			placement: 'bottom',
			middleware: [flip(), shift(), offset(2), floatSize({
				apply({rects, elements}) {
					Object.assign(elements.floating.style, {
						width: `${rects.reference.width}px`
					});
				}
			})],
			whileElementsMounted: autoUpdate,
		}
	);

	const floatingTop = computed(() => `${y.value ?? 0}px`);
	const floatingLeft = computed(() => `${x.value ?? 0}px`);

	function selectItem(item: Item | null | undefined) {
		if (item === null || item === undefined)
			return;
		showList.value = false;
		emit('selected', item);
	}

	function selectDefault() {
		if (props.default !== undefined)
			selectItem(props.default);
	}

	onClickOutside(button, ()=> showList.value = false);

</script>

<style lang="postcss">
.button {
	@apply rounded-md border;
	@apply select-none relative;
	@apply text-button font-bold;
	@apply flex items-center;
	gap: 0.25em;
	padding: 0.45em 0.75em;

  .svws-ui-table-filter & {
    min-height: 2.25rem;
  }

	svg {
		margin-top: -0.1em;
		margin-bottom: -0.1em;
	}

	&:hover {
		@apply brightness-110;
	}

	&:focus {
		@apply outline-none;
	}

	&:focus-visible {
		@apply ring;
	}

	&:active {
		@apply ring-0 brightness-100;
	}

	.svws-ui-tfoot & {
		@apply h-7 min-h-[unset];
	}
}

.button--primary {
	@apply bg-svws text-white border-svws;

	.page--statistik & {
		@apply bg-violet-500 border-violet-500;
	}

	&:focus-visible {
		@apply ring-svws/50;

		.page--statistik & {
			@apply ring-violet-500/50;
		}
	}
}

.button--secondary {
	@apply bg-transparent dark:bg-transparent text-black dark:text-white border-black/90 dark:border-white/90;

	.notification--error &,
	.svws-ui-stundenplan--unterricht--warning & {
		@apply text-white dark:text-black border-white/25 dark:border-black/25;
	}

	&:hover {
		@apply border-svws text-svws;

		.page--statistik & {
			@apply border-violet-500 text-violet-500;
		}

		.notification--error &,
		.svws-ui-stundenplan--unterricht--warning & {
			@apply border-white dark:border-black text-white dark:text-black brightness-100;
		}
	}

	&:focus-visible {
		@apply ring-svws/25 border-svws;

		.page--statistik & {
			@apply ring-violet-500/25 border-violet-500;
		}

		.notification--error &,
		.svws-ui-stundenplan--unterricht--warning & {
			@apply ring-white/25 dark:ring-black/25 border-white dark:border-black;
		}
	}

	&:active {
		@apply bg-svws/5 brightness-100;

		.notification--error &,
    .svws-ui-stundenplan--unterricht--warning &{
			@apply bg-white/10 dark:bg-black/10;
		}
	}
}

.button--transparent {
	@apply bg-transparent border-transparent dark:bg-transparent dark:border-transparent;

	&:hover {
		@apply bg-black/10 dark:bg-white/5 brightness-95;
	}

	&:focus-visible {
		@apply bg-black/10 dark:bg-white/5 ring-black/25 dark:ring-white/25;
	}
}

.button--danger {
	@apply bg-transparent dark:bg-transparent text-error border-error;

	&:hover,
	&:focus {
		@apply bg-error text-white;
	}

	&:focus-visible {
		@apply ring-error/50;
	}
}

.button--trash {
	@apply bg-transparent rounded relative;
	@apply py-0 px-2;
	@apply text-error;
	border: 0 !important;
	padding: 0.2em !important;
	width: 1.6em;
	height: 1.6em;

	.icon--fill {
		@apply hidden;
	}

	&:hover {
		@apply bg-error text-white;

		.icon--line {
			@apply hidden;
		}

		.icon--fill {
			@apply inline-block;
		}
	}

	&:focus {
		@apply bg-error;
		@apply text-white;
	}

	&:focus-visible {
		@apply ring-error/25;
	}
}

.button--trash,
.button--icon {
	&.button--small,
  	.svws-ui-tbody & {
		@apply text-sm font-medium h-6 w-6;
		padding: 0.3em !important;

		svg {
			width: 1.2em;
			height: 1.2em;
		}
	}

	.svws-ui-tfoot &.button {
		@apply text-button h-7 w-7;
		padding: 0.25em !important;

		svg {
			width: 1.3em;
			height: 1.3em;
		}
	}
}

.button--icon {
	@apply p-1.5 justify-center border-0 items-center;
	@apply w-9 h-9;

	svg {
		width: 1.3rem;
		height: 1.3rem;
	}

	&:hover, &:focus {
		@apply bg-black/10 dark:bg-white/5;
	}

	&:focus-visible {
		@apply ring-black/25 dark:ring-white/25;
	}
}

.button:disabled {
	&,
	&:hover,
	&:focus,
	&:focus-visible {
		@apply bg-black/25 border-black/50 text-black dark:bg-white/25 dark:border-white/50 dark:text-white;
		@apply opacity-25;
		@apply cursor-not-allowed pointer-events-none;
	}
}

.hover--danger:hover {
	@apply text-error bg-error/10;
}

.button--small,
.content-card--header .button,
.svws-ui-tbody .button,
.router-tab-bar--subnav .button,
.router-tab-bar--subnav .radio--label {
	@apply text-sm font-bold;
}

.button--small,
.svws-ui-tbody .button,
.content-card--header .button,
.router-tab-bar--subnav .button {
	padding: 0.3rem 0.65rem;
}

.svws-ui-tbody .button {
	@apply -my-2 h-full self-center rounded;
	padding: 0.1rem 0.5rem;

	&.button--transparent {
		@apply px-1.5;
	}
}

.svws-ui-tbody .button--icon.button--small {
	@apply w-5 h-5 -m-0.5;
	@apply p-0 !important;
}

.button--big {
	padding-top: 0.64em;
	padding-bottom: 0.64em;
}

.button--badge {
	@apply absolute top-0 left-[100%];
	@apply font-bold text-white;
	@apply bg-error rounded-full shadow;
	@apply flex items-center justify-center;
	@apply pointer-events-none;
	@apply -mt-3 -ml-3;
	@apply px-1.5;
	@apply h-5;
	font-size: 0.8rem;

	.svws-ui-tfoot .button &,
	.content-card--header .button &,
	.router-tab-bar--subnav .button & {
		@apply -mt-1 h-4 -ml-1.5;
		font-size: 0.75rem;
	}

	.router-tab-bar--subnav .button & {
		@apply -ml-3;
	}
}
</style>
