<script lang="ts">
	export default {
		inheritAttrs: false
	};
</script>
<script setup lang="ts">
	import { InputType } from "../../types";
	import { genId } from "../../utils";

	const {
		type = "text",
		modelValue = "",
		placeholder = "",
		statistics = false,
		valid = true,
		disabled = false,
		required = false,
		readonly = false,
		headless = false,
		focus = false,
		rounded = false
	} = defineProps<{
		type?: InputType;
		modelValue?: string | number;
		placeholder?: string;
		statistics?: boolean;
		valid?: boolean;
		disabled?: boolean;
		required?: boolean;
		readonly?: boolean;
		headless?: boolean;
		focus?: boolean;
		rounded?: boolean;
	}>();

	const emit = defineEmits<{
		(e: "update:modelValue", value: string | number): void;
	}>();

	const slots = useSlots();

	const input = ref<null | HTMLElement>(null);
	const vFocus = {
		mounted: (el: HTMLInputElement) => {
			if (focus) el.focus();
		}
	};

	const emailValid = computed(() => {
		if (type !== "email" || !modelValue) return true;
		else {
			return (
				// eslint-disable-next-line no-useless-escape
				/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))[^@]?$/.test(modelValue as string) ||
				// eslint-disable-next-line no-useless-escape
				/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(
					modelValue as string
				)
			);
		}
	});
	const hasIcon = computed(() => !!slots.default);

	const hasContent = ref(!!modelValue);
	watch(
		() => modelValue,
		value => {
			hasContent.value = !!value;
		}
	);
	function onInput(event: Event) {
		hasContent.value = (event.target as HTMLInputElement)?.value.trim() != "";
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
			'text-input-filled': hasContent,
			'text-input-invalid': !valid || !emailValid,
			'text-input-disabled': disabled,
			'text-input-readonly': readonly,
			'text-input--icon': hasIcon,
			'text-input--statistics': statistics,
			'text-input--search': type === 'search',
		}">
		<input ref="input"
			v-focus
			:class="{
				'text-input--control': !headless,
				'text-input--headless': headless,
				'text-input--rounded': rounded
			}"
			v-bind="{ ...$attrs }"
			:type="type"
			:value="modelValue"
			:disabled="disabled"
			:required="required"
			:readonly="readonly"
			:aria-labelledby="labelId"
			@input="onInput">
		<span v-if="placeholder && !headless"
			:id="labelId"
			class="text-input--placeholder"
			:class="{
				'text-input--placeholder--required': required
			}">
			{{ placeholder }}
			<i-ri-bar-chart-fill v-if="statistics" class="ml-2" />
		</span>
		<Icon v-if="type !== 'date' && hasIcon">
			<slot />
		</Icon>
		<Icon v-else-if="type === 'date'" class="text-input--calendar-icon">
			<i-ri-calendar-line />
		</Icon>
	</label>
</template>

<style lang="postcss" scoped>
	.text-input-component {
		@apply flex;
		@apply relative;
		@apply w-full;
		@apply overflow-hidden whitespace-nowrap;
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
		@apply rounded-md border border-black border-opacity-20;
		@apply h-9 w-full;
		@apply text-base;
		@apply whitespace-nowrap;
		padding: 0.5em 0.7em;
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
		@apply border-gray border-opacity-100;
		@apply outline-none;
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
		@apply pointer-events-none cursor-default;
	}

	.text-input--placeholder {
		@apply absolute;
		@apply pointer-events-none;
		@apply opacity-50;
		@apply transform;
		@apply flex items-center;

		top: 0.5em;
		left: 0.7em;
		line-height: 1.33;
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
		@apply border-purple;
		@apply bg-purple bg-opacity-[0.02];
	}

	.text-input-invalid .text-input--control {
		@apply border-error;
	}

	.text-input--statistics .text-input--placeholder {
		@apply text-purple;
	}

	.text-input-invalid .text-input--placeholder,
	.text-input-invalid .text-input--control {
		@apply text-error;
	}

	.text-input-disabled {
		@apply cursor-not-allowed;
		@apply opacity-50;
	}

	.text-input-component:focus-within,
	.text-input-filled {
		@apply overflow-visible;
	}

	.text-input--placeholder--required:after {
		@apply text-error;
		content: " *";
	}

	.text-input--icon .text-input--control {
		@apply pr-8;
	}

	.text-input--headless {
		@apply w-full whitespace-nowrap border-0 bg-transparent px-3 font-medium text-black outline-none;
	}
</style>
<style lang="postcss">
	.text-input--inline {
		@apply cursor-text underline decoration-dashed underline-offset-2;
	}
</style>
