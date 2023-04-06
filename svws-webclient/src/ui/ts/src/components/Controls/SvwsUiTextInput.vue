<script lang="ts">
	export default {
		inheritAttrs: false
	};
</script>
<script setup lang="ts">
	import { useSlots, ref, computed, watch } from "vue";
	import type { InputType } from "../../types";
	import { genId } from "../../utils";

	const props = withDefaults(defineProps<{
		type?: InputType;
		modelValue?: string | number | null;
		placeholder?: string;
		statistics?: boolean;
		valid?: boolean;
		disabled?: boolean;
		required?: boolean;
		readonly?: boolean;
		headless?: boolean;
		focus?: boolean;
		rounded?: boolean;
		url?: boolean;
	}>(), {
		type: "text",
		modelValue: "",
		placeholder: "",
		statistics: false,
		valid: true,
		disabled: false,
		required: false,
		readonly: false,
		headless: false,
		focus: false,
		rounded: false,
		url: false,
	});

	const emit = defineEmits<{
		(e: "update:modelValue", value: string | number): void;
	}>();

	const slots = useSlots();

	const tmp = ref<string | number | null>(props.modelValue);

	watch(()=>props.modelValue, (neu)=>tmp.value = neu);

	const input = ref<null | HTMLElement>(null);
	const vFocus = {
		mounted: (el: HTMLInputElement) => {
			if (props.focus) el.focus();
		}
	};

	const emailValid = computed(() => {
		if (props.type !== "email" || !tmp.value)
			return true;
		else
			return (
				// eslint-disable-next-line no-useless-escape
				/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))[^@]?$/.test(tmp.value as string) ||
				// eslint-disable-next-line no-useless-escape
				/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(
					tmp.value as string
				)
			);
	});

	const hasIcon = computed(() => !!slots.default);

	function onInput(event: Event) {
		tmp.value = (event.target as HTMLInputElement).value;
		emit("update:modelValue", (event.target as HTMLInputElement).value);
	}

	defineExpose({
		input
	});

	const labelId = genId();
</script>

<template>
	<label class="text-input-component"
		:class="{
			'text-input-filled': `${tmp}`.length > 0,
			'text-input-invalid': (valid === false) || (emailValid === false),
			'text-input-disabled': disabled,
			'text-input-readonly': readonly,
			'text-input--icon': hasIcon,
			'text-input--statistics': statistics,
			'text-input--search': type === 'search',
		}">
		<span v-if="url" data-before="https://" class="pointer-events-none absolute inset-y-0 left-0 flex items-center pl-3 opacity-60 before:content-[attr(data-before)]" />
		<i-ri-search-line v-if="type === 'search'" class="text-input--search-icon" />
		<input ref="input"
			v-focus
			:class="{
				'text-input--control': !headless,
				'text-input--headless': headless,
				'text-input--rounded': rounded,
				'text-input--prefix': url,
			}"
			v-bind="{ ...$attrs }"
			:type="type"
			:value="tmp"
			:disabled="disabled"
			:required="required"
			:readonly="readonly"
			:aria-labelledby="labelId"
			:placeholder="headless || type === 'search' ? placeholder : ''"
			@input="onInput">
		<span v-if="placeholder && !headless && type !== 'search'"
			:id="labelId"
			class="text-input--placeholder"
			:class="{
				'text-input--placeholder--required': required,
				'text-input--placeholder--prefix': url
			}">
			{{ placeholder }}
			<span v-if="statistics" class="cursor-pointer">
				<svws-ui-tooltip position="right">
					<i-ri-bar-chart-fill class="pointer-events-auto ml-1" />
					<template #content>
						Relevant für die Statistik
					</template>
				</svws-ui-tooltip>
			</span>
		</span>
		<svws-ui-icon v-if="type !== 'date' && hasIcon">
			<slot />
		</svws-ui-icon>
		<svws-ui-icon v-else-if="type === 'date'" class="text-input--calendar-icon">
			<i-ri-calendar-line />
		</svws-ui-icon>
	</label>
</template>

<style lang="postcss" scoped>
	.text-input-component {
		@apply flex;
		@apply relative;
		@apply w-full;
		@apply overflow-hidden whitespace-nowrap;

		input::placeholder {
			@apply text-black/25;
		}
	}

	.text-input-component .icon {
		@apply absolute;
		@apply flex;
		@apply inset-y-0 right-0;
		@apply items-center justify-center;
		@apply opacity-20;
		@apply pointer-events-none;
		@apply rounded;
		@apply w-8;

		margin-bottom: 1px;
		margin-right: 1px;
		margin-top: 1px;
	}

	.text-input-component:focus-within .icon,
	.text-input-filled .icon {
		@apply opacity-100;
	}

	.text-input-invalid .icon {
		@apply text-error;
	}

	.text-input--control {
		@apply bg-white;
		@apply rounded-md border border-black border-opacity-25;
		@apply h-9 w-full;
		@apply text-base;
		@apply whitespace-nowrap;
		padding: 0.5em 0.7em;
	}

	.text-input--control[type="number"] {
		padding-right: 0.5em;
	}

	.text-input--prefix {
		padding-left: 4.3em;
	}

	.text-input--rounded {
		@apply rounded-full;
	}

	.multiselect-input-component .text-input--control {
		@apply overflow-hidden text-ellipsis;
		padding-right: 3.5em;
	}

	.text-input-component:focus-within .text-input--control,
	.text-input-filled .text-input--control {
		@apply border-black;
		@apply outline-none;
	}

	.text-input--statistics.text-input-component:focus-within .text-input--control,
	.text-input--statistics.text-input-filled .text-input--control {
		@apply border-purple-500;
	}

	.text-input--search {
		@apply relative;

		&-icon {
			@apply absolute left-2 opacity-40;
			top: 50%;
			transform: translateY(-50%);

			.text-input-component:not(.text-input-filled):not(:focus-within):not(.text-input-disabled):hover & {
				@apply opacity-60;
			}

			.text-input-component:focus-within &,
			.text-input-filled & {
				@apply opacity-100;
			}
		}

		input {
			@apply pl-8;
		}
	}

	/*.text-input--search:not(.text-input-filled) .text-input--placeholder {
		@apply sr-only;
	}

	.text-input--search:not(.text-input-filled) .text-input--control {
		@apply border-transparent bg-transparent text-transparent;
		margin-bottom: -2em;
	}

	.text-input-component.text-input--search:focus-within .text-input--control {
		@apply mb-0;
	}

	.text-input--search:not(.text-input-filled) .icon {
		@apply bg-primary;
	}*/

	.text-input--control[type="date"]::-webkit-inner-spin-button,
	.text-input--control[type="date"]::-webkit-calendar-picker-indicator {
		@apply opacity-0;
	}

	.text-input--control[type="date"] {
		color: transparent;
		@apply bg-transparent;
	}

	.text-input-component:focus-within .text-input--control[type="date"],
	.text-input-filled .text-input--control[type="date"] {
		@apply text-black;
		@apply pr-1;
	}

	@-moz-document url-prefix() {
		.text-input-component:focus-within .text-input--calendar-icon,
		.text-input-filled .text-input--calendar-icon {
			@apply hidden;
		}
	}

	.text-input-readonly .text-input--control {
		@apply pointer-events-none cursor-default select-none;
	}

	.text-input--placeholder {
		@apply absolute;
		@apply pointer-events-none;
		@apply opacity-40;
		@apply transform;
		@apply flex items-center;

		top: 0.5em;
		left: 0.7em;
		line-height: 1.33;
	}

	.text-input-component:not(.text-input-filled):not(:focus-within):not(.text-input-disabled):hover .text-input--placeholder {
		@apply opacity-60;
	}

	.text-input--placeholder--prefix {
		left: 4.5em;
		top: 0.5em;
	}

	.multiselect-input-component .text-input--placeholder {
		top: 0.5em;
	}

	.text-input-component:focus-within .text-input--placeholder,
	.text-input-filled .text-input--placeholder {
		@apply -translate-y-1/2;
		@apply bg-white opacity-100;
		@apply rounded;
		@apply px-1;

		top: 0;
		left: 0.7em;
		font-size: 0.78rem;

		&:after {
			content: "";
		}
	}

	.app-layout--secondary.text-input--control,
	.app-layout--secondary.text-input-component:focus-within .text-input--placeholder,
	.app-layout--secondary.text-input-filled .text-input--placeholder {
		@apply bg-light;
	}

	.text-input--statistics .text-input--control {
		@apply border-purple-500;
		/*@apply bg-purple/5;*/
	}

	.text-input-invalid:not(:focus-within) .text-input--control {
		@apply border-error;
	}

	.text-input--statistics .text-input--placeholder {
		@apply font-bold text-purple-500;
	}

	.text-input-invalid:not(:focus-within) .text-input--placeholder,
	.text-input-invalid:not(:focus-within) .text-input--control {
		@apply text-error;
	}

	.text-input-disabled {
		@apply cursor-not-allowed;

		.text-input--placeholder {
			@apply text-black/25;
		}
	}

	.text-input--control:disabled {
		@apply bg-black bg-opacity-10 border-black border-opacity-50 text-black;
		@apply opacity-20;
		@apply pointer-events-none;
	}

	.text-input-component:focus-within,
	.text-input-filled {
		@apply overflow-visible;
	}

	.text-input--placeholder--required:after {
		@apply text-error;
		content: "*";
	}

	.text-input--icon .text-input--control {
		@apply pr-8;
	}

	.text-input--headless {
		@apply w-full whitespace-nowrap border-0 outline-none;

		&:not([class*="bg-"]) {
			background-color: unset;
		}

		&::placeholder {
			@apply opacity-40;
			color: inherit;
		}

		&:hover:not(:focus) {
			@apply underline decoration-dashed underline-offset-2;

			&::placeholder {
				@apply opacity-60;
			}
		}
	}
	.text-input--inline {
		@apply cursor-text underline decoration-dashed underline-offset-2;
	}
</style>
