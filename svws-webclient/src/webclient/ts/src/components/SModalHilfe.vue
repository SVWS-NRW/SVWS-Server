<template>
	<div>
		<svws-ui-badge class="cursor-pointer print:hidden" size="tiny" type="primary" @click="modal.openModal()"> Hilfe </svws-ui-badge>
		<svws-ui-modal ref="modal">
			<template #modalTitle>Hilfe</template>
			<template #modalDescription>
				<slot> Komponente für den Hilfe-Text fehlt... </slot>
			</template>
			<template #modalActions>
				<svws-ui-button type="secondary" @click="modal.closeModal">
					Schließen
				</svws-ui-button>
			</template>
		</svws-ui-modal>
	</div>
</template>

<script setup lang="ts">

	import { ref } from 'vue';

	const modal = ref();

</script>
