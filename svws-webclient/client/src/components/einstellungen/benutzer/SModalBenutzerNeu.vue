<template>
	<svws-ui-modal v-model:show="show" size="small">
		<template #modalTitle>
			Benutzer hinzufügen
		</template>

		<template #modalContent>
			<svws-ui-input-wrapper>
				<svws-ui-text-input v-model="modelProxy.proxy.anzeigename" placeholder="Anzeigename (z.B. Tim Taler)" />
				<svws-ui-spacing />
				<svws-ui-text-input v-model.trim="modelProxy.proxy.name" placeholder="Anmeldename (z.B. tim)" :validation="() => modelProxy.getFehler('name')" skip-default-validation />
				<svws-ui-text-input v-model.trim="modelProxy.proxy.passwort1" type="password" placeholder="Passwort" />
				<svws-ui-text-input v-model.trim="modelProxy.proxy.passwort2" type="password" placeholder="Passwort wiederholen" :validation="() => modelProxy.getFehler('passwort2')" skip-default-validation />
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

	import { computed, ref, shallowRef, watch } from "vue";
	import type { BenutzerListeEintrag } from "@core";
	import { BenutzerModelProxy } from "./BenutzerModelProxy";

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

	const dataNotPatched = shallowRef({ anzeigename: "", name: "", passwort1: "", passwort2: "" });

	const modelProxy = shallowRef(new BenutzerModelProxy(() => dataNotPatched.value, () => props.mapBenutzer.values()));
	watch(() => props.mapBenutzer, () => modelProxy.value = new BenutzerModelProxy(() => dataNotPatched.value, () => props.mapBenutzer.values()), { immediate: false });

	const disabled = computed(() => (modelProxy.value.proxy.passwort1 !== modelProxy.value.proxy.passwort2) || (modelProxy.value.proxy.passwort1.length === 0) || modelProxy.value.hatFehler());

	async function create() {
		await props.createBenutzerAllgemein(modelProxy.value.proxy.anzeigename, modelProxy.value.proxy.name, modelProxy.value.proxy.passwort1);
		show.value = false;
		dataNotPatched.value = { anzeigename: "", name: "", passwort1: "", passwort2: "" };
	}

</script>
