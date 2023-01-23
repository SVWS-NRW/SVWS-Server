<script setup lang='ts'>
//TODO: props weiterleiten über :open. Das mit den refs ist Blödsinn
	import { Dialog, DialogTitle, DialogDescription, TransitionRoot, TransitionChild } from "@headlessui/vue";
	import { Size } from "~/types";
	import {ref} from "vue";

	const { size = 'small' } = defineProps<{ size?: Extract<Size, 'small' | 'medium' | 'big'>; }>();

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
		<Dialog class="modal--wrapper" @close="closeModal">
			<div class="modal--pageWrapper">
				<TransitionChild as="template"
					enter="ease-out duration-200"
					enter-from="opacity-0"
					enter-to="opacity-100"
					leave="ease-in duration-100"
					leave-from="opacity-100"
					leave-to="opacity-0">
					<Overlay @click="closeModal" />
				</TransitionChild>
				<TransitionChild as="div"
					enter="ease-out duration-200"
					enter-from="opacity-0 scale-95"
					enter-to="opacity-100 scale-100"
					leave="ease-in duration-100"
					leave-from="opacity-100 scale-100"
					leave-to="opacity-0 scale-95"
					class="modal inline-block align-bottom sm:align-middle sm:w-full sm:max-w-sm sm:my-8 transform transition-all overflow-hidden"
					:class="{
						'modal--sm': size === 'small',
						'modal--md': size === 'medium',
						'modal--lg': size === 'big'
					}">
					<div class="modal--titlebar">
						<DialogTitle class="modal--title">
							<slot name="modalTitle" />
						</DialogTitle>
						<Button type="icon" @click="closeModal">
							<Icon class="modal--closeIcon">
								<i-ri-close-line />
							</Icon>
						</Button>
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
	@apply p-8;
}

.modal--titlebar {
	@apply flex flex-row items-center justify-between;
	@apply w-full;
}

.modal {
	@apply bg-white max-h-full;
	@apply flex flex-col items-center;
	@apply mx-auto;
	@apply relative z-50;
	@apply rounded-lg overflow-hidden;
	@apply shadow-xl shadow-dark-20;
}

.modal--sm {
	@apply w-full max-w-modal-sm;
}

.modal--md {
	@apply w-full max-w-modal-md;
}

.modal--lg {
	@apply w-full max-w-modal-lg;
}

.modal--titlebar {
	@apply p-2;
}

.modal--title {
	@apply flex-grow px-2;
	@apply text-sm-bold uppercase;
}

.modal--content-wrapper {
	@apply h-full overflow-y-auto w-full;
	-webkit-overflow-scrolling: touch;
}

.modal--description {
	@apply p-4 text-base;
}

.modal--wrapper {
	@apply fixed inset-0 z-50;
	@apply overflow-y-auto;
}

.modal--content {
	@apply w-full text-base p-4 text-center;
}

.modal--actions {
	@apply flex items-center justify-center p-4;
	@apply space-x-2;
}

.modal--description + .modal--content {
	@apply pt-0;
}

.modal--description + .modal--actions,
.modal--content + .modal--actions {
	@apply pt-2;
}
</style>
