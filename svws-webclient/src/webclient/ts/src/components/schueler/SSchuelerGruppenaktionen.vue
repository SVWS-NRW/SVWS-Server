<template>
	<div class="flex h-full flex-row">
		<div class="flex w-full flex-col">
			<svws-ui-header>
				<span>Gruppenaktionen für Schüler</span>
				<svws-ui-badge variant="highlight"
					>{{ items.length }} Einträge</svws-ui-badge
				>
			</svws-ui-header>
			<svws-ui-button type="danger" @click="modal.openModal()">
				Alle {{ items.length }} Schüler löschen
			</svws-ui-button>
			<svws-ui-modal ref="modal">
				<template #modalTitle>Schüler löschen</template>
				<template #modalDescription>
					Sollen diese {{ items.length }} Schüler gelöscht werden?
				</template>

				<template #modalActions>
					<svws-ui-button type="secondary" @click="modal.closeModal">
						Abbrechen
					</svws-ui-button>
					<svws-ui-button type="danger" @click="deleteEntries()">
						Löschen
					</svws-ui-button>
				</template>
			</svws-ui-modal>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { ref, Ref } from "vue";
	// import { SvwsUiModal } from "@svws-nrw/svws-ui";

	const props = defineProps({
		items: {
			type: Array,
			default() {
				return [];
			}
		}
	});

	const emits = defineEmits([
		"update:hidden",
		"update:selected",
		"update:items"
	]);

	// FIXME: const modal: Ref<SvwsUiModal> = ref(null);
	const modal: Ref<any> = ref(null);
	function deleteEntries() {
		console.log({
			message: "delete entries",
			items: props.items,
			count: props.items.length
		});
		modal.value.closeModal();
	}
</script>
