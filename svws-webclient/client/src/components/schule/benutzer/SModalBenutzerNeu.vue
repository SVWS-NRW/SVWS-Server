<template>
	<svws-ui-modal :show="showModal" size="small">
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
			<svws-ui-button type="secondary" @click="showModal().value = false"> Abbrechen </svws-ui-button>
			<svws-ui-button @click="create"> Weiter </svws-ui-button>
		</template>
	</svws-ui-modal>

	<svws-ui-button type="trash" v-if="showDeleteIcon" @click="deleteBenutzerAllgemein()" />

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

	const props = withDefaults(defineProps<{
		showDeleteIcon?: boolean;
		createBenutzerAllgemein: (anmeldename: string, benutzername: string, passwort: string) => Promise<void>;
		deleteBenutzerAllgemein: () => Promise<void>;
	}>(), {
		showDeleteIcon: false,
	});

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const anzeigename = ref();
	const name = ref();
	const passwort1 = ref();
	const passwort2 = ref();

	async function create() {
		if (passwort1.value === passwort2.value){
			await props.createBenutzerAllgemein(name.value, anzeigename.value, passwort1.value);
			showModal().value = false;
			anzeigename.value = "";
			name.value = "";
			passwort1.value = "";
			passwort2.value = "";
		} else {
			alert("Passwörter stimmen nicht überein")
		}
	}

</script>
