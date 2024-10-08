<template>
	<label class="radio--label" :class="{
		'radio--label--disabled': disabled,
		'radio--statistics': statistics,
		'radio--label--checked-': forceChecked || modelValue === value || checked,
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
		<span class="radio--label--text"> {{ label }} <span class="icon i-ri-bar-chart-2-line icon-statistics ml-2 inline-block -my-0.5" v-if="statistics" /> </span>
	</label>
</template>

<script setup lang='ts'>

	import type { WritableComputedRef} from 'vue';
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

	const checked: WritableComputedRef<object | number | boolean | string> = computed({
		get: () => props.modelValue,
		set: (value) => emit('update:modelValue', value)
	})

</script>

<style lang="postcss">
.radio--label {
	@apply cursor-pointer relative;
	@apply select-none;
	@apply text-button;
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

	&:focus {
		@apply ring-svws/50;

		.radio--statistics &,
		.page--statistik & {
			@apply ring-violet-500/50;
		}
	}
}

.radio--label:hover {
	.radio--label--text {
		@apply bg-black/10 dark:bg-white/10;
	}

	.radio--indicator ~ .radio--indicator-icon {
		@apply opacity-100;
	}
}

.radio--indicator {
	@apply appearance-none absolute inset-0 w-full h-full pointer-events-none;

	&:focus {
		@apply ring-0;
	}
}

.radio--indicator:checked ~ .radio--label--text,
.radio--label--checked .radio--label--text {
	@apply bg-svws/5 dark:bg-svws/10 text-svws;

	.svws-sub-nav-target & {
		@apply bg-white dark:bg-black shadow;
	}

	.radio--statistics &,
	.page--statistik & {
		@apply bg-violet-500/5 text-violet-500;
	}
}

.radio--indicator ~ .radio--indicator-icon {
	@apply absolute inset-0 opacity-25 pointer-events-none left-1.5 top-1.5 w-5 h-5;
}

.radio--label:not(.radio--label--checked) .radio--indicator-icon .radio--indicator-icon--checked {
	@apply hidden;
}

.radio--indicator:checked ~ .radio--indicator-icon,
.radio--label--checked .radio--indicator-icon {
	@apply opacity-100 text-svws;
	span.icon {
	-webkit-filter: invert(44%) sepia(52%) saturate(1260%) hue-rotate(173deg) brightness(91%) contrast(86%);
	filter: invert(44%) sepia(52%) saturate(1260%) hue-rotate(173deg) brightness(91%) contrast(86%);
	}

	.radio--statistics &,
	.page--statistik & {
		@apply text-violet-500;
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

  .radio--label--text,
  &:hover .radio--label--text,
  &:focus .radio--label--text,
  &:focus-visible .radio--label--text {
    @apply bg-black/20 text-black dark:bg-white/20 dark:text-white;
    @apply opacity-25;
  }

  .radio--indicator:checked ~ .radio--indicator-icon,
  &.radio--label--checked .radio--indicator-icon {
    @apply opacity-25 text-black dark:text-white;
		span.icon {
			-webkit-filter: invert(23%) sepia(18%) saturate(978%) hue-rotate(158deg) brightness(96%) contrast(91%);
			filter: invert(23%) sepia(18%) saturate(978%) hue-rotate(158deg) brightness(96%) contrast(91%);
		}
		dark:span.icon {
			-webkit-filter: invert(95%) sepia(100%) saturate(14%) hue-rotate(213deg) brightness(104%) contrast(104%);
			filter: invert(95%) sepia(100%) saturate(14%) hue-rotate(213deg) brightness(104%) contrast(104%);
		}

    .radio--statistics &,
    .page--statistik & {
      @apply opacity-25 text-black dark:text-white;
    }
  }

  .radio--indicator:checked ~ .radio--label--text,
  &.radio--label--checked .radio--label--text {
    @apply bg-black/20 text-black dark:bg-white/20 dark:text-white;

    .radio--statistics &,
    .page--statistik & {
      @apply bg-black/20 text-black dark:bg-white/20 dark:text-white;
    }
  }
}
</style>
