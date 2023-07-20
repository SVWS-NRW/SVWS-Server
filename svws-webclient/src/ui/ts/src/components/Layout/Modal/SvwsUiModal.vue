<script setup lang='ts'>
//TODO: props weiterleiten über :open. Das mit den refs ist Blödsinn
	import { Dialog, DialogTitle, DialogDescription, TransitionRoot, TransitionChild } from "@headlessui/vue";
	import type { Size } from "../../../types";
	import {ref} from "vue";

	const props = withDefaults(defineProps<{
		size?: Extract<Size, 'small' | 'medium' | 'big'> | 'help';
		type?: 'default' | 'danger';
	}>(), {
		size: 'small',
		type: 'default'
	});

	const isOpen = ref(false);

	function closeModal() {
		isOpen.value = false;
	}

	function openModal() {
		isOpen.value = true;
	}

	defineExpose({
		openModal,
		closeModal,
		isOpen
	});
</script>

<template>
	<TransitionRoot appear :show="isOpen">
		<Dialog class="modal--wrapper" @close="closeModal" @keyup.esc="closeModal">
			<div class="modal--pageWrapper"
				:class="{
					'modal--pageWrapper--help': size === 'help',
				}">
				<TransitionChild as="div"
					enter="ease-out duration-200"
					enter-from="opacity-0"
					enter-to="opacity-100"
					leave="ease-in duration-100"
					leave-from="opacity-100"
					leave-to="opacity-0"
					v-if="size !== 'help'">
					<div class="modal--overlay" @click="closeModal" />
				</TransitionChild>
				<TransitionChild as="div"
					enter="ease-out duration-200"
					enter-from="opacity-0 scale-95"
					enter-to="opacity-100 scale-100"
					leave="ease-in duration-100"
					leave-from="opacity-100 scale-100"
					leave-to="opacity-0 scale-95"
					class="modal"
					:class="{
						'modal--sm': size === 'small',
						'modal--md': size === 'medium',
						'modal--lg': size === 'big',
						'modal--help': size === 'help',
						'modal--danger': type === 'danger',
					}">
					<div class="modal--titlebar">
						<DialogTitle class="modal--title inline-flex items-center gap-1">
							<i-ri-alert-fill v-if="type === 'danger'" class="text-error" />
							<slot name="modalTitle" />
						</DialogTitle>
						<svws-ui-button type="icon" @click="closeModal">
							<span class="icon modal--closeIcon">
								<i-ri-close-line />
							</span>
						</svws-ui-button>
					</div>
					<div class="modal--content-wrapper">
						<DialogDescription v-if="$slots.modalDescription" class="modal--description">
							<slot name="modalDescription" />
						</DialogDescription>

						<div v-if="$slots.modalContent" class="modal--content">
							<slot name="modalContent" />
						</div>

						<div class="modal--actions">
							<slot name="modalActions" />
						</div>
					</div>
				</TransitionChild>
			</div>
		</Dialog>
	</TransitionRoot>
</template>

<style lang="postcss">
.modal--pageWrapper {
	@apply flex items-center justify-center;
	@apply h-screen;
	@apply p-6;
	@apply pointer-events-auto;
}

.modal--pageWrapper--help {
	@apply justify-end items-start;
	@apply pointer-events-none;
}

.modal--titlebar {
	@apply flex flex-row items-center justify-between;
	@apply w-full;
}

.modal {
	@apply align-bottom sm:align-middle my-8 transform transition-all overflow-hidden;
	@apply bg-white dark:bg-black max-h-full max-w-modal-sm border border-black/20 dark:border-white/5;
	@apply flex flex-col items-center;
	@apply w-full mx-auto;
	@apply relative z-50;
	@apply rounded-lg;
	@apply shadow-xl shadow-black/10 dark:shadow-white/5;
}

.modal--sm {
	@apply w-full max-w-modal-sm;
}

.modal--md {
	@apply w-full max-w-modal-md;
	@apply rounded-xl;
}

.modal--lg {
	@apply w-full max-w-modal-lg;
	@apply rounded-xl;
}

.modal--help {
	@apply m-0 h-full;
	@apply shadow-xl;
	@apply border border-black/25 dark:border-white/25;
	@apply pointer-events-auto rounded-lg;

	.modal--description,
	.modal--content {
		@apply text-left text-base;
	}
}

.modal--titlebar {
	@apply p-2 border-b-2 border-light dark:border-white/10;

	.button {
		@apply rounded-md;

		&:focus {
			@apply outline-none ring-0 border-0;
		}
	}
}

.modal--title {
	@apply flex-grow px-2;
	@apply text-headline-sm;
}

.modal--content-wrapper {
	@apply h-full overflow-y-auto w-full;
	-webkit-overflow-scrolling: touch;
}

.modal--description {
	@apply px-4 py-5 text-headline-sm text-center;
}

.modal--content {
	@apply w-full text-base px-4 py-5 text-center;
}

.modal--wrapper {
	@apply fixed inset-0 z-50 pointer-events-none;
	@apply overflow-y-auto;
	-webkit-overflow-scrolling: touch;
}

.modal--actions {
	@apply flex items-center justify-center p-4 pb-6 gap-2;
}

.modal--description + .modal--content,
.modal--description + .modal--actions,
.modal--content + .modal--actions{
	@apply pt-0;
}

.modal--overlay {
	@apply bg-light/75 dark:bg-[#000]/75 backdrop-filter backdrop-grayscale;
	@apply absolute top-0 left-0;
	@apply h-full w-full;
	@apply z-50;
}
</style>
