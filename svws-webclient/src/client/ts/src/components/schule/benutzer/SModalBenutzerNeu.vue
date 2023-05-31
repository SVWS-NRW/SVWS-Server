<template>
	<svws-ui-modal ref="modalNeuerBenutzer" size="small">
		<template #modalTitle>
			Benutzer hinzufügen
		</template>

		<template #modalContent>
			<svws-ui-input-wrapper>
				<svws-ui-text-input v-model="anzeigename" type="text" placeholder="Anzeigename" />
				<svws-ui-text-input v-model="name" type="text" placeholder="Name" />
				<svws-ui-spacing />
				<svws-ui-text-input v-model="passwort1" type="password" placeholder="Passwort" />
				<svws-ui-text-input v-model="passwort2" type="password" placeholder="Passwort wiederholen" />
			</svws-ui-input-wrapper>
		</template>

		<template #modalActions>
			<svws-ui-button type="secondary" @click="modalNeuerBenutzer.closeModal()"> Abbrechen </svws-ui-button>
			<svws-ui-button @click="create"> Weiter </svws-ui-button>
		</template>
	</svws-ui-modal>

	<button class="button button--icon" @click="modalNeuerBenutzer.openModal()">
		<svws-ui-icon><i-ri-add-line /></svws-ui-icon>
	</button>

	<button v-if="showDeleteIcon" class="button button--icon" @click="deleteBenutzerAllgemein()">
		<svws-ui-icon><i-ri-delete-bin2-line /></svws-ui-icon>
	</button>

	<button class="button button--icon">
		<svws-ui-icon><i-ri-file-copy-line /></svws-ui-icon>
	</button>

	<button class="button button--icon">
		<svws-ui-icon><i-ri-more-2-line /></svws-ui-icon>
	</button>
</template>

<script setup lang="ts">

	import { ref } from "vue";

	const modalNeuerBenutzer = ref();

	const props = withDefaults(defineProps<{
		showDeleteIcon?: boolean;
		createBenutzerAllgemein: (anmeldename: string, benutzername: string, passwort: string) => Promise<void>;
		deleteBenutzerAllgemein: () => Promise<void>;
	}>(), {
		showDeleteIcon: false,
	});

	const anzeigename = ref();
	const name = ref();
	const passwort1 = ref();
	const passwort2 = ref();

	async function create() {
		if (passwort1.value === passwort2.value){
			await props.createBenutzerAllgemein(name.value, anzeigename.value, passwort1.value);
			modalNeuerBenutzer.value.closeModal();
			anzeigename.value = "";
			name.value = "";
			passwort1.value = "";
			passwort2.value = "";
		} else {
			alert("Passwörter stimmen nicht überein")
		}
	}

</script>
