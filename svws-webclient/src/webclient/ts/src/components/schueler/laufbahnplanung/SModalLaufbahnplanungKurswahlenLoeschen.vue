<template>
	<div>
		<svws-ui-button @click="toggle_modal" size="small" type="transparent" class="hover--danger">Zurücksetzen</svws-ui-button>
		<svws-ui-modal ref="modal" size="small" type="danger">
			<template #modalTitle>Alle Kurswahlen löschen</template>
			<template #modalDescription>
				<div class="flex gap-1 mb-2">
					Sollen die Fachwahlen der noch in Planung befindlichen Halbjahre gelöscht werden?
				</div>
			</template>
			<template #modalActions>
				<svws-ui-button @click="toggle_modal" type="secondary">Abbrechen</svws-ui-button>
				<svws-ui-button @click="reset_fachwahlen" type="danger">Ja <i-ri-delete-bin-line/></svws-ui-button>
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
