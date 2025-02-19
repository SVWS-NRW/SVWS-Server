<template>
	<Teleport to="body">
		<div class="fixed top-0 right-0 z-50 w-136 h-full p-6 gap-1 flex flex-col justify-start pointer-events-none overflow-y-auto" style="scrollbar-width: none">
			<slot />
		</div>
	</Teleport>
</template>

<script setup lang='ts'>

	import {ref} from "vue";

	const isOpen = ref(false);

	function closeModal() {
		isOpen.value = false;
	}

	function openModal() {
		isOpen.value = true;
	}

	function toggleModal() {
		isOpen.value = !isOpen.value;
	}

	defineExpose({
		openModal,
		closeModal,
		toggleModal,
		isOpen,
	});

</script>
