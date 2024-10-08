<template>
	<div class="inline-flex">
		<label class="svws-ui-checkbox" :class="{'svws-statistik': statistics, 'svws-bw': bw, 'svws-ui-toggle': type === 'toggle'}" :title>
			<input type="checkbox" v-model="checked" :class="{'svws-headless': headless && type !== 'toggle'}" :disabled :readonly :indeterminate :color ref="input">
			<span v-if="type === 'toggle'" class="svws-ui-toggle--icon" />
			<span v-if="$slots.default" class="svws-ui-checkbox--label">
				<span v-if="statistics" class="mr-1 -mb-1 inline-block align-top">
					<svws-ui-tooltip position="right">
						<span class="icon i-ri-bar-chart-2-line pointer-events-auto" />
						<template #content>
							Relevant für die Statistik
						</template>
					</svws-ui-tooltip>
				</span>
				<slot />
			</span>
		</label>
	</div>
</template>

<script lang="ts" setup>

	import type { ComputedRef, Ref } from 'vue';
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
	});

	const emit = defineEmits<{
		(e: 'update:modelValue', event: boolean): void;
	}>();

	const checked = computed<boolean>({
		get: () => props.modelValue,
		set: (value) =>	{
			if (props.readonly === false)
				emit("update:modelValue", value);
		}
	})

	const input = ref<null | HTMLInputElement>(null);
	const content = computed<boolean>(() => checked.value);

	defineExpose<{
		content: ComputedRef<boolean>,
		input: Ref<HTMLInputElement | null>,
	}>({ content, input });
</script>

<style lang="postcss">
	.svws-ui-checkbox {
		@apply inline-flex items-start text-base leading-tight my-0.5;

		input[color="success"] { @apply accent-success; }
		input[color="error"] { @apply accent-error; }
		input[color="warning"] { @apply accent-orange-400; }

		&.svws-statistik {
			@apply text-violet-500;

			input[type="checkbox"] {
				@apply accent-violet-500;
			}
		}

		&.svws-bw {
			input[type="checkbox"] {
				@apply accent-black dark:accent-white;
			}
		}

		&.svws-loading {
			@apply animate-pulse;

			input[type="checkbox"],
			input[type="checkbox"] ~ .svws-ui-toggle--icon {
				@apply opacity-25 grayscale filter;
			}

			input[type="checkbox"],
			.svws-ui-checkbox--label {
				@apply cursor-wait;
			}
		}

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
		@apply h-4 w-4 cursor-pointer accent-svws;

		& ~ .svws-ui-checkbox--label {
			@apply cursor-pointer ml-1.5;
			margin-top: -0.1rem;
		}

		&[disabled] {
			@apply pointer-events-none cursor-default opacity-50;

			& ~ .svws-ui-checkbox--label,
			& ~ .svws-ui-toggle--icon {
				@apply cursor-default;
				@apply text-black/50 dark:text-white/50;
			}
		}

		&.svws-headless {
			@apply flex appearance-none items-center justify-center rounded border border-transparent font-bold opacity-50 p-0;

			&:before {
				content: '';
			}

			&:focus-visible {
				@apply outline-none ring-2 ring-black ring-offset-1;
			}

			&:not(:checked),
			&:hover:not([disabled]),
			&:focus-visible:not([disabled]) {
				@apply border-black/25 bg-black/5 dark:border-white/25 dark:bg-white/5;

				.table--with-background & {
					@apply dark:border-black/25 dark:bg-black/5;
				}
			}

			&:not(:checked)[disabled] {
				@apply border-transparent bg-transparent opacity-25 dark:border-transparent dark:bg-transparent;

				&:before {
					content: '\2715';
					font-size: 75%;
				}
			}

			&:checked {
				@apply opacity-100;
				font-size: 95%;

				&[disabled] {
					@apply opacity-75;
				}

				&:before {
					content: '\2713';
				}
			}

			&:hover,
			&:focus-visible {
				@apply opacity-100;

				&[disabled]:not(:checked) {
					@apply opacity-25;
				}
			}
		}
	}

	.svws-ui-toggle {
		.svws-ui-toggle--icon {
			@apply -ml-4 flex h-4 w-8 flex-shrink-0 cursor-pointer items-center justify-start overflow-hidden rounded-md bg-black/25 shadow-inner dark:bg-white/25;
			padding: 2px;

			&:before {
				content: '';
				@apply inline-block h-full w-4 bg-white shadow-md shadow-black/25 rounded-[0.275rem] dark:bg-black;
			}
		}

		&:hover {
			.svws-ui-toggle--icon {
				@apply bg-black/50 dark:bg-white/50;
			}
		}

		input[type="checkbox"] {
			@apply mr-0 opacity-0;

			&:focus-visible ~ .svws-ui-toggle--icon {
				@apply bg-black/50 ring-2 ring-offset-1 ring-svws dark:bg-white/50;
			}

			&:checked ~ .svws-ui-toggle--icon {
				@apply justify-end bg-svws dark:bg-svws;
			}

			&:indeterminate ~ .svws-ui-toggle--icon {
				@apply justify-center;
			}

			&[disabled] ~ .svws-ui-toggle--icon,
			&[disabled] ~ .svws-ui-toggle--icon {
				@apply opacity-10;
			}
		}

		&.svws-statistik {
			input[type="checkbox"] {
				&:focus-visible ~ .svws-ui-toggle--icon {
					@apply ring-violet-500;
				}

				&:checked ~ .svws-ui-toggle--icon {
					@apply bg-violet-500 dark:bg-violet-500;
				}
			}
		}

		&.svws-bw {
			input[type="checkbox"] {
				&:focus-visible ~ .svws-ui-toggle--icon {
					@apply ring-black;
				}

				&:checked ~ .svws-ui-toggle--icon {
					@apply bg-black dark:bg-black;
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
