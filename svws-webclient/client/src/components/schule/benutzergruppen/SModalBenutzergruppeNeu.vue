<template>
	<svws-ui-modal :show="showModal" size="small">
		<template #modalTitle>
			Benutzergruppe hinzufügen
		</template>

		<template #modalContent>
			<svws-ui-input-wrapper center>
				<svws-ui-text-input v-model="bezeichnung" type="text" placeholder="Bezeichnung" />
				<svws-ui-checkbox type="toggle" v-model="inputbgIstAdmin"> Admin-Rechte </svws-ui-checkbox>
			</svws-ui-input-wrapper>
		</template>

		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false"> Abbrechen </svws-ui-button>
			<svws-ui-button @click="create"> Weiter </svws-ui-button>
		</template>
	</svws-ui-modal>

	<svws-ui-button type="trash" v-if="showDeleteIcon" @click="deleteBenutzergruppe_n" />

	<svws-ui-button type="icon" @click="showModal().value = true">
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

	const props = defineProps<{
		showDeleteIcon: boolean;
		createBenutzergruppe : (bezeichnung: string, istAdmin: boolean) => Promise<void>;
		// eslint-disable-next-line vue/prop-name-casing
		deleteBenutzergruppe_n : () => Promise<void>;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const bezeichnung = ref();
	const inputbgIstAdmin=ref(false);

	function create(){
		void props.createBenutzergruppe(bezeichnung.value,inputbgIstAdmin.value);
		showModal().value = false;
		bezeichnung.value = "";
		inputbgIstAdmin.value=false;
	}

</script>
