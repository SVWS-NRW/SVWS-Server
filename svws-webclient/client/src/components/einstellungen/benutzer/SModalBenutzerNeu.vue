<template>
	<svws-ui-modal v-model:show="show" size="small">
		<template #modalTitle>
			Benutzer hinzufügen
		</template>

		<template #modalContent>
			<svws-ui-input-wrapper>
				<svws-ui-text-input v-model="anzeigename" placeholder="Anzeigename (z.B. Tim Taler)" />
				<svws-ui-spacing />
				<svws-ui-text-input v-model.trim="name" placeholder="Anmeldename (z.B. tim)" :valid />
				<svws-ui-text-input v-model.trim="passwort1" type="password" placeholder="Passwort" />
				<svws-ui-text-input v-model.trim="passwort2" type="password" placeholder="Passwort wiederholen" :valid="validPassword2" />
			</svws-ui-input-wrapper>
		</template>

		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button @click="create" :disabled> Weiter </svws-ui-button>
		</template>
	</svws-ui-modal>

	<svws-ui-button type="trash" v-if="showDeleteIcon" @click="deleteBenutzerAllgemein" />

	<svws-ui-button type="icon" @click="show = true" :has-focus>
		<span class="icon i-ri-add-line" />
	</svws-ui-button>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { BenutzerListeEintrag } from "@core";

	const props = withDefaults(defineProps<{
		showDeleteIcon?: boolean;
		createBenutzerAllgemein: (anmeldename: string, benutzername: string, passwort: string) => Promise<void>;
		deleteBenutzerAllgemein: () => Promise<void>;
		hasFocus?: boolean;
		mapBenutzer: Map<number, BenutzerListeEintrag>;
	}>(), {
		showDeleteIcon: false,
		hasFocus: false,
	});

	const show = ref<boolean>(false);
	const anzeigename = ref("");
	const name = ref("");
	const passwort1 = ref("");
	const passwort2 = ref("");

	function valid(value: string | null) {
		for (const b of props.mapBenutzer.values())
			if ((value === null) || (b.name.toLocaleLowerCase('de') === value.toLocaleLowerCase('de')) || (/\s/.exec(value) !== null))
				return false
		return true;
	}

	function validPassword2() {
		return passwort1.value === passwort2.value;
	}

	const disabled = computed(() => (passwort1.value !== passwort2.value) || (passwort1.value.length === 0) || !valid(name.value));

	async function create() {
		await props.createBenutzerAllgemein(anzeigename.value, name.value, passwort1.value);
		show.value = false;
		anzeigename.value = "";
		name.value = "";
		passwort1.value = "";
		passwort2.value = "";
	}

</script>
