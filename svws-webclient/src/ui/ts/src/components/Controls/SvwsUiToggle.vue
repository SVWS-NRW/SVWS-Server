<script setup lang='ts'>
	import {computed, ref} from 'vue';

	const props = withDefaults(defineProps<{
		modelValue?: boolean;
		statistics?: boolean;
		headless?: boolean;
	}>(), {
		modelValue: false,
		statistics: false,
		headless: false,
	});

	const emit = defineEmits<{
		(e: 'update:modelValue', value: boolean): void;
	}>();

	const value = computed({
		get() {
			return props.modelValue;
		},
		set(value: boolean) {
			emit('update:modelValue', value);
		}
	});
</script>

<template>
	<label class="toggle" :class="{
		'toggle--statistics': statistics,
		'toggle--headless': headless
	}">
		<input v-model="value" class="toggle--control" type="checkbox">
		<span class="toggle--indicator" />
		<span v-if="$slots.default || statistics" class="toggle--label">
			<slot />
			<i-ri-bar-chart-2-line v-if="statistics" class="ml-2" />
		</span>
	</label>
</template>

<style>
.toggle {
	@apply cursor-pointer;
	@apply inline-flex gap-2 items-center;
	@apply select-none;
	@apply text-base;

	.data-table__filter-simple & {
		@apply my-auto;
	}

	.data-table__filter__fields & {

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
	@apply bg-white;
	@apply rounded-full ring-1 ring-black;
	@apply relative;
	@apply h-4 w-8;

	&:before {
		@apply absolute;
		@apply bg-black border border-white;
		@apply block;
		@apply rounded-full;
		@apply h-4 w-4;
		@apply left-0 top-0;
		content: "";

		.toggle:hover & {
			@apply translate-x-[10%];
		}
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
	@apply bg-svws ring-svws;

	&:before {
		@apply bg-white translate-x-full border-svws;
	}
}

.toggle:hover input:checked+.toggle--indicator {
	&:before {
		@apply translate-x-[90%];
	}
}

.toggle.toggle--statistics input:checked+.toggle--indicator {
	@apply bg-violet-500;
	@apply border-violet-500;
}

.toggle--label {
	@apply flex items-center gap-1 font-medium;
}

.toggle--headless {
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
</style>
