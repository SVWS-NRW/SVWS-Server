<template>
	<div v-if="isOpen" class="notification inline-flex transform overflow-hidden"
		:class="{
			'notification--info': type === 'info',
			'notification--success': type === 'success',
			'notification--error': type === 'error',
			'notification--warning': type === 'warning',
		}">
		<div class="notification--content-wrapper flex justify-between items-start">
			<div class="notification--content" :class="{'notification--content--has-header': $slots.header}">
				<svws-ui-icon v-if="icon || type" class="notification--icon">
					<i-ri-lock-2-line v-if="icon === 'login'" />
					<i-ri-alert-fill v-else-if="icon === 'error' || type === 'error'" />
					<i-ri-check-line v-else-if="icon === 'success' || type === 'success'" />
					<i-ri-information-line v-else-if="icon === 'info' || type === 'info'" />
					<i-ri-error-warning-line v-else-if="icon === 'warning' || type === 'warning'" />
				</svws-ui-icon>
				<div class="notification--header" v-if="$slots.header">
					<slot name="header" />
				</div>
				<div class="notification--text">
					<slot />
				</div>
				<button v-if="$slots.stack" @click="toggleStackOpen" class="mt-4 inline-flex items-center text-sm-bold opacity-50 hover:opacity-100">
					<span>Details</span>
					<i-ri-arrow-up-s-line v-if="stackOpen" />
					<i-ri-arrow-down-s-line v-else />
				</button>
				<div class="notification--stack" v-if="$slots.stack && stackOpen">
					<slot name="stack" />
				</div>
			</div>
			<div class="absolute top-0 right-0 p-1">
				<svws-ui-button type="icon" @click="isOpen = false" tabindex="-1">
					<svws-ui-icon class="notification--closeIcon">
						<i-ri-close-line />
					</svws-ui-icon>
				</svws-ui-button>
			</div>
		</div>
	</div>
</template>

<script setup lang='ts'>
	import {TransitionRoot, TransitionChild} from "@headlessui/vue";
	import {ref} from "vue";

	const {
		type = 'info',
		icon = null,
	} = defineProps<{
		type?: 'info' | 'error' | 'success' | 'warning';
		icon?: 'error' | 'login' | 'success' | 'warning' | 'info';
	}>();

	const isOpen = ref(true)
	const stackOpen = ref(false)

	function setIsOpen(value: boolean) {
		isOpen.value = value
	}

	function toggleStackOpen () {
		stackOpen.value = !stackOpen.value
	}

	defineExpose({
		setIsOpen,
		toggleStackOpen,
		isOpen,
		stackOpen,
	});
</script>

<style lang="postcss">
.notification {
	@apply max-h-full;
	@apply flex flex-col flex-shrink-0;
	@apply w-full;
	@apply relative z-50;
	@apply rounded-lg overflow-hidden;
	@apply shadow-lg shadow-black/10;
	@apply text-base pointer-events-auto;
	@apply bg-primary text-white font-bold;
	transition: transform 0.2s ease-out;

	.button, .button--icon {
		@apply rounded-md;

		&:hover,
		&:focus {
			@apply ring-0 bg-white/25;
		}
	}

	&--error {
		@apply bg-error text-white;

		.notification--icon {
			@apply animate-pulse;
		}
	}

	&--success {
		@apply bg-success text-black;

		.button, .button--icon {
			&:hover,
			&:focus {
				@apply bg-black/10;
			}
		}
	}
}

.notification--content-wrapper {
	@apply h-full overflow-y-auto w-full;
	-webkit-overflow-scrolling: touch;
}

.notification--wrapper {
	@apply fixed inset-0 z-50;
	@apply overflow-y-auto;
}

.notification--content {
	@apply flex-grow flex flex-wrap;
	@apply px-4 py-2.5 overflow-hidden;

	.notification--icon {
		@apply mr-2 text-base leading-none pt-0.5;
	}

	.notification--text {
		@apply text-base font-bold;
	};

	&--has-header {
		@apply py-3;

		.notification--icon {
			@apply text-headline-sm;
		}

		.notification--header {
			@apply w-auto text-headline-sm font-bold mb-1;
		}

		.notification--text {
			@apply w-full font-normal;
		}
	}

	.notification--stack {
		@apply whitespace-pre-wrap bg-black mt-2 -mb-1.5 -mx-2 p-2 font-mono overflow-auto min-w-full rounded-md;
	}
}

</style>
