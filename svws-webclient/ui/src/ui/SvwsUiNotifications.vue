<template>
	<Teleport to="body">
		<div class="notifications--wrapper">
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
