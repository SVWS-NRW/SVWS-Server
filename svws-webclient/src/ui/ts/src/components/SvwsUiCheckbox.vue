<script lang="ts" setup>

	import { computed, ref, watch } from 'vue';

	type CheckboxValue = string | number | boolean | null;
	type ModelValue = boolean | Array<CheckboxValue> | undefined | 'indeterminate';

	const props = withDefaults(defineProps<{
		value?: CheckboxValue;
		modelValue: ModelValue;
		statistics?: boolean;
		disabled?: boolean;
		bw?: boolean;
		title?: string;
		type?: 'checkbox' | 'toggle';
	}>(), {
		value: '',
		statistics: false,
		disabled: false,
		bw: false,
		title: undefined,
		type: 'checkbox'
	});

	const loading = ref(false);

	const value = computed({
		get: () => props.modelValue,
		set: (value) =>	{
			loading.value = true;
			emit("update:modelValue", value);

			if (value !== props.modelValue) {
				watch(() => props.modelValue, () => {
					loading.value = false;
				});
			}
		}
	})

	const emit = defineEmits<{
		(e: 'update:modelValue', event: ModelValue): void;
	}>();

</script>

<template>
	<label class="svws-ui-checkbox" :class="{'svws-statistik': statistics, 'svws-loading': loading, 'svws-bw': bw, 'svws-ui-toggle': type === 'toggle'}" :title="title">
		<input type="checkbox" v-model="value" :value="value" :disabled="disabled" :indeterminate="value === 'indeterminate'">
		<span v-if="type === 'toggle'" class="svws-ui-toggle--icon" />
		<span class="svws-ui-checkbox--label" v-if="$slots.default">
			<span v-if="statistics" class="mr-1 -mb-1 inline-block align-top">
				<svws-ui-tooltip position="right">
					<i-ri-bar-chart-2-line class="pointer-events-auto" />
					<template #content>
						Relevant für die Statistik
					</template>
				</svws-ui-tooltip>
			</span>
			<slot />
		</span>
	</label>
</template>

<style lang="postcss">
.svws-ui-checkbox {
	@apply inline-flex items-start text-base leading-tight my-0.5;

	.data-table__filter-simple & {
		@apply my-auto;
	}

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
}

input[type="checkbox"] {
	@apply h-4 w-4 cursor-pointer accent-svws;

	& ~ .svws-ui-checkbox--label {
		@apply cursor-pointer ml-1.5;
		margin-top: -0.1rem;
	}

	&[disabled] {
		@apply cursor-default opacity-50;

		& ~ .svws-ui-checkbox--label,
		& ~ .svws-ui-toggle--icon {
			@apply cursor-default;
			@apply text-black/50 dark:text-white/50;
		}
	}
}

.svws-ui-toggle {
	.svws-ui-toggle--icon {
		@apply flex h-4 w-8 cursor-pointer items-center justify-start overflow-hidden rounded-md bg-black/25 shadow-inner dark:bg-white/25 -ml-4;
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
