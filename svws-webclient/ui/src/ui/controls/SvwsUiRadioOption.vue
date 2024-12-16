<template>
	<label class="radio--label" :class="{
		'radio--label--disabled': disabled,
		'radio--statistics': statistics,
		'radio--label--checked-': forceChecked || (modelValue === value) || checked,
		'radio--label--no-icon': !icon,
		'radio--label--no-text': !label,
		'radio--icon-type-view': iconType === 'view'
	}">
		<input v-model="checked" type="radio" :name="name" :value="value" :disabled="disabled" class="radio--indicator">
		<span v-if="icon" class="radio--indicator-icon">
			<template v-if="iconType === 'view'">
				<span class="icon i-ri-eye-line radio--indicator-icon--checked inline-block -my-0.5" />
			</template>
			<template v-else>
				<slot>
					<span class="icon i-ri-checkbox-blank-circle-line radio--indicator-icon--blank inline-block -my-0.5" />
					<span class="icon i-ri-checkbox-circle-line radio--indicator-icon--checked inline-block -my-0.5" />
				</slot>
			</template>
		</span>
		<span class="radio--label--text"> {{ label }} <span class="icon i-ri-bar-chart-2-line icon-ui-statistic inline-block -my-0.5" v-if="statistics" /> </span>
	</label>
</template>

<script setup lang='ts'>

	import { computed } from 'vue';

	const props = withDefaults(defineProps<{
		name?: string;
		label?: string;
		value?: object | number | string | boolean;
		disabled?: boolean;
		statistics?: boolean;
		icon?: boolean;
		iconType?: string;
		modelValue?: object | number | string | boolean;
		forceChecked?: boolean;
	}>(), {
		name: '',
		label: '',
		value: '',
		disabled: false,
		statistics: false,
		icon: true,
		iconType: 'default',
		modelValue: '',
		forceChecked: false,
	});

	const emit = defineEmits<{
		(e: 'update:modelValue', value: object | number | string | boolean): void,
	}>();

	const checked = computed<object | number | boolean | string>({
		get: () => props.modelValue,
		set: (value) => emit('update:modelValue', value),
	})

</script>

<style lang="postcss">
.radio--label {
	@apply cursor-pointer relative;
	@apply select-none;
	@apply text-button;

	&:focus-within input {
		@apply rounded;
		outline-color: var(--color-ring-ui-brand);
	}

	&.radio--statistics {
		&:focus-within input {
			outline-color: var(--color-ring-ui-statistic);
		}
	}
}

.radio--label--text {
	@apply flex items-center rounded-md;
	@apply gap-x-2 py-1.5 pr-3 pl-7;
	min-height: 1.25rem;

  .radio--label--no-text & {
    padding-left: 1.5em;
  }

	.radio--icon-type-view:not(.radio--label--checked) & {
		padding-left: 0.75em;
	}

	.radio--row .radio--label--no-icon & {
		padding-left: 0.75em;
	}
}

.radio--label:hover {
	.radio--label--text {
		@apply bg-ui-hover text-ui-hover;
	}

	&.radio--statistics,
	.page--statistik & {
		.radio--label--text {
			@apply text-ui-statistic;
		}
	}

	.radio--indicator ~ .radio--indicator-icon {
		@apply opacity-100;
	}
}

.radio--indicator {
	@apply appearance-none absolute inset-0 w-full h-full pointer-events-none;
}

.radio--indicator:checked ~ .radio--label--text,
.radio--label--checked .radio--label--text {
	@apply bg-ui-selected text-ui-onselected;

	.svws-sub-nav-target & {
		@apply bg-ui;
	}
}

.radio--indicator ~ .radio--indicator-icon {
	@apply absolute inset-0 opacity-70 pointer-events-none left-1.5 top-1.5 w-5 h-5;
}

.radio--label:not(.radio--label--checked) .radio--indicator-icon .radio--indicator-icon--checked {
	@apply hidden;
}

.radio--indicator:checked ~ .radio--indicator-icon,
.radio--label--checked .radio--indicator-icon {
	@apply opacity-100 text-ui-brand;

	.icon {
		@apply icon-ui-onselected;

		.dark & {
			@apply icon-ui-onselected--dark;
		}
	}

	.radio--statistics &,
	.page--statistik & {
		@apply text-ui-statistic;
	}
}

.radio--indicator:checked ~ .radio--indicator-icon,
.radio--label--checked .radio--indicator-icon {
	.radio--indicator-icon--blank {
		@apply hidden;
	}

	.radio--indicator-icon--checked {
		@apply block;
	}
}

.radio--label--disabled {
  @apply cursor-not-allowed pointer-events-none;

  .radio--label--text {
    @apply bg-ui-disabled text-ui-disabled;
  }

  .icon {
	@apply opacity-50;
  }

  .radio--indicator:checked ~ .radio--indicator-icon,
  &.radio--label--checked .radio--indicator-icon {
	@apply text-ui-disabled;

    .radio--statistics &,
    .page--statistik & {
      	@apply text-ui-disabled;
    }
  }

  .radio--indicator:checked ~ .radio--label--text,
  &.radio--label--checked .radio--label--text {
    @apply bg-ui-disabled text-ui-disabled;

    .radio--statistics &,
    .page--statistik & {
      @apply bg-ui-disabled text-ui-disabled;
    }
  }
}
</style>
