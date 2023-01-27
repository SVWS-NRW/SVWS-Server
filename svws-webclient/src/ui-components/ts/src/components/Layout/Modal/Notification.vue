<template>
	<TransitionRoot :show="isOpen">
		<Dialog class="notification--wrapper" :initial-focus="null"
				:class="{
								'notification--primary': type === 'primary',
								'notification--success': type === 'success',
								'notification--error': type === 'error',
								'notification--highlight': type === 'highlight',
							 }"
		>
			<TransitionChild
				enter="duration-300 ease-out"
				enter-from="opacity-0"
				enter-to="opacity-100"
				leave="duration-200 ease-in"
				leave-from="opacity-100"
				leave-to="opacity-0"
			>
				<div class="fixed inset-0 notification--gradient"/>
			</TransitionChild>
			<TransitionChild as="div"
							 enter="ease-out duration-200"
							 enter-from="opacity-0 scale-95 translate-y-8"
							 enter-to="opacity-100 scale-100 translate-y-0"
							 leave="ease-in duration-100"
							 leave-from="opacity-100 scale-100"
							 leave-to="opacity-0 scale-95"
							 class="notification inline-flex mt-4 mb-10 transform transition-all overflow-hidden"
			>
				<div class="notification--content-wrapper flex justify-between items-start">
					<div class="notification--content py-2.5 px-4">
						<slot/>
					</div>
					<div class="flex-shrink-0 p-1">
						<Button type="icon" @click="isOpen = false" tabindex="-1">
							<Icon class="notification--closeIcon">
								<i-ri-close-line/>
							</Icon>
						</Button>
					</div>
				</div>
			</TransitionChild>
		</Dialog>
	</TransitionRoot>
</template>

<script setup lang='ts'>
import {Dialog, TransitionRoot, TransitionChild} from "@headlessui/vue";
import {ref} from "vue";
import {Type} from "~/types";

const {
	type = 'primary'
} = defineProps<{
	type?: Type;
}>();

const isOpen = ref(true)

function setIsOpen(value: boolean) {
	isOpen.value = value
}
</script>

<style lang="postcss">
.notification--wrapper {
	@apply fixed top-0 left-0 right-0 bottom-0;
	@apply flex flex-col items-center justify-end pointer-events-none;
}

.notification {
	@apply max-h-full;
	@apply flex flex-col;
	@apply w-full max-w-modal-sm mx-auto;
	@apply relative z-50;
	@apply rounded-lg overflow-hidden;
	@apply shadow-lg shadow-black/10;
	@apply text-base pointer-events-auto;
	@apply bg-primary text-white font-bold;

	.button, .button--icon {
		&:hover,
		&:focus {
			@apply ring-0 bg-white/25;
		}
	}

	.notification--error & {
		@apply bg-error text-white;
	}

	.notification--success & {
		@apply bg-success text-black;

		.button, .button--icon {
			&:hover,
			&:focus {
				@apply bg-black/10;
			}
		}
	}

	.notification--highlight & {
		@apply bg-highlight text-black;

		.button, .button--icon {
			&:hover,
			&:focus {
				@apply bg-black/10;
			}
		}
	}
}

.notification--gradient {
	/*box-shadow: inset 0 0 0.75rem 0.25rem rgba(var(--color-primary),.5);*/
	background-image: linear-gradient(to top, rgba(var(--color-primary), 0.1) 0, rgba(var(--color-primary), 0) 100%);
	background-size: 100% 25%;
	background-repeat: no-repeat;
	background-position: bottom;

	.notification--error & {
		background-image: linear-gradient(to top, rgba(var(--color-error), 0.1) 0, rgba(var(--color-error), 0) 100%);
	}

	.notification--success & {
		background-image: linear-gradient(to top, rgba(var(--color-success), 0.1) 0, rgba(var(--color-success), 0) 100%);
	}

	.notification--highlight & {
		background-image: linear-gradient(to top, rgba(var(--color-highlight), 0.1) 0, rgba(var(--color-highlight), 0) 100%);
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
	@apply flex-grow;
}

</style>
