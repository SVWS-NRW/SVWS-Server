<script lang="ts" setup>

	import { computed } from 'vue';

	type CheckboxValue = string | number | boolean | null;
	type ModelValue = boolean | Array<CheckboxValue> | undefined | 'indeterminate';

	const props = withDefaults(defineProps<{
		value?: CheckboxValue;
		modelValue: ModelValue;
		statistics?: boolean;
		disabled?: boolean;
		circle?: boolean;
		headless?: boolean;
		bw?: boolean;
		span?: 'full';
		title?: string;
	}>(), {
		value: '',
		statistics: false,
		disabled: false,
		circle: false,
		headless: false,
		bw: false,
		span: undefined,
		title: undefined
	});


	const value = computed({
		get: () => props.modelValue,
		set: (value) =>	emit("update:modelValue", value)
	})

	const emit = defineEmits<{
		(e: 'update:modelValue', event: ModelValue): void;
	}>();

</script>

<template>
	<label role="none" class="checkbox"
		:class="{
			'checkbox--disabled': disabled,
			'checkbox--statistics': statistics,
			'checkbox--checked': value,
			'checkbox--circle': circle,
			'checkbox--headless': headless,
			'checkbox--indeterminate': value === 'indeterminate' && value !== undefined,
			'checkbox--bw': bw,
			'col-span-full': span === 'full'
		}">
		<input class="checkbox--control" type="checkbox" v-model="value" :value="value" :disabled="disabled" :title="disabled ? 'Hinweis: Checkbox deaktiviert' : (title || '')">
		<div v-if="value === 'indeterminate' && value !== undefined" role="checkbox" class="icon">
			<i-ri-checkbox-indeterminate-fill />
		</div>
		<div v-else-if="value" role="checkbox" class="icon">
			<i-ri-checkbox-fill v-if="!circle" />
			<i-ri-checkbox-circle-line v-if="circle" />
		</div>
		<div v-else-if="!value" role="checkbox" class="icon">
			<i-ri-checkbox-blank-line v-if="!circle" />
			<i-ri-checkbox-blank-circle-line v-if="circle" />
		</div>
		<span class="checkbox--label" v-if="$slots.default || statistics || title">
			<template v-if="$slots.default">
				<slot />
			</template>
			<template v-else>
				{{ title }}
			</template>
			<span class="cursor-pointer inline-block -my-4" v-if="statistics">
				<svws-ui-tooltip position="right">
					<i-ri-bar-chart-2-line class="pointer-events-auto ml-1 pt-1 relative top-0.5" />
					<template #content>
						Relevant für die Statistik
					</template>
				</svws-ui-tooltip>
			</span>
		</span>
	</label>
</template>

<style lang="postcss">
.checkbox {
	@apply cursor-pointer;
	@apply inline-flex;
	@apply items-start justify-start;
	@apply select-none;
	@apply text-base font-normal leading-none;
	@apply my-1;

	.icon svg {
		@apply -my-0.5;
		width: 1.4em;
		height: 1.4em;
	}

	&:hover,
	&:focus {
		.icon {
			@apply text-svws/75;
		}
	}

	&.checkbox--bw {
		&:hover,
		&:focus {
			.icon {
				@apply text-black dark:text-white;
			}
		}
	}

	.input-wrapper .text-input-component + &,
	.input-wrapper .wrapper + & {
		@apply items-center;
	}
}

.checkbox:not(.checkbox--checked):not(.checkbox--indeterminate) {
	.icon {
		@apply opacity-25;
	}

	&:not(.checkbox--disabled) {
		&:hover,
		&:focus-visible {
			.icon {
				@apply opacity-100;
				@apply text-svws;
			}
		}
	}

	&.checkbox--statistics:hover,
	&.checkbox--statistics:focus-visible {
		.icon {
			@apply text-violet-500;
		}
	}

	&.checkbox--bw {
		&:hover,
		&:focus {
			.icon {
				@apply text-black dark:text-white;
			}
		}
	}
}

.checkbox--control {
	@apply w-0 h-0 absolute opacity-0;

	&:focus-visible ~ .icon {
		@apply text-svws/75;

		.checkbox:not(.checkbox--checked):not(.checkbox--indeterminate) & {
			@apply opacity-100 !important;
			@apply text-svws;
		}

		svg {
			@apply rounded-sm ring-1 ring-svws ring-offset-0;
		}
	}
}

.checkbox--checked,
.checkbox--indeterminate {
	@apply font-medium;

	&:not(.checkbox--bw) .icon {
		@apply text-svws;
	}

	&.checkbox--statistics .icon {
		@apply text-violet-500;
	}
}


.checkbox--disabled {
	@apply opacity-50;
	@apply cursor-not-allowed;

	&:not(.checkbox--bw) .icon {
		@apply text-black dark:text-white;
	}
}

.checkbox--label {
	margin: 0.1rem 0 0 0.2rem;
}

.checkbox--disabled .checkbox--label {
	@apply text-black/50 dark:text-white/50;
}

.checkbox--statistics {
	@apply text-violet-500;

	.tooltip-trigger--triggered svg {
		@apply text-violet-800;
	}
}

.checkbox--statistics .checkbox--label .icon {
	@apply opacity-100 inline-block;
	width: 0.8em;
	height: 0.8em;

	svg {
		@apply w-full h-full;
	}
}

.checkbox--wrapper {
	margin-left: -1rem;
}

.checkbox--wrapper .checkbox {
	margin-left: 1rem;
}

.checkbox--headless,
.data-table .checkbox {
	margin: 0;
}
</style>
