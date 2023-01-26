<template>
	<TransitionRoot :show="isOpen">
		<Dialog class="notification--wrapper" :initial-focus="null">
			<TransitionChild as="div"
							 enter="ease-out duration-200"
							 enter-from="opacity-0 scale-95"
							 enter-to="opacity-100 scale-100"
							 leave="ease-in duration-100"
							 leave-from="opacity-100 scale-100"
							 leave-to="opacity-0 scale-95"
							 class="notification inline-flex my-5 transform transition-all overflow-hidden"
							 :class="{
								'notification--primary': type === 'primary',
								'notification--success': type === 'success',
								'notification--error': type === 'error',
								'notification--highlight': type === 'highlight',
							 }"
			>
				<div class="notification--content-wrapper flex justify-between items-start">
					<div class="notification--content py-2.5 px-4">
						<slot/>
					</div>
					<div class="flex-shrink-0 p-1">
						<Button type="icon" @click="isOpen = false">
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

	&--error {
		@apply bg-error text-white;
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

	&--highlight {
		@apply bg-highlight text-black;

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
	@apply flex-grow;
}

</style>
