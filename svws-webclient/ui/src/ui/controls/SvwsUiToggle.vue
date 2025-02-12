<template>
	<label class="toggle" :class="{
		'toggle--statistics': statistics,
		'toggle--headless': headless,
		'toggle--disabled': disabled
	}">
		<input v-model="value" class="toggle--control" type="checkbox">
		<span class="toggle--indicator" />
		<span v-if="$slots.default || statistics" class="toggle--label">
			<slot />
			<span class="icon i-ri-bar-chart-2-line icon-ui-statistic ml-2" v-if="statistics" />
		</span>
	</label>
</template>

<script setup lang='ts'>
	import { computed } from 'vue';

	const props = withDefaults(defineProps<{
		modelValue?: boolean;
		statistics?: boolean;
		headless?: boolean;
		disabled?: boolean;
	}>(), {
		modelValue: false,
		statistics: false,
		headless: false,
		disabled: false,
	});

	const emit = defineEmits<{
		(e: 'update:modelValue', value: boolean): void;
	}>();

	const value = computed({
		get: () => props.modelValue,
		set: (value: boolean) => emit('update:modelValue', value),
	});
</script>

<style lang="postcss">

@reference "../../assets/styles/index.css";

.toggle {
	@apply cursor-pointer;
	@apply inline-flex gap-2 items-center;
	@apply select-none;
	@apply text-base;

	.data-table__filter-simple & {
		@apply my-auto;
	}
}

.toggle--disabled {
	@apply opacity-50;
	@apply cursor-not-allowed pointer-events-none;
}

.toggle--control {
	@apply w-0 h-0 absolute opacity-0;

	&:focus-visible ~ .toggle--indicator {
		@apply ring-ui-brand ring-2 ring-offset-1;

		&:before {
			@apply bg-ui-brand;
		}
	}
}

.toggle--indicator {
	@apply bg-ui-contrast-0;
	@apply rounded-full ring-1 ring-ui-contrast-100;
	@apply relative;
	@apply h-4 w-8;

	&:before {
		@apply absolute;
		@apply bg-ui-contrast-100 border border-ui-contrast-0;
		@apply block;
		@apply rounded-full;
		@apply h-4 w-4;
		@apply left-0 top-0;
		content: "";
	}
}

.toggle--statistics {
	@apply text-ui-statistic;

	.tooltip-trigger--triggered svg {
		@apply text-ui-statistic-hover;  /**TODO: passt die Farbe? Ist nicht ganz dieselbe */
	}
}

.toggle--statistics .toggle--indicator {
	@apply border-ui-statistic;
}

.toggle--statistics .toggle--indicator:before {
	@apply bg-ui-statistic;
}

.toggle input:checked+.toggle--indicator {
	@apply bg-ui-brand ring-ui-brand;

	&:before {
		@apply bg-ui-contrast-0 translate-x-full border-ui-brand;
	}
}

.toggle.toggle--statistics input:checked+.toggle--indicator {
	@apply bg-ui-statistic;
	@apply border-ui-statistic;
}

.toggle--label {
	@apply flex items-center gap-1 font-medium;
}

.toggle--headless,
.data-table .toggle {
	@apply h-full w-full;

	.toggle--indicator {
		@apply w-full rounded-none border-0;

		&:before {
			@apply relative bg-transparent w-auto h-auto text-center font-bold;
			content: "x";
		}
	}

	input:checked+.toggle--indicator {
		@apply text-ui-brand bg-transparent;

		&:before {
			@apply bg-transparent left-auto right-auto;
			content: "✔";
		}
	}
}

.toggle.toggle--disabled input:checked+.toggle--indicator {
	@apply bg-ui-contrast-100 ring-ui-contrast-100 border-ui-contrast-100 text-ui-contrast-100;

	&:before {
		@apply border-ui-contrast-100;
	}
}
</style>
