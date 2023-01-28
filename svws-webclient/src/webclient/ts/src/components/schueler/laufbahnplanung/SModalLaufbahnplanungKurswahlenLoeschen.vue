<template>
	<div>
		<svws-ui-button @click="toggle_modal">Reset</svws-ui-button>
		<svws-ui-modal ref="modal" size="small">
			<template #modalTitle>Alle Kurswahlen löschen</template>
			<template #modalDescription>
				<div class="flex gap-1 mb-2">
					Sollen die Fachwahlen der noch in Planung befindlichen Halbjahre gelöscht werden?
				</div>
				<div class="flex gap-1">
					<svws-ui-button @click="toggle_modal">Abbrechen</svws-ui-button>
					<svws-ui-button @click="reset_fachwahlen">Ja</svws-ui-button>
				</div>
			</template>
		</svws-ui-modal>
	</div>
</template>

<script setup lang="ts">

	import { Ref, ref } from 'vue';

	const modal: Ref<any> = ref(null);

	const emit = defineEmits<{
		(e: 'delete'): void;
	}>()

	function toggle_modal() {
		modal.value.isOpen ? modal.value.closeModal() : modal.value.openModal();
	}

	function reset_fachwahlen() {
		modal.value.closeModal();
		emit('delete');
	}

</script>