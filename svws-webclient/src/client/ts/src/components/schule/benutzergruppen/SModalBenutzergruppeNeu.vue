<template>
	<svws-ui-modal ref="modalNeueBenutzergruppe" size="small">
		<template #modalTitle>
			Benutzergruppe hinzufügen
		</template>

		<template #modalContent>
			<svws-ui-input-wrapper center>
				<svws-ui-text-input v-model="bezeichnung" type="text" placeholder="Bezeichnung" />
				<svws-ui-toggle v-model="inputbgIstAdmin"> Admin-Rechte </svws-ui-toggle>
			</svws-ui-input-wrapper>
		</template>

		<template #modalActions>
			<svws-ui-button type="secondary" @click="modalNeueBenutzergruppe.closeModal()"> Abbrechen </svws-ui-button>
			<svws-ui-button @click="create"> Weiter </svws-ui-button>
		</template>
	</svws-ui-modal>

	<svws-ui-button type="trash" v-if="showDeleteIcon" @click="deleteBenutzergruppe_n" />

	<svws-ui-button type="icon" @click="modalNeueBenutzergruppe.openModal()">
		<i-ri-add-line />
	</svws-ui-button>

	<svws-ui-button type="icon" disabled>
		<i-ri-file-copy-line />
	</svws-ui-button>

	<svws-ui-button type="icon" disabled>
		<i-ri-more-2-line />
	</svws-ui-button>
</template>

<script setup lang="ts">

	import { ref } from "vue";

	const modalNeueBenutzergruppe = ref();
	const bezeichnung = ref();
	const inputbgIstAdmin=ref(false);

	const props = defineProps<{
		showDeleteIcon: boolean;
		createBenutzergruppe : (bezeichnung: string, istAdmin: boolean) => Promise<void>;
		// eslint-disable-next-line vue/prop-name-casing
		deleteBenutzergruppe_n : () => Promise<void>;
	}>();

	function create(){
		void props.createBenutzergruppe(bezeichnung.value,inputbgIstAdmin.value);
		modalNeueBenutzergruppe.value.closeModal();
		bezeichnung.value="";
		inputbgIstAdmin.value=false;
	}

</script>
