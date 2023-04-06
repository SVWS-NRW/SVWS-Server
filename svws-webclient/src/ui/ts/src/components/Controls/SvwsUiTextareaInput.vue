<script setup lang="ts">
	import { ref, computed, onMounted, watch } from 'vue';

	type ResizableOption = "both" | "horizontal" | "vertical" | "none";

	const props = withDefaults(defineProps<{
		modelValue?: string | null;
		placeholder?: string;
		valid?: boolean;
		statistics?: boolean;
		required?: boolean;
		disabled?: boolean;
		resizeable?: ResizableOption;
		autoresize?: boolean;
		cols?: number;
		rows?: number;
	}>(), {
		modelValue: "",
		placeholder: "",
		valid: true,
		statistics: false,
		required: false,
		disabled: false,
		resizeable: "both",
		autoresize: false,
		cols: 80,
		rows: 3
	})

	const emit = defineEmits<{
		(e: "update:modelValue", value: string): void;
		(e: "focus", event: Event): void;
		(e: "blur", event: Event): void;
		(e: "click", event: Event): void;
		(e: "mousedown", event: Event): void;
		(e: "keydown", event: Event): void;
	}>();

	const focused = ref(false);
	const element = ref<HTMLElement | null>(null);
	const tag = computed(() => (props.autoresize ? "span" : "textarea"));
	const bindings = computed(() => {
		return {
			required: props.required,
			disabled: props.disabled,
			onInput,
			onFocus,
			onBlur,
			onClick,
			onMousedown,
			onKeydown,
			...(props.autoresize
				? {
					contenteditable: !props.disabled
				}
				: {
					rows: props.rows,
					cols: props.cols,
					value: props.modelValue,
				}),
		};
	});

	function onInput(event: Event) {
		const field = props.autoresize ? "innerText" : "value";
		emit("update:modelValue", (event.target as HTMLInputElement)[field]);
	}

	function onFocus(event: FocusEvent) {
		focused.value = true;
		emit("focus", event);
	}

	function onBlur(event: FocusEvent) {
		focused.value = false;
		emit("blur", event);
	}

	function onClick(event: MouseEvent) {
		emit("click", event);
	}

	function onMousedown(event: MouseEvent) {
		emit("mousedown", event);
	}

	function onKeydown(event: KeyboardEvent) {
		emit("keydown", event);
	}

	function updateContent(newcontent: string) {
		if (element.value) {
			element.value.innerText = newcontent;
		}
	}

	onMounted(() => {
		if (props.autoresize) {
			updateContent(props.modelValue ?? "");
		}
	});

	watch(
		() => props.modelValue,
		newval => {
			if (props.autoresize) {
				const currentText = element.value?.innerText ?? "";
				if (newval !== currentText) {
					updateContent(newval ?? "");
				}
			}
		}
	);
</script>

<template>
	<label class="textarea-input"
		:class="{
			'textarea-input-focus': focused,
			'textarea-input-filled': !!modelValue,
			'textarea-input-invalid': valid === false,
			'textarea-input-disabled': disabled,
			'textarea-input--statistics': statistics,
			'textarea-input--resize-none': resizeable === 'none',
			'textarea-input--resize-horizontal': resizeable === 'horizontal',
			'textarea-input--resize-vertical': resizeable === 'vertical',
			'textarea-input--resize-both': resizeable === 'both'
		}">
		<component :is="tag" v-bind="bindings" class="textarea-input--control" ref="element" />
		<span v-if="placeholder"
			class="textarea-input--placeholder"
			:class="{
				'textarea-input--placeholder--required': required
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
	</label>
</template>

<style lang="postcss">
	.textarea-input {
		@apply flex;
		@apply relative;
	}

	.textarea-input--control {
		@apply bg-white;
		@apply rounded-md border border-black border-opacity-20;
		@apply w-full;
		@apply text-base;
		@apply cursor-text;
		padding: 0.5em 0.7em;
		min-height: theme("spacing.9");
	}

	span.textarea-input--control {
		padding-top: 0.4em;
		padding-bottom: 0.4em;
	}

	.textarea-input-focus .textarea-input--control,
	.textarea-input-filled .textarea-input--control {
		@apply border-gray border-opacity-100;
		@apply outline-none;
	}

	.textarea-input-disabled .textarea-input--control {
		@apply cursor-not-allowed;
	}

	.textarea-input-invalid:not(:focus-within) .textarea-input--control {
		@apply border-error;
	}

	.textarea-input--resize-none .textarea-input--control {
		@apply resize-none;
	}

	.textarea-input--resize-vertical .textarea-input--control {
		@apply resize-y;
	}

	.textarea-input--resize-horizontal .textarea-input--control {
		@apply resize-x;
	}

	.textarea-input--resize-both .textarea-input--control {
		@apply resize;
	}

	.textarea-input--placeholder {
		@apply absolute;
		@apply pointer-events-none;
		@apply opacity-40;
		@apply transform;
		@apply flex items-center;

		top: 0.5em;
		left: 0.7em;
		line-height: 1.33;
	}

	.textarea-input-component:not(.textarea-input-filled):not(:focus-within):not(.textarea-input-disabled):hover .textarea-input--placeholder {
		@apply opacity-60;
	}

	.textarea-input--statistics .textarea-input--control {
		@apply border-purple-500;
		@apply bg-purple-500 bg-opacity-[0.02];
	}

	.textarea-input--statistics.textarea-input-invalid .textarea-input--control {
		@apply border-error;
	}

	.textarea-input--statistics .textarea-input--placeholder {
		@apply text-purple-500;
	}

	.textarea-input--statistics.textarea-input-invalid .textarea-input--placeholder {
		@apply text-purple-500;
	}

	.textarea-input-focus .textarea-input--placeholder,
	.textarea-input-filled .textarea-input--placeholder {
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

	.textarea-input-invalid:not(:focus-within) .textarea-input--placeholder,
	.textarea-input-invalid:not(:focus-within) .textarea-input--control {
		@apply text-error;
	}

	.textarea-input-disabled {
		@apply cursor-not-allowed;

		.textarea-input--placeholder {
			@apply text-black/25;
		}

		.textarea-input--control {
			@apply bg-black bg-opacity-10 border-black border-opacity-50 text-black;
			@apply opacity-20;
			@apply cursor-not-allowed;
		}
	}

	.textarea-input--placeholder--required:after {
		@apply text-error;
		content: " *";
	}

	.textarea--disguise {
		&:not(.textarea-input-filled):not(.textarea-input-focus) {
			.textarea-input--placeholder {
				&:after {
					content: ' hinzufügen…';
				}
			}
		}

		&:not(.textarea-input-filled):not(.textarea-input-focus):not(:hover):not(:focus) {
			.textarea-input--control {
				@apply border-transparent resize-none;
			}

			.textarea-input--placeholder {
				@apply left-0;
			}
		}
	}
</style>
