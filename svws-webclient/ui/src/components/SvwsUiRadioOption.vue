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
				<i-ri-eye-line class="radio--indicator-icon--checked" />
			</template>
			<template v-else>
				<slot />
				<i-ri-checkbox-blank-circle-line v-if="!$slots.default" class="radio--indicator-icon--blank" />
				<i-ri-checkbox-circle-line v-if="!$slots.default" class="radio--indicator-icon--checked" />
			</template>
		</span>
		<span class="radio--label--text"> {{ label }} <i-ri-bar-chart-2-line v-if="statistics" class="ml-2" /> </span>
	</label>
</template>

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
	@apply absolute inset-0 opacity-25 pointer-events-none left-2 top-1.5 w-5 h-5;
}

.radio--label:not(.radio--label--checked) .radio--indicator-icon .radio--indicator-icon--checked {
	@apply hidden;
}

.radio--indicator:checked ~ .radio--indicator-icon,
.radio--label--checked .radio--indicator-icon {
	@apply opacity-100 text-svws;

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
