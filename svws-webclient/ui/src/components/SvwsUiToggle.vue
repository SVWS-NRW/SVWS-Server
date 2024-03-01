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
			<i-ri-bar-chart-2-line v-if="statistics" class="ml-2" />
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
		disabled: false
	});

	const emit = defineEmits<{
		(e: 'update:modelValue', value: boolean): void;
	}>();

	const value = computed({
		get: () => props.modelValue,
		set: (value: boolean) => emit('update:modelValue', value)
	});
</script>

<style>
.toggle {
	@apply cursor-pointer;
	@apply inline-flex gap-2 items-center;
	@apply select-none;
	@apply text-base;

	.data-table__filter-simple & {
		@apply my-auto;
	}

	&--disabled {
		@apply opacity-50;
		@apply cursor-not-allowed pointer-events-none;
	}
}

.toggle--control {
	@apply w-0 h-0 absolute opacity-0;

	&:focus-visible ~ .toggle--indicator {
		@apply ring-svws ring-2 ring-offset-1;

		&:before {
			@apply bg-svws;
		}
	}
}

.toggle--indicator {
	@apply bg-white dark:bg-black;
	@apply rounded-full ring-1 ring-black dark:ring-white;
	@apply relative;
	@apply h-4 w-8;

	&:before {
		@apply absolute;
		@apply bg-black dark:bg-white border border-white dark:border-black;
		@apply block;
		@apply rounded-full;
		@apply h-4 w-4;
		@apply left-0 top-0;
		content: "";
	}
}

.toggle--statistics {
	@apply text-violet-500;

	.tooltip-trigger--triggered svg {
		@apply text-violet-800;
	}
}

.toggle--statistics .toggle--indicator {
	@apply border-violet-500;
}

.toggle--statistics .toggle--indicator:before {
	@apply bg-violet-500;
}

.toggle input:checked+.toggle--indicator {
	@apply bg-svws dark:bg-svws ring-svws;

	&:before {
		@apply bg-white translate-x-full border-svws;
	}
}

.toggle.toggle--statistics input:checked+.toggle--indicator {
	@apply bg-violet-500;
	@apply border-violet-500;
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
		@apply text-primary bg-transparent;

		&:before {
			@apply bg-transparent left-auto right-auto;
			content: "✔";
		}
	}
}

.toggle.toggle--disabled input:checked+.toggle--indicator {
	@apply bg-black ring-black border-black text-black dark:bg-white dark:ring-white dark:border-white dark:text-white;

	&:before {
		@apply border-black;
	}
}
</style>
