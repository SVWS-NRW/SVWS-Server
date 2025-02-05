<template>
	<div class="inline-flex">
		<label class="svws-ui-checkbox" :class="{'svws-statistik': statistics, 'svws-bw': bw, 'svws-ui-toggle': type === 'toggle'}" :title :color>
			<input type="checkbox" v-model="checked" :class="{'svws-headless': headless && type !== 'toggle', 'contentFocusField': focusClassContent}" :disabled :readonly :indeterminate :color ref="input">
			<span v-if="type === 'toggle'" class="svws-ui-toggle--icon" />
			<span v-if="$slots.default" class="svws-ui-checkbox--label">
				<span v-if="statistics" class="-mb-1 inline-block align-top">
					<svws-ui-tooltip position="right">
						<span class="icon icon-ui-statistic i-ri-bar-chart-2-line pointer-events-auto mr-1" />
						<template #content>Relevant für die Statistik</template>
					</svws-ui-tooltip>
				</span>
				<slot />
			</span>
		</label>
	</div>
</template>

<script lang="ts" setup>

	import { onMounted } from 'vue';
	import { computed, ref } from 'vue';

	const props = withDefaults(defineProps<{
		modelValue: boolean;
		statistics?: boolean;
		disabled?: boolean;
		bw?: boolean;
		title?: string;
		type?: 'checkbox' | 'toggle';
		headless?: boolean;
		indeterminate?: boolean;
		readonly?: boolean;
		color?: 'success' | 'error' | 'warning';
		autofocus?: boolean;
		focusClassContent?: boolean;
	}>(), {
		statistics: false,
		disabled: false,
		bw: false,
		title: undefined,
		type: 'checkbox',
		headless: false,
		indeterminate: false,
		readonly: false,
		color: undefined,
		autofocus: false,
		focusClassContent: false,
	});

	onMounted(() => doFocus())

	function doFocus() {
		if (props.autofocus)
			input.value?.focus();
	}

	const emit = defineEmits<{
		(e: 'update:modelValue', event: boolean): void;
	}>();

	const checked = computed<boolean>({
		get: () => props.modelValue,
		set: (value) =>	{
			if (props.readonly === false)
				emit("update:modelValue", value);
		},
	})

	const input = ref<null | HTMLInputElement>(null);
	const content = computed<boolean>(() => checked.value);

	defineExpose({ content, input });
</script>

<style lang="postcss">

	@reference "../../assets/styles/index.css";

	.svws-ui-checkbox,
	input[type="checkbox"] {
		@apply text-inherit accent-ui;

		&[color="success"] {
			@apply accent-ui-success text-ui-success;
		}

		&[color="error"] {
			@apply accent-ui-danger text-ui-danger;
		}

		&[color="warning"] {
			@apply accent-ui-warning text-ui-warning;
		}

		&.svws-statistik,
		&.svws-statistik input[type="checkbox"] {
			@apply text-ui-statistic accent-ui-statistic;
		}

		&.svws-bw,
		&.svws-bw input[type="checkbox"] {
			@apply accent-ui-neutral;
		}

		.svws-loading &,
		&.svws-loading {
			@apply accent-ui-disabled;
			@apply animate-pulse;

			&,
			.svws-ui-checkbox--label {
				@apply cursor-wait;
			}
		}
	}

	.svws-ui-checkbox {
		@apply inline-flex items-start text-base leading-tight my-0.5;

		.router-tab-bar--subnav & {
			@apply text-sm font-bold my-0;
			padding: 0.3rem 0.65rem;

			&:first-child {
				@apply -ml-3;
			}

			.svws-ui-checkbox--label {
				@apply mt-0;
			}
		}
	}

	input[type="checkbox"] {
		@apply h-4 w-4 cursor-pointer;

		&:focus,
		&:focus-visible {
			@apply ring;
			@apply outline-hidden;
		}

		&:focus-visible {
			@apply ring-3 ring-offset-2 ring-ui;

			&[color="success"] {
				@apply ring-ui-success;
			}

			&[color="error"] {
				@apply ring-ui-danger;
			}

			&[color="warning"] {
				@apply ring-ui-warning;
			}

			&.svws-statistik,
			.svws-statistik & {
				@apply ring-ui-statistic;
			}
		}

		& ~ .svws-ui-checkbox--label {
			@apply cursor-pointer ml-1.5;
			margin-top: -0.1rem;
		}

		&[disabled] {
			@apply pointer-events-none cursor-default accent-ui-disabled;

			& ~ .svws-ui-checkbox--label,
			& ~ .svws-ui-toggle--icon {
				@apply text-ui-disabled;
				@apply cursor-default;
			}
		}

		&.svws-headless {
			@apply flex appearance-none items-center justify-center rounded-sm border border-transparent font-bold p-0 bg-transparent;

			&:before {
				content: '';
			}

			&:not(:checked) {
				@apply border-ui-dark-gray;
			}

			&:hover:not([disabled]),
			&:focus-visible:not([disabled]) {
				@apply border-ui-dark-gray;
			}

			&:not(:checked)[disabled] {
				@apply border-transparent opacity-25;

				&:before {
					content: '\2715';
					font-size: 75%;
				}
			}

			&:checked {
				font-size: 95%;

				&[disabled] {
					@apply opacity-75;
				}

				&:before {
					content: '\2713';
				}
			}
		}
	}

	.svws-ui-toggle {
		.svws-ui-toggle--icon {
			@apply bg-ui-neutral border border-ui-neutral;
			@apply -ml-4 flex h-4 w-8 shrink-0 cursor-pointer items-center justify-start overflow-hidden rounded-[0.3rem] shadow-inner p-px;

			&:before {
				content: '';
				@apply bg-ui-white border border-ui;
				@apply inline-block h-full w-4 rounded-[0.2rem];
			}
		}

		&:hover {
			.svws-ui-toggle--icon,
			.svws-ui-toggle--icon:before {
				@apply border-ui-neutral-hover;
			}

			input[type="checkbox"]:checked ~ .svws-ui-toggle--icon {
				@apply bg-ui-brand-hover border-transparent;
			}
		}

		&:has([disabled]) {
			@apply pointer-events-none;
		}

		input[type="checkbox"] {
			@apply mr-0 !opacity-0;

			&:focus-visible ~ .svws-ui-toggle--icon {
				@apply ring-3 ring-offset-1 ring-ui-brand;
			}

			&:checked ~ .svws-ui-toggle--icon {
				@apply bg-ui-brand border-ui-brand;
				@apply justify-end;

				&:before {
					@apply border-transparent;
				}
			}

			&:checked:focus-visible ~ .svws-ui-toggle--icon {
				@apply bg-ui-brand-hover;
			}

			&:indeterminate ~ .svws-ui-toggle--icon {
				@apply justify-center;
			}

			&[disabled] ~ .svws-ui-toggle--icon {
				@apply opacity-50;

				&:before {
					@apply border-ui-neutral;
				}
			}
		}

		&.svws-statistik {
			input[type="checkbox"] {
				&:focus-visible ~ .svws-ui-toggle--icon {
					@apply ring-ui-statistic;
				}

				&:checked ~ .svws-ui-toggle--icon {
					@apply bg-ui-statistic border-ui-statistic;
				}

				&:checked:focus-visible ~ .svws-ui-toggle--icon {
					@apply bg-ui-statistic-hover;
				}
			}
		}

		&.svws-bw {
			input[type="checkbox"] {
				&:checked ~ .svws-ui-toggle--icon {
					@apply bg-ui-inverted border-transparent;

					&:before {
						@apply border-transparent bg-ui;
					}
				}
			}
		}

		&.svws-loading {
			input[type="checkbox"] {
				@apply invisible;
			}
		}
	}
</style>
